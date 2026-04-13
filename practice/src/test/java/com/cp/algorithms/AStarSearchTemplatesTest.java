package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for AStarSearchTemplates.
 * 
 * Tests all A* algorithm variants with:
 * - Basic functionality verification
 * - Edge cases (no path, same start/end, blocked cells)
 * - Path correctness and optimality
 * - Different grid sizes and configurations
 * - Heuristic admissibility checks
 * - Performance characteristics
 */
@DisplayName("A* Search Templates Test Suite")
class AStarSearchTemplatesTest {

    // ============================================================================
    // GRID A* - 4 DIRECTIONAL TESTS
    // ============================================================================
    
    @Test
    @DisplayName("GridAStar 4-dir: Simple path in small grid")
    void testGridAStar4Dir_SimplePath() {
        int[][] grid = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{2, 2}
        );
        assertEquals(4, result, "Shortest 4-dir path from (0,0) to (2,2) should be 4");
    }
    
    @Test
    @DisplayName("GridAStar 4-dir: Path with obstacles")
    void testGridAStar4Dir_WithObstacles() {
        int[][] grid = {
            {0, 0, 0, 0},
            {1, 1, 0, 1},
            {0, 0, 0, 0},
            {0, 1, 1, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{3, 3}
        );
        assertEquals(6, result, "Should navigate around obstacles");
    }
    
    @Test
    @DisplayName("GridAStar 4-dir: No path exists")
    void testGridAStar4Dir_NoPath() {
        int[][] grid = {
            {0, 1, 0},
            {1, 1, 1},
            {0, 1, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{2, 2}
        );
        assertEquals(-1, result, "Should return -1 when no path exists");
    }
    
    @Test
    @DisplayName("GridAStar 4-dir: Start equals end")
    void testGridAStar4Dir_StartEqualsEnd() {
        int[][] grid = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{1, 1}, new int[]{1, 1}
        );
        assertEquals(0, result, "Distance should be 0 when start equals end");
    }
    
    @Test
    @DisplayName("GridAStar 4-dir: Start is blocked")
    void testGridAStar4Dir_StartBlocked() {
        int[][] grid = {
            {1, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{2, 2}
        );
        assertEquals(-1, result, "Should return -1 when start is blocked");
    }
    
    @Test
    @DisplayName("GridAStar 4-dir: End is blocked")
    void testGridAStar4Dir_EndBlocked() {
        int[][] grid = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 1}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{2, 2}
        );
        assertEquals(-1, result, "Should return -1 when end is blocked");
    }
    
    @Test
    @DisplayName("GridAStar 4-dir: Single cell grid")
    void testGridAStar4Dir_SingleCell() {
        int[][] grid = {{0}};
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{0, 0}
        );
        assertEquals(0, result, "Single cell with same start/end should return 0");
    }
    
    @Test
    @DisplayName("GridAStar 4-dir: Adjacent cells")
    void testGridAStar4Dir_AdjacentCells() {
        int[][] grid = {
            {0, 0},
            {0, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{0, 1}
        );
        assertEquals(1, result, "Adjacent cells should have distance 1");
    }
    
    @Test
    @DisplayName("GridAStar 4-dir: Long corridor")
    void testGridAStar4Dir_LongCorridor() {
        int[][] grid = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{0, 9}
        );
        assertEquals(9, result, "Straight corridor distance should equal cell difference");
    }
    
    @Test
    @DisplayName("GridAStar 4-dir: Maze with multiple paths")
    void testGridAStar4Dir_MultiplePaths() {
        int[][] grid = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{4, 4}
        );
        // Optimal path goes through middle opening
        assertTrue(result > 0 && result <= 12, "Should find one of the valid paths");
    }

    // ============================================================================
    // GRID A* - 8 DIRECTIONAL TESTS
    // ============================================================================
    
    @Test
    @DisplayName("GridAStar 8-dir: Diagonal path")
    void testGridAStar8Dir_DiagonalPath() {
        int[][] grid = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath8Dir(
            grid, new int[]{0, 0}, new int[]{2, 2}
        );
        assertEquals(2, result, "Diagonal path should be shorter than 4-directional");
    }
    
    @Test
    @DisplayName("GridAStar 8-dir: Path with obstacles")
    void testGridAStar8Dir_WithObstacles() {
        int[][] grid = {
            {0, 0, 0, 0},
            {1, 1, 0, 1},
            {0, 0, 0, 0},
            {0, 1, 1, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath8Dir(
            grid, new int[]{0, 0}, new int[]{3, 3}
        );
        assertEquals(4, result, "8-dir should use diagonal moves to shorten path");
    }
    
    @Test
    @DisplayName("GridAStar 8-dir: No path")
    void testGridAStar8Dir_NoPath() {
        int[][] grid = {
            {0, 1, 0},
            {1, 1, 1},
            {0, 1, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath8Dir(
            grid, new int[]{0, 0}, new int[]{2, 2}
        );
        assertEquals(-1, result, "Should return -1 when no path exists");
    }
    
    @Test
    @DisplayName("GridAStar 8-dir: Pure diagonal")
    void testGridAStar8Dir_PureDiagonal() {
        int[][] grid = {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
        };
        int result = AStarSearchTemplates.GridAStar.shortestPath8Dir(
            grid, new int[]{0, 0}, new int[]{4, 4}
        );
        assertEquals(4, result, "Pure diagonal should be min(dx, dy)");
    }

    // ============================================================================
    // PATH RECONSTRUCTION TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Path reconstruction: Valid path returned")
    void testPathReconstruction_ValidPath() {
        int[][] grid = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        List<int[]> path = AStarSearchTemplates.GridAStar.shortestPathWithRoute(
            grid, new int[]{0, 0}, new int[]{2, 2}
        );
        
        assertNotNull(path, "Path should not be null");
        assertTrue(path.size() > 0, "Path should contain cells");
        
        // Verify start
        assertArrayEquals(new int[]{0, 0}, path.get(0), "Path should start at start position");
        
        // Verify end
        int[] lastCell = path.get(path.size() - 1);
        assertArrayEquals(new int[]{2, 2}, lastCell, "Path should end at end position");
        
        // Verify continuity (each step is adjacent)
        for (int i = 0; i < path.size() - 1; i++) {
            int[] curr = path.get(i);
            int[] next = path.get(i + 1);
            int dx = Math.abs(curr[0] - next[0]);
            int dy = Math.abs(curr[1] - next[1]);
            assertTrue(dx + dy == 1, "Path should have adjacent 4-directional steps");
        }
    }
    
    @Test
    @DisplayName("Path reconstruction: No path returns null")
    void testPathReconstruction_NoPath() {
        int[][] grid = {
            {0, 1},
            {1, 0}
        };
        List<int[]> path = AStarSearchTemplates.GridAStar.shortestPathWithRoute(
            grid, new int[]{0, 0}, new int[]{1, 1}
        );
        assertNull(path, "Should return null when no path exists");
    }
    
    @Test
    @DisplayName("Path reconstruction: Start equals end")
    void testPathReconstruction_StartEqualsEnd() {
        int[][] grid = {{0, 0}, {0, 0}};
        List<int[]> path = AStarSearchTemplates.GridAStar.shortestPathWithRoute(
            grid, new int[]{1, 1}, new int[]{1, 1}
        );
        
        // Could be null or single element path
        // Either implementation is acceptable
        if (path != null) {
            assertTrue(path.size() >= 1, "Path should have at least start position");
        }
    }
    
    @Test
    @DisplayName("Path reconstruction: Blocked start")
    void testPathReconstruction_BlockedStart() {
        int[][] grid = {{1, 0}, {0, 0}};
        List<int[]> path = AStarSearchTemplates.GridAStar.shortestPathWithRoute(
            grid, new int[]{0, 0}, new int[]{1, 1}
        );
        assertNull(path, "Should return null when start is blocked");
    }

    // ============================================================================
    // OBSTACLE ELIMINATION TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Obstacle elimination: Basic elimination")
    void testObstacleElimination_Basic() {
        int[][] grid = {
            {0, 1, 1},
            {0, 1, 0},
            {0, 0, 0}
        };
        int result = AStarSearchTemplates.AStarWithObstacleElimination
            .shortestPathWithElimination(grid, 1);
        assertEquals(4, result, "Should find path with 1 obstacle elimination");
    }
    
    @Test
    @DisplayName("Obstacle elimination: No elimination needed")
    void testObstacleElimination_NoEliminationNeeded() {
        int[][] grid = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        int result = AStarSearchTemplates.AStarWithObstacleElimination
            .shortestPathWithElimination(grid, 5);
        assertEquals(4, result, "Should find direct path without eliminating obstacles");
    }
    
    @Test
    @DisplayName("Obstacle elimination: Insufficient eliminations")
    void testObstacleElimination_InsufficientEliminations() {
        int[][] grid = {
            {0, 1, 1, 1},
            {0, 1, 1, 1},
            {0, 0, 0, 0}
        };
        int result = AStarSearchTemplates.AStarWithObstacleElimination
            .shortestPathWithElimination(grid, 1);
        // Path through bottom row: (0,0)->(1,0)->(2,0)->(2,1)->(2,2)->(2,3) = 5 steps
        assertEquals(5, result, "Should find best path with available eliminations");
    }
    
    @Test
    @DisplayName("Obstacle elimination: Zero eliminations allowed")
    void testObstacleElimination_ZeroEliminations() {
        int[][] grid = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        int result = AStarSearchTemplates.AStarWithObstacleElimination
            .shortestPathWithElimination(grid, 0);
        assertEquals(4, result, "Should find path without any eliminations");
    }
    
    @Test
    @DisplayName("Obstacle elimination: Eliminate all obstacles")
    void testObstacleElimination_EliminateAll() {
        int[][] grid = {
            {0, 1, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        int result = AStarSearchTemplates.AStarWithObstacleElimination
            .shortestPathWithElimination(grid, 10);
        assertEquals(4, result, "Should find optimal path when all obstacles can be eliminated");
    }
    
    @Test
    @DisplayName("Obstacle elimination: Single cell grid")
    void testObstacleElimination_SingleCell() {
        int[][] grid = {{0}};
        int result = AStarSearchTemplates.AStarWithObstacleElimination
            .shortestPathWithElimination(grid, 0);
        assertEquals(0, result, "Single cell should return 0");
    }
    
    @Test
    @DisplayName("Obstacle elimination: All blocked no way")
    void testObstacleElimination_CompletelyBlocked() {
        int[][] grid = {
            {0, 1, 1},
            {1, 1, 1},
            {1, 1, 0}
        };
        int result = AStarSearchTemplates.AStarWithObstacleElimination
            .shortestPathWithElimination(grid, 2);
        // Need more than 2 eliminations
        assertEquals(-1, result, "Should return -1 when eliminations are insufficient");
    }

    // ============================================================================
    // WEIGHTED GRAPH A* TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Weighted graph: Simple weighted path")
    void testWeightedGraph_SimplePath() {
        @SuppressWarnings("unchecked")
        List<AStarSearchTemplates.WeightedGraphAStar.Edge>[] graph = new ArrayList[5];
        for (int i = 0; i < 5; i++) graph[i] = new ArrayList<>();
        
        graph[0].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(1, 4));
        graph[0].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(2, 1));
        graph[1].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(3, 1));
        graph[2].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(1, 2));
        graph[2].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(3, 5));
        graph[3].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(4, 3));
        
        int result = AStarSearchTemplates.WeightedGraphAStar.astar(
            graph, 0, 4, node -> Math.max(0, 4 - node)
        );
        assertEquals(7, result, "Should find path 0->2->1->3->4 with cost 7");
    }
    
    @Test
    @DisplayName("Weighted graph: No path")
    void testWeightedGraph_NoPath() {
        @SuppressWarnings("unchecked")
        List<AStarSearchTemplates.WeightedGraphAStar.Edge>[] graph = new ArrayList[3];
        for (int i = 0; i < 3; i++) graph[i] = new ArrayList<>();
        
        graph[0].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(1, 5));
        // Node 2 is disconnected
        
        int result = AStarSearchTemplates.WeightedGraphAStar.astar(
            graph, 0, 2, node -> 0
        );
        assertEquals(-1, result, "Should return -1 when no path exists");
    }
    
    @Test
    @DisplayName("Weighted graph: Start equals goal")
    void testWeightedGraph_StartEqualsGoal() {
        @SuppressWarnings("unchecked")
        List<AStarSearchTemplates.WeightedGraphAStar.Edge>[] graph = new ArrayList[3];
        for (int i = 0; i < 3; i++) graph[i] = new ArrayList<>();
        
        graph[0].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(1, 5));
        
        int result = AStarSearchTemplates.WeightedGraphAStar.astar(
            graph, 1, 1, node -> 0
        );
        assertEquals(0, result, "Should return 0 when start equals goal");
    }
    
    @Test
    @DisplayName("Weighted graph: Multiple paths, choose shortest")
    void testWeightedGraph_MultiplePathsChooseShortest() {
        @SuppressWarnings("unchecked")
        List<AStarSearchTemplates.WeightedGraphAStar.Edge>[] graph = new ArrayList[4];
        for (int i = 0; i < 4; i++) graph[i] = new ArrayList<>();
        
        // Direct path: 0->3 = 10
        graph[0].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(3, 10));
        // Via path: 0->1->2->3 = 2+2+2 = 6
        graph[0].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(1, 2));
        graph[1].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(2, 2));
        graph[2].add(new AStarSearchTemplates.WeightedGraphAStar.Edge(3, 2));
        
        int result = AStarSearchTemplates.WeightedGraphAStar.astar(
            graph, 0, 3, node -> 0
        );
        assertEquals(6, result, "Should choose shorter path");
    }

    // ============================================================================
    // BIDIRECTIONAL A* TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Bidirectional: Simple path")
    void testBidirectional_SimplePath() {
        int[][] grid = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        int result = AStarSearchTemplates.BidirectionalAStar.bidirectionalSearch(
            grid, new int[]{0, 0}, new int[]{2, 2}
        );
        assertEquals(4, result, "Should find shortest path");
    }
    
    @Test
    @DisplayName("Bidirectional: With obstacles")
    void testBidirectional_WithObstacles() {
        int[][] grid = {
            {0, 0, 0, 0},
            {1, 1, 0, 1},
            {0, 0, 0, 0},
            {0, 1, 1, 0}
        };
        int result = AStarSearchTemplates.BidirectionalAStar.bidirectionalSearch(
            grid, new int[]{0, 0}, new int[]{3, 3}
        );
        assertEquals(6, result, "Should find path around obstacles");
    }
    
    @Test
    @DisplayName("Bidirectional: No path")
    void testBidirectional_NoPath() {
        int[][] grid = {
            {0, 1},
            {1, 0}
        };
        int result = AStarSearchTemplates.BidirectionalAStar.bidirectionalSearch(
            grid, new int[]{0, 0}, new int[]{1, 1}
        );
        assertEquals(-1, result, "Should return -1 when no path exists");
    }
    
    @Test
    @DisplayName("Bidirectional: Start equals end")
    void testBidirectional_StartEqualsEnd() {
        int[][] grid = {
            {0, 0},
            {0, 0}
        };
        int result = AStarSearchTemplates.BidirectionalAStar.bidirectionalSearch(
            grid, new int[]{1, 1}, new int[]{1, 1}
        );
        assertEquals(0, result, "Should return 0 when start equals end");
    }

    // ============================================================================
    // IDA* (ITERATIVE DEEPENING A*) TESTS
    // ============================================================================
    
    @Test
    @DisplayName("IDA*: Simple path")
    void testIDAStar_SimplePath() {
        int[][] grid = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        int result = AStarSearchTemplates.IterativeDeepeningAStar.idastar(
            grid, new int[]{0, 0}, new int[]{2, 2}
        );
        assertEquals(4, result, "Should find shortest path with IDA*");
    }
    
    @Test
    @DisplayName("IDA*: Straight line")
    void testIDAStar_StraightLine() {
        int[][] grid = {
            {0, 0, 0, 0, 0}
        };
        int result = AStarSearchTemplates.IterativeDeepeningAStar.idastar(
            grid, new int[]{0, 0}, new int[]{0, 4}
        );
        assertEquals(4, result, "Should find straight path");
    }
    
    @Test
    @DisplayName("IDA*: No path")
    void testIDAStar_NoPath() {
        int[][] grid = {
            {0, 1},
            {1, 0}
        };
        int result = AStarSearchTemplates.IterativeDeepeningAStar.idastar(
            grid, new int[]{0, 0}, new int[]{1, 1}
        );
        assertEquals(-1, result, "Should return -1 when no path exists");
    }
    
    @Test
    @DisplayName("IDA*: Complex maze")
    void testIDAStar_ComplexMaze() {
        int[][] grid = {
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0}
        };
        int result = AStarSearchTemplates.IterativeDeepeningAStar.idastar(
            grid, new int[]{0, 0}, new int[]{4, 4}
        );
        assertTrue(result > 0, "Should find a path in complex maze");
    }

    // ============================================================================
    // HEURISTIC TESTS - VERIFY ADMISSIBILITY
    // ============================================================================
    
    @Test
    @DisplayName("Heuristic: Manhattan never overestimates")
    void testManhattanHeuristic_Admissible() {
        // Manhattan distance should never overestimate actual 4-dir path
        int[][] grid = {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
        };
        
        // For empty grid, actual distance == Manhattan distance (4-dir)
        int actual = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{4, 4}
        );
        int manhattan = Math.abs(0 - 4) + Math.abs(0 - 4);
        
        assertEquals(manhattan, actual, "In empty grid, actual should equal Manhattan");
    }
    
    @Test
    @DisplayName("Heuristic: Chebyshev never overestimates for 8-dir")
    void testChebyshevHeuristic_Admissible() {
        // Chebyshev distance should never overestimate actual 8-dir path
        int[][] grid = {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
        };
        
        int actual = AStarSearchTemplates.GridAStar.shortestPath8Dir(
            grid, new int[]{0, 0}, new int[]{4, 4}
        );
        int chebyshev = Math.max(Math.abs(0 - 4), Math.abs(0 - 4));
        
        assertEquals(chebyshev, actual, "In empty grid, actual should equal Chebyshev for 8-dir");
    }

    // ============================================================================
    // EDGE CASE TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Edge case: Large empty grid")
    void testLargeEmptyGrid() {
        int size = 50;
        int[][] grid = new int[size][size];
        
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{size - 1, size - 1}
        );
        
        assertEquals(2 * (size - 1), result, "Should handle large grids efficiently");
    }
    
    @Test
    @DisplayName("Edge case: Narrow passage")
    void testNarrowPassage() {
        int[][] grid = {
            {0, 1, 1, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 1, 1, 0}
        };
        
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{4, 4}
        );
        
        assertTrue(result > 0, "Should find path through narrow passage");
    }
    
    @Test
    @DisplayName("Edge case: Spiral maze")
    void testSpiralMaze() {
        int[][] grid = {
            {0, 0, 0, 0, 0},
            {1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1},
            {0, 0, 0, 0, 0}
        };
        
        int result = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{4, 4}
        );
        
        assertTrue(result > 0, "Should navigate spiral maze");
    }

    // ============================================================================
    // COMPARISON TESTS - A* VS OTHER ALGORITHMS
    // ============================================================================
    
    @Test
    @DisplayName("Comparison: 4-dir vs 8-dir (8-dir should be shorter or equal)")
    void testComparison_4DirVs8Dir() {
        int[][] grid = {
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0}
        };
        
        int result4Dir = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{4, 4}
        );
        
        int result8Dir = AStarSearchTemplates.GridAStar.shortestPath8Dir(
            grid, new int[]{0, 0}, new int[]{4, 4}
        );
        
        assertTrue(result8Dir <= result4Dir, 
            "8-directional should find path <= 4-directional");
    }
    
    @Test
    @DisplayName("Comparison: Bidirectional should match unidirectional")
    void testComparison_BidirectionalVsUnidirectional() {
        int[][] grid = {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
        };
        
        int resultUnidirectional = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{4, 3}
        );
        
        int resultBidirectional = AStarSearchTemplates.BidirectionalAStar.bidirectionalSearch(
            grid, new int[]{0, 0}, new int[]{4, 3}
        );
        
        assertEquals(resultUnidirectional, resultBidirectional,
            "Bidirectional and unidirectional should find same distance");
    }
    
    @Test
    @DisplayName("Comparison: IDA* should match regular A*")
    void testComparison_IDAStarVsAStar() {
        int[][] grid = {
            {0, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0}
        };
        
        int resultAStar = AStarSearchTemplates.GridAStar.shortestPath4Dir(
            grid, new int[]{0, 0}, new int[]{2, 3}
        );
        
        int resultIDAStar = AStarSearchTemplates.IterativeDeepeningAStar.idastar(
            grid, new int[]{0, 0}, new int[]{2, 3}
        );
        
        assertEquals(resultAStar, resultIDAStar,
            "IDA* and A* should find same distance");
    }
}
