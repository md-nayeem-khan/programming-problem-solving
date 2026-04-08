"use client";

import { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import {
  Target,
  Plus,
  Calendar,
  TrendingUp,
  TrendingDown,
  CheckCircle2,
  Clock,
  AlertTriangle,
  Trophy,
  Zap,
  Flag,
  Edit,
  Trash2,
  Play,
  Sparkles,
} from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { GlassCard } from "@/components/ui/glass-card";
import { Skeleton } from "@/components/ui/skeleton";
import { toast } from "sonner";
import { fadeInUp, staggerContainer, staggerItem } from "@/lib/animations";
import type { Goal, GoalProgress } from "@/types";

export default function GoalsPage() {
  const [goals, setGoals] = useState<GoalProgress[]>([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState<"all" | "active" | "completed">("active");

  useEffect(() => {
    fetchGoals();
  }, [filter]);

  const fetchGoals = async () => {
    try {
      setLoading(true);
      const statusParam = filter === "all" ? "" : `?status=${filter}`;
      const response = await fetch(`/api/goals${statusParam}`);
      const data = await response.json();
      setGoals(data.goals || []);
    } catch (error) {
      console.error("Failed to fetch goals:", error);
      toast.error("Failed to load goals");
    } finally {
      setLoading(false);
    }
  };

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case "critical":
        return "bg-coral-red/10 text-coral-red border-coral-red/30 dark:bg-coral-red/20";
      case "high":
        return "bg-sunset-orange/10 text-sunset-orange border-sunset-orange/30 dark:bg-sunset-orange/20";
      case "medium":
        return "bg-electric-blue/10 text-electric-blue border-electric-blue/30 dark:bg-electric-blue/20";
      case "low":
        return "bg-gray-100 text-gray-600 border-gray-200 dark:bg-gray-900/20 dark:text-gray-400";
      default:
        return "bg-gray-100 text-gray-600";
    }
  };

  const getTypeIcon = (type: string) => {
    const iconClass = "h-5 w-5";
    switch (type) {
      case "problemCount":
        return <Target className={`${iconClass} text-electric-purple`} />;
      case "streakDays":
        return <Zap className={`${iconClass} text-golden-yellow`} />;
      case "companyReady":
        return <Trophy className={`${iconClass} text-sunset-orange`} />;
      case "patternMastery":
        return <Flag className={`${iconClass} text-electric-cyan`} />;
      default:
        return <Target className={`${iconClass} text-electric-purple`} />;
    }
  };

  const formatDate = (date: Date | string) => {
    return new Date(date).toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
      year: "numeric",
    });
  };

  const activeGoals = goals.filter((g) => g.status === "active");
  const completedGoals = goals.filter((g) => g.status === "completed");

  if (loading) {
    return (
      <div className="space-y-8">
        <div className="space-y-4">
          <Skeleton className="h-12 w-64" />
          <Skeleton className="h-6 w-96" />
        </div>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          {[1, 2, 3, 4].map((i) => (
            <Skeleton key={i} className="h-32" />
          ))}
        </div>
        <Skeleton className="h-96" />
      </div>
    );
  }

  return (
    <div className="space-y-8">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <div className="relative flex items-center justify-between">
          <div>
            <h1 className="relative text-4xl font-bold">
              <span className="text-gradient-purple-pink">
                Goals & Milestones
              </span>
            </h1>
            <p className="text-muted-foreground mt-2 text-base">
              <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
              Track your interview preparation progress with structured goals
            </p>
          </div>
          <Button
            className="bg-gradient-to-r from-electric-purple to-bright-pink hover:shadow-glow transition-all duration-300 border-0"
            size="lg"
          >
            <Plus className="h-5 w-5 mr-2" />
            New Goal
          </Button>
          </div>
        </motion.div>

        {/* Summary Stats */}
        <motion.div
          variants={staggerContainer}
          initial="hidden"
          animate="visible"
          className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6"
        >
          <motion.div variants={staggerItem}>
            <GlassCard variant="purple" size="default" hover className="bg-white/80">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground font-medium">Active Goals</p>
                  <p className="text-3xl font-bold text-electric-purple mt-1">
                    {activeGoals.length}
                  </p>
                </div>
                <motion.div
                  className="flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-electric-purple/20 to-bright-pink/20"
                  whileHover={{ scale: 1.1, rotate: 10 }}
                  transition={{ duration: 0.3 }}
                >
                  <Target className="h-6 w-6 text-electric-purple" />
                </motion.div>
              </div>
            </GlassCard>
          </motion.div>

          <motion.div variants={staggerItem}>
            <GlassCard variant="green" size="default" hover className="bg-white/80">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground font-medium">Completed</p>
                  <p className="text-3xl font-bold text-vibrant-green mt-1">
                    {completedGoals.length}
                  </p>
                </div>
                <motion.div
                  className="flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-vibrant-green/20 to-emerald-500/20"
                  whileHover={{ scale: 1.1, rotate: 10 }}
                  transition={{ duration: 0.3 }}
                >
                  <CheckCircle2 className="h-6 w-6 text-vibrant-green" />
                </motion.div>
              </div>
            </GlassCard>
          </motion.div>

          <motion.div variants={staggerItem}>
            <GlassCard variant="blue" size="default" hover className="bg-white/80">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground font-medium">On Track</p>
                  <p className="text-3xl font-bold text-electric-blue mt-1">
                    {activeGoals.filter((g) => g.isOnTrack).length}
                  </p>
                </div>
                <motion.div
                  className="flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-electric-blue/20 to-electric-cyan/20"
                  whileHover={{ scale: 1.1, rotate: 10 }}
                  transition={{ duration: 0.3 }}
                >
                  <TrendingUp className="h-6 w-6 text-electric-blue" />
                </motion.div>
              </div>
            </GlassCard>
          </motion.div>

          <motion.div variants={staggerItem}>
            <GlassCard variant="orange" size="default" hover className="bg-white/80">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground font-medium">At Risk</p>
                  <p className="text-3xl font-bold text-sunset-orange mt-1">
                    {activeGoals.filter((g) => !g.isOnTrack).length}
                  </p>
                </div>
                <motion.div
                  className="flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-sunset-orange/20 to-coral-red/20"
                  whileHover={{ scale: 1.1, rotate: 10 }}
                  transition={{ duration: 0.3 }}
                >
                  <AlertTriangle className="h-6 w-6 text-sunset-orange" />
                </motion.div>
              </div>
            </GlassCard>
          </motion.div>
        </motion.div>

        {/* Filter Tabs */}
        <Tabs value={filter} onValueChange={(v: any) => setFilter(v)}>
          <TabsList className="grid w-full max-w-md grid-cols-3 bg-white/60 backdrop-blur-xl">
            <TabsTrigger value="active">Active</TabsTrigger>
            <TabsTrigger value="completed">Completed</TabsTrigger>
            <TabsTrigger value="all">All</TabsTrigger>
          </TabsList>

          <TabsContent value={filter} className="mt-6">
            {goals.length === 0 ? (
              <GlassCard variant="default" className="bg-white/60 border-dashed">
                <div className="p-12 text-center">
                  <motion.div
                    initial={{ scale: 0 }}
                    animate={{ scale: 1 }}
                    transition={{ duration: 0.5, type: "spring" }}
                    className="flex h-16 w-16 items-center justify-center rounded-2xl bg-gradient-to-br from-electric-purple/20 to-bright-pink/20 mx-auto mb-4"
                  >
                    <Target className="h-8 w-8 text-electric-purple" />
                  </motion.div>
                  <h3 className="text-xl font-semibold mb-2">No goals yet</h3>
                  <p className="text-muted-foreground mb-6">
                    Create your first goal to start tracking your interview prep progress
                  </p>
                  <Button className="bg-gradient-to-r from-electric-purple to-bright-pink hover:shadow-glow transition-all duration-300 border-0">
                    <Plus className="h-4 w-4 mr-2" />
                    Create First Goal
                  </Button>
                </div>
              </GlassCard>
            ) : (
              <motion.div
                variants={staggerContainer}
                initial="hidden"
                animate="visible"
                className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
              >
                <AnimatePresence mode="popLayout">
                  {goals.map((goal) => (
                    <GoalCard key={goal.id} goal={goal} onUpdate={fetchGoals} />
                  ))}
                </AnimatePresence>
              </motion.div>
            )}
          </TabsContent>
        </Tabs>

        {/* Footer Tip */}
        <div className="text-center py-4">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.5 }}
            className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-muted/50 border border-border"
          >
            <span className="text-sm text-muted-foreground">
              💡 Tip: Set realistic milestones and track your progress regularly for best results!
            </span>
          </motion.div>
        </div>
      </div>
  );
}

function GoalCard({
  goal,
  onUpdate,
}: {
  goal: GoalProgress;
  onUpdate: () => void;
}) {
  const isCompleted = goal.status === "completed";
  const isOnTrack = goal.isOnTrack;
  const isPastDeadline = goal.daysRemaining < 0;

  const handleMarkComplete = async () => {
    try {
      await fetch(`/api/goals/${goal.id}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ status: "completed" }),
      });
      toast.success("Goal marked as complete! 🎉");
      onUpdate();
    } catch (error) {
      toast.error("Failed to update goal");
    }
  };

  // Get card variant based on status
  const getCardVariant = () => {
    if (isCompleted) return "green";
    if (isPastDeadline) return "orange";
    if (isOnTrack) return "blue";
    return "purple";
  };

  return (
    <motion.div
      layout
      variants={staggerItem}
      initial="hidden"
      animate="visible"
      exit="hidden"
    >
      <GlassCard
        variant={getCardVariant()}
        size="default"
        hover
        className={`h-full bg-white/80 ${
          isCompleted
            ? "border-vibrant-green/50"
            : isPastDeadline
            ? "border-sunset-orange/50"
            : isOnTrack
            ? "border-electric-blue/50"
            : "border-electric-purple/50"
        }`}
      >
        <div className="space-y-4">
          {/* Header */}
          <div className="flex items-start justify-between gap-2">
            <div className="flex-1">
              <div className="flex items-center gap-2 mb-2">
                <motion.div
                  whileHover={{ rotate: 360 }}
                  transition={{ duration: 0.5 }}
                >
                  {getTypeIcon(goal.type)}
                </motion.div>
                <Badge 
                  variant="outline" 
                  className={`${getPriorityColor(goal.priority)} font-semibold`}
                >
                  {goal.priority}
                </Badge>
              </div>
              <h3 className="text-lg font-bold line-clamp-2 mb-1">{goal.title}</h3>
              {goal.description && (
                <p className="text-sm text-muted-foreground line-clamp-2">
                  {goal.description}
                </p>
              )}
            </div>
          </div>

          {/* Progress */}
          <div>
            <div className="flex items-center justify-between text-sm mb-2">
              <span className="text-muted-foreground font-medium">Progress</span>
              <span className="font-bold">
                {goal.currentValue} / {goal.targetValue} {goal.unit}
              </span>
            </div>
            <Progress
              value={goal.progressPercentage}
              className={`h-2 ${
                isCompleted
                  ? "[&>*]:bg-gradient-to-r [&>*]:from-vibrant-green [&>*]:to-emerald-500"
                  : isOnTrack
                  ? "[&>*]:bg-gradient-to-r [&>*]:from-electric-blue [&>*]:to-electric-cyan"
                  : "[&>*]:bg-gradient-to-r [&>*]:from-sunset-orange [&>*]:to-coral-red"
              }`}
            />
            <p className="text-xs text-muted-foreground mt-1 font-medium">
              {goal.progressPercentage}% complete
            </p>
          </div>

          {/* Timeline */}
          <div className="flex items-center gap-4 text-sm">
            <div className="flex items-center gap-1 text-muted-foreground">
              <Calendar className="h-4 w-4" />
              <span className="font-medium">{formatDate(goal.deadline)}</span>
            </div>
            {!isCompleted && (
              <div
                className={`flex items-center gap-1 font-semibold ${
                  isPastDeadline
                    ? "text-coral-red"
                    : goal.daysRemaining <= 7
                    ? "text-sunset-orange"
                    : "text-muted-foreground"
                }`}
              >
                <Clock className="h-4 w-4" />
                <span>
                  {isPastDeadline
                    ? `${Math.abs(goal.daysRemaining)}d overdue`
                    : `${goal.daysRemaining}d left`}
                </span>
              </div>
            )}
          </div>

          {/* Status Indicator */}
          {!isCompleted && (
            <div className="flex items-center gap-2 text-sm">
              {isOnTrack ? (
                <div className="flex items-center gap-1 text-vibrant-green">
                  <TrendingUp className="h-4 w-4" />
                  <span className="font-semibold">On track</span>
                </div>
              ) : (
                <div className="flex items-center gap-1 text-sunset-orange">
                  <TrendingDown className="h-4 w-4" />
                  <span className="font-semibold">Needs attention</span>
                </div>
              )}
              <span className="text-muted-foreground font-medium">
                • {goal.velocity} {goal.unit}/day
              </span>
            </div>
          )}

          {/* Next Milestone */}
          {goal.nextMilestone && (
            <motion.div
              className="p-3 rounded-xl bg-gradient-to-br from-electric-purple/10 to-bright-pink/10 border border-electric-purple/30"
              whileHover={{ scale: 1.02 }}
              transition={{ duration: 0.2 }}
            >
              <p className="text-xs text-electric-purple font-bold mb-1">
                Next Milestone
              </p>
              <p className="text-sm font-semibold">{goal.nextMilestone.title}</p>
              <p className="text-xs text-muted-foreground mt-1 font-medium">
                Due {formatDate(goal.nextMilestone.dueDate)}
              </p>
            </motion.div>
          )}

          {/* Actions */}
          <div className="flex gap-2 pt-2">
            {!isCompleted && (
              <Button
                size="sm"
                variant="outline"
                className="flex-1 hover:bg-vibrant-green/10 hover:text-vibrant-green hover:border-vibrant-green/50 transition-all"
                onClick={handleMarkComplete}
              >
                <CheckCircle2 className="h-4 w-4 mr-1" />
                Complete
              </Button>
            )}
            <Button 
              size="sm" 
              variant="ghost" 
              className="flex-1 hover:bg-electric-purple/10 hover:text-electric-purple transition-all"
            >
              <Edit className="h-4 w-4 mr-1" />
              Edit
            </Button>
          </div>
        </div>
      </GlassCard>
    </motion.div>
  );
}

function getTypeIcon(type: string) {
  const iconClass = "h-5 w-5";
  switch (type) {
    case "problemCount":
      return <Target className={`${iconClass} text-electric-purple`} />;
    case "streakDays":
      return <Zap className={`${iconClass} text-golden-yellow`} />;
    case "companyReady":
      return <Trophy className={`${iconClass} text-sunset-orange`} />;
    case "patternMastery":
      return <Flag className={`${iconClass} text-electric-cyan`} />;
    default:
      return <Target className={`${iconClass} text-electric-purple`} />;
  }
}

function getPriorityColor(priority: string) {
  switch (priority) {
    case "critical":
      return "bg-coral-red/10 text-coral-red border-coral-red/30 dark:bg-coral-red/20";
    case "high":
      return "bg-sunset-orange/10 text-sunset-orange border-sunset-orange/30 dark:bg-sunset-orange/20";
    case "medium":
      return "bg-electric-blue/10 text-electric-blue border-electric-blue/30 dark:bg-electric-blue/20";
    case "low":
      return "bg-gray-100 text-gray-600 border-gray-200 dark:bg-gray-900/20 dark:text-gray-400";
    default:
      return "bg-gray-100 text-gray-600";
  }
}

function formatDate(date: Date | string) {
  return new Date(date).toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
  });
}
