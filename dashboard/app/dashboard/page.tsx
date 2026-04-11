"use client";

import { motion } from "framer-motion";
import { ReadinessCard } from "@/components/dashboard/ReadinessCard";
import { DailyProgressCard } from "@/components/dashboard/DailyProgressCard";
import { TimePerformanceCard } from "@/components/dashboard/TimePerformanceCard";
import { AnalyticsHighlightsCard } from "@/components/dashboard/AnalyticsHighlightsCard";
import { DashboardHeaderStats } from "@/components/dashboard/DashboardHeaderStats";
import { staggerContainer, staggerItem } from "@/lib/animations";
import { Sparkles } from "lucide-react";
import type { Variants } from "framer-motion";

const cardHoverVariants: Variants = {
  rest: {
    scale: 1,
    boxShadow: "0 4px 6px -1px rgba(0, 0, 0, 0.1)",
  },
  hover: {
    scale: 1.02,
    boxShadow: "0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)",
    transition: {
      duration: 0.3,
      ease: "easeOut",
    },
  },
};

export default function DashboardPage() {
  return (
    <div className="min-h-screen relative overflow-hidden">
      <div className="space-y-8 p-6 max-w-7xl mx-auto relative z-10">
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

        <DashboardHeaderStats />

        {/* Dashboard Grid */}
        <motion.div
          variants={staggerContainer}
          initial="hidden"
          animate="visible"
          className="grid grid-cols-1 xl:grid-cols-3 gap-8 items-stretch"
        >
          {/* Row 1: Readiness Score & Daily Progress */}
          <motion.div
            variants={staggerItem}
            className="xl:col-span-1 h-full"
          >
            <motion.div
              variants={cardHoverVariants}
              initial="rest"
              whileHover="hover"
              whileTap={{ scale: 0.98 }}
              className="cursor-pointer h-full"
            >
              <ReadinessCard />
            </motion.div>
          </motion.div>
          <motion.div
            variants={staggerItem}
            className="xl:col-span-1 h-full"
          >
            <motion.div
              variants={cardHoverVariants}
              initial="rest"
              whileHover="hover"
              whileTap={{ scale: 0.98 }}
              className="cursor-pointer h-full"
            >
              <DailyProgressCard />
            </motion.div>
          </motion.div>

          {/* Row 2: Time Performance */}
          <motion.div
            variants={staggerItem}
            className="xl:col-span-1 h-full"
          >
            <motion.div
              variants={cardHoverVariants}
              initial="rest"
              whileHover="hover"
              whileTap={{ scale: 0.98 }}
              className="cursor-pointer h-full"
            >
              <TimePerformanceCard />
            </motion.div>
          </motion.div>

        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.1 }}
        >
          <motion.div
            variants={cardHoverVariants}
            initial="rest"
            whileHover="hover"
            whileTap={{ scale: 0.98 }}
            className="cursor-pointer"
          >
            <AnalyticsHighlightsCard />
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
