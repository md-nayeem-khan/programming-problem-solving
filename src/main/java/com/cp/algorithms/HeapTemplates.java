package com.cp.algorithms;

import com.cp.datastructures.ListNode;
import com.cp.problems.Complexity;
import java.util.*;

/**
 * Heap and Priority Queue algorithm templates for coding interviews.
 * 
 * Coverage:
 * - MinHeap / MaxHeap implementation
 * - Heapify operations
 * - Heap Sort
 * - Top K Elements (various patterns)
 * - Median of Data Stream
 * - K-way Merge
 * - Merge K Sorted Lists
 * 
 * Critical for: Google, Amazon, Meta, Microsoft
 * Frequency: Appears in ~20% of Big Tech interviews
 */
public class HeapTemplates {

    // ============================================================================
    // MIN HEAP IMPLEMENTATION
    // ============================================================================
    
    /**
     * Min Heap data structure with standard heap operations.
     * Perfect binary tree stored in array with parent at i, children at 2i+1, 2i+2.
     */
    public static class MinHeap {
        private List<Integer> heap;
        
        public MinHeap() {
            heap = new ArrayList<>();
        }
        
        public MinHeap(int[] arr) {
            heap = new ArrayList<>();
            for (int val : arr) heap.add(val);
            buildHeap();
        }
        
        @Complexity(time = "O(log n)", space = "O(1)")
        public void insert(int val) {
            heap.add(val);
            heapifyUp(heap.size() - 1);
        }
        
        @Complexity(time = "O(log n)", space = "O(1)")
        public int extractMin() {
            if (heap.isEmpty()) throw new NoSuchElementException("Heap is empty");
            int min = heap.get(0);
            int last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                heapifyDown(0);
            }
            return min;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public int peek() {
            if (heap.isEmpty()) throw new NoSuchElementException("Heap is empty");
            return heap.get(0);
        }
        
        public int size() { return heap.size(); }
        public boolean isEmpty() { return heap.isEmpty(); }
        
        @Complexity(time = "O(n)", space = "O(1)")
        private void buildHeap() {
            // Start from last non-leaf node and heapify down
            for (int i = heap.size() / 2 - 1; i >= 0; i--) {
                heapifyDown(i);
            }
        }
        
        private void heapifyUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i) >= heap.get(parent)) break;
                swap(i, parent);
                i = parent;
            }
        }
        
        private void heapifyDown(int i) {
            int n = heap.size();
            while (true) {
                int smallest = i;
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                
                if (left < n && heap.get(left) < heap.get(smallest)) {
                    smallest = left;
                }
                if (right < n && heap.get(right) < heap.get(smallest)) {
                    smallest = right;
                }
                if (smallest == i) break;
                
                swap(i, smallest);
                i = smallest;
            }
        }
        
        private void swap(int i, int j) {
            int temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }
    
    // ============================================================================
    // MAX HEAP IMPLEMENTATION
    // ============================================================================
    
    /**
     * Max Heap data structure - useful for Kth largest problems.
     */
    public static class MaxHeap {
        private List<Integer> heap;
        
        public MaxHeap() {
            heap = new ArrayList<>();
        }
        
        public MaxHeap(int[] arr) {
            heap = new ArrayList<>();
            for (int val : arr) heap.add(val);
            buildHeap();
        }
        
        @Complexity(time = "O(log n)", space = "O(1)")
        public void insert(int val) {
            heap.add(val);
            heapifyUp(heap.size() - 1);
        }
        
        @Complexity(time = "O(log n)", space = "O(1)")
        public int extractMax() {
            if (heap.isEmpty()) throw new NoSuchElementException("Heap is empty");
            int max = heap.get(0);
            int last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                heapifyDown(0);
            }
            return max;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public int peek() {
            if (heap.isEmpty()) throw new NoSuchElementException("Heap is empty");
            return heap.get(0);
        }
        
        public int size() { return heap.size(); }
        public boolean isEmpty() { return heap.isEmpty(); }
        
        @Complexity(time = "O(n)", space = "O(1)")
        private void buildHeap() {
            for (int i = heap.size() / 2 - 1; i >= 0; i--) {
                heapifyDown(i);
            }
        }
        
        private void heapifyUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i) <= heap.get(parent)) break;
                swap(i, parent);
                i = parent;
            }
        }
        
        private void heapifyDown(int i) {
            int n = heap.size();
            while (true) {
                int largest = i;
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                
                if (left < n && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }
                if (right < n && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }
                if (largest == i) break;
                
                swap(i, largest);
                i = largest;
            }
        }
        
        private void swap(int i, int j) {
            int temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }
    
    // ============================================================================
    // HEAP SORT
    // ============================================================================
    
    /**
     * Heap Sort - sorts array in-place using max heap.
     * Interview tip: Mention it's NOT stable but in-place.
     */
    @Complexity(time = "O(n log n)", space = "O(1)")
    public static void heapSort(int[] arr) {
        int n = arr.length;
        
        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyDown(arr, n, i);
        }
        
        // Extract elements one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root (max) to end
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            
            // Heapify reduced heap
            heapifyDown(arr, i, 0);
        }
    }
    
    private static void heapifyDown(int[] arr, int n, int i) {
        while (true) {
            int largest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            
            if (left < n && arr[left] > arr[largest]) largest = left;
            if (right < n && arr[right] > arr[largest]) largest = right;
            if (largest == i) break;
            
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            i = largest;
        }
    }
    
    // ============================================================================
    // TOP K ELEMENTS PATTERNS
    // ============================================================================
    
    /**
     * Kth Largest Element using min heap.
     * Pattern: Maintain heap of size k with k largest elements.
     * Interview tip: O(n log k) better than O(n log n) sort for large n.
     */
    @Complexity(time = "O(n log k)", space = "O(k)")
    public static int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }
    
    /**
     * Kth Smallest Element using max heap.
     */
    @Complexity(time = "O(n log k)", space = "O(k)")
    public static int findKthSmallest(int[] nums, int k) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int num : nums) {
            maxHeap.offer(num);
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }
        return maxHeap.peek();
    }
    
    /**
     * Top K Frequent Elements.
     * Pattern: Use frequency map + min heap.
     * Companies: Amazon, Meta
     */
    @Complexity(time = "O(n log k)", space = "O(n)")
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            minHeap.offer(new int[]{entry.getKey(), entry.getValue()});
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll()[0];
        }
        return result;
    }
    
    /**
     * K Closest Points to Origin.
     * Pattern: Use max heap with custom comparator for distance.
     * Companies: Meta, Amazon
     */
    @Complexity(time = "O(n log k)", space = "O(k)")
    public static int[][] kClosest(int[][] points, int k) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> 
            (b[0] * b[0] + b[1] * b[1]) - (a[0] * a[0] + a[1] * a[1])
        );
        
        for (int[] point : points) {
            maxHeap.offer(point);
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }
        
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        return result;
    }
    
    /**
     * Last Stone Weight.
     * Pattern: Repeatedly extract max two elements, process, reinsert.
     * Companies: Amazon
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int stone : stones) {
            maxHeap.offer(stone);
        }
        
        while (maxHeap.size() > 1) {
            int first = maxHeap.poll();
            int second = maxHeap.poll();
            if (first != second) {
                maxHeap.offer(first - second);
            }
        }
        
        return maxHeap.isEmpty() ? 0 : maxHeap.peek();
    }
    
    // ============================================================================
    // MEDIAN OF DATA STREAM
    // ============================================================================
    
    /**
     * Find median from data stream using two heaps.
     * Pattern: Max heap for smaller half, min heap for larger half.
     * Balance heaps to keep sizes within 1.
     * 
     * Interview favorite: Meta, Google, Amazon
     */
    public static class MedianFinder {
        private PriorityQueue<Integer> maxHeap; // smaller half
        private PriorityQueue<Integer> minHeap; // larger half
        
        public MedianFinder() {
            maxHeap = new PriorityQueue<>((a, b) -> b - a);
            minHeap = new PriorityQueue<>();
        }
        
        @Complexity(time = "O(log n)", space = "O(1)")
        public void addNum(int num) {
            // Always add to max heap first
            maxHeap.offer(num);
            // Balance: move max from maxHeap to minHeap
            minHeap.offer(maxHeap.poll());
            
            // Ensure maxHeap has equal or 1 more element
            if (maxHeap.size() < minHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public double findMedian() {
            if (maxHeap.size() > minHeap.size()) {
                return maxHeap.peek();
            }
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }
    
    /**
     * Sliding Window Median using two heaps.
     * Pattern: Maintain two heaps + lazy removal.
     * Companies: Google, Meta
     */
    @Complexity(time = "O(n log k)", space = "O(k)")
    public static double[] medianSlidingWindow(int[] nums, int k) {
        double[] result = new double[nums.length - k + 1];
        TreeMap<Integer, Integer> maxHeap = new TreeMap<>((a, b) -> b - a);
        TreeMap<Integer, Integer> minHeap = new TreeMap<>();
        
        // Simplified version - using TreeMap for easier removal
        int maxSize = 0, minSize = 0;
        
        for (int i = 0; i < nums.length; i++) {
            // Add to max heap
            maxHeap.put(nums[i], maxHeap.getOrDefault(nums[i], 0) + 1);
            maxSize++;
            
            // Balance: move largest from maxHeap to minHeap
            int key = maxHeap.firstKey();
            minHeap.put(key, minHeap.getOrDefault(key, 0) + 1);
            minSize++;
            maxHeap.put(key, maxHeap.get(key) - 1);
            if (maxHeap.get(key) == 0) maxHeap.remove(key);
            maxSize--;
            
            // Keep maxHeap size >= minHeap size
            if (maxSize < minSize) {
                key = minHeap.firstKey();
                maxHeap.put(key, maxHeap.getOrDefault(key, 0) + 1);
                maxSize++;
                minHeap.put(key, minHeap.get(key) - 1);
                if (minHeap.get(key) == 0) minHeap.remove(key);
                minSize--;
            }
            
            // Remove oldest if window full
            if (i >= k) {
                int remove = nums[i - k];
                if (maxHeap.containsKey(remove)) {
                    maxHeap.put(remove, maxHeap.get(remove) - 1);
                    if (maxHeap.get(remove) == 0) maxHeap.remove(remove);
                    maxSize--;
                } else {
                    minHeap.put(remove, minHeap.get(remove) - 1);
                    if (minHeap.get(remove) == 0) minHeap.remove(remove);
                    minSize--;
                }
                
                // Rebalance after removal
                if (maxSize < minSize) {
                    key = minHeap.firstKey();
                    maxHeap.put(key, maxHeap.getOrDefault(key, 0) + 1);
                    maxSize++;
                    minHeap.put(key, minHeap.get(key) - 1);
                    if (minHeap.get(key) == 0) minHeap.remove(key);
                    minSize--;
                } else if (maxSize > minSize + 1) {
                    // If maxHeap has too many, move one to minHeap
                    key = maxHeap.firstKey();
                    minHeap.put(key, minHeap.getOrDefault(key, 0) + 1);
                    minSize++;
                    maxHeap.put(key, maxHeap.get(key) - 1);
                    if (maxHeap.get(key) == 0) maxHeap.remove(key);
                    maxSize--;
                }
            }
            
            if (i >= k - 1) {
                if (maxSize > minSize) {
                    result[i - k + 1] = maxHeap.firstKey();
                } else {
                    result[i - k + 1] = (maxHeap.firstKey() + minHeap.firstKey()) / 2.0;
                }
            }
        }
        
        return result;
    }
    
    // ============================================================================
    // K-WAY MERGE PATTERNS
    // ============================================================================
    
    /**
     * Merge K Sorted Arrays using min heap.
     * Pattern: Heap of (value, arrayIndex, elementIndex).
     * Companies: Google, Amazon
     */
    @Complexity(time = "O(n log k)", space = "O(k)")
    public static List<Integer> mergeKSortedArrays(int[][] arrays) {
        List<Integer> result = new ArrayList<>();
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // Initialize heap with first element from each array
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].length > 0) {
                minHeap.offer(new int[]{arrays[i][0], i, 0});
            }
        }
        
        while (!minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int value = curr[0];
            int arrayIdx = curr[1];
            int elemIdx = curr[2];
            
            result.add(value);
            
            // Add next element from same array
            if (elemIdx + 1 < arrays[arrayIdx].length) {
                minHeap.offer(new int[]{
                    arrays[arrayIdx][elemIdx + 1],
                    arrayIdx,
                    elemIdx + 1
                });
            }
        }
        
        return result;
    }
    
    /**
     * Merge K Sorted Linked Lists.
     * Pattern: Same as K-way merge but with ListNode.
     * Companies: Meta, Amazon, Google
     */
    @Complexity(time = "O(n log k)", space = "O(k)")
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));
        
        // Add first node of each list
        for (ListNode node : lists) {
            if (node != null) {
                minHeap.offer(node);
            }
        }
        
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (!minHeap.isEmpty()) {
            ListNode node = minHeap.poll();
            curr.next = node;
            curr = curr.next;
            
            if (node.next != null) {
                minHeap.offer(node.next);
            }
        }
        
        return dummy.next;
    }
    
    /**
     * Kth Smallest Element in Sorted Matrix.
     * Pattern: K-way merge on matrix rows.
     * Companies: Google, Meta
     */
    @Complexity(time = "O(k log n)", space = "O(n)")
    public static int kthSmallestInMatrix(int[][] matrix, int k) {
        int n = matrix.length;
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // Add first element of each row
        for (int i = 0; i < Math.min(n, k); i++) {
            minHeap.offer(new int[]{matrix[i][0], i, 0});
        }
        
        int result = 0;
        for (int i = 0; i < k; i++) {
            int[] curr = minHeap.poll();
            result = curr[0];
            int row = curr[1];
            int col = curr[2];
            
            if (col + 1 < n) {
                minHeap.offer(new int[]{matrix[row][col + 1], row, col + 1});
            }
        }
        
        return result;
    }
    
    /**
     * Find K Pairs with Smallest Sums.
     * Pattern: K-way merge on pairs.
     * Companies: Google, Amazon
     */
    @Complexity(time = "O(k log k)", space = "O(k)")
    public static List<int[]> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<int[]> result = new ArrayList<>();
        if (nums1.length == 0 || nums2.length == 0) return result;
        
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        
        // Start with pairs (nums1[i], nums2[0])
        for (int i = 0; i < Math.min(k, nums1.length); i++) {
            minHeap.offer(new int[]{nums1[i] + nums2[0], i, 0});
        }
        
        while (k > 0 && !minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int i = curr[1];
            int j = curr[2];
            result.add(new int[]{nums1[i], nums2[j]});
            
            if (j + 1 < nums2.length) {
                minHeap.offer(new int[]{nums1[i] + nums2[j + 1], i, j + 1});
            }
            k--;
        }
        
        return result;
    }
    
    // ============================================================================
    // ADVANCED HEAP PATTERNS
    // ============================================================================
    
    /**
     * Reorganize String (no two adjacent same chars).
     * Pattern: Greedy + max heap by frequency.
     * Companies: Meta, Amazon
     */
    @Complexity(time = "O(n log 26) = O(n)", space = "O(26) = O(1)")
    public static String reorganizeString(String s) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        
        PriorityQueue<Character> maxHeap = new PriorityQueue<>((a, b) -> 
            freq.get(b) - freq.get(a)
        );
        maxHeap.addAll(freq.keySet());
        
        StringBuilder sb = new StringBuilder();
        while (maxHeap.size() > 1) {
            char first = maxHeap.poll();
            char second = maxHeap.poll();
            
            sb.append(first);
            sb.append(second);
            
            freq.put(first, freq.get(first) - 1);
            freq.put(second, freq.get(second) - 1);
            
            if (freq.get(first) > 0) maxHeap.offer(first);
            if (freq.get(second) > 0) maxHeap.offer(second);
        }
        
        if (!maxHeap.isEmpty()) {
            char last = maxHeap.poll();
            if (freq.get(last) > 1) return ""; // impossible
            sb.append(last);
        }
        
        return sb.toString();
    }
    
    /**
     * Task Scheduler (CPU scheduling with cooldown).
     * Pattern: Max heap + cooldown tracking.
     * Companies: Meta, Amazon
     */
    @Complexity(time = "O(n)", space = "O(26) = O(1)")
    public static int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }
        
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int f : freq) {
            if (f > 0) maxHeap.offer(f);
        }
        
        int time = 0;
        while (!maxHeap.isEmpty()) {
            List<Integer> temp = new ArrayList<>();
            int tasksProcessed = 0;
            
            for (int i = 0; i <= n; i++) {
                if (!maxHeap.isEmpty()) {
                    int f = maxHeap.poll();
                    tasksProcessed++;
                    if (f > 1) temp.add(f - 1);
                }
            }
            
            for (int f : temp) {
                maxHeap.offer(f);
            }
            
            // Add time for this cycle
            // If heap is empty after putting back, this was the last cycle
            // Count only tasks processed, no idle time needed
            // Otherwise, count full cycle (n+1) which includes idle time
            time += maxHeap.isEmpty() ? tasksProcessed : n + 1;
        }
        
        return time;
    }
}
