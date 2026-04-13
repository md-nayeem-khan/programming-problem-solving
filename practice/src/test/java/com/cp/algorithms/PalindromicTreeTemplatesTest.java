package com.cp.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Palindromic Tree (Eertree) implementation
 * Tests cover:
 * - Core palindromic tree building and traversal
 * - Palindrome counting (total and distinct)
 * - LeetCode problem implementations (647, etc.)
 * - Advanced query operations
 * - Edge cases and boundary conditions
 * - Performance and complexity validation
 * - DP optimizations and applications
 * - Variations (case-insensitive, alphanumeric)
 */
@DisplayName("Palindromic Tree (Eertree) Tests")
class PalindromicTreeTemplatesTest {

    @Nested
    @DisplayName("Basic Palindromic Tree Construction")
    class BasicTreeTests {

        @Test
        @DisplayName("Empty string should build valid tree")
        void testEmptyString() {
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree("");
            
            assertEquals(0, tree.countAllPalindromes());
            assertEquals(0, tree.countDistinctPalindromes());
            assertTrue(tree.getAllPalindromes().isEmpty());
        }

        @Test
        @DisplayName("Single character should create one palindrome")
        void testSingleCharacter() {
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree("a");
            
            assertEquals(1, tree.countAllPalindromes());
            assertEquals(1, tree.countDistinctPalindromes());
            assertEquals(Arrays.asList("a"), tree.getAllPalindromes());
        }

        @Test
        @DisplayName("Two identical characters")
        void testTwoIdenticalChars() {
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree("aa");
            
            assertEquals(3, tree.countAllPalindromes()); // "a", "a", "aa"
            assertEquals(2, tree.countDistinctPalindromes()); // "a", "aa"
            
            List<String> palindromes = tree.getAllPalindromes();
            assertTrue(palindromes.contains("a"));
            assertTrue(palindromes.contains("aa"));
        }

        @Test
        @DisplayName("Classic example: abaaba")
        void testClassicExample() {
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree("abaaba");
            
            assertEquals(11, tree.countAllPalindromes());
            assertEquals(6, tree.countDistinctPalindromes());
            
            List<String> palindromes = tree.getAllPalindromes();
            assertTrue(palindromes.contains("a"));
            assertTrue(palindromes.contains("b"));
            assertTrue(palindromes.contains("aba"));
            assertTrue(palindromes.contains("aa"));
            assertTrue(palindromes.contains("baab"));
            assertTrue(palindromes.contains("abaaba"));
        }

        @Test
        @DisplayName("Palindrome frequencies")
        void testPalindromeFrequencies() {
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree("ababa");
            
            Map<String, Long> frequencies = tree.getPalindromeFrequencies();
            
            assertEquals(3L, frequencies.get("a")); // appears 3 times
            assertEquals(2L, frequencies.get("b")); // appears 2 times
            assertEquals(2L, frequencies.get("aba")); // appears 2 times
            assertEquals(1L, frequencies.get("bab")); // appears 1 time
            assertEquals(1L, frequencies.get("ababa")); // appears 1 time
        }
    }

    @Nested
    @DisplayName("LeetCode Problem Solutions")
    class LeetCodeTests {

        @Test
        @DisplayName("LeetCode 647: Palindromic Substrings - Basic cases")
        void testLeetCode647Basic() {
            assertEquals(3, PalindromicTreeTemplates.PalindromeCounter.countSubstrings("abc"));
            assertEquals(6, PalindromicTreeTemplates.PalindromeCounter.countSubstrings("aaa"));
        }

        @Test
        @DisplayName("LeetCode 647: Palindromic Substrings - Complex cases")
        void testLeetCode647Complex() {
            assertEquals(10, PalindromicTreeTemplates.PalindromeCounter.countSubstrings("racecar"));
            assertEquals(1, PalindromicTreeTemplates.PalindromeCounter.countSubstrings("a"));
            assertEquals(0, PalindromicTreeTemplates.PalindromeCounter.countSubstrings(""));
        }

        @ParameterizedTest
        @ValueSource(strings = {"a", "aa", "aba", "abcba", "racecar", "madam"})
        @DisplayName("Single palindromes should return correct counts")
        void testSinglePalindromes(String s) {
            int count = PalindromicTreeTemplates.PalindromeCounter.countSubstrings(s);
            assertTrue(count >= s.length()); // At least n single character palindromes
        }

        @Test
        @DisplayName("Longest palindrome detection")
        void testLongestPalindrome() {
            assertEquals("bab", PalindromicTreeTemplates.PalindromeCounter.longestPalindrome("babad"));
            assertEquals("bb", PalindromicTreeTemplates.PalindromeCounter.longestPalindrome("cbbd"));
            assertEquals("a", PalindromicTreeTemplates.PalindromeCounter.longestPalindrome("ac"));
            assertEquals("", PalindromicTreeTemplates.PalindromeCounter.longestPalindrome(""));
        }
    }

    @Nested
    @DisplayName("Advanced Query Operations")
    class QueryTests {

        private PalindromicTreeTemplates.PalindromeQueries queries;

        @BeforeEach
        void setUp() {
            queries = new PalindromicTreeTemplates.PalindromeQueries("abacabad");
        }

        @Test
        @DisplayName("Palindrome existence queries")
        void testIsPalindrome() {
            assertTrue(queries.isPalindrome(0, 2)); // "aba"
            assertTrue(queries.isPalindrome(2, 4)); // "aca"
            assertFalse(queries.isPalindrome(1, 3)); // "bac"
            assertFalse(queries.isPalindrome(0, 7)); // entire string
        }

        @Test
        @DisplayName("Palindromes starting at position")
        void testPalindromesStartingAt() {
            assertTrue(queries.countPalindromesStartingAt(0) >= 1); // At least "a"
            assertTrue(queries.countPalindromesStartingAt(2) >= 1); // At least "a"
        }

        @Test
        @DisplayName("Palindromes ending at position")
        void testPalindromesEndingAt() {
            assertTrue(queries.countPalindromesEndingAt(0) >= 1); // At least "a"
            assertTrue(queries.countPalindromesEndingAt(7) >= 1); // At least "d"
        }

        @Test
        @DisplayName("Palindromes containing position")
        void testPalindromesContaining() {
            List<String> containing2 = queries.getPalindromesContaining(2);
            assertTrue(containing2.contains("a")); // Single character
            assertTrue(containing2.contains("aba")); // Position 2 is in "aba" (0-2)
        }

        @Test
        @DisplayName("Query boundary conditions")
        void testQueryBoundaries() {
            assertFalse(queries.isPalindrome(-1, 0));
            assertFalse(queries.isPalindrome(0, 10));
            assertFalse(queries.isPalindrome(5, 3));
            
            assertEquals(0, queries.countPalindromesStartingAt(-1));
            assertEquals(0, queries.countPalindromesStartingAt(10));
            
            assertTrue(queries.getPalindromesContaining(-1).isEmpty());
            assertTrue(queries.getPalindromesContaining(10).isEmpty());
        }
    }

    @Nested
    @DisplayName("Palindrome Counting by Length")
    class CountingTests {

        @Test
        @DisplayName("Count palindromes by specific length")
        void testCountByLength() {
            String s = "abaaba";
            
            long length1 = PalindromicTreeTemplates.PalindromeCounter
                .countPalindromesByLength(s, 1);
            long length2 = PalindromicTreeTemplates.PalindromeCounter
                .countPalindromesByLength(s, 2);
            long length3 = PalindromicTreeTemplates.PalindromeCounter
                .countPalindromesByLength(s, 3);
            long length6 = PalindromicTreeTemplates.PalindromeCounter
                .countPalindromesByLength(s, 6);
            
            assertTrue(length1 >= 4); // At least "a", "b", "a", "a", "b", "a"
            assertTrue(length2 >= 1); // At least "aa"
            assertTrue(length3 >= 2); // At least two "aba"
            assertEquals(1, length6); // Only "abaaba"
        }

        @Test
        @DisplayName("Count palindromes - invalid length")
        void testCountInvalidLength() {
            assertEquals(0, PalindromicTreeTemplates.PalindromeCounter
                .countPalindromesByLength("abc", 0));
            assertEquals(0, PalindromicTreeTemplates.PalindromeCounter
                .countPalindromesByLength("abc", -1));
            assertEquals(0, PalindromicTreeTemplates.PalindromeCounter
                .countPalindromesByLength("abc", 10));
        }
    }

    @Nested
    @DisplayName("DP Optimization Applications")
    class DPOptimizationTests {

        @Test
        @DisplayName("Minimum cuts for palindrome partitioning")
        void testMinCuts() {
            assertEquals(1, PalindromicTreeTemplates.PalindromeDPOptimizations.minCuts("aab")); // ["aa", "b"] = 1 cut
            assertEquals(0, PalindromicTreeTemplates.PalindromeDPOptimizations.minCuts("aabaa")); // "aabaa" is palindrome = 0 cuts  
            assertEquals(4, PalindromicTreeTemplates.PalindromeDPOptimizations.minCuts("abcde")); // ["a","b","c","d","e"] = 4 cuts
        }

        @Test
        @DisplayName("Count palindrome partitions")
        void testCountPartitions() {
            assertEquals(1, PalindromicTreeTemplates.PalindromeDPOptimizations.countPartitions(""));
            assertEquals(1, PalindromicTreeTemplates.PalindromeDPOptimizations.countPartitions("a"));
            assertTrue(PalindromicTreeTemplates.PalindromeDPOptimizations.countPartitions("aab") >= 1);
        }

        @Test
        @DisplayName("Can partition into palindromes")
        void testCanPartition() {
            assertTrue(PalindromicTreeTemplates.PalindromeCounter.canPartitionIntoPalindromes(""));
            assertTrue(PalindromicTreeTemplates.PalindromeCounter.canPartitionIntoPalindromes("a"));
            assertTrue(PalindromicTreeTemplates.PalindromeCounter.canPartitionIntoPalindromes("aab"));
            assertTrue(PalindromicTreeTemplates.PalindromeCounter.canPartitionIntoPalindromes("racecar"));
        }
    }

    @Nested
    @DisplayName("Palindrome Tree Variations")
    class VariationTests {

        @Test
        @DisplayName("Multiple string processing")
        void testMultipleStrings() {
            List<String> strings = Arrays.asList("aba", "bab", "cdc");
            List<PalindromicTreeTemplates.PalindromicTree> trees = 
                PalindromicTreeTemplates.PalindromeVariations.buildMultiple(strings);
            
            assertEquals(3, trees.size());
            for (PalindromicTreeTemplates.PalindromicTree tree : trees) {
                assertTrue(tree.countAllPalindromes() > 0);
            }
        }

        @Test
        @DisplayName("Common palindromes between strings")
        void testCommonPalindromes() {
            List<String> strings = Arrays.asList("ababa", "babab", "abcba");
            Set<String> common = PalindromicTreeTemplates.PalindromeVariations
                .findCommonPalindromes(strings);
            
            assertTrue(common.contains("a"));
            assertTrue(common.contains("b"));
            // May contain other single characters depending on implementation
        }

        @Test
        @DisplayName("Case insensitive palindromes")
        void testCaseInsensitive() {
            PalindromicTreeTemplates.PalindromicTree tree = 
                PalindromicTreeTemplates.PalindromeVariations.buildCaseInsensitive("RaceCar");
            
            assertTrue(tree.countAllPalindromes() >= 7); // At least the individual chars + "racecar"
        }

        @Test
        @DisplayName("Alphanumeric palindromes")
        void testAlphanumericPalindromes() {
            long count1 = PalindromicTreeTemplates.PalindromeVariations
                .countAlphanumericPalindromes("A man, a plan, a canal: Panama");
            assertTrue(count1 > 0);
            
            long count2 = PalindromicTreeTemplates.PalindromeVariations
                .countAlphanumericPalindromes("race a car");
            assertTrue(count2 > 0);
            
            long count3 = PalindromicTreeTemplates.PalindromeVariations
                .countAlphanumericPalindromes("");
            assertEquals(0, count3);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Conditions")
    class EdgeCaseTests {

        @Test
        @DisplayName("All identical characters")
        void testAllIdentical() {
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree("aaaa");
            
            // Should have palindromes: "a" (4 times), "aa" (3 times), "aaa" (2 times), "aaaa" (1 time)
            assertEquals(10, tree.countAllPalindromes()); // 4+3+2+1 = 10
            assertEquals(4, tree.countDistinctPalindromes()); // "a", "aa", "aaa", "aaaa"
        }

        @Test
        @DisplayName("No palindromes longer than 1")
        void testNoLongPalindromes() {
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree("abcd");
            
            assertEquals(4, tree.countAllPalindromes()); // Only single characters
            assertEquals(4, tree.countDistinctPalindromes()); // "a", "b", "c", "d"
        }

        @Test
        @DisplayName("Alternating characters")
        void testAlternatingChars() {
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree("abab");
            
            assertTrue(tree.countAllPalindromes() >= 4); // At least the single characters
            List<String> palindromes = tree.getAllPalindromes();
            assertTrue(palindromes.contains("a"));
            assertTrue(palindromes.contains("b"));
        }

        @Test
        @DisplayName("Very long palindrome")
        void testLongPalindrome() {
            String palindrome = "abcdefghijklmnopqrstuvwxyzzyxwvutsrqponmlkjihgfedcba";
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree(palindrome);
            
            assertTrue(tree.getAllPalindromes().contains(palindrome));
            assertTrue(tree.countAllPalindromes() >= palindrome.length());
        }

        @Test
        @DisplayName("Null and empty input handling")
        void testNullAndEmpty() {
            // Test null handling would require modifications to original code
            // For now, test empty string
            assertEquals(0, PalindromicTreeTemplates.PalindromeCounter.countSubstrings(""));
            assertEquals("", PalindromicTreeTemplates.PalindromeCounter.longestPalindrome(""));
        }
    }

    @Nested
    @DisplayName("Performance and Complexity Validation")
    class PerformanceTests {

        @Test
        @DisplayName("Large input performance - O(n) complexity")
        void testLargeInputPerformance() {
            // Create a moderately large string for testing
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                sb.append((char)('a' + (i % 3))); // Pattern: abcabcabc...
            }
            String largeString = sb.toString();
            
            long startTime = System.currentTimeMillis();
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree(largeString);
            long endTime = System.currentTimeMillis();
            
            assertTrue(tree.countAllPalindromes() > 0);
            assertTrue(endTime - startTime < 5000); // Should complete in reasonable time
        }

        @Test
        @DisplayName("Memory usage - linear space complexity")
        void testMemoryUsage() {
            String input = "abcabcabc";
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree(input);
            
            // Tree should not create more nodes than necessary
            assertTrue(tree.countDistinctPalindromes() <= input.length());
            assertNotNull(tree.getAllNodes());
        }
    }

    @Nested
    @DisplayName("Correctness Validation Against Known Results")
    class CorrectnessTests {

        private static Stream<Arguments> palindromeTestCases() {
            return Stream.of(
                Arguments.of("a", 1, 1),
                Arguments.of("ab", 2, 2),
                Arguments.of("aa", 3, 2),
                Arguments.of("aba", 4, 3),
                Arguments.of("abab", 6, 4),
                Arguments.of("aaa", 6, 3),
                Arguments.of("racecar", 10, 7) // 7 distinct: r,a,c,e,cec,aceca,racecar
            );
        }

        @ParameterizedTest
        @MethodSource("palindromeTestCases")
        @DisplayName("Validate palindrome counts against expected values")
        void testPalindromeCounts(String input, int expectedTotal, int expectedDistinct) {
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree(input);
            
            assertEquals(expectedTotal, tree.countAllPalindromes(), 
                "Total palindrome count mismatch for: " + input);
            assertEquals(expectedDistinct, tree.countDistinctPalindromes(), 
                "Distinct palindrome count mismatch for: " + input);
        }

        @Test
        @DisplayName("Validate against manual counting")
        void testManualValidation() {
            // For "abcba": palindromes are a, b, c, b, a, bcb, abcba
            String s = "abcba";
            PalindromicTreeTemplates.PalindromicTree tree = 
                new PalindromicTreeTemplates.PalindromicTree(s);
            
            List<String> palindromes = tree.getAllPalindromes();
            assertTrue(palindromes.contains("a"));
            assertTrue(palindromes.contains("b"));
            assertTrue(palindromes.contains("c"));
            assertTrue(palindromes.contains("bcb"));
            assertTrue(palindromes.contains("abcba"));
            
            assertEquals(7, tree.countAllPalindromes()); // a(2) + b(2) + c(1) + bcb(1) + abcba(1) = 7
            assertEquals(5, tree.countDistinctPalindromes()); // a, b, c, bcb, abcba
        }
    }

    @Nested
    @DisplayName("Integration with Other Algorithms")
    class IntegrationTests {

        @Test
        @DisplayName("Integration with dynamic programming")
        void testDPIntegration() {
            String s = "aabcc";
            
            // Test that DP optimizations work correctly
            int minCuts = PalindromicTreeTemplates.PalindromeDPOptimizations.minCuts(s);
            long partitions = PalindromicTreeTemplates.PalindromeDPOptimizations.countPartitions(s);
            boolean canPartition = PalindromicTreeTemplates.PalindromeCounter.canPartitionIntoPalindromes(s);
            
            assertTrue(minCuts >= 0);
            assertTrue(partitions >= 1);
            assertTrue(canPartition);
        }

        @Test
        @DisplayName("Comparison with naive palindrome detection")
        void testComparisonWithNaive() {
            String s = "abccba";
            
            // Count palindromes using palindromic tree
            int treeCount = (int) new PalindromicTreeTemplates.PalindromicTree(s).countAllPalindromes();
            
            // Count palindromes naively
            int naiveCount = 0;
            for (int i = 0; i < s.length(); i++) {
                for (int j = i; j < s.length(); j++) {
                    if (isPalindromeNaive(s, i, j)) {
                        naiveCount++;
                    }
                }
            }
            
            assertEquals(naiveCount, treeCount, "Tree count should match naive count");
        }

        private boolean isPalindromeNaive(String s, int i, int j) {
            while (i < j) {
                if (s.charAt(i) != s.charAt(j)) {
                    return false;
                }
                i++;
                j--;
            }
            return true;
        }
    }
}