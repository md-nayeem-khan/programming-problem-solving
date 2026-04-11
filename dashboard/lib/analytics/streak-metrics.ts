import { addDays, format, startOfDay, subDays } from "date-fns";

export interface SubmissionPoint {
  submittedAt: Date;
  timeSpentSeconds: number;
}

export interface CalendarDay {
  date: string;
  count: number;
  totalTime: number;
  level: number;
}

export interface DateWindow {
  startDate: Date;
  endDate: Date;
  endExclusive: Date;
}

export function getActivityLevel(count: number): number {
  if (count === 0) return 0;
  if (count <= 2) return 1;
  if (count <= 4) return 2;
  if (count <= 6) return 3;
  return 4;
}

export function toDateKey(date: Date): string {
  return format(startOfDay(date), "yyyy-MM-dd");
}

export function getDateWindow(days: number, now = new Date()): DateWindow {
  const safeDays = Number.isFinite(days) && days > 0 ? Math.floor(days) : 30;
  const endDate = startOfDay(now);
  const endExclusive = addDays(endDate, 1);
  const startDate = subDays(endDate, safeDays - 1);

  return { startDate, endDate, endExclusive };
}

export function buildDailyMap(submissions: SubmissionPoint[]): Map<string, { count: number; totalTime: number }> {
  const dayMap = new Map<string, { count: number; totalTime: number }>();

  for (const submission of submissions) {
    const key = toDateKey(submission.submittedAt);
    const existing = dayMap.get(key) ?? { count: 0, totalTime: 0 };
    existing.count += 1;
    existing.totalTime += submission.timeSpentSeconds || 0;
    dayMap.set(key, existing);
  }

  return dayMap;
}

export function buildCalendarData(days: number, now: Date, dayMap: Map<string, { count: number; totalTime: number }>): CalendarDay[] {
  const { endDate } = getDateWindow(days, now);
  const calendarData: CalendarDay[] = [];

  for (let i = days - 1; i >= 0; i -= 1) {
    const date = subDays(endDate, i);
    const key = toDateKey(date);
    const values = dayMap.get(key) ?? { count: 0, totalTime: 0 };

    calendarData.push({
      date: key,
      count: values.count,
      totalTime: values.totalTime,
      level: getActivityLevel(values.count),
    });
  }

  return calendarData;
}

export function calculateCurrentStreakFromSolvedDays(solvedDayKeys: Set<string>, now = new Date()): number {
  let streak = 0;
  let cursor = startOfDay(now);

  while (true) {
    const key = toDateKey(cursor);
    if (!solvedDayKeys.has(key)) {
      break;
    }

    streak += 1;
    cursor = subDays(cursor, 1);
  }

  return streak;
}

export function calculateLongestStreakFromSolvedDays(solvedDayKeys: Set<string>): number {
  const sortedKeys = Array.from(solvedDayKeys).sort();
  if (sortedKeys.length === 0) return 0;

  let longest = 1;
  let running = 1;

  for (let i = 1; i < sortedKeys.length; i += 1) {
    const previous = new Date(sortedKeys[i - 1]);
    const current = new Date(sortedKeys[i]);
    const delta = (current.getTime() - previous.getTime()) / (24 * 60 * 60 * 1000);

    if (delta === 1) {
      running += 1;
      if (running > longest) longest = running;
    } else {
      running = 1;
    }
  }

  return longest;
}