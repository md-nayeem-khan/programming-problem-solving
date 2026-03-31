package com.cp.datastructures;

/**
 * Trie (Prefix Tree) – reusable template.
 *
 * Usage:
 *   Trie trie = new Trie();
 *   trie.insert("apple");
 *   trie.search("apple");   // true
 *   trie.startsWith("app"); // true
 */
public class TrieNode {

    public TrieNode[] children;
    public boolean isEnd;

    public TrieNode() {
        children = new TrieNode[26]; // lowercase a-z
        isEnd = false;
    }

    // ---------------------------------------------------------------
    // Trie class — ready to paste into LeetCode
    // ---------------------------------------------------------------
    public static class Trie {
        private final TrieNode root = new TrieNode();

        public void insert(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (cur.children[idx] == null) cur.children[idx] = new TrieNode();
                cur = cur.children[idx];
            }
            cur.isEnd = true;
        }

        public boolean search(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (cur.children[idx] == null) return false;
                cur = cur.children[idx];
            }
            return cur.isEnd;
        }

        public boolean startsWith(String prefix) {
            TrieNode cur = root;
            for (char c : prefix.toCharArray()) {
                int idx = c - 'a';
                if (cur.children[idx] == null) return false;
                cur = cur.children[idx];
            }
            return true;
        }

        /** Delete a word from the trie */
        public boolean delete(String word) {
            return deleteHelper(root, word, 0);
        }

        private boolean deleteHelper(TrieNode node, String word, int depth) {
            if (node == null) return false;
            if (depth == word.length()) {
                if (!node.isEnd) return false;
                node.isEnd = false;
                return isEmpty(node);
            }
            int idx = word.charAt(depth) - 'a';
            if (deleteHelper(node.children[idx], word, depth + 1)) {
                node.children[idx] = null;
                return !node.isEnd && isEmpty(node);
            }
            return false;
        }

        private boolean isEmpty(TrieNode node) {
            for (TrieNode child : node.children) if (child != null) return false;
            return true;
        }
    }
}
