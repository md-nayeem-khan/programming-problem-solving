package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Aho-Corasick Automaton - Multi-Pattern String Matching
 * MEDIUM PRIORITY for Meta, Amazon, Google
 * 
 * Essential for:
 * - Find all occurrences of multiple patterns in text simultaneously
 * - Dictionary word search in text
 * - DNA sequence matching
 * - Content filtering and virus scanning
 * - Keyword spotting
 * 
 * Interview Questions:
 * - LeetCode 1032: Stream of Characters
 * - LeetCode 820: Short Encoding of Words
 * - Multi-pattern matching in documents
 * - Dictionary-based text processing
 * 
 * Key Concepts:
 * - Trie: Store all patterns
 * - Failure links: Fall back when mismatch
 * - Output links: Find all pattern matches at current position
 * - Time: O(n + m + z) where n=text, m=sum of pattern lengths, z=matches
 * 
 * Companies: Meta (4/5), Amazon (4/5), Google (3/5)
 */
public class AhoCorasickTemplates {
    
    // ========== AHO-CORASICK AUTOMATON ==========
    
    /**
     * Aho-Corasick Automaton for multi-pattern matching
     */
    @Complexity(time = "Build O(m), Search O(n+z)", space = "O(m)")
    static class AhoCorasick {
        static class Node {
            Map<Character, Node> children = new HashMap<>();
            Node failureLink = null;
            Node outputLink = null;
            List<Integer> output = new ArrayList<>(); // Pattern IDs ending here
            int depth = 0;
            
            public Node getChild(char c) {
                return children.get(c);
            }
            
            public void addChild(char c, Node child) {
                children.put(c, child);
            }
            
            public boolean hasChild(char c) {
                return children.containsKey(c);
            }
        }
        
        private Node root;
        private List<String> patterns;
        
        public AhoCorasick(List<String> patterns) {
            this.patterns = patterns;
            this.root = new Node();
            buildTrie();
            buildFailureLinks();
        }
        
        /**
         * Step 1: Build trie from all patterns
         */
        private void buildTrie() {
            for (int i = 0; i < patterns.size(); i++) {
                String pattern = patterns.get(i);
                Node current = root;
                
                for (char c : pattern.toCharArray()) {
                    if (!current.hasChild(c)) {
                        Node child = new Node();
                        child.depth = current.depth + 1;
                        current.addChild(c, child);
                    }
                    current = current.getChild(c);
                }
                
                current.output.add(i); // Mark end of pattern i
            }
        }
        
        /**
         * Step 2: Build failure links using BFS
         * 
         * Failure link: Points to longest proper suffix that is also prefix of some pattern
         */
        private void buildFailureLinks() {
            Queue<Node> queue = new LinkedList<>();
            
            // Level 1: failure links point to root
            for (Node child : root.children.values()) {
                child.failureLink = root;
                queue.offer(child);
            }
            
            // BFS to build failure links
            while (!queue.isEmpty()) {
                Node current = queue.poll();
                
                for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                    char c = entry.getKey();
                    Node child = entry.getValue();
                    
                    // Find failure link
                    Node fail = current.failureLink;
                    while (fail != null && !fail.hasChild(c)) {
                        fail = fail.failureLink;
                    }
                    
                    if (fail == null) {
                        child.failureLink = root;
                    } else {
                        child.failureLink = fail.getChild(c);
                    }
                    
                    // Build output link (for finding all matches)
                    if (!child.failureLink.output.isEmpty()) {
                        child.outputLink = child.failureLink;
                    } else {
                        child.outputLink = child.failureLink.outputLink;
                    }
                    
                    queue.offer(child);
                }
            }
        }
        
        /**
         * Search for all pattern occurrences in text
         * 
         * @param text haystack
         * @return map of pattern ID to list of starting positions
         */
        public Map<Integer, List<Integer>> search(String text) {
            Map<Integer, List<Integer>> result = new HashMap<>();
            for (int i = 0; i < patterns.size(); i++) {
                result.put(i, new ArrayList<>());
            }
            
            Node current = root;
            
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                
                // Follow failure links until we find a match or reach root
                while (current != root && !current.hasChild(c)) {
                    current = current.failureLink;
                }
                
                // Move to next state if possible
                if (current.hasChild(c)) {
                    current = current.getChild(c);
                }
                // If no transition possible, stay at current state (which is root after the while loop)
                
                // Report all patterns ending at position i
                reportMatches(current, i, result);
            }
            
            return result;
        }
        
        /**
         * Report all pattern matches at current node
         */
        private void reportMatches(Node node, int position, Map<Integer, List<Integer>> result) {
            // Use a set to avoid duplicate pattern IDs at the same position
            Set<Integer> reported = new HashSet<>();
            
            // Traverse the node and all its suffix links (failure + output)
            Node temp = node;
            while (temp != null && temp != root) {
                // Check current node's outputs
                for (int patternId : temp.output) {
                    if (!reported.contains(patternId)) {
                        int startPos = position - patterns.get(patternId).length() + 1;
                        result.get(patternId).add(startPos);
                        reported.add(patternId);
                    }
                }
                // Move to failure link (which captures all proper suffixes)
                temp = temp.failureLink;
            }
        }
        
        /**
         * Count total number of pattern occurrences in text
         */
        public int countMatches(String text) {
            Map<Integer, List<Integer>> matches = search(text);
            int count = 0;
            for (List<Integer> positions : matches.values()) {
                count += positions.size();
            }
            return count;
        }
        
        /**
         * Check if text contains any of the patterns
         */
        public boolean containsAny(String text) {
            Node current = root;
            
            for (char c : text.toCharArray()) {
                while (current != root && !current.hasChild(c)) {
                    current = current.failureLink;
                }
                
                if (current.hasChild(c)) {
                    current = current.getChild(c);
                    
                    if (!current.output.isEmpty() || current.outputLink != null) {
                        return true;
                    }
                }
            }
            
            return false;
        }
    }
    
    // ========== LEETCODE 1032: STREAM OF CHARACTERS ==========
    
    /**
     * LeetCode 1032: Stream of Characters
     * Check if any suffix of query stream matches dictionary words
     */
    @Complexity(time = "Build O(m), Query O(1) amortized", space = "O(m)")
    static class StreamChecker {
        static class TrieNode {
            Map<Character, TrieNode> children = new HashMap<>();
            boolean isWord = false;
        }
        
        private TrieNode root;
        private StringBuilder stream;
        private int maxLen;
        
        public StreamChecker(String[] words) {
            root = new TrieNode();
            stream = new StringBuilder();
            maxLen = 0;
            
            // Build trie with reversed words
            for (String word : words) {
                maxLen = Math.max(maxLen, word.length());
                TrieNode node = root;
                
                for (int i = word.length() - 1; i >= 0; i--) {
                    char c = word.charAt(i);
                    node.children.putIfAbsent(c, new TrieNode());
                    node = node.children.get(c);
                }
                
                node.isWord = true;
            }
        }
        
        public boolean query(char letter) {
            stream.append(letter);
            
            // Keep only last maxLen characters
            if (stream.length() > maxLen) {
                stream.deleteCharAt(0);
            }
            
            // Check if any suffix of the stream matches a word
            // We traverse from the end of stream backwards through trie (which has reversed words)
            TrieNode node = root;
            for (int i = stream.length() - 1; i >= 0; i--) {
                char c = stream.charAt(i);
                
                if (!node.children.containsKey(c)) {
                    return false;
                }
                
                node = node.children.get(c);
                
                if (node.isWord) {
                    return true;
                }
            }
            
            return false;
        }
    }
    
    // ========== DICTIONARY WORD REPLACEMENT ==========
    
    /**
     * Replace dictionary words in text with alternatives
     * Multiple patterns can match - use shortest/longest/first
     */
    @Complexity(time = "O(n + m + z)", space = "O(m)")
    public static String replaceWords(List<String> dictionary, String text, 
                                      Map<String, String> replacements) {
        AhoCorasick ac = new AhoCorasick(dictionary);
        Map<Integer, List<Integer>> matches = ac.search(text);
        
        // Collect all match positions
        Set<Integer> matchedPositions = new TreeSet<>();
        Map<Integer, String> positionToWord = new HashMap<>();
        
        for (int i = 0; i < dictionary.size(); i++) {
            String word = dictionary.get(i);
            for (int pos : matches.get(i)) {
                matchedPositions.add(pos);
                positionToWord.put(pos, word);
            }
        }
        
        // Build result with replacements
        StringBuilder result = new StringBuilder();
        int i = 0;
        
        for (int pos : matchedPositions) {
            if (pos >= i) {
                result.append(text, i, pos);
                String word = positionToWord.get(pos);
                result.append(replacements.getOrDefault(word, word));
                i = pos + word.length();
            }
        }
        
        result.append(text.substring(i));
        return result.toString();
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Aho-Corasick Automaton Examples ===\n");
        
        // Example 1: Basic multi-pattern matching
        System.out.println("1. Multi-Pattern Matching:");
        List<String> patterns = Arrays.asList("he", "she", "his", "hers");
        String text = "ahishers";
        
        AhoCorasick ac = new AhoCorasick(patterns);
        Map<Integer, List<Integer>> matches = ac.search(text);
        
        System.out.println("   Text: \"" + text + "\"");
        System.out.println("   Patterns: " + patterns);
        System.out.println("   Matches:");
        
        for (int i = 0; i < patterns.size(); i++) {
            if (!matches.get(i).isEmpty()) {
                System.out.println("     \"" + patterns.get(i) + "\" at positions: " + matches.get(i));
            }
        }
        
        System.out.println("   Total matches: " + ac.countMatches(text));
        
        // Example 2: DNA sequence matching
        System.out.println("\n2. DNA Sequence Matching:");
        List<String> dnaPatterns = Arrays.asList("ATG", "TGC", "GCA", "CAT");
        String dnaText = "ATGCATGCAT";
        
        AhoCorasick dnaAc = new AhoCorasick(dnaPatterns);
        Map<Integer, List<Integer>> dnaMatches = dnaAc.search(dnaText);
        
        System.out.println("   Sequence: \"" + dnaText + "\"");
        System.out.println("   Looking for: " + dnaPatterns);
        
        for (int i = 0; i < dnaPatterns.size(); i++) {
            if (!dnaMatches.get(i).isEmpty()) {
                System.out.println("     \"" + dnaPatterns.get(i) + "\" found at: " + dnaMatches.get(i));
            }
        }
        
        // Example 3: Word filtering
        System.out.println("\n3. Content Filtering:");
        List<String> forbiddenWords = Arrays.asList("bad", "evil", "wrong");
        String content = "This is a bad example with evil intent";
        
        AhoCorasick filterAc = new AhoCorasick(forbiddenWords);
        boolean hasForbidden = filterAc.containsAny(content);
        
        System.out.println("   Content: \"" + content + "\"");
        System.out.println("   Forbidden words: " + forbiddenWords);
        System.out.println("   Contains forbidden words: " + hasForbidden);
        
        // Example 4: Stream checker
        System.out.println("\n4. Stream of Characters (LeetCode 1032):");
        StreamChecker sc = new StreamChecker(new String[]{"cd", "f", "kl"});
        
        String stream = "abcdefghijkl";
        System.out.println("   Dictionary: [\"cd\", \"f\", \"kl\"]");
        System.out.println("   Stream: \"" + stream + "\"");
        System.out.print("   Queries: ");
        
        for (char c : stream.toCharArray()) {
            System.out.print(sc.query(c) ? "T" : "F");
        }
        System.out.println();
        
        // Example 5: Performance comparison
        System.out.println("\n5. Performance Comparison:");
        System.out.println("   Naive approach: O(n × k × m) where k=patterns, m=avg pattern length");
        System.out.println("   Aho-Corasick: O(n + m + z) where m=sum of pattern lengths, z=matches");
        System.out.println("   ");
        System.out.println("   Example with 1000 patterns in 1,000,000 character text:");
        System.out.println("   Naive: ~1,000,000 × 1,000 × 10 = 10^10 operations");
        System.out.println("   Aho-Corasick: ~1,000,000 + 10,000 + matches ≈ 10^6 operations");
        System.out.println("   Speedup: ~10,000× faster!");
        
        // Example 6: Overlapping patterns
        System.out.println("\n6. Overlapping Pattern Detection:");
        List<String> overlapPatterns = Arrays.asList("aa", "aaa", "aaaa");
        String overlapText = "aaaaa";
        
        AhoCorasick overlapAc = new AhoCorasick(overlapPatterns);
        Map<Integer, List<Integer>> overlapMatches = overlapAc.search(overlapText);
        
        System.out.println("   Text: \"" + overlapText + "\"");
        System.out.println("   Patterns: " + overlapPatterns);
        
        for (int i = 0; i < overlapPatterns.size(); i++) {
            if (!overlapMatches.get(i).isEmpty()) {
                System.out.println("     \"" + overlapPatterns.get(i) + "\" at: " + overlapMatches.get(i));
            }
        }
        
        System.out.println("\n=== Key Points ===");
        System.out.println("Aho-Corasick: Multi-pattern matching in linear time");
        System.out.println("Trie: Stores all patterns efficiently");
        System.out.println("Failure Links: Handle mismatches by falling back");
        System.out.println("Output Links: Find all pattern matches at once");
        System.out.println("Applications: Content filtering, DNA analysis, keyword search");
    }
}
