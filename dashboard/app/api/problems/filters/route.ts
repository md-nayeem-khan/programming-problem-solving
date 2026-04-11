import { NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

function normalize(value: string | null | undefined): string | null {
  if (typeof value !== 'string') return null
  const trimmed = value.trim()
  return trimmed.length > 0 ? trimmed : null
}

// GET /api/problems/filters - Distinct filter values for problems page
export async function GET() {
  try {
    const [patterns, tags, relationCompanies] = await Promise.all([
      prisma.pattern.findMany({
        distinct: ['name'],
        select: { name: true },
        orderBy: { name: 'asc' },
      }),
      prisma.problemTag.findMany({
        distinct: ['tag'],
        select: { tag: true },
        orderBy: { tag: 'asc' },
      }),
      prisma.problemCompany.findMany({
        distinct: ['companyId'],
        select: {
          companyId: true,
          company: {
            select: {
              name: true,
            },
          },
        },
      }),
    ])

    const companyOptions = relationCompanies
      .map((entry) => {
        const name = normalize(entry.company.name)
        if (!name) return null
        return {
          id: entry.companyId,
          name,
        }
      })
      .filter((entry): entry is { id: number; name: string } => Boolean(entry))
      .sort((a, b) => a.name.localeCompare(b.name))

    return NextResponse.json({
      patterns: patterns.map((p) => p.name),
      tags: tags.map((t) => t.tag),
      companyOptions,
    })
  } catch (error) {
    console.error('Error fetching problem filters:', error)
    return NextResponse.json(
      { error: 'Failed to fetch problem filters' },
      { status: 500 }
    )
  }
}
