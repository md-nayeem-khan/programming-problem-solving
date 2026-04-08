import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

// GET /api/problems/[id] - Get a single problem with details
export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params
    const problemId = parseInt(id)

    if (isNaN(problemId)) {
      return NextResponse.json(
        { error: 'Invalid problem ID' },
        { status: 400 }
      )
    }

    const problem = await prisma.problem.findUnique({
      where: { id: problemId },
      include: {
        tags: true,
        patterns: {
          include: {
            pattern: true
          }
        },
        submissions: {
          orderBy: { submittedAt: 'desc' },
          include: {
            revisions: true
          }
        }
      }
    })

    if (!problem) {
      return NextResponse.json(
        { error: 'Problem not found' },
        { status: 404 }
      )
    }

    return NextResponse.json({
      ...problem,
      tags: problem.tags.map(t => t.tag),
      patterns: problem.patterns.map(p => ({
        id: p.pattern.id,
        name: p.pattern.name,
        category: p.pattern.category
      }))
    })
  } catch (error) {
    console.error('Error fetching problem:', error)
    return NextResponse.json(
      { error: 'Failed to fetch problem' },
      { status: 500 }
    )
  }
}

// PUT /api/problems/[id] - Update a problem
export async function PUT(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params
    const problemId = parseInt(id)
    const body = await request.json()

    if (isNaN(problemId)) {
      return NextResponse.json(
        { error: 'Invalid problem ID' },
        { status: 400 }
      )
    }

    const { title, difficulty, url, notes, source, company, tags, patterns } = body

    const problem = await prisma.problem.update({
      where: { id: problemId },
      data: {
        ...(title && { title }),
        ...(difficulty && { difficulty }),
        ...(url !== undefined && { url }),
        ...(notes !== undefined && { notes }),
        ...(source && { source }),
        ...(company !== undefined && { company }),
        ...(tags && {
          tags: {
            deleteMany: {},
            create: tags.map((tag: string) => ({ tag }))
          }
        }),
        ...(patterns && {
          patterns: {
            deleteMany: {},
            create: patterns.map((patternId: number) => ({ patternId }))
          }
        })
      },
      include: {
        tags: true,
        patterns: {
          include: {
            pattern: true
          }
        }
      }
    })

    return NextResponse.json({
      ...problem,
      tags: problem.tags.map(t => t.tag),
      patterns: problem.patterns.map(p => ({
        id: p.pattern.id,
        name: p.pattern.name,
        category: p.pattern.category
      }))
    })
  } catch (error) {
    console.error('Error updating problem:', error)
    return NextResponse.json(
      { error: 'Failed to update problem' },
      { status: 500 }
    )
  }
}

// DELETE /api/problems/[id] - Delete a problem
export async function DELETE(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params
    const problemId = parseInt(id)

    if (isNaN(problemId)) {
      return NextResponse.json(
        { error: 'Invalid problem ID' },
        { status: 400 }
      )
    }

    await prisma.problem.delete({
      where: { id: problemId }
    })

    return NextResponse.json({ success: true })
  } catch (error) {
    console.error('Error deleting problem:', error)
    return NextResponse.json(
      { error: 'Failed to delete problem' },
      { status: 500 }
    )
  }
}
