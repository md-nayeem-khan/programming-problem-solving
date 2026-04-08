// @ts-nocheck
import { NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

export async function GET() {
  try {
    const totalProblems = await prisma.problem.count()
    const timeResult = await prisma.submission.aggregate({ _sum: { timeSpentSeconds: true } })
    
    // Get problem counts by difficulty
    const byDifficulty = await prisma.problem.groupBy({ by: ['difficulty'], _count: true })
    const byPlatform = await prisma.problem.groupBy({ by: ['platform'], _count: true })
    
    // Calculate average times per difficulty
    const easyTimes: number[] = []
    const mediumTimes: number[] = []
    const hardTimes: number[] = []
    
    // Get solved submissions with problem difficulty
    const solvedSubmissions = await prisma.submission.findMany({
      where: { status: 'solved' },
      select: {
        timeSpentSeconds: true,
        problem: {
          select: { difficulty: true }
        }
      }
    })
    
    solvedSubmissions.forEach(sub => {
      const timeInMinutes = sub.timeSpentSeconds / 60
      switch (sub.problem.difficulty) {
        case 'Easy':
          easyTimes.push(timeInMinutes)
          break
        case 'Medium':
          mediumTimes.push(timeInMinutes)
          break
        case 'Hard':
          hardTimes.push(timeInMinutes)
          break
      }
    })
    
    const calculateAvg = (times: number[]) => times.length > 0 ? Math.round(times.reduce((a, b) => a + b) / times.length) : 0
    
    // Extract counts by difficulty with fallback to 0
    const difficultyMap = Object.fromEntries(byDifficulty.map(d => [d.difficulty, d._count]))
    
    return NextResponse.json({
      totalProblems,
      totalTimeSeconds: timeResult._sum.timeSpentSeconds || 0,
      totalTimeMinutes: Math.round((timeResult._sum.timeSpentSeconds || 0) / 60),
      
      // Individual difficulty counts (expected by UI)
      easyCount: difficultyMap['Easy'] || 0,
      mediumCount: difficultyMap['Medium'] || 0,
      hardCount: difficultyMap['Hard'] || 0,
      
      // Average times in minutes (expected by UI)
      easyAvgTime: calculateAvg(easyTimes),
      mediumAvgTime: calculateAvg(mediumTimes),
      hardAvgTime: calculateAvg(hardTimes),
      
      // Original grouped data
      problemsByDifficulty: difficultyMap,
      problemsByPlatform: Object.fromEntries(byPlatform.map(p => [p.platform, p._count])),
    })
  } catch (error) {
    console.error('Stats API Error:', error)
    return NextResponse.json({ error: 'Failed to fetch stats' }, { status: 500 })
  }
}

