package com.cp.problems;

/**
 * Base class for all solutions.
 *
 * Inherit this and implement solve().
 * The test runner calls solve() with parsed arguments.
 *
 * For LeetCode-style (method-based) solutions, you don't need to
 * override solve() — just write your methods normally and use the
 * test framework directly.
 *
 * For Codeforces-style (stdin/stdout) solutions, override solve()
 * to read from the provided Scanner and write to PrintWriter.
 */
public abstract class BaseSolution {

    /**
     * Override for Codeforces-style problems.
     * Input is already buffered; output is flushed automatically.
     */
    public void solve(java.util.Scanner in, java.io.PrintWriter out) {
        // Default: no-op (LeetCode style doesn't use this)
    }

    /**
     * Utility: print array for debugging
     */
    protected static void debug(Object... args) {
        StringBuilder sb = new StringBuilder("[DEBUG] ");
        for (Object a : args) {
            if (a instanceof int[])      sb.append(java.util.Arrays.toString((int[]) a));
            else if (a instanceof int[][]) sb.append(java.util.Arrays.deepToString((int[][]) a));
            else                         sb.append(a);
            sb.append(" ");
        }
        System.err.println(sb);
    }
}
