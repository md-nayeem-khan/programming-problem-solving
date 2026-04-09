"use client";

import { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Progress } from "@/components/ui/progress";
import { Calendar, Clock, CheckCircle2, AlertCircle, Target, Award, Star } from "lucide-react";
import { fadeInUp, springBounce, goalCelebration, achievementBadge } from "@/lib/animations";
import { GlassCard } from "@/components/ui/glass-card";
import { AnimatedCounter, CountUpStats } from "@/components/ui/animated-counter";
import { ProgressRing } from "@/components/ui/progress-ring";

interface DailyProgressData {
  problemsSolved: number;
  timeSpentHours: number;
  dailyGoal: number;
}

export function DailyProgressCard() {
  const [data, setData] = useState<DailyProgressData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showCelebration, setShowCelebration] = useState(false);

  useEffect(() => {
    async function fetchDailyProgress() {
      try {
        const [statsRes, streakRes] = await Promise.all([
          fetch("/api/analytics/stats"),
          fetch("/api/analytics/streak?days=30"),
        ]);

        if (!statsRes.ok || !streakRes.ok) {
          throw new Error("Failed to fetch daily progress");
        }

        await statsRes.json();
        const streak = await streakRes.json();

        const today = new Date().toISOString().split("T")[0];
        const todayActivity = streak.calendar?.find((day: any) => 
          day.date.startsWith(today)
        );

        const progressData = {
          problemsSolved: todayActivity?.count || 0,
          timeSpentHours: todayActivity?.totalTime ? todayActivity.totalTime / 3600 : 0,
          dailyGoal: 2,
        };

        setData(progressData);
        
        // Show celebration if goal is met
        if (progressData.problemsSolved >= progressData.dailyGoal && progressData.problemsSolved > 0) {
          setTimeout(() => setShowCelebration(true), 1500);
        }
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load data");
      } finally {
        setLoading(false);
      }
    }

    fetchDailyProgress();
  }, []);

  if (loading) {
    return (
      <GlassCard variant="default" className="min-h-[400px] bg-white/80">
        <CardHeader>
          <Skeleton className="h-8 w-32" />
          <Skeleton className="h-4 w-48" />
        </CardHeader>
        <CardContent className="space-y-6">
          <div className="grid grid-cols-2 gap-4">
            <Skeleton className="h-24 w-full" />
            <Skeleton className="h-24 w-full" />
          </div>
          <Skeleton className="h-20 w-full" />
          <Skeleton className="h-16 w-full" />
        </CardContent>
      </GlassCard>
    );
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

  const goalProgress = (data.problemsSolved / data.dailyGoal) * 100;
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
          <CardTitle className="flex items-center gap-3 text-xl font-bold">
            <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-blue-cyan">
              <Calendar className="h-6 w-6 text-white" />
            </div>
            <div>
              <span className="text-gradient-blue-cyan text-xl">
                Today's Progress
              </span>
              <p className="text-sm text-muted-foreground font-normal mt-1">
                Daily activity and streaks
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
                <div className="dashboard-metric-pill flex items-center gap-2 px-4 py-2 rounded-full bg-gradient-green-emerald text-white font-bold shadow-glow-green border-green-100/40">
                  <CheckCircle2 className="h-4 w-4" />
                  Goal Achieved!
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

        {/* Goal Celebration Overlay */}
        <AnimatePresence>
          {showCelebration && goalMet && (
            <motion.div
              className="absolute inset-0 flex items-center justify-center bg-black/20 backdrop-blur-sm z-10"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              onClick={() => setShowCelebration(false)}
            >
              <motion.div
                variants={goalCelebration}
                initial="hidden"
                animate="visible"
                className="text-center space-y-4"
              >
                <div className="text-6xl">🎉</div>
                <div>
                  <h3 className="text-2xl font-bold text-gradient-green-emerald mb-2">
                    Daily Goal Achieved!
                  </h3>
                  <p className="text-muted-foreground">
                    Great work completing {data.problemsSolved} problems today!
                  </p>
                </div>
                <div className="flex justify-center gap-2">
                  {[...Array(3)].map((_, i) => (
                    <motion.div
                      key={i}
                      initial={{ scale: 0, rotate: -180 }}
                      animate={{ scale: 1, rotate: 0 }}
                      transition={{ 
                        delay: i * 0.1 + 0.5,
                        type: "spring",
                        stiffness: 400
                      }}
                    >
                      <Award className="h-6 w-6 text-golden-yellow" />
                    </motion.div>
                  ))}
                </div>
              </motion.div>
            </motion.div>
          )}
        </AnimatePresence>
      </GlassCard>
    </motion.div>
  );
}
