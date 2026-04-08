"use client";

import { motion, AnimatePresence } from "framer-motion";
import { Search, Plus, User, Settings, LogOut } from "lucide-react";
import { Button } from "@/components/ui/button";
import { SearchInput } from "@/components/ui/enhanced-input";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Badge } from "@/components/ui/badge";
import { AddProblemForm } from "@/components/forms/AddProblemForm";
import { EnhancedTooltip } from "@/components/ui/tooltip";
import { useState, useEffect } from "react";
import { fadeInUp, buttonRipple } from "@/lib/animations";

export function Navbar() {
  const [searchQuery, setSearchQuery] = useState("");
  const [isScrolled, setIsScrolled] = useState(false);
  const [searchFocused, setSearchFocused] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 20);
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <motion.header
      className="sticky top-0 z-30 flex h-16 items-center justify-between px-6 transition-all duration-300 border-b border-white/10 shadow-md"
      style={{
        background: isScrolled 
          ? "rgba(255, 255, 255, 0.9)" 
          : "rgba(255, 255, 255, 0.8)",
        backdropFilter: "blur(16px)",
        boxShadow: isScrolled
          ? "0 4px 20px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.05)"
          : "0 2px 10px rgba(0, 0, 0, 0.06)",
      }}
      variants={fadeInUp}
      initial="initial"
      animate="animate"
    >
      {/* Enhanced Background Gradient */}
      <motion.div 
        className="absolute inset-0 bg-gradient-to-r from-electric-purple/5 via-transparent to-bright-pink/5 opacity-0"
        animate={{ opacity: isScrolled ? 1 : 0 }}
        transition={{ duration: 0.3 }}
      />

      {/* Search Bar */}
      <div className="flex items-center gap-4 flex-1 max-w-xl relative z-10">
        <motion.div 
          className="relative flex-1"
          animate={{ 
            scale: searchFocused ? 1.01 : 1,
          }}
          transition={{ duration: 0.2 }}
        >
          <div 
            className={`relative rounded-full transition-all duration-300 ${
              searchFocused 
                ? "shadow-lg shadow-electric-purple/20 ring-2 ring-electric-purple/30" 
                : "shadow-sm hover:shadow-md"
            }`}
          >
            <SearchInput
              placeholder="Search problems, patterns, companies..."
              variant="glass"
              size="default"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onFocus={() => setSearchFocused(true)}
              onBlur={() => setSearchFocused(false)}
              className={`rounded-full border-gray-200/60 bg-gray-50/80 hover:bg-white/90 transition-all duration-300 ${
                searchFocused ? "bg-white border-electric-purple/40" : ""
              }`}
            />
          </div>
          
          {/* Search suggestions overlay */}
          <AnimatePresence>
            {searchQuery.length > 0 && (
              <motion.div
                initial={{ opacity: 0, y: 10, scale: 0.95 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                exit={{ opacity: 0, y: 10, scale: 0.95 }}
                transition={{ duration: 0.2, ease: "easeOut" }}
                className="absolute top-full left-0 right-0 mt-3 p-4 bg-white/95 backdrop-blur-xl border border-gray-200/50 rounded-2xl shadow-xl shadow-gray-200/50 z-50"
              >
                <div className="text-xs text-muted-foreground mb-3 font-medium">Quick results for "{searchQuery}"</div>
                <div className="space-y-1">
                  <div className="px-3 py-2 hover:bg-gradient-to-r hover:from-electric-purple/10 hover:to-bright-pink/5 rounded-lg cursor-pointer transition-all duration-200 text-sm font-medium">
                    Two Sum - Array Pattern
                  </div>
                  <div className="px-3 py-2 hover:bg-gradient-to-r hover:from-electric-purple/10 hover:to-bright-pink/5 rounded-lg cursor-pointer transition-all duration-200 text-sm font-medium">
                    Binary Search Template
                  </div>
                </div>
              </motion.div>
            )}
          </AnimatePresence>
        </motion.div>
      </div>

      {/* Right Section */}
      <div className="flex items-center gap-3 relative z-10">
        {/* Add Problem Button */}
        <EnhancedTooltip content="Add new problem" variant="premium">
          <motion.div variants={buttonRipple} whileHover="hover" whileTap="tap">
            <AddProblemForm />
          </motion.div>
        </EnhancedTooltip>

        {/* Profile Menu */}
        <EnhancedTooltip content="Account settings" variant="glass">
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <motion.div variants={buttonRipple} whileHover="hover" whileTap="tap">
                <Button variant="ghost" size="icon" className="rounded-full overflow-hidden">
                  <motion.div 
                    className="flex h-8 w-8 items-center justify-center rounded-full bg-gradient-to-br from-electric-purple to-bright-pink shadow-glow-purple"
                    whileHover={{ 
                      background: "linear-gradient(135deg, #7C3AED 0%, #EC4899 100%)",
                      scale: 1.05,
                    }}
                  >
                    <User className="h-4 w-4 text-white" />
                  </motion.div>
                </Button>
              </motion.div>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-56 bg-white/90 backdrop-blur-md border-white/20">
              <DropdownMenuLabel>
                <div className="flex flex-col space-y-1">
                  <p className="text-sm font-medium">User</p>
                  <p className="text-xs text-muted-foreground">user@company.com</p>
                </div>
              </DropdownMenuLabel>
              <DropdownMenuSeparator />
              
              <motion.div whileHover={{ x: 4 }} transition={{ duration: 0.2 }}>
                <DropdownMenuItem className="cursor-pointer">
                  <User className="mr-2 h-4 w-4" />
                  Profile
                </DropdownMenuItem>
              </motion.div>
              
              <motion.div whileHover={{ x: 4 }} transition={{ duration: 0.2 }}>
                <DropdownMenuItem className="cursor-pointer">
                  <Settings className="mr-2 h-4 w-4" />
                  Settings
                </DropdownMenuItem>
              </motion.div>
              
              <DropdownMenuSeparator />
              
              <motion.div whileHover={{ x: 4 }} transition={{ duration: 0.2 }}>
                <DropdownMenuItem className="text-red-600 cursor-pointer hover:bg-red-50">
                  <LogOut className="mr-2 h-4 w-4" />
                  Log out
                </DropdownMenuItem>
              </motion.div>
            </DropdownMenuContent>
          </DropdownMenu>
        </EnhancedTooltip>
      </div>
      
      {/* Floating particles effect */}
      <div className="absolute top-0 left-1/4 w-1 h-1 bg-electric-purple/20 rounded-full animate-float" />
      <div className="absolute bottom-0 right-1/3 w-1 h-1 bg-bright-pink/20 rounded-full animate-float" style={{ animationDelay: "1s" }} />
    </motion.header>
  );
}
