"use client";

import { useEffect, useState, useMemo } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { useRouter } from "next/navigation";
import {
  Check,
  Brain,
  Clock,
  ChevronRight,
  Sparkles,
  BarChart3,
  AlertCircle,
  Edit,
  Plus,
  Trash2,
  X,
} from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";
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
import { Textarea } from "@/components/ui/textarea";
import { toast } from "sonner";

import type { Variants } from "framer-motion";
import { staggerContainer, staggerItem } from "@/lib/animations";
import { calculateMasteryFromSignals } from "@/lib/analytics/pattern-metrics";
import { calculatePatternSummary } from "@/lib/analytics/pattern-summary";

// Types
interface PatternStat {
  id: number;
  name: string;
  category: string;
  solved: number;
  avgTimeMinutes: number;
  confidence: "Weak" | "Medium" | "Strong";
  hintUsageRate: number;
  masteryPercentage: number;
  problemIds: number[];
  solvedProblemIds: number[];
}

interface PatternRecord {
  id: number;
  name: string;
  category: string;
  description?: string | null;
}

type PatternForm = {
  name: string;
  category: string;
  description: string;
};

// Animation variants
const tableRowVariants: Variants = {
  hidden: { opacity: 0, x: -20 },
  visible: (i: number) => ({
    opacity: 1,
    x: 0,
    transition: {
      delay: i * 0.04,
      duration: 0.25,
      ease: [0.25, 0.46, 0.45, 0.94],
    },
  }),
  hover: {
    backgroundColor: "rgba(147, 51, 234, 0.05)",
    transition: { duration: 0.2 },
  },
};

export default function PatternsPage() {
  const router = useRouter();
  const [patterns, setPatterns] = useState<PatternStat[]>([]);
  const [patternRecords, setPatternRecords] = useState<PatternRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [savingPattern, setSavingPattern] = useState(false);
  const [deletingPattern, setDeletingPattern] = useState(false);
  const [editingPatternId, setEditingPatternId] = useState<number | null>(null);
  const [pendingDeletePattern, setPendingDeletePattern] = useState<PatternRecord | null>(null);
  const [patternForm, setPatternForm] = useState<PatternForm>({
    name: "",
    category: "",
    description: "",
  });

  const fetchPatterns = async () => {
    try {
      setLoading(true);
      setError(null);

      const [analyticsResponse, patternsResponse] = await Promise.all([
        fetch("/api/analytics/patterns/confidence"),
        fetch("/api/patterns"),
      ]);

      if (!patternsResponse.ok) {
        throw new Error("Failed to fetch pattern list");
      }

      const patternsData = await patternsResponse.json();
      const records: PatternRecord[] = Array.isArray(patternsData.patterns)
        ? patternsData.patterns
        : [];

      setPatternRecords(records);

      let analyticsPatterns: any[] = [];
      if (analyticsResponse.ok) {
        const analyticsData = await analyticsResponse.json();
        analyticsPatterns = Array.isArray(analyticsData.patterns) ? analyticsData.patterns : [];
      }

      const analyticsByName = new Map(
        analyticsPatterns.map((item) => [String(item.pattern).toLowerCase(), item])
      );

      const transformedPatterns: PatternStat[] = records.map((pattern) => {
        const analytics = analyticsByName.get(pattern.name.toLowerCase());
        const avgTimeSeconds = Number(analytics?.avgTimeSeconds || 0);
        const hintUsageRate = Number(analytics?.hintUsageRate || 0);
        const totalSolved = Number(analytics?.totalSolved || 0);
        const confidence =
          analytics?.confidence === "Strong" ||
          analytics?.confidence === "Medium" ||
          analytics?.confidence === "Weak"
            ? analytics.confidence
            : "Weak";

        return {
          id: pattern.id,
          name: pattern.name,
          category: pattern.category || analytics?.category || "General",
          solved: totalSolved,
          avgTimeMinutes: Math.round(avgTimeSeconds / 60),
          confidence,
          hintUsageRate,
          masteryPercentage:
            totalSolved > 0
              ? calculateMasteryFromSignals(confidence, avgTimeSeconds, hintUsageRate)
              : 0,
          problemIds: Array.isArray(analytics?.problemIds) ? analytics.problemIds : [],
          solvedProblemIds: Array.isArray(analytics?.solvedProblemIds)
            ? analytics.solvedProblemIds
            : [],
        };
      });

      setPatterns(transformedPatterns);
    } catch (err) {
      console.error("Error fetching patterns:", err);
      setPatterns([]);
      setPatternRecords([]);
      setError("Failed to load pattern data. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPatterns();
  }, []);

  const openCreateDialog = () => {
    setEditingPatternId(null);
    setPatternForm({ name: "", category: "", description: "" });
    setDialogOpen(true);
  };

  const openEditDialog = (pattern: PatternRecord) => {
    setEditingPatternId(pattern.id);
    setPatternForm({
      name: pattern.name,
      category: pattern.category,
      description: pattern.description || "",
    });
    setDialogOpen(true);
  };

  const openDeleteDialog = (pattern: PatternRecord) => {
    setPendingDeletePattern(pattern);
    setDeleteDialogOpen(true);
  };

  const validatePatternForm = () => {
    if (!patternForm.name.trim()) {
      toast.error("Pattern name is required");
      return false;
    }

    if (!patternForm.category.trim()) {
      toast.error("Pattern category is required");
      return false;
    }

    return true;
  };

  const handleSavePattern = async () => {
    if (!validatePatternForm()) return;

    setSavingPattern(true);
    try {
      const endpoint = editingPatternId ? `/api/patterns/${editingPatternId}` : "/api/patterns";
      const method = editingPatternId ? "PATCH" : "POST";

      const response = await fetch(endpoint, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: patternForm.name.trim(),
          category: patternForm.category.trim(),
          description: patternForm.description.trim() || null,
        }),
      });

      if (!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.error || "Failed to save pattern");
      }

      toast.success(editingPatternId ? "Pattern updated" : "Pattern created");
      setDialogOpen(false);
      await fetchPatterns();
    } catch (err) {
      console.error(err);
      toast.error(err instanceof Error ? err.message : "Could not save pattern");
    } finally {
      setSavingPattern(false);
    }
  };

  const handleDeletePattern = async () => {
    if (!pendingDeletePattern) return;

    setDeletingPattern(true);
    try {
      const response = await fetch(`/api/patterns/${pendingDeletePattern.id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.error || "Failed to delete pattern");
      }

      toast.success("Pattern deleted");
      setDeleteDialogOpen(false);
      setPendingDeletePattern(null);
      await fetchPatterns();
    } catch (err) {
      console.error(err);
      toast.error(err instanceof Error ? err.message : "Could not delete pattern");
    } finally {
      setDeletingPattern(false);
    }
  };

  // Use all patterns without filtering
  const filteredPatterns = patterns;

  // Calculate summary stats
  const summaryStats = useMemo(() => {
    return calculatePatternSummary(patterns);
  }, [patterns]);

  // Get confidence badge styling
  const getConfidenceBadge = (confidence: string) => {
    switch (confidence) {
      case "Strong":
        return "bg-gradient-to-r from-emerald-500 to-green-500 text-white border-0 shadow-lg shadow-emerald-500/30";
      case "Medium":
        return "bg-gradient-to-r from-amber-500 to-yellow-500 text-white border-0 shadow-lg shadow-amber-500/30";
      case "Weak":
        return "bg-gradient-to-r from-rose-500 to-red-500 text-white border-0 shadow-lg shadow-rose-500/30";
      default:
        return "";
    }
  };

  // Row click navigates to the filtered problems list for the pattern.
  const handlePatternClick = (pattern: PatternStat) => {
    router.push(`/problems?pattern=${encodeURIComponent(pattern.name)}`);
  };

  if (loading) {
    return <PatternsPageSkeleton />;
  }

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Page Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between"
      >
        <div className="relative">
          <h1 className="relative text-4xl font-bold">
            <span className="text-gradient-purple-pink">
              Algorithm Patterns
            </span>
          </h1>
          <p className="text-muted-foreground mt-2 text-base">
            <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
            Master the building blocks of coding interviews
          </p>
        </div>

        <Button
          className="bg-gradient-to-r from-electric-purple to-bright-pink hover:shadow-glow transition-all duration-300 border-0"
          onClick={openCreateDialog}
        >
          <Plus className="mr-1 h-4 w-4" />
          New Pattern
        </Button>
      </motion.div>

      {/* Summary Cards */}
      <motion.div
        variants={staggerContainer}
        initial="hidden"
        animate="visible"
        className="grid grid-cols-1 md:grid-cols-3 gap-4"
      >
        <motion.div variants={staggerItem}>
          <Card className="bg-gradient-to-br from-pink-100 via-purple-50 to-fuchsia-100 dark:from-pink-950/40 dark:via-purple-900/20 dark:to-fuchsia-950/40 border-2 border-pink-200/60 dark:border-pink-800/60 overflow-hidden relative">
            <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-pink-400/30 to-purple-400/30 rounded-full blur-2xl" />
            <CardContent className="pt-6 relative">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">Total Patterns</p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-pink-600 to-purple-600 bg-clip-text text-transparent">
                    {summaryStats.total}
                  </p>
                </div>
                <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-pink-500 to-purple-500 flex items-center justify-center shadow-lg shadow-pink-500/30">
                  <Brain className="h-6 w-6 text-white" />
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        <motion.div variants={staggerItem}>
          <Card className="bg-gradient-to-br from-blue-100 via-indigo-50 to-violet-100 dark:from-blue-950/40 dark:via-indigo-900/20 dark:to-violet-950/40 border-2 border-blue-200/60 dark:border-blue-800/60 overflow-hidden relative">
            <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-blue-400/30 to-indigo-400/30 rounded-full blur-2xl" />
            <CardContent className="pt-6 relative">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">Problems Solved</p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
                    {summaryStats.totalSolved}
                  </p>
                </div>
                <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-500 flex items-center justify-center shadow-lg shadow-blue-500/30">
                  <BarChart3 className="h-6 w-6 text-white" />
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        <motion.div variants={staggerItem}>
          <Card className="bg-gradient-to-br from-emerald-100 via-green-50 to-teal-100 dark:from-emerald-950/40 dark:via-green-900/20 dark:to-teal-950/40 border-2 border-emerald-200/60 dark:border-emerald-800/60 overflow-hidden relative">
            <div className="absolute -top-8 -right-8 w-24 h-24 bg-gradient-to-br from-emerald-400/30 to-teal-400/30 rounded-full blur-2xl" />
            <CardContent className="pt-6 relative">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">Avg Mastery</p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-emerald-600 to-teal-600 bg-clip-text text-transparent">
                    {summaryStats.avgMastery}%
                  </p>
                </div>
                <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-emerald-500 to-teal-500 flex items-center justify-center shadow-lg shadow-emerald-500/30">
                  <Sparkles className="h-6 w-6 text-white" />
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>

      </motion.div>

      {/* Patterns List */}
      <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
        <Card className="bg-gradient-to-br from-white to-purple-50/30 dark:from-gray-900 dark:to-purple-950/10 border-2 border-purple-200/50 dark:border-purple-800/30 shadow-xl shadow-purple-500/10 overflow-hidden">
          <div className="overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow className="bg-gradient-to-r from-purple-50/50 to-pink-50/50 dark:from-purple-950/30 dark:to-pink-950/30 hover:bg-purple-50/70 dark:hover:bg-purple-950/40">
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Pattern
                  </TableHead>
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Category
                  </TableHead>
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Solved
                  </TableHead>
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Avg Time
                  </TableHead>
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Hints
                  </TableHead>
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Confidence
                  </TableHead>
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Mastery
                  </TableHead>
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 text-right px-6">
                    Actions
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                <AnimatePresence mode="popLayout">
                  {filteredPatterns.map((pattern, index) => (
                    <motion.tr
                      key={pattern.id}
                      custom={index}
                      variants={tableRowVariants}
                      initial="hidden"
                      animate="visible"
                      whileHover="hover"
                      exit={{ opacity: 0, x: -20 }}
                      className="group border-b border-purple-100 dark:border-purple-900/30 transition-colors cursor-pointer hover:bg-purple-50/50 dark:hover:bg-purple-950/20"
                      onClick={() => handlePatternClick(pattern)}
                    >
                      <TableCell className="font-medium px-6">
                        <div className="flex items-center gap-2">
                          <span className="text-foreground group-hover:text-purple-700 dark:group-hover:text-purple-300 transition-colors">
                            {pattern.name}
                          </span>
                          <ChevronRight className="h-4 w-4 text-muted-foreground opacity-0 transition-opacity group-hover:opacity-100" />
                        </div>
                      </TableCell>
                      <TableCell className="px-6">
                        <span className="text-sm text-muted-foreground">{pattern.category || "General"}</span>
                      </TableCell>
                      <TableCell className="px-6">
                        <span className="font-semibold text-foreground">{pattern.solved}</span>
                      </TableCell>
                      <TableCell className="px-6">
                        <span className="text-sm text-muted-foreground">{pattern.avgTimeMinutes}m</span>
                      </TableCell>
                      <TableCell className="px-6">
                        <span className="text-sm text-muted-foreground">
                          {Math.round(pattern.hintUsageRate * 100)}%
                        </span>
                      </TableCell>
                      <TableCell className="px-6">
                        <Badge className={`${getConfidenceBadge(pattern.confidence)} text-xs font-medium`}>
                          {pattern.confidence}
                        </Badge>
                      </TableCell>
                      <TableCell className="px-6">
                        <div className="min-w-[130px] space-y-1">
                          <div className="flex items-center justify-between text-xs text-muted-foreground">
                            <span>Level</span>
                            <span className="font-medium text-foreground">{pattern.masteryPercentage}%</span>
                          </div>
                          <div className="h-2 rounded-full bg-gray-200 dark:bg-gray-800 overflow-hidden">
                            <div
                              className="h-full rounded-full bg-gradient-to-r from-purple-500 to-pink-500"
                              style={{ width: `${pattern.masteryPercentage}%` }}
                            />
                          </div>
                        </div>
                      </TableCell>
                      <TableCell className="text-right px-6">
                        <div
                          className="flex items-center justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity"
                          onClick={(event) => event.stopPropagation()}
                        >
                          <motion.button
                            whileHover={{ scale: 1.1 }}
                            whileTap={{ scale: 0.95 }}
                            onClick={() => {
                              const record = patternRecords.find((item) => item.id === pattern.id);
                              if (record) openEditDialog(record);
                            }}
                            className="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-blue-100 text-blue-600 transition-colors hover:bg-blue-200 dark:bg-blue-900/30 dark:text-blue-400 dark:hover:bg-blue-900/50"
                            title="Edit Pattern"
                            aria-label="Edit Pattern"
                          >
                            <Edit className="h-4 w-4" />
                          </motion.button>

                          <motion.button
                            whileHover={{ scale: 1.1 }}
                            whileTap={{ scale: 0.95 }}
                            onClick={() => {
                              const record = patternRecords.find((item) => item.id === pattern.id);
                              if (record) openDeleteDialog(record);
                            }}
                            className="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-red-100 text-red-600 transition-colors hover:bg-red-200 dark:bg-red-900/30 dark:text-red-400 dark:hover:bg-red-900/50"
                            title="Delete Pattern"
                            aria-label="Delete Pattern"
                          >
                            <Trash2 className="h-4 w-4" />
                          </motion.button>
                        </div>
                      </TableCell>
                    </motion.tr>
                  ))}
                </AnimatePresence>
              </TableBody>
            </Table>
          </div>
        </Card>
      </motion.div>

      {/* Empty state */}
      {filteredPatterns.length === 0 && !loading && (
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center py-16"
        >
          <div className="flex h-20 w-20 items-center justify-center rounded-full bg-gradient-to-br from-pink-100 to-purple-100 dark:from-pink-950/30 dark:to-purple-950/30 mx-auto mb-4">
            <Brain className="h-10 w-10 text-pink-500" />
          </div>
          <h3 className="text-lg font-semibold text-foreground mb-2">
            {error ? "Failed to load patterns" : "No patterns found"}
          </h3>
          <p className="text-muted-foreground">
            {error ? error : "Start solving problems to see pattern analytics here."}
          </p>
        </motion.div>
      )}

      {/* Footer Info - similar to dashboard */}
      <div className="text-center py-8">
        <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-muted/50 border border-border">
          <span className="text-sm text-muted-foreground">
            💡 Tip: Focus on weak patterns and practice consistently to improve your mastery!
          </span>
        </div>
      </div>

      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent className="w-full max-w-[650px] min-w-[320px] sm:min-w-[500px] lg:min-w-[600px] max-h-[90vh] overflow-hidden bg-white/90 backdrop-blur-md border border-gray-200/60 rounded-2xl shadow-xl mx-4">
          <DialogHeader className="pb-6 border-b border-gray-200">
            <DialogTitle className="text-2xl font-semibold text-gray-900">
              {editingPatternId ? "Edit Pattern" : "Create Pattern"}
            </DialogTitle>
            <DialogDescription className="text-sm text-gray-500">
              Define the pattern name, category, and optional description.
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-8 pt-8 overflow-y-auto overflow-x-hidden max-h-[75vh] pr-2 w-full">
            <div className="grid gap-2">
              <Label htmlFor="pattern-name">Pattern Name</Label>
              <Input
                id="pattern-name"
                value={patternForm.name}
                onChange={(e) =>
                  setPatternForm((prev) => ({ ...prev, name: e.target.value }))
                }
                placeholder="Example: Sliding Window"
                className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
              />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="pattern-category">Category</Label>
              <Input
                id="pattern-category"
                value={patternForm.category}
                onChange={(e) =>
                  setPatternForm((prev) => ({ ...prev, category: e.target.value }))
                }
                placeholder="Example: Array and String"
                className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
              />
            </div>

            <div className="grid gap-2">
              <Label htmlFor="pattern-description">Description</Label>
              <Textarea
                id="pattern-description"
                value={patternForm.description}
                onChange={(e) =>
                  setPatternForm((prev) => ({ ...prev, description: e.target.value }))
                }
                placeholder="Optional notes"
                className="min-h-[100px] resize-none !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
              />
            </div>
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={() => setDialogOpen(false)}>
              <X className="h-4 w-4 mr-2" />
              Cancel
            </Button>
            <Button
              onClick={handleSavePattern}
              disabled={savingPattern}
              className="bg-gradient-purple-pink hover:shadow-md transition-all duration-300 disabled:opacity-50"
            >
              {savingPattern ? (
                <div className="h-4 w-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2" />
              ) : (
                <Check className="h-4 w-4 mr-2" />
              )}
              {savingPattern
                ? "Saving..."
                : editingPatternId
                  ? "Update Pattern"
                  : "Create Pattern"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
        <DialogContent className="w-full max-w-[500px] bg-white/90 backdrop-blur-md border border-gray-200/60 rounded-2xl shadow-xl">
          <DialogHeader className="pb-4 border-b border-gray-200">
            <DialogTitle className="text-2xl font-semibold text-gray-900">Delete Pattern</DialogTitle>
            <DialogDescription className="text-sm text-gray-500">
              This action cannot be undone.
            </DialogDescription>
          </DialogHeader>

          <div className="py-4 text-sm text-muted-foreground">
            Delete pattern <span className="font-semibold text-foreground">{pendingDeletePattern?.name}</span>?
          </div>

          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => {
                setDeleteDialogOpen(false);
                setPendingDeletePattern(null);
              }}
            >
              <X className="h-4 w-4 mr-2" />
              Cancel
            </Button>
            <Button
              onClick={handleDeletePattern}
              disabled={deletingPattern}
              className="bg-red-600 hover:bg-red-700 text-white transition-all duration-300 disabled:opacity-50"
            >
              {deletingPattern ? (
                <div className="h-4 w-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2" />
              ) : (
                <Trash2 className="h-4 w-4 mr-2" />
              )}
              {deletingPattern ? "Deleting..." : "Delete Pattern"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}

// Loading Skeleton
function PatternsPageSkeleton() {
  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Header skeleton */}
      <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
        <div className="space-y-2">
          <Skeleton className="h-10 w-64" />
          <Skeleton className="h-4 w-72" />
        </div>
        <Skeleton className="h-10 w-32 rounded-md" />
      </div>

      {/* Summary cards skeleton */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {[1, 2, 3].map((i) => (
          <Card key={i} className="border-2">
            <CardContent className="pt-6">
              <div className="flex items-center justify-between">
                <div className="space-y-2">
                  <Skeleton className="h-4 w-20" />
                  <Skeleton className="h-8 w-16" />
                </div>
                <Skeleton className="h-12 w-12 rounded-xl" />
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Table list skeleton */}
      <Card className="border-2 overflow-hidden">
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                {Array.from({ length: 8 }).map((_, i) => (
                  <TableHead key={i} className="px-6 py-4">
                    <Skeleton className="h-4 w-20" />
                  </TableHead>
                ))}
              </TableRow>
            </TableHeader>
            <TableBody>
              {Array.from({ length: 7 }).map((_, row) => (
                <TableRow key={row}>
                  <TableCell className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <Skeleton className="h-4 w-36" />
                      <Skeleton className="h-4 w-4" />
                    </div>
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <Skeleton className="h-4 w-24" />
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <Skeleton className="h-4 w-8" />
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <Skeleton className="h-4 w-12" />
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <Skeleton className="h-4 w-10" />
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <Skeleton className="h-6 w-20 rounded-full" />
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <div className="space-y-2 min-w-[130px]">
                      <div className="flex items-center justify-between">
                        <Skeleton className="h-3 w-10" />
                        <Skeleton className="h-3 w-8" />
                      </div>
                      <Skeleton className="h-2 w-full rounded-full" />
                    </div>
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <div className="flex items-center justify-end gap-2">
                      <Skeleton className="h-8 w-8 rounded-lg" />
                      <Skeleton className="h-8 w-8 rounded-lg" />
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </Card>
    </div>
  );
}
