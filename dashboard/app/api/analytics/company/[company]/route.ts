import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

// GET /api/analytics/company/[company] - Get analytics for a specific company
export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ company: string }> }
) {
  try {
    const { company } = await params
    
    // Validate company
    const validCompanies = ['Amazon', 'Google', 'Meta', 'Apple', 'Netflix', 'Microsoft']
    if (!validCompanies.includes(company)) {
      return NextResponse.json(
        { error: `Invalid company. Must be one of: ${validCompanies.join(', ')}` },
        { status: 400 }
      )
    }

    // Get all problems for this company
    const problems = await prisma.problem.findMany({
      where: { company },
      include: {
        submissions: {
          orderBy: { submittedAt: 'desc' }
        },
        patterns: {
          include: {
            pattern: true
          }
        }
      }
    })

    // Calculate statistics
    const totalProblems = problems.length
    const solvedProblems = problems.filter((p: any) => 
      p.submissions.some((s: any) => s.status === 'solved')
    ).length

    // Difficulty breakdown
    const byDifficulty = {
      Easy: { total: 0, solved: 0 },
      Medium: { total: 0, solved: 0 },
      Hard: { total: 0, solved: 0 }
    }

    problems.forEach((p: any) => {
      const diff = p.difficulty as keyof typeof byDifficulty
      if (byDifficulty[diff]) {
        byDifficulty[diff].total++
        if (p.submissions.some((s: any) => s.status === 'solved')) {
          byDifficulty[diff].solved++
        }
      }
    })

    // Pattern coverage
    const patternCounts: Record<string, { total: number; solved: number }> = {}
    problems.forEach((p: any) => {
      const isSolved = p.submissions.some((s: any) => s.status === 'solved')
      p.patterns.forEach((pp: any) => {
        const name = pp.pattern.name
        if (!patternCounts[name]) {
          patternCounts[name] = { total: 0, solved: 0 }
        }
        patternCounts[name].total++
        if (isSolved) patternCounts[name].solved++
      })
    })

    // Average time for solved problems
    const solvedSubmissions = problems
      .flatMap((p: any) => p.submissions)
      .filter((s: any) => s.status === 'solved')
    
    const avgTimeSeconds = solvedSubmissions.length > 0
      ? Math.round(solvedSubmissions.reduce((acc: number, s: any) => acc + s.timeSpentSeconds, 0) / solvedSubmissions.length)
      : 0

    // Hint usage
    const hintedSolves = solvedSubmissions.filter((s: any) => s.wasHintUsed).length
    const hintPercentage = solvedSubmissions.length > 0
      ? Math.round((hintedSolves / solvedSubmissions.length) * 100)
      : 0

    return NextResponse.json({
      company,
      summary: {
        totalProblems,
        solvedProblems,
        completionRate: totalProblems > 0 ? Math.round((solvedProblems / totalProblems) * 100) : 0,
        avgTimeSeconds,
        hintPercentage
      },
      byDifficulty,
      patternCoverage: Object.entries(patternCounts).map(([name, data]) => ({
        pattern: name,
        total: data.total,
        solved: data.solved,
        percentage: data.total > 0 ? Math.round((data.solved / data.total) * 100) : 0
      })).sort((a, b) => b.total - a.total),
      recentProblems: problems.slice(0, 10).map((p: any) => ({
        id: p.id,
        title: p.title,
        difficulty: p.difficulty,
        status: p.submissions[0]?.status || 'not_attempted',
        lastAttempt: p.submissions[0]?.submittedAt || null
      }))
    })
  } catch (error) {
    console.error('Error fetching company analytics:', error)
    return NextResponse.json(
      { error: 'Failed to fetch company analytics' },
      { status: 500 }
    )
  }
}
