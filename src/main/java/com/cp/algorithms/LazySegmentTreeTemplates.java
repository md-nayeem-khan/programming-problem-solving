package com.cp.algorithms;

import com.cp.problems.Complexity;

/**
 * Segment Tree with Lazy Propagation
 * MEDIUM PRIORITY for Amazon, Meta, Google
 * 
 * Essential for:
 * - Range updates in O(log n) instead of O(n)
 * - Range sum/min/max queries with range updates
 * - Interval modification problems
 * - Efficient batch updates
 * 
 * Interview Questions:
 * - LeetCode 307: Range Sum Query - Mutable
 * - LeetCode 699: Falling Squares
 * - LeetCode 715: Range Module
 * - LeetCode 732: My Calendar III
 * - Range addition problems
 * 
 * Key Concepts:
 * - Lazy propagation: Delay updates until needed
 * - Push down: Propagate lazy values to children
 * - Range operations: update/query O(log n)
 * - Multiple operation types: add, set, min, max
 * 
 * Companies: Amazon (4/5), Meta (4/5), Google (3/5)
 */
public class LazySegmentTreeTemplates {
    
    // ========== LAZY SEGMENT TREE FOR RANGE SUM ==========
    
    /**
     * Lazy Segment Tree supporting:
     * - Range sum query
     * - Range addition update
     */
    @Complexity(time = "O(log n) per operation", space = "O(n)")
    static class LazySegmentTreeSum {
        private long[] tree;  // Segment tree
        private long[] lazy;  // Lazy propagation array
        private int n;
        
        public LazySegmentTreeSum(int size) {
            this.n = size;
            tree = new long[4 * n];
            lazy = new long[4 * n];
        }
        
        public LazySegmentTreeSum(int[] arr) {
            this(arr.length);
            build(arr, 0, 0, n - 1);
        }
        
        private void build(int[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
                return;
            }
            int mid = (start + end) / 2;
            build(arr, 2 * node + 1, start, mid);
            build(arr, 2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
        
        /**
         * Push lazy value down to children
         */
        private void pushDown(int node, int start, int end) {
            if (lazy[node] != 0) {
                tree[node] += (end - start + 1) * lazy[node];
                
                if (start != end) {
                    lazy[2 * node + 1] += lazy[node];
                    lazy[2 * node + 2] += lazy[node];
                }
                
                lazy[node] = 0;
            }
        }
        
        /**
         * Range update: add delta to all elements in [l, r]
         */
        public void rangeUpdate(int l, int r, long delta) {
            rangeUpdate(0, 0, n - 1, l, r, delta);
        }
        
        private void rangeUpdate(int node, int start, int end, int l, int r, long delta) {
            pushDown(node, start, end);
            
            if (r < start || end < l) return;
            
            if (l <= start && end <= r) {
                lazy[node] += delta;
                pushDown(node, start, end);
                return;
            }
            
            int mid = (start + end) / 2;
            rangeUpdate(2 * node + 1, start, mid, l, r, delta);
            rangeUpdate(2 * node + 2, mid + 1, end, l, r, delta);
            
            pushDown(2 * node + 1, start, mid);
            pushDown(2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
        
        /**
         * Range sum query [l, r]
         */
        public long rangeQuery(int l, int r) {
            return rangeQuery(0, 0, n - 1, l, r);
        }
        
        private long rangeQuery(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return 0;
            
            pushDown(node, start, end);
            
            if (l <= start && end <= r) {
                return tree[node];
            }
            
            int mid = (start + end) / 2;
            return rangeQuery(2 * node + 1, start, mid, l, r)
                 + rangeQuery(2 * node + 2, mid + 1, end, l, r);
        }
        
        /**
         * Point update: set index idx to value
         */
        public void pointUpdate(int idx, long value) {
            long current = rangeQuery(idx, idx);
            rangeUpdate(idx, idx, value - current);
        }
    }
    
    // ========== LAZY SEGMENT TREE FOR RANGE MIN/MAX ==========
    
    /**
     * Lazy Segment Tree for range minimum with range set updates
     */
    @Complexity(time = "O(log n) per operation", space = "O(n)")
    static class LazySegmentTreeMin {
        private long[] tree;
        private long[] lazy;
        private boolean[] hasLazy;
        private int n;
        private static final long INF = Long.MAX_VALUE / 2;
        
        public LazySegmentTreeMin(int size) {
            this.n = size;
            tree = new long[4 * n];
            lazy = new long[4 * n];
            hasLazy = new boolean[4 * n];
            java.util.Arrays.fill(tree, INF);
        }
        
        public LazySegmentTreeMin(int[] arr) {
            this(arr.length);
            build(arr, 0, 0, n - 1);
        }
        
        private void build(int[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
                return;
            }
            int mid = (start + end) / 2;
            build(arr, 2 * node + 1, start, mid);
            build(arr, 2 * node + 2, mid + 1, end);
            tree[node] = Math.min(tree[2 * node + 1], tree[2 * node + 2]);
        }
        
        private void pushDown(int node, int start, int end) {
            if (hasLazy[node]) {
                tree[node] = lazy[node];
                
                if (start != end) {
                    lazy[2 * node + 1] = lazy[node];
                    lazy[2 * node + 2] = lazy[node];
                    hasLazy[2 * node + 1] = true;
                    hasLazy[2 * node + 2] = true;
                }
                
                hasLazy[node] = false;
            }
        }
        
        /**
         * Range update: set all elements in [l, r] to value
         */
        public void rangeSet(int l, int r, long value) {
            rangeSet(0, 0, n - 1, l, r, value);
        }
        
        private void rangeSet(int node, int start, int end, int l, int r, long value) {
            pushDown(node, start, end);
            
            if (r < start || end < l) return;
            
            if (l <= start && end <= r) {
                lazy[node] = value;
                hasLazy[node] = true;
                pushDown(node, start, end);
                return;
            }
            
            int mid = (start + end) / 2;
            rangeSet(2 * node + 1, start, mid, l, r, value);
            rangeSet(2 * node + 2, mid + 1, end, l, r, value);
            
            pushDown(2 * node + 1, start, mid);
            pushDown(2 * node + 2, mid + 1, end);
            tree[node] = Math.min(tree[2 * node + 1], tree[2 * node + 2]);
        }
        
        /**
         * Range minimum query [l, r]
         */
        public long rangeMin(int l, int r) {
            return rangeMin(0, 0, n - 1, l, r);
        }
        
        private long rangeMin(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return INF;
            
            pushDown(node, start, end);
            
            if (l <= start && end <= r) {
                return tree[node];
            }
            
            int mid = (start + end) / 2;
            return Math.min(rangeMin(2 * node + 1, start, mid, l, r),
                           rangeMin(2 * node + 2, mid + 1, end, l, r));
        }
    }
    
    /**
     * Lazy Segment Tree for range maximum with range set updates
     */
    @Complexity(time = "O(log n) per operation", space = "O(n)")
    static class LazySegmentTreeMax {
        private long[] tree;
        private long[] lazy;
        private boolean[] hasLazy;
        private int n;
        
        public LazySegmentTreeMax(int size) {
            this.n = size;
            tree = new long[4 * n];
            lazy = new long[4 * n];
            hasLazy = new boolean[4 * n];
        }
        
        public LazySegmentTreeMax(int[] arr) {
            this(arr.length);
            build(arr, 0, 0, n - 1);
        }
        
        private void build(int[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
                return;
            }
            int mid = (start + end) / 2;
            build(arr, 2 * node + 1, start, mid);
            build(arr, 2 * node + 2, mid + 1, end);
            tree[node] = Math.max(tree[2 * node + 1], tree[2 * node + 2]);
        }
        
        private void pushDown(int node, int start, int end) {
            if (hasLazy[node]) {
                tree[node] = lazy[node];
                
                if (start != end) {
                    lazy[2 * node + 1] = lazy[node];
                    lazy[2 * node + 2] = lazy[node];
                    hasLazy[2 * node + 1] = true;
                    hasLazy[2 * node + 2] = true;
                }
                
                hasLazy[node] = false;
            }
        }
        
        public void rangeSet(int l, int r, long value) {
            rangeSet(0, 0, n - 1, l, r, value);
        }
        
        private void rangeSet(int node, int start, int end, int l, int r, long value) {
            pushDown(node, start, end);
            
            if (r < start || end < l) return;
            
            if (l <= start && end <= r) {
                lazy[node] = value;
                hasLazy[node] = true;
                pushDown(node, start, end);
                return;
            }
            
            int mid = (start + end) / 2;
            rangeSet(2 * node + 1, start, mid, l, r, value);
            rangeSet(2 * node + 2, mid + 1, end, l, r, value);
            
            pushDown(2 * node + 1, start, mid);
            pushDown(2 * node + 2, mid + 1, end);
            tree[node] = Math.max(tree[2 * node + 1], tree[2 * node + 2]);
        }
        
        /**
         * Range maximum query [l, r]
         */
        public long rangeMax(int l, int r) {
            return rangeMax(0, 0, n - 1, l, r);
        }
        
        private long rangeMax(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return 0; // Return 0 for empty ranges
            
            pushDown(node, start, end);
            
            if (l <= start && end <= r) {
                return tree[node];
            }
            
            int mid = (start + end) / 2;
            return Math.max(rangeMax(2 * node + 1, start, mid, l, r),
                           rangeMax(2 * node + 2, mid + 1, end, l, r));
        }
    }
    
    // ========== LAZY SEGMENT TREE WITH MULTIPLE OPERATIONS ==========
    
    /**
     * Advanced Lazy Segment Tree supporting multiple operations:
     * - Range addition
     * - Range set
     * - Range min/max/sum queries
     */
    @Complexity(time = "O(log n) per operation", space = "O(n)")
    static class AdvancedLazySegmentTree {
        private long[] tree;
        private long[] lazyAdd;     // Lazy for additions
        private long[] lazySet;     // Lazy for set operations
        private boolean[] hasSet;   // Flag for set operation
        private int n;
        
        public AdvancedLazySegmentTree(int size) {
            this.n = size;
            tree = new long[4 * n];
            lazyAdd = new long[4 * n];
            lazySet = new long[4 * n];
            hasSet = new boolean[4 * n];
        }
        
        public AdvancedLazySegmentTree(int[] arr) {
            this(arr.length);
            build(arr, 0, 0, n - 1);
        }
        
        private void build(int[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
                return;
            }
            int mid = (start + end) / 2;
            build(arr, 2 * node + 1, start, mid);
            build(arr, 2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
        
        private void pushDown(int node, int start, int end) {
            // Apply ADD operations first
            if (lazyAdd[node] != 0) {
                tree[node] += (end - start + 1) * lazyAdd[node];
                
                if (start != end) {
                    lazyAdd[2 * node + 1] += lazyAdd[node];
                    lazyAdd[2 * node + 2] += lazyAdd[node];
                }
                
                lazyAdd[node] = 0;
            }
            
            // Then apply SET operations (which override ADD)
            if (hasSet[node]) {
                tree[node] = (end - start + 1) * lazySet[node];
                
                if (start != end) {
                    lazySet[2 * node + 1] = lazySet[node];
                    lazySet[2 * node + 2] = lazySet[node];
                    hasSet[2 * node + 1] = true;
                    hasSet[2 * node + 2] = true;
                    lazyAdd[2 * node + 1] = 0; // Clear ADD since SET overrides
                    lazyAdd[2 * node + 2] = 0;
                }
                
                hasSet[node] = false;
                lazySet[node] = 0;
            }
        }
        
        /**
         * Range set: set all elements in [l, r] to value
         */
        public void rangeSet(int l, int r, long value) {
            rangeSet(0, 0, n - 1, l, r, value);
        }
        
        private void rangeSet(int node, int start, int end, int l, int r, long value) {
            pushDown(node, start, end);
            
            if (r < start || end < l) return;
            
            if (l <= start && end <= r) {
                lazySet[node] = value;
                hasSet[node] = true;
                lazyAdd[node] = 0;
                pushDown(node, start, end);
                return;
            }
            
            int mid = (start + end) / 2;
            rangeSet(2 * node + 1, start, mid, l, r, value);
            rangeSet(2 * node + 2, mid + 1, end, l, r, value);
            
            pushDown(2 * node + 1, start, mid);
            pushDown(2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
        
        /**
         * Range add: add delta to all elements in [l, r]
         */
        public void rangeAdd(int l, int r, long delta) {
            rangeAdd(0, 0, n - 1, l, r, delta);
        }
        
        private void rangeAdd(int node, int start, int end, int l, int r, long delta) {
            pushDown(node, start, end);
            
            if (r < start || end < l) return;
            
            if (l <= start && end <= r) {
                lazyAdd[node] += delta;
                pushDown(node, start, end);
                return;
            }
            
            int mid = (start + end) / 2;
            rangeAdd(2 * node + 1, start, mid, l, r, delta);
            rangeAdd(2 * node + 2, mid + 1, end, l, r, delta);
            
            pushDown(2 * node + 1, start, mid);
            pushDown(2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
        
        /**
         * Range sum query [l, r]
         */
        public long rangeSum(int l, int r) {
            return rangeSum(0, 0, n - 1, l, r);
        }
        
        private long rangeSum(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return 0;
            
            pushDown(node, start, end);
            
            if (l <= start && end <= r) {
                return tree[node];
            }
            
            int mid = (start + end) / 2;
            return rangeSum(2 * node + 1, start, mid, l, r)
                 + rangeSum(2 * node + 2, mid + 1, end, l, r);
        }
    }
    
    // ========== LEETCODE PROBLEMS ==========
    
    /**
     * LeetCode 307: Range Sum Query - Mutable
     * With range updates
     */
    @Complexity(time = "O(log n) per operation", space = "O(n)")
    static class NumArray {
        private LazySegmentTreeSum tree;
        
        public NumArray(int[] nums) {
            tree = new LazySegmentTreeSum(nums);
        }
        
        public void update(int index, int val) {
            tree.pointUpdate(index, val);
        }
        
        public int sumRange(int left, int right) {
            return (int) tree.rangeQuery(left, right);
        }
    }
    
    /**
     * LeetCode 699: Falling Squares
     * Track maximum height after each square falls
     */
    @Complexity(time = "O(n log C)", space = "O(C)")
    static class FallingSquares {
        public java.util.List<Integer> fallingSquares(int[][] positions) {
            // Coordinate compression
            java.util.Set<Integer> coords = new java.util.TreeSet<>();
            for (int[] pos : positions) {
                coords.add(pos[0]);
                coords.add(pos[0] + pos[1] - 1);
            }
            
            java.util.Map<Integer, Integer> compress = new java.util.HashMap<>();
            int idx = 0;
            for (int coord : coords) {
                compress.put(coord, idx++);
            }
            
            LazySegmentTreeMax tree = new LazySegmentTreeMax(coords.size());
            java.util.List<Integer> result = new java.util.ArrayList<>();
            long maxHeight = 0;
            
            for (int[] pos : positions) {
                int left = compress.get(pos[0]);
                int right = compress.get(pos[0] + pos[1] - 1);
                
                long currentHeight = tree.rangeMax(left, right);
                
                long newHeight = currentHeight + pos[1];
                tree.rangeSet(left, right, newHeight);
                
                maxHeight = Math.max(maxHeight, newHeight);
                result.add((int) maxHeight);
            }
            
            return result;
        }
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Lazy Segment Tree Examples ===\n");
        
        // Example 1: Range Sum with Range Updates
        System.out.println("1. Range Sum with Range Addition:");
        int[] arr1 = {1, 2, 3, 4, 5};
        LazySegmentTreeSum sumTree = new LazySegmentTreeSum(arr1);
        
        System.out.println("   Initial array: [1, 2, 3, 4, 5]");
        System.out.println("   Sum [0, 4] = " + sumTree.rangeQuery(0, 4)); // 15
        
        sumTree.rangeUpdate(1, 3, 10); // Add 10 to indices 1, 2, 3
        System.out.println("   After adding 10 to [1, 3]:");
        System.out.println("   Sum [0, 4] = " + sumTree.rangeQuery(0, 4)); // 45
        System.out.println("   Sum [1, 3] = " + sumTree.rangeQuery(1, 3)); // 39
        
        // Example 2: Range Min with Range Set
        System.out.println("\n2. Range Min with Range Set:");
        int[] arr2 = {5, 3, 7, 2, 8, 1, 9};
        LazySegmentTreeMin minTree = new LazySegmentTreeMin(arr2);
        
        System.out.println("   Initial array: [5, 3, 7, 2, 8, 1, 9]");
        System.out.println("   Min [0, 6] = " + minTree.rangeMin(0, 6)); // 1
        System.out.println("   Min [2, 4] = " + minTree.rangeMin(2, 4)); // 2
        
        minTree.rangeSet(2, 4, 4); // Set indices 2, 3, 4 to 4
        System.out.println("   After setting [2, 4] to 4:");
        System.out.println("   Min [0, 6] = " + minTree.rangeMin(0, 6)); // 1
        System.out.println("   Min [2, 4] = " + minTree.rangeMin(2, 4)); // 4
        
        // Example 3: Advanced operations
        System.out.println("\n3. Advanced Lazy Segment Tree (Set + Add):");
        int[] arr3 = {1, 2, 3, 4, 5};
        AdvancedLazySegmentTree advTree = new AdvancedLazySegmentTree(arr3);
        
        System.out.println("   Initial array: [1, 2, 3, 4, 5]");
        System.out.println("   Sum [0, 4] = " + advTree.rangeSum(0, 4)); // 15
        
        advTree.rangeSet(1, 3, 10);
        System.out.println("   After setting [1, 3] to 10:");
        System.out.println("   Sum [0, 4] = " + advTree.rangeSum(0, 4)); // 35
        
        advTree.rangeAdd(0, 4, 5);
        System.out.println("   After adding 5 to [0, 4]:");
        System.out.println("   Sum [0, 4] = " + advTree.rangeSum(0, 4)); // 60
        
        // Example 4: Performance comparison
        System.out.println("\n4. Performance Comparison:");
        System.out.println("   Without lazy propagation:");
        System.out.println("     Range update: O(n log n) - update each element");
        System.out.println("     Range query: O(log n)");
        System.out.println("   With lazy propagation:");
        System.out.println("     Range update: O(log n) - delay updates");
        System.out.println("     Range query: O(log n)");
        System.out.println("   Speedup for k updates on n elements: O(n) → O(log n)");
        
        System.out.println("\n=== Key Points ===");
        System.out.println("Lazy Propagation: Delay updates until needed");
        System.out.println("Push Down: Propagate lazy values to children");
        System.out.println("Range Operations: Both update and query in O(log n)");
        System.out.println("Use Cases: Interval problems, batch updates, range modifications");
    }
}
