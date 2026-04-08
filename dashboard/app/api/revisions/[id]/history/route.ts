import { NextRequest, NextResponse } from "next/server";
import { prisma } from "@/lib/prisma";

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params;
    const revisionId = parseInt(id);
    if (isNaN(revisionId)) {
      return NextResponse.json(
        { error: "Invalid revision ID" },
        { status: 400 }
      );
    }

    // Get the revision with full problem and submission data
    const revision = await prisma.revision.findUnique({
      where: { id: revisionId },
      include: {
        submission: {
          include: {
            problem: {
              include: {
                patterns: {
                  include: {
                    pattern: true
                  }
                },
                company: true,
                platform: true
              }
            }
          }
        }
      }
    });

    if (!revision) {
      return NextResponse.json(
        { error: "Revision not found" },
        { status: 404 }
      );
    }

    // Get revision history (all revisions for this submission)
    const revisionHistory = await prisma.revision.findMany({
      where: {
        submissionId: revision.submissionId,
        completed: true
      },
      orderBy: {
        completedAt: 'asc'
      },
      select: {
        id: true,
        intervalLevel: true,
        completedAt: true,
        wasSuccessful: true,
        timeSpentSeconds: true,
        solvedWithoutHint: true,
        confidenceLevel: true,
        difficultyRating: true,
        notes: true
      }
    });

    // Calculate performance trends
    const performanceTrends = revisionHistory.map((rev, index) => {
      let improvement = null;
      if (index > 0 && rev.timeSpentSeconds && revisionHistory[index - 1].timeSpentSeconds) {
        const previousTime = revisionHistory[index - 1].timeSpentSeconds!;
        const currentTime = rev.timeSpentSeconds;
        const timeDiff = previousTime - currentTime;
        const improvementPercentage = (timeDiff / previousTime) * 100;
        
        improvement = {
          timeDifferenceSeconds: timeDiff,
          improvementPercentage: Math.round(improvementPercentage * 10) / 10,
          wasImprovement: timeDiff > 0
        };
      }

      return {
        ...rev,
        performance: improvement
      };
    });

    // Calculate overall statistics
    const completedRevisions = revisionHistory.filter(r => r.wasSuccessful);
    const averageTime = revisionHistory.length > 0 
      ? revisionHistory
          .filter(r => r.timeSpentSeconds)
          .reduce((sum, r) => sum + (r.timeSpentSeconds || 0), 0) / 
        revisionHistory.filter(r => r.timeSpentSeconds).length
      : null;

    const confidenceTrend = revisionHistory.length >= 2
      ? revisionHistory[revisionHistory.length - 1].confidenceLevel! - 
        revisionHistory[0].confidenceLevel!
      : null;

    const statistics = {
      totalRevisions: revisionHistory.length,
      successfulRevisions: completedRevisions.length,
      successRate: revisionHistory.length > 0 
        ? (completedRevisions.length / revisionHistory.length) * 100 
        : 0,
      averageTimeSeconds: averageTime,
      confidenceTrend,
      latestConfidence: revisionHistory.length > 0 
        ? revisionHistory[revisionHistory.length - 1].confidenceLevel 
        : null
    };

    return NextResponse.json({
      revision,
      revisionHistory: performanceTrends,
      statistics
    });

  } catch (error) {
    console.error('Error fetching revision history:', error);
    return NextResponse.json(
      { error: "Internal server error" },
      { status: 500 }
    );
  }
}