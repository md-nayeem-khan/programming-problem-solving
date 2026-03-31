package com.cp.datastructures;

import java.util.*;

/**
 * Graph representations used in competitive programming:
 *   1. GraphNode  — LeetCode clone-graph style node
 *   2. AdjacencyList — weighted / unweighted directed or undirected
 *   3. AdjacencyMatrix — for dense graphs
 *
 * Usage (adjacency list):
 *   Graph g = new Graph(5); // 5 nodes, 0-indexed
 *   g.addEdge(0, 1, 4);     // weighted
 *   g.addEdge(1, 2);         // unweighted (weight=1)
 *   List<int[]> neighbors = g.adj(0); // [neighborId, weight]
 */
public class GraphNode {

    public int val;
    public List<GraphNode> neighbors;

    public GraphNode() { val = 0; neighbors = new ArrayList<>(); }

    public GraphNode(int val) { this.val = val; neighbors = new ArrayList<>(); }

    public GraphNode(int val, List<GraphNode> neighbors) {
        this.val = val;
        this.neighbors = neighbors;
    }

    // ---------------------------------------------------------------
    // Build from LeetCode adjacency list format [[2,4],[1,3],[2,4],[1,3]]
    // nodes are 1-indexed
    // ---------------------------------------------------------------
    public static GraphNode buildFromAdjList(int[][] adj) {
        if (adj == null || adj.length == 0) return null;
        Map<Integer, GraphNode> map = new HashMap<>();
        for (int i = 1; i <= adj.length; i++) map.put(i, new GraphNode(i));
        for (int i = 0; i < adj.length; i++) {
            for (int neighbor : adj[i]) {
                map.get(i + 1).neighbors.add(map.get(neighbor));
            }
        }
        return map.get(1);
    }

    // ---------------------------------------------------------------
    // Inner class: weighted directed/undirected adjacency list
    // ---------------------------------------------------------------
    public static class Graph {
        private final int n;
        private final List<int[]>[] adjList; // [neighbor, weight]
        private final boolean directed;

        @SuppressWarnings("unchecked")
        public Graph(int n, boolean directed) {
            this.n = n;
            this.directed = directed;
            adjList = new ArrayList[n];
            for (int i = 0; i < n; i++) adjList[i] = new ArrayList<>();
        }

        public Graph(int n) { this(n, false); } // default: undirected

        public void addEdge(int u, int v, int w) {
            adjList[u].add(new int[]{v, w});
            if (!directed) adjList[v].add(new int[]{u, w});
        }

        public void addEdge(int u, int v) { addEdge(u, v, 1); }

        public List<int[]> adj(int u) { return adjList[u]; }

        public int size() { return n; }

        // Build from edge list: edges[i] = {u, v, w}
        public static Graph fromEdges(int n, int[][] edges, boolean directed) {
            Graph g = new Graph(n, directed);
            for (int[] e : edges) {
                if (e.length == 2) g.addEdge(e[0], e[1]);
                else               g.addEdge(e[0], e[1], e[2]);
            }
            return g;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                sb.append(i).append(" -> ");
                for (int[] e : adjList[i]) sb.append(e[0]).append("(w=").append(e[1]).append(") ");
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    // ---------------------------------------------------------------
    // Inner class: adjacency matrix
    // ---------------------------------------------------------------
    public static class MatrixGraph {
        public final int[][] matrix;
        public final int n;
        public static final int INF = Integer.MAX_VALUE / 2;

        public MatrixGraph(int n) {
            this.n = n;
            matrix = new int[n][n];
            for (int[] row : matrix) Arrays.fill(row, INF);
            for (int i = 0; i < n; i++) matrix[i][i] = 0;
        }

        public void addEdge(int u, int v, int w) { matrix[u][v] = w; }

        public void addUndirectedEdge(int u, int v, int w) { matrix[u][v] = matrix[v][u] = w; }
    }
}
