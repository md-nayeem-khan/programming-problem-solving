package com.cp.algorithms;

import java.util.*;

/**
 * Combinatorics Templates - Essential for Counting Problems
 * 
 * WHEN TO USE:
 * - Counting problems (ways to arrange, select, partition)
 * - Probability calculations
 * - Modular arithmetic with combinations
 * - Catalan number problems (balanced parentheses, BST counting)
 * 
 * KEY CONCEPTS:
 * - Combinations: C(n,k) = n! / (k!(n-k)!)
 * - Permutations: P(n,k) = n! / (n-k)!
 * - Lucas' Theorem: C(n,k) mod p for prime p
 * - Catalan Numbers: C_n = (2n)! / ((n+1)!n!)
 * - Stirling Numbers: Partitions and cycles
 * 
 * COMPLEXITY:
 * - Factorial precomputation: O(n)
 * - Combination with mod: O(log p) per query with precomputation
 * - Lucas' theorem: O(log_p(n) * log p)
 * - Catalan numbers: O(n) with DP
 * 
 * COMMON INTERVIEW PROBLEMS:
 * - LeetCode 62: Unique Paths (combinations)
 * - LeetCode 96: Unique BSTs (Catalan)
 * - LeetCode 22: Generate Parentheses (Catalan)
 * - LeetCode 1569: Ways to Reorder Array (counting)
 * - Amazon counting problems (12-15% SDE2+ frequency)
 * 
 * 
 */
public class CombinatoricsTemplates {

    private static final int MOD = 1_000_000_007;
    private static final int MAX_N = 200005;
    
    /**
     * 1. FACTORIAL WITH MODULAR ARITHMETIC
     * 
     * Precompute factorials and inverse factorials for fast C(n,k) queries
     * 
     * Time: O(n) preprocessing, O(1) per query
     * Space: O(n)
     */
    public static class FactorialMod {
        private long[] fact;
        private long[] invFact;
        private int mod;
        private int maxN;
        
        public FactorialMod(int maxN, int mod) {
            this.maxN = maxN;
            this.mod = mod;
            fact = new long[maxN + 1];
            invFact = new long[maxN + 1];
            precompute();
        }
        
        private void precompute() {
            fact[0] = 1;
            for (int i = 1; i <= maxN; i++) {
                fact[i] = (fact[i - 1] * i) % mod;
            }
            
            // Fermat's little theorem: a^(p-1) ≡ 1 (mod p)
            // So a^(-1) ≡ a^(p-2) (mod p)
            invFact[maxN] = modPow(fact[maxN], mod - 2, mod);
            for (int i = maxN - 1; i >= 0; i--) {
                invFact[i] = (invFact[i + 1] * (i + 1)) % mod;
            }
        }
        
        // C(n, k) mod p
        public long nCr(int n, int k) {
            if (k < 0 || k > n) return 0;
            if (k == 0 || k == n) return 1;
            return (fact[n] * invFact[k] % mod) * invFact[n - k] % mod;
        }
        
        // P(n, k) = n!/(n-k)! mod p
        public long nPr(int n, int k) {
            if (k < 0 || k > n) return 0;
            return fact[n] * invFact[n - k] % mod;
        }
        
        public long factorial(int n) {
            return fact[n];
        }
        
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
    }

    /**
     * 2. LUCAS' THEOREM - C(n,k) mod p for Large n,k
     * 
     * For prime p: C(n,k) mod p = PRODUCT C(n_i, k_i) mod p
     * where n = n_t*p^t + ... + n_1*p + n_0 (base-p representation)
     * 
     * Time: O(log_p(n) * log p)
     * Space: O(p)
     * 
     * Essential when n,k are huge but p is small (≤10^6)
     */
    public static class LucasTheorem {
        private long[] fact;
        private long[] invFact;
        private int p;
        
        public LucasTheorem(int prime) {
            this.p = prime;
            fact = new long[p];
            invFact = new long[p];
            precompute();
        }
        
        private void precompute() {
            fact[0] = 1;
            for (int i = 1; i < p; i++) {
                fact[i] = (fact[i - 1] * i) % p;
            }
            invFact[p - 1] = modPow(fact[p - 1], p - 2);
            for (int i = p - 2; i >= 0; i--) {
                invFact[i] = (invFact[i + 1] * (i + 1)) % p;
            }
        }
        
        // C(n,k) mod p using Lucas' theorem
        public long nCrModP(long n, long k) {
            if (k > n) return 0;
            
            long result = 1;
            while (n > 0 || k > 0) {
                int ni = (int)(n % p);
                int ki = (int)(k % p);
                
                if (ki > ni) return 0;
                
                result = (result * nCrSmall(ni, ki)) % p;
                n /= p;
                k /= p;
            }
            
            return result;
        }
        
        // C(n,k) mod p for small n,k < p
        private long nCrSmall(int n, int k) {
            if (k > n) return 0;
            return (fact[n] * invFact[k] % p) * invFact[n - k] % p;
        }
        
        private long modPow(long base, long exp) {
            long result = 1;
            base %= p;
            while (exp > 0) {
                if ((exp & 1) == 1) result = (result * base) % p;
                base = (base * base) % p;
                exp >>= 1;
            }
            return result;
        }
    }

    /**
     * 3. CATALAN NUMBERS - Essential Counting Sequence
     * 
     * C_n = (2n)! / ((n+1)!*n!) = C(2n,n)/(n+1)
     * 
     * Applications:
     * - Balanced parentheses: C_n ways
     * - Binary Search Trees: C_n unique BSTs with n nodes
     * - Dyck paths: C_n paths
     * - Polygon triangulation: C_{n-2} ways for n-gon
     * - Mountain ranges: C_n shapes with n upstrokes
     * 
     * Time: O(n) for first n Catalan numbers
     * Space: O(n)
     */
    public static class CatalanNumbers {
        // Method 1: Using C(2n,n)/(n+1) with modular arithmetic
        public static long catalanMod(int n, int mod) {
            FactorialMod fm = new FactorialMod(2 * n, mod);
            long c2n_n = fm.nCr(2 * n, n);
            long nPlus1Inv = modInverse(n + 1, mod);
            return (c2n_n * nPlus1Inv) % mod;
        }
        
        // Method 2: DP recurrence C_n = sum(C_i * C_{n-1-i})
        public static long[] catalanDP(int n, int mod) {
            long[] cat = new long[n + 1];
            cat[0] = 1;
            if (n > 0) cat[1] = 1;
            
            for (int i = 2; i <= n; i++) {
                for (int j = 0; j < i; j++) {
                    cat[i] = (cat[i] + cat[j] * cat[i - 1 - j]) % mod;
                }
            }
            
            return cat;
        }
        
        // Method 3: Direct formula C_n = (2n)! / ((n+1)!n!)
        public static long[] catalanFactorial(int n) {
            long[] cat = new long[n + 1];
            long[] fact = new long[2 * n + 1];
            fact[0] = 1;
            for (int i = 1; i <= 2 * n; i++) {
                fact[i] = fact[i - 1] * i;
            }
            
            for (int i = 0; i <= n; i++) {
                cat[i] = fact[2 * i] / (fact[i + 1] * fact[i]);
            }
            
            return cat;
        }
        
        // Method 4: Direct recurrence C_n = 2*(2n-1)*C_{n-1}/(n+1)
        public static long[] catalanRecurrence(int n, int mod) {
            long[] cat = new long[n + 1];
            cat[0] = 1;
            
            for (int i = 1; i <= n; i++) {
                long numerator = (2L * (2 * i - 1) % mod * cat[i - 1]) % mod;
                long denominator = i + 1;
                cat[i] = (numerator * modInverse(denominator, mod)) % mod;
            }
            
            return cat;
        }
        
        private static long modInverse(long a, long mod) {
            return modPow(a, mod - 2, mod);
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
     * 4. STIRLING NUMBERS - Partitions and Cycles
     * 
     * Stirling Number of First Kind S(n,k):
     * - Number of permutations of n elements with exactly k cycles
     * - Recurrence: S(n,k) = S(n-1,k-1) + (n-1)*S(n-1,k)
     * 
     * Stirling Number of Second Kind S(n,k):
     * - Number of ways to partition n elements into k non-empty subsets
     * - Recurrence: S(n,k) = S(n-1,k-1) + k*S(n-1,k)
     * 
     * Time: O(n*k)
     * Space: O(n*k)
     */
    public static class StirlingNumbers {
        // Stirling numbers of the first kind (signed)
        public static long[][] stirlingFirst(int n, int mod) {
            long[][] S = new long[n + 1][n + 1];
            S[0][0] = 1;
            
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= i; j++) {
                    S[i][j] = (S[i - 1][j - 1] + (i - 1) * S[i - 1][j]) % mod;
                }
            }
            
            return S;
        }
        
        // Stirling numbers of the second kind
        public static long[][] stirlingSecond(int n, int mod) {
            long[][] S = new long[n + 1][n + 1];
            S[0][0] = 1;
            
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= i; j++) {
                    S[i][j] = (S[i - 1][j - 1] + j * S[i - 1][j]) % mod;
                }
            }
            
            return S;
        }
        
        // Alternative: Stirling second kind using inclusion-exclusion
        // S(n,k) = 1/k! * sum_{j=0}^{k} (-1)^j * C(k,j) * (k-j)^n
        public static long stirlingSecondSingle(int n, int k, int mod) {
            FactorialMod fm = new FactorialMod(k, mod);
            long sum = 0;
            
            for (int j = 0; j <= k; j++) {
                long term = fm.nCr(k, j);
                long pow = modPow(k - j, n, mod);
                term = (term * pow) % mod;
                
                if (j % 2 == 0) {
                    sum = (sum + term) % mod;
                } else {
                    sum = (sum - term + mod) % mod;
                }
            }
            
            return (sum * modInverse(fm.factorial(k), mod)) % mod;
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
        
        private static long modInverse(long a, long mod) {
            return modPow(a, mod - 2, mod);
        }
    }

    /**
     * 5. DERANGEMENTS - Permutations with No Fixed Points
     * 
     * D_n = number of permutations where no element is in its original position
     * Formula: D_n = n! * sum_{i=0}^{n} (-1)^i / i!
     * Recurrence: D_n = (n-1) * (D_{n-1} + D_{n-2})
     * 
     * Time: O(n)
     * Space: O(n)
     */
    public static class Derangements {
        public static long[] derangements(int n, int mod) {
            long[] D = new long[n + 1];
            if (n >= 0) D[0] = 1;
            if (n >= 1) D[1] = 0;
            
            for (int i = 2; i <= n; i++) {
                D[i] = ((i - 1) * ((D[i - 1] + D[i - 2]) % mod)) % mod;
            }
            
            return D;
        }
        
        // Probability that random permutation is derangement
        // Approaches 1/e ≈ 0.368 as n → ∞
        public static double derangementProbability(int n) {
            if (n == 0) return 1.0;
            if (n == 1) return 0.0;
            
            long[] D = derangements(n, Integer.MAX_VALUE);
            long fact = 1;
            for (int i = 1; i <= n; i++) fact *= i;
            
            return (double) D[n] / fact;
        }
    }

    /**
     * 6. BINOMIAL COEFFICIENTS - Multiple Methods
     */
    public static class BinomialCoefficients {
        // Pascal's triangle (no mod)
        public static long[][] pascalTriangle(int n) {
            long[][] C = new long[n + 1][n + 1];
            
            for (int i = 0; i <= n; i++) {
                C[i][0] = 1;
                for (int j = 1; j <= i; j++) {
                    C[i][j] = C[i - 1][j - 1] + C[i - 1][j];
                }
            }
            
            return C;
        }
        
        // Space-optimized O(k)
        public static long nCr(int n, int k) {
            if (k > n - k) k = n - k;  // C(n,k) = C(n,n-k)
            
            long result = 1;
            for (int i = 0; i < k; i++) {
                result = result * (n - i) / (i + 1);
            }
            
            return result;
        }
        
        // With modular arithmetic
        public static long nCrMod(int n, int k, int mod) {
            if (k > n) return 0;
            if (k == 0 || k == n) return 1;
            
            FactorialMod fm = new FactorialMod(n, mod);
            return fm.nCr(n, k);
        }
    }

    /**
     * 7. MULTINOMIAL COEFFICIENTS
     * 
     * Number of ways to partition n items into groups of sizes k1, k2, ..., km
     * M(n; k1, k2, ..., km) = n! / (k1! * k2! * ... * km!)
     * 
     * Time: O(m) with precomputed factorials
     */
    public static class MultinomialCoefficients {
        public static long multinomial(int[] groups, int mod) {
            int n = 0;
            for (int g : groups) n += g;
            
            FactorialMod fm = new FactorialMod(n, mod);
            long result = fm.factorial(n);
            
            for (int g : groups) {
                result = (result * fm.invFact[g]) % mod;
            }
            
            return result;
        }
    }

    /**
     * USAGE EXAMPLES AND TEST CASES
     */
    public static void main(String[] args) {
        // Example 1: Factorial and combinations
        FactorialMod fm = new FactorialMod(20, MOD);
        System.out.println("C(10, 3) = " + fm.nCr(10, 3)); // 120
        System.out.println("P(10, 3) = " + fm.nPr(10, 3)); // 720
        System.out.println("10! = " + fm.factorial(10)); // 3628800
        
        // Example 2: Lucas' theorem for large n,k
        LucasTheorem lt = new LucasTheorem(1000003);
        System.out.println("\nC(1000000, 500000) mod 1000003 = " + 
                         lt.nCrModP(1000000, 500000));
        
        // Example 3: Catalan numbers
        System.out.println("\nFirst 10 Catalan numbers:");
        long[] catalan = CatalanNumbers.catalanDP(10, MOD);
        for (int i = 0; i <= 10; i++) {
            System.out.println("C_" + i + " = " + catalan[i]);
        }
        // C_0=1, C_1=1, C_2=2, C_3=5, C_4=14, C_5=42, ...
        
        // Example 4: Stirling numbers
        System.out.println("\nStirling numbers of second kind (first 6):");
        long[][] S2 = StirlingNumbers.stirlingSecond(6, MOD);
        for (int n = 0; n <= 6; n++) {
            System.out.print("n=" + n + ": ");
            for (int k = 0; k <= n; k++) {
                System.out.print(S2[n][k] + " ");
            }
            System.out.println();
        }
        
        // Example 5: Derangements
        System.out.println("\nDerangements:");
        long[] derang = Derangements.derangements(10, MOD);
        for (int i = 0; i <= 5; i++) {
            System.out.println("D_" + i + " = " + derang[i]);
        }
        // D_0=1, D_1=0, D_2=1, D_3=2, D_4=9, D_5=44
        
        // Example 6: Binomial coefficients
        System.out.println("\nBinomial C(20, 10) = " + 
                         BinomialCoefficients.nCr(20, 10)); // 184756
        
        // Example 7: Multinomial
        int[] groups = {3, 2, 5}; // Partition 10 into groups of 3, 2, 5
        long multi = MultinomialCoefficients.multinomial(groups, MOD);
        System.out.println("\nMultinomial(10; 3,2,5) = " + multi); // 2520
        
        // Example 8: Practical application - Unique Paths
        // LeetCode 62: m x n grid, paths from (0,0) to (m-1,n-1)
        // Answer: C(m+n-2, m-1)
        int m = 3, n = 7;
        long paths = fm.nCr(m + n - 2, m - 1);
        System.out.println("\nUnique paths in " + m + "x" + n + " grid: " + paths); // 28
    }
}
