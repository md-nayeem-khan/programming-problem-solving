package com.cp.algorithms;

import com.cp.problems.Complexity;

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
    @Complexity(time = "O(n × m)", space = "O(n × m)", explanation = "n and m are string lengths")
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
    @Complexity(time = "O(n log n)", space = "O(n)", explanation = "Binary search on tails array")
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
    @Complexity(time = "O(n × capacity)", space = "O(capacity)", explanation = "Space-optimized 1D DP")
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
    // 4. Kadane's Algorithm and Variants (Maximum Subarray)
    // ---------------------------------------------------------------
    
    /**
     * Maximum Subarray Sum (Kadane's Algorithm).
     * Find contiguous subarray with maximum sum.
     * 
     * Pattern: Track current sum, reset to 0 if negative.
     * Very common: Amazon, Meta, Microsoft, Google
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int maxSubarraySum(int[] nums) {
        int maxSum = nums[0];
        int currentSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }
    
    /**
     * Maximum Subarray Sum with indices.
     * Returns [maxSum, startIndex, endIndex].
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int[] maxSubarraySumWithIndices(int[] nums) {
        int maxSum = nums[0];
        int currentSum = nums[0];
        int start = 0, end = 0, tempStart = 0;
        
        for (int i = 1; i < nums.length; i++) {
            if (currentSum < 0) {
                currentSum = nums[i];
                tempStart = i;
            } else {
                currentSum += nums[i];
            }
            
            if (currentSum > maxSum) {
                maxSum = currentSum;
                start = tempStart;
                end = i;
            }
        }
        
        return new int[]{maxSum, start, end};
    }
    
    /**
     * Maximum Product Subarray.
     * Track both max and min (because negative × negative = positive).
     * 
     * Companies: Amazon, Meta, Microsoft
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int maxProductSubarray(int[] nums) {
        int maxProduct = nums[0];
        int currentMax = nums[0];
        int currentMin = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < 0) {
                // Swap max and min when multiplying by negative
                int temp = currentMax;
                currentMax = currentMin;
                currentMin = temp;
            }
            
            currentMax = Math.max(nums[i], currentMax * nums[i]);
            currentMin = Math.min(nums[i], currentMin * nums[i]);
            
            maxProduct = Math.max(maxProduct, currentMax);
        }
        
        return maxProduct;
    }
    
    /**
     * Maximum Sum Circular Subarray.
     * Pattern: Max sum is either normal Kadane OR total - min subarray.
     * 
     * Companies: Amazon, Google
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int maxSubarraySumCircular(int[] nums) {
        int totalSum = 0;
        int maxSum = nums[0], currentMax = nums[0];
        int minSum = nums[0], currentMin = nums[0];
        
        for (int i = 0; i < nums.length; i++) {
            totalSum += nums[i];
            
            if (i > 0) {
                currentMax = Math.max(nums[i], currentMax + nums[i]);
                maxSum = Math.max(maxSum, currentMax);
                
                currentMin = Math.min(nums[i], currentMin + nums[i]);
                minSum = Math.min(minSum, currentMin);
            }
        }
        
        // If all negative, return maxSum
        if (maxSum < 0) return maxSum;
        
        // Max of: normal max OR circular max (total - minSum)
        return Math.max(maxSum, totalSum - minSum);
    }
    
    /**
     * Maximum Sum Subarray with at most K elements.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int maxSubarraySumAtMostK(int[] nums, int k) {
        int maxSum = Integer.MIN_VALUE;
        
        for (int i = 0; i < nums.length; i++) {
            int currentSum = 0;
            for (int j = i; j < nums.length && j < i + k; j++) {
                currentSum += nums[j];
                maxSum = Math.max(maxSum, currentSum);
            }
        }
        
        return maxSum;
    }
    
    /**
     * Minimum Subarray Sum (reverse Kadane).
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int minSubarraySum(int[] nums) {
        int minSum = nums[0];
        int currentSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.min(nums[i], currentSum + nums[i]);
            minSum = Math.min(minSum, currentSum);
        }
        
        return minSum;
    }
    
    /**
     * Best Time to Buy and Sell Stock (single transaction).
     * Pattern: Track min price and max profit.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int maxProfit(int[] prices) {
        int minPrice = prices[0];
        int maxProfit = 0;
        
        for (int i = 1; i < prices.length; i++) {
            maxProfit = Math.max(maxProfit, prices[i] - minPrice);
            minPrice = Math.min(minPrice, prices[i]);
        }
        
        return maxProfit;
    }
    
    /**
     * Best Time to Buy and Sell Stock II (unlimited transactions).
     * Pattern: Sum all positive differences.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int maxProfitUnlimited(int[] prices) {
        int maxProfit = 0;
        
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                maxProfit += prices[i] - prices[i - 1];
            }
        }
        
        return maxProfit;
    }
    
    /**
     * Maximum Alternating Subarray Sum.
     * Pattern: Track even and odd indexed sums.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static long maxAlternatingSum(int[] nums) {
        long even = 0, odd = 0;
        
        for (int num : nums) {
            long newEven = Math.max(even, odd + num);
            long newOdd = Math.max(odd, even - num);
            even = newEven;
            odd = newOdd;
        }
        
        return even;
    }
    
    // ---------------------------------------------------------------
    // 7. STATE MACHINE DP PATTERNS
    // ---------------------------------------------------------------
    
    /**
     * STATE MACHINE DP - Pattern for problems with state transitions.
     * 
     * Key insight: Track multiple states where each state represents a valid condition.
     * Transition between states based on current decision.
     * 
     * Common applications:
     * - Stock buy/sell with constraints
     * - House robber with variations
     * - Paint house/fence problems
     * - String problems with state rules
     * 
     * Pattern:
     *   states[i] = {state0, state1, state2, ...}
     *   for each element:
     *     new_state0 = f(old_states)
     *     new_state1 = g(old_states)
     *     ...
     * 
     * Interview favorites: Google, Meta, Amazon (very common)
     */
    
    /**
     * Best Time to Buy and Sell Stock III (at most 2 transactions).
     * 
     * States:
     * - buy1: max profit after first buy
     * - sell1: max profit after first sell
     * - buy2: max profit after second buy  
     * - sell2: max profit after second sell
     * 
     * Interview favorite: Amazon, Google
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int maxProfitTwoTransactions(int[] prices) {
        int buy1 = Integer.MIN_VALUE, sell1 = 0;
        int buy2 = Integer.MIN_VALUE, sell2 = 0;
        
        for (int price : prices) {
            buy1 = Math.max(buy1, -price);          // Buy first stock
            sell1 = Math.max(sell1, buy1 + price);  // Sell first stock
            buy2 = Math.max(buy2, sell1 - price);   // Buy second stock
            sell2 = Math.max(sell2, buy2 + price);  // Sell second stock
        }
        
        return sell2;
    }
    
    /**
     * Best Time to Buy and Sell Stock IV (at most k transactions).
     * 
     * States: buy[i] and sell[i] for each transaction i.
     * 
     * Interview favorite: Google, Meta (hard)
     */
    @Complexity(time = "O(n × k)", space = "O(k)")
    public static int maxProfitKTransactions(int[] prices, int k) {
        if (k == 0 || prices.length == 0) return 0;
        
        // If k >= n/2, unlimited transactions
        if (k >= prices.length / 2) {
            return maxProfitUnlimited(prices);
        }
        
        int[] buy = new int[k + 1];
        int[] sell = new int[k + 1];
        Arrays.fill(buy, Integer.MIN_VALUE);
        
        for (int price : prices) {
            for (int i = k; i >= 1; i--) {
                sell[i] = Math.max(sell[i], buy[i] + price);
                buy[i] = Math.max(buy[i], sell[i - 1] - price);
            }
        }
        
        return sell[k];
    }
    
    /**
     * Best Time to Buy and Sell Stock with Cooldown.
     * After selling, must wait 1 day before buying again.
     * 
     * States:
     * - hold: currently holding stock
     * - sold: just sold (in cooldown)
     * - rest: resting (can buy tomorrow)
     * 
     * State transitions:
     * hold → sold (sell)
     * sold → rest (cooldown)
     * rest → hold (buy) OR rest → rest (do nothing)
     * 
     * Interview favorite: Meta, Amazon
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int maxProfitWithCooldown(int[] prices) {
        int sold = 0;           // Just sold, in cooldown
        int hold = -prices[0];  // Holding stock
        int rest = 0;           // Resting, can buy
        
        for (int i = 1; i < prices.length; i++) {
            int prevSold = sold;
            int prevHold = hold;
            int prevRest = rest;
            
            sold = prevHold + prices[i];           // Sell stock
            hold = Math.max(prevHold, prevRest - prices[i]); // Buy or keep holding
            rest = Math.max(prevRest, prevSold);   // Rest after cooldown
        }
        
        return Math.max(sold, rest);
    }
    
    /**
     * Best Time to Buy and Sell Stock with Transaction Fee.
     * Each transaction has a fixed fee.
     * 
     * States:
     * - cash: not holding stock
     * - hold: holding stock
     * 
     * Interview favorite: Amazon, Microsoft
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int maxProfitWithFee(int[] prices, int fee) {
        int cash = 0;           // Not holding
        int hold = -prices[0];  // Holding
        
        for (int i = 1; i < prices.length; i++) {
            cash = Math.max(cash, hold + prices[i] - fee); // Sell
            hold = Math.max(hold, cash - prices[i]);       // Buy
        }
        
        return cash;
    }
    
    /**
     * House Robber I - Rob houses in a line (can't rob adjacent).
     * 
     * States:
     * - rob: max profit if rob current house
     * - skip: max profit if skip current house
     * 
     * Pattern: At each house, choose to rob or skip.
     * 
     * Interview favorite: Amazon, Meta, Microsoft
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int rob(int[] nums) {
        int rob = 0;   // Max profit if rob current
        int skip = 0;  // Max profit if skip current
        
        for (int num : nums) {
            int newRob = skip + num;           // Rob current (must skip prev)
            int newSkip = Math.max(rob, skip); // Skip current (take max so far)
            rob = newRob;
            skip = newSkip;
        }
        
        return Math.max(rob, skip);
    }
    
    /**
     * House Robber II - Houses in a circle (first and last are adjacent).
     * 
     * Pattern: Either rob house 0 (can't rob last) OR skip house 0.
     * 
     * Interview favorite: Amazon, Google
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int robCircular(int[] nums) {
        if (nums.length == 1) return nums[0];
        if (nums.length == 2) return Math.max(nums[0], nums[1]);
        
        // Case 1: Rob first house (exclude last)
        int max1 = robRange(nums, 0, nums.length - 2);
        
        // Case 2: Don't rob first house (can rob last)
        int max2 = robRange(nums, 1, nums.length - 1);
        
        return Math.max(max1, max2);
    }
    
    private static int robRange(int[] nums, int start, int end) {
        int rob = 0, skip = 0;
        for (int i = start; i <= end; i++) {
            int newRob = skip + nums[i];
            int newSkip = Math.max(rob, skip);
            rob = newRob;
            skip = newSkip;
        }
        return Math.max(rob, skip);
    }
    
    /**
     * Paint House - Paint n houses with k colors, no adjacent same color.
     * costs[i][j] = cost to paint house i with color j.
     * 
     * States: minCost[color] = min cost to paint houses up to current with ending color.
     * 
     * Interview favorite: Meta, Google
     */
    @Complexity(time = "O(n × k²)", space = "O(k)")
    public static int minCostToPaintHouses(int[][] costs) {
        if (costs.length == 0) return 0;
        
        int n = costs.length;
        int k = costs[0].length;
        int[] dp = costs[0].clone();
        
        for (int i = 1; i < n; i++) {
            int[] newDp = new int[k];
            for (int j = 0; j < k; j++) {
                newDp[j] = Integer.MAX_VALUE;
                // Choose different color from previous house
                for (int prevColor = 0; prevColor < k; prevColor++) {
                    if (prevColor != j) {
                        newDp[j] = Math.min(newDp[j], dp[prevColor] + costs[i][j]);
                    }
                }
            }
            dp = newDp;
        }
        
        int minCost = Integer.MAX_VALUE;
        for (int cost : dp) minCost = Math.min(minCost, cost);
        return minCost;
    }
    
    /**
     * Paint House II (Optimized for k colors) - O(n × k).
     * Track first and second minimum from previous row.
     * 
     * Interview favorite: Meta (optimization question)
     */
    @Complexity(time = "O(n × k)", space = "O(k)")
    public static int minCostToPaintHousesOptimized(int[][] costs) {
        if (costs.length == 0) return 0;
        
        int n = costs.length;
        int k = costs[0].length;
        int[] dp = costs[0].clone();
        
        for (int i = 1; i < n; i++) {
            // Find first and second minimum
            int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
            int min1Idx = -1;
            
            for (int j = 0; j < k; j++) {
                if (dp[j] < min1) {
                    min2 = min1;
                    min1 = dp[j];
                    min1Idx = j;
                } else if (dp[j] < min2) {
                    min2 = dp[j];
                }
            }
            
            int[] newDp = new int[k];
            for (int j = 0; j < k; j++) {
                // Use min2 if previous was min1 (same color), otherwise use min1
                newDp[j] = (j == min1Idx ? min2 : min1) + costs[i][j];
            }
            dp = newDp;
        }
        
        int minCost = Integer.MAX_VALUE;
        for (int cost : dp) minCost = Math.min(minCost, cost);
        return minCost;
    }
    
    /**
     * Paint Fence - n fence posts, k colors, no more than 2 adjacent same color.
     * 
     * States:
     * - same: ways to paint with last 2 posts same color
     * - diff: ways to paint with last 2 posts different colors
     * 
     * Transitions:
     * - same = diff (previous 2 were different, now same)
     * - diff = (same + diff) * (k - 1) (previous was anything, now different)
     * 
     * Interview favorite: Meta, Amazon
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int numWaysToPaintFence(int n, int k) {
        if (n == 0) return 0;
        if (n == 1) return k;
        
        int same = k;       // First 2 posts same color: k ways
        int diff = k * (k - 1); // First 2 posts different: k * (k-1) ways
        
        for (int i = 3; i <= n; i++) {
            int newSame = diff;
            int newDiff = (same + diff) * (k - 1);
            same = newSame;
            diff = newDiff;
        }
        
        return same + diff;
    }
    
    /**
     * Delete and Earn - Pick nums[i], earn nums[i] points, delete all nums[i]-1 and nums[i]+1.
     * 
     * Pattern: Similar to House Robber after grouping by value.
     * 
     * Interview favorite: Amazon, Microsoft
     */
    @Complexity(time = "O(n + max)", space = "O(max)")
    public static int deleteAndEarn(int[] nums) {
        int max = 0;
        for (int num : nums) max = Math.max(max, num);
        
        int[] points = new int[max + 1];
        for (int num : nums) points[num] += num;
        
        // Now it's House Robber problem
        int take = 0, skip = 0;
        for (int point : points) {
            int newTake = skip + point;
            int newSkip = Math.max(take, skip);
            take = newTake;
            skip = newSkip;
        }
        
        return Math.max(take, skip);
    }

    // ---------------------------------------------------------------
    // 5. Bit manipulation tricks
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
    
    // ---------------------------------------------------------------
    // 6. Advanced String DP
    // ---------------------------------------------------------------
    
    /**
     * Distinct Subsequences - number of distinct subsequences of t in s.
     * 
     * DP[i][j] = number of ways to match first j chars of t using first i chars of s
     * If s[i-1] == t[j-1]: DP[i][j] = DP[i-1][j-1] + DP[i-1][j]
     * Else: DP[i][j] = DP[i-1][j]
     * 
     * Interview favorite: Amazon, Microsoft
     */
    @Complexity(time = "O(n × m)", space = "O(n × m)")
    public static int numDistinct(String s, String t) {
        int n = s.length(), m = t.length();
        int[][] dp = new int[n + 1][m + 1];
        
        // Empty t can be matched in 1 way
        for (int i = 0; i <= n; i++) dp[i][0] = 1;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dp[i][j] = dp[i-1][j]; // Don't use s[i-1]
                if (s.charAt(i-1) == t.charAt(j-1)) {
                    dp[i][j] += dp[i-1][j-1]; // Use s[i-1]
                }
            }
        }
        
        return dp[n][m];
    }
    
    /**
     * Interleaving String - check if s3 is interleaving of s1 and s2.
     * 
     * DP[i][j] = can we form first i+j chars of s3 using first i of s1 and j of s2
     * 
     * Interview favorite: Amazon, Meta
     */
    @Complexity(time = "O(n × m)", space = "O(n × m)")
    public static boolean isInterleave(String s1, String s2, String s3) {
        int n = s1.length(), m = s2.length();
        if (n + m != s3.length()) return false;
        
        boolean[][] dp = new boolean[n + 1][m + 1];
        dp[0][0] = true;
        
        // Initialize first column (using only s1)
        for (int i = 1; i <= n; i++) {
            dp[i][0] = dp[i-1][0] && s1.charAt(i-1) == s3.charAt(i-1);
        }
        
        // Initialize first row (using only s2)
        for (int j = 1; j <= m; j++) {
            dp[0][j] = dp[0][j-1] && s2.charAt(j-1) == s3.charAt(j-1);
        }
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int k = i + j - 1;
                dp[i][j] = (dp[i-1][j] && s1.charAt(i-1) == s3.charAt(k)) ||
                           (dp[i][j-1] && s2.charAt(j-1) == s3.charAt(k));
            }
        }
        
        return dp[n][m];
    }
    
    /**
     * Word Break - check if string can be segmented into words from dictionary.
     */
    @Complexity(time = "O(n² × m) where m=avg word length", space = "O(n)")
    public static boolean wordBreak(String s, java.util.List<String> wordDict) {
        java.util.Set<String> words = new java.util.HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && words.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * Word Break II - return all possible word break combinations.
     */
    @Complexity(time = "O(2^n) worst case", space = "O(2^n)")
    public static java.util.List<String> wordBreakII(String s, java.util.List<String> wordDict) {
        java.util.Set<String> words = new java.util.HashSet<>(wordDict);
        java.util.Map<String, java.util.List<String>> memo = new java.util.HashMap<>();
        return wordBreakHelper(s, words, memo);
    }
    
    private static java.util.List<String> wordBreakHelper(String s, java.util.Set<String> words,
                                                          java.util.Map<String, java.util.List<String>> memo) {
        if (memo.containsKey(s)) return memo.get(s);
        
        java.util.List<String> result = new java.util.ArrayList<>();
        if (s.isEmpty()) {
            result.add("");
            return result;
        }
        
        for (String word : words) {
            if (s.startsWith(word)) {
                java.util.List<String> sublist = wordBreakHelper(s.substring(word.length()), words, memo);
                for (String sub : sublist) {
                    result.add(word + (sub.isEmpty() ? "" : " " + sub));
                }
            }
        }
        
        memo.put(s, result);
        return result;
    }
    
    /**
     * Regular Expression Matching - support '.' and '*'.
     * '.' matches any single character
     * '*' matches zero or more of preceding element
     * 
     * Interview favorite: Amazon, Google, Meta (hard)
     */
    @Complexity(time = "O(n × m)", space = "O(n × m)")
    public static boolean isMatch(String s, String p) {
        int n = s.length(), m = p.length();
        boolean[][] dp = new boolean[n + 1][m + 1];
        dp[0][0] = true;
        
        // Handle patterns like a*, a*b*, etc. that can match empty string
        for (int j = 2; j <= m; j++) {
            if (p.charAt(j-1) == '*') {
                dp[0][j] = dp[0][j-2];
            }
        }
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char sc = s.charAt(i-1);
                char pc = p.charAt(j-1);
                
                if (pc == '*') {
                    // Match zero occurrences
                    dp[i][j] = dp[i][j-2];
                    
                    // Match one or more occurrences
                    char prev = p.charAt(j-2);
                    if (prev == '.' || prev == sc) {
                        dp[i][j] |= dp[i-1][j];
                    }
                } else if (pc == '.' || pc == sc) {
                    dp[i][j] = dp[i-1][j-1];
                }
            }
        }
        
        return dp[n][m];
    }
    
    /**
     * Wildcard Matching - support '?' and '*'.
     * '?' matches any single character
     * '*' matches any sequence of characters (including empty)
     */
    @Complexity(time = "O(n × m)", space = "O(n × m)")
    public static boolean isMatchWildcard(String s, String p) {
        int n = s.length(), m = p.length();
        boolean[][] dp = new boolean[n + 1][m + 1];
        dp[0][0] = true;
        
        // Handle leading *s
        for (int j = 1; j <= m; j++) {
            if (p.charAt(j-1) == '*') {
                dp[0][j] = dp[0][j-1];
            }
        }
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char pc = p.charAt(j-1);
                
                if (pc == '*') {
                    // Match empty or match one char
                    dp[i][j] = dp[i][j-1] || dp[i-1][j];
                } else if (pc == '?' || pc == s.charAt(i-1)) {
                    dp[i][j] = dp[i-1][j-1];
                }
            }
        }
        
        return dp[n][m];
    }
    
    /**
     * Palindrome Partitioning II - minimum cuts to partition into palindromes.
     */
    @Complexity(time = "O(n²)", space = "O(n²)")
    public static int minCut(String s) {
        int n = s.length();
        boolean[][] isPalin = new boolean[n][n];
        
        // Build palindrome table
        for (int len = 1; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (len == 1) {
                    isPalin[i][j] = true;
                } else if (len == 2) {
                    isPalin[i][j] = s.charAt(i) == s.charAt(j);
                } else {
                    isPalin[i][j] = s.charAt(i) == s.charAt(j) && isPalin[i+1][j-1];
                }
            }
        }
        
        // DP for minimum cuts
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            if (isPalin[0][i]) {
                dp[i] = 0;
            } else {
                dp[i] = i; // Max cuts
                for (int j = 0; j < i; j++) {
                    if (isPalin[j+1][i]) {
                        dp[i] = Math.min(dp[i], dp[j] + 1);
                    }
                }
            }
        }
        
        return dp[n-1];
    }
    
    /**
     * Longest Palindromic Subsequence.
     */
    @Complexity(time = "O(n²)", space = "O(n²)")
    public static int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        
        // Single characters are palindromes
        for (int i = 0; i < n; i++) dp[i][i] = 1;
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = 2 + (i + 1 <= j - 1 ? dp[i+1][j-1] : 0);
                } else {
                    dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
                }
            }
        }
        
        return dp[0][n-1];
    }
    
    /**
     * Minimum Insertions to Make String Palindrome.
     */
    @Complexity(time = "O(n²)", space = "O(n²)")
    public static int minInsertions(String s) {
        int lps = longestPalindromeSubseq(s);
        return s.length() - lps;
    }
    
    // ---------------------------------------------------------------
    // 7. MATRIX EXPONENTIATION - CRITICAL for Google (15%)
    // ---------------------------------------------------------------
    
    /**
     * Matrix Exponentiation - CRITICAL for solving recurrence relations
     * 
     * Applications:
     * - Fibonacci(n) in O(log n) where n up to 10^18
     * - Count paths of length k in a graph
     * - Solve linear recurrence relations
     * - Tower of Hanoi states
     * 
     * Interview Questions:
     * - LeetCode 70: Climbing Stairs (Fibonacci variant)
     * - LeetCode 509: Fibonacci Number
     * - LeetCode 1137: N-th Tribonacci Number
     * - Count paths in DAG
     * 
     * Companies: Google (5/5), Amazon (4/5)
     */
    
    /**
     * Multiply two n×n matrices modulo mod.
     * 
     * @param A first matrix
     * @param B second matrix
     * @param mod modulo value (use Long.MAX_VALUE for no mod)
     * @return A × B
     */
    @Complexity(time = "O(n³)", space = "O(n²)")
    public static long[][] matrixMultiply(long[][] A, long[][] B, long mod) {
        int n = A.length;
        long[][] C = new long[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    C[i][j] = (C[i][j] + A[i][k] * B[k][j]) % mod;
                }
            }
        }
        
        return C;
    }
    
    /**
     * Matrix exponentiation - compute A^exp using binary exponentiation.
     * 
     * @param A base matrix (n×n)
     * @param exp exponent (can be up to 10^18)
     * @param mod modulo value
     * @return A^exp mod mod
     */
    @Complexity(time = "O(n³ log exp)", space = "O(n²)")
    public static long[][] matrixPower(long[][] A, long exp, long mod) {
        int n = A.length;
        
        // Initialize result as identity matrix
        long[][] result = new long[n][n];
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }
        
        // Binary exponentiation
        long[][] base = A;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = matrixMultiply(result, base, mod);
            }
            base = matrixMultiply(base, base, mod);
            exp >>= 1;
        }
        
        return result;
    }
    
    /**
     * Fibonacci number at position n using matrix exponentiation.
     * 
     * Matrix form: [F(n+1), F(n)] = [F(1), F(0)] × [[1,1],[1,0]]^n
     * 
     * This can compute F(10^18) in O(log n) time!
     * 
     * @param n position (0-indexed, F(0)=0, F(1)=1)
     * @param mod modulo value
     * @return F(n) % mod
     */
    @Complexity(time = "O(log n)", space = "O(1)")
    public static long fibonacciMatrix(long n, long mod) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        // Transformation matrix: [[1,1],[1,0]]
        long[][] matrix = {{1, 1}, {1, 0}};
        
        long[][] result = matrixPower(matrix, n, mod);
        
        return result[0][1]; // F(n)
    }
    
    /**
     * Fibonacci without mod (for smaller n).
     */
    @Complexity(time = "O(log n)", space = "O(1)")
    public static long fibonacciMatrix(long n) {
        return fibonacciMatrix(n, Long.MAX_VALUE);
    }
    
    /**
     * N-th Tribonacci number using matrix exponentiation.
     * T(n) = T(n-1) + T(n-2) + T(n-3)
     * T(0) = 0, T(1) = 1, T(2) = 1
     * 
     * Matrix: [[1,1,1],[1,0,0],[0,1,0]]
     */
    @Complexity(time = "O(log n)", space = "O(1)")
    public static long tribonacciMatrix(long n, long mod) {
        if (n == 0) return 0;
        if (n == 1 || n == 2) return 1;
        
        // Transformation matrix
        long[][] matrix = {
            {1, 1, 1},
            {1, 0, 0},
            {0, 1, 0}
        };
        
        long[][] result = matrixPower(matrix, n - 2, mod);
        
        // [T(n), T(n-1), T(n-2)] = [T(2), T(1), T(0)] × matrix^(n-2)
        // = [1, 1, 0] × matrix^(n-2)
        return (result[0][0] + result[0][1]) % mod;
    }
    
    /**
     * Count paths of length k from u to v in a graph.
     * 
     * Build adjacency matrix A where A[i][j] = number of edges from i to j.
     * A^k [i][j] = number of paths of length k from i to j.
     * 
     * @param adj adjacency matrix
     * @param k path length
     * @param u source node
     * @param v destination node
     * @param mod modulo value
     * @return number of paths of length k from u to v
     */
    @Complexity(time = "O(n³ log k)", space = "O(n²)")
    public static long countPathsLengthK(long[][] adj, long k, int u, int v, long mod) {
        long[][] result = matrixPower(adj, k, mod);
        return result[u][v];
    }
    
    /**
     * Solve linear recurrence relation using matrix exponentiation.
     * 
     * Given: a(n) = c[0]*a(n-1) + c[1]*a(n-2) + ... + c[k-1]*a(n-k)
     * Find: a(n) efficiently
     * 
     * Example: For Fibonacci, c = [1, 1], base = [F(1), F(0)] = [1, 0]
     * 
     * @param coeffs coefficients [c[0], c[1], ..., c[k-1]]
     * @param base initial values [a(k-1), a(k-2), ..., a(0)]
     * @param n position to compute
     * @param mod modulo value
     * @return a(n) % mod
     */
    @Complexity(time = "O(k³ log n)", space = "O(k²)")
    public static long solveRecurrence(long[] coeffs, long[] base, long n, long mod) {
        int k = coeffs.length;
        
        if (n < k) {
            return base[k - 1 - (int)n] % mod;
        }
        
        // Build transformation matrix
        long[][] matrix = new long[k][k];
        
        // First row: coefficients
        for (int i = 0; i < k; i++) {
            matrix[0][i] = coeffs[i];
        }
        
        // Remaining rows: identity shift
        for (int i = 1; i < k; i++) {
            matrix[i][i - 1] = 1;
        }
        
        // Compute matrix^(n - k + 1)
        long[][] result = matrixPower(matrix, n - k + 1, mod);
        
        // Multiply with base vector
        long answer = 0;
        for (int i = 0; i < k; i++) {
            answer = (answer + result[0][i] * base[i]) % mod;
        }
        
        return answer;
    }
    
    /**
     * Example: Climbing Stairs with variable steps
     * You can climb 1, 2, or 3 stairs at a time.
     * Find number of ways to reach stair n.
     * 
     * Recurrence: ways(n) = ways(n-1) + ways(n-2) + ways(n-3)
     */
    @Complexity(time = "O(log n)", space = "O(1)")
    public static long climbingStairs123(long n, long mod) {
        if (n == 0) return 1;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        // ways(n) = ways(n-1) + ways(n-2) + ways(n-3)
        long[] coeffs = {1, 1, 1}; // coefficients
        long[] base = {4, 2, 1}; // [ways(3), ways(2), ways(1)]
        
        return solveRecurrence(coeffs, base, n, mod);
    }
    
    /**
     * Example: Tile a 2×n board with 1×2 tiles
     * 
     * Recurrence: ways(n) = ways(n-1) + ways(n-2)
     * This is Fibonacci!
     */
    @Complexity(time = "O(log n)", space = "O(1)")
    public static long tilingWays(long n, long mod) {
        return fibonacciMatrix(n + 1, mod);
    }
    
    // ---------------------------------------------------------------
    // 8. EXTENDED GCD (BEZOUT'S IDENTITY)
    // ---------------------------------------------------------------
    
    /**
     * Extended GCD - Find x, y such that ax + by = gcd(a,b)
     * 
     * Applications:
     * - Modular multiplicative inverse
     * - Solve linear Diophantine equations
     * - Chinese Remainder Theorem
     * 
     * Returns: [gcd, x, y]
     */
    @Complexity(time = "O(log min(a,b))", space = "O(log min(a,b))")
    public static long[] extendedGCD(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0}; // gcd = a, x = 1, y = 0
        }
        
        long[] result = extendedGCD(b, a % b);
        long gcd = result[0];
        long x1 = result[1];
        long y1 = result[2];
        
        // x = y1
        // y = x1 - (a/b) * y1
        long x = y1;
        long y = x1 - (a / b) * y1;
        
        return new long[]{gcd, x, y};
    }
    
    /**
     * Modular multiplicative inverse using Extended GCD.
     * Find x such that (a * x) % mod = 1
     * 
     * More general than modInverse() - works when mod is not prime!
     * 
     * @param a number to find inverse of
     * @param mod modulus (must be coprime with a)
     * @return a^(-1) mod mod
     */
    @Complexity(time = "O(log mod)", space = "O(log mod)")
    public static long modInverseExtGCD(long a, long mod) {
        long[] result = extendedGCD(a, mod);
        long gcd = result[0];
        
        if (gcd != 1) {
            throw new IllegalArgumentException("Inverse doesn't exist (not coprime)");
        }
        
        long x = result[1];
        
        // Make sure result is positive
        return (x % mod + mod) % mod;
    }
    
    /**
     * Solve linear Diophantine equation: ax + by = c
     * 
     * Returns: [x, y] if solution exists, null otherwise
     * 
     * Solution exists iff gcd(a,b) divides c
     */
    @Complexity(time = "O(log min(a,b))", space = "O(log min(a,b))")
    public static long[] solveDiophantine(long a, long b, long c) {
        long[] result = extendedGCD(a, b);
        long gcd = result[0];
        
        if (c % gcd != 0) {
            return null; // No solution
        }
        
        long x0 = result[1];
        long y0 = result[2];
        
        // Scale solution
        long x = x0 * (c / gcd);
        long y = y0 * (c / gcd);
        
        return new long[]{x, y};
    }
    
    // ---------------------------------------------------------------
    // 9. CHINESE REMAINDER THEOREM (CRT)
    // ---------------------------------------------------------------
    
    /**
     * Chinese Remainder Theorem - solve system of congruences:
     * x ≡ a[0] (mod m[0])
     * x ≡ a[1] (mod m[1])
     * ...
     * x ≡ a[k-1] (mod m[k-1])
     * 
     * Requirement: m[i] must be pairwise coprime
     * 
     * @param a remainders
     * @param m moduli (must be pairwise coprime)
     * @return x such that all congruences hold, -1 if no solution
     */
    @Complexity(time = "O(k² log M) where M=product of all m[i]", space = "O(1)")
    public static long chineseRemainderTheorem(long[] a, long[] m) {
        int k = a.length;
        
        // Compute M = product of all moduli
        long M = 1;
        for (long mi : m) {
            M *= mi;
        }
        
        long x = 0;
        
        for (int i = 0; i < k; i++) {
            long Mi = M / m[i];
            long Mi_inv = modInverseExtGCD(Mi, m[i]);
            x = (x + a[i] * Mi % M * Mi_inv) % M;
        }
        
        return (x % M + M) % M;
    }
    
    /**
     * CRT for two congruences (simpler version).
     */
    @Complexity(time = "O(log M)", space = "O(log M)")
    public static long crtTwo(long a1, long m1, long a2, long m2) {
        return chineseRemainderTheorem(new long[]{a1, a2}, new long[]{m1, m2});
    }
    
    // ========== RESERVOIR SAMPLING ==========
    
    /**
     * Reservoir Sampling - Random sample from stream
     * MEDIUM PRIORITY for Meta, Google system design
     * 
     * Sample k elements from stream of unknown/infinite size
     * Each element has equal probability k/n of being selected
     * 
     * Companies: Meta (4/5), Google (3/5), Amazon (3/5)
     * 
     * Interview Questions:
     * - LeetCode 382: Linked List Random Node
     * - LeetCode 398: Random Pick Index
     * - Sample from streaming data
     * - Fair random selection
     */
    
    /**
     * Reservoir Sampling - Sample k items from stream
     * 
     * Algorithm:
     * 1. Keep first k items in reservoir
     * 2. For i-th item (i > k):
     *    - Generate random j in [0, i)
     *    - If j < k, replace reservoir[j] with item i
     * 
     * @param stream input stream (can be infinite)
     * @param k number of samples
     * @return k random samples with equal probability
     */
    @Complexity(time = "O(n)", space = "O(k)")
    public static <T> java.util.List<T> reservoirSampling(java.util.Iterator<T> stream, int k) {
        java.util.List<T> reservoir = new java.util.ArrayList<>(k);
        java.util.Random rand = new java.util.Random();
        
        // Fill reservoir with first k elements
        int count = 0;
        while (stream.hasNext() && count < k) {
            reservoir.add(stream.next());
            count++;
        }
        
        // Process remaining elements
        while (stream.hasNext()) {
            T item = stream.next();
            count++;
            
            // Generate random index
            int j = rand.nextInt(count);
            
            // Replace element with probability k/count
            if (j < k) {
                reservoir.set(j, item);
            }
        }
        
        return reservoir;
    }
    
    /**
     * Reservoir Sampling for single element (k=1)
     * Simpler version for selecting one random element
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static <T> T reservoirSamplingOne(java.util.Iterator<T> stream) {
        java.util.Random rand = new java.util.Random();
        T result = null;
        int count = 0;
        
        while (stream.hasNext()) {
            T item = stream.next();
            count++;
            
            // Select current item with probability 1/count
            if (rand.nextInt(count) == 0) {
                result = item;
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 382: Linked List Random Node
     * Return random node value from linked list in O(1) space
     */
    @Complexity(time = "O(n) per call", space = "O(1)")
    public static class LinkedListRandom {
        private static class ListNode {
            int val;
            ListNode next;
            ListNode(int val) { this.val = val; }
        }
        
        private ListNode head;
        private java.util.Random rand;
        
        public LinkedListRandom(ListNode head) {
            this.head = head;
            this.rand = new java.util.Random();
        }
        
        public int getRandom() {
            ListNode current = head;
            int result = current.val;
            int count = 1;
            
            while (current != null) {
                if (rand.nextInt(count) == 0) {
                    result = current.val;
                }
                count++;
                current = current.next;
            }
            
            return result;
        }
    }
    
    /**
     * LeetCode 398: Random Pick Index
     * Pick random index of target value from array
     */
    @Complexity(time = "O(n) per call", space = "O(1)")
    public static class RandomPickIndex {
        private int[] nums;
        private java.util.Random rand;
        
        public RandomPickIndex(int[] nums) {
            this.nums = nums;
            this.rand = new java.util.Random();
        }
        
        public int pick(int target) {
            int result = -1;
            int count = 0;
            
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == target) {
                    count++;
                    if (rand.nextInt(count) == 0) {
                        result = i;
                    }
                }
            }
            
            return result;
        }
    }
    
    /**
     * Weighted Reservoir Sampling
     * Sample with probability proportional to weight
     */
    @Complexity(time = "O(n)", space = "O(k)")
    public static <T> java.util.List<T> weightedReservoirSampling(
            java.util.List<T> items, java.util.List<Double> weights, int k) {
        
        if (items.size() != weights.size()) {
            throw new IllegalArgumentException("Items and weights must have same size");
        }
        
        java.util.Random rand = new java.util.Random();
        java.util.PriorityQueue<WeightedItem<T>> heap = 
            new java.util.PriorityQueue<>((a, b) -> Double.compare(a.key, b.key));
        
        for (int i = 0; i < items.size(); i++) {
            double key = Math.pow(rand.nextDouble(), 1.0 / weights.get(i));
            heap.offer(new WeightedItem<>(items.get(i), key));
            
            if (heap.size() > k) {
                heap.poll();
            }
        }
        
        java.util.List<T> result = new java.util.ArrayList<>();
        while (!heap.isEmpty()) {
            result.add(heap.poll().item);
        }
        
        return result;
    }
    
    private static class WeightedItem<T> {
        T item;
        double key;
        
        WeightedItem(T item, double key) {
            this.item = item;
            this.key = key;
        }
    }
    
    /**
     * Distributed Reservoir Sampling
     * Combine samples from multiple streams
     */
    @Complexity(time = "O(k log k)", space = "O(k)")
    public static <T> java.util.List<T> mergeReservoirs(
            java.util.List<java.util.List<T>> reservoirs,
            java.util.List<Integer> streamSizes,
            int k) {
        
        java.util.Random rand = new java.util.Random();
        java.util.List<T> merged = new java.util.ArrayList<>();
        
        for (int i = 0; i < reservoirs.size(); i++) {
            for (T item : reservoirs.get(i)) {
                merged.add(item);
            }
        }
        
        // Apply weighted sampling based on stream sizes
        java.util.List<T> result = new java.util.ArrayList<>();
        int totalSize = streamSizes.stream().mapToInt(Integer::intValue).sum();
        
        for (int i = 0; i < Math.min(k, merged.size()); i++) {
            int idx = rand.nextInt(merged.size() - i) + i;
            result.add(merged.get(idx));
            // Swap to avoid reselection
            T temp = merged.get(i);
            merged.set(i, merged.get(idx));
            merged.set(idx, temp);
        }
        
        return result;
    }
}

