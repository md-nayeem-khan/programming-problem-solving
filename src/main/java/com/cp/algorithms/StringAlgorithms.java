package com.cp.algorithms;

import com.cp.problems.Complexity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * String algorithm templates:
 *   1. KMP — pattern matching in O(n+m)
 *   2. Z-function — all prefix match lengths in O(n)
 *   3. Rabin-Karp — rolling hash pattern matching
 *   4. Manacher — longest palindromic substring in O(n)
 *
 * Usage:
 *   List<Integer> positions = KMPSearch.search("aabaab", "aab"); // [0, 3]
 *   int[] z = ZFunction.compute("aabxaa");
 */
public class StringAlgorithms {

    // ---------------------------------------------------------------
    // 1. KMP — Knuth-Morris-Pratt
    // ---------------------------------------------------------------
    public static class KMP {

        /** Build failure (partial match) table for pattern */
        public static int[] buildLPS(String pattern) {
            int m = pattern.length();
            int[] lps = new int[m];
            int len = 0, i = 1;
            while (i < m) {
                if (pattern.charAt(i) == pattern.charAt(len)) { lps[i++] = ++len; }
                else if (len > 0)                              { len = lps[len - 1]; }
                else                                           { lps[i++] = 0; }
            }
            return lps;
        }

        /**
         * Find all starting indices of pattern in text.
         * Returns list of 0-indexed positions.
         */
        public static List<Integer> search(String text, String pattern) {
            List<Integer> result = new ArrayList<>();
            int n = text.length(), m = pattern.length();
            if (m == 0) return result;

            int[] lps = buildLPS(pattern);
            int i = 0, j = 0;

            while (i < n) {
                if (text.charAt(i) == pattern.charAt(j)) { i++; j++; }
                if (j == m) { result.add(i - j); j   = lps[j - 1]; }
                else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                    if (j > 0) j = lps[j - 1]; else i++;
                }
            }
            return result;
        }
    }

    // ---------------------------------------------------------------
    // 2. Z-function
    // z[i] = length of the longest substring starting at s[i] that is
    //        also a prefix of s. z[0] = 0 by convention.
    // ---------------------------------------------------------------
    public static int[] zFunction(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i < r) z[i] = Math.min(r - i, z[i - l]);
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
            if (i + z[i] > r) { l = i; r = i + z[i]; }
        }
        return z;
    }

    /**
     * Pattern matching using Z-function:
     * Concatenate pattern + "$" + text, then find positions where z[i] == m.
     */
    public static List<Integer> zSearch(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int m = pattern.length();
        if (m == 0) return result; // Handle empty pattern
        
        String concat = pattern + "$" + text;
        int[] z = zFunction(concat);
        for (int i = m + 1; i < concat.length(); i++) {
            if (z[i] == m) result.add(i - m - 1);
        }
        return result;
    }

    // ---------------------------------------------------------------
    // 3. Rabin-Karp rolling hash
    // ---------------------------------------------------------------
    public static class RabinKarp {
        private static final long MOD  = 1_000_000_007L;
        private static final long BASE = 256L; // Use 256 for all ASCII characters

        public static List<Integer> search(String text, String pattern) {
            List<Integer> result = new ArrayList<>();
            int n = text.length(), m = pattern.length();
            if (m == 0 || m > n) return result; // Handle empty pattern and pattern longer than text

            long patHash = 0, winHash = 0, power = 1;

            for (int i = 0; i < m; i++) {
                patHash = (patHash * BASE + pattern.charAt(i)) % MOD;
                winHash = (winHash * BASE + text.charAt(i)) % MOD;
                if (i < m - 1) power = (power * BASE) % MOD; // BASE^(m-1)
            }

            for (int i = 0; i <= n - m; i++) {
                if (winHash == patHash && text.substring(i, i + m).equals(pattern))
                    result.add(i);
                if (i < n - m) {
                    winHash = (winHash - (text.charAt(i) * power) % MOD + MOD) % MOD;
                    winHash = (winHash * BASE + text.charAt(i + m)) % MOD;
                }
            }
            return result;
        }
    }

    // ---------------------------------------------------------------
    // 4. Manacher's Algorithm — O(n) longest palindromic substring
    // ---------------------------------------------------------------
    
    /**
     * Manacher's Algorithm - Find longest palindromic substring in O(n)
     * 
     * Key Insight: Use previously computed palindrome information to skip redundant checks
     * Transform string to handle both odd and even length palindromes uniformly
     * 
     * Time: O(n), Space: O(n)
     * 
     * LeetCode: 5. Longest Palindromic Substring
     * Companies: Meta (5/5), Google (5/5), Amazon (4/5)
     * 
     * Example:
     *   longestPalindrome("babad") -> "bab" or "aba"
     *   longestPalindrome("cbbd") -> "bb"
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static String longestPalindrome(String s) {
        if (s == null || s.length() == 0) return "";
        
        // Transform: "abc" -> "^#a#b#c#$"
        // ^ and $ are sentinels to avoid boundary checks
        // # allows us to treat odd and even length palindromes uniformly
        StringBuilder t = new StringBuilder("^#");
        for (char c : s.toCharArray()) {
            t.append(c).append('#');
        }
        t.append('$');
        String T = t.toString();
        
        int n = T.length();
        int[] p = new int[n]; // p[i] = radius of palindrome centered at i
        int C = 0; // center of rightmost palindrome
        int R = 0; // right boundary of rightmost palindrome
        
        for (int i = 1; i < n - 1; i++) {
            // Mirror of i with respect to center C
            int mirror = 2 * C - i;
            
            // If i is within the right boundary R, we can use previously computed values
            if (i < R) {
                p[i] = Math.min(R - i, p[mirror]);
            }
            
            // Try to expand palindrome centered at i
            while (T.charAt(i + p[i] + 1) == T.charAt(i - p[i] - 1)) {
                p[i]++;
            }
            
            // If palindrome centered at i extends past R, update C and R
            if (i + p[i] > R) {
                C = i;
                R = i + p[i];
            }
        }
        
        // Find the longest palindrome
        int maxLen = 0;
        int centerIndex = 0;
        for (int i = 1; i < n - 1; i++) {
            if (p[i] > maxLen) {
                maxLen = p[i];
                centerIndex = i;
            }
        }
        
        // Convert back to original string indices
        int start = (centerIndex - maxLen) / 2;
        return s.substring(start, start + maxLen);
    }
    
    /**
     * Manacher's Algorithm - Compute palindrome radii array
     * Returns array where p[i] = length of longest palindrome centered at position i
     * 
     * Time: O(n), Space: O(n)
     * 
     * Example:
     *   manacherArray("abacabad") -> [0, 0, 1, 0, 3, 0, 1, 0, 1, ...]
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static int[] manacherArray(String s) {
        if (s == null || s.length() == 0) return new int[0];
        
        StringBuilder t = new StringBuilder("^#");
        for (char c : s.toCharArray()) {
            t.append(c).append('#');
        }
        t.append('$');
        String T = t.toString();
        
        int n = T.length();
        int[] p = new int[n];
        int C = 0, R = 0;
        
        for (int i = 1; i < n - 1; i++) {
            int mirror = 2 * C - i;
            if (i < R) {
                p[i] = Math.min(R - i, p[mirror]);
            }
            while (T.charAt(i + p[i] + 1) == T.charAt(i - p[i] - 1)) {
                p[i]++;
            }
            if (i + p[i] > R) {
                C = i;
                R = i + p[i];
            }
        }
        
        return p;
    }
    
    /**
     * Count all palindromic substrings using Manacher's algorithm
     * 
     * Time: O(n), Space: O(n)
     * 
     * LeetCode: 647. Palindromic Substrings
     * 
     * Example:
     *   countPalindromes("abc") -> 3 ("a", "b", "c")
     *   countPalindromes("aaa") -> 6 ("a", "a", "a", "aa", "aa", "aaa")
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static int countPalindromes(String s) {
        if (s == null || s.length() == 0) return 0;
        
        int[] p = manacherArray(s);
        int count = 0;
        
        // Each p[i] represents radius, so number of palindromes centered at i is (p[i] + 1) / 2
        for (int i = 1; i < p.length - 1; i++) {
            count += (p[i] + 1) / 2;
        }
        
        return count;
    }
    
    /**
     * Find all unique palindromic substrings
     * 
     * Time: O(n^2), Space: O(n) - Using simple center expansion for correctness
     * 
     * Example:
     *   getAllPalindromes("aab") -> ["a", "aa", "b"]
     */
    @Complexity(time = "O(n^2)", space = "O(n)")
    public static List<String> getAllPalindromes(String s) {
        if (s == null || s.length() == 0) return new ArrayList<>();
        
        Set<String> result = new HashSet<>();
        
        // Find all palindromes by center expansion
        for (int center = 0; center < s.length(); center++) {
            // Odd length palindromes
            expandAroundCenter(s, center, center, result);
            // Even length palindromes
            if (center < s.length() - 1) {
                expandAroundCenter(s, center, center + 1, result);
            }
        }
        
        return new ArrayList<>(result);
    }
    
    private static void expandAroundCenter(String s, int left, int right, Set<String> result) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            result.add(s.substring(left, right + 1));
            left--;
            right++;
        }
    }
    
    /**
     * Check if string is k-palindrome (can be palindrome after removing at most k characters)
     * Uses LCS approach: k-palindrome if LCS(s, reverse(s)) >= n - k
     * 
     * Time: O(n^2), Space: O(n)
     * 
     * Example:
     *   isKPalindrome("abcdeca", 2) -> true (remove 'b' and 'd' to get "aceca")
     */
    @Complexity(time = "O(n^2)", space = "O(n)")
    public static boolean isKPalindrome(String s, int k) {
        String rev = new StringBuilder(s).reverse().toString();
        int n = s.length();
        
        // LCS using space-optimized DP
        int[] dp = new int[n + 1];
        int[] prev = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (s.charAt(i - 1) == rev.charAt(j - 1)) {
                    dp[j] = prev[j - 1] + 1;
                } else {
                    dp[j] = Math.max(dp[j - 1], prev[j]);
                }
            }
            int[] temp = prev;
            prev = dp;
            dp = temp;
        }
        
        int lcs = prev[n];
        return n - lcs <= k;
    }
}
