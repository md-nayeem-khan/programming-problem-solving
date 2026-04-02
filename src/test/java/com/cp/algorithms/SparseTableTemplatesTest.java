package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Comprehensive Test Suite for SparseTableTemplates
 * Tests all implementations: RMQ, RMaxQ, GCD, Generic, LCA, 2D
 */
public class SparseTableTemplatesTest {

    // ==================== RANGE MINIMUM QUERY TESTS ====================
    
    @Test
    @DisplayName("RMQ: Basic functionality with small array")
    public void testRMQBasic() {
        int[] arr = {1, 3, 2, 7, 9, 11, 3, 5};
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        
        assertEquals(2, rmq.query(1, 4), "Min of [3,2,7,9] should be 2");
        assertEquals(1, rmq.query(0, 7), "Min of entire array should be 1");
        assertEquals(3, rmq.query(3, 6), "Min of [7,9,11,3] should be 3");
        assertEquals(3, rmq.query(1, 1), "Single element query should return itself");
    }
    
    @Test
    @DisplayName("RMQ: Edge cases - single element array")
    public void testRMQSingleElement() {
        int[] arr = {42};
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        assertEquals(42, rmq.query(0, 0), "Single element query should return the element");
    }
    
    @Test
    @DisplayName("RMQ: All elements are same")
    public void testRMQAllSame() {
        int[] arr = {5, 5, 5, 5, 5};
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        assertEquals(5, rmq.query(0, 4), "All same elements should return 5");
        assertEquals(5, rmq.query(1, 3), "Subrange of same elements should return 5");
    }
    
    @Test
    @DisplayName("RMQ: Sorted ascending array")
    public void testRMQSortedAscending() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        assertEquals(1, rmq.query(0, 7), "Min should be first element");
        assertEquals(3, rmq.query(2, 5), "Min should be leftmost in range");
    }
    
    @Test
    @DisplayName("RMQ: Sorted descending array")
    public void testRMQSortedDescending() {
        int[] arr = {8, 7, 6, 5, 4, 3, 2, 1};
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        assertEquals(1, rmq.query(0, 7), "Min should be last element");
        assertEquals(3, rmq.query(2, 5), "Min should be rightmost in range");
    }
    
    @Test
    @DisplayName("RMQ: Negative numbers")
    public void testRMQNegativeNumbers() {
        int[] arr = {5, -3, 8, -10, 15, -2};
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        assertEquals(-10, rmq.query(0, 5), "Min should handle negative numbers");
        assertEquals(-3, rmq.query(0, 2), "Min should be -3");
        assertEquals(-10, rmq.query(2, 4), "Min should be -10");
    }
    
    @Test
    @DisplayName("RMQ: Large array stress test")
    public void testRMQLargeArray() {
        int n = 100000;
        int[] arr = new int[n];
        Random rand = new Random(42);
        
        for (int i = 0; i < n; i++) {
            arr[i] = rand.nextInt(1000000);
        }
        
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        
        // Verify random queries
        for (int i = 0; i < 1000; i++) {
            int l = rand.nextInt(n);
            int r = l + rand.nextInt(n - l);
            
            int expected = Integer.MAX_VALUE;
            for (int j = l; j <= r; j++) {
                expected = Math.min(expected, arr[j]);
            }
            
            assertEquals(expected, rmq.query(l, r), 
                "RMQ failed for range [" + l + ", " + r + "]");
        }
    }
    
    @Test
    @DisplayName("RMQ: Overlapping ranges (idempotent property)")
    public void testRMQOverlappingRanges() {
        int[] arr = {4, 2, 8, 1, 9, 6};
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        
        // The key property: min is idempotent, so overlapping ranges work
        assertEquals(1, rmq.query(0, 5), "Full range min");
        assertEquals(2, rmq.query(0, 2), "Left part min");
        assertEquals(1, rmq.query(2, 5), "Right part min");
    }

    // ==================== RANGE MAXIMUM QUERY TESTS ====================
    
    @Test
    @DisplayName("RMaxQ: Basic functionality")
    public void testRMaxQBasic() {
        int[] arr = {1, 3, 2, 7, 9, 11, 3, 5};
        SparseTableTemplates.RangeMaximumQuery rmaxq = new SparseTableTemplates.RangeMaximumQuery(arr);
        
        assertEquals(9, rmaxq.query(1, 4), "Max of [3,2,7,9] should be 9");
        assertEquals(11, rmaxq.query(0, 7), "Max of entire array should be 11");
        assertEquals(11, rmaxq.query(3, 6), "Max of [7,9,11,3] should be 11");
    }
    
    @Test
    @DisplayName("RMaxQ: Negative numbers")
    public void testRMaxQNegativeNumbers() {
        int[] arr = {-5, -3, -8, -10, -15, -2};
        SparseTableTemplates.RangeMaximumQuery rmaxq = new SparseTableTemplates.RangeMaximumQuery(arr);
        assertEquals(-2, rmaxq.query(0, 5), "Max should be -2");
        assertEquals(-3, rmaxq.query(0, 2), "Max should be -3");
    }
    
    @Test
    @DisplayName("RMaxQ: Mixed positive and negative")
    public void testRMaxQMixedNumbers() {
        int[] arr = {-10, 5, -3, 20, -8, 15};
        SparseTableTemplates.RangeMaximumQuery rmaxq = new SparseTableTemplates.RangeMaximumQuery(arr);
        assertEquals(20, rmaxq.query(0, 5), "Max should be 20");
        assertEquals(5, rmaxq.query(0, 1), "Max should be 5");
        assertEquals(20, rmaxq.query(2, 4), "Max should be 20");
    }

    // ==================== RANGE GCD QUERY TESTS ====================
    
    @Test
    @DisplayName("GCD: Basic functionality")
    public void testGCDBasic() {
        int[] arr = {6, 12, 18, 24, 30};
        SparseTableTemplates.RangeGCDQuery gcdq = new SparseTableTemplates.RangeGCDQuery(arr);
        
        assertEquals(6, gcdq.query(0, 2), "GCD of [6,12,18] should be 6");
        assertEquals(6, gcdq.query(1, 4), "GCD of [12,18,24,30] should be 6");
        assertEquals(6, gcdq.query(0, 4), "GCD of entire array should be 6");
    }
    
    @Test
    @DisplayName("GCD: Prime numbers (GCD should be 1)")
    public void testGCDPrimes() {
        int[] arr = {7, 11, 13, 17, 19};
        SparseTableTemplates.RangeGCDQuery gcdq = new SparseTableTemplates.RangeGCDQuery(arr);
        
        assertEquals(1, gcdq.query(0, 4), "GCD of primes should be 1");
        assertEquals(1, gcdq.query(1, 3), "GCD of subset of primes should be 1");
    }
    
    @Test
    @DisplayName("GCD: Powers of 2")
    public void testGCDPowersOf2() {
        int[] arr = {4, 8, 16, 32, 64};
        SparseTableTemplates.RangeGCDQuery gcdq = new SparseTableTemplates.RangeGCDQuery(arr);
        
        assertEquals(4, gcdq.query(0, 4), "GCD of powers of 2 should be 4");
        assertEquals(8, gcdq.query(1, 4), "GCD should be 8");
    }
    
    @Test
    @DisplayName("GCD: Single element")
    public void testGCDSingleElement() {
        int[] arr = {42};
        SparseTableTemplates.RangeGCDQuery gcdq = new SparseTableTemplates.RangeGCDQuery(arr);
        assertEquals(42, gcdq.query(0, 0), "GCD of single element is itself");
    }
    
    @Test
    @DisplayName("GCD: Co-prime numbers")
    public void testGCDCoPrime() {
        int[] arr = {15, 28, 33, 44};
        SparseTableTemplates.RangeGCDQuery gcdq = new SparseTableTemplates.RangeGCDQuery(arr);
        
        assertEquals(1, gcdq.query(0, 1), "GCD(15, 28) should be 1");
        assertEquals(11, gcdq.query(2, 3), "GCD(33, 44) should be 11");
    }
    
    @Test
    @DisplayName("GCD: Large numbers")
    public void testGCDLargeNumbers() {
        int[] arr = {1000000, 2000000, 3000000, 4000000};
        SparseTableTemplates.RangeGCDQuery gcdq = new SparseTableTemplates.RangeGCDQuery(arr);
        
        assertEquals(1000000, gcdq.query(0, 3), "GCD should be 1000000");
        assertEquals(1000000, gcdq.query(1, 2), "GCD should be 1000000");
    }

    // ==================== GENERIC SPARSE TABLE TESTS ====================
    
    @Test
    @DisplayName("Generic: Min operation")
    public void testGenericMin() {
        Integer[] arr = {5, 2, 8, 1, 9};
        SparseTableTemplates.GenericSparseTable<Integer> gst = 
            new SparseTableTemplates.GenericSparseTable<>(arr, Math::min);
        
        assertEquals(1, gst.query(1, 3), "Min of [2,8,1] should be 1");
        assertEquals(1, gst.query(0, 4), "Min of entire array should be 1");
    }
    
    @Test
    @DisplayName("Generic: Max operation")
    public void testGenericMax() {
        Integer[] arr = {5, 2, 8, 1, 9};
        SparseTableTemplates.GenericSparseTable<Integer> gst = 
            new SparseTableTemplates.GenericSparseTable<>(arr, Math::max);
        
        assertEquals(8, gst.query(1, 3), "Max of [2,8,1] should be 8");
        assertEquals(9, gst.query(0, 4), "Max of entire array should be 9");
    }
    
    @Test
    @DisplayName("Generic: GCD operation")
    public void testGenericGCD() {
        Integer[] arr = {12, 18, 24, 36};
        SparseTableTemplates.GenericSparseTable<Integer> gst = 
            new SparseTableTemplates.GenericSparseTable<>(arr, this::gcd);
        
        assertEquals(6, gst.query(0, 2), "GCD of [12,18,24] should be 6");
        assertEquals(6, gst.query(0, 3), "GCD of entire array should be 6");
    }
    
    @Test
    @DisplayName("Generic: Bitwise AND operation")
    public void testGenericBitwiseAND() {
        Integer[] arr = {7, 5, 3, 1}; // Binary: 111, 101, 011, 001
        SparseTableTemplates.GenericSparseTable<Integer> gst = 
            new SparseTableTemplates.GenericSparseTable<>(arr, (a, b) -> a & b);
        
        assertEquals(1, gst.query(0, 3), "AND of all should be 1");
        assertEquals(5, gst.query(0, 1), "AND of [7,5] should be 5");
    }
    
    @Test
    @DisplayName("Generic: Bitwise OR operation")
    public void testGenericBitwiseOR() {
        Integer[] arr = {1, 2, 4, 8}; // Powers of 2
        SparseTableTemplates.GenericSparseTable<Integer> gst = 
            new SparseTableTemplates.GenericSparseTable<>(arr, (a, b) -> a | b);
        
        assertEquals(15, gst.query(0, 3), "OR of all should be 15");
        assertEquals(3, gst.query(0, 1), "OR of [1,2] should be 3");
    }
    
    @Test
    @DisplayName("Generic: String min (lexicographic)")
    public void testGenericString() {
        String[] arr = {"dog", "cat", "elephant", "ant", "bear"};
        SparseTableTemplates.GenericSparseTable<String> gst = 
            new SparseTableTemplates.GenericSparseTable<>(arr, (a, b) -> a.compareTo(b) < 0 ? a : b);
        
        assertEquals("ant", gst.query(0, 4), "Min string should be 'ant'");
        assertEquals("cat", gst.query(0, 2), "Min of [dog,cat,elephant] should be 'cat'");
    }

    // ==================== LCA BINARY LIFTING TESTS ====================
    
    @Test
    @DisplayName("LCA Binary Lifting: Basic tree")
    public void testLCABinaryLiftingBasic() {
        int n = 7;
        @SuppressWarnings("unchecked")
        List<Integer>[] tree = new ArrayList[n];
        for (int i = 0; i < n; i++) tree[i] = new ArrayList<>();
        
        // Build tree:      0
        //                 / \
        //                1   2
        //               / \   \
        //              3   4   5
        //                     /
        //                    6
        tree[0].add(1); tree[1].add(0);
        tree[0].add(2); tree[2].add(0);
        tree[1].add(3); tree[3].add(1);
        tree[1].add(4); tree[4].add(1);
        tree[2].add(5); tree[5].add(2);
        tree[5].add(6); tree[6].add(5);
        
        SparseTableTemplates.LCABinaryLifting lca = new SparseTableTemplates.LCABinaryLifting(n, 0, tree);
        
        assertEquals(1, lca.lca(3, 4), "LCA(3, 4) should be 1");
        assertEquals(0, lca.lca(3, 6), "LCA(3, 6) should be 0");
        assertEquals(0, lca.lca(4, 5), "LCA(4, 5) should be 0");
        assertEquals(5, lca.lca(5, 6), "LCA(5, 6) should be 5 (parent of 6)");
        assertEquals(0, lca.lca(1, 2), "LCA(1, 2) should be 0");
    }
    
    @Test
    @DisplayName("LCA Binary Lifting: Node with itself")
    public void testLCABinaryLiftingSameNode() {
        int n = 5;
        @SuppressWarnings("unchecked")
        List<Integer>[] tree = new ArrayList[n];
        for (int i = 0; i < n; i++) tree[i] = new ArrayList<>();
        
        tree[0].add(1); tree[1].add(0);
        tree[1].add(2); tree[2].add(1);
        tree[2].add(3); tree[3].add(2);
        tree[3].add(4); tree[4].add(3);
        
        SparseTableTemplates.LCABinaryLifting lca = new SparseTableTemplates.LCABinaryLifting(n, 0, tree);
        
        assertEquals(0, lca.lca(0, 0), "LCA(0, 0) should be 0");
        assertEquals(2, lca.lca(2, 2), "LCA(2, 2) should be 2");
        assertEquals(4, lca.lca(4, 4), "LCA(4, 4) should be 4");
    }
    
    @Test
    @DisplayName("LCA Binary Lifting: Kth ancestor")
    public void testKthAncestor() {
        int n = 7;
        @SuppressWarnings("unchecked")
        List<Integer>[] tree = new ArrayList[n];
        for (int i = 0; i < n; i++) tree[i] = new ArrayList<>();
        
        tree[0].add(1); tree[1].add(0);
        tree[0].add(2); tree[2].add(0);
        tree[1].add(3); tree[3].add(1);
        tree[1].add(4); tree[4].add(1);
        tree[2].add(5); tree[5].add(2);
        tree[5].add(6); tree[6].add(5);
        
        SparseTableTemplates.LCABinaryLifting lca = new SparseTableTemplates.LCABinaryLifting(n, 0, tree);
        
        assertEquals(5, lca.kthAncestor(6, 1), "1st ancestor of 6 should be 5");
        assertEquals(2, lca.kthAncestor(6, 2), "2nd ancestor of 6 should be 2");
        assertEquals(0, lca.kthAncestor(6, 3), "3rd ancestor of 6 should be 0");
        assertEquals(-1, lca.kthAncestor(6, 4), "4th ancestor of 6 should be -1");
        assertEquals(0, lca.kthAncestor(4, 2), "2nd ancestor of 4 should be 0");
    }
    
    @Test
    @DisplayName("LCA Binary Lifting: Distance between nodes")
    public void testDistanceBetweenNodes() {
        int n = 7;
        @SuppressWarnings("unchecked")
        List<Integer>[] tree = new ArrayList[n];
        for (int i = 0; i < n; i++) tree[i] = new ArrayList<>();
        
        tree[0].add(1); tree[1].add(0);
        tree[0].add(2); tree[2].add(0);
        tree[1].add(3); tree[3].add(1);
        tree[1].add(4); tree[4].add(1);
        tree[2].add(5); tree[5].add(2);
        tree[5].add(6); tree[6].add(5);
        
        SparseTableTemplates.LCABinaryLifting lca = new SparseTableTemplates.LCABinaryLifting(n, 0, tree);
        
        assertEquals(2, lca.distance(3, 4), "Distance(3, 4) should be 2");
        assertEquals(5, lca.distance(3, 6), "Distance(3, 6) should be 5");
        assertEquals(4, lca.distance(4, 5), "Distance(4, 5) should be 4");
        assertEquals(0, lca.distance(2, 2), "Distance(2, 2) should be 0");
    }
    
    @Test
    @DisplayName("LCA Binary Lifting: Linear tree (chain)")
    public void testLCALinearTree() {
        int n = 10;
        @SuppressWarnings("unchecked")
        List<Integer>[] tree = new ArrayList[n];
        for (int i = 0; i < n; i++) tree[i] = new ArrayList<>();
        
        // Linear chain: 0 - 1 - 2 - 3 - ... - 9
        for (int i = 0; i < n - 1; i++) {
            tree[i].add(i + 1);
            tree[i + 1].add(i);
        }
        
        SparseTableTemplates.LCABinaryLifting lca = new SparseTableTemplates.LCABinaryLifting(n, 0, tree);
        
        assertEquals(2, lca.lca(2, 5), "LCA in chain should be lower node");
        assertEquals(0, lca.lca(0, 9), "LCA should be root");
        assertEquals(5, lca.lca(5, 9), "LCA(5, 9) should be 5");
    }

    // ==================== LCA EULER TOUR TESTS ====================
    
    @Test
    @DisplayName("LCA Euler Tour: Basic tree O(1) query")
    public void testLCAEulerTourBasic() {
        int n = 7;
        @SuppressWarnings("unchecked")
        List<Integer>[] tree = new ArrayList[n];
        for (int i = 0; i < n; i++) tree[i] = new ArrayList<>();
        
        tree[0].add(1); tree[1].add(0);
        tree[0].add(2); tree[2].add(0);
        tree[1].add(3); tree[3].add(1);
        tree[1].add(4); tree[4].add(1);
        tree[2].add(5); tree[5].add(2);
        tree[5].add(6); tree[6].add(5);
        
        SparseTableTemplates.LCAEulerTour lca = new SparseTableTemplates.LCAEulerTour(n, 0, tree);
        
        assertEquals(1, lca.lca(3, 4), "LCA(3, 4) should be 1");
        assertEquals(0, lca.lca(3, 6), "LCA(3, 6) should be 0");
        assertEquals(0, lca.lca(4, 5), "LCA(4, 5) should be 0");
        assertEquals(5, lca.lca(5, 6), "LCA(5, 6) should be 5 (parent of 6)");
    }
    
    @Test
    @DisplayName("LCA Euler Tour: Compare with Binary Lifting")
    public void testLCAEulerTourVsBinaryLifting() {
        int n = 15;
        @SuppressWarnings("unchecked")
        List<Integer>[] tree = new ArrayList[n];
        for (int i = 0; i < n; i++) tree[i] = new ArrayList<>();
        
        // Build a more complex tree
        tree[0].add(1); tree[1].add(0);
        tree[0].add(2); tree[2].add(0);
        tree[1].add(3); tree[3].add(1);
        tree[1].add(4); tree[4].add(1);
        tree[2].add(5); tree[5].add(2);
        tree[2].add(6); tree[6].add(2);
        tree[3].add(7); tree[7].add(3);
        tree[4].add(8); tree[8].add(4);
        tree[4].add(9); tree[9].add(4);
        tree[5].add(10); tree[10].add(5);
        tree[6].add(11); tree[11].add(6);
        tree[6].add(12); tree[12].add(6);
        tree[9].add(13); tree[13].add(9);
        tree[10].add(14); tree[14].add(10);
        
        SparseTableTemplates.LCABinaryLifting lcaBL = new SparseTableTemplates.LCABinaryLifting(n, 0, tree);
        SparseTableTemplates.LCAEulerTour lcaET = new SparseTableTemplates.LCAEulerTour(n, 0, tree);
        
        // Test multiple queries
        int[][] queries = {{7, 14}, {8, 11}, {13, 12}, {3, 9}, {10, 13}};
        
        for (int[] query : queries) {
            int u = query[0], v = query[1];
            assertEquals(lcaBL.lca(u, v), lcaET.lca(u, v), 
                "LCA(" + u + ", " + v + ") should match between methods");
        }
    }
    
    @Test
    @DisplayName("LCA Euler Tour: Node with itself")
    public void testLCAEulerTourSameNode() {
        int n = 5;
        @SuppressWarnings("unchecked")
        List<Integer>[] tree = new ArrayList[n];
        for (int i = 0; i < n; i++) tree[i] = new ArrayList<>();
        
        tree[0].add(1); tree[1].add(0);
        tree[1].add(2); tree[2].add(1);
        tree[2].add(3); tree[3].add(2);
        tree[3].add(4); tree[4].add(3);
        
        SparseTableTemplates.LCAEulerTour lca = new SparseTableTemplates.LCAEulerTour(n, 0, tree);
        
        assertEquals(0, lca.lca(0, 0), "LCA(0, 0) should be 0");
        assertEquals(2, lca.lca(2, 2), "LCA(2, 2) should be 2");
        assertEquals(4, lca.lca(4, 4), "LCA(4, 4) should be 4");
    }

    // ==================== 2D SPARSE TABLE TESTS ====================
    
    @Test
    @DisplayName("2D Sparse Table: Basic 4x4 matrix")
    public void test2DSparseTableBasic() {
        int[][] matrix = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        SparseTableTemplates.SparseTable2D st2d = new SparseTableTemplates.SparseTable2D(matrix);
        
        assertEquals(1, st2d.query(0, 0, 1, 1), "Top-left 2x2 min should be 1");
        assertEquals(6, st2d.query(1, 1, 2, 2), "Middle 2x2 min should be 6");
        assertEquals(1, st2d.query(0, 0, 3, 3), "Entire matrix min should be 1");
        assertEquals(11, st2d.query(2, 2, 3, 3), "Bottom-right 2x2 min should be 11");
    }
    
    @Test
    @DisplayName("2D Sparse Table: Single cell query")
    public void test2DSparseTableSingleCell() {
        int[][] matrix = {
            {10, 20, 30},
            {40, 50, 60},
            {70, 80, 90}
        };
        SparseTableTemplates.SparseTable2D st2d = new SparseTableTemplates.SparseTable2D(matrix);
        
        assertEquals(50, st2d.query(1, 1, 1, 1), "Single cell query should return cell value");
        assertEquals(10, st2d.query(0, 0, 0, 0), "Single cell query should return cell value");
    }
    
    @Test
    @DisplayName("2D Sparse Table: Entire row")
    public void test2DSparseTableRow() {
        int[][] matrix = {
            {5, 3, 8, 1},
            {2, 9, 4, 7},
            {6, 1, 5, 3}
        };
        SparseTableTemplates.SparseTable2D st2d = new SparseTableTemplates.SparseTable2D(matrix);
        
        assertEquals(1, st2d.query(0, 0, 0, 3), "First row min should be 1");
        assertEquals(2, st2d.query(1, 0, 1, 3), "Second row min should be 2");
    }
    
    @Test
    @DisplayName("2D Sparse Table: Entire column")
    public void test2DSparseTableColumn() {
        int[][] matrix = {
            {5, 3, 8, 1},
            {2, 9, 4, 7},
            {6, 1, 5, 3}
        };
        SparseTableTemplates.SparseTable2D st2d = new SparseTableTemplates.SparseTable2D(matrix);
        
        assertEquals(2, st2d.query(0, 0, 2, 0), "First column min should be 2");
        assertEquals(1, st2d.query(0, 1, 2, 1), "Second column min should be 1");
    }
    
    @Test
    @DisplayName("2D Sparse Table: Large matrix")
    public void test2DSparseTableLarge() {
        int n = 100, m = 100;
        int[][] matrix = new int[n][m];
        Random rand = new Random(42);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = rand.nextInt(10000);
            }
        }
        
        SparseTableTemplates.SparseTable2D st2d = new SparseTableTemplates.SparseTable2D(matrix);
        
        // Test random queries
        for (int q = 0; q < 100; q++) {
            int x1 = rand.nextInt(n);
            int y1 = rand.nextInt(m);
            int x2 = x1 + rand.nextInt(n - x1);
            int y2 = y1 + rand.nextInt(m - y1);
            
            int expected = Integer.MAX_VALUE;
            for (int i = x1; i <= x2; i++) {
                for (int j = y1; j <= y2; j++) {
                    expected = Math.min(expected, matrix[i][j]);
                }
            }
            
            assertEquals(expected, st2d.query(x1, y1, x2, y2),
                "2D RMQ failed for rectangle [" + x1 + "," + y1 + "] to [" + x2 + "," + y2 + "]");
        }
    }
    
    @Test
    @DisplayName("2D Sparse Table: All same values")
    public void test2DSparseTableAllSame() {
        int[][] matrix = {
            {7, 7, 7},
            {7, 7, 7},
            {7, 7, 7}
        };
        SparseTableTemplates.SparseTable2D st2d = new SparseTableTemplates.SparseTable2D(matrix);
        
        assertEquals(7, st2d.query(0, 0, 2, 2), "All same values should return 7");
        assertEquals(7, st2d.query(1, 1, 2, 2), "Submatrix should also return 7");
    }

    // ==================== EDGE CASES AND BOUNDARY TESTS ====================
    
    @Test
    @DisplayName("Edge Case: Power of 2 array size")
    public void testPowerOf2ArraySize() {
        int[] arr = new int[16]; // 2^4
        for (int i = 0; i < 16; i++) arr[i] = i + 1;
        
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        assertEquals(1, rmq.query(0, 15), "Min should be 1");
        assertEquals(5, rmq.query(4, 10), "Min should be 5");
    }
    
    @Test
    @DisplayName("Edge Case: Non-power of 2 array size")
    public void testNonPowerOf2ArraySize() {
        int[] arr = new int[13]; // Not a power of 2
        for (int i = 0; i < 13; i++) arr[i] = 13 - i; // arr = [13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
        
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        assertEquals(1, rmq.query(0, 12), "Min should be 1");
        assertEquals(3, rmq.query(5, 10), "Min should be 3"); // arr[5..10] = [8,7,6,5,4,3], min = 3
    }
    
    @Test
    @DisplayName("Edge Case: Two element array")
    public void testTwoElementArray() {
        int[] arr = {10, 5};
        SparseTableTemplates.RangeMinimumQuery rmq = new SparseTableTemplates.RangeMinimumQuery(arr);
        assertEquals(5, rmq.query(0, 1), "Min should be 5");
        assertEquals(10, rmq.query(0, 0), "Single element should be 10");
    }

    // ==================== HELPER METHODS ====================
    
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
