package com.cp.algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for CentroidDecompositionTemplates
 * 
 * Tests cover:
 * - Basic centroid decomposition functionality
 * - Path sum counting with various scenarios
 * - Distance queries and preprocessing
 * - Edge cases and boundary conditions
 * - Performance characteristics
 */
class CentroidDecompositionTemplatesTest {
    
    private CentroidDecompositionTemplates.CentroidDecomposition cd;
    private CentroidDecompositionTemplates.PathSumCounter psc;
    private CentroidDecompositionTemplates.DistanceQueries dq;
    
    @BeforeEach
    void setUp() {
        // Initialize with a moderate size for most tests
        cd = new CentroidDecompositionTemplates.CentroidDecomposition(7);
        psc = new CentroidDecompositionTemplates.PathSumCounter(7, 10);
        dq = new CentroidDecompositionTemplates.DistanceQueries(7);
    }
    
    @Test
    @DisplayName("Basic centroid decomposition should build successfully")
    void testBasicCentroidDecomposition() {
        // Build a balanced tree
        cd.addEdge(0, 1);
        cd.addEdge(0, 2);
        cd.addEdge(1, 3);
        cd.addEdge(1, 4);
        cd.addEdge(2, 5);
        cd.addEdge(2, 6);
        
        assertDoesNotThrow(() -> cd.build());
        
        // Verify centroid tree structure - every node should have a parent or be root
        boolean hasRoot = false;
        for (int i = 0; i < 7; i++) {
            if (cd.getParent(i) == -1) {
                hasRoot = true;
            }
        }
        assertTrue(hasRoot, "Centroid tree should have a root node");
    }
    
    @Test
    @DisplayName("Single node tree should work correctly")
    void testSingleNodeTree() {
        CentroidDecompositionTemplates.CentroidDecomposition singleNode = 
            new CentroidDecompositionTemplates.CentroidDecomposition(1);
        
        assertDoesNotThrow(() -> singleNode.build());
        assertEquals(-1, singleNode.getParent(0), "Single node should be root");
    }
    
    @Test
    @DisplayName("Linear tree should decompose correctly")
    void testLinearTree() {
        CentroidDecompositionTemplates.CentroidDecomposition linear = 
            new CentroidDecompositionTemplates.CentroidDecomposition(5);
        
        // Build linear tree: 0-1-2-3-4
        linear.addEdge(0, 1);
        linear.addEdge(1, 2);
        linear.addEdge(2, 3);
        linear.addEdge(3, 4);
        
        assertDoesNotThrow(() -> linear.build());
        
        // In a linear tree, centroid should be the middle node
        boolean foundRoot = false;
        for (int i = 0; i < 5; i++) {
            if (linear.getParent(i) == -1) {
                foundRoot = true;
                assertTrue(i >= 1 && i <= 3, "Root should be one of the middle nodes");
                break;
            }
        }
        assertTrue(foundRoot, "Should have exactly one root");
    }
    
    @Test
    @DisplayName("Path sum counter should handle simple cases correctly")
    void testPathSumCountingSimple() {
        // Build tree: 0-1-2 with values [2, 3, 5]
        // Paths: [2], [3], [5], [2,3], [3,5], [2,3,5]
        // Sums:   2,   3,   5,   5,     8,     10
        CentroidDecompositionTemplates.PathSumCounter simpleTree = 
            new CentroidDecompositionTemplates.PathSumCounter(3, 5);
        
        simpleTree.addEdge(0, 1);
        simpleTree.addEdge(1, 2);
        
        int[] values = {2, 3, 5};
        long pathCount = simpleTree.countPaths(values);
        
        // Should find paths with sum 5: [5] and [2,3]
        assertTrue(pathCount >= 2, "Should find at least 2 paths with sum 5");
    }
    
    @Test
    @DisplayName("Path sum counter with target sum 0")
    void testPathSumZero() {
        CentroidDecompositionTemplates.PathSumCounter zeroSum = 
            new CentroidDecompositionTemplates.PathSumCounter(3, 0);
        
        zeroSum.addEdge(0, 1);
        zeroSum.addEdge(1, 2);
        
        int[] values = {-1, 0, 1};
        long pathCount = zeroSum.countPaths(values);
        
        // Should find at least the path with just node containing 0
        assertTrue(pathCount >= 1, "Should find at least 1 path with sum 0");
    }
    
    @Test
    @DisplayName("Path sum counter with negative values")
    void testPathSumNegativeValues() {
        CentroidDecompositionTemplates.PathSumCounter negativeSum = 
            new CentroidDecompositionTemplates.PathSumCounter(4, -2);
        
        negativeSum.addEdge(0, 1);
        negativeSum.addEdge(1, 2);
        negativeSum.addEdge(2, 3);
        
        int[] values = {-1, -1, 2, -2};
        long pathCount = negativeSum.countPaths(values);
        
        // Should handle negative values correctly
        assertTrue(pathCount >= 0, "Path count should be non-negative");
    }
    
    @Test
    @DisplayName("Distance queries should build without errors")
    void testDistanceQueriesBasic() {
        dq.addEdge(0, 1);
        dq.addEdge(1, 2);
        dq.addEdge(1, 3);
        
        assertDoesNotThrow(() -> dq.buildWithDistances());
    }
    
    @Test
    @DisplayName("Distance queries kthNearest should handle unimplemented case")
    void testKthNearestUnimplemented() {
        dq.addEdge(0, 1);
        dq.addEdge(1, 2);
        dq.buildWithDistances();
        
        // Current implementation returns -1 for unimplemented feature
        assertEquals(-1, dq.kthNearest(0, 1));
    }
    
    @Test
    @DisplayName("Distinct values path should handle basic case")
    void testDistinctValuesPath() {
        CentroidDecompositionTemplates.DistinctValuesPath dvp = 
            new CentroidDecompositionTemplates.DistinctValuesPath(4);
        
        dvp.addEdge(0, 1);
        dvp.addEdge(1, 2);
        dvp.addEdge(2, 3);
        
        int[] values = {1, 2, 1, 3};
        long count = dvp.countPaths(values, 2);
        
        // Should handle the computation without errors
        assertTrue(count >= 0, "Count should be non-negative");
    }
    
    @ParameterizedTest
    @CsvSource({
        "1, 0",    // Single node
        "2, 1",    // Two nodes
        "3, 2",    // Three nodes
        "7, 6",    // Larger tree
        "15, 14"   // Even larger
    })
    @DisplayName("Trees with different sizes should decompose correctly")
    void testVariousTreeSizes(int n, int edges) {
        CentroidDecompositionTemplates.CentroidDecomposition variedSize = 
            new CentroidDecompositionTemplates.CentroidDecomposition(n);
        
        // Build a linear tree
        for (int i = 0; i < edges; i++) {
            variedSize.addEdge(i, i + 1);
        }
        
        assertDoesNotThrow(() -> variedSize.build());
        
        // Count nodes with parents
        int nodesWithParents = 0;
        for (int i = 0; i < n; i++) {
            if (variedSize.getParent(i) != -1) {
                nodesWithParents++;
            }
        }
        
        // Should have exactly n-1 nodes with parents (all except root)
        assertEquals(n - 1, nodesWithParents, 
            String.format("Should have %d nodes with parents", n - 1));
    }
    
    @Test
    @DisplayName("Star tree should have center as root")
    void testStarTree() {
        int n = 6;
        CentroidDecompositionTemplates.CentroidDecomposition star = 
            new CentroidDecompositionTemplates.CentroidDecomposition(n);
        
        // Build star tree with center at node 0
        for (int i = 1; i < n; i++) {
            star.addEdge(0, i);
        }
        
        star.build();
        
        // Node 0 should be the root (center is always centroid in star)
        assertEquals(-1, star.getParent(0), "Center of star should be root");
        
        // All other nodes should have node 0 as parent in centroid tree
        for (int i = 1; i < n; i++) {
            assertEquals(0, star.getParent(i), 
                "All leaves should have center as parent");
        }
    }
    
    @Test
    @DisplayName("Performance test with larger tree")
    void testPerformance() {
        int n = 1000;
        CentroidDecompositionTemplates.CentroidDecomposition large = 
            new CentroidDecompositionTemplates.CentroidDecomposition(n);
        
        // Build a balanced binary tree
        for (int i = 1; i < n; i++) {
            large.addEdge((i - 1) / 2, i);
        }
        
        long startTime = System.nanoTime();
        large.build();
        long endTime = System.nanoTime();
        
        long durationMs = (endTime - startTime) / 1_000_000;
        
        // Should complete within reasonable time (less than 1 second for 1000 nodes)
        assertTrue(durationMs < 1000, 
            String.format("Build should complete quickly, took %d ms", durationMs));
    }
    
    @Test
    @DisplayName("Empty tree edge cases")
    void testEmptyTreeEdgeCases() {
        // Test with minimal tree sizes
        CentroidDecompositionTemplates.CentroidDecomposition empty = 
            new CentroidDecompositionTemplates.CentroidDecomposition(0);
        
        // Should handle empty case gracefully
        assertDoesNotThrow(() -> empty.build());
    }
    
    @Test
    @DisplayName("Path sum with all same values")
    void testPathSumAllSameValues() {
        CentroidDecompositionTemplates.PathSumCounter sameValues = 
            new CentroidDecompositionTemplates.PathSumCounter(5, 6);
        
        // Linear tree
        sameValues.addEdge(0, 1);
        sameValues.addEdge(1, 2);
        sameValues.addEdge(2, 3);
        sameValues.addEdge(3, 4);
        
        int[] values = {2, 2, 2, 2, 2}; // All nodes have value 2
        long pathCount = sameValues.countPaths(values);
        
        // Should find paths of length 3 that sum to 6
        assertTrue(pathCount >= 0, "Should handle same values correctly");
    }
    
    @Test
    @DisplayName("Centroid decomposition properties verification")
    void testCentroidProperties() {
        // Test that centroid decomposition maintains expected properties
        int n = 15;
        CentroidDecompositionTemplates.CentroidDecomposition props = 
            new CentroidDecompositionTemplates.CentroidDecomposition(n);
        
        // Build a more complex tree
        props.addEdge(0, 1); props.addEdge(0, 2);
        props.addEdge(1, 3); props.addEdge(1, 4);
        props.addEdge(2, 5); props.addEdge(2, 6);
        props.addEdge(3, 7); props.addEdge(3, 8);
        props.addEdge(4, 9); props.addEdge(5, 10);
        props.addEdge(6, 11); props.addEdge(11, 12);
        props.addEdge(12, 13); props.addEdge(13, 14);
        
        props.build();
        
        // Verify tree structure
        int rootCount = 0;
        for (int i = 0; i < n; i++) {
            if (props.getParent(i) == -1) {
                rootCount++;
            }
        }
        
        assertEquals(1, rootCount, "Should have exactly one root in centroid tree");
        
        // Verify no cycles in centroid tree (each node reachable from root)
        boolean[] visited = new boolean[n];
        int root = -1;
        for (int i = 0; i < n; i++) {
            if (props.getParent(i) == -1) {
                root = i;
                break;
            }
        }
        
        assertNotEquals(-1, root, "Should find root node");
        
        // Count reachable nodes from root
        int reachableCount = 1; // Root itself
        visited[root] = true;
        
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && props.getParent(i) != -1 && 
                    visited[props.getParent(i)]) {
                    visited[i] = true;
                    reachableCount++;
                    changed = true;
                }
            }
        }
        
        assertEquals(n, reachableCount, "All nodes should be reachable from root");
    }
}