package com.cp.algorithms;

import java.util.*;

/**
 * Graph algorithm templates:
 *   1. Dijkstra — single-source shortest path (non-negative weights)
 *   2. Bellman-Ford — SSSP with negative weights, detects negative cycles
 *   3. Floyd-Warshall — all-pairs shortest path
 *   4. Topological Sort — Kahn's (BFS) + DFS-based
 *   5. Prim's MST — minimum spanning tree
 *   6. Kruskal's MST — using DSU
 */
public class GraphAlgorithms {

    private static final int INF = Integer.MAX_VALUE / 2;

    // ---------------------------------------------------------------
    // 1. Dijkstra — O((V + E) log V) with priority queue
    //    adj[u] = list of {v, weight}
    // ---------------------------------------------------------------
    public static int[] dijkstra(List<int[]>[] adj, int src, int n) {
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        // PriorityQueue: {distance, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, src});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int d = cur[0], u = cur[1];
            if (d > dist[u]) continue; // stale entry

            for (int[] edge : adj[u]) {
                int v = edge[0], w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }
        return dist;
    }

    // ---------------------------------------------------------------
    // 2. Bellman-Ford — O(V*E), handles negative weights
    //    edges = list of {u, v, w}
    //    Returns null if negative cycle exists
    // ---------------------------------------------------------------
    public static int[] bellmanFord(int n, int[][] edges, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean relaxed = false;
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1], w = edge[2];
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    relaxed = true;
                }
            }
            if (!relaxed) break; // early termination
        }

        // Check for negative cycle
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (dist[u] != INF && dist[u] + w < dist[v]) return null; // negative cycle
        }
        return dist;
    }

    // ---------------------------------------------------------------
    // 3. Floyd-Warshall — O(V^3), all-pairs shortest path
    //    dist[i][j] = INF if no direct edge, 0 on diagonal
    // ---------------------------------------------------------------
    public static int[][] floydWarshall(int[][] dist) {
        int n = dist.length;
        int[][] d = new int[n][n];
        for (int i = 0; i < n; i++) d[i] = dist[i].clone();

        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (d[i][k] != INF && d[k][j] != INF)
                        d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
        return d;
    }

    // ---------------------------------------------------------------
    // 4a. Topological Sort — Kahn's algorithm (BFS-based)
    //     Returns sorted order, or empty list if cycle exists
    // ---------------------------------------------------------------
    public static List<Integer> topoSortKahn(int n, List<List<Integer>> adj) {
        int[] inDegree = new int[n];
        for (int u = 0; u < n; u++)
            for (int v : adj.get(u)) inDegree[v]++;

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) if (inDegree[i] == 0) queue.offer(i);

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            for (int v : adj.get(u)) if (--inDegree[v] == 0) queue.offer(v);
        }
        return order.size() == n ? order : Collections.emptyList();
    }

    // ---------------------------------------------------------------
    // 4b. Topological Sort — DFS-based
    // ---------------------------------------------------------------
    public static List<Integer> topoSortDFS(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        boolean[] onStack = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        boolean[] hasCycle = {false};

        for (int i = 0; i < n; i++)
            if (!visited[i]) dfsTopoSort(adj, i, visited, onStack, stack, hasCycle);

        if (hasCycle[0]) return Collections.emptyList();
        List<Integer> result = new ArrayList<>(stack);
        return result;
    }

    private static void dfsTopoSort(List<List<Integer>> adj, int u, boolean[] visited,
                                    boolean[] onStack, Deque<Integer> stack, boolean[] hasCycle) {
        visited[u] = onStack[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v]) dfsTopoSort(adj, v, visited, onStack, stack, hasCycle);
            else if (onStack[v]) hasCycle[0] = true;
        }
        onStack[u] = false;
        stack.push(u);
    }

    // ---------------------------------------------------------------
    // 5. Prim's MST — O(E log V)
    //    Returns total MST weight
    // ---------------------------------------------------------------
    public static int primMST(List<int[]>[] adj, int n) {
        boolean[] inMST = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, 0}); // {weight, node}
        int totalWeight = 0, edgesAdded = 0;

        while (!pq.isEmpty() && edgesAdded < n) {
            int[] cur = pq.poll();
            int w = cur[0], u = cur[1];
            if (inMST[u]) continue;
            inMST[u] = true;
            totalWeight += w;
            edgesAdded++;
            for (int[] edge : adj[u]) {
                if (!inMST[edge[0]]) pq.offer(new int[]{edge[1], edge[0]});
            }
        }
        return totalWeight;
    }

    // ---------------------------------------------------------------
    // 6. Kruskal's MST using DSU — O(E log E)
    //    edges = list of {u, v, w}, sorted by weight
    // ---------------------------------------------------------------
    public static int kruskalMST(int n, int[][] edges) {
        Arrays.sort(edges, Comparator.comparingInt(a -> a[2]));
        int[] parent = new int[n], rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int totalWeight = 0, edgesAdded = 0;

        for (int[] edge : edges) {
            int pu = find(parent, edge[0]), pv = find(parent, edge[1]);
            if (pu != pv) {
                if (rank[pu] < rank[pv]) { int t = pu; pu = pv; pv = t; }
                parent[pv] = pu;
                if (rank[pu] == rank[pv]) rank[pu]++;
                totalWeight += edge[2];
                if (++edgesAdded == n - 1) break;
            }
        }
        return totalWeight;
    }

    private static int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }
}
