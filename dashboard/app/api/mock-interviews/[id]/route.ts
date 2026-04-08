import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

// GET /api/mock-interviews/[id] - Get a single mock interview
export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params
    const mockId = parseInt(id)

    if (isNaN(mockId)) {
      return NextResponse.json(
        { error: 'Invalid mock interview ID' },
        { status: 400 }
      )
    }

    const mockInterview = await prisma.mockInterview.findUnique({
      where: { id: mockId },
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

    if (!mockInterview) {
      return NextResponse.json(
        { error: 'Mock interview not found' },
        { status: 404 }
      )
    }

    return NextResponse.json({
      ...mockInterview,
      problem: {
        ...mockInterview.problem,
        patterns: mockInterview.problem.patterns.map(p => p.pattern.name)
      }
    })
  } catch (error) {
    console.error('Error fetching mock interview:', error)
    return NextResponse.json(
      { error: 'Failed to fetch mock interview' },
      { status: 500 }
    )
  }
}

// PUT /api/mock-interviews/[id] - Update a mock interview (submit evaluation, etc.)
export async function PUT(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params
    const mockId = parseInt(id)
    const body = await request.json()

    if (isNaN(mockId)) {
      return NextResponse.json(
        { error: 'Invalid mock interview ID' },
        { status: 400 }
      )
    }

    const { 
      status, 
      timeSpent,
      score,
      approachClarity,
      codeQuality,
      problemSolving,
      timeManagement,
      communication,
      feedback
    } = body

    const updateData: any = {}
    
    if (status) updateData.status = status
    if (timeSpent !== undefined) updateData.timeSpent = timeSpent
    if (score !== undefined) updateData.score = score
    if (approachClarity !== undefined) updateData.approachClarity = approachClarity
    if (codeQuality !== undefined) updateData.codeQuality = codeQuality
    if (problemSolving !== undefined) updateData.problemSolving = problemSolving
    if (timeManagement !== undefined) updateData.timeManagement = timeManagement
    if (communication !== undefined) updateData.communication = communication
    if (feedback !== undefined) updateData.feedback = feedback
    
    if (status === 'completed') {
      updateData.completedAt = new Date()
    }

    const mockInterview = await prisma.mockInterview.update({
      where: { id: mockId },
      data: updateData,
      include: {
        problem: true
      }
    })

    return NextResponse.json(mockInterview)
  } catch (error) {
    console.error('Error updating mock interview:', error)
    return NextResponse.json(
      { error: 'Failed to update mock interview' },
      { status: 500 }
    )
  }
}

// DELETE /api/mock-interviews/[id] - Delete a mock interview
export async function DELETE(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params
    const mockId = parseInt(id)

    if (isNaN(mockId)) {
      return NextResponse.json(
        { error: 'Invalid mock interview ID' },
        { status: 400 }
      )
    }

    await prisma.mockInterview.delete({
      where: { id: mockId }
    })

    return NextResponse.json({ success: true })
  } catch (error) {
    console.error('Error deleting mock interview:', error)
    return NextResponse.json(
      { error: 'Failed to delete mock interview' },
      { status: 500 }
    )
  }
}
