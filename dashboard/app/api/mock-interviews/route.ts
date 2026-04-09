import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

const MOCK_PLATFORM = 'mock'

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url)
    const limit = parseInt(searchParams.get('limit') || '10')
    const recent = searchParams.get('recent') === 'true'
    const where = recent
      ? {
          date: {
            gte: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000),
          },
        }
      : undefined

    const mockInterviews = await prisma.mockInterview.findMany({
      take: limit,
      orderBy: { date: 'desc' },
      where,
      include: {
        problem: {
          include: {
            patterns: {
              include: {
                pattern: true
              }
            }
          }
        }
      }
    })

    // Count only completed mock interviews (with recorded time) for readiness stats.
    const completedWhere = {
      timeTakenSeconds: { not: null },
    }

    const totalMocks = await prisma.mockInterview.count({ where: completedWhere })
    const passedMocks = await prisma.mockInterview.count({
      where: { ...completedWhere, solved: true },
    })
    const avgScores = await prisma.mockInterview.aggregate({
      _avg: {
        explanationScore: true,
        codeQualityScore: true,
        overallScore: true,
        timeTakenSeconds: true,
      },
      where: completedWhere,
    })

    const passRate = totalMocks > 0 ? (passedMocks / totalMocks) * 100 : 0

    return NextResponse.json({
      mockInterviews,
      stats: {
        total: totalMocks,
        passed: passedMocks,
        passRate: Math.round(passRate * 100) / 100,
        avgDurationMinutes: Math.round(((avgScores._avg.timeTakenSeconds || 0) / 60) * 100) / 100,
        avgScores: {
          explanation: Math.round((avgScores._avg.explanationScore || 0) * 100) / 100,
          codeQuality: Math.round((avgScores._avg.codeQualityScore || 0) * 100) / 100,
          overall: Math.round((avgScores._avg.overallScore || 0) * 100) / 100,
        },
      },
    })
    
  } catch (error) {
    console.error('Error fetching mock interviews:', error)
    return NextResponse.json(
      { error: 'Failed to fetch mock interviews' },
      { status: 500 }
    )
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json()
    const {
      problemId,
      problemTitle,
      date,
      timeLimit,
      timeTakenSeconds,
      patternRecognitionSeconds,
      solved,
      explanationScore,
      codeQualityScore,
      notes,
    } = body

    let resolvedProblemId = problemId

    if (!resolvedProblemId && !problemTitle) {
      return NextResponse.json(
        { error: 'problemId or problemTitle is required' },
        { status: 400 }
      )
    }

    if (!resolvedProblemId && problemTitle) {
      const generatedProblemId = `mock-${Date.now()}`
      const createdProblem = await prisma.problem.create({
        data: {
          platform: MOCK_PLATFORM,
          problemId: generatedProblemId,
          title: problemTitle,
          difficulty: 'Medium',
          source: 'NeetCode',
          company: null,
        },
      })

      resolvedProblemId = createdProblem.id
    }

    if (!resolvedProblemId) {
      return NextResponse.json(
        { error: 'Failed to resolve problem for mock interview' },
        { status: 400 }
      )
    }

    // Calculate overall score if both scores are provided
    let overallScore = null
    if (explanationScore !== null && explanationScore !== undefined && codeQualityScore !== null && codeQualityScore !== undefined) {
      overallScore = (explanationScore + codeQualityScore) / 2
    }

    // Create mock interview record
    const mockInterview = await prisma.mockInterview.create({
      data: {
        problemId: resolvedProblemId,
        date: date ? new Date(date) : new Date(),
        timeLimit: typeof timeLimit === 'number' ? timeLimit * 60 : 2700,
        timeTakenSeconds: timeTakenSeconds || null,
        patternRecognitionSeconds: patternRecognitionSeconds || null,
        solved: Boolean(solved),
        explanationScore: explanationScore || null,
        codeQualityScore: codeQualityScore || null,
        overallScore,
        notes: notes || null,
      },
      include: {
        problem: {
          include: {
            patterns: {
              include: {
                pattern: true
              }
            }
          }
        }
      }
    })

    return NextResponse.json({
      success: true,
      mockInterview
    }, { status: 201 })
    
  } catch (error) {
    console.error('Error creating mock interview:', error)
    return NextResponse.json(
      { error: 'Failed to create mock interview' },
      { status: 500 }
    )
  }
}
