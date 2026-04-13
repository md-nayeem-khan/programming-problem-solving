package com.cp.algorithms;

import com.cp.datastructures.TreeNode;
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
 * Comprehensive test suite for BFSDFSTemplates.
 * 
 * Validates all BFS and DFS algorithms with:
 * - Basic functionality tests
 * - Edge cases (empty, single node, disconnected)
 * - Complex scenarios (cycles, multiple paths)
 * - Performance validation
 * 
 * Coverage:
 * 1. Graph BFS (shortest path)
 * 2. Grid BFS (4-directional)
 * 3. Multi-source BFS
 * 4. Graph DFS (recursive)
 * 5. Cycle detection (directed graph)
 * 6. Tree BFS (level-order)
 * 7. Tree DFS (pre/in/post order)
 * 8. 0-1 BFS (graph and grid)
 * 9. Bidirectional BFS (graph, grid, word ladder)
 */
public class BFSDFSTemplatesTest {

    // ========== HELPER METHODS ==========
    
    private List<List<Integer>> createAdjacencyList(int n) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        return adj;
    }
    
    private void addEdge(List<List<Integer>> adj, int u, int v) {
        adj.get(u).add(v);
    }
    
    private void addBidirectionalEdge(List<List<Integer>> adj, int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);
    }
    
    private List<List<int[]>> createWeightedAdjacencyList(int n) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        return adj;
    }
    
    private void addWeightedEdge(List<List<int[]>> adj, int u, int v, int weight) {
        adj.get(u).add(new int[]{v, weight});
    }

    // ========== 1. GRAPH BFS TESTS ==========
    
    @Nested
    @DisplayName("Graph BFS Tests")
    class GraphBFSTests {
        
        @Test
        @DisplayName("Basic graph BFS - linear chain")
        void testLinearChain() {
            List<List<Integer>> adj = createAdjacencyList(4);
            addEdge(adj, 0, 1);
            addEdge(adj, 1, 2);
            addEdge(adj, 2, 3);
            
            int[] dist = BFSDFSTemplates.graphBFS(adj, 0, 4);
            
            assertArrayEquals(new int[]{0, 1, 2, 3}, dist);
        }
        
        @Test
        @DisplayName("Graph BFS - star topology")
        void testStarTopology() {
            List<List<Integer>> adj = createAdjacencyList(5);
            addEdge(adj, 0, 1);
            addEdge(adj, 0, 2);
            addEdge(adj, 0, 3);
            addEdge(adj, 0, 4);
            
            int[] dist = BFSDFSTemplates.graphBFS(adj, 0, 5);
            
            assertArrayEquals(new int[]{0, 1, 1, 1, 1}, dist);
        }
        
        @Test
        @DisplayName("Graph BFS - disconnected node")
        void testDisconnectedNode() {
            List<List<Integer>> adj = createAdjacencyList(4);
            addEdge(adj, 0, 1);
            addEdge(adj, 1, 2);
            // Node 3 is disconnected
            
            int[] dist = BFSDFSTemplates.graphBFS(adj, 0, 4);
            
            assertEquals(0, dist[0]);
            assertEquals(1, dist[1]);
            assertEquals(2, dist[2]);
            assertEquals(-1, dist[3]); // Unreachable
        }
        
        @Test
        @DisplayName("Graph BFS - single node")
        void testSingleNode() {
            List<List<Integer>> adj = createAdjacencyList(1);
            
            int[] dist = BFSDFSTemplates.graphBFS(adj, 0, 1);
            
            assertArrayEquals(new int[]{0}, dist);
        }
        
        @Test
        @DisplayName("Graph BFS - undirected cycle")
        void testUndirectedCycle() {
            List<List<Integer>> adj = createAdjacencyList(4);
            addBidirectionalEdge(adj, 0, 1);
            addBidirectionalEdge(adj, 1, 2);
            addBidirectionalEdge(adj, 2, 3);
            addBidirectionalEdge(adj, 3, 0);
            
            int[] dist = BFSDFSTemplates.graphBFS(adj, 0, 4);
            
            assertArrayEquals(new int[]{0, 1, 2, 1}, dist);
        }
        
        @Test
        @DisplayName("Graph BFS - complete graph")
        void testCompleteGraph() {
            List<List<Integer>> adj = createAdjacencyList(4);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (i != j) addEdge(adj, i, j);
                }
            }
            
            int[] dist = BFSDFSTemplates.graphBFS(adj, 0, 4);
            
            assertArrayEquals(new int[]{0, 1, 1, 1}, dist);
        }
    }

    // ========== 2. GRID BFS TESTS ==========
    
    @Nested
    @DisplayName("Grid BFS Tests")
    class GridBFSTests {
        
        @Test
        @DisplayName("Grid BFS - straight path")
        void testStraightPath() {
            int[][] grid = {
                {0, 0, 0, 0},
                {0, 0, 0, 0}
            };
            
            int result = BFSDFSTemplates.gridBFS(grid, 0, 0, 1, 3);
            
            assertEquals(4, result);
        }
        
        @Test
        @DisplayName("Grid BFS - with obstacles")
        void testWithObstacles() {
            int[][] grid = {
                {0, 0, 0, 0},
                {0, 1, 1, 0},
                {0, 0, 0, 0}
            };
            
            int result = BFSDFSTemplates.gridBFS(grid, 0, 0, 0, 3);
            
            assertEquals(3, result);
        }
        
        @Test
        @DisplayName("Grid BFS - unreachable destination")
        void testUnreachable() {
            int[][] grid = {
                {0, 0, 1},
                {1, 1, 1},
                {0, 0, 0}
            };
            
            int result = BFSDFSTemplates.gridBFS(grid, 0, 0, 2, 2);
            
            assertEquals(-1, result);
        }
        
        @Test
        @DisplayName("Grid BFS - same start and end")
        void testSameStartEnd() {
            int[][] grid = {
                {0, 0, 0},
                {0, 0, 0}
            };
            
            int result = BFSDFSTemplates.gridBFS(grid, 0, 0, 0, 0);
            
            assertEquals(0, result);
        }
        
        @Test
        @DisplayName("Grid BFS - blocked start")
        void testBlockedStart() {
            int[][] grid = {
                {1, 0, 0},
                {0, 0, 0}
            };
            
            int result = BFSDFSTemplates.gridBFS(grid, 0, 0, 1, 2);
            
            assertEquals(-1, result);
        }
        
        @Test
        @DisplayName("Grid BFS - blocked end")
        void testBlockedEnd() {
            int[][] grid = {
                {0, 0, 0},
                {0, 0, 1}
            };
            
            int result = BFSDFSTemplates.gridBFS(grid, 0, 0, 1, 2);
            
            assertEquals(-1, result);
        }
        
        @Test
        @DisplayName("Grid BFS - single cell")
        void testSingleCell() {
            int[][] grid = {{0}};
            
            int result = BFSDFSTemplates.gridBFS(grid, 0, 0, 0, 0);
            
            assertEquals(0, result);
        }
        
        @Test
        @DisplayName("Grid BFS - maze navigation")
        void testMazeNavigation() {
            int[][] grid = {
                {0, 1, 0, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 0, 0},
                {0, 0, 0, 1, 0}
            };
            
            int result = BFSDFSTemplates.gridBFS(grid, 0, 0, 4, 4);
            
            assertEquals(8, result);
        }
    }

    // ========== 3. MULTI-SOURCE BFS TESTS ==========
    
    @Nested
    @DisplayName("Multi-Source BFS Tests")
    class MultiSourceBFSTests {
        
        @Test
        @DisplayName("Multi-source BFS - single source")
        void testSingleSource() {
            int[][] grid = {
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
            };
            
            int[][] dist = BFSDFSTemplates.multiSourceBFS(grid, 1);
            
            assertEquals(0, dist[0][0]);
            assertEquals(1, dist[0][1]);
            assertEquals(2, dist[0][2]);
            assertEquals(1, dist[1][0]);
            assertEquals(2, dist[1][1]);
            assertEquals(3, dist[1][2]);
        }
        
        @Test
        @DisplayName("Multi-source BFS - multiple sources")
        void testMultipleSources() {
            int[][] grid = {
                {1, 0, 1},
                {0, 0, 0},
                {0, 0, 0}
            };
            
            int[][] dist = BFSDFSTemplates.multiSourceBFS(grid, 1);
            
            assertEquals(0, dist[0][0]);
            assertEquals(1, dist[0][1]);
            assertEquals(0, dist[0][2]);
            assertEquals(2, dist[1][1]);
        }
        
        @Test
        @DisplayName("Multi-source BFS - all sources")
        void testAllSources() {
            int[][] grid = {
                {1, 1},
                {1, 1}
            };
            
            int[][] dist = BFSDFSTemplates.multiSourceBFS(grid, 1);
            
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    assertEquals(0, dist[i][j]);
                }
            }
        }
        
        @Test
        @DisplayName("Multi-source BFS - no sources")
        void testNoSources() {
            int[][] grid = {
                {0, 0, 0},
                {0, 0, 0}
            };
            
            int[][] dist = BFSDFSTemplates.multiSourceBFS(grid, 1);
            
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(Integer.MAX_VALUE, dist[i][j]);
                }
            }
        }
        
        @Test
        @DisplayName("Multi-source BFS - corners as sources")
        void testCornerSources() {
            int[][] grid = {
                {1, 0, 0, 1},
                {0, 0, 0, 0},
                {1, 0, 0, 1}
            };
            
            int[][] dist = BFSDFSTemplates.multiSourceBFS(grid, 1);
            
            assertEquals(0, dist[0][0]);
            assertEquals(0, dist[0][3]);
            assertEquals(0, dist[2][0]);
            assertEquals(0, dist[2][3]);
            assertEquals(2, dist[1][1]); // Center is distance 2 from any corner
            assertEquals(2, dist[1][2]);
        }
    }

    // ========== 4. GRAPH DFS TESTS ==========
    
    @Nested
    @DisplayName("Graph DFS Tests")
    class GraphDFSTests {
        
        @Test
        @DisplayName("DFS - single component")
        void testSingleComponent() {
            List<List<Integer>> adj = createAdjacencyList(4);
            addBidirectionalEdge(adj, 0, 1);
            addBidirectionalEdge(adj, 1, 2);
            addBidirectionalEdge(adj, 2, 3);
            
            boolean[] visited = new boolean[4];
            List<Integer> component = new ArrayList<>();
            
            BFSDFSTemplates.graphDFS(adj, 0, visited, component);
            
            assertEquals(4, component.size());
            assertTrue(component.containsAll(Arrays.asList(0, 1, 2, 3)));
        }
        
        @Test
        @DisplayName("DFS - multiple components")
        void testMultipleComponents() {
            List<List<Integer>> adj = createAdjacencyList(6);
            addBidirectionalEdge(adj, 0, 1);
            addBidirectionalEdge(adj, 1, 2);
            addBidirectionalEdge(adj, 3, 4);
            addBidirectionalEdge(adj, 4, 5);
            
            int count = BFSDFSTemplates.countComponents(6, adj);
            
            assertEquals(2, count);
        }
        
        @Test
        @DisplayName("DFS - all isolated nodes")
        void testAllIsolatedNodes() {
            List<List<Integer>> adj = createAdjacencyList(5);
            
            int count = BFSDFSTemplates.countComponents(5, adj);
            
            assertEquals(5, count);
        }
        
        @Test
        @DisplayName("DFS - complete graph")
        void testCompleteGraphDFS() {
            List<List<Integer>> adj = createAdjacencyList(4);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (i != j) addEdge(adj, i, j);
                }
            }
            
            int count = BFSDFSTemplates.countComponents(4, adj);
            
            assertEquals(1, count);
        }
        
        @Test
        @DisplayName("DFS - tree structure")
        void testTreeStructure() {
            List<List<Integer>> adj = createAdjacencyList(7);
            addBidirectionalEdge(adj, 0, 1);
            addBidirectionalEdge(adj, 0, 2);
            addBidirectionalEdge(adj, 1, 3);
            addBidirectionalEdge(adj, 1, 4);
            addBidirectionalEdge(adj, 2, 5);
            addBidirectionalEdge(adj, 2, 6);
            
            int count = BFSDFSTemplates.countComponents(7, adj);
            
            assertEquals(1, count);
        }
    }

    // ========== 5. CYCLE DETECTION TESTS ==========
    
    @Nested
    @DisplayName("Cycle Detection Tests")
    class CycleDetectionTests {
        
        @Test
        @DisplayName("Detect cycle - simple cycle")
        void testSimpleCycle() {
            List<List<Integer>> adj = createAdjacencyList(3);
            addEdge(adj, 0, 1);
            addEdge(adj, 1, 2);
            addEdge(adj, 2, 0);
            
            assertTrue(BFSDFSTemplates.hasCycleDirected(3, adj));
        }
        
        @Test
        @DisplayName("Detect cycle - no cycle (DAG)")
        void testNoCycle() {
            List<List<Integer>> adj = createAdjacencyList(4);
            addEdge(adj, 0, 1);
            addEdge(adj, 0, 2);
            addEdge(adj, 1, 3);
            addEdge(adj, 2, 3);
            
            assertFalse(BFSDFSTemplates.hasCycleDirected(4, adj));
        }
        
        @Test
        @DisplayName("Detect cycle - self loop")
        void testSelfLoop() {
            List<List<Integer>> adj = createAdjacencyList(2);
            addEdge(adj, 0, 0);
            
            assertTrue(BFSDFSTemplates.hasCycleDirected(2, adj));
        }
        
        @Test
        @DisplayName("Detect cycle - disconnected with cycle")
        void testDisconnectedWithCycle() {
            List<List<Integer>> adj = createAdjacencyList(5);
            addEdge(adj, 0, 1);
            addEdge(adj, 1, 2);
            addEdge(adj, 3, 4);
            addEdge(adj, 4, 3); // Cycle in second component
            
            assertTrue(BFSDFSTemplates.hasCycleDirected(5, adj));
        }
        
        @Test
        @DisplayName("Detect cycle - long cycle")
        void testLongCycle() {
            List<List<Integer>> adj = createAdjacencyList(6);
            addEdge(adj, 0, 1);
            addEdge(adj, 1, 2);
            addEdge(adj, 2, 3);
            addEdge(adj, 3, 4);
            addEdge(adj, 4, 5);
            addEdge(adj, 5, 0); // Creates cycle
            
            assertTrue(BFSDFSTemplates.hasCycleDirected(6, adj));
        }
        
        @Test
        @DisplayName("Detect cycle - linear chain (no cycle)")
        void testLinearChainNoCycle() {
            List<List<Integer>> adj = createAdjacencyList(5);
            addEdge(adj, 0, 1);
            addEdge(adj, 1, 2);
            addEdge(adj, 2, 3);
            addEdge(adj, 3, 4);
            
            assertFalse(BFSDFSTemplates.hasCycleDirected(5, adj));
        }
    }

    // ========== 6. TREE BFS TESTS ==========
    
    @Nested
    @DisplayName("Tree BFS (Level Order) Tests")
    class TreeBFSTests {
        
        @Test
        @DisplayName("Level order - balanced tree")
        void testBalancedTree() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5, 6, 7});
            
            List<List<Integer>> result = BFSDFSTemplates.levelOrder(root);
            
            assertEquals(3, result.size());
            assertEquals(Arrays.asList(1), result.get(0));
            assertEquals(Arrays.asList(2, 3), result.get(1));
            assertEquals(Arrays.asList(4, 5, 6, 7), result.get(2));
        }
        
        @Test
        @DisplayName("Level order - single node")
        void testSingleNode() {
            TreeNode root = new TreeNode(1);
            
            List<List<Integer>> result = BFSDFSTemplates.levelOrder(root);
            
            assertEquals(1, result.size());
            assertEquals(Arrays.asList(1), result.get(0));
        }
        
        @Test
        @DisplayName("Level order - null tree")
        void testNullTree() {
            List<List<Integer>> result = BFSDFSTemplates.levelOrder(null);
            
            assertTrue(result.isEmpty());
        }
        
        @Test
        @DisplayName("Level order - left skewed tree")
        void testLeftSkewedTree() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, null, 3, null, 4});
            
            List<List<Integer>> result = BFSDFSTemplates.levelOrder(root);
            
            assertEquals(4, result.size());
            assertEquals(Arrays.asList(1), result.get(0));
            assertEquals(Arrays.asList(2), result.get(1));
            assertEquals(Arrays.asList(3), result.get(2));
            assertEquals(Arrays.asList(4), result.get(3));
        }
        
        @Test
        @DisplayName("Level order - right skewed tree")
        void testRightSkewedTree() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, null, 2, null, 3, null, 4});
            
            List<List<Integer>> result = BFSDFSTemplates.levelOrder(root);
            
            assertEquals(4, result.size());
            assertEquals(Arrays.asList(1), result.get(0));
            assertEquals(Arrays.asList(2), result.get(1));
            assertEquals(Arrays.asList(3), result.get(2));
            assertEquals(Arrays.asList(4), result.get(3));
        }
        
        @Test
        @DisplayName("Level order - complete tree")
        void testCompleteTree() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5, 6});
            
            List<List<Integer>> result = BFSDFSTemplates.levelOrder(root);
            
            assertEquals(3, result.size());
            assertEquals(Arrays.asList(1), result.get(0));
            assertEquals(Arrays.asList(2, 3), result.get(1));
            assertEquals(Arrays.asList(4, 5, 6), result.get(2));
        }
    }

    // ========== 7. TREE DFS TESTS ==========
    
    @Nested
    @DisplayName("Tree DFS (Pre/In/Post Order) Tests")
    class TreeDFSTests {
        
        @Test
        @DisplayName("Pre-order traversal")
        void testPreOrder() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5});
            
            List<Integer> result = BFSDFSTemplates.preOrder(root);
            
            assertEquals(Arrays.asList(1, 2, 4, 5, 3), result);
        }
        
        @Test
        @DisplayName("In-order traversal")
        void testInOrder() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5});
            
            List<Integer> result = BFSDFSTemplates.inOrder(root);
            
            assertEquals(Arrays.asList(4, 2, 5, 1, 3), result);
        }
        
        @Test
        @DisplayName("Post-order traversal")
        void testPostOrder() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5});
            
            List<Integer> result = BFSDFSTemplates.postOrder(root);
            
            assertEquals(Arrays.asList(4, 5, 2, 3, 1), result);
        }
        
        @Test
        @DisplayName("Pre-order - single node")
        void testPreOrderSingleNode() {
            TreeNode root = new TreeNode(1);
            
            List<Integer> result = BFSDFSTemplates.preOrder(root);
            
            assertEquals(Arrays.asList(1), result);
        }
        
        @Test
        @DisplayName("In-order - single node")
        void testInOrderSingleNode() {
            TreeNode root = new TreeNode(1);
            
            List<Integer> result = BFSDFSTemplates.inOrder(root);
            
            assertEquals(Arrays.asList(1), result);
        }
        
        @Test
        @DisplayName("Post-order - single node")
        void testPostOrderSingleNode() {
            TreeNode root = new TreeNode(1);
            
            List<Integer> result = BFSDFSTemplates.postOrder(root);
            
            assertEquals(Arrays.asList(1), result);
        }
        
        @Test
        @DisplayName("Pre-order - null tree")
        void testPreOrderNullTree() {
            List<Integer> result = BFSDFSTemplates.preOrder(null);
            
            assertTrue(result.isEmpty());
        }
        
        @Test
        @DisplayName("In-order - null tree")
        void testInOrderNullTree() {
            List<Integer> result = BFSDFSTemplates.inOrder(null);
            
            assertTrue(result.isEmpty());
        }
        
        @Test
        @DisplayName("Post-order - null tree")
        void testPostOrderNullTree() {
            List<Integer> result = BFSDFSTemplates.postOrder(null);
            
            assertTrue(result.isEmpty());
        }
        
        @Test
        @DisplayName("All orders - left skewed tree")
        void testLeftSkewedTreeAllOrders() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, null, 3});
            
            assertEquals(Arrays.asList(1, 2, 3), BFSDFSTemplates.preOrder(root));
            assertEquals(Arrays.asList(3, 2, 1), BFSDFSTemplates.inOrder(root));
            assertEquals(Arrays.asList(3, 2, 1), BFSDFSTemplates.postOrder(root));
        }
        
        @Test
        @DisplayName("All orders - right skewed tree")
        void testRightSkewedTreeAllOrders() {
            TreeNode root = TreeNode.fromArray(new Integer[]{1, null, 2, null, 3});
            
            assertEquals(Arrays.asList(1, 2, 3), BFSDFSTemplates.preOrder(root));
            assertEquals(Arrays.asList(1, 2, 3), BFSDFSTemplates.inOrder(root));
            assertEquals(Arrays.asList(3, 2, 1), BFSDFSTemplates.postOrder(root));
        }
    }

    // ========== 8. 0-1 BFS TESTS ==========
    
    @Nested
    @DisplayName("0-1 BFS Tests")
    class ZeroOneBFSTests {
        
        @Test
        @DisplayName("0-1 BFS Graph - basic test")
        void testZeroOneBFSBasic() {
            List<List<int[]>> adj = createWeightedAdjacencyList(4);
            addWeightedEdge(adj, 0, 1, 0);
            addWeightedEdge(adj, 0, 2, 1);
            addWeightedEdge(adj, 1, 3, 1);
            addWeightedEdge(adj, 2, 3, 0);
            
            int[] dist = BFSDFSTemplates.zeroOneBFS(adj, 0, 4);
            
            assertEquals(0, dist[0]);
            assertEquals(0, dist[1]);
            assertEquals(1, dist[2]);
            assertEquals(1, dist[3]);
        }
        
        @Test
        @DisplayName("0-1 BFS Graph - all zero edges")
        void testAllZeroEdges() {
            List<List<int[]>> adj = createWeightedAdjacencyList(3);
            addWeightedEdge(adj, 0, 1, 0);
            addWeightedEdge(adj, 1, 2, 0);
            
            int[] dist = BFSDFSTemplates.zeroOneBFS(adj, 0, 3);
            
            assertArrayEquals(new int[]{0, 0, 0}, dist);
        }
        
        @Test
        @DisplayName("0-1 BFS Graph - all one edges")
        void testAllOneEdges() {
            List<List<int[]>> adj = createWeightedAdjacencyList(3);
            addWeightedEdge(adj, 0, 1, 1);
            addWeightedEdge(adj, 1, 2, 1);
            
            int[] dist = BFSDFSTemplates.zeroOneBFS(adj, 0, 3);
            
            assertArrayEquals(new int[]{0, 1, 2}, dist);
        }
        
        @Test
        @DisplayName("0-1 BFS Graph - multiple paths")
        void testMultiplePaths() {
            List<List<int[]>> adj = createWeightedAdjacencyList(4);
            addWeightedEdge(adj, 0, 1, 1);
            addWeightedEdge(adj, 0, 2, 0);
            addWeightedEdge(adj, 2, 1, 0);
            addWeightedEdge(adj, 1, 3, 0);
            
            int[] dist = BFSDFSTemplates.zeroOneBFS(adj, 0, 4);
            
            assertEquals(0, dist[0]);
            assertEquals(0, dist[1]); // Via 0->2->1 (cost 0)
            assertEquals(0, dist[2]);
            assertEquals(0, dist[3]);
        }
        
        @Test
        @DisplayName("0-1 BFS Grid - basic test")
        void testZeroOneBFSGrid() {
            // Grid values: 0=right, 1=down, 2=left, 3=up (preferred direction)
            int[][] grid = {
                {0, 1, 0},
                {0, 0, 1},
                {1, 0, 0}
            };
            
            int result = BFSDFSTemplates.zeroOneBFSGrid(grid, 0, 0, 2, 2);
            
            // From (0,0) to (2,2): optimal path follows preferred directions
            // (0,0)[pref=R] -> R to (0,1) cost 0
            // (0,1)[pref=D] -> D to (1,1) cost 0
            // (1,1)[pref=R] -> R to (1,2) cost 0
            // (1,2)[pref=D] -> D to (2,2) cost 0
            // Total cost = 0
            assertEquals(0, result);
        }
        
        @Test
        @DisplayName("0-1 BFS Grid - all zeros")
        void testGridAllZeros() {
            // All cells prefer RIGHT (value 0)
            int[][] grid = {
                {0, 0, 0},
                {0, 0, 0}
            };
            
            int result = BFSDFSTemplates.zeroOneBFSGrid(grid, 0, 0, 1, 2);
            
            // From (0,0) to (1,2): must go right twice and down once
            // Going down from any cell with pref=0=RIGHT costs 1
            // Minimum cost = 1 (one direction change for going down)
            assertEquals(1, result);
        }
        
        @Test
        @DisplayName("0-1 BFS minCostToReachEnd")
        void testMinCostToReachEnd() {
            int[][] grid = {
                {0, 1, 0},
                {1, 1, 0}
            };
            // 0=right, 1=down, 2=left, 3=up
            
            int result = BFSDFSTemplates.minCostToReachEnd(grid);
            
            assertTrue(result >= 0); // Should find some path
        }
    }

    // ========== 9. BIDIRECTIONAL BFS TESTS ==========
    
    @Nested
    @DisplayName("Bidirectional BFS Tests")
    class BidirectionalBFSTests {
        
        @Test
        @DisplayName("Bidirectional BFS - linear path")
        void testLinearPath() {
            List<List<Integer>> adj = createAdjacencyList(5);
            addBidirectionalEdge(adj, 0, 1);
            addBidirectionalEdge(adj, 1, 2);
            addBidirectionalEdge(adj, 2, 3);
            addBidirectionalEdge(adj, 3, 4);
            
            int result = BFSDFSTemplates.bidirectionalBFS(adj, 0, 4, 5);
            
            assertEquals(4, result);
        }
        
        @Test
        @DisplayName("Bidirectional BFS - same start and end")
        void testSameStartEnd() {
            List<List<Integer>> adj = createAdjacencyList(3);
            addBidirectionalEdge(adj, 0, 1);
            
            int result = BFSDFSTemplates.bidirectionalBFS(adj, 0, 0, 3);
            
            assertEquals(0, result);
        }
        
        @Test
        @DisplayName("Bidirectional BFS - disconnected")
        void testDisconnected() {
            List<List<Integer>> adj = createAdjacencyList(4);
            addBidirectionalEdge(adj, 0, 1);
            addBidirectionalEdge(adj, 2, 3);
            
            int result = BFSDFSTemplates.bidirectionalBFS(adj, 0, 3, 4);
            
            assertEquals(-1, result);
        }
        
        @Test
        @DisplayName("Bidirectional BFS - complete graph")
        void testCompleteGraphBidirectional() {
            List<List<Integer>> adj = createAdjacencyList(4);
            for (int i = 0; i < 4; i++) {
                for (int j = i + 1; j < 4; j++) {
                    addBidirectionalEdge(adj, i, j);
                }
            }
            
            int result = BFSDFSTemplates.bidirectionalBFS(adj, 0, 3, 4);
            
            assertEquals(1, result);
        }
        
        @Test
        @DisplayName("Bidirectional BFS Grid - basic test")
        void testBidirectionalBFSGrid() {
            int[][] grid = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
            };
            
            int result = BFSDFSTemplates.bidirectionalBFSGrid(grid, 0, 0, 2, 3);
            
            assertEquals(5, result);
        }
        
        @Test
        @DisplayName("Bidirectional BFS Grid - with obstacles")
        void testBidirectionalGridWithObstacles() {
            int[][] grid = {
                {0, 1, 0},
                {0, 1, 0},
                {0, 0, 0}
            };
            
            int result = BFSDFSTemplates.bidirectionalBFSGrid(grid, 0, 0, 0, 2);
            
            assertEquals(6, result);
        }
        
        @Test
        @DisplayName("Bidirectional BFS Grid - unreachable")
        void testBidirectionalGridUnreachable() {
            int[][] grid = {
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
            };
            
            int result = BFSDFSTemplates.bidirectionalBFSGrid(grid, 0, 0, 0, 2);
            
            assertEquals(-1, result);
        }
        
        @Test
        @DisplayName("Word Ladder - basic test")
        void testWordLadder() {
            List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
            
            int result = BFSDFSTemplates.ladderLength("hit", "cog", wordList);
            
            assertEquals(5, result); // hit -> hot -> dot -> dog -> cog
        }
        
        @Test
        @DisplayName("Word Ladder - no path")
        void testWordLadderNoPath() {
            List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log");
            
            int result = BFSDFSTemplates.ladderLength("hit", "cog", wordList);
            
            assertEquals(0, result); // cog not in dictionary
        }
        
        @Test
        @DisplayName("Word Ladder - direct path")
        void testWordLadderDirect() {
            List<String> wordList = Arrays.asList("hot");
            
            int result = BFSDFSTemplates.ladderLength("hot", "hot", wordList);
            
            assertEquals(1, result);
        }
        
        @Test
        @DisplayName("Word Ladder - single transformation")
        void testWordLadderSingleTransformation() {
            List<String> wordList = Arrays.asList("hot", "dot");
            
            int result = BFSDFSTemplates.ladderLength("hot", "dot", wordList);
            
            assertEquals(2, result);
        }
    }

    // ========== STRESS AND EDGE CASE TESTS ==========
    
    @Nested
    @DisplayName("Stress and Edge Case Tests")
    class StressTests {
        
        @Test
        @DisplayName("Large graph BFS")
        void testLargeGraphBFS() {
            int n = 1000;
            List<List<Integer>> adj = createAdjacencyList(n);
            
            // Create a chain
            for (int i = 0; i < n - 1; i++) {
                addEdge(adj, i, i + 1);
            }
            
            int[] dist = BFSDFSTemplates.graphBFS(adj, 0, n);
            
            assertEquals(0, dist[0]);
            assertEquals(999, dist[999]);
        }
        
        @Test
        @DisplayName("Large grid BFS")
        void testLargeGridBFS() {
            int[][] grid = new int[50][50]; // All zeros
            
            int result = BFSDFSTemplates.gridBFS(grid, 0, 0, 49, 49);
            
            assertEquals(98, result); // Manhattan distance
        }
        
        @Test
        @DisplayName("Dense graph with many components")
        void testDenseGraphComponents() {
            int n = 100;
            List<List<Integer>> adj = createAdjacencyList(n);
            
            // Create 10 components of 10 nodes each
            for (int comp = 0; comp < 10; comp++) {
                int base = comp * 10;
                for (int i = 0; i < 10; i++) {
                    for (int j = i + 1; j < 10; j++) {
                        addBidirectionalEdge(adj, base + i, base + j);
                    }
                }
            }
            
            int count = BFSDFSTemplates.countComponents(n, adj);
            
            assertEquals(10, count);
        }
        
        @Test
        @DisplayName("Cycle detection in large graph")
        void testCycleDetectionLarge() {
            int n = 500;
            List<List<Integer>> adj = createAdjacencyList(n);
            
            // Create a long chain
            for (int i = 0; i < n - 1; i++) {
                addEdge(adj, i, i + 1);
            }
            
            // Add cycle at the end
            addEdge(adj, n - 1, n - 10);
            
            assertTrue(BFSDFSTemplates.hasCycleDirected(n, adj));
        }
    }
}
