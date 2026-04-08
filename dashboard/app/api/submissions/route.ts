// @ts-nocheck
import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams
    const problemId = searchParams.get('problemId')
    const attemptType = searchParams.get('attemptType')
    const status = searchParams.get('status')
    const limit = searchParams.get('limit')
    
    const where: any = {}
    
    if (problemId) where.problemId = parseInt(problemId)
    if (attemptType) where.attemptType = attemptType
    if (status) where.status = status
    
    const submissions = await prisma.submission.findMany({
      where,
      include: { 
        problem: { 
          select: { 
            id: true, 
            platform: true, 
            problemId: true, 
            title: true,
            source: true,
            company: true 
          } 
        } 
      },
      orderBy: { submittedAt: 'desc' },
      ...(limit ? { take: parseInt(limit) } : {}),
    })
    
    return NextResponse.json({ submissions })
  } catch (error) {
    console.error('Error fetching submissions:', error)
    return NextResponse.json(
      { error: 'Failed to fetch submissions' },
      { status: 500 }
    )
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json()
    const { 
      problemId, 
      timeSpentSeconds, 
      status, 
      submittedAt, 
      notes,
      // NEW FIELDS
      attemptType = 'First',
      wasHintUsed = false,
      mistakeNote,
      approachNote
    } = body
    
    // Validation
    if (!problemId || !timeSpentSeconds || !status) {
      return NextResponse.json(
        { error: 'Missing required fields: problemId, timeSpentSeconds, status' },
        { status: 400 }
      )
    }

    // Validate attemptType
    if (!['First', 'Revision'].includes(attemptType)) {
      return NextResponse.json(
        { error: 'attemptType must be "First" or "Revision"' },
        { status: 400 }
      )
    }

    // Validate status
    const validStatuses = ['solved', 'failed', 'partial', 'abandoned']
    if (!validStatuses.includes(status)) {
      return NextResponse.json(
        { error: `status must be one of: ${validStatuses.join(', ')}` },
        { status: 400 }
      )
    }

    // Check if problem exists
    const problem = await prisma.problem.findUnique({
      where: { id: problemId }
    })

    if (!problem) {
      return NextResponse.json(
        { error: 'Problem not found' },
        { status: 404 }
      )
    }
    
    const lastSubmission = await prisma.submission.findFirst({
      where: { problemId },
      orderBy: { attemptNumber: 'desc' },
    })
    
    const submission = await prisma.submission.create({
      data: {
        problemId,
        attemptNumber: (lastSubmission?.attemptNumber || 0) + 1,
        timeSpentSeconds,
        status,
        submittedAt: submittedAt ? new Date(submittedAt) : new Date(),
        notes: notes || null,
        // NEW FIELDS
        attemptType,
        wasHintUsed,
        mistakeNote: mistakeNote || null,
        approachNote: approachNote || null,
      },
      include: {
        problem: {
          select: {
            id: true,
            platform: true,
            problemId: true,
            title: true,
            source: true,
            company: true,
          }
        }
      }
    })

    // Update daily progress automatically
    try {
      const submissionDate = submission.submittedAt
      const normalizedDate = new Date(submissionDate)
      normalizedDate.setUTCHours(0, 0, 0, 0)

      // Get patterns for this problem to count unique patterns worked on today
      const problemWithPatterns = await prisma.problem.findUnique({
        where: { id: problemId },
        include: {
          patterns: {
            include: { pattern: true }
          }
        }
      })

      // Get all submissions for today to calculate patterns worked
      const todaySubmissions = await prisma.submission.findMany({
        where: {
          submittedAt: {
            gte: normalizedDate,
            lt: new Date(normalizedDate.getTime() + 24 * 60 * 60 * 1000)
          }
        },
        include: {
          problem: {
            include: {
              patterns: { include: { pattern: true } }
            }
          }
        }
      })

      // Count unique patterns worked on today
      const patternsSet = new Set<string>()
      todaySubmissions.forEach(sub => {
        sub.problem.patterns.forEach(pp => {
          patternsSet.add(pp.pattern.name)
        })
      })

      // Update daily progress
      await prisma.dailyProgress.upsert({
        where: { date: normalizedDate },
        update: {
          problemsSolved: { increment: status === 'solved' ? 1 : 0 },
          totalTimeSpent: { increment: timeSpentSeconds },
          patternsWorked: patternsSet.size,
        },
        create: {
          date: normalizedDate,
          problemsSolved: status === 'solved' ? 1 : 0,
          totalTimeSpent: timeSpentSeconds,
          patternsWorked: patternsSet.size,
          mockInterviews: 0
        }
      })
    } catch (progressError) {
      console.error('Error updating daily progress:', progressError)
      // Don't fail the submission if daily progress update fails
    }

    // Auto-schedule revision for successful submissions
    try {
      if (status === 'solved') {
        // Check if revision already exists for this submission
        const existingRevision = await prisma.revision.findFirst({
          where: { submissionId: submission.id }
        })

        if (!existingRevision) {
          // Calculate first revision date (1 day after solving)
          const reviewDate = new Date()
          reviewDate.setUTCDate(reviewDate.getUTCDate() + 1)
          reviewDate.setUTCHours(0, 0, 0, 0)

          await prisma.revision.create({
            data: {
              submissionId: submission.id,
              intervalLevel: 0,
              nextReviewDate: reviewDate,
              completed: false
            }
          })
        }
      }
    } catch (revisionError) {
      console.error('Error scheduling revision:', revisionError)
      // Don't fail the submission if revision scheduling fails
    }
    
    return NextResponse.json({ submission }, { status: 201 })
  } catch (error) {
    console.error('Error creating submission:', error)
    return NextResponse.json(
      { error: 'Failed to create submission' },
      { status: 500 }
    )
  }
}

