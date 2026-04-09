"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { motion } from "framer-motion";
import { 
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
      <div className="space-y-8 p-2">
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
      <div className="space-y-8 p-2">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center py-12"
        >
          <AlertCircle className="h-12 w-12 text-red-500 mx-auto mb-4" />
          <h2 className="text-xl font-semibold text-red-600 mb-2">
            Error Loading Revisions
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
        className="flex items-center justify-between"
      >
        <div>
          <h1 className="text-4xl font-bold bg-gradient-to-r from-teal-600 via-emerald-500 to-green-500 bg-clip-text text-transparent">
            Revision Schedule
          </h1>
          <p className="text-muted-foreground mt-2 text-lg">
            Spaced repetition system for long-term retention
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Badge variant="outline" className="gap-2">
            <RotateCcw className="h-3 w-3" />
            {todayRevisions.length} Due Today
          </Badge>
          <Badge variant="outline" className="gap-2">
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
          <Card className="bg-gradient-to-br from-teal-100 via-emerald-50 to-green-100 border-2 border-teal-300 shadow-xl shadow-teal-500/20 h-fit">
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-teal-700">
                <Calendar className="h-5 w-5" />
                Today's Reviews ({todayRevisions.length})
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              {todayRevisions.slice(0, 5).map((revision) => (
                <div key={revision.id} className="flex items-center justify-between p-3 bg-white/70 rounded-lg border border-teal-200">
                  <div className="flex-1">
                    <button
                      type="button"
                      onClick={() => router.push(`/problems/${revision.problemId}?from=revision`)}
                      className="font-medium text-left hover:underline text-teal-800"
                    >
                      {revision.problemTitle}
                    </button>
                    <div className="flex items-center gap-2 mt-1">
                      <Badge className={getDifficultyColor(revision.difficulty)}>
                        {revision.difficulty}
                      </Badge>
                      <Badge variant="outline">{revision.pattern}</Badge>
                      <span className="text-xs text-teal-600">
                        {getRevisionIcon(revision.repetition)} Rep {revision.repetition + 1}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
              {todayRevisions.length === 0 && (
                <div className="text-center py-8">
                  <Sparkles className="h-8 w-8 text-teal-500 mx-auto mb-2" />
                  <p className="text-teal-700 font-medium">All caught up for today!</p>
                  <p className="text-sm text-teal-600">Great work on staying consistent.</p>
                </div>
              )}
            </CardContent>
          </Card>
        </motion.div>

        {/* Upcoming Reviews */}
        <motion.div variants={staggerItem}>
          <Card className="bg-gradient-to-br from-purple-50 to-pink-50 border-2 border-purple-200 shadow-xl shadow-purple-500/20 h-fit">
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-purple-700">
                <Clock className="h-5 w-5" />
                Upcoming This Week ({upcomingRevisions.length})
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              {upcomingRevisions.slice(0, 8).map((revision) => (
                <div
                  key={revision.id}
                  className="flex items-center justify-between p-3 bg-white/70 rounded-lg border border-purple-200"
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
                  <RotateCcw className="h-12 w-12 text-emerald-400 mx-auto mb-4" />
                  <h3 className="text-lg font-semibold text-emerald-600 mb-2">
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