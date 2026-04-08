"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { motion, AnimatePresence } from "framer-motion";
import { cn } from "@/lib/utils";
import {
  LayoutDashboard,
  BookOpen,
  Brain,
  Building2,
  RotateCcw,
  Target,
  BarChart3,
  ChevronLeft,
  ChevronRight,
  Flag,
  Code,
  } from "lucide-react";
import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { Tooltip, TooltipContent, TooltipTrigger } from "@/components/ui/tooltip";
import { GlassCard } from "@/components/ui/glass-card";
import { springBounce, fadeInLeft, staggerContainer, staggerItem } from "@/lib/animations";

const navItems = [
  {
    title: "Dashboard",
    href: "/dashboard",
    icon: LayoutDashboard,
    color: "text-electric-blue",
    gradient: "from-electric-blue to-electric-cyan",
    description: "Overview & insights",
  },
  {
    title: "Goals",
    href: "/goals",
    icon: Flag,
    color: "text-amber-500",
    gradient: "from-amber-500 to-orange-500",
    description: "Goals & milestones",
  },
  {
    title: "Problems",
    href: "/problems",
    icon: BookOpen,
    color: "text-electric-purple",
    gradient: "from-electric-purple to-bright-pink",
    description: "Manage problems",
  },
  {
    title: "Patterns",
    href: "/patterns",
    icon: Brain,
    color: "text-bright-pink",
    gradient: "from-bright-pink to-electric-purple",
    description: "Pattern analysis",
  },
  {
    title: "Company",
    href: "/company",
    icon: Building2,
    color: "text-sunset-orange",
    gradient: "from-sunset-orange to-coral-red",
    description: "Company readiness",
  },
  {
    title: "Revision",
    href: "/revision",
    icon: RotateCcw,
    color: "text-vibrant-green",
    gradient: "from-vibrant-green to-emerald-500",
    description: "Review schedule",
  },
  {
    title: "Mock Interviews",
    href: "/mock",
    icon: Target,
    color: "text-coral-red",
    gradient: "from-coral-red to-sunset-orange",
    description: "Practice sessions",
  },
  {
    title: "Analytics",
    href: "/analytics",
    icon: BarChart3,
    color: "text-electric-cyan",
    gradient: "from-electric-cyan to-electric-blue",
    description: "Deep insights",
  },
];

export function Sidebar() {
  const pathname = usePathname();
  const [collapsed, setCollapsed] = useState(false);
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  // Emit sidebar state changes to parent components
  useEffect(() => {
    const event = new CustomEvent('sidebarToggle', { detail: collapsed });
    window.dispatchEvent(event);
  }, [collapsed]);

  if (!mounted) {
    return (
      <div className="fixed left-0 top-0 z-40 h-screen w-64 border-r border-white/20 bg-card/50 backdrop-blur-xl" />
    );
  }

  return (
    <motion.aside
      initial={{ x: -256 }}
      animate={{ 
        x: 0, 
        width: collapsed ? 80 : 256 
      }}
      transition={{ 
        duration: 0.4, 
        ease: [0.25, 0.46, 0.45, 0.94],
        x: { duration: 0.6 }
      }}
      className="fixed left-0 top-0 z-40 h-screen overflow-hidden"
    >
      <GlassCard 
        variant="default" 
        size="sm"
        className="h-full w-full rounded-none border-r border-white/20 shadow-xl !p-0 bg-white/60"
        hover={false}
      >
        <div className="flex h-full flex-col relative">

          {/* Logo Section */}
          <motion.div 
            className="flex h-16 items-center justify-between px-4 border-b border-white/10 relative z-10"
            style={{
              background: "rgba(255, 255, 255, 0.8)",
              boxShadow: "0 2px 10px rgba(0, 0, 0, 0.06)",
            }}
            variants={springBounce}
            initial="hidden"
            animate="visible"
          >
            <AnimatePresence mode="wait">
              {!collapsed && (
                <motion.div
                  initial={{ opacity: 0, x: -20 }}
                  animate={{ opacity: 1, x: 0 }}
                  exit={{ opacity: 0, x: -20 }}
                  transition={{ duration: 0.3 }}
                  className="flex items-center gap-3"
                >
                  <motion.div 
                    className="flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-electric-purple to-bright-pink shadow-lg shadow-electric-purple/30"
                    whileHover={{ 
                      scale: 1.1, 
                      rotate: 360,
                      boxShadow: "0 0 30px rgba(124, 58, 237, 0.5)"
                    }}
                    transition={{ duration: 0.5 }}
                  >
                    <Code className="h-6 w-6 text-white" />
                  </motion.div>
                  <div className="flex flex-col">
                    <span className="text-base font-black text-gradient-purple-pink leading-tight whitespace-nowrap">
                      AlgoMetrics
                    </span>
                    <span className="text-xs text-muted-foreground font-medium">
                      Dashboard
                    </span>
                  </div>
                </motion.div>
              )}
            </AnimatePresence>

            <motion.div
              whileHover={{ scale: 1.1 }}
              whileTap={{ scale: 0.9 }}
            >
              <Button
                variant="ghost"
                size="icon"
                className="h-8 w-8 rounded-lg hover:bg-white/10 border border-white/20 backdrop-blur-sm"
                onClick={() => setCollapsed(!collapsed)}
              >
                <motion.div
                  animate={{ rotate: collapsed ? 180 : 0 }}
                  transition={{ duration: 0.3 }}
                >
                  <ChevronLeft className="h-4 w-4" />
                </motion.div>
              </Button>
            </motion.div>
          </motion.div>

          {/* Navigation Items */}
          <div className="flex-1 overflow-y-auto p-4 relative z-10">
            <motion.div
              variants={staggerContainer}
              initial="hidden"
              animate="visible"
              className="space-y-1"
            >
              {navItems.map((item, index) => {
                const isActive = pathname === item.href || pathname?.startsWith(item.href + "/");
                const Icon = item.icon;

                return (
                  <motion.div
                    key={item.href}
                    variants={staggerItem}
                  >
                    <Tooltip delayDuration={0}>
                      <TooltipTrigger asChild>
                        <Link href={item.href}>
                          <motion.div
                            className={cn(
                              "group relative flex items-center gap-4 rounded-xl px-3 py-3 transition-all duration-300 cursor-pointer overflow-hidden",
                              isActive
                                ? "bg-white/20 text-foreground border border-white/30 shadow-lg backdrop-blur-sm"
                                : "text-muted-foreground hover:bg-white/10 hover:text-foreground border border-transparent hover:border-white/20"
                            )}
                            whileHover={{ 
                              scale: 1.02,
                              x: 4,
                            }}
                            whileTap={{ scale: 0.98 }}
                            transition={{ duration: 0.2 }}
                          >
                            {/* Active indicator */}
                            {isActive && (
                              <motion.div
                                layoutId="sidebar-active-indicator"
                                className={`absolute left-0 top-0 h-full w-1 bg-gradient-to-b ${item.gradient} rounded-r-full`}
                                transition={{ type: "spring", bounce: 0.2, duration: 0.6 }}
                              />
                            )}
                            
                            {/* Hover glow effect */}
                            <motion.div
                              className={`absolute inset-0 bg-gradient-to-r ${item.gradient} opacity-0 group-hover:opacity-10 rounded-xl transition-opacity duration-300`}
                            />

                            {/* Icon */}
                            <motion.div
                              className={cn(
                                "relative flex h-6 w-6 items-center justify-center",
                                isActive ? item.color : ""
                              )}
                              whileHover={{ 
                                rotate: [0, -10, 10, 0],
                                scale: [1, 1.1, 1]
                              }}
                              transition={{ duration: 0.5 }}
                            >
                              <Icon className="h-5 w-5" />
                              {isActive && (
                                <motion.div
                                  className={`absolute inset-0 bg-gradient-to-r ${item.gradient} opacity-20 rounded-full blur-sm`}
                                  animate={{ scale: [1, 1.2, 1] }}
                                  transition={{ duration: 2, repeat: Infinity }}
                                />
                              )}
                            </motion.div>

                            {/* Label */}
                            <AnimatePresence mode="wait">
                              {!collapsed && (
                                <motion.div
                                  initial={{ opacity: 0, x: -10 }}
                                  animate={{ opacity: 1, x: 0 }}
                                  exit={{ opacity: 0, x: -10 }}
                                  transition={{ duration: 0.2 }}
                                  className="flex flex-col min-w-0 flex-1"
                                >
                                  <span className="text-sm font-semibold truncate">
                                    {item.title}
                                  </span>
                                  <span className="text-xs text-muted-foreground/70 truncate">
                                    {item.description}
                                  </span>
                                </motion.div>
                              )}
                            </AnimatePresence></motion.div>
                        </Link>
                      </TooltipTrigger>
                      {collapsed && (
                        <TooltipContent side="right" className="bg-background border border-white/20">
                          <div>
                            <p className="font-semibold">{item.title}</p>
                            <p className="text-xs text-muted-foreground">{item.description}</p>
                          </div>
                        </TooltipContent>
                      )}
                    </Tooltip>
                  </motion.div>
                );
              })}
            </motion.div>
          </div>
        </div>
      </GlassCard>
    </motion.aside>
  );
}
