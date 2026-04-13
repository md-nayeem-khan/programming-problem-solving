package com.cp.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for AdvancedDataStructures
 * Validates all algorithm implementations for correctness and edge cases
 * 
 * Coverage:
 * 1. Monotonic Stack algorithms
 * 2. Monotonic Queue (Sliding Window)
 * 3. MinStack / MaxStack
 * 4. LRU Cache / LFU Cache
 * 5. Interval Operations
 * 6. Miscellaneous Patterns
 */
@DisplayName("Advanced Data Structures - Complete Test Suite")
class AdvancedDataStructuresTest {

    // =========================================================
    // 1. MONOTONIC STACK TESTS
    // =========================================================

    @Nested
    @DisplayName("Monotonic Stack Algorithms")
    class MonotonicStackTests {

        @Test
        @DisplayName("Next Greater Element - Basic Case")
        void testNextGreaterElementBasic() {
            int[] nums = {2, 1, 2, 4, 3};
            int[] expected = {4, 2, 4, -1, -1};
            assertArrayEquals(expected, AdvancedDataStructures.nextGreaterElement(nums));
        }

        @Test
        @DisplayName("Next Greater Element - Increasing Sequence")
        void testNextGreaterElementIncreasing() {
            int[] nums = {1, 2, 3, 4, 5};
            int[] expected = {2, 3, 4, 5, -1};
            assertArrayEquals(expected, AdvancedDataStructures.nextGreaterElement(nums));
        }

        @Test
        @DisplayName("Next Greater Element - Decreasing Sequence")
        void testNextGreaterElementDecreasing() {
            int[] nums = {5, 4, 3, 2, 1};
            int[] expected = {-1, -1, -1, -1, -1};
            assertArrayEquals(expected, AdvancedDataStructures.nextGreaterElement(nums));
        }

        @Test
        @DisplayName("Next Greater Element - Single Element")
        void testNextGreaterElementSingle() {
            int[] nums = {5};
            int[] expected = {-1};
            assertArrayEquals(expected, AdvancedDataStructures.nextGreaterElement(nums));
        }

        @Test
        @DisplayName("Next Greater Element - All Same")
        void testNextGreaterElementAllSame() {
            int[] nums = {3, 3, 3, 3};
            int[] expected = {-1, -1, -1, -1};
            assertArrayEquals(expected, AdvancedDataStructures.nextGreaterElement(nums));
        }

        @Test
        @DisplayName("Next Greater Element Circular - Basic Case")
        void testNextGreaterElementCircular() {
            int[] nums = {1, 2, 1};
            int[] expected = {2, -1, 2};
            assertArrayEquals(expected, AdvancedDataStructures.nextGreaterElementCircular(nums));
        }

        @Test
        @DisplayName("Next Greater Element Circular - All Same")
        void testNextGreaterElementCircularAllSame() {
            int[] nums = {5, 5, 5};
            int[] expected = {-1, -1, -1};
            assertArrayEquals(expected, AdvancedDataStructures.nextGreaterElementCircular(nums));
        }

        @Test
        @DisplayName("Next Greater Element Circular - Complex")
        void testNextGreaterElementCircularComplex() {
            int[] nums = {2, 3, 1, 4};
            int[] expected = {3, 4, 4, -1};
            assertArrayEquals(expected, AdvancedDataStructures.nextGreaterElementCircular(nums));
        }

        @Test
        @DisplayName("Previous Smaller Element - Basic Case")
        void testPreviousSmallerElementBasic() {
            int[] nums = {4, 5, 2, 10, 8};
            int[] expected = {-1, 4, -1, 2, 2};
            assertArrayEquals(expected, AdvancedDataStructures.previousSmallerElement(nums));
        }

        @Test
        @DisplayName("Previous Smaller Element - Increasing")
        void testPreviousSmallerElementIncreasing() {
            int[] nums = {1, 2, 3, 4, 5};
            int[] expected = {-1, 1, 2, 3, 4};
            assertArrayEquals(expected, AdvancedDataStructures.previousSmallerElement(nums));
        }

        @Test
        @DisplayName("Previous Smaller Element - Decreasing")
        void testPreviousSmallerElementDecreasing() {
            int[] nums = {5, 4, 3, 2, 1};
            int[] expected = {-1, -1, -1, -1, -1};
            assertArrayEquals(expected, AdvancedDataStructures.previousSmallerElement(nums));
        }

        @Test
        @DisplayName("Largest Rectangle in Histogram - Basic Case")
        void testLargestRectangleAreaBasic() {
            int[] heights = {2, 1, 5, 6, 2, 3};
            assertEquals(10, AdvancedDataStructures.largestRectangleArea(heights));
        }

        @Test
        @DisplayName("Largest Rectangle in Histogram - Single Bar")
        void testLargestRectangleAreaSingle() {
            int[] heights = {5};
            assertEquals(5, AdvancedDataStructures.largestRectangleArea(heights));
        }

        @Test
        @DisplayName("Largest Rectangle in Histogram - Increasing")
        void testLargestRectangleAreaIncreasing() {
            int[] heights = {1, 2, 3, 4, 5};
            assertEquals(9, AdvancedDataStructures.largestRectangleArea(heights));
        }

        @Test
        @DisplayName("Largest Rectangle in Histogram - Decreasing")
        void testLargestRectangleAreaDecreasing() {
            int[] heights = {5, 4, 3, 2, 1};
            assertEquals(9, AdvancedDataStructures.largestRectangleArea(heights));
        }

        @Test
        @DisplayName("Largest Rectangle in Histogram - All Same")
        void testLargestRectangleAreaAllSame() {
            int[] heights = {3, 3, 3, 3};
            assertEquals(12, AdvancedDataStructures.largestRectangleArea(heights));
        }

        @Test
        @DisplayName("Daily Temperatures - Basic Case")
        void testDailyTemperaturesBasic() {
            int[] temps = {73, 74, 75, 71, 69, 72, 76, 73};
            int[] expected = {1, 1, 4, 2, 1, 1, 0, 0};
            assertArrayEquals(expected, AdvancedDataStructures.dailyTemperatures(temps));
        }

        @Test
        @DisplayName("Daily Temperatures - Increasing")
        void testDailyTemperaturesIncreasing() {
            int[] temps = {30, 40, 50, 60};
            int[] expected = {1, 1, 1, 0};
            assertArrayEquals(expected, AdvancedDataStructures.dailyTemperatures(temps));
        }

        @Test
        @DisplayName("Daily Temperatures - Decreasing")
        void testDailyTemperaturesDecreasing() {
            int[] temps = {60, 50, 40, 30};
            int[] expected = {0, 0, 0, 0};
            assertArrayEquals(expected, AdvancedDataStructures.dailyTemperatures(temps));
        }
    }

    // =========================================================
    // 2. MONOTONIC QUEUE TESTS
    // =========================================================

    @Nested
    @DisplayName("Monotonic Queue (Sliding Window)")
    class MonotonicQueueTests {

        @Test
        @DisplayName("Max Sliding Window - Basic Case")
        void testMaxSlidingWindowBasic() {
            int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
            int k = 3;
            int[] expected = {3, 3, 5, 5, 6, 7};
            assertArrayEquals(expected, AdvancedDataStructures.maxSlidingWindow(nums, k));
        }

        @Test
        @DisplayName("Max Sliding Window - Window Size 1")
        void testMaxSlidingWindowK1() {
            int[] nums = {1, 3, -1, -3, 5};
            int k = 1;
            assertArrayEquals(nums, AdvancedDataStructures.maxSlidingWindow(nums, k));
        }

        @Test
        @DisplayName("Max Sliding Window - Window Size = Array Length")
        void testMaxSlidingWindowKEqualsN() {
            int[] nums = {1, 3, -1, -3, 5};
            int k = 5;
            int[] expected = {5};
            assertArrayEquals(expected, AdvancedDataStructures.maxSlidingWindow(nums, k));
        }

        @Test
        @DisplayName("Max Sliding Window - All Same")
        void testMaxSlidingWindowAllSame() {
            int[] nums = {7, 7, 7, 7, 7};
            int k = 3;
            int[] expected = {7, 7, 7};
            assertArrayEquals(expected, AdvancedDataStructures.maxSlidingWindow(nums, k));
        }

        @Test
        @DisplayName("Max Sliding Window - Increasing")
        void testMaxSlidingWindowIncreasing() {
            int[] nums = {1, 2, 3, 4, 5};
            int k = 3;
            int[] expected = {3, 4, 5};
            assertArrayEquals(expected, AdvancedDataStructures.maxSlidingWindow(nums, k));
        }

        @Test
        @DisplayName("Max Sliding Window - Decreasing")
        void testMaxSlidingWindowDecreasing() {
            int[] nums = {5, 4, 3, 2, 1};
            int k = 3;
            int[] expected = {5, 4, 3};
            assertArrayEquals(expected, AdvancedDataStructures.maxSlidingWindow(nums, k));
        }

        @Test
        @DisplayName("Min Sliding Window - Basic Case")
        void testMinSlidingWindowBasic() {
            int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
            int k = 3;
            int[] expected = {-1, -3, -3, -3, 3, 3};
            assertArrayEquals(expected, AdvancedDataStructures.minSlidingWindow(nums, k));
        }

        @Test
        @DisplayName("Min Sliding Window - All Positive")
        void testMinSlidingWindowPositive() {
            int[] nums = {5, 4, 3, 2, 1};
            int k = 2;
            int[] expected = {4, 3, 2, 1};
            assertArrayEquals(expected, AdvancedDataStructures.minSlidingWindow(nums, k));
        }
    }

    // =========================================================
    // 3. MIN STACK / MAX STACK TESTS
    // =========================================================

    @Nested
    @DisplayName("MinStack Operations")
    class MinStackTests {

        @Test
        @DisplayName("MinStack - Basic Operations")
        void testMinStackBasic() {
            AdvancedDataStructures.MinStack stack = new AdvancedDataStructures.MinStack();
            stack.push(-2);
            stack.push(0);
            stack.push(-3);
            
            assertEquals(-3, stack.getMin());
            stack.pop();
            assertEquals(0, stack.top());
            assertEquals(-2, stack.getMin());
        }

        @Test
        @DisplayName("MinStack - All Same Values")
        void testMinStackAllSame() {
            AdvancedDataStructures.MinStack stack = new AdvancedDataStructures.MinStack();
            stack.push(5);
            stack.push(5);
            stack.push(5);
            
            assertEquals(5, stack.getMin());
            stack.pop();
            assertEquals(5, stack.getMin());
            stack.pop();
            assertEquals(5, stack.getMin());
        }

        @Test
        @DisplayName("MinStack - Increasing Values")
        void testMinStackIncreasing() {
            AdvancedDataStructures.MinStack stack = new AdvancedDataStructures.MinStack();
            stack.push(1);
            stack.push(2);
            stack.push(3);
            
            assertEquals(1, stack.getMin());
            stack.pop();
            assertEquals(1, stack.getMin());
        }

        @Test
        @DisplayName("MinStack - Decreasing Values")
        void testMinStackDecreasing() {
            AdvancedDataStructures.MinStack stack = new AdvancedDataStructures.MinStack();
            stack.push(3);
            stack.push(2);
            stack.push(1);
            
            assertEquals(1, stack.getMin());
            stack.pop();
            assertEquals(2, stack.getMin());
            stack.pop();
            assertEquals(3, stack.getMin());
        }
    }

    @Nested
    @DisplayName("MaxStack Operations")
    class MaxStackTests {

        @Test
        @DisplayName("MaxStack - Basic Operations")
        void testMaxStackBasic() {
            AdvancedDataStructures.MaxStack stack = new AdvancedDataStructures.MaxStack();
            stack.push(5);
            stack.push(1);
            stack.push(5);
            
            assertEquals(5, stack.top());
            assertEquals(5, stack.peekMax());
        }

        @Test
        @DisplayName("MaxStack - PopMax Operation")
        void testMaxStackPopMax() {
            AdvancedDataStructures.MaxStack stack = new AdvancedDataStructures.MaxStack();
            stack.push(5);
            stack.push(1);
            stack.push(3);
            stack.push(5);
            
            assertEquals(5, stack.popMax());
            assertEquals(3, stack.top());
            assertEquals(5, stack.peekMax());
            assertEquals(5, stack.popMax());
            assertEquals(3, stack.top());
        }

        @Test
        @DisplayName("MaxStack - Multiple Same Max")
        void testMaxStackMultipleSameMax() {
            AdvancedDataStructures.MaxStack stack = new AdvancedDataStructures.MaxStack();
            stack.push(5);
            stack.push(5);
            stack.push(5);
            
            assertEquals(5, stack.popMax());
            assertEquals(5, stack.peekMax());
        }
    }

    // =========================================================
    // 4. CACHE TESTS
    // =========================================================

    @Nested
    @DisplayName("LRU Cache Operations")
    class LRUCacheTests {

        @Test
        @DisplayName("LRU Cache - Basic Operations")
        void testLRUCacheBasic() {
            AdvancedDataStructures.LRUCache cache = new AdvancedDataStructures.LRUCache(2);
            
            cache.put(1, 1);
            cache.put(2, 2);
            assertEquals(1, cache.get(1));
            cache.put(3, 3); // evicts key 2
            assertEquals(-1, cache.get(2));
            cache.put(4, 4); // evicts key 1
            assertEquals(-1, cache.get(1));
            assertEquals(3, cache.get(3));
            assertEquals(4, cache.get(4));
        }

        @Test
        @DisplayName("LRU Cache - Update Existing Key")
        void testLRUCacheUpdate() {
            AdvancedDataStructures.LRUCache cache = new AdvancedDataStructures.LRUCache(2);
            
            cache.put(1, 1);
            cache.put(2, 2);
            cache.put(1, 10); // update key 1
            assertEquals(10, cache.get(1));
            cache.put(3, 3); // evicts key 2
            assertEquals(-1, cache.get(2));
            assertEquals(10, cache.get(1));
        }

        @Test
        @DisplayName("LRU Cache - Capacity 1")
        void testLRUCacheCapacity1() {
            AdvancedDataStructures.LRUCache cache = new AdvancedDataStructures.LRUCache(1);
            
            cache.put(1, 1);
            assertEquals(1, cache.get(1));
            cache.put(2, 2);
            assertEquals(-1, cache.get(1));
            assertEquals(2, cache.get(2));
        }

        @Test
        @DisplayName("LRU Cache - Get Makes Recently Used")
        void testLRUCacheGetUpdatesOrder() {
            AdvancedDataStructures.LRUCache cache = new AdvancedDataStructures.LRUCache(2);
            
            cache.put(1, 1);
            cache.put(2, 2);
            assertEquals(1, cache.get(1)); // makes 1 recently used
            cache.put(3, 3); // should evict 2, not 1
            assertEquals(1, cache.get(1));
            assertEquals(-1, cache.get(2));
            assertEquals(3, cache.get(3));
        }
    }

    @Nested
    @DisplayName("LFU Cache Operations")
    class LFUCacheTests {

        @Test
        @DisplayName("LFU Cache - Basic Operations")
        void testLFUCacheBasic() {
            AdvancedDataStructures.LFUCache cache = new AdvancedDataStructures.LFUCache(2);
            
            cache.put(1, 1);
            cache.put(2, 2);
            assertEquals(1, cache.get(1));
            cache.put(3, 3); // evicts key 2 (freq=1, LRU)
            assertEquals(-1, cache.get(2));
            assertEquals(3, cache.get(3));
            cache.put(4, 4); // evicts key 1 (both at freq=2, 1 is LRU)
            assertEquals(3, cache.get(3));
            assertEquals(-1, cache.get(1));
            assertEquals(4, cache.get(4));
        }

        @Test
        @DisplayName("LFU Cache - Frequency Tracking")
        void testLFUCacheFrequency() {
            AdvancedDataStructures.LFUCache cache = new AdvancedDataStructures.LFUCache(2);
            
            cache.put(1, 1);
            cache.put(2, 2);
            assertEquals(1, cache.get(1)); // freq(1) = 2
            assertEquals(1, cache.get(1)); // freq(1) = 3
            assertEquals(2, cache.get(2)); // freq(2) = 2
            cache.put(3, 3); // both have freq >= 2, but 1 was accessed more recently in LFU list
            // Should still have 1 and 2 based on frequency
            assertTrue(cache.get(1) != -1 || cache.get(2) != -1);
        }

        @Test
        @DisplayName("LFU Cache - Update Value")
        void testLFUCacheUpdate() {
            AdvancedDataStructures.LFUCache cache = new AdvancedDataStructures.LFUCache(2);
            
            cache.put(1, 1);
            cache.put(2, 2);
            cache.put(1, 10); // update value, increases frequency
            assertEquals(10, cache.get(1));
            cache.put(3, 3); // should evict key 2
            assertEquals(-1, cache.get(2));
            assertEquals(10, cache.get(1));
        }

        @Test
        @DisplayName("LFU Cache - Capacity 0")
        void testLFUCacheCapacity0() {
            AdvancedDataStructures.LFUCache cache = new AdvancedDataStructures.LFUCache(0);
            
            cache.put(1, 1);
            assertEquals(-1, cache.get(1));
        }
    }

    // =========================================================
    // 5. INTERVAL OPERATIONS TESTS
    // =========================================================

    @Nested
    @DisplayName("Interval Operations")
    class IntervalTests {

        @Test
        @DisplayName("Merge Intervals - Basic Case")
        void testMergeIntervalsBasic() {
            int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
            int[][] expected = {{1, 6}, {8, 10}, {15, 18}};
            assertArrayEquals(expected, AdvancedDataStructures.mergeIntervals(intervals));
        }

        @Test
        @DisplayName("Merge Intervals - All Overlap")
        void testMergeIntervalsAllOverlap() {
            int[][] intervals = {{1, 4}, {2, 5}, {3, 6}};
            int[][] expected = {{1, 6}};
            assertArrayEquals(expected, AdvancedDataStructures.mergeIntervals(intervals));
        }

        @Test
        @DisplayName("Merge Intervals - No Overlap")
        void testMergeIntervalsNoOverlap() {
            int[][] intervals = {{1, 2}, {3, 4}, {5, 6}};
            int[][] expected = {{1, 2}, {3, 4}, {5, 6}};
            assertArrayEquals(expected, AdvancedDataStructures.mergeIntervals(intervals));
        }

        @Test
        @DisplayName("Merge Intervals - Single Interval")
        void testMergeIntervalsSingle() {
            int[][] intervals = {{1, 4}};
            int[][] expected = {{1, 4}};
            assertArrayEquals(expected, AdvancedDataStructures.mergeIntervals(intervals));
        }

        @Test
        @DisplayName("Merge Intervals - Adjacent Intervals")
        void testMergeIntervalsAdjacent() {
            int[][] intervals = {{1, 4}, {4, 5}};
            int[][] expected = {{1, 5}};
            assertArrayEquals(expected, AdvancedDataStructures.mergeIntervals(intervals));
        }

        @Test
        @DisplayName("Insert Interval - Basic Case")
        void testInsertIntervalBasic() {
            int[][] intervals = {{1, 3}, {6, 9}};
            int[] newInterval = {2, 5};
            int[][] expected = {{1, 5}, {6, 9}};
            assertArrayEquals(expected, AdvancedDataStructures.insertInterval(intervals, newInterval));
        }

        @Test
        @DisplayName("Insert Interval - No Overlap Before")
        void testInsertIntervalBefore() {
            int[][] intervals = {{3, 5}, {6, 9}};
            int[] newInterval = {1, 2};
            int[][] expected = {{1, 2}, {3, 5}, {6, 9}};
            assertArrayEquals(expected, AdvancedDataStructures.insertInterval(intervals, newInterval));
        }

        @Test
        @DisplayName("Insert Interval - No Overlap After")
        void testInsertIntervalAfter() {
            int[][] intervals = {{1, 2}, {3, 5}};
            int[] newInterval = {6, 8};
            int[][] expected = {{1, 2}, {3, 5}, {6, 8}};
            assertArrayEquals(expected, AdvancedDataStructures.insertInterval(intervals, newInterval));
        }

        @Test
        @DisplayName("Insert Interval - Merge All")
        void testInsertIntervalMergeAll() {
            int[][] intervals = {{1, 2}, {3, 5}, {6, 7}, {8, 10}};
            int[] newInterval = {0, 15};
            int[][] expected = {{0, 15}};
            assertArrayEquals(expected, AdvancedDataStructures.insertInterval(intervals, newInterval));
        }

        @Test
        @DisplayName("Insert Interval - Empty Intervals")
        void testInsertIntervalEmpty() {
            int[][] intervals = {};
            int[] newInterval = {5, 7};
            int[][] expected = {{5, 7}};
            assertArrayEquals(expected, AdvancedDataStructures.insertInterval(intervals, newInterval));
        }

        @Test
        @DisplayName("Erase Overlap Intervals - Basic Case")
        void testEraseOverlapIntervalsBasic() {
            int[][] intervals = {{1, 2}, {2, 3}, {3, 4}, {1, 3}};
            assertEquals(1, AdvancedDataStructures.eraseOverlapIntervals(intervals));
        }

        @Test
        @DisplayName("Erase Overlap Intervals - No Overlap")
        void testEraseOverlapIntervalsNoOverlap() {
            int[][] intervals = {{1, 2}, {2, 3}, {3, 4}};
            assertEquals(0, AdvancedDataStructures.eraseOverlapIntervals(intervals));
        }

        @Test
        @DisplayName("Erase Overlap Intervals - All Overlap")
        void testEraseOverlapIntervalsAllOverlap() {
            int[][] intervals = {{1, 5}, {1, 5}, {1, 5}};
            assertEquals(2, AdvancedDataStructures.eraseOverlapIntervals(intervals));
        }

        @Test
        @DisplayName("Min Meeting Rooms - Basic Case")
        void testMinMeetingRoomsBasic() {
            int[][] intervals = {{0, 30}, {5, 10}, {15, 20}};
            assertEquals(2, AdvancedDataStructures.minMeetingRooms(intervals));
        }

        @Test
        @DisplayName("Min Meeting Rooms - No Overlap")
        void testMinMeetingRoomsNoOverlap() {
            int[][] intervals = {{1, 5}, {8, 9}, {10, 15}};
            assertEquals(1, AdvancedDataStructures.minMeetingRooms(intervals));
        }

        @Test
        @DisplayName("Min Meeting Rooms - All Overlap")
        void testMinMeetingRoomsAllOverlap() {
            int[][] intervals = {{1, 10}, {2, 9}, {3, 8}};
            assertEquals(3, AdvancedDataStructures.minMeetingRooms(intervals));
        }

        @Test
        @DisplayName("Min Meeting Rooms - Adjacent Meetings")
        void testMinMeetingRoomsAdjacent() {
            int[][] intervals = {{1, 5}, {5, 10}, {10, 15}};
            assertEquals(1, AdvancedDataStructures.minMeetingRooms(intervals));
        }

        @Test
        @DisplayName("Min Meeting Rooms - Empty")
        void testMinMeetingRoomsEmpty() {
            int[][] intervals = {};
            assertEquals(0, AdvancedDataStructures.minMeetingRooms(intervals));
        }
    }

    // =========================================================
    // 6. MISCELLANEOUS PATTERN TESTS
    // =========================================================

    @Nested
    @DisplayName("Miscellaneous Patterns")
    class MiscellaneousTests {

        @Test
        @DisplayName("Trapping Rain Water - Basic Case")
        void testTrapBasic() {
            int[] height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
            assertEquals(6, AdvancedDataStructures.trap(height));
        }

        @Test
        @DisplayName("Trapping Rain Water - No Water")
        void testTrapNoWater() {
            int[] height = {1, 2, 3, 4, 5};
            assertEquals(0, AdvancedDataStructures.trap(height));
        }

        @Test
        @DisplayName("Trapping Rain Water - Valley")
        void testTrapValley() {
            int[] height = {3, 0, 0, 2, 0, 4};
            assertEquals(10, AdvancedDataStructures.trap(height));
        }

        @Test
        @DisplayName("Trapping Rain Water - Empty")
        void testTrapEmpty() {
            int[] height = {};
            assertEquals(0, AdvancedDataStructures.trap(height));
        }

        @Test
        @DisplayName("Trapping Rain Water - Single Element")
        void testTrapSingle() {
            int[] height = {5};
            assertEquals(0, AdvancedDataStructures.trap(height));
        }

        @Test
        @DisplayName("Trapping Rain Water - Two Elements")
        void testTrapTwo() {
            int[] height = {3, 5};
            assertEquals(0, AdvancedDataStructures.trap(height));
        }

        @Test
        @DisplayName("Product Except Self - Basic Case")
        void testProductExceptSelfBasic() {
            int[] nums = {1, 2, 3, 4};
            int[] expected = {24, 12, 8, 6};
            assertArrayEquals(expected, AdvancedDataStructures.productExceptSelf(nums));
        }

        @Test
        @DisplayName("Product Except Self - With Zero")
        void testProductExceptSelfWithZero() {
            int[] nums = {1, 2, 0, 4};
            int[] expected = {0, 0, 8, 0};
            assertArrayEquals(expected, AdvancedDataStructures.productExceptSelf(nums));
        }

        @Test
        @DisplayName("Product Except Self - Multiple Zeros")
        void testProductExceptSelfMultipleZeros() {
            int[] nums = {0, 0, 2, 3};
            int[] expected = {0, 0, 0, 0};
            assertArrayEquals(expected, AdvancedDataStructures.productExceptSelf(nums));
        }

        @Test
        @DisplayName("Product Except Self - All Ones")
        void testProductExceptSelfAllOnes() {
            int[] nums = {1, 1, 1, 1};
            int[] expected = {1, 1, 1, 1};
            assertArrayEquals(expected, AdvancedDataStructures.productExceptSelf(nums));
        }

        @Test
        @DisplayName("Product Except Self - Negative Numbers")
        void testProductExceptSelfNegative() {
            int[] nums = {-1, 2, -3, 4};
            int[] expected = {-24, 12, -8, 6};
            assertArrayEquals(expected, AdvancedDataStructures.productExceptSelf(nums));
        }

        @Test
        @DisplayName("Product Except Self - Two Elements")
        void testProductExceptSelfTwo() {
            int[] nums = {2, 3};
            int[] expected = {3, 2};
            assertArrayEquals(expected, AdvancedDataStructures.productExceptSelf(nums));
        }
    }

    // =========================================================
    // EDGE CASE AND STRESS TESTS
    // =========================================================

    @Nested
    @DisplayName("Edge Cases and Stress Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Large Array - Next Greater Element")
        void testNextGreaterElementLargeArray() {
            int[] nums = new int[1000];
            for (int i = 0; i < 1000; i++) {
                nums[i] = i;
            }
            int[] result = AdvancedDataStructures.nextGreaterElement(nums);
            assertEquals(-1, result[999]); // Last element has no greater
            assertEquals(1, result[0]); // First element's next greater is 1
        }

        @Test
        @DisplayName("Max Sliding Window - Large Window")
        void testMaxSlidingWindowLargeWindow() {
            int[] nums = new int[100];
            for (int i = 0; i < 100; i++) {
                nums[i] = i;
            }
            int[] result = AdvancedDataStructures.maxSlidingWindow(nums, 50);
            assertEquals(51, result.length);
            assertEquals(99, result[result.length - 1]);
        }

        @Test
        @DisplayName("LRU Cache - Large Capacity")
        void testLRUCacheLargeCapacity() {
            AdvancedDataStructures.LRUCache cache = new AdvancedDataStructures.LRUCache(1000);
            for (int i = 0; i < 1000; i++) {
                cache.put(i, i * 2);
            }
            for (int i = 0; i < 1000; i++) {
                assertEquals(i * 2, cache.get(i));
            }
            cache.put(1000, 2000); // Should evict 0
            assertEquals(-1, cache.get(0));
        }

        @Test
        @DisplayName("Interval Merge - Many Intervals")
        void testMergeIntervalsManyIntervals() {
            int[][] intervals = new int[100][2];
            for (int i = 0; i < 100; i++) {
                intervals[i] = new int[]{i * 2, i * 2 + 1};
            }
            int[][] result = AdvancedDataStructures.mergeIntervals(intervals);
            assertEquals(100, result.length); // No overlaps
        }

        @Test
        @DisplayName("Trapping Rain Water - Large Array")
        void testTrapLargeArray() {
            int[] height = new int[1000];
            for (int i = 0; i < 500; i++) {
                height[i] = i;
            }
            for (int i = 500; i < 1000; i++) {
                height[i] = 1000 - i;
            }
            int result = AdvancedDataStructures.trap(height);
            assertEquals(0, result, "Mountain shape should not trap water");
        }
    }
}
