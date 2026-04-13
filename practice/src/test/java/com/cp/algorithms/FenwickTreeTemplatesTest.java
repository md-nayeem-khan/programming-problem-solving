package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import com.cp.algorithms.FenwickTreeTemplates.*;

/**
 * Comprehensive test suite for FenwickTreeTemplates
 * Tests correctness, edge cases, and performance characteristics
 */
public class FenwickTreeTemplatesTest {

    @Nested
    @DisplayName("Standard Fenwick Tree Tests")
    class StandardFenwickTreeTest {
        
        @Test
        @DisplayName("Basic construction and queries")
        void testBasicOperations() {
            int[] arr = {1, 3, 5, 7, 9, 11};
            FenwickTree ft = new FenwickTree(arr);
            
            // Test prefix sums
            assertEquals(1, ft.query(0)); // arr[0]
            assertEquals(4, ft.query(1)); // arr[0] + arr[1]
            assertEquals(9, ft.query(2)); // arr[0] + arr[1] + arr[2]
            assertEquals(36, ft.query(5)); // sum of all elements
            
            // Test range queries
            assertEquals(9, ft.rangeQuery(0, 2)); // 1+3+5
            assertEquals(8, ft.rangeQuery(1, 2)); // 3+5
            assertEquals(16, ft.rangeQuery(3, 4)); // 7+9
            assertEquals(0, ft.rangeQuery(3, 2)); // invalid range
        }
        
        @Test
        @DisplayName("Point updates")
        void testUpdates() {
            int[] arr = {1, 2, 3, 4, 5};
            FenwickTree ft = new FenwickTree(arr);
            
            // Initial sum
            assertEquals(15, ft.query(4));
            
            // Update and verify
            ft.update(2, 10); // add 10 to arr[2], so arr becomes [1,2,13,4,5]
            assertEquals(25, ft.query(4)); // sum should increase by 10
            assertEquals(19, ft.rangeQuery(1, 3)); // 2+13+4 = 19 (not 16)
            
            // Negative update
            ft.update(1, -1); // arr[1] becomes 1, so arr is [1,1,13,4,5]
            assertEquals(24, ft.query(4)); // sum should decrease by 1
            assertEquals(18, ft.rangeQuery(1, 3)); // 1+13+4 = 18 (not 16)
        }
        
        @Test
        @DisplayName("Set operation")
        void testSetOperation() {
            int[] arr = {1, 2, 3, 4, 5};
            FenwickTree ft = new FenwickTree(arr);
            
            // Set arr[2] = 100
            ft.set(2, 100);
            assertEquals(100, ft.rangeQuery(2, 2)); // single element
            assertEquals(112, ft.query(4)); // 1+2+100+4+5
        }
        
        @Test
        @DisplayName("Empty and single element")
        void testEdgeCases() {
            // Single element
            FenwickTree ft1 = new FenwickTree(new int[]{42});
            assertEquals(42, ft1.query(0));
            assertEquals(42, ft1.rangeQuery(0, 0));
            
            // Empty tree construction
            FenwickTree ft2 = new FenwickTree(5);
            assertEquals(0, ft2.query(4));
            ft2.update(2, 10);
            assertEquals(10, ft2.query(4));
        }
        
        @Test
        @DisplayName("Optimized build vs regular build")
        void testOptimizedBuild() {
            int[] arr = {1, 3, 5, 7, 9, 11, 13, 15};
            
            FenwickTree ft1 = new FenwickTree(arr);
            FenwickTree ft2 = FenwickTree.buildOptimized(arr);
            
            // Should produce identical results
            for (int i = 0; i < arr.length; i++) {
                assertEquals(ft1.query(i), ft2.query(i), "Query " + i + " differs");
                for (int j = i; j < arr.length; j++) {
                    assertEquals(ft1.rangeQuery(i, j), ft2.rangeQuery(i, j), 
                               "Range query [" + i + "," + j + "] differs");
                }
            }
        }
        
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 4, 8, 15, 16, 31, 32, 100})
        @DisplayName("Different array sizes")
        void testDifferentSizes(int size) {
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = i + 1;
            }
            
            FenwickTree ft = new FenwickTree(arr);
            
            // Test prefix sum formula: sum of 1..n = n*(n+1)/2
            int expectedSum = size * (size + 1) / 2;
            assertEquals(expectedSum, ft.query(size - 1));
        }
        
        @Test
        @DisplayName("Large values and potential overflow")
        void testLargeValues() {
            // Test with values that might cause overflow
            FenwickTree ft = new FenwickTree(5);
            ft.update(0, Integer.MAX_VALUE / 4);
            ft.update(1, Integer.MAX_VALUE / 4);
            ft.update(2, Integer.MAX_VALUE / 4);
            ft.update(3, Integer.MAX_VALUE / 4);
            
            // This should not overflow - result is Integer.MAX_VALUE - 3
            long expected = (long) Integer.MAX_VALUE - 3; // Due to integer division truncation
            assertEquals(expected, (long) ft.query(3));
        }
        
        @Test
        @DisplayName("Boundary index validation")
        void testBoundaryConditions() {
            FenwickTree ft = new FenwickTree(new int[]{1, 2, 3});
            
            // Valid operations
            assertDoesNotThrow(() -> ft.query(0));
            assertDoesNotThrow(() -> ft.query(2));
            assertDoesNotThrow(() -> ft.update(0, 5));
            assertDoesNotThrow(() -> ft.update(2, 5));
            
            // Edge case: Invalid ranges should return 0
            assertEquals(0, ft.rangeQuery(2, 1)); // left > right
        }
    }
    
    @Nested
    @DisplayName("Range Update Point Query Tests")
    class RangeUpdatePointQueryTest {
        
        @Test
        @DisplayName("Basic range updates")
        void testRangeUpdates() {
            int[] arr = {1, 2, 3, 4, 5};
            FenwickTreeRangeUpdate ft = new FenwickTreeRangeUpdate(arr);
            
            // Initial values
            assertEquals(1, ft.pointQuery(0));
            assertEquals(3, ft.pointQuery(2));
            assertEquals(5, ft.pointQuery(4));
            
            // Range update [1, 3] += 10
            ft.rangeUpdate(1, 3, 10);
            assertEquals(1, ft.pointQuery(0)); // unchanged
            assertEquals(12, ft.pointQuery(1)); // 2 + 10
            assertEquals(13, ft.pointQuery(2)); // 3 + 10
            assertEquals(14, ft.pointQuery(3)); // 4 + 10
            assertEquals(5, ft.pointQuery(4)); // unchanged
        }
        
        @Test
        @DisplayName("Multiple overlapping range updates")
        void testOverlappingRangeUpdates() {
            FenwickTreeRangeUpdate ft = new FenwickTreeRangeUpdate(5);
            
            ft.rangeUpdate(0, 4, 1); // All elements += 1
            ft.rangeUpdate(1, 3, 2); // Middle elements += 2 more
            ft.rangeUpdate(2, 2, 3); // Middle element += 3 more
            
            assertEquals(1, ft.pointQuery(0)); // 0 + 1
            assertEquals(3, ft.pointQuery(1)); // 0 + 1 + 2
            assertEquals(6, ft.pointQuery(2)); // 0 + 1 + 2 + 3
            assertEquals(3, ft.pointQuery(3)); // 0 + 1 + 2
            assertEquals(1, ft.pointQuery(4)); // 0 + 1
        }
        
        @Test
        @DisplayName("Single element range updates")
        void testSingleElementUpdates() {
            FenwickTreeRangeUpdate ft = new FenwickTreeRangeUpdate(new int[]{1, 2, 3, 4, 5});
            
            // Update single elements
            ft.rangeUpdate(0, 0, 10);
            ft.rangeUpdate(2, 2, 20);
            ft.rangeUpdate(4, 4, 30);
            
            assertEquals(11, ft.pointQuery(0));
            assertEquals(2, ft.pointQuery(1));
            assertEquals(23, ft.pointQuery(2));
            assertEquals(4, ft.pointQuery(3));
            assertEquals(35, ft.pointQuery(4));
        }
        
        @Test
        @DisplayName("Edge cases and boundary conditions")
        void testEdgeCases() {
            // Single element array
            FenwickTreeRangeUpdate ft1 = new FenwickTreeRangeUpdate(new int[]{42});
            assertEquals(42, ft1.pointQuery(0));
            ft1.rangeUpdate(0, 0, 10);
            assertEquals(52, ft1.pointQuery(0));
            
            // Empty initialization
            FenwickTreeRangeUpdate ft2 = new FenwickTreeRangeUpdate(3);
            assertEquals(0, ft2.pointQuery(1));
            ft2.rangeUpdate(0, 2, 5);
            assertEquals(5, ft2.pointQuery(1));
        }
    }
    
    @Nested
    @DisplayName("Range Update Range Query Tests")
    class RangeUpdateRangeQueryTest {
        
        @Test
        @DisplayName("Basic functionality")
        void testBasicFunctionality() {
            FenwickTreeRangeUpdateRangeQuery ft = new FenwickTreeRangeUpdateRangeQuery(5);
            
            // Add 10 to range [1, 3]
            ft.rangeUpdate(1, 3, 10);
            
            // Test range queries
            assertEquals(0, ft.rangeQuery(0, 0)); // unchanged
            assertEquals(10, ft.rangeQuery(1, 1)); // single updated element
            assertEquals(20, ft.rangeQuery(1, 2)); // two updated elements
            assertEquals(30, ft.rangeQuery(1, 3)); // three updated elements
            assertEquals(10, ft.rangeQuery(2, 2)); // single updated element
        }
        
        @Test
        @DisplayName("Multiple range updates")
        void testMultipleRangeUpdates() {
            FenwickTreeRangeUpdateRangeQuery ft = new FenwickTreeRangeUpdateRangeQuery(6);
            
            ft.rangeUpdate(0, 5, 1); // All += 1
            ft.rangeUpdate(2, 4, 2); // Middle += 2
            ft.rangeUpdate(1, 3, 3); // Overlapping += 3
            
            // Expected values: [1, 4, 6, 6, 3, 1]
            assertEquals(1, ft.rangeQuery(0, 0));
            assertEquals(5, ft.rangeQuery(0, 1)); // 1 + 4
            assertEquals(11, ft.rangeQuery(0, 2)); // 1 + 4 + 6
            assertEquals(17, ft.rangeQuery(0, 3)); // 1 + 4 + 6 + 6
        }
        
        // Remove the bug test since the bug is now fixed
        @Test
        @DisplayName("Range update implementation working correctly")
        void testRangeUpdateFixed() {
            FenwickTreeRangeUpdateRangeQuery ft = new FenwickTreeRangeUpdateRangeQuery(3);
            
            // This should work correctly now
            ft.rangeUpdate(0, 1, 5);
            assertEquals(10, ft.rangeQuery(0, 1)); // 5 + 5 = 10
            assertEquals(5, ft.rangeQuery(0, 0)); // just first element
            assertEquals(5, ft.rangeQuery(1, 1)); // just second element
        }
    }
    
    @Nested
    @DisplayName("2D Fenwick Tree Tests")
    class FenwickTree2DTest {
        
        @Test
        @DisplayName("Basic 2D operations")
        void testBasic2DOperations() {
            int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
            };
            FenwickTree2D ft = new FenwickTree2D(matrix);
            
            // Test rectangle sums
            assertEquals(1, ft.query(0, 0)); // single cell
            assertEquals(12, ft.rangeQuery(0, 0, 1, 1)); // 1+2+4+5
            assertEquals(45, ft.rangeQuery(0, 0, 2, 2)); // entire matrix
            assertEquals(28, ft.rangeQuery(1, 1, 2, 2)); // 5+6+8+9 = 28 (not 21)
        }
        
        @Test
        @DisplayName("2D updates")
        void test2DUpdates() {
            FenwickTree2D ft = new FenwickTree2D(3, 3);
            
            ft.update(1, 1, 10);
            assertEquals(10, ft.query(1, 1));
            assertEquals(10, ft.rangeQuery(0, 0, 2, 2));
            
            ft.update(0, 0, 5);
            assertEquals(15, ft.query(1, 1)); // 5 + 10
            assertEquals(15, ft.rangeQuery(0, 0, 2, 2));
        }
        
        @Test
        @DisplayName("2D edge cases")
        void test2DEdgeCases() {
            // Single cell matrix
            FenwickTree2D ft1 = new FenwickTree2D(new int[][]{{42}});
            assertEquals(42, ft1.query(0, 0));
            assertEquals(42, ft1.rangeQuery(0, 0, 0, 0));
            
            // Rectangle matrix
            int[][] matrix = {{1, 2}, {3, 4}, {5, 6}};
            FenwickTree2D ft2 = new FenwickTree2D(matrix);
            assertEquals(10, ft2.rangeQuery(0, 0, 1, 1)); // 1+2+3+4
        }
    }
    
    @Nested
    @DisplayName("Inversion Counter Tests")
    class InversionCounterTest {
        
        @Test
        @DisplayName("Basic inversion counting")
        void testBasicInversions() {
            assertEquals(0, InversionCounter.countInversions(new int[]{1, 2, 3, 4}));
            assertEquals(6, InversionCounter.countInversions(new int[]{4, 3, 2, 1}));
            assertEquals(4, InversionCounter.countInversions(new int[]{5, 2, 6, 1}));
        }
        
        @Test
        @DisplayName("Count smaller numbers after self")
        void testCountSmaller() {
            assertArrayEquals(new int[]{2, 1, 1, 0}, 
                InversionCounter.countSmaller(new int[]{5, 2, 6, 1}));
            assertArrayEquals(new int[]{0, 0, 0, 0}, 
                InversionCounter.countSmaller(new int[]{1, 2, 3, 4}));
            assertArrayEquals(new int[]{3, 2, 1, 0}, 
                InversionCounter.countSmaller(new int[]{4, 3, 2, 1}));
        }
        
        @Test
        @DisplayName("Edge cases for inversion counting")
        void testInversionEdgeCases() {
            assertEquals(0, InversionCounter.countInversions(new int[]{}));
            assertEquals(0, InversionCounter.countInversions(new int[]{42}));
            assertEquals(0, InversionCounter.countInversions(new int[]{1, 1, 1}));
        }
        
        @Test
        @DisplayName("Duplicate elements")
        void testDuplicateElements() {
            assertEquals(3, InversionCounter.countInversions(new int[]{3, 1, 1, 2}));
            assertArrayEquals(new int[]{2, 0, 0}, 
                InversionCounter.countSmaller(new int[]{3, 1, 1}));
        }
    }
    
    @Nested
    @DisplayName("Order Statistics Tests")
    class OrderStatisticsTest {
        
        @Test
        @DisplayName("Basic order statistics")
        void testBasicOrderStatistics() {
            OrderStatistics os = new OrderStatistics(100);
            
            os.insert(5);
            os.insert(3);
            os.insert(7);
            os.insert(3);
            os.insert(9);
            
            // Elements: [3, 3, 5, 7, 9]
            assertEquals(3, os.kthSmallest(1));
            assertEquals(3, os.kthSmallest(2));
            assertEquals(5, os.kthSmallest(3));
            assertEquals(7, os.kthSmallest(4));
            assertEquals(9, os.kthSmallest(5));
        }
        
        @Test
        @DisplayName("Count operations")
        void testCountOperations() {
            OrderStatistics os = new OrderStatistics(100);
            os.insert(5);
            os.insert(3);
            os.insert(7);
            os.insert(3);
            
            assertEquals(0, os.countLess(3));
            assertEquals(2, os.countLessOrEqual(3));
            assertEquals(2, os.countLess(5));
            assertEquals(3, os.countLessOrEqual(5));
            assertEquals(3, os.countLess(7));
            assertEquals(4, os.countLessOrEqual(7));
        }
        
        @Test
        @DisplayName("Insert and remove operations")
        void testInsertRemove() {
            OrderStatistics os = new OrderStatistics(10);
            
            os.insert(5);
            os.insert(3);
            os.insert(5);
            assertEquals(3, os.kthSmallest(1));
            assertEquals(5, os.kthSmallest(2));
            assertEquals(5, os.kthSmallest(3));
            
            os.remove(5);
            assertEquals(3, os.kthSmallest(1));
            assertEquals(5, os.kthSmallest(2));
            
            os.remove(3);
            assertEquals(5, os.kthSmallest(1));
        }
        
        @Test
        @DisplayName("Compare kthSmallest implementations")
        void testKthSmallestImplementations() {
            OrderStatistics os = new OrderStatistics(20);
            
            for (int i : new int[]{5, 3, 8, 3, 12, 1, 8, 15}) {
                os.insert(i);
            }
            
            // Both implementations should return same results
            for (int k = 1; k <= 8; k++) {
                assertEquals(os.kthSmallest(k), os.kthSmallestFast(k), 
                           "kth smallest differs at k=" + k);
            }
        }
    }
    
    @Nested
    @DisplayName("Range Sum Count Tests")
    class RangeSumCountTest {
        
        @Test
        @DisplayName("Basic range sum counting")
        void testBasicRangeSumCount() {
            assertEquals(3, RangeSumCount.countRangeSum(new int[]{-2, 5, -1}, -2, 2));
            assertEquals(1, RangeSumCount.countRangeSum(new int[]{0}, 0, 0));
        }
        
        @Test
        @DisplayName("Edge cases for range sum")
        void testRangeSumEdgeCases() {
            assertEquals(0, RangeSumCount.countRangeSum(new int[]{}, 0, 1));
            assertEquals(1, RangeSumCount.countRangeSum(new int[]{1}, 0, 2));
            assertEquals(0, RangeSumCount.countRangeSum(new int[]{10}, 0, 5));
        }
    }
    
    @Nested
    @DisplayName("Performance and Stress Tests")
    class PerformanceTest {
        
        @Test
        @DisplayName("Performance stress test")
        void testPerformance() {
            int n = 10000;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = i + 1;
            }
            
            long startTime = System.currentTimeMillis();
            FenwickTree ft = new FenwickTree(arr);
            
            // Perform many operations
            for (int i = 0; i < 1000; i++) {
                ft.update(i % n, 1);
                ft.query(i % n);
                ft.rangeQuery(0, i % n);
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // Should complete in reasonable time (less than 1 second for this workload)
            assertTrue(duration < 1000, "Operations took too long: " + duration + "ms");
        }
    }
}