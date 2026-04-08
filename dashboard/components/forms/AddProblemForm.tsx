"use client";

import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { 
  Plus, Check, X, Code, Building2, Globe, Tag, 
  Zap, Star, ArrowRight, Layers, Palette, Clock 
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import {
  Dialog,
  DialogContent,
  DialogTrigger,
} from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { toast } from "sonner";
import { fadeInUp, staggerContainer, staggerItem } from "@/lib/animations";

interface AddProblemFormProps {
  onSuccess?: () => void;
}

const difficultyConfig = {
  Easy: { 
    color: "from-emerald-400 to-green-500", 
    badge: "bg-emerald-50 text-emerald-700 border border-emerald-200 shadow-emerald-100", 
    icon: "🟢",
    ring: "ring-emerald-500/20"
  },
  Medium: { 
    color: "from-amber-400 to-orange-500", 
    badge: "bg-amber-50 text-amber-700 border border-amber-200 shadow-amber-100", 
    icon: "🟡",
    ring: "ring-amber-500/20"
  },
  Hard: { 
    color: "from-red-400 to-pink-500", 
    badge: "bg-red-50 text-red-700 border border-red-200 shadow-red-100", 
    icon: "🔴",
    ring: "ring-red-500/20"
  },
};

const platformConfig = {
  "LeetCode": { color: "from-orange-500 to-red-500", icon: "💻", accent: "border-orange-200" },
  "HackerRank": { color: "from-green-500 to-emerald-600", icon: "🏆", accent: "border-green-200" },
  "CodeForces": { color: "from-blue-500 to-indigo-600", icon: "🚀", accent: "border-blue-200" },
  "AtCoder": { color: "from-purple-500 to-violet-600", icon: "⚡", accent: "border-purple-200" },
  "GeeksforGeeks": { color: "from-emerald-500 to-teal-600", icon: "📚", accent: "border-emerald-200" },
  "Other": { color: "from-gray-500 to-slate-600", icon: "🔧", accent: "border-gray-200" },
};

const patterns = [
  "Two Pointers", "Sliding Window", "Binary Search", "Dynamic Programming",
  "Greedy", "Backtracking", "Depth-First Search", "Breadth-First Search",
  "Hash Table", "Array", "String", "Stack", "Queue", "Heap", "Tree", "Graph",
  "Linked List", "Trie", "Union Find", "Bit Manipulation"
];

const companies = [
  "Google", "Amazon", "Microsoft", "Meta", "Apple", "Netflix",
  "Tesla", "Uber", "Airbnb", "LinkedIn", "Spotify", "Dropbox",
  "Stripe", "Coinbase", "ByteDance", "Nvidia"
];

export function AddProblemForm({ onSuccess }: AddProblemFormProps) {
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    difficulty: "",
    pattern: "",
    company: "",
    platform: "",
    url: "",
    problemId: "",
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.title || !formData.difficulty) {
      toast.error("Required fields missing", {
        description: "Please fill in the problem title and difficulty",
      });
      return;
    }

    setLoading(true);
    
    try {
      const response = await fetch("/api/problems", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          ...formData,
          source: formData.company ? "Company" : "NeetCode",
        }),
      });

      if (!response.ok) throw new Error("Failed to add problem");

      toast.success("🎉 Problem added successfully!", {
        description: `${formData.title} is ready for tracking`,
        action: {
          label: "Start Solving",
          onClick: () => console.log("Navigate to problem"),
        },
      });
      
      setOpen(false);
      setFormData({
        title: "", description: "", difficulty: "", 
        pattern: "", company: "", platform: "", url: "", problemId: ""
      });
      onSuccess?.();
    } catch (error) {
      toast.error("❌ Failed to add problem", {
        description: "Please check your connection and try again",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <motion.div
          whileHover={{ scale: 1.02, y: -2 }}
          whileTap={{ scale: 0.98 }}
        >
          <Button className="relative gap-2 bg-gradient-purple-pink hover:shadow-md transition-all duration-300 border-0 text-white font-semibold">
            <motion.div
              initial={{ rotate: 0 }}
              whileHover={{ rotate: 180 }}
              transition={{ duration: 0.3, type: "spring" }}
            >
              <Plus className="h-4 w-4" />
            </motion.div>
            
            <span>Add Problem</span>
          </Button>
        </motion.div>
      </DialogTrigger>
      
      <AnimatePresence>
        {open && (
          <DialogContent className="w-full max-w-[650px] min-w-[320px] sm:min-w-[500px] lg:min-w-[600px] max-h-[90vh] overflow-hidden bg-white/90 backdrop-blur-md border border-gray-200/60 rounded-2xl shadow-xl mx-4">
            
            <motion.div
              variants={fadeInUp}
              initial="hidden"
              animate="visible"
              exit="hidden"
              className="relative z-10"
            >
              
              {/* Simple Header */}
              <div className="pb-6 border-b border-gray-200">
                <h2 className="text-2xl font-semibold text-gray-900">Add Problem</h2>
              </div>

              {/* Enhanced Form */}
              <motion.form 
                onSubmit={handleSubmit} 
                variants={staggerContainer}
                initial="hidden"
                animate="visible"
                className="space-y-8 pt-8 overflow-y-auto overflow-x-hidden max-h-[75vh] pr-2 w-full"
              >
                
                {/* All Fields in Two Columns */}
                <motion.div variants={staggerItem} className="space-y-6">
                  {/* Row 1: Problem Title & Problem ID */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <Label htmlFor="title" className="text-sm font-medium text-foreground">
                        Problem Title *
                      </Label>
                      <Input
                        id="title"
                        value={formData.title}
                        onChange={(e) => setFormData(prev => ({ ...prev, title: e.target.value }))}
                        placeholder="e.g., Two Sum, Longest Substring Without Repeating Characters"
                        className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                        required
                      />
                    </div>
                    
                    <div className="space-y-2">
                      <Label htmlFor="problemId" className="text-sm font-medium text-foreground">
                        Problem ID *
                      </Label>
                      <Input
                        id="problemId"
                        value={formData.problemId}
                        onChange={(e) => setFormData(prev => ({ ...prev, problemId: e.target.value }))}
                        placeholder="e.g., 1, 20, 146"
                        className="h-10 font-mono !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                        required
                      />
                    </div>
                  </div>

                  {/* Row 2: Platform & Difficulty */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-foreground">
                        Platform *
                      </Label>
                      <Select value={formData.platform} onValueChange={(value) => setFormData(prev => ({ ...prev, platform: value }))} required>
                        <SelectTrigger className="h-10 w-full !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400">
                          <SelectValue placeholder="Choose platform" />
                        </SelectTrigger>
                        <SelectContent>
                          {Object.entries(platformConfig).map(([platform, config]) => (
                            <SelectItem key={platform} value={platform}>
                              {platform}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>

                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-foreground">
                        Difficulty *
                      </Label>
                      <Select value={formData.difficulty} onValueChange={(value) => setFormData(prev => ({ ...prev, difficulty: value }))} required>
                        <SelectTrigger className="h-10 w-full !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400">
                          <SelectValue placeholder="Select difficulty" />
                        </SelectTrigger>
                        <SelectContent>
                          {Object.entries(difficultyConfig).map(([difficulty, config]) => (
                            <SelectItem key={difficulty} value={difficulty}>
                              {difficulty}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>
                  </div>

                  {/* Row 3: Algorithm Pattern & Company Association */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-foreground">
                        Algorithm Pattern *
                      </Label>
                      <Select value={formData.pattern} onValueChange={(value) => setFormData(prev => ({ ...prev, pattern: value }))} required>
                        <SelectTrigger className="h-10 w-full !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400">
                          <SelectValue placeholder="Choose algorithm pattern" />
                        </SelectTrigger>
                        <SelectContent>
                          {patterns.map((pattern) => (
                            <SelectItem key={pattern} value={pattern}>
                              {pattern}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>

                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-foreground">
                        Company Association *
                      </Label>
                      <Select value={formData.company} onValueChange={(value) => setFormData(prev => ({ ...prev, company: value }))} required>
                        <SelectTrigger className="h-10 w-full !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400">
                          <SelectValue placeholder="Select target company" />
                        </SelectTrigger>
                        <SelectContent>
                          {companies.map((company) => (
                            <SelectItem key={company} value={company}>
                              {company}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>
                  </div>

                  {/* Row 4: Problem URL - Full Width */}
                  <div className="space-y-2">
                    <Label htmlFor="url" className="text-sm font-medium text-foreground">
                      Problem URL *
                    </Label>
                    <Input
                      id="url"
                      type="url"
                      value={formData.url}
                      onChange={(e) => setFormData(prev => ({ ...prev, url: e.target.value }))}
                      placeholder="https://leetcode.com/problems/two-sum/"
                      className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                      required
                    />
                  </div>
                </motion.div>

                {/* Notes Section - Full Width */}
                <motion.div variants={staggerItem} className="space-y-4">
                  <div className="space-y-2">
                    <Label htmlFor="description" className="text-sm font-medium text-foreground">
                      Notes & Description
                    </Label>
                    <Textarea
                      id="description"
                      value={formData.description}
                      onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
                      placeholder="Add any notes, approach hints, or problem insights..."
                      className="min-h-[80px] resize-none !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                      rows={3}
                    />
                  </div>
                </motion.div>

                {/* Action Buttons */}
                <motion.div variants={staggerItem} className="flex gap-4 pt-4">
                  <Button 
                    type="submit" 
                    disabled={loading || !formData.title || !formData.difficulty} 
                    className="flex-1 bg-gradient-purple-pink hover:shadow-md transition-all duration-300 disabled:opacity-50"
                  >
                    {loading ? (
                      <div className="h-4 w-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2" />
                    ) : (
                      <Check className="h-4 w-4 mr-2" />
                    )}
                    {loading ? "Adding Problem..." : "Add Problem"}
                  </Button>
                  
                  <Button 
                    type="button" 
                    variant="outline" 
                    onClick={() => setOpen(false)}
                  >
                    <X className="h-4 w-4 mr-2" />
                    Cancel
                  </Button>
                </motion.div>
              </motion.form>
            </motion.div>
          </DialogContent>
        )}
      </AnimatePresence>
    </Dialog>
  );
}
