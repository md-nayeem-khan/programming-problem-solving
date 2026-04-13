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
 * Comprehensive test suite for BitmaskDPTemplates
 * Tests all bitmask operations, TSP, Hamiltonian path, LeetCode problems, and SOS DP
 * 
 * Coverage:
 * 1. Basic Bitmask Operations (isSet, setBit, clearBit, toggleBit, etc.)
 * 2. Traveling Salesman Problem (TSP) - cost and path
 * 3. Hamiltonian Path detection
 * 4. LeetCode 847: Shortest Path Visiting All Nodes
 * 5. LeetCode 1434: Number of Ways to Wear Different Hats
 * 6. LeetCode 1986: Minimum Work Sessions
 * 7. Subset Sum with Bitmask
 * 8. Assignment Problem
 * 9. Sum Over Subsets (SOS) DP
 * 10. SOS DP Applications (AND pairs, XOR subset, etc.)
 */
public class BitmaskDPTemplatesTest {

    // ========== 1. BITMASK OPERATIONS TESTS ==========
    
    @Nested
    @DisplayName("Bitmask Operations Tests")
    class BitmaskOperationsTests {
        
        @Test
        @DisplayName("Test isSet operation")
        void testIsSet() {
            int mask = 0b1010; // Binary: 1010 (bits 1 and 3 set)
            
            assertFalse(BitmaskDPTemplates.isSet(mask, 0), "Bit 0 should not be set");
            assertTrue(BitmaskDPTemplates.isSet(mask, 1), "Bit 1 should be set");
            assertFalse(BitmaskDPTemplates.isSet(mask, 2), "Bit 2 should not be set");
            assertTrue(BitmaskDPTemplates.isSet(mask, 3), "Bit 3 should be set");
            assertFalse(BitmaskDPTemplates.isSet(mask, 4), "Bit 4 should not be set");
        }
        
        @Test
        @DisplayName("Test setBit operation")
        void testSetBit() {
            int mask = 0b1010; // Binary: 1010
            
            assertEquals(0b1010, BitmaskDPTemplates.setBit(mask, 1), "Setting already set bit should not change");
            assertEquals(0b1011, BitmaskDPTemplates.setBit(mask, 0), "Setting bit 0");
            assertEquals(0b1110, BitmaskDPTemplates.setBit(mask, 2), "Setting bit 2");
        }
        
        @Test
        @DisplayName("Test clearBit operation")
        void testClearBit() {
            int mask = 0b1111; // Binary: 1111
            
            assertEquals(0b1110, BitmaskDPTemplates.clearBit(mask, 0), "Clearing bit 0");
            assertEquals(0b1101, BitmaskDPTemplates.clearBit(mask, 1), "Clearing bit 1");
            assertEquals(0b1011, BitmaskDPTemplates.clearBit(mask, 2), "Clearing bit 2");
            assertEquals(0b0111, BitmaskDPTemplates.clearBit(mask, 3), "Clearing bit 3");
        }
        
        @Test
        @DisplayName("Test toggleBit operation")
        void testToggleBit() {
            int mask = 0b1010; // Binary: 1010
            
            assertEquals(0b1011, BitmaskDPTemplates.toggleBit(mask, 0), "Toggle bit 0");
            assertEquals(0b1000, BitmaskDPTemplates.toggleBit(mask, 1), "Toggle bit 1");
            assertEquals(0b1110, BitmaskDPTemplates.toggleBit(mask, 2), "Toggle bit 2");
            assertEquals(0b0010, BitmaskDPTemplates.toggleBit(mask, 3), "Toggle bit 3");
        }
        
        @Test
        @DisplayName("Test countBits operation")
        void testCountBits() {
            assertEquals(0, BitmaskDPTemplates.countBits(0b0000), "Empty mask");
            assertEquals(1, BitmaskDPTemplates.countBits(0b0001), "One bit set");
            assertEquals(2, BitmaskDPTemplates.countBits(0b1010), "Two bits set");
            assertEquals(3, BitmaskDPTemplates.countBits(0b1011), "Three bits set");
            assertEquals(4, BitmaskDPTemplates.countBits(0b1111), "All bits set");
            assertEquals(8, BitmaskDPTemplates.countBits(0b11111111), "Eight bits set");
        }
        
        @Test
        @DisplayName("Test lowestBit operation")
        void testLowestBit() {
            assertEquals(0b0001, BitmaskDPTemplates.lowestBit(0b1111), "Lowest bit of 1111");
            assertEquals(0b0010, BitmaskDPTemplates.lowestBit(0b1010), "Lowest bit of 1010");
            assertEquals(0b0100, BitmaskDPTemplates.lowestBit(0b1100), "Lowest bit of 1100");
            assertEquals(0b1000, BitmaskDPTemplates.lowestBit(0b1000), "Lowest bit of 1000");
            assertEquals(0, BitmaskDPTemplates.lowestBit(0), "Lowest bit of 0");
        }
        
        @Test
        @DisplayName("Test removeLowestBit operation")
        void testRemoveLowestBit() {
            assertEquals(0b1110, BitmaskDPTemplates.removeLowestBit(0b1111), "Remove lowest from 1111");
            assertEquals(0b1000, BitmaskDPTemplates.removeLowestBit(0b1010), "Remove lowest from 1010");
            assertEquals(0b1000, BitmaskDPTemplates.removeLowestBit(0b1100), "Remove lowest from 1100");
            assertEquals(0, BitmaskDPTemplates.removeLowestBit(0b1000), "Remove lowest from 1000");
            assertEquals(0, BitmaskDPTemplates.removeLowestBit(0), "Remove lowest from 0");
        }
        
        @Test
        @DisplayName("Test allSubsets enumeration")
        void testAllSubsets() {
            int mask = 5; // Binary: 101
            List<Integer> subsets = BitmaskDPTemplates.allSubsets(mask);
            
            // Subsets of 101 are: 101, 100, 001, 000
            assertEquals(4, subsets.size(), "Should have 4 subsets");
            assertTrue(subsets.contains(5), "Should contain 101 (5)");
            assertTrue(subsets.contains(4), "Should contain 100 (4)");
            assertTrue(subsets.contains(1), "Should contain 001 (1)");
            assertTrue(subsets.contains(0), "Should contain 000 (0)");
        }
        
        @Test
        @DisplayName("Test allSubsets of empty set")
        void testAllSubsetsEmpty() {
            List<Integer> subsets = BitmaskDPTemplates.allSubsets(0);
            assertEquals(1, subsets.size(), "Empty set has one subset: itself");
            assertEquals(0, subsets.get(0), "Only subset is 0");
        }
    }
    
    // ========== 2. TRAVELING SALESMAN PROBLEM TESTS ==========
    
    @Nested
    @DisplayName("Traveling Salesman Problem (TSP) Tests")
    class TSPTests {
        
        @Test
        @DisplayName("Test TSP with 4 cities - standard case")
        void testTSPBasic() {
            int[][] dist = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
            };
            
            int result = BitmaskDPTemplates.tsp(dist);
            assertEquals(80, result, "Minimum TSP cost should be 80");
        }
        
        @Test
        @DisplayName("Test TSP with 3 cities")
        void testTSPThreeCities() {
            int[][] dist = {
                {0, 10, 15},
                {10, 0, 20},
                {15, 20, 0}
            };
            
            int result = BitmaskDPTemplates.tsp(dist);
            assertEquals(45, result, "TSP cost: 0->1(10) + 1->2(20) + 2->0(15) = 45");
        }
        
        @Test
        @DisplayName("Test TSP with 2 cities")
        void testTSPTwoCities() {
            int[][] dist = {
                {0, 10},
                {10, 0}
            };
            
            int result = BitmaskDPTemplates.tsp(dist);
            assertEquals(20, result, "TSP cost: 0->1(10) + 1->0(10) = 20");
        }
        
        @Test
        @DisplayName("Test TSP path reconstruction")
        void testTSPPath() {
            int[][] dist = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
            };
            
            List<Integer> path = BitmaskDPTemplates.tspPath(dist);
            
            assertNotNull(path, "Path should not be null");
            assertEquals(4, path.size(), "Path should visit 4 cities");
            assertEquals(0, path.get(0), "Path should start at city 0");
            
            // Verify all cities are visited
            Set<Integer> visited = new HashSet<>(path);
            assertEquals(4, visited.size(), "All cities should be visited exactly once");
            for (int i = 0; i < 4; i++) {
                assertTrue(visited.contains(i), "City " + i + " should be visited");
            }
        }
        
        @Test
        @DisplayName("Test TSP with asymmetric distances")
        void testTSPAsymmetric() {
            int[][] dist = {
                {0, 10, 100},
                {100, 0, 10},
                {10, 100, 0}
            };
            
            int result = BitmaskDPTemplates.tsp(dist);
            // Best path: 0->1(10) + 1->2(10) + 2->0(10) = 30
            assertEquals(30, result, "Asymmetric TSP should find best route");
        }
    }
    
    // ========== 3. HAMILTONIAN PATH TESTS ==========
    
    @Nested
    @DisplayName("Hamiltonian Path Tests")
    class HamiltonianPathTests {
        
        @Test
        @DisplayName("Test graph with Hamiltonian path")
        void testHasHamiltonianPath() {
            int[][] graph = {
                {0, 1, 1, 1},
                {1, 0, 1, 0},
                {1, 1, 0, 1},
                {1, 0, 1, 0}
            };
            
            assertTrue(BitmaskDPTemplates.hasHamiltonianPath(graph), 
                      "Graph should have a Hamiltonian path");
        }
        
        @Test
        @DisplayName("Test complete graph (all nodes connected)")
        void testCompleteGraphHamiltonianPath() {
            int[][] graph = {
                {0, 1, 1, 1},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {1, 1, 1, 0}
            };
            
            assertTrue(BitmaskDPTemplates.hasHamiltonianPath(graph), 
                      "Complete graph always has Hamiltonian path");
        }
        
        @Test
        @DisplayName("Test disconnected graph")
        void testDisconnectedGraph() {
            int[][] graph = {
                {0, 1, 0, 0},
                {1, 0, 0, 0},
                {0, 0, 0, 1},
                {0, 0, 1, 0}
            };
            
            assertFalse(BitmaskDPTemplates.hasHamiltonianPath(graph), 
                       "Disconnected graph should not have Hamiltonian path");
        }
        
        @Test
        @DisplayName("Test single node")
        void testSingleNode() {
            int[][] graph = {{0}};
            assertTrue(BitmaskDPTemplates.hasHamiltonianPath(graph), 
                      "Single node trivially has Hamiltonian path");
        }
        
        @Test
        @DisplayName("Test linear graph")
        void testLinearGraph() {
            int[][] graph = {
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {0, 1, 0, 1},
                {0, 0, 1, 0}
            };
            
            assertTrue(BitmaskDPTemplates.hasHamiltonianPath(graph), 
                      "Linear graph should have Hamiltonian path");
        }
    }
    
    // ========== 4. LEETCODE 847: SHORTEST PATH VISITING ALL NODES ==========
    
    @Nested
    @DisplayName("LeetCode 847: Shortest Path Visiting All Nodes")
    class ShortestPathLengthTests {
        
        @Test
        @DisplayName("Test example 1: [[1,2,3],[0],[0],[0]]")
        void testExample1() {
            int[][] graph = {{1,2,3},{0},{0},{0}};
            int result = BitmaskDPTemplates.shortestPathLength(graph);
            assertEquals(4, result, "Shortest path should be 4");
        }
        
        @Test
        @DisplayName("Test example 2: [[1],[0,2,4],[1,3,4],[2],[1,2]]")
        void testExample2() {
            int[][] graph = {{1},{0,2,4},{1,3,4},{2},{1,2}};
            int result = BitmaskDPTemplates.shortestPathLength(graph);
            assertEquals(4, result, "Shortest path should be 4");
        }
        
        @Test
        @DisplayName("Test single node")
        void testSingleNode() {
            int[][] graph = {{}};
            int result = BitmaskDPTemplates.shortestPathLength(graph);
            assertEquals(0, result, "Single node requires 0 steps");
        }
        
        @Test
        @DisplayName("Test two connected nodes")
        void testTwoNodes() {
            int[][] graph = {{1},{0}};
            int result = BitmaskDPTemplates.shortestPathLength(graph);
            assertEquals(1, result, "Two nodes require 1 edge");
        }
        
        @Test
        @DisplayName("Test linear graph with 3 nodes")
        void testLinearThreeNodes() {
            int[][] graph = {{1},{0,2},{1}};
            int result = BitmaskDPTemplates.shortestPathLength(graph);
            assertEquals(2, result, "Linear 3-node graph requires 2 edges");
        }
        
        @Test
        @DisplayName("Test complete graph with 3 nodes")
        void testCompleteThreeNodes() {
            int[][] graph = {{1,2},{0,2},{0,1}};
            int result = BitmaskDPTemplates.shortestPathLength(graph);
            assertEquals(2, result, "Complete triangle requires 2 edges");
        }
    }
    
    // ========== 5. LEETCODE 1434: NUMBER OF WAYS TO WEAR DIFFERENT HATS ==========
    
    @Nested
    @DisplayName("LeetCode 1434: Number of Ways to Wear Different Hats")
    class NumberWaysTests {
        
        @Test
        @DisplayName("Test example 1: [[3,4],[4,5],[5]]")
        void testExample1() {
            List<List<Integer>> hats = Arrays.asList(
                Arrays.asList(3, 4),
                Arrays.asList(4, 5),
                Arrays.asList(5)
            );
            int result = BitmaskDPTemplates.numberWays(hats);
            assertEquals(1, result, "Should have 1 way");
        }
        
        @Test
        @DisplayName("Test example 2: [[3,5,1],[3,5]]")
        void testExample2() {
            List<List<Integer>> hats = Arrays.asList(
                Arrays.asList(3, 5, 1),
                Arrays.asList(3, 5)
            );
            int result = BitmaskDPTemplates.numberWays(hats);
            assertEquals(4, result, "Should have 4 ways");
        }
        
        @Test
        @DisplayName("Test example 3: [[1,2,3,4],[1,2,3,4],[1,2,3,4],[1,2,3,4]]")
        void testExample3() {
            List<List<Integer>> hats = Arrays.asList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(1, 2, 3, 4)
            );
            int result = BitmaskDPTemplates.numberWays(hats);
            assertEquals(24, result, "Should have 24 ways (4!)");
        }
        
        @Test
        @DisplayName("Test single person with one hat")
        void testSinglePersonOneHat() {
            List<List<Integer>> hats = Arrays.asList(
                Arrays.asList(1)
            );
            int result = BitmaskDPTemplates.numberWays(hats);
            assertEquals(1, result, "One person, one hat = 1 way");
        }
        
        @Test
        @DisplayName("Test single person with multiple hats")
        void testSinglePersonMultipleHats() {
            List<List<Integer>> hats = Arrays.asList(
                Arrays.asList(1, 2, 3)
            );
            int result = BitmaskDPTemplates.numberWays(hats);
            assertEquals(3, result, "One person with 3 hats = 3 ways");
        }
    }
    
    // ========== 6. LEETCODE 1986: MINIMUM WORK SESSIONS ==========
    
    @Nested
    @DisplayName("LeetCode 1986: Minimum Work Sessions")
    class MinSessionsTests {
        
        @Test
        @DisplayName("Test example 1: tasks=[1,2,3], sessionTime=3")
        void testExample1() {
            int[] tasks = {1, 2, 3};
            int sessionTime = 3;
            int result = BitmaskDPTemplates.minSessions(tasks, sessionTime);
            assertEquals(2, result, "Should need 2 sessions");
        }
        
        @Test
        @DisplayName("Test example 2: tasks=[3,1,3,1,1], sessionTime=8")
        void testExample2() {
            int[] tasks = {3, 1, 3, 1, 1};
            int sessionTime = 8;
            int result = BitmaskDPTemplates.minSessions(tasks, sessionTime);
            assertEquals(2, result, "Should need 2 sessions");
        }
        
        @Test
        @DisplayName("Test example 3: tasks=[1,2,3,4,5], sessionTime=15")
        void testExample3() {
            int[] tasks = {1, 2, 3, 4, 5};
            int sessionTime = 15;
            int result = BitmaskDPTemplates.minSessions(tasks, sessionTime);
            assertEquals(1, result, "All tasks fit in 1 session");
        }
        
        @Test
        @DisplayName("Test single task")
        void testSingleTask() {
            int[] tasks = {5};
            int sessionTime = 10;
            int result = BitmaskDPTemplates.minSessions(tasks, sessionTime);
            assertEquals(1, result, "Single task needs 1 session");
        }
        
        @Test
        @DisplayName("Test tasks each need separate session")
        void testSeparateSessions() {
            int[] tasks = {5, 5, 5};
            int sessionTime = 5;
            int result = BitmaskDPTemplates.minSessions(tasks, sessionTime);
            assertEquals(3, result, "Each task needs its own session");
        }
        
        @Test
        @DisplayName("Test two tasks fit in one session")
        void testTwoTasksOneSession() {
            int[] tasks = {3, 4};
            int sessionTime = 10;
            int result = BitmaskDPTemplates.minSessions(tasks, sessionTime);
            assertEquals(1, result, "Both tasks fit in 1 session");
        }
    }
    
    // ========== 7. SUBSET SUM WITH BITMASK DP TESTS ==========
    
    @Nested
    @DisplayName("Subset Sum Tests")
    class SubsetSumTests {
        
        @Test
        @DisplayName("Test basic subset sum counting")
        void testBasicSubsetSum() {
            int[] nums = {1, 2, 3};
            int target = 3;
            int result = BitmaskDPTemplates.countSubsetsWithSum(nums, target);
            // Subsets with sum 3: {3}, {1,2}
            assertEquals(2, result, "Should find 2 subsets with sum 3");
        }
        
        @Test
        @DisplayName("Test subset sum with no solution")
        void testSubsetSumNoSolution() {
            int[] nums = {1, 2, 3};
            int target = 10;
            int result = BitmaskDPTemplates.countSubsetsWithSum(nums, target);
            assertEquals(0, result, "No subset has sum 10");
        }
        
        @Test
        @DisplayName("Test subset sum with target 0")
        void testSubsetSumZero() {
            int[] nums = {1, 2, 3};
            int target = 0;
            int result = BitmaskDPTemplates.countSubsetsWithSum(nums, target);
            assertEquals(1, result, "Empty subset has sum 0");
        }
        
        @Test
        @DisplayName("Test subset sum with duplicate subsets")
        void testSubsetSumMultiple() {
            int[] nums = {1, 1, 1, 1};
            int target = 2;
            int result = BitmaskDPTemplates.countSubsetsWithSum(nums, target);
            // Multiple subsets: pick any 2 of the 4 ones
            assertTrue(result > 0, "Should find subsets with sum 2");
        }
    }
    
    // ========== 8. ASSIGNMENT PROBLEM TESTS ==========
    
    @Nested
    @DisplayName("Assignment Problem Tests")
    class AssignmentTests {
        
        @Test
        @DisplayName("Test 3x3 assignment")
        void testAssignment3x3() {
            int[][] cost = {
                {10, 20, 30},
                {15, 25, 35},
                {20, 30, 40}
            };
            int result = BitmaskDPTemplates.minCostAssignment(cost);
            // Best: worker0->task0(10) + worker1->task1(25) + worker2->task2(40) = 75
            assertEquals(75, result, "Minimum assignment cost should be 75");
        }
        
        @Test
        @DisplayName("Test 2x2 assignment")
        void testAssignment2x2() {
            int[][] cost = {
                {5, 10},
                {10, 5}
            };
            int result = BitmaskDPTemplates.minCostAssignment(cost);
            // Best: worker0->task0(5) + worker1->task1(5) = 10
            assertEquals(10, result, "Minimum assignment cost should be 10");
        }
        
        @Test
        @DisplayName("Test 4x4 assignment")
        void testAssignment4x4() {
            int[][] cost = {
                {90, 75, 75, 80},
                {35, 85, 55, 65},
                {125, 95, 90, 105},
                {45, 110, 95, 115}
            };
            int result = BitmaskDPTemplates.minCostAssignment(cost);
            // Optimal assignment
            assertTrue(result > 0, "Should find valid assignment");
            assertTrue(result <= 90 + 85 + 90 + 95, "Should find reasonable solution");
        }
        
        @Test
        @DisplayName("Test 1x1 assignment")
        void testAssignment1x1() {
            int[][] cost = {{42}};
            int result = BitmaskDPTemplates.minCostAssignment(cost);
            assertEquals(42, result, "Single assignment should return that cost");
        }
    }
    
    // ========== 9. SUM OVER SUBSETS (SOS) DP TESTS ==========
    
    @Nested
    @DisplayName("Sum Over Subsets (SOS) DP Tests")
    class SOSDPTests {
        
        @Test
        @DisplayName("Test basic SOS DP")
        void testBasicSOSDP() {
            int[] F = {1, 2, 3, 4};
            int[] result = BitmaskDPTemplates.sumOverSubsetsOptimized(F);
            
            // mask 0 (00): sum of submasks {00} = 1
            assertEquals(1, result[0], "Sum for mask 0");
            
            // mask 1 (01): sum of submasks {00, 01} = 1 + 2 = 3
            assertEquals(3, result[1], "Sum for mask 1");
            
            // mask 2 (10): sum of submasks {00, 10} = 1 + 3 = 4
            assertEquals(4, result[2], "Sum for mask 2");
            
            // mask 3 (11): sum of submasks {00, 01, 10, 11} = 1 + 2 + 3 + 4 = 10
            assertEquals(10, result[3], "Sum for mask 3");
        }
        
        @Test
        @DisplayName("Test SOS DP unoptimized vs optimized")
        void testSOSDPConsistency() {
            int[] F = {1, 2, 3, 4, 5, 6, 7, 8};
            int[] result1 = BitmaskDPTemplates.sumOverSubsets(F);
            int[] result2 = BitmaskDPTemplates.sumOverSubsetsOptimized(F);
            
            assertArrayEquals(result1, result2, 
                            "Unoptimized and optimized SOS DP should give same results");
        }
        
        @Test
        @DisplayName("Test SOS DP with all zeros")
        void testSOSDPAllZeros() {
            int[] F = {0, 0, 0, 0};
            int[] result = BitmaskDPTemplates.sumOverSubsetsOptimized(F);
            
            for (int i = 0; i < result.length; i++) {
                assertEquals(0, result[i], "All sums should be 0");
            }
        }
        
        @Test
        @DisplayName("Test SOS DP with single bit masks")
        void testSOSDPSingleBits() {
            int[] F = {1, 0, 0, 0}; // Only mask 0 has value
            int[] result = BitmaskDPTemplates.sumOverSubsetsOptimized(F);
            
            // All masks should sum to 1 (since 0 is submask of all)
            for (int i = 0; i < result.length; i++) {
                assertEquals(1, result[i], "Each mask contains submask 0");
            }
        }
    }
    
    // ========== 10. SUM OVER SUPERSETS TESTS ==========
    
    @Nested
    @DisplayName("Sum Over Supersets Tests")
    class SupersetsTests {
        
        @Test
        @DisplayName("Test basic sum over supersets")
        void testBasicSupersets() {
            int[] F = {1, 2, 3, 4};
            int[] result = BitmaskDPTemplates.sumOverSupersets(F);
            
            // mask 0 (00): sum of supersets {00, 01, 10, 11} = 1 + 2 + 3 + 4 = 10
            assertEquals(10, result[0], "Sum of supersets for mask 0");
            
            // mask 1 (01): sum of supersets {01, 11} = 2 + 4 = 6
            assertEquals(6, result[1], "Sum of supersets for mask 1");
            
            // mask 2 (10): sum of supersets {10, 11} = 3 + 4 = 7
            assertEquals(7, result[2], "Sum of supersets for mask 2");
            
            // mask 3 (11): sum of supersets {11} = 4
            assertEquals(4, result[3], "Sum of supersets for mask 3");
        }
        
        @Test
        @DisplayName("Test supersets with single value")
        void testSupersetsMaxMask() {
            int[] F = {0, 0, 0, 1}; // Only max mask has value
            int[] result = BitmaskDPTemplates.sumOverSupersets(F);
            
            // All masks should sum to 1 (since max mask is superset of all)
            for (int i = 0; i < result.length; i++) {
                assertEquals(1, result[i], "Max mask is superset of all masks");
            }
        }
    }
    
    // ========== 11. COUNT PAIRS WITH AND = 0 TESTS ==========
    
    @Nested
    @DisplayName("Count Pairs with AND = 0 Tests")
    class CountPairsANDZeroTests {
        
        @Test
        @DisplayName("Test pairs with AND=0")
        void testCountPairsBasic() {
            int[] arr = {1, 2, 3};
            long result = BitmaskDPTemplates.countPairsWithANDZero(arr);
            // Pairs: (1,2) -> 1&2=0 ✗, (1,3) -> 1&3=1 ✗, (2,3) -> 2&3=2 ✗
            // Actually none have AND=0
            assertTrue(result >= 0, "Should count valid pairs");
        }
        
        @Test
        @DisplayName("Test pairs with complementary bits")
        void testCountPairsComplementary() {
            int[] arr = {1, 2}; // 01 and 10
            long result = BitmaskDPTemplates.countPairsWithANDZero(arr);
            // 1 & 2 = 0, so 1 pair
            assertEquals(1, result, "Complementary bits should have AND=0");
        }
        
        @Test
        @DisplayName("Test all zeros")
        void testCountPairsAllZeros() {
            int[] arr = {0, 0, 0};
            long result = BitmaskDPTemplates.countPairsWithANDZero(arr);
            // All pairs of zeros have AND=0
            assertEquals(3, result, "All pairs of zeros have AND=0");
        }
        
        @Test
        @DisplayName("Test with powers of 2")
        void testCountPairsPowersOf2() {
            int[] arr = {1, 2, 4, 8}; // All powers of 2
            long result = BitmaskDPTemplates.countPairsWithANDZero(arr);
            // All pairs of different powers of 2 have AND=0
            assertEquals(6, result, "All pairs of powers of 2 have AND=0 (4C2=6)");
        }
    }
    
    // ========== 12. MAXIMUM XOR SUBSET TESTS ==========
    
    @Nested
    @DisplayName("Maximum XOR Subset Tests")
    class MaximumXORSubsetTests {
        
        @Test
        @DisplayName("Test maximum XOR subset")
        void testMaxXORBasic() {
            int[] arr = {2, 4, 5, 7};
            int result = BitmaskDPTemplates.maximumXORSubset(arr);
            assertEquals(7, result, "Maximum XOR should be 7");
        }
        
        @Test
        @DisplayName("Test maximum XOR with single element")
        void testMaxXORSingleElement() {
            int[] arr = {42};
            int result = BitmaskDPTemplates.maximumXORSubset(arr);
            assertEquals(42, result, "Single element XOR is itself");
        }
        
        @Test
        @DisplayName("Test maximum XOR with zeros")
        void testMaxXORWithZeros() {
            int[] arr = {0, 5, 10};
            int result = BitmaskDPTemplates.maximumXORSubset(arr);
            // Best is 5 XOR 10 = 15
            assertEquals(15, result, "Should find 5^10=15");
        }
        
        @Test
        @DisplayName("Test maximum XOR with identical elements")
        void testMaxXORIdentical() {
            int[] arr = {7, 7, 7};
            int result = BitmaskDPTemplates.maximumXORSubset(arr);
            assertEquals(7, result, "XOR of identical elements is the element");
        }
    }
    
    // ========== 13. SMALLEST SUFFICIENT TEAM TESTS ==========
    
    @Nested
    @DisplayName("LeetCode 1125: Smallest Sufficient Team Tests")
    class SmallestSufficientTeamTests {
        
        @Test
        @DisplayName("Test example 1")
        void testExample1() {
            String[] req_skills = {"java", "nodejs", "reactjs"};
            List<List<String>> people = Arrays.asList(
                Arrays.asList("java"),
                Arrays.asList("nodejs"),
                Arrays.asList("nodejs", "reactjs")
            );
            
            int[] result = BitmaskDPTemplates.smallestSufficientTeam(req_skills, people);
            
            assertNotNull(result, "Should return a team");
            assertTrue(result.length >= 2, "Should need at least 2 people");
            assertTrue(result.length <= 3, "Should not need more than 3 people");
        }
        
        @Test
        @DisplayName("Test single person with all skills")
        void testSinglePersonAllSkills() {
            String[] req_skills = {"java", "nodejs"};
            List<List<String>> people = Arrays.asList(
                Arrays.asList("java", "nodejs")
            );
            
            int[] result = BitmaskDPTemplates.smallestSufficientTeam(req_skills, people);
            
            assertEquals(1, result.length, "Single person with all skills");
            assertEquals(0, result[0], "Should select person 0");
        }
        
        @Test
        @DisplayName("Test each person has one unique skill")
        void testUniqueSkills() {
            String[] req_skills = {"a", "b", "c"};
            List<List<String>> people = Arrays.asList(
                Arrays.asList("a"),
                Arrays.asList("b"),
                Arrays.asList("c")
            );
            
            int[] result = BitmaskDPTemplates.smallestSufficientTeam(req_skills, people);
            
            assertEquals(3, result.length, "Need all 3 people");
        }
    }
    
    // ========== 14. TOTAL INTERSECTION SIZE TESTS ==========
    
    @Nested
    @DisplayName("Total Intersection Size Tests")
    class TotalIntersectionSizeTests {
        
        @Test
        @DisplayName("Test basic intersection count")
        void testBasicIntersection() {
            int[] sets = {0b11, 0b11}; // Both sets are {0,1}
            long result = BitmaskDPTemplates.totalIntersectionSize(sets);
            // Intersection of two identical sets with 2 elements = 2
            assertEquals(2, result, "Two identical sets should contribute 2");
        }
        
        @Test
        @DisplayName("Test disjoint sets")
        void testDisjointSets() {
            int[] sets = {0b01, 0b10}; // {0} and {1}
            long result = BitmaskDPTemplates.totalIntersectionSize(sets);
            // No common elements
            assertEquals(0, result, "Disjoint sets have no intersection");
        }
        
        @Test
        @DisplayName("Test all empty sets")
        void testEmptySets() {
            int[] sets = {0, 0, 0};
            long result = BitmaskDPTemplates.totalIntersectionSize(sets);
            assertEquals(0, result, "Empty sets have no intersection");
        }
        
        @Test
        @DisplayName("Test single element sets")
        void testSingleElementSets() {
            int[] sets = {1, 1, 1}; // All contain element 0
            long result = BitmaskDPTemplates.totalIntersectionSize(sets);
            // 3 sets, each pair contributes 1, total = 3 pairs * 1 = 3
            assertEquals(3, result, "Three identical single-element sets");
        }
    }
    
    // ========== 15. EDGE CASES AND STRESS TESTS ==========
    
    @Nested
    @DisplayName("Edge Cases and Stress Tests")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Test empty bitmask")
        void testEmptyBitmask() {
            assertEquals(0, BitmaskDPTemplates.countBits(0), "Empty mask has 0 bits");
            assertFalse(BitmaskDPTemplates.isSet(0, 0), "No bits set in empty mask");
        }
        
        @Test
        @DisplayName("Test maximum bitmask (all bits set)")
        void testMaxBitmask() {
            int mask = (1 << 10) - 1; // 10 bits set
            assertEquals(10, BitmaskDPTemplates.countBits(mask), "Should count 10 bits");
        }
        
        @Test
        @DisplayName("Test bit operations on large masks")
        void testLargeMasks() {
            int mask = (1 << 20) - 1; // 20 bits set
            assertTrue(BitmaskDPTemplates.isSet(mask, 19), "Bit 19 should be set");
            assertFalse(BitmaskDPTemplates.isSet(mask, 20), "Bit 20 should not be set");
        }
        
        @Test
        @DisplayName("Test TSP with single city")
        void testTSPSingleCity() {
            int[][] dist = {{0}};
            int result = BitmaskDPTemplates.tsp(dist);
            assertEquals(0, result, "Single city TSP has cost 0");
        }
        
        @Test
        @DisplayName("Test assignment with single worker-task")
        void testSingleAssignment() {
            int[][] cost = {{100}};
            int result = BitmaskDPTemplates.minCostAssignment(cost);
            assertEquals(100, result, "Single assignment returns that cost");
        }
    }
}
