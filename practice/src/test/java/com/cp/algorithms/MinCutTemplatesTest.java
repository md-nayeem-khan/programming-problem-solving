package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MinCutTemplates
 * Tests all 4 min-cut algorithm implementations
 */
class MinCutTemplatesTest {

    // ============================================================================
    // 1. STOER-WAGNER ALGORITHM TESTS
    // ============================================================================

    @Test
    @DisplayName("Stoer-Wagner: Simple square graph with diagonal")
    void testStoerWagnerSimpleGraph() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(4);
        sw.addEdge(0, 1, 2);
        sw.addEdge(1, 2, 3);
        sw.addEdge(2, 3, 1);
        sw.addEdge(3, 0, 2);
        sw.addEdge(0, 2, 1);
        
        int minCut = sw.globalMinCut();
        assertEquals(3, minCut, "Min-cut should be 3 (cutting 0-1 and 0-3, or 2-3 and 1-2)");
    }

    @Test
    @DisplayName("Stoer-Wagner: Pentagon graph")
    void testStoerWagnerPentagon() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(5);
        sw.addEdge(0, 1, 1);
        sw.addEdge(1, 2, 1);
        sw.addEdge(2, 3, 1);
        sw.addEdge(3, 4, 1);
        sw.addEdge(4, 0, 1);
        sw.addEdge(1, 3, 2);
        
        int minCut = sw.globalMinCut();
        assertEquals(2, minCut, "Min-cut should be 2");
    }

    @Test
    @DisplayName("Stoer-Wagner: Complete graph K4")
    void testStoerWagnerCompleteGraph() {
        int n = 4;
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                sw.addEdge(i, j, 1);
            }
        }
        
        int minCut = sw.globalMinCut();
        assertEquals(3, minCut, "Min-cut in K4 should be 3 (degree of any vertex)");
    }

    @Test
    @DisplayName("Stoer-Wagner: Single edge (two vertices)")
    void testStoerWagnerTwoVertices() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(2);
        sw.addEdge(0, 1, 5);
        
        int minCut = sw.globalMinCut();
        assertEquals(5, minCut, "Min-cut with single edge should equal edge weight");
    }

    @Test
    @DisplayName("Stoer-Wagner: Triangle graph")
    void testStoerWagnerTriangle() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(3);
        sw.addEdge(0, 1, 1);
        sw.addEdge(1, 2, 1);
        sw.addEdge(2, 0, 1);
        
        int minCut = sw.globalMinCut();
        assertEquals(2, minCut, "Min-cut in triangle should be 2");
    }

    @Test
    @DisplayName("Stoer-Wagner: Disconnected components - handled as single component")
    void testStoerWagnerDisconnected() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(4);
        sw.addEdge(0, 1, 3);
        sw.addEdge(2, 3, 5);
        
        int minCut = sw.globalMinCut();
        assertEquals(0, minCut, "Min-cut for disconnected graph should be 0");
    }

    @Test
    @DisplayName("Stoer-Wagner: Weighted path graph")
    void testStoerWagnerPath() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(5);
        sw.addEdge(0, 1, 10);
        sw.addEdge(1, 2, 5);
        sw.addEdge(2, 3, 3);
        sw.addEdge(3, 4, 7);
        
        int minCut = sw.globalMinCut();
        assertEquals(3, minCut, "Min-cut should be the minimum edge weight");
    }

    @Test
    @DisplayName("Stoer-Wagner: Multiple edges between same vertices")
    void testStoerWagnerMultipleEdges() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(3);
        sw.addEdge(0, 1, 2);
        sw.addEdge(0, 1, 3);  // Another edge between same vertices
        sw.addEdge(1, 2, 4);
        sw.addEdge(2, 0, 1);
        
        int minCut = sw.globalMinCut();
        assertEquals(5, minCut, "Multiple edges should be summed");
    }

    @Test
    @DisplayName("Stoer-Wagner: With partition result")
    void testStoerWagnerWithPartition() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(4);
        sw.addEdge(0, 1, 1);
        sw.addEdge(1, 2, 1);
        sw.addEdge(2, 3, 1);
        sw.addEdge(3, 0, 1);
        
        MinCutTemplates.MinCutResult result = sw.globalMinCutWithPartition();
        assertEquals(2, result.cutValue, "Min-cut value should be 2");
        assertNotNull(result.partition, "Partition should not be null");
        assertFalse(result.partition.isEmpty(), "Partition should not be empty");
        assertTrue(result.partition.size() > 0 && result.partition.size() < 4, 
                   "Partition should contain some but not all vertices");
    }

    @Test
    @DisplayName("Stoer-Wagner: Larger graph (stress test)")
    void testStoerWagnerLargerGraph() {
        int n = 10;
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(n);
        
        // Create a graph with clear bottleneck
        for (int i = 0; i < n/2; i++) {
            for (int j = i + 1; j < n/2; j++) {
                sw.addEdge(i, j, 10);
            }
        }
        for (int i = n/2; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                sw.addEdge(i, j, 10);
            }
        }
        // Bottleneck edge
        sw.addEdge(n/2 - 1, n/2, 1);
        
        int minCut = sw.globalMinCut();
        assertEquals(1, minCut, "Should find bottleneck edge");
    }

    // ============================================================================
    // 2. MAX-FLOW MIN-CUT ALGORITHM TESTS
    // ============================================================================

    @Test
    @DisplayName("MaxFlow-MinCut: Standard flow network")
    void testMaxFlowMinCutStandard() {
        MinCutTemplates.MaxFlowMinCut mf = new MinCutTemplates.MaxFlowMinCut(6);
        mf.addEdge(0, 1, 16);
        mf.addEdge(0, 2, 13);
        mf.addEdge(1, 3, 12);
        mf.addEdge(2, 1, 4);
        mf.addEdge(2, 4, 14);
        mf.addEdge(3, 2, 9);
        mf.addEdge(3, 5, 20);
        mf.addEdge(4, 3, 7);
        mf.addEdge(4, 5, 4);
        
        int maxFlow = mf.maxFlow(0, 5);
        assertEquals(23, maxFlow, "Max flow should be 23");
    }

    @Test
    @DisplayName("MaxFlow-MinCut: Get min-cut edges")
    void testGetMinCutEdges() {
        MinCutTemplates.MaxFlowMinCut mf = new MinCutTemplates.MaxFlowMinCut(6);
        mf.addEdge(0, 1, 16);
        mf.addEdge(0, 2, 13);
        mf.addEdge(1, 3, 12);
        mf.addEdge(2, 1, 4);
        mf.addEdge(2, 4, 14);
        mf.addEdge(3, 2, 9);
        mf.addEdge(3, 5, 20);
        mf.addEdge(4, 3, 7);
        mf.addEdge(4, 5, 4);
        
        mf.maxFlow(0, 5);
        List<int[]> cutEdges = mf.getMinCutEdges(0);
        
        assertNotNull(cutEdges);
        assertTrue(cutEdges.size() > 0, "Should have at least one min-cut edge");
        
        // Verify sum of cut edge capacities equals max flow
        int cutSum = 0;
        for (int[] edge : cutEdges) {
            cutSum += edge[2];
        }
        assertEquals(23, cutSum, "Sum of min-cut edge capacities should equal max flow");
    }

    @Test
    @DisplayName("MaxFlow-MinCut: Simple path")
    void testMaxFlowSimplePath() {
        MinCutTemplates.MaxFlowMinCut mf = new MinCutTemplates.MaxFlowMinCut(4);
        mf.addEdge(0, 1, 10);
        mf.addEdge(1, 2, 5);
        mf.addEdge(2, 3, 15);
        
        int maxFlow = mf.maxFlow(0, 3);
        assertEquals(5, maxFlow, "Max flow should be limited by bottleneck");
    }

    @Test
    @DisplayName("MaxFlow-MinCut: Parallel edges")
    void testMaxFlowParallelEdges() {
        MinCutTemplates.MaxFlowMinCut mf = new MinCutTemplates.MaxFlowMinCut(2);
        mf.addEdge(0, 1, 3);
        mf.addEdge(0, 1, 5);
        
        int maxFlow = mf.maxFlow(0, 1);
        assertEquals(8, maxFlow, "Parallel edges should sum up");
    }

    @Test
    @DisplayName("MaxFlow-MinCut: No path from source to sink")
    void testMaxFlowNoPath() {
        MinCutTemplates.MaxFlowMinCut mf = new MinCutTemplates.MaxFlowMinCut(4);
        mf.addEdge(0, 1, 10);
        mf.addEdge(2, 3, 10);
        
        int maxFlow = mf.maxFlow(0, 3);
        assertEquals(0, maxFlow, "Max flow should be 0 when no path exists");
    }

    @Test
    @DisplayName("MaxFlow-MinCut: Source equals sink")
    void testMaxFlowSourceEqualsSink() {
        MinCutTemplates.MaxFlowMinCut mf = new MinCutTemplates.MaxFlowMinCut(4);
        mf.addEdge(0, 1, 10);
        mf.addEdge(1, 2, 10);
        
        int maxFlow = mf.maxFlow(0, 0);
        assertEquals(0, maxFlow, "Max flow should be 0 when source equals sink");
    }

    @Test
    @DisplayName("MaxFlow-MinCut: Multiple paths")
    void testMaxFlowMultiplePaths() {
        MinCutTemplates.MaxFlowMinCut mf = new MinCutTemplates.MaxFlowMinCut(4);
        mf.addEdge(0, 1, 10);
        mf.addEdge(0, 2, 10);
        mf.addEdge(1, 3, 10);
        mf.addEdge(2, 3, 10);
        
        int maxFlow = mf.maxFlow(0, 3);
        assertEquals(20, maxFlow, "Should use both paths");
    }

    @Test
    @DisplayName("MaxFlow-MinCut: Complex network")
    void testMaxFlowComplexNetwork() {
        MinCutTemplates.MaxFlowMinCut mf = new MinCutTemplates.MaxFlowMinCut(8);
        mf.addEdge(0, 1, 10);
        mf.addEdge(0, 2, 10);
        mf.addEdge(1, 3, 4);
        mf.addEdge(1, 4, 8);
        mf.addEdge(2, 4, 9);
        mf.addEdge(3, 5, 10);
        mf.addEdge(4, 5, 10);
        mf.addEdge(4, 6, 6);
        mf.addEdge(5, 7, 10);
        mf.addEdge(6, 7, 10);
        
        int maxFlow = mf.maxFlow(0, 7);
        assertTrue(maxFlow > 0, "Should find some flow");
        assertTrue(maxFlow <= 20, "Flow cannot exceed source output");
    }

    // ============================================================================
    // 3. GOMORY-HU TREE TESTS
    // ============================================================================

    @Test
    @DisplayName("Gomory-Hu: Simple triangle")
    void testGomoryHuTriangle() {
        int[][] capacity = new int[3][3];
        capacity[0][1] = capacity[1][0] = 1;
        capacity[1][2] = capacity[2][1] = 1;
        capacity[2][0] = capacity[0][2] = 1;
        
        MinCutTemplates.GomoryHuTree gh = new MinCutTemplates.GomoryHuTree(3, capacity);
        
        int cut01 = gh.queryMinCut(0, 1);
        int cut12 = gh.queryMinCut(1, 2);
        int cut02 = gh.queryMinCut(0, 2);
        
        assertEquals(2, cut01, "Min-cut between 0 and 1 should be 2");
        assertEquals(2, cut12, "Min-cut between 1 and 2 should be 2");
        assertEquals(2, cut02, "Min-cut between 0 and 2 should be 2");
    }

    @Test
    @DisplayName("Gomory-Hu: Path graph")
    void testGomoryHuPath() {
        int[][] capacity = new int[4][4];
        capacity[0][1] = capacity[1][0] = 5;
        capacity[1][2] = capacity[2][1] = 3;
        capacity[2][3] = capacity[3][2] = 7;
        
        MinCutTemplates.GomoryHuTree gh = new MinCutTemplates.GomoryHuTree(4, capacity);
        
        int cut03 = gh.queryMinCut(0, 3);
        assertEquals(3, cut03, "Min-cut should be the minimum edge in path");
    }

    @Test
    @DisplayName("Gomory-Hu: Star graph")
    void testGomoryHuStar() {
        int[][] capacity = new int[5][5];
        for (int i = 1; i < 5; i++) {
            capacity[0][i] = capacity[i][0] = i;
        }
        
        MinCutTemplates.GomoryHuTree gh = new MinCutTemplates.GomoryHuTree(5, capacity);
        
        // Min-cut between any two leaves should go through center
        int cut12 = gh.queryMinCut(1, 2);
        assertTrue(cut12 >= 1, "Min-cut should be at least 1");
    }

    @Test
    @DisplayName("Gomory-Hu: Complete graph K4")
    void testGomoryHuCompleteGraph() {
        int[][] capacity = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                capacity[i][j] = capacity[j][i] = 1;
            }
        }
        
        MinCutTemplates.GomoryHuTree gh = new MinCutTemplates.GomoryHuTree(4, capacity);
        
        int cut01 = gh.queryMinCut(0, 1);
        assertEquals(3, cut01, "Min-cut in K4 should be 3");
    }

    // ============================================================================
    // 4. KARGER'S RANDOMIZED MIN-CUT TESTS
    // ============================================================================

    @Test
    @DisplayName("Karger: Simple square graph")
    void testKargerSimpleGraph() {
        List<MinCutTemplates.KargerMinCut.Edge> edges = new ArrayList<>();
        edges.add(new MinCutTemplates.KargerMinCut.Edge(0, 1, 2));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(1, 2, 3));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(2, 3, 1));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(3, 0, 2));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(0, 2, 1));
        
        int minCut = MinCutTemplates.KargerMinCut.kargerMinCut(4, edges);
        // Karger is randomized, but with enough iterations should find correct answer
        assertTrue(minCut >= 3 && minCut <= 4, "Should find min-cut close to 3");
    }

    @Test
    @DisplayName("Karger: Triangle graph")
    void testKargerTriangle() {
        List<MinCutTemplates.KargerMinCut.Edge> edges = new ArrayList<>();
        edges.add(new MinCutTemplates.KargerMinCut.Edge(0, 1, 1));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(1, 2, 1));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(2, 0, 1));
        
        int minCut = MinCutTemplates.KargerMinCut.kargerMinCut(3, edges);
        assertTrue(minCut >= 2 && minCut <= 3, "Should find min-cut close to 2");
    }

    @Test
    @DisplayName("Karger: Single edge")
    void testKargerSingleEdge() {
        List<MinCutTemplates.KargerMinCut.Edge> edges = new ArrayList<>();
        edges.add(new MinCutTemplates.KargerMinCut.Edge(0, 1, 5));
        
        int minCut = MinCutTemplates.KargerMinCut.kargerMinCut(2, edges);
        assertEquals(5, minCut, "Should find the single edge");
    }

    @Test
    @DisplayName("Karger: Path graph")
    void testKargerPath() {
        List<MinCutTemplates.KargerMinCut.Edge> edges = new ArrayList<>();
        edges.add(new MinCutTemplates.KargerMinCut.Edge(0, 1, 10));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(1, 2, 5));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(2, 3, 3));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(3, 4, 7));
        
        int minCut = MinCutTemplates.KargerMinCut.kargerMinCut(5, edges);
        assertTrue(minCut >= 3 && minCut <= 5, "Should find minimum edge weight");
    }

    // ============================================================================
    // 5. EDGE CASE TESTS
    // ============================================================================

    @Test
    @DisplayName("Edge Case: Single vertex graph")
    void testSingleVertex() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(1);
        int minCut = sw.globalMinCut();
        assertEquals(Integer.MAX_VALUE, minCut, "Single vertex has no cut");
    }

    @Test
    @DisplayName("Edge Case: Zero weight edges")
    void testZeroWeightEdges() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(3);
        sw.addEdge(0, 1, 0);
        sw.addEdge(1, 2, 1);
        sw.addEdge(2, 0, 1);
        
        int minCut = sw.globalMinCut();
        // Min-cut separates one vertex from others. Vertex 1 has edges (0,1,0) and (1,2,1) = total 1
        assertEquals(1, minCut, "Min-cut should consider total edge weight of separated vertex");
    }

    @Test
    @DisplayName("Edge Case: Large weights")
    void testLargeWeights() {
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(3);
        sw.addEdge(0, 1, 1000000);
        sw.addEdge(1, 2, 1000000);
        sw.addEdge(2, 0, 1);
        
        int minCut = sw.globalMinCut();
        // Min-cut separates vertex 2 from others: edges (2,0,1) + (2,1,1000000) = 1000001
        assertEquals(1000001, minCut, "Should handle large weights correctly");
    }

    // ============================================================================
    // 6. CONSISTENCY TESTS (Compare algorithms)
    // ============================================================================

    @Test
    @DisplayName("Consistency: Stoer-Wagner vs Karger on same graph")
    void testStoerWagnerVsKarger() {
        // Create graph for Stoer-Wagner
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(4);
        sw.addEdge(0, 1, 1);
        sw.addEdge(1, 2, 1);
        sw.addEdge(2, 3, 1);
        sw.addEdge(3, 0, 1);
        
        int swResult = sw.globalMinCut();
        
        // Create same graph for Karger
        List<MinCutTemplates.KargerMinCut.Edge> edges = new ArrayList<>();
        edges.add(new MinCutTemplates.KargerMinCut.Edge(0, 1, 1));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(1, 2, 1));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(2, 3, 1));
        edges.add(new MinCutTemplates.KargerMinCut.Edge(3, 0, 1));
        
        int kargerResult = MinCutTemplates.KargerMinCut.kargerMinCut(4, edges);
        
        // Karger is probabilistic, allow small error margin
        assertTrue(Math.abs(swResult - kargerResult) <= 1, 
                   "Stoer-Wagner and Karger should give similar results");
    }

    // ============================================================================
    // 7. PERFORMANCE AND STRESS TESTS
    // ============================================================================

    @Test
    @DisplayName("Performance: Medium-sized complete graph")
    void testPerformanceMediumGraph() {
        int n = 20;
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(n);
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                sw.addEdge(i, j, 1);
            }
        }
        
        long start = System.currentTimeMillis();
        int minCut = sw.globalMinCut();
        long end = System.currentTimeMillis();
        
        assertEquals(n - 1, minCut, "Min-cut in complete graph should be n-1");
        assertTrue(end - start < 5000, "Should complete in reasonable time");
    }

    @Test
    @DisplayName("Correctness: Verify DSU in Karger's algorithm")
    void testKargerDSU() {
        MinCutTemplates.KargerMinCut.DSU dsu = new MinCutTemplates.KargerMinCut.DSU(5);
        
        assertFalse(dsu.connected(0, 1));
        
        dsu.union(0, 1);
        assertTrue(dsu.connected(0, 1));
        
        dsu.union(2, 3);
        assertTrue(dsu.connected(2, 3));
        assertFalse(dsu.connected(0, 2));
        
        dsu.union(1, 2);
        assertTrue(dsu.connected(0, 3));
    }

    // ============================================================================
    // 8. REAL-WORLD SCENARIO TESTS
    // ============================================================================

    @Test
    @DisplayName("Scenario: Network reliability (find weak link)")
    void testNetworkReliability() {
        // Network with one weak link
        MinCutTemplates.StoerWagner sw = new MinCutTemplates.StoerWagner(6);
        
        // Strong connections in subnet 1
        sw.addEdge(0, 1, 100);
        sw.addEdge(1, 2, 100);
        sw.addEdge(2, 0, 100);
        
        // Strong connections in subnet 2
        sw.addEdge(3, 4, 100);
        sw.addEdge(4, 5, 100);
        sw.addEdge(5, 3, 100);
        
        // Weak link between subnets
        sw.addEdge(2, 3, 1);
        
        int minCut = sw.globalMinCut();
        assertEquals(1, minCut, "Should identify the weak link");
    }

    @Test
    @DisplayName("Scenario: Max flow in supply chain")
    void testSupplyChainMaxFlow() {
        MinCutTemplates.MaxFlowMinCut mf = new MinCutTemplates.MaxFlowMinCut(5);
        
        // Source (factory) to warehouses
        mf.addEdge(0, 1, 20);
        mf.addEdge(0, 2, 30);
        
        // Warehouses to distribution centers
        mf.addEdge(1, 3, 15);
        mf.addEdge(2, 3, 25);
        
        // Distribution to destination
        mf.addEdge(3, 4, 35);
        
        int maxFlow = mf.maxFlow(0, 4);
        // Bottleneck is at node 3->4 with capacity 35 (even though 40 can reach node 3)
        assertEquals(35, maxFlow, "Should find maximum throughput limited by bottleneck");
    }
}
