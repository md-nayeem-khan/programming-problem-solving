package com.cp.algorithms;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Stream;

import com.cp.algorithms.BinaryLiftingTemplates.*;

/**
 * Comprehensive test suite for BinaryLiftingTemplates
 * Tests all three implementations: BinaryLiftingLCA, WeightedBinaryLifting, TreeAncestor
 * 
 * Coverage:
 * - Basic functionality
 * - Edge cases
 * - Performance validation
 * - Correctness verification
 * - LeetCode problem validation
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BinaryLiftingTemplatesTest {

    // ========== BASIC LCA TESTS ==========
    
    @Test
    @Order(1)
    @DisplayName("Basic LCA - Simple Tree")
    public void testBasicLCA() {
        // Tree structure:
        //       0
        //      / \
        //     1   2
        //    / \   \
        //   3   4   5
        //  /
        // 6
        
        int n = 7;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // Build undirected tree
        addEdge(graph, 0, 1);
        addEdge(graph, 0, 2);
        addEdge(graph, 1, 3);
        addEdge(graph, 1, 4);
        addEdge(graph, 2, 5);
        addEdge(graph, 3, 6);
        
        BinaryLiftingLCA lca = new BinaryLiftingLCA(n, graph, 0);
        
        // Test LCA queries
        assertEquals(1, lca.lca(3, 4), "LCA(3,4) should be 1");
        assertEquals(0, lca.lca(3, 5), "LCA(3,5) should be 0");
        assertEquals(1, lca.lca(6, 4), "LCA(6,4) should be 1");
        assertEquals(0, lca.lca(1, 2), "LCA(1,2) should be 0");
        assertEquals(3, lca.lca(3, 6), "LCA(3,6) should be 3");
        assertEquals(0, lca.lca(0, 5), "LCA(0,5) should be 0 (root)");
    }
    
    @Test
    @Order(2)
    @DisplayName("Distance Calculations")
    public void testDistanceCalculations() {
        int n = 7;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        addEdge(graph, 0, 1);
        addEdge(graph, 0, 2);
        addEdge(graph, 1, 3);
        addEdge(graph, 1, 4);
        addEdge(graph, 2, 5);
        addEdge(graph, 3, 6);
        
        BinaryLiftingLCA lca = new BinaryLiftingLCA(n, graph, 0);
        
        // Test distance calculations
        assertEquals(2, lca.distance(3, 4), "Distance(3,4) should be 2");
        assertEquals(4, lca.distance(3, 5), "Distance(3,5) should be 4");
        assertEquals(3, lca.distance(6, 4), "Distance(6,4) should be 3");
        assertEquals(2, lca.distance(1, 2), "Distance(1,2) should be 2");
        assertEquals(1, lca.distance(3, 6), "Distance(3,6) should be 1");
        assertEquals(0, lca.distance(5, 5), "Distance(5,5) should be 0");
    }
    
    @Test
    @Order(3)
    @DisplayName("Kth Ancestor Queries")
    public void testKthAncestor() {
        int n = 7;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        addEdge(graph, 0, 1);
        addEdge(graph, 0, 2);
        addEdge(graph, 1, 3);
        addEdge(graph, 1, 4);
        addEdge(graph, 2, 5);
        addEdge(graph, 3, 6);
        
        BinaryLiftingLCA lca = new BinaryLiftingLCA(n, graph, 0);
        
        // Test kth ancestor queries
        assertEquals(6, lca.kthAncestor(6, 0), "0th ancestor of 6 should be 6");
        assertEquals(3, lca.kthAncestor(6, 1), "1st ancestor of 6 should be 3");
        assertEquals(1, lca.kthAncestor(6, 2), "2nd ancestor of 6 should be 1");
        assertEquals(0, lca.kthAncestor(6, 3), "3rd ancestor of 6 should be 0");
        assertEquals(-1, lca.kthAncestor(6, 4), "4th ancestor of 6 should be -1 (doesn't exist)");
        
        assertEquals(0, lca.kthAncestor(0, 0), "0th ancestor of root should be root");
        assertEquals(-1, lca.kthAncestor(0, 1), "1st ancestor of root should be -1");
    }
    
    @Test
    @Order(4)
    @DisplayName("Kth Node on Path")
    public void testKthNodeOnPath() {
        int n = 7;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        addEdge(graph, 0, 1);
        addEdge(graph, 0, 2);
        addEdge(graph, 1, 3);
        addEdge(graph, 1, 4);
        addEdge(graph, 2, 5);
        addEdge(graph, 3, 6);
        
        BinaryLiftingLCA lca = new BinaryLiftingLCA(n, graph, 0);
        
        // Test kth node on path from 6 to 4: 6 -> 3 -> 1 -> 4
        assertEquals(6, lca.kthNodeOnPath(6, 4, 0), "0th node on path 6->4 should be 6");
        assertEquals(3, lca.kthNodeOnPath(6, 4, 1), "1st node on path 6->4 should be 3");
        assertEquals(1, lca.kthNodeOnPath(6, 4, 2), "2nd node on path 6->4 should be 1");
        assertEquals(4, lca.kthNodeOnPath(6, 4, 3), "3rd node on path 6->4 should be 4");
        assertEquals(-1, lca.kthNodeOnPath(6, 4, 4), "4th node on path 6->4 should be -1");
        assertEquals(-1, lca.kthNodeOnPath(6, 4, -1), "Invalid k should return -1");
    }

    // ========== EDGE CASES ==========
    
    @Test
    @Order(5)
    @DisplayName("Single Node Tree")
    public void testSingleNode() {
        int n = 1;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n];
        graph[0] = new ArrayList<>();
        
        BinaryLiftingLCA lca = new BinaryLiftingLCA(n, graph, 0);
        
        assertEquals(0, lca.lca(0, 0), "LCA of single node with itself should be itself");
        assertEquals(0, lca.distance(0, 0), "Distance of single node to itself should be 0");
        assertEquals(0, lca.kthAncestor(0, 0), "0th ancestor of single node should be itself");
        assertEquals(-1, lca.kthAncestor(0, 1), "1st ancestor of single node should be -1");
        assertEquals(0, lca.kthNodeOnPath(0, 0, 0), "Path from node to itself should return the node");
    }
    
    @Test
    @Order(6)
    @DisplayName("Linear Tree (Path)")
    public void testLinearTree() {
        // Linear tree: 0 - 1 - 2 - 3 - 4
        int n = 5;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int i = 0; i < n - 1; i++) {
            addEdge(graph, i, i + 1);
        }
        
        BinaryLiftingLCA lca = new BinaryLiftingLCA(n, graph, 0);
        
        assertEquals(1, lca.lca(1, 4), "LCA(1,4) in linear tree should be 1");
        assertEquals(2, lca.lca(2, 3), "LCA(2,3) in linear tree should be 2");  
        assertEquals(4, lca.distance(0, 4), "Distance(0,4) in linear tree should be 4");
        assertEquals(3, lca.distance(1, 4), "Distance(1,4) in linear tree should be 3");
        assertEquals(2, lca.kthAncestor(4, 2), "2nd ancestor of 4 should be 2");
        assertEquals(0, lca.kthAncestor(4, 4), "4th ancestor of 4 should be 0");
    }
    
    @Test
    @Order(7)
    @DisplayName("Complete Binary Tree")
    public void testCompleteBinaryTree() {
        // Complete binary tree with 7 nodes:
        //       0
        //      / \
        //     1   2
        //    / \ / \
        //   3  4 5  6
        
        int n = 7;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        addEdge(graph, 0, 1);
        addEdge(graph, 0, 2);
        addEdge(graph, 1, 3);
        addEdge(graph, 1, 4);
        addEdge(graph, 2, 5);
        addEdge(graph, 2, 6);
        
        BinaryLiftingLCA lca = new BinaryLiftingLCA(n, graph, 0);
        
        assertEquals(1, lca.lca(3, 4), "LCA(3,4) should be 1");
        assertEquals(2, lca.lca(5, 6), "LCA(5,6) should be 2");
        assertEquals(0, lca.lca(3, 6), "LCA(3,6) should be 0");
        assertEquals(4, lca.distance(3, 6), "Distance(3,6) should be 4");
        assertEquals(0, lca.kthAncestor(3, 2), "2nd ancestor of 3 should be 0");
    }

    // ========== WEIGHTED BINARY LIFTING TESTS ==========
    
    @Test
    @Order(8)
    @DisplayName("Weighted Path Queries - Max Edge")
    public void testWeightedMaxEdge() {
        int n = 5;
        @SuppressWarnings("unchecked")
        List<WeightedBinaryLifting.Edge>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // Tree: 0-(5)-1-(2)-3-(7)-4
        //         \-(3)-2
        addWeightedEdge(graph, 0, 1, 5);
        addWeightedEdge(graph, 0, 2, 3);
        addWeightedEdge(graph, 1, 3, 2);
        addWeightedEdge(graph, 3, 4, 7);
        
        WeightedBinaryLifting wbl = new WeightedBinaryLifting(n, graph, 0);
        
        assertEquals(7, wbl.maxEdgeOnPath(1, 4), "Max edge on path 1->4 should be 7");
        assertEquals(7, wbl.maxEdgeOnPath(0, 4), "Max edge on path 0->4 should be 7");
        assertEquals(5, wbl.maxEdgeOnPath(2, 1), "Max edge on path 2->1 should be 5");
        assertEquals(7, wbl.maxEdgeOnPath(2, 4), "Max edge on path 2->4 should be 7");
    }
    
    @Test
    @Order(9)
    @DisplayName("Weighted Path Queries - Min Edge")
    public void testWeightedMinEdge() {
        int n = 5;
        @SuppressWarnings("unchecked")
        List<WeightedBinaryLifting.Edge>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        addWeightedEdge(graph, 0, 1, 5);
        addWeightedEdge(graph, 0, 2, 3);
        addWeightedEdge(graph, 1, 3, 2);
        addWeightedEdge(graph, 3, 4, 7);
        
        WeightedBinaryLifting wbl = new WeightedBinaryLifting(n, graph, 0);
        
        assertEquals(2, wbl.minEdgeOnPath(1, 4), "Min edge on path 1->4 should be 2");
        assertEquals(2, wbl.minEdgeOnPath(0, 4), "Min edge on path 0->4 should be 2");
        assertEquals(3, wbl.minEdgeOnPath(2, 1), "Min edge on path 2->1 should be 3");
        assertEquals(2, wbl.minEdgeOnPath(2, 4), "Min edge on path 2->4 should be 2");
    }
    
    @Test
    @Order(10)
    @DisplayName("Weighted Path Queries - Sum of Edges")
    public void testWeightedSumEdge() {
        int n = 5;
        @SuppressWarnings("unchecked")
        List<WeightedBinaryLifting.Edge>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        addWeightedEdge(graph, 0, 1, 5);
        addWeightedEdge(graph, 0, 2, 3);
        addWeightedEdge(graph, 1, 3, 2);
        addWeightedEdge(graph, 3, 4, 7);
        
        WeightedBinaryLifting wbl = new WeightedBinaryLifting(n, graph, 0);
        
        assertEquals(9, wbl.sumEdgeOnPath(1, 4), "Sum of edges on path 1->4 should be 9");
        assertEquals(14, wbl.sumEdgeOnPath(0, 4), "Sum of edges on path 0->4 should be 14");
        assertEquals(8, wbl.sumEdgeOnPath(2, 1), "Sum of edges on path 2->1 should be 8");
        assertEquals(17, wbl.sumEdgeOnPath(2, 4), "Sum of edges on path 2->4 should be 17");
    }

    // ========== LEETCODE 1483 TESTS ==========
    
    @Test
    @Order(11)
    @DisplayName("LeetCode 1483 - Tree Ancestor")
    public void testLeetCode1483() {
        // Test case from LeetCode 1483
        int[] parent = {-1, 0, 0, 1, 1, 2, 2};
        TreeAncestor treeAncestor = new TreeAncestor(7, parent);
        
        assertEquals(1, treeAncestor.getKthAncestor(3, 1), "1st ancestor of 3 should be 1");
        assertEquals(0, treeAncestor.getKthAncestor(5, 2), "2nd ancestor of 5 should be 0");
        assertEquals(-1, treeAncestor.getKthAncestor(6, 3), "3rd ancestor of 6 should be -1");
        assertEquals(0, treeAncestor.getKthAncestor(3, 2), "2nd ancestor of 3 should be 0");
        assertEquals(-1, treeAncestor.getKthAncestor(0, 1), "1st ancestor of root should be -1");
    }
    
    @Test
    @Order(12)
    @DisplayName("LeetCode 1483 - Large Tree")
    public void testLeetCode1483LargeTree() {
        // Build a larger tree for stress testing
        int n = 100;
        int[] parent = new int[n];
        parent[0] = -1;
        
        // Create a balanced-ish tree
        for (int i = 1; i < n; i++) {
            parent[i] = (i - 1) / 2;
        }
        
        TreeAncestor treeAncestor = new TreeAncestor(n, parent);
        
        // Test various queries
        assertEquals(0, treeAncestor.getKthAncestor(99, 6), "Deep ancestor query");
        assertEquals(49, treeAncestor.getKthAncestor(99, 1), "1st ancestor of 99");
        assertEquals(-1, treeAncestor.getKthAncestor(99, 10), "Non-existent ancestor");
        assertEquals(0, treeAncestor.getKthAncestor(1, 1), "Direct child of root");
    }

    // ========== PERFORMANCE TESTS ==========
    
    @Test
    @Order(13)
    @DisplayName("Performance Test - Large Tree")
    public void testPerformanceLargeTree() {
        int n = 10000;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // Build a balanced binary tree
        for (int i = 1; i < n; i++) {
            int parent = (i - 1) / 2;
            addEdge(graph, parent, i);
        }
        
        long startTime = System.currentTimeMillis();
        BinaryLiftingLCA lca = new BinaryLiftingLCA(n, graph, 0);
        long constructionTime = System.currentTimeMillis() - startTime;
        
        // Construction should be reasonably fast (under 1 second for 10k nodes)
        assertTrue(constructionTime < 1000, 
            "Construction time should be under 1 second, was: " + constructionTime + "ms");
        
        // Test query performance
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            int u = i * 7 % n;
            int v = i * 11 % n;
            lca.lca(u, v);
        }
        long queryTime = System.currentTimeMillis() - startTime;
        
        // 1000 queries should be very fast (under 100ms)
        assertTrue(queryTime < 100, 
            "1000 LCA queries should be under 100ms, was: " + queryTime + "ms");
    }

    // ========== STRESS TESTS ==========
    
    @ParameterizedTest
    @MethodSource("provideStressTestCases")
    @Order(14)
    @DisplayName("Stress Test - Random Trees")
    public void testRandomTreesStress(int treeSize, int numQueries) {
        // Generate random tree
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[treeSize];
        for (int i = 0; i < treeSize; i++) {
            graph[i] = new ArrayList<>();
        }
        
        Random rand = new Random(42); // Fixed seed for reproducibility
        for (int i = 1; i < treeSize; i++) {
            int parent = rand.nextInt(i);
            addEdge(graph, parent, i);
        }
        
        BinaryLiftingLCA lca = new BinaryLiftingLCA(treeSize, graph, 0);
        
        // Test random queries and verify they complete without errors
        for (int i = 0; i < numQueries; i++) {
            int u = rand.nextInt(treeSize);
            int v = rand.nextInt(treeSize);
            
            int lcaResult = lca.lca(u, v);
            assertNotEquals(-1, lcaResult, "LCA should always exist in connected tree");
            assertTrue(lcaResult >= 0 && lcaResult < treeSize, "LCA should be valid node");
            
            int dist = lca.distance(u, v);
            assertTrue(dist >= 0, "Distance should be non-negative");
        }
    }
    
    static Stream<Arguments> provideStressTestCases() {
        return Stream.of(
            Arguments.of(100, 50),
            Arguments.of(500, 100),
            Arguments.of(1000, 200)
        );
    }

    // ========== HELPER METHODS ==========
    
    private void addEdge(List<Integer>[] graph, int u, int v) {
        graph[u].add(v);
        graph[v].add(u);
    }
    
    private void addWeightedEdge(List<WeightedBinaryLifting.Edge>[] graph, int u, int v, long weight) {
        graph[u].add(new WeightedBinaryLifting.Edge(v, weight));
        graph[v].add(new WeightedBinaryLifting.Edge(u, weight));
    }
    
    // ========== INTEGRATION TESTS WITH EXAMPLE EXECUTION ==========
    
    @Test
    @Order(15)
    @DisplayName("Integration Test - Run Main Method")
    public void testMainMethodExecution() {
        // This test ensures the main method runs without exceptions
        assertDoesNotThrow(() -> BinaryLiftingTemplates.main(new String[]{}),
            "Main method should execute without throwing exceptions");
    }
}