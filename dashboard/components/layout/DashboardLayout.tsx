"use client";

import { ReactNode, useState, useEffect } from "react";
import { Sidebar } from "@/components/layout/Sidebar";
import { Navbar } from "@/components/layout/Navbar";
import { motion, AnimatePresence } from "framer-motion";
import { pageTransition } from "@/lib/animations";

interface DashboardLayoutProps {
  children: ReactNode;
}

export function DashboardLayout({ children }: DashboardLayoutProps) {
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);

  // Listen for sidebar state changes
  useEffect(() => {
    const handleSidebarToggle = (event: CustomEvent<boolean>) => {
      setSidebarCollapsed(event.detail);
    };
    
    window.addEventListener('sidebarToggle' as any, handleSidebarToggle);
    return () => window.removeEventListener('sidebarToggle' as any, handleSidebarToggle);
  }, []);

  return (
    <div className="relative h-screen overflow-hidden bg-background">
      {/* Sidebar */}
      <Sidebar />

      {/* Main Content Area */}
      <motion.div 
        className="relative z-10 h-screen flex flex-col transition-all duration-300 ease-in-out"
        animate={{
          marginLeft: sidebarCollapsed ? "80px" : "256px"
        }}
        transition={{ duration: 0.4, ease: [0.25, 0.46, 0.45, 0.94] }}
      >
        {/* Navbar - Fixed at top */}
        <div className="flex-shrink-0">
          <Navbar />
        </div>

        {/* Page Content - Scrollable */}
        <main className="flex-1 p-4 bg-gray-100 overflow-y-auto">
          <div className="min-h-full bg-white rounded-2xl border border-gray-200/60">
            <div className="relative z-10 p-6">
              <AnimatePresence mode="wait">
                <motion.div
                  key={typeof window !== 'undefined' ? window.location.pathname : 'static'}
                  variants={pageTransition}
                  initial="initial"
                  animate="animate"
                  exit="exit"
                  className="w-full"
                >
                  {children}
                </motion.div>
              </AnimatePresence>
            </div>
          </div>
        </main>
      </motion.div>
    </div>
  );
}