package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Comprehensive test suite for MatrixTemplates.java
 * 
 * Tests all 17 matrix algorithms with edge cases, boundary conditions,
 * and performance validation for big tech coding interviews.
 */
class MatrixTemplatesTest {

    // ============================================================================
    // ROTATE MATRIX TESTS
    // ============================================================================
    
    @Test
    void testRotate90Clockwise_3x3() {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] expected = {{7,4,1},{8,5,2},{9,6,3}};
        
        MatrixTemplates.rotate90Clockwise(matrix);
        assertArrayEquals(expected, matrix);
    }
    
    @Test
    void testRotate90Clockwise_4x4() {
        int[][] matrix = {{5,1,9,11},{2,4,8,10},{13,3,6,7},{15,14,12,16}};
        int[][] expected = {{15,13,2,5},{14,3,4,1},{12,6,8,9},{16,7,10,11}};
        
        MatrixTemplates.rotate90Clockwise(matrix);
        assertArrayEquals(expected, matrix);
    }
    
    @Test 
    void testRotate90Clockwise_1x1() {
        int[][] matrix = {{1}};
        int[][] expected = {{1}};
        
        MatrixTemplates.rotate90Clockwise(matrix);
        assertArrayEquals(expected, matrix);
    }
    
    @Test
    void testRotate90Clockwise_2x2() {
        int[][] matrix = {{1,2},{3,4}};
        int[][] expected = {{3,1},{4,2}};
        
        MatrixTemplates.rotate90Clockwise(matrix);
        assertArrayEquals(expected, matrix);
    }

    @Test
    void testRotate90CounterClockwise_3x3() {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] expected = {{3,6,9},{2,5,8},{1,4,7}};
        
        MatrixTemplates.rotate90CounterClockwise(matrix);
        assertArrayEquals(expected, matrix);
    }

    @Test
    void testRotate180_3x3() {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] expected = {{9,8,7},{6,5,4},{3,2,1}};
        
        MatrixTemplates.rotate180(matrix);
        assertArrayEquals(expected, matrix);
    }
    
    @Test
    void testRotate180_4x4() {
        int[][] matrix = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
        int[][] expected = {{16,15,14,13},{12,11,10,9},{8,7,6,5},{4,3,2,1}};
        
        MatrixTemplates.rotate180(matrix);
        assertArrayEquals(expected, matrix);
    }

    // ============================================================================
    // SPIRAL MATRIX TESTS
    // ============================================================================

    @Test
    void testSpiralOrder_3x3() {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        List<Integer> expected = Arrays.asList(1,2,3,6,9,8,7,4,5);
        
        assertEquals(expected, MatrixTemplates.spiralOrder(matrix));
    }
    
    @Test
    void testSpiralOrder_3x4() {
        int[][] matrix = {{1,2,3,4},{5,6,7,8},{9,10,11,12}};
        List<Integer> expected = Arrays.asList(1,2,3,4,8,12,11,10,9,5,6,7);
        
        assertEquals(expected, MatrixTemplates.spiralOrder(matrix));
    }
    
    @Test
    void testSpiralOrder_1x1() {
        int[][] matrix = {{1}};
        List<Integer> expected = Arrays.asList(1);
        
        assertEquals(expected, MatrixTemplates.spiralOrder(matrix));
    }
    
    @Test
    void testSpiralOrder_1xN() {
        int[][] matrix = {{1,2,3,4,5}};
        List<Integer> expected = Arrays.asList(1,2,3,4,5);
        
        assertEquals(expected, MatrixTemplates.spiralOrder(matrix));
    }
    
    @Test
    void testSpiralOrder_Nx1() {
        int[][] matrix = {{1},{2},{3},{4}};
        List<Integer> expected = Arrays.asList(1,2,3,4);
        
        assertEquals(expected, MatrixTemplates.spiralOrder(matrix));
    }
    
    @Test
    void testSpiralOrder_Empty() {
        int[][] matrix = {};
        List<Integer> expected = new ArrayList<>();
        
        assertEquals(expected, MatrixTemplates.spiralOrder(matrix));
    }

    @Test
    void testGenerateMatrix_3x3() {
        int[][] expected = {{1,2,3},{8,9,4},{7,6,5}};
        assertArrayEquals(expected, MatrixTemplates.generateMatrix(3));
    }
    
    @Test
    void testGenerateMatrix_1x1() {
        int[][] expected = {{1}};
        assertArrayEquals(expected, MatrixTemplates.generateMatrix(1));
    }
    
    @Test
    void testGenerateMatrix_4x4() {
        int[][] expected = {{1,2,3,4},{12,13,14,5},{11,16,15,6},{10,9,8,7}};
        assertArrayEquals(expected, MatrixTemplates.generateMatrix(4));
    }

    @Test
    void testSpiralMatrixIII_1x4() {
        // 1x4 matrix starting at (0,0) should visit: (0,0) -> (0,1) -> (0,2) -> (0,3)
        int[][] expected = {{0,0},{0,1},{0,2},{0,3}};
        assertArrayEquals(expected, MatrixTemplates.spiralMatrixIII(1, 4, 0, 0));
    }

    // ============================================================================
    // SEARCH IN 2D MATRIX TESTS  
    // ============================================================================

    @Test
    void testSearchMatrix_Found() {
        // Matrix sorted row-wise and first integer of each row > last integer of previous row
        int[][] matrix = {{1,3,5,7},{10,11,16,20},{23,30,34,60}};
        assertTrue(MatrixTemplates.searchMatrix(matrix, 3));
        assertTrue(MatrixTemplates.searchMatrix(matrix, 11));
        assertTrue(MatrixTemplates.searchMatrix(matrix, 1));
        assertTrue(MatrixTemplates.searchMatrix(matrix, 60));
    }
    
    @Test
    void testSearchMatrix_NotFound() {
        int[][] matrix = {{1,4,7,11},{2,5,8,12},{3,6,9,16}};
        assertFalse(MatrixTemplates.searchMatrix(matrix, 13));
        assertFalse(MatrixTemplates.searchMatrix(matrix, 0));
        assertFalse(MatrixTemplates.searchMatrix(matrix, 17));
    }
    
    @Test
    void testSearchMatrix_Empty() {
        int[][] matrix = {};
        assertFalse(MatrixTemplates.searchMatrix(matrix, 1));
    }
    
    @Test
    void testSearchMatrix_SingleElement() {
        int[][] matrix = {{1}};
        assertTrue(MatrixTemplates.searchMatrix(matrix, 1));
        assertFalse(MatrixTemplates.searchMatrix(matrix, 2));
    }

    @Test
    void testSearchMatrixII_Found() {
        int[][] matrix = {{1,4,7,11,15},{2,5,8,12,19},{3,6,9,16,22},{10,13,14,17,24},{18,21,23,26,30}};
        assertTrue(MatrixTemplates.searchMatrixII(matrix, 5));
        assertTrue(MatrixTemplates.searchMatrixII(matrix, 14));
        assertTrue(MatrixTemplates.searchMatrixII(matrix, 1));
        assertTrue(MatrixTemplates.searchMatrixII(matrix, 30));
    }
    
    @Test  
    void testSearchMatrixII_NotFound() {
        int[][] matrix = {{1,4,7,11,15},{2,5,8,12,19},{3,6,9,16,22},{10,13,14,17,24},{18,21,23,26,30}};
        assertFalse(MatrixTemplates.searchMatrixII(matrix, 20));
        assertFalse(MatrixTemplates.searchMatrixII(matrix, 0));
        assertFalse(MatrixTemplates.searchMatrixII(matrix, 31));
    }

    // ============================================================================
    // SET MATRIX ZEROES TESTS
    // ============================================================================

    @Test
    void testSetZeroes_3x3() {
        int[][] matrix = {{1,1,1},{1,0,1},{1,1,1}};
        int[][] expected = {{1,0,1},{0,0,0},{1,0,1}};
        
        MatrixTemplates.setZeroes(matrix);
        assertArrayEquals(expected, matrix);
    }
    
    @Test
    void testSetZeroes_FirstRowAndColumn() {
        int[][] matrix = {{0,1,2,0},{3,4,5,2},{1,3,1,5}};
        int[][] expected = {{0,0,0,0},{0,4,5,0},{0,3,1,0}};
        
        MatrixTemplates.setZeroes(matrix);
        assertArrayEquals(expected, matrix);
    }
    
    @Test
    void testSetZeroes_AllZeroes() {
        int[][] matrix = {{0,0},{0,0}};
        int[][] expected = {{0,0},{0,0}};
        
        MatrixTemplates.setZeroes(matrix);
        assertArrayEquals(expected, matrix);
    }

    @Test
    void testSetZeroes_NoZeroes() {
        int[][] matrix = {{1,2,3},{4,5,6}};
        int[][] expected = {{1,2,3},{4,5,6}};
        
        MatrixTemplates.setZeroes(matrix);
        assertArrayEquals(expected, matrix);
    }

    // ============================================================================
    // DIAGONAL TRAVERSE TESTS
    // ============================================================================

    @Test
    void testDiagonalOrder_3x3() {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        int[] expected = {1,2,4,7,5,3,6,8,9};
        
        assertArrayEquals(expected, MatrixTemplates.findDiagonalOrder(matrix));
    }
    
    @Test
    void testDiagonalOrder_Empty() {
        int[][] matrix = {};
        int[] expected = {};
        
        assertArrayEquals(expected, MatrixTemplates.findDiagonalOrder(matrix));
    }
    
    @Test
    void testDiagonalOrder_1x1() {
        int[][] matrix = {{5}};
        int[] expected = {5};
        
        assertArrayEquals(expected, MatrixTemplates.findDiagonalOrder(matrix));
    }

    // ============================================================================
    // GAME OF LIFE TESTS
    // ============================================================================

    @Test
    void testGameOfLife_Oscillator() {
        int[][] board = {{0,1,0},{0,1,0},{0,1,0}};
        int[][] expected = {{0,0,0},{1,1,1},{0,0,0}};
        
        MatrixTemplates.gameOfLife(board);
        assertArrayEquals(expected, board);
    }
    
    @Test
    void testGameOfLife_Block() {
        int[][] board = {{1,1},{1,1}};
        int[][] expected = {{1,1},{1,1}};
        
        MatrixTemplates.gameOfLife(board);
        assertArrayEquals(expected, board);
    }

    // ============================================================================
    // VALID SUDOKU TESTS
    // ============================================================================

    @Test
    void testValidSudoku_Valid() {
        char[][] board = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        
        assertTrue(MatrixTemplates.isValidSudoku(board));
    }
    
    @Test
    void testValidSudoku_Invalid() {
        char[][] board = {
            {'8','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'}, // duplicate 8 in column 0
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        
        assertFalse(MatrixTemplates.isValidSudoku(board));
    }

    // ============================================================================
    // FLOOD FILL TESTS
    // ============================================================================

    @Test
    void testFloodFill_Basic() {
        int[][] image = {{1,1,1},{1,1,0},{1,0,1}};
        int[][] expected = {{2,2,2},{2,2,0},{2,0,1}};
        
        assertArrayEquals(expected, MatrixTemplates.floodFill(image, 1, 1, 2));
    }
    
    @Test
    void testFloodFill_SameColor() {
        int[][] image = {{0,0,0},{0,1,1}};
        int[][] expected = {{0,0,0},{0,1,1}};
        
        assertArrayEquals(expected, MatrixTemplates.floodFill(image, 1, 1, 1));
    }

    // ============================================================================
    // NUMBER OF ISLANDS TESTS
    // ============================================================================

    @Test
    void testNumIslands_Basic() {
        char[][] grid = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        assertEquals(1, MatrixTemplates.numIslands(grid));
    }
    
    @Test
    void testNumIslands_Multiple() {
        char[][] grid = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        assertEquals(3, MatrixTemplates.numIslands(grid));
    }
    
    @Test
    void testNumIslands_Empty() {
        char[][] grid = {};
        assertEquals(0, MatrixTemplates.numIslands(grid));
    }

    // ============================================================================
    // TRANSPOSE TESTS
    // ============================================================================

    @Test
    void testTranspose_Square() {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] expected = {{1,4,7},{2,5,8},{3,6,9}};
        
        assertArrayEquals(expected, MatrixTemplates.transpose(matrix));
    }
    
    @Test
    void testTranspose_Rectangle() {
        int[][] matrix = {{1,2,3,4},{5,6,7,8}};
        int[][] expected = {{1,5},{2,6},{3,7},{4,8}};
        
        assertArrayEquals(expected, MatrixTemplates.transpose(matrix));
    }

    // ============================================================================
    // SURROUNDED REGIONS TESTS
    // ============================================================================

    @Test
    void testSolve_Basic() {
        char[][] board = {
            {'X','X','X','X'},
            {'X','O','O','X'},
            {'X','X','O','X'},
            {'X','O','X','X'}
        };
        char[][] expected = {
            {'X','X','X','X'},
            {'X','X','X','X'},
            {'X','X','X','X'},
            {'X','O','X','X'}
        };
        
        MatrixTemplates.solve(board);
        assertArrayEquals(expected, board);
    }

    // ============================================================================
    // PACIFIC ATLANTIC TESTS
    // ============================================================================

    @Test
    void testPacificAtlantic_Basic() {
        int[][] heights = {
            {1,2,2,3,5},
            {3,2,3,4,4},
            {2,4,5,3,1},
            {6,7,1,4,5},
            {5,1,1,2,4}
        };
        
        List<List<Integer>> result = MatrixTemplates.pacificAtlantic(heights);
        Set<List<Integer>> resultSet = new HashSet<>(result);
        
        // Expected cells that can reach both oceans
        assertTrue(resultSet.contains(Arrays.asList(0, 4)));
        assertTrue(resultSet.contains(Arrays.asList(1, 3)));
        assertTrue(resultSet.contains(Arrays.asList(1, 4)));
        assertTrue(resultSet.contains(Arrays.asList(2, 2)));
        assertTrue(resultSet.contains(Arrays.asList(3, 0)));
        assertTrue(resultSet.contains(Arrays.asList(3, 1)));
        assertTrue(resultSet.contains(Arrays.asList(4, 0)));
    }

    // ============================================================================
    // PERFORMANCE TESTS
    // ============================================================================

    @Test
    void testPerformance_LargeMatrix() {
        // Test with 1000x1000 matrix for performance
        int n = 1000;
        int[][] matrix = new int[n][n];
        
        // Fill with values
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = i * n + j + 1;
            }
        }
        
        long start = System.currentTimeMillis();
        List<Integer> result = MatrixTemplates.spiralOrder(matrix);
        long end = System.currentTimeMillis();
        
        assertEquals(n * n, result.size());
        assertTrue((end - start) < 1000, "Performance test failed - took too long");
    }
    
    // ============================================================================
    // EDGE CASE STRESS TESTS
    // ============================================================================
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 10, 50, 100})
    void testGenerateMatrix_StressTest(int n) {
        int[][] result = MatrixTemplates.generateMatrix(n);
        
        // Verify dimensions
        assertEquals(n, result.length);
        assertEquals(n, result[0].length);
        
        // Verify all numbers 1 to n^2 are present exactly once
        boolean[] seen = new boolean[n * n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = result[i][j];
                assertTrue(val >= 1 && val <= n * n, "Value out of range: " + val);
                assertFalse(seen[val], "Duplicate value: " + val);
                seen[val] = true;
            }
        }
        
        // Verify all numbers were seen
        for (int i = 1; i <= n * n; i++) {
            assertTrue(seen[i], "Missing value: " + i);
        }
    }
}