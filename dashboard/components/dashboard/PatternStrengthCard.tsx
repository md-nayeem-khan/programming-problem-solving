"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Badge } from "@/components/ui/badge";
import { Brain, AlertCircle, TrendingUp, Zap, Star } from "lucide-react";
import { fadeInUp, staggerContainer, staggerItem, springBounce, shimmer } from "@/lib/animations";
import { GlassCard } from "@/components/ui/glass-card";
import { AnimatedCounter } from "@/components/ui/animated-counter";

interface PatternStat {
  pattern: string;
  solved: number;
  avgTime: number;
  confidence: "Weak" | "Medium" | "Strong";
  hintUsageRate: number;
}

export function PatternStrengthCard() {
  const [patterns, setPatterns] = useState<PatternStat[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [hoveredPattern, setHoveredPattern] = useState<string | null>(null);

  useEffect(() => {
    async function fetchPatterns() {
      try {
        const response = await fetch("/api/analytics/patterns/confidence");
        if (!response.ok) throw new Error("Failed to fetch pattern data");
        
        const data = await response.json();
        
        // Transform API data to pattern stats
        const patternStats: PatternStat[] = data.patterns.map((pattern: any) => ({
          pattern: pattern.pattern,
          solved: pattern.totalSolved,
          avgTime: Math.round(pattern.avgTimeSeconds / 60), // Convert to minutes
          confidence: pattern.confidence,
          hintUsageRate: pattern.hintUsageRate,
        }));
        
        setPatterns(patternStats.sort((a, b) => {
          const order = { "Strong": 0, "Medium": 1, "Weak": 2 };
          return order[a.confidence] - order[b.confidence];
        }));
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load pattern data");
      } finally {
        setLoading(false);
      }
    }

    fetchPatterns();
  }, []);

  if (loading) {
    return (
      <GlassCard variant="default" className="min-h-[400px] bg-white/80">
        <CardHeader>
          <Skeleton className="h-8 w-40" />
          <Skeleton className="h-4 w-32" />
        </CardHeader>
        <CardContent className="space-y-4">
          {[1, 2, 3, 4].map((i) => (
            <Skeleton key={i} className="h-16 w-full" />
          ))}
        </CardContent>
      </GlassCard>
    );
  }

  if (error) {
    return (
      <GlassCard variant="default" className="min-h-[400px] bg-white/80">
        <CardHeader>
          <CardTitle className="flex items-center gap-3 text-coral-red">
            <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-coral-red/20">
              <AlertCircle className="h-5 w-5" />
            </div>
            Error Loading Patterns
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">{error}</p>
        </CardContent>
      </GlassCard>
    );
  }

  if (patterns.length === 0) {
    return (
      <GlassCard variant="default" className="min-h-[400px] bg-white/80">
        <CardHeader>
          <CardTitle className="flex items-center gap-3 text-lg font-bold">
            <motion.div 
              className="flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-green-emerald shadow-glow-green"
              variants={springBounce}
              initial="hidden"
              animate="visible"
            >
              <Brain className="h-6 w-6 text-white" />
            </motion.div>
            <div>
              <span className="text-gradient-green-emerald text-xl">
                Pattern Strength
              </span>
              <p className="text-sm text-muted-foreground font-normal mt-1">
                Pattern mastery analysis
              </p>
            </div>
          </CardTitle>
        </CardHeader>
        <CardContent className="text-center py-12">
          <motion.div
            variants={springBounce}
            initial="hidden"
            animate="visible"
            transition={{ delay: 0.3 }}
            className="space-y-4"
          >
            <Brain className="h-16 w-16 text-muted-foreground mx-auto opacity-50" />
            <div>
              <p className="text-lg font-semibold text-muted-foreground">No patterns practiced yet</p>
              <p className="text-sm text-muted-foreground mt-2">Start solving problems to see pattern analysis</p>
            </div>
          </motion.div>
        </CardContent>
      </GlassCard>
    );
  }

  const getConfidenceConfig = (confidence: string) => {
    switch (confidence) {
      case "Strong": 
        return {
          color: "bg-vibrant-green",
          percentage: 90,
          badge: "bg-vibrant-green/20 text-vibrant-green border-vibrant-green/30",
          icon: "💚",
          glow: "shadow-glow-green"
        };
      case "Medium": 
        return {
          color: "bg-golden-yellow",
          percentage: 60,
          badge: "bg-golden-yellow/20 text-golden-yellow border-golden-yellow/30",
          icon: "💛",
          glow: "shadow-glow-orange"
        };
      case "Weak": 
        return {
          color: "bg-coral-red",
          percentage: 30,
          badge: "bg-coral-red/20 text-coral-red border-coral-red/30",
          icon: "❤️",
          glow: "shadow-glow"
        };
      default: 
        return {
          color: "bg-gray-500",
          percentage: 0,
          badge: "bg-gray-100 text-gray-700 border-gray-300",
          icon: "⚪",
          glow: ""
        };
    }
  };

  const strongCount = patterns.filter(p => p.confidence === "Strong").length;
  const totalPatterns = patterns.length;

  return (
    <motion.div
      variants={fadeInUp}
      initial="hidden"
      animate="visible"
      className="group"
    >
      <GlassCard
        variant="default"
        className="dashboard-card dashboard-card-teal dashboard-soft-grid group min-h-[400px] border-emerald-200/60 shadow-xl shadow-emerald-500/20"
        hover={true}
      >
        <div className="pointer-events-none absolute -top-10 -right-8 h-24 w-24 rounded-full bg-emerald-400/20 blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />

        {/* Header */}
        <CardHeader className="relative pb-4">
          <motion.div
            variants={springBounce}
            initial="hidden"
            animate="visible"
            transition={{ delay: 0.2 }}
          >
            <CardTitle className="flex items-center gap-3 text-xl font-bold">
              <motion.div 
                className="flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-green-emerald shadow-glow-green"
                variants={strongCount >= 3 ? springBounce : undefined}
                animate={strongCount >= 3 ? "visible" : undefined}
              >
                <Brain className="h-6 w-6 text-white" />
              </motion.div>
              <div>
                <span className="text-gradient-green-emerald text-xl">
                  Pattern Strength
                </span>
                <p className="text-sm text-muted-foreground font-normal mt-1">
                  {strongCount}/{totalPatterns} patterns mastered
                </p>
              </div>
            </CardTitle>
          </motion.div>
        </CardHeader>
        
        <CardContent className="space-y-6">
          {/* Pattern List */}
          <motion.div
            variants={staggerContainer}
            initial="hidden"
            animate="visible"
            className="space-y-4"
          >
            {patterns.slice(0, 6).map((pattern, index) => {
              const config = getConfidenceConfig(pattern.confidence);
              const isHovered = hoveredPattern === pattern.pattern;
              
              return (
                <motion.div
                  key={pattern.pattern}
                  variants={staggerItem}
                  className="group/item relative"
                  onHoverStart={() => setHoveredPattern(pattern.pattern)}
                  onHoverEnd={() => setHoveredPattern(null)}
                >
                  {/* Pattern Row */}
                  <motion.div
                    className="relative p-4 rounded-xl transition-all duration-300"
                    style={{
                      background: isHovered 
                        ? "rgba(255, 255, 255, 0.55)" 
                        : "rgba(255, 255, 255, 0.32)",
                      backdropFilter: isHovered ? "blur(10px)" : "blur(5px)",
                      border: isHovered 
                        ? "1px solid rgba(16, 185, 129, 0.35)" 
                        : "1px solid rgba(255, 255, 255, 0.45)",
                    }}
                    whileHover={{ scale: 1.02, y: -2 }}
                    transition={{ duration: 0.2 }}
                  >
                    {/* Header */}
                    <div className="flex items-center justify-between mb-3">
                      <div className="flex items-center gap-3">
                        <span className="text-lg font-semibold text-foreground">
                          {pattern.pattern}
                        </span>
                        <Badge 
                          variant="outline" 
                          className={`text-xs font-bold border-2 ${config.badge} ${config.glow}`}
                        >
                          {config.icon} {pattern.confidence}
                        </Badge>
                        {pattern.confidence === "Strong" && (
                          <motion.div
                            initial={{ scale: 0, rotate: -180 }}
                            animate={{ scale: 1, rotate: 0 }}
                            transition={{ 
                              type: "spring", 
                              stiffness: 500, 
                              damping: 25,
                              delay: index * 0.1 + 0.5
                            }}
                          >
                            <Star className="h-4 w-4 text-golden-yellow fill-golden-yellow" />
                          </motion.div>
                        )}
                      </div>
                      <div className="text-right">
                        <AnimatedCounter
                          value={pattern.solved}
                          suffix=" solved"
                          className="text-sm text-muted-foreground"
                          delay={index * 0.1 + 0.3}
                        />
                      </div>
                    </div>
                    
                    {/* Progress Bar */}
                    <div className="relative h-3 bg-white/10 rounded-full overflow-hidden">
                      <motion.div
                        className={`h-full rounded-full ${config.color} relative shimmer-enhanced`}
                        initial={{ width: 0 }}
                        animate={{ width: `${config.percentage}%` }}
                        transition={{ 
                          duration: 1.2, 
                          delay: index * 0.1 + 0.4,
                          ease: "easeOut" 
                        }}
                      >
                        {/* Shimmer effect on hover */}
                        <motion.div
                          className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent -skew-x-12"
                          initial={{ x: "-100%" }}
                          animate={isHovered ? { x: "100%" } : { x: "-100%" }}
                          transition={{ duration: 0.8, ease: "easeInOut" }}
                        />
                      </motion.div>
                    </div>

                    {/* Hover Details */}
                    <motion.div
                      className="mt-3 grid grid-cols-2 gap-4 text-xs"
                      initial={{ opacity: 0, height: 0 }}
                      animate={{ 
                        opacity: isHovered ? 1 : 0, 
                        height: isHovered ? "auto" : 0 
                      }}
                      transition={{ duration: 0.3 }}
                    >
                      <div className="space-y-1">
                        <div className="flex justify-between">
                          <span className="text-muted-foreground">Avg Time:</span>
                          <span className={pattern.avgTime > 30 ? "text-coral-red" : "text-vibrant-green"}>
                            {pattern.avgTime} min
                          </span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-muted-foreground">Hint Rate:</span>
                          <span className={(pattern.hintUsageRate ?? 0) > 0.3 ? "text-coral-red" : "text-vibrant-green"}>
                            {Math.round((pattern.hintUsageRate ?? 0) * 100)}%
                          </span>
                        </div>
                      </div>
                      <div className="flex items-center justify-center">
                        {pattern.confidence === "Strong" && (
                          <div className="flex items-center gap-1 text-vibrant-green">
                            <Zap className="h-3 w-3" />
                            <span className="font-semibold">Mastered!</span>
                          </div>
                        )}
                        {pattern.confidence === "Medium" && (
                          <div className="flex items-center gap-1 text-golden-yellow">
                            <TrendingUp className="h-3 w-3" />
                            <span className="font-semibold">Improving</span>
                          </div>
                        )}
                        {pattern.confidence === "Weak" && (
                          <div className="flex items-center gap-1 text-coral-red">
                            <AlertCircle className="h-3 w-3" />
                            <span className="font-semibold">Needs Work</span>
                          </div>
                        )}
                      </div>
                    </motion.div>
                  </motion.div>
                </motion.div>
              );
            })}
          </motion.div>

          {/* Legend */}
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 1.5 }}
            className="flex items-center justify-center gap-6 pt-4 border-t border-white/20"
          >
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 rounded-full bg-vibrant-green shadow-glow-green" />
              <span className="text-xs text-muted-foreground">Strong</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 rounded-full bg-golden-yellow shadow-glow-orange" />
              <span className="text-xs text-muted-foreground">Medium</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 rounded-full bg-coral-red shadow-glow" />
              <span className="text-xs text-muted-foreground">Weak</span>
            </div>
          </motion.div>
        </CardContent>
      </GlassCard>
    </motion.div>
  );
}
