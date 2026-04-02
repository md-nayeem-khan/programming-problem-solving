package com.cp;

import com.cp.algorithms.*;
import com.cp.datastructures.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Data Structures & Algorithm Template Tests")
class TemplateTests {

    // =========================================================
    // Backtracking Tests (NEW!)
    // =========================================================
    @Nested @DisplayName("Backtracking Algorithms")
    class BacktrackingTests {
        @Test void permutations() {
            List<List<Integer>> result = BacktrackingTemplates.permute(new int[]{1, 2, 3});
            assertEquals(6, result.size()); // 3! = 6
            assertTrue(result.contains(List.of(1, 2, 3)));
            assertTrue(result.contains(List.of(3, 2, 1)));
        }

        @Test void combinations() {
            List<List<Integer>> result = BacktrackingTemplates.combine(4, 2);
            assertEquals(6, result.size()); // C(4,2) = 6
            assertTrue(result.contains(List.of(1, 2)));
            assertTrue(result.contains(List.of(3, 4)));
        }

        @Test void subsets() {
            List<List<Integer>> result = BacktrackingTemplates.subsets(new int[]{1, 2, 3});
            assertEquals(8, result.size()); // 2^3 = 8
            assertTrue(result.contains(List.of()));
            assertTrue(result.contains(List.of(1, 2, 3)));
        }

        @Test void nQueens() {
            List<List<String>> result = BacktrackingTemplates.solveNQueens(4);
            assertEquals(2, result.size()); // 4-Queens has 2 solutions
        }

        @Test void generateParenthesis() {
            List<String> result = BacktrackingTemplates.generateParenthesis(3);
            assertEquals(5, result.size()); // Catalan(3) = 5
            assertTrue(result.contains("((()))"));
            assertTrue(result.contains("()()()"));
        }

        @Test void combinationSum() {
            List<List<Integer>> result = BacktrackingTemplates.combinationSum(new int[]{2, 3, 6, 7}, 7);
            assertTrue(result.contains(List.of(2, 2, 3)));
            assertTrue(result.contains(List.of(7)));
        }

        @Test void wordSearch() {
            char[][] board = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
            };
            assertTrue(BacktrackingTemplates.existWordInGrid(board, "ABCCED"));
            assertFalse(BacktrackingTemplates.existWordInGrid(board, "ABCB"));
        }
    }

    // =========================================================
    // Advanced Data Structures Tests (NEW!)
    // =========================================================
    @Nested @DisplayName("Advanced Data Structures")
    class AdvancedDSTests {
        @Test void nextGreaterElement() {
            int[] result = AdvancedDataStructures.nextGreaterElement(new int[]{2, 1, 2, 4, 3});
            assertArrayEquals(new int[]{4, 2, 4, -1, -1}, result);
        }

        @Test void slidingWindowMaximum() {
            int[] result = AdvancedDataStructures.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3);
            assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, result);
        }

        @Test void minStack() {
            AdvancedDataStructures.MinStack stack = new AdvancedDataStructures.MinStack();
            stack.push(-2);
            stack.push(0);
            stack.push(-3);
            assertEquals(-3, stack.getMin());
            stack.pop();
            assertEquals(0, stack.top());
            assertEquals(-2, stack.getMin());
        }

        @Test void lruCache() {
            AdvancedDataStructures.LRUCache cache = new AdvancedDataStructures.LRUCache(2);
            cache.put(1, 1);
            cache.put(2, 2);
            assertEquals(1, cache.get(1));
            cache.put(3, 3); // evicts key 2
            assertEquals(-1, cache.get(2));
            assertEquals(3, cache.get(3));
        }

        @Test void mergeIntervals() {
            int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
            int[][] result = AdvancedDataStructures.mergeIntervals(intervals);
            assertEquals(3, result.length);
            assertArrayEquals(new int[]{1, 6}, result[0]);
            assertArrayEquals(new int[]{8, 10}, result[1]);
        }

        @Test void insertInterval() {
            int[][] intervals = {{1, 3}, {6, 9}};
            int[] newInterval = {2, 5};
            int[][] result = AdvancedDataStructures.insertInterval(intervals, newInterval);
            assertEquals(2, result.length);
            assertArrayEquals(new int[]{1, 5}, result[0]);
        }

        @Test void trappingRainWater() {
            int water = AdvancedDataStructures.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1});
            assertEquals(6, water);
        }

        @Test void largestRectangle() {
            int area = AdvancedDataStructures.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3});
            assertEquals(10, area); // rectangle of height 5, width 2
        }

        @Test void dailyTemperatures() {
            int[] result = AdvancedDataStructures.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73});
            assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, result);
        }
    }

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
        
        // Basic Binary Search Tests
        @Test void binarySearch() {
            int[] arr = {1, 3, 5, 7, 9, 11, 13};
            assertEquals(3,  SearchTemplates.binarySearch(arr, 7));
            assertEquals(-1, SearchTemplates.binarySearch(arr, 4));
        }
        
        @Test void binarySearchEdgeCases() {
            // Empty array
            assertEquals(-1, SearchTemplates.binarySearch(new int[]{}, 5));
            
            // Single element
            assertEquals(0, SearchTemplates.binarySearch(new int[]{5}, 5));
            assertEquals(-1, SearchTemplates.binarySearch(new int[]{5}, 3));
            
            // First and last elements
            int[] arr = {1, 3, 5, 7, 9};
            assertEquals(0, SearchTemplates.binarySearch(arr, 1));
            assertEquals(4, SearchTemplates.binarySearch(arr, 9));
        }
        
        // Lower/Upper Bound Tests
        @Test void lowerUpperBound() {
            int[] arr = {1, 2, 2, 2, 3, 4};
            assertEquals(1, SearchTemplates.lowerBound(arr, 2)); // first 2
            assertEquals(4, SearchTemplates.upperBound(arr, 2)); // after last 2
        }
        
        @Test void lowerUpperBoundEdgeCases() {
            int[] arr = {1, 2, 2, 2, 3, 4};
            
            // Target smaller than all
            assertEquals(0, SearchTemplates.lowerBound(arr, 0));
            assertEquals(0, SearchTemplates.upperBound(arr, 0));
            
            // Target larger than all
            assertEquals(6, SearchTemplates.lowerBound(arr, 10));
            assertEquals(6, SearchTemplates.upperBound(arr, 10));
            
            // Empty array
            assertEquals(0, SearchTemplates.lowerBound(new int[]{}, 5));
            assertEquals(0, SearchTemplates.upperBound(new int[]{}, 5));
        }
        
        // Binary Search on Answer
        @Test void binarySearchOnAnswer() {
            // Find minimum x where x^2 >= 16 (answer should be 4)
            java.util.function.IntPredicate isSquareAtLeast16 = x -> x * x >= 16;
            assertEquals(4, SearchTemplates.binarySearchOnAnswer(0, 10, isSquareAtLeast16));
            
            // Find minimum days to make m bouquets (classic problem pattern)
            java.util.function.IntPredicate canMakeBouquets = days -> days >= 3; // simplified
            assertEquals(3, SearchTemplates.binarySearchOnAnswer(1, 10, canMakeBouquets));
        }
        
        // Two Pointers Tests
        @Test void twoSum() {
            int[] arr = {1, 2, 3, 4, 6};
            assertArrayEquals(new int[]{1, 3}, SearchTemplates.twoSum(arr, 6)); // 2 + 4 = 6
            assertArrayEquals(new int[]{-1, -1}, SearchTemplates.twoSum(arr, 12)); // not found
        }
        
        @Test void twoSumEdgeCases() {
            // Empty array
            assertArrayEquals(new int[]{-1, -1}, SearchTemplates.twoSum(new int[]{}, 5));
            
            // Single element
            assertArrayEquals(new int[]{-1, -1}, SearchTemplates.twoSum(new int[]{5}, 5));
            
            // Two elements
            assertArrayEquals(new int[]{0, 1}, SearchTemplates.twoSum(new int[]{2, 3}, 5));
            assertArrayEquals(new int[]{-1, -1}, SearchTemplates.twoSum(new int[]{2, 3}, 7));
        }
        
        // Sliding Window Tests
        @Test void slidingWindowFixed() {
            assertEquals(9, SearchTemplates.maxSumFixed(new int[]{2,1,5,1,3,2}, 3));
        }
        
        @Test void slidingWindowFixedEdgeCases() {
            // Window size equals array length
            assertEquals(6, SearchTemplates.maxSumFixed(new int[]{1,2,3}, 3));
            
            // Single element window
            assertEquals(5, SearchTemplates.maxSumFixed(new int[]{2,1,5,1,3}, 1));
            
            // Negative numbers
            assertEquals(-3, SearchTemplates.maxSumFixed(new int[]{-3,-1,-2,-4}, 2));
        }
        
        @Test void slidingWindowVariable() {
            assertEquals(2, SearchTemplates.minSubarrayLen(new int[]{2,3,1,2,4,3}, 7));
        }
        
        @Test void slidingWindowVariableEdgeCases() {
            // No valid subarray
            assertEquals(0, SearchTemplates.minSubarrayLen(new int[]{1,1,1,1}, 10));
            
            // Single element satisfies
            assertEquals(1, SearchTemplates.minSubarrayLen(new int[]{1,5,3}, 5));
            
            // Entire array needed
            assertEquals(4, SearchTemplates.minSubarrayLen(new int[]{1,1,1,1}, 4));
        }
        
        // Prefix Sum Tests
        @Test void prefixSum1D() {
            SearchTemplates.PrefixSum ps = new SearchTemplates.PrefixSum(new int[]{1,2,3,4,5});
            assertEquals(5, ps.rangeSum(1, 2)); // 2 + 3 = 5
            assertEquals(15, ps.rangeSum(0, 4)); // entire array
            assertEquals(1, ps.rangeSum(0, 0)); // single element
            assertEquals(5, ps.rangeSum(4, 4)); // last element
        }
        
        @Test void prefixSum2D() {
            int[][] m = {{3,0,1,4},{5,6,3,2},{1,2,0,1},{4,1,0,1}};
            SearchTemplates.PrefixSum2D ps = new SearchTemplates.PrefixSum2D(m);
            // Query(2,1,3,3): rows 2-3, cols 1-3
            // Row 2: [1, 2, 0, 1] -> cols 1-3 = 2+0+1 = 3
            // Row 3: [4, 1, 0, 1] -> cols 1-3 = 1+0+1 = 2
            // Total = 5
            assertEquals(5,  ps.query(2,1,3,3));
            // Query(1,1,2,2): rows 1-2, cols 1-2
            // Row 1: [5, 6, 3, 2] -> cols 1-2 = 6+3 = 9
            // Row 2: [1, 2, 0, 1] -> cols 1-2 = 2+0 = 2
            // Total = 11
            assertEquals(11, ps.query(1,1,2,2));
        }
        
        @Test void prefixSum2DEdgeCases() {
            int[][] m = {{1,2},{3,4}};
            SearchTemplates.PrefixSum2D ps = new SearchTemplates.PrefixSum2D(m);
            
            // Single cell
            assertEquals(1, ps.query(0,0,0,0));
            assertEquals(4, ps.query(1,1,1,1));
            
            // Entire matrix
            assertEquals(10, ps.query(0,0,1,1)); // 1+2+3+4 = 10
            
            // Single row/column
            assertEquals(3, ps.query(0,0,0,1)); // first row: 1+2
            assertEquals(6, ps.query(0,1,1,1)); // second column: 2+4
        }
        
        // Meet in the Middle Tests
        @Test void subsetSumMeetInMiddle() {
            // Basic test case
            assertTrue(SearchTemplates.subsetSumMeetInMiddle(new int[]{1,5,7,9,2,12,8,3}, 15));
            assertFalse(SearchTemplates.subsetSumMeetInMiddle(new int[]{1,2,3,4}, 15));
            
            // Target is 0 (empty subset)
            assertTrue(SearchTemplates.subsetSumMeetInMiddle(new int[]{1,2,3}, 0));
            
            // Single element cases
            assertTrue(SearchTemplates.subsetSumMeetInMiddle(new int[]{5}, 5));
            assertFalse(SearchTemplates.subsetSumMeetInMiddle(new int[]{5}, 3));
        }
        
        @Test void closestSubsequenceSum() {
            // Method returns the closest sum to target, not the difference
            int[] nums = {5, -7, 3, 5};
            int target = 6;
            // Closest possible sums to 6: 5, 3, 8, 1, -2, 6, -4, 0
            // Closest sum is 6 (exact match)
            assertEquals(6, SearchTemplates.closestSubsequenceSum(nums, target));
        }
        
        @Test void countSubsetsInRange() {
            int[] nums = {1, 2, 3};
            // Possible subset sums: 0, 1, 2, 3, 3, 4, 5, 6
            // Range [2, 4]: sums 2, 3, 3, 4 -> count = 4
            assertEquals(4, SearchTemplates.countSubsetsInRange(nums, 2, 4));
            
            // Range [0, 0]: sum 0 -> count = 1
            assertEquals(1, SearchTemplates.countSubsetsInRange(nums, 0, 0));
        }
        
        @Test void canPartitionMeetInMiddle() {
            // Can partition [1,5,11,5] into [1,5,5] and [11]? Yes, both sum to 11
            assertTrue(SearchTemplates.canPartitionMeetInMiddle(new int[]{1, 5, 11, 5}));
            
            // Can partition [1,2,3,5]? Total = 11 (odd), impossible
            assertFalse(SearchTemplates.canPartitionMeetInMiddle(new int[]{1, 2, 3, 5}));
            
            // Edge case: empty array
            assertTrue(SearchTemplates.canPartitionMeetInMiddle(new int[]{}));
        }
        
        @Test void maxXORSubsets() {
            // XOR of all possible subset pairs
            int[] nums = {1, 2, 3};
            // Left subsets: {}, {1}, {2}, {1,2} -> XORs: 0, 1, 2, 3
            // Right subsets: {}, {3} -> XORs: 0, 3  
            // Max XOR: max(0^0, 0^3, 1^0, 1^3, 2^0, 2^3, 3^0, 3^3) = max(0,3,1,2,2,1,3,0) = 3
            assertEquals(3, SearchTemplates.maxXORSubsets(nums));
        }
    }
}
