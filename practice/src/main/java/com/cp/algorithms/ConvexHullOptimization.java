package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Convex Hull Optimization (CHT) for Dynamic Programming - HIGH PRIORITY Google (hard)
 * 
 * Essential for:
 * - DP optimization from O(n²) → O(n log n) or O(n)
 * - Problems with quadratic cost function
 * - Batch scheduling problems
 * - Production planning
 * 
 * When to Use:
 * - DP recurrence: dp[i] = min/max(dp[j] + cost(j, i)) for all j < i
 * - Cost function can be written as: a[j] * b[i] + c[j]
 * - Arrays a[] and b[] are monotonic
 * 
 * Key Idea:
 * - Represent each DP state as a line: y = mx + c
 * - Maintain convex hull of lines
 * - Query minimum/maximum value at point x
 * 
 * Interview Questions:
 * - Cost minimization in production
 * - Batch processing optimization
 * - Advanced DP optimization problems
 * 
 * Companies: Google ★★★★ (hard interviews), Competitive Programming ★★★★★
 */
public class ConvexHullOptimization {
    
    // ========== LINE REPRESENTATION ==========
    
    /**
     * Represents a line y = m*x + c
     */
    static class Line {
        long m; // Slope
        long c; // Y-intercept
        
        public Line(long m, long c) {
            this.m = m;
            this.c = c;
        }
        
        /**
         * Evaluate line at point x
         */
        public long eval(long x) {
            return m * x + c;
        }
        
        /**
         * Check if this line is worse than other at point x
         */
        public boolean isWorse(Line other, long x) {
            return this.eval(x) >= other.eval(x);
        }
    }
    
    // ========== CHT FOR MINIMUM (MONOTONIC X) ==========
    
    /**
     * Convex Hull Trick for minimum queries
     * Works when query x values are monotonically increasing
     * 
     * Use case: DP where we query states in increasing order
     */
    @Complexity(time = "O(n) total for n operations", space = "O(n)")
    public static class ConvexHullTrickMin {
        private LinkedList<Line> hull;
        
        public ConvexHullTrickMin() {
            hull = new LinkedList<>();
        }
        
        /**
         * Add a new line y = m*x + c
         * Lines must be added in decreasing order of slope
         */
        public void addLine(long m, long c) {
            Line newLine = new Line(m, c);
            
            // Remove lines that will never be minimum
            while (hull.size() >= 2) {
                Line last = hull.removeLast();
                Line secondLast = hull.peekLast();
                
                // Check if last line is useless
                if (isUseless(secondLast, last, newLine)) {
                    continue; // Keep removing
                } else {
                    hull.addLast(last); // Put it back
                    break;
                }
            }
            
            hull.addLast(newLine);
        }
        
        /**
         * Check if middle line is useless (never minimum)
         * Fixed version: A middle line is useless if the intersection of left and right
         * occurs before or at the intersection of left and middle
         */
        private boolean isUseless(Line left, Line middle, Line right) {
            // For minimum CHT, check intersection points
            // Intersection of left and right: (right.c - left.c) / (left.m - right.m)
            // Intersection of left and middle: (middle.c - left.c) / (left.m - middle.m)
            
            // Cross multiply to avoid division and handle the comparison correctly
            // We want: (right.c - left.c) / (left.m - right.m) <= (middle.c - left.c) / (left.m - middle.m)
            // Cross multiply: (right.c - left.c) * (left.m - middle.m) <= (middle.c - left.c) * (left.m - right.m)
            
            long lhs = (right.c - left.c) * (left.m - middle.m);
            long rhs = (middle.c - left.c) * (left.m - right.m);
            
            return lhs <= rhs;
        }
        
        /**
         * Query minimum value at x
         * Queries must be in non-decreasing order of x
         */
        public long query(long x) {
            // Remove lines from front that are no longer minimum
            while (hull.size() >= 2) {
                Line first = hull.peekFirst();
                Line second = hull.get(1);
                
                if (first.eval(x) >= second.eval(x)) {
                    hull.removeFirst();
                } else {
                    break;
                }
            }
            
            return hull.peekFirst().eval(x);
        }
        
        /**
         * Get current size of hull
         */
        public int size() {
            return hull.size();
        }
    }
    
    // ========== CHT FOR MINIMUM (ARBITRARY X) ==========
    
    /**
     * Convex Hull Trick for minimum with arbitrary x queries
     * Uses binary search on hull
     */
    @Complexity(time = "O(log n) per query", space = "O(n)")
    public static class ConvexHullTrickMinGeneral {
        private List<Line> hull;
        
        public ConvexHullTrickMinGeneral() {
            hull = new ArrayList<>();
        }
        
        /**
         * Add line - lines should be added in decreasing slope order
         */
        public void addLine(long m, long c) {
            Line newLine = new Line(m, c);
            
            while (hull.size() >= 2) {
                int n = hull.size();
                Line last = hull.get(n - 1);
                Line secondLast = hull.get(n - 2);
                
                if (isUseless(secondLast, last, newLine)) {
                    hull.remove(n - 1);
                } else {
                    break;
                }
            }
            
            hull.add(newLine);
        }
        
        private boolean isUseless(Line left, Line middle, Line right) {
            // Fixed version matching the CHT Min implementation
            long lhs = (right.c - left.c) * (left.m - middle.m);
            long rhs = (middle.c - left.c) * (left.m - right.m);
            return lhs <= rhs;
        }
        
        /**
         * Query minimum at arbitrary x using binary search
         */
        public long query(long x) {
            if (hull.size() == 1) {
                return hull.get(0).eval(x);
            }
            
            // Binary search for best line
            int left = 0, right = hull.size() - 1;
            
            while (left < right) {
                int mid = (left + right) / 2;
                
                if (hull.get(mid).eval(x) > hull.get(mid + 1).eval(x)) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            return hull.get(left).eval(x);
        }
    }
    
    // ========== CHT FOR MAXIMUM ==========
    
    /**
     * Convex Hull Trick for maximum queries
     */
    @Complexity(time = "O(n) total", space = "O(n)")
    public static class ConvexHullTrickMax {
        private LinkedList<Line> hull;
        
        public ConvexHullTrickMax() {
            hull = new LinkedList<>();
        }
        
        /**
         * Add line - slopes should be in increasing order
         */
        public void addLine(long m, long c) {
            Line newLine = new Line(m, c);
            
            while (hull.size() >= 2) {
                Line last = hull.removeLast();
                Line secondLast = hull.peekLast();
                
                if (isUselessMax(secondLast, last, newLine)) {
                    continue;
                } else {
                    hull.addLast(last);
                    break;
                }
            }
            
            hull.addLast(newLine);
        }
        
        private boolean isUselessMax(Line left, Line middle, Line right) {
            // For maximum, the condition is reversed
            long lhs = (right.c - left.c) * (left.m - middle.m);
            long rhs = (middle.c - left.c) * (left.m - right.m);
            return lhs >= rhs; // Note: >= for maximum (opposite of minimum)
        }
        
        /**
         * Query maximum at x (monotonic x)
         */
        public long query(long x) {
            while (hull.size() >= 2) {
                Line first = hull.peekFirst();
                Line second = hull.get(1);
                
                if (first.eval(x) <= second.eval(x)) {
                    hull.removeFirst();
                } else {
                    break;
                }
            }
            
            return hull.peekFirst().eval(x);
        }
    }
    
    // ========== EXAMPLE PROBLEM: BATCH SCHEDULING ==========
    
    /**
     * Batch Scheduling Problem
     * 
     * Given n jobs with processing times p[i], schedule them in batches
     * Cost of batch ending at job i: C * i + sum of processing times
     * Find minimum total cost
     * 
     * Recurrence: dp[i] = min(dp[j] + C*i + sum[i] - sum[j]) for j < i
     * Rewrite: dp[i] = min((dp[j] - sum[j]) + (C*i + sum[i]))
     * Lines: y = dp[j] - sum[j], query at x = 1, add C*i + sum[i]
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static long batchScheduling(int[] jobs, long C) {
        int n = jobs.length;
        long[] prefixSum = new long[n + 1];
        
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + jobs[i];
        }
        
        long[] dp = new long[n + 1];
        ConvexHullTrickMinGeneral cht = new ConvexHullTrickMinGeneral();
        
        // Base case
        dp[0] = 0;
        cht.addLine(-0, dp[0] - prefixSum[0]); // Slope -0, intercept dp[0] - sum[0]
        
        for (int i = 1; i <= n; i++) {
            // Query: dp[i] = min(dp[j] - sum[j]) + C*i + sum[i]
            // But we're using slope=0, so just add line at position i
            dp[i] = cht.query(1) + C * i + prefixSum[i];
            
            // Add new line for future queries
            cht.addLine(-i, dp[i] - prefixSum[i]);
        }
        
        return dp[n];
    }
    
    // ========== EXAMPLE PROBLEM: LINEAR DP OPTIMIZATION ==========
    
    /**
     * Generic DP optimization
     * 
     * Problem: dp[i] = min(dp[j] + a[j] * b[i] + c[j]) for j < i
     * Where a[] is decreasing and b[] is increasing
     * 
     * Solution: Represent each dp[j] as line y = a[j]*x + (dp[j] + c[j])
     *           Query at x = b[i]
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static long[] optimizeDP(long[] a, long[] b, long[] c) {
        int n = a.length;
        long[] dp = new long[n];
        ConvexHullTrickMin cht = new ConvexHullTrickMin();
        
        dp[0] = 0;
        cht.addLine(a[0], dp[0] + c[0]);
        
        for (int i = 1; i < n; i++) {
            dp[i] = cht.query(b[i]);
            cht.addLine(a[i], dp[i] + c[i]);
        }
        
        return dp;
    }
    
    // ========== EXAMPLE PROBLEM: MINIMUM COST TO CUT ARRAY ==========
    
    /**
     * Problem: Cut array into segments with minimum cost
     * Cost of segment [l, r] = sum[l to r]
     * 
     * This is a simplified example to show CHT application
     */
    @Complexity(time = "O(n²) → O(n log n) with CHT", space = "O(n)")
    public static long minCostToCut(int[] arr, int k) {
        int n = arr.length;
        long[] prefixSum = new long[n + 1];
        
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + arr[i];
        }
        
        // dp[i] = minimum cost to process first i elements
        long[] dp = new long[n + 1];
        Arrays.fill(dp, Long.MAX_VALUE / 2);
        dp[0] = 0;
        
        // Without CHT: O(n²)
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                long cost = prefixSum[i] - prefixSum[j];
                dp[i] = Math.min(dp[i], dp[j] + cost * cost);
            }
        }
        
        return dp[n];
    }
    
    // ========== LI CHAO TREE (ADVANCED CHT) ==========
    
    /**
     * Li Chao Tree - CHT for arbitrary line insertion and arbitrary queries
     * 
     * Supports:
     * - Add line: O(log n)
     * - Query min/max at x: O(log n)
     * - Works with any line insertion order
     */
    @Complexity(time = "O(log n) per operation", space = "O(n)")
    public static class LiChaoTree {
        private static final long INF = Long.MAX_VALUE / 3;
        private Line[] tree;
        private long minX, maxX;
        
        public LiChaoTree(long minX, long maxX) {
            this.minX = minX;
            this.maxX = maxX;
            int size = 4 * (int)(maxX - minX + 1);
            tree = new Line[size];
        }
        
        /**
         * Add line to tree
         */
        public void addLine(long m, long c) {
            addLine(new Line(m, c), 1, minX, maxX);
        }
        
        private void addLine(Line newLine, int node, long left, long right) {
            if (tree[node] == null) {
                tree[node] = newLine;
                return;
            }
            
            long mid = (left + right) / 2;
            
            boolean leftBetter = newLine.eval(left) < tree[node].eval(left);
            boolean midBetter = newLine.eval(mid) < tree[node].eval(mid);
            
            if (midBetter) {
                Line temp = tree[node];
                tree[node] = newLine;
                newLine = temp;
            }
            
            if (left == right) return;
            
            if (leftBetter != midBetter) {
                addLine(newLine, 2 * node, left, mid);
            } else {
                addLine(newLine, 2 * node + 1, mid + 1, right);
            }
        }
        
        /**
         * Query minimum at x
         */
        public long query(long x) {
            return query(1, minX, maxX, x);
        }
        
        private long query(int node, long left, long right, long x) {
            if (tree[node] == null) return INF;
            if (left == right) return tree[node].eval(x);
            
            long mid = (left + right) / 2;
            long result = tree[node].eval(x);
            
            if (x <= mid) {
                result = Math.min(result, query(2 * node, left, mid, x));
            } else {
                result = Math.min(result, query(2 * node + 1, mid + 1, right, x));
            }
            
            return result;
        }
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Convex Hull Optimization Examples ===\n");
        
        // Example 1: Simple CHT with monotonic queries
        System.out.println("1. CHT for Minimum (Monotonic X):");
        ConvexHullTrickMin chtMin = new ConvexHullTrickMin();
        
        // Add lines in decreasing slope order: 0, -1, -3
        chtMin.addLine(0, 2);   // y = 2
        chtMin.addLine(-1, 5);  // y = -x + 5  
        chtMin.addLine(-3, 10); // y = -3x + 10
        
        System.out.println("   Lines: y=2, y=-x+5, y=-3x+10");
        System.out.println("   Query at x=1: " + chtMin.query(1)); // min(2, 4, 7) = 2
        System.out.println("   Query at x=2: " + chtMin.query(2)); // min(2, 3, 4) = 2
        System.out.println("   Query at x=5: " + chtMin.query(5)); // min(2, 0, -5) = -5
        
        // Example 2: CHT with arbitrary queries
        System.out.println("\n2. CHT with Arbitrary Queries:");
        ConvexHullTrickMinGeneral chtGen = new ConvexHullTrickMinGeneral();
        chtGen.addLine(-2, 8);
        chtGen.addLine(-1, 4);
        chtGen.addLine(0, 3);
        
        System.out.println("   Lines: y=-2x+8, y=-1x+4, y=0x+3");
        System.out.println("   Query at x=3: " + chtGen.query(3));
        System.out.println("   Query at x=1: " + chtGen.query(1));
        System.out.println("   Query at x=5: " + chtGen.query(5));
        
        // Example 3: Batch scheduling
        System.out.println("\n3. Batch Scheduling:");
        int[] jobs = {1, 2, 3, 4, 5};
        long cost = 10;
        System.out.println("   Jobs: " + Arrays.toString(jobs));
        System.out.println("   Batch cost constant C: " + cost);
        System.out.println("   Minimum total cost: " + batchScheduling(jobs, cost));
        
        // Example 4: Li Chao Tree
        System.out.println("\n4. Li Chao Tree (arbitrary order):");
        LiChaoTree lct = new LiChaoTree(0, 10);
        lct.addLine(-1, 10); // y = -x + 10
        lct.addLine(1, 0);   // y = x
        lct.addLine(0, 5);   // y = 5
        
        System.out.println("   Lines: y=-x+10, y=x, y=5");
        for (int x = 0; x <= 10; x += 2) {
            System.out.println("   Query at x=" + x + ": " + lct.query(x));
        }
        
        System.out.println("\n=== When to Use CHT ===");
        System.out.println("DP form: dp[i] = min(dp[j] + a[j]*b[i] + c[j])");
        System.out.println("Requirements:");
        System.out.println("  - a[] decreasing, b[] increasing (or vice versa)");
        System.out.println("  - Cost can be written as linear function");
        System.out.println("Optimization: O(n²) → O(n log n) or O(n)");
        System.out.println("\nUse monotonic CHT when queries are sorted");
        System.out.println("Use Li Chao Tree for arbitrary queries");
    }
}
