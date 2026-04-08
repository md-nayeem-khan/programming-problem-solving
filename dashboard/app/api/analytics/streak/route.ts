// @ts-nocheck
import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'
import { startOfDay, subDays, format } from 'date-fns'

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url)
    const days = parseInt(searchParams.get('days') || '30')

    // Calculate streak
    const currentStreak = await calculateCurrentStreak()
    const longestStreak = await calculateLongestStreak()

    // Get recent activity for contribution calendar
    const endDate = startOfDay(new Date())
    const startDate = subDays(endDate, days - 1)

    const dailyActivity = await prisma.submission.groupBy({
      by: ['submittedAt'],
      where: {
        submittedAt: {
          gte: startDate,
          lte: endDate
        },
        status: 'solved'
      },
      _count: {
        id: true
      }
    })

    // Format activity data for calendar
    const activityMap = new Map()
    for (let i = 0; i < days; i++) {
      const date = subDays(endDate, i)
      const dateStr = format(date, 'yyyy-MM-dd')
      activityMap.set(dateStr, 0)
    }

    dailyActivity.forEach(activity => {
      const dateStr = format(startOfDay(new Date(activity.submittedAt)), 'yyyy-MM-dd')
      if (activityMap.has(dateStr)) {
        activityMap.set(dateStr, activity._count.id)
      }
    })

    const calendarData = Array.from(activityMap.entries()).map(([date, count]) => ({
      date,
      count,
      level: getActivityLevel(count)
    }))

    // Weekly breakdown
    const weeklyStats = await getWeeklyStats()

    return NextResponse.json({
      currentStreak,
      longestStreak,
      calendarData: calendarData.reverse(), // Most recent first
      weeklyStats,
      totalActiveDays: calendarData.filter(d => d.count > 0).length
    })
  } catch (error) {
    console.error('Error fetching streak data:', error)
    return NextResponse.json({ error: 'Failed to fetch streak data' }, { status: 500 })
  }
}

async function calculateCurrentStreak(): Promise<number> {
  let streak = 0
  let currentDate = startOfDay(new Date())

  while (true) {
    const dailySolvedCount = await prisma.submission.count({
      where: {
        submittedAt: {
          gte: currentDate,
          lt: new Date(currentDate.getTime() + 24 * 60 * 60 * 1000)
        },
        status: 'solved'
      }
    })

    if (dailySolvedCount > 0) {
      streak++
      currentDate = subDays(currentDate, 1)
    } else {
      break
    }
  }

  return streak
}

async function calculateLongestStreak(): Promise<number> {
  // Get all days with solved submissions
  const solvedDays = await prisma.submission.groupBy({
    by: ['submittedAt'],
    where: {
      status: 'solved'
    },
    _count: {
      id: true
    },
    orderBy: {
      submittedAt: 'asc'
    }
  })

  if (solvedDays.length === 0) return 0

  const uniqueDays = [...new Set(
    solvedDays.map(day => 
      format(startOfDay(new Date(day.submittedAt)), 'yyyy-MM-dd')
    )
  )].sort()

  let longestStreak = 1
  let currentStreak = 1

  for (let i = 1; i < uniqueDays.length; i++) {
    const currentDay = new Date(uniqueDays[i])
    const previousDay = new Date(uniqueDays[i - 1])
    const dayDifference = (currentDay.getTime() - previousDay.getTime()) / (1000 * 60 * 60 * 24)

    if (dayDifference === 1) {
      currentStreak++
      longestStreak = Math.max(longestStreak, currentStreak)
    } else {
      currentStreak = 1
    }
  }

  return longestStreak
}

async function getWeeklyStats() {
  const endDate = startOfDay(new Date())
  const startDate = subDays(endDate, 6) // Last 7 days

  const weeklySubmissions = await prisma.submission.findMany({
    where: {
      submittedAt: {
        gte: startDate,
        lte: endDate
      }
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

  const solvedCount = weeklySubmissions.filter(s => s.status === 'solved').length
  const totalTimeSpent = weeklySubmissions.reduce((sum, s) => sum + s.timeSpentSeconds, 0)
  const uniquePatterns = new Set(
    weeklySubmissions.flatMap(s => s.problem.patterns.map(p => p.pattern.name))
  )

  return {
    problemsSolved: solvedCount,
    totalTimeSpent,
    patternsWorked: uniquePatterns.size,
    avgTimePerProblem: solvedCount > 0 ? Math.round(totalTimeSpent / solvedCount) : 0
  }
}

function getActivityLevel(count: number): number {
  if (count === 0) return 0
  if (count <= 2) return 1
  if (count <= 4) return 2
  if (count <= 6) return 3
  return 4
}
