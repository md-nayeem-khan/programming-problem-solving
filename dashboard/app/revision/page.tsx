"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { motion } from "framer-motion";
import { 
  RefreshCw,
  Calendar,
  RotateCcw,
  Clock,
  Sparkles,
  AlertCircle,
} from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";
import {
  staggerContainer,
  staggerItem,
} from "@/lib/animations";
import { format, parseISO, isToday, isTomorrow, addDays } from "date-fns";

interface RevisionItem {
  id: number;
  problemId: number;
  problemTitle: string;
  pattern: string;
  difficulty: "Easy" | "Medium" | "Hard";
  dueDate: string;
  interval: number;
  repetition: number;
  isOverdue: boolean;
  company?: string;
}

export default function RevisionPage() {
  const router = useRouter();
  const [revisions, setRevisions] = useState<RevisionItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchRevisions() {
      try {
        const response = await fetch("/api/revisions");
        if (!response.ok) throw new Error("Failed to fetch revisions");
        
        const data = await response.json();
        
        // The API returns { dueToday: [], upcoming: [], statistics: {} }
        const allRevisions = [...(data.dueToday || []), ...(data.upcoming || [])];
        
        const startOfToday = new Date();
        startOfToday.setHours(0, 0, 0, 0);

        const revisionItems: RevisionItem[] = allRevisions.map((item: any) => ({
          id: item.id,
          problemId: item.submission?.problem?.id || item.problemId,
          problemTitle: item.submission?.problem?.title || `Problem ${item.problemId}`,
          pattern: item.submission?.problem?.patterns?.[0]?.pattern?.name || "Unknown",
          difficulty: item.submission?.problem?.difficulty || "Medium",
          dueDate: item.nextReviewDate,
          interval: item.intervalLevel || 0,
          repetition: item.repetitionCount || 0,
          isOverdue: new Date(item.nextReviewDate) < startOfToday,
          company: item.submission?.problem?.company,
        }));
        
        setRevisions(revisionItems);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load revisions");
      } finally {
        setLoading(false);
      }
    }

    fetchRevisions();
  }, []);

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

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case "Easy": return "bg-green-100 text-green-700 border-green-300";
      case "Medium": return "bg-amber-100 text-amber-700 border-amber-300";
      case "Hard": return "bg-red-100 text-red-700 border-red-300";
      default: return "bg-gray-100 text-gray-700 border-gray-300";
    }
  };

  const getRevisionIcon = (repetition: number) => {
    if (repetition === 0) return "🆕";
    if (repetition === 1) return "🔄";
    if (repetition === 2) return "💪";
    return "🎯";
  };

  const todayRevisions = revisions.filter(r => 
    isToday(parseISO(r.dueDate)) || r.isOverdue
  );

  const upcomingRevisions = revisions.filter(r => {
    const dueDate = parseISO(r.dueDate);
    const startOfToday = new Date();
    startOfToday.setHours(0, 0, 0, 0);

    const startOfTomorrow = addDays(startOfToday, 1);
    const endOfWindow = addDays(startOfToday, 8);

    return dueDate >= startOfTomorrow && dueDate < endOfWindow;
  });

  if (loading) {
    return (
      <div className="space-y-8 max-w-7xl mx-auto">
        <Skeleton className="h-10 w-64 mb-2" />
        <Skeleton className="h-6 w-96 mb-6" />
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Skeleton className="h-96" />
          <Skeleton className="h-96" />
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="space-y-8 max-w-7xl mx-auto">
        <Card className="bg-gradient-to-br from-white to-purple-50/30 border-2 border-purple-200/50 shadow-xl shadow-purple-500/10">
          <CardContent className="flex flex-col items-center justify-center py-16">
            <div className="flex h-16 w-16 items-center justify-center rounded-full bg-red-100">
              <AlertCircle className="h-8 w-8 text-red-500" />
            </div>
            <h3 className="mt-4 text-lg font-semibold text-foreground">Error Loading Revisions</h3>
            <p className="mt-2 text-sm text-muted-foreground">{error}</p>
            <button
              type="button"
              onClick={() => window.location.reload()}
              className="mt-4 inline-flex items-center rounded-md border border-purple-200 px-3 py-2 text-sm font-medium text-purple-700 hover:bg-purple-50"
            >
              <RefreshCw className="h-4 w-4 mr-2" />
              Retry
            </button>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="flex items-center justify-between"
      >
        <div>
          <h1 className="text-4xl font-bold">
            <span className="text-gradient-purple-pink">Revision Schedule</span>
          </h1>
          <p className="text-muted-foreground mt-2 text-base">
            <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
            Spaced repetition system for long-term retention
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Badge variant="outline" className="gap-2 bg-purple-100 text-purple-700 border-purple-300">
            <RotateCcw className="h-3 w-3" />
            {todayRevisions.length} Due Today
          </Badge>
          <Badge variant="outline" className="gap-2 bg-indigo-100 text-indigo-700 border-indigo-300">
            <Clock className="h-3 w-3" />
            {upcomingRevisions.length} This Week
          </Badge>
        </div>
      </motion.div>

      {/* Main Content */}
      <motion.div
        variants={staggerContainer}
        initial="hidden"
        animate="visible"
        className="grid grid-cols-1 lg:grid-cols-2 gap-8"
      >
        {/* Today's Reviews */}
        <motion.div variants={staggerItem}>
          <Card className="bg-gradient-to-br from-white to-purple-50/30 border-2 border-purple-200/50 shadow-xl shadow-purple-500/10 h-fit overflow-hidden">
            <CardHeader className="border-b border-purple-100/80">
              <CardTitle className="flex items-center gap-2 text-lg">
                <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-purple-500 to-pink-500 shadow-lg shadow-purple-500/30">
                  <Calendar className="h-4 w-4 text-white" />
                </div>
                <span className="bg-gradient-to-r from-purple-700 to-pink-600 bg-clip-text text-transparent">
                  Today's Reviews ({todayRevisions.length})
                </span>
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              {todayRevisions.slice(0, 5).map((revision) => (
                <div key={revision.id} className="flex items-center justify-between p-3 bg-gradient-to-r from-white to-purple-50/40 rounded-lg border-2 border-purple-100">
                  <div className="flex-1">
                    <button
                      type="button"
                      onClick={() => router.push(`/problems/${revision.problemId}?from=revision`)}
                      className="font-medium text-left hover:underline text-purple-800"
                    >
                      {revision.problemTitle}
                    </button>
                    <div className="flex items-center gap-2 mt-1">
                      <Badge className={getDifficultyColor(revision.difficulty)}>
                        {revision.difficulty}
                      </Badge>
                      <Badge variant="outline">{revision.pattern}</Badge>
                      <span className="text-xs text-purple-600">
                        {getRevisionIcon(revision.repetition)} Rep {revision.repetition + 1}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
              {todayRevisions.length === 0 && (
                <div className="text-center py-8">
                  <Sparkles className="h-8 w-8 text-purple-500 mx-auto mb-2" />
                  <p className="text-purple-700 font-medium">All caught up for today!</p>
                  <p className="text-sm text-purple-600">Great work on staying consistent.</p>
                </div>
              )}
            </CardContent>
          </Card>
        </motion.div>

        {/* Upcoming Reviews */}
        <motion.div variants={staggerItem}>
          <Card className="bg-gradient-to-br from-white to-purple-50/30 border-2 border-purple-200/50 shadow-xl shadow-purple-500/10 h-fit overflow-hidden">
            <CardHeader className="border-b border-purple-100/80">
              <CardTitle className="flex items-center gap-2 text-lg">
                <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-indigo-500 to-purple-500 shadow-lg shadow-indigo-500/30">
                  <Clock className="h-4 w-4 text-white" />
                </div>
                <span className="bg-gradient-to-r from-purple-700 to-pink-600 bg-clip-text text-transparent">
                  Upcoming This Week ({upcomingRevisions.length})
                </span>
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              {upcomingRevisions.slice(0, 8).map((revision) => (
                <div
                  key={revision.id}
                  className="flex items-center justify-between p-3 bg-gradient-to-r from-white to-purple-50/40 rounded-lg border-2 border-purple-100"
                >
                  <div className="flex-1">
                    <button
                      type="button"
                      onClick={() => router.push(`/problems/${revision.problemId}?from=revision`)}
                      className="font-medium text-sm text-left hover:underline text-purple-800"
                    >
                      {revision.problemTitle}
                    </button>
                    <div className="flex items-center gap-2 mt-1">
                      <Badge className={getDifficultyColor(revision.difficulty)}>
                        {revision.difficulty}
                      </Badge>
                      <Badge variant="outline">{revision.pattern}</Badge>
                      <span className="text-xs text-muted-foreground">
                        📅 {getDateLabel(revision.dueDate, revision.isOverdue)}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
              
              {upcomingRevisions.length === 0 && (
                <div className="text-center py-8">
                  <RotateCcw className="h-12 w-12 text-purple-400 mx-auto mb-4" />
                  <h3 className="text-lg font-semibold text-purple-600 mb-2">
                    All Up to Date!
                  </h3>
                  <p className="text-sm text-muted-foreground">
                    No upcoming revisions. Keep solving!
                  </p>
                </div>
              )}
            </CardContent>
          </Card>
        </motion.div>
      </motion.div>
    </div>
  );
}