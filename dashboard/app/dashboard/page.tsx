"use client";

import { motion } from "framer-motion";
import { ReadinessCard } from "@/components/dashboard/ReadinessCard";
import { DailyProgressCard } from "@/components/dashboard/DailyProgressCard";
import { PatternStrengthCard } from "@/components/dashboard/PatternStrengthCard";
import { WeakAreasCard } from "@/components/dashboard/WeakAreasCard";
import { TimePerformanceCard } from "@/components/dashboard/TimePerformanceCard";
import { CompanyReadinessCard } from "@/components/dashboard/CompanyReadinessCard";
import { RevisionQueueCard } from "@/components/dashboard/RevisionQueueCard";
import { MockInterviewCard } from "@/components/dashboard/MockInterviewCard";
import { staggerContainer, staggerItem } from "@/lib/animations";
import { Sparkles } from "lucide-react";

export default function DashboardPage() {
  return (
    <div className="min-h-screen">
      <div className="space-y-8 p-6 max-w-7xl mx-auto">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
        >
          <div className="relative">
            <h1 className="relative text-4xl font-bold">
              <span className="text-gradient-purple-pink">
                Dashboard
              </span>
            </h1>
            <p className="text-muted-foreground mt-2 text-base">
              <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
              Track your coding interview preparation progress
            </p>
          </div>
        </motion.div>

        {/* Dashboard Grid */}
        <motion.div
          variants={staggerContainer}
          initial="hidden"
          animate="visible"
          className="grid grid-cols-1 xl:grid-cols-2 gap-8"
        >
          {/* Row 1: Readiness Score & Daily Progress */}
          <motion.div variants={staggerItem} className="xl:col-span-1">
            <ReadinessCard />
          </motion.div>
          <motion.div variants={staggerItem} className="xl:col-span-1">
            <DailyProgressCard />
          </motion.div>

          {/* Row 2: Pattern Strength & Weak Areas */}
          <motion.div variants={staggerItem} className="xl:col-span-1">
            <PatternStrengthCard />
          </motion.div>
          <motion.div variants={staggerItem} className="xl:col-span-1">
            <WeakAreasCard />
          </motion.div>

          {/* Row 3: Time Performance & Company Readiness */}
          <motion.div variants={staggerItem} className="xl:col-span-1">
            <TimePerformanceCard />
          </motion.div>
          <motion.div variants={staggerItem} className="xl:col-span-1">
            <CompanyReadinessCard />
          </motion.div>

          {/* Row 4: Revision Queue & Mock Interviews */}
          <motion.div variants={staggerItem} className="xl:col-span-1">
            <RevisionQueueCard />
          </motion.div>
          <motion.div variants={staggerItem} className="xl:col-span-1">
            <MockInterviewCard />
          </motion.div>
        </motion.div>

        {/* Footer Info */}
        <div className="text-center py-8">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-muted/50 border border-border">
            <span className="text-sm text-muted-foreground">
              💡 Tip: Focus on weak patterns and maintain your daily streak for best results!
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
