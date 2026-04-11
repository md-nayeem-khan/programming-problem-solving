"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Badge } from "@/components/ui/badge";
import { Building2, AlertCircle } from "lucide-react";
import { fadeInUp, staggerContainer, staggerItem } from "@/lib/animations";

interface CompanyReadiness {
  company: string;
  percentage: number;
  problemsSolved: number;
  readiness: "Ready" | "Almost Ready" | "Not Ready";
}

const COMPANY_COLORS = [
  "from-blue-500 to-cyan-500",
  "from-emerald-500 to-teal-500",
  "from-violet-500 to-fuchsia-500",
  "from-orange-500 to-amber-500",
  "from-rose-500 to-pink-500",
  "from-slate-500 to-gray-600",
];

export function CompanyReadinessCard() {
  const [companies, setCompanies] = useState<CompanyReadiness[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchCompanyReadiness() {
      try {
        const response = await fetch("/api/analytics/company?timeframe=month");
        if (!response.ok) throw new Error("Failed to fetch company data");
        
        const data = await response.json();
        
        const companyData = data.companies?.map((c: any) => {
          const readinessValue = c.readinessScore ?? 0;
          return {
            company: c.company,
            percentage: Math.round(readinessValue * 100),
            problemsSolved: c.problemsSolved || 0,
            readiness: readinessValue >= 0.75 ? "Ready" : 
                       readinessValue >= 0.5 ? "Almost Ready" : "Not Ready",
          };
        }) || [];
        
        setCompanies(companyData.sort((a: CompanyReadiness, b: CompanyReadiness) => b.percentage - a.percentage));
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load data");
      } finally {
        setLoading(false);
      }
    }

    fetchCompanyReadiness();
  }, []);

  if (loading) {
    return (
      <Card className="bg-gradient-to-br from-slate-50 to-gray-50 dark:from-slate-950/20 dark:to-gray-950/20 border-slate-200 dark:border-slate-800">
        <CardHeader>
          <Skeleton className="h-6 w-44" />
        </CardHeader>
        <CardContent className="space-y-3">
          {[1, 2, 3, 4].map((i) => (
            <Skeleton key={i} className="h-14 w-full" />
          ))}
        </CardContent>
      </Card>
    );
  }

  if (error) {
    return (
      <Card className="bg-gradient-to-br from-red-50 to-orange-50 dark:from-red-950/20 dark:to-orange-950/20 border-red-200 dark:border-red-800">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-red-600 dark:text-red-400">
            <AlertCircle className="h-5 w-5" />
            Error Loading Company Data
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">{error}</p>
        </CardContent>
      </Card>
    );
  }

  const getReadinessColor = (readiness: string) => {
    switch (readiness) {
      case "Ready": return "text-green-600 dark:text-green-400 bg-green-100 dark:bg-green-900/30 border-green-300 dark:border-green-700";
      case "Almost Ready": return "text-yellow-600 dark:text-yellow-400 bg-yellow-100 dark:bg-yellow-900/30 border-yellow-300 dark:border-yellow-700";
      default: return "text-red-600 dark:text-red-400 bg-red-100 dark:bg-red-900/30 border-red-300 dark:border-red-700";
    }
  };

  return (
    <motion.div
      variants={fadeInUp}
      initial="hidden"
      animate="visible"
    >
      <Card className="dashboard-card dashboard-card-slate dashboard-soft-grid group border-2 border-slate-300/80 dark:border-slate-700 overflow-hidden relative shadow-xl shadow-slate-500/20 hover:shadow-2xl hover:shadow-slate-500/35 transition-all duration-300">
        {/* Decorative gradient orbs */}
        <div className="pointer-events-none absolute -top-10 -right-10 w-40 h-40 bg-gradient-to-br from-slate-400/30 to-zinc-400/30 rounded-full blur-3xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
        <div className="pointer-events-none absolute -bottom-10 -left-10 w-32 h-32 bg-gradient-to-tr from-zinc-400/20 to-slate-400/20 rounded-full blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
        
        <CardHeader className="relative">
          <CardTitle className="flex items-center gap-2 text-lg font-semibold">
            <motion.div
              className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-slate-600 to-zinc-700 shadow-lg shadow-slate-500/30"
              whileHover={{ rotate: 8, scale: 1.05 }}
              transition={{ duration: 0.2 }}
            >
              <Building2 className="h-4 w-4 text-white" />
            </motion.div>
            <span className="bg-gradient-to-r from-slate-700 to-zinc-600 bg-clip-text text-transparent">
              Company Readiness
            </span>
          </CardTitle>
        </CardHeader>
        
        <CardContent className="relative">
          <motion.div
            variants={staggerContainer}
            initial="hidden"
            animate="visible"
            className="space-y-3"
          >
            {companies.map((company, index) => (
              <motion.div
                key={company.company}
                variants={staggerItem}
                className="dashboard-metric-pill group cursor-pointer rounded-xl px-3 py-2"
                whileHover={{ y: -2, scale: 1.01 }}
              >
                <div className="flex items-center justify-between mb-1.5">
                  <div className="flex items-center gap-2">
                    <span className="text-sm font-semibold text-foreground">
                      {company.company}
                    </span>
                    <Badge variant="outline" className={`text-xs ${getReadinessColor(company.readiness)}`}>
                      {company.readiness === "Ready" ? "🟢" : company.readiness === "Almost Ready" ? "🟡" : "🔴"}
                    </Badge>
                  </div>
                  <div className="text-right">
                    <span className="text-lg font-bold text-foreground">
                      {company.percentage}%
                    </span>
                  </div>
                </div>

                {/* Progress Bar */}
                <div className="relative h-3 bg-gray-200 dark:bg-gray-800 rounded-full overflow-hidden">
                  <motion.div
                    className={`h-full bg-gradient-to-r ${COMPANY_COLORS[index % COMPANY_COLORS.length]} relative`}
                    initial={{ width: 0 }}
                    animate={{ width: `${company.percentage}%` }}
                    transition={{ 
                      duration: 1, 
                      delay: index * 0.1 + 0.3,
                      ease: "easeOut" 
                    }}
                  >
                    <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent animate-shimmer" />
                  </motion.div>
                </div>

                <div className="mt-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
                  <p className="text-xs text-muted-foreground">
                    {company.problemsSolved} problems solved
                  </p>
                </div>
              </motion.div>
            ))}
          </motion.div>

          {companies.length === 0 && (
            <div className="text-center py-8">
              <p className="text-sm text-muted-foreground">
                No company data available yet.
              </p>
              <p className="text-xs text-muted-foreground mt-2">
                Start solving company-specific problems to see your readiness!
              </p>
            </div>
          )}

        </CardContent>
      </Card>
    </motion.div>
  );
}
