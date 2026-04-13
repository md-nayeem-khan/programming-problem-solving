package com.cp.algorithms;

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
 * Comprehensive test suite for GreedyTemplates.
 * 
 * Validates all greedy algorithms with:
 * - Basic functionality tests
 * - Edge cases (empty, single element, boundary conditions)
 * - Complex scenarios (large inputs, tie-breaking)
 * - Algorithm correctness validation
 * 
 * Coverage:
 * 1. Jump Game I, II, III
 * 2. Gas Station
 * 3. Candy Distribution
 * 4. Remove K Digits
 * 5. Activity Selection / Interval Scheduling
 * 6. Fractional Knapsack
 * 7. Partition Labels
 * 8. Minimum Cost to Hire Workers
 * 9. Boats to Save People
 * 10. Minimum Arrows to Burst Balloons
 * 11. Minimize Maximum Pair Sum
 * 12. Maximum Units on Truck
 * 13. Wiggle Subsequence
 * 14. Queue Reconstruction by Height
 * 15. Hand of Straights
 */
public class GreedyTemplatesTest {

    // ========== 1. JUMP GAME TESTS ==========
    
    @Nested
    @DisplayName("Jump Game I Tests")
    class JumpGameITests {
        
        @Test
        @DisplayName("Basic jump game - can reach end")
        void testCanReachEnd() {
            int[] nums = {2, 3, 1, 1, 4};
            assertTrue(GreedyTemplates.canJump(nums));
        }
        
        @Test
        @DisplayName("Jump game - cannot reach end")
        void testCannotReachEnd() {
            int[] nums = {3, 2, 1, 0, 4};
            assertFalse(GreedyTemplates.canJump(nums));
        }
        
        @Test
        @DisplayName("Single element - always reachable")
        void testSingleElement() {
            int[] nums = {0};
            assertTrue(GreedyTemplates.canJump(nums));
        }
        
        @Test
        @DisplayName("All zeros except first")
        void testAllZeros() {
            int[] nums = {2, 0, 0};
            assertTrue(GreedyTemplates.canJump(nums));
        }
        
        @Test
        @DisplayName("Large jumps")
        void testLargeJumps() {
            int[] nums = {10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            assertTrue(GreedyTemplates.canJump(nums));
        }
        
        @Test
        @DisplayName("Stuck in middle")
        void testStuckInMiddle() {
            int[] nums = {1, 1, 0, 1};
            assertFalse(GreedyTemplates.canJump(nums));
        }
    }
    
    @Nested
    @DisplayName("Jump Game II Tests")
    class JumpGameIITests {
        
        @Test
        @DisplayName("Basic minimum jumps")
        void testMinimumJumps() {
            int[] nums = {2, 3, 1, 1, 4};
            assertEquals(2, GreedyTemplates.jump(nums));
        }
        
        @Test
        @DisplayName("Single element - zero jumps")
        void testSingleElement() {
            int[] nums = {0};
            assertEquals(0, GreedyTemplates.jump(nums));
        }
        
        @Test
        @DisplayName("One jump needed")
        void testOneJump() {
            int[] nums = {2, 1};
            assertEquals(1, GreedyTemplates.jump(nums));
        }
        
        @Test
        @DisplayName("Greedy choice - pick best range")
        void testGreedyChoice() {
            int[] nums = {2, 3, 0, 1, 4};
            assertEquals(2, GreedyTemplates.jump(nums));
        }
        
        @Test
        @DisplayName("Large array")
        void testLargeArray() {
            int[] nums = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            assertEquals(9, GreedyTemplates.jump(nums));
        }
    }
    
    @Nested
    @DisplayName("Jump Game III Tests")
    class JumpGameIIITests {
        
        @Test
        @DisplayName("Can reach zero")
        void testCanReachZero() {
            int[] arr = {4, 2, 3, 0, 3, 1, 2};
            assertTrue(GreedyTemplates.canReachZero(arr.clone(), 5));
        }
        
        @Test
        @DisplayName("Can reach zero from start")
        void testCanReachZeroFromStart() {
            int[] arr = {4, 2, 3, 0, 3, 1, 2};
            assertTrue(GreedyTemplates.canReachZero(arr.clone(), 0));
        }
        
        @Test
        @DisplayName("Cannot reach zero - stuck in loop")
        void testCannotReachZero() {
            int[] arr = {3, 0, 2, 1, 2};
            assertFalse(GreedyTemplates.canReachZero(arr.clone(), 2));
        }
        
        @Test
        @DisplayName("Start at zero")
        void testStartAtZero() {
            int[] arr = {0, 1, 2};
            assertTrue(GreedyTemplates.canReachZero(arr.clone(), 0));
        }
        
        @Test
        @DisplayName("Single non-zero element")
        void testSingleNonZero() {
            int[] arr = {1};
            assertFalse(GreedyTemplates.canReachZero(arr.clone(), 0));
        }
    }
    
    // ========== 2. GAS STATION TESTS ==========
    
    @Nested
    @DisplayName("Gas Station Tests")
    class GasStationTests {
        
        @Test
        @DisplayName("Basic gas station - solution exists")
        void testBasicSolution() {
            int[] gas = {1, 2, 3, 4, 5};
            int[] cost = {3, 4, 5, 1, 2};
            assertEquals(3, GreedyTemplates.canCompleteCircuit(gas, cost));
        }
        
        @Test
        @DisplayName("No solution - insufficient gas")
        void testNoSolution() {
            int[] gas = {2, 3, 4};
            int[] cost = {3, 4, 3};
            assertEquals(-1, GreedyTemplates.canCompleteCircuit(gas, cost));
        }
        
        @Test
        @DisplayName("Single station")
        void testSingleStation() {
            int[] gas = {5};
            int[] cost = {4};
            assertEquals(0, GreedyTemplates.canCompleteCircuit(gas, cost));
        }
        
        @Test
        @DisplayName("Start from first station")
        void testStartFromFirst() {
            int[] gas = {5, 1, 2, 3, 4};
            int[] cost = {4, 4, 1, 5, 1};
            assertEquals(4, GreedyTemplates.canCompleteCircuit(gas, cost));
        }
        
        @Test
        @DisplayName("Exact match - total gas equals total cost")
        void testExactMatch() {
            int[] gas = {3, 3, 4};
            int[] cost = {3, 4, 3};
            assertEquals(2, GreedyTemplates.canCompleteCircuit(gas, cost));
        }
    }
    
    // ========== 3. CANDY DISTRIBUTION TESTS ==========
    
    @Nested
    @DisplayName("Candy Distribution Tests")
    class CandyTests {
        
        @Test
        @DisplayName("Basic candy distribution")
        void testBasicCandy() {
            int[] ratings = {1, 0, 2};
            assertEquals(5, GreedyTemplates.candy(ratings));
        }
        
        @Test
        @DisplayName("All same ratings")
        void testAllSameRatings() {
            int[] ratings = {1, 1, 1, 1};
            assertEquals(4, GreedyTemplates.candy(ratings));
        }
        
        @Test
        @DisplayName("Increasing ratings")
        void testIncreasingRatings() {
            int[] ratings = {1, 2, 3, 4, 5};
            assertEquals(15, GreedyTemplates.candy(ratings));
        }
        
        @Test
        @DisplayName("Decreasing ratings")
        void testDecreasingRatings() {
            int[] ratings = {5, 4, 3, 2, 1};
            assertEquals(15, GreedyTemplates.candy(ratings));
        }
        
        @Test
        @DisplayName("Single child")
        void testSingleChild() {
            int[] ratings = {1};
            assertEquals(1, GreedyTemplates.candy(ratings));
        }
        
        @Test
        @DisplayName("Valley pattern")
        void testValleyPattern() {
            int[] ratings = {1, 3, 2, 2, 1};
            assertEquals(7, GreedyTemplates.candy(ratings));
        }
        
        @Test
        @DisplayName("Complex pattern")
        void testComplexPattern() {
            int[] ratings = {1, 2, 87, 87, 87, 2, 1};
            assertEquals(13, GreedyTemplates.candy(ratings));
        }
    }
    
    // ========== 4. REMOVE K DIGITS TESTS ==========
    
    @Nested
    @DisplayName("Remove K Digits Tests")
    class RemoveKDigitsTests {
        
        @Test
        @DisplayName("Basic remove k digits")
        void testBasicRemove() {
            assertEquals("1219", GreedyTemplates.removeKdigits("1432219", 3));
        }
        
        @Test
        @DisplayName("Remove all digits")
        void testRemoveAll() {
            assertEquals("0", GreedyTemplates.removeKdigits("10", 2));
        }
        
        @Test
        @DisplayName("Remove leading zeros")
        void testRemoveLeadingZeros() {
            assertEquals("200", GreedyTemplates.removeKdigits("10200", 1));
        }
        
        @Test
        @DisplayName("Increasing digits")
        void testIncreasingDigits() {
            assertEquals("0", GreedyTemplates.removeKdigits("123", 3));
        }
        
        @Test
        @DisplayName("Decreasing digits")
        void testDecreasingDigits() {
            assertEquals("1", GreedyTemplates.removeKdigits("54321", 4));
        }
        
        @Test
        @DisplayName("No removal needed from end")
        void testNoRemovalFromEnd() {
            assertEquals("11", GreedyTemplates.removeKdigits("112", 1));
        }
        
        @Test
        @DisplayName("Single digit")
        void testSingleDigit() {
            assertEquals("0", GreedyTemplates.removeKdigits("9", 1));
        }
    }
    
    // ========== 5. ACTIVITY SELECTION TESTS ==========
    
    @Nested
    @DisplayName("Activity Selection Tests")
    class ActivitySelectionTests {
        
        @Test
        @DisplayName("Basic interval selection")
        void testBasicIntervals() {
            int[][] intervals = {{1, 2}, {2, 3}, {3, 4}, {1, 3}};
            assertEquals(3, GreedyTemplates.maxNonOverlappingIntervals(intervals));
        }
        
        @Test
        @DisplayName("All overlapping")
        void testAllOverlapping() {
            int[][] intervals = {{1, 5}, {2, 6}, {3, 7}};
            assertEquals(1, GreedyTemplates.maxNonOverlappingIntervals(intervals));
        }
        
        @Test
        @DisplayName("No overlapping")
        void testNoOverlapping() {
            int[][] intervals = {{1, 2}, {3, 4}, {5, 6}};
            assertEquals(3, GreedyTemplates.maxNonOverlappingIntervals(intervals));
        }
        
        @Test
        @DisplayName("Empty intervals")
        void testEmptyIntervals() {
            int[][] intervals = {};
            assertEquals(0, GreedyTemplates.maxNonOverlappingIntervals(intervals));
        }
        
        @Test
        @DisplayName("Single interval")
        void testSingleInterval() {
            int[][] intervals = {{1, 2}};
            assertEquals(1, GreedyTemplates.maxNonOverlappingIntervals(intervals));
        }
    }
    
    @Nested
    @DisplayName("Erase Overlap Intervals Tests")
    class EraseOverlapTests {
        
        @Test
        @DisplayName("Basic erase overlaps")
        void testBasicErase() {
            int[][] intervals = {{1, 2}, {2, 3}, {3, 4}, {1, 3}};
            assertEquals(1, GreedyTemplates.eraseOverlapIntervals(intervals));
        }
        
        @Test
        @DisplayName("No overlaps")
        void testNoOverlaps() {
            int[][] intervals = {{1, 2}, {2, 3}};
            assertEquals(0, GreedyTemplates.eraseOverlapIntervals(intervals));
        }
        
        @Test
        @DisplayName("All overlaps")
        void testAllOverlaps() {
            int[][] intervals = {{1, 5}, {2, 6}, {3, 7}, {4, 8}};
            assertEquals(3, GreedyTemplates.eraseOverlapIntervals(intervals));
        }
    }
    
    // ========== 6. FRACTIONAL KNAPSACK TESTS ==========
    
    @Nested
    @DisplayName("Fractional Knapsack Tests")
    class FractionalKnapsackTests {
        
        @Test
        @DisplayName("Basic fractional knapsack")
        void testBasicKnapsack() {
            int[] weights = {10, 20, 30};
            int[] values = {60, 100, 120};
            int capacity = 50;
            assertEquals(240.0, GreedyTemplates.fractionalKnapsack(weights, values, capacity), 0.001);
        }
        
        @Test
        @DisplayName("All items fit")
        void testAllItemsFit() {
            int[] weights = {10, 20};
            int[] values = {60, 100};
            int capacity = 50;
            assertEquals(160.0, GreedyTemplates.fractionalKnapsack(weights, values, capacity), 0.001);
        }
        
        @Test
        @DisplayName("Zero capacity")
        void testZeroCapacity() {
            int[] weights = {10, 20, 30};
            int[] values = {60, 100, 120};
            int capacity = 0;
            assertEquals(0.0, GreedyTemplates.fractionalKnapsack(weights, values, capacity), 0.001);
        }
        
        @Test
        @DisplayName("Single item - partial")
        void testSingleItemPartial() {
            int[] weights = {10};
            int[] values = {60};
            int capacity = 5;
            assertEquals(30.0, GreedyTemplates.fractionalKnapsack(weights, values, capacity), 0.001);
        }
        
        @Test
        @DisplayName("Best value/weight ratio first")
        void testBestRatioFirst() {
            int[] weights = {10, 40, 20, 24};
            int[] values = {100, 280, 120, 120};
            int capacity = 60;
            assertEquals(440.0, GreedyTemplates.fractionalKnapsack(weights, values, capacity), 0.001);
        }
    }
    
    // ========== 7. PARTITION LABELS TESTS ==========
    
    @Nested
    @DisplayName("Partition Labels Tests")
    class PartitionLabelsTests {
        
        @Test
        @DisplayName("Basic partition")
        void testBasicPartition() {
            String s = "ababcbacadefegdehijhklij";
            List<Integer> expected = Arrays.asList(9, 7, 8);
            assertEquals(expected, GreedyTemplates.partitionLabels(s));
        }
        
        @Test
        @DisplayName("Single partition")
        void testSinglePartition() {
            String s = "abcabc";
            List<Integer> expected = Arrays.asList(6);
            assertEquals(expected, GreedyTemplates.partitionLabels(s));
        }
        
        @Test
        @DisplayName("Each character separate")
        void testEachSeparate() {
            String s = "abcdef";
            List<Integer> expected = Arrays.asList(1, 1, 1, 1, 1, 1);
            assertEquals(expected, GreedyTemplates.partitionLabels(s));
        }
        
        @Test
        @DisplayName("Single character")
        void testSingleCharacter() {
            String s = "a";
            List<Integer> expected = Arrays.asList(1);
            assertEquals(expected, GreedyTemplates.partitionLabels(s));
        }
        
        @Test
        @DisplayName("All same character")
        void testAllSame() {
            String s = "aaaaa";
            List<Integer> expected = Arrays.asList(5);
            assertEquals(expected, GreedyTemplates.partitionLabels(s));
        }
    }
    
    // ========== 8. HIRE WORKERS TESTS ==========
    
    @Nested
    @DisplayName("Minimum Cost to Hire Workers Tests")
    class HireWorkersTests {
        
        @Test
        @DisplayName("Basic hire workers")
        void testBasicHire() {
            int[] quality = {10, 20, 5};
            int[] wage = {70, 50, 30};
            int k = 2;
            assertEquals(105.0, GreedyTemplates.minCostToHireWorkers(quality, wage, k), 0.001);
        }
        
        @Test
        @DisplayName("Hire all workers")
        void testHireAll() {
            int[] quality = {3, 1, 10, 10, 1};
            int[] wage = {4, 8, 2, 2, 7};
            int k = 5;
            double result = GreedyTemplates.minCostToHireWorkers(quality, wage, k);
            assertTrue(result > 0);
        }
        
        @Test
        @DisplayName("Hire single worker")
        void testHireSingle() {
            int[] quality = {10, 20, 5};
            int[] wage = {70, 50, 30};
            int k = 1;
            assertEquals(30.0, GreedyTemplates.minCostToHireWorkers(quality, wage, k), 0.001);
        }
    }
    
    // ========== 9. BOATS TO SAVE PEOPLE TESTS ==========
    
    @Nested
    @DisplayName("Boats to Save People Tests")
    class BoatsTests {
        
        @Test
        @DisplayName("Basic boats needed")
        void testBasicBoats() {
            int[] people = {1, 2};
            int limit = 3;
            assertEquals(1, GreedyTemplates.numRescueBoats(people, limit));
        }
        
        @Test
        @DisplayName("Cannot pair anyone")
        void testCannotPair() {
            int[] people = {3, 2, 2, 1};
            int limit = 3;
            assertEquals(3, GreedyTemplates.numRescueBoats(people, limit));
        }
        
        @Test
        @DisplayName("All same weight")
        void testAllSameWeight() {
            int[] people = {5, 5, 5, 5};
            int limit = 5;
            assertEquals(4, GreedyTemplates.numRescueBoats(people, limit));
        }
        
        @Test
        @DisplayName("Pair lightest with heaviest")
        void testOptimalPairing() {
            int[] people = {3, 5, 3, 4};
            int limit = 5;
            assertEquals(4, GreedyTemplates.numRescueBoats(people, limit));
        }
        
        @Test
        @DisplayName("Single person")
        void testSinglePerson() {
            int[] people = {3};
            int limit = 5;
            assertEquals(1, GreedyTemplates.numRescueBoats(people, limit));
        }
    }
    
    // ========== 10. MINIMUM ARROWS TESTS ==========
    
    @Nested
    @DisplayName("Minimum Arrows to Burst Balloons Tests")
    class MinArrowsTests {
        
        @Test
        @DisplayName("Basic minimum arrows")
        void testBasicArrows() {
            int[][] points = {{10, 16}, {2, 8}, {1, 6}, {7, 12}};
            assertEquals(2, GreedyTemplates.findMinArrowShots(points));
        }
        
        @Test
        @DisplayName("All overlapping")
        void testAllOverlapping() {
            int[][] points = {{1, 10}, {2, 9}, {3, 8}, {4, 7}};
            assertEquals(1, GreedyTemplates.findMinArrowShots(points));
        }
        
        @Test
        @DisplayName("No overlapping")
        void testNoOverlapping() {
            int[][] points = {{1, 2}, {3, 4}, {5, 6}};
            assertEquals(3, GreedyTemplates.findMinArrowShots(points));
        }
        
        @Test
        @DisplayName("Single balloon")
        void testSingleBalloon() {
            int[][] points = {{1, 2}};
            assertEquals(1, GreedyTemplates.findMinArrowShots(points));
        }
        
        @Test
        @DisplayName("Adjacent balloons")
        void testAdjacentBalloons() {
            int[][] points = {{1, 2}, {2, 3}, {3, 4}, {4, 5}};
            assertEquals(2, GreedyTemplates.findMinArrowShots(points));
        }
        
        @Test
        @DisplayName("Empty array")
        void testEmptyArray() {
            int[][] points = {};
            assertEquals(0, GreedyTemplates.findMinArrowShots(points));
        }
    }
    
    // ========== 11. MINIMIZE MAXIMUM PAIR SUM TESTS ==========
    
    @Nested
    @DisplayName("Minimize Maximum Pair Sum Tests")
    class MinPairSumTests {
        
        @Test
        @DisplayName("Basic min pair sum")
        void testBasicMinPair() {
            int[] nums = {3, 5, 2, 3};
            assertEquals(7, GreedyTemplates.minPairSum(nums));
        }
        
        @Test
        @DisplayName("Two elements")
        void testTwoElements() {
            int[] nums = {3, 5};
            assertEquals(8, GreedyTemplates.minPairSum(nums));
        }
        
        @Test
        @DisplayName("All same elements")
        void testAllSame() {
            int[] nums = {4, 4, 4, 4};
            assertEquals(8, GreedyTemplates.minPairSum(nums));
        }
        
        @Test
        @DisplayName("Larger array")
        void testLargerArray() {
            int[] nums = {3, 5, 4, 2, 4, 6};
            assertEquals(8, GreedyTemplates.minPairSum(nums));
        }
    }
    
    // ========== 12. MAXIMUM UNITS ON TRUCK TESTS ==========
    
    @Nested
    @DisplayName("Maximum Units on Truck Tests")
    class MaxUnitsTests {
        
        @Test
        @DisplayName("Basic maximum units")
        void testBasicMaxUnits() {
            int[][] boxTypes = {{1, 3}, {2, 2}, {3, 1}};
            int truckSize = 4;
            assertEquals(8, GreedyTemplates.maximumUnits(boxTypes, truckSize));
        }
        
        @Test
        @DisplayName("Truck too small")
        void testTruckTooSmall() {
            int[][] boxTypes = {{5, 10}, {2, 5}, {4, 7}, {3, 9}};
            int truckSize = 10;
            assertEquals(91, GreedyTemplates.maximumUnits(boxTypes, truckSize));
        }
        
        @Test
        @DisplayName("Take all boxes")
        void testTakeAllBoxes() {
            int[][] boxTypes = {{1, 3}, {2, 2}};
            int truckSize = 10;
            assertEquals(7, GreedyTemplates.maximumUnits(boxTypes, truckSize));
        }
        
        @Test
        @DisplayName("Single box type")
        void testSingleBoxType() {
            int[][] boxTypes = {{5, 10}};
            int truckSize = 3;
            assertEquals(30, GreedyTemplates.maximumUnits(boxTypes, truckSize));
        }
    }
    
    // ========== 13. WIGGLE SUBSEQUENCE TESTS ==========
    
    @Nested
    @DisplayName("Wiggle Subsequence Tests")
    class WiggleTests {
        
        @Test
        @DisplayName("Basic wiggle sequence")
        void testBasicWiggle() {
            int[] nums = {1, 7, 4, 9, 2, 5};
            assertEquals(6, GreedyTemplates.wiggleMaxLength(nums));
        }
        
        @Test
        @DisplayName("All increasing")
        void testAllIncreasing() {
            int[] nums = {1, 2, 3, 4, 5};
            assertEquals(2, GreedyTemplates.wiggleMaxLength(nums));
        }
        
        @Test
        @DisplayName("All decreasing")
        void testAllDecreasing() {
            int[] nums = {5, 4, 3, 2, 1};
            assertEquals(2, GreedyTemplates.wiggleMaxLength(nums));
        }
        
        @Test
        @DisplayName("All same")
        void testAllSame() {
            int[] nums = {3, 3, 3, 3};
            assertEquals(1, GreedyTemplates.wiggleMaxLength(nums));
        }
        
        @Test
        @DisplayName("Single element")
        void testSingleElement() {
            int[] nums = {1};
            assertEquals(1, GreedyTemplates.wiggleMaxLength(nums));
        }
        
        @Test
        @DisplayName("Two elements different")
        void testTwoElementsDifferent() {
            int[] nums = {1, 2};
            assertEquals(2, GreedyTemplates.wiggleMaxLength(nums));
        }
        
        @Test
        @DisplayName("Complex pattern")
        void testComplexPattern() {
            int[] nums = {1, 17, 5, 10, 13, 15, 10, 5, 16, 8};
            assertEquals(7, GreedyTemplates.wiggleMaxLength(nums));
        }
    }
    
    // ========== 14. QUEUE RECONSTRUCTION TESTS ==========
    
    @Nested
    @DisplayName("Queue Reconstruction by Height Tests")
    class QueueReconstructionTests {
        
        @Test
        @DisplayName("Basic queue reconstruction")
        void testBasicReconstruction() {
            int[][] people = {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}};
            int[][] expected = {{5, 0}, {7, 0}, {5, 2}, {6, 1}, {4, 4}, {7, 1}};
            assertArrayEquals(expected, GreedyTemplates.reconstructQueue(people));
        }
        
        @Test
        @DisplayName("Single person")
        void testSinglePerson() {
            int[][] people = {{7, 0}};
            int[][] expected = {{7, 0}};
            assertArrayEquals(expected, GreedyTemplates.reconstructQueue(people));
        }
        
        @Test
        @DisplayName("All same height")
        void testAllSameHeight() {
            int[][] people = {{6, 0}, {6, 1}, {6, 2}};
            int[][] expected = {{6, 0}, {6, 1}, {6, 2}};
            assertArrayEquals(expected, GreedyTemplates.reconstructQueue(people));
        }
        
        @Test
        @DisplayName("Two people")
        void testTwoPeople() {
            int[][] people = {{6, 0}, {5, 0}};
            int[][] expected = {{5, 0}, {6, 0}};
            assertArrayEquals(expected, GreedyTemplates.reconstructQueue(people));
        }
    }
    
    // ========== 15. HAND OF STRAIGHTS TESTS ==========
    
    @Nested
    @DisplayName("Hand of Straights Tests")
    class HandOfStraightsTests {
        
        @Test
        @DisplayName("Basic hand of straights - possible")
        void testBasicStraights() {
            int[] hand = {1, 2, 3, 6, 2, 3, 4, 7, 8};
            int groupSize = 3;
            assertTrue(GreedyTemplates.isNStraightHand(hand, groupSize));
        }
        
        @Test
        @DisplayName("Hand of straights - impossible")
        void testImpossible() {
            int[] hand = {1, 2, 3, 4, 5};
            int groupSize = 4;
            assertFalse(GreedyTemplates.isNStraightHand(hand, groupSize));
        }
        
        @Test
        @DisplayName("Group size 1")
        void testGroupSizeOne() {
            int[] hand = {1, 2, 3, 4, 5};
            int groupSize = 1;
            assertTrue(GreedyTemplates.isNStraightHand(hand, groupSize));
        }
        
        @Test
        @DisplayName("Not divisible by group size")
        void testNotDivisible() {
            int[] hand = {1, 2, 3, 4, 5};
            int groupSize = 2;
            assertFalse(GreedyTemplates.isNStraightHand(hand, groupSize));
        }
        
        @Test
        @DisplayName("Single group")
        void testSingleGroup() {
            int[] hand = {1, 2, 3};
            int groupSize = 3;
            assertTrue(GreedyTemplates.isNStraightHand(hand, groupSize));
        }
        
        @Test
        @DisplayName("Gap in sequence")
        void testGapInSequence() {
            int[] hand = {1, 2, 3, 5};
            int groupSize = 2;
            assertFalse(GreedyTemplates.isNStraightHand(hand, groupSize));
        }
        
        @Test
        @DisplayName("Duplicates")
        void testDuplicates() {
            int[] hand = {1, 1, 2, 2, 3, 3};
            int groupSize = 3;
            assertTrue(GreedyTemplates.isNStraightHand(hand, groupSize));
        }
    }
    
    // ========== EDGE CASES AND STRESS TESTS ==========
    
    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Large jump array")
        void testLargeJumpArray() {
            int[] nums = new int[10000];
            Arrays.fill(nums, 1);
            nums[0] = 10000;
            assertTrue(GreedyTemplates.canJump(nums));
        }
        
        @Test
        @DisplayName("Large candy array with random ratings")
        void testLargeCandyArray() {
            int[] ratings = new int[1000];
            for (int i = 0; i < ratings.length; i++) {
                ratings[i] = (i % 3) + 1;
            }
            int result = GreedyTemplates.candy(ratings);
            assertTrue(result >= ratings.length);
        }
        
        @Test
        @DisplayName("Remove k digits - all same digits")
        void testRemoveKAllSame() {
            assertEquals("0", GreedyTemplates.removeKdigits("1111", 4));
        }
        
        @Test
        @DisplayName("Fractional knapsack with zero values")
        void testKnapsackZeroValues() {
            int[] weights = {10, 20, 30};
            int[] values = {0, 0, 0};
            int capacity = 50;
            assertEquals(0.0, GreedyTemplates.fractionalKnapsack(weights, values, capacity), 0.001);
        }
    }
}
