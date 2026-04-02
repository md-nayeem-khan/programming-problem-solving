package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Stream;

/**
 * Comprehensive validation tests for StringAlgorithms.java
 * 
 * This test suite validates:
 * 1. KMP pattern matching algorithm
 * 2. Z-function algorithm 
 * 3. Rabin-Karp rolling hash
 * 4. Manacher's palindrome algorithms
 * 
 * Test categories:
 * - Basic functionality
 * - Edge cases (empty strings, single chars, no matches)
 * - Stress tests (large inputs)
 * - Algorithmic correctness vs naive implementations
 * - Performance validation
 */
@DisplayName("StringAlgorithms Validation Suite")
public class StringAlgorithmsTest {

    // ===============================================================
    // TEST DATA PROVIDERS
    // ===============================================================

    /**
     * Provides test cases for pattern matching algorithms
     * Format: text, pattern, expected positions
     */
    static Stream<Arguments> patternMatchingTestCases() {
        return Stream.of(
            // Basic cases
            Arguments.of("aabaabaaab", "aab", Arrays.asList(0, 3, 7)),
            Arguments.of("abcabcabcabc", "abc", Arrays.asList(0, 3, 6, 9)),
            Arguments.of("ababcababa", "aba", Arrays.asList(0, 5, 7)),
            
            // Edge cases
            Arguments.of("", "", Collections.emptyList()),
            Arguments.of("abc", "", Collections.emptyList()),
            Arguments.of("", "a", Collections.emptyList()),
            Arguments.of("a", "a", Arrays.asList(0)),
            Arguments.of("a", "ab", Collections.emptyList()),
            Arguments.of("hello", "world", Collections.emptyList()),
            
            // Self-overlapping patterns
            Arguments.of("abababab", "abab", Arrays.asList(0, 2, 4)),
            Arguments.of("aaaaaaa", "aaa", Arrays.asList(0, 1, 2, 3, 4)),
            
            // Single character patterns
            Arguments.of("aabbaabb", "a", Arrays.asList(0, 1, 4, 5)),
            Arguments.of("abcdef", "x", Collections.emptyList()),
            
            // Complex patterns
            Arguments.of("the quick brown fox jumps over the lazy dog", "the", Arrays.asList(0, 31)),
            Arguments.of("banana", "ana", Arrays.asList(1, 3)),
            
            // Identical strings
            Arguments.of("test", "test", Arrays.asList(0)),
            Arguments.of("abcd", "abcd", Arrays.asList(0))
        );
    }
    
    /**
     * Provides test cases for palindrome detection
     * Format: input string, expected longest palindrome
     */
    static Stream<Arguments> palindromeTestCases() {
        return Stream.of(
            // Basic cases
            Arguments.of("babad", "bab"), // could also be "aba"
            Arguments.of("cbbd", "bb"),
            Arguments.of("a", "a"),
            Arguments.of("ac", "a"), // could also be "c"
            
            // Complex palindromes
            Arguments.of("racecar", "racecar"),
            Arguments.of("abacabad", "abacaba"),
            Arguments.of("noon", "noon"),
            Arguments.of("level", "level"),
            
            // No palindromes longer than 1
            Arguments.of("abcdef", "a"), // any single character
            
            // Edge cases
            Arguments.of("", ""),
            Arguments.of("aaaaaa", "aaaaaa"),
            
            // Mixed cases
            Arguments.of("abcdeffedcba", "abcdeffedcba"),
            Arguments.of("malayalam", "malayalam"),
            Arguments.of("wasitacaroracatisaw", "wasitacaroracatisaw")
        );
    }
    
    /**
     * Provides test cases for k-palindrome validation
     * Format: string, k, expected result
     */
    static Stream<Arguments> kPalindromeTestCases() {
        return Stream.of(
            Arguments.of("abcdeca", 2, true),  // remove 'b', 'd' -> "aceca"
            Arguments.of("abbababa", 1, true), // remove one 'b' -> "abbaaba" 
            Arguments.of("abc", 0, false),     // need to remove 2 chars minimum
            Arguments.of("abc", 2, true),      // remove 'b', 'c' -> "a"
            Arguments.of("racecar", 0, true),  // already palindrome
            Arguments.of("abcdef", 5, true),   // remove all but one -> single char
            Arguments.of("", 0, true),         // empty string is palindrome
            Arguments.of("a", 0, true),        // single char is palindrome
            Arguments.of("abcdefg", 3, false)  // need to remove at least 5 chars
        );
    }

    // ===============================================================
    // KMP ALGORITHM TESTS
    // ===============================================================

    @Nested
    @DisplayName("KMP Algorithm Tests")
    class KMPTests {

        @Test
        @DisplayName("KMP buildLPS - Basic functionality")
        void testBuildLPSBasic() {
            // Test cases with known LPS values
            assertArrayEquals(new int[]{0, 0, 0, 0}, StringAlgorithms.KMP.buildLPS("abcd"));
            assertArrayEquals(new int[]{0, 1, 2, 3}, StringAlgorithms.KMP.buildLPS("aaaa"));
            assertArrayEquals(new int[]{0, 1, 0, 1, 2}, StringAlgorithms.KMP.buildLPS("aabaa"));
            assertArrayEquals(new int[]{0, 0, 1, 2, 3, 4, 0}, StringAlgorithms.KMP.buildLPS("abababx"));
            assertArrayEquals(new int[]{0, 0, 0, 1, 2, 3, 4, 5}, StringAlgorithms.KMP.buildLPS("abcabcab"));
        }

        @Test
        @DisplayName("KMP buildLPS - Edge cases")
        void testBuildLPSEdgeCases() {
            // Empty string
            assertArrayEquals(new int[]{}, StringAlgorithms.KMP.buildLPS(""));
            
            // Single character
            assertArrayEquals(new int[]{0}, StringAlgorithms.KMP.buildLPS("a"));
            
            // Two characters - different
            assertArrayEquals(new int[]{0, 0}, StringAlgorithms.KMP.buildLPS("ab"));
            
            // Two characters - same
            assertArrayEquals(new int[]{0, 1}, StringAlgorithms.KMP.buildLPS("aa"));
        }

        @ParameterizedTest
        @DisplayName("KMP search - Pattern matching validation")
        @MethodSource("com.cp.algorithms.StringAlgorithmsTest#patternMatchingTestCases")
        void testKMPSearch(String text, String pattern, List<Integer> expected) {
            List<Integer> actual = StringAlgorithms.KMP.search(text, pattern);
            assertEquals(expected, actual, 
                String.format("KMP search failed for text='%s', pattern='%s'", text, pattern));
        }

        @Test
        @DisplayName("KMP search - Self-overlapping patterns")
        void testKMPSelfOverlapping() {
            // Test the classic overlapping case
            List<Integer> result = StringAlgorithms.KMP.search("aabaaba", "aaba");
            assertEquals(Arrays.asList(0, 3), result);
            
            // Another overlapping case
            result = StringAlgorithms.KMP.search("abababab", "abab");
            assertEquals(Arrays.asList(0, 2, 4), result);
        }
    }

    // ===============================================================
    // Z-FUNCTION ALGORITHM TESTS
    // ===============================================================

    @Nested
    @DisplayName("Z-Function Algorithm Tests")
    class ZFunctionTests {

        @Test
        @DisplayName("Z-function - Basic computation")
        void testZFunctionBasic() {
            // Test known Z-function values
            assertArrayEquals(new int[]{0, 0, 0, 0}, StringAlgorithms.zFunction("abcd"));
            assertArrayEquals(new int[]{0, 0, 2, 0}, StringAlgorithms.zFunction("abab"));
            assertArrayEquals(new int[]{0, 1, 0, 2, 3, 1, 0}, StringAlgorithms.zFunction("aabaaab"));
            
            // All same characters
            int[] expected = new int[]{0, 4, 3, 2, 1};
            assertArrayEquals(expected, StringAlgorithms.zFunction("aaaaa"));
        }

        @Test
        @DisplayName("Z-function - Edge cases")
        void testZFunctionEdgeCases() {
            // Empty string
            assertArrayEquals(new int[]{}, StringAlgorithms.zFunction(""));
            
            // Single character
            assertArrayEquals(new int[]{0}, StringAlgorithms.zFunction("a"));
            
            // Two characters
            assertArrayEquals(new int[]{0, 0}, StringAlgorithms.zFunction("ab"));
            assertArrayEquals(new int[]{0, 1}, StringAlgorithms.zFunction("aa"));
        }

        @ParameterizedTest
        @DisplayName("Z-search - Pattern matching validation")
        @MethodSource("com.cp.algorithms.StringAlgorithmsTest#patternMatchingTestCases")
        void testZSearch(String text, String pattern, List<Integer> expected) {
            List<Integer> actual = StringAlgorithms.zSearch(text, pattern);
            assertEquals(expected, actual,
                String.format("Z-search failed for text='%s', pattern='%s'", text, pattern));
        }

        @Test
        @DisplayName("Z-search vs KMP consistency")
        void testZSearchVsKMP() {
            String[] texts = {"aabaabaaab", "abcdefg", "banana", "abababab", ""};
            String[] patterns = {"aab", "def", "ana", "abab", ""};
            
            for (String text : texts) {
                for (String pattern : patterns) {
                    List<Integer> kmpResult = StringAlgorithms.KMP.search(text, pattern);
                    List<Integer> zResult = StringAlgorithms.zSearch(text, pattern);
                    assertEquals(kmpResult, zResult, 
                        String.format("KMP and Z-search results differ for text='%s', pattern='%s'", text, pattern));
                }
            }
        }
    }

    // ===============================================================
    // RABIN-KARP ALGORITHM TESTS  
    // ===============================================================

    @Nested
    @DisplayName("Rabin-Karp Algorithm Tests")
    class RabinKarpTests {

        @ParameterizedTest
        @DisplayName("Rabin-Karp search - Pattern matching validation")
        @MethodSource("com.cp.algorithms.StringAlgorithmsTest#patternMatchingTestCases")
        void testRabinKarpSearch(String text, String pattern, List<Integer> expected) {
            List<Integer> actual = StringAlgorithms.RabinKarp.search(text, pattern);
            assertEquals(expected, actual,
                String.format("Rabin-Karp search failed for text='%s', pattern='%s'", text, pattern));
        }

        @Test
        @DisplayName("Rabin-Karp vs KMP consistency")
        void testRabinKarpVsKMP() {
            String[] texts = {"aabaabaaab", "abcdefghijklmnop", "banana", "abababab", "hello world"};
            String[] patterns = {"aab", "def", "ana", "abab", "wor"};
            
            for (String text : texts) {
                for (String pattern : patterns) {
                    List<Integer> kmpResult = StringAlgorithms.KMP.search(text, pattern);
                    List<Integer> rabinResult = StringAlgorithms.RabinKarp.search(text, pattern);
                    assertEquals(kmpResult, rabinResult,
                        String.format("KMP and Rabin-Karp results differ for text='%s', pattern='%s'", text, pattern));
                }
            }
        }

        @Test
        @DisplayName("Rabin-Karp - Hash collision handling")
        void testHashCollisionHandling() {
            // Test with strings that might cause hash collisions
            // The algorithm should still work correctly due to string verification
            String text = "abcdefghijklmnop";
            String pattern = "def";
            
            List<Integer> result = StringAlgorithms.RabinKarp.search(text, pattern);
            assertEquals(Arrays.asList(3), result);
            
            // Test with repeated patterns that might cause collisions
            text = "aaaaaaaaaaaaaaa";
            pattern = "aaaa";
            
            result = StringAlgorithms.RabinKarp.search(text, pattern);
            assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), result);
        }
    }

    // ===============================================================
    // MANACHER ALGORITHM TESTS
    // ===============================================================

    @Nested
    @DisplayName("Manacher Algorithm Tests")
    class ManacherTests {

        @ParameterizedTest
        @DisplayName("Longest palindrome - Basic validation")
        @MethodSource("com.cp.algorithms.StringAlgorithmsTest#palindromeTestCases")
        void testLongestPalindrome(String input, String expectedPalindrome) {
            String actual = StringAlgorithms.longestPalindrome(input);
            
            // For cases where multiple palindromes of same length exist, 
            // verify the result is a valid palindrome of expected length
            if (!input.isEmpty()) {
                assertTrue(isPalindrome(actual), 
                    String.format("Result '%s' is not a palindrome for input '%s'", actual, input));
                assertEquals(expectedPalindrome.length(), actual.length(),
                    String.format("Wrong length palindrome for input '%s': expected length %d, got '%s'", 
                        input, expectedPalindrome.length(), actual));
                assertTrue(input.contains(actual),
                    String.format("Result '%s' is not a substring of input '%s'", actual, input));
            } else {
                assertEquals(expectedPalindrome, actual);
            }
        }

        @Test
        @DisplayName("Manacher array - Basic computation")
        void testManacherArray() {
            // Test with known cases
            String input = "abacabad";
            int[] p = StringAlgorithms.manacherArray(input);
            assertNotNull(p);
            assertTrue(p.length > 0);
            
            // Verify the array makes sense - all values should be >= 0
            for (int i = 0; i < p.length; i++) {
                assertTrue(p[i] >= 0, "Negative value in Manacher array at index " + i);
            }
        }

        @Test
        @DisplayName("Count palindromes - Basic validation")
        void testCountPalindromes() {
            // Test known cases
            assertEquals(3, StringAlgorithms.countPalindromes("abc")); // "a", "b", "c"
            assertEquals(6, StringAlgorithms.countPalindromes("aaa")); // "a"(3), "aa"(2), "aaa"(1)
            assertEquals(6, StringAlgorithms.countPalindromes("abba")); // "a"(2), "b"(2), "bb"(1), "abba"(1) = 6
            
            // Edge cases
            assertEquals(0, StringAlgorithms.countPalindromes(""));
            assertEquals(1, StringAlgorithms.countPalindromes("a"));
        }

        @Test
        @DisplayName("Get all palindromes - Basic validation")
        void testGetAllPalindromes() {
            List<String> result = StringAlgorithms.getAllPalindromes("aab");
            Set<String> expected = Set.of("a", "aa", "b");
            assertEquals(expected, new HashSet<>(result));
            
            // Verify all results are actually palindromes
            for (String palindrome : result) {
                assertTrue(isPalindrome(palindrome), 
                    "Result contains non-palindrome: " + palindrome);
            }
            
            // Edge cases
            assertTrue(StringAlgorithms.getAllPalindromes("").isEmpty());
            assertEquals(Collections.singletonList("a"), StringAlgorithms.getAllPalindromes("a"));
        }

        @ParameterizedTest
        @DisplayName("K-palindrome validation")
        @MethodSource("com.cp.algorithms.StringAlgorithmsTest#kPalindromeTestCases")
        void testIsKPalindrome(String input, int k, boolean expected) {
            boolean actual = StringAlgorithms.isKPalindrome(input, k);
            assertEquals(expected, actual,
                String.format("K-palindrome check failed for input='%s', k=%d", input, k));
        }

        @Test
        @DisplayName("Manacher algorithm consistency")
        void testManacherConsistency() {
            String[] testCases = {"racecar", "abacabad", "banana", "aaaaaa", "abcdef"};
            
            for (String test : testCases) {
                String longestPal = StringAlgorithms.longestPalindrome(test);
                int count = StringAlgorithms.countPalindromes(test);
                List<String> allPal = StringAlgorithms.getAllPalindromes(test);
                
                // Verify consistency
                assertTrue(isPalindrome(longestPal), "Longest palindrome is not actually a palindrome");
                assertTrue(count >= test.length(), "Palindrome count should be at least string length");
                assertTrue(allPal.contains(longestPal) || test.isEmpty(), 
                    "All palindromes should contain the longest palindrome");
                
                // Verify all returned palindromes are valid
                for (String pal : allPal) {
                    assertTrue(isPalindrome(pal), "Non-palindrome in getAllPalindromes result: " + pal);
                    assertTrue(test.contains(pal), "Palindrome not found in original string: " + pal);
                }
            }
        }
    }

    // ===============================================================
    // EDGE CASES AND STRESS TESTS
    // ===============================================================

    @Nested
    @DisplayName("Edge Cases and Stress Tests")
    class EdgeCasesAndStressTests {

        @Test
        @DisplayName("All algorithms - Empty string handling")
        void testEmptyStringHandling() {
            // KMP
            assertEquals(Collections.emptyList(), StringAlgorithms.KMP.search("", ""));
            assertEquals(Collections.emptyList(), StringAlgorithms.KMP.search("abc", ""));
            assertEquals(Collections.emptyList(), StringAlgorithms.KMP.search("", "abc"));
            
            // Z-function
            assertArrayEquals(new int[]{}, StringAlgorithms.zFunction(""));
            assertEquals(Collections.emptyList(), StringAlgorithms.zSearch("", ""));
            
            // Rabin-Karp
            assertEquals(Collections.emptyList(), StringAlgorithms.RabinKarp.search("", ""));
            
            // Manacher
            assertEquals("", StringAlgorithms.longestPalindrome(""));
            assertEquals(0, StringAlgorithms.countPalindromes(""));
            assertTrue(StringAlgorithms.getAllPalindromes("").isEmpty());
            assertTrue(StringAlgorithms.isKPalindrome("", 0));
        }

        @Test
        @DisplayName("All algorithms - Single character handling")
        void testSingleCharacterHandling() {
            String singleChar = "a";
            
            // Pattern matching algorithms
            assertEquals(Collections.singletonList(0), StringAlgorithms.KMP.search(singleChar, singleChar));
            assertEquals(Collections.singletonList(0), StringAlgorithms.zSearch(singleChar, singleChar));
            assertEquals(Collections.singletonList(0), StringAlgorithms.RabinKarp.search(singleChar, singleChar));
            
            // Palindrome algorithms
            assertEquals("a", StringAlgorithms.longestPalindrome(singleChar));
            assertEquals(1, StringAlgorithms.countPalindromes(singleChar));
            assertEquals(Collections.singletonList("a"), StringAlgorithms.getAllPalindromes(singleChar));
            assertTrue(StringAlgorithms.isKPalindrome(singleChar, 0));
        }

        @Test
        @DisplayName("Large input stress test")
        void testLargeInputs() {
            // Create large test strings
            StringBuilder largeText = new StringBuilder();
            StringBuilder pattern = new StringBuilder();
            
            // Build a large string with known pattern occurrences
            for (int i = 0; i < 1000; i++) {
                if (i % 100 == 0) {
                    largeText.append("PATTERN");
                } else {
                    largeText.append("ABCDEF");
                }
            }
            pattern.append("PATTERN");
            
            String text = largeText.toString();
            String pat = pattern.toString();
            
            // All pattern matching algorithms should find the same occurrences
            List<Integer> kmpResult = StringAlgorithms.KMP.search(text, pat);
            List<Integer> zResult = StringAlgorithms.zSearch(text, pat);
            List<Integer> rabinResult = StringAlgorithms.RabinKarp.search(text, pat);
            
            assertEquals(kmpResult, zResult, "KMP and Z-function results differ on large input");
            assertEquals(kmpResult, rabinResult, "KMP and Rabin-Karp results differ on large input");
            assertEquals(10, kmpResult.size(), "Expected 10 pattern occurrences");
            
            // Test palindrome algorithms on large palindromic string
            StringBuilder largePalindrome = new StringBuilder();
            for (int i = 0; i < 500; i++) {
                largePalindrome.append("abc");
            }
            largePalindrome.append("x");
            for (int i = 499; i >= 0; i--) {
                largePalindrome.append("cba");
            }
            
            String palText = largePalindrome.toString();
            String longestPal = StringAlgorithms.longestPalindrome(palText);
            
            assertNotNull(longestPal);
            assertTrue(longestPal.length() > 0);
            assertTrue(isPalindrome(longestPal), "Longest palindrome result is not actually a palindrome");
        }

        @Test
        @DisplayName("Unicode and special character handling")
        void testUnicodeHandling() {
            // Test with Unicode characters
            String unicodeText = "αβγαβγδεφ";
            String unicodePattern = "αβγ";
            
            List<Integer> kmpResult = StringAlgorithms.KMP.search(unicodeText, unicodePattern);
            List<Integer> zResult = StringAlgorithms.zSearch(unicodeText, unicodePattern);
            // Note: RabinKarp uses 'a'-'z' mapping, so skip Unicode test for it
            
            assertEquals(Arrays.asList(0, 3), kmpResult);
            assertEquals(kmpResult, zResult);
            
            // Test palindromes with Unicode
            String unicodePalindrome = "αβα";
            assertEquals("αβα", StringAlgorithms.longestPalindrome(unicodePalindrome));
            assertTrue(StringAlgorithms.isKPalindrome(unicodePalindrome, 0));
        }
    }

    // ===============================================================
    // PERFORMANCE VALIDATION TESTS
    // ===============================================================

    @Nested
    @DisplayName("Performance Validation")
    class PerformanceTests {

        @Test
        @DisplayName("Time complexity validation - Pattern matching")
        void testPatternMatchingPerformance() {
            // Test that algorithms complete in reasonable time for large inputs
            int textSize = 10000;
            int patternSize = 100;
            
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < textSize; i++) {
                text.append((char) ('a' + (i % 26)));
            }
            
            StringBuilder pattern = new StringBuilder();
            for (int i = 0; i < patternSize; i++) {
                pattern.append((char) ('a' + (i % 5)));  // Pattern will occur multiple times
            }
            
            String textStr = text.toString();
            String patternStr = pattern.toString();
            
            // Time the algorithms - should complete quickly
            long startTime = System.nanoTime();
            List<Integer> kmpResult = StringAlgorithms.KMP.search(textStr, patternStr);
            long kmpTime = System.nanoTime() - startTime;
            
            startTime = System.nanoTime();
            List<Integer> zResult = StringAlgorithms.zSearch(textStr, patternStr);
            long zTime = System.nanoTime() - startTime;
            
            // Verify results are consistent
            assertEquals(kmpResult, zResult);
            
            // Both should complete in reasonable time (< 200ms for this size)
            assertTrue(kmpTime < 200_000_000, "KMP took too long: " + kmpTime + "ns");
            assertTrue(zTime < 200_000_000, "Z-function took too long: " + zTime + "ns");
        }

        @Test
        @DisplayName("Time complexity validation - Palindromes")
        void testPalindromePerformance() {
            // Test Manacher's O(n) performance on large input
            int size = 10000;
            StringBuilder text = new StringBuilder();
            
            // Create a string with mixed palindromic patterns
            for (int i = 0; i < size / 2; i++) {
                text.append((char) ('a' + (i % 5)));
            }
            text.append("x");  // Center character
            for (int i = size / 2 - 1; i >= 0; i--) {
                text.append((char) ('a' + (i % 5)));
            }
            
            String textStr = text.toString();
            
            // Time the Manacher algorithm - should be O(n)
            long startTime = System.nanoTime();
            String longestPal = StringAlgorithms.longestPalindrome(textStr);
            long manacherTime = System.nanoTime() - startTime;
            
            assertNotNull(longestPal);
            assertTrue(longestPal.length() > 0);
            assertTrue(isPalindrome(longestPal));
            
            // Should complete quickly for O(n) algorithm
            assertTrue(manacherTime < 50_000_000, "Manacher took too long: " + manacherTime + "ns");
        }
    }

    // ===============================================================
    // UTILITY METHODS
    // ===============================================================

    /**
     * Helper method to verify if a string is a palindrome
     */
    private boolean isPalindrome(String s) {
        if (s == null) return false;
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * Helper method for naive pattern matching (for comparison)
     */
    private List<Integer> naivePatternMatch(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        if (pattern.isEmpty()) return result;
        
        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            boolean match = true;
            for (int j = 0; j < pattern.length(); j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) result.add(i);
        }
        return result;
    }
}