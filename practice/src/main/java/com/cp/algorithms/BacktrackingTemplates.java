package com.cp.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Backtracking algorithm templates — essential for interview problems.
 *
 * Covers:
 *   1. Permutations (unique elements)
 *   2. Permutations (with duplicates)
 *   3. Combinations
 *   4. Subsets (power set)
 *   5. Subsets (with duplicates)
 *   6. N-Queens
 *   7. Sudoku Solver
 *   8. Generate Parentheses
 *   9. Word Search (grid backtracking)
 *   10. Combination Sum variants
 *
 * Time Complexity Notes:
 *   - Permutations: O(n! * n)
 *   - Combinations: O(C(n,k) * k)
 *   - Subsets: O(2^n * n)
 *   - N-Queens: O(n!)
 */
public class BacktrackingTemplates {

    // ---------------------------------------------------------------
    // 1. Permutations — all orderings of n unique elements
    // Example: [1,2,3] → [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
    // Time: O(n! * n), Space: O(n)
    // ---------------------------------------------------------------
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackPermute(nums, new ArrayList<>(), new boolean[nums.length], result);
        return result;
    }

    private static void backtrackPermute(int[] nums, List<Integer> current, 
                                         boolean[] used, List<List<Integer>> result) {
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            current.add(nums[i]);
            backtrackPermute(nums, current, used, result);
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }

    // ---------------------------------------------------------------
    // 2. Permutations with Duplicates — handle duplicate elements
    // Example: [1,1,2] → [[1,1,2],[1,2,1],[2,1,1]]
    // Time: O(n! * n), Space: O(n)
    // ---------------------------------------------------------------
    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        java.util.Arrays.sort(nums); // sort to group duplicates
        backtrackPermuteUnique(nums, new ArrayList<>(), new boolean[nums.length], result);
        return result;
    }

    private static void backtrackPermuteUnique(int[] nums, List<Integer> current,
                                               boolean[] used, List<List<Integer>> result) {
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            // Skip duplicates: if nums[i] == nums[i-1] and i-1 was not used, skip
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue;
            used[i] = true;
            current.add(nums[i]);
            backtrackPermuteUnique(nums, current, used, result);
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }

    // ---------------------------------------------------------------
    // 3. Combinations — choose k elements from n
    // Example: combine(4, 2) → [[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
    // Time: O(C(n,k) * k), Space: O(k)
    // ---------------------------------------------------------------
    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackCombine(1, n, k, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackCombine(int start, int n, int k,
                                         List<Integer> current, List<List<Integer>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        // Pruning: if remaining elements < needed elements, skip
        for (int i = start; i <= n - (k - current.size()) + 1; i++) {
            current.add(i);
            backtrackCombine(i + 1, n, k, current, result);
            current.remove(current.size() - 1);
        }
    }

    // ---------------------------------------------------------------
    // 4. Subsets (Power Set) — all possible subsets
    // Example: [1,2,3] → [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
    // Time: O(2^n * n), Space: O(n)
    // ---------------------------------------------------------------
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackSubsets(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackSubsets(int[] nums, int start,
                                         List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));
        for (int i = start; i < nums.length; i++) {
            current.add(nums[i]);
            backtrackSubsets(nums, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    // ---------------------------------------------------------------
    // 5. Subsets with Duplicates
    // Example: [1,2,2] → [[],[1],[1,2],[1,2,2],[2],[2,2]]
    // Time: O(2^n * n), Space: O(n)
    // ---------------------------------------------------------------
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        java.util.Arrays.sort(nums);
        backtrackSubsetsDup(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackSubsetsDup(int[] nums, int start,
                                            List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));
        for (int i = start; i < nums.length; i++) {
            if (i > start && nums[i] == nums[i - 1]) continue; // skip duplicates
            current.add(nums[i]);
            backtrackSubsetsDup(nums, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    // ---------------------------------------------------------------
    // 6. N-Queens — place n queens on n×n board
    // Returns list of board configurations
    // Time: O(n!), Space: O(n^2)
    // ---------------------------------------------------------------
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        char[][] board = new char[n][n];
        for (char[] row : board) java.util.Arrays.fill(row, '.');
        backtrackNQueens(board, 0, result);
        return result;
    }

    private static void backtrackNQueens(char[][] board, int row, List<List<String>> result) {
        if (row == board.length) {
            result.add(construct(board));
            return;
        }
        for (int col = 0; col < board.length; col++) {
            if (isValidQueen(board, row, col)) {
                board[row][col] = 'Q';
                backtrackNQueens(board, row + 1, result);
                board[row][col] = '.';
            }
        }
    }

    private static boolean isValidQueen(char[][] board, int row, int col) {
        int n = board.length;
        // Check column
        for (int i = 0; i < row; i++) if (board[i][col] == 'Q') return false;
        // Check diagonal \
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 'Q') return false;
        // Check diagonal /
        for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++)
            if (board[i][j] == 'Q') return false;
        return true;
    }

    private static List<String> construct(char[][] board) {
        List<String> res = new ArrayList<>();
        for (char[] row : board) res.add(new String(row));
        return res;
    }

    // ---------------------------------------------------------------
    // 7. Sudoku Solver — solve 9×9 sudoku
    // Modifies board in-place, returns true if solvable
    // Time: O(9^(n²)) worst case, Space: O(1)
    // ---------------------------------------------------------------
    public static boolean solveSudoku(char[][] board) {
        return backtrackSudoku(board);
    }

    private static boolean backtrackSudoku(char[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != '.') continue;
                for (char digit = '1'; digit <= '9'; digit++) {
                    if (isValidSudoku(board, r, c, digit)) {
                        board[r][c] = digit;
                        if (backtrackSudoku(board)) return true;
                        board[r][c] = '.';
                    }
                }
                return false; // no valid digit found
            }
        }
        return true; // all cells filled
    }

    private static boolean isValidSudoku(char[][] board, int row, int col, char c) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == c) return false; // check row
            if (board[i][col] == c) return false; // check column
            if (board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == c) return false; // check box
        }
        return true;
    }

    // ---------------------------------------------------------------
    // 8. Generate Parentheses — all valid n pairs of parentheses
    // Example: n=3 → ["((()))","(()())","(())()","()(())","()()()"]
    // Time: O(4^n / sqrt(n)) (Catalan number), Space: O(n)
    // ---------------------------------------------------------------
    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrackParens(n, 0, 0, new StringBuilder(), result);
        return result;
    }

    private static void backtrackParens(int n, int open, int close,
                                        StringBuilder current, List<String> result) {
        if (current.length() == 2 * n) {
            result.add(current.toString());
            return;
        }
        if (open < n) {
            current.append('(');
            backtrackParens(n, open + 1, close, current, result);
            current.deleteCharAt(current.length() - 1);
        }
        if (close < open) {
            current.append(')');
            backtrackParens(n, open, close + 1, current, result);
            current.deleteCharAt(current.length() - 1);
        }
    }

    // ---------------------------------------------------------------
    // 9. Word Search in Grid — find word in 2D board
    // Time: O(m*n * 4^L) where L = word length, Space: O(L)
    // ---------------------------------------------------------------
    public static boolean existWordInGrid(char[][] board, String word) {
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++)
                if (dfsWordSearch(board, word, r, c, 0)) return true;
        return false;
    }

    private static boolean dfsWordSearch(char[][] board, String word, int r, int c, int idx) {
        if (idx == word.length()) return true;
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
        if (board[r][c] != word.charAt(idx)) return false;

        char temp = board[r][c];
        board[r][c] = '#'; // mark visited
        boolean found = dfsWordSearch(board, word, r + 1, c, idx + 1) ||
                        dfsWordSearch(board, word, r - 1, c, idx + 1) ||
                        dfsWordSearch(board, word, r, c + 1, idx + 1) ||
                        dfsWordSearch(board, word, r, c - 1, idx + 1);
        board[r][c] = temp; // restore
        return found;
    }

    // ---------------------------------------------------------------
    // 10. Combination Sum (candidates can be reused)
    // Example: candidates=[2,3,6,7], target=7 → [[2,2,3],[7]]
    // Time: O(2^target), Space: O(target)
    // ---------------------------------------------------------------
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        java.util.Arrays.sort(candidates);
        backtrackCombinationSum(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackCombinationSum(int[] candidates, int target, int start,
                                                List<Integer> current, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > target) break; // pruning
            current.add(candidates[i]);
            backtrackCombinationSum(candidates, target - candidates[i], i, current, result);
            current.remove(current.size() - 1);
        }
    }

    // ---------------------------------------------------------------
    // 10b. Combination Sum II (each candidate used once, array has duplicates)
    // Example: candidates=[10,1,2,7,6,1,5], target=8 → [[1,1,6],[1,2,5],[1,7],[2,6]]
    // Time: O(2^n), Space: O(n)
    // ---------------------------------------------------------------
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        java.util.Arrays.sort(candidates);
        backtrackCombinationSum2(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackCombinationSum2(int[] candidates, int target, int start,
                                                 List<Integer> current, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > target) break;
            if (i > start && candidates[i] == candidates[i - 1]) continue; // skip duplicates
            current.add(candidates[i]);
            backtrackCombinationSum2(candidates, target - candidates[i], i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    // ---------------------------------------------------------------
    // 11. Palindrome Partitioning — partition string into palindromes
    // Example: "aab" → [["a","a","b"],["aa","b"]]
    // Time: O(n * 2^n), Space: O(n)
    // ---------------------------------------------------------------
    public static List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        backtrackPartition(s, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackPartition(String s, int start,
                                           List<String> current, List<List<String>> result) {
        if (start == s.length()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int end = start; end < s.length(); end++) {
            if (isPalindrome(s, start, end)) {
                current.add(s.substring(start, end + 1));
                backtrackPartition(s, end + 1, current, result);
                current.remove(current.size() - 1);
            }
        }
    }

    private static boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) return false;
        }
        return true;
    }
}
