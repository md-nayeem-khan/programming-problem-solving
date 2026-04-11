"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { BarChart3, Clock, Flame, Sparkles } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { staggerContainer, staggerItem } from "@/lib/animations";
import { DashboardHeaderStatsSkeleton } from "@/components/dashboard/DashboardSkeletons";

interface DashboardHeaderStatsData {
  totalProblemsSolved: number;
  totalStudyHours: number;
  dailyAverageHours: number;
  currentStreak: number;
}

export function DashboardHeaderStats() {
  const [data, setData] = useState<DashboardHeaderStatsData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchStats() {
      try {
        const [statsRes, streakRes] = await Promise.all([
          fetch("/api/analytics/stats"),
          fetch("/api/analytics/streak?days=30"),
        ]);

        if (!statsRes.ok || !streakRes.ok) {
          throw new Error("Failed to fetch dashboard header stats");
        }

        const [stats, streak] = await Promise.all([
          statsRes.json(),
          streakRes.json(),
        ]);

        setData({
          totalProblemsSolved:
            stats.totalProblemsSolvedUnique ?? stats.totalSolvedSubmissions ?? 0,
          totalStudyHours:
            Math.round((((stats.totalTimeMinutes || 0) / 60) * 10)) / 10,
          dailyAverageHours:
            Math.round((((streak.weeklyStats?.totalTimeSpent || 0) / (7 * 3600)) * 10)) /
            10,
          currentStreak: streak.currentStreak || 0,
        });
      } catch {
        setData({
          totalProblemsSolved: 0,
          totalStudyHours: 0,
          dailyAverageHours: 0,
          currentStreak: 0,
        });
      } finally {
        setLoading(false);
      }
    }

    fetchStats();
  }, []);

  if (loading || !data) {
    return <DashboardHeaderStatsSkeleton />;
  }

  return (
    <motion.div
      variants={staggerContainer}
      initial="hidden"
      animate="visible"
      className="grid grid-cols-1 md:grid-cols-4 gap-4"
    >
      <motion.div variants={staggerItem}>
        <Card className="bg-gradient-to-br from-pink-100 via-purple-50 to-fuchsia-100 dark:from-pink-950/40 dark:via-purple-900/20 dark:to-fuchsia-950/40 border-2 border-pink-200/60 dark:border-pink-800/60 overflow-hidden relative">
          <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-pink-400/30 to-purple-400/30 rounded-full blur-2xl" />
          <CardContent className="pt-6 relative">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Total Problems Solved</p>
                <p className="text-3xl font-bold bg-gradient-to-r from-pink-600 to-purple-600 bg-clip-text text-transparent">
                  {data.totalProblemsSolved}
                </p>
              </div>
              <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-pink-500 to-purple-500 flex items-center justify-center shadow-lg shadow-pink-500/30">
                <BarChart3 className="h-6 w-6 text-white" />
              </div>
            </div>
          </CardContent>
        </Card>
      </motion.div>

      <motion.div variants={staggerItem}>
        <Card className="bg-gradient-to-br from-blue-100 via-indigo-50 to-violet-100 dark:from-blue-950/40 dark:via-indigo-900/20 dark:to-violet-950/40 border-2 border-blue-200/60 dark:border-blue-800/60 overflow-hidden relative">
          <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-blue-400/30 to-indigo-400/30 rounded-full blur-2xl" />
          <CardContent className="pt-6 relative">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Total Study Time</p>
                <p className="text-3xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
                  {data.totalStudyHours}h
                </p>
              </div>
              <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-500 flex items-center justify-center shadow-lg shadow-blue-500/30">
                <Clock className="h-6 w-6 text-white" />
              </div>
            </div>
          </CardContent>
        </Card>
      </motion.div>

      <motion.div variants={staggerItem}>
        <Card className="bg-gradient-to-br from-emerald-100 via-green-50 to-teal-100 dark:from-emerald-950/40 dark:via-green-900/20 dark:to-teal-950/40 border-2 border-emerald-200/60 dark:border-emerald-800/60 overflow-hidden relative">
          <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-emerald-400/30 to-teal-400/30 rounded-full blur-2xl" />
          <CardContent className="pt-6 relative">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Daily Average</p>
                <p className="text-3xl font-bold bg-gradient-to-r from-emerald-600 to-teal-600 bg-clip-text text-transparent">
                  {data.dailyAverageHours}h
                </p>
              </div>
              <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-emerald-500 to-teal-500 flex items-center justify-center shadow-lg shadow-emerald-500/30">
                <Sparkles className="h-6 w-6 text-white" />
              </div>
            </div>
          </CardContent>
        </Card>
      </motion.div>

      <motion.div variants={staggerItem}>
        <Card className="bg-gradient-to-br from-orange-100 via-amber-50 to-yellow-100 dark:from-orange-950/40 dark:via-amber-900/20 dark:to-yellow-950/40 border-2 border-orange-200/60 dark:border-orange-800/60 overflow-hidden relative">
          <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-orange-400/30 to-amber-400/30 rounded-full blur-2xl" />
          <CardContent className="pt-6 relative">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Current Streak</p>
                <p className="text-3xl font-bold bg-gradient-to-r from-orange-600 to-amber-600 bg-clip-text text-transparent">
                  {data.currentStreak}
                </p>
              </div>
              <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-orange-500 to-amber-500 flex items-center justify-center shadow-lg shadow-orange-500/30">
                <Flame className="h-6 w-6 text-white" />
              </div>
            </div>
          </CardContent>
        </Card>
      </motion.div>
    </motion.div>
  );
}
