package com.cp.problems.codeforces;

import com.cp.problems.BaseSolution;
import com.cp.problems.Problem;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Codeforces template — inherits BaseSolution and overrides solve().
 *
 * To run standalone (without test framework):
 *   public static void main(String[] args) { new CF_XXX_YYY().runStdin(); }
 *
 * Naming convention: CF_{contestId}_{problemLetter}.java
 * e.g. CF_1900_A.java = Codeforces Round 1900, Problem A
 */
@Problem(
        id = "1900A",
        title = "Dont Try to Count",
        platform = Problem.Platform.CODEFORCES,
        difficulty = Problem.Difficulty.EASY,
        tags = {"Strings", "Brute Force"},
        url = "https://codeforces.com/contest/1900/problem/A"
)
public class CF_1900_A extends BaseSolution {

    @Override
    public void solve(Scanner in, PrintWriter out) {
        int t = in.nextInt();
        while (t-- > 0) {
            String x = in.next();
            String s = in.next();

            boolean found = false;
            // Try appending x to itself up to |s| times
            StringBuilder xExt = new StringBuilder(x);
            for (int i = 0; i <= s.length(); i++) {
                if (xExt.toString().contains(s)) { out.println(i); found = true; break; }
                xExt.append(x);
            }
            if (!found) out.println(-1);
        }
    }

    /** Convenience: run from stdin for manual testing */
    public void runStdin() {
        try (PrintWriter out = new PrintWriter(System.out)) {
            solve(new Scanner(System.in), out);
        }
    }
}
