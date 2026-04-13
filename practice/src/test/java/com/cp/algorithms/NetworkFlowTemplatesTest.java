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
 * Comprehensive tests for NetworkFlowTemplates
 * 
 * Test Coverage:
 * 1. Ford-Fulkerson (Edmonds-Karp) - Basic and complex flow networks
 * 2. Dinic's Algorithm - Performance and correctness
 * 3. Minimum Cut - Edge detection in min-cut
 * 4. Bipartite Matching - Maximum matching and actual pairs
 * 5. Hungarian Algorithm - Min-cost assignment
 * 6. Min-Cost Max-Flow - MCMF with costs
 * 7. Edge cases - Empty graphs, single nodes, disconnected components
 */
@DisplayName("Network Flow Templates Validation Tests")
class NetworkFlowTemplatesTest {

    // ========== FORD-FULKERSON TESTS ==========
    
    @Test
    @DisplayName("Ford-Fulkerson: Basic flow network from CLRS textbook")
    void testFordFulkersonBasic() {
        // Classic example from Introduction to Algorithms (CLRS)
        int[][] capacity = {
            {0, 16, 13, 0, 0, 0},
            {0, 0, 10, 12, 0, 0},
            {0, 4, 0, 0, 14, 0},
            {0, 0, 9, 0, 0, 20},
            {0, 0, 0, 7, 0, 4},
            {0, 0, 0, 0, 0, 0}
        };
        
        int maxFlow = NetworkFlowTemplates.fordFulkerson(capacity, 0, 5);
        assertEquals(23, maxFlow, "Max flow should be 23");
    }
    
    @Test
    @DisplayName("Ford-Fulkerson: Simple serial network")
    void testFordFulkersonSerial() {
        // Linear path: 0 --10--> 1 --5--> 2
        // Max flow limited by minimum capacity (5)
        int[][] capacity = {
            {0, 10, 0},
            {0, 0, 5},
            {0, 0, 0}
        };
        
        int maxFlow = NetworkFlowTemplates.fordFulkerson(capacity, 0, 2);
        assertEquals(5, maxFlow, "Max flow should be limited by bottleneck capacity");
    }
    
    @Test
    @DisplayName("Ford-Fulkerson: Parallel paths")
    void testFordFulkersonParallel() {
        // Two parallel paths from 0 to 2
        // 0 --10--> 1 --10--> 3
        // 0 --5---> 2 --5---> 3
        int[][] capacity = {
            {0, 10, 5, 0},
            {0, 0, 0, 10},
            {0, 0, 0, 5},
            {0, 0, 0, 0}
        };
        
        int maxFlow = NetworkFlowTemplates.fordFulkerson(capacity, 0, 3);
        assertEquals(15, maxFlow, "Max flow should be sum of parallel paths");
    }
    
    @Test
    @DisplayName("Ford-Fulkerson: No path from source to sink")
    void testFordFulkersonNoPath() {
        // Disconnected: source and sink not connected
        int[][] capacity = {
            {0, 10, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        
        int maxFlow = NetworkFlowTemplates.fordFulkerson(capacity, 0, 2);
        assertEquals(0, maxFlow, "No flow when source and sink are disconnected");
    }
    
    @Test
    @DisplayName("Ford-Fulkerson: Source equals sink")
    void testFordFulkersonSourceEqualsSink() {
        int[][] capacity = {
            {0, 10},
            {0, 0}
        };
        
        int maxFlow = NetworkFlowTemplates.fordFulkerson(capacity, 0, 0);
        assertEquals(0, maxFlow, "Flow should be 0 when source equals sink");
    }
    
    @Test
    @DisplayName("Ford-Fulkerson: Complex network with multiple bottlenecks")
    void testFordFulkersonComplexNetwork() {
        // Network with multiple potential paths and bottlenecks
        int[][] capacity = {
            {0, 10, 10, 0, 0, 0},
            {0, 0, 2, 4, 8, 0},
            {0, 0, 0, 0, 9, 0},
            {0, 0, 0, 0, 0, 10},
            {0, 0, 0, 6, 0, 10},
            {0, 0, 0, 0, 0, 0}
        };
        
        int maxFlow = NetworkFlowTemplates.fordFulkerson(capacity, 0, 5);
        assertTrue(maxFlow > 0, "Should find a valid flow");
        assertTrue(maxFlow <= 20, "Flow should not exceed source capacity");
    }

    // ========== DINIC'S ALGORITHM TESTS ==========
    
    @Test
    @DisplayName("Dinic: Basic flow network (same as Ford-Fulkerson)")
    void testDinicBasic() {
        int n = 6;
        @SuppressWarnings("unchecked")
        List<NetworkFlowTemplates.Edge>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        NetworkFlowTemplates.addEdge(graph, 0, 1, 16);
        NetworkFlowTemplates.addEdge(graph, 0, 2, 13);
        NetworkFlowTemplates.addEdge(graph, 1, 2, 10);
        NetworkFlowTemplates.addEdge(graph, 1, 3, 12);
        NetworkFlowTemplates.addEdge(graph, 2, 1, 4);
        NetworkFlowTemplates.addEdge(graph, 2, 4, 14);
        NetworkFlowTemplates.addEdge(graph, 3, 2, 9);
        NetworkFlowTemplates.addEdge(graph, 3, 5, 20);
        NetworkFlowTemplates.addEdge(graph, 4, 3, 7);
        NetworkFlowTemplates.addEdge(graph, 4, 5, 4);
        
        long maxFlow = NetworkFlowTemplates.dinic(graph, n, 0, 5);
        assertEquals(23, maxFlow, "Dinic should produce same result as Ford-Fulkerson");
    }
    
    @Test
    @DisplayName("Dinic: Large capacity flow")
    void testDinicLargeCapacity() {
        int n = 4;
        @SuppressWarnings("unchecked")
        List<NetworkFlowTemplates.Edge>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // Test with large capacity values
        NetworkFlowTemplates.addEdge(graph, 0, 1, 1_000_000_000L);
        NetworkFlowTemplates.addEdge(graph, 0, 2, 1_000_000_000L);
        NetworkFlowTemplates.addEdge(graph, 1, 3, 1_000_000_000L);
        NetworkFlowTemplates.addEdge(graph, 2, 3, 1_000_000_000L);
        
        long maxFlow = NetworkFlowTemplates.dinic(graph, n, 0, 3);
        assertEquals(2_000_000_000L, maxFlow, "Should handle large capacity values");
    }
    
    @Test
    @DisplayName("Dinic: Single path network")
    void testDinicSinglePath() {
        int n = 3;
        @SuppressWarnings("unchecked")
        List<NetworkFlowTemplates.Edge>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        NetworkFlowTemplates.addEdge(graph, 0, 1, 100);
        NetworkFlowTemplates.addEdge(graph, 1, 2, 50);
        
        long maxFlow = NetworkFlowTemplates.dinic(graph, n, 0, 2);
        assertEquals(50, maxFlow, "Flow limited by minimum capacity edge");
    }
    
    @Test
    @DisplayName("Dinic: No edges")
    void testDinicNoEdges() {
        int n = 3;
        @SuppressWarnings("unchecked")
        List<NetworkFlowTemplates.Edge>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        long maxFlow = NetworkFlowTemplates.dinic(graph, n, 0, 2);
        assertEquals(0, maxFlow, "No flow when no edges exist");
    }

    // ========== MINIMUM CUT TESTS ==========
    
    @Test
    @DisplayName("MinCut: Basic min-cut detection")
    void testMinCutBasic() {
        int[][] capacity = {
            {0, 10, 10, 0},
            {0, 0, 0, 10},
            {0, 0, 0, 10},
            {0, 0, 0, 0}
        };
        
        List<int[]> minCut = NetworkFlowTemplates.findMinCut(capacity, 0, 3);
        
        assertNotNull(minCut, "MinCut should not be null");
        assertFalse(minCut.isEmpty(), "MinCut should contain edges");
        
        // Verify min-cut edges
        int totalCapacity = 0;
        for (int[] edge : minCut) {
            int u = edge[0];
            int v = edge[1];
            totalCapacity += capacity[u][v];
        }
        
        assertEquals(20, totalCapacity, "Total capacity of min-cut should equal max flow");
    }
    
    @Test
    @DisplayName("MinCut: Single bottleneck edge")
    void testMinCutBottleneck() {
        // 0 --100--> 1 --5--> 2
        // Min-cut should be the edge 1->2 with capacity 5
        int[][] capacity = {
            {0, 100, 0},
            {0, 0, 5},
            {0, 0, 0}
        };
        
        List<int[]> minCut = NetworkFlowTemplates.findMinCut(capacity, 0, 2);
        
        assertEquals(1, minCut.size(), "Should have exactly one edge in min-cut");
        
        int[] edge = minCut.get(0);
        assertEquals(1, edge[0], "Min-cut edge should start from node 1");
        assertEquals(2, edge[1], "Min-cut edge should end at node 2");
    }
    
    @Test
    @DisplayName("MinCut: No path - empty cut")
    void testMinCutNoPath() {
        int[][] capacity = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        
        List<int[]> minCut = NetworkFlowTemplates.findMinCut(capacity, 0, 2);
        
        assertTrue(minCut.isEmpty(), "MinCut should be empty when no path exists");
    }

    // ========== BIPARTITE MATCHING TESTS ==========
    
    @Test
    @DisplayName("BipartiteMatching: Perfect matching")
    void testBipartiteMatchingPerfect() {
        // 3 workers, 3 jobs, perfect matching exists
        int[][] edges = {
            {0, 0}, {0, 1},  // Worker 0 can do jobs 0, 1
            {1, 0}, {1, 2},  // Worker 1 can do jobs 0, 2
            {2, 1}, {2, 2}   // Worker 2 can do jobs 1, 2
        };
        
        int matching = NetworkFlowTemplates.maxBipartiteMatching(3, 3, edges);
        assertEquals(3, matching, "Should find perfect matching");
    }
    
    @Test
    @DisplayName("BipartiteMatching: Partial matching")
    void testBipartiteMatchingPartial() {
        // 3 workers, 2 jobs
        int[][] edges = {
            {0, 0},
            {1, 0},
            {2, 1}
        };
        
        int matching = NetworkFlowTemplates.maxBipartiteMatching(3, 2, edges);
        assertEquals(2, matching, "Maximum matching limited by smaller partition");
    }
    
    @Test
    @DisplayName("BipartiteMatching: No edges")
    void testBipartiteMatchingNoEdges() {
        int[][] edges = {};
        
        int matching = NetworkFlowTemplates.maxBipartiteMatching(3, 3, edges);
        assertEquals(0, matching, "No matching when no edges");
    }
    
    @Test
    @DisplayName("BipartiteMatching: Find actual matching pairs")
    void testFindBipartiteMatching() {
        int[][] edges = {
            {0, 0}, {0, 1},
            {1, 0}, {1, 2},
            {2, 1}, {2, 2}
        };
        
        List<int[]> matching = NetworkFlowTemplates.findBipartiteMatching(3, 3, edges);
        
        assertEquals(3, matching.size(), "Should find 3 matching pairs");
        
        // Verify each left node appears at most once
        Set<Integer> leftNodes = new HashSet<>();
        Set<Integer> rightNodes = new HashSet<>();
        
        for (int[] pair : matching) {
            assertFalse(leftNodes.contains(pair[0]), "Left node should not be matched twice");
            assertFalse(rightNodes.contains(pair[1]), "Right node should not be matched twice");
            leftNodes.add(pair[0]);
            rightNodes.add(pair[1]);
        }
    }
    
    @Test
    @DisplayName("BipartiteMatching: Unbalanced partitions")
    void testBipartiteMatchingUnbalanced() {
        // More jobs than workers
        int[][] edges = {
            {0, 0}, {0, 1}, {0, 2},
            {1, 1}, {1, 3}
        };
        
        int matching = NetworkFlowTemplates.maxBipartiteMatching(2, 4, edges);
        assertEquals(2, matching, "Matching limited by smaller partition");
    }

    // ========== HUNGARIAN ALGORITHM TESTS ==========
    
    @Test
    @DisplayName("Hungarian: Basic min-cost assignment")
    void testHungarianBasic() {
        int[][] cost = {
            {10, 20, 30},
            {15, 25, 35},
            {20, 30, 40}
        };
        
        // Optimal: Worker 0->Job 0 (10), Worker 1->Job 1 (25), Worker 2->Job 2 (40)
        // Total: 75
        int minCost = NetworkFlowTemplates.hungarianAlgorithm(cost);
        assertEquals(75, minCost, "Should find minimum cost assignment");
    }
    
    @Test
    @DisplayName("Hungarian: Identical costs - any assignment valid")
    void testHungarianIdenticalCosts() {
        int[][] cost = {
            {5, 5, 5},
            {5, 5, 5},
            {5, 5, 5}
        };
        
        int minCost = NetworkFlowTemplates.hungarianAlgorithm(cost);
        assertEquals(15, minCost, "All assignments have same cost");
    }
    
    @Test
    @DisplayName("Hungarian: Single optimal assignment")
    void testHungarianSingleOptimal() {
        int[][] cost = {
            {1, 100, 100},
            {100, 2, 100},
            {100, 100, 3}
        };
        
        int minCost = NetworkFlowTemplates.hungarianAlgorithm(cost);
        assertEquals(6, minCost, "Should select diagonal (1+2+3)");
    }
    
    @Test
    @DisplayName("Hungarian: Get actual assignment")
    void testHungarianAssignment() {
        int[][] cost = {
            {10, 20, 30},
            {15, 25, 35},
            {20, 30, 40}
        };
        
        int[] assignment = NetworkFlowTemplates.hungarianAssignment(cost);
        
        assertEquals(3, assignment.length, "Assignment array should have n elements");
        
        // Verify assignment is a valid permutation
        Set<Integer> jobs = new HashSet<>();
        for (int job : assignment) {
            assertTrue(job >= 0 && job < 3, "Job index should be valid");
            assertFalse(jobs.contains(job), "Each job assigned to exactly one worker");
            jobs.add(job);
        }
        
        // Calculate total cost
        int totalCost = 0;
        for (int worker = 0; worker < 3; worker++) {
            totalCost += cost[worker][assignment[worker]];
        }
        assertEquals(75, totalCost, "Assignment should have minimum cost");
    }
    
    @Test
    @DisplayName("Hungarian: 2x2 matrix")
    void testHungarian2x2() {
        int[][] cost = {
            {1, 2},
            {3, 4}
        };
        
        // Optimal: 0->0 (1) and 1->1 (4) = 5
        // Or: 0->1 (2) and 1->0 (3) = 5
        int minCost = NetworkFlowTemplates.hungarianAlgorithm(cost);
        assertEquals(5, minCost);
    }
    
    @Test
    @DisplayName("Hungarian: Max weight matching conversion")
    void testMaxWeightBipartiteMatching() {
        int[][] weight = {
            {10, 5, 1},
            {8, 12, 7},
            {9, 6, 11}
        };
        
        // Maximum weight matching
        int maxWeight = NetworkFlowTemplates.maxWeightBipartiteMatching(weight);
        
        // Optimal: 0->0 (10), 1->1 (12), 2->2 (11) = 33
        assertEquals(33, maxWeight, "Should maximize total weight");
    }

    // ========== MIN-COST MAX-FLOW TESTS ==========
    
    @Test
    @DisplayName("MCMF: Basic min-cost max-flow")
    void testMinCostMaxFlowBasic() {
        int n = 4;
        int[][] edges = {
            {0, 1, 10, 2},  // from, to, capacity, cost
            {0, 2, 10, 4},
            {1, 2, 2, 1},
            {1, 3, 4, 2},
            {2, 3, 10, 3}
        };
        
        long[] result = NetworkFlowTemplates.minCostMaxFlow(n, 0, 3, edges);
        long maxFlow = result[0];
        long minCost = result[1];
        
        assertTrue(maxFlow > 0, "Should find a flow");
        assertTrue(minCost > 0, "Flow should have a cost");
    }
    
    @Test
    @DisplayName("MCMF: Zero cost edges")
    void testMinCostMaxFlowZeroCost() {
        int n = 3;
        int[][] edges = {
            {0, 1, 10, 0},
            {1, 2, 10, 0}
        };
        
        long[] result = NetworkFlowTemplates.minCostMaxFlow(n, 0, 2, edges);
        
        assertEquals(10, result[0], "Max flow should be 10");
        assertEquals(0, result[1], "Min cost should be 0 (all edges have 0 cost)");
    }
    
    @Test
    @DisplayName("MCMF: Prefer lower cost path")
    void testMinCostMaxFlowPreferLowerCost() {
        int n = 3;
        int[][] edges = {
            {0, 1, 10, 1},   // Low cost path
            {0, 2, 10, 100}, // High cost path
            {1, 2, 10, 0}
        };
        
        long[] result = NetworkFlowTemplates.minCostMaxFlow(n, 0, 2, edges);
        long maxFlow = result[0];
        long minCost = result[1];
        
        // Maximum flow is 20 (both paths used)
        // Path 0->1->2: 10 units at cost 10
        // Path 0->2: 10 units at cost 1000
        // Total: 20 units, cost 1010
        assertEquals(20, maxFlow, "Should find maximum flow of 20");
        assertEquals(1010, minCost, "Should have minimum cost for max flow");
    }
    
    @Test
    @DisplayName("MCMF: Verify lower cost path used first")
    void testMinCostMaxFlowLowerCostFirst() {
        int n = 4;
        int[][] edges = {
            {0, 1, 15, 1},   // Cheap path through node 1
            {0, 2, 15, 10},  // Expensive path through node 2
            {1, 3, 10, 1},   // Bottleneck on cheap path
            {2, 3, 15, 1}
        };
        
        long[] result = NetworkFlowTemplates.minCostMaxFlow(n, 0, 3, edges);
        long maxFlow = result[0];
        long minCost = result[1];
        
        // Max flow: 10 via cheap path 0->1->3, 15 via 0->2->3 = 25 total
        // Optimal cost: 10*(1+1) + 15*(10+1) = 20 + 165 = 185
        assertEquals(25, maxFlow, "Max flow should be 25");
        assertEquals(185, minCost, "Should use cheaper paths optimally");
    }
    
    @Test
    @DisplayName("MCMF Fast: Same result as standard MCMF")
    void testMinCostMaxFlowFast() {
        int n = 4;
        int[][] edges = {
            {0, 1, 10, 2},
            {0, 2, 10, 4},
            {1, 2, 2, 1},
            {1, 3, 4, 2},
            {2, 3, 10, 3}
        };
        
        long[] resultStandard = NetworkFlowTemplates.minCostMaxFlow(n, 0, 3, edges);
        long[] resultFast = NetworkFlowTemplates.minCostMaxFlowFast(n, 0, 3, edges);
        
        assertEquals(resultStandard[0], resultFast[0], "Both should find same max flow");
        assertEquals(resultStandard[1], resultFast[1], "Both should find same min cost");
    }
    
    @Test
    @DisplayName("MCMF: With demands - feasible case")
    void testMinCostFlowWithDemandsFeasible() {
        int n = 3;
        int[] demands = {10, -5, -5}; // Node 0 supplies 10, nodes 1,2 demand 5 each
        
        int[][] edges = {
            {0, 1, 10, 1},
            {0, 2, 10, 2},
            {1, 2, 10, 1}
        };
        
        long minCost = NetworkFlowTemplates.minCostFlowWithDemands(n, demands, edges);
        
        assertTrue(minCost >= 0, "Should find feasible solution with non-negative cost");
    }
    
    @Test
    @DisplayName("MCMF: With demands - infeasible case")
    void testMinCostFlowWithDemandsInfeasible() {
        int n = 3;
        int[] demands = {10, -5, -10}; // Demands sum to -5, unbalanced
        
        int[][] edges = {
            {0, 1, 5, 1},  // Not enough capacity
            {0, 2, 5, 2}
        };
        
        long minCost = NetworkFlowTemplates.minCostFlowWithDemands(n, demands, edges);
        
        assertEquals(-1, minCost, "Should return -1 when demands cannot be satisfied");
    }

    // ========== LEETCODE PROBLEM TESTS ==========
    
    @Test
    @DisplayName("LeetCode 1579: Max edges to remove - basic case")
    void testMaxNumEdgesToRemoveBasic() {
        int n = 4;
        int[][] edges = {
            {3, 1, 2}, {3, 2, 3}, {1, 1, 3}, {1, 2, 4}, {1, 1, 2}, {2, 3, 4}
        };
        
        int result = NetworkFlowTemplates.maxNumEdgesToRemove(n, edges);
        assertEquals(2, result, "Should remove 2 redundant edges");
    }
    
    @Test
    @DisplayName("LeetCode 1579: Cannot traverse for both")
    void testMaxNumEdgesToRemoveImpossible() {
        int n = 4;
        int[][] edges = {
            {1, 1, 2}, {2, 3, 4}  // Alice and Bob have disconnected graphs
        };
        
        int result = NetworkFlowTemplates.maxNumEdgesToRemove(n, edges);
        assertEquals(-1, result, "Should return -1 when graph not fully traversable");
    }
    
    @Test
    @DisplayName("LeetCode 1579: All type-3 edges sufficient")
    void testMaxNumEdgesToRemoveAllType3() {
        int n = 3;
        int[][] edges = {
            {3, 1, 2}, {3, 2, 3}, {3, 1, 3}
        };
        
        int result = NetworkFlowTemplates.maxNumEdgesToRemove(n, edges);
        assertEquals(1, result, "Should remove 1 edge (MST has n-1 edges)");
    }
    
    @Test
    @DisplayName("LeetCode 1579: Single node graph")
    void testMaxNumEdgesToRemoveSingleNode() {
        int n = 1;
        int[][] edges = {};
        
        int result = NetworkFlowTemplates.maxNumEdgesToRemove(n, edges);
        assertEquals(0, result, "Single node is already fully traversable");
    }

    // ========== EDGE CASE TESTS ==========
    
    @Test
    @DisplayName("Edge Case: Empty graph")
    void testEmptyGraph() {
        int[][] capacity = {{0}};
        int flow = NetworkFlowTemplates.fordFulkerson(capacity, 0, 0);
        assertEquals(0, flow, "Empty graph should have 0 flow");
    }
    
    @Test
    @DisplayName("Edge Case: Two nodes, no edge")
    void testTwoNodesNoEdge() {
        int[][] capacity = {
            {0, 0},
            {0, 0}
        };
        int flow = NetworkFlowTemplates.fordFulkerson(capacity, 0, 1);
        assertEquals(0, flow, "No flow when no edges");
    }
    
    @Test
    @DisplayName("Edge Case: Large flow values")
    void testLargeFlowValues() {
        int n = 3;
        @SuppressWarnings("unchecked")
        List<NetworkFlowTemplates.Edge>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // Test near Long.MAX_VALUE
        long largeCapacity = 1_000_000_000_000L;
        NetworkFlowTemplates.addEdge(graph, 0, 1, largeCapacity);
        NetworkFlowTemplates.addEdge(graph, 1, 2, largeCapacity);
        
        long maxFlow = NetworkFlowTemplates.dinic(graph, n, 0, 2);
        assertEquals(largeCapacity, maxFlow, "Should handle large capacity values");
    }
    
    @Test
    @DisplayName("Edge Case: Self-loops ignored")
    void testSelfLoops() {
        int[][] capacity = {
            {100, 10, 0},  // Self-loop at node 0 (should be ignored)
            {0, 50, 5},    // Self-loop at node 1
            {0, 0, 0}
        };
        
        int flow = NetworkFlowTemplates.fordFulkerson(capacity, 0, 2);
        assertEquals(5, flow, "Self-loops should not affect max flow");
    }

    // ========== STRESS TESTS ==========
    
    @Test
    @DisplayName("Stress Test: Large bipartite matching")
    void testLargeBipartiteMatching() {
        int leftSize = 100;
        int rightSize = 100;
        
        // Create complete bipartite graph
        List<int[]> edgeList = new ArrayList<>();
        for (int i = 0; i < leftSize; i++) {
            for (int j = 0; j < rightSize; j++) {
                edgeList.add(new int[]{i, j});
            }
        }
        
        int[][] edges = edgeList.toArray(new int[0][]);
        int matching = NetworkFlowTemplates.maxBipartiteMatching(leftSize, rightSize, edges);
        
        assertEquals(100, matching, "Complete bipartite graph should have perfect matching");
    }
    
    @Test
    @DisplayName("Stress Test: Dense flow network")
    void testDenseFlowNetwork() {
        int n = 20;
        int[][] capacity = new int[n][n];
        
        // Create dense random network
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                capacity[i][j] = (i + j) % 10 + 1; // Deterministic "random" capacity
            }
        }
        
        int flow = NetworkFlowTemplates.fordFulkerson(capacity, 0, n - 1);
        assertTrue(flow > 0, "Dense network should have positive flow");
    }
    
    @Test
    @DisplayName("Correctness: Ford-Fulkerson vs Dinic consistency")
    void testFordFulkersonVsDinic() {
        // Test multiple random graphs to ensure both algorithms agree
        for (int test = 0; test < 10; test++) {
            int n = 6;
            int[][] capacity = new int[n][n];
            
            // Create deterministic test case
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    capacity[i][j] = (i * 7 + j * 11 + test) % 20;
                }
            }
            
            int ffFlow = NetworkFlowTemplates.fordFulkerson(capacity, 0, n - 1);
            
            // Convert to Dinic graph
            @SuppressWarnings("unchecked")
            List<NetworkFlowTemplates.Edge>[] graph = new List[n];
            for (int i = 0; i < n; i++) {
                graph[i] = new ArrayList<>();
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (capacity[i][j] > 0) {
                        NetworkFlowTemplates.addEdge(graph, i, j, capacity[i][j]);
                    }
                }
            }
            
            long dinicFlow = NetworkFlowTemplates.dinic(graph, n, 0, n - 1);
            
            assertEquals(ffFlow, dinicFlow, 
                "Ford-Fulkerson and Dinic should produce same max flow (test " + test + ")");
        }
    }
}
