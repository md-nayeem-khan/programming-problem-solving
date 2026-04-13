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
 * Comprehensive test suite for GameTheoryTemplates.
 * 
 * Validates all game theory algorithms with:
 * - Basic functionality tests
 * - Edge cases (empty, single element)
 * - LeetCode test cases
 * - Nim game variants
 * - Grundy number calculations
 * - Stone game problems
 * 
 * Coverage:
 * 1. Classic Nim Game
 * 2. Single Pile Nim (LeetCode 292)
 * 3. Misère Nim
 * 4. Grundy Numbers (Sprague-Grundy theorem)
 * 5. Mex calculation
 * 6. Stone Game IV (LeetCode 1510)
 * 7. Stone Game VII (LeetCode 1690)
 * 8. Stone Game VIII (LeetCode 1872)
 * 9. Composite Games
 * 10. Green Hackenbush
 */
public class GameTheoryTemplatesTest {

    // ========== 1. CLASSIC NIM GAME TESTS ==========
    
    @Nested
    @DisplayName("Classic Nim Game Tests")
    class ClassicNimTests {
        
        @Test
        @DisplayName("Basic Nim - winning position")
        void testNimWinningPosition() {
            int[] piles = {3, 5, 7};
            assertTrue(GameTheoryTemplates.canWinNim(piles), 
                "XOR = 3^5^7 = 1, first player should win");
        }
        
        @Test
        @DisplayName("Basic Nim - losing position")
        void testNimLosingPosition() {
            int[] piles = {3, 5, 6};
            assertFalse(GameTheoryTemplates.canWinNim(piles), 
                "XOR = 3^5^6 = 0, first player should lose");
        }
        
        @Test
        @DisplayName("Single pile Nim")
        void testSinglePile() {
            int[] piles = {7};
            assertTrue(GameTheoryTemplates.canWinNim(piles), 
                "Single pile with size > 0 should be winning");
        }
        
        @Test
        @DisplayName("All equal piles - even count")
        void testEqualPilesEven() {
            int[] piles = {5, 5, 5, 5};
            assertFalse(GameTheoryTemplates.canWinNim(piles), 
                "Even count of equal piles should be losing");
        }
        
        @Test
        @DisplayName("All equal piles - odd count")
        void testEqualPilesOdd() {
            int[] piles = {5, 5, 5};
            assertTrue(GameTheoryTemplates.canWinNim(piles), 
                "Odd count of equal piles should be winning");
        }
        
        @Test
        @DisplayName("Large piles")
        void testLargePiles() {
            int[] piles = {1000000, 999999, 500000};
            int xorSum = 1000000 ^ 999999 ^ 500000;
            assertEquals(xorSum != 0, GameTheoryTemplates.canWinNim(piles));
        }
        
        @Test
        @DisplayName("Two piles - always winning")
        void testTwoPiles() {
            int[] piles = {3, 7};
            assertTrue(GameTheoryTemplates.canWinNim(piles), 
                "Two different piles always winning");
        }
        
        @Test
        @DisplayName("Two equal piles - losing")
        void testTwoEqualPiles() {
            int[] piles = {5, 5};
            assertFalse(GameTheoryTemplates.canWinNim(piles), 
                "Two equal piles should be losing");
        }
    }
    
    // ========== 2. SINGLE PILE NIM (LEETCODE 292) ==========
    
    @Nested
    @DisplayName("Single Pile Nim - LeetCode 292")
    class SinglePileNimTests {
        
        @ParameterizedTest(name = "n={0} -> can win: {1}")
        @MethodSource("provideSinglePileTestCases")
        @DisplayName("Single pile Nim test cases")
        void testSinglePileNim(int n, boolean expected) {
            assertEquals(expected, GameTheoryTemplates.canWinNimSingle(n));
        }
        
        static Stream<Arguments> provideSinglePileTestCases() {
            return Stream.of(
                Arguments.of(1, true),   // Can take 1, opponent loses
                Arguments.of(2, true),   // Can take 2, opponent loses
                Arguments.of(3, true),   // Can take 3, opponent loses
                Arguments.of(4, false),  // Any move leaves 1-3 for opponent to win
                Arguments.of(5, true),   // Take 1, leave 4 (losing position)
                Arguments.of(6, true),   // Take 2, leave 4
                Arguments.of(7, true),   // Take 3, leave 4
                Arguments.of(8, false),  // Any move leaves 5-7 for opponent
                Arguments.of(100, false), // 100 % 4 == 0
                Arguments.of(101, true),  // 101 % 4 == 1
                Arguments.of(1000000, false) // Multiple of 4
            );
        }
        
        @Test
        @DisplayName("Edge case - n=1")
        void testMinimalCase() {
            assertTrue(GameTheoryTemplates.canWinNimSingle(1));
        }
    }
    
    // ========== 3. MISÈRE NIM TESTS ==========
    
    @Nested
    @DisplayName("Misère Nim Tests")
    class MisereNimTests {
        
        @Test
        @DisplayName("All piles size 1 - odd count")
        void testAllOnesOdd() {
            int[] piles = {1, 1, 1};
            assertTrue(GameTheoryTemplates.canWinMisereNim(piles), 
                "Odd count of 1s - first player wins");
        }
        
        @Test
        @DisplayName("All piles size 1 - even count")
        void testAllOnesEven() {
            int[] piles = {1, 1, 1, 1};
            assertFalse(GameTheoryTemplates.canWinMisereNim(piles), 
                "Even count of 1s - first player loses");
        }
        
        @Test
        @DisplayName("Mixed piles - XOR != 0")
        void testMixedPilesWinning() {
            int[] piles = {3, 5, 7};
            assertTrue(GameTheoryTemplates.canWinMisereNim(piles), 
                "Not all 1s and XOR != 0 - first player wins");
        }
        
        @Test
        @DisplayName("Mixed piles - XOR == 0")
        void testMixedPilesLosing() {
            int[] piles = {3, 5, 6};
            assertFalse(GameTheoryTemplates.canWinMisereNim(piles), 
                "Not all 1s and XOR == 0 - first player loses");
        }
        
        @Test
        @DisplayName("Single pile size 1")
        void testSingleOne() {
            int[] piles = {1};
            assertTrue(GameTheoryTemplates.canWinMisereNim(piles), 
                "Single 1 - first player wins");
        }
        
        @Test
        @DisplayName("Single pile size > 1")
        void testSingleLarge() {
            int[] piles = {5};
            assertTrue(GameTheoryTemplates.canWinMisereNim(piles), 
                "Single pile > 1 - first player wins");
        }
        
        @Test
        @DisplayName("Mix of 1s and larger piles")
        void testMixedWithOnes() {
            int[] piles = {1, 1, 3};
            assertTrue(GameTheoryTemplates.canWinMisereNim(piles), 
                "Has pile > 1, so use normal Nim XOR");
        }
    }
    
    // ========== 4. MEX CALCULATION TESTS ==========
    
    @Nested
    @DisplayName("Mex Calculation Tests")
    class MexTests {
        
        @Test
        @DisplayName("Mex of empty set")
        void testMexEmpty() {
            Set<Integer> set = new HashSet<>();
            assertEquals(0, GameTheoryTemplates.mex(set));
        }
        
        @Test
        @DisplayName("Mex of {0}")
        void testMexZero() {
            Set<Integer> set = new HashSet<>(Arrays.asList(0));
            assertEquals(1, GameTheoryTemplates.mex(set));
        }
        
        @Test
        @DisplayName("Mex of {0, 1, 2}")
        void testMexConsecutive() {
            Set<Integer> set = new HashSet<>(Arrays.asList(0, 1, 2));
            assertEquals(3, GameTheoryTemplates.mex(set));
        }
        
        @Test
        @DisplayName("Mex of {0, 1, 3, 4}")
        void testMexWithGap() {
            Set<Integer> set = new HashSet<>(Arrays.asList(0, 1, 3, 4));
            assertEquals(2, GameTheoryTemplates.mex(set), 
                "Should return first missing value");
        }
        
        @Test
        @DisplayName("Mex of {1, 2, 3}")
        void testMexMissingZero() {
            Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3));
            assertEquals(0, GameTheoryTemplates.mex(set), 
                "Missing 0, should return 0");
        }
        
        @Test
        @DisplayName("Mex of array")
        void testMexArray() {
            int[] arr = {0, 1, 3, 4};
            assertEquals(2, GameTheoryTemplates.mex(arr));
        }
        
        @Test
        @DisplayName("Mex with large numbers")
        void testMexLarge() {
            Set<Integer> set = new HashSet<>(Arrays.asList(0, 1, 2, 100, 1000));
            assertEquals(3, GameTheoryTemplates.mex(set));
        }
    }
    
    // ========== 5. GRUNDY NUMBER TESTS ==========
    
    @Nested
    @DisplayName("Grundy Number Tests")
    class GrundyNumberTests {
        
        @Test
        @DisplayName("Terminal state has Grundy 0")
        void testTerminalState() {
            Map<Integer, Integer> memo = new HashMap<>();
            
            // Game with no moves from state 0
            java.util.function.Function<Integer, List<Integer>> getNextStates = 
                state -> new ArrayList<>();
            
            assertEquals(0, GameTheoryTemplates.grundyNumber(0, memo, getNextStates));
        }
        
        @Test
        @DisplayName("Remove 1 or 2 stones game")
        void testRemove1or2() {
            Map<Integer, Integer> memo = new HashMap<>();
            
            // Game: Remove 1 or 2 stones
            java.util.function.Function<Integer, List<Integer>> getNextStates = state -> {
                List<Integer> next = new ArrayList<>();
                if (state >= 1) next.add(state - 1);
                if (state >= 2) next.add(state - 2);
                return next;
            };
            
            // Grundy numbers for this game follow pattern: 0,1,2,0,1,2,...
            assertEquals(0, GameTheoryTemplates.grundyNumber(0, memo, getNextStates));
            assertEquals(1, GameTheoryTemplates.grundyNumber(1, memo, getNextStates));
            assertEquals(2, GameTheoryTemplates.grundyNumber(2, memo, getNextStates));
            assertEquals(0, GameTheoryTemplates.grundyNumber(3, memo, getNextStates));
            assertEquals(1, GameTheoryTemplates.grundyNumber(4, memo, getNextStates));
            assertEquals(2, GameTheoryTemplates.grundyNumber(5, memo, getNextStates));
        }
        
        @Test
        @DisplayName("Remove 1, 2, or 3 stones game")
        void testRemove1or2or3() {
            Map<Integer, Integer> memo = new HashMap<>();
            
            java.util.function.Function<Integer, List<Integer>> getNextStates = state -> {
                List<Integer> next = new ArrayList<>();
                if (state >= 1) next.add(state - 1);
                if (state >= 2) next.add(state - 2);
                if (state >= 3) next.add(state - 3);
                return next;
            };
            
            // Grundy pattern for 1-3 removal: 0,1,2,3,0,1,2,3,...
            assertEquals(0, GameTheoryTemplates.grundyNumber(0, memo, getNextStates));
            assertEquals(1, GameTheoryTemplates.grundyNumber(1, memo, getNextStates));
            assertEquals(2, GameTheoryTemplates.grundyNumber(2, memo, getNextStates));
            assertEquals(3, GameTheoryTemplates.grundyNumber(3, memo, getNextStates));
            assertEquals(0, GameTheoryTemplates.grundyNumber(4, memo, getNextStates));
        }
        
        @Test
        @DisplayName("Grundy number memoization works")
        void testMemoization() {
            Map<Integer, Integer> memo = new HashMap<>();
            
            java.util.function.Function<Integer, List<Integer>> getNextStates = state -> {
                List<Integer> next = new ArrayList<>();
                if (state >= 1) next.add(state - 1);
                return next;
            };
            
            int result1 = GameTheoryTemplates.grundyNumber(5, memo, getNextStates);
            int result2 = GameTheoryTemplates.grundyNumber(5, memo, getNextStates);
            
            assertEquals(result1, result2);
            assertTrue(memo.containsKey(5));
        }
    }
    
    // ========== 6. STONE GAME IV (LEETCODE 1510) ==========
    
    @Nested
    @DisplayName("Stone Game IV - LeetCode 1510")
    class StoneGameIVTests {
        
        @ParameterizedTest(name = "n={0} -> can win: {1}")
        @MethodSource("provideStoneGameIVTestCases")
        @DisplayName("Stone Game IV test cases")
        void testStoneGameIV(int n, boolean expected) {
            assertEquals(expected, GameTheoryTemplates.winnerSquareGame(n), 
                "DP solution");
            assertEquals(expected, GameTheoryTemplates.winnerSquareGameGrundy(n), 
                "Grundy solution");
        }
        
        static Stream<Arguments> provideStoneGameIVTestCases() {
            return Stream.of(
                Arguments.of(1, true),   // Remove 1^2 = 1
                Arguments.of(2, false),  // Remove 1, opponent wins with 1
                Arguments.of(3, true),   // Remove 1, leave 2 (losing)
                Arguments.of(4, true),   // Remove 4
                Arguments.of(5, false),  // All moves lead to winning positions
                Arguments.of(7, false),  // Example from LeetCode
                Arguments.of(8, true),   // Remove 4, leave 4 (true) - can win
                Arguments.of(10, false)   // All moves (remove 1,4,9) leave winning positions
            );
        }
        
        @Test
        @DisplayName("Both methods produce same result")
        void testConsistency() {
            for (int i = 1; i <= 20; i++) {
                boolean dp = GameTheoryTemplates.winnerSquareGame(i);
                boolean grundy = GameTheoryTemplates.winnerSquareGameGrundy(i);
                assertEquals(dp, grundy, "Methods should agree for n=" + i);
            }
        }
    }
    
    // ========== 7. STONE GAME VII (LEETCODE 1690) ==========
    
    @Nested
    @DisplayName("Stone Game VII - LeetCode 1690")
    class StoneGameVIITests {
        
        @Test
        @DisplayName("Example 1 from LeetCode")
        void testExample1() {
            int[] stones = {5, 3, 1, 4, 2};
            assertEquals(6, GameTheoryTemplates.stoneGameVII(stones));
        }
        
        @Test
        @DisplayName("Example 2 from LeetCode")
        void testExample2() {
            int[] stones = {7, 90, 5, 1, 100, 10, 10, 2};
            assertEquals(122, GameTheoryTemplates.stoneGameVII(stones));
        }
        
        @Test
        @DisplayName("Two stones")
        void testTwoStones() {
            int[] stones = {5, 3};
            // First player removes 5 (scores 3) or 3 (scores 5)
            // Best is to score 5
            assertEquals(5, GameTheoryTemplates.stoneGameVII(stones));
        }
        
        @Test
        @DisplayName("Three equal stones")
        void testThreeEqual() {
            int[] stones = {2, 2, 2};
            // Remove left: score 4, opponent gets dp[1][2]=2, diff = 4-2 = 2
            // Remove right: score 4, opponent gets dp[0][1]=2, diff = 4-2 = 2
            assertEquals(2, GameTheoryTemplates.stoneGameVII(stones));
        }
        
        @Test
        @DisplayName("Large values")
        void testLargeValues() {
            int[] stones = {1000, 1, 1, 1000};
            int result = GameTheoryTemplates.stoneGameVII(stones);
            assertTrue(result >= 0, "Result should be non-negative");
        }
        
        @Test
        @DisplayName("Alternating high-low")
        void testAlternating() {
            int[] stones = {10, 1, 10, 1, 10};
            int result = GameTheoryTemplates.stoneGameVII(stones);
            assertTrue(result > 0, "First player should have advantage");
        }
    }
    
    // ========== 8. STONE GAME VIII (LEETCODE 1872) ==========
    
    @Nested
    @DisplayName("Stone Game VIII - LeetCode 1872")
    class StoneGameVIIITests {
        
        @Test
        @DisplayName("Example 1 from LeetCode")
        void testExample1() {
            int[] stones = {-1, 2, -3, 4, -5};
            assertEquals(5, GameTheoryTemplates.stoneGameVIII(stones));
        }
        
        @Test
        @DisplayName("Example 2 from LeetCode")
        void testExample2() {
            int[] stones = {7, -6, 5, 10, 5, -2, -6};
            assertEquals(13, GameTheoryTemplates.stoneGameVIII(stones));
        }
        
        @Test
        @DisplayName("Example 3 from LeetCode")
        void testExample3() {
            int[] stones = {-10, -12};
            assertEquals(-22, GameTheoryTemplates.stoneGameVIII(stones));
        }
        
        @Test
        @DisplayName("All positive")
        void testAllPositive() {
            int[] stones = {1, 2, 3, 4, 5};
            int result = GameTheoryTemplates.stoneGameVIII(stones);
            assertTrue(result > 0);
        }
        
        @Test
        @DisplayName("All negative")
        void testAllNegative() {
            int[] stones = {-1, -2, -3, -4, -5};
            int result = GameTheoryTemplates.stoneGameVIII(stones);
            // Result is 5 (positive) even though all stones are negative
            // This is the maximum difference achievable
            assertEquals(5, result);
        }
        
        @Test
        @DisplayName("Two stones only")
        void testTwoStones() {
            int[] stones = {5, 3};
            // Must merge all, score is sum
            assertEquals(8, GameTheoryTemplates.stoneGameVIII(stones));
        }
    }
    
    // ========== 9. COMPOSITE GAME TESTS ==========
    
    @Nested
    @DisplayName("Composite Game Tests")
    class CompositeGameTests {
        
        @Test
        @DisplayName("Multiple Nim piles as composite game")
        void testMultipleNimPiles() {
            int[] games = {3, 5, 7};
            
            // Simple Nim game: next states are just reducing the pile
            java.util.function.Function<Integer, List<Integer>> getNextStates = state -> {
                List<Integer> next = new ArrayList<>();
                for (int i = 0; i < state; i++) {
                    next.add(i);
                }
                return next;
            };
            
            boolean canWin = GameTheoryTemplates.canWinCompositeGame(games, getNextStates);
            // For Nim, this should match standard Nim result
            assertTrue(canWin);
        }
        
        @Test
        @DisplayName("Two independent games")
        void testTwoGames() {
            int[] games = {1, 1};
            
            // Remove 1 or 2
            java.util.function.Function<Integer, List<Integer>> getNextStates = state -> {
                List<Integer> next = new ArrayList<>();
                if (state >= 1) next.add(state - 1);
                if (state >= 2) next.add(state - 2);
                return next;
            };
            
            boolean canWin = GameTheoryTemplates.canWinCompositeGame(games, getNextStates);
            // Both have Grundy 1, XOR = 0, losing
            assertFalse(canWin);
        }
        
        @Test
        @DisplayName("Three independent games")
        void testThreeGames() {
            int[] games = {2, 3, 4};
            
            java.util.function.Function<Integer, List<Integer>> getNextStates = state -> {
                List<Integer> next = new ArrayList<>();
                if (state >= 1) next.add(state - 1);
                if (state >= 2) next.add(state - 2);
                return next;
            };
            
            boolean canWin = GameTheoryTemplates.canWinCompositeGame(games, getNextStates);
            // Grundy: 2->2, 3->0, 4->1; XOR = 2^0^1 = 3 != 0
            assertTrue(canWin);
        }
    }
    
    // ========== 10. GREEN HACKENBUSH TESTS ==========
    
    @Nested
    @DisplayName("Green Hackenbush Tests")
    class GreenHackenbushTests {
        
        @Test
        @DisplayName("Single edge tree")
        void testSingleEdge() {
            @SuppressWarnings("unchecked")
            List<Integer>[] tree = new List[2];
            tree[0] = new ArrayList<>();
            tree[1] = new ArrayList<>();
            tree[0].add(1);
            tree[1].add(0);
            
            int grundy = GameTheoryTemplates.greenHackenbushGrundy(0, tree);
            // Leaf at depth 1: (1+1) = 2
            assertEquals(2, grundy);
        }
        
        @Test
        @DisplayName("Star tree (root with multiple children)")
        void testStarTree() {
            @SuppressWarnings("unchecked")
            List<Integer>[] tree = new List[4];
            for (int i = 0; i < 4; i++) {
                tree[i] = new ArrayList<>();
            }
            // Root 0 connected to leaves 1, 2, 3
            tree[0].add(1);
            tree[0].add(2);
            tree[0].add(3);
            tree[1].add(0);
            tree[2].add(0);
            tree[3].add(0);
            
            int grundy = GameTheoryTemplates.greenHackenbushGrundy(0, tree);
            // All leaves at depth 1: XOR of (1+1, 1+1, 1+1) = 2^2^2 = 2
            assertEquals(2, grundy);
        }
        
        @Test
        @DisplayName("Linear chain tree")
        void testLinearChain() {
            @SuppressWarnings("unchecked")
            List<Integer>[] tree = new List[4];
            for (int i = 0; i < 4; i++) {
                tree[i] = new ArrayList<>();
            }
            // Chain: 0-1-2-3
            tree[0].add(1);
            tree[1].add(0);
            tree[1].add(2);
            tree[2].add(1);
            tree[2].add(3);
            tree[3].add(2);
            
            int grundy = GameTheoryTemplates.greenHackenbushGrundy(0, tree);
            // Only leaf is node 3 at depth 3: (3+1) = 4
            assertEquals(4, grundy);
        }
    }
    
    // ========== INTEGRATION TESTS ==========
    
    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {
        
        @Test
        @DisplayName("Verify main method runs without errors")
        void testMainMethod() {
            assertDoesNotThrow(() -> GameTheoryTemplates.main(new String[]{}));
        }
        
        @Test
        @DisplayName("All algorithms are consistent")
        void testAlgorithmConsistency() {
            // Test that different approaches to same problem give same answer
            
            // Nim game
            int[] nimPiles = {3, 5, 7};
            boolean nimResult = GameTheoryTemplates.canWinNim(nimPiles);
            int xorSum = 0;
            for (int p : nimPiles) xorSum ^= p;
            assertEquals(xorSum != 0, nimResult);
            
            // Stone Game IV
            for (int n = 1; n <= 15; n++) {
                boolean dp = GameTheoryTemplates.winnerSquareGame(n);
                boolean grundy = GameTheoryTemplates.winnerSquareGameGrundy(n);
                assertEquals(dp, grundy, "Inconsistency at n=" + n);
            }
        }
        
        @Test
        @DisplayName("Performance test - large inputs")
        void testPerformance() {
            // Test with reasonably large inputs
            
            // Nim with many piles
            int[] largePiles = new int[1000];
            for (int i = 0; i < 1000; i++) {
                largePiles[i] = i + 1;
            }
            assertDoesNotThrow(() -> GameTheoryTemplates.canWinNim(largePiles));
            
            // Stone Game IV with moderate n
            assertDoesNotThrow(() -> GameTheoryTemplates.winnerSquareGame(100));
            
            // Stone Game VII with moderate array
            int[] stones = new int[100];
            Arrays.fill(stones, 5);
            assertDoesNotThrow(() -> GameTheoryTemplates.stoneGameVII(stones));
        }
    }
    
    // ========== EDGE CASES ==========
    
    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Empty Nim piles")
        void testEmptyNimPiles() {
            int[] piles = {};
            assertFalse(GameTheoryTemplates.canWinNim(piles), 
                "No moves available, should lose");
        }
        
        @Test
        @DisplayName("Single zero pile")
        void testZeroPile() {
            int[] piles = {0};
            assertFalse(GameTheoryTemplates.canWinNim(piles), 
                "XOR of 0 is 0");
        }
        
        @Test
        @DisplayName("Multiple zero piles")
        void testMultipleZeroPiles() {
            int[] piles = {0, 0, 0};
            assertFalse(GameTheoryTemplates.canWinNim(piles), 
                "XOR of all zeros is 0");
        }
        
        @Test
        @DisplayName("Mix of zero and non-zero piles")
        void testMixedZeroPiles() {
            int[] piles = {0, 0, 5};
            assertTrue(GameTheoryTemplates.canWinNim(piles), 
                "XOR = 5, winning");
        }
        
        @Test
        @DisplayName("Stone Game with minimum stones")
        void testMinimalStoneGame() {
            int[] stones = {1, 1};
            assertEquals(1, GameTheoryTemplates.stoneGameVII(stones));
        }
    }
}
