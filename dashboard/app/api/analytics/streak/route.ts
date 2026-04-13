// @ts-nocheck
import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'
import { getDateWindow, buildDailyMap, buildCalendarData, calculateCurrentStreakFromSolvedDays, calculateLongestStreakFromSolvedDays, toDateKey } from '@/lib/analytics/streak-metrics'
import { getLatestSubmissionsPerProblem } from '@/types'

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url)
    const requestedDays = parseInt(searchParams.get('days') || '30', 10)
    const days = Number.isFinite(requestedDays) && requestedDays > 0 ? requestedDays : 30

    const solvedSubmissions = await prisma.submission.findMany({
      where: { status: 'solved' },
      select: {
        submittedAt: true,
        timeSpentSeconds: true,
      },
      orderBy: { submittedAt: 'asc' }
    })

    const solvedDayKeys = new Set(
      solvedSubmissions.map(submission => toDateKey(new Date(submission.submittedAt)))
    )

    // Calculate streak
    const currentStreak = calculateCurrentStreakFromSolvedDays(solvedDayKeys)
    const longestStreak = calculateLongestStreakFromSolvedDays(solvedDayKeys)

    // Get recent activity for contribution calendar
    const { startDate, endExclusive } = getDateWindow(days)
    const windowSolvedSubmissions = solvedSubmissions.filter(submission => {
      const submittedAt = new Date(submission.submittedAt)
      return submittedAt >= startDate && submittedAt < endExclusive
    })

    const activityMap = buildDailyMap(windowSolvedSubmissions)
    const calendarData = buildCalendarData(days, new Date(), activityMap)

    // Weekly breakdown
    const weeklyStats = await getWeeklyStats()

    return NextResponse.json({
      currentStreak,
      longestStreak,
      calendarData,
      calendar: calendarData,
      weeklyStats,
      totalActiveDays: calendarData.filter(d => d.count > 0).length
    })
  } catch (error) {
    console.error('Error fetching streak data:', error)
    return NextResponse.json({ error: 'Failed to fetch streak data' }, { status: 500 })
  }
}

async function getWeeklyStats() {
  const { startDate, endExclusive } = getDateWindow(7)

  const weeklySubmissions = await prisma.submission.findMany({
    where: {
      submittedAt: {
        gte: startDate,
        lt: endExclusive
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

  const latestWeeklySubmissions = getLatestSubmissionsPerProblem(weeklySubmissions)
  const solvedLatestSubmissions = latestWeeklySubmissions.filter(s => s.status === 'solved')

  const solvedCount = solvedLatestSubmissions.length
  const totalTimeSpent = latestWeeklySubmissions.reduce((sum, s) => sum + s.timeSpentSeconds, 0)
  const uniquePatterns = new Set(
    latestWeeklySubmissions.flatMap(s => s.problem.patterns.map(p => p.pattern.name))
  )
  const solvedTimeSpent = solvedLatestSubmissions.reduce((sum, s) => sum + s.timeSpentSeconds, 0)

  return {
    problemsSolved: solvedCount,
    totalTimeSpent,
    patternsWorked: uniquePatterns.size,
    avgTimePerProblem: solvedCount > 0 ? Math.round(solvedTimeSpent / solvedCount) : 0
  }
}
