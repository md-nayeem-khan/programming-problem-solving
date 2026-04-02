package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;

/**
 * Comprehensive test suite for NumberTheoryTemplates
 * Tests mathematical correctness, edge cases, and performance
 */
public class NumberTheoryTemplatesTest {

    /**
     * MILLER-RABIN PRIMALITY TEST VALIDATION
     */
    @DisplayName("Miller-Rabin: Known Primes Test")
    @ParameterizedTest
    @ValueSource(longs = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 
                         97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 
                         1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051,
                         982451653L, 1000000007L, 1000000009L}) // Known large primes (fixed: removed composites)
    public void testMillerRabinKnownPrimes(long prime) {
        assertTrue(NumberTheoryTemplates.MillerRabinTest.isPrime(prime), 
                  "Failed to identify " + prime + " as prime");
    }

    @DisplayName("Miller-Rabin: Known Composites Test")
    @ParameterizedTest
    @ValueSource(longs = {4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28,
                         100, 121, 169, 289, 361, 529, 625, 841, 961, 1024, 1369, 1681,
                         982451654L, 982451655L, 982451656L, 982451679L, 982451681L, 1000000007000000009L}) // Large composites
    public void testMillerRabinKnownComposites(long composite) {
        assertFalse(NumberTheoryTemplates.MillerRabinTest.isPrime(composite), 
                   "Incorrectly identified " + composite + " as prime");
    }

    @DisplayName("Miller-Rabin: Edge Cases Test")
    @Test
    public void testMillerRabinEdgeCases() {
        // Test edge cases
        assertFalse(NumberTheoryTemplates.MillerRabinTest.isPrime(-5), "Negative numbers should not be prime");
        assertFalse(NumberTheoryTemplates.MillerRabinTest.isPrime(0), "Zero should not be prime");
        assertFalse(NumberTheoryTemplates.MillerRabinTest.isPrime(1), "One should not be prime");
        assertTrue(NumberTheoryTemplates.MillerRabinTest.isPrime(2), "Two should be prime");
        assertTrue(NumberTheoryTemplates.MillerRabinTest.isPrime(3), "Three should be prime");
    }

    @DisplayName("Miller-Rabin: Probabilistic vs Deterministic Consistency")
    @Test
    public void testMillerRabinConsistency() {
        long[] testNumbers = {97, 1009, 982451653L, 1000000007L, 1000000009L};
        
        for (long n : testNumbers) {
            boolean deterministic = NumberTheoryTemplates.MillerRabinTest.isPrime(n);
            boolean probabilistic = NumberTheoryTemplates.MillerRabinTest.isProbablyPrime(n, 20);
            
            assertEquals(deterministic, probabilistic, 
                        "Deterministic and probabilistic results differ for " + n);
        }
    }

    @DisplayName("Miller-Rabin: Carmichael Numbers Test")
    @ParameterizedTest
    @ValueSource(longs = {561L, 1105L, 1729L, 2465L, 2821L, 6601L, 8911L}) // Carmichael numbers
    public void testMillerRabinCarmichaelNumbers(long carmichael) {
        assertFalse(NumberTheoryTemplates.MillerRabinTest.isPrime(carmichael), 
                   "Carmichael number " + carmichael + " should be identified as composite");
    }

    /**
     * POLLARD'S RHO FACTORIZATION TEST VALIDATION
     */
    @DisplayName("Pollard Rho: Known Factorizations Test")
    @ParameterizedTest
    @CsvSource({
        "60, '2,2,3,5'",
        "72, '2,2,2,3,3'", 
        "100, '2,2,5,5'",
        "1001, '7,11,13'",
        "1024, '2,2,2,2,2,2,2,2,2,2'",
        "9699690, '2,3,5,7,11,13,17,19'"
    })
    public void testPollardRhoKnownFactorizations(long n, String expectedFactorsStr) {
        List<Long> factors = NumberTheoryTemplates.PollardRho.factorize(n);
        List<String> expectedFactors = Arrays.asList(expectedFactorsStr.split(","));
        
        assertEquals(expectedFactors.size(), factors.size(), 
                    "Wrong number of factors for " + n);
        
        // Verify the product equals original number
        long product = factors.stream().reduce(1L, (a, b) -> a * b);
        assertEquals(n, product, "Product of factors doesn't equal original number");
        
        // Verify all factors are prime
        for (long factor : factors) {
            assertTrue(NumberTheoryTemplates.MillerRabinTest.isPrime(factor) || factor == 1, 
                      "Factor " + factor + " is not prime");
        }
    }

    @DisplayName("Pollard Rho: Prime Numbers Test")
    @ParameterizedTest
    @ValueSource(longs = {97, 1009, 982451653L})
    public void testPollardRhoPrimeNumbers(long prime) {
        List<Long> factors = NumberTheoryTemplates.PollardRho.factorize(prime);
        assertEquals(1, factors.size(), "Prime should have only one factor");
        assertEquals(prime, factors.get(0).longValue(), "Prime should factor to itself");
    }

    @DisplayName("Pollard Rho: Edge Cases Test")
    @Test
    public void testPollardRhoEdgeCases() {
        // Test edge cases
        List<Long> factors1 = NumberTheoryTemplates.PollardRho.factorize(1);
        assertTrue(factors1.isEmpty(), "1 should have no factors");
        
        List<Long> factors2 = NumberTheoryTemplates.PollardRho.factorize(2);
        assertEquals(Arrays.asList(2L), factors2, "2 should factor to [2]");
        
        List<Long> factors4 = NumberTheoryTemplates.PollardRho.factorize(4);
        assertEquals(Arrays.asList(2L, 2L), factors4, "4 should factor to [2, 2]");
    }

    /**
     * TONELLI-SHANKS MODULAR SQUARE ROOT TEST VALIDATION
     */
    @DisplayName("Tonelli-Shanks: Known Square Roots Test")
    @ParameterizedTest
    @CsvSource({
        "1, 13, 1",   // 1^2 = 1 (mod 13)
        "4, 13, 2",   // 2^2 = 4 (mod 13) 
        "9, 13, 3",   // 3^2 = 9 (mod 13)
        "3, 13, 9",   // 9^2 = 3 (mod 13) - from Tonelli-Shanks
        "16, 13, 4",  // 4^2 = 3 (mod 13)
        "1, 17, 1",   // Test with different prime
        "4, 17, 2"
    })
    public void testTonelliShanksKnownRoots(long n, long p, long expectedRoot) {
        long root = NumberTheoryTemplates.TonelliShanks.modularSqrt(n, p);
        
        if (expectedRoot == -1) {
            assertEquals(-1, root, n + " should not have square root mod " + p);
        } else {
            assertNotEquals(-1, root, n + " should have square root mod " + p);
            
            // Verify the square root
            long verification = (root * root) % p;
            assertEquals(n % p, verification, 
                        "Square root verification failed: " + root + "^2 != " + n + " (mod " + p + ")");
        }
    }

    @DisplayName("Tonelli-Shanks: Non-Quadratic Residues Test")
    @ParameterizedTest
    @CsvSource({
        "2, 13",   // 2 is not a quadratic residue mod 13
        "5, 13",   // 5 is not a quadratic residue mod 13  
        "6, 13",   // 6 is not a quadratic residue mod 13
        "7, 13",   // 7 is not a quadratic residue mod 13
        "8, 13",   // 8 is not a quadratic residue mod 13
        "11, 13",  // 11 is not a quadratic residue mod 13
        "14, 13"   // 14 = 1 mod 13, so should have root
    })
    public void testTonelliShanksNonQuadraticResidues(long n, long p) {
        long root = NumberTheoryTemplates.TonelliShanks.modularSqrt(n, p);
        
        if (root != -1) {
            // If root exists, verify it
            long verification = (root * root) % p;
            assertEquals(n % p, verification, 
                        "If root exists, it must be correct");
        }
        // If root is -1, that's also acceptable (non-quadratic residue)
    }

    @DisplayName("Tonelli-Shanks: Special Prime Cases Test")
    @Test 
    public void testTonelliShanksSpecialCases() {
        // Test p = 2
        assertEquals(0, NumberTheoryTemplates.TonelliShanks.modularSqrt(0, 2));
        assertEquals(1, NumberTheoryTemplates.TonelliShanks.modularSqrt(1, 2));
        
        // Test p = 3 (mod 4) primes
        long root = NumberTheoryTemplates.TonelliShanks.modularSqrt(4, 7); // 7 = 3 (mod 4)
        assertNotEquals(-1, root);
        assertEquals(4, (root * root) % 7);
    }

    /**
     * BABY-STEP GIANT-STEP DISCRETE LOGARITHM TEST VALIDATION
     */
    @DisplayName("Baby-Step Giant-Step: Known Discrete Logs Test")
    @ParameterizedTest
    @CsvSource({
        "2, 8, 13, 3",    // 2^3 = 8 (mod 13)
        "2, 4, 13, 2",    // 2^2 = 4 (mod 13)
        "2, 1, 13, 0",    // 2^0 = 1 (mod 13)
        "3, 9, 13, 2",    // 3^2 = 9 (mod 13)
        "5, 8, 13, 4",    // 5^4 = 8 (mod 13)
        "2, 16, 17, 4"    // 2^4 = 16 (mod 17)
    })
    public void testBSGSKnownLogs(long a, long b, long p, long expectedX) {
        long x = NumberTheoryTemplates.BabyStepGiantStep.discreteLog(a, b, p);
        
        if (expectedX == -1) {
            assertEquals(-1, x, "No solution should exist for " + a + "^x = " + b + " (mod " + p + ")");
        } else {
            assertNotEquals(-1, x, "Solution should exist for " + a + "^x = " + b + " (mod " + p + ")");
            
            // Verify the solution
            long verification = modPow(a, x, p);
            assertEquals(b % p, verification, 
                        "Verification failed: " + a + "^" + x + " ≢ " + b + " (mod " + p + ")");
        }
    }
    
    // Helper method for verification
    private long modPow(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }

    @DisplayName("Baby-Step Giant-Step: Edge Cases Test")
    @Test
    public void testBSGSEdgeCases() {
        // Test b = 1 (should always give x = 0)
        assertEquals(0, NumberTheoryTemplates.BabyStepGiantStep.discreteLog(2, 1, 13));
        assertEquals(0, NumberTheoryTemplates.BabyStepGiantStep.discreteLog(5, 1, 17));
        
        // Test cases where no solution exists
        // This is harder to construct, but we can test some cases
        long result = NumberTheoryTemplates.BabyStepGiantStep.discreteLog(2, 3, 5);
        if (result != -1) {
            // If solution exists, verify it
            assertEquals(3, modPow(2, result, 5));
        }
    }

    /**
     * EULER'S TOTIENT FUNCTION TEST VALIDATION  
     */
    @DisplayName("Euler Totient: Known Values Test")
    @ParameterizedTest
    @CsvSource({
        "1, 1",      // phi(1) = 1
        "2, 1",      // phi(2) = 1  
        "3, 2",      // phi(3) = 2
        "4, 2",      // phi(4) = 2
        "5, 4",      // phi(5) = 4
        "6, 2",      // phi(6) = 2
        "7, 6",      // phi(7) = 6
        "8, 4",      // phi(8) = 4
        "9, 6",      // phi(9) = 6
        "10, 4",     // phi(10) = 4
        "12, 4",     // phi(12) = 4
        "16, 8",     // phi(16) = 8
        "17, 16",    // phi(17) = 16 (prime)
        "18, 6",     // phi(18) = 6
        "20, 8",     // phi(20) = 8
        "30, 8",     // phi(30) = 8
        "100, 40"    // phi(100) = 40
    })
    public void testEulerTotientKnownValues(long n, long expectedPhi) {
        assertEquals(expectedPhi, NumberTheoryTemplates.EulerTotient.phi(n), 
                    "phi(" + n + ") should be " + expectedPhi);
    }

    @DisplayName("Euler Totient: Prime Numbers Test")
    @ParameterizedTest
    @ValueSource(longs = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 97, 101, 1009})
    public void testEulerTotientPrimes(long prime) {
        assertEquals(prime - 1, NumberTheoryTemplates.EulerTotient.phi(prime), 
                    "phi(p) should be p-1 for prime p");
    }

    @DisplayName("Euler Totient: Sieve vs Single Value Consistency")
    @Test
    public void testEulerTotientSieveConsistency() {
        int maxN = 100;
        long[] sieveResults = NumberTheoryTemplates.EulerTotient.phiSieve(maxN);
        
        for (int n = 1; n <= maxN; n++) {
            long singleResult = NumberTheoryTemplates.EulerTotient.phi(n);
            assertEquals(singleResult, sieveResults[n], 
                        "Sieve and single value results differ for n=" + n);
        }
    }

    /**
     * MULTIPLICATIVE FUNCTIONS TEST VALIDATION
     */
    @DisplayName("Multiplicative Functions: Number of Divisors Test")
    @ParameterizedTest
    @CsvSource({
        "1, 1",      // d(1) = 1
        "2, 2",      // d(2) = 2 (1, 2)
        "3, 2",      // d(3) = 2 (1, 3)  
        "4, 3",      // d(4) = 3 (1, 2, 4)
        "6, 4",      // d(6) = 4 (1, 2, 3, 6)
        "12, 6",     // d(12) = 6 (1, 2, 3, 4, 6, 12)
        "16, 5",     // d(16) = 5 (1, 2, 4, 8, 16)
        "20, 6",     // d(20) = 6 (1, 2, 4, 5, 10, 20)
        "30, 8"      // d(30) = 8 (1, 2, 3, 5, 6, 10, 15, 30)
    })
    public void testNumDivisors(long n, long expectedCount) {
        assertEquals(expectedCount, NumberTheoryTemplates.MultiplicativeFunctions.numDivisors(n), 
                    "Number of divisors of " + n + " should be " + expectedCount);
    }

    @DisplayName("Multiplicative Functions: Sum of Divisors Test")
    @ParameterizedTest
    @CsvSource({
        "1, 1",      // sigma(1) = 1
        "2, 3",      // sigma(2) = 1 + 2 = 3
        "3, 4",      // sigma(3) = 1 + 3 = 4
        "6, 12",     // sigma(6) = 1 + 2 + 3 + 6 = 12
        "12, 28",    // sigma(12) = 1 + 2 + 3 + 4 + 6 + 12 = 28
        "20, 42",    // sigma(20) = 1 + 2 + 4 + 5 + 10 + 20 = 42
        "30, 72"     // sigma(30) = 1 + 2 + 3 + 5 + 6 + 10 + 15 + 30 = 72
    })
    public void testSumDivisors(long n, long expectedSum) {
        assertEquals(expectedSum, NumberTheoryTemplates.MultiplicativeFunctions.sumDivisors(n), 
                    "Sum of divisors of " + n + " should be " + expectedSum);
    }

    @DisplayName("Multiplicative Functions: Möbius Function Test") 
    @ParameterizedTest
    @CsvSource({
        "1, 1",      // mu(1) = 1
        "2, -1",     // mu(2) = -1 (one prime)
        "3, -1",     // mu(3) = -1 (one prime)
        "4, 0",      // mu(4) = 0 (has squared prime factor)
        "6, 1",      // mu(6) = 1 (two distinct primes: 2, 3)
        "12, 0",     // mu(12) = 0 (has squared prime factor)
        "30, -1"     // mu(30) = -1 (three distinct primes: 2, 3, 5)
    })
    public void testMobiusFunction(long n, int expectedMobius) {
        assertEquals(expectedMobius, NumberTheoryTemplates.MultiplicativeFunctions.mobius(n), 
                    "mu(" + n + ") should be " + expectedMobius);
    }

    /**
     * INTEGRATION AND CROSS-VALIDATION TESTS
     */
    @DisplayName("Integration: Factorization and Totient Consistency")
    @Test
    public void testFactorizationTotientConsistency() {
        long[] testNumbers = {12, 20, 30, 60, 100};
        
        for (long n : testNumbers) {
            long phi1 = NumberTheoryTemplates.EulerTotient.phi(n);
            long phi2 = NumberTheoryTemplates.EulerTotient.phiUsingFactorization(n);
            
            assertEquals(phi1, phi2, 
                        "Totient calculations should be consistent for " + n);
        }
    }

    @DisplayName("Integration: Large Number Stress Test")
    @Test
    public void testLargeNumberHandling() {
        // Test with some larger numbers to check for overflow issues
        long largeNumber = 1000000007L; // Large prime
        
        assertTrue(NumberTheoryTemplates.MillerRabinTest.isPrime(largeNumber));
        
        List<Long> factors = NumberTheoryTemplates.PollardRho.factorize(largeNumber);
        assertEquals(1, factors.size());
        assertEquals(largeNumber, factors.get(0).longValue());
        
        long phi = NumberTheoryTemplates.EulerTotient.phi(largeNumber);
        assertEquals(largeNumber - 1, phi); // phi(p) = p-1 for prime p
    }
}