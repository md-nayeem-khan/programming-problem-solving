package com.cp.algorithms;

import java.util.ArrayList;
import java.util.List;

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
                if (j == m) { result.add(i - j); j = lps[j - 1]; }
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
        String concat = pattern + "$" + text;
        int[] z = zFunction(concat);
        List<Integer> result = new ArrayList<>();
        int m = pattern.length();
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
        private static final long BASE = 31L;

        public static List<Integer> search(String text, String pattern) {
            List<Integer> result = new ArrayList<>();
            int n = text.length(), m = pattern.length();
            if (m > n) return result;

            long patHash = 0, winHash = 0, power = 1;

            for (int i = 0; i < m; i++) {
                patHash = (patHash * BASE + (pattern.charAt(i) - 'a' + 1)) % MOD;
                winHash = (winHash * BASE + (text.charAt(i) - 'a' + 1)) % MOD;
                if (i > 0) power = (power * BASE) % MOD;
            }

            for (int i = 0; i <= n - m; i++) {
                if (winHash == patHash && text.substring(i, i + m).equals(pattern))
                    result.add(i);
                if (i < n - m) {
                    winHash = (winHash - (text.charAt(i) - 'a' + 1) * power % MOD + MOD) % MOD;
                    winHash = (winHash * BASE + (text.charAt(i + m) - 'a' + 1)) % MOD;
                }
            }
            return result;
        }
    }

    // ---------------------------------------------------------------
    // 4. Manacher — longest palindromic substring
    // ---------------------------------------------------------------
    public static String longestPalindrome(String s) {
        // Transform: "abc" -> "^#a#b#c#$"
        StringBuilder t = new StringBuilder("^#");
        for (char c : s.toCharArray()) t.append(c).append('#');
        t.append('$');
        String T = t.toString();
        int n = T.length();
        int[] p = new int[n];
        int C = 0, R = 0;

        for (int i = 1; i < n - 1; i++) {
            int mirror = 2 * C - i;
            if (i < R) p[i] = Math.min(R - i, p[mirror]);
            while (T.charAt(i + p[i] + 1) == T.charAt(i - p[i] - 1)) p[i]++;
            if (i + p[i] > R) { C = i; R = i + p[i]; }
        }

        int maxLen = 0, center = 0;
        for (int i = 1; i < n - 1; i++) {
            if (p[i] > maxLen) { maxLen = p[i]; center = i; }
        }
        int start = (center - maxLen) / 2;
        return s.substring(start, start + maxLen);
    }
}
