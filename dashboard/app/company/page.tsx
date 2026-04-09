"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { useRouter } from "next/navigation";
import {
  Building2,
  Target,
  TrendingUp,
  CheckCircle2,
  Clock,
  ExternalLink,
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

// Animation variants for better card interactions
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

const gridItemVariants: Variants = {
  hidden: { 
    opacity: 0, 
    y: 20,
    scale: 0.95 
  },
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

// Company data with brand colors
const COMPANIES = [
  {
    name: "Google",
    icon: "🇬",
    colors: {
      from: "blue-500",
      to: "green-500",
      text: "blue-700",
      bg: "blue-50",
      border: "blue-200",
    },
    targetProblems: 150,
  },
  {
    name: "Amazon",
    icon: "📦",
    colors: {
      from: "orange-500",
      to: "amber-500",
      text: "orange-700",
      bg: "orange-50",
      border: "orange-200",
    },
    targetProblems: 120,
  },
  {
    name: "Meta",
    icon: "👤",
    colors: {
      from: "blue-600",
      to: "indigo-600",
      text: "blue-700",
      bg: "blue-50",
      border: "blue-200",
    },
    targetProblems: 100,
  },
  {
    name: "Apple",
    icon: "🍎",
    colors: {
      from: "slate-600",
      to: "gray-700",
      text: "slate-700",
      bg: "slate-50",
      border: "slate-200",
    },
    targetProblems: 80,
  },
  {
    name: "Netflix",
    icon: "🎬",
    colors: {
      from: "red-600",
      to: "rose-600",
      text: "red-700",
      bg: "red-50",
      border: "red-200",
    },
    targetProblems: 60,
  },
  {
    name: "Microsoft",
    icon: "🪟",
    colors: {
      from: "blue-500",
      to: "cyan-500",
      text: "blue-700",
      bg: "blue-50",
      border: "blue-200",
    },
    targetProblems: 90,
  },
];

interface CompanyStat {
  company: string;
  solved: number;
  readiness: string;
  topPatterns: string[];
  avgTime: number;
  difficulty: { easy: number; medium: number; hard: number };
}

export default function CompanyPage() {
  const [stats, setStats] = useState<CompanyStat[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  useEffect(() => {
    async function fetchCompanyStats() {
      try {
        setLoading(true);
        setError(null);
        
        const response = await fetch("/api/analytics/company?timeframe=month");
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        const apiCompanies = Array.isArray(data.companies) ? data.companies : [];
        
        // Use real API-backed values only (no random fallback), so empty DB shows empty state.
        const companyStats: CompanyStat[] = apiCompanies
          .filter((company: any) => typeof company?.company === "string" && company.company.trim().length > 0)
          .map((company: any) => {
            const readinessScore = Number(company?.readinessScore ?? company?.readiness ?? 0);

            return {
              company: company.company,
              solved: Number(company?.problemsSolved ?? 0),
              readiness: company?.isReady
                ? "Ready"
                : readinessScore > 0.5
                  ? "Almost Ready"
                  : "Not Ready",
              topPatterns: Array.isArray(company?.topPatterns)
                ? company.topPatterns.filter((pattern: unknown) => typeof pattern === "string")
                : [],
              avgTime: company?.avgTimeSeconds ? Math.round(company.avgTimeSeconds / 60) : 0,
              difficulty: {
                easy: Number(company?.difficulty?.easy ?? 0),
                medium: Number(company?.difficulty?.medium ?? 0),
                hard: Number(company?.difficulty?.hard ?? 0),
              },
            };
          })
          .filter((company) =>
            company.solved > 0 ||
            company.avgTime > 0 ||
            company.topPatterns.length > 0 ||
            company.difficulty.easy + company.difficulty.medium + company.difficulty.hard > 0
          );
        
        setStats(companyStats);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load data");
      } finally {
        setLoading(false);
      }
    }

    fetchCompanyStats();
  }, []);

  const getReadinessColor = (readiness: string) => {
    switch (readiness) {
      case "Ready": return "bg-gradient-to-r from-emerald-500 to-green-500 text-white border-0 shadow-lg shadow-emerald-500/30";
      case "Almost Ready": return "bg-gradient-to-r from-amber-500 to-yellow-500 text-white border-0 shadow-lg shadow-amber-500/30";
      default: return "bg-gradient-to-r from-rose-500 to-red-500 text-white border-0 shadow-lg shadow-rose-500/30";
    }
  };

  const getReadinessIcon = (readiness: string) => {
    switch (readiness) {
      case "Ready": return "✨";
      case "Almost Ready": return "⚡";
      default: return "🎯";
    }
  };

  const getProgressColor = (percentage: number) => {
    if (percentage >= 80) return "bg-gradient-to-r from-emerald-500 to-green-400";
    if (percentage >= 50) return "bg-gradient-to-r from-amber-500 to-yellow-400";
    return "bg-gradient-to-r from-rose-500 to-red-400";
  };

  const getCompanyCardGradient = (company: any) => {
    switch (company.name) {
      case "Google":
        return "from-blue-50/80 via-white/60 to-blue-50/80 dark:from-blue-50/10 dark:via-gray-900/80 dark:to-blue-50/10 border-blue-200/60 dark:border-blue-200/40";
      case "Amazon":
        return "from-orange-50/80 via-white/60 to-orange-50/80 dark:from-orange-50/10 dark:via-gray-900/80 dark:to-orange-50/10 border-orange-200/60 dark:border-orange-200/40";
      case "Meta":
        return "from-blue-50/80 via-white/60 to-blue-50/80 dark:from-blue-50/10 dark:via-gray-900/80 dark:to-blue-50/10 border-blue-200/60 dark:border-blue-200/40";
      case "Apple":
        return "from-slate-50/80 via-white/60 to-slate-50/80 dark:from-slate-50/10 dark:via-gray-900/80 dark:to-slate-50/10 border-slate-200/60 dark:border-slate-200/40";
      case "Netflix":
        return "from-red-50/80 via-white/60 to-red-50/80 dark:from-red-50/10 dark:via-gray-900/80 dark:to-red-50/10 border-red-200/60 dark:border-red-200/40";
      case "Microsoft":
        return "from-blue-50/80 via-white/60 to-blue-50/80 dark:from-blue-50/10 dark:via-gray-900/80 dark:to-blue-50/10 border-blue-200/60 dark:border-blue-200/40";
      default:
        return "from-gray-50/80 via-white/60 to-gray-50/80 dark:from-gray-50/10 dark:via-gray-900/80 dark:to-gray-50/10 border-gray-200/60 dark:border-gray-200/40";
    }
  };

  const getCompanyNameClass = (company: any) => {
    switch (company.name) {
      case "Google":
        return "text-lg font-semibold bg-gradient-to-r from-blue-700 to-blue-500 bg-clip-text text-transparent flex items-center gap-2";
      case "Amazon":
        return "text-lg font-semibold bg-gradient-to-r from-orange-700 to-orange-500 bg-clip-text text-transparent flex items-center gap-2";
      case "Meta":
        return "text-lg font-semibold bg-gradient-to-r from-blue-700 to-blue-600 bg-clip-text text-transparent flex items-center gap-2";
      case "Apple":
        return "text-lg font-semibold bg-gradient-to-r from-slate-700 to-slate-600 bg-clip-text text-transparent flex items-center gap-2";
      case "Netflix":
        return "text-lg font-semibold bg-gradient-to-r from-red-700 to-red-600 bg-clip-text text-transparent flex items-center gap-2";
      case "Microsoft":
        return "text-lg font-semibold bg-gradient-to-r from-blue-700 to-blue-500 bg-clip-text text-transparent flex items-center gap-2";
      default:
        return "text-lg font-semibold text-foreground flex items-center gap-2";
    }
  };

  if (loading) {
    return (
      <div className="space-y-8 p-2">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="flex items-center justify-between"
        >
          <div>
            <Skeleton className="h-10 w-64 mb-2" />
            <Skeleton className="h-6 w-96" />
          </div>
        </motion.div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {Array.from({ length: 6 }).map((_, i) => (
            <Card key={i} className="h-96 border-2 overflow-hidden relative">
              <CardHeader className="pb-3">
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <Skeleton className="h-12 w-12 rounded-xl" />
                    <div>
                      <Skeleton className="h-6 w-24 mb-1" />
                      <Skeleton className="h-3 w-32" />
                    </div>
                  </div>
                  <Skeleton className="h-6 w-16 rounded-full" />
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <Skeleton className="h-4 w-16" />
                    <Skeleton className="h-4 w-12" />
                  </div>
                  <Skeleton className="h-3 w-full rounded-full" />
                  <Skeleton className="h-3 w-20" />
                </div>
                <div className="grid grid-cols-2 gap-3">
                  <Skeleton className="h-20 w-full rounded-lg" />
                  <Skeleton className="h-20 w-full rounded-lg" />
                </div>
                <div className="space-y-2">
                  <Skeleton className="h-4 w-24" />
                  <div className="flex gap-1">
                    <Skeleton className="h-5 w-16 rounded-full" />
                    <Skeleton className="h-5 w-20 rounded-full" />
                  </div>
                </div>
                <Skeleton className="h-8 w-full rounded" />
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="space-y-8 p-2">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center py-12"
        >
          <AlertCircle className="h-12 w-12 text-red-500 mx-auto mb-4" />
          <h2 className="text-xl font-semibold text-red-600 mb-2">
            Error Loading Company Data
          </h2>
          <p className="text-muted-foreground">{error}</p>
        </motion.div>
      </div>
    );
  }

  return (
    <div className="space-y-8 p-2">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="flex items-center"
      >
        <div>
          <h1 className="text-4xl font-bold bg-gradient-to-r from-blue-600 via-purple-500 to-pink-500 bg-clip-text text-transparent">
            Company Readiness
          </h1>
          <p className="text-muted-foreground mt-2 text-lg">
            Track your preparation progress for top tech companies
          </p>
        </div>
      </motion.div>

      {stats.length === 0 ? (
        <motion.div
          variants={scaleIn}
          initial="hidden"
          animate="visible"
          className="text-center py-16 rounded-xl border border-dashed border-border bg-muted/20"
        >
          <Building2 className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
          <h2 className="text-xl font-semibold mb-2">No Company Data Yet</h2>
          <p className="text-muted-foreground max-w-xl mx-auto">
            Company cards will appear after you solve company-tagged problems.
          </p>
        </motion.div>
      ) : (
        <motion.div
          variants={staggerContainer}
          initial="hidden"
          animate="visible"
          className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
        >
          {stats.map((companyStat) => {
            const company = COMPANIES.find((item) =>
              item.name.toLowerCase() === companyStat.company.toLowerCase()
            ) || {
              name: companyStat.company,
              icon: "🏢",
              colors: {
                from: "gray-500",
                to: "slate-500",
                text: "gray-700",
                bg: "gray-50",
                border: "gray-200",
              },
              targetProblems: 100,
            };

            const readinessPercentage = Math.round((companyStat.solved / company.targetProblems) * 100);

            return (
            <motion.div
              key={company.name}
              variants={gridItemVariants}
            >
              <motion.div
                variants={cardHoverVariants}
                initial="rest"
                whileHover="hover"
                whileTap={{ scale: 0.98 }}
                className="cursor-pointer"
              >
                <Card className={`bg-gradient-to-br ${getCompanyCardGradient(company)} border-2 overflow-hidden relative group transition-all duration-300 h-96`}>
                  {/* Animated gradient orbs */}
                  <div className={`absolute -top-12 -right-12 w-32 h-32 bg-gradient-to-br from-${company.colors.from}/20 to-${company.colors.to}/10 rounded-full blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500`} />
                  <div className={`absolute -bottom-8 -left-8 w-24 h-24 bg-gradient-to-tr from-${company.colors.to}/20 to-${company.colors.from}/10 rounded-full blur-xl opacity-0 group-hover:opacity-100 transition-opacity duration-500`} />
                  
                  <CardHeader className="pb-3 relative">
                    <div className="flex items-start justify-between">
                      <div className="flex items-center gap-3">
                        <div className={`flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-${company.colors.from} to-${company.colors.to} shadow-lg shadow-${company.colors.from}/30 text-2xl`}>
                          {company.icon}
                        </div>
                        <div>
                          <CardTitle className="text-lg font-semibold text-foreground flex items-center gap-2">
                            {company.name}
                            <ExternalLink className="h-4 w-4 text-muted-foreground opacity-0 group-hover:opacity-100 transition-opacity" />
                          </CardTitle>
                          <p className="text-xs text-muted-foreground mt-1">Target: {company.targetProblems} problems</p>
                        </div>
                      </div>
                      <Badge className={`${companyStat ? getReadinessColor(companyStat.readiness) : 'bg-gray-100 text-gray-600'} text-xs font-medium`}>
                        {getReadinessIcon(companyStat.readiness)} 
                        {companyStat.readiness}
                      </Badge>
                    </div>
                  </CardHeader>
                  
                  <CardContent className="space-y-4 relative">
                    {/* Progress Section */}
                    <div className="space-y-2">
                      <div className="flex items-center justify-between text-sm">
                        <span className="text-muted-foreground font-medium">Progress</span>
                        <span className="font-bold text-foreground">
                          {companyStat.solved}/{company.targetProblems}
                        </span>
                      </div>
                      <div className="relative h-3 bg-gray-200 dark:bg-gray-800 rounded-full overflow-hidden">
                        <motion.div
                          className={`h-full rounded-full ${getProgressColor(readinessPercentage)} relative`}
                          initial={{ width: 0 }}
                          animate={{ width: `${Math.min(readinessPercentage, 100)}%` }}
                          transition={{ duration: 1, ease: "easeOut", delay: 0.3 }}
                        >
                          {/* Shimmer effect */}
                          <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent animate-shimmer" 
                               style={{ backgroundSize: "200% 100%" }} />
                        </motion.div>
                      </div>
                      <span className="text-xs text-muted-foreground font-medium">
                        {readinessPercentage}% Complete
                      </span>
                    </div>
                    
                    {/* Stats Grid */}
                    <div className="grid grid-cols-2 gap-3">
                      <div className="text-center p-3 rounded-lg bg-white/50 dark:bg-gray-900/30 border border-white/20 dark:border-gray-800/20">
                        <div className="flex items-center justify-center gap-1 text-muted-foreground mb-1">
                          <Clock className="h-3 w-3" />
                        </div>
                        <p className="text-lg font-bold text-foreground">{companyStat.avgTime}m</p>
                        <p className="text-[10px] text-muted-foreground uppercase tracking-wide">Avg Time</p>
                      </div>
                      <div className="text-center p-3 rounded-lg bg-white/50 dark:bg-gray-900/30 border border-white/20 dark:border-gray-800/20">
                        <div className="flex items-center justify-center gap-1 text-muted-foreground mb-1">
                          <Target className="h-3 w-3" />
                        </div>
                        <p className="text-lg font-bold text-foreground">{companyStat.difficulty.easy + companyStat.difficulty.medium + companyStat.difficulty.hard}</p>
                        <p className="text-[10px] text-muted-foreground uppercase tracking-wide">Total Solved</p>
                      </div>
                    </div>
                    
                    {/* Top Patterns */}
                    {companyStat.topPatterns.length > 0 && (
                      <div>
                        <span className="text-sm font-medium text-muted-foreground">Top Patterns:</span>
                        <div className="flex flex-wrap gap-1 mt-2">
                          {companyStat.topPatterns.slice(0, 3).map((pattern) => (
                            <Badge 
                              key={pattern} 
                              variant="secondary" 
                              className="text-xs bg-white/30 dark:bg-gray-800/30 border border-white/20 dark:border-gray-700/20"
                            >
                              {pattern}
                            </Badge>
                          ))}
                        </div>
                      </div>
                    )}
                    
                    {/* Action Button */}
                    <Button
                      size="sm"
                      className={`w-full bg-gradient-to-r from-${company.colors.from} to-${company.colors.to} hover:from-${company.colors.from} hover:to-${company.colors.to} text-white shadow-lg hover:shadow-xl transition-all duration-200 opacity-0 group-hover:opacity-100 mt-2`}
                      onClick={() => router.push(`/problems?company=${companyStat.company.toLowerCase()}`)}
                    >
                      <ExternalLink className="h-3 w-3 mr-2" />
                      View Problems
                    </Button>
                  </CardContent>
                </Card>
              </motion.div>
            </motion.div>
            );
          })}
        </motion.div>
      )}
    </div>
  );
}