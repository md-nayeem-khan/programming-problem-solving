package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Heavy-Light Decomposition - CRITICAL for Google (10% of hard interviews)
 * 
 * Essential for:
 * - Path queries on trees (sum, max, min)
 * - Path updates on trees
 * - Subtree queries and updates
 * - LCA with path aggregation
 * 
 * Interview Questions:
 * - Sum of nodes on path between two nodes
 * - Update path between two nodes
 * - Range queries on tree paths
 * - Advanced tree problems
 * 
 * Concept: Decompose tree into "heavy" and "light" edges
 * - Heavy edge: goes to largest subtree
 * - Light edge: all others
 * - At most O(log n) light edges on any path
 * 
 * Companies: Google (5 stars), Meta (4 stars)
 */
public class HeavyLightDecomposition {
    
    private int n;                  // Number of nodes
    private List<Integer>[] adj;    // Adjacency list
    private int[] parent;           // Parent of each node
    private int[] depth;            // Depth of each node
    private int[] subtreeSize;      // Size of subtree rooted at node
    private int[] heavy;            // Heavy child of each node (-1 if none)
    private int[] chainHead;        // Head of the chain containing node
    private int[] chainPos;         // Position in chain
    private int chainCount;         // Number of chains
    private SegmentTree[] chains;   // Segment tree for each chain
    
    /**
     * Initialize Heavy-Light Decomposition
     * 
     * @param n number of nodes (0-indexed)
     * @param edges list of edges [u, v]
     * @param root root node
     * @param values initial values of nodes
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    @SuppressWarnings("unchecked")
    public HeavyLightDecomposition(int n, int[][] edges, int root, int[] values) {
        this.n = n;
        this.adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        // Build adjacency list
        for (int[] edge : edges) {
            adj[edge[0]].add(edge[1]);
            adj[edge[1]].add(edge[0]);
        }
        
        // Initialize arrays
        parent = new int[n];
        depth = new int[n];
        subtreeSize = new int[n];
        heavy = new int[n];
        chainHead = new int[n];
        chainPos = new int[n];
        
        Arrays.fill(heavy, -1);
        Arrays.fill(parent, -1);
        
        // Decompose tree
        dfs(root, -1, 0);
        decompose(root, root);
        
        // Build segment trees for each chain
        chains = new SegmentTree[chainCount];
        buildChains(values);
    }
    
    /**
     * DFS to compute subtree sizes and find heavy children
     */
    private int dfs(int u, int p, int d) {
        parent[u] = p;
        depth[u] = d;
        subtreeSize[u] = 1;
        
        int maxSubtree = 0;
        
        for (int v : adj[u]) {
            if (v == p) continue;
            
            int size = dfs(v, u, d + 1);
            subtreeSize[u] += size;
            
            // Find heavy child (child with largest subtree)
            if (size > maxSubtree) {
                maxSubtree = size;
                heavy[u] = v;
            }
        }
        
        return subtreeSize[u];
    }
    
    /**
     * Decompose tree into heavy chains
     */
    private void decompose(int u, int head) {
        chainHead[u] = head;
        chainPos[u] = (head == u) ? 0 : chainPos[parent[u]] + 1;
        
        if (head == u) {
            chainCount++;
        }
        
        // Continue heavy chain
        if (heavy[u] != -1) {
            decompose(heavy[u], head);
        }
        
        // Start new chains for light children
        for (int v : adj[u]) {
            if (v != parent[u] && v != heavy[u]) {
                decompose(v, v);
            }
        }
    }
    
    /**
     * Build segment trees for each chain
     */
    private void buildChains(int[] values) {
        Map<Integer, List<Integer>> chainNodes = new HashMap<>();
        
        for (int i = 0; i < n; i++) {
            chainNodes.computeIfAbsent(chainHead[i], k -> new ArrayList<>()).add(i);
        }
        
        int chainId = 0;
        Map<Integer, Integer> headToId = new HashMap<>();
        
        for (int head : chainNodes.keySet()) {
            List<Integer> nodes = chainNodes.get(head);
            nodes.sort(Comparator.comparingInt(a -> chainPos[a]));
            
            int[] chainValues = new int[nodes.size()];
            for (int i = 0; i < nodes.size(); i++) {
                chainValues[i] = values[nodes.get(i)];
            }
            
            chains[chainId] = new SegmentTree(chainValues);
            headToId.put(head, chainId);
            chainId++;
        }
        
        // Update chain references
        this.chainIdMap = headToId;
    }
    
    private Map<Integer, Integer> chainIdMap;
    
    /**
     * Get chain ID for a node
     */
    private int getChainId(int u) {
        return chainIdMap.get(chainHead[u]);
    }
    
    // ========== PATH QUERIES ==========
    
    /**
     * Query sum on path from u to v
     */
    @Complexity(time = "O(log² n)", space = "O(log n)")
    public long pathQuery(int u, int v) {
        long sum = 0;
        
        while (chainHead[u] != chainHead[v]) {
            // Move up the tree from deeper node
            if (depth[chainHead[u]] < depth[chainHead[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // Query from u to head of its chain
            int chainId = getChainId(u);
            int pos = chainPos[u];
            sum += chains[chainId].query(0, pos);
            
            // Move to parent of chain head
            u = parent[chainHead[u]];
        }
        
        // u and v are now on same chain
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // Query from u to v on same chain
        int chainId = getChainId(u);
        int posU = chainPos[u];
        int posV = chainPos[v];
        sum += chains[chainId].query(posU, posV);
        
        return sum;
    }
    
    /**
     * Update value at node u
     */
    @Complexity(time = "O(log n)", space = "O(1)")
    public void update(int u, int val) {
        int chainId = getChainId(u);
        int pos = chainPos[u];
        chains[chainId].update(pos, val);
    }
    
    /**
     * Update path from u to v (add delta to all nodes on path)
     */
    @Complexity(time = "O(log² n)", space = "O(log n)")
    public void pathUpdate(int u, int v, int delta) {
        while (chainHead[u] != chainHead[v]) {
            if (depth[chainHead[u]] < depth[chainHead[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // Update from u to head of its chain
            int chainId = getChainId(u);
            int pos = chainPos[u];
            chains[chainId].rangeUpdate(0, pos, delta);
            
            u = parent[chainHead[u]];
        }
        
        // Update on same chain
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        int chainId = getChainId(u);
        int posU = chainPos[u];
        int posV = chainPos[v];
        chains[chainId].rangeUpdate(posU, posV, delta);
    }
    
    /**
     * LCA using Heavy-Light Decomposition
     */
    @Complexity(time = "O(log n)", space = "O(1)")
    public int lca(int u, int v) {
        while (chainHead[u] != chainHead[v]) {
            if (depth[chainHead[u]] < depth[chainHead[v]]) {
                v = parent[chainHead[v]];
            } else {
                u = parent[chainHead[u]];
            }
        }
        
        return depth[u] < depth[v] ? u : v;
    }
    
    /**
     * Distance between two nodes
     */
    @Complexity(time = "O(log n)", space = "O(1)")
    public int distance(int u, int v) {
        int l = lca(u, v);
        return depth[u] + depth[v] - 2 * depth[l];
    }
    
    // ========== SEGMENT TREE (FOR CHAINS) ==========
    
    /**
     * Segment Tree for chain operations
     */
    static class SegmentTree {
        private long[] tree;
        private long[] lazy;
        private int n;
        
        public SegmentTree(int[] arr) {
            this.n = arr.length;
            this.tree = new long[4 * n];
            this.lazy = new long[4 * n];
            build(arr, 0, 0, n - 1);
        }
        
        private void build(int[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
            } else {
                int mid = (start + end) / 2;
                build(arr, 2 * node + 1, start, mid);
                build(arr, 2 * node + 2, mid + 1, end);
                tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
            }
        }
        
        private void push(int node, int start, int end) {
            if (lazy[node] != 0) {
                tree[node] += lazy[node] * (end - start + 1);
                
                if (start != end) {
                    lazy[2 * node + 1] += lazy[node];
                    lazy[2 * node + 2] += lazy[node];
                }
                
                lazy[node] = 0;
            }
        }
        
        public long query(int l, int r) {
            return queryRec(0, 0, n - 1, l, r);
        }
        
        private long queryRec(int node, int start, int end, int l, int r) {
            if (start > end || start > r || end < l) {
                return 0;
            }
            
            push(node, start, end);
            
            if (start >= l && end <= r) {
                return tree[node];
            }
            
            int mid = (start + end) / 2;
            long leftSum = queryRec(2 * node + 1, start, mid, l, r);
            long rightSum = queryRec(2 * node + 2, mid + 1, end, l, r);
            
            return leftSum + rightSum;
        }
        
        public void update(int idx, int val) {
            updateRec(0, 0, n - 1, idx, val);
        }
        
        private void updateRec(int node, int start, int end, int idx, int val) {
            push(node, start, end);
            
            if (start == end) {
                tree[node] = val;
            } else {
                int mid = (start + end) / 2;
                
                if (idx <= mid) {
                    updateRec(2 * node + 1, start, mid, idx, val);
                } else {
                    updateRec(2 * node + 2, mid + 1, end, idx, val);
                }
                
                push(2 * node + 1, start, mid);
                push(2 * node + 2, mid + 1, end);
                tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
            }
        }
        
        public void rangeUpdate(int l, int r, int delta) {
            rangeUpdateRec(0, 0, n - 1, l, r, delta);
        }
        
        private void rangeUpdateRec(int node, int start, int end, int l, int r, int delta) {
            push(node, start, end);
            
            if (start > end || start > r || end < l) {
                return;
            }
            
            if (start >= l && end <= r) {
                lazy[node] += delta;
                push(node, start, end);
                return;
            }
            
            int mid = (start + end) / 2;
            rangeUpdateRec(2 * node + 1, start, mid, l, r, delta);
            rangeUpdateRec(2 * node + 2, mid + 1, end, l, r, delta);
            
            push(2 * node + 1, start, mid);
            push(2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        // Example tree:
        //       0
        //      / \
        //     1   2
        //    / \   \
        //   3   4   5
        //      /
        //     6
        
        int n = 7;
        int[][] edges = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {4, 6}
        };
        int[] values = {1, 2, 3, 4, 5, 6, 7}; // Values at nodes 0-6
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        System.out.println("=== Heavy-Light Decomposition Demo ===\n");
        
        // Path queries
        System.out.println("Sum on path 3 to 6: " + hld.pathQuery(3, 6)); // 3+2+5+7 = 17
        System.out.println("Sum on path 3 to 5: " + hld.pathQuery(3, 5)); // 3+2+1+3 = 9
        
        // LCA
        System.out.println("\nLCA(3, 6): " + hld.lca(3, 6)); // 1
        System.out.println("LCA(3, 5): " + hld.lca(3, 5)); // 0
        
        // Distance
        System.out.println("\nDistance(3, 6): " + hld.distance(3, 6)); // 3
        System.out.println("Distance(3, 5): " + hld.distance(3, 5)); // 4
        
        // Update node
        hld.update(4, 10);
        System.out.println("\nAfter updating node 4 to 10:");
        System.out.println("Sum on path 3 to 6: " + hld.pathQuery(3, 6)); // 3+2+10+7 = 22
        
        // Path update
        hld.pathUpdate(3, 5, 1); // Add 1 to all nodes on path 3->5
        System.out.println("\nAfter adding 1 to path 3->5:");
        System.out.println("Sum on path 3 to 5: " + hld.pathQuery(3, 5)); // (3+1)+(2+1)+(1+1)+(3+1) = 13
        
        System.out.println("\n=== Performance Characteristics ===");
        System.out.println("Path query: O(log² n)");
        System.out.println("Path update: O(log² n)");
        System.out.println("LCA: O(log n)");
        System.out.println("Update single node: O(log n)");
    }
}
