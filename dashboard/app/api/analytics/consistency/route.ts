// @ts-nocheck
import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

// GET /api/analytics/consistency - Get consistency metrics
export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams
    const days = parseInt(searchParams.get('days') || '30')
    
    const startDate = new Date()
    startDate.setDate(startDate.getDate() - days)
    startDate.setHours(0, 0, 0, 0)

    // Get all submissions in the date range
    const submissions = await prisma.submission.findMany({
      where: {
        submittedAt: {
          gte: startDate
        }
      },
      orderBy: { submittedAt: 'asc' },
      select: {
        id: true,
        submittedAt: true,
        status: true,
        timeSpentSeconds: true
      }
    })

    // Group by date
    const dailyData: Record<string, { count: number; totalTime: number }> = {}
    
    submissions.forEach(sub => {
      const dateKey = sub.submittedAt.toISOString().split('T')[0]
      if (!dailyData[dateKey]) {
        dailyData[dateKey] = { count: 0, totalTime: 0 }
      }
      dailyData[dateKey].count++
      dailyData[dateKey].totalTime += sub.timeSpentSeconds
    })

    // Calculate streaks and consistency metrics
    const dates = Object.keys(dailyData).sort()
    let currentStreak = 0
    let longestStreak = 0
    let tempStreak = 0
    
    const today = new Date().toISOString().split('T')[0]
    const yesterday = new Date(Date.now() - 86400000).toISOString().split('T')[0]

    // Calculate streaks
    for (let i = 0; i < days; i++) {
      const checkDate = new Date()
      checkDate.setDate(checkDate.getDate() - i)
      const dateStr = checkDate.toISOString().split('T')[0]
      
      if (dailyData[dateStr]) {
        tempStreak++
        if (i === 0 || dailyData[new Date(Date.now() - 86400000 * i).toISOString().split('T')[0]]) {
          currentStreak = tempStreak
        }
      } else {
        longestStreak = Math.max(longestStreak, tempStreak)
        tempStreak = 0
      }
    }
    longestStreak = Math.max(longestStreak, tempStreak)

    // Calculate consistency percentage
    const activeDays = Object.keys(dailyData).length
    const consistencyPercentage = Math.round((activeDays / days) * 100)

    // Calculate average daily problems
    const totalProblems = submissions.length
    const avgDailyProblems = activeDays > 0 ? (totalProblems / activeDays).toFixed(1) : 0

    // Format daily data for chart
    const chartData = []
    for (let i = days - 1; i >= 0; i--) {
      const date = new Date()
      date.setDate(date.getDate() - i)
      const dateStr = date.toISOString().split('T')[0]
      chartData.push({
        date: dateStr,
        count: dailyData[dateStr]?.count || 0,
        timeMinutes: Math.round((dailyData[dateStr]?.totalTime || 0) / 60)
      })
    }

    return NextResponse.json({
      currentStreak,
      longestStreak,
      activeDays,
      totalDays: days,
      consistencyPercentage,
      totalProblems,
      avgDailyProblems,
      dailyData: chartData
    })
  } catch (error) {
    console.error('Error fetching consistency data:', error)
    return NextResponse.json(
      { error: 'Failed to fetch consistency data' },
      { status: 500 }
    )
  }
}
