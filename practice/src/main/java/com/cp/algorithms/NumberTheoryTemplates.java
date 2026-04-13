package com.cp.algorithms;

import java.util.*;

/**
 * Advanced Number Theory Templates - Essential for Math-Heavy Interviews
 * 
 * WHEN TO USE:
 * - Large prime testing (cryptography, hashing)
 * - Integer factorization problems
 * - Modular arithmetic with unknown modulus
 * - Advanced counting with number-theoretic functions
 * 
 * KEY ALGORITHMS:
 * - Miller-Rabin: Probabilistic primality test O(k log³ n)
 * - Pollard's Rho: Integer factorization O(n^(1/4))
 * - Tonelli-Shanks: Modular square root
 * - Baby-Step Giant-Step: Discrete logarithm
 * - Advanced Euler's totient and multiplicative functions
 * 
 * COMPLEXITY:
 * - Miller-Rabin: O(k log³ n) for k iterations
 * - Pollard's Rho: O(n^(1/4)) expected
 * - Tonelli-Shanks: O(log² p)
 * - BSGS: O(√n) time and space
 * 
 * COMMON INTERVIEW PROBLEMS:
 * - Large prime generation/testing
 * - RSA-related problems
 * - Hash function design
 * - Advanced modular equations
 * - Competitive programming number theory
 * 
 * COMPANIES:
 * - Google L5+: Advanced algorithms, cryptography
 * - Amazon SDE2+: System security, distributed systems
 * - Meta: Infrastructure, security algorithms
 * 
 * 
 */
public class NumberTheoryTemplates {

    private static final Random random = new Random();

    /**
     * 1. MILLER-RABIN PRIMALITY TEST - Fast Probabilistic Prime Testing
     * 
     * Algorithm:
     * For odd n > 2, write n-1 = 2^r * d where d is odd
     * For witness a: if a^d ≡ 1 (mod n) or a^(2^i * d) ≡ -1 (mod n) for some i < r
     * then n passes the test for witness a
     * 
     * Time: O(k log³ n) for k iterations
     * Error probability: ≤ 4^(-k)
     * 
     * Deterministic for n < 3,317,044,064,679,887,385,961,981
     */
    static class MillerRabinTest {
        // Deterministic witnesses for different ranges
        private static final long[] WITNESSES_32 = {2, 7, 61};
        private static final long[] WITNESSES_64 = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};
        
        // Check if n is prime (deterministic for n < 2^64)
        public static boolean isPrime(long n) {
            if (n < 2) return false;
            if (n == 2 || n == 3) return true;
            if (n % 2 == 0) return false;
            
            // Write n-1 = 2^r * d
            long d = n - 1;
            int r = 0;
            while (d % 2 == 0) {
                d /= 2;
                r++;
            }
            
            // Choose appropriate witnesses
            long[] witnesses = (n < (1L << 32)) ? WITNESSES_32 : WITNESSES_64;
            
            for (long a : witnesses) {
                if (a >= n) continue;
                if (!millerTest(n, d, r, a)) {
                    return false;
                }
            }
            
            return true;
        }
        
        // Probabilistic version with k random witnesses
        public static boolean isProbablyPrime(long n, int k) {
            if (n < 2) return false;
            if (n == 2 || n == 3) return true;
            if (n % 2 == 0) return false;
            
            long d = n - 1;
            int r = 0;
            while (d % 2 == 0) {
                d /= 2;
                r++;
            }
            
            for (int i = 0; i < k; i++) {
                long a = 2 + Math.abs(random.nextLong()) % (n - 3);
                if (!millerTest(n, d, r, a)) {
                    return false;
                }
            }
            
            return true;
        }
        
        private static boolean millerTest(long n, long d, int r, long a) {
            long x = modPow(a, d, n);
            if (x == 1 || x == n - 1) return true;
            
            for (int i = 0; i < r - 1; i++) {
                x = mulMod(x, x, n);
                if (x == n - 1) return true;
            }
            
            return false;
        }
        
        // Modular exponentiation
        private static long modPow(long base, long exp, long mod) {
            long result = 1;
            base %= mod;
            while (exp > 0) {
                if ((exp & 1) == 1) result = mulMod(result, base, mod);
                base = mulMod(base, base, mod);
                exp >>= 1;
            }
            return result;
        }
        
        // Modular multiplication avoiding overflow using double precision or splitting
        private static long mulMod(long a, long b, long mod) {
            if (mod <= 1000000) {
                // Safe for small moduli
                return ((a % mod) * (b % mod)) % mod;
            }
            
            // For larger moduli, use double precision check
            a %= mod;
            b %= mod;
            
            // Use double precision to check for overflow
            double check = (double)a * b;
            if (check < Long.MAX_VALUE) {
                return (a * b) % mod;
            }
            
            // Split multiplication for very large numbers
            return splitMulMod(a, b, mod);
        }
        
        // Split multiplication for avoiding overflow
        private static long splitMulMod(long a, long b, long mod) {
            if (a == 0 || b == 0) return 0;
            if (a == 1) return b % mod;
            if (b == 1) return a % mod;
            
            // Use Russian peasant multiplication
            long result = 0;
            a %= mod;
            b %= mod;
            
            while (b > 0) {
                if ((b & 1) == 1) {
                    result = (result + a) % mod;
                }
                a = (a * 2) % mod;
                b >>= 1;
            }
            
            return result;
        }
    }

    /**
     * 2. POLLARD'S RHO ALGORITHM - Integer Factorization
     * 
     * Finds non-trivial factor of composite number
     * Uses Floyd's cycle detection with polynomial f(x) = x² + c
     * 
     * Time: O(n^(1/4)) expected
     * Space: O(1)
     * 
     * Works well for numbers with small factors
     */
    static class PollardRho {
        public static List<Long> factorize(long n) {
            List<Long> factors = new ArrayList<>();
            factorizeHelper(n, factors);
            Collections.sort(factors);
            return factors;
        }
        
        private static void factorizeHelper(long n, List<Long> factors) {
            if (n == 1) return;
            
            if (MillerRabinTest.isPrime(n)) {
                factors.add(n);
                return;
            }
            
            // Handle even numbers
            while (n % 2 == 0) {
                factors.add(2L);
                n /= 2;
            }
            
            if (n == 1) return;
            if (MillerRabinTest.isPrime(n)) {
                factors.add(n);
                return;
            }
            
            // Find factor using Pollard's rho
            long factor = rho(n);
            factorizeHelper(factor, factors);
            factorizeHelper(n / factor, factors);
        }
        
        private static long rho(long n) {
            if (n % 2 == 0) return 2;
            
            for (long c = 1; c < n; c++) {
                long x = 2, y = 2, d = 1;
                
                while (d == 1) {
                    x = f(x, c, n);
                    y = f(f(y, c, n), c, n);
                    d = gcd(Math.abs(x - y), n);
                }
                
                if (d != n) return d;
            }
            
            return n;  // Fallback (shouldn't happen for composite n)
        }
        
        private static long f(long x, long c, long n) {
            return (MillerRabinTest.mulMod(x, x, n) + c) % n;
        }
        
        private static long gcd(long a, long b) {
            return b == 0 ? a : gcd(b, a % b);
        }
    }

    /**
     * 3. TONELLI-SHANKS ALGORITHM - Modular Square Root
     * 
     * Find x such that x² ≡ n (mod p) where p is odd prime
     * 
     * Time: O(log² p)
     * Space: O(1)
     * 
     * Returns -1 if n is not quadratic residue mod p
     */
    static class TonelliShanks {
        public static long modularSqrt(long n, long p) {
            if (p == 2) return n % 2;
            
            // Check if n is quadratic residue using Legendre symbol
            if (legendreSymbol(n, p) != 1) return -1;
            
            // Special case: p ≡ 3 (mod 4)
            if (p % 4 == 3) {
                return modPow(n, (p + 1) / 4, p);
            }
            
            // General case: Tonelli-Shanks algorithm
            // Write p - 1 = Q * 2^S with Q odd
            long Q = p - 1;
            int S = 0;
            while (Q % 2 == 0) {
                Q /= 2;
                S++;
            }
            
            // Find quadratic non-residue z
            long z = 2;
            while (legendreSymbol(z, p) != -1) {
                z++;
            }
            
            long M = S;
            long c = modPow(z, Q, p);
            long t = modPow(n, Q, p);
            long R = modPow(n, (Q + 1) / 2, p);
            
            while (t != 1) {
                // Find smallest i such that t^(2^i) ≡ 1 (mod p)
                long temp = t;
                int i;
                for (i = 1; i < M; i++) {
                    temp = (temp * temp) % p;
                    if (temp == 1) break;
                }
                
                // Update values
                long b = modPow(c, 1L << (M - i - 1), p);
                M = i;
                c = (b * b) % p;
                t = (t * c) % p;
                R = (R * b) % p;
            }
            
            return R;
        }
        
        // Legendre symbol (n/p) = n^((p-1)/2) mod p
        private static int legendreSymbol(long n, long p) {
            long result = modPow(n, (p - 1) / 2, p);
            return result == p - 1 ? -1 : (int)result;
        }
        
        private static long modPow(long base, long exp, long mod) {
            long result = 1;
            base %= mod;
            while (exp > 0) {
                if ((exp & 1) == 1) result = (result * base) % mod;
                base = (base * base) % mod;
                exp >>= 1;
            }
            return result;
        }
    }

    /**
     * 4. BABY-STEP GIANT-STEP - Discrete Logarithm
     * 
     * Find x such that a^x ≡ b (mod p)
     * 
     * Time: O(√p)
     * Space: O(√p)
     * 
     * Returns -1 if no solution exists
     */
    static class BabyStepGiantStep {
        public static long discreteLog(long a, long b, long p) {
            if (b == 1) return 0;
            
            long n = (long)Math.sqrt(p) + 1;
            
            // Baby steps: store a^j mod p for j = 0, 1, ..., n-1
            Map<Long, Long> babySteps = new HashMap<>();
            long current = 1;
            for (long j = 0; j < n; j++) {
                if (!babySteps.containsKey(current)) {
                    babySteps.put(current, j);
                }
                current = (current * a) % p;
            }
            
            // Giant steps: check if b * (a^(-n))^i is in baby steps
            long gamma = modPow(modInverse(a, p), n, p);  // a^(-n) mod p
            current = b;
            
            for (long i = 0; i < n; i++) {
                if (babySteps.containsKey(current)) {
                    long ans = i * n + babySteps.get(current);
                    if (ans < p) return ans;
                }
                current = (current * gamma) % p;
            }
            
            return -1;  // No solution
        }
        
        private static long modInverse(long a, long p) {
            return modPow(a, p - 2, p);  // Fermat's little theorem
        }
        
        private static long modPow(long base, long exp, long mod) {
            long result = 1;
            base %= mod;
            while (exp > 0) {
                if ((exp & 1) == 1) result = (result * base) % mod;
                base = (base * base) % mod;
                exp >>= 1;
            }
            return result;
        }
    }

    /**
     * 5. ADVANCED EULER'S TOTIENT FUNCTION
     * 
     * φ(n) = number of integers ≤ n that are coprime to n
     * 
     * Multiple algorithms:
     * - Single value: O(√n)
     * - Range [1,n]: O(n log log n) using sieve
     * - Using prime factorization: O(number of distinct primes)
     */
    static class EulerTotient {
        // Single value using prime factorization
        public static long phi(long n) {
            long result = n;
            
            // Handle factor 2
            if (n % 2 == 0) {
                while (n % 2 == 0) n /= 2;
                result -= result / 2;
            }
            
            // Handle odd factors
            for (long i = 3; i * i <= n; i += 2) {
                if (n % i == 0) {
                    while (n % i == 0) n /= i;
                    result -= result / i;
                }
            }
            
            if (n > 1) result -= result / n;
            
            return result;
        }
        
        // Sieve for range [1, n]
        public static long[] phiSieve(int n) {
            long[] phi = new long[n + 1];
            for (int i = 1; i <= n; i++) phi[i] = i;
            
            for (int i = 2; i <= n; i++) {
                if (phi[i] == i) {  // i is prime
                    for (int j = i; j <= n; j += i) {
                        phi[j] -= phi[j] / i;
                    }
                }
            }
            
            return phi;
        }
        
        // Using factorization from Pollard's rho
        public static long phiUsingFactorization(long n) {
            List<Long> factors = PollardRho.factorize(n);
            Map<Long, Integer> primeCount = new HashMap<>();
            
            for (long p : factors) {
                primeCount.put(p, primeCount.getOrDefault(p, 0) + 1);
            }
            
            long result = 1;
            for (Map.Entry<Long, Integer> entry : primeCount.entrySet()) {
                long p = entry.getKey();
                int k = entry.getValue();
                // φ(p^k) = p^(k-1) * (p - 1)
                long pk_minus_1 = 1;
                for (int i = 0; i < k - 1; i++) {
                    pk_minus_1 *= p;
                }
                result *= pk_minus_1 * (p - 1);
            }
            
            return result;
        }
        
        private static long modPow(long base, long exp, long mod) {
            if (mod == Long.MAX_VALUE) {
                // No modular arithmetic needed
                long result = 1;
                while (exp > 0) {
                    if ((exp & 1) == 1) result *= base;
                    base *= base;
                    exp >>= 1;
                }
                return result;
            }
            
            long result = 1;
            base %= mod;
            while (exp > 0) {
                if ((exp & 1) == 1) result = (result * base) % mod;
                base = (base * base) % mod;
                exp >>= 1;
            }
            return result;
        }
    }

    /**
     * 6. MULTIPLICATIVE FUNCTIONS AND DIVISOR PROPERTIES
     */
    static class MultiplicativeFunctions {
        // Number of divisors
        public static long numDivisors(long n) {
            List<Long> factors = PollardRho.factorize(n);
            Map<Long, Integer> primeCount = new HashMap<>();
            
            for (long p : factors) {
                primeCount.put(p, primeCount.getOrDefault(p, 0) + 1);
            }
            
            long result = 1;
            for (int count : primeCount.values()) {
                result *= (count + 1);
            }
            
            return result;
        }
        
        // Sum of divisors
        public static long sumDivisors(long n) {
            List<Long> factors = PollardRho.factorize(n);
            Map<Long, Integer> primeCount = new HashMap<>();
            
            for (long p : factors) {
                primeCount.put(p, primeCount.getOrDefault(p, 0) + 1);
            }
            
            long result = 1;
            for (Map.Entry<Long, Integer> entry : primeCount.entrySet()) {
                long p = entry.getKey();
                int k = entry.getValue();
                // σ(p^k) = (p^(k+1) - 1) / (p - 1)
                // Use safer calculation to avoid overflow
                long sum = 0;
                long pk = 1;
                for (int i = 0; i <= k; i++) {
                    sum += pk;
                    if (i < k) pk *= p; // Only multiply if not last iteration
                }
                result *= sum;
            }
            
            return result;
        }
        
        // Möbius function μ(n)
        public static int mobius(long n) {
            List<Long> factors = PollardRho.factorize(n);
            Set<Long> distinctPrimes = new HashSet<>(factors);
            
            if (distinctPrimes.size() != factors.size()) {
                return 0;  // Has squared prime factor
            }
            
            return (factors.size() % 2 == 0) ? 1 : -1;
        }
        
        private static long modPow(long base, long exp) {
            long result = 1;
            while (exp > 0) {
                if ((exp & 1) == 1) {
                    // Check for overflow before multiplication
                    if (result > Long.MAX_VALUE / base) {
                        throw new ArithmeticException("Overflow in modPow calculation");
                    }
                    result *= base;
                }
                // Check for overflow before squaring
                if (base > Math.sqrt(Long.MAX_VALUE)) {
                    throw new ArithmeticException("Overflow in modPow base squaring");
                }
                base *= base;
                exp >>= 1;
            }
            return result;
        }
    }

    /**
     * USAGE EXAMPLES AND TEST CASES
     */
    public static void main(String[] args) {
        // Example 1: Miller-Rabin primality testing
        System.out.println("=== Example 1: Miller-Rabin Primality ===");
        long[] testNumbers = {97, 1009, 982451653L, 982451654L};
        for (long n : testNumbers) {
            System.out.println(n + " is prime: " + MillerRabinTest.isPrime(n));
        }
        
        // Example 2: Integer factorization
        System.out.println("\n=== Example 2: Pollard's Rho Factorization ===");
        long[] factorizeNumbers = {60, 1001, 982451653L};
        for (long n : factorizeNumbers) {
            List<Long> factors = PollardRho.factorize(n);
            System.out.println("Factors of " + n + ": " + factors);
        }
        
        // Example 3: Modular square root
        System.out.println("\n=== Example 3: Tonelli-Shanks Square Root ===");
        long[] testRoots = {2, 3, 5, 7};
        long p = 13;
        for (long n : testRoots) {
            long root = TonelliShanks.modularSqrt(n, p);
            if (root != -1) {
                System.out.println("sqrt(" + n + ") = " + root + " (mod " + p + ")");
                System.out.println("Verification: " + root + "^2 = " + (root * root % p) + " (mod " + p + ")");
            } else {
                System.out.println("sqrt(" + n + ") does not exist mod " + p);
            }
        }
        
        // Example 4: Discrete logarithm
        System.out.println("\n=== Example 4: Baby-Step Giant-Step ===");
        long a = 2, b = 8, p2 = 13;
        long x = BabyStepGiantStep.discreteLog(a, b, p2);
        System.out.println("Discrete log: " + a + "^x = " + b + " (mod " + p2 + ")");
        if (x != -1) {
            System.out.println("x = " + x);
            long verification = 1;
            for (int i = 0; i < x; i++) verification = (verification * a) % p2;
            System.out.println("Verification: " + a + "^" + x + " = " + verification + " (mod " + p2 + ")");
        } else {
            System.out.println("No solution exists");
        }
        
        // Example 5: Euler's totient function
        System.out.println("\n=== Example 5: Euler's Totient ===");
        long[] phiNumbers = {12, 17, 30, 100};
        for (long n : phiNumbers) {
            System.out.println("phi(" + n + ") = " + EulerTotient.phi(n));
        }
        
        // Example 6: Multiplicative functions
        System.out.println("\n=== Example 6: Multiplicative Functions ===");
        long[] testNums = {12, 20, 30};
        for (long n : testNums) {
            System.out.println("n = " + n);
            System.out.println("  Number of divisors: " + MultiplicativeFunctions.numDivisors(n));
            System.out.println("  Sum of divisors: " + MultiplicativeFunctions.sumDivisors(n));
            System.out.println("  Mobius mu(n): " + MultiplicativeFunctions.mobius(n));
        }
    }
}