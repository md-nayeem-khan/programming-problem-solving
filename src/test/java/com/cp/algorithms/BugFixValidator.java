package com.cp.algorithms;

/**
 * Critical bug fix verification for MatrixTemplates
 * Tests specifically for the generateMatrix boundary bug and other edge cases
 */
public class BugFixValidator {
    
    public static void main(String[] args) {
        System.out.println("=== Bug Fix Verification Tests ===\n");
        
        boolean allPassed = true;
        
        // Test 1: generateMatrix boundary fix
        allPassed &= testGenerateMatrixBoundaryFix();
        
        // Test 2: Stress test edge cases that could expose the bug
        allPassed &= testGenerateMatrixStressTest();
        
        // Test 3: Verify existing functionality still works
        allPassed &= testExistingFunctionality();
        
        System.out.println("\n=== BUG FIX VERIFICATION RESULTS ===");
        if (allPassed) {
            System.out.println("SUCCESS: All bug fixes verified - code is now robust!");
        } else {
            System.out.println("FAILURE: Bug fixes introduced new issues!");
        }
    }
    
    private static boolean testGenerateMatrixBoundaryFix() {
        System.out.println("Testing generateMatrix boundary fix...");
        
        boolean allPassed = true;
        
        // Test the specific cases that could trigger the boundary bug
        for (int n = 1; n <= 10; n++) {
            System.out.println("  Testing n=" + n + "...");
            
            int[][] result = MatrixTemplates.generateMatrix(n);
            
            // Verify dimensions
            if (result.length != n || result[0].length != n) {
                System.out.println("    FAIL: Wrong dimensions for n=" + n);
                allPassed = false;
                continue;
            }
            
            // Verify all numbers 1 to n^2 are present exactly once
            boolean[] seen = new boolean[n * n + 1];
            boolean hasError = false;
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int val = result[i][j];
                    if (val < 1 || val > n * n) {
                        System.out.println("    FAIL: Value " + val + " out of range at (" + i + "," + j + ")");
                        hasError = true;
                    } else if (seen[val]) {
                        System.out.println("    FAIL: Duplicate value " + val + " at (" + i + "," + j + ")");
                        hasError = true;
                    } else {
                        seen[val] = true;
                    }
                }
            }
            
            // Check for missing values
            for (int i = 1; i <= n * n; i++) {
                if (!seen[i]) {
                    System.out.println("    FAIL: Missing value " + i + " for n=" + n);
                    hasError = true;
                }
            }
            
            if (hasError) {
                allPassed = false;
                // Print the matrix for debugging
                System.out.println("    Matrix for n=" + n + ":");
                printMatrix(result);
            } else {
                System.out.println("    PASS: n=" + n + " correct");
            }
        }
        
        System.out.println(allPassed ? "PASS: Boundary fix verified" : "FAIL: Boundary issues remain");
        return allPassed;
    }
    
    private static boolean testGenerateMatrixStressTest() {
        System.out.println("Testing generateMatrix stress scenarios...");
        
        // Test scenarios that are most likely to expose boundary bugs
        int[] testSizes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 50};
        
        for (int n : testSizes) {
            int[][] result = MatrixTemplates.generateMatrix(n);
            
            // Quick validation - check corners and center
            if (result[0][0] != 1) {
                System.out.println("  FAIL: First element not 1 for n=" + n);
                return false;
            }
            
            // For n=2, last element should be at position [1][1] with value 3
            // For general n, we need to verify all values are 1 to n^2
            boolean[] seen = new boolean[n * n + 1];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int val = result[i][j];
                    if (val < 1 || val > n * n || seen[val]) {
                        System.out.println("  FAIL: Invalid or duplicate value " + val + " at (" + i + "," + j + ") for n=" + n);
                        return false;
                    }
                    seen[val] = true;
                }
            }
        }
        
        System.out.println("PASS: Stress test completed");
        return true;
    }
    
    private static boolean testExistingFunctionality() {
        System.out.println("Testing existing functionality still works...");
        
        // Test the key algorithms to ensure fixes didn't break anything
        
        // Test 1: spiralOrder
        int[][] matrix1 = {{1,2,3},{4,5,6},{7,8,9}};
        java.util.List<Integer> result1 = MatrixTemplates.spiralOrder(matrix1);
        java.util.List<Integer> expected1 = java.util.Arrays.asList(1,2,3,6,9,8,7,4,5);
        boolean test1 = result1.equals(expected1);
        
        // Test 2: rotate90Clockwise
        int[][] matrix2 = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] expected2 = {{7,4,1},{8,5,2},{9,6,3}};
        MatrixTemplates.rotate90Clockwise(matrix2);
        boolean test2 = arraysEqual(matrix2, expected2);
        
        // Test 3: searchMatrixII
        int[][] matrix3 = {{1,4,7,11},{2,5,8,12},{3,6,9,16}};
        boolean test3 = MatrixTemplates.searchMatrixII(matrix3, 5) && !MatrixTemplates.searchMatrixII(matrix3, 13);
        
        boolean allPassed = test1 && test2 && test3;
        System.out.println(allPassed ? "PASS: Existing functionality preserved" : "FAIL: Existing functionality broken");
        
        if (!test1) System.out.println("  - spiralOrder broken");
        if (!test2) System.out.println("  - rotate90Clockwise broken");
        if (!test3) System.out.println("  - searchMatrixII broken");
        
        return allPassed;
    }
    
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.printf("%3d ", val);
            }
            System.out.println();
        }
    }
    
    private static boolean arraysEqual(int[][] a, int[][] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i].length != b[i].length) return false;
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] != b[i][j]) return false;
            }
        }
        return true;
    }
}