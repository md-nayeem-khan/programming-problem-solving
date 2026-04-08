"use client";

import { useEffect, useState, useMemo } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { useRouter } from "next/navigation";
import {
  Brain,
  Clock,
  TrendingUp,
  ChevronRight,
  Sparkles,
  Zap,
  BarChart3,
  AlertCircle,
} from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { Skeleton } from "@/components/ui/skeleton";

import type { Variants } from "framer-motion";
import {
  staggerContainer,
  staggerItem,
  fadeInUp,
  scaleIn,
} from "@/lib/animations";

// Types
interface PatternStat {
  id: number;
  name: string;
  category: string;
  solved: number;
  avgTimeMinutes: number;
  confidence: "Weak" | "Medium" | "Strong";
  hintUsageRate: number;
  masteryPercentage: number;
  problemIds: number[];
}

interface PatternCategory {
  name: string;
  icon: React.ReactNode;
  gradient: string;
  patterns: PatternStat[];
}



// Animation variants
const gridContainerVariants: Variants = {
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: {
      staggerChildren: 0.08,
      delayChildren: 0.1,
    },
  },
};

const gridItemVariants: Variants = {
  hidden: { opacity: 0, y: 30, scale: 0.95 },
  visible: {
    opacity: 1,
    y: 0,
    scale: 1,
    transition: {
      duration: 0.5,
      ease: "easeOut",
    },
  },
};

const cardHoverVariants: Variants = {
  rest: {
    scale: 1,
    boxShadow: "0 4px 6px -1px rgba(0, 0, 0, 0.1)",
  },
  hover: {
    scale: 1.02,
    boxShadow: "0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)",
    transition: {
      duration: 0.3,
      ease: "easeOut",
    },
  },
};

export default function PatternsPage() {
  const router = useRouter();
  const [patterns, setPatterns] = useState<PatternStat[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);


  useEffect(() => {
    async function fetchPatterns() {
      try {
        const response = await fetch("/api/analytics/patterns/confidence");
        
        if (!response.ok) {
          throw new Error("Failed to fetch pattern data");
        }
        
        const data = await response.json();
        
        if (data.patterns && data.patterns.length > 0) {
          // Transform API data to our format
          const transformedPatterns: PatternStat[] = data.patterns.map((p: any, index: number) => ({
            id: index + 1,
            name: p.pattern,
            category: p.category || "General",
            solved: p.totalSolved,
            avgTimeMinutes: Math.round(p.avgTimeSeconds / 60),
            confidence: p.confidence,
            hintUsageRate: p.hintUsageRate,
            masteryPercentage: calculateMastery(p.confidence, p.avgTimeSeconds, p.hintUsageRate),
            problemIds: p.problemIds || [],
          }));
          setPatterns(transformedPatterns);
        } else {
          // No data available from database
          setPatterns([]);
        }
      } catch (err) {
        console.error("Error fetching patterns:", err);
        // Set empty array on error instead of mock data
        setPatterns([]);
        setError("Failed to load pattern data. Please try again later.");
      } finally {
        setLoading(false);
      }
    }

    fetchPatterns();
  }, []);

  // Calculate mastery percentage based on metrics
  function calculateMastery(confidence: string, avgTimeSeconds: number, hintUsageRate: number): number {
    const avgTimeMinutes = avgTimeSeconds / 60;
    let base = confidence === "Strong" ? 80 : confidence === "Medium" ? 55 : 30;
    
    // Adjust based on time (faster is better)
    if (avgTimeMinutes < 20) base += 10;
    else if (avgTimeMinutes > 35) base -= 10;
    
    // Adjust based on hint usage (lower is better)
    if (hintUsageRate < 0.15) base += 5;
    else if (hintUsageRate > 0.4) base -= 5;
    
    return Math.min(100, Math.max(0, base));
  }

  // Use all patterns without filtering
  const filteredPatterns = patterns;

  // Calculate summary stats
  const summaryStats = useMemo(() => {
    const total = patterns.length;
    const strong = patterns.filter(p => p.confidence === "Strong").length;
    const medium = patterns.filter(p => p.confidence === "Medium").length;
    const weak = patterns.filter(p => p.confidence === "Weak").length;
    const totalSolved = patterns.reduce((sum, p) => sum + p.solved, 0);
    const avgMastery = patterns.length > 0 
      ? Math.round(patterns.reduce((sum, p) => sum + p.masteryPercentage, 0) / patterns.length)
      : 0;
    
    return { total, strong, medium, weak, totalSolved, avgMastery };
  }, [patterns]);

  // Get confidence badge styling
  const getConfidenceBadge = (confidence: string) => {
    switch (confidence) {
      case "Strong":
        return "bg-gradient-to-r from-emerald-500 to-green-500 text-white border-0 shadow-lg shadow-emerald-500/30";
      case "Medium":
        return "bg-gradient-to-r from-amber-500 to-yellow-500 text-white border-0 shadow-lg shadow-amber-500/30";
      case "Weak":
        return "bg-gradient-to-r from-rose-500 to-red-500 text-white border-0 shadow-lg shadow-rose-500/30";
      default:
        return "";
    }
  };

  // Get progress bar color
  const getProgressColor = (mastery: number) => {
    if (mastery >= 80) return "bg-gradient-to-r from-emerald-500 to-green-400";
    if (mastery >= 50) return "bg-gradient-to-r from-amber-500 to-yellow-400";
    return "bg-gradient-to-r from-rose-500 to-red-400";
  };

  // Get card gradient based on confidence
  const getCardGradient = (confidence: string) => {
    switch (confidence) {
      case "Strong":
        return "from-emerald-50/80 via-green-50/50 to-teal-50/80 dark:from-emerald-950/40 dark:via-green-900/20 dark:to-teal-950/40 border-emerald-200/60 dark:border-emerald-800/60";
      case "Medium":
        return "from-amber-50/80 via-yellow-50/50 to-orange-50/80 dark:from-amber-950/40 dark:via-yellow-900/20 dark:to-orange-950/40 border-amber-200/60 dark:border-amber-800/60";
      case "Weak":
        return "from-rose-50/80 via-pink-50/50 to-red-50/80 dark:from-rose-950/40 dark:via-pink-900/20 dark:to-red-950/40 border-rose-200/60 dark:border-rose-800/60";
      default:
        return "from-gray-50 to-gray-100 dark:from-gray-900 dark:to-gray-800";
    }
  };

  // Handle card click - navigate to pattern detail
  const handlePatternClick = (pattern: PatternStat) => {
    router.push(`/problems?pattern=${encodeURIComponent(pattern.name)}`);
  };

  if (loading) {
    return <PatternsPageSkeleton />;
  }

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Page Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <div className="relative">
          <h1 className="relative text-4xl font-bold">
            <span className="text-gradient-purple-pink">
              Algorithm Patterns
            </span>
          </h1>
          <p className="text-muted-foreground mt-2 text-base">
            <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
            Master the building blocks of coding interviews
          </p>
        </div>
      </motion.div>

      {/* Summary Cards */}
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
                  <p className="text-sm font-medium text-muted-foreground">Total Patterns</p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-pink-600 to-purple-600 bg-clip-text text-transparent">
                    {summaryStats.total}
                  </p>
                </div>
                <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-pink-500 to-purple-500 flex items-center justify-center shadow-lg shadow-pink-500/30">
                  <Brain className="h-6 w-6 text-white" />
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
                  <p className="text-sm font-medium text-muted-foreground">Problems Solved</p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
                    {summaryStats.totalSolved}
                  </p>
                </div>
                <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-500 flex items-center justify-center shadow-lg shadow-blue-500/30">
                  <BarChart3 className="h-6 w-6 text-white" />
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
                  <p className="text-sm font-medium text-muted-foreground">Avg Mastery</p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-emerald-600 to-teal-600 bg-clip-text text-transparent">
                    {summaryStats.avgMastery}%
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
                  <p className="text-sm font-medium text-muted-foreground">Strong Rate</p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-orange-600 to-amber-600 bg-clip-text text-transparent">
                    {summaryStats.total > 0 ? Math.round((summaryStats.strong / summaryStats.total) * 100) : 0}%
                  </p>
                </div>
                <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-orange-500 to-amber-500 flex items-center justify-center shadow-lg shadow-orange-500/30">
                  <TrendingUp className="h-6 w-6 text-white" />
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>
      </motion.div>



      {/* Patterns Grid */}
      <motion.div
        variants={gridContainerVariants}
        initial="hidden"
        animate="visible"
        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
      >
        <AnimatePresence mode="popLayout">
          {filteredPatterns.map((pattern) => (
            <motion.div
              key={pattern.id}
              variants={gridItemVariants}
              layout
              initial="hidden"
              animate="visible"
              exit={{ opacity: 0, scale: 0.9, transition: { duration: 0.2 } }}
            >
              <motion.div
                variants={cardHoverVariants}
                initial="rest"
                whileHover="hover"
                whileTap={{ scale: 0.98 }}
                onClick={() => handlePatternClick(pattern)}
                className="cursor-pointer"
              >
                <Card className={`bg-gradient-to-br ${getCardGradient(pattern.confidence)} border-2 overflow-hidden relative group transition-all duration-300`}>
                  {/* Animated gradient orbs */}
                  <div className="absolute -top-12 -right-12 w-32 h-32 bg-gradient-to-br from-current/10 to-current/5 rounded-full blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
                  <div className="absolute -bottom-8 -left-8 w-24 h-24 bg-gradient-to-tr from-current/10 to-current/5 rounded-full blur-xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
                  
                  <CardHeader className="pb-3 relative">
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <CardTitle className="text-lg font-semibold text-foreground flex items-center gap-2">
                          {pattern.name}
                          <ChevronRight className="h-4 w-4 text-muted-foreground opacity-0 group-hover:opacity-100 transition-opacity" />
                        </CardTitle>
                        <p className="text-xs text-muted-foreground mt-1">{pattern.category}</p>
                      </div>
                      <Badge className={`${getConfidenceBadge(pattern.confidence)} text-xs font-medium`}>
                        {pattern.confidence}
                      </Badge>
                    </div>
                  </CardHeader>
                  
                  <CardContent className="space-y-4 relative">
                    {/* Stats row */}
                    <div className="grid grid-cols-3 gap-3">
                      <div className="text-center p-2 rounded-lg bg-white/50 dark:bg-gray-900/30">
                        <div className="flex items-center justify-center gap-1 text-muted-foreground mb-1">
                          <Zap className="h-3 w-3" />
                        </div>
                        <p className="text-lg font-bold text-foreground">{pattern.solved}</p>
                        <p className="text-[10px] text-muted-foreground uppercase tracking-wide">Solved</p>
                      </div>
                      <div className="text-center p-2 rounded-lg bg-white/50 dark:bg-gray-900/30">
                        <div className="flex items-center justify-center gap-1 text-muted-foreground mb-1">
                          <Clock className="h-3 w-3" />
                        </div>
                        <p className="text-lg font-bold text-foreground">{pattern.avgTimeMinutes}m</p>
                        <p className="text-[10px] text-muted-foreground uppercase tracking-wide">Avg Time</p>
                      </div>
                      <div className="text-center p-2 rounded-lg bg-white/50 dark:bg-gray-900/30">
                        <div className="flex items-center justify-center gap-1 text-muted-foreground mb-1">
                          <AlertCircle className="h-3 w-3" />
                        </div>
                        <p className="text-lg font-bold text-foreground">{Math.round(pattern.hintUsageRate * 100)}%</p>
                        <p className="text-[10px] text-muted-foreground uppercase tracking-wide">Hints</p>
                      </div>
                    </div>

                    {/* Mastery Progress */}
                    <div className="space-y-2">
                      <div className="flex items-center justify-between text-sm">
                        <span className="text-muted-foreground font-medium">Mastery Level</span>
                        <span className="font-bold text-foreground">{pattern.masteryPercentage}%</span>
                      </div>
                      <div className="relative h-3 bg-gray-200 dark:bg-gray-800 rounded-full overflow-hidden">
                        <motion.div
                          className={`h-full rounded-full ${getProgressColor(pattern.masteryPercentage)} relative`}
                          initial={{ width: 0 }}
                          animate={{ width: `${pattern.masteryPercentage}%` }}
                          transition={{ duration: 1, ease: "easeOut", delay: 0.5 }}
                        >
                          {/* Shimmer effect */}
                          <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent animate-shimmer" 
                               style={{ backgroundSize: "200% 100%" }} />
                        </motion.div>
                      </div>
                    </div>

                    {/* View Problems button */}
                    <Button 
                      variant="ghost" 
                      size="sm" 
                      className="w-full mt-2 text-sm font-medium opacity-0 group-hover:opacity-100 transition-all duration-300 bg-white/50 dark:bg-gray-900/50 hover:bg-white/80 dark:hover:bg-gray-900/80"
                    >
                      View Problems
                      <ChevronRight className="h-4 w-4 ml-1" />
                    </Button>
                  </CardContent>
                </Card>
              </motion.div>
            </motion.div>
          ))}
        </AnimatePresence>
      </motion.div>

      {/* Empty state */}
      {filteredPatterns.length === 0 && !loading && (
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center py-16"
        >
          <div className="flex h-20 w-20 items-center justify-center rounded-full bg-gradient-to-br from-pink-100 to-purple-100 dark:from-pink-950/30 dark:to-purple-950/30 mx-auto mb-4">
            <Brain className="h-10 w-10 text-pink-500" />
          </div>
          <h3 className="text-lg font-semibold text-foreground mb-2">
            {error ? "Failed to load patterns" : "No patterns found"}
          </h3>
          <p className="text-muted-foreground">
            {error ? error : "Start solving problems to see pattern analytics here."}
          </p>
        </motion.div>
      )}

      {/* Footer Info - similar to dashboard */}
      <div className="text-center py-8">
        <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-muted/50 border border-border">
          <span className="text-sm text-muted-foreground">
            💡 Tip: Focus on weak patterns and practice consistently to improve your mastery!
          </span>
        </div>
      </div>
    </div>
  );
}

// Loading Skeleton
function PatternsPageSkeleton() {
  return (
    <div className="space-y-8">
      {/* Header skeleton */}
      <div className="flex items-center gap-3">
        <Skeleton className="h-12 w-12 rounded-xl" />
        <div className="space-y-2">
          <Skeleton className="h-8 w-48" />
          <Skeleton className="h-4 w-64" />
        </div>
      </div>

      {/* Summary cards skeleton */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        {[1, 2, 3, 4].map((i) => (
          <Card key={i} className="border-2">
            <CardContent className="pt-6">
              <div className="flex items-center justify-between">
                <div className="space-y-2">
                  <Skeleton className="h-4 w-20" />
                  <Skeleton className="h-8 w-16" />
                </div>
                <Skeleton className="h-12 w-12 rounded-xl" />
              </div>
            </CardContent>
          </Card>
        ))}
      </div>



      {/* Grid skeleton */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {[1, 2, 3, 4, 5, 6].map((i) => (
          <Card key={i} className="border-2">
            <CardHeader className="pb-3">
              <div className="flex justify-between">
                <div className="space-y-2">
                  <Skeleton className="h-5 w-32" />
                  <Skeleton className="h-3 w-20" />
                </div>
                <Skeleton className="h-6 w-16 rounded-full" />
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-3 gap-3">
                {[1, 2, 3].map((j) => (
                  <Skeleton key={j} className="h-16 rounded-lg" />
                ))}
              </div>
              <div className="space-y-2">
                <div className="flex justify-between">
                  <Skeleton className="h-4 w-24" />
                  <Skeleton className="h-4 w-12" />
                </div>
                <Skeleton className="h-3 w-full rounded-full" />
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
