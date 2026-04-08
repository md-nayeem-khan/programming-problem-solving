-- CreateTable
CREATE TABLE "problems" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "platform" TEXT NOT NULL,
    "problemId" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "difficulty" TEXT NOT NULL,
    "url" TEXT,
    "notes" TEXT,
    "createdAt" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" DATETIME NOT NULL,
    "source" TEXT NOT NULL DEFAULT 'NeetCode',
    "company" TEXT
);

-- CreateTable
CREATE TABLE "problem_tags" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "problemId" INTEGER NOT NULL,
    "tag" TEXT NOT NULL,
    CONSTRAINT "problem_tags_problemId_fkey" FOREIGN KEY ("problemId") REFERENCES "problems" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "patterns" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name" TEXT NOT NULL,
    "category" TEXT NOT NULL,
    "description" TEXT
);

-- CreateTable
CREATE TABLE "problem_patterns" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "problemId" INTEGER NOT NULL,
    "patternId" INTEGER NOT NULL,
    CONSTRAINT "problem_patterns_problemId_fkey" FOREIGN KEY ("problemId") REFERENCES "problems" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "problem_patterns_patternId_fkey" FOREIGN KEY ("patternId") REFERENCES "patterns" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "submissions" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "problemId" INTEGER NOT NULL,
    "attemptNumber" INTEGER NOT NULL,
    "timeSpentSeconds" INTEGER NOT NULL,
    "status" TEXT NOT NULL,
    "notes" TEXT,
    "submittedAt" DATETIME NOT NULL,
    "createdAt" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "attemptType" TEXT NOT NULL DEFAULT 'First',
    "wasHintUsed" BOOLEAN NOT NULL DEFAULT false,
    "mistakeNote" TEXT,
    "approachNote" TEXT,
    "patternRecognitionSeconds" INTEGER,
    CONSTRAINT "submissions_problemId_fkey" FOREIGN KEY ("problemId") REFERENCES "problems" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "sessions" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "problemId" INTEGER NOT NULL,
    "startedAt" DATETIME NOT NULL,
    "endedAt" DATETIME,
    "durationSeconds" INTEGER,
    "notes" TEXT,
    CONSTRAINT "sessions_problemId_fkey" FOREIGN KEY ("problemId") REFERENCES "problems" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "revisions" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "problemId" INTEGER NOT NULL,
    "scheduledDate" DATETIME NOT NULL,
    "completedDate" DATETIME,
    "status" TEXT NOT NULL DEFAULT 'pending',
    "solvedWithoutHint" BOOLEAN,
    "timeSpentSeconds" INTEGER,
    "notes" TEXT,
    CONSTRAINT "revisions_problemId_fkey" FOREIGN KEY ("problemId") REFERENCES "problems" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "mock_interviews" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "problemId" INTEGER NOT NULL,
    "date" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "timeLimit" INTEGER NOT NULL DEFAULT 2700,
    "timeTakenSeconds" INTEGER,
    "solved" BOOLEAN NOT NULL DEFAULT false,
    "explanationScore" INTEGER,
    "codeQualityScore" INTEGER,
    "overallScore" INTEGER,
    "notes" TEXT,
    CONSTRAINT "mock_interviews_problemId_fkey" FOREIGN KEY ("problemId") REFERENCES "problems" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "daily_progress" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "date" DATETIME NOT NULL,
    "problemsSolved" INTEGER NOT NULL DEFAULT 0,
    "totalTimeSpent" INTEGER NOT NULL DEFAULT 0,
    "patternsWorked" INTEGER NOT NULL DEFAULT 0,
    "mockInterviews" INTEGER NOT NULL DEFAULT 0,
    "createdAt" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" DATETIME NOT NULL
);

-- CreateIndex
CREATE INDEX "problems_platform_idx" ON "problems"("platform");

-- CreateIndex
CREATE INDEX "problems_difficulty_idx" ON "problems"("difficulty");

-- CreateIndex
CREATE INDEX "problems_source_idx" ON "problems"("source");

-- CreateIndex
CREATE INDEX "problems_company_idx" ON "problems"("company");

-- CreateIndex
CREATE UNIQUE INDEX "problems_platform_problemId_key" ON "problems"("platform", "problemId");

-- CreateIndex
CREATE INDEX "problem_tags_problemId_idx" ON "problem_tags"("problemId");

-- CreateIndex
CREATE INDEX "problem_tags_tag_idx" ON "problem_tags"("tag");

-- CreateIndex
CREATE UNIQUE INDEX "patterns_name_key" ON "patterns"("name");

-- CreateIndex
CREATE INDEX "patterns_category_idx" ON "patterns"("category");

-- CreateIndex
CREATE INDEX "problem_patterns_problemId_idx" ON "problem_patterns"("problemId");

-- CreateIndex
CREATE INDEX "problem_patterns_patternId_idx" ON "problem_patterns"("patternId");

-- CreateIndex
CREATE UNIQUE INDEX "problem_patterns_problemId_patternId_key" ON "problem_patterns"("problemId", "patternId");

-- CreateIndex
CREATE INDEX "submissions_problemId_idx" ON "submissions"("problemId");

-- CreateIndex
CREATE INDEX "submissions_submittedAt_idx" ON "submissions"("submittedAt");

-- CreateIndex
CREATE INDEX "submissions_status_idx" ON "submissions"("status");

-- CreateIndex
CREATE INDEX "submissions_attemptType_idx" ON "submissions"("attemptType");

-- CreateIndex
CREATE INDEX "sessions_problemId_idx" ON "sessions"("problemId");

-- CreateIndex
CREATE INDEX "sessions_startedAt_idx" ON "sessions"("startedAt");

-- CreateIndex
CREATE INDEX "revisions_scheduledDate_idx" ON "revisions"("scheduledDate");

-- CreateIndex
CREATE INDEX "revisions_status_idx" ON "revisions"("status");

-- CreateIndex
CREATE INDEX "revisions_problemId_idx" ON "revisions"("problemId");

-- CreateIndex
CREATE INDEX "mock_interviews_date_idx" ON "mock_interviews"("date");

-- CreateIndex
CREATE INDEX "mock_interviews_problemId_idx" ON "mock_interviews"("problemId");

-- CreateIndex
CREATE UNIQUE INDEX "daily_progress_date_key" ON "daily_progress"("date");

-- CreateIndex
CREATE INDEX "daily_progress_date_idx" ON "daily_progress"("date");
