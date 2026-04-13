package com.cp.algorithms;

/**
 * Overflow stress test for NumberTheoryTemplates
 * Tests edge cases that could cause integer overflow
 */
public class OverflowStressTest {
    
    public static void main(String[] args) {
        System.out.println("=== Overflow Stress Test ===\n");
        
        testLargeNumberMultiplication();
        testLargeModularArithmetic();
        testLargePrimes();
        testFactorizationEdgeCases();
        
        System.out.println("\n=== Overflow Test Complete ===");
    }
    
    private static void testLargeNumberMultiplication() {
        System.out.println("Testing large number multiplication...");
        
        // Test numbers close to Long.MAX_VALUE
        long large1 = Long.MAX_VALUE / 2;
        long large2 = Long.MAX_VALUE / 3;
        long mod = 1000000007L;
        
        System.out.println("Testing mulMod with very large numbers:");
        System.out.println("a = " + large1);
        System.out.println("b = " + large2);
        System.out.println("mod = " + mod);
        
        try {
            // This should expose overflow issues if they exist
            // The current implementation may overflow on (a % mod) * (b % mod)
            long result = testMulMod(large1, large2, mod);
            System.out.println("Result: " + result);
            
            // Verify with BigInteger
            java.math.BigInteger ba = java.math.BigInteger.valueOf(large1);
            java.math.BigInteger bb = java.math.BigInteger.valueOf(large2);
            java.math.BigInteger bmod = java.math.BigInteger.valueOf(mod);
            java.math.BigInteger expected = ba.multiply(bb).mod(bmod);
            
            if (result == expected.longValue()) {
                System.out.println("✓ Large multiplication test PASSED");
            } else {
                System.out.println("✗ Large multiplication test FAILED");
                System.out.println("Expected: " + expected);
                System.out.println("Got: " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Exception during large multiplication: " + e.getMessage());
        }
    }
    
    private static void testLargeModularArithmetic() {
        System.out.println("\nTesting large modular exponentiation...");
        
        long base = Long.MAX_VALUE / 4;
        long exp = 1000000;
        long mod = 1000000007L;
        
        System.out.println("Testing modPow(" + base + ", " + exp + ", " + mod + ")");
        
        try {
            long result = testModPow(base, exp, mod);
            System.out.println("Result: " + result);
            System.out.println("✓ Large modular exponentiation completed without overflow");
        } catch (Exception e) {
            System.out.println("✗ Exception during large modular exponentiation: " + e.getMessage());
        }
    }
    
    private static void testLargePrimes() {
        System.out.println("\nTesting with large primes...");
        
        // Test with verified large primes
        // 2^31 - 1 = 2147483647 (Mersenne prime)
        long mersenne31 = 2147483647L;
        
        System.out.println("Testing Miller-Rabin on 2^31 - 1: " + mersenne31);
        
        try {
            boolean isPrime = NumberTheoryTemplates.MillerRabinTest.isPrime(mersenne31);
            System.out.println("Result: " + isPrime);
            if (isPrime) {
                System.out.println("✓ Mersenne prime 2^31-1 test PASSED");
            } else {
                System.out.println("✗ Mersenne prime 2^31-1 test FAILED - should be prime");
            }
        } catch (Exception e) {
            System.out.println("✗ Exception during Mersenne prime test: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Test another known large prime: 1000000007
        long largePrime2 = 1000000007L;
        System.out.println("Testing Miller-Rabin on " + largePrime2);
        
        try {
            boolean isPrime2 = NumberTheoryTemplates.MillerRabinTest.isPrime(largePrime2);
            System.out.println("Result: " + isPrime2);
            if (isPrime2) {
                System.out.println("✓ Large prime " + largePrime2 + " test PASSED");
            } else {
                System.out.println("✗ Large prime " + largePrime2 + " test FAILED - should be prime");
            }
        } catch (Exception e) {
            System.out.println("✗ Exception during large prime test: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Note about 2^61-1: Let's verify if it's actually prime
        long questionable = 2305843009213693951L;
        System.out.println("Note: 2^61-1 = " + questionable + " may not actually be prime");
        System.out.println("Testing anyway...");
        boolean result = NumberTheoryTemplates.MillerRabinTest.isPrime(questionable);
        System.out.println("Miller-Rabin result for 2^61-1: " + result);
        
        // If it's composite, try to factor it
        if (!result) {
            System.out.println("Attempting to factor 2^61-1...");
            try {
                var factors = NumberTheoryTemplates.PollardRho.factorize(questionable);
                if (factors.size() > 1) {
                    System.out.println("✓ 2^61-1 is composite. Factors: " + factors);
                } else {
                    System.out.println("? Could not factor 2^61-1, but Miller-Rabin says composite");
                }
            } catch (Exception e) {
                System.out.println("Exception during factorization: " + e.getMessage());
            }
        }
    }
    
    private static void testFactorizationEdgeCases() {
        System.out.println("\nTesting factorization edge cases...");
        
        // Test with large composite numbers
        long largeComposite = 1000000007L * 1000000009L; // This will overflow long!
        
        System.out.println("Note: Testing product of two large primes (will exceed long range)");
        System.out.println("1000000007 * 1000000009 = " + largeComposite);
        
        if (largeComposite < 0) {
            System.out.println("⚠ Overflow detected in multiplication - result is negative!");
        }
        
        // Test with smaller but still challenging numbers
        long challengingNumber = 982451653L * 982451679L; // Still very large
        if (challengingNumber > 0) {
            System.out.println("Testing factorization of " + challengingNumber);
            try {
                var factors = NumberTheoryTemplates.PollardRho.factorize(challengingNumber);
                System.out.println("Factors: " + factors);
                
                long product = factors.stream().reduce(1L, (a, b) -> a * b);
                if (product == challengingNumber) {
                    System.out.println("✓ Large factorization test PASSED");
                } else {
                    System.out.println("✗ Large factorization test FAILED");
                }
            } catch (Exception e) {
                System.out.println("✗ Exception during large factorization: " + e.getMessage());
            }
        } else {
            System.out.println("⚠ Number overflowed - cannot test factorization");
        }
    }
    
    // Test methods to expose potential issues
    private static long testMulMod(long a, long b, long mod) {
        // This replicates the current implementation that may have overflow issues
        return ((a % mod) * (b % mod)) % mod;
    }
    
    private static long testModPow(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = testMulMod(result, base, mod);
            base = testMulMod(base, base, mod);
            exp >>= 1;
        }
        return result;
    }
}