package com.cp.algorithms;

/**
 * Simple test runner to validate MatrixTemplates algorithms
 */
public class SimpleValidator {
    
    public static void main(String[] args) {
        System.out.println("=== MatrixTemplates Algorithm Validation ===\n");
        
        boolean allPassed = true;
        
        // Test 1: Rotate 90 Clockwise
        System.out.println("Testing rotate90Clockwise...");
        int[][] matrix1 = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] expected1 = {{7,4,1},{8,5,2},{9,6,3}};
        MatrixTemplates.rotate90Clockwise(matrix1);
        boolean test1 = arraysEqual(matrix1, expected1);
        System.out.println(test1 ? "PASS" : "FAIL");
        allPassed &= test1;
        
        // Test 2: Spiral Order
        System.out.println("Testing spiralOrder...");
        int[][] matrix2 = {{1,2,3},{4,5,6},{7,8,9}};
        java.util.List<Integer> result2 = MatrixTemplates.spiralOrder(matrix2);
        java.util.List<Integer> expected2 = java.util.Arrays.asList(1,2,3,6,9,8,7,4,5);
        boolean test2 = result2.equals(expected2);
        System.out.println(test2 ? "PASS" : "FAIL");
        allPassed &= test2;
        
        // Test 3: Generate Matrix (Critical Test)
        System.out.println("Testing generateMatrix...");
        int[][] result3 = MatrixTemplates.generateMatrix(3);
        int[][] expected3 = {{1,2,3},{8,9,4},{7,6,5}};
        boolean test3 = arraysEqual(result3, expected3);
        System.out.println(test3 ? "PASS" : "FAIL - CRITICAL BUG FOUND");
        allPassed &= test3;
        
        // Test 4: Search Matrix II
        System.out.println("Testing searchMatrixII...");
        int[][] matrix4 = {{1,4,7,11},{2,5,8,12},{3,6,9,16}};
        boolean found = MatrixTemplates.searchMatrixII(matrix4, 5);
        boolean notFound = !MatrixTemplates.searchMatrixII(matrix4, 13);
        boolean test4 = found && notFound;
        System.out.println(test4 ? "PASS" : "FAIL");
        allPassed &= test4;
        
        // Test 5: Number of Islands
        System.out.println("Testing numIslands...");
        char[][] grid = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        int islands = MatrixTemplates.numIslands(grid);
        boolean test5 = (islands == 3);
        System.out.println(test5 ? "PASS" : "FAIL - Expected 3, got " + islands);
        allPassed &= test5;
        
        System.out.println("\n=== VALIDATION RESULTS ===");
        if (allPassed) {
            System.out.println("SUCCESS: All critical algorithms validated!");
        } else {
            System.out.println("WARNING: Some tests failed - review needed!");
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