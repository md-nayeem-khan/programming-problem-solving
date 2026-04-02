package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Advanced Graph Algorithm Templates for coding interviews.
 * 
 * Coverage:
 * - Tarjan's Algorithm (SCC, Bridges, Articulation Points)
 * - Biconnected Components
 * - Eulerian Path/Circuit
 * - Strongly Connected Components (Kosaraju's)
 * - 2-SAT Problem
 * 
 * Critical for: Google (advanced), Meta (senior)
 * Frequency: ~3-5% of interviews (advanced/senior roles)
 */
public class AdvancedGraphTemplates {

    // ============================================================================
    // TARJAN'S ALGORITHM - STRONGLY CONNECTED COMPONENTS (SCC)
    // ============================================================================
    
    /**
     * Tarjan's Algorithm for finding Strongly Connected Components.
     * 
     * SCC: Maximal set of vertices where every vertex is reachable from every other.
     * 
     * Interview favorite: Google (hard)
     */
    @Complexity(time = "O(V + E)", space = "O(V)")
    public static List<List<Integer>> tarjanSCC(int n, List<List<Integer>> adj) {
        int[] ids = new int[n];
        int[] low = new int[n];
        boolean[] onStack = new boolean[n];
        Arrays.fill(ids, -1);
        
        int[] id = {0}; // Current ID
        Deque<Integer> stack = new ArrayDeque<>();
        List<List<Integer>> sccs = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            if (ids[i] == -1) {
                tarjanDFS(i, adj, ids, low, onStack, stack, id, sccs);
            }
        }
        
        return sccs;
    }
    
    private static void tarjanDFS(int u, List<List<Integer>> adj, int[] ids, int[] low,
                                 boolean[] onStack, Deque<Integer> stack, int[] id,
                                 List<List<Integer>> sccs) {
        ids[u] = low[u] = id[0]++;
        stack.push(u);
        onStack[u] = true;
        
        for (int v : adj.get(u)) {
            if (ids[v] == -1) {
                tarjanDFS(v, adj, ids, low, onStack, stack, id, sccs);
            }
            if (onStack[v]) {
                low[u] = Math.min(low[u], low[v]);
            }
        }
        
        // Found SCC root
        if (ids[u] == low[u]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                scc.add(node);
                if (node == u) break;
            }
            sccs.add(scc);
        }
    }
    
    // ============================================================================
    // TARJAN'S ALGORITHM - BRIDGES (CRITICAL EDGES)
    // ============================================================================
    
    /**
     * Find Bridges - edges whose removal increases connected components.
     * 
     * Interview favorite: Google, Meta
     */
    @Complexity(time = "O(V + E)", space = "O(V)")
    public static List<List<Integer>> findBridges(int n, List<List<Integer>> adj) {
        int[] ids = new int[n];
        int[] low = new int[n];
        Arrays.fill(ids, -1);
        
        int[] id = {0};
        List<List<Integer>> bridges = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            if (ids[i] == -1) {
                bridgesDFS(i, -1, adj, ids, low, id, bridges);
            }
        }
        
        return bridges;
    }
    
    private static void bridgesDFS(int u, int parent, List<List<Integer>> adj,
                                  int[] ids, int[] low, int[] id, List<List<Integer>> bridges) {
        ids[u] = low[u] = id[0]++;
        
        for (int v : adj.get(u)) {
            if (v == parent) continue;
            
            if (ids[v] == -1) {
                bridgesDFS(v, u, adj, ids, low, id, bridges);
                low[u] = Math.min(low[u], low[v]);
                
                // Bridge found
                if (ids[u] < low[v]) {
                    bridges.add(Arrays.asList(u, v));
                }
            } else {
                low[u] = Math.min(low[u], ids[v]);
            }
        }
    }
    
    // ============================================================================
    // TARJAN'S ALGORITHM - ARTICULATION POINTS (CUT VERTICES)
    // ============================================================================
    
    /**
     * Find Articulation Points - vertices whose removal increases components.
     * 
     * Interview favorite: Google
     */
    @Complexity(time = "O(V + E)", space = "O(V)")
    public static List<Integer> findArticulationPoints(int n, List<List<Integer>> adj) {
        int[] ids = new int[n];
        int[] low = new int[n];
        Arrays.fill(ids, -1);
        
        int[] id = {0};
        Set<Integer> articulation = new HashSet<>();
        
        for (int i = 0; i < n; i++) {
            if (ids[i] == -1) {
                int[] children = {0};
                articulationDFS(i, -1, true, adj, ids, low, id, articulation, children);
            }
        }
        
        return new ArrayList<>(articulation);
    }
    
    private static void articulationDFS(int u, int parent, boolean isRoot,
                                       List<List<Integer>> adj, int[] ids, int[] low,
                                       int[] id, Set<Integer> articulation, int[] children) {
        ids[u] = low[u] = id[0]++;
        
        for (int v : adj.get(u)) {
            if (v == parent) continue;
            
            if (ids[v] == -1) {
                if (isRoot) children[0]++;
                
                articulationDFS(v, u, false, adj, ids, low, id, articulation, children);
                low[u] = Math.min(low[u], low[v]);
                
                // Articulation point found
                if (!isRoot && ids[u] <= low[v]) {
                    articulation.add(u);
                }
            } else {
                low[u] = Math.min(low[u], ids[v]);
            }
        }
        
        // Root is articulation point if has >= 2 children
        if (isRoot && children[0] >= 2) {
            articulation.add(u);
        }
    }
    
    // ============================================================================
    // KOSARAJU'S ALGORITHM - ALTERNATIVE SCC
    // ============================================================================
    
    /**
     * Kosaraju's Algorithm for SCC (alternative to Tarjan's).
     * Two DFS passes: one on original graph, one on transpose.
     */
    @Complexity(time = "O(V + E)", space = "O(V)")
    public static List<List<Integer>> kosarajuSCC(int n, List<List<Integer>> adj) {
        // First DFS: get finish times
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                kosarajuDFS1(i, adj, visited, stack);
            }
        }
        
        // Build transpose graph
        List<List<Integer>> transpose = new ArrayList<>();
        for (int i = 0; i < n; i++) transpose.add(new ArrayList<>());
        
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                transpose.get(v).add(u);
            }
        }
        
        // Second DFS on transpose in reverse finish time order
        Arrays.fill(visited, false);
        List<List<Integer>> sccs = new ArrayList<>();
        
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (!visited[u]) {
                List<Integer> scc = new ArrayList<>();
                kosarajuDFS2(u, transpose, visited, scc);
                sccs.add(scc);
            }
        }
        
        return sccs;
    }
    
    private static void kosarajuDFS1(int u, List<List<Integer>> adj, boolean[] visited,
                                    Deque<Integer> stack) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                kosarajuDFS1(v, adj, visited, stack);
            }
        }
        stack.push(u);
    }
    
    private static void kosarajuDFS2(int u, List<List<Integer>> adj, boolean[] visited,
                                    List<Integer> scc) {
        visited[u] = true;
        scc.add(u);
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                kosarajuDFS2(v, adj, visited, scc);
            }
        }
    }
    
    // ============================================================================
    // EULERIAN PATH/CIRCUIT
    // ============================================================================
    
    /**
     * Check if Eulerian Path exists in directed graph.
     * Eulerian Path: visit every edge exactly once.
     * 
     * Conditions:
     * - Path exists: at most one vertex with out-degree = in-degree + 1,
     *                at most one with in-degree = out-degree + 1, rest equal
     * - Circuit exists: all vertices have out-degree = in-degree
     */
    @Complexity(time = "O(V + E)", space = "O(V)")
    public static boolean hasEulerianPath(int n, int[][] edges) {
        int[] inDegree = new int[n];
        int[] outDegree = new int[n];
        
        for (int[] edge : edges) {
            outDegree[edge[0]]++;
            inDegree[edge[1]]++;
        }
        
        int startNodes = 0, endNodes = 0;
        for (int i = 0; i < n; i++) {
            if (outDegree[i] - inDegree[i] > 1 || inDegree[i] - outDegree[i] > 1) {
                return false;
            }
            if (outDegree[i] - inDegree[i] == 1) startNodes++;
            if (inDegree[i] - outDegree[i] == 1) endNodes++;
        }
        
        return (startNodes == 0 && endNodes == 0) || (startNodes == 1 && endNodes == 1);
    }
    
    /**
     * Find Eulerian Path using Hierholzer's Algorithm.
     */
    @Complexity(time = "O(E)", space = "O(E)")
    public static List<Integer> findEulerianPath(int n, int[][] edges) {
        Map<Integer, Deque<Integer>> graph = new HashMap<>();
        int[] inDegree = new int[n];
        int[] outDegree = new int[n];
        
        for (int[] edge : edges) {
            graph.computeIfAbsent(edge[0], k -> new ArrayDeque<>()).add(edge[1]);
            outDegree[edge[0]]++;
            inDegree[edge[1]]++;
        }
        
        // Find start node
        int start = 0;
        for (int i = 0; i < n; i++) {
            if (outDegree[i] - inDegree[i] == 1) {
                start = i;
                break;
            }
            if (outDegree[i] > 0) start = i;
        }
        
        List<Integer> path = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        
        while (!stack.isEmpty()) {
            int u = stack.peek();
            if (graph.containsKey(u) && !graph.get(u).isEmpty()) {
                int v = graph.get(u).poll();
                stack.push(v);
            } else {
                path.add(stack.pop());
            }
        }
        
        Collections.reverse(path);
        return path.size() == edges.length + 1 ? path : new ArrayList<>();
    }
    
    // ============================================================================
    // 2-SAT PROBLEM
    // ============================================================================
    
    /**
     * 2-SAT Problem - satisfiability of boolean formula in CNF with 2 literals per clause.
     * 
     * Build implication graph and check if variable and its negation are in same SCC.
     */
    @Complexity(time = "O(V + E)", space = "O(V)")
    public static boolean solve2SAT(int n, List<int[]> clauses) {
        // Build implication graph
        // For clause (a OR b), add edges: NOT a -> b, NOT b -> a
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < 2 * n; i++) adj.add(new ArrayList<>());
        
        for (int[] clause : clauses) {
            int a = clause[0];
            int b = clause[1];
            
            // a = 2*var for positive, 2*var+1 for negative
            int notA = a ^ 1;
            int notB = b ^ 1;
            
            adj.get(notA).add(b);
            adj.get(notB).add(a);
        }
        
        // Find SCCs
        List<List<Integer>> sccs = tarjanSCC(2 * n, adj);
        
        // Check if any variable and its negation are in same SCC
        int[] sccId = new int[2 * n];
        for (int i = 0; i < sccs.size(); i++) {
            for (int node : sccs.get(i)) {
                sccId[node] = i;
            }
        }
        
        for (int i = 0; i < n; i++) {
            if (sccId[2 * i] == sccId[2 * i + 1]) {
                return false; // Unsatisfiable
            }
        }
        
        return true;
    }
    
    // ============================================================================
    // BICONNECTED COMPONENTS
    // ============================================================================
    
    /**
     * Find Biconnected Components - maximal subgraphs with no articulation points.
     */
    @Complexity(time = "O(V + E)", space = "O(V + E)")
    public static List<List<int[]>> findBiconnectedComponents(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }
        
        int[] ids = new int[n];
        int[] low = new int[n];
        Arrays.fill(ids, -1);
        
        int[] id = {0};
        Deque<int[]> stack = new ArrayDeque<>();
        List<List<int[]>> components = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            if (ids[i] == -1) {
                biconnectedDFS(i, -1, adj, ids, low, id, stack, components);
            }
        }
        
        return components;
    }
    
    private static void biconnectedDFS(int u, int parent, List<List<Integer>> adj,
                                      int[] ids, int[] low, int[] id,
                                      Deque<int[]> stack, List<List<int[]>> components) {
        ids[u] = low[u] = id[0]++;
        
        for (int v : adj.get(u)) {
            if (v == parent) continue;
            
            if (ids[v] == -1) {
                stack.push(new int[]{u, v});
                biconnectedDFS(v, u, adj, ids, low, id, stack, components);
                low[u] = Math.min(low[u], low[v]);
                
                // Found biconnected component
                if (ids[u] <= low[v]) {
                    List<int[]> component = new ArrayList<>();
                    int[] edge;
                    do {
                        edge = stack.pop();
                        component.add(edge);
                    } while (edge[0] != u || edge[1] != v);
                    components.add(component);
                }
            } else if (ids[v] < ids[u]) {
                stack.push(new int[]{u, v});
                low[u] = Math.min(low[u], ids[v]);
            }
        }
    }
}
