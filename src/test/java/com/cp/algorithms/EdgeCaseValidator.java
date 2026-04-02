package com.cp.algorithms;

/**
 * Edge case and bug detection tests for MatrixTemplates
 */
public class EdgeCaseValidator {
    
    public static void main(String[] args) {
        System.out.println("=== Edge Case and Bug Detection Tests ===\n");
        
        boolean allPassed = true;
        
        // Test 1: generateMatrix edge cases (where I suspect bugs)
        allPassed &= testGenerateMatrixEdgeCases();
        
        // Test 2: Empty matrix handling
        allPassed &= testEmptyMatrixHandling();
        
        // Test 3: Single element matrices
        allPassed &= testSingleElementMatrices();
        
        // Test 4: Large matrix performance
        allPassed &= testLargeMatrixPerformance();
        
        // Test 5: Boundary conditions
        allPassed &= testBoundaryConditions();
        
        System.out.println("\n=== EDGE CASE RESULTS ===");
        if (allPassed) {
            System.out.println("SUCCESS: All edge cases handled correctly!");
        } else {
            System.out.println("WARNING: Edge case issues found!");
        }
    }
    
    private static boolean testGenerateMatrixEdgeCases() {
        System.out.println("Testing generateMatrix edge cases...");
        
        // Test n=1
        int[][] result1 = MatrixTemplates.generateMatrix(1);
        boolean test1 = (result1.length == 1 && result1[0].length == 1 && result1[0][0] == 1);
        
        // Test n=2 
        int[][] result2 = MatrixTemplates.generateMatrix(2);
        int[][] expected2 = {{1,2},{4,3}};
        boolean test2 = arraysEqual(result2, expected2);
        
        // Test n=4 (potential infinite loop issue)
        int[][] result4 = MatrixTemplates.generateMatrix(4);
        boolean test4 = (result4.length == 4 && result4[0].length == 4);
        
        // Verify all numbers 1-16 present
        boolean[] seen = new boolean[17];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int val = result4[i][j];
                if (val >= 1 && val <= 16) {
                    seen[val] = true;
                }
            }
        }
        boolean allNumsPresent = true;
        for (int i = 1; i <= 16; i++) {
            if (!seen[i]) allNumsPresent = false;
        }
        
        boolean passed = test1 && test2 && test4 && allNumsPresent;
        System.out.println(passed ? "PASS" : "FAIL");
        
        if (!test1) System.out.println("  - n=1 test failed");
        if (!test2) System.out.println("  - n=2 test failed");
        if (!test4) System.out.println("  - n=4 dimensions failed");
        if (!allNumsPresent) System.out.println("  - n=4 missing numbers");
        
        return passed;
    }
    
    private static boolean testEmptyMatrixHandling() {
        System.out.println("Testing empty matrix handling...");
        
        // Test spiralOrder with empty matrix
        int[][] empty = {};
        java.util.List<Integer> result1 = MatrixTemplates.spiralOrder(empty);
        boolean test1 = result1.isEmpty();
        
        // Test findDiagonalOrder with empty matrix
        int[] result2 = MatrixTemplates.findDiagonalOrder(empty);
        boolean test2 = (result2.length == 0);
        
        // Test searchMatrix with empty matrix
        boolean result3 = MatrixTemplates.searchMatrix(empty, 1);
        boolean test3 = !result3;
        
        boolean passed = test1 && test2 && test3;
        System.out.println(passed ? "PASS" : "FAIL");
        return passed;
    }
    
    private static boolean testSingleElementMatrices() {
        System.out.println("Testing single element matrices...");
        
        // Test rotate90Clockwise 1x1
        int[][] matrix1 = {{5}};
        MatrixTemplates.rotate90Clockwise(matrix1);
        boolean test1 = (matrix1[0][0] == 5);
        
        // Test spiralOrder 1x1
        int[][] matrix2 = {{7}};
        java.util.List<Integer> result2 = MatrixTemplates.spiralOrder(matrix2);
        boolean test2 = (result2.size() == 1 && result2.get(0) == 7);
        
        // Test transpose 1x1
        int[][] matrix3 = {{3}};
        int[][] result3 = MatrixTemplates.transpose(matrix3);
        boolean test3 = (result3.length == 1 && result3[0].length == 1 && result3[0][0] == 3);
        
        boolean passed = test1 && test2 && test3;
        System.out.println(passed ? "PASS" : "FAIL");
        return passed;
    }
    
    private static boolean testLargeMatrixPerformance() {
        System.out.println("Testing large matrix performance...");
        
        // Test 100x100 matrix
        int n = 100;
        long start = System.currentTimeMillis();
        int[][] result = MatrixTemplates.generateMatrix(n);
        long end = System.currentTimeMillis();
        
        boolean dimensionCheck = (result.length == n && result[0].length == n);
        boolean timeCheck = (end - start) < 1000; // Should complete in < 1 second
        
        boolean passed = dimensionCheck && timeCheck;
        System.out.println(passed ? "PASS" : "FAIL");
        
        if (!dimensionCheck) System.out.println("  - Dimension check failed");
        if (!timeCheck) System.out.println("  - Performance too slow: " + (end - start) + "ms");
        
        return passed;
    }
    
    private static boolean testBoundaryConditions() {
        System.out.println("Testing boundary conditions...");
        
        // Test 1xN matrix
        int[][] matrix1x5 = {{1,2,3,4,5}};
        java.util.List<Integer> result1 = MatrixTemplates.spiralOrder(matrix1x5);
        boolean test1 = result1.equals(java.util.Arrays.asList(1,2,3,4,5));
        
        // Test Nx1 matrix  
        int[][] matrix5x1 = {{1},{2},{3},{4},{5}};
        java.util.List<Integer> result2 = MatrixTemplates.spiralOrder(matrix5x1);
        boolean test2 = result2.equals(java.util.Arrays.asList(1,2,3,4,5));
        
        // Test rectangular matrix transpose
        int[][] rect = {{1,2,3},{4,5,6}};
        int[][] transposeResult = MatrixTemplates.transpose(rect);
        int[][] expectedTranspose = {{1,4},{2,5},{3,6}};
        boolean test3 = arraysEqual(transposeResult, expectedTranspose);
        
        boolean passed = test1 && test2 && test3;
        System.out.println(passed ? "PASS" : "FAIL");
        
        if (!test1) System.out.println("  - 1xN matrix failed");
        if (!test2) System.out.println("  - Nx1 matrix failed");  
        if (!test3) System.out.println("  - Rectangle transpose failed");
        
        return passed;
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