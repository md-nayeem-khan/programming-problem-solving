package com.cp.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Aho-Corasick Automaton implementation
 * Tests cover:
 * - Basic functionality
 * - Edge cases
 * - Overlapping patterns
 * - Empty inputs
 * - Performance characteristics
 * - LeetCode 1032: Stream of Characters
 */
@DisplayName("Aho-Corasick Automaton Tests")
class AhoCorasickTemplatesTest {

    @Nested
    @DisplayName("Basic Aho-Corasick Functionality")
    class BasicAhoCorasickTests {

        @Test
        @DisplayName("Single pattern match")
        void testSinglePatternMatch() {
            List<String> patterns = Arrays.asList("abc");
            String text = "xabcyabcz";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(1, result.size());
            assertEquals(Arrays.asList(1, 5), result.get(0));
        }

        @Test
        @DisplayName("Multiple pattern matches - Classic example")
        void testMultiplePatternMatches() {
            List<String> patterns = Arrays.asList("he", "she", "his", "hers");
            String text = "ahishers";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            // "his" at position 1 (ahishers)
            assertEquals(Arrays.asList(1), result.get(2));
            
            // "she" at position 3 (ahishers)
            assertEquals(Arrays.asList(3), result.get(1));
            
            // "he" at position 4 (ahishers)
            assertEquals(Arrays.asList(4), result.get(0));
            
            // "hers" at position 4 (ahishers)
            assertEquals(Arrays.asList(4), result.get(3));
            
            assertEquals(4, ac.countMatches(text));
        }

        @Test
        @DisplayName("No matches found")
        void testNoMatches() {
            List<String> patterns = Arrays.asList("abc", "def", "ghi");
            String text = "xyz";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            for (List<Integer> positions : result.values()) {
                assertTrue(positions.isEmpty());
            }
            
            assertEquals(0, ac.countMatches(text));
            assertFalse(ac.containsAny(text));
        }

        @Test
        @DisplayName("Pattern at beginning of text")
        void testPatternAtBeginning() {
            List<String> patterns = Arrays.asList("abc");
            String text = "abcdef";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(Arrays.asList(0), result.get(0));
        }

        @Test
        @DisplayName("Pattern at end of text")
        void testPatternAtEnd() {
            List<String> patterns = Arrays.asList("xyz");
            String text = "abcxyz";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(Arrays.asList(3), result.get(0));
        }

        @Test
        @DisplayName("Entire text is a pattern")
        void testEntireTextIsPattern() {
            List<String> patterns = Arrays.asList("hello");
            String text = "hello";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(Arrays.asList(0), result.get(0));
        }
    }

    @Nested
    @DisplayName("Overlapping Patterns")
    class OverlappingPatternTests {

        @Test
        @DisplayName("Overlapping patterns - same length")
        void testOverlappingSameLength() {
            List<String> patterns = Arrays.asList("aa", "aa");
            String text = "aaa";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            // "aa" appears at positions 0 and 1
            assertEquals(Arrays.asList(0, 1), result.get(0));
        }

        @Test
        @DisplayName("Overlapping patterns - different lengths")
        void testOverlappingDifferentLengths() {
            List<String> patterns = Arrays.asList("aa", "aaa", "aaaa");
            String text = "aaaaa";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            // "aa" at positions: 0, 1, 2, 3
            assertEquals(Arrays.asList(0, 1, 2, 3), result.get(0));
            
            // "aaa" at positions: 0, 1, 2
            assertEquals(Arrays.asList(0, 1, 2), result.get(1));
            
            // "aaaa" at positions: 0, 1
            assertEquals(Arrays.asList(0, 1), result.get(2));
        }

        @Test
        @DisplayName("Nested patterns")
        void testNestedPatterns() {
            List<String> patterns = Arrays.asList("a", "ab", "abc", "abcd");
            String text = "abcd";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(Arrays.asList(0), result.get(0)); // "a"
            assertEquals(Arrays.asList(0), result.get(1)); // "ab"
            assertEquals(Arrays.asList(0), result.get(2)); // "abc"
            assertEquals(Arrays.asList(0), result.get(3)); // "abcd"
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Empty pattern list")
        void testEmptyPatternList() {
            List<String> patterns = Arrays.asList();
            String text = "test";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertTrue(result.isEmpty());
            assertEquals(0, ac.countMatches(text));
        }

        @Test
        @DisplayName("Empty text")
        void testEmptyText() {
            List<String> patterns = Arrays.asList("abc", "def");
            String text = "";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            for (List<Integer> positions : result.values()) {
                assertTrue(positions.isEmpty());
            }
            
            assertEquals(0, ac.countMatches(text));
            assertFalse(ac.containsAny(text));
        }

        @Test
        @DisplayName("Single character patterns")
        void testSingleCharPatterns() {
            List<String> patterns = Arrays.asList("a", "b", "c");
            String text = "abcabc";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(Arrays.asList(0, 3), result.get(0)); // "a"
            assertEquals(Arrays.asList(1, 4), result.get(1)); // "b"
            assertEquals(Arrays.asList(2, 5), result.get(2)); // "c"
        }

        @Test
        @DisplayName("Pattern longer than text")
        void testPatternLongerThanText() {
            List<String> patterns = Arrays.asList("abcdefgh");
            String text = "abc";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertTrue(result.get(0).isEmpty());
            assertEquals(0, ac.countMatches(text));
        }

        @Test
        @DisplayName("Duplicate patterns")
        void testDuplicatePatterns() {
            List<String> patterns = Arrays.asList("abc", "abc", "def");
            String text = "abcdef";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            // Both "abc" patterns should match at position 0
            assertEquals(Arrays.asList(0), result.get(0));
            assertEquals(Arrays.asList(0), result.get(1));
            assertEquals(Arrays.asList(3), result.get(2)); // "def"
        }

        @Test
        @DisplayName("Special characters")
        void testSpecialCharacters() {
            List<String> patterns = Arrays.asList("@#$", "!@#", "$%^");
            String text = "!@#$%^";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(Arrays.asList(1), result.get(0)); // "@#$"
            assertEquals(Arrays.asList(0), result.get(1)); // "!@#"
            assertEquals(Arrays.asList(3), result.get(2)); // "$%^"
        }
    }

    @Nested
    @DisplayName("DNA Sequence Matching")
    class DNASequenceTests {

        @Test
        @DisplayName("DNA codons matching")
        void testDNACodons() {
            List<String> patterns = Arrays.asList("ATG", "TGC", "GCA", "CAT");
            String text = "ATGCATGCAT";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(Arrays.asList(0, 4), result.get(0)); // "ATG"
            assertEquals(Arrays.asList(1, 5), result.get(1)); // "TGC"
            assertEquals(Arrays.asList(2, 6), result.get(2)); // "GCA"
            assertEquals(Arrays.asList(3, 7), result.get(3)); // "CAT"
        }

        @Test
        @DisplayName("Find start codons")
        void testStartCodons() {
            List<String> patterns = Arrays.asList("ATG", "GTG", "TTG");
            String text = "ATGCGTGATGTTG";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertTrue(ac.containsAny(text));
            assertTrue(ac.countMatches(text) >= 3);
        }
    }

    @Nested
    @DisplayName("Content Filtering Tests")
    class ContentFilteringTests {

        @Test
        @DisplayName("Filter forbidden words")
        void testForbiddenWords() {
            List<String> patterns = Arrays.asList("bad", "evil", "wrong");
            String text = "This is a bad example with evil intent";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            assertTrue(ac.containsAny(text));
            assertEquals(2, ac.countMatches(text));
        }

        @Test
        @DisplayName("No forbidden words")
        void testNoForbiddenWords() {
            List<String> patterns = Arrays.asList("bad", "evil", "wrong");
            String text = "This is a good example with positive intent";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            assertFalse(ac.containsAny(text));
            assertEquals(0, ac.countMatches(text));
        }

        @Test
        @DisplayName("Case sensitive filtering")
        void testCaseSensitive() {
            List<String> patterns = Arrays.asList("Bad", "Evil");
            String text = "bad and evil are present";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            // Should not match because of case difference
            assertFalse(ac.containsAny(text));
        }
    }

    @Nested
    @DisplayName("LeetCode 1032: Stream of Characters")
    class StreamCheckerTests {

        @Test
        @DisplayName("Basic stream checker")
        void testBasicStreamChecker() {
            AhoCorasickTemplates.StreamChecker sc = 
                new AhoCorasickTemplates.StreamChecker(new String[]{"cd", "f", "kl"});
            
            assertFalse(sc.query('a')); // a
            assertFalse(sc.query('b')); // ab
            assertFalse(sc.query('c')); // abc
            assertTrue(sc.query('d'));  // abcd - "cd" matched
            assertFalse(sc.query('e')); // bcde
            assertTrue(sc.query('f'));  // cdef - "f" matched
            assertFalse(sc.query('g')); // defg
            assertFalse(sc.query('h')); // efgh
            assertFalse(sc.query('i')); // fghi
            assertFalse(sc.query('j')); // ghij
            assertFalse(sc.query('k')); // hijk
            assertTrue(sc.query('l'));  // ijkl - "kl" matched
        }

        @Test
        @DisplayName("Stream with single character words")
        void testSingleCharWords() {
            AhoCorasickTemplates.StreamChecker sc = 
                new AhoCorasickTemplates.StreamChecker(new String[]{"a", "b", "c"});
            
            assertTrue(sc.query('a'));  // "a" matched
            assertTrue(sc.query('b'));  // "b" matched
            assertTrue(sc.query('c'));  // "c" matched
            assertFalse(sc.query('d')); // no match
        }

        @Test
        @DisplayName("Stream with overlapping suffixes")
        void testOverlappingSuffixes() {
            AhoCorasickTemplates.StreamChecker sc = 
                new AhoCorasickTemplates.StreamChecker(new String[]{"ab", "ba", "aaab"});
            
            assertFalse(sc.query('a')); // a
            assertFalse(sc.query('a')); // aa  
            assertFalse(sc.query('a')); // aaa
            assertTrue(sc.query('b'));  // aaab - both "ab" and "aaab" matched
        }

        @Test
        @DisplayName("Stream longer than max word length")
        void testLongStream() {
            AhoCorasickTemplates.StreamChecker sc = 
                new AhoCorasickTemplates.StreamChecker(new String[]{"abc"});
            
            assertFalse(sc.query('x'));
            assertFalse(sc.query('y'));
            assertFalse(sc.query('z'));
            assertFalse(sc.query('a'));
            assertFalse(sc.query('b'));
            assertTrue(sc.query('c'));  // "abc" matched (last 3 chars)
        }
    }

    @Nested
    @DisplayName("Performance and Stress Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Large number of patterns")
        void testManyPatterns() {
            List<String> patterns = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                patterns.add("pattern" + i);
            }
            
            String text = "pattern50pattern75pattern99";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            // Patterns found:
            // - "pattern5" (prefix of pattern50) at position 0
            // - "pattern50" at position 0
            // - "pattern7" (prefix of pattern75) at position 9
            // - "pattern75" at position 9
            // - "pattern9" (prefix of pattern99) at position 18
            // - "pattern99" at position 18
            // Total: 6 matches
            assertEquals(6, ac.countMatches(text));
            assertTrue(ac.containsAny(text));
        }

        @Test
        @DisplayName("Large text with repeated patterns")
        void testLargeText() {
            List<String> patterns = Arrays.asList("abc", "def");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                sb.append("abc");
                sb.append("def");
            }
            String text = sb.toString();
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            assertEquals(200, ac.countMatches(text)); // 100 "abc" + 100 "def"
            assertTrue(ac.containsAny(text));
        }

        @Test
        @DisplayName("All patterns match at same position")
        void testAllPatternsMatchSamePosition() {
            List<String> patterns = Arrays.asList("a", "ab", "abc", "abcd", "abcde");
            String text = "abcde";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            assertEquals(5, ac.countMatches(text));
        }
    }

    @Nested
    @DisplayName("Failure Link Tests")
    class FailureLinkTests {

        @Test
        @DisplayName("Failure link backtracking")
        void testFailureLinkBacktracking() {
            // Pattern that requires proper failure link handling
            List<String> patterns = Arrays.asList("aba", "bab");
            String text = "ababab";  // length 6, indices 0-5
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            // "aba" at positions 0, 2 (starting indices)
            // Position 0: "aba" = chars 0,1,2 = "aba" ✓
            // Position 2: "aba" = chars 2,3,4 = "aba" ✓
            assertEquals(Arrays.asList(0, 2), result.get(0));
            
            // "bab" at positions 1, 3 (starting indices)
            // Position 1: "bab" = chars 1,2,3 = "bab" ✓
            // Position 3: "bab" = chars 3,4,5 = "bab" ✓
            assertEquals(Arrays.asList(1, 3), result.get(1));
        }

        @Test
        @DisplayName("Complex failure link scenario")
        void testComplexFailureLinks() {
            List<String> patterns = Arrays.asList("she", "he", "her", "hers");
            String text = "shershe";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertTrue(ac.countMatches(text) >= 4);
        }
    }

    @Nested
    @DisplayName("Count Matches Tests")
    class CountMatchesTests {

        @Test
        @DisplayName("Count all occurrences")
        void testCountAllOccurrences() {
            List<String> patterns = Arrays.asList("a");
            String text = "aaaaa";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            assertEquals(5, ac.countMatches(text));
        }

        @Test
        @DisplayName("Count overlapping matches")
        void testCountOverlappingMatches() {
            List<String> patterns = Arrays.asList("aa");
            String text = "aaaa";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            // "aa" appears at positions 0, 1, 2
            assertEquals(3, ac.countMatches(text));
        }
    }

    @Nested
    @DisplayName("Contains Any Tests")
    class ContainsAnyTests {

        @Test
        @DisplayName("Text contains at least one pattern")
        void testContainsOnePattern() {
            List<String> patterns = Arrays.asList("abc", "def", "ghi");
            String text = "xyz def uvw";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            assertTrue(ac.containsAny(text));
        }

        @Test
        @DisplayName("Text contains no patterns")
        void testContainsNoPatterns() {
            List<String> patterns = Arrays.asList("abc", "def", "ghi");
            String text = "xyz uvw rst";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            assertFalse(ac.containsAny(text));
        }

        @Test
        @DisplayName("Early termination on first match")
        void testEarlyTermination() {
            List<String> patterns = Arrays.asList("abc");
            String text = "abc" + "x".repeat(1000);
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            // Should find match quickly without scanning entire text
            assertTrue(ac.containsAny(text));
        }
    }

    @Nested
    @DisplayName("Unicode and Special Cases")
    class UnicodeTests {

        @Test
        @DisplayName("Unicode characters")
        void testUnicodeCharacters() {
            List<String> patterns = Arrays.asList("你好", "世界");
            String text = "你好世界";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(Arrays.asList(0), result.get(0)); // "你好"
            assertEquals(Arrays.asList(2), result.get(1)); // "世界"
        }

        @Test
        @DisplayName("Mixed ASCII and Unicode")
        void testMixedASCIIUnicode() {
            List<String> patterns = Arrays.asList("hello", "مرحبا");
            String text = "hello مرحبا world";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            assertEquals(2, ac.countMatches(text));
        }

        @Test
        @DisplayName("Emojis")
        void testEmojis() {
            List<String> patterns = Arrays.asList("😀", "🎉");
            String text = "Hello 😀 World 🎉";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            assertTrue(ac.containsAny(text));
            assertEquals(2, ac.countMatches(text));
        }
    }

    @Nested
    @DisplayName("Regression Tests")
    class RegressionTests {

        @Test
        @DisplayName("Pattern is prefix of another pattern")
        void testPrefixPattern() {
            List<String> patterns = Arrays.asList("test", "testing");
            String text = "testing";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(Arrays.asList(0), result.get(0)); // "test"
            assertEquals(Arrays.asList(0), result.get(1)); // "testing"
        }

        @Test
        @DisplayName("Pattern is suffix of another pattern")
        void testSuffixPattern() {
            List<String> patterns = Arrays.asList("ing", "testing");
            String text = "testing";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            Map<Integer, List<Integer>> result = ac.search(text);
            
            assertEquals(Arrays.asList(4), result.get(0)); // "ing"
            assertEquals(Arrays.asList(0), result.get(1)); // "testing"
        }

        @Test
        @DisplayName("Consecutive matches without gap")
        void testConsecutiveMatches() {
            List<String> patterns = Arrays.asList("ab", "bc", "cd");
            String text = "abcd";
            
            AhoCorasickTemplates.AhoCorasick ac = new AhoCorasickTemplates.AhoCorasick(patterns);
            
            assertEquals(3, ac.countMatches(text));
        }
    }
}
