"use client";

import { useEffect, useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { motion } from "framer-motion";
import { format, parseISO } from "date-fns";
import {
  AlertCircle,
  Calendar,
  Check,
  CheckCircle2,
  Clock,
  Plus,
  RefreshCw,
  Sparkles,
  Target,
  X,
  XCircle,
} from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";
import { staggerContainer, staggerItem } from "@/lib/animations";

interface ApiMockInterview {
  id: number;
  date: string;
  timeLimit: number;
  timeTakenSeconds: number | null;
  solved: boolean;
  explanationScore: number | null;
  codeQualityScore: number | null;
  overallScore: number | null;
  problem: {
    title: string;
    difficulty: string;
  };
}

interface CreateMockForm {
  date: string;
  problemTitle: string;
  timeLimitMinutes: number;
}

interface InterviewStats {
  totalCompleted: number;
  passRate: number;
}

const DEFAULT_FORM: CreateMockForm = {
  date: new Date().toISOString().slice(0, 10),
  problemTitle: "",
  timeLimitMinutes: 45,
};

const formatDuration = (seconds: number | null): string => {
  if (!seconds || seconds <= 0) return "-";

  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  return `${mins}m ${secs}s`;
};

const getDifficultyBadge = (difficulty: string) => {
  const normalized = difficulty.toLowerCase();

  if (normalized === "easy") {
    return "bg-emerald-100 text-emerald-700 border-emerald-300";
  }
  if (normalized === "medium") {
    return "bg-amber-100 text-amber-700 border-amber-300";
  }
  if (normalized === "hard") {
    return "bg-red-100 text-red-700 border-red-300";
  }
  return "bg-slate-100 text-slate-700 border-slate-300";
};

export default function MockPage() {
  const router = useRouter();

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const [form, setForm] = useState<CreateMockForm>(DEFAULT_FORM);
  const [interviews, setInterviews] = useState<ApiMockInterview[]>([]);

  const completedInterviews = useMemo(
    () => interviews.filter((interview) => interview.timeTakenSeconds !== null),
    [interviews]
  );

  const stats: InterviewStats = useMemo(() => {
    const totalCompleted = completedInterviews.length;
    const passed = completedInterviews.filter((interview) => interview.solved).length;
    const passRate = totalCompleted > 0 ? (passed / totalCompleted) * 100 : 0;

    return {
      totalCompleted,
      passRate,
    };
  }, [completedInterviews]);

  const fetchInterviews = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await fetch("/api/mock-interviews?limit=100");

      if (!response.ok) {
        throw new Error("Failed to load mock interviews");
      }

      const data = await response.json();
      setInterviews(data.mockInterviews || []);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load mock interviews");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchInterviews();
  }, []);

  const handleCreateMock = async () => {
    if (!form.problemTitle.trim()) {
      toast.error("Problem title is required");
      return;
    }

    if (!form.date) {
      toast.error("Date is required");
      return;
    }

    if (!Number.isInteger(form.timeLimitMinutes) || form.timeLimitMinutes <= 0) {
      toast.error("Time limit must be a positive integer");
      return;
    }

    try {
      setSaving(true);
      const response = await fetch("/api/mock-interviews", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          date: form.date,
          problemTitle: form.problemTitle.trim(),
          timeLimit: form.timeLimitMinutes,
          solved: false,
          timeTakenSeconds: null,
          explanationScore: null,
          codeQualityScore: null,
        }),
      });

      if (!response.ok) {
        const payload = await response.json().catch(() => ({}));
        throw new Error(payload.error || "Failed to create mock interview");
      }

      const payload = await response.json();
      const createdId = payload?.mockInterview?.id;

      if (!createdId) {
        throw new Error("Mock created but no interview ID was returned");
      }

      setIsCreateOpen(false);
      setForm(DEFAULT_FORM);
      toast.success("Mock interview created");
      router.push(`/mock/${createdId}`);
    } catch (err) {
      toast.error(err instanceof Error ? err.message : "Failed to create mock interview");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      <motion.div
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.3 }}
        className="flex items-center justify-between gap-4"
      >
        <div>
          <h1 className="text-4xl font-bold">
            <span className="text-gradient-purple-pink">Mock Interviews</span>
          </h1>
          <p className="text-muted-foreground mt-2 text-base">
            <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
            Practice, evaluate, and track interview performance under pressure
          </p>
        </div>

        <Dialog open={isCreateOpen} onOpenChange={setIsCreateOpen}>
          <DialogTrigger asChild>
            <Button className="relative gap-2 bg-gradient-purple-pink hover:shadow-md transition-all duration-300 border-0 text-white font-semibold">
              <Plus className="h-4 w-4" />
              Create Mock Interview
            </Button>
          </DialogTrigger>
          <DialogContent className="w-full max-w-[650px] min-w-[320px] sm:min-w-[500px] lg:min-w-[600px] max-h-[90vh] overflow-hidden bg-white/90 backdrop-blur-md border border-gray-200/60 rounded-2xl shadow-xl mx-4">
            <div className="relative z-10">
              <DialogHeader className="pb-6 border-b border-gray-200">
                <DialogTitle className="text-2xl font-semibold text-gray-900">Create Mock Interview</DialogTitle>
              </DialogHeader>

              <div className="space-y-6 pt-8 pb-2">
              <div className="space-y-2">
                <Label htmlFor="mock-date" className="text-sm font-medium text-foreground">Date</Label>
                <Input
                  id="mock-date"
                  type="date"
                  value={form.date}
                  onChange={(e) => setForm((prev) => ({ ...prev, date: e.target.value }))}
                  className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="mock-problem-title" className="text-sm font-medium text-foreground">Problem Title</Label>
                <Input
                  id="mock-problem-title"
                  placeholder="e.g. Course Schedule"
                  value={form.problemTitle}
                  onChange={(e) => setForm((prev) => ({ ...prev, problemTitle: e.target.value }))}
                  className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="mock-time-limit" className="text-sm font-medium text-foreground">Time Limit (minutes)</Label>
                <Input
                  id="mock-time-limit"
                  type="number"
                  min={1}
                  value={form.timeLimitMinutes}
                  onChange={(e) =>
                    setForm((prev) => ({
                      ...prev,
                      timeLimitMinutes: Number(e.target.value),
                    }))
                  }
                  className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                />
              </div>

              <div className="flex gap-4 pt-2">
                <Button
                  onClick={handleCreateMock}
                  disabled={saving}
                  className="flex-1 bg-gradient-purple-pink hover:shadow-md transition-all duration-300 disabled:opacity-50"
                >
                  <Check className="h-4 w-4 mr-2" />
                  {saving ? "Creating..." : "Create and Start"}
                </Button>
                <Button type="button" variant="outline" onClick={() => setIsCreateOpen(false)}>
                  <X className="h-4 w-4 mr-2" />
                  Cancel
                </Button>
              </div>
              </div>
            </div>
          </DialogContent>
        </Dialog>
      </motion.div>

      {loading ? (
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Skeleton className="h-36" />
            <Skeleton className="h-36" />
          </div>
          <Skeleton className="h-96" />
        </div>
      ) : error ? (
        <Card className="bg-gradient-to-br from-white to-purple-50/30 border-2 border-purple-200/50 shadow-xl shadow-purple-500/10">
          <CardContent className="flex flex-col items-center justify-center py-16">
            <div className="flex h-16 w-16 items-center justify-center rounded-full bg-red-100">
              <AlertCircle className="h-8 w-8 text-red-500" />
            </div>
            <h3 className="mt-4 text-lg font-semibold text-foreground">Error Loading Mock Interviews</h3>
            <p className="mt-2 text-sm text-muted-foreground">{error}</p>
            <Button variant="outline" className="mt-4" onClick={fetchInterviews}>
              <RefreshCw className="h-4 w-4 mr-2" />
              Retry
            </Button>
          </CardContent>
        </Card>
      ) : (
        <>
          <motion.div
            variants={staggerContainer}
            initial="hidden"
            animate="visible"
            className="grid grid-cols-1 md:grid-cols-2 gap-6"
          >
            <motion.div variants={staggerItem}>
              <Card className="bg-gradient-to-br from-pink-100 via-purple-50 to-fuchsia-100 border-2 border-pink-200/60 overflow-hidden relative">
                <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-pink-400/30 to-purple-400/30 rounded-full blur-2xl" />
                <CardHeader className="pb-3 relative">
                  <CardTitle className="text-base flex items-center gap-2">
                    <Target className="h-4 w-4 text-pink-700" />
                    Total Completed
                  </CardTitle>
                </CardHeader>
                <CardContent className="relative">
                  <div className="text-4xl font-bold bg-gradient-to-r from-pink-600 to-purple-600 bg-clip-text text-transparent">
                    {stats.totalCompleted}
                  </div>
                  <p className="text-sm text-muted-foreground mt-1">Mocks with submission data</p>
                </CardContent>
              </Card>
            </motion.div>

            <motion.div variants={staggerItem}>
              <Card className="bg-gradient-to-br from-emerald-100 via-green-50 to-teal-100 border-2 border-emerald-200/60 overflow-hidden relative">
                <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-emerald-400/30 to-teal-400/30 rounded-full blur-2xl" />
                <CardHeader className="pb-3 relative">
                  <CardTitle className="text-base flex items-center gap-2">
                    <CheckCircle2 className="h-4 w-4 text-emerald-700" />
                    Pass Rate
                  </CardTitle>
                </CardHeader>
                <CardContent className="relative">
                  <div className="text-4xl font-bold bg-gradient-to-r from-emerald-600 to-teal-600 bg-clip-text text-transparent">
                    {Math.round(stats.passRate)}%
                  </div>
                  <p className="text-sm text-muted-foreground mt-1">
                    Calculated on completed mock interviews only
                  </p>
                </CardContent>
              </Card>
            </motion.div>
          </motion.div>

          <Card className="bg-gradient-to-br from-white to-purple-50/30 border-2 border-purple-200/50 shadow-xl shadow-purple-500/10 overflow-hidden">
            <CardHeader className="border-b border-purple-100/80">
              <CardTitle className="flex items-center gap-2 text-lg">
                <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-purple-500 to-pink-500 shadow-lg shadow-purple-500/30">
                  <Calendar className="h-4 w-4 text-white" />
                </div>
                <span className="bg-gradient-to-r from-purple-700 to-pink-600 bg-clip-text text-transparent">
                  Interview Items
                </span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              {interviews.length === 0 ? (
                <div className="text-center py-12 text-muted-foreground">
                  No mock interviews yet. Create your first one.
                </div>
              ) : (
                <div className="space-y-3">
                  {interviews.map((interview) => {
                    const isCompleted = interview.timeTakenSeconds !== null;
                    return (
                      <div
                        key={interview.id}
                        className="rounded-xl border-2 border-purple-100 bg-gradient-to-r from-white to-purple-50/40 p-4 transition-all duration-300 hover:border-purple-200 hover:shadow-md"
                      >
                        <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
                          <div className="space-y-1">
                            <div className="font-semibold text-lg">{interview.problem.title}</div>
                            <div className="text-sm text-muted-foreground flex items-center gap-3 flex-wrap">
                              <span className="inline-flex items-center gap-1">
                                <Calendar className="h-3.5 w-3.5" />
                                {format(parseISO(interview.date), "MMM d, yyyy")}
                              </span>
                              <span className="inline-flex items-center gap-1">
                                <Clock className="h-3.5 w-3.5" />
                                Limit: {Math.round(interview.timeLimit / 60)}m
                              </span>
                              {isCompleted && (
                                <span className="inline-flex items-center gap-1">
                                  <Clock className="h-3.5 w-3.5" />
                                  Taken: {formatDuration(interview.timeTakenSeconds)}
                                </span>
                              )}
                              <Badge variant="outline" className={getDifficultyBadge(interview.problem.difficulty)}>
                                {interview.problem.difficulty}
                              </Badge>
                            </div>
                          </div>

                          <div className="flex items-center gap-2 flex-wrap">
                            {!isCompleted && (
                              <Badge variant="outline" className="bg-amber-100 text-amber-700 border-amber-300">
                                <Clock className="h-3 w-3 mr-1" />
                                In Progress
                              </Badge>
                            )}
                            {isCompleted && interview.solved && (
                              <Badge variant="outline" className="bg-emerald-100 text-emerald-700 border-emerald-300">
                                <CheckCircle2 className="h-3 w-3 mr-1" />
                                Passed
                              </Badge>
                            )}
                            {isCompleted && !interview.solved && (
                              <Badge variant="outline" className="bg-red-100 text-red-700 border-red-300">
                                <XCircle className="h-3 w-3 mr-1" />
                                Failed
                              </Badge>
                            )}

                            <Button
                              size="sm"
                              variant={isCompleted ? "outline" : "default"}
                              className={
                                isCompleted
                                  ? "border-purple-200 hover:bg-purple-50 text-purple-700"
                                  : "bg-gradient-purple-pink hover:shadow-md"
                              }
                              onClick={() => router.push(`/mock/${interview.id}`)}
                            >
                              {isCompleted ? "View Details" : "Continue"}
                            </Button>
                          </div>
                        </div>

                        {isCompleted && (
                          <div className="mt-3 flex items-center gap-2 text-sm text-muted-foreground">
                            {interview.solved ? (
                              <CheckCircle2 className="h-4 w-4 text-emerald-600" />
                            ) : (
                              <XCircle className="h-4 w-4 text-red-500" />
                            )}
                            <span>
                              Explanation: {interview.explanationScore ?? "-"} / 5, Code Quality: {interview.codeQualityScore ?? "-"} / 5
                            </span>
                          </div>
                        )}
                      </div>
                    );
                  })}
                </div>
              )}
            </CardContent>
          </Card>
        </>
      )}
    </div>
  );
}
