"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import {
  BarChart3,
  TrendingUp,
  Calendar,
  Clock,
  Target,
  Brain,
  Zap,
  AlertCircle,
  Award,
  TrendingDown,
  ChevronRight,
} from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ProgressRing } from "@/components/ui/progress-ring";
import { AnimatedCounter } from "@/components/ui/animated-counter";
import { GlassCard } from "@/components/ui/glass-card";
import {
  BarChart,
  Bar,
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";
import {
  staggerContainer,
  staggerItem,
} from "@/lib/animations";

interface AnalyticsData {
  dailyProgress: Array<{
    date: string;
    problems: number;
    timeSpent: number;
  }>;
  difficultyBreakdown: Array<{
    difficulty: string;
    count: number;
    avgTime: number;
  }>;
  patternMastery: Array<{
    pattern: string;
    mastery: number;
    problems: number;
  }>;
  companyProgress: Array<{
    company: string;
    solved: number;
    target: number;
  }>;
  timeAnalytics: {
    totalHours: number;
    avgPerDay: number;
    bestStreak: number;
    currentStreak: number;
  };
}

const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#ff7c7c', '#8dd1e1'];

// Time Performance Quick Stats Component for Analytics
function TimePerformanceQuickStats() {
  const [timeData, setTimeData] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchTimeData() {
      try {
        const response = await fetch("/api/analytics/time-benchmarks");
        if (!response.ok) throw new Error("Failed to fetch time data");
        const result = await response.json();
        setTimeData(result);
      } catch (err) {
        console.error('Time data fetch error:', err);
      } finally {
        setLoading(false);
      }
    }
    fetchTimeData();
  }, []);

  const getBenchmarkStatus = (avgTime: number, target: number) => {
    if (avgTime <= target) return { status: 'Good', color: 'text-green-600', bgColor: 'bg-green-50' };
    if (avgTime <= target * 1.5) return { status: 'Warning', color: 'text-orange-600', bgColor: 'bg-orange-50' };
    return { status: 'Needs Work', color: 'text-red-600', bgColor: 'bg-red-50' };
  };

  if (loading) {
    return (
      <Card className="h-full">
        <CardHeader>
          <Skeleton className="h-6 w-40" />
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {[1, 2, 3].map(i => (
              <div key={i} className="flex items-center justify-between p-3 border rounded">
                <Skeleton className="h-4 w-16" />
                <Skeleton className="h-4 w-20" />
                <Skeleton className="h-6 w-20" />
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    );
  }

  const benchmarks = [
    { difficulty: 'Easy', target: 15, current: timeData?.easy?.avgTime || 0 },
    { difficulty: 'Medium', target: 25, current: timeData?.medium?.avgTime || 0 },
    { difficulty: 'Hard', target: 40, current: timeData?.hard?.avgTime || 0 }
  ];

  return (
    <Card className="h-full bg-gradient-to-br from-blue-50/50 to-cyan-50/50">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Clock className="h-5 w-5 text-blue-600" />
          Time Performance
        </CardTitle>
        <p className="text-sm text-muted-foreground">Your speed vs interview benchmarks</p>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          {benchmarks.map((benchmark, index) => {
            const status = getBenchmarkStatus(benchmark.current, benchmark.target);
            const percentage = benchmark.current > 0 ? Math.min((benchmark.target / benchmark.current) * 100, 100) : 0;
            
            return (
              <div key={index} className="p-4 bg-white/60 rounded-lg border border-blue-200/50">
                <div className="flex items-center justify-between mb-2">
                  <div className="flex items-center gap-2">
                    <span className="font-medium text-blue-700">{benchmark.difficulty}</span>
                    <Badge className={`${status.bgColor} ${status.color} text-xs`}>
                      {status.status}
                    </Badge>
                  </div>
                  <div className="text-sm text-muted-foreground">
                    Target: ≤{benchmark.target}min
                  </div>
                </div>
                
                <div className="flex items-center gap-3">
                  <div className="flex-1">
                    <div className="flex justify-between text-sm mb-1">
                      <span>Current: {Math.round(benchmark.current)}min</span>
                      <span>{Math.round(percentage)}%</span>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2">
                      <div 
                        className={`h-2 rounded-full transition-all duration-500 ${
                          percentage >= 100 ? 'bg-green-500' : 
                          percentage >= 70 ? 'bg-orange-500' : 'bg-red-500'
                        }`}
                        style={{ width: `${percentage}%` }}
                      />
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </CardContent>
    </Card>
  );
}

// Smart Recommendations Card Component for Analytics
function SmartRecommendationsCard() {
  const [recommendations, setRecommendations] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchRecommendations() {
      try {
        const response = await fetch("/api/analytics/readiness");
        if (!response.ok) throw new Error("Failed to fetch readiness data");
        const result = await response.json();
        setRecommendations(result.recommendations || []);
      } catch (err) {
        console.error('Recommendations fetch error:', err);
      } finally {
        setLoading(false);
      }
    }
    fetchRecommendations();
  }, []);

  if (loading) {
    return (
      <Card className="h-full">
        <CardHeader>
          <Skeleton className="h-6 w-40" />
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {[1, 2, 3].map(i => (
              <Skeleton key={i} className="h-4 w-full" />
            ))}
          </div>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="h-full bg-gradient-to-br from-purple-50/50 to-pink-50/50">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Brain className="h-5 w-5 text-purple-600" />
          Smart Recommendations
        </CardTitle>
        <p className="text-sm text-muted-foreground">AI-generated improvement suggestions</p>
      </CardHeader>
      <CardContent>
        {recommendations.length === 0 ? (
          <div className="flex items-center justify-center py-8">
            <div className="text-center">
              <Target className="h-12 w-12 text-purple-600 mx-auto mb-2" />
              <p className="text-purple-600 font-semibold">You're doing great!</p>
              <p className="text-sm text-muted-foreground">No specific recommendations at the moment</p>
            </div>
          </div>
        ) : (
          <div className="space-y-3">
            {recommendations.slice(0, 5).map((recommendation, index) => (
              <div key={index} className="flex items-start gap-3 p-3 bg-white/60 rounded-lg border border-purple-200/50">
                <div className="flex h-6 w-6 items-center justify-center rounded-full bg-purple-100 shrink-0 mt-0.5">
                  <span className="text-xs font-bold text-purple-600">{index + 1}</span>
                </div>
                <div className="flex-1">
                  <p className="text-sm text-gray-700">{recommendation}</p>
                </div>
              </div>
            ))}
            
            {recommendations.length > 5 && (
              <p className="text-xs text-muted-foreground text-center">
                +{recommendations.length - 5} more recommendations available
              </p>
            )}
          </div>
        )}
      </CardContent>
    </Card>
  );
}

// Pattern Confidence Table Component for Analytics
function PatternConfidenceTable() {
  const [patterns, setPatterns] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchPatterns() {
      try {
        const response = await fetch("/api/analytics/patterns");
        if (!response.ok) throw new Error("Failed to fetch patterns data");
        const result = await response.json();
        setPatterns(result.patterns || []);
      } catch (err) {
        console.error('Patterns fetch error:', err);
      } finally {
        setLoading(false);
      }
    }
    fetchPatterns();
  }, []);

  const getConfidenceBadge = (confidence: string) => {
    switch (confidence) {
      case 'Strong':
        return <Badge className="bg-green-100 text-green-800 border-green-300">Strong</Badge>;
      case 'Medium':
        return <Badge className="bg-yellow-100 text-yellow-800 border-yellow-300">Medium</Badge>;
      case 'Weak':
        return <Badge className="bg-red-100 text-red-800 border-red-300">Weak</Badge>;
      default:
        return <Badge variant="outline">Unknown</Badge>;
    }
  };

  if (loading) {
    return (
      <Card className="h-full">
        <CardHeader>
          <Skeleton className="h-6 w-40" />
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {[1, 2, 3, 4, 5].map(i => (
              <div key={i} className="flex items-center justify-between p-3 border rounded">
                <Skeleton className="h-4 w-24" />
                <Skeleton className="h-4 w-12" />
                <Skeleton className="h-4 w-16" />
                <Skeleton className="h-6 w-16" />
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="h-full bg-gradient-to-br from-indigo-50/50 to-purple-50/50">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Target className="h-5 w-5 text-indigo-600" />
          Pattern Confidence Table
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-2">
          {/* Header */}
          <div className="grid grid-cols-4 gap-4 p-3 bg-white/60 rounded-lg font-semibold text-sm border border-indigo-200">
            <div>Pattern</div>
            <div className="text-center">Solved</div>
            <div className="text-center">Avg Time</div>
            <div className="text-center">Confidence</div>
          </div>
          
          {/* Pattern Rows */}
          {patterns.length === 0 ? (
            <div className="flex items-center justify-center py-8">
              <p className="text-muted-foreground">No pattern data available</p>
            </div>
          ) : (
            <div className="space-y-1 max-h-80 overflow-y-auto">
              {patterns.map((pattern, index) => (
                <div 
                  key={index} 
                  className="grid grid-cols-4 gap-4 p-3 bg-white/40 rounded-lg text-sm border border-transparent hover:border-indigo-200 transition-colors"
                >
                  <div className="font-medium text-indigo-700 truncate" title={pattern.pattern}>
                    {pattern.pattern}
                  </div>
                  <div className="text-center text-muted-foreground">
                    {pattern.totalSolved || 0}
                  </div>
                  <div className="text-center text-muted-foreground">
                    {Math.round(pattern.avgTime || 0)}min
                  </div>
                  <div className="flex justify-center">
                    {getConfidenceBadge(pattern.confidence || 'Unknown')}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  );
}

// Readiness Score Card Component for Analytics
function ReadinessScoreCard() {
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchReadiness() {
      try {
        const response = await fetch("/api/analytics/readiness");
        if (!response.ok) throw new Error("Failed to fetch readiness data");
        const result = await response.json();
        setData(result);
      } catch (err) {
        console.error('Readiness fetch error:', err);
      } finally {
        setLoading(false);
      }
    }
    fetchReadiness();
  }, []);

  if (loading) {
    return (
      <Card className="h-full">
        <CardHeader>
          <Skeleton className="h-6 w-32" />
        </CardHeader>
        <CardContent>
          <Skeleton className="h-32 w-32 rounded-full mx-auto mb-4" />
          <Skeleton className="h-4 w-20 mx-auto mb-2" />
          <Skeleton className="h-6 w-16 mx-auto" />
        </CardContent>
      </Card>
    );
  }

  if (!data) {
    return (
      <Card className="h-full">
        <CardContent className="flex items-center justify-center py-8">
          <p className="text-muted-foreground">No readiness data available</p>
        </CardContent>
      </Card>
    );
  }

  const scorePercentage = Math.round((data.readinessScore?.score ?? 0) * 100);
  const status = scorePercentage >= 80 ? "Ready" : scorePercentage >= 60 ? "Almost Ready" : "Not Ready";
  
  const statusConfig = {
    "Ready": { color: "text-green-600", bgColor: "bg-green-50", icon: "🚀", ringVariant: "green" },
    "Almost Ready": { color: "text-orange-600", bgColor: "bg-orange-50", icon: "⚡", ringVariant: "orange" },
    "Not Ready": { color: "text-red-600", bgColor: "bg-red-50", icon: "🎯", ringVariant: "purple" },
  } as const;

  const config = statusConfig[status as keyof typeof statusConfig];

  return (
    <Card className="h-full bg-gradient-to-br from-indigo-50/50 to-purple-50/50">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Target className="h-5 w-5 text-indigo-600" />
          Interview Readiness
        </CardTitle>
      </CardHeader>
      <CardContent className="flex flex-col items-center space-y-4">
        <ProgressRing
          value={scorePercentage}
          size="lg"
          variant={config.ringVariant}
        >
          <div className="text-center">
            <AnimatedCounter
              value={scorePercentage}
              suffix="%"
              size="lg"
            />
          </div>
        </ProgressRing>
        
        <Badge className={`${config.bgColor} ${config.color} px-4 py-1 font-semibold`}>
          <span className="mr-1">{config.icon}</span>
          {status}
        </Badge>
        
        <div className="text-center text-sm">
          <p className="text-muted-foreground">
            {data.metrics?.totalProblems || 0} problems solved
          </p>
          <p className="text-muted-foreground">
            {Math.round(data.metrics?.avgTimeMinutes || 0)} min avg time
          </p>
        </div>
      </CardContent>
    </Card>
  );
}

// Weak Areas Card Component for Analytics
function WeakAreasCard() {
  const [weaknesses, setWeaknesses] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchWeaknesses() {
      try {
        const response = await fetch("/api/analytics/weaknesses");
        if (!response.ok) throw new Error("Failed to fetch weaknesses data");
        const result = await response.json();
        setWeaknesses(result.weaknesses || []);
      } catch (err) {
        console.error('Weaknesses fetch error:', err);
      } finally {
        setLoading(false);
      }
    }
    fetchWeaknesses();
  }, []);

  if (loading) {
    return (
      <Card className="h-full">
        <CardHeader>
          <Skeleton className="h-6 w-32" />
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {[1, 2, 3].map(i => (
              <div key={i} className="flex items-center justify-between">
                <Skeleton className="h-4 w-32" />
                <Skeleton className="h-4 w-20" />
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="h-full bg-gradient-to-br from-red-50/50 to-orange-50/50">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <TrendingDown className="h-5 w-5 text-red-600" />
          Weak Areas
        </CardTitle>
      </CardHeader>
      <CardContent>
        {weaknesses.length === 0 ? (
          <div className="flex items-center justify-center py-8">
            <div className="text-center">
              <Award className="h-12 w-12 text-green-600 mx-auto mb-2" />
              <p className="text-green-600 font-semibold">Great Job!</p>
              <p className="text-sm text-muted-foreground">No weak areas detected</p>
            </div>
          </div>
        ) : (
          <div className="space-y-4">
            {weaknesses.slice(0, 5).map((weakness, index) => (
              <div key={index} className="flex items-center justify-between p-3 bg-white/60 rounded-lg border border-red-200/50">
                <div className="flex-1">
                  <div className="flex items-center gap-2">
                    <div className="text-sm font-medium text-red-700">
                      {weakness.pattern}
                    </div>
                    <Badge variant="outline" className="text-xs border-red-300 text-red-600">
                      {weakness.confidence}
                    </Badge>
                  </div>
                  <div className="text-xs text-muted-foreground mt-1">
                    {weakness.avgTime}min avg • {weakness.hintUsageRate}% hints
                  </div>
                </div>
                <ChevronRight className="h-4 w-4 text-muted-foreground" />
              </div>
            ))}
            
            {weaknesses.length > 5 && (
              <p className="text-xs text-muted-foreground text-center">
                +{weaknesses.length - 5} more areas to improve
              </p>
            )}
          </div>
        )}
      </CardContent>
    </Card>
  );
}

export default function AnalyticsPage() {
  const [data, setData] = useState<AnalyticsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchAnalytics() {
      try {
        console.log('Fetching analytics data...');
        // Fetch data from multiple endpoints
        const [statsRes, streakRes, companyRes, readinessRes] = await Promise.all([
          fetch("/api/analytics/stats"),
          fetch("/api/analytics/streak?days=30"),
          fetch("/api/analytics/company?timeframe=all"),
          fetch("/api/analytics/readiness"),
        ]);

        console.log('Response status:', { 
          stats: statsRes.status, 
          streak: streakRes.status, 
          company: companyRes.status, 
          readiness: readinessRes.status 
        });

        if (!statsRes.ok || !streakRes.ok || !companyRes.ok || !readinessRes.ok) {
          const errorDetails = {
            stats: !statsRes.ok ? `${statsRes.status}: ${statsRes.statusText}` : 'OK',
            streak: !streakRes.ok ? `${streakRes.status}: ${streakRes.statusText}` : 'OK',
            company: !companyRes.ok ? `${companyRes.status}: ${companyRes.statusText}` : 'OK',
            readiness: !readinessRes.ok ? `${readinessRes.status}: ${readinessRes.statusText}` : 'OK'
          };
          console.error('API Error Details:', errorDetails);
          throw new Error(`Failed to fetch analytics data: ${JSON.stringify(errorDetails)}`);
        }

        const [stats, streak, company, readiness] = await Promise.all([
          statsRes.json(),
          streakRes.json(),
          companyRes.json(),
          readinessRes.json(),
        ]);

        console.log('Fetched data:', { stats, streak, company, readiness });

        // Transform data for charts
        const analyticsData: AnalyticsData = {
          dailyProgress: streak.calendarData?.slice(-14).map((day: any) => ({
            date: new Date(day.date).toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
            problems: day.count || 0,
            timeSpent: Math.round((day.totalTime || 0) / 3600 * 10) / 10,
          })) || [],
          
          difficultyBreakdown: [
            { difficulty: 'Easy', count: stats.easyCount || 0, avgTime: stats.easyAvgTime || 15 },
            { difficulty: 'Medium', count: stats.mediumCount || 0, avgTime: stats.mediumAvgTime || 35 },
            { difficulty: 'Hard', count: stats.hardCount || 0, avgTime: stats.hardAvgTime || 55 },
          ],
          
          patternMastery: readiness.patternBreakdown ? 
            Object.entries(readiness.patternBreakdown).map(([pattern, data]: [string, any]) => ({
              pattern,
              mastery: Math.round(((data.avgTime < 30 ? 1 : 0) + (data.hintUsageRate < 0.3 ? 1 : 0)) * 50),
              problems: data.solved || 0,
            })).slice(0, 8) : 
            // Fallback to empty array if no pattern breakdown
            [],
          
          companyProgress: company.companies?.map((comp: any) => ({
            company: comp.company,
            solved: comp.problemsSolved || 0,
            target: comp.company === 'Google' ? 150 : comp.company === 'Amazon' ? 120 : 100,
          })) || [],
          
          timeAnalytics: {
            totalHours: Math.round((stats.totalTimeMinutes || 0) / 60 * 10) / 10,
            avgPerDay: Math.round((streak.weeklyStats?.totalTimeSpent || 0) / (7 * 3600) * 10) / 10,
            bestStreak: streak.longestStreak || 0,
            currentStreak: streak.currentStreak || 0,
          },
        };

        console.log('Transformed analytics data:', analyticsData);
        setData(analyticsData);
      } catch (err) {
        console.error('Analytics fetch error:', err);
        setError(err instanceof Error ? err.message : "Failed to load analytics");
      } finally {
        setLoading(false);
      }
    }

    fetchAnalytics();
  }, []);

  if (loading) {
    return (
      <div className="space-y-8 p-2">
        <Skeleton className="h-10 w-64 mb-2" />
        <Skeleton className="h-6 w-96 mb-6" />
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Skeleton className="h-80" />
          <Skeleton className="h-80" />
          <Skeleton className="h-80" />
          <Skeleton className="h-80" />
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
            Error Loading Analytics
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
          <h1 className="text-4xl font-bold bg-gradient-to-r from-indigo-600 via-purple-500 to-pink-500 bg-clip-text text-transparent">
            Advanced Analytics
          </h1>
          <p className="text-muted-foreground mt-2 text-lg">
            Deep insights into your coding progress and performance
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Badge variant="outline" className="gap-2">
            <TrendingUp className="h-3 w-3" />
            Last 30 Days
          </Badge>
        </div>
      </motion.div>

      {/* Readiness Score Section */}
      <motion.div
        variants={staggerContainer}
        initial="hidden"
        animate="visible"
        className="grid grid-cols-1 lg:grid-cols-3 gap-6"
      >
        <motion.div variants={staggerItem} className="lg:col-span-1">
          <ReadinessScoreCard />
        </motion.div>
        
        <motion.div variants={staggerItem} className="lg:col-span-2">
          <WeakAreasCard />
        </motion.div>
      </motion.div>

      {/* Time Performance Section */}
      <motion.div
        variants={staggerContainer}
        initial="hidden"
        animate="visible"
        className="grid grid-cols-1 lg:grid-cols-2 gap-6"
      >
        <motion.div variants={staggerItem}>
          <TimePerformanceQuickStats />
        </motion.div>
        
        <motion.div variants={staggerItem}>
          <SmartRecommendationsCard />
        </motion.div>
      </motion.div>

      {/* Key Metrics */}
      <motion.div
        variants={staggerContainer}
        initial="hidden"
        animate="visible"
        className="grid grid-cols-1 md:grid-cols-4 gap-6"
      >
        <motion.div variants={staggerItem}>
          <Card className="bg-gradient-to-br from-blue-50 to-indigo-50 border-blue-200">
            <CardContent className="p-6 text-center">
              <Clock className="h-8 w-8 text-blue-600 mx-auto mb-2" />
              <div className="text-3xl font-bold text-blue-600">{data?.timeAnalytics.totalHours || 0}h</div>
              <p className="text-sm text-muted-foreground">Total Study Time</p>
            </CardContent>
          </Card>
        </motion.div>
        
        <motion.div variants={staggerItem}>
          <Card className="bg-gradient-to-br from-green-50 to-emerald-50 border-green-200">
            <CardContent className="p-6 text-center">
              <Target className="h-8 w-8 text-green-600 mx-auto mb-2" />
              <div className="text-3xl font-bold text-green-600">{data?.timeAnalytics.currentStreak || 0}</div>
              <p className="text-sm text-muted-foreground">Current Streak</p>
            </CardContent>
          </Card>
        </motion.div>
        
        <motion.div variants={staggerItem}>
          <Card className="bg-gradient-to-br from-purple-50 to-pink-50 border-purple-200">
            <CardContent className="p-6 text-center">
              <Zap className="h-8 w-8 text-purple-600 mx-auto mb-2" />
              <div className="text-3xl font-bold text-purple-600">{data?.timeAnalytics.avgPerDay || 0}h</div>
              <p className="text-sm text-muted-foreground">Daily Average</p>
            </CardContent>
          </Card>
        </motion.div>
        
        <motion.div variants={staggerItem}>
          <Card className="bg-gradient-to-br from-orange-50 to-red-50 border-orange-200">
            <CardContent className="p-6 text-center">
              <Brain className="h-8 w-8 text-orange-600 mx-auto mb-2" />
              <div className="text-3xl font-bold text-orange-600">{data?.timeAnalytics.bestStreak || 0}</div>
              <p className="text-sm text-muted-foreground">Best Streak</p>
            </CardContent>
          </Card>
        </motion.div>
      </motion.div>

      {/* Charts */}
      <Tabs defaultValue="progress" className="space-y-6">
        <TabsList className="grid w-full grid-cols-4 lg:w-[600px]">
          <TabsTrigger value="progress">Daily Progress</TabsTrigger>
          <TabsTrigger value="difficulty">Difficulty</TabsTrigger>
          <TabsTrigger value="patterns">Patterns</TabsTrigger>
          <TabsTrigger value="companies">Companies</TabsTrigger>
        </TabsList>

        <TabsContent value="progress">
          <motion.div
            variants={staggerContainer}
            initial="hidden"
            animate="visible"
            className="grid grid-cols-1 gap-6"
          >
            <motion.div variants={staggerItem}>
              <Card className="bg-gradient-to-br from-blue-50/50 to-indigo-50/50">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Calendar className="h-5 w-5 text-blue-600" />
                    Daily Progress (Last 14 Days)
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={data?.dailyProgress}>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="date" />
                      <YAxis yAxisId="left" />
                      <YAxis yAxisId="right" orientation="right" />
                      <Tooltip />
                      <Legend />
                      <Bar yAxisId="left" dataKey="problems" fill="#3B82F6" name="Problems Solved" />
                      <Line yAxisId="right" type="monotone" dataKey="timeSpent" stroke="#10B981" name="Hours Spent" />
                    </BarChart>
                  </ResponsiveContainer>
                </CardContent>
              </Card>
            </motion.div>
          </motion.div>
        </TabsContent>

        <TabsContent value="difficulty">
          <motion.div
            variants={staggerContainer}
            initial="hidden"
            animate="visible"
            className="grid grid-cols-1 lg:grid-cols-2 gap-6"
          >
            <motion.div variants={staggerItem}>
              <Card className="bg-gradient-to-br from-green-50/50 to-emerald-50/50">
                <CardHeader>
                  <CardTitle>Problems by Difficulty</CardTitle>
                </CardHeader>
                <CardContent>
                  <ResponsiveContainer width="100%" height={300}>
                    <PieChart>
                      <Pie
                        data={data?.difficultyBreakdown}
                        dataKey="count"
                        nameKey="difficulty"
                        cx="50%"
                        cy="50%"
                        outerRadius={80}
                        fill="#8884d8"
                        label
                      >
                        {data?.difficultyBreakdown.map((entry, index) => (
                          <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                      </Pie>
                      <Tooltip />
                      <Legend />
                    </PieChart>
                  </ResponsiveContainer>
                </CardContent>
              </Card>
            </motion.div>
            
            <motion.div variants={staggerItem}>
              <Card className="bg-gradient-to-br from-purple-50/50 to-pink-50/50">
                <CardHeader>
                  <CardTitle>Average Solve Time</CardTitle>
                </CardHeader>
                <CardContent>
                  <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={data?.difficultyBreakdown}>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="difficulty" />
                      <YAxis />
                      <Tooltip />
                      <Bar dataKey="avgTime" fill="#A855F7" />
                    </BarChart>
                  </ResponsiveContainer>
                </CardContent>
              </Card>
            </motion.div>
          </motion.div>
        </TabsContent>

        <TabsContent value="patterns">
          <motion.div
            variants={staggerContainer}
            initial="hidden"
            animate="visible"
            className="grid grid-cols-1 lg:grid-cols-2 gap-6"
          >
            <motion.div variants={staggerItem}>
              <Card className="bg-gradient-to-br from-teal-50/50 to-cyan-50/50">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Brain className="h-5 w-5 text-teal-600" />
                    Pattern Mastery Chart
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <ResponsiveContainer width="100%" height={400}>
                    <BarChart data={data?.patternMastery} layout="horizontal">
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis type="number" domain={[0, 100]} />
                      <YAxis dataKey="pattern" type="category" width={120} />
                      <Tooltip />
                      <Bar dataKey="mastery" fill="#0D9488" />
                    </BarChart>
                  </ResponsiveContainer>
                </CardContent>
              </Card>
            </motion.div>
            
            <motion.div variants={staggerItem}>
              <PatternConfidenceTable />
            </motion.div>
          </motion.div>
        </TabsContent>

        <TabsContent value="companies">
          <motion.div variants={staggerItem}>
            <Card className="bg-gradient-to-br from-rose-50/50 to-pink-50/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Target className="h-5 w-5 text-rose-600" />
                  Company Readiness
                </CardTitle>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={400}>
                  <BarChart data={data?.companyProgress}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="company" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="solved" fill="#EC4899" name="Solved" />
                    <Bar dataKey="target" fill="#F3F4F6" name="Target" />
                  </BarChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>
          </motion.div>
        </TabsContent>
      </Tabs>
    </div>
  );
}