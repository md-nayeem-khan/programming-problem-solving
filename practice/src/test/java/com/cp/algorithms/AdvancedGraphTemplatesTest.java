package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for AdvancedGraphTemplates.
 * 
 * Tests all graph algorithms with:
 * - Basic functionality
 * - Edge cases
 * - Complex scenarios
 * - Interview-style problems
 */
@DisplayName("Advanced Graph Templates Test Suite")
class AdvancedGraphTemplatesTest {

    // ============================================================================
    // HELPER METHODS - GRAPH BUILDERS
    // ============================================================================
    
    /**
     * Build adjacency list from edge array for directed graph.
     */
    private static List<List<Integer>> buildDirectedGraph(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
        }
        return adj;
    }
    
    /**
     * Build adjacency list from edge array for undirected graph.
     */
    private static List<List<Integer>> buildUndirectedGraph(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }
        return adj;
    }
    
    /**
     * Check if two sets of SCCs are equivalent (order-independent).
     */
    private static boolean areSCCsEqual(List<List<Integer>> sccs1, List<List<Integer>> sccs2) {
        if (sccs1.size() != sccs2.size()) return false;
        
        Set<Set<Integer>> set1 = new HashSet<>();
        Set<Set<Integer>> set2 = new HashSet<>();
        
        for (List<Integer> scc : sccs1) {
            set1.add(new HashSet<>(scc));
        }
        for (List<Integer> scc : sccs2) {
            set2.add(new HashSet<>(scc));
        }
        
        return set1.equals(set2);
    }
    
    /**
     * Check if edge exists in list of edges.
     */
    private static boolean containsEdge(List<List<Integer>> edges, int u, int v) {
        for (List<Integer> edge : edges) {
            if ((edge.get(0) == u && edge.get(1) == v) || 
                (edge.get(0) == v && edge.get(1) == u)) {
                return true;
            }
        }
        return false;
    }

    // ============================================================================
    // TARJAN'S SCC TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Tarjan SCC: Single node")
    void testTarjanSCC_SingleNode() {
        List<List<Integer>> adj = buildDirectedGraph(1, new int[][]{});
        List<List<Integer>> sccs = AdvancedGraphTemplates.tarjanSCC(1, adj);
        
        assertEquals(1, sccs.size());
        assertEquals(1, sccs.get(0).size());
        assertTrue(sccs.get(0).contains(0));
    }
    
    @Test
    @DisplayName("Tarjan SCC: Simple cycle")
    void testTarjanSCC_SimpleCycle() {
        // 0 -> 1 -> 2 -> 0
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        List<List<Integer>> adj = buildDirectedGraph(3, edges);
        List<List<Integer>> sccs = AdvancedGraphTemplates.tarjanSCC(3, adj);
        
        assertEquals(1, sccs.size());
        assertEquals(3, sccs.get(0).size());
        assertTrue(sccs.get(0).containsAll(Arrays.asList(0, 1, 2)));
    }
    
    @Test
    @DisplayName("Tarjan SCC: Multiple SCCs")
    void testTarjanSCC_MultipleSCCs() {
        // SCC1: 0 <-> 1, SCC2: 2 <-> 3, Edge: 1 -> 2
        int[][] edges = {{0, 1}, {1, 0}, {1, 2}, {2, 3}, {3, 2}};
        List<List<Integer>> adj = buildDirectedGraph(4, edges);
        List<List<Integer>> sccs = AdvancedGraphTemplates.tarjanSCC(4, adj);
        
        assertEquals(2, sccs.size());
        
        // Verify SCCs content (order may vary)
        Set<Set<Integer>> expected = new HashSet<>();
        expected.add(new HashSet<>(Arrays.asList(0, 1)));
        expected.add(new HashSet<>(Arrays.asList(2, 3)));
        
        Set<Set<Integer>> actual = new HashSet<>();
        for (List<Integer> scc : sccs) {
            actual.add(new HashSet<>(scc));
        }
        
        assertEquals(expected, actual);
    }
    
    @Test
    @DisplayName("Tarjan SCC: Line graph (no cycles)")
    void testTarjanSCC_LineGraph() {
        // 0 -> 1 -> 2 -> 3
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}};
        List<List<Integer>> adj = buildDirectedGraph(4, edges);
        List<List<Integer>> sccs = AdvancedGraphTemplates.tarjanSCC(4, adj);
        
        assertEquals(4, sccs.size()); // Each node is its own SCC
    }
    
    @Test
    @DisplayName("Tarjan SCC: Self-loop")
    void testTarjanSCC_SelfLoop() {
        // 0 -> 0
        int[][] edges = {{0, 0}};
        List<List<Integer>> adj = buildDirectedGraph(1, edges);
        List<List<Integer>> sccs = AdvancedGraphTemplates.tarjanSCC(1, adj);
        
        assertEquals(1, sccs.size());
        assertTrue(sccs.get(0).contains(0));
    }
    
    @Test
    @DisplayName("Tarjan SCC: Disconnected components")
    void testTarjanSCC_Disconnected() {
        // Component 1: 0 -> 1, Component 2: 2 -> 3, No connection
        int[][] edges = {{0, 1}, {2, 3}};
        List<List<Integer>> adj = buildDirectedGraph(4, edges);
        List<List<Integer>> sccs = AdvancedGraphTemplates.tarjanSCC(4, adj);
        
        assertEquals(4, sccs.size()); // Each node separate
    }
    
    @Test
    @DisplayName("Tarjan SCC: Complex graph")
    void testTarjanSCC_ComplexGraph() {
        // Classic example with 3 SCCs
        // SCC1: {0,1,2}, SCC2: {3,4}, SCC3: {5,6,7}
        int[][] edges = {
            {0, 1}, {1, 2}, {2, 0},  // SCC1
            {2, 3}, {3, 4}, {4, 3},  // SCC2
            {4, 5}, {5, 6}, {6, 7}, {7, 5}  // SCC3
        };
        List<List<Integer>> adj = buildDirectedGraph(8, edges);
        List<List<Integer>> sccs = AdvancedGraphTemplates.tarjanSCC(8, adj);
        
        assertEquals(3, sccs.size());
    }

    // ============================================================================
    // BRIDGES TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Bridges: Single edge (is a bridge)")
    void testBridges_SingleEdge() {
        int[][] edges = {{0, 1}};
        List<List<Integer>> adj = buildUndirectedGraph(2, edges);
        List<List<Integer>> bridges = AdvancedGraphTemplates.findBridges(2, adj);
        
        assertEquals(1, bridges.size());
        assertTrue(containsEdge(bridges, 0, 1));
    }
    
    @Test
    @DisplayName("Bridges: Simple cycle (no bridges)")
    void testBridges_SimpleCycle() {
        // Triangle: 0-1-2-0
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        List<List<Integer>> adj = buildUndirectedGraph(3, edges);
        List<List<Integer>> bridges = AdvancedGraphTemplates.findBridges(3, adj);
        
        assertEquals(0, bridges.size());
    }
    
    @Test
    @DisplayName("Bridges: Tree (all edges are bridges)")
    void testBridges_Tree() {
        // Tree: 0-1, 0-2, 1-3
        int[][] edges = {{0, 1}, {0, 2}, {1, 3}};
        List<List<Integer>> adj = buildUndirectedGraph(4, edges);
        List<List<Integer>> bridges = AdvancedGraphTemplates.findBridges(4, adj);
        
        assertEquals(3, bridges.size());
    }
    
    @Test
    @DisplayName("Bridges: Mixed (some bridges, some not)")
    void testBridges_Mixed() {
        // 0-1-2-3 with 1-2-4 cycle, so 0-1 and 2-3 are bridges
        //     0---1
        //         |\ 
        //         2-4
        //         |
        //         3
        int[][] edges = {{0, 1}, {1, 2}, {2, 4}, {4, 1}, {2, 3}};
        List<List<Integer>> adj = buildUndirectedGraph(5, edges);
        List<List<Integer>> bridges = AdvancedGraphTemplates.findBridges(5, adj);
        
        assertEquals(2, bridges.size());
        assertTrue(containsEdge(bridges, 0, 1));
        assertTrue(containsEdge(bridges, 2, 3));
    }
    
    @Test
    @DisplayName("Bridges: Disconnected graph")
    void testBridges_Disconnected() {
        // Two components: 0-1 and 2-3
        int[][] edges = {{0, 1}, {2, 3}};
        List<List<Integer>> adj = buildUndirectedGraph(4, edges);
        List<List<Integer>> bridges = AdvancedGraphTemplates.findBridges(4, adj);
        
        assertEquals(2, bridges.size());
    }

    // ============================================================================
    // ARTICULATION POINTS TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Articulation Points: Single node")
    void testArticulationPoints_SingleNode() {
        List<List<Integer>> adj = buildUndirectedGraph(1, new int[][]{});
        List<Integer> aps = AdvancedGraphTemplates.findArticulationPoints(1, adj);
        
        assertEquals(0, aps.size());
    }
    
    @Test
    @DisplayName("Articulation Points: Simple edge")
    void testArticulationPoints_SimpleEdge() {
        int[][] edges = {{0, 1}};
        List<List<Integer>> adj = buildUndirectedGraph(2, edges);
        List<Integer> aps = AdvancedGraphTemplates.findArticulationPoints(2, adj);
        
        assertEquals(0, aps.size()); // Neither is articulation point
    }
    
    @Test
    @DisplayName("Articulation Points: Chain")
    void testArticulationPoints_Chain() {
        // 0-1-2-3: nodes 1 and 2 are articulation points
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}};
        List<List<Integer>> adj = buildUndirectedGraph(4, edges);
        List<Integer> aps = AdvancedGraphTemplates.findArticulationPoints(4, adj);
        
        assertEquals(2, aps.size());
        assertTrue(aps.contains(1));
        assertTrue(aps.contains(2));
    }
    
    @Test
    @DisplayName("Articulation Points: Star graph")
    void testArticulationPoints_Star() {
        // Center node 0 connected to 1, 2, 3
        int[][] edges = {{0, 1}, {0, 2}, {0, 3}};
        List<List<Integer>> adj = buildUndirectedGraph(4, edges);
        List<Integer> aps = AdvancedGraphTemplates.findArticulationPoints(4, adj);
        
        assertEquals(1, aps.size());
        assertTrue(aps.contains(0)); // Center is articulation point
    }
    
    @Test
    @DisplayName("Articulation Points: Cycle (none)")
    void testArticulationPoints_Cycle() {
        // 0-1-2-3-0: no articulation points
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 0}};
        List<List<Integer>> adj = buildUndirectedGraph(4, edges);
        List<Integer> aps = AdvancedGraphTemplates.findArticulationPoints(4, adj);
        
        assertEquals(0, aps.size());
    }
    
    @Test
    @DisplayName("Articulation Points: Complex graph")
    void testArticulationPoints_Complex() {
        // Bridge between two triangles: node 1 is articulation point
        // Triangle 1: 0-1-2-0, Triangle 2: 1-3-4-1
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}, {1, 3}, {3, 4}, {4, 1}};
        List<List<Integer>> adj = buildUndirectedGraph(5, edges);
        List<Integer> aps = AdvancedGraphTemplates.findArticulationPoints(5, adj);
        
        assertTrue(aps.contains(1));
    }

    // ============================================================================
    // KOSARAJU'S SCC TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Kosaraju SCC: Matches Tarjan on simple cycle")
    void testKosarajuSCC_SimpleCycle() {
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        List<List<Integer>> adj = buildDirectedGraph(3, edges);
        
        List<List<Integer>> tarjanSCCs = AdvancedGraphTemplates.tarjanSCC(3, adj);
        List<List<Integer>> kosarajuSCCs = AdvancedGraphTemplates.kosarajuSCC(3, adj);
        
        assertTrue(areSCCsEqual(tarjanSCCs, kosarajuSCCs));
    }
    
    @Test
    @DisplayName("Kosaraju SCC: Matches Tarjan on complex graph")
    void testKosarajuSCC_ComplexGraph() {
        int[][] edges = {
            {0, 1}, {1, 2}, {2, 0},
            {2, 3}, {3, 4}, {4, 3},
            {4, 5}, {5, 6}, {6, 7}, {7, 5}
        };
        List<List<Integer>> adj = buildDirectedGraph(8, edges);
        
        List<List<Integer>> tarjanSCCs = AdvancedGraphTemplates.tarjanSCC(8, adj);
        List<List<Integer>> kosarajuSCCs = AdvancedGraphTemplates.kosarajuSCC(8, adj);
        
        assertTrue(areSCCsEqual(tarjanSCCs, kosarajuSCCs));
    }
    
    @Test
    @DisplayName("Kosaraju SCC: Single node")
    void testKosarajuSCC_SingleNode() {
        List<List<Integer>> adj = buildDirectedGraph(1, new int[][]{});
        List<List<Integer>> sccs = AdvancedGraphTemplates.kosarajuSCC(1, adj);
        
        assertEquals(1, sccs.size());
        assertTrue(sccs.get(0).contains(0));
    }

    // ============================================================================
    // EULERIAN PATH/CIRCUIT TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Eulerian: Simple circuit exists")
    void testEulerian_SimpleCircuit() {
        // 0->1->2->0
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        
        assertTrue(AdvancedGraphTemplates.hasEulerianPath(3, edges));
        
        List<Integer> path = AdvancedGraphTemplates.findEulerianPath(3, edges);
        assertEquals(4, path.size()); // 3 edges + 1 = 4 nodes
    }
    
    @Test
    @DisplayName("Eulerian: Simple path exists")
    void testEulerian_SimplePath() {
        // 0->1->2 (path, not circuit)
        int[][] edges = {{0, 1}, {1, 2}};
        
        assertTrue(AdvancedGraphTemplates.hasEulerianPath(3, edges));
        
        List<Integer> path = AdvancedGraphTemplates.findEulerianPath(3, edges);
        assertEquals(3, path.size());
        assertEquals(0, path.get(0));
        assertEquals(2, path.get(2));
    }
    
    @Test
    @DisplayName("Eulerian: No path exists")
    void testEulerian_NoPath() {
        // 0->1, 0->2 (vertex 0 has out-degree 2, others have 0)
        int[][] edges = {{0, 1}, {0, 2}};
        
        assertFalse(AdvancedGraphTemplates.hasEulerianPath(3, edges));
    }
    
    @Test
    @DisplayName("Eulerian: Single edge")
    void testEulerian_SingleEdge() {
        int[][] edges = {{0, 1}};
        
        assertTrue(AdvancedGraphTemplates.hasEulerianPath(2, edges));
        
        List<Integer> path = AdvancedGraphTemplates.findEulerianPath(2, edges);
        assertEquals(2, path.size());
    }
    
    @Test
    @DisplayName("Eulerian: Complex circuit")
    void testEulerian_ComplexCircuit() {
        // Pentagon circuit
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 0}};
        
        assertTrue(AdvancedGraphTemplates.hasEulerianPath(5, edges));
        
        List<Integer> path = AdvancedGraphTemplates.findEulerianPath(5, edges);
        assertEquals(6, path.size());
    }

    // ============================================================================
    // 2-SAT TESTS
    // ============================================================================
    
    @Test
    @DisplayName("2-SAT: Trivially satisfiable")
    void test2SAT_TriviallySatisfiable() {
        // (x0 OR x1)
        List<int[]> clauses = new ArrayList<>();
        clauses.add(new int[]{0, 2}); // x0=0*2, x1=1*2
        
        assertTrue(AdvancedGraphTemplates.solve2SAT(2, clauses));
    }
    
    @Test
    @DisplayName("2-SAT: Simple satisfiable")
    void test2SAT_SimpleSatisfiable() {
        // (x0 OR x1) AND (¬x0 OR x1)
        // x0=0, x1=2, ¬x0=1, ¬x1=3
        List<int[]> clauses = new ArrayList<>();
        clauses.add(new int[]{0, 2}); // x0 OR x1
        clauses.add(new int[]{1, 2}); // ¬x0 OR x1
        
        assertTrue(AdvancedGraphTemplates.solve2SAT(2, clauses));
    }
    
    @Test
    @DisplayName("2-SAT: Unsatisfiable")
    void test2SAT_Unsatisfiable() {
        // (x0 OR x1) AND (x0 OR ¬x1) AND (¬x0 OR x1) AND (¬x0 OR ¬x1)
        // This is unsatisfiable
        List<int[]> clauses = new ArrayList<>();
        clauses.add(new int[]{0, 2}); // x0 OR x1
        clauses.add(new int[]{0, 3}); // x0 OR ¬x1
        clauses.add(new int[]{1, 2}); // ¬x0 OR x1
        clauses.add(new int[]{1, 3}); // ¬x0 OR ¬x1
        
        assertFalse(AdvancedGraphTemplates.solve2SAT(2, clauses));
    }
    
    @Test
    @DisplayName("2-SAT: Complex satisfiable formula")
    void test2SAT_ComplexSatisfiable() {
        // More complex but satisfiable formula
        List<int[]> clauses = new ArrayList<>();
        clauses.add(new int[]{0, 2}); // x0 OR x1
        clauses.add(new int[]{1, 4}); // ¬x0 OR x2
        clauses.add(new int[]{3, 4}); // ¬x1 OR x2
        
        assertTrue(AdvancedGraphTemplates.solve2SAT(3, clauses));
    }

    // ============================================================================
    // BICONNECTED COMPONENTS TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Biconnected: Single edge")
    void testBiconnected_SingleEdge() {
        int[][] edges = {{0, 1}};
        List<List<int[]>> components = AdvancedGraphTemplates.findBiconnectedComponents(2, edges);
        
        assertEquals(1, components.size());
        assertEquals(1, components.get(0).size());
    }
    
    @Test
    @DisplayName("Biconnected: Triangle (single component)")
    void testBiconnected_Triangle() {
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        List<List<int[]>> components = AdvancedGraphTemplates.findBiconnectedComponents(3, edges);
        
        assertEquals(1, components.size());
        assertEquals(3, components.get(0).size()); // 3 edges in component
    }
    
    @Test
    @DisplayName("Biconnected: Chain (multiple components)")
    void testBiconnected_Chain() {
        // 0-1-2-3: each edge is its own biconnected component
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}};
        List<List<int[]>> components = AdvancedGraphTemplates.findBiconnectedComponents(4, edges);
        
        assertEquals(3, components.size());
    }
    
    @Test
    @DisplayName("Biconnected: Complex graph")
    void testBiconnected_ComplexGraph() {
        // Two triangles sharing a vertex: 0-1-2-0 and 2-3-4-2
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}, {2, 3}, {3, 4}, {4, 2}};
        List<List<int[]>> components = AdvancedGraphTemplates.findBiconnectedComponents(5, edges);
        
        assertEquals(2, components.size());
    }

    // ============================================================================
    // PERFORMANCE TESTS (Large Graphs)
    // ============================================================================
    
    @Test
    @DisplayName("Performance: Large graph with 1000 nodes")
    void testPerformance_LargeGraph() {
        int n = 1000;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        
        // Create a chain graph
        for (int i = 0; i < n - 1; i++) {
            adj.get(i).add(i + 1);
        }
        
        long start = System.currentTimeMillis();
        List<List<Integer>> sccs = AdvancedGraphTemplates.tarjanSCC(n, adj);
        long end = System.currentTimeMillis();
        
        assertEquals(n, sccs.size()); // Each node is its own SCC
        assertTrue(end - start < 1000, "Should complete in under 1 second");
    }
    
    @Test
    @DisplayName("Performance: Dense graph")
    void testPerformance_DenseGraph() {
        int n = 100;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        
        // Create a complete graph (all edges)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    adj.get(i).add(j);
                }
            }
        }
        
        long start = System.currentTimeMillis();
        List<List<Integer>> sccs = AdvancedGraphTemplates.tarjanSCC(n, adj);
        long end = System.currentTimeMillis();
        
        assertEquals(1, sccs.size()); // One big SCC
        assertTrue(end - start < 1000, "Should complete in under 1 second");
    }
}
