"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { CalendarCheck2, CheckCircle2, AlertCircle, Star } from "lucide-react";
import { fadeInUp } from "@/lib/animations";
import { GlassCard } from "@/components/ui/glass-card";
import { AnimatedCounter, CountUpStats } from "@/components/ui/animated-counter";
import { ProgressRing } from "@/components/ui/progress-ring";
import { DashboardMainCardSkeleton } from "@/components/dashboard/DashboardSkeletons";

interface DailyProgressData {
  problemsSolved: number;
  timeSpentHours: number;
  dailyGoal: number;
}

export function DailyProgressCard() {
  const [data, setData] = useState<DailyProgressData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchDailyProgress() {
      try {
        const response = await fetch("/api/analytics/daily-progress?days=1&streak=true");

        if (!response.ok) {
          throw new Error("Failed to fetch daily progress");
        }

        const result = await response.json();
        const todayActivity = Array.isArray(result.data) ? result.data[0] : null;

        const progressData = {
          problemsSolved: todayActivity?.problemsSolved || 0,
          timeSpentHours: (todayActivity?.totalTimeMinutes || 0) / 60,
          dailyGoal: 2,
        };

        setData(progressData);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load data");
      } finally {
        setLoading(false);
      }
    }

    fetchDailyProgress();
  }, []);

  if (loading) {
    return <DashboardMainCardSkeleton rows={4} />;
  }

  if (error || !data) {
    return (
      <GlassCard variant="default" className="min-h-[400px] bg-white/80">
        <CardHeader>
          <CardTitle className="flex items-center gap-3 text-coral-red">
            <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-coral-red/20">
              <AlertCircle className="h-5 w-5" />
            </div>
            Error Loading Progress
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">{error || "No data available"}</p>
        </CardContent>
      </GlassCard>
    );
  }

  const goalProgressRaw = (data.problemsSolved / data.dailyGoal) * 100;
  const goalProgress = Math.min(goalProgressRaw, 100);
  const goalMet = data.problemsSolved >= data.dailyGoal;

  return (
    <motion.div
      variants={fadeInUp}
      initial="hidden"
      animate="visible"
      className="group relative"
    >
      <GlassCard
        variant="default"
        className="dashboard-card dashboard-card-sky dashboard-soft-grid group min-h-[400px] border-blue-200/60 shadow-xl shadow-blue-500/20 overflow-hidden"
        hover={true}
      >
        <div className="pointer-events-none absolute -top-10 -right-8 h-24 w-24 rounded-full bg-cyan-400/20 blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
        <div className="pointer-events-none absolute -bottom-10 -left-8 h-24 w-24 rounded-full bg-blue-400/20 blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />

        {/* Header */}
        <CardHeader className="pb-4">
          <CardTitle className="-ml-6 flex items-center gap-3 text-xl font-bold">
            <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-2xl bg-gradient-blue-cyan">
              <CalendarCheck2 className="h-6 w-6 text-white" />
            </div>
            <div>
              <span className="text-gradient-blue-cyan text-xl">
                Today's Progress
              </span>
              <p className="text-sm text-muted-foreground font-normal mt-1">
                Problems solved and time invested today
              </p>
            </div>
          </CardTitle>
        </CardHeader>
        
        <CardContent className="space-y-6">
          {/* Main Progress Ring */}
          <div className="flex flex-col items-center">
            <motion.div
              initial={{ scale: 0.8, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              transition={{ duration: 0.6, delay: 0.4 }}
            >
              <ProgressRing
                value={goalProgress}
                size="lg"
                variant="blue"
                glow={true}
                animated={true}
              >
                <div className="text-center">
                  <AnimatedCounter
                    value={data.problemsSolved}
                    duration={1.2}
                    delay={0.8}
                    gradient={false}
                    className="text-gradient-blue-cyan"
                    size="lg"
                  />
                  <p className="text-xs text-muted-foreground mt-1">
                    of {data.dailyGoal} problems
                  </p>
                </div>
              </ProgressRing>
            </motion.div>

            {/* Goal Status */}
            <motion.div
              initial={{ scale: 0, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              transition={{ 
                type: "spring", 
                stiffness: 500, 
                damping: 25,
                delay: 1.2 
              }}
              className="mt-4"
            >
              {goalMet && (
                <div className="dashboard-metric-pill flex items-center gap-2 px-4 py-2 rounded-full bg-gradient-green-emerald font-bold shadow-glow-green border-green-100/40">
                  <CheckCircle2 className="h-4 w-4" />
                  <span className="text-gradient-blue-cyan">Goal Achieved!</span>
                  <Star className="h-4 w-4" />
                </div>
              )}
            </motion.div>
          </div>

          {/* Stats Grid */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 1.4 }}
          >
            <CountUpStats
              stats={[
                {
                  label: "Hours Today",
                  value: data.timeSpentHours,
                  suffix: "h",
                  decimal: 1,
                  color: "blueGradient"
                },
                {
                  label: "Completion Rate",
                  value: goalProgress,
                  suffix: "%",
                  color: "blueGradient"
                }
              ]}
              className="grid-cols-2"
              staggerDelay={0.1}
            />
          </motion.div>

        </CardContent>
      </GlassCard>
    </motion.div>
  );
}
