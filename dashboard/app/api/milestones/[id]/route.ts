// @ts-nocheck
import { NextRequest, NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';

// PATCH /api/milestones/[id] - Update milestone (mainly for completion)
export async function PATCH(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params;
    const milestoneId = parseInt(id);
    const body = await request.json();

    const { completed, completionNote } = body;

    const updateData: any = {};
    
    if (completed !== undefined) {
      updateData.completed = completed;
      if (completed) {
        updateData.completedDate = new Date();
        if (completionNote) {
          updateData.completionNote = completionNote;
        }
      }
    }

    const milestone = await prisma.milestone.update({
      where: { id: milestoneId },
      data: updateData
    });

    return NextResponse.json({ milestone });
  } catch (error) {
    console.error('Error updating milestone:', error);
    return NextResponse.json(
      { error: 'Failed to update milestone' },
      { status: 500 }
    );
  }
}
