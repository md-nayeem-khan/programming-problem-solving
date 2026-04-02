package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for CombinatoricsTemplates
 * 
 * Tests all combinatorial algorithms including:
 * - FactorialMod (factorials, combinations, permutations)
 * - LucasTheorem (large n,k combinations)
 * - CatalanNumbers (all 4 computation methods)
 * - StirlingNumbers (first and second kind)
 * - Derangements (permutations with no fixed points)
 * - BinomialCoefficients (multiple methods)
 * - MultinomialCoefficients
 */
@DisplayName("CombinatoricsTemplates Validation Tests")
class CombinatoricsTemplatesTest {
    
    private static final int MOD = 1_000_000_007;
    private static final double EPSILON = 1e-9;
    
    @Nested
    @DisplayName("FactorialMod Tests")
    class FactorialModTests {
        
        private CombinatoricsTemplates.FactorialMod fm;
        
        @BeforeEach
        void setUp() {
            fm = new CombinatoricsTemplates.FactorialMod(20, MOD);
        }
        
        @Test
        @DisplayName("Factorial calculation")
        void testFactorial() {
            assertEquals(1, fm.factorial(0));
            assertEquals(1, fm.factorial(1));
            assertEquals(2, fm.factorial(2));
            assertEquals(6, fm.factorial(3));
            assertEquals(24, fm.factorial(4));
            assertEquals(120, fm.factorial(5));
            assertEquals(3628800, fm.factorial(10));
        }
        
        @ParameterizedTest
        @CsvSource({
            "10, 3, 120",
            "5, 2, 10",
            "10, 0, 1",
            "10, 10, 1",
            "15, 7, 6435",
            "20, 10, 184756"
        })
        @DisplayName("Combination nCr calculation")
        void testCombination(int n, int k, long expected) {
            assertEquals(expected, fm.nCr(n, k));
        }
        
        @ParameterizedTest
        @CsvSource({
            "10, 3, 720",
            "5, 2, 20",
            "10, 0, 1",
            "8, 5, 6720",
            "7, 7, 5040"
        })
        @DisplayName("Permutation nPr calculation")
        void testPermutation(int n, int k, long expected) {
            assertEquals(expected, fm.nPr(n, k));
        }
        
        @Test
        @DisplayName("Edge cases")
        void testEdgeCases() {
            // Invalid k values
            assertEquals(0, fm.nCr(5, -1));
            assertEquals(0, fm.nCr(5, 6));
            assertEquals(0, fm.nPr(5, -1));
            assertEquals(0, fm.nPr(5, 6));
            
            // Boundary cases
            assertEquals(1, fm.nCr(0, 0));
            assertEquals(1, fm.nPr(0, 0));
        }
        
        @Test
        @DisplayName("Modular arithmetic verification")
        void testModularArithmetic() {
            CombinatoricsTemplates.FactorialMod smallMod = 
                new CombinatoricsTemplates.FactorialMod(15, 97);
            
            // Verify results are properly reduced modulo p
            long result = smallMod.nCr(15, 7);
            assertTrue(result >= 0 && result < 97);
        }
        
        @Test
        @DisplayName("Mathematical identity: C(n,k) = C(n,n-k)")
        void testCombinationSymmetry() {
            for (int n = 0; n <= 20; n++) {
                for (int k = 0; k <= n; k++) {
                    assertEquals(fm.nCr(n, k), fm.nCr(n, n - k),
                        "C(" + n + "," + k + ") should equal C(" + n + "," + (n-k) + ")");
                }
            }
        }
    }
    
    @Nested
    @DisplayName("LucasTheorem Tests")
    class LucasTheoremTests {
        
        private CombinatoricsTemplates.LucasTheorem lt;
        
        @BeforeEach
        void setUp() {
            lt = new CombinatoricsTemplates.LucasTheorem(1000003);
        }
        
        @Test
        @DisplayName("Small values consistency with regular computation")
        void testSmallValues() {
            CombinatoricsTemplates.FactorialMod fm = 
                new CombinatoricsTemplates.FactorialMod(100, 1000003);
            
            for (int n = 0; n <= 50; n++) {
                for (int k = 0; k <= Math.min(n, 20); k++) {
                    assertEquals(fm.nCr(n, k), lt.nCrModP(n, k),
                        "Lucas theorem should match regular computation for C(" + n + "," + k + ")");
                }
            }
        }
        
        @Test
        @DisplayName("Large values computation")
        void testLargeValues() {
            // Test specific large combinations
            assertNotNull(lt.nCrModP(1000000, 500000));
            assertNotNull(lt.nCrModP(123456789L, 987654321L));
            
            // Result should be within modulus
            long result = lt.nCrModP(1000000, 500000);
            assertTrue(result >= 0 && result < 1000003);
        }
        
        @Test
        @DisplayName("Edge cases")
        void testEdgeCases() {
            assertEquals(0, lt.nCrModP(5, 6)); // k > n
            assertEquals(1, lt.nCrModP(100, 0)); // k = 0
            assertEquals(1, lt.nCrModP(100, 100)); // k = n
        }
        
        @Test
        @DisplayName("Different prime modulus")
        void testDifferentPrimes() {
            CombinatoricsTemplates.LucasTheorem lt7 = 
                new CombinatoricsTemplates.LucasTheorem(7);
            
            // Test with small prime
            long result = lt7.nCrModP(10, 3);
            assertTrue(result >= 0 && result < 7);
        }
    }
    
    @Nested
    @DisplayName("CatalanNumbers Tests")
    class CatalanNumbersTests {
        
        // Known Catalan numbers: C_0=1, C_1=1, C_2=2, C_3=5, C_4=14, C_5=42, ...
        private final long[] expectedCatalan = {1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, 16796};
        
        @Test
        @DisplayName("Catalan DP method")
        void testCatalanDP() {
            long[] catalan = CombinatoricsTemplates.CatalanNumbers.catalanDP(10, MOD);
            
            for (int i = 0; i <= 10; i++) {
                assertEquals(expectedCatalan[i], catalan[i],
                    "Catalan DP C_" + i + " should be " + expectedCatalan[i]);
            }
        }
        
        @Test
        @DisplayName("Catalan factorial method")
        void testCatalanFactorial() {
            long[] catalan = CombinatoricsTemplates.CatalanNumbers.catalanFactorial(10);
            
            for (int i = 0; i <= 10; i++) {
                assertEquals(expectedCatalan[i], catalan[i],
                    "Catalan factorial C_" + i + " should be " + expectedCatalan[i]);
            }
        }
        
        @Test
        @DisplayName("Catalan recurrence method")
        void testCatalanRecurrence() {
            long[] catalan = CombinatoricsTemplates.CatalanNumbers.catalanRecurrence(10, MOD);
            
            for (int i = 0; i <= 10; i++) {
                assertEquals(expectedCatalan[i], catalan[i],
                    "Catalan recurrence C_" + i + " should be " + expectedCatalan[i]);
            }
        }
        
        @Test
        @DisplayName("Catalan modular method")
        void testCatalanMod() {
            for (int i = 0; i <= 10; i++) {
                long result = CombinatoricsTemplates.CatalanNumbers.catalanMod(i, MOD);
                assertEquals(expectedCatalan[i], result,
                    "Catalan mod C_" + i + " should be " + expectedCatalan[i]);
            }
        }
        
        @Test
        @DisplayName("All methods consistency")
        void testAllMethodsConsistency() {
            long[] dp = CombinatoricsTemplates.CatalanNumbers.catalanDP(8, MOD);
            long[] factorial = CombinatoricsTemplates.CatalanNumbers.catalanFactorial(8);
            long[] recurrence = CombinatoricsTemplates.CatalanNumbers.catalanRecurrence(8, MOD);
            
            for (int i = 0; i <= 8; i++) {
                assertEquals(dp[i], factorial[i], "DP and factorial should match for C_" + i);
                assertEquals(dp[i], recurrence[i], "DP and recurrence should match for C_" + i);
                
                long mod = CombinatoricsTemplates.CatalanNumbers.catalanMod(i, MOD);
                assertEquals(dp[i], mod, "DP and mod should match for C_" + i);
            }
        }
    }
    
    @Nested
    @DisplayName("StirlingNumbers Tests")
    class StirlingNumbersTests {
        
        @Test
        @DisplayName("Stirling first kind calculation")
        void testStirlingFirst() {
            long[][] S1 = CombinatoricsTemplates.StirlingNumbers.stirlingFirst(6, MOD);
            
            // Base cases
            assertEquals(1, S1[0][0]);
            assertEquals(0, S1[1][0]);
            assertEquals(1, S1[1][1]);
            
            // Known values: S(4,2) = 11
            assertEquals(11, S1[4][2]);
            
            // Triangle property: S(n,0) = 0 for n > 0
            for (int n = 1; n <= 6; n++) {
                assertEquals(0, S1[n][0]);
            }
            
            // S(n,n) = 1
            for (int n = 0; n <= 6; n++) {
                assertEquals(1, S1[n][n]);
            }
        }
        
        @Test
        @DisplayName("Stirling second kind calculation")
        void testStirlingSecond() {
            long[][] S2 = CombinatoricsTemplates.StirlingNumbers.stirlingSecond(6, MOD);
            
            // Base cases
            assertEquals(1, S2[0][0]);
            assertEquals(0, S2[1][0]);
            assertEquals(1, S2[1][1]);
            
            // Known values
            assertEquals(1, S2[2][1]);
            assertEquals(1, S2[2][2]);
            assertEquals(1, S2[3][1]);
            assertEquals(3, S2[3][2]);
            assertEquals(1, S2[3][3]);
            assertEquals(7, S2[4][2]);
            assertEquals(15, S2[5][2]);
            
            // Triangle property: S(n,0) = 0 for n > 0
            for (int n = 1; n <= 6; n++) {
                assertEquals(0, S2[n][0]);
            }
            
            // S(n,n) = 1
            for (int n = 0; n <= 6; n++) {
                assertEquals(1, S2[n][n]);
            }
        }
        
        @Test
        @DisplayName("Stirling second kind single value")
        void testStirlingSecondSingle() {
            // Test inclusion-exclusion formula
            assertEquals(7, CombinatoricsTemplates.StirlingNumbers.stirlingSecondSingle(4, 2, MOD));
            assertEquals(25, CombinatoricsTemplates.StirlingNumbers.stirlingSecondSingle(5, 3, MOD));
            
            // Compare with DP approach
            long[][] S2 = CombinatoricsTemplates.StirlingNumbers.stirlingSecond(8, MOD);
            for (int n = 1; n <= 8; n++) {
                for (int k = 1; k <= n; k++) {
                    long single = CombinatoricsTemplates.StirlingNumbers.stirlingSecondSingle(n, k, MOD);
                    assertEquals(S2[n][k], single, 
                        "S(" + n + "," + k + ") should match between methods");
                }
            }
        }
    }
    
    @Nested
    @DisplayName("Derangements Tests")
    class DerangementsTests {
        
        // Known derangement values: D_0=1, D_1=0, D_2=1, D_3=2, D_4=9, D_5=44, ...
        private final long[] expectedDerangements = {1, 0, 1, 2, 9, 44, 265, 1854, 14833, 133496};
        
        @Test
        @DisplayName("Derangement calculation")
        void testDerangements() {
            long[] derang = CombinatoricsTemplates.Derangements.derangements(9, MOD);
            
            for (int i = 0; i <= 9; i++) {
                assertEquals(expectedDerangements[i], derang[i],
                    "D_" + i + " should be " + expectedDerangements[i]);
            }
        }
        
        @Test
        @DisplayName("Derangement probability")
        void testDerangementProbability() {
            assertEquals(1.0, CombinatoricsTemplates.Derangements.derangementProbability(0), EPSILON);
            assertEquals(0.0, CombinatoricsTemplates.Derangements.derangementProbability(1), EPSILON);
            assertEquals(0.5, CombinatoricsTemplates.Derangements.derangementProbability(2), EPSILON);
            
            // For large n, probability approaches 1/e ≈ 0.368
            double prob10 = CombinatoricsTemplates.Derangements.derangementProbability(10);
            assertTrue(Math.abs(prob10 - (1.0 / Math.E)) < 0.01,
                "Probability should approach 1/e for large n");
        }
        
        @Test
        @DisplayName("Recurrence relation verification")
        void testRecurrenceRelation() {
            long[] derang = CombinatoricsTemplates.Derangements.derangements(10, MOD);
            
            // Verify D_n = (n-1) * (D_{n-1} + D_{n-2})
            for (int n = 2; n <= 10; n++) {
                long expected = (n - 1) * (derang[n - 1] + derang[n - 2]);
                assertEquals(expected % MOD, derang[n],
                    "Recurrence relation should hold for D_" + n);
            }
        }
    }
    
    @Nested
    @DisplayName("BinomialCoefficients Tests")
    class BinomialCoefficientsTests {
        
        @Test
        @DisplayName("Pascal triangle")
        void testPascalTriangle() {
            long[][] pascal = CombinatoricsTemplates.BinomialCoefficients.pascalTriangle(10);
            
            // Row 0: 1
            assertEquals(1, pascal[0][0]);
            
            // Row 1: 1 1
            assertEquals(1, pascal[1][0]);
            assertEquals(1, pascal[1][1]);
            
            // Row 4: 1 4 6 4 1
            assertEquals(1, pascal[4][0]);
            assertEquals(4, pascal[4][1]);
            assertEquals(6, pascal[4][2]);
            assertEquals(4, pascal[4][3]);
            assertEquals(1, pascal[4][4]);
            
            // Symmetry property
            for (int n = 0; n <= 10; n++) {
                for (int k = 0; k <= n; k++) {
                    assertEquals(pascal[n][k], pascal[n][n - k],
                        "Pascal triangle should be symmetric");
                }
            }
        }
        
        @ParameterizedTest
        @CsvSource({
            "10, 3, 120",
            "20, 10, 184756",
            "5, 0, 1",
            "5, 5, 1",
            "8, 3, 56"
        })
        @DisplayName("nCr calculation")
        void testNCR(int n, int k, long expected) {
            assertEquals(expected, CombinatoricsTemplates.BinomialCoefficients.nCr(n, k));
        }
        
        @Test
        @DisplayName("nCr with modular arithmetic")
        void testNCRMod() {
            assertEquals(120, CombinatoricsTemplates.BinomialCoefficients.nCrMod(10, 3, MOD));
            assertEquals(0, CombinatoricsTemplates.BinomialCoefficients.nCrMod(5, 6, MOD));
        }
        
        @Test
        @DisplayName("Consistency between methods")
        void testConsistencyBetweenMethods() {
            long[][] pascal = CombinatoricsTemplates.BinomialCoefficients.pascalTriangle(15);
            
            for (int n = 0; n <= 15; n++) {
                for (int k = 0; k <= n; k++) {
                    long directNCR = CombinatoricsTemplates.BinomialCoefficients.nCr(n, k);
                    long modNCR = CombinatoricsTemplates.BinomialCoefficients.nCrMod(n, k, MOD);
                    
                    assertEquals(pascal[n][k], directNCR,
                        "Pascal and direct nCr should match for C(" + n + "," + k + ")");
                    assertEquals(pascal[n][k], modNCR,
                        "Pascal and modular nCr should match for C(" + n + "," + k + ")");
                }
            }
        }
    }
    
    @Nested
    @DisplayName("MultinomialCoefficients Tests")
    class MultinomialCoefficientsTests {
        
        @Test
        @DisplayName("Basic multinomial calculation")
        void testMultinomial() {
            // M(10; 3,2,5) = 10!/(3!*2!*5!) = 2520
            int[] groups1 = {3, 2, 5};
            assertEquals(2520, CombinatoricsTemplates.MultinomialCoefficients.multinomial(groups1, MOD));
            
            // M(6; 2,2,2) = 6!/(2!*2!*2!) = 90
            int[] groups2 = {2, 2, 2};
            assertEquals(90, CombinatoricsTemplates.MultinomialCoefficients.multinomial(groups2, MOD));
            
            // M(4; 4) = 4!/4! = 1
            int[] groups3 = {4};
            assertEquals(1, CombinatoricsTemplates.MultinomialCoefficients.multinomial(groups3, MOD));
        }
        
        @Test
        @DisplayName("Reduction to binomial")
        void testReductionToBinomial() {
            // When there are only 2 groups, multinomial reduces to binomial
            int[] groups = {3, 7};
            long multinomial = CombinatoricsTemplates.MultinomialCoefficients.multinomial(groups, MOD);
            long binomial = CombinatoricsTemplates.BinomialCoefficients.nCrMod(10, 3, MOD);
            
            assertEquals(binomial, multinomial,
                "Multinomial with 2 groups should equal binomial coefficient");
        }
        
        @Test
        @DisplayName("Symmetry property")
        void testSymmetry() {
            // Multinomial should be same regardless of group order
            int[] groups1 = {2, 3, 5};
            int[] groups2 = {5, 2, 3};
            int[] groups3 = {3, 5, 2};
            
            long result1 = CombinatoricsTemplates.MultinomialCoefficients.multinomial(groups1, MOD);
            long result2 = CombinatoricsTemplates.MultinomialCoefficients.multinomial(groups2, MOD);
            long result3 = CombinatoricsTemplates.MultinomialCoefficients.multinomial(groups3, MOD);
            
            assertEquals(result1, result2);
            assertEquals(result2, result3);
        }
    }
    
    @Nested
    @DisplayName("Edge Cases and Mathematical Properties")
    class EdgeCasesAndPropertiesTests {
        
        @Test
        @DisplayName("Zero and one inputs")
        void testZeroAndOneInputs() {
            CombinatoricsTemplates.FactorialMod fm = 
                new CombinatoricsTemplates.FactorialMod(5, MOD);
            
            // Test with n=0, k=0
            assertEquals(1, fm.nCr(0, 0));
            assertEquals(1, fm.nPr(0, 0));
            assertEquals(1, fm.factorial(0));
            
            // Test with n=1
            assertEquals(1, fm.nCr(1, 0));
            assertEquals(1, fm.nCr(1, 1));
            assertEquals(1, fm.nPr(1, 1));
        }
        
        @Test
        @DisplayName("Negative inputs handling")
        void testNegativeInputs() {
            CombinatoricsTemplates.FactorialMod fm = 
                new CombinatoricsTemplates.FactorialMod(10, MOD);
            
            assertEquals(0, fm.nCr(5, -1));
            assertEquals(0, fm.nPr(5, -1));
        }
        
        @Test
        @DisplayName("Pascal identity: C(n,k) = C(n-1,k-1) + C(n-1,k)")
        void testPascalIdentity() {
            CombinatoricsTemplates.FactorialMod fm = 
                new CombinatoricsTemplates.FactorialMod(20, MOD);
            
            for (int n = 1; n <= 20; n++) {
                for (int k = 1; k < n; k++) {
                    long left = fm.nCr(n, k);
                    long right = (fm.nCr(n - 1, k - 1) + fm.nCr(n - 1, k)) % MOD;
                    
                    assertEquals(left, right,
                        "Pascal identity should hold for C(" + n + "," + k + ")");
                }
            }
        }
        
        @Test
        @DisplayName("Vandermonde identity")
        void testVandermondeIdentity() {
            // Sum_{k=0}^{r} C(m,k) * C(n,r-k) = C(m+n,r)
            CombinatoricsTemplates.FactorialMod fm = 
                new CombinatoricsTemplates.FactorialMod(30, MOD);
            
            int m = 10, n = 12, r = 8;
            long sum = 0;
            
            for (int k = 0; k <= r; k++) {
                sum = (sum + fm.nCr(m, k) * fm.nCr(n, r - k)) % MOD;
            }
            
            assertEquals(fm.nCr(m + n, r), sum,
                "Vandermonde identity should hold");
        }
        
        @Test
        @DisplayName("Catalan recurrence identity")
        void testCatalanRecurrenceIdentity() {
            // C_n = Sum_{i=0}^{n-1} C_i * C_{n-1-i}
            long[] catalan = CombinatoricsTemplates.CatalanNumbers.catalanDP(10, MOD);
            
            for (int n = 1; n <= 10; n++) {
                long sum = 0;
                for (int i = 0; i < n; i++) {
                    sum = (sum + catalan[i] * catalan[n - 1 - i]) % MOD;
                }
                assertEquals(catalan[n], sum,
                    "Catalan recurrence identity should hold for C_" + n);
            }
        }
    }
    
    @Test
    @DisplayName("Performance benchmark test")
    void testPerformance() {
        // Test that large computations complete in reasonable time
        long startTime = System.currentTimeMillis();
        
        // Large factorial mod
        CombinatoricsTemplates.FactorialMod fm = 
            new CombinatoricsTemplates.FactorialMod(10000, MOD);
        fm.nCr(10000, 5000);
        
        // Lucas theorem with large values
        CombinatoricsTemplates.LucasTheorem lt = 
            new CombinatoricsTemplates.LucasTheorem(1000003);
        lt.nCrModP(1000000L, 500000L);
        
        // Large Catalan numbers
        CombinatoricsTemplates.CatalanNumbers.catalanDP(1000, MOD);
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Should complete within 5 seconds
        assertTrue(duration < 5000, "Performance test should complete within 5 seconds, took " + duration + "ms");
    }
}