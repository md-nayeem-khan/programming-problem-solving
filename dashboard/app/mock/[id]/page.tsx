"use client";

import { FormEvent, useEffect, useMemo, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { motion } from "framer-motion";
import {
  AlertCircle,
  ArrowLeft,
  Check,
  CheckCircle2,
  Clock,
  Pause,
  Play,
  Plus,
  RotateCcw,
  Save,
  Sparkles,
  X,
  XCircle,
} from "lucide-react";
import { toast } from "sonner";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { Textarea } from "@/components/ui/textarea";
import { staggerItem } from "@/lib/animations";

interface MockInterviewDetails {
  id: number;
  date: string;
  timeLimit: number;
  timeTakenSeconds: number | null;
  solved: boolean;
  explanationScore: number | null;
  codeQualityScore: number | null;
  overallScore: number | null;
  notes: string | null;
  problem: {
    title: string;
    difficulty: string;
    patterns: string[];
  };
}

interface SubmissionForm {
  timeTakenMinutes: number;
  solved: boolean;
  explanationScore: number;
  codeQualityScore: number;
  notes: string;
}

const formatClock = (seconds: number): string => {
  const hrs = Math.floor(seconds / 3600);
  const mins = Math.floor((seconds % 3600) / 60);
  const secs = seconds % 60;

  if (hrs > 0) {
    return `${hrs}h ${mins}m ${secs}s`;
  }
  if (mins > 0) {
    return `${mins}m ${secs}s`;
  }
  return `${secs}s`;
};

export default function MockInterviewDetailsPage() {
  const params = useParams();
  const router = useRouter();

  const mockId = params.id as string;

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [mock, setMock] = useState<MockInterviewDetails | null>(null);

  const [isRunning, setIsRunning] = useState(false);
  const [timerStartAt, setTimerStartAt] = useState<number | null>(null);
  const [elapsedSeconds, setElapsedSeconds] = useState(0);

  const [isSubmitOpen, setIsSubmitOpen] = useState(false);
  const [form, setForm] = useState<SubmissionForm>({
    timeTakenMinutes: 0,
    solved: true,
    explanationScore: 3,
    codeQualityScore: 3,
    notes: "",
  });

  const isCompleted = useMemo(() => mock?.timeTakenSeconds !== null, [mock]);

  const fetchMockDetails = async () => {
    try {
      setLoading(true);
      setError(null);

      const response = await fetch(`/api/mock-interviews/${mockId}`);
      if (!response.ok) {
        const payload = await response.json().catch(() => ({}));
        throw new Error(payload.error || "Failed to load mock interview");
      }

      const data: MockInterviewDetails = await response.json();
      setMock(data);

      if (data.timeTakenSeconds) {
        setElapsedSeconds(data.timeTakenSeconds);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load mock interview");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (mockId) {
      fetchMockDetails();
    }
  }, [mockId]);

  useEffect(() => {
    let intervalId: NodeJS.Timeout | null = null;

    if (isRunning && timerStartAt) {
      intervalId = setInterval(() => {
        const diff = Math.floor((Date.now() - timerStartAt) / 1000);
        setElapsedSeconds(diff);
      }, 1000);
    }

    return () => {
      if (intervalId) {
        clearInterval(intervalId);
      }
    };
  }, [isRunning, timerStartAt]);

  const startTimer = () => {
    if (isCompleted) return;
    setIsRunning(true);
    setTimerStartAt(Date.now() - elapsedSeconds * 1000);
  };

  const pauseTimer = () => {
    setIsRunning(false);
    setTimerStartAt(null);
  };

  const resetTimer = () => {
    if (isCompleted) return;
    setIsRunning(false);
    setTimerStartAt(null);
    setElapsedSeconds(0);
  };

  const openSubmitModal = () => {
    if (isCompleted) return;

    if (isRunning) {
      pauseTimer();
    }

    setForm((prev) => ({
      ...prev,
      timeTakenMinutes: elapsedSeconds > 0 ? Math.ceil(elapsedSeconds / 60) : prev.timeTakenMinutes,
    }));

    setIsSubmitOpen(true);
  };

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();

    if (!mock) return;

    if (!Number.isInteger(form.timeTakenMinutes) || form.timeTakenMinutes <= 0) {
      toast.error("Time taken must be a positive integer");
      return;
    }

    if (form.explanationScore < 1 || form.explanationScore > 5) {
      toast.error("Explanation score must be between 1 and 5");
      return;
    }

    if (form.codeQualityScore < 1 || form.codeQualityScore > 5) {
      toast.error("Code quality score must be between 1 and 5");
      return;
    }

    try {
      setSaving(true);
      const response = await fetch(`/api/mock-interviews/${mock.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          timeTakenSeconds: form.timeTakenMinutes * 60,
          solved: form.solved,
          explanationScore: form.explanationScore,
          codeQualityScore: form.codeQualityScore,
          notes: form.notes.trim() || null,
        }),
      });

      if (!response.ok) {
        const payload = await response.json().catch(() => ({}));
        throw new Error(payload.error || "Failed to save mock interview");
      }

      toast.success("Mock interview submitted");
      setIsSubmitOpen(false);
      router.push("/mock");
      router.refresh();
    } catch (err) {
      toast.error(err instanceof Error ? err.message : "Failed to save mock interview");
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="space-y-8 max-w-7xl mx-auto">
        <Card className="bg-gradient-to-br from-white to-purple-50/30 border-2 border-purple-200/50 shadow-xl shadow-purple-500/10">
          <CardContent className="py-12 text-center text-muted-foreground">Loading mock interview...</CardContent>
        </Card>
      </div>
    );
  }

  if (error || !mock) {
    return (
      <div className="space-y-8 max-w-7xl mx-auto">
        <Card className="bg-gradient-to-br from-white to-purple-50/30 border-2 border-purple-200/50 shadow-xl shadow-purple-500/10">
          <CardContent className="flex flex-col items-center justify-center py-16">
            <div className="flex h-16 w-16 items-center justify-center rounded-full bg-red-100">
              <AlertCircle className="h-8 w-8 text-red-500" />
            </div>
            <h3 className="mt-4 text-lg font-semibold text-foreground">Error Loading Mock Interview</h3>
            <p className="mt-2 text-sm text-muted-foreground">{error || "Mock interview not found"}</p>
            <Button className="mt-4" variant="outline" onClick={() => router.push("/mock")}>
              Back to Mock Interviews
            </Button>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      <motion.div
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.3 }}
        className="flex items-start justify-between gap-4"
      >
        <div className="flex items-start gap-3">
          <Button
            variant="outline"
            size="icon"
            onClick={() => router.push("/mock")}
            className="border-purple-200 hover:bg-purple-100"
          >
            <ArrowLeft className="h-4 w-4" />
          </Button>
          <div>
            <h1 className="text-4xl font-bold">
              <span className="text-gradient-purple-pink">{mock.problem.title}</span>
            </h1>
            <p className="text-muted-foreground mt-2 text-base">
              <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
              Mock Interview Details and performance summary
            </p>
          </div>
        </div>

        {!isCompleted && (
          <Badge variant="secondary" className="mt-1">
            In Progress
          </Badge>
        )}
      </motion.div>

      {!isCompleted ? (
        <motion.div variants={staggerItem} initial="hidden" animate="visible">
          <Card className="border-gradient-to-r from-emerald-200 to-teal-200 dark:from-emerald-900/50 dark:to-teal-900/50 shadow-lg shadow-emerald-500/10">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Clock className="h-5 w-5 text-emerald-600" />
                Solving Timer
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="text-center">
                <div className="flex items-center justify-center gap-4 mb-4">
                  <div className="text-4xl font-mono font-bold text-emerald-600 dark:text-emerald-400">
                    {formatClock(elapsedSeconds)}
                  </div>
                  <Button onClick={resetTimer} variant="outline" size="icon" disabled={elapsedSeconds === 0}>
                    <RotateCcw className="h-4 w-4" />
                  </Button>
                </div>
              </div>

              <div className="flex justify-center gap-3 w-full max-w-sm mx-auto">
                {!isRunning ? (
                  <Button onClick={startTimer} className="flex-1 bg-emerald-600 hover:bg-emerald-700">
                    <Play className="h-4 w-4 mr-2" />
                    Start
                  </Button>
                ) : (
                  <Button onClick={pauseTimer} className="flex-1" variant="outline">
                    <Pause className="h-4 w-4 mr-2" />
                    Pause
                  </Button>
                )}

                <Button
                  onClick={openSubmitModal}
                  className="flex-1 bg-gradient-to-r from-blue-600 to-cyan-600 hover:from-blue-700 hover:to-cyan-700"
                >
                  <Plus className="h-4 w-4 mr-2" />
                  Submit
                </Button>
              </div>
            </CardContent>
          </Card>
        </motion.div>
      ) : (
        <Card className="bg-gradient-to-br from-white to-purple-50/30 border-2 border-purple-200/50 shadow-xl shadow-purple-500/10 overflow-hidden">
          <CardHeader className="border-b border-purple-100/80">
            <CardTitle className="flex items-center gap-2 text-lg">
              <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-purple-500 to-pink-500 shadow-lg shadow-purple-500/30">
                <CheckCircle2 className="h-4 w-4 text-white" />
              </div>
              <span className="bg-gradient-to-r from-purple-700 to-pink-600 bg-clip-text text-transparent">
                Mock Interview Details
              </span>
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex items-center gap-2">
              {mock.solved ? (
                <Badge variant="outline" className="bg-emerald-100 text-emerald-700 border-emerald-300">
                  <CheckCircle2 className="h-3.5 w-3.5 mr-1" />
                  Passed
                </Badge>
              ) : (
                <Badge variant="outline" className="bg-red-100 text-red-700 border-red-300">
                  <XCircle className="h-3.5 w-3.5 mr-1" />
                  Failed
                </Badge>
              )}
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <Card className="bg-gradient-to-br from-blue-100 via-indigo-50 to-violet-100 border-2 border-blue-200/60 overflow-hidden relative">
                <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-blue-400/25 to-indigo-400/25 rounded-full blur-2xl" />
                <CardContent className="p-4">
                  <p className="text-xs text-muted-foreground">Time Taken</p>
                  <p className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
                    {Math.round((mock.timeTakenSeconds || 0) / 60)}m
                  </p>
                </CardContent>
              </Card>
              <Card className="bg-gradient-to-br from-amber-100 via-yellow-50 to-orange-100 border-2 border-amber-200/60 overflow-hidden relative">
                <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-amber-400/25 to-orange-400/25 rounded-full blur-2xl" />
                <CardContent className="p-4">
                  <p className="text-xs text-muted-foreground">Explanation Score</p>
                  <p className="text-2xl font-bold bg-gradient-to-r from-amber-600 to-orange-600 bg-clip-text text-transparent">
                    {mock.explanationScore ?? "-"} / 5
                  </p>
                </CardContent>
              </Card>
              <Card className="bg-gradient-to-br from-pink-100 via-purple-50 to-fuchsia-100 border-2 border-pink-200/60 overflow-hidden relative">
                <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-pink-400/25 to-purple-400/25 rounded-full blur-2xl" />
                <CardContent className="p-4">
                  <p className="text-xs text-muted-foreground">Code Quality Score</p>
                  <p className="text-2xl font-bold bg-gradient-to-r from-pink-600 to-purple-600 bg-clip-text text-transparent">
                    {mock.codeQualityScore ?? "-"} / 5
                  </p>
                </CardContent>
              </Card>
            </div>

            {mock.notes && (
              <div className="rounded-lg border-2 border-purple-100 bg-gradient-to-r from-white to-purple-50/50 p-4">
                <p className="text-sm font-medium mb-1">Notes</p>
                <p className="text-sm text-muted-foreground whitespace-pre-wrap">{mock.notes}</p>
              </div>
            )}
          </CardContent>
        </Card>
      )}

      <Dialog open={isSubmitOpen} onOpenChange={setIsSubmitOpen}>
        <DialogContent className="w-full max-w-[650px] min-w-[320px] sm:min-w-[500px] lg:min-w-[600px] max-h-[90vh] overflow-hidden bg-white/90 backdrop-blur-md border border-gray-200/60 rounded-2xl shadow-xl mx-4">
          <DialogHeader className="pb-6 border-b border-gray-200">
            <DialogTitle className="text-2xl font-semibold text-gray-900">Submit Mock Interview</DialogTitle>
          </DialogHeader>

          <form onSubmit={handleSubmit} className="space-y-6 pt-8 pb-2 overflow-y-auto overflow-x-hidden max-h-[75vh] pr-2 w-full">
            <div className="space-y-2">
              <Label htmlFor="timeTakenMinutes" className="text-sm font-medium text-foreground">Time Taken (minutes)</Label>
              <Input
                id="timeTakenMinutes"
                type="number"
                min={1}
                value={form.timeTakenMinutes}
                onChange={(e) =>
                  setForm((prev) => ({
                    ...prev,
                    timeTakenMinutes: Number(e.target.value),
                  }))
                }
                className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
              />
            </div>

            <div className="flex items-center justify-between rounded-lg border border-purple-200 p-3 bg-white/80">
              <div>
                <p className="font-medium">Solved</p>
                <p className="text-sm text-muted-foreground">Did you finish the problem successfully?</p>
              </div>
              <Switch
                checked={form.solved}
                onCheckedChange={(checked) => setForm((prev) => ({ ...prev, solved: checked }))}
              />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div className="space-y-2">
                <Label htmlFor="explanationScore" className="text-sm font-medium text-foreground">Explanation Score (1-5)</Label>
                <Input
                  id="explanationScore"
                  type="number"
                  min={1}
                  max={5}
                  value={form.explanationScore}
                  onChange={(e) =>
                    setForm((prev) => ({
                      ...prev,
                      explanationScore: Number(e.target.value),
                    }))
                  }
                  className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="codeQualityScore" className="text-sm font-medium text-foreground">Code Quality Score (1-5)</Label>
                <Input
                  id="codeQualityScore"
                  type="number"
                  min={1}
                  max={5}
                  value={form.codeQualityScore}
                  onChange={(e) =>
                    setForm((prev) => ({
                      ...prev,
                      codeQualityScore: Number(e.target.value),
                    }))
                  }
                  className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                />
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="notes" className="text-sm font-medium text-foreground">Notes (optional)</Label>
              <Textarea
                id="notes"
                value={form.notes}
                onChange={(e) => setForm((prev) => ({ ...prev, notes: e.target.value }))}
                placeholder="Brief feedback about your performance"
                rows={3}
                className="min-h-[80px] resize-none !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
              />
            </div>

            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setIsSubmitOpen(false)}>
                <X className="h-4 w-4 mr-2" />
                Cancel
              </Button>
              <Button
                type="submit"
                disabled={saving}
                className="bg-gradient-purple-pink hover:shadow-md transition-all duration-300 disabled:opacity-50"
              >
                {saving ? <Save className="h-4 w-4 mr-2" /> : <Check className="h-4 w-4 mr-2" />}
                {saving ? "Saving..." : "Save Interview"}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
