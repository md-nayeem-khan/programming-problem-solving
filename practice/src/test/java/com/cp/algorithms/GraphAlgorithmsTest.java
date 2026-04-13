package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for GraphAlgorithms.
 * 
 * Validates all graph algorithms:
 * 1. Dijkstra's shortest path
 * 2. Bellman-Ford (with negative weights and cycle detection)
 * 3. Floyd-Warshall (all-pairs shortest path)
 * 4. Topological Sort (Kahn's BFS and DFS-based)
 * 5. Prim's MST
 * 6. Kruskal's MST
 * 
 * Test coverage:
 * - Basic functionality
 * - Edge cases (empty graphs, single node, disconnected components)
 * - Algorithm-specific cases (negative weights, cycles, etc.)
 * - Correctness validation
 */
public class GraphAlgorithmsTest {

    private static final int INF = Integer.MAX_VALUE / 2;

    // ========== HELPER METHODS ==========
    
    @SuppressWarnings("unchecked")
    private List<int[]>[] createWeightedGraph(int n) {
        List<int[]>[] adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        return adj;
    }
    
    private void addWeightedEdge(List<int[]>[] adj, int u, int v, int weight) {
        adj[u].add(new int[]{v, weight});
    }
    
    private void addBidirectionalEdge(List<int[]>[] adj, int u, int v, int weight) {
        adj[u].add(new int[]{v, weight});
        adj[v].add(new int[]{u, weight});
    }
    
    private List<List<Integer>> createAdjacencyList(int n) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        return adj;
    }
    
    private void addDirectedEdge(List<List<Integer>> adj, int u, int v) {
        adj.get(u).add(v);
    }

    // ========== DIJKSTRA TESTS ==========
    
    @Nested
    @DisplayName("Dijkstra's Algorithm Tests")
    class DijkstraTests {
        
        @Test
        @DisplayName("Basic shortest path - simple graph")
        void testBasicShortestPath() {
            // Graph: 0 --1--> 1 --2--> 2
            //        |        |
            //        4        1
            //        v        v
            //        3 <--1-- 2
            int n = 4;
            List<int[]>[] adj = createWeightedGraph(n);
            addWeightedEdge(adj, 0, 1, 1);
            addWeightedEdge(adj, 0, 3, 4);
            addWeightedEdge(adj, 1, 2, 2);
            addWeightedEdge(adj, 1, 3, 1);
            addWeightedEdge(adj, 2, 3, 1);
            
            int[] dist = GraphAlgorithms.dijkstra(adj, 0, n);
            
            assertArrayEquals(new int[]{0, 1, 3, 2}, dist);
        }
        
        @Test
        @DisplayName("Single node graph")
        void testSingleNode() {
            int n = 1;
            List<int[]>[] adj = createWeightedGraph(n);
            
            int[] dist = GraphAlgorithms.dijkstra(adj, 0, n);
            
            assertArrayEquals(new int[]{0}, dist);
        }
        
        @Test
        @DisplayName("Disconnected graph")
        void testDisconnectedGraph() {
            int n = 4;
            List<int[]>[] adj = createWeightedGraph(n);
            addWeightedEdge(adj, 0, 1, 1);
            // Nodes 2 and 3 are disconnected from 0
            addWeightedEdge(adj, 2, 3, 1);
            
            int[] dist = GraphAlgorithms.dijkstra(adj, 0, n);
            
            assertEquals(0, dist[0]);
            assertEquals(1, dist[1]);
            assertEquals(INF, dist[2]);
            assertEquals(INF, dist[3]);
        }
        
        @Test
        @DisplayName("Multiple paths - choose shortest")
        void testMultiplePaths() {
            // Graph with multiple paths from 0 to 3
            int n = 4;
            List<int[]>[] adj = createWeightedGraph(n);
            addWeightedEdge(adj, 0, 1, 1);
            addWeightedEdge(adj, 1, 3, 1);  // Path: 0->1->3 = 2
            addWeightedEdge(adj, 0, 2, 5);
            addWeightedEdge(adj, 2, 3, 1);  // Path: 0->2->3 = 6
            
            int[] dist = GraphAlgorithms.dijkstra(adj, 0, n);
            
            assertEquals(2, dist[3]); // Shortest path 0->1->3
        }
        
        @Test
        @DisplayName("Self loop should not affect result")
        void testSelfLoop() {
            int n = 2;
            List<int[]>[] adj = createWeightedGraph(n);
            addWeightedEdge(adj, 0, 0, 5); // Self loop
            addWeightedEdge(adj, 0, 1, 3);
            
            int[] dist = GraphAlgorithms.dijkstra(adj, 0, n);
            
            assertArrayEquals(new int[]{0, 3}, dist);
        }
        
        @Test
        @DisplayName("Complete graph")
        void testCompleteGraph() {
            int n = 4;
            List<int[]>[] adj = createWeightedGraph(n);
            // All nodes connected
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        addWeightedEdge(adj, i, j, i + j + 1);
                    }
                }
            }
            
            int[] dist = GraphAlgorithms.dijkstra(adj, 0, n);
            
            assertEquals(0, dist[0]);
            assertTrue(dist[1] > 0);
            assertTrue(dist[2] > 0);
            assertTrue(dist[3] > 0);
        }
    }

    // ========== BELLMAN-FORD TESTS ==========
    
    @Nested
    @DisplayName("Bellman-Ford Algorithm Tests")
    class BellmanFordTests {
        
        @Test
        @DisplayName("Basic shortest path with positive weights")
        void testBasicShortestPath() {
            int n = 4;
            int[][] edges = {
                {0, 1, 1},
                {0, 3, 4},
                {1, 2, 2},
                {1, 3, 1},
                {2, 3, 1}
            };
            
            int[] dist = GraphAlgorithms.bellmanFord(n, edges, 0);
            
            assertNotNull(dist);
            assertArrayEquals(new int[]{0, 1, 3, 2}, dist);
        }
        
        @Test
        @DisplayName("Graph with negative weights - no cycle")
        void testNegativeWeights() {
            int n = 4;
            int[][] edges = {
                {0, 1, 1},
                {1, 2, -3},
                {0, 2, 4},
                {2, 3, 2}
            };
            
            int[] dist = GraphAlgorithms.bellmanFord(n, edges, 0);
            
            assertNotNull(dist);
            assertEquals(0, dist[0]);
            assertEquals(1, dist[1]);
            assertEquals(-2, dist[2]); // 0->1->2 = 1 + (-3) = -2
            assertEquals(0, dist[3]);  // 0->1->2->3 = -2 + 2 = 0
        }
        
        @Test
        @DisplayName("Negative cycle detection")
        void testNegativeCycle() {
            int n = 3;
            int[][] edges = {
                {0, 1, 1},
                {1, 2, -3},
                {2, 0, 1}  // Cycle: 0->1->2->0 = 1-3+1 = -1 (negative)
            };
            
            int[] dist = GraphAlgorithms.bellmanFord(n, edges, 0);
            
            assertNull(dist, "Should return null when negative cycle exists");
        }
        
        @Test
        @DisplayName("Single node graph")
        void testSingleNode() {
            int n = 1;
            int[][] edges = {};
            
            int[] dist = GraphAlgorithms.bellmanFord(n, edges, 0);
            
            assertNotNull(dist);
            assertArrayEquals(new int[]{0}, dist);
        }
        
        @Test
        @DisplayName("Disconnected graph")
        void testDisconnectedGraph() {
            int n = 4;
            int[][] edges = {
                {0, 1, 1},
                {2, 3, 1}
            };
            
            int[] dist = GraphAlgorithms.bellmanFord(n, edges, 0);
            
            assertNotNull(dist);
            assertEquals(0, dist[0]);
            assertEquals(1, dist[1]);
            assertEquals(INF, dist[2]);
            assertEquals(INF, dist[3]);
        }
        
        @Test
        @DisplayName("Early termination optimization")
        void testEarlyTermination() {
            // Simple chain - should terminate early
            int n = 5;
            int[][] edges = {
                {0, 1, 1},
                {1, 2, 1},
                {2, 3, 1},
                {3, 4, 1}
            };
            
            int[] dist = GraphAlgorithms.bellmanFord(n, edges, 0);
            
            assertNotNull(dist);
            assertArrayEquals(new int[]{0, 1, 2, 3, 4}, dist);
        }
        
        @Test
        @DisplayName("Complex negative weight graph without cycle")
        void testComplexNegativeWeights() {
            int n = 5;
            int[][] edges = {
                {0, 1, 5},
                {0, 2, 2},
                {1, 3, 1},
                {2, 1, -3},
                {2, 3, 4},
                {3, 4, 2},
                {2, 4, 5}
            };
            
            int[] dist = GraphAlgorithms.bellmanFord(n, edges, 0);
            
            assertNotNull(dist);
            assertEquals(0, dist[0]);
            assertEquals(-1, dist[1]); // 0->2->1 = 2-3 = -1
            assertEquals(2, dist[2]);
            assertEquals(0, dist[3]);  // 0->2->1->3 = -1+1 = 0
            assertEquals(2, dist[4]);  // 0->2->1->3->4 = 0+2 = 2
        }
    }

    // ========== FLOYD-WARSHALL TESTS ==========
    
    @Nested
    @DisplayName("Floyd-Warshall Algorithm Tests")
    class FloydWarshallTests {
        
        @Test
        @DisplayName("Basic all-pairs shortest path")
        void testBasicAllPairs() {
            int n = 4;
            int[][] dist = new int[n][n];
            
            // Initialize with INF and set diagonals to 0
            for (int i = 0; i < n; i++) {
                Arrays.fill(dist[i], INF);
                dist[i][i] = 0;
            }
            
            // Add edges
            dist[0][1] = 1;
            dist[0][3] = 4;
            dist[1][2] = 2;
            dist[1][3] = 1;
            dist[2][3] = 1;
            
            int[][] result = GraphAlgorithms.floydWarshall(dist);
            
            assertEquals(0, result[0][0]);
            assertEquals(1, result[0][1]);
            assertEquals(3, result[0][2]);
            assertEquals(2, result[0][3]);
        }
        
        @Test
        @DisplayName("Single node")
        void testSingleNode() {
            int n = 1;
            int[][] dist = {{0}};
            
            int[][] result = GraphAlgorithms.floydWarshall(dist);
            
            assertArrayEquals(new int[]{0}, result[0]);
        }
        
        @Test
        @DisplayName("Disconnected nodes")
        void testDisconnectedNodes() {
            int n = 3;
            int[][] dist = new int[n][n];
            
            for (int i = 0; i < n; i++) {
                Arrays.fill(dist[i], INF);
                dist[i][i] = 0;
            }
            
            dist[0][1] = 1;
            // Node 2 is disconnected
            
            int[][] result = GraphAlgorithms.floydWarshall(dist);
            
            assertEquals(0, result[0][0]);
            assertEquals(1, result[0][1]);
            assertEquals(INF, result[0][2]);
            assertEquals(INF, result[1][2]);
        }
        
        @Test
        @DisplayName("Negative weights without cycle")
        void testNegativeWeights() {
            int n = 3;
            int[][] dist = new int[n][n];
            
            for (int i = 0; i < n; i++) {
                Arrays.fill(dist[i], INF);
                dist[i][i] = 0;
            }
            
            dist[0][1] = 3;
            dist[1][2] = -2;
            dist[0][2] = 5;
            
            int[][] result = GraphAlgorithms.floydWarshall(dist);
            
            assertEquals(0, result[0][0]);
            assertEquals(3, result[0][1]);
            assertEquals(1, result[0][2]); // 0->1->2 = 3-2 = 1
        }
        
        @Test
        @DisplayName("Complete graph")
        void testCompleteGraph() {
            int n = 4;
            int[][] dist = new int[n][n];
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) dist[i][j] = 0;
                    else dist[i][j] = 1;
                }
            }
            
            int[][] result = GraphAlgorithms.floydWarshall(dist);
            
            // All distances should be at most 1
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) assertEquals(0, result[i][j]);
                    else assertEquals(1, result[i][j]);
                }
            }
        }
        
        @Test
        @DisplayName("Verify transitive closure")
        void testTransitiveClosure() {
            int n = 4;
            int[][] dist = new int[n][n];
            
            for (int i = 0; i < n; i++) {
                Arrays.fill(dist[i], INF);
                dist[i][i] = 0;
            }
            
            // Chain: 0->1->2->3
            dist[0][1] = 1;
            dist[1][2] = 1;
            dist[2][3] = 1;
            
            int[][] result = GraphAlgorithms.floydWarshall(dist);
            
            assertEquals(0, result[0][0]);
            assertEquals(1, result[0][1]);
            assertEquals(2, result[0][2]);
            assertEquals(3, result[0][3]);
        }
    }

    // ========== TOPOLOGICAL SORT TESTS ==========
    
    @Nested
    @DisplayName("Topological Sort Tests")
    class TopologicalSortTests {
        
        @Test
        @DisplayName("Kahn's algorithm - simple DAG")
        void testKahnSimpleDAG() {
            int n = 6;
            List<List<Integer>> adj = createAdjacencyList(n);
            
            // DAG: 5->2->3->1, 5->0, 4->0, 4->1
            addDirectedEdge(adj, 5, 2);
            addDirectedEdge(adj, 5, 0);
            addDirectedEdge(adj, 4, 0);
            addDirectedEdge(adj, 4, 1);
            addDirectedEdge(adj, 2, 3);
            addDirectedEdge(adj, 3, 1);
            
            List<Integer> order = GraphAlgorithms.topoSortKahn(n, adj);
            
            assertNotNull(order);
            assertEquals(n, order.size());
            
            // Verify topological order
            Map<Integer, Integer> position = new HashMap<>();
            for (int i = 0; i < order.size(); i++) {
                position.put(order.get(i), i);
            }
            
            // For every edge u->v, u should come before v
            assertTrue(position.get(5) < position.get(2));
            assertTrue(position.get(5) < position.get(0));
            assertTrue(position.get(4) < position.get(0));
            assertTrue(position.get(4) < position.get(1));
            assertTrue(position.get(2) < position.get(3));
            assertTrue(position.get(3) < position.get(1));
        }
        
        @Test
        @DisplayName("Kahn's algorithm - detects cycle")
        void testKahnDetectsCycle() {
            int n = 3;
            List<List<Integer>> adj = createAdjacencyList(n);
            
            // Cycle: 0->1->2->0
            addDirectedEdge(adj, 0, 1);
            addDirectedEdge(adj, 1, 2);
            addDirectedEdge(adj, 2, 0);
            
            List<Integer> order = GraphAlgorithms.topoSortKahn(n, adj);
            
            assertTrue(order.isEmpty(), "Should return empty list for cyclic graph");
        }
        
        @Test
        @DisplayName("DFS-based - simple DAG")
        void testDFSSimpleDAG() {
            int n = 6;
            List<List<Integer>> adj = createAdjacencyList(n);
            
            // Same DAG as Kahn test
            addDirectedEdge(adj, 5, 2);
            addDirectedEdge(adj, 5, 0);
            addDirectedEdge(adj, 4, 0);
            addDirectedEdge(adj, 4, 1);
            addDirectedEdge(adj, 2, 3);
            addDirectedEdge(adj, 3, 1);
            
            List<Integer> order = GraphAlgorithms.topoSortDFS(n, adj);
            
            assertNotNull(order);
            assertEquals(n, order.size());
            
            // Verify topological order
            Map<Integer, Integer> position = new HashMap<>();
            for (int i = 0; i < order.size(); i++) {
                position.put(order.get(i), i);
            }
            
            assertTrue(position.get(5) < position.get(2));
            assertTrue(position.get(5) < position.get(0));
            assertTrue(position.get(4) < position.get(0));
            assertTrue(position.get(4) < position.get(1));
            assertTrue(position.get(2) < position.get(3));
            assertTrue(position.get(3) < position.get(1));
        }
        
        @Test
        @DisplayName("DFS-based - detects cycle")
        void testDFSDetectsCycle() {
            int n = 3;
            List<List<Integer>> adj = createAdjacencyList(n);
            
            // Cycle: 0->1->2->0
            addDirectedEdge(adj, 0, 1);
            addDirectedEdge(adj, 1, 2);
            addDirectedEdge(adj, 2, 0);
            
            List<Integer> order = GraphAlgorithms.topoSortDFS(n, adj);
            
            assertTrue(order.isEmpty(), "Should return empty list for cyclic graph");
        }
        
        @Test
        @DisplayName("Single node graph")
        void testSingleNode() {
            int n = 1;
            List<List<Integer>> adj = createAdjacencyList(n);
            
            List<Integer> orderKahn = GraphAlgorithms.topoSortKahn(n, adj);
            List<Integer> orderDFS = GraphAlgorithms.topoSortDFS(n, adj);
            
            assertEquals(List.of(0), orderKahn);
            assertEquals(List.of(0), orderDFS);
        }
        
        @Test
        @DisplayName("Linear chain")
        void testLinearChain() {
            int n = 5;
            List<List<Integer>> adj = createAdjacencyList(n);
            
            // Chain: 0->1->2->3->4
            for (int i = 0; i < n - 1; i++) {
                addDirectedEdge(adj, i, i + 1);
            }
            
            List<Integer> orderKahn = GraphAlgorithms.topoSortKahn(n, adj);
            List<Integer> orderDFS = GraphAlgorithms.topoSortDFS(n, adj);
            
            assertEquals(List.of(0, 1, 2, 3, 4), orderKahn);
            assertEquals(List.of(0, 1, 2, 3, 4), orderDFS);
        }
        
        @Test
        @DisplayName("Disconnected components")
        void testDisconnectedComponents() {
            int n = 4;
            List<List<Integer>> adj = createAdjacencyList(n);
            
            // Component 1: 0->1
            // Component 2: 2->3
            addDirectedEdge(adj, 0, 1);
            addDirectedEdge(adj, 2, 3);
            
            List<Integer> orderKahn = GraphAlgorithms.topoSortKahn(n, adj);
            List<Integer> orderDFS = GraphAlgorithms.topoSortDFS(n, adj);
            
            assertEquals(n, orderKahn.size());
            assertEquals(n, orderDFS.size());
            
            // Verify partial order
            Map<Integer, Integer> posKahn = new HashMap<>();
            for (int i = 0; i < orderKahn.size(); i++) {
                posKahn.put(orderKahn.get(i), i);
            }
            assertTrue(posKahn.get(0) < posKahn.get(1));
            assertTrue(posKahn.get(2) < posKahn.get(3));
        }
    }

    // ========== PRIM'S MST TESTS ==========
    
    @Nested
    @DisplayName("Prim's MST Tests")
    class PrimMSTTests {
        
        @Test
        @DisplayName("Basic MST calculation")
        void testBasicMST() {
            int n = 4;
            List<int[]>[] adj = createWeightedGraph(n);
            
            // Complete graph
            addBidirectionalEdge(adj, 0, 1, 1);
            addBidirectionalEdge(adj, 0, 2, 4);
            addBidirectionalEdge(adj, 0, 3, 3);
            addBidirectionalEdge(adj, 1, 2, 2);
            addBidirectionalEdge(adj, 1, 3, 5);
            addBidirectionalEdge(adj, 2, 3, 1);
            
            int mstWeight = GraphAlgorithms.primMST(adj, n);
            
            // MST: edges (0,1)=1, (1,2)=2, (2,3)=1 = 4
            assertEquals(4, mstWeight);
        }
        
        @Test
        @DisplayName("Single node")
        void testSingleNode() {
            int n = 1;
            List<int[]>[] adj = createWeightedGraph(n);
            
            int mstWeight = GraphAlgorithms.primMST(adj, n);
            
            assertEquals(0, mstWeight);
        }
        
        @Test
        @DisplayName("Linear graph")
        void testLinearGraph() {
            int n = 4;
            List<int[]>[] adj = createWeightedGraph(n);
            
            // Chain: 0-1-2-3
            addBidirectionalEdge(adj, 0, 1, 2);
            addBidirectionalEdge(adj, 1, 2, 3);
            addBidirectionalEdge(adj, 2, 3, 4);
            
            int mstWeight = GraphAlgorithms.primMST(adj, n);
            
            assertEquals(9, mstWeight); // 2+3+4
        }
        
        @Test
        @DisplayName("Star graph")
        void testStarGraph() {
            int n = 5;
            List<int[]>[] adj = createWeightedGraph(n);
            
            // Star: center 0 connected to all others
            for (int i = 1; i < n; i++) {
                addBidirectionalEdge(adj, 0, i, i);
            }
            
            int mstWeight = GraphAlgorithms.primMST(adj, n);
            
            assertEquals(10, mstWeight); // 1+2+3+4
        }
        
        @Test
        @DisplayName("Classic MST example")
        void testClassicMST() {
            int n = 9;
            List<int[]>[] adj = createWeightedGraph(n);
            
            // Classic textbook example
            addBidirectionalEdge(adj, 0, 1, 4);
            addBidirectionalEdge(adj, 0, 7, 8);
            addBidirectionalEdge(adj, 1, 2, 8);
            addBidirectionalEdge(adj, 1, 7, 11);
            addBidirectionalEdge(adj, 2, 3, 7);
            addBidirectionalEdge(adj, 2, 8, 2);
            addBidirectionalEdge(adj, 2, 5, 4);
            addBidirectionalEdge(adj, 3, 4, 9);
            addBidirectionalEdge(adj, 3, 5, 14);
            addBidirectionalEdge(adj, 4, 5, 10);
            addBidirectionalEdge(adj, 5, 6, 2);
            addBidirectionalEdge(adj, 6, 7, 1);
            addBidirectionalEdge(adj, 6, 8, 6);
            addBidirectionalEdge(adj, 7, 8, 7);
            
            int mstWeight = GraphAlgorithms.primMST(adj, n);
            
            assertEquals(37, mstWeight);
        }
    }

    // ========== KRUSKAL'S MST TESTS ==========
    
    @Nested
    @DisplayName("Kruskal's MST Tests")
    class KruskalMSTTests {
        
        @Test
        @DisplayName("Basic MST calculation")
        void testBasicMST() {
            int n = 4;
            int[][] edges = {
                {0, 1, 1},
                {0, 2, 4},
                {0, 3, 3},
                {1, 2, 2},
                {1, 3, 5},
                {2, 3, 1}
            };
            
            int mstWeight = GraphAlgorithms.kruskalMST(n, edges);
            
            // MST: edges (0,1)=1, (2,3)=1, (1,2)=2 = 4
            assertEquals(4, mstWeight);
        }
        
        @Test
        @DisplayName("Single node")
        void testSingleNode() {
            int n = 1;
            int[][] edges = {};
            
            int mstWeight = GraphAlgorithms.kruskalMST(n, edges);
            
            assertEquals(0, mstWeight);
        }
        
        @Test
        @DisplayName("Linear graph")
        void testLinearGraph() {
            int n = 4;
            int[][] edges = {
                {0, 1, 2},
                {1, 2, 3},
                {2, 3, 4}
            };
            
            int mstWeight = GraphAlgorithms.kruskalMST(n, edges);
            
            assertEquals(9, mstWeight); // 2+3+4
        }
        
        @Test
        @DisplayName("Classic MST example")
        void testClassicMST() {
            int n = 9;
            int[][] edges = {
                {0, 1, 4},
                {0, 7, 8},
                {1, 2, 8},
                {1, 7, 11},
                {2, 3, 7},
                {2, 8, 2},
                {2, 5, 4},
                {3, 4, 9},
                {3, 5, 14},
                {4, 5, 10},
                {5, 6, 2},
                {6, 7, 1},
                {6, 8, 6},
                {7, 8, 7}
            };
            
            int mstWeight = GraphAlgorithms.kruskalMST(n, edges);
            
            assertEquals(37, mstWeight);
        }
        
        @Test
        @DisplayName("Prim and Kruskal should give same result")
        void testPrimKruskalEquivalence() {
            int n = 5;
            
            // Build graph for Prim
            List<int[]>[] adj = createWeightedGraph(n);
            addBidirectionalEdge(adj, 0, 1, 2);
            addBidirectionalEdge(adj, 0, 3, 6);
            addBidirectionalEdge(adj, 1, 2, 3);
            addBidirectionalEdge(adj, 1, 3, 8);
            addBidirectionalEdge(adj, 1, 4, 5);
            addBidirectionalEdge(adj, 2, 4, 7);
            addBidirectionalEdge(adj, 3, 4, 9);
            
            // Build edges for Kruskal
            int[][] edges = {
                {0, 1, 2},
                {0, 3, 6},
                {1, 2, 3},
                {1, 3, 8},
                {1, 4, 5},
                {2, 4, 7},
                {3, 4, 9}
            };
            
            int primWeight = GraphAlgorithms.primMST(adj, n);
            int kruskalWeight = GraphAlgorithms.kruskalMST(n, edges);
            
            assertEquals(primWeight, kruskalWeight);
        }
        
        @Test
        @DisplayName("All edges same weight")
        void testSameWeightEdges() {
            int n = 4;
            int[][] edges = {
                {0, 1, 1},
                {0, 2, 1},
                {0, 3, 1},
                {1, 2, 1},
                {1, 3, 1},
                {2, 3, 1}
            };
            
            int mstWeight = GraphAlgorithms.kruskalMST(n, edges);
            
            assertEquals(3, mstWeight); // Any 3 edges form MST
        }
    }

    // ========== EDGE CASES & STRESS TESTS ==========
    
    @Nested
    @DisplayName("Edge Cases and Stress Tests")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Large sparse graph - Dijkstra")
        void testLargeSparseGraphDijkstra() {
            int n = 1000;
            List<int[]>[] adj = createWeightedGraph(n);
            
            // Create a long chain
            for (int i = 0; i < n - 1; i++) {
                addWeightedEdge(adj, i, i + 1, 1);
            }
            
            int[] dist = GraphAlgorithms.dijkstra(adj, 0, n);
            
            assertEquals(0, dist[0]);
            assertEquals(n - 1, dist[n - 1]);
        }
        
        @Test
        @DisplayName("Zero weight edges - Dijkstra")
        void testZeroWeightEdges() {
            int n = 4;
            List<int[]>[] adj = createWeightedGraph(n);
            
            addWeightedEdge(adj, 0, 1, 0);
            addWeightedEdge(adj, 1, 2, 0);
            addWeightedEdge(adj, 2, 3, 1);
            
            int[] dist = GraphAlgorithms.dijkstra(adj, 0, n);
            
            assertArrayEquals(new int[]{0, 0, 0, 1}, dist);
        }
        
        @Test
        @DisplayName("Large weights don't cause overflow")
        void testLargeWeights() {
            int n = 3;
            List<int[]>[] adj = createWeightedGraph(n);
            
            addWeightedEdge(adj, 0, 1, 1000000);
            addWeightedEdge(adj, 1, 2, 1000000);
            
            int[] dist = GraphAlgorithms.dijkstra(adj, 0, n);
            
            assertEquals(0, dist[0]);
            assertEquals(1000000, dist[1]);
            assertEquals(2000000, dist[2]);
        }
    }
}
