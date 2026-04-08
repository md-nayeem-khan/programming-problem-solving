"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";
import { Target, TrendingUp, AlertCircle, Zap, Award } from "lucide-react";
import { fadeInUp, springBounce, glowPulse } from "@/lib/animations";
import { ProgressRing } from "@/components/ui/progress-ring";
import { AnimatedCounter, CountUpStats } from "@/components/ui/animated-counter";
import { GlassCard } from "@/components/ui/glass-card";

interface ReadinessData {
  score: number;
  status: "Ready" | "Almost Ready" | "Not Ready";
  totalProblems: number;
  perfectSolves: number;
  avgTime: number;
  tips: string[];
}

export function ReadinessCard() {
  const [data, setData] = useState<ReadinessData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchReadiness() {
      try {
        const response = await fetch("/api/analytics/readiness");
        if (!response.ok) throw new Error("Failed to fetch readiness data");
        const result = await response.json();
        
        setData({
          score: result.readinessScore.score,
          status: result.readinessScore.score >= 0.8 ? "Ready" : 
                  result.readinessScore.score >= 0.6 ? "Almost Ready" : "Not Ready",
          totalProblems: result.metrics.totalProblems,
          perfectSolves: result.readinessScore.breakdown.perfectSolves,
          avgTime: result.metrics.avgTimeMinutes,
          tips: result.recommendations || [],
        });
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load data");
      } finally {
        setLoading(false);
      }
    }

    fetchReadiness();
  }, []);

  if (loading) {
    return (
      <GlassCard variant="default" className="min-h-[400px] bg-white/80">
        <CardHeader>
          <Skeleton className="h-8 w-48" />
          <Skeleton className="h-4 w-32" />
        </CardHeader>
        <CardContent className="space-y-6">
          <Skeleton className="h-40 w-40 rounded-full mx-auto" />
          <div className="grid grid-cols-2 gap-4">
            <Skeleton className="h-16 w-full" />
            <Skeleton className="h-16 w-full" />
          </div>
          <Skeleton className="h-20 w-full" />
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
            Error Loading Readiness
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">{error || "No data available"}</p>
        </CardContent>
      </GlassCard>
    );
  }

  const scorePercentage = Math.round((data.score ?? 0) * 100);
  const isReady = data.status === "Ready";
  const isAlmostReady = data.status === "Almost Ready";
  
  const statusConfig = {
    "Ready": {
      color: "text-vibrant-green",
      bgColor: "bg-vibrant-green/20 border-vibrant-green/30",
      icon: "🚀",
      glowColor: "shadow-glow-green",
      ringVariant: "green" as const,
    },
    "Almost Ready": {
      color: "text-golden-yellow",
      bgColor: "bg-golden-yellow/20 border-golden-yellow/30",
      icon: "⚡",
      glowColor: "shadow-glow-orange",
      ringVariant: "orange" as const,
    },
    "Not Ready": {
      color: "text-coral-red",
      bgColor: "bg-coral-red/20 border-coral-red/30", 
      icon: "🎯",
      glowColor: "shadow-glow",
      ringVariant: "purple" as const,
    },
  };

  const config = statusConfig[data.status];

  return (
    <motion.div
      variants={fadeInUp}
      initial="hidden"
      animate="visible"
      className="group"
    >
      <GlassCard 
        variant="default" 
        className="min-h-[400px] bg-white/80 transition-all duration-300"
        hover={true}
      >
        {/* Header */}
        <CardHeader className="relative pb-4">
          <CardTitle className="flex items-center gap-3 text-xl font-bold">
            <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-purple-pink">
              <Target className="h-6 w-6 text-white" />
            </div>
            <div>
              <span className="text-gradient-purple-pink text-xl">
                Interview Readiness
              </span>
              <p className="text-sm text-muted-foreground font-normal mt-1">
                Your interview preparation score
              </p>
            </div>
          </CardTitle>
        </CardHeader>
        
        <CardContent className="space-y-8">
          {/* Main Score Display */}
          <div className="flex flex-col items-center justify-center">
            <motion.div
              initial={{ scale: 0.8, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              transition={{ duration: 0.6, delay: 0.4 }}
            >
              <ProgressRing
                value={scorePercentage}
                size="xl"
                variant={config.ringVariant}
                glow={true}
                animated={true}
              >
                <div className="text-center">
                  <AnimatedCounter
                    value={scorePercentage}
                    suffix="%"
                    duration={1.5}
                    delay={0.8}
                    gradient={true}
                    size="xl"
                  />
                  <p className="text-xs text-muted-foreground mt-1">Score</p>
                </div>
              </ProgressRing>
            </motion.div>

            {/* Status Badge */}
            <motion.div
              initial={{ scale: 0, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              transition={{ 
                type: "spring", 
                stiffness: 500, 
                damping: 25,
                delay: 1.2 
              }}
            >
              <Badge 
                className={`${config.bgColor} ${config.color} text-base px-6 py-2 font-bold border-2 mt-6 ${config.glowColor}`}
              >
                <span className="mr-2 text-lg">{config.icon}</span>
                {data.status}
                {isReady && <Award className="ml-2 h-4 w-4" />}
              </Badge>
            </motion.div>
          </div>

          {/* Stats Grid */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 1.4 }}
            className="relative"
          >
            <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/10 to-transparent h-px top-0" />
            
            <CountUpStats
              stats={[
                {
                  label: "Total Solved",
                  value: data.totalProblems,
                  color: "purple"
                },
                {
                  label: "Perfect Solves",
                  value: data.perfectSolves,
                  color: "green"
                },
                {
                  label: "Avg Time",
                  value: data.avgTime,
                  suffix: " min",
                  decimal: 1,
                  color: "blue"
                },
                {
                  label: "Success Rate",
                  value: data.totalProblems > 0 ? (data.perfectSolves / data.totalProblems * 100) : 0,
                  suffix: "%",
                  color: "orange"
                }
              ]}
              className="pt-6"
              staggerDelay={0.1}
            />
          </motion.div>

          {/* Tips Section */}
          {data.tips.length > 0 && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 1.8 }}
              className="relative"
            >
              <GlassCard variant="default" size="sm" className="border-white/30">
                <div className="flex items-start gap-3">
                  <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-blue-cyan shrink-0 shadow-glow-blue">
                    <TrendingUp className="h-4 w-4 text-white" />
                  </div>
                  <div className="flex-1">
                    <h4 className="text-sm font-semibold text-gradient-blue-cyan mb-3">
                      Improvement Tips:
                    </h4>
                    <ul className="space-y-2">
                      {data.tips.slice(0, 3).map((tip, index) => (
                        <motion.li
                          key={index}
                          initial={{ opacity: 0, x: -10 }}
                          animate={{ opacity: 1, x: 0 }}
                          transition={{ delay: 2 + index * 0.1 }}
                          className="text-xs text-muted-foreground flex items-start gap-2"
                        >
                          <div className="h-1 w-1 rounded-full bg-electric-cyan mt-2 shrink-0" />
                          <span>{tip}</span>
                        </motion.li>
                      ))}
                    </ul>
                  </div>
                </div>
              </GlassCard>
            </motion.div>
          )}

          {/* Ready Celebration */}
          {isReady && (
            <motion.div
              initial={{ scale: 0, rotate: -180 }}
              animate={{ scale: 1, rotate: 0 }}
              transition={{ 
                type: "spring", 
                stiffness: 300, 
                damping: 20,
                delay: 2.5 
              }}
              className="text-center"
            >
              <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-gradient-green-emerald text-white text-sm font-semibold shadow-glow-green">
                <Zap className="h-4 w-4" />
                Ready to apply! 
                <Award className="h-4 w-4" />
              </div>
            </motion.div>
          )}
        </CardContent>
      </GlassCard>
    </motion.div>
  );
}
