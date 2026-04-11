// @ts-nocheck
import { NextRequest, NextResponse } from 'next/server'
import { PrismaClient } from '@prisma/client'

const prisma = new PrismaClient()

export async function POST(request: NextRequest) {
  try {
    const body = await request.json()
    const { difficulty, timeLimit = 2700, company } = body // 45 min default

    // Select a random problem based on criteria
    const whereClause: any = {}
    
    if (difficulty) {
      whereClause.difficulty = difficulty
    }
    
    if (company) {
      whereClause.companies = {
        some: {
          company: {
            name: company,
          },
        },
      }
    }
    
    // Get problems that haven't been used in mock interviews recently
    const problems = await prisma.problem.findMany({
      where: {
        ...whereClause,
        // Exclude problems used in mock interviews in the last 7 days
        mockInterviews: {
          none: {
            date: {
              gte: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000)
            }
          }
        }
      },
      include: {
        patterns: {
          include: {
            pattern: true
          }
        }
      }
    })

    if (problems.length === 0) {
      return NextResponse.json(
        { error: 'No suitable problems found for mock interview' },
        { status: 404 }
      )
    }

    // Select random problem
    const selectedProblem = problems[Math.floor(Math.random() * problems.length)]

    // Create mock interview record
    const mockInterview = await prisma.mockInterview.create({
      data: {
        problemId: selectedProblem.id,
        date: new Date(),
        timeLimit,
        solved: false, // Will be updated after completion
        timeTakenSeconds: 0,
        explanationScore: null,
        codeQualityScore: null,
        overallScore: null
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
      mockInterview,
      message: 'Mock interview started successfully'
    })
    
  } catch (error) {
    console.error('Error starting mock interview:', error)
    return NextResponse.json(
      { error: 'Failed to start mock interview' },
      { status: 500 }
    )
  }
}
