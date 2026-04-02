package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Array Pattern Algorithms for Big Tech Interviews.
 * 
 * Coverage:
 * 1. Boyer-Moore Voting Algorithm (Majority Element)
 * 2. Dutch National Flag (3-way Partitioning)
 * 3. Fisher-Yates Shuffle
 * 4. Reservoir Sampling
 * 5. Partitioning Algorithms
 * 
 * Critical for: All FAANG companies
 * Frequency: 8-12% of interviews combined
 */
public class ArrayPatterns {

    // ============================================================================
    // 1. BOYER-MOORE VOTING ALGORITHM
    // ============================================================================
    
    /**
     * Boyer-Moore Majority Vote Algorithm.
     * Find element appearing more than n/2 times.
     * 
     * Key insight: If majority element exists, it survives cancellation.
     * Cancel pairs of different elements; majority element remains.
     * 
     * Real interview problems:
     * - LC 169: Majority Element
     * - LC 229: Majority Element II
     * 
     * Interview favorites: Amazon (very common), Meta, Microsoft
     * 
     * Time: O(n), Space: O(1)
     * 
     * Example:
     *   [2,2,1,1,1,2,2] → 2 (appears 4 times > 7/2)
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int majorityElement(int[] nums) {
        int candidate = 0;
        int count = 0;
        
        // Phase 1: Find candidate
        for (int num : nums) {
            if (count == 0) {
                candidate = num;
            }
            count += (num == candidate) ? 1 : -1;
        }
        
        // Phase 2: Verify candidate (optional if guaranteed to exist)
        count = 0;
        for (int num : nums) {
            if (num == candidate) count++;
        }
        
        return count > nums.length / 2 ? candidate : -1;
    }
    
    /**
     * Boyer-Moore for elements appearing more than n/3 times.
     * At most 2 elements can appear more than n/3 times.
     * 
     * Pattern: Track 2 candidates and 2 counts.
     * 
     * Interview favorite: Amazon, Google (harder variant)
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static List<Integer> majorityElementII(int[] nums) {
        if (nums == null || nums.length == 0) return new ArrayList<>();
        
        // Phase 1: Find up to 2 candidates
        Integer candidate1 = null, candidate2 = null;
        int count1 = 0, count2 = 0;
        
        for (int num : nums) {
            if (candidate1 != null && num == candidate1) {
                count1++;
            } else if (candidate2 != null && num == candidate2) {
                count2++;
            } else if (count1 == 0) {
                candidate1 = num;
                count1 = 1;
            } else if (count2 == 0) {
                candidate2 = num;
                count2 = 1;
            } else {
                count1--;
                count2--;
            }
        }
        
        // Phase 2: Verify candidates
        count1 = 0;
        count2 = 0;
        for (int num : nums) {
            if (candidate1 != null && num == candidate1) count1++;
            else if (candidate2 != null && num == candidate2) count2++;
        }
        
        List<Integer> result = new ArrayList<>();
        if (count1 > nums.length / 3) result.add(candidate1);
        if (candidate2 != null && count2 > nums.length / 3 && !candidate2.equals(candidate1)) {
            result.add(candidate2);
        }
        
        return result;
    }
    
    /**
     * Generalized Boyer-Moore for k candidates (elements appearing > n/(k+1) times).
     * At most k elements can appear more than n/(k+1) times.
     * 
     * Interview favorite: Google (hard)
     */
    @Complexity(time = "O(n × k)", space = "O(k)")
    public static List<Integer> majorityElementK(int[] nums, int k) {
        Map<Integer, Integer> candidates = new HashMap<>();
        
        // Phase 1: Find up to k candidates
        for (int num : nums) {
            if (candidates.containsKey(num)) {
                candidates.put(num, candidates.get(num) + 1);
            } else if (candidates.size() < k) {
                candidates.put(num, 1);
            } else {
                // Decrease all counts
                List<Integer> toRemove = new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : candidates.entrySet()) {
                    if (entry.getValue() == 1) {
                        toRemove.add(entry.getKey());
                    } else {
                        candidates.put(entry.getKey(), entry.getValue() - 1);
                    }
                }
                for (int key : toRemove) candidates.remove(key);
            }
        }
        
        // Phase 2: Verify candidates
        for (int key : candidates.keySet()) {
            candidates.put(key, 0);
        }
        for (int num : nums) {
            if (candidates.containsKey(num)) {
                candidates.put(num, candidates.get(num) + 1);
            }
        }
        
        List<Integer> result = new ArrayList<>();
        int threshold = nums.length / (k + 1);
        for (Map.Entry<Integer, Integer> entry : candidates.entrySet()) {
            if (entry.getValue() > threshold) {
                result.add(entry.getKey());
            }
        }
        
        return result;
    }
    
    // ============================================================================
    // 2. DUTCH NATIONAL FLAG (3-WAY PARTITIONING)
    // ============================================================================
    
    /**
     * Dutch National Flag Algorithm (3-way partitioning).
     * Partition array into 3 sections: < pivot, = pivot, > pivot.
     * 
     * Invented by Edsger Dijkstra.
     * 
     * Real interview problems:
     * - LC 75: Sort Colors
     * - Three-way quicksort partition
     * 
     * Interview favorites: Meta, Amazon, Microsoft
     * 
     * Time: O(n), Space: O(1)
     * Pattern: 3 pointers (low, mid, high)
     * - [0..low): values < pivot
     * - [low..mid): values = pivot
     * - [mid..high]: unprocessed
     * - (high..n): values > pivot
     * 
     * Example:
     *   [2,0,2,1,1,0] → [0,0,1,1,2,2]
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static void sortColors(int[] nums) {
        int low = 0;      // Next position for 0
        int mid = 0;      // Current element
        int high = nums.length - 1; // Next position for 2
        
        while (mid <= high) {
            if (nums[mid] == 0) {
                swap(nums, low, mid);
                low++;
                mid++;
            } else if (nums[mid] == 1) {
                mid++;
            } else { // nums[mid] == 2
                swap(nums, mid, high);
                high--;
                // Don't increment mid - need to check swapped element
            }
        }
    }
    
    /**
     * Generic 3-way partition around pivot.
     * Partitions array so that:
     * - Elements < pivot are at the start
     * - Elements = pivot are in the middle
     * - Elements > pivot are at the end
     * 
     * Returns [left, right] where:
     * - arr[0..left) < pivot
     * - arr[left..right] = pivot
     * - arr(right..n) > pivot
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int[] threeWayPartition(int[] nums, int pivot) {
        int low = 0;
        int mid = 0;
        int high = nums.length - 1;
        
        while (mid <= high) {
            if (nums[mid] < pivot) {
                swap(nums, low, mid);
                low++;
                mid++;
            } else if (nums[mid] == pivot) {
                mid++;
            } else {
                swap(nums, mid, high);
                high--;
            }
        }
        
        return new int[]{low, high};
    }
    
    /**
     * Wiggle Sort II - Rearrange array so that nums[0] < nums[1] > nums[2] < nums[3]...
     * Uses median partitioning and proper element placement.
     * 
     * Algorithm:
     * 1. Sort array to find median and partition elements
     * 2. Place smaller half at even indices (0, 2, 4...)  
     * 3. Place larger half at odd indices (1, 3, 5...)
     * 4. Use reverse order to handle duplicates properly
     * 
     * Interview favorite: Meta, Google (hard)
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static void wiggleSort(int[] nums) {
        int n = nums.length;
        if (n <= 1) return;
        
        // Create sorted copy to get proper element distribution
        int[] sorted = nums.clone();
        java.util.Arrays.sort(sorted);
        
        // Place elements in wiggle pattern
        // Smaller half goes to even indices (peaks): (n-1)/2, (n-1)/2-1, ...
        // Larger half goes to odd indices (valleys): n-1, n-2, ...
        int smallIdx = (n - 1) / 2;  // Last index of smaller half
        int largeIdx = n - 1;        // Last index of larger half
        
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                // Even indices get smaller elements (in reverse order to avoid duplicates)
                nums[i] = sorted[smallIdx--];
            } else {
                // Odd indices get larger elements (in reverse order)
                nums[i] = sorted[largeIdx--];
            }
        }
    }
    
    /**
     * Quickselect to find kth smallest element.
     */
    private static int findKthElement(int[] nums, int k) {
        int[] copy = nums.clone();
        return quickSelect(copy, 0, copy.length - 1, k);
    }
    
    private static int quickSelect(int[] nums, int left, int right, int k) {
        if (left == right) return nums[left];
        
        int pivotIndex = partition(nums, left, right);
        
        if (k == pivotIndex) {
            return nums[k];
        } else if (k < pivotIndex) {
            return quickSelect(nums, left, pivotIndex - 1, k);
        } else {
            return quickSelect(nums, pivotIndex + 1, right, k);
        }
    }
    
    private static int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int i = left;
        
        for (int j = left; j < right; j++) {
            if (nums[j] <= pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        
        swap(nums, i, right);
        return i;
    }
    
    // ============================================================================
    // 3. FISHER-YATES SHUFFLE
    // ============================================================================
    
    /**
     * Fisher-Yates Shuffle Algorithm (Knuth Shuffle).
     * Generates uniformly random permutation in O(n) time.
     * 
     * Key property: Every permutation has equal probability (1/n!).
     * 
     * Interview favorites: Google, Meta
     * 
     * Time: O(n), Space: O(1)
     * 
     * Common mistakes to avoid:
     * - Don't swap with random from [0, n) - this creates bias
     * - Don't shuffle forward - must shuffle backward from end
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static void fisherYatesShuffle(int[] nums) {
        Random random = new Random();
        
        for (int i = nums.length - 1; i > 0; i--) {
            // Pick random index from 0 to i (inclusive)
            int j = random.nextInt(i + 1);
            swap(nums, i, j);
        }
    }
    
    /**
     * Shuffle with custom random source.
     */
    public static void shuffle(int[] nums, Random random) {
        for (int i = nums.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(nums, i, j);
        }
    }
    
    // ============================================================================
    // 4. RESERVOIR SAMPLING
    // ============================================================================
    
    /**
     * Reservoir Sampling Algorithm.
     * Randomly sample k elements from stream of unknown size.
     * Each element has equal probability k/n of being selected.
     * 
     * Real use cases:
     * - Sample from large log files
     * - Random selection from database query results
     * - Streaming data sampling
     * 
     * Interview favorites: Google, Meta, Amazon
     * 
     * Time: O(n), Space: O(k)
     * 
     * Example:
     *   Stream: [1,2,3,4,5,6,7,8,9,10], k=3
     *   Each element has 3/10 probability in final sample
     */
    @Complexity(time = "O(n)", space = "O(k)")
    public static int[] reservoirSample(int[] stream, int k) {
        int[] reservoir = new int[k];
        Random random = new Random();
        
        // Fill reservoir with first k elements
        for (int i = 0; i < k && i < stream.length; i++) {
            reservoir[i] = stream[i];
        }
        
        // Process remaining elements
        for (int i = k; i < stream.length; i++) {
            // Random index from 0 to i
            int j = random.nextInt(i + 1);
            
            // If j < k, replace element at j
            if (j < k) {
                reservoir[j] = stream[i];
            }
        }
        
        return reservoir;
    }
    
    /**
     * Reservoir sampling for linked list (LC 382).
     * Sample one random node with equal probability.
     */
    public static class ReservoirSampler {
        private Random random = new Random();
        
        /**
         * Returns random element from list with equal probability.
         */
        public int sample(int[] nums) {
            int result = 0;
            int count = 0;
            
            for (int num : nums) {
                count++;
                // Replace with probability 1/count
                if (random.nextInt(count) == 0) {
                    result = num;
                }
            }
            
            return result;
        }
    }
    
    /**
     * Weighted Random Sampling.
     * Sample element with probability proportional to its weight.
     * 
     * Interview favorite: Meta, Google
     */
    public static class WeightedRandomSampler {
        private int[] prefixSum;
        private int totalSum;
        private Random random = new Random();
        
        public WeightedRandomSampler(int[] weights) {
            prefixSum = new int[weights.length];
            prefixSum[0] = weights[0];
            
            for (int i = 1; i < weights.length; i++) {
                prefixSum[i] = prefixSum[i - 1] + weights[i];
            }
            
            totalSum = prefixSum[weights.length - 1];
        }
        
        /**
         * Returns random index with probability proportional to weight.
         */
        public int sample() {
            int target = random.nextInt(totalSum);
            
            // Binary search for target in prefixSum
            int left = 0, right = prefixSum.length - 1;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (prefixSum[mid] <= target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            return left;
        }
    }
    
    // ============================================================================
    // 5. PARTITIONING ALGORITHMS
    // ============================================================================
    
    /**
     * Move all zeros to end while maintaining relative order.
     * 
     * Interview favorite: Amazon, Microsoft
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static void moveZeroes(int[] nums) {
        int writePos = 0;
        
        // Move all non-zeros forward
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[writePos++] = nums[i];
            }
        }
        
        // Fill remaining with zeros
        while (writePos < nums.length) {
            nums[writePos++] = 0;
        }
    }
    
    /**
     * Partition array into even and odd (maintaining relative order).
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static void partitionEvenOdd(int[] nums) {
        int writePos = 0;
        
        // First pass: move all even numbers forward
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] % 2 == 0) {
                swap(nums, writePos++, i);
            }
        }
    }
    
    /**
     * Partition array by predicate while maintaining order.
     * Uses auxiliary array for true O(n) stable partitioning.
     * 
     * Algorithm:
     * 1. Create temporary array to store result
     * 2. First pass: copy elements matching predicate
     * 3. Second pass: copy elements not matching predicate
     * 4. Copy result back to original array
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static void stablePartition(int[] nums, java.util.function.Predicate<Integer> predicate) {
        int n = nums.length;
        if (n <= 1) return;
        
        // Use auxiliary array for O(n) stable partition
        int[] temp = new int[n];
        int writePos = 0;
        
        // First pass: copy elements that match predicate (maintaining order)
        for (int i = 0; i < n; i++) {
            if (predicate.test(nums[i])) {
                temp[writePos++] = nums[i];
            }
        }
        
        // Second pass: copy elements that don't match predicate (maintaining order)
        for (int i = 0; i < n; i++) {
            if (!predicate.test(nums[i])) {
                temp[writePos++] = nums[i];
            }
        }
        
        // Copy result back to original array
        System.arraycopy(temp, 0, nums, 0, n);
    }
    
    // ============================================================================
    // HELPER METHODS
    // ============================================================================
    
    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
