package com.cp.problems.codeforces;

import com.cp.problems.BaseSolution;
import com.cp.problems.Problem;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * HOW TO USE THIS TEMPLATE:
 * ─────────────────────────────────────────────────────────────────
 * 1. Copy this file, rename to CF_{contestId}_{letter}.java
 * 2. Fill in @Problem annotation
 * 3. Write your logic in solve()
 * 4. Create src/test/resources/testcases/codeforces/{contestId}_{letter}.txt
 * 5. Create a matching test class using CF_1900_A_Test as reference
 * ─────────────────────────────────────────────────────────────────
 *
 * Fast I/O tip for large inputs:
 *   Use BufferedReader + StringTokenizer instead of Scanner when TLE.
 *   See FastIO inner class below.
 *
 * Common imports (uncomment as needed):
 *   import com.cp.datastructures.*;
 *   import com.cp.algorithms.*;
 *   import java.util.*;
 */
@Problem(
        id = "TODO",
        title = "TODO",
        platform = Problem.Platform.CODEFORCES,
        difficulty = Problem.Difficulty.UNRATED,
        tags = {},
        url = "https://codeforces.com/contest/TODO/problem/TODO"
)
public class CF_000_X extends BaseSolution {

    @Override
    public void solve(Scanner in, PrintWriter out) {
        int t = in.nextInt(); // number of test cases (remove if single test)
        while (t-- > 0) {
            // ── Read input ──────────────────────────────────────
            int n = in.nextInt();

            // ── Solve ───────────────────────────────────────────
            // TODO

            // ── Write output ────────────────────────────────────
            out.println(n);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Fast I/O for large inputs (swap Scanner for this when TLE)
    // ─────────────────────────────────────────────────────────────
    static class FastIO {
        private final java.io.BufferedReader br;
        private java.util.StringTokenizer st;

        FastIO() { br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in)); }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try { st = new java.util.StringTokenizer(br.readLine()); }
                catch (java.io.IOException e) { throw new RuntimeException(e); }
            }
            return st.nextToken();
        }

        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
        double nextDouble() { return Double.parseDouble(next()); }
        String nextLine() {
            try { return br.readLine(); }
            catch (java.io.IOException e) { throw new RuntimeException(e); }
        }
    }

    /** Standalone runner for manual stdin testing */
    public static void main(String[] args) {
        new CF_000_X().runStdin();
    }

    public void runStdin() {
        try (PrintWriter out = new PrintWriter(System.out)) {
            solve(new Scanner(System.in), out);
        }
    }
}
