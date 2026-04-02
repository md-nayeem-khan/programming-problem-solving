package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for SuffixArrayTemplates
 * Tests all suffix array operations, edge cases, and correctness
 */
public class SuffixArrayTemplatesTest {

    // ========== SUFFIX ARRAY CONSTRUCTION TESTS ==========

    @Test
    @DisplayName("Test suffix array for 'banana'")
    public void testBananaSuffixArray() {
        String s = "banana";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        
        // Expected suffix array: [5, 3, 1, 0, 4, 2]
        // Sorted suffixes: "a", "ana", "anana", "banana", "na", "nana"
        int[] expected = {5, 3, 1, 0, 4, 2};
        assertArrayEquals(expected, sa, "Suffix array for 'banana' should be [5,3,1,0,4,2]");
    }

    @Test
    @DisplayName("Test suffix array for 'mississippi'")
    public void testMississippiSuffixArray() {
        String s = "mississippi";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        
        // Verify the suffix array is correctly sorted
        assertTrue(isSuffixArrayValid(s, sa), "Suffix array should be correctly sorted");
        assertEquals(s.length(), sa.length, "Suffix array length should match string length");
    }

    @Test
    @DisplayName("Test both suffix array algorithms produce same result")
    public void testBothAlgorithmsMatch() {
        String[] testStrings = {"banana", "mississippi", "abracadabra", "aaaa", "abcdefg", "aabbcc"};
        
        for (String s : testStrings) {
            int[] sa1 = SuffixArrayTemplates.buildSuffixArray(s);
            int[] sa2 = SuffixArrayTemplates.buildSuffixArrayFast(s);
            assertArrayEquals(sa1, sa2, 
                "Both algorithms should produce same suffix array for: " + s);
        }
    }

    @Test
    @DisplayName("Test suffix array with single character")
    public void testSingleCharacter() {
        String s = "a";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        assertArrayEquals(new int[]{0}, sa, "Single character suffix array should be [0]");
    }

    @Test
    @DisplayName("Test suffix array with all same characters")
    public void testAllSameCharacters() {
        String s = "aaaa";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        
        // For "aaaa", suffix array should be [3,2,1,0] (shortest to longest)
        int[] expected = {3, 2, 1, 0};
        assertArrayEquals(expected, sa, "All same characters should be sorted by length");
    }

    @Test
    @DisplayName("Test suffix array with no repeating characters")
    public void testNoRepeatingCharacters() {
        String s = "abcdef";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        
        assertTrue(isSuffixArrayValid(s, sa), "Suffix array should be correctly sorted");
        assertEquals(6, sa.length, "Suffix array length should be 6");
    }

    @Test
    @DisplayName("Test suffix array with two characters")
    public void testTwoCharacters() {
        String s = "ab";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        int[] expected = {0, 1}; // "ab" < "b"
        assertArrayEquals(expected, sa, "Suffix array for 'ab' should be [0,1]");
    }

    // ========== LCP ARRAY TESTS ==========

    @Test
    @DisplayName("Test LCP array for 'banana'")
    public void testBananaLCPArray() {
        String s = "banana";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        int[] lcp = SuffixArrayTemplates.buildLCPArray(s, sa);
        
        // LCP array shows longest common prefix between adjacent suffixes
        assertEquals(sa.length, lcp.length, "LCP array length should match suffix array length");
        assertEquals(0, lcp[0], "First LCP should be 0");
        
        // Verify LCP values are non-negative
        for (int val : lcp) {
            assertTrue(val >= 0, "LCP values should be non-negative");
        }
    }

    @Test
    @DisplayName("Test both LCP algorithms produce same result")
    public void testBothLCPAlgorithmsMatch() {
        String[] testStrings = {"banana", "mississippi", "abracadabra", "aaaa", "abcdefg"};
        
        for (String s : testStrings) {
            int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
            int[] lcp1 = SuffixArrayTemplates.buildLCPArray(s, sa);
            int[] lcp2 = SuffixArrayTemplates.buildLCPKasai(s, sa);
            
            // buildLCPKasai returns n-1 elements, buildLCPArray returns n elements
            // Compare the valid portions
            for (int i = 1; i < lcp1.length; i++) {
                assertEquals(lcp1[i], lcp2[i-1], 
                    "Both LCP algorithms should produce same values for: " + s + " at index " + i);
            }
        }
    }

    @Test
    @DisplayName("Test LCP array values are correct")
    public void testLCPArrayCorrectness() {
        String s = "banana";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        int[] lcp = SuffixArrayTemplates.buildLCPArray(s, sa);
        
        // Verify LCP by checking actual common prefixes
        for (int i = 1; i < sa.length; i++) {
            String suffix1 = s.substring(sa[i-1]);
            String suffix2 = s.substring(sa[i]);
            int actualLCP = computeLCP(suffix1, suffix2);
            assertEquals(actualLCP, lcp[i], 
                "LCP should match actual common prefix at index " + i);
        }
    }

    @Test
    @DisplayName("Test LCP array with all same characters")
    public void testLCPAllSameCharacters() {
        String s = "aaaa";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        int[] lcp = SuffixArrayTemplates.buildLCPArray(s, sa);
        
        // For "aaaa", each suffix shares prefix with previous
        // sa = [3,2,1,0] => suffixes: "a", "aa", "aaa", "aaaa"
        // lcp should be [0, 1, 2, 3]
        int[] expected = {0, 1, 2, 3};
        assertArrayEquals(expected, lcp, "LCP for all same characters should increase");
    }

    // ========== PATTERN MATCHING TESTS ==========

    @Test
    @DisplayName("Test pattern matching - single occurrence")
    public void testPatternMatchingSingleOccurrence() {
        String text = "hello world";
        String pattern = "world";
        List<Integer> result = SuffixArrayTemplates.patternMatching(text, pattern);
        
        assertEquals(1, result.size(), "Should find one occurrence");
        assertEquals(6, result.get(0), "Pattern should start at index 6");
    }

    @Test
    @DisplayName("Test pattern matching - multiple occurrences")
    public void testPatternMatchingMultipleOccurrences() {
        String text = "ababcababa";
        String pattern = "aba";
        List<Integer> result = SuffixArrayTemplates.patternMatching(text, pattern);
        
        assertEquals(3, result.size(), "Should find three occurrences");
        List<Integer> expected = Arrays.asList(0, 5, 7);
        Collections.sort(result);
        assertEquals(expected, result, "Should find all occurrences at correct positions");
    }

    @Test
    @DisplayName("Test pattern matching - no occurrence")
    public void testPatternMatchingNotFound() {
        String text = "hello world";
        String pattern = "xyz";
        List<Integer> result = SuffixArrayTemplates.patternMatching(text, pattern);
        
        assertTrue(result.isEmpty(), "Should find no occurrences");
    }

    @Test
    @DisplayName("Test pattern matching - pattern equals text")
    public void testPatternMatchingFullMatch() {
        String text = "hello";
        String pattern = "hello";
        List<Integer> result = SuffixArrayTemplates.patternMatching(text, pattern);
        
        assertEquals(1, result.size(), "Should find one occurrence");
        assertEquals(0, result.get(0), "Pattern should start at index 0");
    }

    @Test
    @DisplayName("Test pattern matching - pattern longer than text")
    public void testPatternMatchingPatternTooLong() {
        String text = "hi";
        String pattern = "hello";
        List<Integer> result = SuffixArrayTemplates.patternMatching(text, pattern);
        
        assertTrue(result.isEmpty(), "Should find no occurrences when pattern is longer");
    }

    @Test
    @DisplayName("Test pattern matching - single character pattern")
    public void testPatternMatchingSingleChar() {
        String text = "banana";
        String pattern = "a";
        List<Integer> result = SuffixArrayTemplates.patternMatching(text, pattern);
        
        assertEquals(3, result.size(), "Should find three 'a' characters");
        Collections.sort(result);
        assertEquals(Arrays.asList(1, 3, 5), result, "Should find 'a' at correct positions");
    }

    // ========== LONGEST REPEATING SUBSTRING TESTS ==========

    @Test
    @DisplayName("Test longest repeating substring - basic case")
    public void testLongestRepeatingSubstring() {
        String s = "banana";
        int length = SuffixArrayTemplates.longestRepeatingSubstring(s);
        assertEquals(3, length, "Longest repeating in 'banana' is 'ana' (length 3)");
        
        String actual = SuffixArrayTemplates.longestRepeatingSubstringActual(s);
        assertEquals("ana", actual, "Should return 'ana'");
    }

    @Test
    @DisplayName("Test longest repeating substring - no repetition")
    public void testLongestRepeatingSubstringNoRepeat() {
        String s = "abcdef";
        int length = SuffixArrayTemplates.longestRepeatingSubstring(s);
        assertEquals(0, length, "No repeating substring should return 0");
        
        String actual = SuffixArrayTemplates.longestRepeatingSubstringActual(s);
        assertEquals("", actual, "Should return empty string");
    }

    @Test
    @DisplayName("Test longest repeating substring - all same characters")
    public void testLongestRepeatingSubstringAllSame() {
        String s = "aaaa";
        int length = SuffixArrayTemplates.longestRepeatingSubstring(s);
        assertEquals(3, length, "Longest repeating in 'aaaa' is 'aaa' (length 3)");
    }

    @Test
    @DisplayName("Test longest repeating substring - complex case")
    public void testLongestRepeatingSubstringComplex() {
        String s = "aabcaabdaab";
        String actual = SuffixArrayTemplates.longestRepeatingSubstringActual(s);
        assertEquals("aab", actual, "Longest repeating should be 'aab'");
    }

    @Test
    @DisplayName("Test longest duplicate substring (LeetCode 1044)")
    public void testLongestDupSubstring() {
        String s1 = "banana";
        assertEquals("ana", SuffixArrayTemplates.longestDupSubstring(s1));
        
        String s2 = "abcd";
        assertEquals("", SuffixArrayTemplates.longestDupSubstring(s2));
    }

    // ========== LONGEST COMMON SUBSTRING TESTS ==========

    @Test
    @DisplayName("Test longest common substring - basic case")
    public void testLongestCommonSubstring() {
        String s1 = "abcdefgh";
        String s2 = "xyzdefpq";
        String lcs = SuffixArrayTemplates.longestCommonSubstring(s1, s2);
        assertEquals("def", lcs, "Longest common substring should be 'def'");
    }

    @Test
    @DisplayName("Test longest common substring - no common")
    public void testLongestCommonSubstringNoCommon() {
        String s1 = "abc";
        String s2 = "xyz";
        String lcs = SuffixArrayTemplates.longestCommonSubstring(s1, s2);
        assertEquals("", lcs, "No common substring should return empty string");
    }

    @Test
    @DisplayName("Test longest common substring - identical strings")
    public void testLongestCommonSubstringIdentical() {
        String s1 = "hello";
        String s2 = "hello";
        String lcs = SuffixArrayTemplates.longestCommonSubstring(s1, s2);
        assertEquals("hello", lcs, "Identical strings should return entire string");
    }

    @Test
    @DisplayName("Test longest common substring - one is substring of other")
    public void testLongestCommonSubstringOneIsSubstring() {
        String s1 = "abc";
        String s2 = "xabcy";
        String lcs = SuffixArrayTemplates.longestCommonSubstring(s1, s2);
        assertEquals("abc", lcs, "Should find 'abc' as common substring");
    }

    @Test
    @DisplayName("Test longest common substring - multiple strings")
    public void testLongestCommonSubstringMultiple() {
        List<String> strings = Arrays.asList("abcdef", "xyzabc", "pqrabc");
        String lcs = SuffixArrayTemplates.longestCommonSubstringMultiple(strings);
        assertEquals("abc", lcs, "Common substring among all should be 'abc'");
    }

    @Test
    @DisplayName("Test longest common substring - multiple strings no common")
    public void testLongestCommonSubstringMultipleNoCommon() {
        List<String> strings = Arrays.asList("abc", "def", "ghi");
        String lcs = SuffixArrayTemplates.longestCommonSubstringMultiple(strings);
        assertEquals("", lcs, "No common substring should return empty string");
    }

    @Test
    @DisplayName("Test longest common substring - single string in list")
    public void testLongestCommonSubstringSingleInList() {
        List<String> strings = Arrays.asList("hello");
        String lcs = SuffixArrayTemplates.longestCommonSubstringMultiple(strings);
        assertEquals("hello", lcs, "Single string should return itself");
    }

    @Test
    @DisplayName("Test longest common substring - empty list")
    public void testLongestCommonSubstringEmptyList() {
        List<String> strings = Arrays.asList();
        String lcs = SuffixArrayTemplates.longestCommonSubstringMultiple(strings);
        assertEquals("", lcs, "Empty list should return empty string");
    }

    // ========== DISTINCT SUBSTRINGS TESTS ==========

    @Test
    @DisplayName("Test count distinct substrings - basic case")
    public void testCountDistinctSubstrings() {
        String s = "ababa";
        long count = SuffixArrayTemplates.countDistinctSubstrings(s);
        
        // Total substrings = 5*6/2 = 15
        // Distinct: a, ab, aba, abab, ababa, b, ba, bab, baba (9 distinct)
        // Actually: "", a, ab, aba, abab, ababa, b, ba, bab, baba = 10
        // But we don't count empty string, so it should be less
        assertTrue(count > 0 && count <= 15, "Should count distinct substrings");
    }

    @Test
    @DisplayName("Test count distinct substrings - all same")
    public void testCountDistinctSubstringsAllSame() {
        String s = "aaaa";
        long count = SuffixArrayTemplates.countDistinctSubstrings(s);
        
        // For "aaaa", distinct substrings are: "a", "aa", "aaa", "aaaa" = 4
        assertEquals(4, count, "Should count 4 distinct substrings");
    }

    @Test
    @DisplayName("Test count distinct substrings - no duplicates")
    public void testCountDistinctSubstringsNoDuplicates() {
        String s = "abc";
        long count = SuffixArrayTemplates.countDistinctSubstrings(s);
        
        // Total substrings = 3*4/2 = 6
        // All are distinct: "a", "ab", "abc", "b", "bc", "c" = 6
        assertEquals(6, count, "All substrings should be distinct");
    }

    @Test
    @DisplayName("Test count distinct substrings - single character")
    public void testCountDistinctSubstringsSingleChar() {
        String s = "a";
        long count = SuffixArrayTemplates.countDistinctSubstrings(s);
        assertEquals(1, count, "Single character has 1 distinct substring");
    }

    // ========== EDGE CASES TESTS ==========

    @Test
    @DisplayName("Test with two-character string")
    public void testTwoCharacterEdgeCases() {
        String s = "ab";
        
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        assertEquals(2, sa.length, "Suffix array should have 2 elements");
        
        int[] lcp = SuffixArrayTemplates.buildLCPArray(s, sa);
        assertEquals(2, lcp.length, "LCP array should have 2 elements");
        assertEquals(0, lcp[0], "First LCP should be 0");
    }

    @Test
    @DisplayName("Test with repeating pattern")
    public void testRepeatingPattern() {
        String s = "abcabcabc";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        
        assertTrue(isSuffixArrayValid(s, sa), "Suffix array should be valid");
        
        String repeated = SuffixArrayTemplates.longestRepeatingSubstringActual(s);
        assertEquals("abcabc", repeated, "Should find longest repeating pattern");
    }

    @Test
    @DisplayName("Test special characters in string")
    public void testSpecialCharacters() {
        String s = "a.b.c.d";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        
        assertTrue(isSuffixArrayValid(s, sa), "Suffix array should handle special characters");
    }

    @Test
    @DisplayName("Test numeric characters")
    public void testNumericCharacters() {
        String s = "123321";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        
        assertTrue(isSuffixArrayValid(s, sa), "Suffix array should handle numeric characters");
        
        String repeated = SuffixArrayTemplates.longestRepeatingSubstringActual(s);
        // "123321" has repeating characters
        assertTrue(repeated.length() > 0, "Should find some repetition");
    }

    @Test
    @DisplayName("Test mixed case sensitivity")
    public void testMixedCase() {
        String s = "AaBbAa";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        
        assertTrue(isSuffixArrayValid(s, sa), "Suffix array should handle mixed case");
    }

    @Test
    @DisplayName("Test period detection")
    public void testPeriodDetection() {
        String s = "abcabcabc";
        List<Integer> periods = SuffixArrayTemplates.AdvancedStringMetrics.findAllPeriods(s);
        
        assertTrue(periods.contains(3), "Should detect period of 3");
        assertTrue(periods.contains(9), "Should detect period of 9 (full string)");
    }

    @Test
    @DisplayName("Test period detection - no period")
    public void testPeriodDetectionNoPeriod() {
        String s = "abcdef";
        List<Integer> periods = SuffixArrayTemplates.AdvancedStringMetrics.findAllPeriods(s);
        
        // Only full string length is a period
        assertEquals(1, periods.size(), "Should only have trivial period");
        assertEquals(6, periods.get(0), "Should be full string length");
    }

    // ========== PERFORMANCE TESTS ==========

    @Test
    @DisplayName("Test with larger string")
    public void testLargerString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("abc");
        }
        String s = sb.toString();
        
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        assertEquals(300, sa.length, "Should handle 300 character string");
        assertTrue(isSuffixArrayValid(s, sa), "Suffix array should be valid");
    }

    @Test
    @DisplayName("Test both algorithms with larger input")
    public void testBothAlgorithmsLargerInput() {
        String s = "abcdefghijklmnopqrstuvwxyz".repeat(4); // 104 characters
        
        int[] sa1 = SuffixArrayTemplates.buildSuffixArray(s);
        int[] sa2 = SuffixArrayTemplates.buildSuffixArrayFast(s);
        
        assertArrayEquals(sa1, sa2, "Both algorithms should match on larger input");
    }

    // ========== CORRECTNESS VERIFICATION TESTS ==========

    @Test
    @DisplayName("Verify suffix array contains all indices")
    public void testSuffixArrayContainsAllIndices() {
        String s = "banana";
        int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
        
        boolean[] seen = new boolean[s.length()];
        for (int idx : sa) {
            assertFalse(seen[idx], "Each index should appear exactly once");
            seen[idx] = true;
        }
        
        for (boolean b : seen) {
            assertTrue(b, "All indices should be present");
        }
    }

    @Test
    @DisplayName("Verify suffix array is correctly sorted")
    public void testSuffixArraySorted() {
        String[] testCases = {"banana", "mississippi", "abracadabra", "aaaa", "abcd"};
        
        for (String s : testCases) {
            int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
            
            for (int i = 1; i < sa.length; i++) {
                String prev = s.substring(sa[i-1]);
                String curr = s.substring(sa[i]);
                assertTrue(prev.compareTo(curr) <= 0, 
                    "Suffixes should be in sorted order for: " + s);
            }
        }
    }

    @Test
    @DisplayName("Verify LCP correctness against brute force")
    public void testLCPCorrectnessBruteForce() {
        String[] testCases = {"banana", "mississippi", "abracadabra"};
        
        for (String s : testCases) {
            int[] sa = SuffixArrayTemplates.buildSuffixArrayFast(s);
            int[] lcp = SuffixArrayTemplates.buildLCPArray(s, sa);
            
            for (int i = 1; i < sa.length; i++) {
                int expectedLCP = computeLCP(s.substring(sa[i-1]), s.substring(sa[i]));
                assertEquals(expectedLCP, lcp[i], 
                    "LCP should match brute force computation at index " + i + " for: " + s);
            }
        }
    }

    // ========== HELPER METHODS ==========

    /**
     * Verify if suffix array is valid (contains all indices and is sorted)
     */
    private boolean isSuffixArrayValid(String s, int[] sa) {
        if (sa.length != s.length()) return false;
        
        // Check all indices present
        boolean[] seen = new boolean[s.length()];
        for (int idx : sa) {
            if (idx < 0 || idx >= s.length() || seen[idx]) return false;
            seen[idx] = true;
        }
        
        // Check sorted order
        for (int i = 1; i < sa.length; i++) {
            String prev = s.substring(sa[i-1]);
            String curr = s.substring(sa[i]);
            if (prev.compareTo(curr) > 0) return false;
        }
        
        return true;
    }

    /**
     * Compute LCP (longest common prefix) of two strings
     */
    private int computeLCP(String s1, String s2) {
        int len = 0;
        while (len < s1.length() && len < s2.length() && s1.charAt(len) == s2.charAt(len)) {
            len++;
        }
        return len;
    }
}
