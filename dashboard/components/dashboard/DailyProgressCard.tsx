"use client";

import { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Progress } from "@/components/ui/progress";
import { Calendar, Clock, Flame, CheckCircle2, AlertCircle, Trophy, Target, Zap, Award, Star } from "lucide-react";
import { fadeInUp, springBounce, streakFlame, goalCelebration, achievementBadge } from "@/lib/animations";
import { GlassCard } from "@/components/ui/glass-card";
import { AnimatedCounter, CountUpStats } from "@/components/ui/animated-counter";
import { ProgressRing } from "@/components/ui/progress-ring";

interface DailyProgressData {
  problemsSolved: number;
  timeSpentHours: number;
  streak: number;
  longestStreak: number;
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

        const stats = await statsRes.json();
        const streak = await streakRes.json();

        const today = new Date().toISOString().split("T")[0];
        const todayActivity = streak.calendar?.find((day: any) => 
          day.date.startsWith(today)
        );

        const progressData = {
          problemsSolved: todayActivity?.count || 0,
          timeSpentHours: todayActivity?.totalTime ? todayActivity.totalTime / 3600 : 0,
          streak: streak.currentStreak || 0,
          longestStreak: streak.longestStreak || 0,
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
  const isOnFire = data.streak >= 7; // On fire if streak is 7+ days
  const isNewRecord = data.streak === data.longestStreak && data.streak > 0;

  return (
    <motion.div
      variants={fadeInUp}
      initial="hidden"
      animate="visible"
      className="group relative"
    >
      <GlassCard variant="default" className="min-h-[400px] bg-white/80 overflow-hidden" hover={true}>
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
                    gradient={true}
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
              {goalMet ? (
                <div className="flex items-center gap-2 px-4 py-2 rounded-full bg-gradient-green-emerald text-white font-bold shadow-glow-green">
                  <CheckCircle2 className="h-4 w-4" />
                  Goal Achieved!
                  <Star className="h-4 w-4" />
                </div>
              ) : (
                <div className="flex items-center gap-2 px-4 py-2 rounded-full bg-electric-blue/20 text-electric-blue border-2 border-electric-blue/30">
                  <Target className="h-4 w-4" />
                  {data.dailyGoal - data.problemsSolved} more to go
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
                  color: "blue"
                },
                {
                  label: "Completion Rate",
                  value: goalProgress,
                  suffix: "%",
                  color: "purple"
                }
              ]}
              className="grid-cols-2"
              staggerDelay={0.1}
            />
          </motion.div>

          {/* Streak Display */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 1.8 }}
            className="relative"
          >
            <GlassCard 
              variant={isOnFire ? "orange" : "default"} 
              size="sm" 
              className={`${isOnFire ? 'shadow-glow-orange border-sunset-orange/40' : 'border-white/30'} overflow-hidden`}
            >
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-4">
                  <motion.div
                    variants={isOnFire ? streakFlame : undefined}
                    initial="idle"
                    animate={isOnFire ? "burning" : "idle"}
                    className="relative"
                  >
                    <Flame 
                      className={`h-10 w-10 ${isOnFire ? 'text-sunset-orange' : 'text-muted-foreground'}`}
                    />
                    {isOnFire && (
                      <motion.div
                        className="absolute inset-0 rounded-full bg-sunset-orange/20"
                        animate={{ scale: [1, 1.4, 1], opacity: [0.5, 0, 0.5] }}
                        transition={{ duration: 2, repeat: Infinity }}
                      />
                    )}
                  </motion.div>
                  <div>
                    <div className="flex items-center gap-2">
                      <AnimatedCounter
                        value={data.streak}
                        duration={1}
                        delay={2}
                        className={`text-2xl font-bold ${isOnFire ? 'text-sunset-orange' : 'text-foreground'}`}
                      />
                      <span className={`text-2xl font-bold ${isOnFire ? 'text-sunset-orange' : 'text-foreground'}`}>
                        Days
                      </span>
                      {isOnFire && <Zap className="h-5 w-5 text-sunset-orange" />}
                    </div>
                    <p className="text-xs text-muted-foreground flex items-center gap-1">
                      Current Streak
                      {isNewRecord && (
                        <motion.span
                          initial={{ scale: 0 }}
                          animate={{ scale: 1 }}
                          transition={{ type: "spring", stiffness: 500, delay: 2.5 }}
                          className="text-golden-yellow"
                        >
                          🏆 New Record!
                        </motion.span>
                      )}
                    </p>
                  </div>
                </div>
                <div className="text-right">
                  <div className="flex items-center gap-1">
                    <Trophy className="h-4 w-4 text-golden-yellow" />
                    <AnimatedCounter
                      value={data.longestStreak}
                      duration={0.8}
                      delay={2.2}
                      className="text-lg font-bold text-golden-yellow"
                    />
                  </div>
                  <p className="text-xs text-muted-foreground">Best Streak</p>
                </div>
              </div>
            </GlassCard>
          </motion.div>

          {/* Streak Status Messages */}
          <AnimatePresence>
            {isOnFire && (
              <motion.div
                initial={{ opacity: 0, scale: 0.8 }}
                animate={{ opacity: 1, scale: 1 }}
                exit={{ opacity: 0, scale: 0.8 }}
                transition={{ delay: 2.8 }}
                className="text-center"
              >
                <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-gradient-orange-red text-white text-sm font-semibold shadow-glow-orange">
                  <Flame className="h-4 w-4" />
                  You're on fire! 🔥
                  <Zap className="h-4 w-4" />
                </div>
              </motion.div>
            )}
          </AnimatePresence>
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
