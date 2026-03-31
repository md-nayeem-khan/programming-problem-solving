package com.cp.algorithms;

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
}
