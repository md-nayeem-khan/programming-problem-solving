package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Suffix Array and Suffix Tree
 * MEDIUM PRIORITY for Google hard string problems
 * 
 * Essential for:
 * - Pattern matching in O(m log n)
 * - Longest common substring
 * - Longest repeated substring
 * - Finding all occurrences of pattern
 * - String comparisons and sorting
 * 
 * Interview Questions:
 * - LeetCode 1044: Longest Duplicate Substring
 * - LeetCode 1062: Longest Repeating Substring
 * - Pattern matching problems
 * - Substring search optimization
 * 
 * Key Concepts:
 * - Suffix Array: Sorted array of all suffixes
 * - LCP Array: Longest Common Prefix between adjacent suffixes
 * - Construction: O(n log n) or O(n) with advanced algorithms
 * - Space efficient compared to suffix tree
 * 
 * Companies: Google (5/5), Amazon (3/5), Competitive Programming (5/5)
 */
public class SuffixArrayTemplates {
    
    // ========== SUFFIX ARRAY CONSTRUCTION ==========
    
    /**
     * Build suffix array using O(n log² n) algorithm
     * 
     * Suffix array: sa[i] = starting index of i-th smallest suffix
     * Example: "banana"
     * Suffixes sorted: "a", "ana", "anana", "banana", "na", "nana"
     * Suffix array: [5, 3, 1, 0, 4, 2]
     * 
     * @param s input string
     * @return suffix array
     */
    @Complexity(time = "O(n log² n)", space = "O(n)")
    public static int[] buildSuffixArray(String s) {
        int n = s.length();
        Integer[] sa = new Integer[n];
        int[] rank = new int[n];
        
        // Initialize: rank by first character
        for (int i = 0; i < n; i++) {
            sa[i] = i;
            rank[i] = s.charAt(i);
        }
        
        // Double the compared length each iteration
        for (int k = 1; k < n; k *= 2) {
            final int K = k;
            final int[] currRank = rank.clone();
            
            // Sort by (rank[i], rank[i+k])
            Arrays.sort(sa, (i, j) -> {
                if (currRank[i] != currRank[j]) {
                    return currRank[i] - currRank[j];
                }
                int ri = (i + K < n) ? currRank[i + K] : -1;
                int rj = (j + K < n) ? currRank[j + K] : -1;
                return ri - rj;
            });
            
            // Recompute ranks
            rank[sa[0]] = 0;
            for (int i = 1; i < n; i++) {
                rank[sa[i]] = rank[sa[i - 1]];
                
                if (currRank[sa[i]] != currRank[sa[i - 1]]) {
                    rank[sa[i]]++;
                } else {
                    int ri = (sa[i] + K < n) ? currRank[sa[i] + K] : -1;
                    int rj = (sa[i - 1] + K < n) ? currRank[sa[i - 1] + K] : -1;
                    if (ri != rj) {
                        rank[sa[i]]++;
                    }
                }
            }
        }
        
        return Arrays.stream(sa).mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * Build suffix array using faster O(n log n) counting sort
     */
    /**
     * Build suffix array using O(n log² n) algorithm with optimized sorting
     * This is the "Fast" version but we use the same algorithm as buildSuffixArray
     * for correctness. The name is kept for API compatibility.
     */
    @Complexity(time = "O(n log² n)", space = "O(n)")
    public static int[] buildSuffixArrayFast(String s) {
        int n = s.length();
        Integer[] sa = new Integer[n];
        int[] rank = new int[n];
        
        // Initialize: rank by first character
        for (int i = 0; i < n; i++) {
            sa[i] = i;
            rank[i] = s.charAt(i);
        }
        
        // Double the compared length each iteration
        for (int k = 1; k < n; k *= 2) {
            final int K = k;
            final int[] currRank = rank.clone();
            
            // Sort by (rank[i], rank[i+k])
            Arrays.sort(sa, (i, j) -> {
                if (currRank[i] != currRank[j]) {
                    return currRank[i] - currRank[j];
                }
                int ri = (i + K < n) ? currRank[i + K] : -1;
                int rj = (j + K < n) ? currRank[j + K] : -1;
                return ri - rj;
            });
            
            // Recompute ranks
            rank[sa[0]] = 0;
            for (int i = 1; i < n; i++) {
                rank[sa[i]] = rank[sa[i - 1]];
                
                if (currRank[sa[i]] != currRank[sa[i - 1]]) {
                    rank[sa[i]]++;
                } else {
                    int ri = (sa[i] + K < n) ? currRank[sa[i] + K] : -1;
                    int rj = (sa[i - 1] + K < n) ? currRank[sa[i - 1] + K] : -1;
                    if (ri != rj) {
                        rank[sa[i]]++;
                    }
                }
            }
        }
        
        return Arrays.stream(sa).mapToInt(Integer::intValue).toArray();
    }
    
    // ========== LCP ARRAY (LONGEST COMMON PREFIX) ==========
    
    /**
     * Build LCP array using Kasai's algorithm O(n)
     * 
     * LCP[i] = length of longest common prefix between suffix sa[i] and sa[i-1]
     * 
     * @param s input string
     * @param sa suffix array
     * @return LCP array
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static int[] buildLCPArray(String s, int[] sa) {
        int n = s.length();
        int[] lcp = new int[n];
        int[] rank = new int[n];
        
        // Compute rank array (inverse of suffix array)
        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }
        
        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1];
                
                while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) {
                    h++;
                }
                
                lcp[rank[i]] = h;
                
                if (h > 0) h--;
            }
        }
        
        return lcp;
    }
    
    // ========== PATTERN MATCHING ==========
    
    /**
     * Find all occurrences of pattern in text using suffix array
     * Binary search on suffix array
     * 
     * @param text haystack
     * @param pattern needle
     * @return list of starting indices where pattern occurs
     */
    @Complexity(time = "O(m log n + k)", space = "O(n)")
    public static List<Integer> patternMatching(String text, String pattern) {
        int[] sa = buildSuffixArrayFast(text);
        int n = text.length();
        int m = pattern.length();
        
        if (m > n) return new ArrayList<>();
        
        // Find leftmost position where pattern could start
        // Find first suffix >= pattern
        int left = 0, right = n;
        while (left < right) {
            int mid = (left + right) / 2;
            String suffix = text.substring(sa[mid], Math.min(sa[mid] + m, n));
            if (suffix.compareTo(pattern) < 0) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        int start = left;
        
        // Find rightmost position
        // Find first suffix > pattern
        left = 0;
        right = n;
        while (left < right) {
            int mid = (left + right) / 2;
            String suffix = text.substring(sa[mid], Math.min(sa[mid] + m, n));
            if (suffix.compareTo(pattern) <= 0) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        int end = left;
        
        // Collect all occurrences
        List<Integer> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            if (sa[i] + m <= n) {
                String substr = text.substring(sa[i], sa[i] + m);
                if (substr.equals(pattern)) {
                    result.add(sa[i]);
                }
            }
        }
        
        return result;
    }
    
    // ========== LONGEST REPEATED SUBSTRING ==========
    
    /**
     * LeetCode 1062: Longest Repeating Substring
     * Find the longest substring that appears at least twice
     * 
     * @param s input string
     * @return length of longest repeating substring
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static int longestRepeatingSubstring(String s) {
        int[] sa = buildSuffixArrayFast(s);
        int[] lcp = buildLCPArray(s, sa);
        
        int maxLen = 0;
        for (int len : lcp) {
            maxLen = Math.max(maxLen, len);
        }
        
        return maxLen;
    }
    
    /**
     * Get the actual longest repeating substring
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static String longestRepeatingSubstringActual(String s) {
        int[] sa = buildSuffixArrayFast(s);
        int[] lcp = buildLCPArray(s, sa);
        
        int maxLen = 0;
        int maxIdx = 0;
        
        for (int i = 0; i < lcp.length; i++) {
            if (lcp[i] > maxLen) {
                maxLen = lcp[i];
                maxIdx = sa[i];
            }
        }
        
        return maxLen == 0 ? "" : s.substring(maxIdx, maxIdx + maxLen);
    }
    
    // ========== LONGEST COMMON SUBSTRING OF TWO STRINGS ==========
    
    /**
     * Find longest common substring between two strings
     * 
     * @param s1 first string
     * @param s2 second string
     * @return longest common substring
     */
    @Complexity(time = "O((n+m) log(n+m))", space = "O(n+m)")
    public static String longestCommonSubstring(String s1, String s2) {
        // Concatenate with sentinel
        String combined = s1 + "#" + s2;
        int n1 = s1.length();
        
        int[] sa = buildSuffixArrayFast(combined);
        int[] lcp = buildLCPArray(combined, sa);
        
        int maxLen = 0;
        int maxIdx = 0;
        
        for (int i = 1; i < sa.length; i++) {
            // Check if adjacent suffixes are from different strings
            boolean diff = (sa[i] < n1) != (sa[i - 1] < n1);
            if (diff && lcp[i] > maxLen) {
                maxLen = lcp[i];
                maxIdx = sa[i];
            }
        }
        
        return maxLen == 0 ? "" : combined.substring(maxIdx, maxIdx + maxLen);
    }
    
    // ========== DISTINCT SUBSTRINGS ==========
    
    /**
     * Count number of distinct substrings
     * Total substrings = n*(n+1)/2
     * Duplicates = sum of LCP array
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static long countDistinctSubstrings(String s) {
        int n = s.length();
        int[] sa = buildSuffixArrayFast(s);
        int[] lcp = buildLCPArray(s, sa);
        
        long total = (long) n * (n + 1) / 2;
        long duplicates = 0;
        
        for (int len : lcp) {
            duplicates += len;
        }
        
        return total - duplicates;
    }
    
    // ========== LEETCODE 1044: LONGEST DUPLICATE SUBSTRING ==========
    
    /**
     * Find longest duplicate substring using suffix array
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static String longestDupSubstring(String s) {
        int[] sa = buildSuffixArrayFast(s);
        int[] lcp = buildLCPArray(s, sa);
        
        int maxLen = 0;
        int maxIdx = 0;
        
        for (int i = 1; i < sa.length; i++) {
            if (lcp[i] > maxLen) {
                maxLen = lcp[i];
                maxIdx = sa[i];
            }
        }
        
        return maxLen == 0 ? "" : s.substring(maxIdx, maxIdx + maxLen);
    }
    
    // ========== ENHANCED SUFFIX OPERATIONS (KASAI ALGORITHM & ADVANCED QUERIES) ==========
    
    /**
     * KASAI'S ALGORITHM - Build LCP Array in O(n)
     * 
     * More efficient than the O(n²) method above
     * Uses the property that LCP[rank[i]] >= LCP[rank[i-1]] - 1
     * 
     * Time: O(n)
     * Space: O(n)
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static int[] buildLCPKasai(String s, int[] sa) {
        int n = s.length();
        int[] lcp = new int[n - 1];
        int[] rank = new int[n];
        
        // Build rank array (inverse of suffix array)
        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }
        
        int h = 0; // Height of current LCP
        for (int i = 0; i < n; i++) {
            if (rank[i] == 0) continue; // Skip last suffix in sorted order
            
            int j = sa[rank[i] - 1]; // Previous suffix in sorted order
            
            // Calculate LCP between suffix starting at i and suffix starting at j
            while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) {
                h++;
            }
            
            lcp[rank[i] - 1] = h;
            
            if (h > 0) h--; // Kasai's optimization
        }
        
        return lcp;
    }
    
    /**
     * ADVANCED STRING METRICS using Suffix Array
     */
    static class AdvancedStringMetrics {
        // Find all periods of string using suffix array
        public static List<Integer> findAllPeriods(String s) {
            List<Integer> periods = new ArrayList<>();
            int n = s.length();
            
            for (int p = 1; p <= n; p++) {
                if (n % p == 0 && hasPeriod(s, p)) {
                    periods.add(p);
                }
            }
            
            return periods;
        }
        
        // Check if string has period p
        private static boolean hasPeriod(String s, int p) {
            int n = s.length();
            for (int i = 0; i < n; i++) {
                if (s.charAt(i) != s.charAt(i % p)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    /**
     * SUFFIX ARRAY BASED LCS for Multiple Strings
     * 
     * Find longest common substring among k strings
     */
    @Complexity(time = "O(N log N)", space = "O(N)")
    public static String longestCommonSubstringMultiple(List<String> strings) {
        if (strings.isEmpty()) return "";
        if (strings.size() == 1) return strings.get(0);
        
        // Combine all strings with unique separators
        // Use special characters that won't appear in the input strings
        StringBuilder combined = new StringBuilder();
        int[] boundaries = new int[strings.size() + 1];
        
        // Find a separator character that doesn't exist in any string
        // Start with characters unlikely to be in text: control characters
        char baseSep = '\u0001';
        
        for (int i = 0; i < strings.size(); i++) {
            combined.append(strings.get(i));
            boundaries[i + 1] = combined.length();
            if (i < strings.size() - 1) {
                // Use sequential control characters as separators
                combined.append((char)(baseSep + i));
            }
        }
        
        String text = combined.toString();
        int[] sa = buildSuffixArrayFast(text);
        int[] lcp = buildLCPKasai(text, sa);
        
        int maxLen = 0;
        int bestPos = 0;
        
        // Use sliding window to find if a common substring appears in all strings
        // For each position in suffix array, expand window to cover all strings
        for (int i = 0; i < sa.length; i++) {
            // Track which strings we've seen
            Set<Integer> stringIds = new HashSet<>();
            int minLCP = Integer.MAX_VALUE;
            
            // Determine which string this suffix belongs to
            int pos = sa[i];
            int stringId = -1;
            for (int k = 0; k < strings.size(); k++) {
                if (pos >= boundaries[k] && pos < boundaries[k + 1]) {
                    stringId = k;
                    break;
                }
            }
            if (stringId == -1) continue; // Separator character
            stringIds.add(stringId);
            
            // Expand window to the right
            for (int j = i + 1; j < sa.length && stringIds.size() < strings.size(); j++) {
                // Update minimum LCP in this window
                if (j - 1 < lcp.length) {
                    minLCP = Math.min(minLCP, lcp[j - 1]);
                }
                
                // Find which string sa[j] belongs to
                pos = sa[j];
                for (int k = 0; k < strings.size(); k++) {
                    if (pos >= boundaries[k] && pos < boundaries[k + 1]) {
                        stringIds.add(k);
                        break;
                    }
                }
                
                // If we have all strings, check if this is better
                if (stringIds.size() == strings.size() && minLCP > maxLen) {
                    maxLen = minLCP;
                    bestPos = sa[i];
                }
            }
        }
        
        if (maxLen == 0 || maxLen == Integer.MAX_VALUE) return "";
        return text.substring(bestPos, bestPos + maxLen);
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Suffix Array Examples ===\n");
        
        // Example 1: Build suffix array
        System.out.println("1. Suffix Array Construction:");
        String s1 = "banana";
        int[] sa1 = buildSuffixArrayFast(s1);
        
        System.out.println("   String: \"" + s1 + "\"");
        System.out.println("   Suffix Array: " + Arrays.toString(sa1));
        System.out.println("   Suffixes in sorted order:");
        for (int i = 0; i < sa1.length; i++) {
            System.out.println("     " + i + ": \"" + s1.substring(sa1[i]) + "\" (starts at " + sa1[i] + ")");
        }
        
        // Example 2: LCP Array
        System.out.println("\n2. LCP Array:");
        int[] lcp1 = buildLCPArray(s1, sa1);
        System.out.println("   LCP Array: " + Arrays.toString(lcp1));
        System.out.println("   LCP[i] = longest common prefix of suffix sa[i] and sa[i-1]");
        
        // Example 3: Pattern matching
        System.out.println("\n3. Pattern Matching:");
        String text = "ababcababa";
        String pattern = "aba";
        List<Integer> matches = patternMatching(text, pattern);
        System.out.println("   Text: \"" + text + "\"");
        System.out.println("   Pattern: \"" + pattern + "\"");
        System.out.println("   Occurrences at indices: " + matches);
        
        // Example 4: Longest repeating substring
        System.out.println("\n4. Longest Repeating Substring:");
        String s2 = "aabcaabdaab";
        String longest = longestRepeatingSubstringActual(s2);
        System.out.println("   String: \"" + s2 + "\"");
        System.out.println("   Longest repeating: \"" + longest + "\"");
        System.out.println("   Length: " + longestRepeatingSubstring(s2));
        
        // Example 5: Longest common substring
        System.out.println("\n5. Longest Common Substring:");
        String str1 = "abcdefgh";
        String str2 = "xyzdefpq";
        String lcs = longestCommonSubstring(str1, str2);
        System.out.println("   String 1: \"" + str1 + "\"");
        System.out.println("   String 2: \"" + str2 + "\"");
        System.out.println("   Longest common: \"" + lcs + "\"");
        
        // Example 6: Count distinct substrings
        System.out.println("\n6. Distinct Substrings:");
        String s3 = "ababa";
        long distinct = countDistinctSubstrings(s3);
        System.out.println("   String: \"" + s3 + "\"");
        System.out.println("   Total substrings: " + (s3.length() * (s3.length() + 1) / 2));
        System.out.println("   Distinct substrings: " + distinct);
        
        // Example 7: Performance
        System.out.println("\n7. Performance Comparison:");
        System.out.println("   Naive pattern matching: O(nm)");
        System.out.println("   KMP: O(n + m)");
        System.out.println("   Suffix Array build: O(n log n)");
        System.out.println("   Suffix Array search: O(m log n + k) where k = occurrences");
        System.out.println("   Advantage: Build once, search many patterns efficiently!");
        
        System.out.println("\n=== Key Points ===");
        System.out.println("Suffix Array: Sorted array of all suffixes");
        System.out.println("LCP Array: Longest common prefix between adjacent suffixes");
        System.out.println("Applications: Pattern matching, longest repeating, distinct substrings");
        System.out.println("Space: O(n) vs O(n) for suffix tree, but simpler to implement");
    }
}
