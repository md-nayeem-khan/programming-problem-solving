"use client";

import { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Badge } from "@/components/ui/badge";
import { 
  AlertTriangle, 
  TrendingDown, 
  Clock, 
  HelpCircle, 
  ArrowRight, 
  AlertCircle,
  Flame,
  Target,
  ChevronDown,
  ChevronUp,
  Zap
} from "lucide-react";
import { 
  fadeInUp, 
  staggerContainer, 
  staggerItem, 
  springBounce, 
  pulseGlow,
  wobble 
} from "@/lib/animations";
import { GlassCard } from "@/components/ui/glass-card";
import { GradientButton } from "@/components/ui/gradient-button";

interface Weakness {
  pattern: string;
  reason: string;
  avgTime: number;
  hintUsageRate: number;
  problemCount: number;
  recommendation: string;
}

export function WeakAreasCard() {
  const [weaknesses, setWeaknesses] = useState<Weakness[]>([]);
  const [totalPatternsAnalyzed, setTotalPatternsAnalyzed] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [expandedItems, setExpandedItems] = useState<Set<string>>(new Set());

  useEffect(() => {
    async function fetchWeaknesses() {
      try {
        const response = await fetch("/api/analytics/weaknesses?minProblems=2&timeframe=month");
        if (!response.ok) throw new Error("Failed to fetch weakness data");
        
        const data = await response.json();
        
        const weaknessData: Weakness[] = [];
        
        // Process weak patterns
        if (data.weakPatterns) {
          data.weakPatterns.forEach((pattern: any) => {
            weaknessData.push({
              pattern: pattern.pattern,
              reason: "Slow Completion",
              avgTime: pattern.avgTime || 0,
              hintUsageRate: pattern.hintUsageRate || 0,
              problemCount: pattern.count || 0,
              recommendation: `Practice more ${pattern.pattern} problems to improve speed`,
            });
          });
        }
        
        // Process hint-dependent patterns
        if (data.hintDependentPatterns) {
          data.hintDependentPatterns.forEach((pattern: any) => {
            if (!weaknessData.find(w => w.pattern === pattern.pattern)) {
              weaknessData.push({
                pattern: pattern.pattern,
                reason: "High Hint Usage",
                avgTime: pattern.avgTime || 0,
                hintUsageRate: pattern.hintUsageRate || 0,
                problemCount: pattern.count || 0,
                recommendation: `Focus on understanding ${pattern.pattern} fundamentals`,
              });
            }
          });
        }
        
        // Use recommendations from API if available
        if (data.recommendations && data.recommendations.length > 0) {
          weaknessData.forEach((weakness, index) => {
            if (data.recommendations[index]) {
              weakness.recommendation = data.recommendations[index];
            }
          });
        }
        
        setWeaknesses(weaknessData.slice(0, 4));
        setTotalPatternsAnalyzed(data.summary?.totalPatternsAnalyzed || 0);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load data");
      } finally {
        setLoading(false);
      }
    }

    fetchWeaknesses();
  }, []);

  const toggleExpanded = (pattern: string) => {
    const newExpanded = new Set(expandedItems);
    if (newExpanded.has(pattern)) {
      newExpanded.delete(pattern);
    } else {
      newExpanded.add(pattern);
    }
    setExpandedItems(newExpanded);
  };

  const getPriorityConfig = (weakness: Weakness) => {
    const avgTimeScore = weakness.avgTime > 40 ? 3 : weakness.avgTime > 30 ? 2 : 1;
    const hintScore = weakness.hintUsageRate > 0.5 ? 3 : weakness.hintUsageRate > 0.3 ? 2 : 1;
    const priority = Math.max(avgTimeScore, hintScore);

    switch (priority) {
      case 3:
        return {
          level: "Critical",
          color: "text-coral-red",
          bg: "bg-coral-red/10 border-coral-red/30",
          glow: "shadow-glow animate-pulse-glow",
          icon: <Flame className="h-4 w-4 text-coral-red animate-pulse" />,
          badge: "bg-coral-red/20 text-coral-red border-coral-red/40"
        };
      case 2:
        return {
          level: "High",
          color: "text-sunset-orange",
          bg: "bg-sunset-orange/10 border-sunset-orange/30",
          glow: "shadow-glow-orange",
          icon: <AlertTriangle className="h-4 w-4 text-sunset-orange" />,
          badge: "bg-sunset-orange/20 text-sunset-orange border-sunset-orange/40"
        };
      default:
        return {
          level: "Medium",
          color: "text-golden-yellow",
          bg: "bg-golden-yellow/10 border-golden-yellow/30",
          glow: "shadow-glow-orange",
          icon: <Target className="h-4 w-4 text-golden-yellow" />,
          badge: "bg-golden-yellow/20 text-golden-yellow border-golden-yellow/40"
        };
    }
  };

  if (loading) {
    return (
      <GlassCard variant="default" className="min-h-[400px] bg-white/80">
        <CardHeader>
          <Skeleton className="h-8 w-36" />
          <Skeleton className="h-4 w-32" />
        </CardHeader>
        <CardContent className="space-y-4">
          {[1, 2, 3].map((i) => (
            <Skeleton key={i} className="h-24 w-full" />
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
            <motion.div 
              className="flex h-10 w-10 items-center justify-center rounded-xl bg-coral-red/20"
              variants={wobble}
              animate="wobble"
            >
              <AlertCircle className="h-5 w-5" />
            </motion.div>
            Error Loading Weaknesses
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">{error}</p>
        </CardContent>
      </GlassCard>
    );
  }

  if (weaknesses.length === 0) {
    if (totalPatternsAnalyzed === 0) {
      // No data - database is empty
      return (
        <GlassCard variant="default" className="min-h-[400px] bg-white/80">
          <CardHeader>
            <CardTitle className="flex items-center gap-3 text-lg">
              <motion.div 
                className="flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-to-br from-muted to-muted-foreground/20"
                variants={springBounce}
                initial="hidden"
                animate="visible"
              >
                <AlertTriangle className="h-6 w-6 text-muted-foreground" />
              </motion.div>
              <div>
                <span className="text-muted-foreground text-xl">Weak Areas</span>
                <p className="text-sm text-muted-foreground font-normal mt-1">
                  Identifying improvement opportunities
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
              <AlertTriangle className="h-16 w-16 text-muted-foreground mx-auto opacity-50" />
              <div>
                <p className="text-lg font-semibold text-muted-foreground">No problems solved yet</p>
                <p className="text-sm text-muted-foreground mt-2">Start solving problems to identify areas for improvement</p>
              </div>
            </motion.div>
          </CardContent>
        </GlassCard>
      );
    } else {
      // No weak areas - you're doing great!
      return (
        <GlassCard variant="default" className="min-h-[400px] bg-white/80">
          <CardHeader>
            <CardTitle className="flex items-center gap-3 text-lg">
              <motion.div 
                className="flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-green-emerald shadow-glow-green"
                variants={springBounce}
                initial="hidden"
                animate="visible"
              >
                <Zap className="h-6 w-6 text-white" />
              </motion.div>
              <div>
                <span className="text-gradient-green-emerald text-xl">Weak Areas</span>
                <p className="text-sm text-muted-foreground font-normal mt-1">
                  No weaknesses detected!
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
              className="space-y-6"
            >
              <div className="relative">
                <div className="text-6xl mb-4">🎉</div>
                <motion.div
                  className="absolute inset-0 bg-gradient-to-r from-vibrant-green/20 to-emerald-500/20 rounded-full blur-xl"
                  animate={{ scale: [1, 1.2, 1] }}
                  transition={{ duration: 2, repeat: Infinity }}
                />
              </div>
              <div>
                <p className="text-xl font-bold text-gradient-green-emerald mb-2">
                  Excellent performance!
                </p>
                <p className="text-sm text-muted-foreground">
                  No weak areas detected. Keep up the great work!
                </p>
              </div>
            </motion.div>
          </CardContent>
        </GlassCard>
      );
    }
  }

  return (
    <motion.div
      variants={fadeInUp}
      initial="hidden"
      animate="visible"
      className="group"
    >
      <GlassCard variant="default" className="min-h-[400px] bg-white/80" hover={true}>
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
                className="flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-orange-red shadow-glow-orange"
                variants={pulseGlow}
                animate="pulse"
              >
                <AlertTriangle className="h-6 w-6 text-white" />
              </motion.div>
              <div>
                <span className="text-gradient-orange-red text-xl">
                  Weak Areas
                </span>
                <p className="text-sm text-muted-foreground font-normal mt-1">
                  {weaknesses.length} area{weaknesses.length !== 1 ? 's' : ''} need attention
                </p>
              </div>
            </CardTitle>
          </motion.div>

          {/* Alert decoration */}
          <motion.div 
            className="absolute -top-2 -right-2 w-20 h-20 bg-gradient-to-br from-coral-red/20 to-sunset-orange/20 rounded-full blur-xl"
            animate={{ scale: [1, 1.1, 1] }}
            transition={{ duration: 2, repeat: Infinity }}
          />
        </CardHeader>
        
        <CardContent className="space-y-4">
          {/* Weakness List */}
          <motion.div
            variants={staggerContainer}
            initial="hidden"
            animate="visible"
            className="space-y-4"
          >
            {weaknesses.map((weakness, index) => {
              const config = getPriorityConfig(weakness);
              const isExpanded = expandedItems.has(weakness.pattern);
              
              return (
                <motion.div
                  key={weakness.pattern}
                  variants={staggerItem}
                  className="group/item"
                >
                  <motion.div
                    className={`relative p-4 rounded-xl border-2 ${config.bg} ${config.glow} transition-all duration-300 cursor-pointer`}
                    whileHover={{ scale: 1.02 }}
                    onClick={() => toggleExpanded(weakness.pattern)}
                  >
                    {/* Priority Indicator */}
                    <div className="absolute -top-2 -left-2">
                      <motion.div
                        className={`w-6 h-6 rounded-full ${config.bg} border-2 border-background flex items-center justify-center`}
                        variants={springBounce}
                        initial="hidden"
                        animate="visible"
                        transition={{ delay: index * 0.1 + 0.3 }}
                      >
                        <span className="text-xs font-bold">{index + 1}</span>
                      </motion.div>
                    </div>

                    {/* Header */}
                    <div className="flex items-start justify-between mb-3">
                      <div className="flex items-center gap-3 flex-1">
                        {config.icon}
                        <div>
                          <h4 className="font-bold text-lg text-foreground">
                            {weakness.pattern}
                          </h4>
                          <div className="flex items-center gap-2 mt-1">
                            <Badge className={`text-xs font-bold border-2 ${config.badge}`}>
                              {config.level} Priority
                            </Badge>
                            <Badge variant="outline" className="text-xs">
                              {weakness.reason}
                            </Badge>
                          </div>
                        </div>
                      </div>
                      <motion.div
                        animate={{ rotate: isExpanded ? 180 : 0 }}
                        transition={{ duration: 0.3 }}
                      >
                        <ChevronDown className="h-5 w-5 text-muted-foreground" />
                      </motion.div>
                    </div>

                    {/* Quick Stats */}
                    <div className="grid grid-cols-2 gap-4 mb-3">
                      <div className="flex items-center gap-3">
                        <div className={`p-2 rounded-lg ${config.bg}`}>
                          <Clock className={`h-4 w-4 ${config.color}`} />
                        </div>
                        <div>
                          <p className={`font-bold ${config.color}`}>
                            {weakness.avgTime} min
                          </p>
                          <p className="text-xs text-muted-foreground">Avg Time</p>
                        </div>
                      </div>

                      <div className="flex items-center gap-3">
                        <div className={`p-2 rounded-lg ${config.bg}`}>
                          <HelpCircle className={`h-4 w-4 ${config.color}`} />
                        </div>
                        <div>
                          <p className={`font-bold ${config.color}`}>
                            {Math.round(weakness.hintUsageRate * 100)}%
                          </p>
                          <p className="text-xs text-muted-foreground">Hint Usage</p>
                        </div>
                      </div>
                    </div>

                    {/* Expandable Details */}
                    <AnimatePresence>
                      {isExpanded && (
                        <motion.div
                          initial={{ height: 0, opacity: 0 }}
                          animate={{ height: "auto", opacity: 1 }}
                          exit={{ height: 0, opacity: 0 }}
                          transition={{ duration: 0.3 }}
                          className="overflow-hidden"
                        >
                          <div className="space-y-3 pt-3 border-t border-white/20">
                            {/* Recommendation */}
                            <div className={`p-3 rounded-lg ${config.bg} border border-white/20`}>
                              <div className="flex items-start gap-2">
                                <TrendingDown className={`h-4 w-4 ${config.color} mt-0.5 shrink-0`} />
                                <div>
                                  <p className="text-xs font-semibold text-foreground mb-1">
                                    Recommendation:
                                  </p>
                                  <p className="text-xs text-muted-foreground">
                                    {weakness.recommendation}
                                  </p>
                                </div>
                              </div>
                            </div>

                            {/* Action Button */}
                            <GradientButton
                              variant="purple"
                              size="sm"
                              className="w-full"
                              glow="soft"
                            >
                              <Target className="h-4 w-4" />
                              Practice {weakness.pattern}
                              <ArrowRight className="h-4 w-4" />
                            </GradientButton>
                          </div>
                        </motion.div>
                      )}
                    </AnimatePresence>
                  </motion.div>
                </motion.div>
              );
            })}
          </motion.div>

          {/* Summary */}
          <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 1.2 }}
            className="mt-6"
          >
            <GlassCard variant="default" size="sm" className="border-sunset-orange/30">
              <div className="text-center">
                <div className="flex items-center justify-center gap-2 mb-2">
                  <Flame className="h-4 w-4 text-sunset-orange" />
                  <span className="text-sm font-bold text-gradient-orange-red">
                    Action Plan
                  </span>
                  <Flame className="h-4 w-4 text-sunset-orange" />
                </div>
                <p className="text-xs text-muted-foreground">
                  Focus on these {weaknesses.length} pattern{weaknesses.length !== 1 ? 's' : ''} to significantly boost your readiness score
                </p>
              </div>
            </GlassCard>
          </motion.div>
        </CardContent>
      </GlassCard>
    </motion.div>
  );
}
