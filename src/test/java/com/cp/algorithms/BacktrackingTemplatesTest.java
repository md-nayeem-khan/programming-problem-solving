package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for BacktrackingTemplates
 * Tests all 11 backtracking algorithms with edge cases and stress tests
 * 
 * Coverage:
 * 1. Permutations (unique elements)
 * 2. Permutations (with duplicates)
 * 3. Combinations
 * 4. Subsets (power set)
 * 5. Subsets (with duplicates)
 * 6. N-Queens
 * 7. Sudoku Solver
 * 8. Generate Parentheses
 * 9. Word Search (grid backtracking)
 * 10. Combination Sum variants
 * 11. Palindrome Partitioning
 */
public class BacktrackingTemplatesTest {

    // ========== 1. PERMUTATIONS TESTS ==========
    
    @Nested
    @DisplayName("Permutations Tests")
    class PermutationsTests {
        
        @Test
        @DisplayName("Test permutations of [1,2,3]")
        void testBasicPermutations() {
            int[] nums = {1, 2, 3};
            List<List<Integer>> result = BacktrackingTemplates.permute(nums);
            
            assertEquals(6, result.size(), "Should have 3! = 6 permutations");
            
            List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(1, 3, 2),
                Arrays.asList(2, 1, 3),
                Arrays.asList(2, 3, 1),
                Arrays.asList(3, 1, 2),
                Arrays.asList(3, 2, 1)
            );
            
            assertTrue(containsAll(result, expected), "Should contain all permutations");
        }
        
        @Test
        @DisplayName("Test permutations of single element")
        void testSingleElementPermutation() {
            int[] nums = {1};
            List<List<Integer>> result = BacktrackingTemplates.permute(nums);
            
            assertEquals(1, result.size());
            assertEquals(Arrays.asList(1), result.get(0));
        }
        
        @Test
        @DisplayName("Test permutations of two elements")
        void testTwoElementPermutation() {
            int[] nums = {1, 2};
            List<List<Integer>> result = BacktrackingTemplates.permute(nums);
            
            assertEquals(2, result.size());
            assertTrue(result.contains(Arrays.asList(1, 2)));
            assertTrue(result.contains(Arrays.asList(2, 1)));
        }
        
        @Test
        @DisplayName("Test permutations with negative numbers")
        void testNegativeNumbers() {
            int[] nums = {-1, 0, 1};
            List<List<Integer>> result = BacktrackingTemplates.permute(nums);
            
            assertEquals(6, result.size());
        }
        
        @Test
        @DisplayName("Test permutations of 4 elements")
        void testFourElements() {
            int[] nums = {1, 2, 3, 4};
            List<List<Integer>> result = BacktrackingTemplates.permute(nums);
            
            assertEquals(24, result.size(), "Should have 4! = 24 permutations");
            
            // Verify all permutations are unique
            Set<List<Integer>> uniquePerms = new HashSet<>(result);
            assertEquals(24, uniquePerms.size(), "All permutations should be unique");
        }
    }

    // ========== 2. PERMUTATIONS WITH DUPLICATES TESTS ==========
    
    @Nested
    @DisplayName("Permutations with Duplicates Tests")
    class PermutationsWithDuplicatesTests {
        
        @Test
        @DisplayName("Test permutations of [1,1,2]")
        void testDuplicatePermutations() {
            int[] nums = {1, 1, 2};
            List<List<Integer>> result = BacktrackingTemplates.permuteUnique(nums);
            
            assertEquals(3, result.size(), "Should have 3 unique permutations");
            
            List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(1, 1, 2),
                Arrays.asList(1, 2, 1),
                Arrays.asList(2, 1, 1)
            );
            
            assertTrue(containsAll(result, expected));
        }
        
        @Test
        @DisplayName("Test permutations with all duplicates")
        void testAllDuplicates() {
            int[] nums = {1, 1, 1};
            List<List<Integer>> result = BacktrackingTemplates.permuteUnique(nums);
            
            assertEquals(1, result.size());
            assertEquals(Arrays.asList(1, 1, 1), result.get(0));
        }
        
        @Test
        @DisplayName("Test permutations of [1,2,3] with unique elements")
        void testUniqueElements() {
            int[] nums = {1, 2, 3};
            List<List<Integer>> result = BacktrackingTemplates.permuteUnique(nums);
            
            assertEquals(6, result.size());
        }
        
        @Test
        @DisplayName("Test permutations with multiple duplicates")
        void testMultipleDuplicates() {
            int[] nums = {1, 1, 2, 2};
            List<List<Integer>> result = BacktrackingTemplates.permuteUnique(nums);
            
            assertEquals(6, result.size(), "Should have 4!/(2!*2!) = 6 permutations");
            
            // Verify all are unique
            Set<List<Integer>> uniqueSet = new HashSet<>(result);
            assertEquals(result.size(), uniqueSet.size(), "All should be unique");
        }
    }

    // ========== 3. COMBINATIONS TESTS ==========
    
    @Nested
    @DisplayName("Combinations Tests")
    class CombinationsTests {
        
        @Test
        @DisplayName("Test combine(4, 2)")
        void testBasicCombinations() {
            List<List<Integer>> result = BacktrackingTemplates.combine(4, 2);
            
            assertEquals(6, result.size(), "C(4,2) = 6");
            
            List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(1, 3),
                Arrays.asList(1, 4),
                Arrays.asList(2, 3),
                Arrays.asList(2, 4),
                Arrays.asList(3, 4)
            );
            
            assertTrue(containsAll(result, expected));
        }
        
        @Test
        @DisplayName("Test combine(n, 1) - choose 1")
        void testChooseOne() {
            List<List<Integer>> result = BacktrackingTemplates.combine(5, 1);
            
            assertEquals(5, result.size());
            assertTrue(result.contains(Arrays.asList(1)));
            assertTrue(result.contains(Arrays.asList(5)));
        }
        
        @Test
        @DisplayName("Test combine(n, n) - choose all")
        void testChooseAll() {
            List<List<Integer>> result = BacktrackingTemplates.combine(3, 3);
            
            assertEquals(1, result.size());
            assertEquals(Arrays.asList(1, 2, 3), result.get(0));
        }
        
        @Test
        @DisplayName("Test combine(5, 3)")
        void testCombineFiveChooseThree() {
            List<List<Integer>> result = BacktrackingTemplates.combine(5, 3);
            
            assertEquals(10, result.size(), "C(5,3) = 10");
        }
        
        @Test
        @DisplayName("Test combinations are in ascending order")
        void testCombinationsOrder() {
            List<List<Integer>> result = BacktrackingTemplates.combine(4, 2);
            
            for (List<Integer> combination : result) {
                for (int i = 1; i < combination.size(); i++) {
                    assertTrue(combination.get(i - 1) < combination.get(i),
                            "Combinations should be in ascending order");
                }
            }
        }
    }

    // ========== 4. SUBSETS TESTS ==========
    
    @Nested
    @DisplayName("Subsets (Power Set) Tests")
    class SubsetsTests {
        
        @Test
        @DisplayName("Test subsets of [1,2,3]")
        void testBasicSubsets() {
            int[] nums = {1, 2, 3};
            List<List<Integer>> result = BacktrackingTemplates.subsets(nums);
            
            assertEquals(8, result.size(), "Should have 2^3 = 8 subsets");
            
            assertTrue(result.contains(Arrays.asList()));
            assertTrue(result.contains(Arrays.asList(1)));
            assertTrue(result.contains(Arrays.asList(2)));
            assertTrue(result.contains(Arrays.asList(3)));
            assertTrue(result.contains(Arrays.asList(1, 2)));
            assertTrue(result.contains(Arrays.asList(1, 3)));
            assertTrue(result.contains(Arrays.asList(2, 3)));
            assertTrue(result.contains(Arrays.asList(1, 2, 3)));
        }
        
        @Test
        @DisplayName("Test subsets of single element")
        void testSingleElementSubsets() {
            int[] nums = {1};
            List<List<Integer>> result = BacktrackingTemplates.subsets(nums);
            
            assertEquals(2, result.size());
            assertTrue(result.contains(Arrays.asList()));
            assertTrue(result.contains(Arrays.asList(1)));
        }
        
        @Test
        @DisplayName("Test subsets of empty array")
        void testEmptySubsets() {
            int[] nums = {};
            List<List<Integer>> result = BacktrackingTemplates.subsets(nums);
            
            assertEquals(1, result.size());
            assertTrue(result.contains(Arrays.asList()));
        }
        
        @Test
        @DisplayName("Test subsets count for various sizes")
        void testSubsetsCount() {
            for (int n = 0; n <= 5; n++) {
                int[] nums = new int[n];
                for (int i = 0; i < n; i++) nums[i] = i + 1;
                
                List<List<Integer>> result = BacktrackingTemplates.subsets(nums);
                assertEquals((int) Math.pow(2, n), result.size(),
                        "Should have 2^" + n + " subsets");
            }
        }
    }

    // ========== 5. SUBSETS WITH DUPLICATES TESTS ==========
    
    @Nested
    @DisplayName("Subsets with Duplicates Tests")
    class SubsetsWithDuplicatesTests {
        
        @Test
        @DisplayName("Test subsets of [1,2,2]")
        void testDuplicateSubsets() {
            int[] nums = {1, 2, 2};
            List<List<Integer>> result = BacktrackingTemplates.subsetsWithDup(nums);
            
            assertEquals(6, result.size());
            
            List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(),
                Arrays.asList(1),
                Arrays.asList(1, 2),
                Arrays.asList(1, 2, 2),
                Arrays.asList(2),
                Arrays.asList(2, 2)
            );
            
            assertTrue(containsAll(result, expected));
        }
        
        @Test
        @DisplayName("Test all unique subsets")
        void testAllUnique() {
            int[] nums = {1, 2, 2};
            List<List<Integer>> result = BacktrackingTemplates.subsetsWithDup(nums);
            
            Set<List<Integer>> uniqueSet = new HashSet<>(result);
            assertEquals(result.size(), uniqueSet.size(), "All subsets should be unique");
        }
        
        @Test
        @DisplayName("Test subsets with all same elements")
        void testAllSame() {
            int[] nums = {4, 4, 4};
            List<List<Integer>> result = BacktrackingTemplates.subsetsWithDup(nums);
            
            assertEquals(4, result.size());
            assertTrue(result.contains(Arrays.asList()));
            assertTrue(result.contains(Arrays.asList(4)));
            assertTrue(result.contains(Arrays.asList(4, 4)));
            assertTrue(result.contains(Arrays.asList(4, 4, 4)));
        }
    }

    // ========== 6. N-QUEENS TESTS ==========
    
    @Nested
    @DisplayName("N-Queens Tests")
    class NQueensTests {
        
        @Test
        @DisplayName("Test 4-Queens problem")
        void testFourQueens() {
            List<List<String>> result = BacktrackingTemplates.solveNQueens(4);
            
            assertEquals(2, result.size(), "4-Queens has 2 solutions");
            
            for (List<String> solution : result) {
                assertEquals(4, solution.size());
                assertTrue(isValidQueensSolution(solution));
            }
        }
        
        @Test
        @DisplayName("Test 1-Queen problem")
        void testOneQueen() {
            List<List<String>> result = BacktrackingTemplates.solveNQueens(1);
            
            assertEquals(1, result.size());
            assertEquals(Arrays.asList("Q"), result.get(0));
        }
        
        @Test
        @DisplayName("Test 8-Queens problem")
        void testEightQueens() {
            List<List<String>> result = BacktrackingTemplates.solveNQueens(8);
            
            assertEquals(92, result.size(), "8-Queens has 92 solutions");
            
            for (List<String> solution : result) {
                assertTrue(isValidQueensSolution(solution));
            }
        }
        
        @Test
        @DisplayName("Verify queens don't attack each other")
        void testQueensValidation() {
            List<List<String>> result = BacktrackingTemplates.solveNQueens(4);
            
            for (List<String> solution : result) {
                // Count total queens
                int queenCount = 0;
                for (String row : solution) {
                    queenCount += row.chars().filter(c -> c == 'Q').count();
                }
                assertEquals(4, queenCount, "Should have exactly 4 queens");
            }
        }
        
        private boolean isValidQueensSolution(List<String> board) {
            int n = board.size();
            List<int[]> queens = new ArrayList<>();
            
            // Find all queen positions
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    if (board.get(r).charAt(c) == 'Q') {
                        queens.add(new int[]{r, c});
                    }
                }
            }
            
            if (queens.size() != n) return false;
            
            // Check no two queens attack each other
            for (int i = 0; i < queens.size(); i++) {
                for (int j = i + 1; j < queens.size(); j++) {
                    int[] q1 = queens.get(i);
                    int[] q2 = queens.get(j);
                    
                    // Same row or column
                    if (q1[0] == q2[0] || q1[1] == q2[1]) return false;
                    
                    // Same diagonal
                    if (Math.abs(q1[0] - q2[0]) == Math.abs(q1[1] - q2[1])) return false;
                }
            }
            
            return true;
        }
    }

    // ========== 7. SUDOKU SOLVER TESTS ==========
    
    @Nested
    @DisplayName("Sudoku Solver Tests")
    class SudokuSolverTests {
        
        @Test
        @DisplayName("Test solvable sudoku")
        void testSolvableSudoku() {
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
            
            assertTrue(BacktrackingTemplates.solveSudoku(board));
            assertTrue(isValidSudoku(board));
        }
        
        @Test
        @DisplayName("Test already solved sudoku")
        void testAlreadySolved() {
            char[][] board = {
                {'5','3','4','6','7','8','9','1','2'},
                {'6','7','2','1','9','5','3','4','8'},
                {'1','9','8','3','4','2','5','6','7'},
                {'8','5','9','7','6','1','4','2','3'},
                {'4','2','6','8','5','3','7','9','1'},
                {'7','1','3','9','2','4','8','5','6'},
                {'9','6','1','5','3','7','2','8','4'},
                {'2','8','7','4','1','9','6','3','5'},
                {'3','4','5','2','8','6','1','7','9'}
            };
            
            assertTrue(BacktrackingTemplates.solveSudoku(board));
            assertTrue(isValidSudoku(board));
        }
        
        @Test
        @DisplayName("Test nearly empty sudoku")
        void testNearlyEmpty() {
            char[][] board = new char[9][9];
            for (int i = 0; i < 9; i++) {
                Arrays.fill(board[i], '.');
            }
            board[0][0] = '5';
            
            assertTrue(BacktrackingTemplates.solveSudoku(board));
            assertTrue(isValidSudoku(board));
        }
        
        private boolean isValidSudoku(char[][] board) {
            for (int i = 0; i < 9; i++) {
                Set<Character> row = new HashSet<>();
                Set<Character> col = new HashSet<>();
                
                for (int j = 0; j < 9; j++) {
                    if (board[i][j] == '.' || board[j][i] == '.') return false;
                    if (!row.add(board[i][j])) return false;
                    if (!col.add(board[j][i])) return false;
                }
            }
            
            // Check 3x3 boxes
            for (int box = 0; box < 9; box++) {
                Set<Character> boxSet = new HashSet<>();
                for (int i = 0; i < 9; i++) {
                    int r = 3 * (box / 3) + i / 3;
                    int c = 3 * (box % 3) + i % 3;
                    if (!boxSet.add(board[r][c])) return false;
                }
            }
            
            return true;
        }
    }

    // ========== 8. GENERATE PARENTHESES TESTS ==========
    
    @Nested
    @DisplayName("Generate Parentheses Tests")
    class GenerateParenthesesTests {
        
        @Test
        @DisplayName("Test n=1")
        void testOne() {
            List<String> result = BacktrackingTemplates.generateParenthesis(1);
            
            assertEquals(1, result.size());
            assertTrue(result.contains("()"));
        }
        
        @Test
        @DisplayName("Test n=2")
        void testTwo() {
            List<String> result = BacktrackingTemplates.generateParenthesis(2);
            
            assertEquals(2, result.size());
            assertTrue(result.contains("(())"));
            assertTrue(result.contains("()()"));
        }
        
        @Test
        @DisplayName("Test n=3")
        void testThree() {
            List<String> result = BacktrackingTemplates.generateParenthesis(3);
            
            assertEquals(5, result.size());
            assertTrue(result.contains("((()))"));
            assertTrue(result.contains("(()())"));
            assertTrue(result.contains("(())()"));
            assertTrue(result.contains("()(())"));
            assertTrue(result.contains("()()()"));
        }
        
        @Test
        @DisplayName("Test n=4 - Catalan number")
        void testFour() {
            List<String> result = BacktrackingTemplates.generateParenthesis(4);
            
            assertEquals(14, result.size(), "Catalan(4) = 14");
            
            // Verify all are valid
            for (String s : result) {
                assertTrue(isValidParentheses(s));
                assertEquals(8, s.length());
            }
        }
        
        @Test
        @DisplayName("Verify all generated parentheses are valid")
        void testAllValid() {
            for (int n = 1; n <= 5; n++) {
                List<String> result = BacktrackingTemplates.generateParenthesis(n);
                
                for (String s : result) {
                    assertTrue(isValidParentheses(s), s + " should be valid");
                    assertEquals(2 * n, s.length());
                }
            }
        }
        
        private boolean isValidParentheses(String s) {
            int balance = 0;
            for (char c : s.toCharArray()) {
                if (c == '(') balance++;
                else balance--;
                if (balance < 0) return false;
            }
            return balance == 0;
        }
    }

    // ========== 9. WORD SEARCH TESTS ==========
    
    @Nested
    @DisplayName("Word Search Tests")
    class WordSearchTests {
        
        @Test
        @DisplayName("Test basic word search - found")
        void testWordFound() {
            char[][] board = {
                {'A','B','C','E'},
                {'S','F','C','S'},
                {'A','D','E','E'}
            };
            
            assertTrue(BacktrackingTemplates.existWordInGrid(board, "ABCCED"));
            assertTrue(BacktrackingTemplates.existWordInGrid(board, "SEE"));
            assertTrue(BacktrackingTemplates.existWordInGrid(board, "AS"));
        }
        
        @Test
        @DisplayName("Test word not found")
        void testWordNotFound() {
            char[][] board = {
                {'A','B','C','E'},
                {'S','F','C','S'},
                {'A','D','E','E'}
            };
            
            assertFalse(BacktrackingTemplates.existWordInGrid(board, "ABCB"));
        }
        
        @Test
        @DisplayName("Test single cell")
        void testSingleCell() {
            char[][] board = {{'A'}};
            
            assertTrue(BacktrackingTemplates.existWordInGrid(board, "A"));
            assertFalse(BacktrackingTemplates.existWordInGrid(board, "B"));
        }
        
        @Test
        @DisplayName("Test vertical word")
        void testVerticalWord() {
            char[][] board = {
                {'A','B'},
                {'C','D'},
                {'E','F'}
            };
            
            assertTrue(BacktrackingTemplates.existWordInGrid(board, "ACE"));
        }
        
        @Test
        @DisplayName("Test horizontal word")
        void testHorizontalWord() {
            char[][] board = {
                {'A','B','C'},
                {'D','E','F'}
            };
            
            assertTrue(BacktrackingTemplates.existWordInGrid(board, "ABC"));
        }
        
        @Test
        @DisplayName("Test zigzag path")
        void testZigzagPath() {
            char[][] board = {
                {'A','B','C'},
                {'D','E','F'},
                {'G','H','I'}
            };
            
            assertTrue(BacktrackingTemplates.existWordInGrid(board, "ABEF"));
        }
        
        @Test
        @DisplayName("Test word longer than grid")
        void testWordTooLong() {
            char[][] board = {
                {'A','B'},
                {'C','D'}
            };
            
            assertFalse(BacktrackingTemplates.existWordInGrid(board, "ABCDEFGH"));
        }
    }

    // ========== 10. COMBINATION SUM TESTS ==========
    
    @Nested
    @DisplayName("Combination Sum Tests")
    class CombinationSumTests {
        
        @Test
        @DisplayName("Test combination sum - candidates can be reused")
        void testCombinationSum() {
            int[] candidates = {2, 3, 6, 7};
            int target = 7;
            
            List<List<Integer>> result = BacktrackingTemplates.combinationSum(candidates, target);
            
            assertTrue(result.contains(Arrays.asList(2, 2, 3)));
            assertTrue(result.contains(Arrays.asList(7)));
            
            // Verify all sum to target
            for (List<Integer> combination : result) {
                assertEquals(target, combination.stream().mapToInt(Integer::intValue).sum());
            }
        }
        
        @Test
        @DisplayName("Test combination sum with target 1")
        void testTargetOne() {
            int[] candidates = {1, 2, 3};
            int target = 1;
            
            List<List<Integer>> result = BacktrackingTemplates.combinationSum(candidates, target);
            
            assertEquals(1, result.size());
            assertTrue(result.contains(Arrays.asList(1)));
        }
        
        @Test
        @DisplayName("Test combination sum impossible")
        void testImpossible() {
            int[] candidates = {2, 4, 6};
            int target = 5;
            
            List<List<Integer>> result = BacktrackingTemplates.combinationSum(candidates, target);
            
            assertTrue(result.isEmpty());
        }
        
        @Test
        @DisplayName("Test combination sum II - each used once")
        void testCombinationSum2() {
            int[] candidates = {10, 1, 2, 7, 6, 1, 5};
            int target = 8;
            
            List<List<Integer>> result = BacktrackingTemplates.combinationSum2(candidates, target);
            
            assertTrue(result.contains(Arrays.asList(1, 1, 6)));
            assertTrue(result.contains(Arrays.asList(1, 2, 5)));
            assertTrue(result.contains(Arrays.asList(1, 7)));
            assertTrue(result.contains(Arrays.asList(2, 6)));
            
            // Verify uniqueness
            Set<List<Integer>> uniqueSet = new HashSet<>(result);
            assertEquals(result.size(), uniqueSet.size());
            
            // Verify all sum to target
            for (List<Integer> combination : result) {
                assertEquals(target, combination.stream().mapToInt(Integer::intValue).sum());
            }
        }
        
        @Test
        @DisplayName("Test combination sum II with all duplicates")
        void testCombinationSum2AllDuplicates() {
            int[] candidates = {1, 1, 1, 1};
            int target = 2;
            
            List<List<Integer>> result = BacktrackingTemplates.combinationSum2(candidates, target);
            
            assertEquals(1, result.size());
            assertTrue(result.contains(Arrays.asList(1, 1)));
        }
    }

    // ========== 11. PALINDROME PARTITIONING TESTS ==========
    
    @Nested
    @DisplayName("Palindrome Partitioning Tests")
    class PalindromePartitioningTests {
        
        @Test
        @DisplayName("Test partition 'aab'")
        void testBasicPartition() {
            String s = "aab";
            List<List<String>> result = BacktrackingTemplates.partition(s);
            
            assertEquals(2, result.size());
            assertTrue(result.contains(Arrays.asList("a", "a", "b")));
            assertTrue(result.contains(Arrays.asList("aa", "b")));
        }
        
        @Test
        @DisplayName("Test partition single character")
        void testSingleChar() {
            String s = "a";
            List<List<String>> result = BacktrackingTemplates.partition(s);
            
            assertEquals(1, result.size());
            assertEquals(Arrays.asList("a"), result.get(0));
        }
        
        @Test
        @DisplayName("Test partition all palindrome")
        void testAllPalindrome() {
            String s = "aba";
            List<List<String>> result = BacktrackingTemplates.partition(s);
            
            assertTrue(result.contains(Arrays.asList("a", "b", "a")));
            assertTrue(result.contains(Arrays.asList("aba")));
        }
        
        @Test
        @DisplayName("Test partition no palindrome except singles")
        void testNoPalindrome() {
            String s = "abc";
            List<List<String>> result = BacktrackingTemplates.partition(s);
            
            // Only partition should be individual characters
            assertEquals(1, result.size());
            assertTrue(result.contains(Arrays.asList("a", "b", "c")));
        }
        
        @Test
        @DisplayName("Test all partitions contain valid palindromes")
        void testAllValidPalindromes() {
            String s = "aab";
            List<List<String>> result = BacktrackingTemplates.partition(s);
            
            for (List<String> partition : result) {
                for (String part : partition) {
                    assertTrue(isPalindrome(part), part + " should be palindrome");
                }
                
                // Verify concatenation equals original
                String concatenated = String.join("", partition);
                assertEquals(s, concatenated);
            }
        }
        
        @Test
        @DisplayName("Test partition longer string")
        void testLongerString() {
            String s = "aabb";
            List<List<String>> result = BacktrackingTemplates.partition(s);
            
            assertTrue(result.size() > 0);
            
            // All partitions should be valid
            for (List<String> partition : result) {
                for (String part : partition) {
                    assertTrue(isPalindrome(part));
                }
            }
        }
        
        private boolean isPalindrome(String s) {
            int left = 0, right = s.length() - 1;
            while (left < right) {
                if (s.charAt(left++) != s.charAt(right--)) return false;
            }
            return true;
        }
    }

    // ========== EDGE CASES AND STRESS TESTS ==========
    
    @Nested
    @DisplayName("Edge Cases and Stress Tests")
    class EdgeCasesAndStressTests {
        
        @Test
        @DisplayName("Test empty array handling")
        void testEmptyArray() {
            int[] empty = {};
            
            // Permutations
            List<List<Integer>> perms = BacktrackingTemplates.permute(empty);
            assertTrue(perms.isEmpty() || perms.size() == 1); // Could be empty or [[]]
            
            // Subsets
            List<List<Integer>> subs = BacktrackingTemplates.subsets(empty);
            assertEquals(1, subs.size());
            assertTrue(subs.contains(Arrays.asList()));
        }
        
        @Test
        @DisplayName("Test large permutations count")
        void testLargePermutations() {
            int[] nums = {1, 2, 3, 4, 5};
            List<List<Integer>> result = BacktrackingTemplates.permute(nums);
            
            assertEquals(120, result.size(), "5! = 120");
        }
        
        @Test
        @DisplayName("Test large subsets count")
        void testLargeSubsets() {
            int[] nums = {1, 2, 3, 4, 5, 6};
            List<List<Integer>> result = BacktrackingTemplates.subsets(nums);
            
            assertEquals(64, result.size(), "2^6 = 64");
        }
        
        @Test
        @DisplayName("Test generate parentheses up to n=5")
        void testParenthesesCatalanNumbers() {
            int[] catalan = {1, 1, 2, 5, 14, 42};
            
            for (int n = 0; n < catalan.length; n++) {
                if (n == 0) continue; // Skip n=0 case
                List<String> result = BacktrackingTemplates.generateParenthesis(n);
                assertEquals(catalan[n], result.size(),
                        "Catalan(" + n + ") should be " + catalan[n]);
            }
        }
        
        @Test
        @DisplayName("Test N-Queens for various N")
        void testNQueensVariousSizes() {
            int[] solutions = {1, 0, 0, 2, 10, 4, 40, 92}; // N=1 to N=8
            
            for (int n = 1; n <= 5; n++) {
                List<List<String>> result = BacktrackingTemplates.solveNQueens(n);
                assertEquals(solutions[n - 1], result.size(),
                        n + "-Queens should have " + solutions[n - 1] + " solutions");
            }
        }
    }

    // ========== HELPER METHODS ==========
    
    /**
     * Check if result contains all expected lists (order-independent)
     */
    private boolean containsAll(List<List<Integer>> result, List<List<Integer>> expected) {
        if (result.size() != expected.size()) return false;
        
        Set<List<Integer>> resultSet = new HashSet<>(result);
        Set<List<Integer>> expectedSet = new HashSet<>(expected);
        
        return resultSet.equals(expectedSet);
    }
}
