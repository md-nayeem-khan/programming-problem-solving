package com.cp.algorithms;

import com.cp.problems.Complexity;

/**
 * Essential search and pointer templates:
 *   1. Binary Search (classic + variants)
 *   2. Two Pointers
 *   3. Sliding Window
 *   4. Prefix Sum
 */
public class SearchTemplates {

    // ---------------------------------------------------------------
    // 1a. Binary Search — find exact target, returns -1 if not found
    // ---------------------------------------------------------------
    @Complexity(time = "O(log n)", space = "O(1)")
    public static int binarySearch(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return mid;
            if (nums[mid] < target)  lo = mid + 1;
            else                     hi = mid - 1;
        }
        return -1;
    }

    // ---------------------------------------------------------------
    // 1b. Lower bound — first index where nums[i] >= target
    //     (equivalent to Java's Collections.binarySearch lower bound)
    // ---------------------------------------------------------------
    @Complexity(time = "O(log n)", space = "O(1)")
    public static int lowerBound(int[] nums, int target) {
        int lo = 0, hi = nums.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] < target) lo = mid + 1;
            else                    hi = mid;
        }
        return lo; // lo == nums.length if target > all elements
    }

    // ---------------------------------------------------------------
    // 1c. Upper bound — first index where nums[i] > target
    // ---------------------------------------------------------------
    @Complexity(time = "O(log n)", space = "O(1)")
    public static int upperBound(int[] nums, int target) {
        int lo = 0, hi = nums.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] <= target) lo = mid + 1;
            else                     hi = mid;
        }
        return lo;
    }

    // ---------------------------------------------------------------
    // 1d. Binary search on answer — classic "feasibility" template
    //     Override feasible() with your condition
    // ---------------------------------------------------------------
    public static int binarySearchOnAnswer(int lo, int hi, java.util.function.IntPredicate feasible) {
        // Find minimum k such that feasible(k) == true
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (feasible.test(mid)) hi = mid;
            else                    lo = mid + 1;
        }
        return lo;
    }

    // ---------------------------------------------------------------
    // 2. Two Pointers — find pair with given sum in sorted array
    // ---------------------------------------------------------------
    public static int[] twoSum(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int sum = nums[lo] + nums[hi];
            if (sum == target) return new int[]{lo, hi};
            if (sum < target)  lo++;
            else               hi--;
        }
        return new int[]{-1, -1};
    }

    // ---------------------------------------------------------------
    // 3a. Sliding Window (fixed size k) — max sum subarray
    // ---------------------------------------------------------------
    public static int maxSumFixed(int[] nums, int k) {
        int windowSum = 0;
        for (int i = 0; i < k; i++) windowSum += nums[i];
        int maxSum = windowSum;
        for (int i = k; i < nums.length; i++) {
            windowSum += nums[i] - nums[i - k];
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    // ---------------------------------------------------------------
    // 3b. Sliding Window (variable size) — min length subarray with sum >= target
    // ---------------------------------------------------------------
    public static int minSubarrayLen(int[] nums, int target) {
        int left = 0, sum = 0, minLen = Integer.MAX_VALUE;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum >= target) {
                minLen = Math.min(minLen, right - left + 1);
                sum -= nums[left++];
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    // ---------------------------------------------------------------
    // 4. Prefix Sum — build in O(n), query range sum in O(1)
    // ---------------------------------------------------------------
    public static class PrefixSum {
        private final long[] prefix;

        public PrefixSum(int[] nums) {
            prefix = new long[nums.length + 1];
            for (int i = 0; i < nums.length; i++) prefix[i + 1] = prefix[i] + nums[i];
        }

        /** Sum of nums[l..r] inclusive, 0-indexed */
        public long rangeSum(int l, int r) { return prefix[r + 1] - prefix[l]; }
    }

    // ---------------------------------------------------------------
    // 4b. 2D Prefix Sum
    // ---------------------------------------------------------------
    public static class PrefixSum2D {
        private final long[][] prefix;

        public PrefixSum2D(int[][] matrix) {
            int rows = matrix.length, cols = matrix[0].length;
            prefix = new long[rows + 1][cols + 1];
            for (int r = 1; r <= rows; r++)
                for (int c = 1; c <= cols; c++)
                    prefix[r][c] = matrix[r-1][c-1] + prefix[r-1][c] + prefix[r][c-1] - prefix[r-1][c-1];
        }

        /** Sum in rectangle (r1,c1) to (r2,c2) inclusive, 0-indexed */
        public long query(int r1, int c1, int r2, int c2) {
            return prefix[r2+1][c2+1] - prefix[r1][c2+1] - prefix[r2+1][c1] + prefix[r1][c1];
        }
    }
    
    // ---------------------------------------------------------------
    // 5. MEET IN THE MIDDLE
    // ---------------------------------------------------------------
    
    /**
     * Meet in the Middle Algorithm.
     * 
     * Optimization for exponential search problems.
     * Instead of O(2^n), reduce to O(2^(n/2)) by splitting in half.
     * 
     * Strategy:
     * 1. Split input into two halves
     * 2. Generate all possibilities for each half
     * 3. Combine results using hash map or binary search
     * 
     * Real interview problems:
     * - LC 1755: Closest Subsequence Sum
     * - LC 805: Split Array With Same Average
     * - Subset Sum with large n (n <= 40)
     * 
     * Interview favorites: Google (hard), Meta
     * 
     * Time: O(2^(n/2) × log(2^(n/2))) = O(n × 2^(n/2))
     * Space: O(2^(n/2))
     * 
     * Example: Can we make sum = target using subset of nums?
     *   nums = [1,5,7,9,2,12,8,3], target = 15
     *   Split: [1,5,7,9] and [2,12,8,3]
     *   Generate all subset sums for each half, find complement
     */
    @Complexity(time = "O(n × 2^(n/2))", space = "O(2^(n/2))")
    public static boolean subsetSumMeetInMiddle(int[] nums, int target) {
        int n = nums.length;
        int mid = n / 2;
        
        // Generate all subset sums for first half
        java.util.List<Integer> leftSums = new java.util.ArrayList<>();
        generateSubsetSums(nums, 0, mid, 0, leftSums);
        
        // Generate all subset sums for second half
        java.util.List<Integer> rightSums = new java.util.ArrayList<>();
        generateSubsetSums(nums, mid, n, 0, rightSums);
        
        // Sort right sums for binary search
        java.util.Collections.sort(rightSums);
        
        // For each left sum, check if complement exists in right sums
        for (int leftSum : leftSums) {
            int needed = target - leftSum;
            int pos = java.util.Collections.binarySearch(rightSums, needed);
            if (pos >= 0) return true;
        }
        
        return false;
    }
    
    private static void generateSubsetSums(int[] nums, int start, int end, 
                                          int currentSum, java.util.List<Integer> sums) {
        if (start == end) {
            sums.add(currentSum);
            return;
        }
        
        // Include nums[start]
        generateSubsetSums(nums, start + 1, end, currentSum + nums[start], sums);
        
        // Exclude nums[start]
        generateSubsetSums(nums, start + 1, end, currentSum, sums);
    }
    
    /**
     * Closest Subsequence Sum using Meet in the Middle.
     * Find subsequence sum closest to target (minimize |sum - target|).
     * 
     * Interview favorite: Google (hard)
     */
    @Complexity(time = "O(n × 2^(n/2))", space = "O(2^(n/2))")
    public static int closestSubsequenceSum(int[] nums, int target) {
        int n = nums.length;
        int mid = n / 2;
        
        java.util.List<Integer> leftSums = new java.util.ArrayList<>();
        generateSubsetSums(nums, 0, mid, 0, leftSums);
        
        java.util.List<Integer> rightSums = new java.util.ArrayList<>();
        generateSubsetSums(nums, mid, n, 0, rightSums);
        
        java.util.Collections.sort(rightSums);
        
        int minDiff = Integer.MAX_VALUE;
        int closestSum = 0;
        
        for (int leftSum : leftSums) {
            int needed = target - leftSum;
            
            // Binary search for closest value in rightSums
            int pos = java.util.Collections.binarySearch(rightSums, needed);
            
            if (pos >= 0) {
                // Exact match found
                return target;
            } else {
                // Find insertion point
                pos = -(pos + 1);
                
                // Check element at pos (first element >= needed)
                if (pos < rightSums.size()) {
                    int sum = leftSum + rightSums.get(pos);
                    int diff = Math.abs(sum - target);
                    if (diff < minDiff) {
                        minDiff = diff;
                        closestSum = sum;
                    }
                }
                
                // Check element at pos-1 (last element < needed)
                if (pos > 0) {
                    int sum = leftSum + rightSums.get(pos - 1);
                    int diff = Math.abs(sum - target);
                    if (diff < minDiff) {
                        minDiff = diff;
                        closestSum = sum;
                    }
                }
            }
        }
        
        return closestSum;
    }
    
    /**
     * Count subsets with sum in range [lower, upper].
     * 
     * Pattern: Meet in middle + two pointers.
     */
    @Complexity(time = "O(n × 2^(n/2))", space = "O(2^(n/2))")
    public static int countSubsetsInRange(int[] nums, int lower, int upper) {
        int n = nums.length;
        int mid = n / 2;
        
        java.util.List<Integer> leftSums = new java.util.ArrayList<>();
        generateSubsetSums(nums, 0, mid, 0, leftSums);
        
        java.util.List<Integer> rightSums = new java.util.ArrayList<>();
        generateSubsetSums(nums, mid, n, 0, rightSums);
        
        java.util.Collections.sort(rightSums);
        
        int count = 0;
        
        for (int leftSum : leftSums) {
            // Find range in rightSums where leftSum + rightSum is in [lower, upper]
            int minNeeded = lower - leftSum;
            int maxNeeded = upper - leftSum;
            
            // Find first position >= minNeeded
            int left = lowerBoundList(rightSums, minNeeded);
            
            // Find first position > maxNeeded
            int right = upperBoundList(rightSums, maxNeeded);
            
            count += (right - left);
        }
        
        return count;
    }
    
    private static int lowerBoundList(java.util.List<Integer> list, int target) {
        int lo = 0, hi = list.size();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (list.get(mid) < target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
    
    private static int upperBoundList(java.util.List<Integer> list, int target) {
        int lo = 0, hi = list.size();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (list.get(mid) <= target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
    
    /**
     * Partition array into two subsets with equal sum using Meet in Middle.
     * 
     * Pattern: Check if any subset sum equals totalSum/2.
     */
    @Complexity(time = "O(n × 2^(n/2))", space = "O(2^(n/2))")
    public static boolean canPartitionMeetInMiddle(int[] nums) {
        int totalSum = 0;
        for (int num : nums) totalSum += num;
        
        if (totalSum % 2 != 0) return false;
        int target = totalSum / 2;
        
        return subsetSumMeetInMiddle(nums, target);
    }
    
    /**
     * Maximum XOR of two subsets using Meet in Middle.
     * 
     * Interview favorite: Google (very hard)
     */
    @Complexity(time = "O(n × 2^(n/2))", space = "O(2^(n/2))")
    public static int maxXORSubsets(int[] nums) {
        int n = nums.length;
        int mid = n / 2;
        
        java.util.List<Integer> leftXORs = new java.util.ArrayList<>();
        generateSubsetXORs(nums, 0, mid, 0, leftXORs);
        
        java.util.List<Integer> rightXORs = new java.util.ArrayList<>();
        generateSubsetXORs(nums, mid, n, 0, rightXORs);
        
        // Sort right for optimization (though XOR doesn't benefit from binary search)
        java.util.Set<Integer> rightSet = new java.util.HashSet<>(rightXORs);
        
        int maxXOR = 0;
        
        for (int leftXOR : leftXORs) {
            for (int rightXOR : rightSet) {
                maxXOR = Math.max(maxXOR, leftXOR ^ rightXOR);
            }
        }
        
        return maxXOR;
    }
    
    private static void generateSubsetXORs(int[] nums, int start, int end,
                                          int currentXOR, java.util.List<Integer> xors) {
        if (start == end) {
            xors.add(currentXOR);
            return;
        }
        
        // Include nums[start]
        generateSubsetXORs(nums, start + 1, end, currentXOR ^ nums[start], xors);
        
        // Exclude nums[start]
        generateSubsetXORs(nums, start + 1, end, currentXOR, xors);
    }
}

