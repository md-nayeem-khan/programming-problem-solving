package com.cp.algorithms;

/**
 * Specific test for the sum of divisors fix
 */
public class SumDivisorsTest {
    public static void main(String[] args) {
        System.out.println("=== Sum of Divisors Validation ===");
        
        // Test cases with known results
        long[][] testCases = {
            {1, 1},      // σ(1) = 1
            {2, 3},      // σ(2) = 1 + 2 = 3
            {3, 4},      // σ(3) = 1 + 3 = 4
            {4, 7},      // σ(4) = 1 + 2 + 4 = 7
            {6, 12},     // σ(6) = 1 + 2 + 3 + 6 = 12
            {8, 15},     // σ(8) = 1 + 2 + 4 + 8 = 15
            {12, 28},    // σ(12) = 1 + 2 + 3 + 4 + 6 + 12 = 28
            {16, 31},    // σ(16) = 1 + 2 + 4 + 8 + 16 = 31
            {20, 42},    // σ(20) = 1 + 2 + 4 + 5 + 10 + 20 = 42
            {24, 60},    // σ(24) = 1 + 2 + 3 + 4 + 6 + 8 + 12 + 24 = 60
            {30, 72}     // σ(30) = 1 + 2 + 3 + 5 + 6 + 10 + 15 + 30 = 72
        };
        
        int passed = 0;
        int total = testCases.length;
        
        for (long[] testCase : testCases) {
            long n = testCase[0];
            long expected = testCase[1];
            
            try {
                long result = NumberTheoryTemplates.MultiplicativeFunctions.sumDivisors(n);
                
                if (result == expected) {
                    System.out.println("[PASS] σ(" + n + ") = " + result + " (expected " + expected + ")");
                    passed++;
                } else {
                    System.out.println("[FAIL] σ(" + n + ") = " + result + " (expected " + expected + ")");
                }
            } catch (Exception e) {
                System.out.println("[ERROR] σ(" + n + ") threw exception: " + e.getMessage());
            }
        }
        
        // Test a larger number to ensure no overflow
        try {
            long largeN = 1000;
            long result = NumberTheoryTemplates.MultiplicativeFunctions.sumDivisors(largeN);
            System.out.println("[INFO] σ(" + largeN + ") = " + result + " (large number test)");
        } catch (Exception e) {
            System.out.println("[ERROR] Large number test failed: " + e.getMessage());
        }
        
        System.out.println("\n=== Results ===");
        System.out.println("Passed: " + passed + "/" + total);
        System.out.println("Success Rate: " + (100.0 * passed / total) + "%");
        
        if (passed == total) {
            System.out.println("[SUCCESS] All sum of divisors tests passed!");
        } else {
            System.out.println("[FAILURE] Some tests failed.");
        }
    }
}