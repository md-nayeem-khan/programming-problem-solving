package com.cp.algorithms;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Stream;

import com.cp.algorithms.LazySegmentTreeTemplates.*;

/**
 * Comprehensive test suite for LazySegmentTreeTemplates
 * Tests all implementations: LazySegmentTreeSum, LazySegmentTreeMin, AdvancedLazySegmentTree
 * 
 * Coverage:
 * - Basic functionality (build, query, update)
 * - Edge cases (single element, large arrays, boundary conditions)
 * - Range operations (various ranges)
 * - Lazy propagation correctness
 * - Performance validation
 * - LeetCode problem validation
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LazySegmentTreeTemplatesTest {

    // ========== LAZY SEGMENT TREE SUM TESTS ==========
    
    @Test
    @Order(1)
    @DisplayName("LazySegmentTreeSum - Basic Build and Query")
    public void testBasicBuildAndQuery() {
        int[] arr = {1, 2, 3, 4, 5};
        LazySegmentTreeSum tree = new LazySegmentTreeSum(arr);
        
        assertEquals(15, tree.rangeQuery(0, 4), "Sum of entire array should be 15");
        assertEquals(9, tree.rangeQuery(1, 3), "Sum [1,3] should be 9");
        assertEquals(1, tree.rangeQuery(0, 0), "Single element [0,0] should be 1");
        assertEquals(5, tree.rangeQuery(4, 4), "Single element [4,4] should be 5");
        assertEquals(7, tree.rangeQuery(2, 3), "Sum [2,3] should be 7");
    }
    
    @Test
    @Order(2)
    @DisplayName("LazySegmentTreeSum - Range Update")
    public void testRangeUpdate() {
        int[] arr = {1, 2, 3, 4, 5};
        LazySegmentTreeSum tree = new LazySegmentTreeSum(arr);
        
        // Add 10 to elements [1,3]
        tree.rangeUpdate(1, 3, 10);
        
        // Array becomes: [1, 12, 13, 14, 5]
        assertEquals(45, tree.rangeQuery(0, 4), "Sum after range update should be 45");
        assertEquals(39, tree.rangeQuery(1, 3), "Sum [1,3] after update should be 39");
        assertEquals(1, tree.rangeQuery(0, 0), "Element [0] unchanged should be 1");
        assertEquals(5, tree.rangeQuery(4, 4), "Element [4] unchanged should be 5");
    }
    
    @Test
    @Order(3)
    @DisplayName("LazySegmentTreeSum - Multiple Range Updates")
    public void testMultipleRangeUpdates() {
        int[] arr = {1, 2, 3, 4, 5};
        LazySegmentTreeSum tree = new LazySegmentTreeSum(arr);
        
        tree.rangeUpdate(0, 2, 5);   // [6, 7, 8, 4, 5]
        tree.rangeUpdate(2, 4, 3);   // [6, 7, 11, 7, 8]
        tree.rangeUpdate(1, 3, -2);  // [6, 5, 9, 5, 8]
        
        assertEquals(33, tree.rangeQuery(0, 4), "Sum after multiple updates");
        assertEquals(19, tree.rangeQuery(1, 3), "Sum [1,3] after updates");
        assertEquals(6, tree.rangeQuery(0, 0), "First element after updates");
        assertEquals(8, tree.rangeQuery(4, 4), "Last element after updates");
    }
    
    @Test
    @Order(4)
    @DisplayName("LazySegmentTreeSum - Point Update")
    public void testPointUpdate() {
        int[] arr = {1, 2, 3, 4, 5};
        LazySegmentTreeSum tree = new LazySegmentTreeSum(arr);
        
        tree.pointUpdate(2, 10);  // [1, 2, 10, 4, 5]
        
        assertEquals(22, tree.rangeQuery(0, 4), "Sum after point update");
        assertEquals(10, tree.rangeQuery(2, 2), "Updated element should be 10");
        assertEquals(16, tree.rangeQuery(1, 3), "Sum [1,3] after point update");
    }
    
    @Test
    @Order(5)
    @DisplayName("LazySegmentTreeSum - Edge Cases")
    public void testSumEdgeCases() {
        // Single element
        int[] single = {42};
        LazySegmentTreeSum singleTree = new LazySegmentTreeSum(single);
        assertEquals(42, singleTree.rangeQuery(0, 0), "Single element query");
        singleTree.rangeUpdate(0, 0, 8);
        assertEquals(50, singleTree.rangeQuery(0, 0), "Single element after update");
        
        // Two elements
        int[] two = {10, 20};
        LazySegmentTreeSum twoTree = new LazySegmentTreeSum(two);
        assertEquals(30, twoTree.rangeQuery(0, 1), "Two elements sum");
        twoTree.rangeUpdate(0, 1, 5);
        assertEquals(40, twoTree.rangeQuery(0, 1), "Two elements after update");
        
        // All zeros
        int[] zeros = {0, 0, 0, 0};
        LazySegmentTreeSum zeroTree = new LazySegmentTreeSum(zeros);
        assertEquals(0, zeroTree.rangeQuery(0, 3), "All zeros sum");
        zeroTree.rangeUpdate(1, 2, 5);
        assertEquals(10, zeroTree.rangeQuery(0, 3), "Zeros after update");
    }
    
    @Test
    @Order(6)
    @DisplayName("LazySegmentTreeSum - Negative Values")
    public void testNegativeValues() {
        int[] arr = {-5, 3, -2, 8, -1};
        LazySegmentTreeSum tree = new LazySegmentTreeSum(arr);
        
        assertEquals(3, tree.rangeQuery(0, 4), "Sum with negative values");
        assertEquals(-4, tree.rangeQuery(0, 2), "Sum of negative range");
        
        tree.rangeUpdate(1, 3, -5);  // [-5, -2, -7, 3, -1]
        assertEquals(-12, tree.rangeQuery(0, 4), "Sum after negative update");
    }
    
    @Test
    @Order(7)
    @DisplayName("LazySegmentTreeSum - Large Array")
    public void testLargeArray() {
        int n = 10000;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i + 1;
        }
        
        LazySegmentTreeSum tree = new LazySegmentTreeSum(arr);
        
        // Sum of 1 to 10000 = 10000 * 10001 / 2 = 50005000
        assertEquals(50005000L, tree.rangeQuery(0, n - 1), "Large array sum");
        
        // Range update
        tree.rangeUpdate(0, 999, 10);
        // Added 10 * 1000 = 10000
        assertEquals(50015000L, tree.rangeQuery(0, n - 1), "Large array after update");
    }
    
    // ========== LAZY SEGMENT TREE MIN TESTS ==========
    
    @Test
    @Order(8)
    @DisplayName("LazySegmentTreeMin - Basic Build and Query")
    public void testMinBasicBuildAndQuery() {
        int[] arr = {5, 3, 7, 2, 8, 1, 9};
        LazySegmentTreeMin tree = new LazySegmentTreeMin(arr);
        
        assertEquals(1, tree.rangeMin(0, 6), "Min of entire array should be 1");
        assertEquals(2, tree.rangeMin(2, 4), "Min [2,4] should be 2");
        assertEquals(3, tree.rangeMin(0, 1), "Min [0,1] should be 3");
        assertEquals(9, tree.rangeMin(6, 6), "Single element [6,6] should be 9");
    }
    
    @Test
    @Order(9)
    @DisplayName("LazySegmentTreeMin - Range Set")
    public void testRangeSet() {
        int[] arr = {5, 3, 7, 2, 8, 1, 9};
        LazySegmentTreeMin tree = new LazySegmentTreeMin(arr);
        
        tree.rangeSet(2, 4, 4);  // [5, 3, 4, 4, 4, 1, 9]
        
        assertEquals(1, tree.rangeMin(0, 6), "Min after range set");
        assertEquals(4, tree.rangeMin(2, 4), "Min [2,4] after set should be 4");
        assertEquals(3, tree.rangeMin(0, 1), "Unchanged range [0,1]");
    }
    
    @Test
    @Order(10)
    @DisplayName("LazySegmentTreeMin - Multiple Range Sets")
    public void testMultipleRangeSets() {
        int[] arr = {10, 20, 30, 40, 50};
        LazySegmentTreeMin tree = new LazySegmentTreeMin(arr);
        
        tree.rangeSet(0, 2, 15);   // [15, 15, 15, 40, 50]
        tree.rangeSet(2, 4, 25);   // [15, 15, 25, 25, 25]
        tree.rangeSet(1, 3, 5);    // [15, 5, 5, 5, 25]
        
        assertEquals(5, tree.rangeMin(0, 4), "Min after multiple sets");
        assertEquals(5, tree.rangeMin(1, 3), "Min [1,3] should be 5");
        assertEquals(15, tree.rangeMin(0, 0), "First element should be 15");
        assertEquals(25, tree.rangeMin(4, 4), "Last element should be 25");
    }
    
    @Test
    @Order(11)
    @DisplayName("LazySegmentTreeMin - Edge Cases")
    public void testMinEdgeCases() {
        // Single element
        int[] single = {100};
        LazySegmentTreeMin singleTree = new LazySegmentTreeMin(single);
        assertEquals(100, singleTree.rangeMin(0, 0), "Single element");
        singleTree.rangeSet(0, 0, 50);
        assertEquals(50, singleTree.rangeMin(0, 0), "Single element after set");
        
        // All same values
        int[] same = {7, 7, 7, 7};
        LazySegmentTreeMin sameTree = new LazySegmentTreeMin(same);
        assertEquals(7, sameTree.rangeMin(0, 3), "All same values");
        sameTree.rangeSet(1, 2, 3);
        assertEquals(3, sameTree.rangeMin(0, 3), "Min after partial set");
    }
    
    @Test
    @Order(12)
    @DisplayName("LazySegmentTreeMin - Overlapping Sets")
    public void testOverlappingSets() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
        LazySegmentTreeMin tree = new LazySegmentTreeMin(arr);
        
        tree.rangeSet(1, 5, 10);   // [1, 10, 10, 10, 10, 10, 7, 8]
        tree.rangeSet(3, 6, 5);    // [1, 10, 10, 5, 5, 5, 5, 8]
        
        assertEquals(1, tree.rangeMin(0, 7), "Overall min");
        assertEquals(5, tree.rangeMin(3, 6), "Min of set range [3,6]");
        assertEquals(5, tree.rangeMin(2, 5), "Min crossing boundaries");
    }
    
    // ========== ADVANCED LAZY SEGMENT TREE TESTS ==========
    
    @Test
    @Order(13)
    @DisplayName("AdvancedLazySegmentTree - Basic Operations")
    public void testAdvancedBasicOps() {
        int[] arr = {1, 2, 3, 4, 5};
        AdvancedLazySegmentTree tree = new AdvancedLazySegmentTree(arr);
        
        assertEquals(15, tree.rangeSum(0, 4), "Initial sum");
        assertEquals(9, tree.rangeSum(1, 3), "Partial sum");
    }
    
    @Test
    @Order(14)
    @DisplayName("AdvancedLazySegmentTree - Range Set")
    public void testAdvancedRangeSet() {
        int[] arr = {1, 2, 3, 4, 5};
        AdvancedLazySegmentTree tree = new AdvancedLazySegmentTree(arr);
        
        tree.rangeSet(1, 3, 10);  // [1, 10, 10, 10, 5]
        
        assertEquals(36, tree.rangeSum(0, 4), "Sum after range set");
        assertEquals(30, tree.rangeSum(1, 3), "Sum of set range");
        assertEquals(1, tree.rangeSum(0, 0), "Unchanged element");
    }
    
    @Test
    @Order(15)
    @DisplayName("AdvancedLazySegmentTree - Range Add")
    public void testAdvancedRangeAdd() {
        int[] arr = {1, 2, 3, 4, 5};
        AdvancedLazySegmentTree tree = new AdvancedLazySegmentTree(arr);
        
        tree.rangeAdd(0, 4, 5);  // [6, 7, 8, 9, 10]
        
        assertEquals(40, tree.rangeSum(0, 4), "Sum after range add");
        assertEquals(24, tree.rangeSum(1, 3), "Partial sum after add");
    }
    
    @Test
    @Order(16)
    @DisplayName("AdvancedLazySegmentTree - Combined Set and Add")
    public void testCombinedSetAndAdd() {
        int[] arr = {1, 2, 3, 4, 5};
        AdvancedLazySegmentTree tree = new AdvancedLazySegmentTree(arr);
        
        tree.rangeSet(1, 3, 10);   // [1, 10, 10, 10, 5]
        tree.rangeAdd(0, 4, 5);    // [6, 15, 15, 15, 10]
        
        assertEquals(61, tree.rangeSum(0, 4), "Sum after set and add");
        assertEquals(45, tree.rangeSum(1, 3), "Sum [1,3] after operations");
    }
    
    @Test
    @Order(17)
    @DisplayName("AdvancedLazySegmentTree - Add Then Set")
    public void testAddThenSet() {
        int[] arr = {1, 2, 3, 4, 5};
        AdvancedLazySegmentTree tree = new AdvancedLazySegmentTree(arr);
        
        tree.rangeAdd(0, 4, 10);   // [11, 12, 13, 14, 15]
        tree.rangeSet(2, 3, 20);   // [11, 12, 20, 20, 15]
        
        assertEquals(78, tree.rangeSum(0, 4), "Sum after add then set");
        assertEquals(40, tree.rangeSum(2, 3), "Sum of set range");
        assertEquals(23, tree.rangeSum(0, 1), "Sum of add-only range");
    }
    
    @Test
    @Order(18)
    @DisplayName("AdvancedLazySegmentTree - Overlapping Operations")
    public void testOverlappingOperations() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
        AdvancedLazySegmentTree tree = new AdvancedLazySegmentTree(arr);
        
        tree.rangeSet(1, 5, 10);   // [1, 10, 10, 10, 10, 10, 7, 8]
        tree.rangeAdd(3, 6, 5);    // [1, 10, 10, 15, 15, 15, 12, 8]
        tree.rangeSet(0, 2, 3);    // [3, 3, 3, 15, 15, 15, 12, 8]
        tree.rangeAdd(4, 7, -2);   // [3, 3, 3, 15, 13, 13, 10, 6]
        
        assertEquals(66, tree.rangeSum(0, 7), "Sum after complex operations");
        assertEquals(51, tree.rangeSum(3, 6), "Partial sum");
    }
    
    @Test
    @Order(19)
    @DisplayName("AdvancedLazySegmentTree - Edge Cases")
    public void testAdvancedEdgeCases() {
        // Single element
        int[] single = {5};
        AdvancedLazySegmentTree singleTree = new AdvancedLazySegmentTree(single);
        assertEquals(5, singleTree.rangeSum(0, 0));
        singleTree.rangeAdd(0, 0, 3);
        assertEquals(8, singleTree.rangeSum(0, 0));
        singleTree.rangeSet(0, 0, 10);
        assertEquals(10, singleTree.rangeSum(0, 0));
        
        // Empty initial values
        int[] zeros = {0, 0, 0, 0};
        AdvancedLazySegmentTree zeroTree = new AdvancedLazySegmentTree(zeros);
        assertEquals(0, zeroTree.rangeSum(0, 3));
        zeroTree.rangeAdd(1, 2, 5);
        assertEquals(10, zeroTree.rangeSum(0, 3));
    }
    
    // ========== LEETCODE PROBLEM TESTS ==========
    
    @Test
    @Order(20)
    @DisplayName("LeetCode 307 - NumArray")
    public void testNumArray() {
        int[] nums = {1, 3, 5};
        NumArray obj = new NumArray(nums);
        
        assertEquals(9, obj.sumRange(0, 2), "Initial sum [0,2]");
        
        obj.update(1, 2);
        assertEquals(8, obj.sumRange(0, 2), "Sum after update");
        
        obj.update(0, 10);
        assertEquals(17, obj.sumRange(0, 2), "Sum after second update");
        
        assertEquals(2, obj.sumRange(1, 1), "Single element sum");
    }
    
    @Test
    @Order(21)
    @DisplayName("LeetCode 307 - NumArray Edge Cases")
    public void testNumArrayEdgeCases() {
        // Single element
        int[] single = {5};
        NumArray singleArr = new NumArray(single);
        assertEquals(5, singleArr.sumRange(0, 0));
        singleArr.update(0, 10);
        assertEquals(10, singleArr.sumRange(0, 0));
        
        // Multiple updates to same index
        int[] arr = {1, 2, 3};
        NumArray multiUpdate = new NumArray(arr);
        multiUpdate.update(1, 5);
        multiUpdate.update(1, 8);
        multiUpdate.update(1, 3);
        assertEquals(7, multiUpdate.sumRange(0, 2));
    }
    
    @Test
    @Order(22)
    @DisplayName("LeetCode 699 - Falling Squares")
    public void testFallingSquares() {
        FallingSquares solution = new FallingSquares();
        
        // Test case 1
        int[][] positions1 = {{1, 2}, {2, 3}, {6, 1}};
        List<Integer> result1 = solution.fallingSquares(positions1);
        assertEquals(Arrays.asList(2, 5, 5), result1, "Test case 1");
        
        // Test case 2
        int[][] positions2 = {{100, 100}, {200, 100}};
        List<Integer> result2 = solution.fallingSquares(positions2);
        assertEquals(Arrays.asList(100, 100), result2, "Test case 2 - non-overlapping");
    }
    
    @Test
    @Order(23)
    @DisplayName("Falling Squares - Overlapping Squares")
    public void testFallingSquaresOverlap() {
        FallingSquares solution = new FallingSquares();
        
        // Squares that overlap
        int[][] positions = {{1, 5}, {2, 2}, {3, 3}};
        List<Integer> result = solution.fallingSquares(positions);
        assertEquals(Arrays.asList(5, 7, 10), result, "Overlapping squares");
    }
    
    @Test
    @Order(24)
    @DisplayName("Falling Squares - Single Square")
    public void testFallingSquaresSingle() {
        FallingSquares solution = new FallingSquares();
        
        int[][] positions = {{10, 20}};
        List<Integer> result = solution.fallingSquares(positions);
        assertEquals(Arrays.asList(20), result, "Single square");
    }
    
    // ========== PERFORMANCE AND STRESS TESTS ==========
    
    @Test
    @Order(25)
    @DisplayName("Stress Test - Many Updates")
    public void testManyUpdates() {
        int n = 1000;
        int[] arr = new int[n];
        Arrays.fill(arr, 1);
        
        LazySegmentTreeSum tree = new LazySegmentTreeSum(arr);
        
        // Perform many updates
        for (int i = 0; i < 100; i++) {
            tree.rangeUpdate(i, i + 10, 1);
        }
        
        // Verify some queries
        long sum = tree.rangeQuery(0, n - 1);
        assertTrue(sum > n, "Sum should increase after updates");
    }
    
    @Test
    @Order(26)
    @DisplayName("Stress Test - Interleaved Operations")
    public void testInterleavedOperations() {
        int n = 500;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i + 1;
        }
        
        AdvancedLazySegmentTree tree = new AdvancedLazySegmentTree(arr);
        
        long initialSum = tree.rangeSum(0, n - 1);
        
        // Interleave set and add operations
        for (int i = 0; i < 50; i++) {
            tree.rangeSet(i * 2, Math.min(i * 2 + 5, n - 1), 10);
            tree.rangeAdd(i * 3, Math.min(i * 3 + 7, n - 1), 2);
        }
        
        long finalSum = tree.rangeSum(0, n - 1);
        assertTrue(finalSum != initialSum, "Sum should change after operations");
    }
    
    @Test
    @Order(27)
    @DisplayName("Correctness - Simulate with Array")
    public void testCorrectnessWithSimulation() {
        int n = 50;
        int[] arr = new int[n];
        long[] simulated = new long[n];
        
        for (int i = 0; i < n; i++) {
            arr[i] = i + 1;
            simulated[i] = i + 1;
        }
        
        LazySegmentTreeSum tree = new LazySegmentTreeSum(arr);
        
        // Perform operations and verify
        int[][] operations = {
            {0, 10, 5},
            {5, 15, -3},
            {20, 30, 10},
            {0, 49, 2}
        };
        
        for (int[] op : operations) {
            int l = op[0], r = op[1], delta = op[2];
            
            // Update tree
            tree.rangeUpdate(l, r, delta);
            
            // Update simulated array
            for (int i = l; i <= r; i++) {
                simulated[i] += delta;
            }
        }
        
        // Verify random ranges
        Random rand = new Random(42);
        for (int i = 0; i < 20; i++) {
            int l = rand.nextInt(n);
            int r = l + rand.nextInt(n - l);
            
            long expectedSum = 0;
            for (int j = l; j <= r; j++) {
                expectedSum += simulated[j];
            }
            
            assertEquals(expectedSum, tree.rangeQuery(l, r), 
                "Range [" + l + "," + r + "] sum mismatch");
        }
    }
    
    @Test
    @Order(28)
    @DisplayName("Correctness - Min Tree Simulation")
    public void testMinTreeCorrectness() {
        int n = 40;
        int[] arr = new int[n];
        long[] simulated = new long[n];
        
        for (int i = 0; i < n; i++) {
            arr[i] = (i * 7 + 3) % 20;
            simulated[i] = arr[i];
        }
        
        LazySegmentTreeMin tree = new LazySegmentTreeMin(arr);
        
        // Perform set operations
        int[][] operations = {
            {5, 10, 15},
            {0, 8, 20},
            {15, 25, 5},
            {30, 39, 100}
        };
        
        for (int[] op : operations) {
            int l = op[0], r = op[1], val = op[2];
            
            tree.rangeSet(l, r, val);
            
            for (int i = l; i <= r; i++) {
                simulated[i] = val;
            }
        }
        
        // Verify random ranges
        Random rand = new Random(123);
        for (int i = 0; i < 20; i++) {
            int l = rand.nextInt(n);
            int r = l + rand.nextInt(n - l);
            
            long expectedMin = Long.MAX_VALUE;
            for (int j = l; j <= r; j++) {
                expectedMin = Math.min(expectedMin, simulated[j]);
            }
            
            assertEquals(expectedMin, tree.rangeMin(l, r),
                "Range [" + l + "," + r + "] min mismatch");
        }
    }
    
    @Test
    @Order(29)
    @DisplayName("Boundary Conditions - Full Range Operations")
    public void testFullRangeOperations() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        LazySegmentTreeSum tree = new LazySegmentTreeSum(arr);
        
        // Update entire range
        tree.rangeUpdate(0, 9, 5);
        assertEquals(105, tree.rangeQuery(0, 9), "Full range update");
        
        // Query entire range
        long sum = tree.rangeQuery(0, 9);
        assertEquals(105, sum, "Full range query");
    }
    
    @Test
    @Order(30)
    @DisplayName("Boundary Conditions - Adjacent Ranges")
    public void testAdjacentRanges() {
        int[] arr = {1, 2, 3, 4, 5, 6};
        LazySegmentTreeSum tree = new LazySegmentTreeSum(arr);
        
        tree.rangeUpdate(0, 2, 10);  // [11, 12, 13, 4, 5, 6]
        tree.rangeUpdate(3, 5, 5);   // [11, 12, 13, 9, 10, 11]
        
        assertEquals(36, tree.rangeQuery(0, 2), "First range");
        assertEquals(30, tree.rangeQuery(3, 5), "Second range");
        assertEquals(66, tree.rangeQuery(0, 5), "Combined range");
    }
    
    // ========== HELPER METHODS ==========
    
    private static void printArray(String label, long[] arr) {
        System.out.print(label + ": [");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}
