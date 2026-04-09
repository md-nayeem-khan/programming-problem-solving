"use client";

import { useEffect, useMemo, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import {
  AlertTriangle,
  Calendar,
  Check,
  CheckCircle2,
  Clock,
  Edit,
  Flag,
  Plus,
  Sparkles,
  Target,
  Trash2,
  TrendingUp,
  X,
} from "lucide-react";
import { toast } from "sonner";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Progress } from "@/components/ui/progress";
import { Skeleton } from "@/components/ui/skeleton";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Textarea } from "@/components/ui/textarea";
import { staggerContainer, staggerItem } from "@/lib/animations";
import type { GoalPriority, GoalProgress } from "@/types";

type GoalFilter = "all" | "active" | "completed";

type GoalCardState = GoalProgress & {
  totalMilestones: number;
  completedMilestones: number;
  milestoneProgress: number;
  isDerivedCompleted: boolean;
  isDerivedAtRisk: boolean;
  isDerivedOnTrack: boolean;
};

type MilestoneForm = {
  id?: number;
  title: string;
  description: string;
  dueDate: string;
  targetValue: number;
};

type GoalForm = {
  title: string;
  description: string;
  startDate: string;
  deadline: string;
  priority: GoalPriority;
  milestones: MilestoneForm[];
};

const PRIORITIES: GoalPriority[] = ["critical", "high", "medium", "low"];

function getPriorityBadge(priority: GoalPriority) {
  switch (priority) {
    case "critical":
      return "bg-gradient-to-r from-rose-500 to-red-500 text-white border-0 shadow-lg shadow-rose-500/30";
    case "high":
      return "bg-gradient-to-r from-orange-500 to-amber-500 text-white border-0 shadow-lg shadow-orange-500/30";
    case "medium":
      return "bg-gradient-to-r from-blue-500 to-indigo-500 text-white border-0 shadow-lg shadow-blue-500/30";
    case "low":
      return "bg-gradient-to-r from-slate-500 to-gray-600 text-white border-0 shadow-lg shadow-slate-500/30";
    default:
      return "";
  }
}

function getStatusCardClasses(goal: GoalCardState) {
  if (goal.isDerivedCompleted) {
    return "bg-gradient-to-br from-emerald-50/90 via-green-50/60 to-teal-50/90 dark:from-emerald-950/40 dark:via-green-900/20 dark:to-teal-950/40 border-2 border-emerald-200/60 dark:border-emerald-800/60";
  }

  if (goal.isDerivedAtRisk) {
    return "bg-gradient-to-br from-rose-50/90 via-orange-50/60 to-amber-50/90 dark:from-rose-950/40 dark:via-orange-900/20 dark:to-amber-950/40 border-2 border-rose-200/60 dark:border-rose-800/60";
  }

  return "bg-gradient-to-br from-violet-50/90 via-purple-50/60 to-fuchsia-50/90 dark:from-violet-950/40 dark:via-purple-900/20 dark:to-fuchsia-950/40 border-2 border-violet-200/60 dark:border-violet-800/60";
}

function toDateInputValue(value?: Date | string) {
  if (!value) return "";
  const date = new Date(value);
  const year = date.getFullYear();
  const month = `${date.getMonth() + 1}`.padStart(2, "0");
  const day = `${date.getDate()}`.padStart(2, "0");
  return `${year}-${month}-${day}`;
}

function formatHumanDate(value?: Date | string) {
  if (!value) return "-";
  return new Date(value).toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
  });
}

function createDefaultGoalForm(): GoalForm {
  const today = toDateInputValue(new Date());
  const nextMonth = new Date();
  nextMonth.setMonth(nextMonth.getMonth() + 1);

  return {
    title: "",
    description: "",
    startDate: today,
    deadline: toDateInputValue(nextMonth),
    priority: "medium",
    milestones: [
      {
        title: "",
        description: "",
        dueDate: toDateInputValue(nextMonth),
        targetValue: 1,
      },
    ],
  };
}

function deriveGoalState(goal: GoalProgress): GoalCardState {
  const totalMilestones = goal.milestones?.length ?? 0;
  const completedMilestones = goal.milestones?.filter((m) => m.completed).length ?? 0;

  const milestoneProgress =
    totalMilestones > 0
      ? Math.round((completedMilestones / totalMilestones) * 100)
      : Math.min(100, Math.max(0, goal.progressPercentage || 0));

  const now = new Date();
  const hasOverdueMilestone =
    goal.milestones?.some((m) => !m.completed && new Date(m.dueDate) < now) ?? false;
  const deadlinePassed = new Date(goal.deadline) < now;

  const isDerivedCompleted =
    goal.status === "completed" ||
    (totalMilestones > 0 && completedMilestones === totalMilestones);

  const isDerivedAtRisk = !isDerivedCompleted && (hasOverdueMilestone || deadlinePassed);
  const isDerivedOnTrack = !isDerivedCompleted && !isDerivedAtRisk;

  return {
    ...goal,
    totalMilestones,
    completedMilestones,
    milestoneProgress,
    isDerivedCompleted,
    isDerivedAtRisk,
    isDerivedOnTrack,
  };
}

export default function GoalsPage() {
  const [goals, setGoals] = useState<GoalProgress[]>([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState<GoalFilter>("active");

  const [dialogOpen, setDialogOpen] = useState(false);
  const [savingGoal, setSavingGoal] = useState(false);
  const [editingGoalId, setEditingGoalId] = useState<number | null>(null);
  const [goalForm, setGoalForm] = useState<GoalForm>(createDefaultGoalForm());

  const derivedGoals = useMemo(() => goals.map(deriveGoalState), [goals]);

  const summary = useMemo(() => {
    const active = derivedGoals.filter(
      (g) => g.status === "active" && !g.isDerivedCompleted
    );

    return {
      activeGoals: active.length,
      completedGoals: derivedGoals.filter((g) => g.isDerivedCompleted).length,
      onTrack: active.filter((g) => g.isDerivedOnTrack).length,
      atRisk: active.filter((g) => g.isDerivedAtRisk).length,
    };
  }, [derivedGoals]);

  const filteredGoals = useMemo(() => {
    if (filter === "all") return derivedGoals;
    if (filter === "completed") {
      return derivedGoals.filter((g) => g.isDerivedCompleted);
    }

    return derivedGoals.filter((g) => g.status === "active" && !g.isDerivedCompleted);
  }, [derivedGoals, filter]);

  const fetchGoals = async () => {
    try {
      setLoading(true);
      const response = await fetch("/api/goals");
      if (!response.ok) {
        throw new Error("Could not fetch goals");
      }

      const data = await response.json();
      setGoals(data.goals || []);
    } catch (error) {
      console.error("Failed to fetch goals:", error);
      toast.error("Failed to load goals");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchGoals();
  }, []);

  const openCreateGoalDialog = () => {
    setEditingGoalId(null);
    setGoalForm(createDefaultGoalForm());
    setDialogOpen(true);
  };

  const openEditGoalDialog = (goal: GoalProgress) => {
    setEditingGoalId(goal.id);
    setGoalForm({
      title: goal.title,
      description: goal.description || "",
      startDate: toDateInputValue(goal.startDate),
      deadline: toDateInputValue(goal.deadline),
      priority: goal.priority,
      milestones:
        goal.milestones?.map((m) => ({
          id: m.id,
          title: m.title,
          description: m.description || "",
          dueDate: toDateInputValue(m.dueDate),
          targetValue: m.targetValue,
        })) || [],
    });
    setDialogOpen(true);
  };

  const upsertMilestoneRow = (index: number, patch: Partial<MilestoneForm>) => {
    setGoalForm((prev) => ({
      ...prev,
      milestones: prev.milestones.map((m, i) => (i === index ? { ...m, ...patch } : m)),
    }));
  };

  const addMilestoneRow = () => {
    setGoalForm((prev) => ({
      ...prev,
      milestones: [
        ...prev.milestones,
        {
          title: "",
          description: "",
          dueDate: prev.deadline,
          targetValue: prev.milestones.length + 1,
        },
      ],
    }));
  };

  const deleteMilestoneRow = (index: number) => {
    setGoalForm((prev) => {
      if (prev.milestones.length <= 1) {
        toast.error("A goal must have at least one milestone");
        return prev;
      }

      return {
        ...prev,
        milestones: prev.milestones.filter((_, i) => i !== index),
      };
    });
  };

  const validateGoalForm = () => {
    if (!goalForm.title.trim()) {
      toast.error("Goal title is required");
      return false;
    }

    if (!goalForm.startDate || !goalForm.deadline) {
      toast.error("Start and end dates are required");
      return false;
    }

    if (new Date(goalForm.startDate) > new Date(goalForm.deadline)) {
      toast.error("End date must be after start date");
      return false;
    }

    if (goalForm.milestones.length === 0) {
      toast.error("At least one milestone is required");
      return false;
    }

    for (let i = 0; i < goalForm.milestones.length; i++) {
      const milestone = goalForm.milestones[i];
      if (!milestone.title.trim()) {
        toast.error(`Milestone ${i + 1} title is required`);
        return false;
      }
      if (!milestone.dueDate) {
        toast.error(`Milestone ${i + 1} due date is required`);
        return false;
      }
    }

    return true;
  };

  const handleSaveGoal = async () => {
    if (!validateGoalForm()) return;

    setSavingGoal(true);
    try {
      if (editingGoalId) {
        const goalUpdateResponse = await fetch(`/api/goals/${editingGoalId}`, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            title: goalForm.title.trim(),
            description: goalForm.description.trim() || null,
            startDate: goalForm.startDate,
            deadline: goalForm.deadline,
            priority: goalForm.priority,
            type: "custom",
            unit: "percentage",
          }),
        });

        if (!goalUpdateResponse.ok) {
          throw new Error("Failed to update goal");
        }

        const existingGoal = goals.find((g) => g.id === editingGoalId);
        const existingMilestones = existingGoal?.milestones || [];
        const incomingMilestoneIds = new Set(
          goalForm.milestones.filter((m) => m.id).map((m) => m.id)
        );

        for (const oldMilestone of existingMilestones) {
          if (!incomingMilestoneIds.has(oldMilestone.id)) {
            const removeResponse = await fetch(`/api/milestones/${oldMilestone.id}`, {
              method: "DELETE",
            });
            if (!removeResponse.ok) {
              const err = await removeResponse.json().catch(() => ({}));
              throw new Error(err.error || "Failed to delete milestone");
            }
          }
        }

        for (let i = 0; i < goalForm.milestones.length; i++) {
          const milestone = goalForm.milestones[i];
          const targetValue = i + 1;

          if (milestone.id) {
            const patchResponse = await fetch(`/api/milestones/${milestone.id}`, {
              method: "PATCH",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({
                title: milestone.title.trim(),
                description: milestone.description.trim() || null,
                dueDate: milestone.dueDate,
                targetValue,
              }),
            });

            if (!patchResponse.ok) {
              throw new Error("Failed to update milestone");
            }
          } else {
            const createResponse = await fetch(`/api/goals/${editingGoalId}/milestones`, {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({
                title: milestone.title.trim(),
                description: milestone.description.trim() || null,
                dueDate: milestone.dueDate,
                targetValue,
              }),
            });

            if (!createResponse.ok) {
              throw new Error("Failed to create milestone");
            }
          }
        }

        toast.success("Goal updated");
      } else {
        const createResponse = await fetch("/api/goals", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            title: goalForm.title.trim(),
            description: goalForm.description.trim() || null,
            type: "custom",
            targetValue: 100,
            unit: "percentage",
            startDate: goalForm.startDate,
            deadline: goalForm.deadline,
            priority: goalForm.priority,
            milestones: goalForm.milestones.map((m, index) => ({
              title: m.title.trim(),
              description: m.description.trim() || null,
              dueDate: m.dueDate,
              targetValue: index + 1,
            })),
          }),
        });

        if (!createResponse.ok) {
          const err = await createResponse.json().catch(() => ({}));
          throw new Error(err.error || "Failed to create goal");
        }

        toast.success("Goal created");
      }

      setDialogOpen(false);
      setGoalForm(createDefaultGoalForm());
      setEditingGoalId(null);
      fetchGoals();
    } catch (error) {
      console.error(error);
      toast.error(error instanceof Error ? error.message : "Could not save goal");
    } finally {
      setSavingGoal(false);
    }
  };

  const deleteGoal = async (goalId: number) => {
    const confirmed = window.confirm("Delete this goal and all its milestones?");
    if (!confirmed) return;

    try {
      const response = await fetch(`/api/goals/${goalId}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error("Failed to delete goal");
      }

      toast.success("Goal deleted");
      fetchGoals();
    } catch (error) {
      console.error(error);
      toast.error("Could not delete goal");
    }
  };

  const toggleMilestone = async (goal: GoalCardState, milestoneId: number, checked: boolean) => {
    try {
      const milestoneResponse = await fetch(`/api/milestones/${milestoneId}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ completed: checked }),
      });

      if (!milestoneResponse.ok) {
        throw new Error("Failed to update milestone");
      }

      const nextCompletedCount =
        goal.completedMilestones + (checked ? 1 : -1);
      const totalMilestones = Math.max(1, goal.totalMilestones);
      const shouldComplete = nextCompletedCount >= totalMilestones;

      if (shouldComplete && goal.status !== "completed") {
        await fetch(`/api/goals/${goal.id}`, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ status: "completed" }),
        });
      }

      if (!shouldComplete && goal.status === "completed") {
        await fetch(`/api/goals/${goal.id}`, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ status: "active" }),
        });
      }

      toast.success("Milestone updated");
      fetchGoals();
    } catch (error) {
      console.error(error);
      toast.error("Could not update milestone");
    }
  };

  if (loading) {
    return (
      <div className="space-y-6 max-w-7xl mx-auto">
        <Skeleton className="h-12 w-64" />
        <div className="grid grid-cols-1 gap-4 md:grid-cols-4">
          {[1, 2, 3, 4].map((item) => (
            <Skeleton key={item} className="h-28" />
          ))}
        </div>
        <Skeleton className="h-80" />
      </div>
    );
  }

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.45 }}
        className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between"
      >
        <div>
          <h1 className="text-4xl font-bold">
            <span className="text-gradient-purple-pink">Goals and Milestones</span>
          </h1>
          <p className="text-muted-foreground mt-2 text-base">
            <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
            Build goals with dates and track completion from milestones.
          </p>
        </div>

        <Button
          className="bg-gradient-to-r from-electric-purple to-bright-pink hover:shadow-glow transition-all duration-300 border-0"
          onClick={openCreateGoalDialog}
        >
          <Plus className="mr-1 h-4 w-4" />
          New Goal
        </Button>
      </motion.div>

      <motion.div
        variants={staggerContainer}
        initial="hidden"
        animate="visible"
        className="grid grid-cols-1 gap-4 md:grid-cols-4"
      >
        <SummaryCard
          title="Active Goals"
          value={summary.activeGoals}
          icon={<Target className="h-5 w-5 text-white" />}
          cardClassName="bg-gradient-to-br from-pink-100 via-purple-50 to-fuchsia-100 dark:from-pink-950/40 dark:via-purple-900/20 dark:to-fuchsia-950/40 border-2 border-pink-200/60 dark:border-pink-800/60"
          iconClassName="bg-gradient-to-br from-pink-500 to-purple-500 shadow-pink-500/30"
          valueClassName="bg-gradient-to-r from-pink-600 to-purple-600"
        />
        <SummaryCard
          title="Completed"
          value={summary.completedGoals}
          icon={<CheckCircle2 className="h-5 w-5 text-white" />}
          cardClassName="bg-gradient-to-br from-emerald-100 via-green-50 to-teal-100 dark:from-emerald-950/40 dark:via-green-900/20 dark:to-teal-950/40 border-2 border-emerald-200/60 dark:border-emerald-800/60"
          iconClassName="bg-gradient-to-br from-emerald-500 to-teal-500 shadow-emerald-500/30"
          valueClassName="bg-gradient-to-r from-emerald-600 to-teal-600"
        />
        <SummaryCard
          title="On Track"
          value={summary.onTrack}
          icon={<TrendingUp className="h-5 w-5 text-white" />}
          cardClassName="bg-gradient-to-br from-blue-100 via-indigo-50 to-violet-100 dark:from-blue-950/40 dark:via-indigo-900/20 dark:to-violet-950/40 border-2 border-blue-200/60 dark:border-blue-800/60"
          iconClassName="bg-gradient-to-br from-blue-500 to-indigo-500 shadow-blue-500/30"
          valueClassName="bg-gradient-to-r from-blue-600 to-indigo-600"
        />
        <SummaryCard
          title="At Risk"
          value={summary.atRisk}
          icon={<AlertTriangle className="h-5 w-5 text-white" />}
          cardClassName="bg-gradient-to-br from-orange-100 via-amber-50 to-yellow-100 dark:from-orange-950/40 dark:via-amber-900/20 dark:to-yellow-950/40 border-2 border-orange-200/60 dark:border-orange-800/60"
          iconClassName="bg-gradient-to-br from-orange-500 to-amber-500 shadow-orange-500/30"
          valueClassName="bg-gradient-to-r from-orange-600 to-amber-600"
        />
      </motion.div>

      <Tabs value={filter} onValueChange={(v) => setFilter(v as GoalFilter)}>
        <TabsList className="grid w-fit grid-cols-3 bg-white/60 backdrop-blur-xl justify-start">
          <TabsTrigger
            value="active"
            className="justify-start px-4 data-[state=active]:bg-background data-[state=active]:text-foreground"
          >
            Active
          </TabsTrigger>
          <TabsTrigger
            value="completed"
            className="justify-start px-4 data-[state=active]:bg-background data-[state=active]:text-foreground"
          >
            Completed
          </TabsTrigger>
          <TabsTrigger
            value="all"
            className="justify-start px-4 data-[state=active]:bg-background data-[state=active]:text-foreground"
          >
            All
          </TabsTrigger>
        </TabsList>
      </Tabs>

      {filteredGoals.length === 0 ? (
        <Card className="bg-gradient-to-br from-violet-50/90 via-pink-50/60 to-fuchsia-50/90 dark:from-violet-950/40 dark:via-pink-900/20 dark:to-fuchsia-950/40 border-2 border-violet-200/60 dark:border-violet-800/60">
          <CardContent className="py-14 text-center">
            <Target className="mx-auto mb-3 h-8 w-8 text-electric-purple" />
            <p className="text-lg font-semibold">No goals in this view</p>
            <p className="text-sm text-muted-foreground">
              Create a new goal and add at least one milestone.
            </p>
          </CardContent>
        </Card>
      ) : (
        <motion.div
          variants={staggerContainer}
          initial="hidden"
          animate="visible"
          className="space-y-5"
        >
          <AnimatePresence mode="popLayout">
          {filteredGoals.map((goal) => {
            return (
              <motion.div key={goal.id} variants={staggerItem} layout>
              <Card className={`${getStatusCardClasses(goal)} overflow-hidden relative group`}>
                <div className="absolute -top-10 -right-10 h-28 w-28 rounded-full bg-white/30 blur-2xl" />
                <CardHeader>
                  <div className="flex items-start justify-between gap-4">
                    <div>
                      <CardTitle className="text-xl">{goal.title}</CardTitle>
                      {goal.description && (
                        <p className="mt-1 text-sm text-muted-foreground">{goal.description}</p>
                      )}
                    </div>
                    <div className="flex gap-2">
                      <Badge className={`capitalize ${getPriorityBadge(goal.priority)}`}>
                        {goal.priority}
                      </Badge>
                      <Badge
                        variant="outline"
                        className={`capitalize ${
                          goal.isDerivedCompleted
                            ? "bg-emerald-100 dark:bg-emerald-900/30 text-emerald-700 dark:text-emerald-300 border-emerald-300 dark:border-emerald-700"
                            : "bg-indigo-100 dark:bg-indigo-900/30 text-indigo-700 dark:text-indigo-300 border-indigo-300 dark:border-indigo-700"
                        }`}
                      >
                        {goal.isDerivedCompleted ? "completed" : "active"}
                      </Badge>
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="flex flex-wrap items-center gap-4 text-sm text-muted-foreground">
                    <span className="inline-flex items-center gap-1 rounded-full border border-border/70 bg-white/60 px-2.5 py-1 dark:bg-slate-900/40">
                      <Calendar className="h-4 w-4" />
                      {formatHumanDate(goal.startDate)} - {formatHumanDate(goal.deadline)}
                    </span>
                    <span className="inline-flex items-center gap-1 rounded-full border border-border/70 bg-white/60 px-2.5 py-1 dark:bg-slate-900/40">
                      <Clock className="h-4 w-4" />
                      {goal.daysRemaining >= 0
                        ? `${goal.daysRemaining} days left`
                        : `${Math.abs(goal.daysRemaining)} days overdue`}
                    </span>
                    {goal.isDerivedAtRisk && (
                      <span className="inline-flex items-center gap-1 rounded-full border border-amber-300/80 bg-amber-100/80 px-2.5 py-1 text-amber-700 dark:border-amber-700 dark:bg-amber-900/40 dark:text-amber-300">
                        <Flag className="h-4 w-4" />
                        At Risk
                      </span>
                    )}
                  </div>

                  <div>
                    <div className="mb-1 flex items-center justify-between text-sm">
                      <span>Goal Progress</span>
                      <span className="font-semibold">
                        {goal.milestoneProgress}% ({goal.completedMilestones}/{goal.totalMilestones} milestones)
                      </span>
                    </div>
                    <Progress
                      value={goal.milestoneProgress}
                      className="h-2 [&>*]:bg-gradient-to-r [&>*]:from-electric-purple [&>*]:to-bright-pink"
                    />
                  </div>

                  <div className="grid grid-cols-1 gap-2 rounded-md border border-violet-200/70 bg-white/50 p-3 md:grid-cols-2 dark:border-violet-800/50 dark:bg-slate-900/30">
                    <p className="text-sm font-medium md:col-span-2">Milestones</p>
                    {goal.milestones?.map((milestone) => (
                      <div
                        key={milestone.id}
                        className="flex items-start justify-between gap-3 rounded-md border border-border/60 bg-background/70 p-2"
                      >
                        <div className="flex items-start gap-2">
                          <Checkbox
                            checked={milestone.completed}
                            className="mt-0.5 h-5 w-5 rounded-full data-checked:bg-electric-purple data-checked:border-electric-purple"
                            onCheckedChange={(value) =>
                              toggleMilestone(goal, milestone.id, value === true)
                            }
                          />
                          <div>
                            <p
                              className={`text-sm font-medium ${
                                milestone.completed ? "text-muted-foreground line-through" : ""
                              }`}
                            >
                              {milestone.title}
                            </p>
                            <p className="text-xs text-muted-foreground">
                              Due {formatHumanDate(milestone.dueDate)}
                            </p>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>

                  <div className="flex items-center justify-end gap-2 opacity-0 transition-opacity group-hover:opacity-100">
                    <motion.button
                      whileHover={{ scale: 1.1 }}
                      whileTap={{ scale: 0.95 }}
                      onClick={() => openEditGoalDialog(goal)}
                      className="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-blue-100 text-blue-600 transition-colors hover:bg-blue-200 dark:bg-blue-900/30 dark:text-blue-400 dark:hover:bg-blue-900/50"
                      title="Edit Goal"
                      aria-label="Edit Goal"
                    >
                      <Edit className="h-4 w-4" />
                    </motion.button>

                    <motion.button
                      whileHover={{ scale: 1.1 }}
                      whileTap={{ scale: 0.95 }}
                      onClick={() => deleteGoal(goal.id)}
                      className="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-red-100 text-red-600 transition-colors hover:bg-red-200 dark:bg-red-900/30 dark:text-red-400 dark:hover:bg-red-900/50"
                      title="Delete Goal"
                      aria-label="Delete Goal"
                    >
                      <Trash2 className="h-4 w-4" />
                    </motion.button>
                  </div>
                </CardContent>
              </Card>
              </motion.div>
            );
          })}
          </AnimatePresence>
        </motion.div>
      )}

      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent className="w-full max-w-[650px] min-w-[320px] sm:min-w-[500px] lg:min-w-[600px] max-h-[90vh] overflow-hidden bg-white/90 backdrop-blur-md border border-gray-200/60 rounded-2xl shadow-xl mx-4">
          <DialogHeader className="pb-6 border-b border-gray-200">
            <DialogTitle className="text-2xl font-semibold text-gray-900">
              {editingGoalId ? "Edit Goal" : "Create Goal"}
            </DialogTitle>
            <DialogDescription className="text-sm text-gray-500">
              Every goal must include at least one milestone.
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-8 pt-8 overflow-y-auto overflow-x-hidden max-h-[75vh] pr-2 w-full">
            <div className="grid gap-2">
              <Label htmlFor="goal-title">Title</Label>
              <Input
                id="goal-title"
                value={goalForm.title}
                onChange={(e) =>
                  setGoalForm((prev) => ({ ...prev, title: e.target.value }))
                }
                placeholder="Example: Finish graph and DP prep"
                className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
              />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="goal-description">Description</Label>
              <Textarea
                id="goal-description"
                value={goalForm.description}
                onChange={(e) =>
                  setGoalForm((prev) => ({ ...prev, description: e.target.value }))
                }
                placeholder="Optional notes"
                className="min-h-[80px] resize-none !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
              />
            </div>

            <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
              <div className="grid gap-2">
                <Label htmlFor="goal-start-date">Start Date</Label>
                <Input
                  id="goal-start-date"
                  type="date"
                  value={goalForm.startDate}
                  onChange={(e) =>
                    setGoalForm((prev) => ({ ...prev, startDate: e.target.value }))
                  }
                  className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                />
              </div>

              <div className="grid gap-2">
                <Label htmlFor="goal-end-date">End Date</Label>
                <Input
                  id="goal-end-date"
                  type="date"
                  value={goalForm.deadline}
                  onChange={(e) =>
                    setGoalForm((prev) => ({ ...prev, deadline: e.target.value }))
                  }
                  className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                />
              </div>

              <div className="grid gap-2">
                <Label htmlFor="goal-priority">Priority</Label>
                <select
                  id="goal-priority"
                  className="h-10 w-full rounded-md border border-purple-200 bg-white px-2.5 text-sm outline-none ring-0 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                  value={goalForm.priority}
                  onChange={(e) =>
                    setGoalForm((prev) => ({
                      ...prev,
                      priority: e.target.value as GoalPriority,
                    }))
                  }
                >
                  {PRIORITIES.map((priority) => (
                    <option key={priority} value={priority} className="capitalize">
                      {priority}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="space-y-3 rounded-lg border border-purple-200 bg-white p-3">
              <div className="flex items-center justify-between">
                <p className="font-medium">Milestones</p>
                <Button
                  size="sm"
                  variant="outline"
                  className="border-purple-200 hover:bg-purple-50 hover:text-purple-700"
                  onClick={addMilestoneRow}
                >
                  <Plus className="mr-1 h-3.5 w-3.5" />
                  Add Milestone
                </Button>
              </div>

              {goalForm.milestones.map((milestone, index) => (
                <div key={`${milestone.id || "new"}-${index}`} className="space-y-2 rounded-md border border-purple-200 bg-white p-3">
                  <div className="grid grid-cols-1 gap-2 md:grid-cols-2">
                    <div className="grid gap-2">
                      <Label>Milestone Title</Label>
                      <Input
                        value={milestone.title}
                        onChange={(e) =>
                          upsertMilestoneRow(index, { title: e.target.value })
                        }
                        placeholder="Example: Complete first 20 graph problems"
                        className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                      />
                    </div>
                    <div className="grid gap-2">
                      <Label>Due Date</Label>
                      <Input
                        type="date"
                        value={milestone.dueDate}
                        onChange={(e) =>
                          upsertMilestoneRow(index, { dueDate: e.target.value })
                        }
                        className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                      />
                    </div>
                  </div>

                  <div className="grid gap-2">
                    <Label>Description</Label>
                    <Textarea
                      value={milestone.description}
                      onChange={(e) =>
                        upsertMilestoneRow(index, { description: e.target.value })
                      }
                      placeholder="Optional description"
                      className="min-h-[80px] resize-none !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                    />
                  </div>

                  <div className="flex justify-end">
                    <Button
                      type="button"
                      size="sm"
                      variant="destructive"
                      onClick={() => deleteMilestoneRow(index)}
                    >
                      <Trash2 className="mr-1 h-3.5 w-3.5" />
                      Remove
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={() => setDialogOpen(false)}>
              <X className="h-4 w-4 mr-2" />
              Cancel
            </Button>
            <Button
              onClick={handleSaveGoal}
              disabled={savingGoal}
              className="bg-gradient-purple-pink hover:shadow-md transition-all duration-300 disabled:opacity-50"
            >
              {savingGoal ? (
                <div className="h-4 w-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2" />
              ) : (
                <Check className="h-4 w-4 mr-2" />
              )}
              {savingGoal ? "Saving..." : editingGoalId ? "Update Goal" : "Create Goal"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}

function SummaryCard({
  title,
  value,
  icon,
  cardClassName,
  iconClassName,
  valueClassName,
}: {
  title: string;
  value: number;
  icon: React.ReactNode;
  cardClassName: string;
  iconClassName: string;
  valueClassName: string;
}) {
  return (
    <motion.div variants={staggerItem}>
    <Card className={`${cardClassName} overflow-hidden relative`}>
      <div className="absolute -top-8 -right-8 w-24 h-24 rounded-full bg-white/20 blur-2xl" />
      <CardContent className="flex items-center justify-between p-4 relative">
        <div>
          <p className="text-sm text-muted-foreground">{title}</p>
          <p className={`text-2xl font-bold bg-clip-text text-transparent ${valueClassName}`}>{value}</p>
        </div>
        <div className={`rounded-xl p-2.5 shadow-lg ${iconClassName}`}>{icon}</div>
      </CardContent>
    </Card>
    </motion.div>
  );
}
