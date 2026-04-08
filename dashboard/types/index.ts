// BACKEND TYPE DEFINITIONS (Used by API routes)

export interface PatternConfidenceData {
  pattern: string
  category: string
  totalSolved: number
  avgTimeSeconds: number
  hintUsageRate: number
  confidence: 'Weak' | 'Medium' | 'Strong'
}

// Core enums/types
export type Source = 'NeetCode' | 'Company';
export type Company = 'Amazon' | 'Google' | 'Meta' | 'Apple' | 'Netflix' | 'Microsoft';
export type AttemptType = 'First' | 'Revision';
export type Confidence = 'Weak' | 'Medium' | 'Strong';
export type ReadinessLevel = 'Ready' | 'Almost Ready' | 'Not Ready';
export type RevisionStatus = 'pending' | 'completed' | 'skipped';

// Enhanced existing interfaces
export interface Problem {
  id: number;
  platform: 'leetcode' | 'codeforces' | 'atcoder' | 'hackerrank' | 'other';
  problemId: string;
  title: string;
  difficulty: 'easy' | 'medium' | 'hard' | 'unrated';
  url?: string;
  notes?: string;
  createdAt: Date;
  updatedAt: Date;
  
  // NEW FIELDS
  source: Source;
  company?: Company;
}

export interface ProblemTag {
  id: number;
  problemId: number;
  tag: string;
}

export interface Pattern {
  id: number;
  name: string;
  category: string;
  description?: string;
}

export interface ProblemPattern {
  id: number;
  problemId: number;
  patternId: number;
}

export interface Submission {
  id: number;
  problemId: number;
  attemptNumber: number;
  timeSpentSeconds: number;
  status: 'solved' | 'failed' | 'partial' | 'abandoned';
  notes?: string;
  submittedAt: Date;
  createdAt: Date;
  
  // NEW FIELDS
  attemptType: AttemptType;
  wasHintUsed: boolean;
  mistakeNote?: string;
  approachNote?: string;
  patternRecognitionSeconds?: number;
}

export interface Session {
  id: number;
  problemId: number;
  startedAt: Date;
  endedAt?: Date;
  durationSeconds?: number;
  notes?: string;
}

// NEW INTERFACES

export interface Revision {
  id: number;
  problemId: number;
  scheduledDate: Date;
  completedDate?: Date;
  status: RevisionStatus;
  solvedWithoutHint?: boolean;
  timeSpentSeconds?: number;
  confidenceLevel?: number; // 1-5 scale
  difficultyRating?: number; // 1-5 scale  
  notes?: string;
}

export interface MockInterview {
  id: number;
  problemId: number;
  date: Date;
  timeLimit: number; // seconds
  timeTakenSeconds?: number;
  solved: boolean;
  explanationScore?: number; // 1-5
  codeQualityScore?: number; // 1-5
  overallScore?: number; // 1-5
  notes?: string;
}

export interface DailyProgress {
  id: number;
  date: Date;
  problemsSolved: number;
  totalTimeSpent: number; // seconds
  patternsWorked: number;
  mockInterviews: number;
  createdAt: Date;
  updatedAt: Date;
}

// NEW INTERFACES: Goals & Milestones

export type GoalType = 
  | 'problemCount' 
  | 'patternMastery' 
  | 'companyReady' 
  | 'speedImprovement' 
  | 'streakDays' 
  | 'custom';

export type GoalStatus = 'active' | 'completed' | 'paused' | 'abandoned';
export type GoalPriority = 'critical' | 'high' | 'medium' | 'low';
export type GoalUnit = 'problems' | 'patterns' | 'days' | 'minutes' | 'percentage';

export interface Goal {
  id: number;
  title: string;
  description?: string;
  type: GoalType;
  
  targetValue: number;
  currentValue: number;
  unit: GoalUnit;
  
  startDate: Date;
  deadline: Date;
  
  status: GoalStatus;
  priority: GoalPriority;
  
  targetPattern?: string;
  targetCompany?: string;
  targetDifficulty?: string;
  
  completedAt?: Date;
  lastProgressUpdate: Date;
  
  milestones?: Milestone[];
  
  createdAt: Date;
  updatedAt: Date;
}

export interface Milestone {
  id: number;
  goalId: number;
  title: string;
  description?: string;
  targetValue: number;
  dueDate: Date;
  completed: boolean;
  completedDate?: Date;
  completionNote?: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface GoalProgress {
  // All Goal fields
  id: number;
  title: string;
  description?: string;
  type: GoalType;
  targetValue: number;
  currentValue: number;
  unit: GoalUnit;
  startDate: Date;
  deadline: Date;
  status: GoalStatus;
  priority: GoalPriority;
  targetPattern?: string;
  targetCompany?: string;
  targetDifficulty?: string;
  completedAt?: Date;
  lastProgressUpdate: Date;
  milestones?: Milestone[];
  createdAt: Date;
  updatedAt: Date;
  
  // Calculated progress fields
  progressPercentage: number;
  daysRemaining: number;
  isOnTrack: boolean;
  velocity: number; // Progress per day
  projectedCompletionDate: Date;
  nextMilestone?: Milestone;
}

// ANALYTICS INTERFACES

export interface PatternStats {
  pattern: string;
  category: string;
  totalSolved: number;
  avgTimeSeconds: number;
  hintUsageRate: number;
  confidence: Confidence;
  problemIds: number[];
}

export interface ReadinessScore {
  score: number;           // 0.0 - 1.0
  level: ReadinessLevel;
  breakdown: {
    totalProblems: number;
    perfectSolves: number;  // <25min, no hint
    hintedSolves: number;
    failed: number;
  };
}

export interface CompanyReadiness {
  company: Company;
  problemsSolved: number;
  totalProblems: number;
  avgTimeSeconds: number;
  readinessScore: number;
  isReady: boolean;
}

export interface WeeklyProgress {
  problemsSolved: number;
  patternsImproved: number;
  totalTimeSpent: number;
  avgTimePerProblem: number;
  streakDays: number;
}

export interface WeaknessAnalysis {
  weakPatterns: PatternStats[];
  slowPatterns: PatternStats[];
  hintDependentPatterns: PatternStats[];
  recommendations: string[];
}

export interface TimelineData {
  date: string;
  problemsSolved: number;
  totalTime: number;
  avgTime: number;
}

export interface Recommendation {
  id: string;
  type: 'pattern' | 'company' | 'difficulty' | 'revision';
  title: string;
  description: string;
  priority: 'high' | 'medium' | 'low';
  action?: string;
}

// TIME BENCHMARKS
export const TIME_BENCHMARKS = {
  easy: 15 * 60,    // 15 minutes in seconds
  medium: 25 * 60,  // 25 minutes
  hard: 40 * 60     // 40 minutes
} as const;

// COMPANIES LIST
export const COMPANIES: Company[] = [
  'Amazon',
  'Google', 
  'Meta',
  'Apple',
  'Netflix',
  'Microsoft'
];

// CONFIDENCE CALCULATION HELPERS
export const calculatePatternConfidence = (avgTimeSeconds: number, hintUsageRate: number): Confidence => {
  const avgTimeMinutes = avgTimeSeconds / 60;
  
  if (avgTimeMinutes > 35 || hintUsageRate > 0.4) {
    return 'Weak';
  } else if (avgTimeMinutes < 25 && hintUsageRate < 0.2) {
    return 'Strong';
  }
  return 'Medium';
};

export const calculateReadinessScore = (submissions: Submission[]): ReadinessScore => {
  let totalScore = 0;
  const maxScore = submissions.length * 2;
  
  let perfectSolves = 0;
  let hintedSolves = 0;
  let failed = 0;
  
  for (const sub of submissions) {
    if (sub.status === 'solved') {
      const timeMinutes = sub.timeSpentSeconds / 60;
      if (timeMinutes < 25 && !sub.wasHintUsed) {
        totalScore += 2; // Perfect solve
        perfectSolves++;
      } else {
        totalScore += 1; // Solved with hint or slow
        hintedSolves++;
      }
    } else {
      failed++;
    }
  }
  
  const score = maxScore === 0 ? 0 : totalScore / maxScore;
  
  return {
    score,
    level: score >= 0.8 ? 'Ready' : score >= 0.6 ? 'Almost Ready' : 'Not Ready',
    breakdown: {
      totalProblems: submissions.length,
      perfectSolves,
      hintedSolves,
      failed
    }
  };
};
