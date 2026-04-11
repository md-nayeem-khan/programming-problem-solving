import test from "node:test";
import assert from "node:assert/strict";

import {
  calculateCompanySummaryMetrics,
  calculatePatternCoverageMetrics,
  getLatestSolvedSubmission,
} from "../lib/analytics/company-metrics";

test("getLatestSolvedSubmission returns most recent solved attempt", () => {
  const latest = getLatestSolvedSubmission([
    {
      status: "failed",
      timeSpentSeconds: 2200,
      wasHintUsed: false,
      submittedAt: "2026-04-01T10:00:00.000Z",
    },
    {
      status: "solved",
      timeSpentSeconds: 1400,
      wasHintUsed: true,
      submittedAt: "2026-04-02T10:00:00.000Z",
    },
    {
      status: "solved",
      timeSpentSeconds: 900,
      wasHintUsed: false,
      submittedAt: "2026-04-03T10:00:00.000Z",
    },
  ]);

  assert.ok(latest);
  assert.equal(latest.timeSpentSeconds, 900);
  assert.equal(latest.wasHintUsed, false);
});

test("calculateCompanySummaryMetrics deduplicates solved problems and uses latest solved attempts", () => {
  const metrics = calculateCompanySummaryMetrics([
    {
      id: 1,
      difficulty: "Easy",
      patterns: [{ pattern: { name: "Two Pointers" } }],
      submissions: [
        {
          status: "solved",
          timeSpentSeconds: 1600,
          wasHintUsed: true,
          submittedAt: "2026-04-01T10:00:00.000Z",
        },
        {
          status: "solved",
          timeSpentSeconds: 900,
          wasHintUsed: false,
          submittedAt: "2026-04-02T10:00:00.000Z",
        },
      ],
    },
    {
      id: 2,
      difficulty: "Medium",
      patterns: [{ pattern: { name: "Hash Map" } }],
      submissions: [
        {
          status: "failed",
          timeSpentSeconds: 1700,
          wasHintUsed: true,
          submittedAt: "2026-04-01T11:00:00.000Z",
        },
        {
          status: "solved",
          timeSpentSeconds: 1200,
          wasHintUsed: false,
          submittedAt: "2026-04-03T11:00:00.000Z",
        },
      ],
    },
    {
      id: 3,
      difficulty: "Hard",
      patterns: [{ pattern: { name: "Greedy" } }],
      submissions: [
        {
          status: "failed",
          timeSpentSeconds: 2400,
          wasHintUsed: true,
          submittedAt: "2026-04-03T12:00:00.000Z",
        },
      ],
    },
  ]);

  assert.equal(metrics.solvedProblems, 2);
  assert.deepEqual(metrics.solvedProblemIds, [1, 2]);
  assert.equal(metrics.avgTimeSeconds, 1050);
  assert.equal(metrics.hintPercentage, 0);
  assert.equal(metrics.hintUsageRate, 0);
  assert.equal(metrics.confidence, "Strong");
  assert.equal(metrics.masteryPercentage, 95);
});

test("calculatePatternCoverageMetrics counts solved patterns by solved problem ids", () => {
  const coverage = calculatePatternCoverageMetrics(
    [
      {
        id: 1,
        difficulty: "Easy",
        patterns: [
          { pattern: { name: "Sliding Window" } },
          { pattern: { name: "Hash Map" } },
        ],
        submissions: [],
      },
      {
        id: 2,
        difficulty: "Medium",
        patterns: [{ pattern: { name: "Sliding Window" } }],
        submissions: [],
      },
      {
        id: 3,
        difficulty: "Hard",
        patterns: [{ pattern: { name: "Tree DFS" } }],
        submissions: [],
      },
    ],
    [1, 2]
  );

  const slidingWindow = coverage.find((item) => item.pattern === "Sliding Window");
  const hashMap = coverage.find((item) => item.pattern === "Hash Map");
  const treeDfs = coverage.find((item) => item.pattern === "Tree DFS");

  assert.ok(slidingWindow);
  assert.ok(hashMap);
  assert.ok(treeDfs);

  assert.equal(slidingWindow.total, 2);
  assert.equal(slidingWindow.solved, 2);
  assert.equal(slidingWindow.percentage, 100);

  assert.equal(hashMap.total, 1);
  assert.equal(hashMap.solved, 1);
  assert.equal(hashMap.percentage, 100);

  assert.equal(treeDfs.total, 1);
  assert.equal(treeDfs.solved, 0);
  assert.equal(treeDfs.percentage, 0);
});
