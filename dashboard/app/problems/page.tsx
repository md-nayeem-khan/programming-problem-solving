"use client";

import { useEffect, useState, useMemo, useCallback } from "react";
import { motion, AnimatePresence } from "framer-motion";
import {
  Filter,
  Plus,
  ChevronLeft,
  ChevronRight,
  ExternalLink,
  Clock,
  CheckCircle2,
  XCircle,
  AlertCircle,
  BookOpen,
  Sparkles,
  RefreshCw,
  Edit,
  Trash2,
} from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Skeleton } from "@/components/ui/skeleton";
import { AddProblemForm } from "@/components/forms/AddProblemForm";
import { EditProblemForm } from "@/components/forms/EditProblemForm";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  staggerContainer,
  staggerItem,
  fadeInUp,
  scaleIn,
} from "@/lib/animations";
import { useRouter } from "next/navigation";
import { toast } from "sonner";

// Types
interface Problem {
  id: number;
  platform: string;
  problemId: string;
  title: string;
  difficulty: "easy" | "medium" | "hard" | "unrated";
  url?: string;
  notes?: string;
  source?: string;
  company?: string;
  tags: string[];
  patterns: { id: number; name: string; category: string }[];
  submissions: {
    timeSpentSeconds: number;
    status: string;
    submittedAt: string;
    attemptType?: string;
    wasHintUsed?: boolean;
  }[];
  attemptCount: number;
  lastAttempt: string | null;
  createdAt: string;
  updatedAt: string;
}

interface Filters {
  search: string;
  difficulty: string;
  pattern: string;
  company: string;
  status: string;
}

// Constants
const ITEMS_PER_PAGE = 10;
const TITLE_CLIP_LENGTH = 50;

const COMPANIES = [
  "Amazon",
  "Google",
  "Meta",
  "Apple",
  "Netflix",
  "Microsoft",
];

const PATTERNS = [
  "Two Pointers",
  "Sliding Window",
  "Binary Search",
  "Stack",
  "Linked List",
  "Trees",
  "Graphs",
  "Heap / Priority Queue",
  "Backtracking",
  "Dynamic Programming",
  "Greedy",
  "Intervals",
  "Math & Geometry",
  "Bit Manipulation",
  "Tries",
];

// Animation Variants
const tableRowVariants = {
  hidden: { opacity: 0, x: -20 },
  visible: (i: number) => ({
    opacity: 1,
    x: 0,
    transition: {
      delay: i * 0.05,
      duration: 0.3,
      ease: [0.25, 0.46, 0.45, 0.94] as const,
    },
  }),
  hover: {
    backgroundColor: "rgba(147, 51, 234, 0.05)",
    transition: { duration: 0.2 },
  },
};

const clipTitle = (title: string) => {
  if (title.length <= TITLE_CLIP_LENGTH) return title;
  return `${title.slice(0, TITLE_CLIP_LENGTH)}...`;
};

const filterCardVariants = {
  hidden: { opacity: 0, y: -20, scale: 0.95 },
  visible: {
    opacity: 1,
    y: 0,
    scale: 1,
    transition: { duration: 0.4, ease: [0.25, 0.46, 0.45, 0.94] as const },
  },
};

// Difficulty Badge Component
function DifficultyBadge({ difficulty }: { difficulty: string }) {
  const config = {
    easy: {
      bg: "bg-emerald-100 dark:bg-emerald-900/30",
      text: "text-emerald-700 dark:text-emerald-300",
      border: "border-emerald-300 dark:border-emerald-700",
      glow: "shadow-emerald-500/20",
    },
    medium: {
      bg: "bg-amber-100 dark:bg-amber-900/30",
      text: "text-amber-700 dark:text-amber-300",
      border: "border-amber-300 dark:border-amber-700",
      glow: "shadow-amber-500/20",
    },
    hard: {
      bg: "bg-red-100 dark:bg-red-900/30",
      text: "text-red-700 dark:text-red-300",
      border: "border-red-300 dark:border-red-700",
      glow: "shadow-red-500/20",
    },
    unrated: {
      bg: "bg-gray-100 dark:bg-gray-800/50",
      text: "text-gray-600 dark:text-gray-400",
      border: "border-gray-300 dark:border-gray-700",
      glow: "shadow-gray-500/10",
    },
  };

  const style = config[difficulty as keyof typeof config] || config.unrated;

  return (
    <motion.span
      whileHover={{ scale: 1.05 }}
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold border shadow-sm ${style.bg} ${style.text} ${style.border} ${style.glow}`}
    >
      {difficulty.charAt(0).toUpperCase() + difficulty.slice(1)}
    </motion.span>
  );
}

// Status Badge Component
function StatusBadge({ status, attemptCount }: { status: string; attemptCount: number }) {
  if (attemptCount === 0) {
    return (
      <Badge
        variant="outline"
        className="bg-slate-100 dark:bg-slate-800/50 text-slate-600 dark:text-slate-400 border-slate-300 dark:border-slate-700"
      >
        <XCircle className="h-3 w-3 mr-1" />
        Unsolved
      </Badge>
    );
  }

  const isSolved = status === "solved";
  return (
    <Badge
      variant="outline"
      className={
        isSolved
          ? "bg-emerald-100 dark:bg-emerald-900/30 text-emerald-700 dark:text-emerald-300 border-emerald-300 dark:border-emerald-700"
          : "bg-amber-100 dark:bg-amber-900/30 text-amber-700 dark:text-amber-300 border-amber-300 dark:border-amber-700"
      }
    >
      {isSolved ? (
        <CheckCircle2 className="h-3 w-3 mr-1" />
      ) : (
        <Clock className="h-3 w-3 mr-1" />
      )}
      {isSolved ? "Solved" : "In Progress"}
    </Badge>
  );
}

// Empty State Component
function EmptyState({ hasFilters }: { hasFilters: boolean }) {
  return (
    <motion.div
      variants={scaleIn}
      initial="hidden"
      animate="visible"
      className="flex flex-col items-center justify-center py-16"
    >
      <motion.div
        animate={{ y: [0, -10, 0] }}
        transition={{ duration: 2, repeat: Infinity, ease: "easeInOut" }}
        className="relative"
      >
        <div className="absolute inset-0 bg-gradient-to-r from-purple-500/20 to-pink-500/20 rounded-full blur-xl" />
        <div className="relative flex h-20 w-20 items-center justify-center rounded-full bg-gradient-to-br from-purple-100 to-pink-100 dark:from-purple-900/30 dark:to-pink-900/30">
          <BookOpen className="h-10 w-10 text-purple-500" />
        </div>
      </motion.div>
      <h3 className="mt-6 text-xl font-semibold text-foreground">
        {hasFilters ? "No problems found" : "No problems yet"}
      </h3>
      <p className="mt-2 text-sm text-muted-foreground text-center max-w-md">
        {hasFilters
          ? "Try adjusting your filters to find what you're looking for."
          : "Start your interview prep journey by adding your first problem!"}
      </p>
      {!hasFilters && (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
          className="mt-6"
        >
          <AddProblemForm />
        </motion.div>
      )}
    </motion.div>
  );
}

// Loading Skeleton
function TableSkeleton() {
  return (
    <div className="space-y-3">
      {[...Array(5)].map((_, i) => (
        <div key={i} className="flex items-center gap-4 p-4">
          <Skeleton className="h-6 w-[200px]" />
          <Skeleton className="h-6 w-[80px]" />
          <Skeleton className="h-6 w-[120px]" />
          <Skeleton className="h-6 w-[100px]" />
          <Skeleton className="h-6 w-[80px]" />
          <Skeleton className="h-6 w-[100px]" />
          <Skeleton className="h-6 w-[60px]" />
        </div>
      ))}
    </div>
  );
}

// Main Page Component
export default function ProblemsPage() {
  const router = useRouter();
  const [problems, setProblems] = useState<Problem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingProblem, setEditingProblem] = useState<Problem | null>(null);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [deletingProblem, setDeletingProblem] = useState<Problem | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [filters, setFilters] = useState<Filters>({
    search: "",
    difficulty: "",
    pattern: "",
    company: "",
    status: "",
  });

  // Fetch problems from API
  const fetchProblems = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      const params = new URLSearchParams();
      if (filters.search) params.append("search", filters.search);
      if (filters.difficulty) params.append("difficulty", filters.difficulty);
      if (filters.pattern) params.append("pattern", filters.pattern);
      if (filters.company) params.append("company", filters.company);

      const response = await fetch(`/api/problems?${params.toString()}`);
      if (!response.ok) throw new Error("Failed to fetch problems");

      const data = await response.json();
      setProblems(data.problems || []);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load problems");
    } finally {
      setLoading(false);
    }
  }, [filters.search, filters.difficulty, filters.pattern, filters.company]);

  useEffect(() => {
    const debounceTimer = setTimeout(fetchProblems, 300);
    return () => clearTimeout(debounceTimer);
  }, [fetchProblems]);

  // Filter problems by status (client-side since API doesn't support it directly)
  const filteredProblems = useMemo(() => {
    if (!filters.status) return problems;
    return problems.filter((problem) => {
      const isSolved =
        problem.attemptCount > 0 &&
        problem.submissions.some((s) => s.status === "solved");
      if (filters.status === "solved") return isSolved;
      if (filters.status === "unsolved") return !isSolved;
      return true;
    });
  }, [problems, filters.status]);

  // Pagination
  const totalPages = Math.ceil(filteredProblems.length / ITEMS_PER_PAGE);
  const paginatedProblems = filteredProblems.slice(
    (currentPage - 1) * ITEMS_PER_PAGE,
    currentPage * ITEMS_PER_PAGE
  );

  // Reset to page 1 when filters change
  useEffect(() => {
    setCurrentPage(1);
  }, [filters]);

  // Update filter handler
  const updateFilter = (key: keyof Filters, value: string) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
  };

  // Clear all filters
  const clearFilters = () => {
    setFilters({
      search: "",
      difficulty: "",
      pattern: "",
      company: "",
      status: "",
    });
  };

  const hasActiveFilters = Object.values(filters).some((v) => v !== "");

  // Format date helper
  const formatDate = (dateString: string | null) => {
    if (!dateString) return "Never";
    const date = new Date(dateString);
    const now = new Date();
    const diffDays = Math.floor(
      (now.getTime() - date.getTime()) / (1000 * 60 * 60 * 24)
    );

    if (diffDays === 0) return "Today";
    if (diffDays === 1) return "Yesterday";
    if (diffDays < 7) return `${diffDays} days ago`;
    return date.toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
      year: date.getFullYear() !== now.getFullYear() ? "numeric" : undefined,
    });
  };

  const handleDeleteProblem = async () => {
    if (!deletingProblem?.id) return;

    setIsDeleting(true);
    try {
      const response = await fetch(`/api/problems/${deletingProblem.id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error("Failed to delete problem");
      }

      toast.success("Problem deleted", {
        description: `${deletingProblem.title} has been removed`,
      });

      setIsDeleteModalOpen(false);
      setDeletingProblem(null);
      await fetchProblems();
    } catch {
      toast.error("Failed to delete problem", {
        description: "Please try again",
      });
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      <Dialog
        open={isDeleteModalOpen}
        onOpenChange={(open) => {
          setIsDeleteModalOpen(open);
          if (!open && !isDeleting) {
            setDeletingProblem(null);
          }
        }}
      >
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Delete Problem</DialogTitle>
            <DialogDescription>
              {`Are you sure you want to delete "${deletingProblem?.title || "this problem"}"? This action cannot be undone.`}
            </DialogDescription>
          </DialogHeader>
          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                setIsDeleteModalOpen(false);
                setDeletingProblem(null);
              }}
              disabled={isDeleting}
            >
              Cancel
            </Button>
            <Button
              type="button"
              variant="destructive"
              onClick={handleDeleteProblem}
              disabled={isDeleting}
            >
              {isDeleting ? "Deleting..." : "Delete"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <EditProblemForm
        open={isEditModalOpen}
        onOpenChange={(open) => {
          setIsEditModalOpen(open);
          if (!open) {
            setEditingProblem(null);
          }
        }}
        problem={editingProblem}
        onSuccess={fetchProblems}
      />

      {/* Page Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <div className="relative flex items-center justify-between">
          <div>
            <h1 className="relative text-4xl font-bold">
              <span className="text-gradient-purple-pink">
                Problems
              </span>
            </h1>
            <p className="text-muted-foreground mt-2 text-base">
              <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
              Track and manage your interview preparation problems
            </p>
          </div>
          
          {/* Add Problem Button */}
          <motion.div
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5, delay: 0.2 }}
          >
            <AddProblemForm />
          </motion.div>
        </div>
      </motion.div>

      {/* Filters Card */}
      <motion.div
        variants={filterCardVariants}
        initial="hidden"
        animate="visible"
      >
        <Card className="bg-gradient-to-br from-purple-50/50 via-white to-pink-50/50 dark:from-purple-950/20 dark:via-gray-900/50 dark:to-pink-950/20 border-2 border-purple-200/50 dark:border-purple-800/30 shadow-xl shadow-purple-500/10">
          <CardHeader className="pb-4">
            <CardTitle className="flex items-center gap-2 text-lg">
              <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-purple-500 to-pink-500 shadow-lg shadow-purple-500/30">
                <Filter className="h-4 w-4 text-white" />
              </div>
              <span className="bg-gradient-to-r from-purple-700 to-pink-600 bg-clip-text text-transparent">
                Filters
              </span>
              {hasActiveFilters && (
                <Badge
                  variant="secondary"
                  className="ml-2 bg-purple-100 dark:bg-purple-900/30 text-purple-700 dark:text-purple-300"
                >
                  Active
                </Badge>
              )}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex flex-col md:flex-row gap-4 w-full">
              {/* Difficulty Select */}
              <div className="flex-1">
                <Select
                  value={filters.difficulty}
                  onValueChange={(value) => updateFilter("difficulty", value)}
                >
                  <SelectTrigger className="w-full bg-white/70 dark:bg-gray-900/50 border-purple-200 dark:border-purple-800/50">
                    <SelectValue placeholder="Difficulty" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Difficulties</SelectItem>
                    <SelectItem value="easy">
                      <span className="flex items-center gap-2">
                        <span className="h-2 w-2 rounded-full bg-emerald-500" />
                        Easy
                      </span>
                    </SelectItem>
                    <SelectItem value="medium">
                      <span className="flex items-center gap-2">
                        <span className="h-2 w-2 rounded-full bg-amber-500" />
                        Medium
                      </span>
                    </SelectItem>
                    <SelectItem value="hard">
                      <span className="flex items-center gap-2">
                        <span className="h-2 w-2 rounded-full bg-red-500" />
                        Hard
                      </span>
                    </SelectItem>
                  </SelectContent>
                </Select>
              </div>

              {/* Pattern Select */}
              <div className="flex-1">
                <Select
                  value={filters.pattern}
                  onValueChange={(value) => updateFilter("pattern", value)}
                >
                  <SelectTrigger className="w-full bg-white/70 dark:bg-gray-900/50 border-purple-200 dark:border-purple-800/50">
                    <SelectValue placeholder="Pattern" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Patterns</SelectItem>
                    {PATTERNS.map((pattern) => (
                      <SelectItem key={pattern} value={pattern}>
                        {pattern}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              {/* Company Select */}
              <div className="flex-1">
                <Select
                  value={filters.company}
                  onValueChange={(value) => updateFilter("company", value)}
                >
                  <SelectTrigger className="w-full bg-white/70 dark:bg-gray-900/50 border-purple-200 dark:border-purple-800/50">
                    <SelectValue placeholder="Company" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Companies</SelectItem>
                    {COMPANIES.map((company) => (
                      <SelectItem key={company} value={company}>
                        {company}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              {/* Status Select */}
              <div className="flex-1">
                <Select
                  value={filters.status}
                  onValueChange={(value) => updateFilter("status", value)}
                >
                  <SelectTrigger className="w-full bg-white/70 dark:bg-gray-900/50 border-purple-200 dark:border-purple-800/50">
                    <SelectValue placeholder="Status" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Status</SelectItem>
                    <SelectItem value="solved">
                      <span className="flex items-center gap-2">
                        <CheckCircle2 className="h-3 w-3 text-emerald-500" />
                        Solved
                      </span>
                    </SelectItem>
                    <SelectItem value="unsolved">
                      <span className="flex items-center gap-2">
                        <XCircle className="h-3 w-3 text-slate-500" />
                        Unsolved
                      </span>
                    </SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>

            {/* Clear Filter Button */}
            <div className="flex flex-wrap items-center gap-4 mt-4">
              {hasActiveFilters && (
                <motion.div
                  initial={{ opacity: 0, scale: 0.9 }}
                  animate={{ opacity: 1, scale: 1 }}
                  exit={{ opacity: 0, scale: 0.9 }}
                >
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={clearFilters}
                    className="text-purple-600 hover:text-purple-700 hover:bg-purple-100 dark:text-purple-400 dark:hover:bg-purple-900/30"
                  >
                    <RefreshCw className="h-3 w-3 mr-2" />
                    Clear Filters
                  </Button>
                </motion.div>
              )}

              <div className="ml-auto text-sm text-muted-foreground">
                {loading ? (
                  <Skeleton className="h-4 w-24" />
                ) : (
                  <span>
                    Showing{" "}
                    <span className="font-semibold text-foreground">
                      {filteredProblems.length}
                    </span>{" "}
                    {filteredProblems.length === 1 ? "problem" : "problems"}
                  </span>
                )}
              </div>
            </div>
          </CardContent>
        </Card>
      </motion.div>

      {/* Problems Table */}
      <motion.div
        variants={fadeInUp}
        initial="hidden"
        animate="visible"
        transition={{ delay: 0.2 }}
      >
        <Card className="bg-gradient-to-br from-white to-purple-50/30 dark:from-gray-900 dark:to-purple-950/10 border-2 border-purple-200/50 dark:border-purple-800/30 shadow-xl shadow-purple-500/10 overflow-hidden">
          {loading ? (
            <div className="p-6">
              <TableSkeleton />
            </div>
          ) : error ? (
            <div className="flex flex-col items-center justify-center py-16">
              <div className="flex h-16 w-16 items-center justify-center rounded-full bg-red-100 dark:bg-red-900/30">
                <AlertCircle className="h-8 w-8 text-red-500" />
              </div>
              <h3 className="mt-4 text-lg font-semibold text-foreground">
                Error Loading Problems
              </h3>
              <p className="mt-2 text-sm text-muted-foreground">{error}</p>
              <Button
                variant="outline"
                onClick={fetchProblems}
                className="mt-4"
              >
                <RefreshCw className="h-4 w-4 mr-2" />
                Try Again
              </Button>
            </div>
          ) : filteredProblems.length === 0 ? (
            <EmptyState hasFilters={hasActiveFilters} />
          ) : (
            <>
              <div className="overflow-x-auto">
                <Table>
                  <TableHeader>
                    <TableRow className="bg-gradient-to-r from-purple-50/50 to-pink-50/50 dark:from-purple-950/30 dark:to-pink-950/30 hover:bg-purple-50/70 dark:hover:bg-purple-950/40">
                      <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                        Title
                      </TableHead>
                      <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                        Difficulty
                      </TableHead>
                      <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                        Pattern
                      </TableHead>
                      <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                        Company
                      </TableHead>
                      <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                        Status
                      </TableHead>
                      <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                        Last Attempt
                      </TableHead>
                      <TableHead className="font-semibold text-purple-900 dark:text-purple-100 text-right px-6">
                        Actions
                      </TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    <AnimatePresence mode="popLayout">
                      {paginatedProblems.map((problem, index) => (
                        <motion.tr
                          key={problem.id}
                          custom={index}
                          variants={tableRowVariants}
                          initial="hidden"
                          animate="visible"
                          whileHover="hover"
                          exit={{ opacity: 0, x: -20 }}
                          className="group border-b border-purple-100 dark:border-purple-900/30 transition-colors cursor-pointer hover:bg-purple-50/50 dark:hover:bg-purple-950/20"
                          onClick={() => router.push(`/problems/${problem.id}`)}
                        >
                          <TableCell className="font-medium px-6">
                            <div className="flex flex-col gap-1">
                              <span
                                className="text-foreground group-hover:text-purple-700 dark:group-hover:text-purple-300 transition-colors"
                                title={problem.title}
                              >
                                {clipTitle(problem.title)}
                              </span>
                              <span className="text-xs text-muted-foreground">
                                {problem.platform} • {problem.problemId}
                              </span>
                            </div>
                          </TableCell>
                          <TableCell className="px-6">
                            <DifficultyBadge difficulty={problem.difficulty} />
                          </TableCell>
                          <TableCell className="px-6">
                            {problem.patterns.length > 0 ? (
                              <div className="flex flex-wrap gap-1">
                                {problem.patterns.slice(0, 2).map((pattern) => (
                                  <Badge
                                    key={pattern.id}
                                    variant="outline"
                                    className="text-xs bg-blue-50 dark:bg-blue-900/20 text-blue-700 dark:text-blue-300 border-blue-200 dark:border-blue-800"
                                  >
                                    {pattern.name}
                                  </Badge>
                                ))}
                                {problem.patterns.length > 2 && (
                                  <Badge
                                    variant="secondary"
                                    className="text-xs"
                                  >
                                    +{problem.patterns.length - 2}
                                  </Badge>
                                )}
                              </div>
                            ) : (
                              <span className="text-muted-foreground text-sm">
                                —
                              </span>
                            )}
                          </TableCell>
                          <TableCell className="px-6">
                            {problem.company ? (
                              <Badge
                                variant="outline"
                                className="bg-orange-50 dark:bg-orange-900/20 text-orange-700 dark:text-orange-300 border-orange-200 dark:border-orange-800"
                              >
                                {problem.company}
                              </Badge>
                            ) : (
                              <span className="text-muted-foreground text-sm">
                                —
                              </span>
                            )}
                          </TableCell>
                          <TableCell className="px-6">
                            <StatusBadge
                              status={problem.submissions[0]?.status || "unsolved"}
                              attemptCount={problem.attemptCount}
                            />
                          </TableCell>
                          <TableCell className="px-6">
                            <span className="text-sm text-muted-foreground">
                              {formatDate(problem.lastAttempt)}
                            </span>
                          </TableCell>
                          <TableCell className="text-right px-6">
                            <div 
                              className="flex items-center justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity"
                              onClick={(e) => e.stopPropagation()}
                            >
                              {problem.url && (
                                <motion.a
                                  whileHover={{ scale: 1.1 }}
                                  whileTap={{ scale: 0.95 }}
                                  href={problem.url}
                                  target="_blank"
                                  rel="noopener noreferrer"
                                  onClick={(e) => e.stopPropagation()}
                                  className="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-purple-100 dark:bg-purple-900/30 text-purple-600 dark:text-purple-400 hover:bg-purple-200 dark:hover:bg-purple-900/50 transition-colors"
                                >
                                  <ExternalLink className="h-4 w-4" />
                                </motion.a>
                              )}
                              
                              {/* Edit Button */}
                              <motion.button
                                whileHover={{ scale: 1.1 }}
                                whileTap={{ scale: 0.95 }}
                                onClick={(e) => {
                                  e.stopPropagation();
                                  setEditingProblem(problem);
                                  setIsEditModalOpen(true);
                                }}
                                className="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-blue-100 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 hover:bg-blue-200 dark:hover:bg-blue-900/50 transition-colors"
                                title="Edit Problem"
                              >
                                <Edit className="h-4 w-4" />
                              </motion.button>

                              {/* Delete Button */}
                              <motion.button
                                whileHover={{ scale: 1.1 }}
                                whileTap={{ scale: 0.95 }}
                                onClick={(e) => {
                                  e.stopPropagation();
                                  setDeletingProblem(problem);
                                  setIsDeleteModalOpen(true);
                                }}
                                className="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-red-100 dark:bg-red-900/30 text-red-600 dark:text-red-400 hover:bg-red-200 dark:hover:bg-red-900/50 transition-colors"
                                title="Delete Problem"
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

              {/* Pagination */}
              {totalPages > 1 && (
                <motion.div
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  transition={{ delay: 0.4 }}
                  className="flex items-center justify-between px-6 py-4 border-t border-purple-100 dark:border-purple-900/30 bg-gradient-to-r from-purple-50/30 to-pink-50/30 dark:from-purple-950/20 dark:to-pink-950/20"
                >
                  <div className="text-sm text-muted-foreground">
                    Page{" "}
                    <span className="font-semibold text-foreground">
                      {currentPage}
                    </span>{" "}
                    of{" "}
                    <span className="font-semibold text-foreground">
                      {totalPages}
                    </span>
                  </div>
                  <div className="flex items-center gap-2">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() =>
                        setCurrentPage((prev) => Math.max(1, prev - 1))
                      }
                      disabled={currentPage === 1}
                      className="border-purple-200 dark:border-purple-800/50 hover:bg-purple-100 dark:hover:bg-purple-900/30"
                    >
                      <ChevronLeft className="h-4 w-4 mr-1" />
                      Previous
                    </Button>
                    <div className="flex items-center gap-1">
                      {[...Array(Math.min(5, totalPages))].map((_, i) => {
                        let pageNum: number;
                        if (totalPages <= 5) {
                          pageNum = i + 1;
                        } else if (currentPage <= 3) {
                          pageNum = i + 1;
                        } else if (currentPage >= totalPages - 2) {
                          pageNum = totalPages - 4 + i;
                        } else {
                          pageNum = currentPage - 2 + i;
                        }

                        return (
                          <motion.button
                            key={pageNum}
                            whileHover={{ scale: 1.1 }}
                            whileTap={{ scale: 0.95 }}
                            onClick={() => setCurrentPage(pageNum)}
                            className={`h-8 w-8 rounded-lg text-sm font-medium transition-all ${
                              currentPage === pageNum
                                ? "bg-gradient-to-r from-purple-600 to-pink-600 text-white shadow-lg shadow-purple-500/25"
                                : "text-muted-foreground hover:bg-purple-100 dark:hover:bg-purple-900/30"
                            }`}
                          >
                            {pageNum}
                          </motion.button>
                        );
                      })}
                    </div>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() =>
                        setCurrentPage((prev) => Math.min(totalPages, prev + 1))
                      }
                      disabled={currentPage === totalPages}
                      className="border-purple-200 dark:border-purple-800/50 hover:bg-purple-100 dark:hover:bg-purple-900/30"
                    >
                      Next
                      <ChevronRight className="h-4 w-4 ml-1" />
                    </Button>
                  </div>
                </motion.div>
              )}
            </>
          )}
        </Card>
      </motion.div>

      {/* Footer Info - similar to dashboard */}
      <div className="text-center py-8">
        <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-muted/50 border border-border">
          <span className="text-sm text-muted-foreground">
            💡 Tip: Focus on patterns you find challenging and track your progress consistently!
          </span>
        </div>
      </div>
    </div>
  );
}
