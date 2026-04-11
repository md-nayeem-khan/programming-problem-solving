import test from "node:test";
import assert from "node:assert/strict";

import { GET } from "../app/api/analytics/daily-progress/route";
import { prisma } from "../lib/prisma";

test("daily progress endpoint uses strict current streak (requires activity today)", async () => {
  const originalDailyProgressFindMany = (prisma.dailyProgress as any).findMany;
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const yesterday = new Date(today);
  yesterday.setDate(yesterday.getDate() - 1);

  const twoDaysAgo = new Date(today);
  twoDaysAgo.setDate(twoDaysAgo.getDate() - 2);

  (prisma.dailyProgress as any).findMany = async (args: any) => {
    if (args?.where?.problemsSolved?.gt === 0) {
      return [
        { date: twoDaysAgo, problemsSolved: 1 },
        { date: yesterday, problemsSolved: 2 },
      ];
    }

    return [];
  };

  try {
    const request = {
      nextUrl: new URL("http://localhost:3000/api/analytics/daily-progress?days=7&streak=true"),
    } as any;

    const response = await GET(request);
    const body = await response.json();

    assert.equal(response.status, 200);
    assert.equal(body.streak.currentStreak, 0);
    assert.equal(body.streak.longestStreak, 2);
    assert.equal(body.streak.isActiveToday, false);
  } finally {
    (prisma.dailyProgress as any).findMany = originalDailyProgressFindMany;
  }
});
