package com.cp.problems.codeforces;

import com.cp.problems.BaseSolution;
import com.cp.problems.Problem;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Watermelon - One of the most famous beginner problems on Codeforces.
 *
 * Problem: Pete and Billy want to divide a watermelon of weight w into two parts,
 * each part must have even positive weight. Is it possible?
 *
 * Solution: We need w = a + b where a > 0, b > 0, and both a, b are even.
 * This is possible iff w > 2 and w is even (smallest valid split is 2 + 2 = 4).
 */
@Problem(
        id = "4A",
        title = "Watermelon",
        platform = Problem.Platform.CODEFORCES,
        difficulty = Problem.Difficulty.EASY,
        tags = {"Math", "Brute Force"},
        url = "https://codeforces.com/contest/4/problem/A"
)
public class CF_4_A extends BaseSolution {

    @Override
    public void solve(Scanner in, PrintWriter out) {
        int w = in.nextInt();
        
        // Can divide into two even positive parts iff w >= 4 and w is even
        if (w >= 4 && w % 2 == 0) {
            out.println("YES");
        } else {
            out.println("NO");
        }
    }

    public static void main(String[] args) {
        new CF_4_A().runStdin();
    }

    public void runStdin() {
        try (PrintWriter out = new PrintWriter(System.out)) {
            solve(new Scanner(System.in), out);
        }
    }
}
