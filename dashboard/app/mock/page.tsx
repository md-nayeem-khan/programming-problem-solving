"use client";

import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import {
  Target,
  Clock,
  Play,
  Pause,
  RotateCcw,
  CheckCircle2,
  XCircle,
  Calendar,
  AlertCircle,
} from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  staggerContainer,
  staggerItem,
} from "@/lib/animations";
import { format, parseISO } from "date-fns";
import { PatternRecognitionDrill } from "@/components/pattern-recognition-drill";
import { toast } from "sonner";

interface Problem {
  id: number;
  title: string;
  difficulty: string;
  url?: string;
  patterns: Array<{
    pattern: {
      id: number;
      name: string;
      category: string;
    };
  }>;
}

interface MockInterview {
  id: number;
  company: string;
  difficulty: "Easy" | "Medium" | "Hard";
  duration: number;
  problems: string[];
  passed: boolean;
  completedAt: string;
  feedback?: string;
}

interface InterviewStats {
  totalCompleted: number;
  totalPassed: number;
  passRate: number;
  avgDuration: number;
  recentInterviews: MockInterview[];
}

export default function MockInterviewPage() {
  const [stats, setStats] = useState<InterviewStats | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isActive, setIsActive] = useState(false);
  const [timeElapsed, setTimeElapsed] = useState(0);
  const [currentProblem, setCurrentProblem] = useState<Problem | null>(null);
  
  // Pattern drill state
  const [showPatternDrill, setShowPatternDrill] = useState(false);
  const [patternRecognitionTime, setPatternRecognitionTime] = useState<number | null>(null);
  const [problemSolved, setProblemSolved] = useState(false);

  useEffect(() => {
    async function fetchMockInterviews() {
      try {
        const response = await fetch("/api/mock-interviews?limit=10&recent=true");
        if (!response.ok) throw new Error("Failed to fetch mock interviews");
        
        const data = await response.json();
        
        const interviewStats: InterviewStats = {
          totalCompleted: data.stats?.total || 0,
          totalPassed: data.stats?.passed || 0,
          passRate: data.stats?.passRate || 0,
          avgDuration: data.stats?.avgDuration || 0,
          recentInterviews: data.mockInterviews?.map((interview: any) => ({
            id: interview.id,
            company: interview.problem?.company || "Practice",
            difficulty: interview.problem?.difficulty || "Medium",
            duration: Math.round((interview.timeTakenSeconds || 0) / 60),
            problems: [interview.problem?.title || "Unknown"],
            passed: interview.solved,
            completedAt: interview.date,
            feedback: interview.notes,
          })) || [],
        };
        
        setStats(interviewStats);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load mock interviews");
      } finally {
        setLoading(false);
      }
    }

    fetchMockInterviews();
  }, []);

  // Timer for active interview
  useEffect(() => {
    let interval: NodeJS.Timeout;
    if (isActive) {
      interval = setInterval(() => {
        setTimeElapsed(prev => prev + 1);
      }, 1000);
    }
    return () => clearInterval(interval);
  }, [isActive]);

  const formatTime = (seconds: number) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  const startNewInterview = async () => {
    try {
      // Fetch a random problem for the interview
      const response = await fetch("/api/problems?limit=1&random=true");
      if (!response.ok) throw new Error("Failed to fetch problem");
      
      const data = await response.json();
      if (data.problems && data.problems.length > 0) {
        const problem = data.problems[0];
        setCurrentProblem(problem);
        setShowPatternDrill(true); // Show pattern drill first
        setPatternRecognitionTime(null);
        setProblemSolved(false);
        setIsActive(false); // Don't start timer until after pattern drill
      }
    } catch (err) {
      toast.error("Failed to start interview. Please try again.");
    }
  };

  const handlePatternDrillComplete = (recognitionTimeSeconds: number, selectedPatternIds: number[]) => {
    setPatternRecognitionTime(recognitionTimeSeconds);
    setShowPatternDrill(false);
    setIsActive(true);
    setTimeElapsed(0);
    
    const mins = Math.floor(recognitionTimeSeconds / 60);
    const secs = recognitionTimeSeconds % 60;
    toast.success(`Pattern Recognition Complete! Time: ${mins}m ${secs}s`);
  };

  const pauseInterview = () => {
    setIsActive(false);
  };

  const stopInterview = () => {
    setIsActive(false);
    setTimeElapsed(0);
    setCurrentProblem(null);
    setShowPatternDrill(false);
    setPatternRecognitionTime(null);
    setProblemSolved(false);
  };

  const submitSolution = async (passed: boolean) => {
    if (!currentProblem) return;
    
    try {
      const response = await fetch("/api/mock-interviews", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          problemId: currentProblem.id,
          timeTakenSeconds: timeElapsed,
          patternRecognitionSeconds: patternRecognitionTime,
          solved: passed,
          explanationScore: null,
          codeQualityScore: null,
        }),
      });

      if (!response.ok) throw new Error("Failed to submit solution");

      if (passed) {
        toast.success(`Interview Passed! ✅\nGreat job! Solved in ${formatTime(timeElapsed)}`);
      } else {
        toast.error("Interview Complete\nKeep practicing. You'll get it next time!");
      }

      setProblemSolved(true);
      setIsActive(false);

      // Refresh stats
      const statsResponse = await fetch("/api/mock-interviews?limit=10&recent=true");
      if (statsResponse.ok) {
        const data = await statsResponse.json();
        const interviewStats: InterviewStats = {
          totalCompleted: data.stats?.total || 0,
          totalPassed: data.stats?.passed || 0,
          passRate: data.stats?.passRate || 0,
          avgDuration: data.stats?.avgDuration || 0,
          recentInterviews: data.mockInterviews?.map((interview: any) => ({
            id: interview.id,
            company: interview.problem?.company || "Practice",
            difficulty: interview.problem?.difficulty || "Medium",
            duration: Math.round((interview.timeTakenSeconds || 0) / 60),
            problems: [interview.problem?.title || "Unknown"],
            passed: interview.solved,
            completedAt: interview.date,
            feedback: interview.notes,
          })) || [],
        };
        setStats(interviewStats);
      }
    } catch (err) {
      toast.error("Failed to submit solution. Please try again.");
    }
  };

  if (loading) {
    return (
      <div className="space-y-8 p-2">
        <Skeleton className="h-10 w-64 mb-2" />
        <Skeleton className="h-6 w-96 mb-6" />
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <Skeleton className="h-64" />
          <Skeleton className="h-64" />
          <Skeleton className="h-64" />
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
            Error Loading Mock Interviews
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
          <h1 className="text-4xl font-bold bg-gradient-to-r from-rose-600 via-pink-500 to-fuchsia-500 bg-clip-text text-transparent">
            Mock Interviews
          </h1>
          <p className="text-muted-foreground mt-2 text-lg">
            Practice coding interviews with timed sessions
          </p>
        </div>
        <Button
          onClick={startNewInterview}
          disabled={isActive}
          className="gap-2 bg-gradient-to-r from-rose-500 to-pink-500 hover:from-rose-600 hover:to-pink-600 text-white shadow-lg"
        >
          <Play className="h-4 w-4" />
          Start New Interview
        </Button>
      </motion.div>

      <Tabs defaultValue="practice" className="space-y-6">
        <TabsList className="grid w-full grid-cols-2 lg:w-[400px]">
          <TabsTrigger value="practice">Practice Session</TabsTrigger>
          <TabsTrigger value="history">Interview History</TabsTrigger>
        </TabsList>

        <TabsContent value="practice">
          {/* Pattern Recognition Drill - Shows before problem */}
          {showPatternDrill && currentProblem && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              className="mb-6"
            >
              <Card className="border-2 border-rose-300 bg-gradient-to-br from-rose-50 to-pink-50">
                <CardHeader>
                  <CardTitle className="text-rose-700">
                    Step 1: Pattern Recognition Drill 🎯
                  </CardTitle>
                  <p className="text-sm text-muted-foreground mt-2">
                    First, identify the patterns in this problem before starting to code.
                  </p>
                </CardHeader>
                <CardContent>
                  <PatternRecognitionDrill
                    problemId={currentProblem.id}
                    problemTitle={currentProblem.title}
                    correctPatterns={currentProblem.patterns.map(p => p.pattern)}
                    onComplete={handlePatternDrillComplete}
                  />
                </CardContent>
              </Card>
            </motion.div>
          )}

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Current Problem */}
            <motion.div
              variants={staggerItem}
              initial="hidden"
              animate="visible"
            >
              <Card className="bg-gradient-to-br from-blue-50 to-indigo-50 border-2 border-blue-200 shadow-xl shadow-blue-500/20 h-full">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-blue-700">
                    <Target className="h-5 w-5" />
                    Current Problem
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  {currentProblem && !showPatternDrill ? (
                    <div className="space-y-4">
                      <Badge className="bg-blue-100 text-blue-700">{currentProblem.difficulty}</Badge>
                      <div className="p-4 bg-white/70 rounded-lg border">
                        <h3 className="font-semibold mb-2">{currentProblem.title}</h3>
                        {currentProblem.url && (
                          <a
                            href={currentProblem.url}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-sm text-blue-600 hover:underline"
                          >
                            View Problem →
                          </a>
                        )}
                        {patternRecognitionTime && (
                          <div className="mt-3 p-2 bg-rose-100 rounded text-sm">
                            <span className="font-semibold">Pattern Recognition:</span>{' '}
                            {formatTime(patternRecognitionTime)}
                          </div>
                        )}
                      </div>
                      {!problemSolved && (
                        <div className="flex gap-2">
                          <Button
                            size="sm"
                            onClick={() => submitSolution(true)}
                            className="bg-green-600 hover:bg-green-700"
                          >
                            <CheckCircle2 className="h-3 w-3 mr-1" />
                            Mark as Solved
                          </Button>
                          <Button
                            size="sm"
                            variant="outline"
                            onClick={() => submitSolution(false)}
                          >
                            Mark as Failed
                          </Button>
                        </div>
                      )}
                      {problemSolved && (
                        <Button
                          size="sm"
                          onClick={startNewInterview}
                          className="w-full"
                        >
                          Start Another Interview
                        </Button>
                      )}
                    </div>
                  ) : currentProblem && showPatternDrill ? (
                    <div className="text-center py-12">
                      <Target className="h-12 w-12 text-rose-400 mx-auto mb-4" />
                      <h3 className="text-lg font-semibold text-rose-600 mb-2">
                        Complete Pattern Drill First
                      </h3>
                      <p className="text-muted-foreground">
                        Identify the patterns above to start solving
                      </p>
                    </div>
                  ) : (
                    <div className="text-center py-12">
                      <Target className="h-12 w-12 text-blue-400 mx-auto mb-4" />
                      <h3 className="text-lg font-semibold text-blue-600 mb-2">
                        Ready to Start
                      </h3>
                      <p className="text-muted-foreground">
                        Click "Start New Interview" to begin your practice session
                      </p>
                    </div>
                  )}
                </CardContent>
              </Card>
            </motion.div>

            {/* Timer Card */}
            <motion.div
              variants={staggerItem}
              initial="hidden"
              animate="visible"
            >
              <Card className="bg-gradient-to-br from-rose-100 via-pink-50 to-fuchsia-100 border-2 border-rose-300 shadow-xl shadow-rose-500/20 h-full">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-rose-700">
                    <Clock className="h-5 w-5" />
                    Solving Timer
                  </CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="text-center">
                    <div className="text-4xl font-mono font-bold text-rose-600">
                      {formatTime(timeElapsed)}
                    </div>
                    <p className="text-sm text-muted-foreground mt-1">
                      {isActive ? "Interview in progress" : "Ready to start"}
                    </p>
                  </div>
                  
                  <div className="flex gap-2">
                    {!isActive ? (
                      <Button
                        onClick={startNewInterview}
                        className="flex-1 bg-green-600 hover:bg-green-700"
                      >
                        <Play className="h-4 w-4 mr-2" />
                        Start
                      </Button>
                    ) : (
                      <Button
                        onClick={pauseInterview}
                        className="flex-1 bg-yellow-600 hover:bg-yellow-700"
                      >
                        <Pause className="h-4 w-4 mr-2" />
                        Pause
                      </Button>
                    )}
                    
                    <Button
                      onClick={stopInterview}
                      variant="outline"
                      disabled={!isActive && timeElapsed === 0}
                    >
                      <RotateCcw className="h-4 w-4" />
                    </Button>
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          </div>

          {/* Stats Cards */}
          <motion.div
            variants={staggerContainer}
            initial="hidden"
            animate="visible"
            className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-6"
          >
            <motion.div variants={staggerItem}>
              <Card className="text-center">
                <CardContent className="p-6">
                  <div className="text-3xl font-bold text-rose-600">{stats?.totalCompleted || 0}</div>
                  <p className="text-sm text-muted-foreground">Total Completed</p>
                </CardContent>
              </Card>
            </motion.div>
            
            <motion.div variants={staggerItem}>
              <Card className="text-center">
                <CardContent className="p-6">
                  <div className="text-3xl font-bold text-green-600">{Math.round(stats?.passRate || 0)}%</div>
                  <p className="text-sm text-muted-foreground">Pass Rate</p>
                </CardContent>
              </Card>
            </motion.div>
            
            <motion.div variants={staggerItem}>
              <Card className="text-center">
                <CardContent className="p-6">
                  <div className="text-3xl font-bold text-blue-600">{Math.round(stats?.avgDuration || 0)}m</div>
                  <p className="text-sm text-muted-foreground">Avg Duration</p>
                </CardContent>
              </Card>
            </motion.div>
          </motion.div>
        </TabsContent>

        <TabsContent value="history">
          <motion.div
            variants={staggerContainer}
            initial="hidden"
            animate="visible"
            className="space-y-4"
          >
            {stats?.recentInterviews && stats.recentInterviews.length > 0 ? (
              stats.recentInterviews.map((interview) => (
                <motion.div
                  key={interview.id}
                  variants={staggerItem}
                >
                  <Card className="hover:shadow-lg transition-shadow">
                    <CardContent className="p-4">
                      <div className="flex items-center justify-between">
                        <div className="flex-1">
                          <div className="flex items-center gap-3">
                            <div className={`flex h-10 w-10 items-center justify-center rounded-full ${
                              interview.passed ? 'bg-green-100' : 'bg-red-100'
                            }`}>
                              {interview.passed ? (
                                <CheckCircle2 className="h-5 w-5 text-green-600" />
                              ) : (
                                <XCircle className="h-5 w-5 text-red-600" />
                              )}
                            </div>
                            <div>
                              <div className="font-medium">{interview.company} Interview</div>
                              <div className="flex items-center gap-2 mt-1">
                                <Badge variant={interview.passed ? "default" : "destructive"}>
                                  {interview.passed ? "Passed" : "Failed"}
                                </Badge>
                                <Badge variant="outline">{interview.difficulty}</Badge>
                                <span className="text-sm text-muted-foreground">
                                  {interview.duration}m
                                </span>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div className="text-right">
                          <div className="text-sm text-muted-foreground">
                            {format(parseISO(interview.completedAt), "MMM d, yyyy")}
                          </div>
                          <div className="text-xs text-muted-foreground">
                            {interview.problems.length} problems
                          </div>
                        </div>
                      </div>
                      
                      {interview.feedback && (
                        <div className="mt-3 p-3 bg-muted/50 rounded-lg">
                          <p className="text-sm">{interview.feedback}</p>
                        </div>
                      )}
                    </CardContent>
                  </Card>
                </motion.div>
              ))
            ) : (
              <div className="text-center py-12">
                <Calendar className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                <h3 className="text-lg font-semibold text-muted-foreground mb-2">
                  No Interview History
                </h3>
                <p className="text-muted-foreground">
                  Complete your first mock interview to see your history here
                </p>
              </div>
            )}
          </motion.div>
        </TabsContent>
      </Tabs>
    </div>
  );
}
