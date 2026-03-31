package com.cp.testcases;

import com.cp.problems.BaseSolution;

import java.io.*;
import java.util.List;

/**
 * SolutionRunner — executes Codeforces-style solutions against loaded test cases
 * and compares output. Prints a pass/fail summary.
 *
 * Usage in a JUnit test:
 *   @Test
 *   void testCF1900A() {
 *       SolutionRunner.runAndAssert(new CF_1900_A(), "codeforces/1900_A.txt");
 *   }
 */
public class SolutionRunner {

    /**
     * Run solution against all raw test cases in file. Throws AssertionError on mismatch.
     */
    public static void runAndAssert(BaseSolution solution, String relPath) {
        List<String[]> cases = TestCaseLoader.loadRaw(relPath);
        int passed = 0, failed = 0;

        for (int i = 0; i < cases.size(); i++) {
            String input    = cases.get(i)[0];
            String expected = cases.get(i)[1];
            String actual   = run(solution, input);

            if (normalize(actual).equals(normalize(expected))) {
                passed++;
                System.out.printf("  [PASS] Case %d%n", i + 1);
            } else {
                failed++;
                System.out.printf("  [FAIL] Case %d%n", i + 1);
                System.out.println("    Input:    " + input.replace("\n", "\\n"));
                System.out.println("    Expected: " + expected);
                System.out.println("    Got:      " + actual);
            }
        }

        System.out.printf("%nResult: %d/%d passed%n", passed, passed + failed);
        if (failed > 0) throw new AssertionError(failed + " test case(s) failed.");
    }

    /**
     * Run solution against a single input string, return output string.
     */
    public static String run(BaseSolution solution, String input) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        java.util.Scanner scanner = new java.util.Scanner(input);
        solution.solve(scanner, pw);
        pw.flush();
        return sw.toString().trim();
    }

    private static String normalize(String s) {
        return s.trim().replaceAll("\\r\\n", "\n").replaceAll("[ \\t]+\\n", "\n");
    }
}
