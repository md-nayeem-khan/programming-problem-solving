"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";
import { Target, AlertCircle, Zap, Award } from "lucide-react";
import { fadeInUp } from "@/lib/animations";
import { ProgressRing } from "@/components/ui/progress-ring";
import { AnimatedCounter } from "@/components/ui/animated-counter";
import { GlassCard } from "@/components/ui/glass-card";

interface ReadinessData {
  score: number;
  status: "Ready" | "Almost Ready" | "Not Ready";
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
  const useGradientStatusText = data.status !== "Ready";
  const statusConfig = {
    "Ready": {
      color: "text-vibrant-green",
      bgColor: "bg-vibrant-green/20 border-vibrant-green/30",
      icon: "🚀",
      glowColor: "shadow-glow-green",
      ringVariant: "purple" as const,
    },
    "Almost Ready": {
      color: "",
      bgColor: "bg-fuchsia-100/85 border-fuchsia-300/60",
      icon: "⚡",
      glowColor: "",
      ringVariant: "purple" as const,
    },
    "Not Ready": {
      color: "",
      bgColor: "bg-fuchsia-100/85 border-fuchsia-300/60", 
      icon: "🎯",
      glowColor: "",
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
        className="dashboard-card dashboard-card-violet dashboard-soft-grid group min-h-[400px] border-fuchsia-200/60 shadow-xl shadow-fuchsia-500/20 transition-all duration-300"
        hover={true}
      >
        <div className="pointer-events-none absolute -top-10 -right-8 h-24 w-24 rounded-full bg-fuchsia-400/20 blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
        <div className="pointer-events-none absolute -bottom-10 -left-8 h-24 w-24 rounded-full bg-violet-400/20 blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />

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
                className={`${config.bgColor} ${config.color} ${useGradientStatusText ? "text-sm px-4 py-1.5 font-semibold rounded-lg shadow-none" : "dashboard-metric-pill text-base px-6 py-2 font-bold"} border-2 mt-6 ${config.glowColor}`}
              >
                {!useGradientStatusText && <span className="mr-2 text-lg">{config.icon}</span>}
                {useGradientStatusText ? <span className="text-gradient-purple-pink">{data.status}</span> : data.status}
                {isReady && <Award className="ml-2 h-4 w-4" />}
              </Badge>
            </motion.div>
          </div>

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
