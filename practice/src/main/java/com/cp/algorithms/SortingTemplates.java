package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Sorting Algorithm Templates for coding interviews.
 * 
 * Coverage:
 * - Quick Sort (standard, randomized, 3-way partition)
 * - Quick Select (Kth element in O(n) average)
 * - Dutch National Flag (3-way partition)
 * - Counting Sort
 * - Radix Sort
 * - Bucket Sort
 * - Topological Sort variants
 * 
 * Critical for: Microsoft, Amazon, Google
 * Frequency: Asked in ~8% of interviews
 */
public class SortingTemplates {
    
    // Static Random instance to avoid repeated instantiation in randomized methods
    private static final Random RANDOM = new Random();

    // ============================================================================
    // QUICK SORT
    // ============================================================================
    
    /**
     * Quick Sort - divide and conquer sorting.
     * Average O(n log n), worst O(n²) if always pick bad pivot.
     * 
     * Interview tip: Mention randomization for O(n log n) expected.
     */
    @Complexity(time = "O(n log n) average, O(n²) worst", space = "O(log n)")
    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }
    
    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        
        swap(arr, i + 1, high);
        return i + 1;
    }
    
    /**
     * Randomized Quick Sort - randomize pivot to avoid worst case.
     */
    @Complexity(time = "O(n log n) expected", space = "O(log n)")
    public static void quickSortRandomized(int[] arr) {
        quickSortRandomized(arr, 0, arr.length - 1);
    }
    
    private static void quickSortRandomized(int[] arr, int low, int high) {
        if (low < high) {
            int pi = randomizedPartition(arr, low, high);
            quickSortRandomized(arr, low, pi - 1);
            quickSortRandomized(arr, pi + 1, high);
        }
    }
    
    private static int randomizedPartition(int[] arr, int low, int high) {
        // Random pivot using static Random instance for performance
        int randomIndex = low + RANDOM.nextInt(high - low + 1);
        swap(arr, randomIndex, high);
        
        return partition(arr, low, high);
    }
    
    /**
     * 3-Way Quick Sort - handle many duplicates efficiently.
     * Partitions into < pivot, = pivot, > pivot.
     */
    @Complexity(time = "O(n log n)", space = "O(log n)")
    public static void quickSort3Way(int[] arr) {
        quickSort3Way(arr, 0, arr.length - 1);
    }
    
    private static void quickSort3Way(int[] arr, int low, int high) {
        if (low >= high) return;
        
        int pivot = arr[low];
        int lt = low;      // arr[low..lt-1] < pivot
        int gt = high;     // arr[gt+1..high] > pivot
        int i = low + 1;   // arr[lt..i-1] == pivot
        
        while (i <= gt) {
            if (arr[i] < pivot) {
                swap(arr, lt++, i++);
            } else if (arr[i] > pivot) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }
        
        quickSort3Way(arr, low, lt - 1);
        quickSort3Way(arr, gt + 1, high);
    }
    
    // ============================================================================
    // QUICK SELECT (KTH ELEMENT)
    // ============================================================================
    
    /**
     * Quick Select - find kth smallest element in O(n) average.
     * Like quick sort but only recurse on one partition.
     * 
     * Interview favorite: Amazon, Meta, Google
     */
    @Complexity(time = "O(n) average, O(n²) worst", space = "O(1)")
    public static int quickSelect(int[] arr, int k) {
        return quickSelect(arr, 0, arr.length - 1, k - 1);
    }
    
    private static int quickSelect(int[] arr, int low, int high, int k) {
        if (low == high) return arr[low];
        
        int pi = partition(arr, low, high);
        
        if (k == pi) {
            return arr[k];
        } else if (k < pi) {
            return quickSelect(arr, low, pi - 1, k);
        } else {
            return quickSelect(arr, pi + 1, high, k);
        }
    }
    
    /**
     * Randomized Quick Select - expected O(n).
     */
    @Complexity(time = "O(n) expected", space = "O(1)")
    public static int quickSelectRandomized(int[] arr, int k) {
        return quickSelectRandomized(arr, 0, arr.length - 1, k - 1);
    }
    
    private static int quickSelectRandomized(int[] arr, int low, int high, int k) {
        if (low == high) return arr[low];
        
        int pi = randomizedPartition(arr, low, high);
        
        if (k == pi) {
            return arr[k];
        } else if (k < pi) {
            return quickSelectRandomized(arr, low, pi - 1, k);
        } else {
            return quickSelectRandomized(arr, pi + 1, high, k);
        }
    }
    
    /**
     * Find kth largest element using quick select.
     */
    @Complexity(time = "O(n) average", space = "O(1)")
    public static int findKthLargest(int[] nums, int k) {
        return quickSelect(nums, nums.length - k + 1);
    }
    
    // ============================================================================
    // DUTCH NATIONAL FLAG (3-WAY PARTITION)
    // ============================================================================
    
    /**
     * Dutch National Flag - partition array into 3 sections.
     * Sort Colors (LeetCode #75) - sort array of 0s, 1s, 2s.
     * 
     * Pattern: Three pointers for three partitions.
     * 
     * Interview favorite: Microsoft, Amazon
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;
        
        while (mid <= high) {
            if (nums[mid] == 0) {
                swap(nums, low++, mid++);
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                swap(nums, mid, high--);
            }
        }
    }
    
    /**
     * General 3-way partition around pivot value.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static void partition3Way(int[] arr, int pivot) {
        int low = 0, mid = 0, high = arr.length - 1;
        
        while (mid <= high) {
            if (arr[mid] < pivot) {
                swap(arr, low++, mid++);
            } else if (arr[mid] == pivot) {
                mid++;
            } else {
                swap(arr, mid, high--);
            }
        }
    }
    
    // ============================================================================
    // COUNTING SORT
    // ============================================================================
    
    /**
     * Counting Sort - linear time sort for small range integers.
     * 
     * Use when: Range of values is small (e.g., 0-100).
     * 
     * Interview tip: Mention it's stable and O(n+k).
     */
    @Complexity(time = "O(n + k) where k=range", space = "O(k)")
    public static void countingSort(int[] arr) {
        if (arr.length == 0) return;
        
        int min = arr[0], max = arr[0];
        for (int num : arr) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        
        int range = max - min + 1;
        int[] count = new int[range];
        
        // Count occurrences
        for (int num : arr) {
            count[num - min]++;
        }
        
        // Cumulative count
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }
        
        // Build output (stable sort)
        int[] output = new int[arr.length];
        for (int i = arr.length - 1; i >= 0; i--) {
            int num = arr[i];
            output[count[num - min] - 1] = num;
            count[num - min]--;
        }
        
        System.arraycopy(output, 0, arr, 0, arr.length);
    }
    
    /**
     * Counting Sort for non-negative integers (simplified).
     * Requires all array elements to be in range [0, maxVal].
     */
    @Complexity(time = "O(n + k)", space = "O(k)")
    public static void countingSortSimple(int[] arr, int maxVal) {
        if (arr.length == 0) return;
        
        // Validate input range
        for (int num : arr) {
            if (num < 0 || num > maxVal) {
                throw new IllegalArgumentException(
                    "Array element " + num + " is outside valid range [0, " + maxVal + "]");
            }
        }
        
        int[] count = new int[maxVal + 1];
        
        for (int num : arr) {
            count[num]++;
        }
        
        int idx = 0;
        for (int i = 0; i <= maxVal; i++) {
            while (count[i]-- > 0) {
                arr[idx++] = i;
            }
        }
    }
    
    // ============================================================================
    // RADIX SORT
    // ============================================================================
    
    /**
     * Radix Sort - sort by processing digits from least to most significant.
     * 
     * Use when: Large range of non-negative integers.
     * Note: Only works with non-negative integers. Negative numbers will cause errors.
     */
    @Complexity(time = "O(d × (n + k)) where d=digits, k=base", space = "O(n + k)")
    public static void radixSort(int[] arr) {
        if (arr.length == 0) return;
        
        // Check for negative numbers
        for (int num : arr) {
            if (num < 0) {
                throw new IllegalArgumentException(
                    "Radix sort only works with non-negative integers. Found negative value: " + num);
            }
        }
        
        int max = Arrays.stream(arr).max().getAsInt();
        
        // Sort by each digit, using long to prevent overflow
        for (long exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(arr, (int)exp);
            
            // Check for overflow in next iteration to prevent infinite loop
            if (exp > Long.MAX_VALUE / 10) {
                break;
            }
        }
    }
    
    private static void countingSortByDigit(int[] arr, int exp) {
        int[] output = new int[arr.length];
        int[] count = new int[10];
        
        // Count occurrences of digits
        for (int num : arr) {
            int digit = (num / exp) % 10;
            count[digit]++;
        }
        
        // Cumulative count
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        
        // Build output (stable)
        for (int i = arr.length - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }
        
        System.arraycopy(output, 0, arr, 0, arr.length);
    }
    
    // ============================================================================
    // BUCKET SORT
    // ============================================================================
    
    /**
     * Bucket Sort - distribute elements into buckets, sort each bucket.
     * 
     * Use when: Input uniformly distributed over range.
     * Works well for floating point numbers in [0, 1).
     * Note: Requires all input values to be in range [0, 1.0].
     */
    @Complexity(time = "O(n + k) average, O(n²) worst", space = "O(n + k)")
    public static void bucketSort(float[] arr) {
        int n = arr.length;
        if (n <= 1) return;
        
        // Validate input range [0, 1.0]
        for (float num : arr) {
            if (num < 0.0f || num > 1.0f) {
                throw new IllegalArgumentException(
                    "Bucket sort requires all values in range [0, 1.0]. Found: " + num);
            }
            if (Float.isNaN(num) || Float.isInfinite(num)) {
                throw new IllegalArgumentException(
                    "Bucket sort does not support NaN or Infinite values. Found: " + num);
            }
        }
        
        // Create buckets
        @SuppressWarnings("unchecked")
        List<Float>[] buckets = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        // Distribute elements into buckets
        for (float num : arr) {
            int bucketIndex = (int) (n * num);
            if (bucketIndex == n) bucketIndex--; // Handle 1.0
            buckets[bucketIndex].add(num);
        }
        
        // Sort each bucket and concatenate
        int idx = 0;
        for (List<Float> bucket : buckets) {
            Collections.sort(bucket);
            for (float num : bucket) {
                arr[idx++] = num;
            }
        }
    }
    
    /**
     * Bucket Sort for integers with known range.
     */
    @Complexity(time = "O(n + k)", space = "O(n + k)")
    public static void bucketSortIntegers(int[] arr, int minVal, int maxVal, int numBuckets) {
        if (arr.length <= 1) return;
        if (numBuckets <= 0) {
            throw new IllegalArgumentException("Number of buckets must be positive");
        }
        
        // Prevent excessive memory usage
        if (numBuckets > 1000000) {
            throw new IllegalArgumentException("Number of buckets too large (max 1,000,000): " + numBuckets);
        }
        
        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new ArrayList[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        int range = maxVal - minVal + 1;
        // Prevent division by zero when numBuckets is very large
        int bucketSize = Math.max(1, (range + numBuckets - 1) / numBuckets);
        
        for (int num : arr) {
            int bucketIndex = (num - minVal) / bucketSize;
            if (bucketIndex >= numBuckets) bucketIndex = numBuckets - 1;
            buckets[bucketIndex].add(num);
        }
        
        int idx = 0;
        for (List<Integer> bucket : buckets) {
            Collections.sort(bucket);
            for (int num : bucket) {
                arr[idx++] = num;
            }
        }
    }
    
    // ============================================================================
    // TOPOLOGICAL SORT (GRAPH ALGORITHMS)
    // ============================================================================
    
    /**
     * Kahn's Algorithm - topological sort using BFS.
     * Already in GraphAlgorithms.java, included here for completeness.
     */
    @Complexity(time = "O(V + E)", space = "O(V)")
    public static List<Integer> topologicalSortKahn(int n, List<List<Integer>> adj) {
        int[] inDegree = new int[n];
        
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                inDegree[v]++;
            }
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.add(u);
            
            for (int v : adj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }
        
        return result.size() == n ? result : new ArrayList<>();
    }
    
    // ============================================================================
    // MERGE INTERVALS (SORT-BASED)
    // ============================================================================
    
    /**
     * Sort-based interval merging (already in AdvancedDataStructures).
     * Included for sorting context.
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals.length <= 1) return intervals;
        
        // Use Integer.compare to prevent overflow
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        List<int[]> merged = new ArrayList<>();
        int[] current = intervals[0];
        
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) {
                // Overlapping, merge
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                // No overlap, add current and move to next
                merged.add(current);
                current = intervals[i];
            }
        }
        merged.add(current);
        
        return merged.toArray(new int[merged.size()][]);
    }
    
    // ============================================================================
    // CUSTOM COMPARATOR EXAMPLES
    // ============================================================================
    
    /**
     * Sort array by custom criteria - example patterns.
     */
    public static class ComparatorExamples {
        
        // Sort by absolute value
        public static void sortByAbsoluteValue(int[] arr) {
            Integer[] boxed = Arrays.stream(arr).boxed().toArray(Integer[]::new);
            Arrays.sort(boxed, (a, b) -> {
                // Use long to avoid overflow with Integer.MIN_VALUE
                int cmp = Long.compare(Math.abs((long)a), Math.abs((long)b));
                if (cmp != 0) return cmp;
                // Secondary sort: when absolute values are equal, prefer negative first
                return Integer.compare(a, b);
            });
            for (int i = 0; i < arr.length; i++) arr[i] = boxed[i];
        }
        
        // Sort strings by length, then lexicographically
        public static void sortStringsByLength(String[] arr) {
            Arrays.sort(arr, (a, b) -> {
                if (a.length() != b.length()) {
                    return a.length() - b.length();
                }
                return a.compareTo(b);
            });
        }
        
        // Sort 2D array by first element, then second
        public static void sort2DArray(int[][] arr) {
            Arrays.sort(arr, (a, b) -> {
                int cmp = Integer.compare(a[0], b[0]);
                if (cmp != 0) return cmp;
                return Integer.compare(a[1], b[1]);
            });
        }
        
        // Sort in descending order
        public static void sortDescending(int[] arr) {
            Integer[] boxed = Arrays.stream(arr).boxed().toArray(Integer[]::new);
            Arrays.sort(boxed, (a, b) -> Integer.compare(b, a));
            for (int i = 0; i < arr.length; i++) arr[i] = boxed[i];
        }
    }
    
    // ============================================================================
    // UTILITY METHODS
    // ============================================================================
    
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    /**
     * Check if array is sorted.
     */
    public static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) return false;
        }
        return true;
    }
    
    /**
     * Partition array around kth element (quick select based).
     * After partitioning, element at index k-1 is the kth smallest element.
     * Note: Only partitions the array - elements in each partition are not sorted.
     */
    @Complexity(time = "O(n) average", space = "O(1)")
    public static void partitionAroundKth(int[] arr, int k) {
        quickSelectRandomized(arr, k);
        // After this, element at index k-1 is in its final position,
        // with smaller elements before it and larger elements after it
    }
}
