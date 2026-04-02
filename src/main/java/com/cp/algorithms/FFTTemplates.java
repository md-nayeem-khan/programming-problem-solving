package com.cp.algorithms;

import com.cp.problems.Complexity;

/**
 * Fast Fourier Transform (FFT) & Number Theoretic Transform (NTT)
 * HIGH PRIORITY for Google (rare but important)
 * 
 * Essential for:
 * - Polynomial multiplication in O(n log n)
 * - Convolution of arrays
 * - Large integer multiplication
 * - Signal processing
 * - String matching (advanced)
 * 
 * Interview Questions:
 * - Multiply two polynomials efficiently
 * - Large integer multiplication
 * - Pattern matching with wildcards
 * - Convolution problems
 * 
 * Key Concepts:
 * - FFT: Works with complex numbers
 * - NTT: Works with modular arithmetic (faster, exact)
 * - Cooley-Tukey algorithm: Divide-and-conquer
 * - Time complexity: O(n log n) vs O(n²) naive
 * 
 * Companies: Google (4/5), Competitive Programming (5/5)
 */
public class FFTTemplates {
    
    // ========== COMPLEX NUMBER CLASS ==========
    
    /**
     * Complex number for FFT
     */
    static class Complex {
        double real, imag;
        
        public Complex(double real, double imag) {
            this.real = real;
            this.imag = imag;
        }
        
        public Complex add(Complex other) {
            return new Complex(real + other.real, imag + other.imag);
        }
        
        public Complex subtract(Complex other) {
            return new Complex(real - other.real, imag - other.imag);
        }
        
        public Complex multiply(Complex other) {
            double r = real * other.real - imag * other.imag;
            double i = real * other.imag + imag * other.real;
            return new Complex(r, i);
        }
        
        public Complex divide(double d) {
            return new Complex(real / d, imag / d);
        }
        
        public double abs() {
            return Math.sqrt(real * real + imag * imag);
        }
        
        @Override
        public String toString() {
            return String.format("%.2f + %.2fi", real, imag);
        }
    }
    
    // ========== FAST FOURIER TRANSFORM ==========
    
    /**
     * FFT using Cooley-Tukey algorithm (iterative, in-place)
     * 
     * @param a array of complex numbers (size must be power of 2)
     * @param invert false for FFT, true for inverse FFT
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static void fft(Complex[] a, boolean invert) {
        int n = a.length;
        
        // Bit-reversal permutation
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >> 1;
            while ((j & bit) != 0) {
                j ^= bit;
                bit >>= 1;
            }
            j ^= bit;
            
            if (i < j) {
                Complex temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        
        // FFT computation
        for (int len = 2; len <= n; len <<= 1) {
            double ang = 2 * Math.PI / len * (invert ? -1 : 1);
            Complex wlen = new Complex(Math.cos(ang), Math.sin(ang));
            
            for (int i = 0; i < n; i += len) {
                Complex w = new Complex(1, 0);
                
                for (int j = 0; j < len / 2; j++) {
                    Complex u = a[i + j];
                    Complex v = a[i + j + len / 2].multiply(w);
                    
                    a[i + j] = u.add(v);
                    a[i + j + len / 2] = u.subtract(v);
                    
                    w = w.multiply(wlen);
                }
            }
        }
        
        // Inverse FFT normalization
        if (invert) {
            for (int i = 0; i < n; i++) {
                a[i] = a[i].divide(n);
            }
        }
    }
    
    /**
     * Multiply two polynomials using FFT
     * 
     * @param a coefficients of first polynomial
     * @param b coefficients of second polynomial
     * @return coefficients of product polynomial
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static long[] multiplyPolynomials(long[] a, long[] b) {
        int resultSize = a.length + b.length - 1;
        int n = 1;
        while (n < resultSize) n <<= 1;
        
        Complex[] fa = new Complex[n];
        Complex[] fb = new Complex[n];
        
        for (int i = 0; i < n; i++) {
            fa[i] = new Complex(i < a.length ? a[i] : 0, 0);
            fb[i] = new Complex(i < b.length ? b[i] : 0, 0);
        }
        
        fft(fa, false);
        fft(fb, false);
        
        for (int i = 0; i < n; i++) {
            fa[i] = fa[i].multiply(fb[i]);
        }
        
        fft(fa, true);
        
        long[] result = new long[resultSize];
        for (int i = 0; i < resultSize; i++) {
            result[i] = Math.round(fa[i].real);
        }
        
        return result;
    }
    
    // ========== NUMBER THEORETIC TRANSFORM (NTT) ==========
    
    /**
     * NTT - FFT using modular arithmetic
     * Works with prime modulus of form p = c * 2^k + 1
     * 
     * Common moduli:
     * - 998244353 = 119 * 2^23 + 1 (primitive root = 3)
     * - 1004535809 = 479 * 2^21 + 1 (primitive root = 3)
     */
    private static final long MOD = 998244353;
    private static final long ROOT = 3; // Primitive root of MOD
    
    /**
     * Modular exponentiation
     */
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
    
    /**
     * NTT transform
     * 
     * @param a array of numbers (size must be power of 2)
     * @param invert false for NTT, true for inverse NTT
     */
    @Complexity(time = "O(n log n)", space = "O(1)")
    public static void ntt(long[] a, boolean invert) {
        int n = a.length;
        
        // Bit-reversal permutation
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >> 1;
            while ((j & bit) != 0) {
                j ^= bit;
                bit >>= 1;
            }
            j ^= bit;
            
            if (i < j) {
                long temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        
        // NTT computation
        for (int len = 2; len <= n; len <<= 1) {
            long w = invert ? modPow(ROOT, MOD - 1 - (MOD - 1) / len, MOD) 
                            : modPow(ROOT, (MOD - 1) / len, MOD);
            
            for (int i = 0; i < n; i += len) {
                long wn = 1;
                
                for (int j = 0; j < len / 2; j++) {
                    long u = a[i + j];
                    long v = (a[i + j + len / 2] * wn) % MOD;
                    
                    a[i + j] = (u + v) % MOD;
                    a[i + j + len / 2] = (u - v + MOD) % MOD;
                    
                    wn = (wn * w) % MOD;
                }
            }
        }
        
        // Inverse NTT normalization
        if (invert) {
            long nInv = modPow(n, MOD - 2, MOD);
            for (int i = 0; i < n; i++) {
                a[i] = (a[i] * nInv) % MOD;
            }
        }
    }
    
    /**
     * Multiply two polynomials using NTT (modular arithmetic)
     * 
     * @param a coefficients of first polynomial
     * @param b coefficients of second polynomial
     * @return coefficients of product polynomial (mod MOD)
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static long[] multiplyPolynomialsNTT(long[] a, long[] b) {
        int resultSize = a.length + b.length - 1;
        int n = 1;
        while (n < resultSize) n <<= 1;
        
        long[] fa = new long[n];
        long[] fb = new long[n];
        
        System.arraycopy(a, 0, fa, 0, a.length);
        System.arraycopy(b, 0, fb, 0, b.length);
        
        ntt(fa, false);
        ntt(fb, false);
        
        for (int i = 0; i < n; i++) {
            fa[i] = (fa[i] * fb[i]) % MOD;
        }
        
        ntt(fa, true);
        
        long[] result = new long[resultSize];
        System.arraycopy(fa, 0, result, 0, resultSize);
        
        return result;
    }
    
    // ========== CONVOLUTION ==========
    
    /**
     * Compute convolution of two arrays
     * 
     * Convolution: c[k] = sum(a[i] * b[k-i]) for all valid i
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static long[] convolve(long[] a, long[] b) {
        return multiplyPolynomialsNTT(a, b);
    }
    
    // ========== LARGE INTEGER MULTIPLICATION ==========
    
    /**
     * Multiply two large integers represented as strings
     * 
     * @param num1 first number as string
     * @param num2 second number as string
     * @return product as string
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static String multiplyLargeIntegers(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) return "0";
        
        int n1 = num1.length();
        int n2 = num2.length();
        
        long[] a = new long[n1];
        long[] b = new long[n2];
        
        // Reverse order for easier computation
        for (int i = 0; i < n1; i++) {
            a[i] = num1.charAt(n1 - 1 - i) - '0';
        }
        for (int i = 0; i < n2; i++) {
            b[i] = num2.charAt(n2 - 1 - i) - '0';
        }
        
        long[] product = multiplyPolynomials(a, b);
        
        // Handle carries
        int carry = 0;
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < product.length || carry > 0; i++) {
            long digit = (i < product.length ? product[i] : 0) + carry;
            result.append(digit % 10);
            carry = (int)(digit / 10);
        }
        
        // Remove leading zeros and reverse
        while (result.length() > 1 && result.charAt(result.length() - 1) == '0') {
            result.deleteCharAt(result.length() - 1);
        }
        
        return result.reverse().toString();
    }
    
    // ========== STRING MATCHING WITH WILDCARDS ==========
    
    /**
     * Find all occurrences of pattern in text (with wildcards)
     * Wildcard '*' matches any character
     * 
     * NOTE: This is a simplified O(nm) implementation for demonstration.
     * Full FFT-based wildcard matching is more complex and requires
     * separate FFTs for each character in the alphabet.
     */
    @Complexity(time = "O(n*m)", space = "O(1)")
    public static java.util.List<Integer> wildcardMatching(String text, String pattern) {
        java.util.List<Integer> matches = new java.util.ArrayList<>();
        
        int n = text.length();
        int m = pattern.length();
        
        if (m > n) return matches;
        if (m == 0) return matches; // Empty pattern matches nothing
        
        // Check all possible positions
        for (int i = 0; i <= n - m; i++) {
            boolean match = true;
            for (int j = 0; j < m; j++) {
                if (pattern.charAt(j) != '*' && pattern.charAt(j) != text.charAt(i + j)) {
                    match = false;
                    break;
                }
            }
            if (match) matches.add(i);
        }
        
        return matches;
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Fast Fourier Transform Examples ===\n");
        
        // Example 1: Polynomial multiplication
        System.out.println("1. Polynomial Multiplication:");
        long[] poly1 = {1, 2, 3}; // 1 + 2x + 3x²
        long[] poly2 = {4, 5};    // 4 + 5x
        
        System.out.println("   P(x) = 1 + 2x + 3x²");
        System.out.println("   Q(x) = 4 + 5x");
        
        long[] product = multiplyPolynomials(poly1, poly2);
        System.out.print("   P(x) × Q(x) = ");
        for (int i = 0; i < product.length; i++) {
            if (i > 0) System.out.print(" + ");
            System.out.print(product[i]);
            if (i > 0) System.out.print("x");
            if (i > 1) System.out.print("^" + i);
        }
        System.out.println("\n   Expected: 4 + 13x + 22x² + 15x³");
        
        // Example 2: NTT polynomial multiplication
        System.out.println("\n2. NTT Polynomial Multiplication (mod " + MOD + "):");
        long[] nttProduct = multiplyPolynomialsNTT(poly1, poly2);
        System.out.print("   Result: ");
        for (int i = 0; i < nttProduct.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(nttProduct[i]);
        }
        System.out.println();
        
        // Example 3: Large integer multiplication
        System.out.println("\n3. Large Integer Multiplication:");
        String num1 = "123456789";
        String num2 = "987654321";
        System.out.println("   " + num1 + " × " + num2);
        System.out.println("   = " + multiplyLargeIntegers(num1, num2));
        System.out.println("   Expected: 121932631112635269");
        
        // Example 4: Convolution
        System.out.println("\n4. Convolution:");
        long[] arr1 = {1, 2, 3};
        long[] arr2 = {0, 1, 0, 1};
        long[] conv = convolve(arr1, arr2);
        System.out.print("   Convolution of [1,2,3] and [0,1,0,1]: ");
        for (int i = 0; i < Math.min(conv.length, 10); i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(conv[i]);
        }
        System.out.println();
        
        // Example 5: Performance comparison
        System.out.println("\n5. Performance Comparison:");
        System.out.println("   Naive multiplication: O(n²)");
        System.out.println("   FFT multiplication: O(n log n)");
        System.out.println("   For n=10^6: FFT is ~50,000x faster!");
        
        System.out.println("\n=== Key Points ===");
        System.out.println("FFT: Use for floating-point, approximate results");
        System.out.println("NTT: Use for exact integer results (modular arithmetic)");
        System.out.println("Applications: Polynomial multiplication, convolution, large integers");
        System.out.println("Complexity: O(n log n) vs O(n²) naive");
    }
}
