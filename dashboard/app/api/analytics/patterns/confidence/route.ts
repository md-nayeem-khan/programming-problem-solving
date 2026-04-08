// @ts-nocheck
import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'
import { calculatePatternConfidence, type Confidence, type PatternStats } from '@/types'

// GET /api/analytics/patterns/confidence - Calculate pattern confidence levels
export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams
    const minProblems = parseInt(searchParams.get('minProblems') || '1')
    const timeframe = searchParams.get('timeframe') // 'week', 'month', 'all'
    const company = searchParams.get('company') // Optional filter by company
    const source = searchParams.get('source') // Optional filter by source

    // Build date filter for timeframe
    const submissionWhere: any = {}
    if (timeframe === 'week') {
      const weekAgo = new Date()
      weekAgo.setDate(weekAgo.getDate() - 7)
      submissionWhere.submittedAt = { gte: weekAgo }
    } else if (timeframe === 'month') {
      const monthAgo = new Date()
      monthAgo.setMonth(monthAgo.getMonth() - 1)
      submissionWhere.submittedAt = { gte: monthAgo }
    }

    // Build problem filter
    const problemWhere: any = {}
    if (company) problemWhere.company = company
    if (source) problemWhere.source = source

    // Combine filters
    const where = {
      ...submissionWhere,
      ...(Object.keys(problemWhere).length > 0 ? { problem: problemWhere } : {})
    }

    // Get all submissions with problem and pattern relationships
    const submissions = await prisma.submission.findMany({
      where,
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

    // Group submissions by pattern
    const patternMap = new Map<string, {
      pattern: { id: number; name: string; category: string };
      submissions: any[];
      problemIds: Set<number>;
    }>()

    submissions.forEach(submission => {
      submission.problem.patterns.forEach(problemPattern => {
        const pattern = problemPattern.pattern
        const key = pattern.name

        if (!patternMap.has(key)) {
          patternMap.set(key, {
            pattern,
            submissions: [],
            problemIds: new Set()
          })
        }

        const patternData = patternMap.get(key)!
        patternData.submissions.push(submission)
        patternData.problemIds.add(submission.problemId)
      })
    })

    // Calculate statistics for each pattern
    const patternStats: PatternStats[] = []

    patternMap.forEach((data, patternName) => {
      const { pattern, submissions: patternSubmissions, problemIds } = data
      
      // Only include patterns with minimum problems
      if (problemIds.size < minProblems) return

      // Calculate metrics
      const solvedSubmissions = patternSubmissions.filter(s => s.status === 'solved')
      const totalSolved = solvedSubmissions.length
      
      if (totalSolved === 0) return // Skip patterns with no solved problems

      // Average time calculation
      const totalTime = solvedSubmissions.reduce((sum, s) => sum + s.timeSpentSeconds, 0)
      const avgTimeSeconds = totalTime / totalSolved

      // Hint usage rate calculation
      const hintsUsed = solvedSubmissions.filter(s => s.wasHintUsed).length
      const hintUsageRate = hintsUsed / totalSolved

      // Calculate confidence level
      const confidence = calculatePatternConfidence(avgTimeSeconds, hintUsageRate)

      const stats: PatternStats = {
        pattern: pattern.name,
        category: pattern.category,
        totalSolved,
        avgTimeSeconds,
        hintUsageRate,
        confidence,
        problemIds: Array.from(problemIds)
      }

      patternStats.push(stats)
    })

    // Sort by confidence (weak patterns first) and then by total problems
    patternStats.sort((a, b) => {
      const confidenceOrder = { 'Weak': 0, 'Medium': 1, 'Strong': 2 }
      const aOrder = confidenceOrder[a.confidence]
      const bOrder = confidenceOrder[b.confidence]
      
      if (aOrder !== bOrder) {
        return aOrder - bOrder // Weak patterns first
      }
      
      return b.totalSolved - a.totalSolved // More problems first within same confidence
    })

    // Calculate aggregate metrics
    const totalPatterns = patternStats.length
    const weakPatterns = patternStats.filter(p => p.confidence === 'Weak')
    const mediumPatterns = patternStats.filter(p => p.confidence === 'Medium')  
    const strongPatterns = patternStats.filter(p => p.confidence === 'Strong')

    // Category analysis
    const categoryStats = new Map<string, {
      total: number;
      weak: number;
      medium: number;
      strong: number;
      avgTime: number;
      avgHintRate: number;
    }>()

    patternStats.forEach(pattern => {
      if (!categoryStats.has(pattern.category)) {
        categoryStats.set(pattern.category, {
          total: 0,
          weak: 0,
          medium: 0,
          strong: 0,
          avgTime: 0,
          avgHintRate: 0
        })
      }

      const catStats = categoryStats.get(pattern.category)!
      catStats.total++
      catStats.avgTime += pattern.avgTimeSeconds
      catStats.avgHintRate += pattern.hintUsageRate
      
      if (pattern.confidence === 'Weak') catStats.weak++
      else if (pattern.confidence === 'Medium') catStats.medium++
      else if (pattern.confidence === 'Strong') catStats.strong++
    })

    // Normalize category averages
    categoryStats.forEach(catStats => {
      catStats.avgTime = Math.round(catStats.avgTime / catStats.total)
      catStats.avgHintRate = Math.round((catStats.avgHintRate / catStats.total) * 100) / 100
    })

    const response = {
      patterns: patternStats,
      summary: {
        totalPatterns,
        weakCount: weakPatterns.length,
        mediumCount: mediumPatterns.length,
        strongCount: strongPatterns.length,
        weakestPatterns: weakPatterns.slice(0, 5).map(p => ({
          name: p.pattern,
          category: p.category,
          avgTimeMinutes: Math.round(p.avgTimeSeconds / 60),
          hintUsageRate: p.hintUsageRate,
          totalSolved: p.totalSolved
        })),
        strongestPatterns: strongPatterns.slice(-5).map(p => ({
          name: p.pattern,
          category: p.category,
          avgTimeMinutes: Math.round(p.avgTimeSeconds / 60),
          hintUsageRate: p.hintUsageRate,
          totalSolved: p.totalSolved
        }))
      },
      categories: Object.fromEntries(categoryStats),
      recommendations: [] as string[]
    }

    // Generate recommendations
    if (weakPatterns.length > strongPatterns.length) {
      response.recommendations.push("Focus on your weak patterns to improve overall confidence")
    }

    if (weakPatterns.length >= 3) {
      const topWeakCategories = [...categoryStats.entries()]
        .filter(([_, stats]) => stats.weak > 0)
        .sort((a, b) => b[1].weak - a[1].weak)
        .slice(0, 2)
        .map(([category]) => category)
      
      if (topWeakCategories.length > 0) {
        response.recommendations.push(`Prioritize practicing ${topWeakCategories.join(' and ')} patterns`)
      }
    }

    const highHintPatterns = weakPatterns.filter(p => p.hintUsageRate > 0.5)
    if (highHintPatterns.length > 0) {
      response.recommendations.push("Practice solving problems without hints in your weak areas")
    }

    const slowPatterns = weakPatterns.filter(p => p.avgTimeSeconds > 35 * 60)
    if (slowPatterns.length > 0) {
      response.recommendations.push("Work on solving problems faster in your weak patterns")
    }

    return NextResponse.json(response)
  } catch (error) {
    console.error('Error calculating pattern confidence:', error)
    return NextResponse.json(
      { error: 'Failed to calculate pattern confidence' },
      { status: 500 }
    )
  }
}
