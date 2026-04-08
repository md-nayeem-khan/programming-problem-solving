"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Target, TrendingUp, CheckCircle2, XCircle, Play, AlertCircle } from "lucide-react";
import { fadeInUp } from "@/lib/animations";

interface MockInterviewStats {
  totalCompleted: number;
  totalPassed: number;
  passRate: number;
  avgExplanationScore: number;
  avgCodeQualityScore: number;
  recentInterviews: Array<{
    id: string;
    date: string;
    problemTitle: string;
    solved: boolean;
    overallScore: number;
  }>;
}

export function MockInterviewCard() {
  const [stats, setStats] = useState<MockInterviewStats | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchMockStats() {
      try {
        const response = await fetch("/api/mock-interviews?limit=5&recent=true");
        if (!response.ok) throw new Error("Failed to fetch mock interview data");
        
        const data = await response.json();
        
        const interviews = data.mockInterviews || [];
        const passedCount = interviews.filter((i: any) => i.solved).length;
        
        setStats({
          totalCompleted: data.stats?.total || interviews.length,
          totalPassed: passedCount,
          passRate: interviews.length > 0 ? (passedCount / interviews.length) * 100 : 0,
          avgExplanationScore: data.stats?.avgExplanationScore || 0,
          avgCodeQualityScore: data.stats?.avgCodeQualityScore || 0,
          recentInterviews: interviews.slice(0, 5).map((i: any) => ({
            id: i.id,
            date: i.date,
            problemTitle: i.problem?.title || "Unknown Problem",
            solved: i.solved,
            overallScore: i.overallScore || 0,
          })),
        });
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load data");
      } finally {
        setLoading(false);
      }
    }

    fetchMockStats();
  }, []);

  if (loading) {
    return (
      <Card className="bg-gradient-to-br from-rose-50 to-pink-50 dark:from-rose-950/20 dark:to-pink-950/20 border-rose-200 dark:border-rose-800">
        <CardHeader>
          <Skeleton className="h-6 w-44" />
        </CardHeader>
        <CardContent className="space-y-3">
          <Skeleton className="h-24 w-full" />
          <Skeleton className="h-16 w-full" />
        </CardContent>
      </Card>
    );
  }

  if (error) {
    return (
      <Card className="bg-gradient-to-br from-red-50 to-orange-50 dark:from-red-950/20 dark:to-orange-950/20 border-red-200 dark:border-red-800">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-red-600 dark:text-red-400">
            <AlertCircle className="h-5 w-5" />
            Error Loading Mock Interviews
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">{error}</p>
        </CardContent>
      </Card>
    );
  }

  if (!stats) {
    return null;
  }

  return (
    <motion.div
      variants={fadeInUp}
      initial="hidden"
      animate="visible"
      whileHover={{ scale: 1.02, transition: { duration: 0.2 } }}
    >
      <Card className="bg-gradient-to-br from-rose-100 via-pink-50 to-fuchsia-100 dark:from-rose-950/40 dark:via-pink-900/20 dark:to-fuchsia-950/40 border-2 border-rose-300 dark:border-rose-700 overflow-hidden relative shadow-xl shadow-rose-500/20 hover:shadow-2xl hover:shadow-rose-500/30 transition-shadow duration-300">
        {/* Decorative gradient orbs */}
        <div className="absolute -top-10 -right-10 w-40 h-40 bg-gradient-to-br from-rose-400/30 to-pink-400/30 rounded-full blur-3xl" />
        <div className="absolute -bottom-10 -left-10 w-32 h-32 bg-gradient-to-tr from-pink-400/20 to-rose-400/20 rounded-full blur-2xl" />
        
        <CardHeader className="relative">
          <CardTitle className="flex items-center gap-2 text-lg font-semibold">
            <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-rose-500 to-pink-500 shadow-lg shadow-rose-500/30">
              <Target className="h-4 w-4 text-white" />
            </div>
            <span className="bg-gradient-to-r from-rose-700 to-pink-600 bg-clip-text text-transparent">
              Mock Interviews
            </span>
          </CardTitle>
        </CardHeader>
        
        <CardContent className="relative space-y-4">
          {/* Main Stats */}
          <div className="grid grid-cols-3 gap-3">
            <motion.div
              className="text-center p-3 rounded-lg bg-white/50 dark:bg-black/20 border border-rose-200/50 dark:border-rose-800/50"
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2 }}
            >
              <p className="text-2xl font-bold text-rose-600 dark:text-rose-400">
                {stats.totalCompleted}
              </p>
              <p className="text-xs text-muted-foreground">Total</p>
            </motion.div>

            <motion.div
              className="text-center p-3 rounded-lg bg-white/50 dark:bg-black/20 border border-rose-200/50 dark:border-rose-800/50"
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.3 }}
            >
              <p className="text-2xl font-bold text-green-600 dark:text-green-400">
                {stats.totalPassed}
              </p>
              <p className="text-xs text-muted-foreground">Passed</p>
            </motion.div>

            <motion.div
              className="text-center p-3 rounded-lg bg-white/50 dark:bg-black/20 border border-rose-200/50 dark:border-rose-800/50"
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.4 }}
            >
              <p className="text-2xl font-bold text-purple-600 dark:text-purple-400">
                {Math.round(stats.passRate)}%
              </p>
              <p className="text-xs text-muted-foreground">Pass Rate</p>
            </motion.div>
          </div>

          {/* Recent Results */}
          {stats.recentInterviews.length > 0 ? (
            <motion.div
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.5 }}
              className="space-y-2"
            >
              <h4 className="text-sm font-semibold text-muted-foreground mb-2">Recent Results:</h4>
              <div className="flex items-center gap-2 flex-wrap">
                {stats.recentInterviews.map((interview, index) => (
                  <motion.div
                    key={interview.id}
                    initial={{ scale: 0 }}
                    animate={{ scale: 1 }}
                    transition={{ delay: 0.6 + index * 0.1 }}
                    className="relative group"
                  >
                    <div className={`w-10 h-10 rounded-full flex items-center justify-center ${
                      interview.solved 
                        ? "bg-green-500 dark:bg-green-600" 
                        : "bg-red-500 dark:bg-red-600"
                    } text-white font-bold text-xs`}>
                      {interview.solved ? (
                        <CheckCircle2 className="h-5 w-5" />
                      ) : (
                        <XCircle className="h-5 w-5" />
                      )}
                    </div>
                    
                    {/* Tooltip on hover */}
                    <div className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 px-3 py-2 bg-gray-900 text-white text-xs rounded-lg opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none whitespace-nowrap z-10">
                      <p className="font-semibold">{interview.problemTitle}</p>
                      <p className="text-gray-300">Score: {interview.overallScore}/5</p>
                      <div className="absolute top-full left-1/2 -translate-x-1/2 border-4 border-transparent border-t-gray-900" />
                    </div>
                  </motion.div>
                ))}
              </div>
            </motion.div>
          ) : (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: 0.5 }}
              className="text-center py-6"
            >
              <p className="text-sm text-muted-foreground">
                No mock interviews yet
              </p>
              <p className="text-xs text-muted-foreground mt-1">
                Start your first mock interview to track progress!
              </p>
            </motion.div>
          )}

          {/* CTA Button */}
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.8 }}
          >
            <Button
              className="w-full bg-gradient-to-r from-rose-500 to-pink-500 hover:from-rose-600 hover:to-pink-600 text-white"
            >
              <Play className="h-4 w-4 mr-2" />
              Start Mock Interview
            </Button>
          </motion.div>

          {/* Progress Indicator */}
          {stats.totalCompleted > 0 && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: 0.9 }}
              className="flex items-center justify-center gap-2 text-xs text-muted-foreground"
            >
              <TrendingUp className="h-3.5 w-3.5" />
              <span>
                {stats.passRate >= 70 ? "Great progress!" : "Keep practicing!"}
              </span>
            </motion.div>
          )}
        </CardContent>
      </Card>
    </motion.div>
  );
}
