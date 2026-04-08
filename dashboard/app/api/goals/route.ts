// @ts-nocheck
import { NextRequest, NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';

// GET /api/goals - Fetch all goals with optional filtering
export async function GET(request: NextRequest) {
  try {
    const { searchParams } = request.nextUrl;
    const status = searchParams.get('status'); // 'active', 'completed', etc.
    const priority = searchParams.get('priority');
    const type = searchParams.get('type');

    const where: any = {};
    if (status) where.status = status;
    if (priority) where.priority = priority;
    if (type) where.type = type;

    const goals = await prisma.goal.findMany({
      where,
      include: {
        milestones: {
          orderBy: { dueDate: 'asc' }
        }
      },
      orderBy: [
        { priority: 'desc' },
        { deadline: 'asc' }
      ]
    });

    // Calculate progress for each goal
    const goalsWithProgress = await Promise.all(
      goals.map(async (goal) => {
        const currentValue = await calculateCurrentValue(goal);
        const progressPercentage = goal.targetValue > 0 
          ? Math.round((currentValue / goal.targetValue) * 100)
          : 0;

        const now = new Date();
        const daysRemaining = Math.ceil(
          (new Date(goal.deadline).getTime() - now.getTime()) / (1000 * 60 * 60 * 24)
        );

        const daysSinceStart = Math.ceil(
          (now.getTime() - new Date(goal.startDate).getTime()) / (1000 * 60 * 60 * 24)
        );

        const velocity = daysSinceStart > 0 ? currentValue / daysSinceStart : 0;
        const projectedDays = velocity > 0 ? Math.ceil((goal.targetValue - currentValue) / velocity) : 999;
        const projectedCompletionDate = new Date(now.getTime() + projectedDays * 24 * 60 * 60 * 1000);
        
        const isOnTrack = projectedCompletionDate <= new Date(goal.deadline);

        const nextMilestone = goal.milestones?.find(m => !m.completed);

        return {
          ...goal,
          currentValue,
          progressPercentage,
          daysRemaining,
          isOnTrack,
          velocity: Number(velocity.toFixed(2)),
          projectedCompletionDate,
          nextMilestone
        };
      })
    );

    return NextResponse.json({ goals: goalsWithProgress });
  } catch (error) {
    console.error('Error fetching goals:', error);
    return NextResponse.json(
      { error: 'Failed to fetch goals' },
      { status: 500 }
    );
  }
}

// POST /api/goals - Create a new goal
export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const {
      title,
      description,
      type,
      targetValue,
      unit,
      deadline,
      priority,
      targetPattern,
      targetCompany,
      targetDifficulty,
      milestones
    } = body;

    // Validation
    if (!title || !type || !targetValue || !deadline) {
      return NextResponse.json(
        { error: 'Missing required fields: title, type, targetValue, deadline' },
        { status: 400 }
      );
    }

    const goal = await prisma.goal.create({
      data: {
        title,
        description,
        type,
        targetValue,
        unit: unit || 'problems',
        deadline: new Date(deadline),
        priority: priority || 'medium',
        targetPattern,
        targetCompany,
        targetDifficulty,
        status: 'active',
        currentValue: 0,
        milestones: milestones ? {
          create: milestones.map((m: any) => ({
            title: m.title,
            description: m.description,
            targetValue: m.targetValue,
            dueDate: new Date(m.dueDate)
          }))
        } : undefined
      },
      include: {
        milestones: true
      }
    });

    return NextResponse.json({ goal }, { status: 201 });
  } catch (error) {
    console.error('Error creating goal:', error);
    return NextResponse.json(
      { error: 'Failed to create goal' },
      { status: 500 }
    );
  }
}

// Helper function to calculate current value based on goal type
async function calculateCurrentValue(goal: any): Promise<number> {
  const now = new Date();

  switch (goal.type) {
    case 'problemCount':
      // Count problems solved since goal start date
      const whereClause: any = {
        submittedAt: {
          gte: goal.startDate
        },
        status: 'solved'
      };

      if (goal.targetPattern) {
        whereClause.problem = {
          patterns: {
            some: {
              pattern: {
                name: goal.targetPattern
              }
            }
          }
        };
      }

      if (goal.targetCompany) {
        whereClause.problem = {
          ...whereClause.problem,
          company: goal.targetCompany
        };
      }

      if (goal.targetDifficulty) {
        whereClause.problem = {
          ...whereClause.problem,
          difficulty: goal.targetDifficulty.toLowerCase()
        };
      }

      const problemCount = await prisma.submission.count({ where: whereClause });
      return problemCount;

    case 'streakDays':
      // Calculate current streak
      const dailyProgress = await prisma.dailyProgress.findMany({
        where: {
          date: {
            gte: goal.startDate
          },
          problemsSolved: {
            gt: 0
          }
        },
        orderBy: {
          date: 'desc'
        }
      });

      let currentStreak = 0;
      let checkDate = new Date();
      checkDate.setHours(0, 0, 0, 0);

      for (const progress of dailyProgress) {
        const progressDate = new Date(progress.date);
        progressDate.setHours(0, 0, 0, 0);

        if (progressDate.getTime() === checkDate.getTime()) {
          currentStreak++;
          checkDate.setDate(checkDate.getDate() - 1);
        } else {
          break;
        }
      }

      return currentStreak;

    case 'patternMastery':
      // Count patterns with "Strong" confidence
      // This would require pattern stats calculation
      return goal.currentValue || 0;

    case 'speedImprovement':
      // Average time improvement
      return goal.currentValue || 0;

    case 'companyReady':
      // Company-specific problems solved
      if (!goal.targetCompany) return 0;

      const companyCount = await prisma.submission.count({
        where: {
          submittedAt: {
            gte: goal.startDate
          },
          status: 'solved',
          problem: {
            company: goal.targetCompany
          }
        }
      });
      return companyCount;

    default:
      return goal.currentValue || 0;
  }
}
