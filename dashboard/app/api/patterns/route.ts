// @ts-nocheck
import { NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

export async function GET() {
  try {
    const patterns = await prisma.pattern.findMany({
      orderBy: [{ category: 'asc' }, { name: 'asc' }]
    })
    return NextResponse.json({ patterns })
  } catch (error) {
    console.error('Error fetching patterns:', error)
    return NextResponse.json({ error: 'Failed to fetch patterns' }, { status: 500 })
  }
}

