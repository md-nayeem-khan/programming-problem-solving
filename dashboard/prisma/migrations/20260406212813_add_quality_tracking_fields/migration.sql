-- AlterTable
ALTER TABLE "revisions" ADD COLUMN "confidenceLevel" INTEGER;
ALTER TABLE "revisions" ADD COLUMN "difficultyRating" INTEGER;
ALTER TABLE "revisions" ADD COLUMN "solvedWithoutHint" BOOLEAN;
