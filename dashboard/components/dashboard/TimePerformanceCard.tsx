"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Timer, CheckCircle, AlertCircle, XCircle } from "lucide-react";
import { fadeInUp } from "@/lib/animations";

interface TimeStats {
  easy: { avg: number; count: number; met: boolean };
  medium: { avg: number; count: number; met: boolean };
  hard: { avg: number; count: number; met: boolean };
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
            met: (difficulties.easy?.avgTimeMinutes ?? 0) <= BENCHMARKS.Easy,
          },
          medium: {
            avg: Math.round(difficulties.medium?.avgTimeMinutes ?? 0),
            count: difficulties.medium?.solved ?? 0,
            met: (difficulties.medium?.avgTimeMinutes ?? 0) <= BENCHMARKS.Medium,
          },
          hard: {
            avg: Math.round(difficulties.hard?.avgTimeMinutes ?? 0),
            count: difficulties.hard?.solved ?? 0,
            met: (difficulties.hard?.avgTimeMinutes ?? 0) <= BENCHMARKS.Hard,
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
    return (
      <Card className="bg-gradient-to-br from-indigo-50 to-purple-50 dark:from-indigo-950/20 dark:to-purple-950/20 border-indigo-200 dark:border-indigo-800">
        <CardHeader>
          <Skeleton className="h-6 w-44" />
        </CardHeader>
        <CardContent className="space-y-3">
          {[1, 2, 3].map((i) => (
            <Skeleton key={i} className="h-16 w-full" />
          ))}
        </CardContent>
      </Card>
    );
  }

  if (error || !stats) {
    return (
      <Card className="bg-gradient-to-br from-red-50 to-orange-50 dark:from-red-950/20 dark:to-orange-950/20 border-red-200 dark:border-red-800">
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

  const getDifficultyColor = (met: boolean) => {
    return met 
      ? "bg-green-100 dark:bg-green-900/30 border-green-300 dark:border-green-700"
      : "bg-red-100 dark:bg-red-900/30 border-red-300 dark:border-red-700";
  };

  return (
    <motion.div
      variants={fadeInUp}
      initial="hidden"
      animate="visible"
      whileHover={{ scale: 1.02, transition: { duration: 0.2 } }}
    >
      <Card className="bg-gradient-to-br from-indigo-100 via-violet-50 to-purple-100 dark:from-indigo-950/40 dark:via-violet-900/20 dark:to-purple-950/40 border-2 border-indigo-300 dark:border-indigo-700 overflow-hidden relative shadow-xl shadow-indigo-500/20 hover:shadow-2xl hover:shadow-indigo-500/30 transition-shadow duration-300">
        {/* Decorative gradient orbs */}
        <div className="absolute -top-10 -right-10 w-40 h-40 bg-gradient-to-br from-indigo-400/30 to-purple-400/30 rounded-full blur-3xl" />
        <div className="absolute -bottom-10 -left-10 w-32 h-32 bg-gradient-to-tr from-purple-400/20 to-indigo-400/20 rounded-full blur-2xl" />
        
        <CardHeader className="relative">
          <CardTitle className="flex items-center gap-2 text-lg font-semibold">
            <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-indigo-500 to-purple-500 shadow-lg shadow-indigo-500/30">
              <Timer className="h-4 w-4 text-white" />
            </div>
            <span className="bg-gradient-to-r from-indigo-700 to-purple-600 bg-clip-text text-transparent">
              Time Performance
            </span>
          </CardTitle>
        </CardHeader>
        
        <CardContent className="relative space-y-3">
          {/* Easy */}
          <motion.div
            className={`p-4 rounded-lg border ${getDifficultyColor(stats.easy.met)}`}
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
                  {Math.round(stats.easy.avg)} min
                </p>
                <p className="text-xs text-muted-foreground">
                  Target: ≤{BENCHMARKS.Easy} min {stats.easy.met ? "✅" : "⚠️"}
                </p>
              </div>
            </div>
          </motion.div>

          {/* Medium */}
          <motion.div
            className={`p-4 rounded-lg border ${getDifficultyColor(stats.medium.met)}`}
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
                  {Math.round(stats.medium.avg)} min
                </p>
                <p className="text-xs text-muted-foreground">
                  Target: ≤{BENCHMARKS.Medium} min {stats.medium.met ? "✅" : "❌"}
                </p>
              </div>
            </div>
          </motion.div>

          {/* Hard */}
          <motion.div
            className={`p-4 rounded-lg border ${getDifficultyColor(stats.hard.met)}`}
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
                  {Math.round(stats.hard.avg)} min
                </p>
                <p className="text-xs text-muted-foreground">
                  Target: ≤{BENCHMARKS.Hard} min {stats.hard.met ? "✅" : "❌"}
                </p>
              </div>
            </div>
          </motion.div>

          {/* Summary */}
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.6 }}
            className="mt-4 p-3 rounded-lg bg-indigo-100/50 dark:bg-indigo-900/20 border border-indigo-200 dark:border-indigo-800"
          >
            <p className="text-xs text-center text-muted-foreground">
              💡 Meeting time benchmarks indicates interview-ready speed
            </p>
          </motion.div>
        </CardContent>
      </Card>
    </motion.div>
  );
}
