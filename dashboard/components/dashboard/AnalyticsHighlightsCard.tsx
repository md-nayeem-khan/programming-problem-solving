"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Clock } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { DashboardMainCardSkeleton } from "@/components/dashboard/DashboardSkeletons";
import {
  BarChart,
  Bar,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";

interface HighlightsData {
  dailyProgress: Array<{
    date: string;
    problems: number;
    timeSpent: number;
  }>;
}

export function AnalyticsHighlightsCard() {
  const [data, setData] = useState<HighlightsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchHighlights() {
      try {
        const progressRes = await fetch("/api/analytics/daily-progress?days=30");

        if (!progressRes.ok) {
          throw new Error("Failed to fetch analytics highlights");
        }

        const progress = await progressRes.json();
        const rows = Array.isArray(progress.data) ? progress.data : [];

        const thirtyDayProgress = rows.map((day: any) => {
          const dateObj = new Date(day.date);
          return {
            date: dateObj.toLocaleDateString("en-US", { month: "short", day: "numeric" }),
            problems: day.problemsSolved || 0,
            timeSpent: Math.round((((day.totalTimeMinutes || 0) / 60) * 10)) / 10,
          };
        });

        setData({
          dailyProgress: thirtyDayProgress,
        });
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load analytics highlights");
      } finally {
        setLoading(false);
      }
    }

    fetchHighlights();
  }, []);

  if (loading) {
    return <DashboardMainCardSkeleton rows={1} />;
  }

  if (error || !data) {
    return (
      <Card className="bg-red-50/60 border-red-200">
        <CardHeader>
          <CardTitle className="text-red-700">Analytics Highlights</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-red-600">{error || "No analytics highlights available"}</p>
        </CardContent>
      </Card>
    );
  }

  return (
    <div className="space-y-6">
      <motion.div>
      <Card className="dashboard-card dashboard-card-sky dashboard-soft-grid group border-blue-200/70 shadow-xl shadow-blue-500/20 hover:shadow-2xl hover:shadow-blue-500/30 transition-all duration-300 overflow-hidden relative">
        <div className="pointer-events-none absolute -top-10 -right-8 h-24 w-24 rounded-full bg-blue-400/20 blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
        <div className="pointer-events-none absolute -bottom-10 -left-8 h-24 w-24 rounded-full bg-cyan-400/20 blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Clock className="h-5 w-5 text-blue-600" />
            Daily Progress (Last 30 Days)
          </CardTitle>
        </CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={data.dailyProgress}>
              <CartesianGrid strokeDasharray="3 3" stroke="hsl(207 72% 86%)" />
              <XAxis dataKey="date" />
              <YAxis yAxisId="left" />
              <YAxis yAxisId="right" orientation="right" />
              <Tooltip />
              <Legend />
              <Bar yAxisId="left" dataKey="problems" fill="#0284c7" radius={[6, 6, 0, 0]} name="Problems Solved" />
              <Line yAxisId="right" type="monotone" dataKey="timeSpent" stroke="#14b8a6" strokeWidth={2} dot={{ r: 2, fill: "#14b8a6" }} name="Hours Spent" />
            </BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>
      </motion.div>
    </div>
  );
}
