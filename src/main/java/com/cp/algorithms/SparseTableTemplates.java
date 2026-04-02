package com.cp.algorithms;

/**
 * Sparse Table Templates - O(1) Range Query Data Structure
 * 
 * WHEN TO USE:
 * - Static arrays (no updates after preprocessing)
 * - Need O(1) query time for range queries
 * - Operations are idempotent (min, max, gcd, but NOT sum)
 * - LCA queries on trees with O(1) response time
 * 
 * KEY INSIGHT:
 * Any range [L, R] can be covered by two overlapping ranges of length 2^k
 * where k = floor(log2(R - L + 1))
 * Result = operation(sparse[L][k], sparse[R - 2^k + 1][k])
 * 
 * COMPLEXITY:
 * - Build: O(n log n) time, O(n log n) space
 * - Query: O(1) time
 * - Update: Not supported (static structure)
 * 
 * COMPARISON WITH OTHER STRUCTURES:
 * - Sparse Table: O(n log n) build, O(1) query, no updates, idempotent ops only
 * - Segment Tree: O(n) build, O(log n) query, O(log n) update, any operation
 * - Fenwick Tree: O(n) build, O(log n) query/update, only prefix operations
 * 
 * COMMON INTERVIEW PROBLEMS:
 * - LeetCode 1483: Kth Ancestor of Tree Node
 * - Range Minimum/Maximum Query (RMQ) problems
 * - LCA queries with O(1) time
 * - Range GCD queries
 * 
 * 
 */
public class SparseTableTemplates {

    /**
     * 1. RANGE MINIMUM QUERY (RMQ) - Most Common Use Case
     * 
     * Problem: Given array, answer Q queries: minimum value in range [L, R]
     * Time: O(n log n) preprocessing, O(1) per query
     * Space: O(n log n)
     * 
     * Example:
     * arr = [1, 3, 2, 7, 9, 11]
     * rangeMin(1, 4) = 2 (min of [3, 2, 7, 9])
     * rangeMin(0, 5) = 1 (min of entire array)
     */
    static class RangeMinimumQuery {
        private int[][] sparse;
        private int[] log;
        private int n;
        
        // Build sparse table for RMQ
        // Time: O(n log n), Space: O(n log n)
        public RangeMinimumQuery(int[] arr) {
            n = arr.length;
            int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
            sparse = new int[n][maxLog];
            log = new int[n + 1];
            
            // Precompute logarithms for O(1) query
            log[1] = 0;
            for (int i = 2; i <= n; i++) {
                log[i] = log[i / 2] + 1;
            }
            
            // Initialize first column with array elements
            for (int i = 0; i < n; i++) {
                sparse[i][0] = arr[i];
            }
            
            // Fill sparse table using DP
            // sparse[i][j] = min of range [i, i + 2^j - 1]
            for (int j = 1; j < maxLog; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    sparse[i][j] = Math.min(sparse[i][j - 1], 
                                           sparse[i + (1 << (j - 1))][j - 1]);
                }
            }
        }
        
        // Query minimum in range [L, R] in O(1)
        public int query(int L, int R) {
            int len = R - L + 1;
            int k = log[len];
            // Cover [L, R] with two overlapping ranges of length 2^k
            return Math.min(sparse[L][k], sparse[R - (1 << k) + 1][k]);
        }
    }

    /**
     * 1B. RANGE MINIMUM INDEX QUERY - Returns index of minimum element
     * 
     * Similar to RMQ but returns the index of minimum element instead of value.
     * Useful for LCA with Euler Tour where we need position of minimum depth.
     * 
     * Time: O(n log n) preprocessing, O(1) per query
     * Space: O(n log n)
     */
    static class RangeMinimumIndexQuery {
        private int[][] sparse;  // stores indices, not values
        private int[] log;
        private int[] arr;  // reference to original array for comparisons
        private int n;
        
        public RangeMinimumIndexQuery(int[] arr) {
            this.arr = arr;
            n = arr.length;
            int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
            sparse = new int[n][maxLog];
            log = new int[n + 1];
            
            log[1] = 0;
            for (int i = 2; i <= n; i++) {
                log[i] = log[i / 2] + 1;
            }
            
            // Initialize first column with indices
            for (int i = 0; i < n; i++) {
                sparse[i][0] = i;
            }
            
            // Fill sparse table using DP
            // sparse[i][j] = index of min element in range [i, i + 2^j - 1]
            for (int j = 1; j < maxLog; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    int leftIdx = sparse[i][j - 1];
                    int rightIdx = sparse[i + (1 << (j - 1))][j - 1];
                    sparse[i][j] = (arr[leftIdx] <= arr[rightIdx]) ? leftIdx : rightIdx;
                }
            }
        }
        
        // Query index of minimum in range [L, R] in O(1)
        public int query(int L, int R) {
            int len = R - L + 1;
            int k = log[len];
            int leftIdx = sparse[L][k];
            int rightIdx = sparse[R - (1 << k) + 1][k];
            return (arr[leftIdx] <= arr[rightIdx]) ? leftIdx : rightIdx;
        }
    }

    /**
     * 2. RANGE MAXIMUM QUERY (Similar to RMQ)
     * 
     * Time: O(n log n) preprocessing, O(1) per query
     * Space: O(n log n)
     */
    static class RangeMaximumQuery {
        private int[][] sparse;
        private int[] log;
        private int n;
        
        public RangeMaximumQuery(int[] arr) {
            n = arr.length;
            int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
            sparse = new int[n][maxLog];
            log = new int[n + 1];
            
            log[1] = 0;
            for (int i = 2; i <= n; i++) {
                log[i] = log[i / 2] + 1;
            }
            
            for (int i = 0; i < n; i++) {
                sparse[i][0] = arr[i];
            }
            
            for (int j = 1; j < maxLog; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    sparse[i][j] = Math.max(sparse[i][j - 1], 
                                           sparse[i + (1 << (j - 1))][j - 1]);
                }
            }
        }
        
        public int query(int L, int R) {
            int len = R - L + 1;
            int k = log[len];
            return Math.max(sparse[L][k], sparse[R - (1 << k) + 1][k]);
        }
    }

    /**
     * 3. RANGE GCD QUERY
     * 
     * Problem: Given array, answer queries: GCD of range [L, R]
     * Time: O(n log n) preprocessing, O(1) per query
     * Space: O(n log n)
     * 
     * Note: GCD is idempotent: gcd(a, a) = a
     * So overlapping ranges are fine!
     * 
     * Example:
     * arr = [6, 12, 18, 24, 30]
     * rangeGCD(0, 2) = 6 (gcd of 6, 12, 18)
     * rangeGCD(1, 4) = 6 (gcd of 12, 18, 24, 30)
     */
    static class RangeGCDQuery {
        private int[][] sparse;
        private int[] log;
        private int n;
        
        public RangeGCDQuery(int[] arr) {
            n = arr.length;
            int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
            sparse = new int[n][maxLog];
            log = new int[n + 1];
            
            log[1] = 0;
            for (int i = 2; i <= n; i++) {
                log[i] = log[i / 2] + 1;
            }
            
            for (int i = 0; i < n; i++) {
                sparse[i][0] = arr[i];
            }
            
            for (int j = 1; j < maxLog; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    sparse[i][j] = gcd(sparse[i][j - 1], 
                                      sparse[i + (1 << (j - 1))][j - 1]);
                }
            }
        }
        
        public int query(int L, int R) {
            int len = R - L + 1;
            int k = log[len];
            return gcd(sparse[L][k], sparse[R - (1 << k) + 1][k]);
        }
        
        private int gcd(int a, int b) {
            return b == 0 ? a : gcd(b, a % b);
        }
    }

    /**
     * 4. GENERIC SPARSE TABLE (Template for Any Idempotent Operation)
     * 
     * Works for any operation where f(x, x) = x (idempotent)
     * Examples: min, max, gcd, lcm, bitwise AND, bitwise OR
     * Does NOT work for: sum, product, xor (not idempotent)
     * 
     * Time: O(n log n) preprocessing, O(1) per query
     * Space: O(n log n)
     */
    static class GenericSparseTable<T> {
        private Object[][] sparse;
        private int[] log;
        private int n;
        private java.util.function.BinaryOperator<T> operation;
        
        public GenericSparseTable(T[] arr, java.util.function.BinaryOperator<T> op) {
            n = arr.length;
            int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
            sparse = new Object[n][maxLog];
            log = new int[n + 1];
            operation = op;
            
            log[1] = 0;
            for (int i = 2; i <= n; i++) {
                log[i] = log[i / 2] + 1;
            }
            
            for (int i = 0; i < n; i++) {
                sparse[i][0] = arr[i];
            }
            
            for (int j = 1; j < maxLog; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    @SuppressWarnings("unchecked")
                    T left = (T) sparse[i][j - 1];
                    @SuppressWarnings("unchecked")
                    T right = (T) sparse[i + (1 << (j - 1))][j - 1];
                    sparse[i][j] = operation.apply(left, right);
                }
            }
        }
        
        @SuppressWarnings("unchecked")
        public T query(int L, int R) {
            int len = R - L + 1;
            int k = log[len];
            T left = (T) sparse[L][k];
            T right = (T) sparse[R - (1 << k) + 1][k];
            return operation.apply(left, right);
        }
    }

    /**
     * 5. LCA (LOWEST COMMON ANCESTOR) using Sparse Table + Binary Lifting
     * 
     * Problem: Given tree, answer Q queries: LCA of nodes u and v
     * Approach: Use binary lifting with sparse table
     * 
     * Time: O(n log n) preprocessing, O(log n) per LCA query
     * Space: O(n log n)
     * 
     * For O(1) LCA, use Sparse Table on Euler Tour (see method 6)
     * 
     * Example:
     *       1
     *      / \
     *     2   3
     *    / \
     *   4   5
     * 
     * LCA(4, 5) = 2
     * LCA(4, 3) = 1
     */
    static class LCABinaryLifting {
        private int[][] up;  // up[node][i] = 2^i-th ancestor of node
        private int[] depth;
        private int maxLog;
        private int n;
        
        public LCABinaryLifting(int n, int root, java.util.List<Integer>[] tree) {
            this.n = n;
            maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
            up = new int[n][maxLog];
            depth = new int[n];
            
            // Initialize with -1 (no ancestor)
            for (int i = 0; i < n; i++) {
                java.util.Arrays.fill(up[i], -1);
            }
            
            // DFS to compute depth and immediate parent
            dfs(root, -1, tree);
            
            // Fill sparse table for binary lifting
            // up[node][j] = 2^j-th ancestor of node
            for (int j = 1; j < maxLog; j++) {
                for (int node = 0; node < n; node++) {
                    if (up[node][j - 1] != -1) {
                        up[node][j] = up[up[node][j - 1]][j - 1];
                    }
                }
            }
        }
        
        private void dfs(int node, int parent, java.util.List<Integer>[] tree) {
            up[node][0] = parent;
            if (parent != -1) {
                depth[node] = depth[parent] + 1;
            }
            
            if (tree[node] != null) {
                for (int child : tree[node]) {
                    if (child != parent) {
                        dfs(child, node, tree);
                    }
                }
            }
        }
        
        // Find LCA of nodes u and v in O(log n)
        public int lca(int u, int v) {
            // Make u the deeper node
            if (depth[u] < depth[v]) {
                int temp = u; u = v; v = temp;
            }
            
            // Bring u to same level as v
            int diff = depth[u] - depth[v];
            for (int i = 0; i < maxLog; i++) {
                if (((diff >> i) & 1) == 1) {
                    u = up[u][i];
                }
            }
            
            if (u == v) return u;
            
            // Binary search for LCA
            for (int i = maxLog - 1; i >= 0; i--) {
                if (up[u][i] != up[v][i]) {
                    u = up[u][i];
                    v = up[v][i];
                }
            }
            
            return up[u][0];
        }
        
        // Get k-th ancestor of node in O(log k)
        public int kthAncestor(int node, int k) {
            for (int i = 0; i < maxLog; i++) {
                if (((k >> i) & 1) == 1) {
                    node = up[node][i];
                    if (node == -1) return -1;
                }
            }
            return node;
        }
        
        // Distance between two nodes
        public int distance(int u, int v) {
            int l = lca(u, v);
            return depth[u] + depth[v] - 2 * depth[l];
        }
    }

    /**
     * 6. LCA with O(1) Query using Sparse Table on Euler Tour
     * 
     * Convert LCA to RMQ problem:
     * 1. Do Euler tour of tree (visit each node when entering/leaving)
     * 2. Record first occurrence of each node
     * 3. LCA(u, v) = node with minimum depth in Euler tour between first[u] and first[v]
     * 4. Use RMQ sparse table on depth array
     * 
     * Time: O(n log n) preprocessing, O(1) per LCA query
     * Space: O(n log n)
     * 
     * This is THE FASTEST LCA for static trees!
     */
    static class LCAEulerTour {
        private int[] euler;     // Euler tour of tree
        private int[] depth;     // Depth at each position in Euler tour
        private int[] first;     // First occurrence of each node in Euler tour
        private RangeMinimumIndexQuery rmq;  // Use index query, not value query
        private int eulerIndex;
        
        public LCAEulerTour(int n, int root, java.util.List<Integer>[] tree) {
            euler = new int[2 * n - 1];
            depth = new int[2 * n - 1];
            first = new int[n];
            java.util.Arrays.fill(first, -1);
            eulerIndex = 0;
            
            // Perform Euler tour
            eulerTour(root, -1, 0, tree);
            
            // Build RMQ on depth array - returns INDEX of minimum
            rmq = new RangeMinimumIndexQuery(depth);
        }
        
        private void eulerTour(int node, int parent, int d, java.util.List<Integer>[] tree) {
            // Record first occurrence
            if (first[node] == -1) {
                first[node] = eulerIndex;
            }
            
            euler[eulerIndex] = node;
            depth[eulerIndex] = d;
            eulerIndex++;
            
            if (tree[node] != null) {
                for (int child : tree[node]) {
                    if (child != parent) {
                        eulerTour(child, node, d + 1, tree);
                        // Return to current node in Euler tour
                        euler[eulerIndex] = node;
                        depth[eulerIndex] = d;
                        eulerIndex++;
                    }
                }
            }
        }
        
        // Find LCA in O(1)
        public int lca(int u, int v) {
            int left = first[u];
            int right = first[v];
            if (left > right) {
                int temp = left; left = right; right = temp;
            }
            
            // Find position with minimum depth
            int minDepthPos = rmq.query(left, right);
            return euler[minDepthPos];
        }
    }

    /**
     * 7. 2D SPARSE TABLE for 2D Range Minimum Query
     * 
     * Problem: Given 2D matrix, answer queries: minimum in rectangle
     * Time: O(n * m * log n * log m) preprocessing, O(1) per query
     * Space: O(n * m * log n * log m)
     * 
     * Example:
     * matrix = [[1, 2, 3],
     *           [4, 5, 6],
     *           [7, 8, 9]]
     * query(0, 0, 1, 1) = 1 (min of top-left 2x2)
     * query(1, 1, 2, 2) = 5 (min of bottom-right 2x2)
     */
    static class SparseTable2D {
        private int[][][][] sparse;
        private int[] logX, logY;
        private int n, m;
        
        public SparseTable2D(int[][] matrix) {
            n = matrix.length;
            m = matrix[0].length;
            int maxLogX = (int) (Math.log(n) / Math.log(2)) + 1;
            int maxLogY = (int) (Math.log(m) / Math.log(2)) + 1;
            
            sparse = new int[n][m][maxLogX][maxLogY];
            logX = new int[n + 1];
            logY = new int[m + 1];
            
            logX[1] = 0; logY[1] = 0;
            for (int i = 2; i <= n; i++) logX[i] = logX[i / 2] + 1;
            for (int i = 2; i <= m; i++) logY[i] = logY[i / 2] + 1;
            
            // Initialize base case
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    sparse[i][j][0][0] = matrix[i][j];
                }
            }
            
            // Fill for increasing powers of 2 in y-direction
            for (int k = 1; k < maxLogY; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j + (1 << k) <= m; j++) {
                        sparse[i][j][0][k] = Math.min(sparse[i][j][0][k - 1],
                                                      sparse[i][j + (1 << (k - 1))][0][k - 1]);
                    }
                }
            }
            
            // Fill for increasing powers of 2 in x-direction
            for (int p = 1; p < maxLogX; p++) {
                for (int k = 0; k < maxLogY; k++) {
                    for (int i = 0; i + (1 << p) <= n; i++) {
                        for (int j = 0; j + (1 << k) <= m; j++) {
                            sparse[i][j][p][k] = Math.min(sparse[i][j][p - 1][k],
                                                          sparse[i + (1 << (p - 1))][j][p - 1][k]);
                        }
                    }
                }
            }
        }
        
        // Query minimum in rectangle [x1, y1] to [x2, y2] in O(1)
        public int query(int x1, int y1, int x2, int y2) {
            int lenX = x2 - x1 + 1;
            int lenY = y2 - y1 + 1;
            int kX = logX[lenX];
            int kY = logY[lenY];
            
            int min1 = Math.min(sparse[x1][y1][kX][kY],
                               sparse[x2 - (1 << kX) + 1][y1][kX][kY]);
            int min2 = Math.min(sparse[x1][y2 - (1 << kY) + 1][kX][kY],
                               sparse[x2 - (1 << kX) + 1][y2 - (1 << kY) + 1][kX][kY]);
            return Math.min(min1, min2);
        }
    }

    /**
     * USAGE EXAMPLES AND TEST CASES
     */
    public static void main(String[] args) {
        // Example 1: Range Minimum Query
        int[] arr1 = {1, 3, 2, 7, 9, 11, 3, 5};
        RangeMinimumQuery rmq = new RangeMinimumQuery(arr1);
        System.out.println("RMQ(1, 4) = " + rmq.query(1, 4)); // Expected: 2
        System.out.println("RMQ(0, 7) = " + rmq.query(0, 7)); // Expected: 1
        System.out.println("RMQ(3, 6) = " + rmq.query(3, 6)); // Expected: 3
        
        // Example 2: Range Maximum Query
        RangeMaximumQuery rmaxq = new RangeMaximumQuery(arr1);
        System.out.println("RMaxQ(1, 4) = " + rmaxq.query(1, 4)); // Expected: 9
        
        // Example 3: Range GCD Query
        int[] arr2 = {6, 12, 18, 24, 30};
        RangeGCDQuery gcdq = new RangeGCDQuery(arr2);
        System.out.println("GCD(0, 2) = " + gcdq.query(0, 2)); // Expected: 6
        System.out.println("GCD(1, 4) = " + gcdq.query(1, 4)); // Expected: 6
        
        // Example 4: Generic Sparse Table (min operation)
        Integer[] arr3 = {5, 2, 8, 1, 9};
        GenericSparseTable<Integer> gst = new GenericSparseTable<>(arr3, Math::min);
        System.out.println("Generic Min(1, 3) = " + gst.query(1, 3)); // Expected: 1
        
        // Example 5: LCA with Binary Lifting
        int n = 7;
        @SuppressWarnings("unchecked")
        java.util.List<Integer>[] tree = new java.util.ArrayList[n];
        for (int i = 0; i < n; i++) tree[i] = new java.util.ArrayList<>();
        
        // Build tree:      0
        //                 / \
        //                1   2
        //               / \   \
        //              3   4   5
        //                     /
        //                    6
        tree[0].add(1); tree[1].add(0);
        tree[0].add(2); tree[2].add(0);
        tree[1].add(3); tree[3].add(1);
        tree[1].add(4); tree[4].add(1);
        tree[2].add(5); tree[5].add(2);
        tree[5].add(6); tree[6].add(5);
        
        LCABinaryLifting lcaBL = new LCABinaryLifting(n, 0, tree);
        System.out.println("LCA(3, 4) = " + lcaBL.lca(3, 4)); // Expected: 1
        System.out.println("LCA(3, 6) = " + lcaBL.lca(3, 6)); // Expected: 0
        System.out.println("LCA(4, 5) = " + lcaBL.lca(4, 5)); // Expected: 0
        System.out.println("2nd ancestor of 6 = " + lcaBL.kthAncestor(6, 2)); // Expected: 0
        
        // Example 6: 2D Sparse Table
        int[][] matrix = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        SparseTable2D st2d = new SparseTable2D(matrix);
        System.out.println("2D RMQ(0,0,1,1) = " + st2d.query(0, 0, 1, 1)); // Expected: 1
        System.out.println("2D RMQ(1,1,2,2) = " + st2d.query(1, 1, 2, 2)); // Expected: 6
        System.out.println("2D RMQ(0,0,3,3) = " + st2d.query(0, 0, 3, 3)); // Expected: 1
    }
}
