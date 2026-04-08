"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { motion } from "framer-motion";
import {
  ArrowLeft,
  ExternalLink,
  Clock,
  Play,
  Pause,
  RotateCcw,
  CheckCircle2,
  XCircle,
  AlertCircle,
  BookOpen,
  Calendar,
  Target,
  Lightbulb,
  Save,
  Plus,
  Brain,
} from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Switch } from "@/components/ui/switch";
import { Checkbox } from "@/components/ui/checkbox";
import { Separator } from "@/components/ui/separator";
import {
  Dialog,
  DialogContent,
  DialogTrigger,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { toast } from "sonner";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  fadeInUp,
  staggerContainer,
  staggerItem,
  scaleIn,
} from "@/lib/animations";

// Types
interface Problem {
  id: number;
  title: string;
  platform: string;
  problemId: string;
  difficulty: "Easy" | "Medium" | "Hard";
  url?: string;
  company?: string;
  description?: string;
  patterns: Array<{
    id: number;
    name: string;
  }>;
  submissions: Array<{
    id: number;
    timeSpentMinutes: number;
    usedHints: boolean;
    approach?: string;
    notes?: string;
    passed: boolean;
    submittedAt: string;
  }>;
}

interface TimerState {
  isRunning: boolean;
  startTime: number | null;
  elapsedTime: number;
  totalTime: number;
}

interface NewSubmission {
  timeSpentMinutes: number;
  usedHints: boolean;
  approach: string;
  notes: string;
  passed: boolean;
}

// Utility functions
const formatTime = (seconds: number): string => {
  const hrs = Math.floor(seconds / 3600);
  const mins = Math.floor((seconds % 3600) / 60);
  const secs = seconds % 60;
  
  if (hrs > 0) {
    return `${hrs}h ${mins}m ${secs}s`;
  } else if (mins > 0) {
    return `${mins}m ${secs}s`;
  } else {
    return `${secs}s`;
  }
};

const formatDate = (dateString: string): string => {
  const date = new Date(dateString);
  return date.toLocaleDateString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const getDifficultyColor = (difficulty: string) => {
  switch (difficulty) {
    case "Easy":
      return "bg-emerald-100 text-emerald-800 border-emerald-200 dark:bg-emerald-900/20 dark:text-emerald-300 dark:border-emerald-800";
    case "Medium":
      return "bg-amber-100 text-amber-800 border-amber-200 dark:bg-amber-900/20 dark:text-amber-300 dark:border-amber-800";
    case "Hard":
      return "bg-red-100 text-red-800 border-red-200 dark:bg-red-900/20 dark:text-red-300 dark:border-red-800";
    default:
      return "bg-gray-100 text-gray-800 border-gray-200 dark:bg-gray-900/20 dark:text-gray-300 dark:border-gray-800";
  }
};

export default function ProblemDetailsPage() {
  const params = useParams();
  const router = useRouter();
  const problemId = params.id as string;

  const [problem, setProblem] = useState<Problem | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Timer state
  const [timer, setTimer] = useState<TimerState>({
    isRunning: false,
    startTime: null,
    elapsedTime: 0,
    totalTime: 0,
  });

  // New submission form state
  const [showSubmissionForm, setShowSubmissionForm] = useState(false);
  const [submissionForm, setSubmissionForm] = useState<NewSubmission>({
    timeSpentMinutes: 0,
    usedHints: false,
    approach: "",
    notes: "",
    passed: true,  // Default to solved/passed
  });
  const [submissionLoading, setSubmissionLoading] = useState(false);

  // Load problem data
  useEffect(() => {
    const fetchProblem = async () => {
      try {
        setLoading(true);
        const response = await fetch(`/api/problems/${problemId}`);
        if (!response.ok) {
          throw new Error("Problem not found");
        }
        const data = await response.json();
        setProblem(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load problem");
      } finally {
        setLoading(false);
      }
    };

    if (problemId) {
      fetchProblem();
    }
  }, [problemId]);

  // Timer effect
  useEffect(() => {
    let intervalId: NodeJS.Timeout;

    if (timer.isRunning && timer.startTime) {
      intervalId = setInterval(() => {
        const now = Date.now();
        const elapsed = Math.floor((now - timer.startTime!) / 1000);
        setTimer(prev => ({
          ...prev,
          elapsedTime: elapsed,
        }));
      }, 1000);
    }

    return () => {
      if (intervalId) {
        clearInterval(intervalId);
      }
    };
  }, [timer.isRunning, timer.startTime]);

  // Timer controls
  const startTimer = () => {
    setTimer(prev => ({
      ...prev,
      isRunning: true,
      startTime: Date.now() - prev.elapsedTime * 1000,
    }));
  };

  const pauseTimer = () => {
    setTimer(prev => ({
      ...prev,
      isRunning: false,
      startTime: null,
    }));
  };

  const resetTimer = () => {
    setTimer({
      isRunning: false,
      startTime: null,
      elapsedTime: 0,
      totalTime: 0,
    });
  };

  // Open submission form with timer auto-pause and time pre-fill
  const openSubmissionForm = () => {
    // Auto-pause the timer if it's running
    if (timer.isRunning) {
      setTimer(prev => ({
        ...prev,
        isRunning: false,
        startTime: null,
      }));
    }
    
    // Auto-fill the time spent if timer has elapsed time
    if (timer.elapsedTime > 0) {
      const minutes = Math.ceil(timer.elapsedTime / 60);
      setSubmissionForm(prev => ({
        ...prev,
        timeSpentMinutes: minutes,
      }));
      toast.success(`Time automatically set to ${minutes} minutes from timer`);
    }
    
    // Open the modal
    setShowSubmissionForm(true);
  };

  // Submit new attempt
  const handleSubmitAttempt = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!submissionForm.timeSpentMinutes) {
      toast.error("Please enter time spent or use the timer");
      return;
    }

    setSubmissionLoading(true);
    try {
      const response = await fetch("/api/submissions", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          problemId: parseInt(problemId),
          timeSpentSeconds: submissionForm.timeSpentMinutes * 60, // Convert to seconds
          status: submissionForm.passed ? 'solved' : 'failed',
          wasHintUsed: submissionForm.usedHints,
          approachNote: submissionForm.approach,
          notes: submissionForm.notes,
          attemptType: 'First',
          submittedAt: new Date().toISOString(),
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Failed to submit attempt");
      }

      toast.success("Attempt logged successfully!");
      setShowSubmissionForm(false);
      
      // Reset form and timer
      setSubmissionForm({
        timeSpentMinutes: 0,
        usedHints: false,
        approach: "",
        notes: "",
        passed: false,
      });
      resetTimer();

      // Refresh problem data
      const problemResponse = await fetch(`/api/problems/${problemId}`);
      if (problemResponse.ok) {
        const updatedProblem = await problemResponse.json();
        setProblem(updatedProblem);
      }
    } catch (err) {
      toast.error(err instanceof Error ? err.message : "Failed to submit attempt");
    } finally {
      setSubmissionLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="container max-w-7xl mx-auto p-6 space-y-6">
        <div className="animate-pulse space-y-6">
          <div className="h-8 bg-muted rounded w-1/4"></div>
          <div className="h-64 bg-muted rounded"></div>
          <div className="h-96 bg-muted rounded"></div>
        </div>
      </div>
    );
  }

  if (error || !problem) {
    return (
      <div className="container max-w-7xl mx-auto p-6">
        <Card className="border-destructive/50">
          <CardContent className="p-6 text-center">
            <AlertCircle className="h-12 w-12 text-destructive mx-auto mb-4" />
            <h2 className="text-xl font-semibold mb-2">Problem Not Found</h2>
            <p className="text-muted-foreground mb-4">
              {error || "The requested problem could not be found."}
            </p>
            <Button onClick={() => router.push("/problems")} variant="outline" size="icon">
              <ArrowLeft className="h-4 w-4" />
            </Button>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="container max-w-7xl mx-auto p-6 space-y-8">
      {/* Header with back button */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex items-center gap-4"
      >
        <Button
          variant="outline"
          size="icon"
          onClick={() => router.push("/problems")}
          className="border-purple-200 dark:border-purple-800/50 hover:bg-purple-100 dark:hover:bg-purple-900/30"
        >
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <div className="flex-1">
          <h1 className="text-3xl font-bold bg-gradient-to-r from-purple-600 to-pink-600 bg-clip-text text-transparent">
            {problem.title}
          </h1>
        </div>
      </motion.div>

      <div className="grid grid-cols-1 lg:grid-cols-5 gap-8">
        {/* Left Column - Timer & Submission History (3/5 width) */}
        <div className="lg:col-span-3 space-y-6">
          {/* Timer Card */}
          <motion.div
            variants={staggerItem}
            initial="hidden"
            animate="visible"
            transition={{ delay: 0.2 }}
          >
            <Card className="border-gradient-to-r from-emerald-200 to-teal-200 dark:from-emerald-900/50 dark:to-teal-900/50 shadow-lg shadow-emerald-500/10">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Clock className="h-5 w-5 text-emerald-600" />
                  Solving Timer
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                {/* Timer Display with Reset Button */}
                <div className="text-center">
                  <div className="flex items-center justify-center gap-4 mb-4">
                    <div className="text-4xl font-mono font-bold text-emerald-600 dark:text-emerald-400">
                      {formatTime(timer.elapsedTime)}
                    </div>
                    <Button onClick={resetTimer} variant="outline" size="icon" disabled={timer.elapsedTime === 0}>
                      <RotateCcw className="h-4 w-4" />
                    </Button>
                  </div>
                  
                  {/* Timer Controls */}
                  <div className="flex justify-center gap-3 w-full max-w-sm mx-auto">
                    {!timer.isRunning ? (
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
                    
                    <Dialog open={showSubmissionForm} onOpenChange={setShowSubmissionForm}>
                      <DialogTrigger asChild>
                        <Button 
                          onClick={openSubmissionForm}
                          className="flex-1 bg-gradient-to-r from-blue-600 to-cyan-600 hover:from-blue-700 hover:to-cyan-700"
                        >
                          <Plus className="h-4 w-4 mr-2" />
                          Submit
                        </Button>
                      </DialogTrigger>
                      
                      <DialogContent className="w-full max-w-[500px] max-h-[90vh] overflow-hidden bg-white/90 backdrop-blur-md border border-gray-200/60 rounded-2xl shadow-xl">
                        <motion.div
                          initial={{ opacity: 0, y: 20 }}
                          animate={{ opacity: 1, y: 0 }}
                          exit={{ opacity: 0, y: 20 }}
                          className="relative z-10"
                        >
                          {/* Header */}
                          <DialogHeader className="pb-6 border-b border-gray-200">
                            <DialogTitle className="text-2xl font-semibold text-gray-900 flex items-center gap-2">
                              <Save className="h-5 w-5 text-purple-600" />
                              Log Attempt
                            </DialogTitle>
                          </DialogHeader>

                          {/* Form */}
                          <form onSubmit={handleSubmitAttempt} className="space-y-6 pt-6 overflow-y-auto max-h-[70vh]">
                            {/* Time Input */}
                            <div className="space-y-2">
                              <Label htmlFor="timeSpent" className="text-sm font-medium text-foreground">
                                Time Spent (minutes) *
                              </Label>
                              <Input
                                id="timeSpent"
                                type="number"
                                min="1"
                                value={submissionForm.timeSpentMinutes || ""}
                                onChange={(e) =>
                                  setSubmissionForm(prev => ({
                                    ...prev,
                                    timeSpentMinutes: parseInt(e.target.value) || 0,
                                  }))
                                }
                                className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                                placeholder="Enter minutes"
                                required
                              />
                            </div>

                            {/* Status Checkboxes */}
                            <div className="flex items-center justify-between">
                              <div className="flex items-center space-x-3">
                                <Checkbox
                                  id="passed"
                                  checked={submissionForm.passed}
                                  onCheckedChange={(checked) =>
                                    setSubmissionForm(prev => ({ ...prev, passed: !!checked }))
                                  }
                                />
                                <Label htmlFor="passed" className="text-sm font-medium cursor-pointer">
                                  Solved
                                </Label>
                              </div>

                              <div className="flex items-center space-x-3">
                                <Checkbox
                                  id="usedHints"
                                  checked={submissionForm.usedHints}
                                  onCheckedChange={(checked) =>
                                    setSubmissionForm(prev => ({ ...prev, usedHints: !!checked }))
                                  }
                                />
                                <Label htmlFor="usedHints" className="text-sm font-medium cursor-pointer">
                                  Used Hints
                                </Label>
                              </div>
                            </div>

                            {/* Approach */}
                            <div className="space-y-2">
                              <Label htmlFor="approach" className="text-sm font-medium text-foreground">
                                Approach / Solution
                              </Label>
                              <Textarea
                                id="approach"
                                value={submissionForm.approach}
                                onChange={(e) =>
                                  setSubmissionForm(prev => ({ ...prev, approach: e.target.value }))
                                }
                                className="!border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                                placeholder="Describe your approach..."
                                rows={3}
                              />
                            </div>

                            {/* Notes */}
                            <div className="space-y-2">
                              <Label htmlFor="notes" className="text-sm font-medium text-foreground">
                                Additional Notes
                              </Label>
                              <Textarea
                                id="notes"
                                value={submissionForm.notes}
                                onChange={(e) =>
                                  setSubmissionForm(prev => ({ ...prev, notes: e.target.value }))
                                }
                                className="!border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                                placeholder="Any mistakes, learnings, or insights..."
                                rows={2}
                              />
                            </div>

                            {/* Actions */}
                            <div className="flex pt-4">
                              <Button
                                type="submit"
                                className="w-full bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-700 hover:to-pink-700 text-white font-semibold"
                                disabled={submissionLoading}
                              >
                                {submissionLoading ? "Submitting..." : "Log Attempt"}
                              </Button>
                            </div>
                          </form>
                        </motion.div>
                      </DialogContent>
                    </Dialog>
                  </div>
                </div>
              </CardContent>
            </Card>
          </motion.div>

          {/* Submission History */}
          <motion.div
            variants={staggerItem}
            initial="hidden"
            animate="visible"
            transition={{ delay: 0.1 }}
          >
            <Card className="border-gradient-to-r from-blue-200 to-cyan-200 dark:from-blue-900/50 dark:to-cyan-900/50 shadow-lg shadow-blue-500/10">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Calendar className="h-5 w-5 text-blue-600" />
                  Submission History ({problem.submissions.length})
                </CardTitle>
              </CardHeader>
              <CardContent>
                {problem.submissions.length === 0 ? (
                  <div className="text-center py-8">
                    <Target className="h-12 w-12 text-muted-foreground mx-auto mb-3" />
                    <p className="text-muted-foreground">
                      No attempts recorded yet. Start solving and log your first attempt!
                    </p>
                  </div>
                ) : (
                  <div className="overflow-x-auto">
                    <Table>
                      <TableHeader>
                        <TableRow>
                          <TableHead>Date</TableHead>
                          <TableHead>Result</TableHead>
                          <TableHead>Time</TableHead>
                          <TableHead>Hints</TableHead>
                          <TableHead>Notes</TableHead>
                        </TableRow>
                      </TableHeader>
                      <TableBody>
                        {problem.submissions.map((submission) => (
                          <TableRow key={submission.id}>
                            <TableCell className="font-medium">
                              {formatDate(submission.submittedAt)}
                            </TableCell>
                            <TableCell>
                              <Badge
                                variant={submission.passed ? "default" : "destructive"}
                                className={
                                  submission.passed
                                    ? "bg-emerald-100 text-emerald-800 border-emerald-200 dark:bg-emerald-900/20 dark:text-emerald-300"
                                    : "bg-red-100 text-red-800 border-red-200 dark:bg-red-900/20 dark:text-red-300"
                                }
                              >
                                {submission.passed ? (
                                  <>
                                    <CheckCircle2 className="h-3 w-3 mr-1" />
                                    Solved
                                  </>
                                ) : (
                                  <>
                                    <XCircle className="h-3 w-3 mr-1" />
                                    Failed
                                  </>
                                )}
                              </Badge>
                            </TableCell>
                            <TableCell>
                              <span className="flex items-center gap-1">
                                <Clock className="h-3 w-3 text-muted-foreground" />
                                {submission.timeSpentMinutes}m
                              </span>
                            </TableCell>
                            <TableCell>
                              {submission.usedHints ? (
                                <Badge variant="outline" className="text-amber-600 border-amber-300">
                                  <Lightbulb className="h-3 w-3 mr-1" />
                                  Used
                                </Badge>
                              ) : (
                                <span className="text-muted-foreground text-sm">None</span>
                              )}
                            </TableCell>
                            <TableCell>
                              <span className="text-sm text-muted-foreground max-w-xs truncate block">
                                {submission.approach || submission.notes || "—"}
                              </span>
                            </TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </div>
                )}
              </CardContent>
            </Card>
          </motion.div>
        </div>

        {/* Right Column - Problem Information Only (2/5 width) */}
        <div className="lg:col-span-2 space-y-6">
          {/* Problem Information Card */}
          <motion.div
            variants={staggerItem}
            initial="hidden"
            animate="visible"
          >
            <Card className="border-gradient-to-r from-purple-200 to-pink-200 dark:from-purple-900/50 dark:to-pink-900/50 shadow-lg shadow-purple-500/10">
              <CardHeader>
                <CardTitle className="flex items-center justify-between">
                  <span className="flex items-center gap-2">
                    <BookOpen className="h-5 w-5 text-purple-600" />
                    Problem Information
                  </span>
                  {problem.url && (
                    <Button variant="outline" size="icon" asChild>
                      <a href={problem.url} target="_blank" rel="noopener noreferrer">
                        <ExternalLink className="h-4 w-4" />
                      </a>
                    </Button>
                  )}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                {/* Platform */}
                <div>
                  <Label className="text-sm font-semibold text-muted-foreground mb-2 block">
                    Platform
                  </Label>
                  <Badge variant="outline" className="bg-blue-50 text-blue-700 border-blue-200 dark:bg-blue-900/20 dark:text-blue-300 dark:border-blue-800">
                    {problem.platform} • {problem.problemId}
                  </Badge>
                </div>

                {/* Difficulty */}
                <div>
                  <Label className="text-sm font-semibold text-muted-foreground mb-2 block">
                    Difficulty
                  </Label>
                  <Badge className={getDifficultyColor(problem.difficulty)}>
                    {problem.difficulty}
                  </Badge>
                </div>

                {/* Company */}
                {problem.company && (
                  <div>
                    <Label className="text-sm font-semibold text-muted-foreground mb-2 block">
                      Company
                    </Label>
                    <Badge variant="outline" className="bg-orange-50 text-orange-700 border-orange-200 dark:bg-orange-900/20 dark:text-orange-300 dark:border-orange-800">
                      {problem.company}
                    </Badge>
                  </div>
                )}
                
                {problem.patterns.length > 0 && (
                  <div>
                    <Label className="text-sm font-semibold text-muted-foreground mb-2 block">
                      Patterns & Tags
                    </Label>
                    <div className="flex flex-wrap gap-2">
                      {problem.patterns.map((pattern) => (
                        <Badge
                          key={pattern.id}
                          variant="secondary"
                          className="bg-blue-50 text-blue-700 border-blue-200 dark:bg-blue-900/20 dark:text-blue-300 dark:border-blue-800"
                        >
                          {pattern.name}
                        </Badge>
                      ))}
                    </div>
                  </div>
                )}

                {problem.description && (
                  <div>
                    <Label className="text-sm font-semibold text-muted-foreground mb-2 block">
                      Description
                    </Label>
                    <p className="text-sm text-foreground leading-relaxed">
                      {problem.description}
                    </p>
                  </div>
                )}
              </CardContent>
            </Card>
          </motion.div>
        </div>
      </div>
    </div>
  );
}