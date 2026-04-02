package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Network Flow Algorithms - CRITICAL for Amazon SDE2+ (25%), Microsoft (15%)
 * 
 * Essential for:
 * - Maximum flow problems
 * - Minimum cut problems
 * - Bipartite matching
 * - Assignment problems
 * - Resource allocation
 * 
 * Interview Questions:
 * - LeetCode 1579: Remove Max Number of Edges to Keep Graph Fully Traversable
 * - LeetCode 1168: Optimize Water Distribution in a Village
 * - Maximum Bipartite Matching
 * - Project Selection Problem
 * 
 * Companies: Amazon (5/5), Microsoft (4/5), Google (3/5)
 */
public class NetworkFlowTemplates {
    
    // ========== FORD-FULKERSON (EDMONDS-KARP) ==========
    
    /**
     * Ford-Fulkerson algorithm with BFS (Edmonds-Karp)
     * Finds maximum flow from source to sink in a flow network
     * 
     * @param capacity adjacency matrix where capacity[u][v] = capacity of edge u->v
     * @param source source node
     * @param sink sink node
     * @return maximum flow value
     */
    @Complexity(time = "O(V * E^2)", space = "O(V^2)")
    public static int fordFulkerson(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] residual = new int[n][n];
        
        // Initialize residual graph with capacities
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                residual[i][j] = capacity[i][j];
            }
        }
        
        int maxFlow = 0;
        int[] parent = new int[n];
        
        // While there exists an augmenting path from source to sink
        while (bfs(residual, source, sink, parent)) {
            // Find minimum capacity along the path
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residual[u][v]);
            }
            
            // Update residual capacities
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residual[u][v] -= pathFlow;
                residual[v][u] += pathFlow; // Add to reverse edge
            }
            
            maxFlow += pathFlow;
        }
        
        return maxFlow;
    }
    
    /**
     * BFS to find augmenting path in residual graph
     */
    private static boolean bfs(int[][] residual, int source, int sink, int[] parent) {
        int n = residual.length;
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        
        queue.offer(source);
        visited[source] = true;
        parent[source] = -1;
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            
            for (int v = 0; v < n; v++) {
                if (!visited[v] && residual[u][v] > 0) {
                    visited[v] = true;
                    parent[v] = u;
                    queue.offer(v);
                    
                    if (v == sink) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    // ========== DINIC'S ALGORITHM (FASTER) ==========
    
    /**
     * Dinic's Algorithm - Faster than Ford-Fulkerson
     * Uses level graph and blocking flow concept
     * 
     * @param graph adjacency list representation (edge list)
     * @param n number of nodes
     * @param source source node
     * @param sink sink node
     * @return maximum flow value
     */
    @Complexity(time = "O(V^2 * E)", space = "O(V + E)")
    public static long dinic(List<Edge>[] graph, int n, int source, int sink) {
        long maxFlow = 0;
        int[] level = new int[n];
        int[] nextEdge = new int[n];
        
        while (buildLevelGraph(graph, level, source, sink)) {
            Arrays.fill(nextEdge, 0);
            long flow;
            while ((flow = sendFlow(graph, level, nextEdge, source, sink, Long.MAX_VALUE)) > 0) {
                maxFlow += flow;
            }
        }
        
        return maxFlow;
    }
    
    /**
     * Build level graph using BFS
     */
    private static boolean buildLevelGraph(List<Edge>[] graph, int[] level, int source, int sink) {
        Arrays.fill(level, -1);
        level[source] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (Edge edge : graph[u]) {
                if (level[edge.to] < 0 && edge.flow < edge.capacity) {
                    level[edge.to] = level[u] + 1;
                    queue.offer(edge.to);
                }
            }
        }
        
        return level[sink] >= 0;
    }
    
    /**
     * Send blocking flow using DFS
     */
    private static long sendFlow(List<Edge>[] graph, int[] level, int[] nextEdge, 
                                  int u, int sink, long flow) {
        if (u == sink) return flow;
        
        for (; nextEdge[u] < graph[u].size(); nextEdge[u]++) {
            Edge edge = graph[u].get(nextEdge[u]);
            
            if (level[edge.to] == level[u] + 1 && edge.flow < edge.capacity) {
                long minFlow = Math.min(flow, edge.capacity - edge.flow);
                long pushedFlow = sendFlow(graph, level, nextEdge, edge.to, sink, minFlow);
                
                if (pushedFlow > 0) {
                    edge.flow += pushedFlow;
                    edge.reverse.flow -= pushedFlow;
                    return pushedFlow;
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Edge class for Dinic's algorithm
     */
    public static class Edge {
        int to;
        long capacity;
        long flow;
        Edge reverse;
        
        public Edge(int to, long capacity) {
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }
    
    /**
     * Add edge for Dinic's algorithm (creates forward and reverse edges)
     */
    public static void addEdge(List<Edge>[] graph, int from, int to, long capacity) {
        Edge forward = new Edge(to, capacity);
        Edge backward = new Edge(from, 0); // Reverse edge with 0 capacity
        forward.reverse = backward;
        backward.reverse = forward;
        graph[from].add(forward);
        graph[to].add(backward);
    }
    
    // ========== MINIMUM CUT ==========
    
    /**
     * Find minimum cut in flow network
     * After running max flow, nodes reachable from source form one partition
     * 
     * @param capacity capacity matrix
     * @param source source node
     * @param sink sink node
     * @return list of edges in minimum cut
     */
    @Complexity(time = "O(V * E^2)", space = "O(V^2)")
    public static List<int[]> findMinCut(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] residual = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                residual[i][j] = capacity[i][j];
            }
        }
        
        int[] parent = new int[n];
        
        // Run Ford-Fulkerson to get residual graph
        while (bfs(residual, source, sink, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residual[u][v]);
            }
            
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residual[u][v] -= pathFlow;
                residual[v][u] += pathFlow;
            }
        }
        
        // Find nodes reachable from source in residual graph
        boolean[] reachable = new boolean[n];
        dfsReachable(residual, source, reachable);
        
        // Collect edges in min cut
        List<int[]> minCut = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            if (reachable[u]) {
                for (int v = 0; v < n; v++) {
                    if (!reachable[v] && capacity[u][v] > 0) {
                        minCut.add(new int[]{u, v});
                    }
                }
            }
        }
        
        return minCut;
    }
    
    /**
     * DFS to find reachable nodes in residual graph
     */
    private static void dfsReachable(int[][] residual, int u, boolean[] reachable) {
        reachable[u] = true;
        for (int v = 0; v < residual.length; v++) {
            if (!reachable[v] && residual[u][v] > 0) {
                dfsReachable(residual, v, reachable);
            }
        }
    }
    
    // ========== BIPARTITE MATCHING ==========
    
    /**
     * Maximum bipartite matching using max flow
     * 
     * Problem: Given bipartite graph with left and right sets, find maximum matching
     * Solution: Create source->left edges (cap 1), left->right (cap 1), right->sink (cap 1)
     * 
     * @param leftSize size of left partition
     * @param rightSize size of right partition
     * @param edges list of edges [left_node, right_node] (0-indexed)
     * @return maximum matching size
     */
    @Complexity(time = "O(V * E^2)", space = "O(V^2)")
    public static int maxBipartiteMatching(int leftSize, int rightSize, int[][] edges) {
        // Nodes: 0 = source, 1..leftSize = left partition, 
        //        leftSize+1..leftSize+rightSize = right partition, last = sink
        int n = leftSize + rightSize + 2;
        int source = 0;
        int sink = n - 1;
        
        int[][] capacity = new int[n][n];
        
        // Source to left partition (capacity 1)
        for (int i = 1; i <= leftSize; i++) {
            capacity[source][i] = 1;
        }
        
        // Left to right partition (capacity 1)
        for (int[] edge : edges) {
            int left = edge[0] + 1; // Offset by 1 (source is 0)
            int right = edge[1] + leftSize + 1; // Offset by leftSize + 1
            capacity[left][right] = 1;
        }
        
        // Right partition to sink (capacity 1)
        for (int i = leftSize + 1; i < sink; i++) {
            capacity[i][sink] = 1;
        }
        
        return fordFulkerson(capacity, source, sink);
    }
    
    /**
     * Find actual matching edges (not just count)
     * 
     * @param leftSize size of left partition
     * @param rightSize size of right partition
     * @param edges list of edges [left_node, right_node]
     * @return list of matched pairs [left, right]
     */
    @Complexity(time = "O(V * E^2)", space = "O(V^2)")
    public static List<int[]> findBipartiteMatching(int leftSize, int rightSize, int[][] edges) {
        int n = leftSize + rightSize + 2;
        int source = 0;
        int sink = n - 1;
        
        int[][] capacity = new int[n][n];
        int[][] residual = new int[n][n];
        
        for (int i = 1; i <= leftSize; i++) {
            capacity[source][i] = 1;
            residual[source][i] = 1;
        }
        
        for (int[] edge : edges) {
            int left = edge[0] + 1;
            int right = edge[1] + leftSize + 1;
            capacity[left][right] = 1;
            residual[left][right] = 1;
        }
        
        for (int i = leftSize + 1; i < sink; i++) {
            capacity[i][sink] = 1;
            residual[i][sink] = 1;
        }
        
        // Run max flow
        int[] parent = new int[n];
        while (bfs(residual, source, sink, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residual[u][v]);
            }
            
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residual[u][v] -= pathFlow;
                residual[v][u] += pathFlow;
            }
        }
        
        // Extract matching from flow
        List<int[]> matching = new ArrayList<>();
        for (int[] edge : edges) {
            int left = edge[0] + 1;
            int right = edge[1] + leftSize + 1;
            // If flow went through this edge, it's in the matching
            if (residual[left][right] == 0 && capacity[left][right] == 1) {
                matching.add(new int[]{edge[0], edge[1]});
            }
        }
        
        return matching;
    }
    
    // ========== EXAMPLE PROBLEMS ==========
    
    /**
     * LeetCode 1579: Remove Max Number of Edges to Keep Graph Fully Traversable
     * 
     * Problem: Graph with 3 types of edges - Alice only, Bob only, Both
     * Remove maximum edges while keeping graph traversable for both
     * 
     * Solution: Use Union-Find, prioritize "Both" edges
     */
    @Complexity(time = "O(E * α(V))", space = "O(V)")
    public static int maxNumEdgesToRemove(int n, int[][] edges) {
        DSU alice = new DSU(n);
        DSU bob = new DSU(n);
        
        int edgesUsed = 0;
        
        // First, add type 3 (both) edges
        for (int[] edge : edges) {
            if (edge[0] == 3) {
                int u = edge[1] - 1;
                int v = edge[2] - 1;
                if (alice.union(u, v) | bob.union(u, v)) {
                    edgesUsed++;
                }
            }
        }
        
        // Add Alice's edges (type 1)
        for (int[] edge : edges) {
            if (edge[0] == 1) {
                int u = edge[1] - 1;
                int v = edge[2] - 1;
                if (alice.union(u, v)) {
                    edgesUsed++;
                }
            }
        }
        
        // Add Bob's edges (type 2)
        for (int[] edge : edges) {
            if (edge[0] == 2) {
                int u = edge[1] - 1;
                int v = edge[2] - 1;
                if (bob.union(u, v)) {
                    edgesUsed++;
                }
            }
        }
        
        // Check if both can traverse entire graph
        if (alice.components != 1 || bob.components != 1) {
            return -1;
        }
        
        return edges.length - edgesUsed;
    }
    
    /**
     * Union-Find for LeetCode 1579
     */
    static class DSU {
        int[] parent;
        int[] rank;
        int components;
        
        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        boolean union(int x, int y) {
            int px = find(x);
            int py = find(y);
            
            if (px == py) return false;
            
            if (rank[px] < rank[py]) {
                parent[px] = py;
            } else if (rank[px] > rank[py]) {
                parent[py] = px;
            } else {
                parent[py] = px;
                rank[px]++;
            }
            
            components--;
            return true;
        }
    }
    
    // ========== HUNGARIAN ALGORITHM (MIN-COST BIPARTITE MATCHING) ==========
    
    /**
     * Hungarian Algorithm for minimum cost perfect matching in bipartite graph
     * 
     * Problem: Assign n workers to n jobs with minimum total cost
     * 
     * @param cost cost[i][j] = cost of assigning worker i to job j
     * @return minimum total cost for perfect matching
     */
    @Complexity(time = "O(n³)", space = "O(n²)")
    public static int hungarianAlgorithm(int[][] cost) {
        // Use the working Kuhn-Munkres implementation
        int[] assignment = hungarianAssignment(cost);
        
        // Calculate minimum cost from assignment
        int minCost = 0;
        for (int worker = 0; worker < cost.length; worker++) {
            minCost += cost[worker][assignment[worker]];
        }
        
        return minCost;
    }
    
    /**
     * Hungarian Algorithm that returns the actual assignment
     * 
     * @param cost cost matrix
     * @return array where result[i] = job assigned to worker i
     */
    @Complexity(time = "O(n³)", space = "O(n²)")
    public static int[] hungarianAssignment(int[][] cost) {
        int n = cost.length;
        int[] result = new int[n];
        
        // Use Kuhn-Munkres algorithm
        int[] u = new int[n + 1];
        int[] v = new int[n + 1];
        int[] p = new int[n + 1];
        int[] way = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            p[0] = i;
            int j0 = 0;
            int[] minv = new int[n + 1];
            Arrays.fill(minv, Integer.MAX_VALUE);
            boolean[] used = new boolean[n + 1];
            
            do {
                used[j0] = true;
                int i0 = p[j0];
                int delta = Integer.MAX_VALUE;
                int j1 = -1;
                
                for (int j = 1; j <= n; j++) {
                    if (!used[j]) {
                        int cur = cost[i0 - 1][j - 1] - u[i0] - v[j];
                        if (cur < minv[j]) {
                            minv[j] = cur;
                            way[j] = j0;
                        }
                        if (minv[j] < delta) {
                            delta = minv[j];
                            j1 = j;
                        }
                    }
                }
                
                for (int j = 0; j <= n; j++) {
                    if (used[j]) {
                        u[p[j]] += delta;
                        v[j] -= delta;
                    } else {
                        minv[j] -= delta;
                    }
                }
                
                j0 = j1;
            } while (p[j0] != 0);
            
            do {
                int j1 = way[j0];
                p[j0] = p[j1];
                j0 = j1;
            } while (j0 != 0);
        }
        
        for (int j = 1; j <= n; j++) {
            result[p[j] - 1] = j - 1;
        }
        
        return result;
    }
    
    /**
     * Maximum weight bipartite matching (maximize instead of minimize)
     * 
     * Convert to minimum cost by negating weights
     */
    @Complexity(time = "O(n³)", space = "O(n²)")
    public static int maxWeightBipartiteMatching(int[][] weight) {
        int n = weight.length;
        int[][] cost = new int[n][n];
        
        // Negate to convert max to min
        int maxWeight = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                maxWeight = Math.max(maxWeight, Math.abs(weight[i][j]));
            }
        }
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = maxWeight - weight[i][j];
            }
        }
        
        int minCost = hungarianAlgorithm(cost);
        return n * maxWeight - minCost;
    }
    
    // ========== MIN-COST MAX-FLOW (MCMF) ==========
    
    /**
     * Min-Cost Max-Flow using Bellman-Ford for shortest augmenting path
     * Finds maximum flow with minimum total cost
     * 
     * Applications:
     * - Assignment problems with costs
     * - Transportation problems
     * - Resource allocation with budgets
     * 
     * LeetCode: Network problems with cost optimization
     * Companies: Amazon (5/5), Google (4/5)
     * 
     * @param n number of nodes
     * @param source source node
     * @param sink sink node
     * @param edges list of [from, to, capacity, cost per unit flow]
     * @return array [maxFlow, minCost]
     */
    @Complexity(time = "O(flow * V * E)", space = "O(V + E)")
    public static long[] minCostMaxFlow(int n, int source, int sink, int[][] edges) {
        // Build adjacency list with cost edges
        List<CostEdge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // Add all edges (forward and reverse)
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            long capacity = edge[2];
            long cost = edge[3];
            
            CostEdge forward = new CostEdge(to, capacity, cost);
            CostEdge backward = new CostEdge(from, 0, -cost);
            forward.reverse = backward;
            backward.reverse = forward;
            
            graph[from].add(forward);
            graph[to].add(backward);
        }
        
        long maxFlow = 0;
        long minCost = 0;
        
        // Find minimum cost augmenting paths using SPFA (Bellman-Ford variant)
        while (true) {
            long[] dist = new long[n];
            int[] parent = new int[n];
            CostEdge[] parentEdge = new CostEdge[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            Arrays.fill(parent, -1);
            
            // SPFA (Shortest Path Faster Algorithm)
            boolean[] inQueue = new boolean[n];
            Queue<Integer> queue = new LinkedList<>();
            dist[source] = 0;
            queue.offer(source);
            inQueue[source] = true;
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                inQueue[u] = false;
                
                for (CostEdge edge : graph[u]) {
                    if (edge.flow < edge.capacity && dist[u] + edge.cost < dist[edge.to]) {
                        dist[edge.to] = dist[u] + edge.cost;
                        parent[edge.to] = u;
                        parentEdge[edge.to] = edge;
                        
                        if (!inQueue[edge.to]) {
                            queue.offer(edge.to);
                            inQueue[edge.to] = true;
                        }
                    }
                }
            }
            
            // No augmenting path found
            if (dist[sink] == Long.MAX_VALUE) {
                break;
            }
            
            // Find bottleneck capacity along the path
            long pathFlow = Long.MAX_VALUE;
            int v = sink;
            while (v != source) {
                CostEdge edge = parentEdge[v];
                pathFlow = Math.min(pathFlow, edge.capacity - edge.flow);
                v = parent[v];
            }
            
            // Update flow along the path
            v = sink;
            while (v != source) {
                CostEdge edge = parentEdge[v];
                edge.flow += pathFlow;
                edge.reverse.flow -= pathFlow;
                minCost += pathFlow * edge.cost;
                v = parent[v];
            }
            
            maxFlow += pathFlow;
        }
        
        return new long[]{maxFlow, minCost};
    }
    
    /**
     * Edge class for Min-Cost Max-Flow
     */
    public static class CostEdge {
        int to;
        long capacity;
        long flow;
        long cost; // Cost per unit of flow
        CostEdge reverse;
        
        public CostEdge(int to, long capacity, long cost) {
            this.to = to;
            this.capacity = capacity;
            this.cost = cost;
            this.flow = 0;
        }
    }
    
    /**
     * Min-Cost Max-Flow optimized with potentials (Johnson's algorithm)
     * Faster when many augmenting paths are needed
     * Uses Dijkstra instead of Bellman-Ford after first iteration
     * 
     * @param n number of nodes
     * @param source source node
     * @param sink sink node
     * @param edges list of [from, to, capacity, cost]
     * @return array [maxFlow, minCost]
     */
    @Complexity(time = "O(flow * E * log V)", space = "O(V + E)")
    public static long[] minCostMaxFlowFast(int n, int source, int sink, int[][] edges) {
        List<CostEdge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            long capacity = edge[2];
            long cost = edge[3];
            
            CostEdge forward = new CostEdge(to, capacity, cost);
            CostEdge backward = new CostEdge(from, 0, -cost);
            forward.reverse = backward;
            backward.reverse = forward;
            
            graph[from].add(forward);
            graph[to].add(backward);
        }
        
        long maxFlow = 0;
        long minCost = 0;
        long[] potential = new long[n]; // Potential for each node (Johnson's algorithm)
        
        while (true) {
            // Use Dijkstra with reduced costs
            long[] dist = new long[n];
            int[] parent = new int[n];
            CostEdge[] parentEdge = new CostEdge[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            Arrays.fill(parent, -1);
            
            PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
            dist[source] = 0;
            pq.offer(new long[]{source, 0});
            
            while (!pq.isEmpty()) {
                long[] curr = pq.poll();
                int u = (int) curr[0];
                long d = curr[1];
                
                if (d > dist[u]) continue;
                
                for (CostEdge edge : graph[u]) {
                    if (edge.flow < edge.capacity) {
                        // Reduced cost using potentials
                        long reducedCost = edge.cost + potential[u] - potential[edge.to];
                        if (dist[u] + reducedCost < dist[edge.to]) {
                            dist[edge.to] = dist[u] + reducedCost;
                            parent[edge.to] = u;
                            parentEdge[edge.to] = edge;
                            pq.offer(new long[]{edge.to, dist[edge.to]});
                        }
                    }
                }
            }
            
            if (dist[sink] == Long.MAX_VALUE) {
                break;
            }
            
            // Update potentials
            for (int i = 0; i < n; i++) {
                if (dist[i] < Long.MAX_VALUE) {
                    potential[i] += dist[i];
                }
            }
            
            // Find and push flow
            long pathFlow = Long.MAX_VALUE;
            int v = sink;
            while (v != source) {
                CostEdge edge = parentEdge[v];
                pathFlow = Math.min(pathFlow, edge.capacity - edge.flow);
                v = parent[v];
            }
            
            v = sink;
            while (v != source) {
                CostEdge edge = parentEdge[v];
                edge.flow += pathFlow;
                edge.reverse.flow -= pathFlow;
                minCost += pathFlow * edge.cost;
                v = parent[v];
            }
            
            maxFlow += pathFlow;
        }
        
        return new long[]{maxFlow, minCost};
    }
    
    /**
     * Min-Cost Flow with demands (circulation with demands)
     * Each node has a demand (positive = supply, negative = demand)
     * 
     * @param n number of nodes
     * @param demands array where demands[i] = supply/demand at node i
     * @param edges list of [from, to, capacity, cost]
     * @return minimum cost to satisfy all demands, or -1 if impossible
     */
    @Complexity(time = "O(flow * E * log V)", space = "O(V + E)")
    public static long minCostFlowWithDemands(int n, int[] demands, int[][] edges) {
        // Check if demands are balanced (total supply = total demand)
        long totalSupply = 0;
        long totalDemand = 0;
        for (int demand : demands) {
            if (demand > 0) {
                totalSupply += demand;
            } else if (demand < 0) {
                totalDemand += -demand;
            }
        }
        
        if (totalSupply != totalDemand) {
            return -1; // Demands are unbalanced, impossible to satisfy
        }
        
        // Add super source and super sink
        int source = n;
        int sink = n + 1;
        int newN = n + 2;
        
        List<int[]> newEdges = new ArrayList<>();
        
        // Connect nodes with positive demand (supply) to source
        // Connect sink to nodes with negative demand
        for (int i = 0; i < n; i++) {
            if (demands[i] > 0) {
                newEdges.add(new int[]{source, i, demands[i], 0});
            } else if (demands[i] < 0) {
                newEdges.add(new int[]{i, sink, -demands[i], 0});
            }
        }
        
        // Add original edges
        newEdges.addAll(Arrays.asList(edges));
        
        int[][] edgeArray = newEdges.toArray(new int[0][]);
        long[] result = minCostMaxFlowFast(newN, source, sink, edgeArray);
        
        // Check if all demands are satisfied
        if (result[0] != totalDemand) {
            return -1; // Impossible to satisfy demands
        }
        
        return result[1];
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        // Example 1: Simple max flow
        int[][] capacity1 = {
            {0, 16, 13, 0, 0, 0},
            {0, 0, 10, 12, 0, 0},
            {0, 4, 0, 0, 14, 0},
            {0, 0, 9, 0, 0, 20},
            {0, 0, 0, 7, 0, 4},
            {0, 0, 0, 0, 0, 0}
        };
        System.out.println("Max Flow (Ford-Fulkerson): " + fordFulkerson(capacity1, 0, 5)); // 23
        
        // Example 2: Dinic's algorithm
        int n = 6;
        @SuppressWarnings("unchecked")
        List<Edge>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        addEdge(graph, 0, 1, 16);
        addEdge(graph, 0, 2, 13);
        addEdge(graph, 1, 2, 10);
        addEdge(graph, 1, 3, 12);
        addEdge(graph, 2, 1, 4);
        addEdge(graph, 2, 4, 14);
        addEdge(graph, 3, 2, 9);
        addEdge(graph, 3, 5, 20);
        addEdge(graph, 4, 3, 7);
        addEdge(graph, 4, 5, 4);
        
        System.out.println("Max Flow (Dinic): " + dinic(graph, n, 0, 5)); // 23
        
        // Example 3: Bipartite matching
        int[][] bipartiteEdges = {
            {0, 0}, {0, 1}, // Left node 0 connects to right nodes 0, 1
            {1, 0}, {1, 2}, // Left node 1 connects to right nodes 0, 2
            {2, 1}, {2, 2}  // Left node 2 connects to right nodes 1, 2
        };
        System.out.println("Max Bipartite Matching: " + 
                          maxBipartiteMatching(3, 3, bipartiteEdges)); // 3
        
        // Example 4: Find actual matching
        List<int[]> matching = findBipartiteMatching(3, 3, bipartiteEdges);
        System.out.println("Matching pairs:");
        for (int[] pair : matching) {
            System.out.println("  Left " + pair[0] + " - Right " + pair[1]);
        }
        
        // Example 5: Hungarian Algorithm (Min-Cost Assignment)
        System.out.println("\n=== Hungarian Algorithm Example ===");
        int[][] assignmentCost = {
            {10, 20, 30},
            {15, 25, 35},
            {20, 30, 40}
        };
        System.out.println("Assignment cost matrix:");
        for (int[] row : assignmentCost) {
            System.out.println("  " + java.util.Arrays.toString(row));
        }
        System.out.println("Minimum cost: " + hungarianAlgorithm(assignmentCost));
        
        int[] assignment = hungarianAssignment(assignmentCost);
        System.out.println("Assignment:");
        for (int i = 0; i < assignment.length; i++) {
            System.out.println("  Worker " + i + " -> Job " + assignment[i] + 
                             " (cost: " + assignmentCost[i][assignment[i]] + ")");
        }
    }
}
