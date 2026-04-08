// @ts-nocheck
import { NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

export async function GET() {
  const patterns = await prisma.pattern.findMany({
    include: {
      problems: {
        include: {
          problem: { include: { submissions: { where: { status: 'solved' } } } },
        },
      },
    },
  })
  
  const patternStats = patterns.map(pattern => {
    const submissions = pattern.problems.flatMap(p => p.problem.submissions)
    const totalTime = submissions.reduce((sum, s) => sum + s.timeSpentSeconds, 0)
    return {
      id: pattern.id,
      name: pattern.name,
      category: pattern.category,
      problemCount: pattern.problems.length,
      totalTimeSeconds: totalTime,
      avgTimeSeconds: submissions.length > 0 ? Math.round(totalTime / submissions.length) : 0,
    }
  })
  
  return NextResponse.json({ patterns: patternStats })
}

