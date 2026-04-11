"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Clock, CheckCircle, AlertCircle, XCircle } from "lucide-react";
import { fadeInUp } from "@/lib/animations";
import { DashboardMainCardSkeleton } from "@/components/dashboard/DashboardSkeletons";

interface TimeStats {
  easy: { avg: number; count: number; met: boolean; hasData: boolean };
  medium: { avg: number; count: number; met: boolean; hasData: boolean };
  hard: { avg: number; count: number; met: boolean; hasData: boolean };
}

const BENCHMARKS = {
  Easy: 15,
  Medium: 25,
  Hard: 40,
};

export function TimePerformanceCard() {
  const [stats, setStats] = useState<TimeStats | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchTimeStats() {
      try {
        const response = await fetch("/api/analytics/readiness");
        if (!response.ok) throw new Error("Failed to fetch time stats");
        
        const data = await response.json();
        
        // Use the byDifficulty data from readiness API
        const difficulties = data.byDifficulty || {};
        
        setStats({
          easy: {
            avg: Math.round(difficulties.easy?.avgTimeMinutes ?? 0),
            count: difficulties.easy?.solved ?? 0,
            hasData: (difficulties.easy?.solved ?? 0) > 0,
            met: (difficulties.easy?.solved ?? 0) > 0 && (difficulties.easy?.avgTimeMinutes ?? 0) <= BENCHMARKS.Easy,
          },
          medium: {
            avg: Math.round(difficulties.medium?.avgTimeMinutes ?? 0),
            count: difficulties.medium?.solved ?? 0,
            hasData: (difficulties.medium?.solved ?? 0) > 0,
            met: (difficulties.medium?.solved ?? 0) > 0 && (difficulties.medium?.avgTimeMinutes ?? 0) <= BENCHMARKS.Medium,
          },
          hard: {
            avg: Math.round(difficulties.hard?.avgTimeMinutes ?? 0),
            count: difficulties.hard?.solved ?? 0,
            hasData: (difficulties.hard?.solved ?? 0) > 0,
            met: (difficulties.hard?.solved ?? 0) > 0 && (difficulties.hard?.avgTimeMinutes ?? 0) <= BENCHMARKS.Hard,
          },
        });
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load data");
      } finally {
        setLoading(false);
      }
    }

    fetchTimeStats();
  }, []);

  if (loading) {
    return <DashboardMainCardSkeleton rows={3} centerRows={true} />;
  }

  if (error || !stats) {
    return (
      <Card className="h-full min-h-[400px] bg-gradient-to-br from-red-50 to-orange-50 dark:from-red-950/20 dark:to-orange-950/20 border-red-200 dark:border-red-800">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-red-600 dark:text-red-400">
            <AlertCircle className="h-5 w-5" />
            Error Loading Time Stats
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">{error || "No data available"}</p>
        </CardContent>
      </Card>
    );
  }

  const getDifficultyIcon = (met: boolean) => {
    if (met) return <CheckCircle className="h-5 w-5 text-green-600 dark:text-green-400" />;
    return <XCircle className="h-5 w-5 text-red-600 dark:text-red-400" />;
  };

  const getDifficultyColor = (met: boolean, hasData: boolean) => {
    if (!hasData) {
      return "bg-muted/50 border-border";
    }

    return met 
      ? "bg-green-100 dark:bg-green-900/30 border-green-300 dark:border-green-700"
      : "bg-red-100 dark:bg-red-900/30 border-red-300 dark:border-red-700";
  };

  return (
    <motion.div
      variants={fadeInUp}
      initial="hidden"
      animate="visible"
      className="h-full"
    >
      <Card className="h-full min-h-[400px] flex flex-col dashboard-card dashboard-card-violet dashboard-soft-grid group border-2 border-indigo-300/80 dark:border-indigo-700 overflow-hidden relative shadow-xl shadow-indigo-500/20 hover:shadow-2xl hover:shadow-indigo-500/35 transition-all duration-300">
        {/* Decorative gradient orbs */}
        <div className="pointer-events-none absolute -top-10 -right-10 w-40 h-40 bg-gradient-to-br from-indigo-400/30 to-purple-400/30 rounded-full blur-3xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
        <div className="pointer-events-none absolute -bottom-10 -left-10 w-32 h-32 bg-gradient-to-tr from-purple-400/20 to-indigo-400/20 rounded-full blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
        
        <CardHeader className="relative pb-4">
          <CardTitle className="flex items-center gap-3 text-xl font-bold">
            <motion.div
              className="flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-to-br from-indigo-500 to-purple-500 shadow-lg shadow-indigo-500/30"
              whileHover={{ rotate: 10, scale: 1.05 }}
              transition={{ duration: 0.2 }}
            >
              <Clock className="h-6 w-6 text-white" />
            </motion.div>
            <div>
              <span className="text-gradient-purple-pink text-xl">
                Time Performance
              </span>
              <p className="text-sm text-muted-foreground font-normal mt-1">
                Average solve time versus target by difficulty
              </p>
            </div>
          </CardTitle>
        </CardHeader>
        
        <CardContent className="relative flex-1 flex flex-col justify-center gap-3">
          {/* Easy */}
          <motion.div
            className={`dashboard-metric-pill p-4 rounded-lg border ${getDifficultyColor(stats.easy.met, stats.easy.hasData)}`}
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.2 }}
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3">
                {getDifficultyIcon(stats.easy.met)}
                <div>
                  <p className="font-semibold text-foreground">Easy</p>
                  <p className="text-xs text-muted-foreground">{stats.easy.count} problems</p>
                </div>
              </div>
              <div className="text-right">
                <p className="text-xl font-bold text-foreground">
                  {stats.easy.hasData ? `${Math.round(stats.easy.avg)} min` : "N/A"}
                </p>
                <p className="text-xs text-muted-foreground">
                  {stats.easy.hasData
                    ? `Target: ≤${BENCHMARKS.Easy} min ${stats.easy.met ? "✅" : "⚠️"}`
                    : "Solve at least one Easy problem"}
                </p>
              </div>
            </div>
          </motion.div>

          {/* Medium */}
          <motion.div
            className={`dashboard-metric-pill p-4 rounded-lg border ${getDifficultyColor(stats.medium.met, stats.medium.hasData)}`}
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.3 }}
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3">
                {getDifficultyIcon(stats.medium.met)}
                <div>
                  <p className="font-semibold text-foreground">Medium</p>
                  <p className="text-xs text-muted-foreground">{stats.medium.count} problems</p>
                </div>
              </div>
              <div className="text-right">
                <p className="text-xl font-bold text-foreground">
                  {stats.medium.hasData ? `${Math.round(stats.medium.avg)} min` : "N/A"}
                </p>
                <p className="text-xs text-muted-foreground">
                  {stats.medium.hasData
                    ? `Target: ≤${BENCHMARKS.Medium} min ${stats.medium.met ? "✅" : "❌"}`
                    : "Solve at least one Medium problem"}
                </p>
              </div>
            </div>
          </motion.div>

          {/* Hard */}
          <motion.div
            className={`dashboard-metric-pill p-4 rounded-lg border ${getDifficultyColor(stats.hard.met, stats.hard.hasData)}`}
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.4 }}
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3">
                {getDifficultyIcon(stats.hard.met)}
                <div>
                  <p className="font-semibold text-foreground">Hard</p>
                  <p className="text-xs text-muted-foreground">{stats.hard.count} problems</p>
                </div>
              </div>
              <div className="text-right">
                <p className="text-xl font-bold text-foreground">
                  {stats.hard.hasData ? `${Math.round(stats.hard.avg)} min` : "N/A"}
                </p>
                <p className="text-xs text-muted-foreground">
                  {stats.hard.hasData
                    ? `Target: ≤${BENCHMARKS.Hard} min ${stats.hard.met ? "✅" : "❌"}`
                    : "Solve at least one Hard problem"}
                </p>
              </div>
            </div>
          </motion.div>

        </CardContent>
      </Card>
    </motion.div>
  );
}
