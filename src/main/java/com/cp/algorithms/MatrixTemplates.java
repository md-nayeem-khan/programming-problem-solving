package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Matrix Algorithm Templates for coding interviews.
 * 
 * Coverage:
 * - Rotate Matrix (90, 180, 270 degrees)
 * - Spiral Matrix Traversal (I, II, III)
 * - Search in 2D Matrix (I, II)
 * - Set Matrix Zeroes
 * - Diagonal Traverse
 * - Game of Life
 * - Valid Sudoku
 * - Flood Fill
 * - Islands Count (various)
 * - Matrix Transpose
 * 
 * Critical for: Meta, Amazon, Microsoft
 * Frequency: ~12% of interview problems
 */
public class MatrixTemplates {

    // ============================================================================
    // ROTATE MATRIX
    // ============================================================================
    
    /**
     * Rotate Matrix 90 Degrees Clockwise (in-place).
     * 
     * Pattern: Transpose + Reverse each row
     * OR: Rotate layer by layer
     * 
     * Interview favorite: Meta, Microsoft, Amazon
     */
    @Complexity(time = "O(n²)", space = "O(1)")
    public static void rotate90Clockwise(int[][] matrix) {
        int n = matrix.length;
        
        // Transpose
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        
        // Reverse each row
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][n - 1 - j];
                matrix[i][n - 1 - j] = temp;
            }
        }
    }
    
    /**
     * Rotate Matrix 90 Degrees Counter-Clockwise.
     * Pattern: Transpose + Reverse each column
     */
    @Complexity(time = "O(n²)", space = "O(1)")
    public static void rotate90CounterClockwise(int[][] matrix) {
        int n = matrix.length;
        
        // Transpose
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        
        // Reverse each column
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n / 2; i++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - 1 - i][j];
                matrix[n - 1 - i][j] = temp;
            }
        }
    }
    
    /**
     * Rotate Matrix 180 Degrees.
     */
    @Complexity(time = "O(n²)", space = "O(1)")
    public static void rotate180(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n / 2; i++) {
            for (int j = 0; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - 1 - i][n - 1 - j];
                matrix[n - 1 - i][n - 1 - j] = temp;
            }
        }
        // Handle middle row if n is odd
        if (n % 2 == 1) {
            int mid = n / 2;
            for (int j = 0; j < n / 2; j++) {
                int temp = matrix[mid][j];
                matrix[mid][j] = matrix[mid][n - 1 - j];
                matrix[mid][n - 1 - j] = temp;
            }
        }
    }
    
    // ============================================================================
    // SPIRAL MATRIX
    // ============================================================================
    
    /**
     * Spiral Matrix I - return all elements in spiral order.
     * 
     * Pattern: Four boundaries (top, bottom, left, right), shrink after each pass.
     * 
     * Interview favorite: Meta, Amazon, Microsoft
     */
    @Complexity(time = "O(m × n)", space = "O(1)")
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix.length == 0) return result;
        
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;
        
        while (top <= bottom && left <= right) {
            // Traverse right
            for (int j = left; j <= right; j++) {
                result.add(matrix[top][j]);
            }
            top++;
            
            // Traverse down
            for (int i = top; i <= bottom; i++) {
                result.add(matrix[i][right]);
            }
            right--;
            
            // Traverse left (if still have rows)
            if (top <= bottom) {
                for (int j = right; j >= left; j--) {
                    result.add(matrix[bottom][j]);
                }
                bottom--;
            }
            
            // Traverse up (if still have columns)
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    result.add(matrix[i][left]);
                }
                left++;
            }
        }
        
        return result;
    }
    
    /**
     * Spiral Matrix II - generate n×n matrix filled 1 to n² in spiral order.
     */
    @Complexity(time = "O(n²)", space = "O(1)")
    public static int[][] generateMatrix(int n) {
        int[][] matrix = new int[n][n];
        int top = 0, bottom = n - 1, left = 0, right = n - 1;
        int num = 1;
        
        while (top <= bottom && left <= right) {
            for (int j = left; j <= right; j++) matrix[top][j] = num++;
            top++;
            
            for (int i = top; i <= bottom; i++) matrix[i][right] = num++;
            right--;
            
            // Check boundary before left traversal
            if (top <= bottom) {
                for (int j = right; j >= left; j--) matrix[bottom][j] = num++;
                bottom--;
            }
            
            // Check boundary before up traversal  
            if (left <= right) {
                for (int i = bottom; i >= top; i--) matrix[i][left] = num++;
                left++;
            }
        }
        
        return matrix;
    }
    
    /**
     * Spiral Matrix III - walk in spiral on grid, return coordinates in order visited.
     */
    @Complexity(time = "O(max(rows, cols)²)", space = "O(1)")
    public static int[][] spiralMatrixIII(int rows, int cols, int rStart, int cStart) {
        int[][] result = new int[rows * cols][2];
        int idx = 0;
        
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // right, down, left, up
        int steps = 0;
        int d = 0; // direction index
        
        result[idx++] = new int[]{rStart, cStart};
        
        while (idx < rows * cols) {
            if (d == 0 || d == 2) steps++; // increase steps after right or left
            
            for (int i = 0; i < steps; i++) {
                rStart += dirs[d][0];
                cStart += dirs[d][1];
                
                if (rStart >= 0 && rStart < rows && cStart >= 0 && cStart < cols) {
                    result[idx++] = new int[]{rStart, cStart};
                }
            }
            
            d = (d + 1) % 4;
        }
        
        return result;
    }
    
    // ============================================================================
    // SEARCH IN 2D MATRIX
    // ============================================================================
    
    /**
     * Search 2D Matrix I - matrix rows sorted, first element of row > last of previous.
     * Pattern: Treat as 1D sorted array, binary search.
     */
    @Complexity(time = "O(log(m × n))", space = "O(1)")
    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0) return false;
        
        int m = matrix.length, n = matrix[0].length;
        int left = 0, right = m * n - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midVal = matrix[mid / n][mid % n];
            
            if (midVal == target) return true;
            else if (midVal < target) left = mid + 1;
            else right = mid - 1;
        }
        
        return false;
    }
    
    /**
     * Search 2D Matrix II - each row and column sorted.
     * Pattern: Start from top-right (or bottom-left), move like BST.
     * 
     * Interview favorite: Amazon, Meta, Google
     */
    @Complexity(time = "O(m + n)", space = "O(1)")
    public static boolean searchMatrixII(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0) return false;
        
        int row = 0;
        int col = matrix[0].length - 1;
        
        while (row < matrix.length && col >= 0) {
            if (matrix[row][col] == target) {
                return true;
            } else if (matrix[row][col] > target) {
                col--;
            } else {
                row++;
            }
        }
        
        return false;
    }
    
    // ============================================================================
    // SET MATRIX ZEROES
    // ============================================================================
    
    /**
     * Set Matrix Zeroes - if element is 0, set entire row and column to 0.
     * Do it in-place with O(1) space.
     * 
     * Pattern: Use first row/column as markers.
     * 
     * Interview favorite: Meta, Microsoft
     */
    @Complexity(time = "O(m × n)", space = "O(1)")
    public static void setZeroes(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        boolean firstRowZero = false, firstColZero = false;
        
        // Check if first row should be zero
        for (int j = 0; j < n; j++) {
            if (matrix[0][j] == 0) {
                firstRowZero = true;
                break;
            }
        }
        
        // Check if first column should be zero
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) {
                firstColZero = true;
                break;
            }
        }
        
        // Use first row/col as markers
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }
        
        // Set zeroes based on markers
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        
        // Set first row
        if (firstRowZero) {
            for (int j = 0; j < n; j++) matrix[0][j] = 0;
        }
        
        // Set first column
        if (firstColZero) {
            for (int i = 0; i < m; i++) matrix[i][0] = 0;
        }
    }
    
    // ============================================================================
    // DIAGONAL TRAVERSE
    // ============================================================================
    
    /**
     * Diagonal Traverse - traverse matrix diagonally alternating direction.
     * Pattern: Track diagonal number and direction.
     */
    @Complexity(time = "O(m × n)", space = "O(1)")
    public static int[] findDiagonalOrder(int[][] matrix) {
        if (matrix.length == 0) return new int[0];
        
        int m = matrix.length, n = matrix[0].length;
        int[] result = new int[m * n];
        int idx = 0;
        
        for (int d = 0; d < m + n - 1; d++) {
            if (d % 2 == 0) {
                // Going up
                int row = Math.min(d, m - 1);
                int col = d - row;
                
                while (row >= 0 && col < n) {
                    result[idx++] = matrix[row][col];
                    row--;
                    col++;
                }
            } else {
                // Going down
                int col = Math.min(d, n - 1);
                int row = d - col;
                
                while (col >= 0 && row < m) {
                    result[idx++] = matrix[row][col];
                    row++;
                    col--;
                }
            }
        }
        
        return result;
    }
    
    // ============================================================================
    // GAME OF LIFE
    // ============================================================================
    
    /**
     * Game of Life - simulate Conway's Game of Life.
     * Update in-place using state encoding.
     * 
     * Encoding: 
     * 0 = dead->dead, 1 = live->live
     * 2 = live->dead, 3 = dead->live
     */
    @Complexity(time = "O(m × n)", space = "O(1)")
    public static void gameOfLife(int[][] board) {
        int m = board.length, n = board[0].length;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int liveNeighbors = countLiveNeighbors(board, i, j, m, n);
                
                if (board[i][j] == 1) {
                    // Live cell
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        board[i][j] = 2; // live -> dead
                    }
                } else {
                    // Dead cell
                    if (liveNeighbors == 3) {
                        board[i][j] = 3; // dead -> live
                    }
                }
            }
        }
        
        // Decode
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 2) board[i][j] = 0;
                else if (board[i][j] == 3) board[i][j] = 1;
            }
        }
    }
    
    private static int countLiveNeighbors(int[][] board, int i, int j, int m, int n) {
        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        int count = 0;
        
        for (int[] dir : dirs) {
            int ni = i + dir[0], nj = j + dir[1];
            if (ni >= 0 && ni < m && nj >= 0 && nj < n) {
                // Count 1 (live->live) and 2 (live->dead)
                if (board[ni][nj] == 1 || board[ni][nj] == 2) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    // ============================================================================
    // VALID SUDOKU
    // ============================================================================
    
    /**
     * Valid Sudoku - check if partially filled 9×9 Sudoku is valid.
     */
    @Complexity(time = "O(81) = O(1)", space = "O(1)", explanation = "Fixed 9x9 grid, but O(n²) for general n×n sudoku")
    public static boolean isValidSudoku(char[][] board) {
        Set<String> seen = new HashSet<>();
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    char num = board[i][j];
                    
                    if (!seen.add(num + " in row " + i) ||
                        !seen.add(num + " in col " + j) ||
                        !seen.add(num + " in box " + i/3 + "-" + j/3)) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    // ============================================================================
    // FLOOD FILL
    // ============================================================================
    
    /**
     * Flood Fill - fill connected region with new color (like paint bucket).
     */
    @Complexity(time = "O(m × n)", space = "O(m × n)")
    public static int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int originalColor = image[sr][sc];
        if (originalColor == newColor) return image;
        
        floodFillDFS(image, sr, sc, originalColor, newColor);
        return image;
    }
    
    private static void floodFillDFS(int[][] image, int i, int j, int original, int newColor) {
        if (i < 0 || i >= image.length || j < 0 || j >= image[0].length ||
            image[i][j] != original) {
            return;
        }
        
        image[i][j] = newColor;
        
        floodFillDFS(image, i + 1, j, original, newColor);
        floodFillDFS(image, i - 1, j, original, newColor);
        floodFillDFS(image, i, j + 1, original, newColor);
        floodFillDFS(image, i, j - 1, original, newColor);
    }
    
    // ============================================================================
    // NUMBER OF ISLANDS
    // ============================================================================
    
    /**
     * Number of Islands - count connected components of '1's.
     */
    @Complexity(time = "O(m × n)", space = "O(m × n)")
    public static int numIslands(char[][] grid) {
        if (grid.length == 0) return 0;
        
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfsMarkIsland(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    private static void dfsMarkIsland(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != '1') {
            return;
        }
        
        grid[i][j] = '0'; // Mark as visited
        dfsMarkIsland(grid, i + 1, j);
        dfsMarkIsland(grid, i - 1, j);
        dfsMarkIsland(grid, i, j + 1);
        dfsMarkIsland(grid, i, j - 1);
    }
    
    // ============================================================================
    // TRANSPOSE MATRIX
    // ============================================================================
    
    /**
     * Transpose Matrix - flip over main diagonal.
     */
    @Complexity(time = "O(m × n)", space = "O(m × n)")
    public static int[][] transpose(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] result = new int[n][m];
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        
        return result;
    }
    
    // ============================================================================
    // SURROUNDED REGIONS
    // ============================================================================
    
    /**
     * Capture Surrounded Regions - flip 'O' to 'X' if surrounded by 'X'.
     * Pattern: DFS from borders to mark non-surrounded regions.
     */
    @Complexity(time = "O(m × n)", space = "O(m × n)")
    public static void solve(char[][] board) {
        if (board.length == 0) return;
        
        int m = board.length, n = board[0].length;
        
        // Mark border-connected 'O's
        for (int i = 0; i < m; i++) {
            if (board[i][0] == 'O') markBorderConnected(board, i, 0);
            if (board[i][n-1] == 'O') markBorderConnected(board, i, n-1);
        }
        for (int j = 0; j < n; j++) {
            if (board[0][j] == 'O') markBorderConnected(board, 0, j);
            if (board[m-1][j] == 'O') markBorderConnected(board, m-1, j);
        }
        
        // Flip and restore
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O') board[i][j] = 'X';
                else if (board[i][j] == 'B') board[i][j] = 'O';
            }
        }
    }
    
    private static void markBorderConnected(char[][] board, int i, int j) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != 'O') {
            return;
        }
        
        board[i][j] = 'B'; // Mark as border-connected
        markBorderConnected(board, i + 1, j);
        markBorderConnected(board, i - 1, j);
        markBorderConnected(board, i, j + 1);
        markBorderConnected(board, i, j - 1);
    }
    
    // ============================================================================
    // PACIFIC ATLANTIC WATER FLOW
    // ============================================================================
    
    /**
     * Pacific Atlantic Water Flow - cells where water can flow to both oceans.
     */
    @Complexity(time = "O(m × n)", space = "O(m × n)")
    public static List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();
        if (heights.length == 0) return result;
        
        int m = heights.length, n = heights[0].length;
        boolean[][] pacific = new boolean[m][n];
        boolean[][] atlantic = new boolean[m][n];
        
        // DFS from Pacific borders
        for (int i = 0; i < m; i++) dfsFlow(heights, i, 0, pacific, -1);
        for (int j = 0; j < n; j++) dfsFlow(heights, 0, j, pacific, -1);
        
        // DFS from Atlantic borders
        for (int i = 0; i < m; i++) dfsFlow(heights, i, n-1, atlantic, -1);
        for (int j = 0; j < n; j++) dfsFlow(heights, m-1, j, atlantic, -1);
        
        // Find cells reachable from both
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pacific[i][j] && atlantic[i][j]) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }
        
        return result;
    }
    
    private static void dfsFlow(int[][] heights, int i, int j, boolean[][] visited, int prevHeight) {
        if (i < 0 || i >= heights.length || j < 0 || j >= heights[0].length ||
            visited[i][j] || heights[i][j] < prevHeight) {
            return;
        }
        
        visited[i][j] = true;
        dfsFlow(heights, i+1, j, visited, heights[i][j]);
        dfsFlow(heights, i-1, j, visited, heights[i][j]);
        dfsFlow(heights, i, j+1, visited, heights[i][j]);
        dfsFlow(heights, i, j-1, visited, heights[i][j]);
    }
}
