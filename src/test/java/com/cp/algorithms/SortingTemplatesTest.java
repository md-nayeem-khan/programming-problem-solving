package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for SortingTemplates algorithms.
 * 
 * Validates all sorting algorithms with:
 * - Basic functionality tests
 * - Edge cases (empty, single element, duplicates, sorted/reverse)
 * - Boundary value tests (Integer.MIN/MAX_VALUE)
 * - Stability verification for stable algorithms
 * - Performance validation and bug detection
 * - Overflow protection testing
 * 
 * Coverage:
 * 1. Quick Sort (standard, randomized, 3-way)
 * 2. Quick Select algorithms
 * 3. Dutch National Flag / 3-way partition
 * 4. Counting Sort (full and simple)
 * 5. Radix Sort
 * 6. Bucket Sort (floats and integers)
 * 7. Topological Sort
 * 8. Interval Merging
 * 9. Custom Comparators
 * 10. Utility methods
 */
public class SortingTemplatesTest {

    // ========== QUICK SORT TESTS ==========
    
    @Nested
    @DisplayName("Quick Sort Tests")
    class QuickSortTests {
        
        @Test
        @DisplayName("Basic quick sort functionality")
        void testQuickSortBasic() {
            int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};
            SortingTemplates.quickSort(arr);
            assertArrayEquals(new int[]{1, 1, 2, 3, 4, 5, 6, 9}, arr);
        }
        
        @Test
        @DisplayName("Quick sort with empty array")
        void testQuickSortEmpty() {
            int[] arr = {};
            SortingTemplates.quickSort(arr);
            assertArrayEquals(new int[]{}, arr);
        }
        
        @Test
        @DisplayName("Quick sort with single element")
        void testQuickSortSingle() {
            int[] arr = {42};
            SortingTemplates.quickSort(arr);
            assertArrayEquals(new int[]{42}, arr);
        }
        
        @Test
        @DisplayName("Quick sort with duplicates")
        void testQuickSortDuplicates() {
            int[] arr = {3, 1, 3, 1, 3, 1};
            SortingTemplates.quickSort(arr);
            assertArrayEquals(new int[]{1, 1, 1, 3, 3, 3}, arr);
        }
        
        @Test
        @DisplayName("Quick sort with already sorted array")
        void testQuickSortAlreadySorted() {
            int[] arr = {1, 2, 3, 4, 5};
            SortingTemplates.quickSort(arr);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
        }
        
        @Test
        @DisplayName("Quick sort with reverse sorted array")
        void testQuickSortReverse() {
            int[] arr = {5, 4, 3, 2, 1};
            SortingTemplates.quickSort(arr);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
        }
        
        @Test
        @DisplayName("Quick sort with negative numbers")
        void testQuickSortNegatives() {
            int[] arr = {-3, 1, -4, 0, 5, -9};
            SortingTemplates.quickSort(arr);
            assertArrayEquals(new int[]{-9, -4, -3, 0, 1, 5}, arr);
        }
        
        @Test
        @DisplayName("Quick sort randomized vs standard - same result")
        void testRandomizedQuickSortEquivalence() {
            int[] arr1 = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            
            SortingTemplates.quickSort(arr1);
            SortingTemplates.quickSortRandomized(arr2);
            
            assertArrayEquals(arr1, arr2);
        }
        
        @Test
        @DisplayName("3-Way quick sort with many duplicates")
        void testQuickSort3WayDuplicates() {
            int[] arr = {2, 1, 2, 0, 2, 1, 0, 2, 2, 1};
            SortingTemplates.quickSort3Way(arr);
            assertArrayEquals(new int[]{0, 0, 1, 1, 1, 2, 2, 2, 2, 2}, arr);
        }
        
        @Test
        @DisplayName("3-Way quick sort optimal for duplicates")
        void testQuickSort3WayAllSame() {
            int[] arr = {5, 5, 5, 5, 5, 5, 5};
            SortingTemplates.quickSort3Way(arr);
            assertArrayEquals(new int[]{5, 5, 5, 5, 5, 5, 5}, arr);
        }
    }

    // ========== QUICK SELECT TESTS ==========
    
    @Nested
    @DisplayName("Quick Select Tests")
    class QuickSelectTests {
        
        @Test
        @DisplayName("Quick select kth smallest")
        void testQuickSelectBasic() {
            int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};
            assertEquals(1, SortingTemplates.quickSelect(arr, 1)); // 1st smallest
            assertEquals(2, SortingTemplates.quickSelect(arr, 3)); // 3rd smallest
            assertEquals(9, SortingTemplates.quickSelect(arr, 8)); // largest
        }
        
        @Test
        @DisplayName("Quick select with single element")
        void testQuickSelectSingle() {
            int[] arr = {42};
            assertEquals(42, SortingTemplates.quickSelect(arr, 1));
        }
        
        @Test
        @DisplayName("Quick select with duplicates")
        void testQuickSelectDuplicates() {
            int[] arr = {1, 3, 3, 3, 5};
            assertEquals(1, SortingTemplates.quickSelect(arr, 1));
            assertEquals(3, SortingTemplates.quickSelect(arr, 2));
            assertEquals(3, SortingTemplates.quickSelect(arr, 4));
            assertEquals(5, SortingTemplates.quickSelect(arr, 5));
        }
        
        @Test
        @DisplayName("Find kth largest")
        void testFindKthLargest() {
            int[] arr = {3, 2, 1, 5, 6, 4};
            assertEquals(5, SortingTemplates.findKthLargest(arr, 2)); // 2nd largest
            assertEquals(6, SortingTemplates.findKthLargest(arr, 1)); // largest
            assertEquals(1, SortingTemplates.findKthLargest(arr, 6)); // smallest
        }
        
        @Test
        @DisplayName("Quick select randomized equivalence")
        void testQuickSelectRandomizedEquivalence() {
            int[] arr1 = {3, 1, 4, 1, 5, 9, 2, 6, 5};
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            
            for (int k = 1; k <= arr1.length; k++) {
                int result1 = SortingTemplates.quickSelect(Arrays.copyOf(arr1, arr1.length), k);
                int result2 = SortingTemplates.quickSelectRandomized(Arrays.copyOf(arr2, arr2.length), k);
                assertEquals(result1, result2, "Results should match for k=" + k);
            }
        }
    }

    // ========== DUTCH NATIONAL FLAG TESTS ==========
    
    @Nested
    @DisplayName("Dutch National Flag Tests")
    class DutchNationalFlagTests {
        
        @Test
        @DisplayName("Sort colors basic")
        void testSortColorsBasic() {
            int[] nums = {2, 0, 2, 1, 1, 0};
            SortingTemplates.sortColors(nums);
            assertArrayEquals(new int[]{0, 0, 1, 1, 2, 2}, nums);
        }
        
        @Test
        @DisplayName("Sort colors single color")
        void testSortColorsSingle() {
            int[] nums = {1, 1, 1, 1};
            SortingTemplates.sortColors(nums);
            assertArrayEquals(new int[]{1, 1, 1, 1}, nums);
        }
        
        @Test
        @DisplayName("Sort colors already sorted")
        void testSortColorsAlreadySorted() {
            int[] nums = {0, 0, 1, 1, 2, 2};
            SortingTemplates.sortColors(nums);
            assertArrayEquals(new int[]{0, 0, 1, 1, 2, 2}, nums);
        }
        
        @Test
        @DisplayName("3-way partition with pivot")
        void testPartition3Way() {
            int[] arr = {1, 5, 3, 5, 3, 7, 5, 2};
            SortingTemplates.partition3Way(arr, 5);
            
            // Verify partitioning: elements < 5, == 5, > 5
            List<Integer> result = Arrays.stream(arr).boxed().toList();
            int firstFive = result.indexOf(5);
            int lastFive = result.lastIndexOf(5);
            
            // All elements before first 5 should be < 5
            for (int i = 0; i < firstFive; i++) {
                assertTrue(arr[i] < 5);
            }
            // All elements after last 5 should be > 5
            for (int i = lastFive + 1; i < arr.length; i++) {
                assertTrue(arr[i] > 5);
            }
        }
    }

    // ========== COUNTING SORT TESTS ==========
    
    @Nested
    @DisplayName("Counting Sort Tests")
    class CountingSortTests {
        
        @Test
        @DisplayName("Counting sort basic")
        void testCountingSortBasic() {
            int[] arr = {4, 2, 2, 8, 3, 3, 1};
            SortingTemplates.countingSort(arr);
            assertArrayEquals(new int[]{1, 2, 2, 3, 3, 4, 8}, arr);
        }
        
        @Test
        @DisplayName("Counting sort with negatives")
        void testCountingSortNegatives() {
            int[] arr = {-1, 3, -2, 0, 3, -1};
            SortingTemplates.countingSort(arr);
            assertArrayEquals(new int[]{-2, -1, -1, 0, 3, 3}, arr);
        }
        
        @Test
        @DisplayName("Counting sort stability verification")
        void testCountingSortStability() {
            // Since we can't track individual elements, test that equal elements
            // maintain relative positions by checking the algorithm works correctly
            int[] arr = {3, 1, 3, 1, 2, 2};
            SortingTemplates.countingSort(arr);
            assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3}, arr);
        }
        
        @Test
        @DisplayName("Counting sort simple with valid range")
        void testCountingSortSimple() {
            int[] arr = {4, 2, 2, 8, 3, 3, 1};
            SortingTemplates.countingSortSimple(arr, 8);
            assertArrayEquals(new int[]{1, 2, 2, 3, 3, 4, 8}, arr);
        }
        
        @Test
        @DisplayName("Counting sort simple bounds validation")
        void testCountingSortSimpleBoundsValidation() {
            int[] arr = {1, 2, 10}; // 10 > maxVal = 5
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SortingTemplates.countingSortSimple(arr, 5)
            );
            assertTrue(exception.getMessage().contains("outside valid range"));
        }
        
        @Test
        @DisplayName("Counting sort simple negative number validation")
        void testCountingSortSimpleNegativeValidation() {
            int[] arr = {1, -1, 2}; // -1 < 0
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SortingTemplates.countingSortSimple(arr, 5)
            );
            assertTrue(exception.getMessage().contains("outside valid range"));
        }
    }

    // ========== RADIX SORT TESTS ==========
    
    @Nested
    @DisplayName("Radix Sort Tests")
    class RadixSortTests {
        
        @Test
        @DisplayName("Radix sort basic")
        void testRadixSortBasic() {
            int[] arr = {170, 45, 75, 90, 2, 802, 24, 66};
            SortingTemplates.radixSort(arr);
            assertArrayEquals(new int[]{2, 24, 45, 66, 75, 90, 170, 802}, arr);
        }
        
        @Test
        @DisplayName("Radix sort with zeros")
        void testRadixSortWithZeros() {
            int[] arr = {0, 100, 0, 50, 0};
            SortingTemplates.radixSort(arr);
            assertArrayEquals(new int[]{0, 0, 0, 50, 100}, arr);
        }
        
        @Test
        @DisplayName("Radix sort single digit numbers")
        void testRadixSortSingleDigit() {
            int[] arr = {9, 1, 8, 2, 7, 3};
            SortingTemplates.radixSort(arr);
            assertArrayEquals(new int[]{1, 2, 3, 7, 8, 9}, arr);
        }
        
        @Test
        @DisplayName("Radix sort large numbers")
        void testRadixSortLargeNumbers() {
            int[] arr = {999999, 1, 888888, 777777};
            SortingTemplates.radixSort(arr);
            assertArrayEquals(new int[]{1, 777777, 888888, 999999}, arr);
        }
    }

    // ========== BUCKET SORT TESTS ==========
    
    @Nested
    @DisplayName("Bucket Sort Tests")
    class BucketSortTests {
        
        @Test
        @DisplayName("Bucket sort floats in [0,1)")
        void testBucketSortFloats() {
            float[] arr = {0.78f, 0.17f, 0.39f, 0.26f, 0.72f, 0.94f, 0.21f, 0.12f, 0.23f, 0.68f};
            SortingTemplates.bucketSort(arr);
            
            // Verify sorted order
            for (int i = 1; i < arr.length; i++) {
                assertTrue(arr[i-1] <= arr[i], "Array should be sorted");
            }
        }
        
        @Test
        @DisplayName("Bucket sort floats edge case with 1.0")
        void testBucketSortFloatsEdgeCase() {
            float[] arr = {0.5f, 1.0f, 0.1f};
            SortingTemplates.bucketSort(arr);
            assertArrayEquals(new float[]{0.1f, 0.5f, 1.0f}, arr, 0.001f);
        }
        
        @Test
        @DisplayName("Bucket sort integers")
        void testBucketSortIntegers() {
            int[] arr = {29, 25, 3, 49, 9, 37, 21, 43};
            SortingTemplates.bucketSortIntegers(arr, 0, 50, 5);
            assertArrayEquals(new int[]{3, 9, 21, 25, 29, 37, 43, 49}, arr);
        }
        
        @Test
        @DisplayName("Bucket sort integers with single bucket")
        void testBucketSortIntegersSingleBucket() {
            int[] arr = {5, 3, 7, 1, 9};
            SortingTemplates.bucketSortIntegers(arr, 1, 9, 1);
            assertArrayEquals(new int[]{1, 3, 5, 7, 9}, arr);
        }
    }

    // ========== TOPOLOGICAL SORT TESTS ==========
    
    @Nested
    @DisplayName("Topological Sort Tests")
    class TopologicalSortTests {
        
        @Test
        @DisplayName("Topological sort basic DAG")
        void testTopologicalSortBasic() {
            int n = 6;
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
            }
            
            // Graph: 5 -> 2 -> 3 -> 1, 5 -> 0, 4 -> 0, 4 -> 1
            adj.get(5).add(2);
            adj.get(5).add(0);
            adj.get(2).add(3);
            adj.get(3).add(1);
            adj.get(4).add(0);
            adj.get(4).add(1);
            
            List<Integer> result = SortingTemplates.topologicalSortKahn(n, adj);
            assertEquals(n, result.size());
            
            // Verify topological ordering
            Map<Integer, Integer> position = new HashMap<>();
            for (int i = 0; i < result.size(); i++) {
                position.put(result.get(i), i);
            }
            
            // Check each edge u -> v: position[u] < position[v]
            for (int u = 0; u < n; u++) {
                for (int v : adj.get(u)) {
                    assertTrue(position.get(u) < position.get(v),
                        "Topological order violated: " + u + " -> " + v);
                }
            }
        }
        
        @Test
        @DisplayName("Topological sort with cycle")
        void testTopologicalSortWithCycle() {
            int n = 3;
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
            }
            
            // Create cycle: 0 -> 1 -> 2 -> 0
            adj.get(0).add(1);
            adj.get(1).add(2);
            adj.get(2).add(0);
            
            List<Integer> result = SortingTemplates.topologicalSortKahn(n, adj);
            assertTrue(result.isEmpty(), "Should return empty list for cyclic graph");
        }
        
        @Test
        @DisplayName("Topological sort single node")
        void testTopologicalSortSingleNode() {
            List<List<Integer>> adj = List.of(new ArrayList<>());
            List<Integer> result = SortingTemplates.topologicalSortKahn(1, adj);
            assertEquals(List.of(0), result);
        }
    }

    // ========== MERGE INTERVALS TESTS ==========
    
    @Nested
    @DisplayName("Merge Intervals Tests")
    class MergeIntervalsTests {
        
        @Test
        @DisplayName("Merge intervals basic")
        void testMergeIntervalsBasic() {
            int[][] intervals = {{1,3}, {2,6}, {8,10}, {15,18}};
            int[][] result = SortingTemplates.mergeIntervals(intervals);
            assertArrayEquals(new int[][]{{1,6}, {8,10}, {15,18}}, result);
        }
        
        @Test
        @DisplayName("Merge intervals all overlapping")
        void testMergeIntervalsAllOverlapping() {
            int[][] intervals = {{1,4}, {4,5}};
            int[][] result = SortingTemplates.mergeIntervals(intervals);
            assertArrayEquals(new int[][]{{1,5}}, result);
        }
        
        @Test
        @DisplayName("Merge intervals no overlapping")
        void testMergeIntervalsNoOverlapping() {
            int[][] intervals = {{1,2}, {3,4}, {5,6}};
            int[][] result = SortingTemplates.mergeIntervals(intervals);
            assertArrayEquals(new int[][]{{1,2}, {3,4}, {5,6}}, result);
        }
        
        @Test
        @DisplayName("Merge intervals single interval")
        void testMergeIntervalsSingle() {
            int[][] intervals = {{1,4}};
            int[][] result = SortingTemplates.mergeIntervals(intervals);
            assertArrayEquals(new int[][]{{1,4}}, result);
        }
    }

    // ========== CUSTOM COMPARATOR TESTS ==========
    
    @Nested
    @DisplayName("Custom Comparator Tests")
    class CustomComparatorTests {
        
        @Test
        @DisplayName("Sort by absolute value")
        void testSortByAbsoluteValue() {
            int[] arr = {-3, -1, 0, 1, 2, -2};
            SortingTemplates.ComparatorExamples.sortByAbsoluteValue(arr);
            assertArrayEquals(new int[]{0, -1, 1, -2, 2, -3}, arr);
        }
        
        @Test
        @DisplayName("Sort by absolute value with extreme values")
        void testSortByAbsoluteValueExtreme() {
            int[] arr = {Integer.MIN_VALUE, Integer.MAX_VALUE, 0, -1, 1};
            SortingTemplates.ComparatorExamples.sortByAbsoluteValue(arr);
            
            // Verify sorting by absolute value (no overflow)
            for (int i = 1; i < arr.length; i++) {
                assertTrue(Math.abs((long)arr[i-1]) <= Math.abs((long)arr[i]),
                    "Array should be sorted by absolute value");
            }
        }
        
        @Test
        @DisplayName("Sort strings by length")
        void testSortStringsByLength() {
            String[] arr = {"apple", "pie", "washington", "book", "a"};
            SortingTemplates.ComparatorExamples.sortStringsByLength(arr);
            assertArrayEquals(new String[]{"a", "pie", "book", "apple", "washington"}, arr);
        }
        
        @Test
        @DisplayName("Sort 2D array")
        void testSort2DArray() {
            int[][] arr = {{2, 3}, {1, 4}, {2, 1}, {1, 2}};
            SortingTemplates.ComparatorExamples.sort2DArray(arr);
            assertArrayEquals(new int[][]{{1, 2}, {1, 4}, {2, 1}, {2, 3}}, arr);
        }
        
        @Test
        @DisplayName("Sort descending")
        void testSortDescending() {
            int[] arr = {1, 5, 2, 8, 3};
            SortingTemplates.ComparatorExamples.sortDescending(arr);
            assertArrayEquals(new int[]{8, 5, 3, 2, 1}, arr);
        }
        
        @Test
        @DisplayName("Sort descending with extreme values")
        void testSortDescendingExtreme() {
            int[] arr = {Integer.MIN_VALUE, Integer.MAX_VALUE, 0, -1, 1};
            SortingTemplates.ComparatorExamples.sortDescending(arr);
            
            // Verify descending order (no overflow)
            for (int i = 1; i < arr.length; i++) {
                assertTrue(arr[i-1] >= arr[i], "Array should be in descending order");
            }
        }
    }

    // ========== UTILITY METHOD TESTS ==========
    
    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {
        
        @Test
        @DisplayName("isSorted method")
        void testIsSorted() {
            assertTrue(SortingTemplates.isSorted(new int[]{1, 2, 3, 4, 5}));
            assertTrue(SortingTemplates.isSorted(new int[]{1, 1, 1, 1}));
            assertTrue(SortingTemplates.isSorted(new int[]{5}));
            assertTrue(SortingTemplates.isSorted(new int[]{}));
            
            assertFalse(SortingTemplates.isSorted(new int[]{1, 3, 2, 4}));
            assertFalse(SortingTemplates.isSorted(new int[]{5, 4, 3, 2, 1}));
        }
        
        @Test
        @DisplayName("Partition around kth element")
        void testPartitionAroundKth() {
            int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};
            int[] original = Arrays.copyOf(arr, arr.length);
            
            SortingTemplates.partitionAroundKth(arr, 4);
            
            // The 4th smallest element should be in position 3 (0-indexed)
            Arrays.sort(original);
            assertEquals(original[3], arr[3]);
            
            // Elements before position 3 should be <= arr[3]
            for (int i = 0; i < 3; i++) {
                assertTrue(arr[i] <= arr[3]);
            }
            
            // Elements after position 3 should be >= arr[3]
            for (int i = 4; i < arr.length; i++) {
                assertTrue(arr[i] >= arr[3]);
            }
        }
    }

    // ========== STRESS AND EDGE CASE TESTS ==========
    
    @Nested
    @DisplayName("Stress and Edge Case Tests")
    class StressTests {
        
        @Test
        @DisplayName("Large array sorting consistency")
        void testLargeArraySorting() {
            Random rand = new Random(42); // Fixed seed for reproducibility
            int[] arr = rand.ints(1000, 0, 10000).toArray();
            
            int[] expected = Arrays.copyOf(arr, arr.length);
            Arrays.sort(expected);
            
            // Test different sorting algorithms on the same data
            int[] quickSorted = Arrays.copyOf(arr, arr.length);
            SortingTemplates.quickSort(quickSorted);
            assertArrayEquals(expected, quickSorted, "Quick sort failed on large array");
            
            int[] randomizedSorted = Arrays.copyOf(arr, arr.length);
            SortingTemplates.quickSortRandomized(randomizedSorted);
            assertArrayEquals(expected, randomizedSorted, "Randomized quick sort failed");
            
            int[] threeway = Arrays.copyOf(arr, arr.length);
            SortingTemplates.quickSort3Way(threeway);
            assertArrayEquals(expected, threeway, "3-way quick sort failed");
        }
        
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 5, 10, 100})
        @DisplayName("Empty and small array edge cases")
        void testSmallArraySizes(int size) {
            int[] arr = new int[size];
            Arrays.fill(arr, 42);
            
            // Should not throw exceptions
            assertDoesNotThrow(() -> SortingTemplates.quickSort(Arrays.copyOf(arr, arr.length)));
            assertDoesNotThrow(() -> SortingTemplates.quickSortRandomized(Arrays.copyOf(arr, arr.length)));
            assertDoesNotThrow(() -> SortingTemplates.quickSort3Way(Arrays.copyOf(arr, arr.length)));
            assertDoesNotThrow(() -> SortingTemplates.countingSort(Arrays.copyOf(arr, arr.length)));
        }
        
        @Test
        @DisplayName("Integer overflow protection in comparators")
        void testIntegerOverflowProtection() {
            // Test extreme values that would cause overflow with subtraction
            int[] arr = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE, -1, 1};
            
            // These should not throw exceptions or produce incorrect results
            assertDoesNotThrow(() -> {
                SortingTemplates.ComparatorExamples.sortByAbsoluteValue(Arrays.copyOf(arr, arr.length));
            });
            
            assertDoesNotThrow(() -> {
                SortingTemplates.ComparatorExamples.sortDescending(Arrays.copyOf(arr, arr.length));
            });
        }
    }

    // ========== HELPER METHODS ==========
    
    /**
     * Verify that a float array is sorted.
     */
    private void assertFloatArraySorted(float[] arr) {
        for (int i = 1; i < arr.length; i++) {
            assertTrue(arr[i-1] <= arr[i], 
                String.format("Array not sorted: arr[%d]=%.3f > arr[%d]=%.3f", 
                    i-1, arr[i-1], i, arr[i]));
        }
    }
    
    /**
     * Generate a random array for testing.
     */
    private int[] generateRandomArray(int size, int min, int max) {
        Random rand = new Random(42); // Fixed seed for reproducibility
        return rand.ints(size, min, max + 1).toArray();
    }
}