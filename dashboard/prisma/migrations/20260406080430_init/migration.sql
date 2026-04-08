/*
  Warnings:

  - You are about to drop the column `codeQualityScore` on the `mock_interviews` table. All the data in the column will be lost.
  - You are about to drop the column `explanationScore` on the `mock_interviews` table. All the data in the column will be lost.
  - You are about to drop the column `overallScore` on the `mock_interviews` table. All the data in the column will be lost.
  - You are about to drop the column `problemId` on the `mock_interviews` table. All the data in the column will be lost.
  - You are about to drop the column `timeLimit` on the `mock_interviews` table. All the data in the column will be lost.
  - You are about to drop the column `timeTakenSeconds` on the `mock_interviews` table. All the data in the column will be lost.
  - You are about to drop the column `completedDate` on the `revisions` table. All the data in the column will be lost.
  - You are about to drop the column `problemId` on the `revisions` table. All the data in the column will be lost.
  - You are about to drop the column `scheduledDate` on the `revisions` table. All the data in the column will be lost.
  - You are about to drop the column `solvedWithoutHint` on the `revisions` table. All the data in the column will be lost.
  - You are about to drop the column `status` on the `revisions` table. All the data in the column will be lost.
  - You are about to drop the column `approachNote` on the `submissions` table. All the data in the column will be lost.
  - You are about to drop the column `createdAt` on the `submissions` table. All the data in the column will be lost.
  - You are about to drop the column `mistakeNote` on the `submissions` table. All the data in the column will be lost.
  - You are about to drop the column `patternRecognitionSeconds` on the `submissions` table. All the data in the column will be lost.
  - You are about to drop the column `submittedAt` on the `submissions` table. All the data in the column will be lost.
  - You are about to drop the column `timeSpentSeconds` on the `submissions` table. All the data in the column will be lost.
  - You are about to drop the column `wasHintUsed` on the `submissions` table. All the data in the column will be lost.
  - Added the required column `problem_id` to the `mock_interviews` table without a default value. This is not possible if the table is not empty.
  - Added the required column `nextReviewDate` to the `revisions` table without a default value. This is not possible if the table is not empty.
  - Added the required column `submissionId` to the `revisions` table without a default value. This is not possible if the table is not empty.
  - Added the required column `submitted_at` to the `submissions` table without a default value. This is not possible if the table is not empty.
  - Added the required column `time_spent_seconds` to the `submissions` table without a default value. This is not possible if the table is not empty.

*/
-- RedefineTables
PRAGMA defer_foreign_keys=ON;
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_mock_interviews" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "problem_id" INTEGER NOT NULL,
    "date" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "time_limit" INTEGER NOT NULL DEFAULT 2700,
    "time_taken_seconds" INTEGER,
    "solved" BOOLEAN NOT NULL DEFAULT false,
    "explanation_score" INTEGER,
    "code_quality_score" INTEGER,
    "overall_score" REAL,
    "notes" TEXT,
    CONSTRAINT "mock_interviews_problem_id_fkey" FOREIGN KEY ("problem_id") REFERENCES "problems" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);
INSERT INTO "new_mock_interviews" ("date", "id", "notes", "solved") SELECT "date", "id", "notes", "solved" FROM "mock_interviews";
DROP TABLE "mock_interviews";
ALTER TABLE "new_mock_interviews" RENAME TO "mock_interviews";
CREATE INDEX "mock_interviews_date_idx" ON "mock_interviews"("date");
CREATE INDEX "mock_interviews_problem_id_idx" ON "mock_interviews"("problem_id");
CREATE INDEX "mock_interviews_overall_score_date_idx" ON "mock_interviews"("overall_score", "date");
CREATE TABLE "new_revisions" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "submissionId" INTEGER NOT NULL,
    "intervalLevel" INTEGER NOT NULL DEFAULT 0,
    "nextReviewDate" DATETIME NOT NULL,
    "completed" BOOLEAN NOT NULL DEFAULT false,
    "completedAt" DATETIME,
    "wasSuccessful" BOOLEAN,
    "timeSpentSeconds" INTEGER,
    "notes" TEXT,
    "previousRevisionId" INTEGER,
    CONSTRAINT "revisions_submissionId_fkey" FOREIGN KEY ("submissionId") REFERENCES "submissions" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);
INSERT INTO "new_revisions" ("id", "notes", "timeSpentSeconds") SELECT "id", "notes", "timeSpentSeconds" FROM "revisions";
DROP TABLE "revisions";
ALTER TABLE "new_revisions" RENAME TO "revisions";
CREATE INDEX "revisions_nextReviewDate_idx" ON "revisions"("nextReviewDate");
CREATE INDEX "revisions_completed_idx" ON "revisions"("completed");
CREATE INDEX "revisions_submissionId_idx" ON "revisions"("submissionId");
CREATE TABLE "new_submissions" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "problemId" INTEGER NOT NULL,
    "attemptNumber" INTEGER NOT NULL,
    "time_spent_seconds" INTEGER NOT NULL,
    "status" TEXT NOT NULL,
    "notes" TEXT,
    "submitted_at" DATETIME NOT NULL,
    "created_at" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "attemptType" TEXT NOT NULL DEFAULT 'First',
    "was_hint_used" BOOLEAN NOT NULL DEFAULT false,
    "mistake_note" TEXT,
    "approach_note" TEXT,
    "pattern_recognition_seconds" INTEGER,
    CONSTRAINT "submissions_problemId_fkey" FOREIGN KEY ("problemId") REFERENCES "problems" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);
INSERT INTO "new_submissions" ("attemptNumber", "attemptType", "id", "notes", "problemId", "status") SELECT "attemptNumber", "attemptType", "id", "notes", "problemId", "status" FROM "submissions";
DROP TABLE "submissions";
ALTER TABLE "new_submissions" RENAME TO "submissions";
CREATE INDEX "submissions_problemId_idx" ON "submissions"("problemId");
CREATE INDEX "submissions_submitted_at_idx" ON "submissions"("submitted_at");
CREATE INDEX "submissions_status_idx" ON "submissions"("status");
CREATE INDEX "submissions_attemptType_idx" ON "submissions"("attemptType");
CREATE INDEX "submissions_pattern_recognition_seconds_idx" ON "submissions"("pattern_recognition_seconds");
CREATE INDEX "submissions_submitted_at_status_idx" ON "submissions"("submitted_at", "status");
CREATE INDEX "submissions_problemId_status_submitted_at_idx" ON "submissions"("problemId", "status", "submitted_at");
PRAGMA foreign_keys=ON;
PRAGMA defer_foreign_keys=OFF;

-- CreateIndex
CREATE INDEX "problem_patterns_patternId_problemId_idx" ON "problem_patterns"("patternId", "problemId");

-- CreateIndex
CREATE INDEX "problems_company_source_idx" ON "problems"("company", "source");

-- CreateIndex
CREATE INDEX "problems_source_difficulty_idx" ON "problems"("source", "difficulty");
