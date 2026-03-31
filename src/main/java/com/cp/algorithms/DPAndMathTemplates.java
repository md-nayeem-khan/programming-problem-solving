package com.cp.algorithms;

import java.util.Arrays;

/**
 * Dynamic Programming & Math utility templates.
 *
 * Includes:
 *   1. Classic DP patterns (LCS, LIS, Knapsack, Coin Change)
 *   2. Math utilities (GCD, LCM, fast power, sieve of Eratosthenes)
 *   3. Sorting helpers (merge sort with inversion count)
 *   4. Bit manipulation tricks
 */
public class DPAndMathTemplates {

    // ---------------------------------------------------------------
    // 1a. Longest Common Subsequence — O(n*m)
    // ---------------------------------------------------------------
    public static int lcs(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= m; j++)
                dp[i][j] = a.charAt(i-1) == b.charAt(j-1)
                        ? dp[i-1][j-1] + 1
                        : Math.max(dp[i-1][j], dp[i][j-1]);
        return dp[n][m];
    }

    // ---------------------------------------------------------------
    // 1b. Longest Increasing Subsequence — O(n log n) patience sort
    // ---------------------------------------------------------------
    public static int lis(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) {
            int lo = 0, hi = size;
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (tails[mid] < x) lo = mid + 1; else hi = mid;
            }
            tails[lo] = x;
            if (lo == size) size++;
        }
        return size;
    }

    // ---------------------------------------------------------------
    // 1c. 0/1 Knapsack — O(n * capacity)
    // ---------------------------------------------------------------
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < n; i++)
            for (int w = capacity; w >= weights[i]; w--)
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[capacity];
    }

    // ---------------------------------------------------------------
    // 1d. Coin Change — minimum coins (unbounded knapsack variant)
    // ---------------------------------------------------------------
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++)
            for (int coin : coins)
                if (coin <= i) dp[i] = Math.min(dp[i], dp[i - coin] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // ---------------------------------------------------------------
    // 1e. Edit Distance — O(n*m)
    // ---------------------------------------------------------------
    public static int editDistance(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= m; j++)
                dp[i][j] = a.charAt(i-1) == b.charAt(j-1)
                        ? dp[i-1][j-1]
                        : 1 + Math.min(dp[i-1][j-1], Math.min(dp[i-1][j], dp[i][j-1]));
        return dp[n][m];
    }

    // ---------------------------------------------------------------
    // 2a. GCD and LCM
    // ---------------------------------------------------------------
    public static long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }
    public static long lcm(long a, long b) { return a / gcd(a, b) * b; }

    // ---------------------------------------------------------------
    // 2b. Fast modular exponentiation — base^exp % mod
    // ---------------------------------------------------------------
    public static long modPow(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }

    // ---------------------------------------------------------------
    // 2c. Modular inverse (when mod is prime) — Fermat's little theorem
    // ---------------------------------------------------------------
    public static long modInverse(long a, long mod) { return modPow(a, mod - 2, mod); }

    // ---------------------------------------------------------------
    // 2d. Sieve of Eratosthenes — primes up to n
    // ---------------------------------------------------------------
    public static boolean[] sieve(int n) {
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        for (int i = 2; (long) i * i <= n; i++)
            if (isPrime[i])
                for (int j = i * i; j <= n; j += i) isPrime[j] = false;
        return isPrime;
    }

    // ---------------------------------------------------------------
    // 3. Merge Sort with inversion count
    // ---------------------------------------------------------------
    public static long countInversions(int[] arr) {
        return mergeSort(arr.clone(), 0, arr.length - 1);
    }

    private static long mergeSort(int[] arr, int lo, int hi) {
        if (lo >= hi) return 0;
        int mid = (lo + hi) / 2;
        long count = mergeSort(arr, lo, mid) + mergeSort(arr, mid + 1, hi);
        int[] merged = new int[hi - lo + 1];
        int i = lo, j = mid + 1, k = 0;
        while (i <= mid && j <= hi) {
            if (arr[i] <= arr[j]) { merged[k++] = arr[i++]; }
            else                  { count += (mid - i + 1); merged[k++] = arr[j++]; }
        }
        while (i <= mid) merged[k++] = arr[i++];
        while (j <= hi)  merged[k++] = arr[j++];
        System.arraycopy(merged, 0, arr, lo, merged.length);
        return count;
    }

    // ---------------------------------------------------------------
    // 4. Bit manipulation tricks
    // ---------------------------------------------------------------
    /** Count set bits (popcount) */
    public static int popcount(int n) { return Integer.bitCount(n); }

    /** Check if n is a power of 2 */
    public static boolean isPowerOfTwo(int n) { return n > 0 && (n & (n - 1)) == 0; }

    /** Lowest set bit */
    public static int lowestSetBit(int n) { return n & (-n); }

    /** Enumerate all subsets of mask */
    public static void enumerateSubsets(int mask) {
        for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {
            // process sub
        }
    }

    /** Next permutation — modifies array in place, returns false if last permutation */
    public static boolean nextPermutation(int[] nums) {
        int n = nums.length, i = n - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) i--;
        if (i < 0) { Arrays.sort(nums); return false; }
        int j = n - 1;
        while (nums[j] <= nums[i]) j--;
        int tmp = nums[i]; nums[i] = nums[j]; nums[j] = tmp;
        int lo = i + 1, hi = n - 1;
        while (lo < hi) { tmp = nums[lo]; nums[lo++] = nums[hi]; nums[hi--] = tmp; }
        return true;
    }
}
