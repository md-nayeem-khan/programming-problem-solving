"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { RotateCcw, Play, Calendar, AlertCircle } from "lucide-react";
import { fadeInUp, staggerContainer, staggerItem } from "@/lib/animations";
import { format, isToday, isTomorrow, parseISO } from "date-fns";

interface RevisionItem {
  id: string;
  problemTitle: string;
  pattern: string;
  nextReviewDate: string;
  intervalLevel: number;
  isOverdue: boolean;
}

export function RevisionQueueCard() {
  const [revisions, setRevisions] = useState<RevisionItem[]>([]);
  const [upcomingCount, setUpcomingCount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchRevisions() {
      try {
        const response = await fetch("/api/revisions");
        if (!response.ok) throw new Error("Failed to fetch revisions");
        
        const data = await response.json();
        
        const now = new Date();
        const dueRevisions = (data.dueToday || []).map((rev: any) => ({
          id: rev.id,
          problemTitle: rev.submission?.problem?.title || "Unknown Problem",
          pattern: rev.submission?.problem?.patterns?.[0]?.pattern?.name || "No Pattern",
          nextReviewDate: rev.nextReviewDate,
          intervalLevel: rev.intervalLevel,
          isOverdue: new Date(rev.nextReviewDate) < now,
        }));
        
        setRevisions(dueRevisions.slice(0, 5));
        setUpcomingCount(data.upcoming?.length || 0);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load data");
      } finally {
        setLoading(false);
      }
    }

    fetchRevisions();
  }, []);

  if (loading) {
    return (
      <Card className="bg-gradient-to-br from-teal-50 to-emerald-50 dark:from-teal-950/20 dark:to-emerald-950/20 border-teal-200 dark:border-teal-800">
        <CardHeader>
          <Skeleton className="h-6 w-40" />
        </CardHeader>
        <CardContent className="space-y-3">
          {[1, 2, 3].map((i) => (
            <Skeleton key={i} className="h-16 w-full" />
          ))}
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
            Error Loading Revisions
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">{error}</p>
        </CardContent>
      </Card>
    );
  }

  const getDateLabel = (dateStr: string, isOverdue: boolean) => {
    if (isOverdue) return "Overdue";
    
    try {
      const date = parseISO(dateStr);
      if (isToday(date)) return "Today";
      if (isTomorrow(date)) return "Tomorrow";
      return format(date, "MMM d");
    } catch {
      return "Unknown";
    }
  };

  return (
    <motion.div
      variants={fadeInUp}
      initial="hidden"
      animate="visible"
      whileHover={{ scale: 1.02, transition: { duration: 0.2 } }}
    >
      <Card className="bg-gradient-to-br from-teal-100 via-cyan-50 to-emerald-100 dark:from-teal-950/40 dark:via-cyan-900/20 dark:to-emerald-950/40 border-2 border-teal-300 dark:border-teal-700 overflow-hidden relative shadow-xl shadow-teal-500/20 hover:shadow-2xl hover:shadow-teal-500/30 transition-shadow duration-300">
        {/* Decorative gradient orbs */}
        <div className="absolute -top-10 -right-10 w-40 h-40 bg-gradient-to-br from-teal-400/30 to-emerald-400/30 rounded-full blur-3xl" />
        <div className="absolute -bottom-10 -left-10 w-32 h-32 bg-gradient-to-tr from-emerald-400/20 to-teal-400/20 rounded-full blur-2xl" />
        
        <CardHeader className="relative">
          <div className="flex items-center justify-between">
            <CardTitle className="flex items-center gap-2 text-lg font-semibold">
              <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-teal-500 to-emerald-500 shadow-lg shadow-teal-500/30">
                <RotateCcw className="h-4 w-4 text-white" />
              </div>
              <span className="bg-gradient-to-r from-teal-700 to-emerald-600 bg-clip-text text-transparent">
                Revision Queue
              </span>
            </CardTitle>
            {revisions.length > 0 && (
              <Badge className="bg-teal-600 text-white">
                {revisions.length}
              </Badge>
            )}
          </div>
        </CardHeader>
        
        <CardContent className="relative">
          {revisions.length === 0 ? (
            <div className="text-center py-8">
              <motion.div
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ type: "spring", duration: 0.6 }}
              >
                <p className="text-3xl mb-2">🎉</p>
                <p className="text-lg font-semibold text-teal-600 dark:text-teal-400 mb-1">
                  All caught up!
                </p>
                <p className="text-sm text-muted-foreground">
                  No revisions due today
                </p>
                {upcomingCount > 0 && (
                  <p className="text-xs text-muted-foreground mt-2">
                    {upcomingCount} upcoming in the next 7 days
                  </p>
                )}
              </motion.div>
            </div>
          ) : (
            <>
              <motion.div
                variants={staggerContainer}
                initial="hidden"
                animate="visible"
                className="space-y-2 max-h-64 overflow-y-auto pr-2"
              >
                {revisions.map((revision, index) => (
                  <motion.div
                    key={revision.id}
                    variants={staggerItem}
                    className="group p-3 rounded-lg bg-white/50 dark:bg-black/20 border border-teal-200/50 dark:border-teal-800/50 hover:border-teal-300 dark:hover:border-teal-700 transition-all"
                  >
                    <div className="flex items-start justify-between mb-2">
                      <div className="flex-1">
                        <h4 className="font-semibold text-sm text-foreground line-clamp-1">
                          {revision.problemTitle}
                        </h4>
                        <div className="flex items-center gap-2 mt-1">
                          <Badge variant="outline" className="text-xs bg-teal-100 dark:bg-teal-900/30 text-teal-700 dark:text-teal-300 border-teal-300 dark:border-teal-700">
                            {revision.pattern}
                          </Badge>
                          <Badge 
                            variant="outline" 
                            className={`text-xs ${
                              revision.isOverdue 
                                ? "bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-300 border-red-300 dark:border-red-700"
                                : "bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-300 border-blue-300 dark:border-blue-700"
                            }`}
                          >
                            <Calendar className="h-3 w-3 mr-1" />
                            {getDateLabel(revision.nextReviewDate, revision.isOverdue)}
                          </Badge>
                        </div>
                      </div>
                    </div>
                    <Button
                      size="sm"
                      className="w-full bg-teal-600 hover:bg-teal-700 text-white"
                    >
                      <Play className="h-3.5 w-3.5 mr-1" />
                      Start Review
                    </Button>
                  </motion.div>
                ))}
              </motion.div>

              {upcomingCount > 0 && (
                <motion.div
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  transition={{ delay: 0.6 }}
                  className="mt-4 p-3 rounded-lg bg-teal-100/50 dark:bg-teal-900/20 border border-teal-200 dark:border-teal-800"
                >
                  <p className="text-xs text-center text-muted-foreground">
                    📅 {upcomingCount} more revision{upcomingCount !== 1 ? 's' : ''} in the next 7 days
                  </p>
                </motion.div>
              )}
            </>
          )}
        </CardContent>
      </Card>
    </motion.div>
  );
}
