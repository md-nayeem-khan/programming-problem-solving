package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Binary Lifting (Jump Pointers) - Advanced Tree Query Optimization
 * 
 * Key Applications:
 * - LCA (Lowest Common Ancestor) in O(log n)
 * - Kth ancestor queries in O(log n)
 * - Distance between nodes in O(log n)
 * - Max/Min edge weight on path in O(log n)
 * - Path queries on trees
 * 
 * Core Idea:
 * - Precompute 2^k-th ancestors for all nodes
 * - parent[u][k] = 2^k-th ancestor of node u
 * - parent[u][k] = parent[parent[u][k-1]][k-1]
 * 
 * LeetCode Problems:
 * - 1483. Kth Ancestor of a Tree Node
 * - 2846. Minimum Edge Weight Equilibrium Queries in a Tree
 * - Various LCA problems
 * 
 * Companies: Google (5 stars), Meta (4 stars), Amazon (3 stars)
 */
public class BinaryLiftingTemplates {
    
    // ========== BASIC BINARY LIFTING FOR LCA ==========
    
    /**
     * Binary Lifting for LCA (Lowest Common Ancestor)
     * 
     * Preprocessing: O(n log n)
     * Query: O(log n)
     * Space: O(n log n)
     * 
     * Use when: Multiple LCA queries on same tree
     */
    @Complexity(time = "O(n log n) preprocessing, O(log n) query", space = "O(n log n)")
    public static class BinaryLiftingLCA {
        private int n;
        private int LOG;
        private int[][] parent; // parent[node][k] = 2^k-th ancestor of node
        private int[] depth;
        private List<Integer>[] graph;
        
        /**
         * Constructor with adjacency list
         * @param n number of nodes (0-indexed)
         * @param graph adjacency list representation
         * @param root root of the tree
         * @throws IllegalArgumentException if inputs are invalid
         */
        public BinaryLiftingLCA(int n, List<Integer>[] graph, int root) {
            // Input validation
            if (n <= 0) {
                throw new IllegalArgumentException("Number of nodes must be positive, got: " + n);
            }
            if (graph == null) {
                throw new IllegalArgumentException("Graph cannot be null");
            }
            if (root < 0 || root >= n) {
                throw new IllegalArgumentException("Root must be in range [0, " + (n-1) + "], got: " + root);
            }
            
            this.n = n;
            this.LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
            this.parent = new int[n][LOG];
            this.depth = new int[n];
            this.graph = graph;
            
            // Initialize parent array with -1
            for (int[] row : parent) {
                Arrays.fill(row, -1);
            }
            
            // Build parent and depth arrays using DFS
            dfs(root, -1, 0);
            
            // Build binary lifting table
            for (int k = 1; k < LOG; k++) {
                for (int node = 0; node < n; node++) {
                    if (parent[node][k - 1] != -1) {
                        int ancestor = parent[node][k - 1];
                        parent[node][k] = parent[ancestor][k - 1];
                    }
                }
            }
        }
        
        private void dfs(int node, int par, int d) {
            parent[node][0] = par;
            depth[node] = d;
            
            if (graph[node] != null) {
                for (int child : graph[node]) {
                    if (child != par) {
                        dfs(child, node, d + 1);
                    }
                }
            }
        }
        
        /**
         * Find LCA of two nodes
         * @param u first node
         * @param v second node
         * @return LCA of u and v
         * @throws IllegalArgumentException if node indices are invalid
         */
        public int lca(int u, int v) {
            // Input validation
            if (u < 0 || u >= n || v < 0 || v >= n) {
                throw new IllegalArgumentException(
                    String.format("Node indices must be in range [0, %d], got: u=%d, v=%d", n-1, u, v));
            }
            
            // Make u the deeper node
            if (depth[u] < depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // Bring u to same level as v
            int diff = depth[u] - depth[v];
            for (int k = 0; k < LOG; k++) {
                if ((diff & (1 << k)) != 0) {
                    u = parent[u][k];
                }
            }
            
            // If u and v are the same, that's the LCA
            if (u == v) return u;
            
            // Binary search for LCA
            for (int k = LOG - 1; k >= 0; k--) {
                if (parent[u][k] != parent[v][k]) {
                    u = parent[u][k];
                    v = parent[v][k];
                }
            }
            
            return parent[u][0];
        }
        
        /**
         * Find distance between two nodes
         */
        public int distance(int u, int v) {
            int lca = lca(u, v);
            return depth[u] + depth[v] - 2 * depth[lca];
        }
        
        /**
         * Find kth ancestor of node u (0-indexed, 0th ancestor is u itself)
         * Returns -1 if kth ancestor doesn't exist
         * @param node the node to query
         * @param k the ancestor level (0 = node itself, 1 = parent, etc.)
         * @return kth ancestor or -1 if doesn't exist
         * @throws IllegalArgumentException if node index is invalid
         */
        public int kthAncestor(int node, int k) {
            if (node < 0 || node >= n) {
                throw new IllegalArgumentException(
                    String.format("Node index must be in range [0, %d], got: %d", n-1, node));
            }
            if (k < 0) return -1;
            if (k > depth[node]) return -1;
            
            for (int i = 0; i < LOG; i++) {
                if ((k & (1 << i)) != 0) {
                    node = parent[node][i];
                    if (node == -1) return -1;
                }
            }
            
            return node;
        }
        
        /**
         * Find kth node on path from u to v
         * k = 0 returns u, k = distance(u,v) returns v
         * Returns -1 if k is out of range
         * 
         * OPTIMIZED: Calculates LCA and distances once, avoiding redundant calls
         */
        public int kthNodeOnPath(int u, int v, int k) {
            if (k < 0) return -1;
            
            // Calculate LCA once
            int lcaNode = lca(u, v);
            
            // Calculate distances using already computed LCA
            int distUtoLCA = depth[u] - depth[lcaNode];
            int distVtoLCA = depth[v] - depth[lcaNode];
            int totalDist = distUtoLCA + distVtoLCA;
            
            if (k > totalDist) return -1;
            
            if (k <= distUtoLCA) {
                // kth node is on path from u to lca
                return kthAncestor(u, k);
            } else {
                // kth node is on path from lca to v
                int distFromV = totalDist - k;
                return kthAncestor(v, distFromV);
            }
        }
    }
    
    // ========== BINARY LIFTING WITH EDGE WEIGHTS ==========
    
    /**
     * Binary Lifting with edge weights for path queries
     * Supports:
     * - LCA
     * - Sum of edge weights on path
     * - Max edge weight on path
     * - Min edge weight on path
     */
    @Complexity(time = "O(n log n) preprocessing, O(log n) query", space = "O(n log n)")
    public static class WeightedBinaryLifting {
        private int n;
        private int LOG;
        private int[][] parent;
        private long[][] maxEdge; // Maximum edge weight to 2^k-th ancestor
        private long[][] minEdge; // Minimum edge weight to 2^k-th ancestor
        private long[][] sumEdge; // Sum of edge weights to 2^k-th ancestor
        private int[] depth;
        private List<Edge>[] graph;
        
        public static class Edge {
            int to;
            long weight;
            
            public Edge(int to, long weight) {
                this.to = to;
                this.weight = weight;
            }
        }
        
        /**
         * Constructor for weighted binary lifting
         * @param n number of nodes
         * @param graph adjacency list with edge weights
         * @param root root of the tree
         * @throws IllegalArgumentException if inputs are invalid
         */
        public WeightedBinaryLifting(int n, List<Edge>[] graph, int root) {
            // Input validation
            if (n <= 0) {
                throw new IllegalArgumentException("Number of nodes must be positive, got: " + n);
            }
            if (graph == null) {
                throw new IllegalArgumentException("Graph cannot be null");
            }
            if (root < 0 || root >= n) {
                throw new IllegalArgumentException("Root must be in range [0, " + (n-1) + "], got: " + root);
            }
            
            this.n = n;
            this.LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
            this.parent = new int[n][LOG];
            this.maxEdge = new long[n][LOG];
            this.minEdge = new long[n][LOG];
            this.sumEdge = new long[n][LOG];
            this.depth = new int[n];
            this.graph = graph;
            
            // Initialize
            for (int i = 0; i < n; i++) {
                Arrays.fill(parent[i], -1);
                Arrays.fill(maxEdge[i], Long.MIN_VALUE);
                Arrays.fill(minEdge[i], Long.MAX_VALUE);
            }
            
            // Build with DFS
            dfs(root, -1, 0, 0);
            
            // Build binary lifting table
            for (int k = 1; k < LOG; k++) {
                for (int node = 0; node < n; node++) {
                    if (parent[node][k - 1] != -1) {
                        int ancestor = parent[node][k - 1];
                        parent[node][k] = parent[ancestor][k - 1];
                        
                        if (parent[node][k] != -1) {
                            maxEdge[node][k] = Math.max(maxEdge[node][k - 1], 
                                                        maxEdge[ancestor][k - 1]);
                            minEdge[node][k] = Math.min(minEdge[node][k - 1], 
                                                        minEdge[ancestor][k - 1]);
                            sumEdge[node][k] = sumEdge[node][k - 1] + sumEdge[ancestor][k - 1];
                        }
                    }
                }
            }
        }
        
        private void dfs(int node, int par, int d, long edgeWeight) {
            parent[node][0] = par;
            depth[node] = d;
            
            if (par != -1) {
                maxEdge[node][0] = edgeWeight;
                minEdge[node][0] = edgeWeight;
                sumEdge[node][0] = edgeWeight;
            }
            
            if (graph[node] != null) {
                for (Edge edge : graph[node]) {
                    if (edge.to != par) {
                        dfs(edge.to, node, d + 1, edge.weight);
                    }
                }
            }
        }
        
        public int lca(int u, int v) {
            if (depth[u] < depth[v]) {
                int temp = u; u = v; v = temp;
            }
            
            int diff = depth[u] - depth[v];
            for (int k = 0; k < LOG; k++) {
                if ((diff & (1 << k)) != 0) {
                    u = parent[u][k];
                }
            }
            
            if (u == v) return u;
            
            for (int k = LOG - 1; k >= 0; k--) {
                if (parent[u][k] != parent[v][k]) {
                    u = parent[u][k];
                    v = parent[v][k];
                }
            }
            
            return parent[u][0];
        }
        
        /**
         * Find maximum edge weight on path from u to its kth ancestor
         */
        private long maxEdgeToAncestor(int u, int k) {
            long maxVal = Long.MIN_VALUE;
            
            for (int i = 0; i < LOG; i++) {
                if ((k & (1 << i)) != 0) {
                    maxVal = Math.max(maxVal, maxEdge[u][i]);
                    u = parent[u][i];
                    if (u == -1) break;
                }
            }
            
            return maxVal;
        }
        
        /**
         * Find minimum edge weight on path from u to its kth ancestor
         */
        private long minEdgeToAncestor(int u, int k) {
            long minVal = Long.MAX_VALUE;
            
            for (int i = 0; i < LOG; i++) {
                if ((k & (1 << i)) != 0) {
                    minVal = Math.min(minVal, minEdge[u][i]);
                    u = parent[u][i];
                    if (u == -1) break;
                }
            }
            
            return minVal;
        }
        
        /**
         * Find sum of edge weights on path from u to its kth ancestor
         */
        private long sumEdgeToAncestor(int u, int k) {
            long sum = 0;
            
            for (int i = 0; i < LOG; i++) {
                if ((k & (1 << i)) != 0) {
                    sum += sumEdge[u][i];
                    u = parent[u][i];
                    if (u == -1) break;
                }
            }
            
            return sum;
        }
        
        /**
         * Find maximum edge weight on path from u to v
         * OPTIMIZED: Single LCA computation
         */
        public long maxEdgeOnPath(int u, int v) {
            int lcaNode = lca(u, v);
            int distU = depth[u] - depth[lcaNode];
            int distV = depth[v] - depth[lcaNode];
            
            long maxU = (distU > 0) ? maxEdgeToAncestor(u, distU) : Long.MIN_VALUE;
            long maxV = (distV > 0) ? maxEdgeToAncestor(v, distV) : Long.MIN_VALUE;
            return Math.max(maxU, maxV);
        }
        
        /**
         * Find minimum edge weight on path from u to v
         * OPTIMIZED: Single LCA computation
         */
        public long minEdgeOnPath(int u, int v) {
            int lcaNode = lca(u, v);
            int distU = depth[u] - depth[lcaNode];
            int distV = depth[v] - depth[lcaNode];
            
            long minU = (distU > 0) ? minEdgeToAncestor(u, distU) : Long.MAX_VALUE;
            long minV = (distV > 0) ? minEdgeToAncestor(v, distV) : Long.MAX_VALUE;
            return Math.min(minU, minV);
        }
        
        /**
         * Find sum of edge weights on path from u to v
         * OPTIMIZED: Single LCA computation
         */
        public long sumEdgeOnPath(int u, int v) {
            int lcaNode = lca(u, v);
            int distU = depth[u] - depth[lcaNode];
            int distV = depth[v] - depth[lcaNode];
            
            long sumU = (distU > 0) ? sumEdgeToAncestor(u, distU) : 0;
            long sumV = (distV > 0) ? sumEdgeToAncestor(v, distV) : 0;
            return sumU + sumV;
        }
    }
    
    // ========== LEETCODE 1483: KTH ANCESTOR ==========
    
    /**
     * LeetCode 1483: Kth Ancestor of a Tree Node
     * Optimal solution using binary lifting
     */
    @Complexity(time = "O(n log n) preprocessing, O(log n) query", space = "O(n log n)")
    public static class TreeAncestor {
        private int LOG;
        private int[][] parent;
        
        /**
         * Constructor for LeetCode 1483
         * @param n number of nodes in the tree
         * @param parent parent array where parent[i] is the parent of node i
         * @throws IllegalArgumentException if inputs are invalid
         */
        public TreeAncestor(int n, int[] parent) {
            // Input validation
            if (n <= 0) {
                throw new IllegalArgumentException("Number of nodes must be positive, got: " + n);
            }
            if (parent == null) {
                throw new IllegalArgumentException("Parent array cannot be null");
            }
            if (parent.length != n) {
                throw new IllegalArgumentException(
                    String.format("Parent array length must equal n, expected: %d, got: %d", n, parent.length));
            }
            
            this.LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
            this.parent = new int[n][LOG];
            
            // Initialize first level (direct parent)
            for (int i = 0; i < n; i++) {
                this.parent[i][0] = parent[i];
            }
            
            // Build binary lifting table
            for (int k = 1; k < LOG; k++) {
                for (int node = 0; node < n; node++) {
                    if (this.parent[node][k - 1] != -1) {
                        int ancestor = this.parent[node][k - 1];
                        this.parent[node][k] = this.parent[ancestor][k - 1];
                    } else {
                        this.parent[node][k] = -1;
                    }
                }
            }
        }
        
        /**
         * Get the kth ancestor of a node
         * @param node the node to query
         * @param k the ancestor level (1 = parent, 2 = grandparent, etc.)
         * @return kth ancestor or -1 if doesn't exist
         */
        public int getKthAncestor(int node, int k) {
            if (k < 0) return -1;
            
            for (int i = 0; i < LOG && node != -1; i++) {
                if ((k & (1 << i)) != 0) {
                    node = parent[node][i];
                }
            }
            return node;
        }
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Binary Lifting Examples ===\n");
        
        // Example 1: Basic LCA
        System.out.println("Example 1: LCA Queries");
        int n = 7;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // Build tree: 0 as root
        //       0
        //      / \
        //     1   2
        //    / \   \
        //   3   4   5
        //  /
        // 6
        graph[0].add(1); graph[1].add(0);
        graph[0].add(2); graph[2].add(0);
        graph[1].add(3); graph[3].add(1);
        graph[1].add(4); graph[4].add(1);
        graph[2].add(5); graph[5].add(2);
        graph[3].add(6); graph[6].add(3);
        
        BinaryLiftingLCA lcaTree = new BinaryLiftingLCA(n, graph, 0);
        System.out.println("LCA(3, 4) = " + lcaTree.lca(3, 4)); // 1
        System.out.println("LCA(3, 5) = " + lcaTree.lca(3, 5)); // 0
        System.out.println("LCA(6, 4) = " + lcaTree.lca(6, 4)); // 1
        System.out.println("Distance(6, 4) = " + lcaTree.distance(6, 4)); // 3
        System.out.println("2nd ancestor of node 6 = " + lcaTree.kthAncestor(6, 2)); // 1
        System.out.println();
        
        // Example 2: Weighted Binary Lifting
        System.out.println("Example 2: Path Queries with Edge Weights");
        @SuppressWarnings("unchecked")
        List<WeightedBinaryLifting.Edge>[] weightedGraph = new List[n];
        for (int i = 0; i < n; i++) {
            weightedGraph[i] = new ArrayList<>();
        }
        
        // Same tree structure with edge weights
        weightedGraph[0].add(new WeightedBinaryLifting.Edge(1, 5));
        weightedGraph[1].add(new WeightedBinaryLifting.Edge(0, 5));
        weightedGraph[0].add(new WeightedBinaryLifting.Edge(2, 3));
        weightedGraph[2].add(new WeightedBinaryLifting.Edge(0, 3));
        weightedGraph[1].add(new WeightedBinaryLifting.Edge(3, 2));
        weightedGraph[3].add(new WeightedBinaryLifting.Edge(1, 2));
        weightedGraph[1].add(new WeightedBinaryLifting.Edge(4, 7));
        weightedGraph[4].add(new WeightedBinaryLifting.Edge(1, 7));
        weightedGraph[2].add(new WeightedBinaryLifting.Edge(5, 4));
        weightedGraph[5].add(new WeightedBinaryLifting.Edge(2, 4));
        weightedGraph[3].add(new WeightedBinaryLifting.Edge(6, 1));
        weightedGraph[6].add(new WeightedBinaryLifting.Edge(3, 1));
        
        WeightedBinaryLifting weightedTree = new WeightedBinaryLifting(n, weightedGraph, 0);
        System.out.println("Max edge on path(6, 4) = " + weightedTree.maxEdgeOnPath(6, 4)); // 7
        System.out.println("Min edge on path(6, 4) = " + weightedTree.minEdgeOnPath(6, 4)); // 1
        System.out.println("Sum of edges on path(6, 4) = " + weightedTree.sumEdgeOnPath(6, 4)); // 15
        System.out.println();
        
        // Example 3: LeetCode 1483 - Kth Ancestor
        System.out.println("Example 3: Kth Ancestor (LeetCode 1483)");
        int[] parentArray = {-1, 0, 0, 1, 1, 2, 2};
        TreeAncestor treeAncestor = new TreeAncestor(7, parentArray);
        System.out.println("1st ancestor of 3 = " + treeAncestor.getKthAncestor(3, 1)); // 1
        System.out.println("2nd ancestor of 5 = " + treeAncestor.getKthAncestor(5, 2)); // 0
        System.out.println("3rd ancestor of 6 = " + treeAncestor.getKthAncestor(6, 3)); // -1
    }
}
