"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { useRouter } from "next/navigation";
import {
  Check,
  Building2,
  Sparkles,
  BarChart3,
  AlertCircle,
  Edit,
  Plus,
  Trash2,
  X,
  ChevronRight,
} from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
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
import { toast } from "sonner";
import type { Variants } from "framer-motion";
import { staggerContainer, staggerItem } from "@/lib/animations";

type CompanyConfig = {
  id: number;
  name: string;
  icon: string;
  targetProblems: number;
};

type CompanyForm = {
  name: string;
  icon: string;
  targetProblems: string;
};

interface CompanyStat {
  company: string;
  solved: number;
  solvedProblemIds: number[];
  avgTime: number;
  hintPercentage: number;
  confidence: "Weak" | "Medium" | "Strong";
  masteryPercentage: number;
  topPatterns: string[];
  analyticsError: boolean;
};

type CompanyRow = CompanyConfig & {
  solved: number;
  solvedProblemIds: number[];
  avgTime: number;
  hintPercentage: number;
  confidence: "Weak" | "Medium" | "Strong";
  masteryPercentage: number;
  topPatterns: string[];
  analyticsError: boolean;
};

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

export default function CompanyPage() {
  const [stats, setStats] = useState<CompanyStat[]>([]);
  const [companyConfigs, setCompanyConfigs] = useState<CompanyConfig[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [savingCompany, setSavingCompany] = useState(false);
  const [deletingCompany, setDeletingCompany] = useState(false);
  const [editingCompanyId, setEditingCompanyId] = useState<number | null>(null);
  const [pendingDeleteCompanyId, setPendingDeleteCompanyId] = useState<number | null>(null);
  const [companyForm, setCompanyForm] = useState<CompanyForm>({
    name: "",
    icon: "🏢",
    targetProblems: "0",
  });
  const router = useRouter();

  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      const companiesResponse = await fetch("/api/companies");
      if (!companiesResponse.ok) {
        throw new Error(`Companies HTTP error! status: ${companiesResponse.status}`);
      }
      const companiesData = await companiesResponse.json();
      const companies = Array.isArray(companiesData.companies) ? companiesData.companies : [];
      setCompanyConfigs(companies);

      // Fetch analytics for each company in parallel
      const stats: CompanyStat[] = await Promise.all(
        companies.map(async (company: any) => {
          try {
            const res = await fetch(`/api/analytics/company/${company.id}`);
            if (!res.ok) {
              return {
                company: company.name,
                solved: 0,
                solvedProblemIds: [],
                avgTime: 0,
                hintPercentage: 0,
                confidence: "Weak",
                masteryPercentage: 0,
                topPatterns: [],
                analyticsError: true,
              };
            }

            const data = await res.json();

            return {
              company: data.company,
              solved: data.summary.solvedProblems,
              solvedProblemIds: Array.isArray(data.summary.solvedProblemIds)
                ? data.summary.solvedProblemIds
                : [],
              avgTime: Math.round((data.summary.avgTimeSeconds || 0) / 60),
              hintPercentage: data.summary.hintPercentage,
              confidence: data.summary.confidence || "Weak",
              masteryPercentage: data.summary.masteryPercentage || 0,
              topPatterns: Array.isArray(data.patternCoverage) ? data.patternCoverage.slice(0, 2).map((p: any) => p.pattern) : [],
              analyticsError: false,
            };
          } catch {
            return {
              company: company.name,
              solved: 0,
              solvedProblemIds: [],
              avgTime: 0,
              hintPercentage: 0,
              confidence: "Weak",
              masteryPercentage: 0,
              topPatterns: [],
              analyticsError: true,
            };
          }
        })
      );
      const failedCount = stats.filter((item) => item.analyticsError).length;
      if (failedCount > 0) {
        setError(`${failedCount} company analytics entr${failedCount === 1 ? "y" : "ies"} failed to load. Showing available data.`);
      }

      setStats(stats);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load data");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

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

  const openCreateDialog = () => {
    setEditingCompanyId(null);
    setCompanyForm({ name: "", icon: "🏢", targetProblems: "0" });
    setDialogOpen(true);
  };

  const openEditDialog = (company: CompanyConfig) => {
    setEditingCompanyId(company.id);
    setCompanyForm({
      name: company.name,
      icon: company.icon,
      targetProblems: String(company.targetProblems),
    });
    setDialogOpen(true);
  };

  const openDeleteDialog = (companyId: number) => {
    setPendingDeleteCompanyId(companyId);
    setDeleteDialogOpen(true);
  };

  const validateCompanyForm = () => {
    if (!companyForm.name.trim()) {
      toast.error("Company name is required");
      return false;
    }

    const target = Number(companyForm.targetProblems);
    if (!Number.isFinite(target) || target < 0) {
      toast.error("Target problems must be a non-negative number");
      return false;
    }

    const normalizedName = companyForm.name.trim().toLowerCase();
    const duplicate = companyConfigs.find(
      (company) =>
        company.name.toLowerCase() === normalizedName && company.id !== editingCompanyId
    );

    if (duplicate) {
      toast.error("Company already exists");
      return false;
    }

    return true;
  };

  const handleSaveCompany = async () => {
    if (!validateCompanyForm()) return;

    setSavingCompany(true);
    try {
      const targetProblems = Math.max(0, Math.round(Number(companyForm.targetProblems)));

      if (editingCompanyId) {
        const response = await fetch(`/api/companies/${editingCompanyId}`, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            name: companyForm.name.trim(),
            icon: companyForm.icon.trim() || "🏢",
            targetProblems,
          }),
        });

        if (!response.ok) {
          const err = await response.json().catch(() => ({}));
          throw new Error(err.error || "Failed to update company");
        }
      } else {
        const response = await fetch("/api/companies", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            name: companyForm.name.trim(),
            icon: companyForm.icon.trim() || "🏢",
            targetProblems,
          }),
        });

        if (!response.ok) {
          const err = await response.json().catch(() => ({}));
          throw new Error(err.error || "Failed to create company");
        }
      }

      await fetchData();
      toast.success(editingCompanyId ? "Company updated" : "Company added");
      setDialogOpen(false);
    } catch (err) {
      console.error(err);
      toast.error(err instanceof Error ? err.message : "Could not save company");
    } finally {
      setSavingCompany(false);
    }
  };

  const handleDeleteCompany = async () => {
    if (!pendingDeleteCompanyId) return;

    setDeletingCompany(true);
    try {
      const response = await fetch(`/api/companies/${pendingDeleteCompanyId}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.error || "Failed to delete company");
      }

      await fetchData();
      toast.success("Company deleted");
      setDeleteDialogOpen(false);
      setPendingDeleteCompanyId(null);
    } catch (err) {
      console.error(err);
      toast.error(err instanceof Error ? err.message : "Could not delete company");
    } finally {
      setDeletingCompany(false);
    }
  };

  const companyRows: CompanyRow[] = useMemo(
    () =>
      companyConfigs.map((companyConfig) => {
        const stat = stats.find(
          (item) => item.company.toLowerCase() === companyConfig.name.toLowerCase()
        );
        return {
          ...companyConfig,
          solved: stat?.solved ?? 0,
          solvedProblemIds: stat?.solvedProblemIds ?? [],
          avgTime: stat?.avgTime ?? 0,
          hintPercentage: stat?.hintPercentage ?? 0,
          confidence: stat?.confidence ?? "Weak",
          masteryPercentage: stat?.masteryPercentage ?? 0,
          topPatterns: stat?.topPatterns ?? [],
          analyticsError: stat?.analyticsError ?? true,
        };
      }),
    [companyConfigs, stats]
  );

  const summary = useMemo(() => {
    const totalCompanies = companyRows.length;
    const solvedProblemIds = new Set<number>();

    companyRows.forEach((row) => {
      if (row.analyticsError) return;
      row.solvedProblemIds.forEach((problemId) => solvedProblemIds.add(problemId));
    });

    const totalSolved = solvedProblemIds.size;

    const activeMasteryRows = companyRows.filter(
      (row) => !row.analyticsError && row.solved > 0
    );

    const avgMastery =
      activeMasteryRows.length > 0
        ? Math.round(
            activeMasteryRows.reduce((acc, row) => acc + row.masteryPercentage, 0) /
              activeMasteryRows.length
          )
        : 0;

    return { totalCompanies, totalSolved, avgMastery };
  }, [companyRows]);

  if (loading) {
    return <CompanyPageSkeleton />;
  }

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between"
      >
        <div className="relative">
          <h1 className="relative text-4xl font-bold">
            <span className="text-gradient-purple-pink">Company Readiness</span>
          </h1>
          <p className="text-muted-foreground mt-2 text-base">
            <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
            Track your preparation progress for top companies
          </p>
        </div>

        <Button
          className="bg-gradient-to-r from-electric-purple to-bright-pink hover:shadow-glow transition-all duration-300 border-0"
          onClick={openCreateDialog}
        >
          <Plus className="mr-1 h-4 w-4" />
          New Company
        </Button>
      </motion.div>

      {error && (
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
          <Card className="border-rose-200/70 bg-rose-50/60 dark:bg-rose-950/20">
            <CardContent className="pt-6 flex items-center gap-3 text-rose-700 dark:text-rose-300">
              <AlertCircle className="h-5 w-5" />
              <span>{error}</span>
            </CardContent>
          </Card>
        </motion.div>
      )}

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
                  <p className="text-sm font-medium text-muted-foreground">Total Companies</p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-pink-600 to-purple-600 bg-clip-text text-transparent">
                    {summary.totalCompanies}
                  </p>
                </div>
                <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-pink-500 to-purple-500 flex items-center justify-center shadow-lg shadow-pink-500/30">
                  <Building2 className="h-6 w-6 text-white" />
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
                    {summary.totalSolved}
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
                    {summary.avgMastery}%
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

      <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
        <Card className="bg-gradient-to-br from-white to-purple-50/30 dark:from-gray-900 dark:to-purple-950/10 border-2 border-purple-200/50 dark:border-purple-800/30 shadow-xl shadow-purple-500/10 overflow-hidden">
          <div className="overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow className="bg-gradient-to-r from-purple-50/50 to-pink-50/50 dark:from-purple-950/30 dark:to-pink-950/30 hover:bg-purple-50/70 dark:hover:bg-purple-950/40">
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Company
                  </TableHead>
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Solved
                  </TableHead>
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Avg Time
                  </TableHead>
                  <TableHead className="font-semibold text-purple-900 dark:text-purple-100 px-6">
                    Top Patterns
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
                  {companyRows.map((row, index) => {
                    return (
                      <motion.tr
                        key={row.id}
                        custom={index}
                        variants={tableRowVariants}
                        initial="hidden"
                        animate="visible"
                        whileHover="hover"
                        exit={{ opacity: 0, x: -20 }}
                        className="group border-b border-purple-100 dark:border-purple-900/30 transition-colors cursor-pointer hover:bg-purple-50/50 dark:hover:bg-purple-950/20"
                        onClick={() => router.push(`/problems?companyId=${row.id}`)}
                      >
                        <TableCell className="font-medium px-6">
                          <div className="flex items-center gap-2">
                            <span className="text-xl leading-none">{row.icon || "🏢"}</span>
                            <span className="text-foreground group-hover:text-purple-700 dark:group-hover:text-purple-300 transition-colors">
                              {row.name}
                            </span>
                            <ChevronRight className="h-4 w-4 text-muted-foreground opacity-0 transition-opacity group-hover:opacity-100" />
                          </div>
                        </TableCell>
                        <TableCell className="px-6">
                          <span className="font-semibold text-foreground">
                            {row.analyticsError ? "-" : row.solved}
                          </span>
                        </TableCell>
                        <TableCell className="px-6">
                          <span className="text-sm text-muted-foreground">
                            {row.analyticsError ? "-" : `${row.avgTime}m`}
                          </span>
                        </TableCell>
                        <TableCell className="px-6">
                          <div className="flex flex-wrap gap-1 max-w-[240px]">
                            {row.topPatterns.slice(0, 2).map((pattern) => (
                              <Badge
                                key={pattern}
                                variant="secondary"
                                className="text-xs bg-purple-100/60 text-purple-700 border-purple-200/60 dark:bg-purple-900/20 dark:text-purple-300"
                              >
                                {pattern}
                              </Badge>
                            ))}
                            {row.topPatterns.length === 0 && (
                              <span className="text-sm text-muted-foreground">-</span>
                            )}
                          </div>
                        </TableCell>
                        <TableCell className="px-6">
                          <span className="text-sm text-muted-foreground">
                            {row.analyticsError ? "-" : `${Math.round(row.hintPercentage)}%`}
                          </span>
                        </TableCell>
                        <TableCell className="px-6">
                          <Badge className={`${row.analyticsError ? "bg-slate-500 text-white" : getConfidenceBadge(row.confidence)} text-xs font-medium`}>
                            {row.analyticsError ? "N/A" : row.confidence}
                          </Badge>
                        </TableCell>
                        <TableCell className="px-6">
                          <div className="min-w-[130px] space-y-1">
                            <div className="flex items-center justify-between text-xs text-muted-foreground">
                              <span>Level</span>
                              <span className="font-medium text-foreground">
                                {row.analyticsError ? "-" : `${Math.round(row.masteryPercentage)}%`}
                              </span>
                            </div>
                            <div className="h-2 rounded-full bg-gray-200 dark:bg-gray-800 overflow-hidden">
                              <div
                                className="h-full rounded-full bg-gradient-to-r from-purple-500 to-pink-500"
                                style={{ width: row.analyticsError ? "0%" : `${Math.max(0, Math.min(100, Math.round(row.masteryPercentage)))}%` }}
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
                              onClick={() =>
                                openEditDialog({
                                  id: row.id,
                                  name: row.name,
                                  icon: row.icon,
                                  targetProblems: row.targetProblems,
                                })
                              }
                              className="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-blue-100 text-blue-600 transition-colors hover:bg-blue-200 dark:bg-blue-900/30 dark:text-blue-400 dark:hover:bg-blue-900/50"
                              title="Edit Company"
                              aria-label="Edit Company"
                            >
                              <Edit className="h-4 w-4" />
                            </motion.button>

                            <motion.button
                              whileHover={{ scale: 1.1 }}
                              whileTap={{ scale: 0.95 }}
                              onClick={() => openDeleteDialog(row.id)}
                              className="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-red-100 text-red-600 transition-colors hover:bg-red-200 dark:bg-red-900/30 dark:text-red-400 dark:hover:bg-red-900/50"
                              title="Delete Company"
                              aria-label="Delete Company"
                            >
                              <Trash2 className="h-4 w-4" />
                            </motion.button>
                          </div>
                        </TableCell>
                      </motion.tr>
                    );
                  })}
                </AnimatePresence>
              </TableBody>
            </Table>
          </div>
        </Card>
      </motion.div>

      {companyRows.length === 0 && !error && (
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center py-16"
        >
          <div className="flex h-20 w-20 items-center justify-center rounded-full bg-gradient-to-br from-pink-100 to-purple-100 dark:from-pink-950/30 dark:to-purple-950/30 mx-auto mb-4">
            <Building2 className="h-10 w-10 text-pink-500" />
          </div>
          <h3 className="text-lg font-semibold text-foreground mb-2">No companies found</h3>
          <p className="text-muted-foreground">Add your first company to start tracking readiness.</p>
        </motion.div>
      )}

      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent className="w-full max-w-[650px] min-w-[320px] sm:min-w-[500px] lg:min-w-[600px] max-h-[90vh] overflow-hidden bg-white/90 backdrop-blur-md border border-gray-200/60 rounded-2xl shadow-xl mx-4">
          <DialogHeader className="pb-6 border-b border-gray-200">
            <DialogTitle className="text-2xl font-semibold text-gray-900">
              {editingCompanyId ? "Edit Company" : "Create Company"}
            </DialogTitle>
            <DialogDescription className="text-sm text-gray-500">
              Configure company card details and target problem count.
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-8 pt-8 overflow-y-auto overflow-x-hidden max-h-[75vh] pr-2 w-full">
            <div className="grid gap-2">
              <Label htmlFor="company-name">Company Name</Label>
              <Input
                id="company-name"
                value={companyForm.name}
                onChange={(e) =>
                  setCompanyForm((prev) => ({ ...prev, name: e.target.value }))
                }
                placeholder="Example: OpenAI"
                className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
              />
            </div>

            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
              <div className="grid gap-2">
                <Label htmlFor="company-icon">Icon</Label>
                <Input
                  id="company-icon"
                  value={companyForm.icon}
                  onChange={(e) =>
                    setCompanyForm((prev) => ({ ...prev, icon: e.target.value }))
                  }
                  placeholder="Example: 🏢"
                  className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                />
              </div>

              <div className="grid gap-2">
                <Label htmlFor="company-target">Target Problems</Label>
                <Input
                  id="company-target"
                  type="number"
                  min="0"
                  value={companyForm.targetProblems}
                  onChange={(e) =>
                    setCompanyForm((prev) => ({ ...prev, targetProblems: e.target.value }))
                  }
                  className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                />
              </div>
            </div>
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={() => setDialogOpen(false)}>
              <X className="h-4 w-4 mr-2" />
              Cancel
            </Button>
            <Button
              onClick={handleSaveCompany}
              disabled={savingCompany}
              className="bg-gradient-purple-pink hover:shadow-md transition-all duration-300 disabled:opacity-50"
            >
              {savingCompany ? (
                <div className="h-4 w-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2" />
              ) : (
                <Check className="h-4 w-4 mr-2" />
              )}
              {savingCompany ? "Saving..." : editingCompanyId ? "Update Company" : "Create Company"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
        <DialogContent className="w-full max-w-[500px] bg-white/90 backdrop-blur-md border border-gray-200/60 rounded-2xl shadow-xl">
          <DialogHeader className="pb-4 border-b border-gray-200">
            <DialogTitle className="text-2xl font-semibold text-gray-900">Delete Company</DialogTitle>
            <DialogDescription className="text-sm text-gray-500">
              This action cannot be undone.
            </DialogDescription>
          </DialogHeader>

          <div className="py-4 text-sm text-muted-foreground">Delete this company card?</div>

          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => {
                setDeleteDialogOpen(false);
                setPendingDeleteCompanyId(null);
              }}
            >
              <X className="h-4 w-4 mr-2" />
              Cancel
            </Button>
            <Button
              onClick={handleDeleteCompany}
              disabled={deletingCompany}
              className="bg-red-600 hover:bg-red-700 text-white transition-all duration-300 disabled:opacity-50"
            >
              {deletingCompany ? (
                <div className="h-4 w-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2" />
              ) : (
                <Trash2 className="h-4 w-4 mr-2" />
              )}
              {deletingCompany ? "Deleting..." : "Delete Company"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}

function CompanyPageSkeleton() {
  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
        <div className="space-y-2">
          <Skeleton className="h-10 w-64" />
          <Skeleton className="h-4 w-72" />
        </div>
        <Skeleton className="h-10 w-32 rounded-md" />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {[1, 2, 3].map((i) => (
          <Card key={i} className="border-2">
            <CardContent className="pt-6">
              <div className="flex items-center justify-between">
                <div className="space-y-2">
                  <Skeleton className="h-4 w-24" />
                  <Skeleton className="h-8 w-20" />
                </div>
                <Skeleton className="h-12 w-12 rounded-xl" />
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

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
              {Array.from({ length: 6 }).map((_, row) => (
                <TableRow key={row}>
                  <TableCell className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <Skeleton className="h-5 w-5 rounded" />
                      <Skeleton className="h-4 w-28" />
                    </div>
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <Skeleton className="h-4 w-10" />
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <Skeleton className="h-4 w-12" />
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <Skeleton className="h-6 w-24 rounded-full" />
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <Skeleton className="h-6 w-24 rounded-full" />
                  </TableCell>
                  <TableCell className="px-6 py-4">
                    <Skeleton className="h-6 w-24 rounded-full" />
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
                    <div className="flex justify-end gap-2">
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
