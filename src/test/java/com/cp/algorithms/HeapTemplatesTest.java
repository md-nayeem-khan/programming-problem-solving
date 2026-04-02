package com.cp.algorithms;

import com.cp.datastructures.ListNode;
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
 * Comprehensive test suite for HeapTemplates.
 * 
 * Validates all heap-based algorithms with:
 * - Basic functionality tests
 * - Edge cases (empty, single element, duplicates, negatives, boundary conditions)
 * - Stress tests (large inputs, extreme values)
 * - Algorithm correctness validation
 * - Bug detection tests (integer overflow, precision loss, logic errors)
 * 
 * Coverage:
 * 1. MinHeap / MaxHeap implementations
 * 2. Heap Sort
 * 3. Top K Elements (Kth largest/smallest, top K frequent, K closest points)
 * 4. Last Stone Weight
 * 5. MedianFinder (data stream median)
 * 6. Sliding Window Median
 * 7. K-Way Merge (sorted arrays, linked lists, matrix, pairs)
 * 8. Reorganize String
 * 9. Task Scheduler (least interval)
 */
public class HeapTemplatesTest {

    // ========== 1. MIN HEAP TESTS ==========
    
    @Nested
    @DisplayName("MinHeap Tests")
    class MinHeapTests {
        
        @Test
        @DisplayName("Basic insert and extract")
        void testBasicInsertExtract() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            heap.insert(5);
            heap.insert(3);
            heap.insert(7);
            
            assertEquals(3, heap.extractMin());
            assertEquals(5, heap.extractMin());
            assertEquals(7, heap.extractMin());
        }
        
        @Test
        @DisplayName("Peek operation")
        void testPeek() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            heap.insert(10);
            heap.insert(5);
            
            assertEquals(5, heap.peek());
            assertEquals(2, heap.size()); // Peek shouldn't remove
            assertEquals(5, heap.peek()); // Should still be 5
        }
        
        @Test
        @DisplayName("Empty heap throws exception on extract")
        void testEmptyExtract() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            assertThrows(NoSuchElementException.class, heap::extractMin);
        }
        
        @Test
        @DisplayName("Empty heap throws exception on peek")
        void testEmptyPeek() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            assertThrows(NoSuchElementException.class, heap::peek);
        }
        
        @Test
        @DisplayName("Single element operations")
        void testSingleElement() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            heap.insert(42);
            
            assertEquals(42, heap.peek());
            assertEquals(1, heap.size());
            assertFalse(heap.isEmpty());
            assertEquals(42, heap.extractMin());
            assertTrue(heap.isEmpty());
        }
        
        @Test
        @DisplayName("Duplicate values")
        void testDuplicates() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            heap.insert(5);
            heap.insert(5);
            heap.insert(5);
            
            assertEquals(5, heap.extractMin());
            assertEquals(5, heap.extractMin());
            assertEquals(5, heap.extractMin());
        }
        
        @Test
        @DisplayName("Negative numbers")
        void testNegativeNumbers() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            heap.insert(10);
            heap.insert(-5);
            heap.insert(0);
            heap.insert(-10);
            
            assertEquals(-10, heap.extractMin());
            assertEquals(-5, heap.extractMin());
            assertEquals(0, heap.extractMin());
            assertEquals(10, heap.extractMin());
        }
        
        @Test
        @DisplayName("Integer boundary values")
        void testIntegerBoundaries() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            heap.insert(Integer.MAX_VALUE);
            heap.insert(Integer.MIN_VALUE);
            heap.insert(0);
            
            assertEquals(Integer.MIN_VALUE, heap.extractMin());
            assertEquals(0, heap.extractMin());
            assertEquals(Integer.MAX_VALUE, heap.extractMin());
        }
        
        @Test
        @DisplayName("Constructor with array - buildHeap")
        void testBuildHeap() {
            int[] arr = {5, 3, 7, 1, 9, 2};
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap(arr);
            
            assertEquals(1, heap.extractMin());
            assertEquals(2, heap.extractMin());
            assertEquals(3, heap.extractMin());
            assertEquals(5, heap.extractMin());
            assertEquals(7, heap.extractMin());
            assertEquals(9, heap.extractMin());
        }
        
        @Test
        @DisplayName("Descending order inserts (worst case)")
        void testDescendingInserts() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            for (int i = 100; i > 0; i--) {
                heap.insert(i);
            }
            
            for (int i = 1; i <= 100; i++) {
                assertEquals(i, heap.extractMin());
            }
        }
        
        @Test
        @DisplayName("Ascending order inserts (best case)")
        void testAscendingInserts() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            for (int i = 1; i <= 100; i++) {
                heap.insert(i);
            }
            
            for (int i = 1; i <= 100; i++) {
                assertEquals(i, heap.extractMin());
            }
        }
        
        @Test
        @DisplayName("Size tracking")
        void testSizeTracking() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            assertEquals(0, heap.size());
            
            heap.insert(1);
            assertEquals(1, heap.size());
            
            heap.insert(2);
            heap.insert(3);
            assertEquals(3, heap.size());
            
            heap.extractMin();
            assertEquals(2, heap.size());
        }
    }

    // ========== 2. MAX HEAP TESTS ==========
    
    @Nested
    @DisplayName("MaxHeap Tests")
    class MaxHeapTests {
        
        @Test
        @DisplayName("Basic insert and extract")
        void testBasicInsertExtract() {
            HeapTemplates.MaxHeap heap = new HeapTemplates.MaxHeap();
            heap.insert(5);
            heap.insert(3);
            heap.insert(7);
            
            assertEquals(7, heap.extractMax());
            assertEquals(5, heap.extractMax());
            assertEquals(3, heap.extractMax());
        }
        
        @Test
        @DisplayName("Peek operation")
        void testPeek() {
            HeapTemplates.MaxHeap heap = new HeapTemplates.MaxHeap();
            heap.insert(10);
            heap.insert(50);
            
            assertEquals(50, heap.peek());
            assertEquals(2, heap.size());
            assertEquals(50, heap.peek());
        }
        
        @Test
        @DisplayName("Empty heap throws exception")
        void testEmptyOperations() {
            HeapTemplates.MaxHeap heap = new HeapTemplates.MaxHeap();
            assertThrows(NoSuchElementException.class, heap::extractMax);
            assertThrows(NoSuchElementException.class, heap::peek);
        }
        
        @Test
        @DisplayName("Duplicate values")
        void testDuplicates() {
            HeapTemplates.MaxHeap heap = new HeapTemplates.MaxHeap();
            heap.insert(10);
            heap.insert(10);
            heap.insert(10);
            
            assertEquals(10, heap.extractMax());
            assertEquals(10, heap.extractMax());
            assertEquals(10, heap.extractMax());
        }
        
        @Test
        @DisplayName("Negative numbers")
        void testNegativeNumbers() {
            HeapTemplates.MaxHeap heap = new HeapTemplates.MaxHeap();
            heap.insert(5);
            heap.insert(-5);
            heap.insert(0);
            
            assertEquals(5, heap.extractMax());
            assertEquals(0, heap.extractMax());
            assertEquals(-5, heap.extractMax());
        }
        
        @Test
        @DisplayName("Constructor with array - buildHeap")
        void testBuildHeap() {
            int[] arr = {5, 3, 7, 1, 9, 2};
            HeapTemplates.MaxHeap heap = new HeapTemplates.MaxHeap(arr);
            
            assertEquals(9, heap.extractMax());
            assertEquals(7, heap.extractMax());
            assertEquals(5, heap.extractMax());
            assertEquals(3, heap.extractMax());
            assertEquals(2, heap.extractMax());
            assertEquals(1, heap.extractMax());
        }
    }

    // ========== 3. HEAP SORT TESTS ==========
    
    @Nested
    @DisplayName("Heap Sort Tests")
    class HeapSortTests {
        
        @Test
        @DisplayName("Basic heap sort")
        void testBasicSort() {
            int[] arr = {5, 3, 7, 1, 9, 2};
            HeapTemplates.heapSort(arr);
            assertArrayEquals(new int[]{1, 2, 3, 5, 7, 9}, arr);
        }
        
        @Test
        @DisplayName("Empty array")
        void testEmptyArray() {
            int[] arr = {};
            HeapTemplates.heapSort(arr);
            assertArrayEquals(new int[]{}, arr);
        }
        
        @Test
        @DisplayName("Single element")
        void testSingleElement() {
            int[] arr = {42};
            HeapTemplates.heapSort(arr);
            assertArrayEquals(new int[]{42}, arr);
        }
        
        @Test
        @DisplayName("Already sorted")
        void testAlreadySorted() {
            int[] arr = {1, 2, 3, 4, 5};
            HeapTemplates.heapSort(arr);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
        }
        
        @Test
        @DisplayName("Reverse sorted")
        void testReverseSorted() {
            int[] arr = {5, 4, 3, 2, 1};
            HeapTemplates.heapSort(arr);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
        }
        
        @Test
        @DisplayName("Duplicates")
        void testDuplicates() {
            int[] arr = {5, 2, 5, 1, 5, 2};
            HeapTemplates.heapSort(arr);
            assertArrayEquals(new int[]{1, 2, 2, 5, 5, 5}, arr);
        }
        
        @Test
        @DisplayName("Negative numbers")
        void testNegativeNumbers() {
            int[] arr = {-5, 3, -1, 0, 7};
            HeapTemplates.heapSort(arr);
            assertArrayEquals(new int[]{-5, -1, 0, 3, 7}, arr);
        }
        
        @Test
        @DisplayName("Integer boundaries")
        void testIntegerBoundaries() {
            int[] arr = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 100};
            HeapTemplates.heapSort(arr);
            assertArrayEquals(new int[]{Integer.MIN_VALUE, 0, 100, Integer.MAX_VALUE}, arr);
        }
    }

    // ========== 4. TOP K ELEMENTS TESTS ==========
    
    @Nested
    @DisplayName("Top K Elements Tests")
    class TopKElementsTests {
        
        @Test
        @DisplayName("Kth largest - basic")
        void testKthLargestBasic() {
            int[] nums = {3, 2, 1, 5, 6, 4};
            assertEquals(5, HeapTemplates.findKthLargest(nums, 2));
        }
        
        @Test
        @DisplayName("Kth largest - k=1 (max)")
        void testKthLargestMax() {
            int[] nums = {3, 2, 1, 5, 6, 4};
            assertEquals(6, HeapTemplates.findKthLargest(nums, 1));
        }
        
        @Test
        @DisplayName("Kth largest - k=n (min)")
        void testKthLargestMin() {
            int[] nums = {3, 2, 1, 5, 6, 4};
            assertEquals(1, HeapTemplates.findKthLargest(nums, 6));
        }
        
        @Test
        @DisplayName("Kth largest - duplicates")
        void testKthLargestDuplicates() {
            int[] nums = {5, 5, 5, 5};
            assertEquals(5, HeapTemplates.findKthLargest(nums, 2));
        }
        
        @Test
        @DisplayName("Kth smallest - basic")
        void testKthSmallestBasic() {
            int[] nums = {3, 2, 1, 5, 6, 4};
            assertEquals(2, HeapTemplates.findKthSmallest(nums, 2));
        }
        
        @Test
        @DisplayName("Kth smallest - k=1 (min)")
        void testKthSmallestMin() {
            int[] nums = {3, 2, 1, 5, 6, 4};
            assertEquals(1, HeapTemplates.findKthSmallest(nums, 1));
        }
        
        @Test
        @DisplayName("Top K frequent - basic")
        void testTopKFrequentBasic() {
            int[] nums = {1, 1, 1, 2, 2, 3};
            int[] result = HeapTemplates.topKFrequent(nums, 2);
            Arrays.sort(result);
            assertArrayEquals(new int[]{1, 2}, result);
        }
        
        @Test
        @DisplayName("Top K frequent - all same frequency")
        void testTopKFrequentSameFreq() {
            int[] nums = {1, 2, 3};
            int[] result = HeapTemplates.topKFrequent(nums, 2);
            assertEquals(2, result.length);
        }
        
        @Test
        @DisplayName("Top K frequent - all unique")
        void testTopKFrequentAllUnique() {
            int[] nums = {1, 2, 3, 4, 5};
            int[] result = HeapTemplates.topKFrequent(nums, 3);
            assertEquals(3, result.length);
        }
        
        @Test
        @DisplayName("Top K frequent - single element")
        void testTopKFrequentSingleElement() {
            int[] nums = {1};
            int[] result = HeapTemplates.topKFrequent(nums, 1);
            assertArrayEquals(new int[]{1}, result);
        }
        
        @Test
        @DisplayName("K closest points - basic")
        void testKClosestBasic() {
            int[][] points = {{1, 3}, {-2, 2}};
            int[][] result = HeapTemplates.kClosest(points, 1);
            assertEquals(1, result.length);
            assertArrayEquals(new int[]{-2, 2}, result[0]);
        }
        
        @Test
        @DisplayName("K closest points - single point")
        void testKClosestSinglePoint() {
            int[][] points = {{3, 3}};
            int[][] result = HeapTemplates.kClosest(points, 1);
            assertEquals(1, result.length);
            assertArrayEquals(new int[]{3, 3}, result[0]);
        }
        
        @Test
        @DisplayName("K closest points - origin")
        void testKClosestOrigin() {
            int[][] points = {{0, 0}, {1, 1}, {2, 2}};
            int[][] result = HeapTemplates.kClosest(points, 1);
            assertArrayEquals(new int[]{0, 0}, result[0]);
        }
        
        @Test
        @DisplayName("K closest points - negative coordinates")
        void testKClosestNegative() {
            int[][] points = {{-5, -5}, {-1, -1}};
            int[][] result = HeapTemplates.kClosest(points, 1);
            assertArrayEquals(new int[]{-1, -1}, result[0]);
        }
    }

    // ========== 5. LAST STONE WEIGHT TESTS ==========
    
    @Nested
    @DisplayName("Last Stone Weight Tests")
    class LastStoneWeightTests {
        
        @Test
        @DisplayName("Basic scenario")
        void testBasic() {
            int[] stones = {2, 7, 4, 1, 8, 1};
            assertEquals(1, HeapTemplates.lastStoneWeight(stones));
        }
        
        @Test
        @DisplayName("Single stone")
        void testSingleStone() {
            int[] stones = {5};
            assertEquals(5, HeapTemplates.lastStoneWeight(stones));
        }
        
        @Test
        @DisplayName("Two stones - different weights")
        void testTwoStonesDifferent() {
            int[] stones = {2, 7};
            assertEquals(5, HeapTemplates.lastStoneWeight(stones));
        }
        
        @Test
        @DisplayName("Two stones - same weight")
        void testTwoStonesSame() {
            int[] stones = {5, 5};
            assertEquals(0, HeapTemplates.lastStoneWeight(stones));
        }
        
        @Test
        @DisplayName("All equal weights")
        void testAllEqual() {
            int[] stones = {3, 3, 3, 3};
            assertEquals(0, HeapTemplates.lastStoneWeight(stones));
        }
    }

    // ========== 6. MEDIAN FINDER TESTS ==========
    
    @Nested
    @DisplayName("MedianFinder Tests")
    class MedianFinderTests {
        
        @Test
        @DisplayName("Single element")
        void testSingleElement() {
            HeapTemplates.MedianFinder mf = new HeapTemplates.MedianFinder();
            mf.addNum(1);
            assertEquals(1.0, mf.findMedian(), 0.00001);
        }
        
        @Test
        @DisplayName("Two elements - odd median")
        void testTwoElements() {
            HeapTemplates.MedianFinder mf = new HeapTemplates.MedianFinder();
            mf.addNum(1);
            mf.addNum(2);
            assertEquals(1.5, mf.findMedian(), 0.00001);
        }
        
        @Test
        @DisplayName("Three elements - even median")
        void testThreeElements() {
            HeapTemplates.MedianFinder mf = new HeapTemplates.MedianFinder();
            mf.addNum(1);
            mf.addNum(2);
            mf.addNum(3);
            assertEquals(2.0, mf.findMedian(), 0.00001);
        }
        
        @Test
        @DisplayName("Four elements")
        void testFourElements() {
            HeapTemplates.MedianFinder mf = new HeapTemplates.MedianFinder();
            mf.addNum(1);
            mf.addNum(2);
            mf.addNum(3);
            mf.addNum(4);
            assertEquals(2.5, mf.findMedian(), 0.00001);
        }
        
        @Test
        @DisplayName("Duplicate elements")
        void testDuplicates() {
            HeapTemplates.MedianFinder mf = new HeapTemplates.MedianFinder();
            mf.addNum(1);
            mf.addNum(1);
            mf.addNum(1);
            assertEquals(1.0, mf.findMedian(), 0.00001);
        }
        
        @Test
        @DisplayName("Negative numbers")
        void testNegativeNumbers() {
            HeapTemplates.MedianFinder mf = new HeapTemplates.MedianFinder();
            mf.addNum(-1);
            mf.addNum(0);
            mf.addNum(1);
            assertEquals(0.0, mf.findMedian(), 0.00001);
        }
        
        @Test
        @DisplayName("Integer boundaries")
        void testIntegerBoundaries() {
            HeapTemplates.MedianFinder mf = new HeapTemplates.MedianFinder();
            mf.addNum(Integer.MIN_VALUE);
            mf.addNum(Integer.MAX_VALUE);
            // Average of MIN and MAX should be -0.5
            assertEquals(-0.5, mf.findMedian(), 0.00001);
        }
        
        @Test
        @DisplayName("Alternating small and large - precision test")
        void testPrecision() {
            HeapTemplates.MedianFinder mf = new HeapTemplates.MedianFinder();
            mf.addNum(1);
            mf.addNum(1000000000);
            mf.addNum(2);
            mf.addNum(1000000001);
            // Median should be (1000000000 + 2) / 2.0 = 500000001.0
            assertEquals(500000001.0, mf.findMedian(), 0.00001);
        }
        
        @Test
        @DisplayName("Sequential additions - stream test")
        void testStream() {
            HeapTemplates.MedianFinder mf = new HeapTemplates.MedianFinder();
            mf.addNum(5);
            assertEquals(5.0, mf.findMedian(), 0.00001);
            
            mf.addNum(15);
            assertEquals(10.0, mf.findMedian(), 0.00001);
            
            mf.addNum(1);
            assertEquals(5.0, mf.findMedian(), 0.00001);
            
            mf.addNum(3);
            assertEquals(4.0, mf.findMedian(), 0.00001);
        }
    }

    // ========== 7. SLIDING WINDOW MEDIAN TESTS ==========
    
    @Nested
    @DisplayName("Sliding Window Median Tests")
    class SlidingWindowMedianTests {
        
        @Test
        @DisplayName("Basic window k=3")
        void testBasicWindow() {
            int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
            double[] result = HeapTemplates.medianSlidingWindow(nums, 3);
            double[] expected = {1.0, -1.0, -1.0, 3.0, 5.0, 6.0};
            assertArrayEquals(expected, result, 0.00001);
        }
        
        @Test
        @DisplayName("Window size 1")
        void testWindowSizeOne() {
            int[] nums = {1, 3, 2};
            double[] result = HeapTemplates.medianSlidingWindow(nums, 1);
            double[] expected = {1.0, 3.0, 2.0};
            assertArrayEquals(expected, result, 0.00001);
        }
        
        @Test
        @DisplayName("Window equals array length")
        void testWindowEqualsLength() {
            int[] nums = {1, 2, 3, 4};
            double[] result = HeapTemplates.medianSlidingWindow(nums, 4);
            double[] expected = {2.5};
            assertArrayEquals(expected, result, 0.00001);
        }
        
        @Test
        @DisplayName("Duplicates in window")
        void testDuplicates() {
            int[] nums = {1, 1, 1, 1};
            double[] result = HeapTemplates.medianSlidingWindow(nums, 2);
            double[] expected = {1.0, 1.0, 1.0};
            assertArrayEquals(expected, result, 0.00001);
        }
        
        @Test
        @DisplayName("Negative numbers")
        void testNegativeNumbers() {
            int[] nums = {-1, -2, -3, -4};
            double[] result = HeapTemplates.medianSlidingWindow(nums, 2);
            double[] expected = {-1.5, -2.5, -3.5};
            assertArrayEquals(expected, result, 0.00001);
        }
    }

    // ========== 8. K-WAY MERGE TESTS ==========
    
    @Nested
    @DisplayName("Merge K Sorted Arrays Tests")
    class MergeKSortedArraysTests {
        
        @Test
        @DisplayName("Basic merge")
        void testBasicMerge() {
            int[][] arrays = {{1, 3, 5}, {2, 4, 6}};
            List<Integer> result = HeapTemplates.mergeKSortedArrays(arrays);
            assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), result);
        }
        
        @Test
        @DisplayName("Single array")
        void testSingleArray() {
            int[][] arrays = {{1, 2, 3}};
            List<Integer> result = HeapTemplates.mergeKSortedArrays(arrays);
            assertEquals(Arrays.asList(1, 2, 3), result);
        }
        
        @Test
        @DisplayName("Empty arrays mixed")
        void testEmptyArrays() {
            int[][] arrays = {{}, {1, 2}, {}};
            List<Integer> result = HeapTemplates.mergeKSortedArrays(arrays);
            assertEquals(Arrays.asList(1, 2), result);
        }
        
        @Test
        @DisplayName("All arrays empty")
        void testAllEmpty() {
            int[][] arrays = {{}, {}, {}};
            List<Integer> result = HeapTemplates.mergeKSortedArrays(arrays);
            assertEquals(Collections.emptyList(), result);
        }
        
        @Test
        @DisplayName("Duplicates across arrays")
        void testDuplicates() {
            int[][] arrays = {{1, 3, 5}, {1, 3, 5}};
            List<Integer> result = HeapTemplates.mergeKSortedArrays(arrays);
            assertEquals(Arrays.asList(1, 1, 3, 3, 5, 5), result);
        }
        
        @Test
        @DisplayName("Negative numbers")
        void testNegativeNumbers() {
            int[][] arrays = {{-5, -1}, {-4, 0}, {-3, 1}};
            List<Integer> result = HeapTemplates.mergeKSortedArrays(arrays);
            assertEquals(Arrays.asList(-5, -4, -3, -1, 0, 1), result);
        }
    }

    // ========== 9. MERGE K SORTED LISTS TESTS ==========
    
    @Nested
    @DisplayName("Merge K Sorted Lists Tests")
    class MergeKSortedListsTests {
        
        @Test
        @DisplayName("Basic merge")
        void testBasicMerge() {
            ListNode l1 = createList(new int[]{1, 4, 5});
            ListNode l2 = createList(new int[]{1, 3, 4});
            ListNode l3 = createList(new int[]{2, 6});
            
            ListNode[] lists = {l1, l2, l3};
            ListNode result = HeapTemplates.mergeKLists(lists);
            
            assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4, 5, 6}, listToArray(result));
        }
        
        @Test
        @DisplayName("Single list")
        void testSingleList() {
            ListNode l1 = createList(new int[]{1, 2, 3});
            ListNode[] lists = {l1};
            ListNode result = HeapTemplates.mergeKLists(lists);
            
            assertArrayEquals(new int[]{1, 2, 3}, listToArray(result));
        }
        
        @Test
        @DisplayName("Empty lists")
        void testEmptyLists() {
            ListNode[] lists = {};
            ListNode result = HeapTemplates.mergeKLists(lists);
            assertNull(result);
        }
        
        @Test
        @DisplayName("Null lists mixed")
        void testNullLists() {
            ListNode l1 = createList(new int[]{1, 2});
            ListNode[] lists = {l1, null, null};
            ListNode result = HeapTemplates.mergeKLists(lists);
            
            assertArrayEquals(new int[]{1, 2}, listToArray(result));
        }
        
        @Test
        @DisplayName("Single node lists")
        void testSingleNodeLists() {
            ListNode l1 = createList(new int[]{5});
            ListNode l2 = createList(new int[]{3});
            ListNode l3 = createList(new int[]{7});
            
            ListNode[] lists = {l1, l2, l3};
            ListNode result = HeapTemplates.mergeKLists(lists);
            
            assertArrayEquals(new int[]{3, 5, 7}, listToArray(result));
        }
        
        // Helper to create linked list from array
        private ListNode createList(int[] vals) {
            if (vals.length == 0) return null;
            ListNode head = new ListNode(vals[0]);
            ListNode current = head;
            for (int i = 1; i < vals.length; i++) {
                current.next = new ListNode(vals[i]);
                current = current.next;
            }
            return head;
        }
        
        // Helper to convert list to array
        private int[] listToArray(ListNode head) {
            List<Integer> list = new ArrayList<>();
            while (head != null) {
                list.add(head.val);
                head = head.next;
            }
            return list.stream().mapToInt(i -> i).toArray();
        }
    }

    // ========== 10. KTH SMALLEST IN MATRIX TESTS ==========
    
    @Nested
    @DisplayName("Kth Smallest in Matrix Tests")
    class KthSmallestInMatrixTests {
        
        @Test
        @DisplayName("Basic 3x3 matrix")
        void testBasicMatrix() {
            int[][] matrix = {
                {1, 5, 9},
                {10, 11, 13},
                {12, 13, 15}
            };
            assertEquals(13, HeapTemplates.kthSmallestInMatrix(matrix, 8));
        }
        
        @Test
        @DisplayName("k=1 (minimum)")
        void testKthMin() {
            int[][] matrix = {{1, 2}, {3, 4}};
            assertEquals(1, HeapTemplates.kthSmallestInMatrix(matrix, 1));
        }
        
        @Test
        @DisplayName("k=n*n (maximum)")
        void testKthMax() {
            int[][] matrix = {{1, 2}, {3, 4}};
            assertEquals(4, HeapTemplates.kthSmallestInMatrix(matrix, 4));
        }
        
        @Test
        @DisplayName("Single element matrix")
        void testSingleElement() {
            int[][] matrix = {{42}};
            assertEquals(42, HeapTemplates.kthSmallestInMatrix(matrix, 1));
        }
        
        @Test
        @DisplayName("Duplicate values")
        void testDuplicates() {
            int[][] matrix = {{1, 1}, {1, 1}};
            assertEquals(1, HeapTemplates.kthSmallestInMatrix(matrix, 2));
        }
    }

    // ========== 11. K SMALLEST PAIRS TESTS ==========
    
    @Nested
    @DisplayName("K Smallest Pairs Tests")
    class KSmallestPairsTests {
        
        @Test
        @DisplayName("Basic pairs")
        void testBasicPairs() {
            int[] nums1 = {1, 7, 11};
            int[] nums2 = {2, 4, 6};
            List<int[]> result = HeapTemplates.kSmallestPairs(nums1, nums2, 3);
            
            assertEquals(3, result.size());
            assertArrayEquals(new int[]{1, 2}, result.get(0));
            assertArrayEquals(new int[]{1, 4}, result.get(1));
            assertArrayEquals(new int[]{1, 6}, result.get(2));
        }
        
        @Test
        @DisplayName("k=1")
        void testKOne() {
            int[] nums1 = {1, 2};
            int[] nums2 = {3, 4};
            List<int[]> result = HeapTemplates.kSmallestPairs(nums1, nums2, 1);
            
            assertEquals(1, result.size());
            assertArrayEquals(new int[]{1, 3}, result.get(0));
        }
        
        @Test
        @DisplayName("k > n*m")
        void testKLargerThanTotal() {
            int[] nums1 = {1, 2};
            int[] nums2 = {3, 4};
            List<int[]> result = HeapTemplates.kSmallestPairs(nums1, nums2, 10);
            
            assertEquals(4, result.size()); // Should return all 4 pairs
        }
        
        @Test
        @DisplayName("Single element arrays")
        void testSingleElements() {
            int[] nums1 = {5};
            int[] nums2 = {10};
            List<int[]> result = HeapTemplates.kSmallestPairs(nums1, nums2, 1);
            
            assertEquals(1, result.size());
            assertArrayEquals(new int[]{5, 10}, result.get(0));
        }
        
        @Test
        @DisplayName("Negative numbers")
        void testNegativeNumbers() {
            int[] nums1 = {-1, 0, 1};
            int[] nums2 = {-1, 0, 1};
            List<int[]> result = HeapTemplates.kSmallestPairs(nums1, nums2, 3);
            
            assertEquals(3, result.size());
            // First three should be (-1,-1), (-1,0), (0,-1)
            assertArrayEquals(new int[]{-1, -1}, result.get(0));
        }
    }

    // ========== 12. REORGANIZE STRING TESTS ==========
    
    @Nested
    @DisplayName("Reorganize String Tests")
    class ReorganizeStringTests {
        
        @Test
        @DisplayName("Valid reorganization")
        void testValidReorganization() {
            String result = HeapTemplates.reorganizeString("aab");
            assertTrue(result.equals("aba") || result.equals("baa"));
            assertTrue(isValidReorganization(result));
        }
        
        @Test
        @DisplayName("Impossible to reorganize")
        void testImpossible() {
            String result = HeapTemplates.reorganizeString("aaab");
            assertEquals("", result);
        }
        
        @Test
        @DisplayName("Single character")
        void testSingleChar() {
            String result = HeapTemplates.reorganizeString("a");
            assertEquals("a", result);
        }
        
        @Test
        @DisplayName("All unique characters")
        void testAllUnique() {
            String result = HeapTemplates.reorganizeString("abc");
            assertEquals(3, result.length());
            assertTrue(isValidReorganization(result));
        }
        
        @Test
        @DisplayName("Alternating pairs")
        void testAlternatingPairs() {
            String result = HeapTemplates.reorganizeString("aabbcc");
            assertEquals(6, result.length());
            assertTrue(isValidReorganization(result));
        }
        
        @Test
        @DisplayName("Long valid string")
        void testLongString() {
            String result = HeapTemplates.reorganizeString("aaabbbccc");
            assertEquals(9, result.length());
            assertTrue(isValidReorganization(result));
        }
        
        // Helper to verify no two adjacent characters are the same
        private boolean isValidReorganization(String s) {
            if (s.isEmpty()) return true;
            for (int i = 1; i < s.length(); i++) {
                if (s.charAt(i) == s.charAt(i - 1)) return false;
            }
            return true;
        }
    }

    // ========== 13. LEAST INTERVAL (TASK SCHEDULER) TESTS ==========
    
    @Nested
    @DisplayName("Least Interval (Task Scheduler) Tests")
    class LeastIntervalTests {
        
        @Test
        @DisplayName("Basic task scheduling")
        void testBasicScheduling() {
            char[] tasks = {'A', 'A', 'A', 'B', 'B', 'B'};
            assertEquals(8, HeapTemplates.leastInterval(tasks, 2));
        }
        
        @Test
        @DisplayName("No cooldown (n=0)")
        void testNoCooldown() {
            char[] tasks = {'A', 'A', 'A', 'B', 'B', 'B'};
            assertEquals(6, HeapTemplates.leastInterval(tasks, 0));
        }
        
        @Test
        @DisplayName("Single task")
        void testSingleTask() {
            char[] tasks = {'A'};
            assertEquals(1, HeapTemplates.leastInterval(tasks, 2));
        }
        
        @Test
        @DisplayName("All same task")
        void testAllSameTask() {
            char[] tasks = {'A', 'A', 'A'};
            assertEquals(7, HeapTemplates.leastInterval(tasks, 2));
        }
        
        @Test
        @DisplayName("Many different tasks")
        void testManyDifferentTasks() {
            char[] tasks = {'A', 'B', 'C', 'D', 'E', 'F'};
            assertEquals(6, HeapTemplates.leastInterval(tasks, 2));
        }
        
        @Test
        @DisplayName("High cooldown")
        void testHighCooldown() {
            char[] tasks = {'A', 'A', 'A'};
            assertEquals(11, HeapTemplates.leastInterval(tasks, 4));
        }
    }

    // ========== 14. STRESS AND PERFORMANCE TESTS ==========
    
    @Nested
    @DisplayName("Stress and Performance Tests")
    class StressTests {
        
        @Test
        @DisplayName("Large heap operations (10000 elements)")
        void testLargeHeap() {
            HeapTemplates.MinHeap heap = new HeapTemplates.MinHeap();
            
            // Insert 10000 random elements
            Random rand = new Random(42);
            for (int i = 0; i < 10000; i++) {
                heap.insert(rand.nextInt(100000));
            }
            
            // Extract all and verify sorted order
            int prev = heap.extractMin();
            while (!heap.isEmpty()) {
                int curr = heap.extractMin();
                assertTrue(prev <= curr);
                prev = curr;
            }
        }
        
        @Test
        @DisplayName("Heap sort large array (5000 elements)")
        void testHeapSortLarge() {
            Random rand = new Random(42);
            int[] arr = new int[5000];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = rand.nextInt(100000);
            }
            
            HeapTemplates.heapSort(arr);
            
            // Verify sorted
            for (int i = 1; i < arr.length; i++) {
                assertTrue(arr[i - 1] <= arr[i]);
            }
        }
        
        @Test
        @DisplayName("MedianFinder with 1000 additions")
        void testMedianFinderLarge() {
            HeapTemplates.MedianFinder mf = new HeapTemplates.MedianFinder();
            Random rand = new Random(42);
            
            for (int i = 0; i < 1000; i++) {
                mf.addNum(rand.nextInt(10000));
            }
            
            double median = mf.findMedian();
            assertTrue(median >= 0 && median <= 10000);
        }
    }
}
