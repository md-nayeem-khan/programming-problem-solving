package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Heavy-Light Decomposition
 * Tests cover:
 * - Path queries (sum)
 * - Path updates
 * - Point updates
 * - LCA (Lowest Common Ancestor)
 * - Distance calculations
 * - Edge cases (single node, linear tree, complete binary tree, star tree)
 * - Large trees
 */
class HeavyLightDecompositionTest {

    @Test
    @DisplayName("Test basic path query on example tree")
    void testBasicPathQuery() {
        // Example tree from main():
        //       0(1)
        //      / \
        //    1(2) 2(3)
        //    / \   \
        //  3(4) 4(5) 5(6)
        //       /
        //      6(7)
        
        int n = 7;
        int[][] edges = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {4, 6}
        };
        int[] values = {1, 2, 3, 4, 5, 6, 7}; // Values at nodes 0-6
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        // Path 3 -> 6: 3(4) -> 1(2) -> 4(5) -> 6(7) = 4+2+5+7 = 18
        assertEquals(18, hld.pathQuery(3, 6), "Path sum from 3 to 6");
        
        // Path 3 -> 5: 3(4) -> 1(2) -> 0(1) -> 2(3) -> 5(6) = 4+2+1+3+6 = 16
        assertEquals(16, hld.pathQuery(3, 5), "Path sum from 3 to 5");
        
        // Path from node to itself
        assertEquals(1, hld.pathQuery(0, 0), "Path sum from 0 to 0");
        assertEquals(4, hld.pathQuery(3, 3), "Path sum from 3 to 3");
        
        // Path between parent and child
        assertEquals(3, hld.pathQuery(0, 1), "Path sum from 0 to 1 (parent-child)");
        assertEquals(7, hld.pathQuery(1, 4), "Path sum from 1 to 4 (parent-child)");
    }

    @Test
    @DisplayName("Test point update")
    void testPointUpdate() {
        int n = 7;
        int[][] edges = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {4, 6}
        };
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        // Update node 4 from 5 to 10
        hld.update(4, 10);
        
        // Path 3 -> 6 should now be: 4+2+10+7 = 23
        assertEquals(23, hld.pathQuery(3, 6), "Path sum after updating node 4");
        
        // Update node 0 from 1 to 100
        hld.update(0, 100);
        assertEquals(100, hld.pathQuery(0, 0), "Updated node value");
        
        // Path 3 -> 5 should now be: 4+2+100+3+6 = 115
        assertEquals(115, hld.pathQuery(3, 5), "Path sum after updating root");
    }

    @Test
    @DisplayName("Test path update")
    void testPathUpdate() {
        int n = 7;
        int[][] edges = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {4, 6}
        };
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        long before = hld.pathQuery(3, 5);
        
        // Add 1 to all nodes on path 3 -> 5
        // Path: 3(4) -> 1(2) -> 0(1) -> 2(3) -> 5(6)
        // After: 3(5) -> 1(3) -> 0(2) -> 2(4) -> 5(7)
        hld.pathUpdate(3, 5, 1);
        
        long after = hld.pathQuery(3, 5);
        assertEquals(before + 5, after, "Path sum should increase by 5 (5 nodes on path)");
        
        // Update path from node to itself
        hld.pathUpdate(6, 6, 10);
        assertEquals(17, hld.pathQuery(6, 6), "Single node path update: 7+10=17");
    }

    @Test
    @DisplayName("Test LCA (Lowest Common Ancestor)")
    void testLCA() {
        int n = 7;
        int[][] edges = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {4, 6}
        };
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        assertEquals(1, hld.lca(3, 6), "LCA of 3 and 6 should be 1");
        assertEquals(0, hld.lca(3, 5), "LCA of 3 and 5 should be 0");
        assertEquals(0, hld.lca(1, 2), "LCA of 1 and 2 should be 0");
        assertEquals(1, hld.lca(3, 4), "LCA of 3 and 4 should be 1");
        assertEquals(4, hld.lca(4, 6), "LCA of 4 and 6 should be 4");
        
        // LCA of node with itself
        assertEquals(0, hld.lca(0, 0), "LCA of node with itself");
        assertEquals(3, hld.lca(3, 3), "LCA of node with itself");
    }

    @Test
    @DisplayName("Test distance between nodes")
    void testDistance() {
        int n = 7;
        int[][] edges = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {4, 6}
        };
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        assertEquals(3, hld.distance(3, 6), "Distance from 3 to 6");
        assertEquals(4, hld.distance(3, 5), "Distance from 3 to 5");
        assertEquals(2, hld.distance(1, 2), "Distance from 1 to 2");
        assertEquals(1, hld.distance(0, 1), "Distance from 0 to 1");
        assertEquals(1, hld.distance(4, 6), "Distance from 4 to 6");
        
        // Distance from node to itself
        assertEquals(0, hld.distance(0, 0), "Distance from node to itself");
        assertEquals(0, hld.distance(5, 5), "Distance from node to itself");
    }

    @Test
    @DisplayName("Test single node tree")
    void testSingleNode() {
        int n = 1;
        int[][] edges = {};
        int[] values = {42};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        assertEquals(42, hld.pathQuery(0, 0), "Single node path query");
        assertEquals(0, hld.lca(0, 0), "Single node LCA");
        assertEquals(0, hld.distance(0, 0), "Single node distance");
        
        hld.update(0, 100);
        assertEquals(100, hld.pathQuery(0, 0), "Single node after update");
        
        hld.pathUpdate(0, 0, 50);
        assertEquals(150, hld.pathQuery(0, 0), "Single node after path update");
    }

    @Test
    @DisplayName("Test linear tree (chain)")
    void testLinearTree() {
        // Linear tree: 0 - 1 - 2 - 3 - 4
        int n = 5;
        int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 4}
        };
        int[] values = {1, 2, 3, 4, 5}; // nodes 0-4
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        // Path from 0 to 4: 1+2+3+4+5 = 15
        assertEquals(15, hld.pathQuery(0, 4), "Path sum across entire linear tree");
        
        // Path from 1 to 3: 2+3+4 = 9
        assertEquals(9, hld.pathQuery(1, 3), "Path sum in middle of linear tree");
        
        // LCA tests
        assertEquals(0, hld.lca(0, 4), "LCA in linear tree");
        assertEquals(1, hld.lca(1, 4), "LCA in linear tree");
        assertEquals(2, hld.lca(2, 4), "LCA in linear tree");
        
        // Distance tests
        assertEquals(4, hld.distance(0, 4), "Distance in linear tree");
        assertEquals(2, hld.distance(1, 3), "Distance in linear tree");
    }

    @Test
    @DisplayName("Test complete binary tree")
    void testCompleteBinaryTree() {
        //         0(1)
        //       /     \
        //     1(2)    2(3)
        //     / \     / \
        //   3(4) 4(5) 5(6) 6(7)
        
        int n = 7;
        int[][] edges = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {2, 6}
        };
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        // Path from leaf to leaf
        // Path 3->4: node3(4) -> node1(2) -> node4(5) = 4+2+5 = 11
        assertEquals(11, hld.pathQuery(3, 4), "Path between siblings: 4+2+5 = 11");
        // Path 3->5: node3(4) -> node1(2) -> node0(1) -> node2(3) -> node5(6) = 4+2+1+3+6 = 16
        assertEquals(16, hld.pathQuery(3, 5), "Path across subtrees");
        
        // LCA tests
        assertEquals(1, hld.lca(3, 4), "LCA of siblings");
        assertEquals(0, hld.lca(3, 5), "LCA across subtrees");
        assertEquals(0, hld.lca(1, 2), "LCA of children of root");
    }

    @Test
    @DisplayName("Test star tree (all nodes connected to root)")
    void testStarTree() {
        //      0(10)
        //     /|\ \
        //    1 2 3 4
        //   (1)(2)(3)(4)
        
        int n = 5;
        int[][] edges = {
            {0, 1}, {0, 2}, {0, 3}, {0, 4}
        };
        int[] values = {10, 1, 2, 3, 4};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        // All paths go through root
        assertEquals(11, hld.pathQuery(0, 1), "Root to leaf: 10+1 = 11");
        // Path 1->2: node1(1) -> node0(10) -> node2(2) = 1+10+2 = 13
        assertEquals(13, hld.pathQuery(1, 2), "Leaf to leaf through root: 1+10+2 = 13");
        
        // LCA is always root for different leaves
        assertEquals(0, hld.lca(1, 2), "LCA in star tree");
        assertEquals(0, hld.lca(3, 4), "LCA in star tree");
        
        // Distance between leaves is always 2
        assertEquals(2, hld.distance(1, 2), "Distance in star tree");
        assertEquals(2, hld.distance(1, 4), "Distance in star tree");
        assertEquals(1, hld.distance(0, 1), "Distance from root");
    }

    @Test
    @DisplayName("Test multiple updates and queries")
    void testMultipleUpdates() {
        int n = 7;
        int[][] edges = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {4, 6}
        };
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        long initial = hld.pathQuery(3, 6);
        
        // Multiple point updates
        hld.update(3, 10);
        hld.update(1, 20);
        hld.update(6, 70);
        
        // Path 3 -> 6: now 10+20+5+70 = 105
        assertEquals(105, hld.pathQuery(3, 6), "Path after multiple point updates");
        
        // Multiple path updates
        hld.pathUpdate(3, 6, 5);
        // Each node on path gets +5, path has 4 nodes
        assertEquals(125, hld.pathQuery(3, 6), "Path after path update: 105 + 4*5 = 125");
        
        hld.pathUpdate(3, 6, -5);
        assertEquals(105, hld.pathQuery(3, 6), "Path after negative path update");
    }

    @Test
    @DisplayName("Test with all zeros")
    void testAllZeros() {
        int n = 5;
        int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 4}
        };
        int[] values = {0, 0, 0, 0, 0};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        assertEquals(0, hld.pathQuery(0, 4), "All zeros path query");
        
        hld.pathUpdate(0, 4, 1);
        assertEquals(5, hld.pathQuery(0, 4), "After adding 1 to all");
    }

    @Test
    @DisplayName("Test with negative values")
    void testNegativeValues() {
        int n = 5;
        int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 4}
        };
        int[] values = {-1, -2, -3, -4, -5};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        assertEquals(-15, hld.pathQuery(0, 4), "Negative values path query");
        
        hld.pathUpdate(0, 4, 10);
        assertEquals(35, hld.pathQuery(0, 4), "After adding to negative path");
    }

    @Test
    @DisplayName("Test larger tree")
    void testLargerTree() {
        // Build a larger tree: 15 nodes
        //                  0
        //                 / \
        //                1   2
        //               /|   |\
        //              3 4   5 6
        //             /| |   |
        //            7 8 9  10
        //           /|     /|\
        //          11 12  13 14
        
        int n = 15;
        int[][] edges = {
            {0, 1}, {0, 2},
            {1, 3}, {1, 4},
            {2, 5}, {2, 6},
            {3, 7}, {3, 8},
            {4, 9},
            {5, 10},
            {7, 11}, {7, 12},
            {10, 13}, {10, 14}
        };
        
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = i + 1; // 1, 2, 3, ..., 15
        }
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        // Test various paths
        long path11to14 = hld.pathQuery(11, 14);
        assertTrue(path11to14 > 0, "Path query on larger tree should be positive");
        
        // Test LCA
        int lcaResult = hld.lca(11, 14);
        assertEquals(0, lcaResult, "LCA of nodes in different subtrees should be root");
        
        // Test distance
        int dist = hld.distance(11, 12);
        assertEquals(2, dist, "Distance between siblings should be 2");
        
        // Update and verify
        hld.update(0, 1000);
        long newPath = hld.pathQuery(11, 14);
        assertEquals(path11to14 + 999, newPath, "Path should increase by delta of root update");
    }

    @Test
    @DisplayName("Test symmetric queries")
    void testSymmetricQueries() {
        int n = 7;
        int[][] edges = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {4, 6}
        };
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        // Path query should be symmetric
        assertEquals(hld.pathQuery(3, 6), hld.pathQuery(6, 3), "Path query symmetry");
        assertEquals(hld.pathQuery(1, 5), hld.pathQuery(5, 1), "Path query symmetry");
        
        // LCA should be symmetric
        assertEquals(hld.lca(3, 6), hld.lca(6, 3), "LCA symmetry");
        assertEquals(hld.lca(1, 5), hld.lca(5, 1), "LCA symmetry");
        
        // Distance should be symmetric
        assertEquals(hld.distance(3, 6), hld.distance(6, 3), "Distance symmetry");
        assertEquals(hld.distance(1, 5), hld.distance(5, 1), "Distance symmetry");
    }

    @Test
    @DisplayName("Test edge cases with two nodes")
    void testTwoNodes() {
        int n = 2;
        int[][] edges = {{0, 1}};
        int[] values = {5, 10};
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n, edges, 0, values);
        
        assertEquals(15, hld.pathQuery(0, 1), "Path between two nodes");
        assertEquals(0, hld.lca(0, 1), "LCA of two nodes");
        assertEquals(1, hld.distance(0, 1), "Distance between two nodes");
        
        hld.pathUpdate(0, 1, 5);
        assertEquals(25, hld.pathQuery(0, 1), "Path after update on two nodes");
    }
}
