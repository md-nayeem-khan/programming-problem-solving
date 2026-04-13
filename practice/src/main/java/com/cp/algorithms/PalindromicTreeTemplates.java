package com.cp.algorithms;

import java.util.*;

/**
 * Palindromic Tree (Eertree) Templates - All Palindromes in O(n)
 * 
 * WHEN TO USE:
 * - Count all palindromic substrings
 * - Find distinct palindromes in string
 * - Palindrome-related DP optimizations
 * - Advanced string processing with palindrome queries
 * 
 * KEY INSIGHT:
 * Palindromic tree (Eertree) maintains all palindromic substrings
 * Two roots: odd-length palindromes (-1) and even-length palindromes (0)
 * Suffix links connect palindromes to their longest proper palindromic suffix
 * 
 * COMPLEXITY:
 * - Build: O(n) time, O(n) space
 * - Query all palindromes: O(total palindromes)
 * - Count palindromes: O(n) with DP on tree
 * 
 * COMPARISON:
 * - Manacher: O(n), finds longest palindrome at each position, simpler
 * - Palindromic Tree: O(n), finds ALL palindromes, more versatile
 * - Naive: O(n³), check all substrings
 * 
 * COMMON INTERVIEW PROBLEMS:
 * - LeetCode 647: Palindromic Substrings
 * - LeetCode 516: Longest Palindromic Subsequence (related)
 * - Count distinct palindromes
 * - Palindrome queries in strings
 * 
 * 
 */
public class PalindromicTreeTemplates {

    /**
     * 1. PALINDROMIC TREE NODE
     */
    static class PalindromicNode {
        Map<Character, PalindromicNode> children;
        PalindromicNode suffixLink;  // Link to longest proper palindromic suffix
        int length;                  // Length of palindrome
        int start, end;             // First occurrence in original string (optional)
        long count;                 // Number of occurrences in string
        
        PalindromicNode(int length) {
            this.length = length;
            this.children = new HashMap<>();
            this.count = 0;
        }
    }

    /**
     * 2. PALINDROMIC TREE BUILDER - Core Algorithm
     * 
     * Builds tree containing all palindromic substrings
     * 
     * Time: O(n)
     * Space: O(n + alphabet_size)
     * 
     * Two special roots:
     * - root1 (length -1): represents empty palindrome, parent of odd palindromes
     * - root2 (length 0): represents empty string, parent of even palindromes
     */
    static class PalindromicTree {
        private char[] s;
        private int n;
        private PalindromicNode root1, root2;  // Odd and even roots
        private PalindromicNode lastNode;      // Last added node
        private List<PalindromicNode> nodes;   // All nodes for traversal
        private int nodeCount;
        
        public PalindromicTree(String str) {
            s = str.toCharArray();
            n = s.length;
            nodes = new ArrayList<>();
            
            // Initialize roots
            root1 = new PalindromicNode(-1);  // Odd palindromes
            root2 = new PalindromicNode(0);   // Even palindromes
            root1.suffixLink = root1;         // Self-loop
            root2.suffixLink = root1;
            
            nodes.add(root1);
            nodes.add(root2);
            lastNode = root2;
            nodeCount = 2;
            
            build();
        }
        
        private void build() {
            for (int i = 0; i < n; i++) {
                addCharacter(i);
            }
            
            // Count occurrences using suffix links (bottom-up)
            for (int i = nodes.size() - 1; i >= 2; i--) {
                PalindromicNode node = nodes.get(i);
                node.suffixLink.count += node.count;
            }
        }
        
        private void addCharacter(int pos) {
            char ch = s[pos];
            PalindromicNode cur = lastNode;
            
            // Find the longest palindrome ending at pos-1 that can be extended
            while (true) {
                int len = cur.length;
                if (pos - len - 1 >= 0 && s[pos - len - 1] == ch) {
                    break;
                }
                cur = cur.suffixLink;
            }
            
            // Check if palindrome ch + cur + ch already exists
            if (cur.children.containsKey(ch)) {
                lastNode = cur.children.get(ch);
                lastNode.count++;
                return;
            }
            
            // Create new palindrome
            PalindromicNode newNode = new PalindromicNode(cur.length + 2);
            newNode.count = 1;
            newNode.end = pos;
            newNode.start = pos - newNode.length + 1;
            cur.children.put(ch, newNode);
            nodes.add(newNode);
            nodeCount++;
            
            // Find suffix link for new node
            if (newNode.length == 1) {
                newNode.suffixLink = root2;
            } else {
                PalindromicNode suffixCur = cur.suffixLink;
                while (true) {
                    int len = suffixCur.length;
                    if (pos - len - 1 >= 0 && s[pos - len - 1] == ch) {
                        break;
                    }
                    suffixCur = suffixCur.suffixLink;
                }
                newNode.suffixLink = suffixCur.children.get(ch);
            }
            
            lastNode = newNode;
        }
        
        // Get all palindromic substrings
        public List<String> getAllPalindromes() {
            List<String> palindromes = new ArrayList<>();
            
            for (int i = 2; i < nodes.size(); i++) {
                PalindromicNode node = nodes.get(i);
                String palindrome = new String(s, node.start, node.length);
                palindromes.add(palindrome);
            }
            
            return palindromes;
        }
        
        // Count total palindromic substrings (including repetitions)
        public long countAllPalindromes() {
            long total = 0;
            for (int i = 2; i < nodes.size(); i++) {
                total += nodes.get(i).count;
            }
            return total;
        }
        
        // Count distinct palindromic substrings
        public int countDistinctPalindromes() {
            return nodeCount - 2;  // Exclude roots
        }
        
        // Get palindrome statistics
        public Map<String, Long> getPalindromeFrequencies() {
            Map<String, Long> frequencies = new HashMap<>();
            
            for (int i = 2; i < nodes.size(); i++) {
                PalindromicNode node = nodes.get(i);
                String palindrome = new String(s, node.start, node.length);
                frequencies.put(palindrome, node.count);
            }
            
            return frequencies;
        }
        
        // Get all nodes (for advanced processing)
        public List<PalindromicNode> getAllNodes() {
            return new ArrayList<>(nodes.subList(2, nodes.size()));
        }
    }

    /**
     * 3. PALINDROME COUNTING APPLICATIONS
     */
    static class PalindromeCounter {
        // LeetCode 647: Count palindromic substrings
        public static int countSubstrings(String s) {
            PalindromicTree tree = new PalindromicTree(s);
            return (int) tree.countAllPalindromes();
        }
        
        // Count palindromes of specific length
        public static long countPalindromesByLength(String s, int targetLength) {
            PalindromicTree tree = new PalindromicTree(s);
            long count = 0;
            
            for (PalindromicNode node : tree.getAllNodes()) {
                if (node.length == targetLength) {
                    count += node.count;
                }
            }
            
            return count;
        }
        
        // Find longest palindromic substring (alternative to Manacher)
        public static String longestPalindrome(String s) {
            PalindromicTree tree = new PalindromicTree(s);
            String longest = "";
            
            for (PalindromicNode node : tree.getAllNodes()) {
                if (node.length > longest.length()) {
                    longest = new String(s.toCharArray(), node.start, node.length);
                }
            }
            
            return longest;
        }
        
        // Check if string can be partitioned into palindromes
        public static boolean canPartitionIntoPalindromes(String s) {
            int n = s.length();
            PalindromicTree tree = new PalindromicTree(s);
            
            // Build palindrome existence table
            boolean[][] isPalin = new boolean[n][n];
            for (PalindromicNode node : tree.getAllNodes()) {
                isPalin[node.start][node.end] = true;
            }
            
            // DP: dp[i] = true if s[0..i-1] can be partitioned
            boolean[] dp = new boolean[n + 1];
            dp[0] = true;
            
            for (int i = 1; i <= n; i++) {
                for (int j = 0; j < i; j++) {
                    if (dp[j] && isPalin[j][i - 1]) {
                        dp[i] = true;
                        break;
                    }
                }
            }
            
            return dp[n];
        }
    }

    /**
     * 4. ADVANCED PALINDROME QUERIES
     */
    static class PalindromeQueries {
        private PalindromicTree tree;
        private String original;
        
        public PalindromeQueries(String s) {
            original = s;
            tree = new PalindromicTree(s);
        }
        
        // Check if substring s[i..j] is palindrome
        public boolean isPalindrome(int i, int j) {
            // FIX: Add proper boundary checks
            if (i < 0 || j >= original.length() || i > j) return false;
            
            // FIX: Removed unnecessary substring creation to avoid StringIndexOutOfBoundsException
            
            for (PalindromicNode node : tree.getAllNodes()) {
                if (node.start == i && node.end == j) {
                    return true;
                }
            }
            
            return false;
        }
        
        // Count palindromes starting at position i
        public int countPalindromesStartingAt(int i) {
            // FIX: Add boundary check
            if (i < 0 || i >= original.length()) return 0;
            
            int count = 0;
            
            for (PalindromicNode node : tree.getAllNodes()) {
                if (node.start == i) {
                    count++;
                }
            }
            
            return count;
        }
        
        // Count palindromes ending at position i
        public int countPalindromesEndingAt(int i) {
            // FIX: Add boundary check
            if (i < 0 || i >= original.length()) return 0;
            
            int count = 0;
            
            for (PalindromicNode node : tree.getAllNodes()) {
                if (node.end == i) {
                    count++;
                }
            }
            
            return count;
        }
        
        // Get all palindromes containing position i
        public List<String> getPalindromesContaining(int i) {
            // FIX: Add boundary check
            if (i < 0 || i >= original.length()) return new ArrayList<>();
            
            List<String> result = new ArrayList<>();
            
            // FIX: First add single character at position i
            result.add(String.valueOf(original.charAt(i)));
            
            for (PalindromicNode node : tree.getAllNodes()) {
                // Skip single character palindromes as we added them above
                if (node.length == 1) continue;
                
                if (node.start <= i && node.end >= i) {
                    result.add(original.substring(node.start, node.end + 1));
                }
            }
            
            return result;
        }
    }

    /**
     * 5. PALINDROME TREE VARIATIONS
     */
    static class PalindromeVariations {
        // Build palindromic tree for multiple strings simultaneously
        public static List<PalindromicTree> buildMultiple(List<String> strings) {
            List<PalindromicTree> trees = new ArrayList<>();
            for (String s : strings) {
                trees.add(new PalindromicTree(s));
            }
            return trees;
        }
        
        // Find common palindromes in multiple strings
        public static Set<String> findCommonPalindromes(List<String> strings) {
            if (strings.isEmpty()) return new HashSet<>();
            
            List<PalindromicTree> trees = buildMultiple(strings);
            Set<String> common = new HashSet<>(trees.get(0).getAllPalindromes());
            
            for (int i = 1; i < trees.size(); i++) {
                Set<String> current = new HashSet<>(trees.get(i).getAllPalindromes());
                common.retainAll(current);
            }
            
            return common;
        }
        
        // Palindromic tree with case-insensitive comparison
        public static PalindromicTree buildCaseInsensitive(String s) {
            return new PalindromicTree(s.toLowerCase());
        }
        
        // Count palindromes ignoring spaces and punctuation
        public static long countAlphanumericPalindromes(String s) {
            StringBuilder cleaned = new StringBuilder();
            for (char c : s.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    cleaned.append(Character.toLowerCase(c));
                }
            }
            
            PalindromicTree tree = new PalindromicTree(cleaned.toString());
            return tree.countAllPalindromes();
        }
    }

    /**
     * 6. PALINDROME DP OPTIMIZATIONS
     */
    static class PalindromeDPOptimizations {
        // Minimum cuts to partition string into palindromes
        public static int minCuts(String s) {
            int n = s.length();
            PalindromicTree tree = new PalindromicTree(s);
            
            // Build palindrome table
            boolean[][] isPalin = new boolean[n][n];
            for (PalindromicNode node : tree.getAllNodes()) {
                isPalin[node.start][node.end] = true;
            }
            
            // DP: dp[i] = min cuts for s[0..i-1]
            int[] dp = new int[n + 1];
            for (int i = 0; i <= n; i++) dp[i] = i - 1;
            
            for (int i = 1; i <= n; i++) {
                for (int j = 0; j < i; j++) {
                    if (isPalin[j][i - 1]) {
                        dp[i] = Math.min(dp[i], dp[j] + 1);
                    }
                }
            }
            
            return dp[n];
        }
        
        // Count ways to partition string into palindromes
        public static long countPartitions(String s) {
            int n = s.length();
            PalindromicTree tree = new PalindromicTree(s);
            
            boolean[][] isPalin = new boolean[n][n];
            for (PalindromicNode node : tree.getAllNodes()) {
                isPalin[node.start][node.end] = true;
            }
            
            long[] dp = new long[n + 1];
            dp[0] = 1;
            
            for (int i = 1; i <= n; i++) {
                for (int j = 0; j < i; j++) {
                    if (isPalin[j][i - 1]) {
                        dp[i] += dp[j];
                    }
                }
            }
            
            return dp[n];
        }
    }

    /**
     * USAGE EXAMPLES AND TEST CASES
     */
    public static void main(String[] args) {
        // Example 1: Basic palindromic tree
        System.out.println("=== Example 1: Basic Palindromic Tree ===");
        String s1 = "abaaba";
        PalindromicTree tree1 = new PalindromicTree(s1);
        System.out.println("String: " + s1);
        System.out.println("All palindromes: " + tree1.getAllPalindromes());
        System.out.println("Total palindromic substrings: " + tree1.countAllPalindromes());
        System.out.println("Distinct palindromes: " + tree1.countDistinctPalindromes());
        
        // Example 2: Palindrome frequencies
        System.out.println("\n=== Example 2: Palindrome Frequencies ===");
        String s2 = "ababa";
        PalindromicTree tree2 = new PalindromicTree(s2);
        Map<String, Long> frequencies = tree2.getPalindromeFrequencies();
        System.out.println("String: " + s2);
        for (Map.Entry<String, Long> entry : frequencies.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue() + " times");
        }
        
        // Example 3: LeetCode 647 - Count palindromic substrings
        System.out.println("\n=== Example 3: Count Substrings ===");
        String[] testStrings = {"abc", "aaa", "racecar"};
        for (String s : testStrings) {
            int count = PalindromeCounter.countSubstrings(s);
            System.out.println("Palindromic substrings in '" + s + "': " + count);
        }
        
        // Example 4: Palindrome queries
        System.out.println("\n=== Example 4: Palindrome Queries ===");
        String s3 = "abacabad";
        PalindromeQueries queries = new PalindromeQueries(s3);
        System.out.println("String: " + s3);
        System.out.println("Is substring [0,2] palindrome: " + queries.isPalindrome(0, 2)); // "aba"
        System.out.println("Palindromes starting at 0: " + queries.countPalindromesStartingAt(0));
        System.out.println("Palindromes containing index 2: " + queries.getPalindromesContaining(2));
        
        // Example 5: Longest palindrome
        System.out.println("\n=== Example 5: Longest Palindrome ===");
        String s4 = "babad";
        String longest = PalindromeCounter.longestPalindrome(s4);
        System.out.println("Longest palindrome in '" + s4 + "': " + longest);
        
        // Example 6: Common palindromes
        System.out.println("\n=== Example 6: Common Palindromes ===");
        List<String> strings = Arrays.asList("ababa", "babab", "abcba");
        Set<String> common = PalindromeVariations.findCommonPalindromes(strings);
        System.out.println("Strings: " + strings);
        System.out.println("Common palindromes: " + common);
        
        // Example 7: Minimum cuts
        System.out.println("\n=== Example 7: Minimum Cuts ===");
        String s5 = "aabaa";
        int minCuts = PalindromeDPOptimizations.minCuts(s5);
        System.out.println("Min cuts for '" + s5 + "': " + minCuts);
        
        // Example 8: Case insensitive
        System.out.println("\n=== Example 8: Case Insensitive ===");
        String s6 = "RaceCar";
        long count = PalindromeVariations.countAlphanumericPalindromes(s6);
        System.out.println("Alphanumeric palindromes in '" + s6 + "': " + count);
    }
}