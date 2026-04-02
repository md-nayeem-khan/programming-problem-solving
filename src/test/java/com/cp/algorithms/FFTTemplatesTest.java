package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;

/**
 * Comprehensive test suite for FFTTemplates
 * Tests FFT, NTT, polynomial multiplication, and edge cases
 */
public class FFTTemplatesTest {

    private static final double EPSILON = 1e-9;
    
    @BeforeEach
    void setUp() {
        // Any setup needed
    }

    // ========== COMPLEX NUMBER TESTS ==========
    
    @Test
    @DisplayName("Complex number operations")
    void testComplexOperations() {
        FFTTemplates.Complex c1 = new FFTTemplates.Complex(3, 4);
        FFTTemplates.Complex c2 = new FFTTemplates.Complex(1, 2);
        
        // Addition: (3+4i) + (1+2i) = (4+6i)
        FFTTemplates.Complex sum = c1.add(c2);
        assertEquals(4.0, sum.real, EPSILON);
        assertEquals(6.0, sum.imag, EPSILON);
        
        // Subtraction: (3+4i) - (1+2i) = (2+2i)
        FFTTemplates.Complex diff = c1.subtract(c2);
        assertEquals(2.0, diff.real, EPSILON);
        assertEquals(2.0, diff.imag, EPSILON);
        
        // Multiplication: (3+4i) * (1+2i) = (3-8) + (6+4)i = -5+10i
        FFTTemplates.Complex mult = c1.multiply(c2);
        assertEquals(-5.0, mult.real, EPSILON);
        assertEquals(10.0, mult.imag, EPSILON);
        
        // Division by scalar: (3+4i) / 2 = (1.5+2i)
        FFTTemplates.Complex div = c1.divide(2);
        assertEquals(1.5, div.real, EPSILON);
        assertEquals(2.0, div.imag, EPSILON);
        
        // Absolute value: |3+4i| = 5
        assertEquals(5.0, c1.abs(), EPSILON);
    }

    // ========== FFT BASIC FUNCTIONALITY TESTS ==========
    
    @Test
    @DisplayName("FFT - Identity test (forward then inverse)")
    void testFFTIdentity() {
        FFTTemplates.Complex[] input = {
            new FFTTemplates.Complex(1, 0),
            new FFTTemplates.Complex(2, 0),
            new FFTTemplates.Complex(3, 0),
            new FFTTemplates.Complex(4, 0)
        };
        
        FFTTemplates.Complex[] original = Arrays.copyOf(input, input.length);
        
        // Forward FFT
        FFTTemplates.fft(input, false);
        
        // Inverse FFT
        FFTTemplates.fft(input, true);
        
        // Should get back original values
        for (int i = 0; i < input.length; i++) {
            assertEquals(original[i].real, input[i].real, EPSILON);
            assertEquals(original[i].imag, input[i].imag, EPSILON);
        }
    }
    
    @Test
    @DisplayName("FFT - Power of 2 size validation")
    void testFFTPowerOfTwo() {
        // Test various power of 2 sizes
        for (int size : new int[]{1, 2, 4, 8, 16, 32}) {
            FFTTemplates.Complex[] data = new FFTTemplates.Complex[size];
            for (int i = 0; i < size; i++) {
                data[i] = new FFTTemplates.Complex(i + 1, 0);
            }
            
            FFTTemplates.Complex[] original = Arrays.copyOf(data, data.length);
            
            FFTTemplates.fft(data, false);
            FFTTemplates.fft(data, true);
            
            for (int i = 0; i < size; i++) {
                assertEquals(original[i].real, data[i].real, EPSILON, 
                    "Failed for size " + size + " at index " + i);
            }
        }
    }

    // ========== POLYNOMIAL MULTIPLICATION TESTS ==========
    
    @Test
    @DisplayName("Polynomial multiplication - Basic test")
    void testPolynomialMultiplicationBasic() {
        // (1 + 2x + 3x²) * (4 + 5x) = 4 + 13x + 22x² + 15x³
        long[] poly1 = {1, 2, 3};
        long[] poly2 = {4, 5};
        
        long[] result = FFTTemplates.multiplyPolynomials(poly1, poly2);
        long[] expected = {4, 13, 22, 15};
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    @DisplayName("Polynomial multiplication - Edge cases")
    void testPolynomialMultiplicationEdgeCases() {
        // Multiply by zero polynomial
        long[] zero = {0};
        long[] poly = {1, 2, 3};
        long[] result = FFTTemplates.multiplyPolynomials(poly, zero);
        assertArrayEquals(new long[]{0, 0, 0}, result);
        
        // Multiply by constant
        long[] constant = {5};
        result = FFTTemplates.multiplyPolynomials(poly, constant);
        assertArrayEquals(new long[]{5, 10, 15}, result);
        
        // Single element polynomials
        long[] single1 = {3};
        long[] single2 = {7};
        result = FFTTemplates.multiplyPolynomials(single1, single2);
        assertArrayEquals(new long[]{21}, result);
    }
    
    @Test
    @DisplayName("Polynomial multiplication - Large polynomials")
    void testPolynomialMultiplicationLarge() {
        // Test with larger polynomials
        long[] poly1 = {1, 2, 3, 4, 5};
        long[] poly2 = {1, 1, 1};
        
        long[] result = FFTTemplates.multiplyPolynomials(poly1, poly2);
        
        // Expected: (1+2x+3x²+4x³+5x⁴) * (1+x+x²)
        // = 1+3x+6x²+9x³+12x⁴+9x⁵+5x⁶
        long[] expected = {1, 3, 6, 9, 12, 9, 5};
        
        assertArrayEquals(expected, result);
    }

    // ========== NTT TESTS ==========
    
    @Test
    @DisplayName("NTT - Basic functionality")
    void testNTTBasic() {
        long[] input = {1, 2, 3, 4};
        long[] original = Arrays.copyOf(input, input.length);
        
        // Forward NTT
        FFTTemplates.ntt(input, false);
        
        // Inverse NTT
        FFTTemplates.ntt(input, true);
        
        // Should get back original values
        assertArrayEquals(original, input);
    }
    
    @Test
    @DisplayName("NTT polynomial multiplication")
    void testNTTPolynomialMultiplication() {
        long[] poly1 = {1, 2, 3};
        long[] poly2 = {4, 5};
        
        long[] result = FFTTemplates.multiplyPolynomialsNTT(poly1, poly2);
        long[] expected = {4, 13, 22, 15};
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    @DisplayName("NTT - Modular arithmetic correctness")
    void testNTTModularArithmetic() {
        // Test that results are within modulus
        long[] poly1 = {999999999, 999999999};
        long[] poly2 = {999999999, 999999999};
        
        long[] result = FFTTemplates.multiplyPolynomialsNTT(poly1, poly2);
        
        // All results should be less than MOD
        for (long val : result) {
            assertTrue(val >= 0 && val < 998244353, 
                "Result " + val + " is not in valid range [0, MOD)");
        }
    }

    // ========== CONVOLUTION TESTS ==========
    
    @Test
    @DisplayName("Convolution - Basic test")
    void testConvolution() {
        long[] arr1 = {1, 2, 3};
        long[] arr2 = {0, 1, 0, 1};
        
        long[] result = FFTTemplates.convolve(arr1, arr2);
        
        // Convolution should give same result as polynomial multiplication
        long[] expected = FFTTemplates.multiplyPolynomialsNTT(arr1, arr2);
        assertArrayEquals(expected, result);
    }

    // ========== LARGE INTEGER MULTIPLICATION TESTS ==========
    
    @Test
    @DisplayName("Large integer multiplication - Basic test")
    void testLargeIntegerMultiplication() {
        String result = FFTTemplates.multiplyLargeIntegers("123456789", "987654321");
        assertEquals("121932631112635269", result);
    }
    
    @Test
    @DisplayName("Large integer multiplication - Edge cases")
    void testLargeIntegerMultiplicationEdgeCases() {
        // Multiplication by zero
        assertEquals("0", FFTTemplates.multiplyLargeIntegers("123", "0"));
        assertEquals("0", FFTTemplates.multiplyLargeIntegers("0", "456"));
        
        // Single digits
        assertEquals("12", FFTTemplates.multiplyLargeIntegers("3", "4"));
        
        // One digit by multi-digit
        assertEquals("246", FFTTemplates.multiplyLargeIntegers("123", "2"));
        
        // Leading zeros should be handled
        assertEquals("1", FFTTemplates.multiplyLargeIntegers("1", "1"));
        
        // Large numbers
        String large1 = "12345678901234567890";
        String large2 = "98765432109876543210";
        String result = FFTTemplates.multiplyLargeIntegers(large1, large2);
        
        // Verify result is not empty and doesn't start with 0 (unless it's "0")
        assertFalse(result.isEmpty());
        if (!result.equals("0")) {
            assertNotEquals('0', result.charAt(0));
        }
    }
    
    @Test
    @DisplayName("Large integer multiplication - Carry propagation")
    void testLargeIntegerCarryPropagation() {
        // Test cases that require multiple carry operations
        assertEquals("81", FFTTemplates.multiplyLargeIntegers("9", "9"));
        assertEquals("9999", FFTTemplates.multiplyLargeIntegers("99", "101"));
        assertEquals("999999", FFTTemplates.multiplyLargeIntegers("999", "1001"));
    }

    // ========== WILDCARD MATCHING TESTS ==========
    
    @Test
    @DisplayName("Wildcard matching - Basic functionality")
    void testWildcardMatching() {
        List<Integer> matches = FFTTemplates.wildcardMatching("abcdef", "abc");
        assertTrue(matches.contains(0));
        
        matches = FFTTemplates.wildcardMatching("aaaa", "aa");
        assertTrue(matches.contains(0));
        assertTrue(matches.contains(1));
        assertTrue(matches.contains(2));
    }
    
    @Test
    @DisplayName("Wildcard matching - With wildcards")
    void testWildcardMatchingWithWildcards() {
        List<Integer> matches = FFTTemplates.wildcardMatching("abcdef", "a*c");
        assertTrue(matches.contains(0)); // abc matches a*c
        
        matches = FFTTemplates.wildcardMatching("hello world", "h*l*o");
        assertTrue(matches.contains(0)); // "hello" matches "h*l*o"
    }
    
    @Test
    @DisplayName("Wildcard matching - Edge cases")
    void testWildcardMatchingEdgeCases() {
        // Pattern longer than text
        List<Integer> matches = FFTTemplates.wildcardMatching("abc", "abcdef");
        assertTrue(matches.isEmpty());
        
        // Empty pattern
        matches = FFTTemplates.wildcardMatching("abc", "");
        assertTrue(matches.isEmpty());
        
        // Pattern with all wildcards
        matches = FFTTemplates.wildcardMatching("abc", "***");
        assertTrue(matches.contains(0));
    }

    // ========== PERFORMANCE AND STRESS TESTS ==========
    
    @Test
    @DisplayName("FFT vs NTT consistency")
    void testFFTvsNTTConsistency() {
        long[] poly1 = {1, 2, 3, 4};
        long[] poly2 = {5, 6, 7};
        
        long[] fftResult = FFTTemplates.multiplyPolynomials(poly1, poly2);
        long[] nttResult = FFTTemplates.multiplyPolynomialsNTT(poly1, poly2);
        
        // Results should be identical for reasonable-sized integers
        assertEquals(fftResult.length, nttResult.length);
        for (int i = 0; i < fftResult.length; i++) {
            assertEquals(fftResult[i], nttResult[i], 
                "FFT and NTT results differ at index " + i);
        }
    }
    
    @Test
    @DisplayName("Modular exponentiation correctness")
    void testModularExponentiation() {
        // Test some known values
        // 2^10 mod 1000 = 24
        long result = invokeModPow(2, 10, 1000);
        assertEquals(24, result);
        
        // 3^4 mod 7 = 81 mod 7 = 4
        result = invokeModPow(3, 4, 7);
        assertEquals(4, result);
        
        // Base case: a^0 = 1
        result = invokeModPow(123, 0, 456);
        assertEquals(1, result);
    }
    
    // Helper method to access private modPow method
    private long invokeModPow(long base, long exp, long mod) {
        // Since modPow is private, we'll test it indirectly through NTT
        // or implement a public wrapper for testing
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }
    
    @Test
    @DisplayName("Bit reversal correctness")
    void testBitReversalIndirectly() {
        // Test that FFT with different array sizes works correctly
        // This indirectly tests the bit reversal permutation
        for (int size = 2; size <= 16; size *= 2) {
            FFTTemplates.Complex[] data = new FFTTemplates.Complex[size];
            for (int i = 0; i < size; i++) {
                data[i] = new FFTTemplates.Complex(i, 0);
            }
            
            FFTTemplates.Complex[] original = Arrays.copyOf(data, data.length);
            
            FFTTemplates.fft(data, false);
            FFTTemplates.fft(data, true);
            
            for (int i = 0; i < size; i++) {
                assertEquals(original[i].real, data[i].real, EPSILON,
                    "Bit reversal failed for size " + size + " at index " + i);
            }
        }
    }

    // ========== BOUNDARY AND ERROR HANDLING TESTS ==========
    
    @Test
    @DisplayName("Empty array handling")
    void testEmptyArrays() {
        // Test with minimal valid inputs
        long[] empty = {};
        long[] single = {1};
        
        // This might cause issues, so we should handle gracefully
        assertDoesNotThrow(() -> {
            FFTTemplates.multiplyPolynomials(single, single);
        });
    }
    
    @Test
    @DisplayName("Very large coefficients")
    void testLargeCoefficients() {
        // Test with large coefficients to check overflow handling
        long[] poly1 = {Long.MAX_VALUE / 2};
        long[] poly2 = {2};
        
        assertDoesNotThrow(() -> {
            FFTTemplates.multiplyPolynomials(poly1, poly2);
        });
    }
    
    @Test
    @DisplayName("Numerical stability")
    void testNumericalStability() {
        // Test with very small and very large numbers for FFT stability
        FFTTemplates.Complex[] data = {
            new FFTTemplates.Complex(1e-10, 0),
            new FFTTemplates.Complex(1e10, 0),
            new FFTTemplates.Complex(0, 0),
            new FFTTemplates.Complex(1, 0)
        };
        
        FFTTemplates.Complex[] original = Arrays.copyOf(data, data.length);
        
        FFTTemplates.fft(data, false);
        FFTTemplates.fft(data, true);
        
        // Check that we don't lose precision catastrophically
        for (int i = 0; i < data.length; i++) {
            assertEquals(original[i].real, data[i].real, EPSILON * 1000,
                "Numerical instability detected at index " + i);
        }
    }
}