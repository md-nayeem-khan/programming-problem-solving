package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Digit Dynamic Programming - HIGH PRIORITY for Google (15%), Meta (10%)
 * 
 * Essential for:
 * - Count numbers in range with digit properties
 * - Numbers without adjacent equal digits
 * - Numbers with digit sum constraints
 * - Numbers divisible by certain values
 * - License plate combinations
 * 
 * Interview Questions:
 * - Count numbers with no repeating digits
 * - Numbers where digit sum is divisible by k
 * - Count integers with even digit sum
 * - Numbers with specific digit patterns
 * 
 * Pattern: Use DP with states (position, tight bound, additional constraints)
 * - position: current digit position
 * - tight: whether we're still bounded by the limit
 * - Additional: last digit, sum, etc.
 * 
 * Companies: Google (4/5), Meta (4/5), Amazon (3/5)
 */
public class DigitDPTemplates {
    
    // ========== BASIC DIGIT DP FRAMEWORK ==========
    
    /**
     * Count numbers from 0 to n with a given property
     * 
     * Template for digit DP problems
     * State: dp[pos][tight][...additional states...]
     * - pos: current position (0 = leftmost)
     * - tight: 1 if we're still bounded by n, 0 otherwise
     */
    
    /**
     * Count numbers from L to R with given property
     */
    @Complexity(time = "O(log n × states)", space = "O(log n × states)")
    public static long countInRange(long L, long R, DigitDPPredicate predicate) {
        long countR = countUpTo(R, predicate);
        long countL = L > 0 ? countUpTo(L - 1, predicate) : 0;
        return countR - countL;
    }
    
    /**
     * Count numbers from 0 to n satisfying predicate
     */
    private static long countUpTo(long n, DigitDPPredicate predicate) {
        if (n < 0) return 0;
        
        String numStr = String.valueOf(n);
        int len = numStr.length();
        
        // dp[pos][tight][...states...]
        Map<String, Long> memo = new HashMap<>();
        
        return predicate.count(numStr, 0, true, memo);
    }
    
    /**
     * Interface for digit DP predicates
     */
    interface DigitDPPredicate {
        long count(String num, int pos, boolean tight, Map<String, Long> memo);
    }
    
    // ========== PROBLEM 1: COUNT NUMBERS WITHOUT REPEATING DIGITS ==========
    
    /**
     * Count numbers from 0 to n with no repeating adjacent digits
     * 
     * Example: n = 100
     * Valid: 12, 23, 34, 101, ...
     * Invalid: 11, 22, 100, ...
     */
    @Complexity(time = "O(log n × 10)", space = "O(log n × 10)")
    public static long countNoAdjacentRepeats(long n) {
        if (n < 0) return 0;
        
        String numStr = String.valueOf(n);
        Long[][][] dp = new Long[numStr.length()][2][11]; // [pos][tight][lastDigit+1] (lastDigit: -1 to 9)
        
        return dfsNoAdjacent(numStr, 0, true, -1, dp);
    }
    
    private static long dfsNoAdjacent(String num, int pos, boolean tight, 
                                      int lastDigit, Long[][][] dp) {
        if (pos == num.length()) {
            return 1; // Found a valid number
        }
        
        int tightIdx = tight ? 1 : 0;
        int lastIdx = lastDigit + 1; // Shift by 1 since lastDigit can be -1 (range: 0-10)
        
        if (lastIdx < 0 || lastIdx >= dp[0][0].length) {
            throw new IllegalStateException("lastIdx out of bounds: " + lastIdx);
        }
        
        if (dp[pos][tightIdx][lastIdx] != null) {
            return dp[pos][tightIdx][lastIdx];
        }
        
        int limit = tight ? (num.charAt(pos) - '0') : 9;
        long count = 0;
        
        for (int digit = 0; digit <= limit; digit++) {
            // Skip if digit equals last digit (adjacent repeat)
            if (digit == lastDigit && lastDigit != -1) {
                continue;
            }
            
            boolean newTight = tight && (digit == limit);
            int newLast = (pos == 0 && digit == 0) ? -1 : digit; // Leading zeros
            
            count += dfsNoAdjacent(num, pos + 1, newTight, newLast, dp);
        }
        
        dp[pos][tightIdx][lastIdx] = count;
        return count;
    }
    
    // ========== PROBLEM 2: DIGIT SUM DIVISIBLE BY K ==========
    
    /**
     * Count numbers from 0 to n where digit sum is divisible by k
     * 
     * Example: n = 100, k = 3
     * Valid: 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, ...
     */
    @Complexity(time = "O(log n × k)", space = "O(log n × k)")
    public static long countDigitSumDivisibleByK(long n, int k) {
        if (n < 0) return 0;
        
        String numStr = String.valueOf(n);
        Long[][][] dp = new Long[numStr.length()][2][k]; // [pos][tight][sum%k]
        
        return dfsDigitSum(numStr, 0, true, 0, k, dp);
    }
    
    private static long dfsDigitSum(String num, int pos, boolean tight, 
                                     int sumMod, int k, Long[][][] dp) {
        if (pos == num.length()) {
            return sumMod == 0 ? 1 : 0;
        }
        
        int tightIdx = tight ? 1 : 0;
        
        if (dp[pos][tightIdx][sumMod] != null) {
            return dp[pos][tightIdx][sumMod];
        }
        
        int limit = tight ? (num.charAt(pos) - '0') : 9;
        long count = 0;
        
        for (int digit = 0; digit <= limit; digit++) {
            boolean newTight = tight && (digit == limit);
            int newSumMod = (sumMod + digit) % k;
            
            count += dfsDigitSum(num, pos + 1, newTight, newSumMod, k, dp);
        }
        
        dp[pos][tightIdx][sumMod] = count;
        return count;
    }
    
    // ========== PROBLEM 3: COUNT NUMBERS WITH EVEN DIGIT SUM ==========
    
    /**
     * Count numbers from L to R with even digit sum
     * 
     * Example: L = 10, R = 20
     * Valid: 11 (1+1=2), 13 (1+3=4), 15, 17, 19, 20
     */
    @Complexity(time = "O(log n)", space = "O(log n)")
    public static long countEvenDigitSum(long L, long R) {
        return countDigitSumParity(R, 0) - (L > 0 ? countDigitSumParity(L - 1, 0) : 0);
    }
    
    /**
     * Count numbers with odd digit sum
     */
    public static long countOddDigitSum(long L, long R) {
        return countDigitSumParity(R, 1) - (L > 0 ? countDigitSumParity(L - 1, 1) : 0);
    }
    
    private static long countDigitSumParity(long n, int targetParity) {
        if (n < 0) return 0;
        
        String numStr = String.valueOf(n);
        Long[][][] dp = new Long[numStr.length()][2][2]; // [pos][tight][parity]
        
        return dfsDigitParity(numStr, 0, true, 0, targetParity, dp);
    }
    
    private static long dfsDigitParity(String num, int pos, boolean tight, 
                                        int parity, int targetParity, Long[][][] dp) {
        if (pos == num.length()) {
            return parity == targetParity ? 1 : 0;
        }
        
        int tightIdx = tight ? 1 : 0;
        
        if (dp[pos][tightIdx][parity] != null) {
            return dp[pos][tightIdx][parity];
        }
        
        int limit = tight ? (num.charAt(pos) - '0') : 9;
        long count = 0;
        
        for (int digit = 0; digit <= limit; digit++) {
            boolean newTight = tight && (digit == limit);
            int newParity = (parity + digit) % 2;
            
            count += dfsDigitParity(num, pos + 1, newTight, newParity, targetParity, dp);
        }
        
        dp[pos][tightIdx][parity] = count;
        return count;
    }
    
    // ========== PROBLEM 4: NUMBERS WITH UNIQUE DIGITS ==========
    
    /**
     * Count numbers with all unique digits (no repeating digits anywhere)
     * 
     * Example: n = 100
     * Valid: 1, 2, 3, ..., 10, 12, 13, ..., 20, 21, 23, ... (no 11, 22, etc.)
     */
    @Complexity(time = "O(log n × 2^10)", space = "O(log n × 2^10)")
    public static long countUniqueDigits(long n) {
        if (n < 0) return 0;
        
        String numStr = String.valueOf(n);
        Map<String, Long> memo = new HashMap<>();
        
        return dfsUniqueDigits(numStr, 0, true, 0, memo);
    }
    
    private static long dfsUniqueDigits(String num, int pos, boolean tight, 
                                         int usedMask, Map<String, Long> memo) {
        if (pos == num.length()) {
            return 1;
        }
        
        String key = pos + "," + (tight ? 1 : 0) + "," + usedMask;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        int limit = tight ? (num.charAt(pos) - '0') : 9;
        long count = 0;
        
        for (int digit = 0; digit <= limit; digit++) {
            // Skip if digit already used
            if ((usedMask & (1 << digit)) != 0) {
                continue;
            }
            
            boolean newTight = tight && (digit == limit);
            int newMask = usedMask | (1 << digit);
            
            // Don't mark leading zeros as used
            if (pos == 0 && digit == 0) {
                newMask = 0;
            }
            
            count += dfsUniqueDigits(num, pos + 1, newTight, newMask, memo);
        }
        
        memo.put(key, count);
        return count;
    }
    
    // ========== PROBLEM 5: NUMBERS DIVISIBLE BY D ==========
    
    /**
     * Count numbers from L to R divisible by d
     * 
     * This is simple: R/d - (L-1)/d, but shown for DP pattern
     */
    @Complexity(time = "O(log n × d)", space = "O(log n × d)")
    public static long countDivisibleBy(long L, long R, int d) {
        // Simple solution
        // Special case: if L is 0, count it separately since 0 is divisible by any number
        if (L == 0) {
            return 1 + (R / d);
        }
        long countR = R / d;
        long countL = (L - 1) / d;
        return countR - countL;
    }
    
    /**
     * Digit DP version (for educational purposes)
     */
    @Complexity(time = "O(log n × d)", space = "O(log n × d)")
    public static long countDivisibleByDP(long n, int d) {
        if (n < 0) return 0;
        
        String numStr = String.valueOf(n);
        Long[][][] dp = new Long[numStr.length()][2][d]; // [pos][tight][num%d]
        
        return dfsDivisible(numStr, 0, true, 0, d, dp);
    }
    
    private static long dfsDivisible(String num, int pos, boolean tight, 
                                      int remainder, int d, Long[][][] dp) {
        if (pos == num.length()) {
            return remainder == 0 ? 1 : 0;
        }
        
        int tightIdx = tight ? 1 : 0;
        
        if (dp[pos][tightIdx][remainder] != null) {
            return dp[pos][tightIdx][remainder];
        }
        
        int limit = tight ? (num.charAt(pos) - '0') : 9;
        long count = 0;
        
        for (int digit = 0; digit <= limit; digit++) {
            boolean newTight = tight && (digit == limit);
            int newRemainder = (remainder * 10 + digit) % d;
            
            count += dfsDivisible(num, pos + 1, newTight, newRemainder, d, dp);
        }
        
        dp[pos][tightIdx][remainder] = count;
        return count;
    }
    
    // ========== PROBLEM 6: COUNT PALINDROMIC NUMBERS ==========
    
    /**
     * Count palindromic numbers from 0 to n
     * 
     * Example: n = 100
     * Valid: 1, 2, 3, ..., 9, 11, 22, 33, ..., 99
     */
    @Complexity(time = "O(log n)", space = "O(log n)")
    public static long countPalindromes(long n) {
        if (n < 0) return 0;
        if (n == 0) return 1; // 0 is a palindrome
        
        String numStr = String.valueOf(n);
        int len = numStr.length();
        
        // Count palindromes with length < len (doesn't include 0)
        long count = 0;
        for (int l = 1; l < len; l++) {
            count += countPalindromesOfLength(l);
        }
        
        // Count palindromes with length = len and <= n
        count += countPalindromesUpTo(numStr);
        
        // Add 1 for 0 if n >= 0
        count += 1;
        
        return count;
    }
    
    private static long countPalindromesOfLength(int len) {
        if (len == 1) return 9; // 1-9
        
        int halfLen = (len + 1) / 2;
        long count = 9; // First digit can't be 0
        
        for (int i = 1; i < halfLen; i++) {
            count *= 10;
        }
        
        return count;
    }
    
    private static long countPalindromesUpTo(String num) {
        int len = num.length();
        int halfLen = (len + 1) / 2;
        
        // Build the minimum palindrome with first half
        StringBuilder firstHalf = new StringBuilder();
        for (int i = 0; i < halfLen; i++) {
            firstHalf.append(num.charAt(i));
        }
        
        // Count valid palindromes
        long count = 0;
        String halfStr = firstHalf.toString();
        
        // Start from 1 (0 is counted separately in main function)
        for (long half = 1; ; half++) {
            String palindrome = buildPalindrome(String.valueOf(half), len);
            if (palindrome.length() != len || Long.parseLong(palindrome) > Long.parseLong(num)) {
                break;
            }
            count++;
        }
        
        return count;
    }
    
    private static String buildPalindrome(String half, int totalLen) {
        StringBuilder sb = new StringBuilder(half);
        int start = (totalLen % 2 == 0) ? half.length() - 1 : half.length() - 2;
        
        for (int i = start; i >= 0; i--) {
            sb.append(half.charAt(i));
        }
        
        return sb.toString();
    }
    
    // ========== PROBLEM 7: NUMBERS WITH SPECIFIC DIGIT ==========
    
    /**
     * Count numbers from L to R containing digit d
     * 
     * Example: L = 1, R = 100, d = 5
     * Valid: 5, 15, 25, 35, 45, 50-59, 65, 75, 85, 95
     */
    @Complexity(time = "O(log n)", space = "O(log n)")
    public static long countContainingDigit(long L, long R, int d) {
        return countWithDigit(R, d, true) - (L > 0 ? countWithDigit(L - 1, d, true) : 0);
    }
    
    /**
     * Count numbers NOT containing digit d
     */
    public static long countNotContainingDigit(long L, long R, int d) {
        return countWithDigit(R, d, false) - (L > 0 ? countWithDigit(L - 1, d, false) : 0);
    }
    
    private static long countWithDigit(long n, int d, boolean mustContain) {
        if (n < 0) return 0;
        
        String numStr = String.valueOf(n);
        Long[][][][] dp = new Long[numStr.length()][2][2][2]; // [pos][tight][hasDigit][started]
        
        return dfsContainsDigit(numStr, 0, true, false, false, d, mustContain, dp);
    }
    
    private static long dfsContainsDigit(String num, int pos, boolean tight, 
                                          boolean hasDigit, boolean started,
                                          int targetDigit, boolean mustContain,
                                          Long[][][][] dp) {
        if (pos == num.length()) {
            // If we never started (all leading zeros), this represents 0
            if (!started) {
                // Check if 0 contains the target digit
                return (targetDigit == 0) == mustContain ? 1 : 0;
            }
            return (hasDigit == mustContain) ? 1 : 0;
        }
        
        int tightIdx = tight ? 1 : 0;
        int hasIdx = hasDigit ? 1 : 0;
        int startedIdx = started ? 1 : 0;
        
        if (dp[pos][tightIdx][hasIdx][startedIdx] != null) {
            return dp[pos][tightIdx][hasIdx][startedIdx];
        }
        
        int limit = tight ? (num.charAt(pos) - '0') : 9;
        long count = 0;
        
        for (int digit = 0; digit <= limit; digit++) {
            boolean newTight = tight && (digit == limit);
            boolean newStarted = started || (digit != 0);
            // Only count the digit if the number has started (not a leading zero)
            boolean newHas = hasDigit || (newStarted && digit == targetDigit);
            
            count += dfsContainsDigit(num, pos + 1, newTight, newHas, newStarted,
                                      targetDigit, mustContain, dp);
        }
        
        dp[pos][tightIdx][hasIdx][startedIdx] = count;
        return count;
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Digit DP Examples ===\n");
        
        // Example 1: No adjacent repeats
        System.out.println("1. Count numbers with no adjacent repeating digits (0 to 100):");
        System.out.println("   Result: " + countNoAdjacentRepeats(100));
        
        // Example 2: Digit sum divisible by k
        System.out.println("\n2. Count numbers with digit sum divisible by 3 (0 to 100):");
        System.out.println("   Result: " + countDigitSumDivisibleByK(100, 3));
        
        // Example 3: Even/odd digit sum
        System.out.println("\n3. Count numbers with even digit sum (10 to 20):");
        System.out.println("   Result: " + countEvenDigitSum(10, 20));
        System.out.println("   Numbers: 11, 13, 15, 17, 19, 20");
        
        // Example 4: Unique digits
        System.out.println("\n4. Count numbers with all unique digits (0 to 100):");
        System.out.println("   Result: " + countUniqueDigits(100));
        
        // Example 5: Divisible by d
        System.out.println("\n5. Count numbers divisible by 7 (1 to 100):");
        System.out.println("   Result: " + countDivisibleBy(1, 100, 7));
        System.out.println("   DP version: " + countDivisibleByDP(100, 7));
        
        // Example 6: Palindromes
        System.out.println("\n6. Count palindromic numbers (0 to 100):");
        System.out.println("   Result: " + countPalindromes(100));
        
        // Example 7: Contains digit
        System.out.println("\n7. Count numbers containing digit 5 (1 to 100):");
        System.out.println("   Result: " + countContainingDigit(1, 100, 5));
        
        System.out.println("\n8. Count numbers NOT containing digit 5 (1 to 100):");
        System.out.println("   Result: " + countNotContainingDigit(1, 100, 5));
        
        // Large number example
        System.out.println("\n9. Digit sum divisible by 3 (0 to 10^9):");
        System.out.println("   Result: " + countDigitSumDivisibleByK(1_000_000_000L, 3));
        
        System.out.println("\n=== Pattern Summary ===");
        System.out.println("Digit DP Template:");
        System.out.println("  State: dp[position][tight][...constraints...]");
        System.out.println("  Time: O(log n × states)");
        System.out.println("  Space: O(log n × states)");
        System.out.println("  Key: Memoize with position + tight bound + constraint states");
    }
}
