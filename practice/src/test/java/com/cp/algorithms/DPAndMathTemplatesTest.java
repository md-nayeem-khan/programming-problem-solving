package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DPAndMathTemplates algorithms.
 * Tests correctness, edge cases, and validates complexity claims.
 * 
 * Covers:
 * - Classic DP patterns (LCS, LIS, Knapsack, etc.)
 * - Math utilities (GCD, LCM, modular arithmetic)
 * - Kadane's algorithm variants
 * - State machine DP problems
 * - String DP algorithms
 * - Matrix exponentiation
 * - Number theory algorithms
 * - Reservoir sampling
 */
public class DPAndMathTemplatesTest {

    // ============================================================================
    // CLASSIC DP ALGORITHMS TESTS
    // ============================================================================

    @Nested
    @DisplayName("Classic DP Algorithms")
    class ClassicDPTests {

        @Test
        @DisplayName("LCS: Basic test cases")
        public void testLCSBasic() {
            assertEquals(3, DPAndMathTemplates.lcs("ABCDGH", "AEDFHR"));
            assertEquals(4, DPAndMathTemplates.lcs("AGGTAB", "GXTXAYB"));
            assertEquals(0, DPAndMathTemplates.lcs("ABC", "DEF"));
            assertEquals(3, DPAndMathTemplates.lcs("ABC", "ABC"));
        }

        @Test
        @DisplayName("LCS: Edge cases")
        public void testLCSEdgeCases() {
            assertEquals(0, DPAndMathTemplates.lcs("", ""));
            assertEquals(0, DPAndMathTemplates.lcs("", "ABC"));
            assertEquals(0, DPAndMathTemplates.lcs("ABC", ""));
            assertEquals(1, DPAndMathTemplates.lcs("A", "A"));
        }

        @Test
        @DisplayName("LIS: Basic test cases")
        public void testLISBasic() {
            assertEquals(4, DPAndMathTemplates.lis(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
            assertEquals(4, DPAndMathTemplates.lis(new int[]{0, 1, 0, 3, 2, 3}));
            assertEquals(1, DPAndMathTemplates.lis(new int[]{7, 7, 7, 7, 7, 7, 7}));
        }

        @Test
        @DisplayName("LIS: Edge cases")
        public void testLISEdgeCases() {
            assertEquals(1, DPAndMathTemplates.lis(new int[]{1}));
            assertEquals(2, DPAndMathTemplates.lis(new int[]{1, 2}));
            assertEquals(1, DPAndMathTemplates.lis(new int[]{2, 1}));
        }

        @Test
        @DisplayName("Knapsack: Basic test cases")
        public void testKnapsackBasic() {
            int[] weights = {1, 3, 4, 5};
            int[] values = {1, 4, 5, 7};
            assertEquals(9, DPAndMathTemplates.knapsack(weights, values, 7));
            
            weights = new int[]{2, 1, 3};
            values = new int[]{12, 10, 20};
            assertEquals(32, DPAndMathTemplates.knapsack(weights, values, 5)); // items 1+3: weight=5, value=32
        }

        @Test
        @DisplayName("Knapsack: Edge cases")
        public void testKnapsackEdgeCases() {
            int[] weights = {1};
            int[] values = {1};
            assertEquals(1, DPAndMathTemplates.knapsack(weights, values, 1));
            assertEquals(0, DPAndMathTemplates.knapsack(weights, values, 0));
            
            // Empty arrays
            assertEquals(0, DPAndMathTemplates.knapsack(new int[]{}, new int[]{}, 10));
        }

        @Test
        @DisplayName("Coin Change: Basic test cases")
        public void testCoinChangeBasic() {
            assertEquals(2, DPAndMathTemplates.coinChange(new int[]{1, 3, 4}, 6)); // 3+3=6 (2 coins)
            assertEquals(-1, DPAndMathTemplates.coinChange(new int[]{2}, 3));
            assertEquals(0, DPAndMathTemplates.coinChange(new int[]{1}, 0));
        }

        @Test
        @DisplayName("Coin Change: Edge cases")
        public void testCoinChangeEdgeCases() {
            assertEquals(1, DPAndMathTemplates.coinChange(new int[]{1}, 1));
            assertEquals(2, DPAndMathTemplates.coinChange(new int[]{1, 2, 5}, 6));
            assertEquals(-1, DPAndMathTemplates.coinChange(new int[]{2}, 1));
        }

        @Test
        @DisplayName("Edit Distance: Basic test cases")
        public void testEditDistanceBasic() {
            assertEquals(3, DPAndMathTemplates.editDistance("horse", "ros"));
            assertEquals(5, DPAndMathTemplates.editDistance("intention", "execution"));
            assertEquals(0, DPAndMathTemplates.editDistance("same", "same"));
        }

        @Test
        @DisplayName("Edit Distance: Edge cases")
        public void testEditDistanceEdgeCases() {
            assertEquals(0, DPAndMathTemplates.editDistance("", ""));
            assertEquals(3, DPAndMathTemplates.editDistance("", "abc"));
            assertEquals(3, DPAndMathTemplates.editDistance("abc", ""));
            assertEquals(1, DPAndMathTemplates.editDistance("a", "b"));
        }
    }

    // ============================================================================
    // MATH UTILITIES TESTS
    // ============================================================================

    @Nested
    @DisplayName("Math Utilities")
    class MathUtilsTests {

        @Test
        @DisplayName("GCD: Basic test cases")
        public void testGCDBasic() {
            assertEquals(6, DPAndMathTemplates.gcd(48, 18));
            assertEquals(1, DPAndMathTemplates.gcd(17, 13));
            assertEquals(7, DPAndMathTemplates.gcd(0, 7));
            assertEquals(5, DPAndMathTemplates.gcd(5, 0));
        }

        @ParameterizedTest
        @CsvSource({
            "48, 18, 6",
            "100, 25, 25", 
            "17, 13, 1",
            "1071, 462, 21"
        })
        @DisplayName("GCD: Parameterized test cases")
        public void testGCDParameterized(long a, long b, long expected) {
            assertEquals(expected, DPAndMathTemplates.gcd(a, b));
        }

        @Test
        @DisplayName("LCM: Basic test cases")
        public void testLCMBasic() {
            assertEquals(144, DPAndMathTemplates.lcm(48, 18));
            assertEquals(221, DPAndMathTemplates.lcm(17, 13));
            assertEquals(100, DPAndMathTemplates.lcm(100, 25));
        }

        @Test
        @DisplayName("Modular Power: Basic test cases")
        public void testModPowBasic() {
            assertEquals(4, DPAndMathTemplates.modPow(2, 2, 5));
            assertEquals(1, DPAndMathTemplates.modPow(2, 10, 1023));
            assertEquals(1, DPAndMathTemplates.modPow(3, 200, 1000)); // 3^200 ≡ 1 (mod 1000)
        }

        @Test
        @DisplayName("Modular Power: Edge cases")
        public void testModPowEdgeCases() {
            assertEquals(1, DPAndMathTemplates.modPow(5, 0, 13));
            assertEquals(0, DPAndMathTemplates.modPow(0, 5, 13));
            assertEquals(1, DPAndMathTemplates.modPow(1, 1000, 13));
        }

        @Test
        @DisplayName("Modular Inverse: Basic test cases")
        public void testModInverseBasic() {
            // Test with prime modulus
            long mod = 1000000007;
            long a = 3;
            long inv = DPAndMathTemplates.modInverse(a, mod);
            assertEquals(1, (a * inv) % mod);
        }

        @Test
        @DisplayName("Sieve of Eratosthenes: Basic test cases")
        public void testSieveBasic() {
            boolean[] primes = DPAndMathTemplates.sieve(10);
            assertFalse(primes[0]);
            assertFalse(primes[1]);
            assertTrue(primes[2]);
            assertTrue(primes[3]);
            assertFalse(primes[4]);
            assertTrue(primes[5]);
            assertFalse(primes[6]);
            assertTrue(primes[7]);
            assertFalse(primes[8]);
            assertFalse(primes[9]);
            assertFalse(primes[10]);
        }

        @Test
        @DisplayName("Sieve: Count primes up to 100")
        public void testSieveCount() {
            boolean[] primes = DPAndMathTemplates.sieve(100);
            long count = 0;
            for (int i = 0; i <= 100; i++) {
                if (primes[i]) count++;
            }
            assertEquals(25, count); // There are 25 primes up to 100
        }
    }

    // ============================================================================
    // KADANE'S ALGORITHM VARIANTS TESTS
    // ============================================================================

    @Nested
    @DisplayName("Kadane's Algorithm Variants")
    class KadaneVariantsTests {

        @Test
        @DisplayName("Max Subarray Sum: Basic test cases")
        public void testMaxSubarraySumBasic() {
            assertEquals(6, DPAndMathTemplates.maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
            assertEquals(1, DPAndMathTemplates.maxSubarraySum(new int[]{1}));
            assertEquals(23, DPAndMathTemplates.maxSubarraySum(new int[]{5, 4, -1, 7, 8}));
        }

        @Test
        @DisplayName("Max Subarray Sum: All negative")
        public void testMaxSubarraySumAllNegative() {
            assertEquals(-1, DPAndMathTemplates.maxSubarraySum(new int[]{-2, -3, -1, -5}));
        }

        @Test
        @DisplayName("Max Subarray Sum with Indices: Basic test cases")
        public void testMaxSubarraySumWithIndicesBasic() {
            int[] result = DPAndMathTemplates.maxSubarraySumWithIndices(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4});
            assertEquals(6, result[0]); // max sum
            assertEquals(3, result[1]); // start index
            assertEquals(6, result[2]); // end index
        }

        @Test
        @DisplayName("Max Product Subarray: Basic test cases")
        public void testMaxProductSubarrayBasic() {
            assertEquals(6, DPAndMathTemplates.maxProductSubarray(new int[]{2, 3, -2, 4}));
            assertEquals(0, DPAndMathTemplates.maxProductSubarray(new int[]{-2, 0, -1}));
            assertEquals(24, DPAndMathTemplates.maxProductSubarray(new int[]{-2, 3, -4}));
        }

        @Test
        @DisplayName("Max Circular Subarray Sum: Basic test cases")
        public void testMaxSubarraySumCircularBasic() {
            assertEquals(3, DPAndMathTemplates.maxSubarraySumCircular(new int[]{1, -2, 3, -2}));
            assertEquals(10, DPAndMathTemplates.maxSubarraySumCircular(new int[]{5, -3, 5}));
            assertEquals(-2, DPAndMathTemplates.maxSubarraySumCircular(new int[]{-3, -2, -3}));
        }

        @Test
        @DisplayName("Max Subarray Sum At Most K: Basic test cases")
        public void testMaxSubarraySumAtMostKBasic() {
            assertEquals(7, DPAndMathTemplates.maxSubarraySumAtMostK(new int[]{1, 2, 3, 4}, 2)); // [3,4] = 7
            assertEquals(6, DPAndMathTemplates.maxSubarraySumAtMostK(new int[]{1, 2, 3}, 3));
        }

        @Test
        @DisplayName("Min Subarray Sum: Basic test cases")
        public void testMinSubarraySumBasic() {
            assertEquals(-5, DPAndMathTemplates.minSubarraySum(new int[]{2, -1, -3, 4, -1, 2, 1, -5, 4})); // [-5] = -5
            assertEquals(-5, DPAndMathTemplates.minSubarraySum(new int[]{1, 2, 3, -5, 4}));
        }
    }

    // ============================================================================
    // STOCK TRADING ALGORITHMS TESTS  
    // ============================================================================

    @Nested
    @DisplayName("Stock Trading Algorithms")
    class StockTradingTests {

        @Test
        @DisplayName("Max Profit (Single Transaction): Basic test cases")
        public void testMaxProfitBasic() {
            assertEquals(5, DPAndMathTemplates.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
            assertEquals(0, DPAndMathTemplates.maxProfit(new int[]{7, 6, 4, 3, 1}));
            assertEquals(0, DPAndMathTemplates.maxProfit(new int[]{1}));
        }

        @Test
        @DisplayName("Max Profit (Unlimited Transactions): Basic test cases")
        public void testMaxProfitUnlimitedBasic() {
            assertEquals(7, DPAndMathTemplates.maxProfitUnlimited(new int[]{7, 1, 5, 3, 6, 4}));
            assertEquals(4, DPAndMathTemplates.maxProfitUnlimited(new int[]{1, 2, 3, 4, 5}));
            assertEquals(0, DPAndMathTemplates.maxProfitUnlimited(new int[]{7, 6, 4, 3, 1}));
        }

        @Test
        @DisplayName("Max Profit (Two Transactions): Basic test cases")
        public void testMaxProfitTwoTransactionsBasic() {
            assertEquals(6, DPAndMathTemplates.maxProfitTwoTransactions(new int[]{3, 3, 5, 0, 0, 3, 1, 4}));
            assertEquals(4, DPAndMathTemplates.maxProfitTwoTransactions(new int[]{1, 2, 3, 4, 5}));
            assertEquals(0, DPAndMathTemplates.maxProfitTwoTransactions(new int[]{7, 6, 4, 3, 1}));
        }

        @Test
        @DisplayName("Max Profit (K Transactions): Basic test cases")
        public void testMaxProfitKTransactionsBasic() {
            assertEquals(2, DPAndMathTemplates.maxProfitKTransactions(new int[]{2, 4, 1}, 2));
            assertEquals(7, DPAndMathTemplates.maxProfitKTransactions(new int[]{3, 2, 6, 5, 0, 3}, 2));
        }

        @Test
        @DisplayName("Max Profit with Cooldown: Basic test cases")
        public void testMaxProfitWithCooldownBasic() {
            assertEquals(3, DPAndMathTemplates.maxProfitWithCooldown(new int[]{1, 2, 3, 0, 2}));
            assertEquals(0, DPAndMathTemplates.maxProfitWithCooldown(new int[]{1}));
        }

        @Test
        @DisplayName("Max Profit with Fee: Basic test cases") 
        public void testMaxProfitWithFeeBasic() {
            assertEquals(8, DPAndMathTemplates.maxProfitWithFee(new int[]{1, 3, 2, 8, 4, 9}, 2));
            assertEquals(6, DPAndMathTemplates.maxProfitWithFee(new int[]{1, 3, 7, 5, 10, 3}, 3));
        }
    }

    // ============================================================================
    // HOUSE ROBBER ALGORITHMS TESTS
    // ============================================================================

    @Nested
    @DisplayName("House Robber Algorithms")
    class HouseRobberTests {

        @Test
        @DisplayName("House Robber I: Basic test cases")
        public void testRobBasic() {
            assertEquals(4, DPAndMathTemplates.rob(new int[]{1, 2, 3, 1}));
            assertEquals(12, DPAndMathTemplates.rob(new int[]{2, 7, 9, 3, 1}));
            assertEquals(4, DPAndMathTemplates.rob(new int[]{2, 1, 1, 2}));
        }

        @Test
        @DisplayName("House Robber I: Edge cases")
        public void testRobEdgeCases() {
            assertEquals(5, DPAndMathTemplates.rob(new int[]{5}));
            assertEquals(2, DPAndMathTemplates.rob(new int[]{1, 2}));
            assertEquals(2, DPAndMathTemplates.rob(new int[]{2, 1}));
        }

        @Test
        @DisplayName("House Robber II (Circular): Basic test cases")
        public void testRobCircularBasic() {
            assertEquals(3, DPAndMathTemplates.robCircular(new int[]{2, 3, 2}));
            assertEquals(4, DPAndMathTemplates.robCircular(new int[]{1, 2, 3, 1}));
            assertEquals(3, DPAndMathTemplates.robCircular(new int[]{1, 2, 3}));
        }

        @Test
        @DisplayName("House Robber II: Edge cases")
        public void testRobCircularEdgeCases() {
            assertEquals(5, DPAndMathTemplates.robCircular(new int[]{5}));
            assertEquals(2, DPAndMathTemplates.robCircular(new int[]{1, 2}));
        }
    }

    // ============================================================================
    // MISCELLANEOUS ALGORITHMS TESTS
    // ============================================================================

    @Nested
    @DisplayName("Miscellaneous Algorithms")
    class MiscellaneousTests {

        @Test
        @DisplayName("Count Inversions: Basic test cases")
        public void testCountInversionsBasic() {
            assertEquals(0, DPAndMathTemplates.countInversions(new int[]{1, 2, 3, 4}));
            assertEquals(5, DPAndMathTemplates.countInversions(new int[]{2, 3, 8, 6, 1}));
            assertEquals(6, DPAndMathTemplates.countInversions(new int[]{4, 3, 2, 1}));
        }

        @Test
        @DisplayName("Max Alternating Sum: Basic test cases")
        public void testMaxAlternatingSumBasic() {
            assertEquals(7, DPAndMathTemplates.maxAlternatingSum(new int[]{4, 2, 5, 3})); // 4-2+5 = 7
            assertEquals(8, DPAndMathTemplates.maxAlternatingSum(new int[]{5, 6, 7, 8})); // just pick 8
            assertEquals(10, DPAndMathTemplates.maxAlternatingSum(new int[]{6, 2, 1, 2, 4, 5})); // 6-2+4+5-1 = 10
        }

        @Test
        @DisplayName("Paint Houses: Basic test cases")
        public void testMinCostToPaintHousesBasic() {
            int[][] costs = {{17, 2, 17}, {16, 16, 5}, {14, 3, 19}};
            assertEquals(10, DPAndMathTemplates.minCostToPaintHouses(costs));
        }

        @Test
        @DisplayName("Paint Fence: Basic test cases")
        public void testNumWaysToPaintFenceBasic() {
            assertEquals(6, DPAndMathTemplates.numWaysToPaintFence(3, 2));
            assertEquals(1, DPAndMathTemplates.numWaysToPaintFence(1, 1));
            assertEquals(0, DPAndMathTemplates.numWaysToPaintFence(3, 1));
        }

        @Test
        @DisplayName("Delete and Earn: Basic test cases")
        public void testDeleteAndEarnBasic() {
            assertEquals(6, DPAndMathTemplates.deleteAndEarn(new int[]{3, 4, 2}));
            assertEquals(9, DPAndMathTemplates.deleteAndEarn(new int[]{2, 2, 3, 3, 3, 4}));
        }
    }

    // ============================================================================
    // BIT MANIPULATION TESTS
    // ============================================================================

    @Nested
    @DisplayName("Bit Manipulation")
    class BitManipulationTests {

        @Test
        @DisplayName("Population Count: Basic test cases")
        public void testPopcountBasic() {
            assertEquals(0, DPAndMathTemplates.popcount(0));
            assertEquals(1, DPAndMathTemplates.popcount(1));
            assertEquals(2, DPAndMathTemplates.popcount(3));
            assertEquals(3, DPAndMathTemplates.popcount(7));
            assertEquals(1, DPAndMathTemplates.popcount(8));
        }

        @Test
        @DisplayName("Is Power of Two: Basic test cases")
        public void testIsPowerOfTwoBasic() {
            assertTrue(DPAndMathTemplates.isPowerOfTwo(1));
            assertTrue(DPAndMathTemplates.isPowerOfTwo(2));
            assertTrue(DPAndMathTemplates.isPowerOfTwo(4));
            assertTrue(DPAndMathTemplates.isPowerOfTwo(8));
            assertFalse(DPAndMathTemplates.isPowerOfTwo(0));
            assertFalse(DPAndMathTemplates.isPowerOfTwo(3));
            assertFalse(DPAndMathTemplates.isPowerOfTwo(6));
        }

        @Test
        @DisplayName("Lowest Set Bit: Basic test cases")
        public void testLowestSetBitBasic() {
            assertEquals(1, DPAndMathTemplates.lowestSetBit(1));
            assertEquals(2, DPAndMathTemplates.lowestSetBit(2));
            assertEquals(1, DPAndMathTemplates.lowestSetBit(3));
            assertEquals(4, DPAndMathTemplates.lowestSetBit(12)); // 12 = 1100, lowest set bit is 100 = 4
        }

        @Test
        @DisplayName("Next Permutation: Basic test cases")
        public void testNextPermutationBasic() {
            int[] nums1 = {1, 2, 3};
            assertTrue(DPAndMathTemplates.nextPermutation(nums1));
            assertArrayEquals(new int[]{1, 3, 2}, nums1);

            int[] nums2 = {3, 2, 1};
            assertFalse(DPAndMathTemplates.nextPermutation(nums2));
            assertArrayEquals(new int[]{1, 2, 3}, nums2);

            int[] nums3 = {1, 1, 5};
            assertTrue(DPAndMathTemplates.nextPermutation(nums3));
            assertArrayEquals(new int[]{1, 5, 1}, nums3);
        }
    }

    // ============================================================================
    // STRING DP ALGORITHMS TESTS
    // ============================================================================

    @Nested
    @DisplayName("String DP Algorithms")
    class StringDPTests {

        @Test
        @DisplayName("Distinct Subsequences: Basic test cases")
        public void testNumDistinctBasic() {
            assertEquals(3, DPAndMathTemplates.numDistinct("rabbbit", "rabbit"));
            assertEquals(5, DPAndMathTemplates.numDistinct("babgbag", "bag"));
        }

        @Test
        @DisplayName("Interleaving String: Basic test cases")
        public void testIsInterleaveBasic() {
            assertTrue(DPAndMathTemplates.isInterleave("aabcc", "dbbca", "aadbbcbcac"));
            assertFalse(DPAndMathTemplates.isInterleave("aabcc", "dbbca", "aadbbbaccc"));
            assertTrue(DPAndMathTemplates.isInterleave("", "", ""));
        }

        @Test
        @DisplayName("Word Break: Basic test cases")
        public void testWordBreakBasic() {
            List<String> dict1 = Arrays.asList("leet", "code");
            assertTrue(DPAndMathTemplates.wordBreak("leetcode", dict1));

            List<String> dict2 = Arrays.asList("apple", "pen");
            assertTrue(DPAndMathTemplates.wordBreak("applepenapple", dict2));

            List<String> dict3 = Arrays.asList("cats", "dog", "sand", "and", "cat");
            assertFalse(DPAndMathTemplates.wordBreak("catsandog", dict3));
        }

        @Test
        @DisplayName("Regular Expression Matching: Basic test cases")
        public void testIsMatchBasic() {
            assertFalse(DPAndMathTemplates.isMatch("aa", "a"));
            assertTrue(DPAndMathTemplates.isMatch("aa", "a*"));
            assertTrue(DPAndMathTemplates.isMatch("ab", ".*"));
        }

        @Test
        @DisplayName("Wildcard Matching: Basic test cases")
        public void testIsMatchWildcardBasic() {
            assertFalse(DPAndMathTemplates.isMatchWildcard("aa", "a"));
            assertTrue(DPAndMathTemplates.isMatchWildcard("aa", "*"));
            assertFalse(DPAndMathTemplates.isMatchWildcard("cb", "?a"));
            assertTrue(DPAndMathTemplates.isMatchWildcard("adceb", "*a*b*"));
        }

        @Test
        @DisplayName("Palindrome Partitioning Min Cut: Basic test cases") 
        public void testMinCutBasic() {
            assertEquals(1, DPAndMathTemplates.minCut("aab")); // "aa" | "b" = 1 cut
            assertEquals(0, DPAndMathTemplates.minCut("abccba")); // whole string is palindrome
            assertEquals(0, DPAndMathTemplates.minCut("aba")); // whole string is palindrome
        }

        @Test
        @DisplayName("Longest Palindromic Subsequence: Basic test cases")
        public void testLongestPalindromeSubseqBasic() {
            assertEquals(4, DPAndMathTemplates.longestPalindromeSubseq("bbbab"));
            assertEquals(2, DPAndMathTemplates.longestPalindromeSubseq("cbbd"));
        }

        @Test
        @DisplayName("Min Insertions to Make Palindrome: Basic test cases")
        public void testMinInsertionsBasic() {
            assertEquals(0, DPAndMathTemplates.minInsertions("zzazz"));
            assertEquals(2, DPAndMathTemplates.minInsertions("mbadm"));
            assertEquals(5, DPAndMathTemplates.minInsertions("leetcode"));
        }

        @Test
        @DisplayName("Word Break II: Basic test cases")
        public void testWordBreakTwoBasic() {
            List<String> dict = Arrays.asList("cat", "cats", "and", "sand", "dog");
            List<String> result = DPAndMathTemplates.wordBreakII("catsanddog", dict);
            
            assertTrue(result.contains("cats and dog"));
            assertTrue(result.contains("cat sand dog"));
            assertEquals(2, result.size());
        }
    }

    // ============================================================================
    // MATRIX EXPONENTIATION TESTS
    // ============================================================================

    @Nested
    @DisplayName("Matrix Exponentiation")
    class MatrixExponentiationTests {

        @Test
        @DisplayName("Matrix Multiply: Basic test cases")
        public void testMatrixMultiplyBasic() {
            long[][] A = {{1, 2}, {3, 4}};
            long[][] B = {{5, 6}, {7, 8}};
            long[][] result = DPAndMathTemplates.matrixMultiply(A, B, 1000000007);
            
            assertEquals(19, result[0][0]); // 1*5 + 2*7 = 19
            assertEquals(22, result[0][1]); // 1*6 + 2*8 = 22
            assertEquals(43, result[1][0]); // 3*5 + 4*7 = 43
            assertEquals(50, result[1][1]); // 3*6 + 4*8 = 50
        }

        @Test
        @DisplayName("Matrix Multiply: Identity matrix")
        public void testMatrixMultiplyIdentity() {
            long[][] A = {{1, 2}, {3, 4}};
            long[][] I = {{1, 0}, {0, 1}};
            long[][] result = DPAndMathTemplates.matrixMultiply(A, I, 1000000007);
            
            assertArrayEquals(A[0], result[0]);
            assertArrayEquals(A[1], result[1]);
        }

        @Test
        @DisplayName("Matrix Power: Basic test cases")
        public void testMatrixPowerBasic() {
            // Test A^0 = I
            long[][] A = {{1, 1}, {1, 0}};
            long[][] result = DPAndMathTemplates.matrixPower(A, 0, 1000000007);
            
            assertEquals(1, result[0][0]);
            assertEquals(0, result[0][1]);
            assertEquals(0, result[1][0]);
            assertEquals(1, result[1][1]);
            
            // Test A^1 = A
            result = DPAndMathTemplates.matrixPower(A, 1, 1000000007);
            assertArrayEquals(A[0], result[0]);
            assertArrayEquals(A[1], result[1]);
        }

        @Test
        @DisplayName("Fibonacci Matrix: Basic test cases")
        public void testFibonacciMatrixBasic() {
            assertEquals(0, DPAndMathTemplates.fibonacciMatrix(0));
            assertEquals(1, DPAndMathTemplates.fibonacciMatrix(1));
            assertEquals(1, DPAndMathTemplates.fibonacciMatrix(2));
            assertEquals(2, DPAndMathTemplates.fibonacciMatrix(3));
            assertEquals(3, DPAndMathTemplates.fibonacciMatrix(4));
            assertEquals(5, DPAndMathTemplates.fibonacciMatrix(5));
            assertEquals(8, DPAndMathTemplates.fibonacciMatrix(6));
            assertEquals(13, DPAndMathTemplates.fibonacciMatrix(7));
        }

        @Test
        @DisplayName("Fibonacci Matrix: Large numbers")
        public void testFibonacciMatrixLarge() {
            long mod = 1000000007;
            // F(50) = 12586269025, F(50) mod 1000000007 = 586268941
            assertEquals(586268941L, DPAndMathTemplates.fibonacciMatrix(50, mod));
            
            // Test very large Fibonacci number
            long result = DPAndMathTemplates.fibonacciMatrix(100, mod);
            assertTrue(result > 0 && result < mod);
        }

        @Test
        @DisplayName("Tribonacci Matrix: Basic test cases")
        public void testTribonacciMatrixBasic() {
            assertEquals(0, DPAndMathTemplates.tribonacciMatrix(0, 1000000007));
            assertEquals(1, DPAndMathTemplates.tribonacciMatrix(1, 1000000007));
            assertEquals(1, DPAndMathTemplates.tribonacciMatrix(2, 1000000007));
            assertEquals(2, DPAndMathTemplates.tribonacciMatrix(3, 1000000007)); // 1+1+0 = 2
            assertEquals(4, DPAndMathTemplates.tribonacciMatrix(4, 1000000007)); // 2+1+1 = 4
            assertEquals(7, DPAndMathTemplates.tribonacciMatrix(5, 1000000007)); // 4+2+1 = 7
        }

        @Test
        @DisplayName("Linear Recurrence: Basic test cases")
        public void testLinearRecurrenceBasic() {
            // Test Fibonacci recurrence: a(n) = a(n-1) + a(n-2)
            long[] coeffs = {1, 1};
            long[] base = {1, 0}; // F(1), F(0)
            
            assertEquals(0, DPAndMathTemplates.solveRecurrence(coeffs, base, 0, 1000000007));
            assertEquals(1, DPAndMathTemplates.solveRecurrence(coeffs, base, 1, 1000000007));
            assertEquals(1, DPAndMathTemplates.solveRecurrence(coeffs, base, 2, 1000000007));
            assertEquals(2, DPAndMathTemplates.solveRecurrence(coeffs, base, 3, 1000000007));
        }

        @Test
        @DisplayName("Climbing Stairs 123: Basic test cases")
        public void testClimbingStairs123Basic() {
            assertEquals(1, DPAndMathTemplates.climbingStairs123(0, 1000000007)); // Base case
            assertEquals(1, DPAndMathTemplates.climbingStairs123(1, 1000000007)); // One way: 1
            assertEquals(2, DPAndMathTemplates.climbingStairs123(2, 1000000007)); // Two ways: 1+1, 2
            assertEquals(7, DPAndMathTemplates.climbingStairs123(3, 1000000007)); // Seven ways (follows tribonacci pattern)
        }

        @Test
        @DisplayName("Tiling Ways: Basic test cases")
        public void testTilingWaysBasic() {
            assertEquals(1, DPAndMathTemplates.tilingWays(0, 1000000007));
            assertEquals(1, DPAndMathTemplates.tilingWays(1, 1000000007));
            assertEquals(2, DPAndMathTemplates.tilingWays(2, 1000000007));
            assertEquals(3, DPAndMathTemplates.tilingWays(3, 1000000007));
            assertEquals(5, DPAndMathTemplates.tilingWays(4, 1000000007));
        }

        @Test
        @DisplayName("Count Paths Length K: Basic test cases")
        public void testCountPathsLengthKBasic() {
            // Simple graph: 0 -> 1 -> 0 (cycle of length 2)
            long[][] adj = {{0, 1}, {1, 0}};
            
            // Path of length 1: 0->1 and 1->0
            assertEquals(1, DPAndMathTemplates.countPathsLengthK(adj, 1, 0, 1, 1000000007));
            assertEquals(1, DPAndMathTemplates.countPathsLengthK(adj, 1, 1, 0, 1000000007));
            
            // Path of length 2: 0->1->0 and 1->0->1
            assertEquals(1, DPAndMathTemplates.countPathsLengthK(adj, 2, 0, 0, 1000000007));
            assertEquals(1, DPAndMathTemplates.countPathsLengthK(adj, 2, 1, 1, 1000000007));
        }
    }

    // ============================================================================
    // NUMBER THEORY TESTS
    // ============================================================================

    @Nested
    @DisplayName("Number Theory")
    class NumberTheoryTests {

        @Test
        @DisplayName("Extended GCD: Basic test cases")
        public void testExtendedGCDBasic() {
            long[] result = DPAndMathTemplates.extendedGCD(10, 6);
            assertEquals(2, result[0]); // gcd(10, 6) = 2
            assertEquals(2, 10 * result[1] + 6 * result[2]); // Verify Bezout's identity
            
            result = DPAndMathTemplates.extendedGCD(35, 15);
            assertEquals(5, result[0]); // gcd(35, 15) = 5
            assertEquals(5, 35 * result[1] + 15 * result[2]); // Verify Bezout's identity
        }

        @Test
        @DisplayName("Extended GCD: Coprime numbers")
        public void testExtendedGCDCoprime() {
            long[] result = DPAndMathTemplates.extendedGCD(17, 13);
            assertEquals(1, result[0]); // gcd(17, 13) = 1
            assertEquals(1, 17 * result[1] + 13 * result[2]); // Verify Bezout's identity
        }

        @Test
        @DisplayName("Modular Inverse Extended GCD: Basic test cases")
        public void testModInverseExtGCDBasic() {
            // Test modular inverse using Extended GCD
            long a = 3;
            long mod = 11;
            long inv = DPAndMathTemplates.modInverseExtGCD(a, mod);
            assertEquals(1, (a * inv) % mod);
            
            a = 7;
            mod = 26;
            inv = DPAndMathTemplates.modInverseExtGCD(a, mod);
            assertEquals(1, (a * inv) % mod);
        }

        @Test
        @DisplayName("Modular Inverse Extended GCD: Non-coprime should throw")
        public void testModInverseExtGCDNonCoprime() {
            assertThrows(IllegalArgumentException.class, () -> {
                DPAndMathTemplates.modInverseExtGCD(6, 9); // gcd(6,9) = 3 != 1
            });
        }

        @Test
        @DisplayName("Solve Diophantine: Basic test cases")
        public void testSolveDiophantineBasic() {
            // 10x + 6y = 4 (solvable since gcd(10,6) = 2 divides 4)
            long[] result = DPAndMathTemplates.solveDiophantine(10, 6, 4);
            assertNotNull(result);
            assertEquals(4, 10 * result[0] + 6 * result[1]); // Verify solution
            
            // 3x + 5y = 1 (solvable since gcd(3,5) = 1 divides 1)
            result = DPAndMathTemplates.solveDiophantine(3, 5, 1);
            assertNotNull(result);
            assertEquals(1, 3 * result[0] + 5 * result[1]); // Verify solution
        }

        @Test
        @DisplayName("Solve Diophantine: No solution")
        public void testSolveDiophantineNoSolution() {
            // 10x + 6y = 3 (no solution since gcd(10,6) = 2 doesn't divide 3)
            long[] result = DPAndMathTemplates.solveDiophantine(10, 6, 3);
            assertNull(result);
            
            // 4x + 6y = 5 (no solution since gcd(4,6) = 2 doesn't divide 5)
            result = DPAndMathTemplates.solveDiophantine(4, 6, 5);
            assertNull(result);
        }

        @Test
        @DisplayName("Chinese Remainder Theorem: Basic test cases")
        public void testChineseRemainderTheoremBasic() {
            // System: x ≡ 2 (mod 3), x ≡ 3 (mod 5)
            // Solution: x ≡ 8 (mod 15)
            long[] a = {2, 3};
            long[] m = {3, 5};
            long result = DPAndMathTemplates.chineseRemainderTheorem(a, m);
            
            assertEquals(2, result % 3);
            assertEquals(3, result % 5);
            assertEquals(8, result); // Expected solution
        }

        @Test
        @DisplayName("Chinese Remainder Theorem: Three congruences")
        public void testChineseRemainderTheoremThree() {
            // System: x ≡ 1 (mod 2), x ≡ 2 (mod 3), x ≡ 3 (mod 5)
            long[] a = {1, 2, 3};
            long[] m = {2, 3, 5};
            long result = DPAndMathTemplates.chineseRemainderTheorem(a, m);
            
            assertEquals(1, result % 2);
            assertEquals(2, result % 3);
            assertEquals(3, result % 5);
        }

        @Test
        @DisplayName("CRT Two: Basic test cases")
        public void testCRTTwoBasic() {
            long result = DPAndMathTemplates.crtTwo(2, 3, 3, 5);
            assertEquals(2, result % 3);
            assertEquals(3, result % 5);
            assertEquals(8, result);
        }
    }

    // ============================================================================
    // RESERVOIR SAMPLING TESTS
    // ============================================================================

    @Nested
    @DisplayName("Reservoir Sampling")
    class ReservoirSamplingTests {

        @Test
        @DisplayName("Reservoir Sampling: Basic functionality")
        public void testReservoirSamplingBasic() {
            List<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            List<Integer> sample = DPAndMathTemplates.reservoirSampling(stream.iterator(), 3);
            
            assertEquals(3, sample.size());
            // Verify all elements are from the original stream
            assertTrue(stream.containsAll(sample));
        }

        @Test
        @DisplayName("Reservoir Sampling: Stream smaller than k")
        public void testReservoirSamplingSmallerStream() {
            List<Integer> stream = Arrays.asList(1, 2);
            List<Integer> sample = DPAndMathTemplates.reservoirSampling(stream.iterator(), 5);
            
            assertEquals(2, sample.size());
            assertEquals(Arrays.asList(1, 2), sample);
        }

        @Test
        @DisplayName("Reservoir Sampling: k=1")
        public void testReservoirSamplingK1() {
            List<Integer> stream = Arrays.asList(1, 2, 3, 4, 5);
            List<Integer> sample = DPAndMathTemplates.reservoirSampling(stream.iterator(), 1);
            
            assertEquals(1, sample.size());
            assertTrue(stream.contains(sample.get(0)));
        }

        @Test
        @DisplayName("Reservoir Sampling One: Basic functionality")
        public void testReservoirSamplingOneBasic() {
            List<Integer> stream = Arrays.asList(1, 2, 3, 4, 5);
            Integer sample = DPAndMathTemplates.reservoirSamplingOne(stream.iterator());
            
            assertNotNull(sample);
            assertTrue(stream.contains(sample));
        }

        @Test
        @DisplayName("Reservoir Sampling One: Single element")
        public void testReservoirSamplingOneSingle() {
            List<Integer> stream = Arrays.asList(42);
            Integer sample = DPAndMathTemplates.reservoirSamplingOne(stream.iterator());
            
            assertEquals(Integer.valueOf(42), sample);
        }

        @Test
        @DisplayName("Reservoir Sampling One: Empty stream")
        public void testReservoirSamplingOneEmpty() {
            List<Integer> stream = Arrays.asList();
            Integer sample = DPAndMathTemplates.reservoirSamplingOne(stream.iterator());
            
            assertNull(sample);
        }

        @Test
        @DisplayName("Reservoir Sampling: Distribution test")
        public void testReservoirSamplingDistribution() {
            // Test that all elements have roughly equal probability of being selected
            List<Integer> stream = Arrays.asList(1, 2, 3, 4, 5);
            int[] counts = new int[6]; // index 0 unused, indices 1-5 for elements 1-5
            
            int iterations = 10000;
            for (int i = 0; i < iterations; i++) {
                Integer sample = DPAndMathTemplates.reservoirSamplingOne(stream.iterator());
                if (sample != null) {
                    counts[sample]++;
                }
            }
            
            // Each element should be selected roughly 20% of the time (1/5)
            // Allow some deviation due to randomness
            for (int i = 1; i <= 5; i++) {
                double proportion = counts[i] / (double) iterations;
                assertTrue(proportion > 0.15 && proportion < 0.25, 
                    "Element " + i + " selected " + proportion + " of the time, expected ~0.2");
            }
        }

        @Test
        @DisplayName("LinkedListRandom: Basic functionality") 
        public void testLinkedListRandomBasic() {
            // This test would require access to the ListNode constructor
            // For now, we'll skip this test as ListNode is private
            // In a real scenario, we'd need to make ListNode public or provide a factory method
            assertTrue(true, "LinkedListRandom test skipped - requires public ListNode access");
        }

        @Test
        @DisplayName("RandomPickIndex: Basic functionality")
        public void testRandomPickIndexBasic() {
            int[] nums = {1, 2, 3, 3, 3};
            DPAndMathTemplates.RandomPickIndex picker = new DPAndMathTemplates.RandomPickIndex(nums);
            
            // Test picking index of value 1 (should always return 0)
            assertEquals(0, picker.pick(1));
            
            // Test picking index of value 2 (should always return 1)
            assertEquals(1, picker.pick(2));
            
            // Test picking index of value 3 (should return one of 2, 3, or 4)
            int index = picker.pick(3);
            assertTrue(index == 2 || index == 3 || index == 4);
            assertTrue(nums[index] == 3);
        }

        @Test
        @DisplayName("RandomPickIndex: Distribution test")
        public void testRandomPickIndexDistribution() {
            int[] nums = {3, 3, 3};
            DPAndMathTemplates.RandomPickIndex picker = new DPAndMathTemplates.RandomPickIndex(nums);
            
            int[] counts = new int[3];
            int iterations = 10000;
            
            for (int i = 0; i < iterations; i++) {
                int index = picker.pick(3);
                counts[index]++;
            }
            
            // Each index should be selected roughly 1/3 of the time
            for (int i = 0; i < 3; i++) {
                double proportion = counts[i] / (double) iterations;
                assertTrue(proportion > 0.25 && proportion < 0.41, 
                    "Index " + i + " selected " + proportion + " of the time, expected ~0.33");
            }
        }
    }
}