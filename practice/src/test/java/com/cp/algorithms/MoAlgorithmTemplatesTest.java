package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Comprehensive test suite for Mo's Algorithm Templates
 * Tests all implementations with various edge cases and scenarios
 */
public class MoAlgorithmTemplatesTest {

    // ========== DISTINCT ELEMENTS IN RANGE TESTS ==========
    
    @Nested
    @DisplayName("Distinct Elements in Range Tests")
    class DistinctInRangeTests {
        
        @Test
        @DisplayName("Basic distinct elements test")
        void testBasicDistinct() {
            int[] arr = {1, 2, 1, 3, 2, 4, 3, 5};
            int[][] queries = {
                {0, 3},  // [1,2,1,3] -> 3 distinct
                {2, 5},  // [1,3,2,4] -> 4 distinct
                {0, 7},  // [1,2,1,3,2,4,3,5] -> 5 distinct
                {1, 4}   // [2,1,3,2] -> 3 distinct
            };
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertArrayEquals(new int[]{3, 4, 5, 3}, answers);
        }
        
        @Test
        @DisplayName("Single element")
        void testSingleElement() {
            int[] arr = {5};
            int[][] queries = {{0, 0}};
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertArrayEquals(new int[]{1}, answers);
        }
        
        @Test
        @DisplayName("All same elements")
        void testAllSame() {
            int[] arr = {5, 5, 5, 5, 5};
            int[][] queries = {
                {0, 4},
                {1, 3},
                {2, 2}
            };
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertArrayEquals(new int[]{1, 1, 1}, answers);
        }
        
        @Test
        @DisplayName("All distinct elements")
        void testAllDistinct() {
            int[] arr = {1, 2, 3, 4, 5};
            int[][] queries = {
                {0, 4},
                {1, 3},
                {0, 2}
            };
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertArrayEquals(new int[]{5, 3, 3}, answers);
        }
        
        @Test
        @DisplayName("Overlapping queries")
        void testOverlappingQueries() {
            int[] arr = {1, 2, 3, 2, 1};
            int[][] queries = {
                {0, 2},  // [1,2,3] -> 3 distinct
                {1, 3},  // [2,3,2] -> 2 distinct (2 and 3)
                {2, 4},  // [3,2,1] -> 3 distinct
                {0, 4}   // [1,2,3,2,1] -> 3 distinct
            };
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertArrayEquals(new int[]{3, 2, 3, 3}, answers);
        }
        
        @Test
        @DisplayName("Large values")
        void testLargeValues() {
            int[] arr = {1000, 2000, 1000, 3000};
            int[][] queries = {
                {0, 3},
                {1, 2}
            };
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertArrayEquals(new int[]{3, 2}, answers);
        }
    }
    
    // ========== RANGE MODE TESTS ==========
    
    @Nested
    @DisplayName("Range Mode (Most Frequent Element) Tests")
    class RangeModeTests {
        
        @Test
        @DisplayName("Basic range mode test")
        void testBasicMode() {
            int[] arr = {1, 2, 2, 3, 3, 3, 4, 4, 4, 4};
            int[][] queries = {
                {0, 3},  // [1,2,2,3] -> 2 appears twice
                {3, 6},  // [3,3,3,4] -> 3 appears thrice
                {6, 9}   // [4,4,4,4] -> 4 appears four times
            };
            
            MoAlgorithmTemplates.RangeMode rm = 
                new MoAlgorithmTemplates.RangeMode(arr);
            int[] answers = rm.processQueries(queries);
            
            assertArrayEquals(new int[]{2, 3, 4}, answers);
        }
        
        @Test
        @DisplayName("Single element has frequency 1")
        void testSingleElement() {
            int[] arr = {5};
            int[][] queries = {{0, 0}};
            
            MoAlgorithmTemplates.RangeMode rm = 
                new MoAlgorithmTemplates.RangeMode(arr);
            int[] answers = rm.processQueries(queries);
            
            assertArrayEquals(new int[]{1}, answers);
        }
        
        @Test
        @DisplayName("All elements appear once")
        void testAllDistinct() {
            int[] arr = {1, 2, 3, 4, 5};
            int[][] queries = {
                {0, 4},
                {1, 3}
            };
            
            MoAlgorithmTemplates.RangeMode rm = 
                new MoAlgorithmTemplates.RangeMode(arr);
            int[] answers = rm.processQueries(queries);
            
            assertArrayEquals(new int[]{1, 1}, answers);
        }
        
        @Test
        @DisplayName("Increasing frequencies")
        void testIncreasingFrequencies() {
            int[] arr = {1, 2, 2, 3, 3, 3};
            int[][] queries = {
                {0, 0},  // 1 appears once
                {0, 2},  // 2 appears twice
                {0, 5}   // 3 appears thrice
            };
            
            MoAlgorithmTemplates.RangeMode rm = 
                new MoAlgorithmTemplates.RangeMode(arr);
            int[] answers = rm.processQueries(queries);
            
            assertArrayEquals(new int[]{1, 2, 3}, answers);
        }
        
        @Test
        @DisplayName("Frequency changes across ranges")
        void testFrequencyChanges() {
            int[] arr = {1, 1, 2, 2, 1, 1};
            int[][] queries = {
                {0, 3},  // [1,1,2,2] -> max freq 2
                {2, 5},  // [2,2,1,1] -> max freq 2
                {0, 5}   // [1,1,2,2,1,1] -> max freq 4
            };
            
            MoAlgorithmTemplates.RangeMode rm = 
                new MoAlgorithmTemplates.RangeMode(arr);
            int[] answers = rm.processQueries(queries);
            
            assertArrayEquals(new int[]{2, 2, 4}, answers);
        }
    }
    
    // ========== RANGE XOR TESTS ==========
    
    @Nested
    @DisplayName("Range XOR Tests")
    class RangeXORTests {
        
        @Test
        @DisplayName("Basic XOR test")
        void testBasicXOR() {
            int[] arr = {1, 2, 3, 4, 5};
            int[][] queries = {
                {0, 2},  // 1 XOR 2 XOR 3 = 0
                {1, 4},  // 2 XOR 3 XOR 4 XOR 5 = 0 (2^3=1, 1^4=5, 5^5=0)
                {0, 4}   // 1 XOR 2 XOR 3 XOR 4 XOR 5 = 1
            };
            
            MoAlgorithmTemplates.RangeXOR rx = 
                new MoAlgorithmTemplates.RangeXOR(arr);
            int[] answers = rx.processQueries(queries);
            
            assertArrayEquals(new int[]{0, 0, 1}, answers);
        }
        
        @Test
        @DisplayName("Single element XOR")
        void testSingleElement() {
            int[] arr = {7};
            int[][] queries = {{0, 0}};
            
            MoAlgorithmTemplates.RangeXOR rx = 
                new MoAlgorithmTemplates.RangeXOR(arr);
            int[] answers = rx.processQueries(queries);
            
            assertArrayEquals(new int[]{7}, answers);
        }
        
        @Test
        @DisplayName("Same elements XOR to 0")
        void testSameElements() {
            int[] arr = {5, 5};
            int[][] queries = {{0, 1}};
            
            MoAlgorithmTemplates.RangeXOR rx = 
                new MoAlgorithmTemplates.RangeXOR(arr);
            int[] answers = rx.processQueries(queries);
            
            assertArrayEquals(new int[]{0}, answers);
        }
        
        @Test
        @DisplayName("XOR properties test")
        void testXORProperties() {
            int[] arr = {10, 20, 30, 40};
            int[][] queries = {
                {0, 1},  // 10 XOR 20
                {2, 3},  // 30 XOR 40
                {0, 3}   // (10 XOR 20) XOR (30 XOR 40)
            };
            
            MoAlgorithmTemplates.RangeXOR rx = 
                new MoAlgorithmTemplates.RangeXOR(arr);
            int[] answers = rx.processQueries(queries);
            
            // Verify XOR property: (a XOR b) XOR (c XOR d) = a XOR b XOR c XOR d
            assertEquals(answers[0] ^ answers[1], answers[2]);
        }
        
        @Test
        @DisplayName("All zeros")
        void testAllZeros() {
            int[] arr = {0, 0, 0, 0};
            int[][] queries = {
                {0, 3},
                {1, 2}
            };
            
            MoAlgorithmTemplates.RangeXOR rx = 
                new MoAlgorithmTemplates.RangeXOR(arr);
            int[] answers = rx.processQueries(queries);
            
            assertArrayEquals(new int[]{0, 0}, answers);
        }
    }
    
    // ========== RANGE SUM OF SQUARES TESTS ==========
    
    @Nested
    @DisplayName("Range Sum of Squares Tests")
    class RangeSumOfSquaresTests {
        
        @Test
        @DisplayName("Basic sum of squares test")
        void testBasicSumOfSquares() {
            int[] arr = {1, 2, 3, 4, 5};
            int[][] queries = {
                {0, 2},  // 1² + 2² + 3² = 14
                {1, 4},  // 2² + 3² + 4² + 5² = 54
                {0, 4}   // 1² + 2² + 3² + 4² + 5² = 55
            };
            
            MoAlgorithmTemplates.RangeSumOfSquares rss = 
                new MoAlgorithmTemplates.RangeSumOfSquares(arr);
            int[] answers = rss.processQueries(queries);
            
            assertArrayEquals(new int[]{14, 54, 55}, answers);
        }
        
        @Test
        @DisplayName("Single element square")
        void testSingleElement() {
            int[] arr = {3};
            int[][] queries = {{0, 0}};
            
            MoAlgorithmTemplates.RangeSumOfSquares rss = 
                new MoAlgorithmTemplates.RangeSumOfSquares(arr);
            int[] answers = rss.processQueries(queries);
            
            assertArrayEquals(new int[]{9}, answers);
        }
        
        @Test
        @DisplayName("All zeros")
        void testAllZeros() {
            int[] arr = {0, 0, 0};
            int[][] queries = {{0, 2}};
            
            MoAlgorithmTemplates.RangeSumOfSquares rss = 
                new MoAlgorithmTemplates.RangeSumOfSquares(arr);
            int[] answers = rss.processQueries(queries);
            
            assertArrayEquals(new int[]{0}, answers);
        }
        
        @Test
        @DisplayName("Negative numbers")
        void testNegativeNumbers() {
            int[] arr = {-2, 3, -4};
            int[][] queries = {
                {0, 0},  // (-2)² = 4
                {1, 1},  // 3² = 9
                {2, 2},  // (-4)² = 16
                {0, 2}   // 4 + 9 + 16 = 29
            };
            
            MoAlgorithmTemplates.RangeSumOfSquares rss = 
                new MoAlgorithmTemplates.RangeSumOfSquares(arr);
            int[] answers = rss.processQueries(queries);
            
            assertArrayEquals(new int[]{4, 9, 16, 29}, answers);
        }
        
        @Test
        @DisplayName("Large numbers")
        void testLargeNumbers() {
            int[] arr = {100, 200};
            int[][] queries = {
                {0, 0},  // 10000
                {1, 1},  // 40000
                {0, 1}   // 50000
            };
            
            MoAlgorithmTemplates.RangeSumOfSquares rss = 
                new MoAlgorithmTemplates.RangeSumOfSquares(arr);
            int[] answers = rss.processQueries(queries);
            
            assertArrayEquals(new int[]{10000, 40000, 50000}, answers);
        }
    }
    
    // ========== RANGE GCD TESTS ==========
    
    @Nested
    @DisplayName("Range GCD Tests")
    class RangeGCDTests {
        
        @Test
        @DisplayName("Basic GCD test")
        void testBasicGCD() {
            int[] arr = {6, 9, 12, 15};
            int[][] queries = {
                {0, 1},  // gcd(6, 9) = 3
                {0, 2},  // gcd(6, 9, 12) = 3
                {1, 3}   // gcd(9, 12, 15) = 3
            };
            
            MoAlgorithmTemplates.RangeGCD rg = 
                new MoAlgorithmTemplates.RangeGCD(arr);
            int[] answers = rg.processQueries(queries);
            
            assertArrayEquals(new int[]{3, 3, 3}, answers);
        }
        
        @Test
        @DisplayName("Single element")
        void testSingleElement() {
            int[] arr = {42};
            int[][] queries = {{0, 0}};
            
            MoAlgorithmTemplates.RangeGCD rg = 
                new MoAlgorithmTemplates.RangeGCD(arr);
            int[] answers = rg.processQueries(queries);
            
            assertArrayEquals(new int[]{42}, answers);
        }
        
        @Test
        @DisplayName("GCD of 1")
        void testGCDOne() {
            int[] arr = {2, 3, 5, 7};
            int[][] queries = {{0, 3}};
            
            MoAlgorithmTemplates.RangeGCD rg = 
                new MoAlgorithmTemplates.RangeGCD(arr);
            int[] answers = rg.processQueries(queries);
            
            assertArrayEquals(new int[]{1}, answers);
        }
        
        @Test
        @DisplayName("Same elements")
        void testSameElements() {
            int[] arr = {12, 12, 12};
            int[][] queries = {{0, 2}};
            
            MoAlgorithmTemplates.RangeGCD rg = 
                new MoAlgorithmTemplates.RangeGCD(arr);
            int[] answers = rg.processQueries(queries);
            
            assertArrayEquals(new int[]{12}, answers);
        }
        
        @Test
        @DisplayName("Powers of 2")
        void testPowersOf2() {
            int[] arr = {8, 16, 32, 64};
            int[][] queries = {
                {0, 1},  // gcd(8, 16) = 8
                {1, 3},  // gcd(16, 32, 64) = 16
                {0, 3}   // gcd(8, 16, 32, 64) = 8
            };
            
            MoAlgorithmTemplates.RangeGCD rg = 
                new MoAlgorithmTemplates.RangeGCD(arr);
            int[] answers = rg.processQueries(queries);
            
            assertArrayEquals(new int[]{8, 16, 8}, answers);
        }
        
        @Test
        @DisplayName("Large prime numbers")
        void testLargePrimes() {
            int[] arr = {17, 19, 23};
            int[][] queries = {
                {0, 0},  // 17
                {1, 1},  // 19
                {0, 2}   // gcd(17, 19, 23) = 1
            };
            
            MoAlgorithmTemplates.RangeGCD rg = 
                new MoAlgorithmTemplates.RangeGCD(arr);
            int[] answers = rg.processQueries(queries);
            
            assertArrayEquals(new int[]{17, 19, 1}, answers);
        }
    }
    
    // ========== QUERY SORTING TESTS ==========
    
    @Nested
    @DisplayName("Query Sorting and Order Tests")
    class QuerySortingTests {
        
        @Test
        @DisplayName("Queries in different blocks are sorted correctly")
        void testBlockSorting() {
            int[] arr = new int[100];
            for (int i = 0; i < 100; i++) arr[i] = i;
            
            // Create queries in different blocks
            int[][] queries = {
                {50, 60},  // Block 5
                {0, 10},   // Block 0
                {30, 40}   // Block 3
            };
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            // Should still get correct answers regardless of query order
            assertArrayEquals(new int[]{11, 11, 11}, answers);
        }
        
        @Test
        @DisplayName("Many overlapping queries")
        void testManyOverlappingQueries() {
            int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            int[][] queries = new int[20][2];
            
            // Create 20 random queries
            for (int i = 0; i < 20; i++) {
                queries[i][0] = i % 5;
                queries[i][1] = 5 + (i % 5);
            }
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            // Verify no crashes and reasonable answers
            assertEquals(20, answers.length);
            for (int ans : answers) {
                assertTrue(ans > 0 && ans <= 10);
            }
        }
    }
    
    // ========== EDGE CASES ==========
    
    @Nested
    @DisplayName("Edge Cases and Stress Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Empty queries array")
        void testEmptyQueries() {
            int[] arr = {1, 2, 3};
            int[][] queries = {};
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertEquals(0, answers.length);
        }
        
        @Test
        @DisplayName("Query with same left and right")
        void testPointQuery() {
            int[] arr = {5, 10, 15, 20};
            int[][] queries = {
                {0, 0},
                {2, 2},
                {3, 3}
            };
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertArrayEquals(new int[]{1, 1, 1}, answers);
        }
        
        @Test
        @DisplayName("Full array query")
        void testFullArrayQuery() {
            int[] arr = {1, 2, 3, 4, 5};
            int[][] queries = {{0, 4}};
            
            MoAlgorithmTemplates.RangeXOR rx = 
                new MoAlgorithmTemplates.RangeXOR(arr);
            int[] answers = rx.processQueries(queries);
            
            assertEquals(1, answers[0]);  // 1^2^3^4^5 = 1
        }
        
        @Test
        @DisplayName("Consecutive queries")
        void testConsecutiveQueries() {
            int[] arr = {1, 2, 3, 4, 5};
            int[][] queries = {
                {0, 0},
                {0, 1},
                {0, 2},
                {0, 3},
                {0, 4}
            };
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, answers);
        }
        
        @Test
        @DisplayName("Reverse consecutive queries")
        void testReverseConsecutiveQueries() {
            int[] arr = {1, 2, 3, 4, 5};
            int[][] queries = {
                {0, 4},
                {1, 4},
                {2, 4},
                {3, 4},
                {4, 4}
            };
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertArrayEquals(new int[]{5, 4, 3, 2, 1}, answers);
        }
        
        @Test
        @DisplayName("Multiple queries on same range")
        void testDuplicateQueries() {
            int[] arr = {1, 2, 3};
            int[][] queries = {
                {0, 2},
                {0, 2},
                {0, 2}
            };
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertArrayEquals(new int[]{3, 3, 3}, answers);
        }
    }
    
    // ========== PERFORMANCE VALIDATION ==========
    
    @Nested
    @DisplayName("Performance and Correctness Tests")
    class PerformanceTests {
        
        @Test
        @DisplayName("Medium size array with many queries")
        void testMediumSizeArray() {
            int n = 1000;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = i % 100;  // Values 0-99
            }
            
            int[][] queries = new int[100][2];
            for (int i = 0; i < 100; i++) {
                queries[i][0] = i * 10;
                queries[i][1] = Math.min(i * 10 + 50, n - 1);
            }
            
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] answers = dir.processQueries(queries);
            
            assertEquals(100, answers.length);
            for (int ans : answers) {
                assertTrue(ans >= 1 && ans <= 100);
            }
        }
        
        @Test
        @DisplayName("Verify all implementations produce consistent results")
        void testConsistencyAcrossImplementations() {
            int[] arr = {1, 2, 3, 4, 5};
            int[][] queries = {{0, 4}};
            
            // Test that all implementations run without errors
            MoAlgorithmTemplates.DistinctInRange dir = 
                new MoAlgorithmTemplates.DistinctInRange(arr);
            int[] ans1 = dir.processQueries(queries);
            
            MoAlgorithmTemplates.RangeMode rm = 
                new MoAlgorithmTemplates.RangeMode(arr);
            int[] ans2 = rm.processQueries(queries);
            
            MoAlgorithmTemplates.RangeXOR rx = 
                new MoAlgorithmTemplates.RangeXOR(arr);
            int[] ans3 = rx.processQueries(queries);
            
            MoAlgorithmTemplates.RangeSumOfSquares rss = 
                new MoAlgorithmTemplates.RangeSumOfSquares(arr);
            int[] ans4 = rss.processQueries(queries);
            
            MoAlgorithmTemplates.RangeGCD rg = 
                new MoAlgorithmTemplates.RangeGCD(arr);
            int[] ans5 = rg.processQueries(queries);
            
            // All should complete without errors
            assertNotNull(ans1);
            assertNotNull(ans2);
            assertNotNull(ans3);
            assertNotNull(ans4);
            assertNotNull(ans5);
        }
    }
}
