// @ts-nocheck
import { NextRequest, NextResponse } from 'next/server'
import { PrismaClient } from '@prisma/client'

const prisma = new PrismaClient()

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url)
    const limit = parseInt(searchParams.get('limit') || '10')
    const recent = searchParams.get('recent') === 'true'

    const mockInterviews = await prisma.mockInterview.findMany({
      take: limit,
      orderBy: { date: 'desc' },
      where: recent ? {
        date: {
          gte: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000) // Last 30 days
        }
      } : undefined,
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

    // Calculate summary statistics
    const totalMocks = await prisma.mockInterview.count()
    const passedMocks = await prisma.mockInterview.count({
      where: { solved: true }
    })
    const avgExplanationScore = await prisma.mockInterview.aggregate({
      _avg: {
        explanationScore: true,
        codeQualityScore: true,
        overallScore: true
      },
      where: {
        explanationScore: { not: null }
      }
    })

    const passRate = totalMocks > 0 ? (passedMocks / totalMocks) * 100 : 0

    return NextResponse.json({
      mockInterviews,
      stats: {
        total: totalMocks,
        passed: passedMocks,
        passRate: Math.round(passRate * 100) / 100,
        avgScores: {
          explanation: Math.round((avgExplanationScore._avg.explanationScore || 0) * 100) / 100,
          codeQuality: Math.round((avgExplanationScore._avg.codeQualityScore || 0) * 100) / 100,
          overall: Math.round((avgExplanationScore._avg.overallScore || 0) * 100) / 100
        }
      }
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
      timeTakenSeconds,
      patternRecognitionSeconds,
      solved,
      explanationScore,
      codeQualityScore,
      notes
    } = body

    // Validate required fields
    if (!problemId) {
      return NextResponse.json(
        { error: 'problemId is required' },
        { status: 400 }
      )
    }

    // Calculate overall score if both scores are provided
    let overallScore = null
    if (explanationScore && codeQualityScore) {
      overallScore = (explanationScore + codeQualityScore) / 2
    }

    // Create mock interview record
    const mockInterview = await prisma.mockInterview.create({
      data: {
        problemId,
        timeTakenSeconds: timeTakenSeconds || null,
        patternRecognitionSeconds: patternRecognitionSeconds || null,
        solved: solved || false,
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
