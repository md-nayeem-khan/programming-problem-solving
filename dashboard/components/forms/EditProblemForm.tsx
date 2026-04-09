"use client";

import { useEffect, useMemo, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Check, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Dialog,
  DialogContent,
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

interface ProblemForEdit {
  id: number;
  title: string;
  problemId: string;
  platform: string;
  difficulty: string;
  url?: string;
  notes?: string;
  company?: string;
  patterns: { id: number; name: string; category: string }[];
}

interface EditProblemFormProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  problem: ProblemForEdit | null;
  onSuccess?: () => void;
}

const difficultyOptions = ["Easy", "Medium", "Hard"];

const platformOptions = [
  "LeetCode",
  "HackerRank",
  "CodeForces",
  "AtCoder",
  "GeeksforGeeks",
  "Other",
];

const fallbackPatterns = [
  "Two Pointers",
  "Sliding Window",
  "Binary Search",
  "Dynamic Programming",
  "Greedy",
  "Backtracking",
  "Depth-First Search",
  "Breadth-First Search",
  "Hash Table",
  "Array",
  "String",
  "Stack",
  "Queue",
  "Heap",
  "Tree",
  "Graph",
  "Linked List",
  "Trie",
  "Union Find",
  "Bit Manipulation",
];

const companies = [
  "Google",
  "Amazon",
  "Microsoft",
  "Meta",
  "Apple",
  "Netflix",
  "Tesla",
  "Uber",
  "Airbnb",
  "LinkedIn",
  "Spotify",
  "Dropbox",
  "Stripe",
  "Coinbase",
  "ByteDance",
  "Nvidia",
];

const formatDifficultyForSelect = (difficulty?: string) => {
  if (!difficulty) return "";
  const lower = difficulty.toLowerCase();
  if (lower === "easy") return "Easy";
  if (lower === "medium") return "Medium";
  if (lower === "hard") return "Hard";
  return "";
};

export function EditProblemForm({
  open,
  onOpenChange,
  problem,
  onSuccess,
}: EditProblemFormProps) {
  const [loading, setLoading] = useState(false);
  const [availablePatterns, setAvailablePatterns] = useState<{ id: number; name: string }[]>([]);
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

  useEffect(() => {
    const fetchPatterns = async () => {
      try {
        const response = await fetch("/api/patterns");
        if (!response.ok) return;
        const data = await response.json();
        setAvailablePatterns(data.patterns || []);
      } catch {
        setAvailablePatterns([]);
      }
    };

    fetchPatterns();
  }, []);

  useEffect(() => {
    if (!problem) return;

    setFormData({
      title: problem.title || "",
      description: problem.notes || "",
      difficulty: formatDifficultyForSelect(problem.difficulty),
      pattern: problem.patterns[0]?.name || "",
      company: problem.company || "",
      platform: problem.platform || "",
      url: problem.url || "",
      problemId: problem.problemId || "",
    });
  }, [problem]);

  const patternOptions = useMemo(() => {
    if (availablePatterns.length > 0) {
      return availablePatterns.map((p) => p.name);
    }
    return fallbackPatterns;
  }, [availablePatterns]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!problem?.id) return;

    if (!formData.title || !formData.difficulty) {
      toast.error("Required fields missing", {
        description: "Please fill in the problem title and difficulty",
      });
      return;
    }

    setLoading(true);

    try {
      const selectedPatternId = availablePatterns.find(
        (p) => p.name === formData.pattern
      )?.id;

      const response = await fetch(`/api/problems/${problem.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          title: formData.title,
          difficulty: formData.difficulty.toLowerCase(),
          url: formData.url || null,
          notes: formData.description || null,
          source: formData.company ? "Company" : "NeetCode",
          company: formData.company || null,
          patterns: selectedPatternId ? [selectedPatternId] : [],
        }),
      });

      if (!response.ok) throw new Error("Failed to update problem");

      toast.success("Problem updated successfully", {
        description: `${formData.title} has been updated`,
      });

      onOpenChange(false);
      onSuccess?.();
    } catch {
      toast.error("Failed to update problem", {
        description: "Please check your connection and try again",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
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
              <div className="pb-6 border-b border-gray-200">
                <h2 className="text-2xl font-semibold text-gray-900">Edit Problem</h2>
              </div>

              <motion.form
                onSubmit={handleSubmit}
                variants={staggerContainer}
                initial="hidden"
                animate="visible"
                className="space-y-8 pt-8 overflow-y-auto overflow-x-hidden max-h-[75vh] pr-2 w-full"
              >
                <motion.div variants={staggerItem} className="space-y-6">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <Label htmlFor="title" className="text-sm font-medium text-foreground">
                        Problem Title *
                      </Label>
                      <Input
                        id="title"
                        value={formData.title}
                        onChange={(e) => setFormData((prev) => ({ ...prev, title: e.target.value }))}
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
                        onChange={(e) => setFormData((prev) => ({ ...prev, problemId: e.target.value }))}
                        placeholder="e.g., 1, 20, 146"
                        className="h-10 font-mono !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                        required
                        disabled
                      />
                    </div>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-foreground">Platform *</Label>
                      <Select
                        value={formData.platform}
                        onValueChange={(value) => setFormData((prev) => ({ ...prev, platform: value }))}
                        required
                        disabled
                      >
                        <SelectTrigger className="h-10 w-full !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400">
                          <SelectValue placeholder="Choose platform" />
                        </SelectTrigger>
                        <SelectContent>
                          {platformOptions.map((platform) => (
                            <SelectItem key={platform} value={platform}>
                              {platform}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>

                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-foreground">Difficulty *</Label>
                      <Select
                        value={formData.difficulty}
                        onValueChange={(value) => setFormData((prev) => ({ ...prev, difficulty: value }))}
                        required
                      >
                        <SelectTrigger className="h-10 w-full !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400">
                          <SelectValue placeholder="Select difficulty" />
                        </SelectTrigger>
                        <SelectContent>
                          {difficultyOptions.map((difficulty) => (
                            <SelectItem key={difficulty} value={difficulty}>
                              {difficulty}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-foreground">Algorithm Pattern *</Label>
                      <Select
                        value={formData.pattern}
                        onValueChange={(value) => setFormData((prev) => ({ ...prev, pattern: value }))}
                        required
                      >
                        <SelectTrigger className="h-10 w-full !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400">
                          <SelectValue placeholder="Choose algorithm pattern" />
                        </SelectTrigger>
                        <SelectContent>
                          {patternOptions.map((pattern) => (
                            <SelectItem key={pattern} value={pattern}>
                              {pattern}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>

                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-foreground">Company Association *</Label>
                      <Select
                        value={formData.company}
                        onValueChange={(value) => setFormData((prev) => ({ ...prev, company: value }))}
                        required
                      >
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

                  <div className="space-y-2">
                    <Label htmlFor="url" className="text-sm font-medium text-foreground">
                      Problem URL *
                    </Label>
                    <Input
                      id="url"
                      type="url"
                      value={formData.url}
                      onChange={(e) => setFormData((prev) => ({ ...prev, url: e.target.value }))}
                      placeholder="https://leetcode.com/problems/two-sum/"
                      className="h-10 !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                      required
                    />
                  </div>
                </motion.div>

                <motion.div variants={staggerItem} className="space-y-4">
                  <div className="space-y-2">
                    <Label htmlFor="description" className="text-sm font-medium text-foreground">
                      Notes & Description
                    </Label>
                    <Textarea
                      id="description"
                      value={formData.description}
                      onChange={(e) => setFormData((prev) => ({ ...prev, description: e.target.value }))}
                      placeholder="Add any notes, approach hints, or problem insights..."
                      className="min-h-[80px] resize-none !border !border-purple-200 !rounded-md !ring-0 focus-visible:!ring-1 focus-visible:!ring-purple-400 focus-visible:!border-purple-400"
                      rows={3}
                    />
                  </div>
                </motion.div>

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
                    {loading ? "Updating Problem..." : "Save Changes"}
                  </Button>

                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => onOpenChange(false)}
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
