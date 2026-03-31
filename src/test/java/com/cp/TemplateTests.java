package com.cp;

import com.cp.algorithms.BFSDFSTemplates;
import com.cp.algorithms.GraphAlgorithms;
import com.cp.algorithms.SearchTemplates;
import com.cp.algorithms.StringAlgorithms;
import com.cp.datastructures.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Data Structures & Algorithm Template Tests")
class TemplateTests {

    // =========================================================
    // TreeNode
    // =========================================================
    @Nested @DisplayName("TreeNode")
    class TreeNodeTests {
        @Test void buildFromArray() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, null, 5});
            assertEquals(1, root.val);
            assertEquals(2, root.left.val);
            assertEquals(3, root.right.val);
            assertNull(root.left.left);
            assertEquals(5, root.left.right.val);
        }
        @Test void serialize() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
            assertEquals("[1,2,3]", TreeNode.serialize(root));
        }
        @Test void nullTree() {
            assertNull(TreeNode.fromArray(new Integer[]{}));
            assertEquals("[]", TreeNode.serialize(null));
        }
        @Test void isSameTree() {
            TreeNode a = TreeNode.fromArray(new Integer[]{1,2,3});
            TreeNode b = TreeNode.fromArray(new Integer[]{1,2,3});
            TreeNode c = TreeNode.fromArray(new Integer[]{1,2,4});
            assertTrue(TreeNode.isSameTree(a, b));
            assertFalse(TreeNode.isSameTree(a, c));
        }
    }

    // =========================================================
    // ListNode
    // =========================================================
    @Nested @DisplayName("ListNode")
    class ListNodeTests {
        @Test void buildAndSerialize() {
            ListNode head = ListNode.fromArray(new int[]{1,2,3,4,5});
            assertEquals("[1,2,3,4,5]", ListNode.serialize(head));
        }
        @Test void toArray() {
            ListNode head = ListNode.fromArray(new int[]{10, 20, 30});
            assertArrayEquals(new int[]{10,20,30}, ListNode.toArray(head));
        }
        @Test void emptyList() {
            assertNull(ListNode.fromArray(new int[]{}));
        }
    }

    // =========================================================
    // DSU
    // =========================================================
    @Nested @DisplayName("DSU (Union-Find)")
    class DSUTests {
        @Test void basicUnion() {
            DSU dsu = new DSU(5);
            assertEquals(5, dsu.componentCount());
            assertTrue(dsu.union(0, 1));
            assertFalse(dsu.union(0, 1)); // already connected
            assertTrue(dsu.connected(0, 1));
            assertFalse(dsu.connected(0, 2));
            assertEquals(4, dsu.componentCount());
        }
        @Test void chainUnion() {
            DSU dsu = new DSU(4);
            dsu.union(0,1); dsu.union(1,2); dsu.union(2,3);
            assertTrue(dsu.connected(0,3));
            assertEquals(1, dsu.componentCount());
        }
        @Test void weightedDSU() {
            DSU.WeightedDSU w = new DSU.WeightedDSU(4);
            w.union(0,1); w.union(1,2);
            assertEquals(3, w.size(0));
            assertEquals(1, w.size(3));
        }
    }

    // =========================================================
    // Trie
    // =========================================================
    @Nested @DisplayName("Trie")
    class TrieTests {
        @Test void insertSearchPrefix() {
            TrieNode.Trie trie = new TrieNode.Trie();
            trie.insert("apple");
            assertTrue(trie.search("apple"));
            assertFalse(trie.search("app"));
            assertTrue(trie.startsWith("app"));
            trie.insert("app");
            assertTrue(trie.search("app"));
        }
        @Test void delete() {
            TrieNode.Trie trie = new TrieNode.Trie();
            trie.insert("hello");
            trie.insert("hell");
            assertTrue(trie.delete("hello"));
            assertFalse(trie.search("hello"));
            assertTrue(trie.search("hell"));
        }
    }

    // =========================================================
    // SegmentTree & BIT
    // =========================================================
    @Nested @DisplayName("SegmentTree & BinaryIndexedTree")
    class SegTreeTests {
        @Test void segTreeRangeQuery() {
            SegmentTree st = new SegmentTree(new int[]{1, 3, 5, 7, 9, 11});
            assertEquals(9,  st.query(0, 2));   // 1+3+5
            assertEquals(36, st.query(0, 5));   // total
            st.update(1, 10);                    // change index 1 to 10
            assertEquals(16, st.query(0, 2));   // 1+10+5
        }
        @Test void bitPrefixSum() {
            SegmentTree.BinaryIndexedTree bit = new SegmentTree.BinaryIndexedTree(new int[]{1,2,3,4,5});
            assertEquals(6,  bit.query(1, 3));  // 1+2+3
            assertEquals(15, bit.query(1, 5));  // total
            bit.update(2, 5);                   // add 5 at position 2 (now 2+5=7)
            assertEquals(11, bit.query(1, 3)); // 1+7+3
        }
    }

    // =========================================================
    // BFS / DFS
    // =========================================================
    @Nested @DisplayName("BFS / DFS Algorithms")
    class BFSDFSTests {
        private List<List<Integer>> buildAdj(int n, int[][] edges) {
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
            for (int[] e : edges) { adj.get(e[0]).add(e[1]); adj.get(e[1]).add(e[0]); }
            return adj;
        }

        @Test void graphBFSDistance() {
            List<List<Integer>> adj = buildAdj(5,
                    new int[][]{{0,1},{1,2},{2,3},{3,4}});
            int[] dist = BFSDFSTemplates.graphBFS(adj, 0, 5);
            assertArrayEquals(new int[]{0,1,2,3,4}, dist);
        }

        @Test void gridBFS() {
            int[][] grid = {
                    {0,0,0},
                    {1,1,0},
                    {0,0,0}
            };
            assertEquals(4, BFSDFSTemplates.gridBFS(grid, 0, 0, 2, 2));
        }

        @Test void levelOrder() {
            TreeNode root = TreeNode.fromArray(new Integer[]{3,9,20,null,null,15,7});
            List<List<Integer>> levels = BFSDFSTemplates.levelOrder(root);
            assertEquals(List.of(3),     levels.get(0));
            assertEquals(List.of(9,20),  levels.get(1));
            assertEquals(List.of(15,7),  levels.get(2));
        }

        @Test void inOrder() {
            TreeNode root = TreeNode.fromArray(new Integer[]{4,2,6,1,3,5,7});
            assertEquals(List.of(1,2,3,4,5,6,7), BFSDFSTemplates.inOrder(root));
        }

        @Test void hasCycleDirected() {
            // 0->1->2->0 cycle
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < 3; i++) adj.add(new ArrayList<>());
            adj.get(0).add(1); adj.get(1).add(2); adj.get(2).add(0);
            assertTrue(BFSDFSTemplates.hasCycleDirected(3, adj));

            // DAG: 0->1->2
            List<List<Integer>> dag = new ArrayList<>();
            for (int i = 0; i < 3; i++) dag.add(new ArrayList<>());
            dag.get(0).add(1); dag.get(1).add(2);
            assertFalse(BFSDFSTemplates.hasCycleDirected(3, dag));
        }
    }

    // =========================================================
    // Graph Algorithms
    // =========================================================
    @Nested @DisplayName("Graph Algorithms")
    class GraphAlgoTests {
        @SuppressWarnings("unchecked")
        private List<int[]>[] buildWeightedAdj(int n, int[][] edges) {
            List<int[]>[] adj = new ArrayList[n];
            for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
            for (int[] e : edges) {
                adj[e[0]].add(new int[]{e[1], e[2]});
                adj[e[1]].add(new int[]{e[0], e[2]});
            }
            return adj;
        }

        @Test void dijkstra() {
            List<int[]>[] adj = buildWeightedAdj(5,
                    new int[][]{{0,1,4},{0,2,1},{2,1,2},{1,3,1},{2,3,5},{3,4,3}});
            int[] dist = GraphAlgorithms.dijkstra(adj, 0, 5);
            assertArrayEquals(new int[]{0, 3, 1, 4, 7}, dist);
        }

        @Test void topoSortKahn() {
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < 4; i++) adj.add(new ArrayList<>());
            // 0->1, 0->2, 1->3, 2->3
            adj.get(0).add(1); adj.get(0).add(2);
            adj.get(1).add(3); adj.get(2).add(3);
            List<Integer> order = GraphAlgorithms.topoSortKahn(4, adj);
            assertEquals(4, order.size());
            assertTrue(order.indexOf(0) < order.indexOf(1));
            assertTrue(order.indexOf(0) < order.indexOf(2));
            assertTrue(order.indexOf(1) < order.indexOf(3));
        }

        @Test void topoSortWithCycle() {
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < 3; i++) adj.add(new ArrayList<>());
            adj.get(0).add(1); adj.get(1).add(2); adj.get(2).add(0); // cycle
            assertTrue(GraphAlgorithms.topoSortKahn(3, adj).isEmpty());
        }
    }

    // =========================================================
    // String Algorithms
    // =========================================================
    @Nested @DisplayName("String Algorithms")
    class StringAlgoTests {
        @Test void kmpSearch() {
            assertEquals(List.of(0, 3), StringAlgorithms.KMP.search("aabaab", "aab"));
            assertEquals(List.of(0),    StringAlgorithms.KMP.search("hello", "hello"));
            assertEquals(List.of(),     StringAlgorithms.KMP.search("abc", "xyz"));
        }
        @Test void zSearch() {
            assertEquals(List.of(0, 3), StringAlgorithms.zSearch("aabaab", "aab"));
        }
        @Test void manacher() {
            assertEquals("bab",    StringAlgorithms.longestPalindrome("babad"));
            assertEquals("bb",     StringAlgorithms.longestPalindrome("cbbd"));
            assertEquals("racecar",StringAlgorithms.longestPalindrome("racecar"));
        }
    }

    // =========================================================
    // Search Templates
    // =========================================================
    @Nested @DisplayName("Search Templates")
    class SearchTemplateTests {
        @Test void binarySearch() {
            int[] arr = {1, 3, 5, 7, 9, 11, 13};
            assertEquals(3,  SearchTemplates.binarySearch(arr, 7));
            assertEquals(-1, SearchTemplates.binarySearch(arr, 4));
        }
        @Test void lowerUpperBound() {
            int[] arr = {1, 2, 2, 2, 3, 4};
            assertEquals(1, SearchTemplates.lowerBound(arr, 2)); // first 2
            assertEquals(4, SearchTemplates.upperBound(arr, 2)); // after last 2
        }
        @Test void slidingWindowFixed() {
            assertEquals(9, SearchTemplates.maxSumFixed(new int[]{2,1,5,1,3,2}, 3));
        }
        @Test void slidingWindowVariable() {
            assertEquals(2, SearchTemplates.minSubarrayLen(new int[]{2,3,1,2,4,3}, 7));
        }
        @Test void prefixSum2D() {
            int[][] m = {{3,0,1,4},{5,6,3,2},{1,2,0,1},{4,1,0,1}};
            SearchTemplates.PrefixSum2D ps = new SearchTemplates.PrefixSum2D(m);
            assertEquals(8,  ps.query(2,1,4-1,4-1));  // bottom-right 2x2: 2+0+1+0... let's verify
            assertEquals(12, ps.query(1,1,2,2));       // rows 1-2, cols 1-2
        }
    }
}
