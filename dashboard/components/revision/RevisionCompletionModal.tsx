"use client";

import { useState } from "react";
import { motion } from "framer-motion";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import {
  CheckCircle2,
  Clock,
  Brain,
  Target,
  MessageCircle,
  Sparkles,
  TrendingUp,
} from "lucide-react";

interface RevisionItem {
  id: number;
  problemId: number;
  problemTitle: string;
  pattern: string;
  difficulty: "Easy" | "Medium" | "Hard";
  dueDate: string;
  interval: number;
  repetition: number;
  isOverdue: boolean;
  company?: string;
}

interface CompletionResult {
  wasSuccessful: boolean;
  timeSpentSeconds?: number;
  solvedWithoutHint?: boolean;
  confidenceLevel?: number;
  difficultyRating?: number;
  notes?: string;
}

interface RevisionCompletionModalProps {
  revision: RevisionItem;
  onComplete: (result: CompletionResult) => void;
  isOverdue?: boolean;
}

export function RevisionCompletionModal({ 
  revision, 
  onComplete, 
  isOverdue = false 
}: RevisionCompletionModalProps) {
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  
  // Form state
  const [wasSuccessful, setWasSuccessful] = useState<boolean | null>(null);
  const [timeSpent, setTimeSpent] = useState<string>("");
  const [solvedWithoutHint, setSolvedWithoutHint] = useState<boolean | null>(null);
  const [confidenceLevel, setConfidenceLevel] = useState<number | null>(null);
  const [difficultyRating, setDifficultyRating] = useState<number | null>(null);
  const [notes, setNotes] = useState("");

  const handleSubmit = async () => {
    if (wasSuccessful === null) return;
    
    setLoading(true);
    
    try {
      const result: CompletionResult = {
        wasSuccessful,
        timeSpentSeconds: timeSpent ? parseInt(timeSpent) * 60 : undefined,
        solvedWithoutHint,
        confidenceLevel,
        difficultyRating,
        notes: notes.trim() || undefined,
      };

      await onComplete(result);
      setOpen(false);
      resetForm();
    } catch (error) {
      console.error('Error completing revision:', error);
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setWasSuccessful(null);
    setTimeSpent("");
    setSolvedWithoutHint(null);
    setConfidenceLevel(null);
    setDifficultyRating(null);
    setNotes("");
  };

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case "Easy": return "bg-green-100 text-green-700 border-green-300";
      case "Medium": return "bg-amber-100 text-amber-700 border-amber-300";
      case "Hard": return "bg-red-100 text-red-700 border-red-300";
      default: return "bg-gray-100 text-gray-700 border-gray-300";
    }
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button
          size="sm"
          className={isOverdue 
            ? "bg-red-600 hover:bg-red-700 text-white" 
            : "bg-teal-600 hover:bg-teal-700 text-white"
          }
        >
          {isOverdue ? (
            "Review Now"
          ) : (
            <>
              <CheckCircle2 className="h-3 w-3 mr-1" />
              Complete
            </>
          )}
        </Button>
      </DialogTrigger>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2 text-xl">
            <Target className="h-5 w-5 text-teal-600" />
            Complete Revision: {revision.problemTitle}
          </DialogTitle>
          <div className="flex items-center gap-2 mt-2">
            <Badge className={getDifficultyColor(revision.difficulty)}>
              {revision.difficulty}
            </Badge>
            <Badge variant="outline">{revision.pattern}</Badge>
            {revision.company && (
              <Badge variant="secondary">{revision.company}</Badge>
            )}
          </div>
        </DialogHeader>

        <motion.div 
          className="space-y-6 mt-4"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          {/* Success/Failure */}
          <div className="space-y-3">
            <Label className="text-base font-medium flex items-center gap-2">
              <CheckCircle2 className="h-4 w-4" />
              Did you solve it successfully?
            </Label>
            <RadioGroup 
              value={wasSuccessful?.toString()} 
              onValueChange={(value) => setWasSuccessful(value === "true")}
            >
              <div className="flex items-center space-x-2">
                <RadioGroupItem value="true" id="success" />
                <Label htmlFor="success" className="text-green-600 font-medium">
                  ✅ Yes, I solved it correctly
                </Label>
              </div>
              <div className="flex items-center space-x-2">
                <RadioGroupItem value="false" id="failure" />
                <Label htmlFor="failure" className="text-red-600 font-medium">
                  ❌ No, I couldn't solve it or made mistakes
                </Label>
              </div>
            </RadioGroup>
          </div>

          {/* Time Spent */}
          <div className="space-y-3">
            <Label className="text-base font-medium flex items-center gap-2">
              <Clock className="h-4 w-4" />
              How long did it take? (minutes)
            </Label>
            <Input
              type="number"
              value={timeSpent}
              onChange={(e) => setTimeSpent(e.target.value)}
              placeholder="e.g., 25"
              className="w-32"
            />
          </div>

          {/* Hint Usage */}
          <div className="space-y-3">
            <Label className="text-base font-medium flex items-center gap-2">
              <Brain className="h-4 w-4" />
              Did you solve it without hints?
            </Label>
            <RadioGroup 
              value={solvedWithoutHint?.toString()} 
              onValueChange={(value) => setSolvedWithoutHint(value === "true")}
            >
              <div className="flex items-center space-x-2">
                <RadioGroupItem value="true" id="no-hints" />
                <Label htmlFor="no-hints" className="text-green-600">
                  🧠 No hints needed - solved independently
                </Label>
              </div>
              <div className="flex items-center space-x-2">
                <RadioGroupItem value="false" id="used-hints" />
                <Label htmlFor="used-hints" className="text-amber-600">
                  💡 Used hints or looked at solution
                </Label>
              </div>
            </RadioGroup>
          </div>

          {/* Confidence Level */}
          <div className="space-y-3">
            <Label className="text-base font-medium flex items-center gap-2">
              <Sparkles className="h-4 w-4" />
              How confident do you feel about this problem now?
            </Label>
            <RadioGroup 
              value={confidenceLevel?.toString()} 
              onValueChange={(value) => setConfidenceLevel(parseInt(value))}
              className="grid grid-cols-5 gap-2"
            >
              {[1, 2, 3, 4, 5].map((level) => (
                <div key={level} className="flex flex-col items-center space-y-1">
                  <RadioGroupItem value={level.toString()} id={`confidence-${level}`} />
                  <Label 
                    htmlFor={`confidence-${level}`} 
                    className="text-xs text-center cursor-pointer"
                  >
                    {level === 1 && "😰 Not confident"}
                    {level === 2 && "😕 Slightly"}
                    {level === 3 && "😐 Neutral"}
                    {level === 4 && "😊 Confident"}
                    {level === 5 && "🚀 Very confident"}
                  </Label>
                </div>
              ))}
            </RadioGroup>
          </div>

          {/* Difficulty Rating */}
          <div className="space-y-3">
            <Label className="text-base font-medium flex items-center gap-2">
              <TrendingUp className="h-4 w-4" />
              How difficult did this feel during revision?
            </Label>
            <RadioGroup 
              value={difficultyRating?.toString()} 
              onValueChange={(value) => setDifficultyRating(parseInt(value))}
              className="grid grid-cols-5 gap-2"
            >
              {[1, 2, 3, 4, 5].map((level) => (
                <div key={level} className="flex flex-col items-center space-y-1">
                  <RadioGroupItem value={level.toString()} id={`difficulty-${level}`} />
                  <Label 
                    htmlFor={`difficulty-${level}`} 
                    className="text-xs text-center cursor-pointer"
                  >
                    {level === 1 && "😎 Very easy"}
                    {level === 2 && "🙂 Easy"}
                    {level === 3 && "😐 Medium"}
                    {level === 4 && "😅 Hard"}
                    {level === 5 && "😰 Very hard"}
                  </Label>
                </div>
              ))}
            </RadioGroup>
          </div>

          {/* Notes */}
          <div className="space-y-3">
            <Label className="text-base font-medium flex items-center gap-2">
              <MessageCircle className="h-4 w-4" />
              Notes (optional)
            </Label>
            <Textarea
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="What did you learn? What mistakes did you make? Any insights?"
              className="min-h-[100px]"
            />
          </div>

          {/* Submit Buttons */}
          <div className="flex justify-end gap-3 pt-4 border-t">
            <Button
              variant="outline"
              onClick={() => setOpen(false)}
              disabled={loading}
            >
              Cancel
            </Button>
            <Button
              onClick={handleSubmit}
              disabled={wasSuccessful === null || loading}
              className="bg-teal-600 hover:bg-teal-700"
            >
              {loading ? "Completing..." : "Complete Revision"}
            </Button>
          </div>
        </motion.div>
      </DialogContent>
    </Dialog>
  );
}