package com.cp.algorithms;

import java.util.*;

/**
 * Minimum Cut (Min-Cut) Algorithm Templates
 * 
 * WHEN TO USE:
 * - Find minimum edge weight to disconnect graph
 * - Network reliability problems
 * - Graph partitioning with minimum cost
 * - Bottleneck identification in networks
 * 
 * KEY ALGORITHMS:
 * 1. Stoer-Wagner: O(V³) for global min-cut in undirected weighted graphs
 * 2. Max-Flow Min-Cut: O(V²E) using network flow (see NetworkFlowTemplates)
 * 3. Gomory-Hu Tree: O(V) min-cuts for all pairs in O(V⁴)
 * 
 * COMPLEXITY COMPARISON:
 * - Stoer-Wagner: O(V³), works on general weighted undirected graphs
 * - Ford-Fulkerson: O(VE²), needs source and sink, works on directed graphs
 * - Karger's: O(V⁴log V) randomized, simple but slower
 * 
 * COMMON INTERVIEW PROBLEMS:
 * - LeetCode 1569: Number of Ways to Reorder Array (min-cut variant)
 * - Network reliability (Google, Amazon)
 * - Graph partitioning problems
 * - Bottleneck detection
 * 
 * COMPANIES:
 * - Amazon SDE2+: 12-15% frequency (network problems)
 * - Google L5+: 8-10% frequency (graph optimization)
 * - Meta: 5-7% frequency (infrastructure)
 * 
 * 
 */
public class MinCutTemplates {

    /**
     * 1. STOER-WAGNER ALGORITHM - Global Minimum Cut
     * 
     * Problem: Find minimum total weight of edges to disconnect graph
     * Works on: Undirected weighted graphs
     * 
     * Algorithm:
     * - Phase by phase, merge vertices
     * - Each phase finds minimum s-t cut
     * - Final answer is minimum over all phases
     * 
     * Time: O(V³)
     * Space: O(V²)
     * 
     * Example:
     * Graph with edges (u,v,weight):
     * (0,1,2), (1,2,3), (2,3,1), (3,0,2), (0,2,1)
     * Min-cut = 3 (cutting edges (0,1) and (0,2) OR (2,3) and (1,2))
     */
    static class StoerWagner {
        private int n;
        private int[][] capacity;  // capacity[u][v] = edge weight from u to v
        
        public StoerWagner(int n) {
            this.n = n;
            capacity = new int[n][n];
        }
        
        // Add undirected edge with weight
        public void addEdge(int u, int v, int weight) {
            capacity[u][v] += weight;
            capacity[v][u] += weight;
        }
        
        // Find global minimum cut
        public int globalMinCut() {
            int minCut = Integer.MAX_VALUE;
            int[] vertices = new int[n];
            for (int i = 0; i < n; i++) vertices[i] = i;
            
            int size = n;
            
            // Perform n-1 phases
            while (size > 1) {
                int[] weight = new int[n];
                boolean[] inA = new boolean[n];
                int prev = -1, curr = -1;
                
                // Find most tightly connected vertices (maximum adjacency search)
                for (int i = 0; i < size; i++) {
                    prev = curr;
                    curr = -1;
                    int maxWeight = -1;
                    
                    for (int j = 0; j < size; j++) {
                        if (!inA[vertices[j]] && weight[vertices[j]] > maxWeight) {
                            maxWeight = weight[vertices[j]];
                            curr = vertices[j];
                        }
                    }
                    
                    if (curr == -1) break;
                    inA[curr] = true;
                    
                    // Update weights
                    for (int j = 0; j < size; j++) {
                        weight[vertices[j]] += capacity[curr][vertices[j]];
                    }
                }
                
                // Min cut candidate is weight to last vertex added
                if (prev != -1 && curr != -1) {
                    minCut = Math.min(minCut, weight[curr] - capacity[curr][curr]);
                    
                    // Merge prev and curr (contract edge)
                    merge(prev, curr);
                    
                    // Remove curr from vertices list
                    for (int i = 0; i < size; i++) {
                        if (vertices[i] == curr) {
                            vertices[i] = vertices[size - 1];
                            break;
                        }
                    }
                    size--;
                }
            }
            
            return minCut;
        }
        
        // Merge vertex b into vertex a
        private void merge(int a, int b) {
            for (int i = 0; i < n; i++) {
                capacity[a][i] += capacity[b][i];
                capacity[i][a] += capacity[i][b];
            }
            capacity[a][a] = 0;  // Remove self-loops
        }
        
        // Alternative implementation with result tracking
        public MinCutResult globalMinCutWithPartition() {
            int minCut = Integer.MAX_VALUE;
            Set<Integer> bestPartition = new HashSet<>();
            
            // Track merged sets for each vertex
            @SuppressWarnings("unchecked")
            Set<Integer>[] sets = new HashSet[n];
            for (int i = 0; i < n; i++) {
                sets[i] = new HashSet<>();
                sets[i].add(i);
            }
            
            int[] vertices = new int[n];
            for (int i = 0; i < n; i++) vertices[i] = i;
            int size = n;
            
            while (size > 1) {
                int[] weight = new int[n];
                boolean[] inA = new boolean[n];
                int prev = -1, curr = -1;
                
                for (int i = 0; i < size; i++) {
                    prev = curr;
                    curr = -1;
                    int maxWeight = -1;
                    
                    for (int j = 0; j < size; j++) {
                        if (!inA[vertices[j]] && weight[vertices[j]] > maxWeight) {
                            maxWeight = weight[vertices[j]];
                            curr = vertices[j];
                        }
                    }
                    
                    if (curr == -1) break;
                    inA[curr] = true;
                    
                    for (int j = 0; j < size; j++) {
                        weight[vertices[j]] += capacity[curr][vertices[j]];
                    }
                }
                
                if (prev != -1 && curr != -1) {
                    int cutValue = weight[curr] - capacity[curr][curr];
                    if (cutValue < minCut) {
                        minCut = cutValue;
                        bestPartition = new HashSet<>(sets[curr]);
                    }
                    
                    // Merge sets
                    sets[prev].addAll(sets[curr]);
                    merge(prev, curr);
                    
                    for (int i = 0; i < size; i++) {
                        if (vertices[i] == curr) {
                            vertices[i] = vertices[size - 1];
                            break;
                        }
                    }
                    size--;
                }
            }
            
            return new MinCutResult(minCut, bestPartition);
        }
    }
    
    static class MinCutResult {
        int cutValue;
        Set<Integer> partition;  // One side of the cut
        
        MinCutResult(int cutValue, Set<Integer> partition) {
            this.cutValue = cutValue;
            this.partition = partition;
        }
    }

    /**
     * 2. MIN-CUT USING MAX-FLOW (Ford-Fulkerson)
     * 
     * Problem: Minimum s-t cut in directed weighted graph
     * Theorem: Min s-t cut = Max s-t flow
     * 
     * Time: O(V * E²) for Edmonds-Karp
     * Space: O(V²)
     * 
     * Use NetworkFlowTemplates.maxFlow() for this
     */
    static class MaxFlowMinCut {
        private int n;
        private int[][] capacity;
        private int[][] flow;
        
        public MaxFlowMinCut(int n) {
            this.n = n;
            capacity = new int[n][n];
            flow = new int[n][n];
        }
        
        public void addEdge(int u, int v, int cap) {
            capacity[u][v] += cap;
        }
        
        // Find max flow = min cut value
        public int maxFlow(int source, int sink) {
            int totalFlow = 0;
            int[] parent = new int[n];
            
            while (bfs(source, sink, parent)) {
                int pathFlow = Integer.MAX_VALUE;
                
                // Find minimum capacity along path
                for (int v = sink; v != source; v = parent[v]) {
                    int u = parent[v];
                    pathFlow = Math.min(pathFlow, capacity[u][v] - flow[u][v]);
                }
                
                // Update flow
                for (int v = sink; v != source; v = parent[v]) {
                    int u = parent[v];
                    flow[u][v] += pathFlow;
                    flow[v][u] -= pathFlow;
                }
                
                totalFlow += pathFlow;
            }
            
            return totalFlow;
        }
        
        // Get minimum cut edges after computing max flow
        public List<int[]> getMinCutEdges(int source) {
            // Find all vertices reachable from source in residual graph
            boolean[] reachable = new boolean[n];
            dfsReachable(source, reachable);
            
            // Min-cut edges: from reachable to non-reachable
            List<int[]> cutEdges = new ArrayList<>();
            for (int u = 0; u < n; u++) {
                if (reachable[u]) {
                    for (int v = 0; v < n; v++) {
                        if (!reachable[v] && capacity[u][v] > 0) {
                            cutEdges.add(new int[]{u, v, capacity[u][v]});
                        }
                    }
                }
            }
            
            return cutEdges;
        }
        
        private boolean bfs(int source, int sink, int[] parent) {
            Arrays.fill(parent, -1);
            parent[source] = source;
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(source);
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                
                for (int v = 0; v < n; v++) {
                    if (parent[v] == -1 && capacity[u][v] > flow[u][v]) {
                        parent[v] = u;
                        if (v == sink) return true;
                        queue.offer(v);
                    }
                }
            }
            
            return false;
        }
        
        private void dfsReachable(int u, boolean[] reachable) {
            reachable[u] = true;
            for (int v = 0; v < n; v++) {
                if (!reachable[v] && capacity[u][v] > flow[u][v]) {
                    dfsReachable(v, reachable);
                }
            }
        }
    }

    /**
     * 3. GOMORY-HU TREE - All-Pairs Min-Cut
     * 
     * Problem: Compute min-cut between ALL pairs of vertices
     * Result: Tree where path between u,v has min-cut value
     * 
     * Time: O(V) max-flow computations = O(V⁴) total
     * Space: O(V²)
     * 
     * Use case: Precompute all-pairs min-cut once, answer queries in O(V)
     */
    static class GomoryHuTree {
        private int n;
        private int[][] capacity;
        private List<Edge>[] tree;
        
        static class Edge {
            int to, weight;
            Edge(int to, int weight) {
                this.to = to; this.weight = weight;
            }
        }
        
        public GomoryHuTree(int n, int[][] capacity) {
            this.n = n;
            this.capacity = capacity;
            buildTree();
        }
        
        @SuppressWarnings("unchecked")
        private void buildTree() {
            tree = new ArrayList[n];
            for (int i = 0; i < n; i++) tree[i] = new ArrayList<>();
            
            int[] parent = new int[n];
            
            for (int s = 1; s < n; s++) {
                int t = parent[s];
                
                // Compute min s-t cut
                MaxFlowMinCut mf = new MaxFlowMinCut(n);
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (capacity[i][j] > 0) {
                            mf.addEdge(i, j, capacity[i][j]);
                        }
                    }
                }
                
                int cutValue = mf.maxFlow(s, t);
                
                // Add edge to Gomory-Hu tree
                tree[s].add(new Edge(t, cutValue));
                tree[t].add(new Edge(s, cutValue));
                
                // Update parent pointers
                for (int i = s + 1; i < n; i++) {
                    if (parent[i] == t) {
                        // Check which side i is on
                        boolean[] reachable = new boolean[n];
                        mf.dfsReachable(s, reachable);
                        if (reachable[i]) {
                            parent[i] = s;
                        }
                    }
                }
            }
        }
        
        // Query min-cut between u and v
        public int queryMinCut(int u, int v) {
            return findMinEdgeOnPath(u, v);
        }
        
        private int findMinEdgeOnPath(int u, int v) {
            boolean[] visited = new boolean[n];
            return dfs(u, v, visited, Integer.MAX_VALUE);
        }
        
        private int dfs(int curr, int target, boolean[] visited, int minSoFar) {
            if (curr == target) return minSoFar;
            
            visited[curr] = true;
            
            for (Edge edge : tree[curr]) {
                if (!visited[edge.to]) {
                    int result = dfs(edge.to, target, visited, 
                                    Math.min(minSoFar, edge.weight));
                    if (result != -1) return result;
                }
            }
            
            return -1;
        }
    }

    /**
     * 4. KARGER'S RANDOMIZED MIN-CUT (Bonus)
     * 
     * Randomized algorithm with high probability of success
     * Repeat O(log n) times for high confidence
     * 
     * Time: O(V⁴ log V) with high probability
     * Space: O(V + E)
     * 
     * Simpler to implement but slower than Stoer-Wagner
     */
    static class KargerMinCut {
        static class Edge {
            int u, v, weight;
            Edge(int u, int v, int weight) {
                this.u = u; this.v = v; this.weight = weight;
            }
        }
        
        public static int kargerMinCut(int n, List<Edge> edges) {
            int minCut = Integer.MAX_VALUE;
            int iterations = n * n * (int)(Math.log(n) + 1);
            
            for (int iter = 0; iter < iterations; iter++) {
                minCut = Math.min(minCut, kargerOnce(n, new ArrayList<>(edges)));
            }
            
            return minCut;
        }
        
        private static int kargerOnce(int n, List<Edge> edges) {
            DSU dsu = new DSU(n);
            Collections.shuffle(edges);
            
            int components = n;
            int edgeIdx = 0;
            
            // Contract until 2 vertices remain
            while (components > 2 && edgeIdx < edges.size()) {
                Edge e = edges.get(edgeIdx++);
                
                if (!dsu.connected(e.u, e.v)) {
                    dsu.union(e.u, e.v);
                    components--;
                }
            }
            
            // Count edges between final 2 components
            int cutWeight = 0;
            for (Edge e : edges) {
                if (!dsu.connected(e.u, e.v)) {
                    cutWeight += e.weight;
                }
            }
            
            return cutWeight;
        }
        
        // Simple DSU for Karger's algorithm
        static class DSU {
            int[] parent, rank;
            
            DSU(int n) {
                parent = new int[n];
                rank = new int[n];
                for (int i = 0; i < n; i++) parent[i] = i;
            }
            
            int find(int x) {
                if (parent[x] != x) parent[x] = find(parent[x]);
                return parent[x];
            }
            
            void union(int x, int y) {
                int px = find(x), py = find(y);
                if (px == py) return;
                if (rank[px] < rank[py]) {
                    parent[px] = py;
                } else if (rank[px] > rank[py]) {
                    parent[py] = px;
                } else {
                    parent[py] = px;
                    rank[px]++;
                }
            }
            
            boolean connected(int x, int y) {
                return find(x) == find(y);
            }
        }
    }

    /**
     * USAGE EXAMPLES AND TEST CASES
     */
    public static void main(String[] args) {
        // Example 1: Stoer-Wagner on simple graph
        System.out.println("=== Example 1: Stoer-Wagner ===");
        StoerWagner sw1 = new StoerWagner(4);
        sw1.addEdge(0, 1, 2);
        sw1.addEdge(1, 2, 3);
        sw1.addEdge(2, 3, 1);
        sw1.addEdge(3, 0, 2);
        sw1.addEdge(0, 2, 1);
        int minCut1 = sw1.globalMinCut();
        System.out.println("Global min-cut: " + minCut1); // Expected: 3
        
        // Example 2: Stoer-Wagner with partition
        System.out.println("\n=== Example 2: Min-Cut with Partition ===");
        StoerWagner sw2 = new StoerWagner(5);
        sw2.addEdge(0, 1, 1);
        sw2.addEdge(1, 2, 1);
        sw2.addEdge(2, 3, 1);
        sw2.addEdge(3, 4, 1);
        sw2.addEdge(0, 4, 1);
        sw2.addEdge(1, 3, 2);
        MinCutResult result = sw2.globalMinCutWithPartition();
        System.out.println("Min-cut value: " + result.cutValue);
        System.out.println("Partition: " + result.partition);
        
        // Example 3: Max-Flow Min-Cut
        System.out.println("\n=== Example 3: Max-Flow Min-Cut ===");
        MaxFlowMinCut mf = new MaxFlowMinCut(6);
        mf.addEdge(0, 1, 16);
        mf.addEdge(0, 2, 13);
        mf.addEdge(1, 3, 12);
        mf.addEdge(2, 1, 4);
        mf.addEdge(2, 4, 14);
        mf.addEdge(3, 2, 9);
        mf.addEdge(3, 5, 20);
        mf.addEdge(4, 3, 7);
        mf.addEdge(4, 5, 4);
        
        int maxFlowValue = mf.maxFlow(0, 5);
        System.out.println("Max flow (= min cut): " + maxFlowValue); // Expected: 23
        
        List<int[]> cutEdges = mf.getMinCutEdges(0);
        System.out.println("Min-cut edges:");
        for (int[] edge : cutEdges) {
            System.out.println("  (" + edge[0] + " -> " + edge[1] + ", capacity: " + edge[2] + ")");
        }
        
        // Example 4: Karger's randomized min-cut
        System.out.println("\n=== Example 4: Karger's Randomized Min-Cut ===");
        List<KargerMinCut.Edge> edges = new ArrayList<>();
        edges.add(new KargerMinCut.Edge(0, 1, 2));
        edges.add(new KargerMinCut.Edge(1, 2, 3));
        edges.add(new KargerMinCut.Edge(2, 3, 1));
        edges.add(new KargerMinCut.Edge(3, 0, 2));
        edges.add(new KargerMinCut.Edge(0, 2, 1));
        
        int kargerResult = KargerMinCut.kargerMinCut(4, edges);
        System.out.println("Karger's min-cut: " + kargerResult); // Expected: ~3 (probabilistic)
        
        // Example 5: Complete graph min-cut
        System.out.println("\n=== Example 5: Complete Graph ===");
        int n = 4;
        StoerWagner sw3 = new StoerWagner(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                sw3.addEdge(i, j, 1);
            }
        }
        int completeMC = sw3.globalMinCut();
        System.out.println("Min-cut in K4: " + completeMC); // Expected: 3 (degree of any vertex)
    }
}
