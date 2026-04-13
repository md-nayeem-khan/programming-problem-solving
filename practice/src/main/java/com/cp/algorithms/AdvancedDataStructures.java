package com.cp.algorithms;

import com.cp.problems.Complexity;

import java.util.*;

/**
 * Advanced data structure patterns commonly seen in FAANG interviews.
 *
 * Covers:
 *   1. Monotonic Stack (increasing/decreasing)
 *   2. Monotonic Queue (sliding window maximum)
 *   3. Min Stack / Max Stack
 *   4. Design patterns (LRU Cache, LFU Cache)
 *   5. Interval operations
 *
 * These patterns appear frequently in interviews at Google, Meta, Amazon, etc.
 */
public class AdvancedDataStructures {

    // =========================================================
    // 1. MONOTONIC STACK
    // =========================================================

    /**
     * Next Greater Element — for each element, find next element greater than it
     * Example: [2,1,2,4,3] → [4,2,4,-1,-1]
     * Time: O(n), Space: O(n)
     * Pattern: Use monotonic decreasing stack
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static int[] nextGreaterElement(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>(); // store indices
        
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                result[stack.pop()] = nums[i];
            }
            stack.push(i);
        }
        return result;
    }

    /**
     * Next Greater Element (Circular Array)
     * Example: [1,2,1] → [2,-1,2] (circular: after 1 comes 1,2,1 again)
     * Time: O(n), Space: O(n)
     */
    public static int[] nextGreaterElementCircular(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        
        for (int i = 0; i < 2 * n; i++) {
            int idx = i % n;
            while (!stack.isEmpty() && nums[stack.peek()] < nums[idx]) {
                result[stack.pop()] = nums[idx];
            }
            if (i < n) stack.push(idx);
        }
        return result;
    }

    /**
     * Previous Smaller Element — for each element, find previous element smaller than it
     * Example: [4,5,2,10,8] → [-1,4,-1,2,2]
     * Time: O(n), Space: O(n)
     */
    public static int[] previousSmallerElement(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                stack.pop();
            }
            if (!stack.isEmpty()) result[i] = nums[stack.peek()];
            stack.push(i);
        }
        return result;
    }

    /**
     * Largest Rectangle in Histogram
     * Example: heights = [2,1,5,6,2,3] → 10 (rectangle 5×2)
     * Time: O(n), Space: O(n)
     */
    public static int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;
        int n = heights.length;
        
        for (int i = 0; i <= n; i++) {
            int h = (i == n) ? 0 : heights[i];
            while (!stack.isEmpty() && heights[stack.peek()] > h) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }
        return maxArea;
    }

    /**
     * Daily Temperatures — how many days until warmer temperature
     * Example: [73,74,75,71,69,72,76,73] → [1,1,4,2,1,1,0,0]
     * Time: O(n), Space: O(n)
     */
    public static int[] dailyTemperatures(int[] temps) {
        int n = temps.length;
        int[] result = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temps[stack.peek()] < temps[i]) {
                int idx = stack.pop();
                result[idx] = i - idx;
            }
            stack.push(i);
        }
        return result;
    }

    // =========================================================
    // 2. MONOTONIC QUEUE (Sliding Window Maximum)
    // =========================================================

    /**
     * Sliding Window Maximum — max in each window of size k
     * Example: nums = [1,3,-1,-3,5,3,6,7], k = 3 → [3,3,5,5,6,7]
     * Time: O(n), Space: O(k)
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0 || k == 0) return new int[0];
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>(); // store indices
        
        for (int i = 0; i < n; i++) {
            // Remove elements outside window
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            // Remove smaller elements (maintain decreasing order)
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            // Add to result when window is full
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return result;
    }

    /**
     * Sliding Window Minimum — min in each window of size k
     * Time: O(n), Space: O(k)
     */
    public static int[] minSlidingWindow(int[] nums, int k) {
        if (nums.length == 0 || k == 0) return new int[0];
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();
        
        for (int i = 0; i < n; i++) {
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            while (!deque.isEmpty() && nums[deque.peekLast()] > nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return result;
    }

    // =========================================================
    // 3. MIN STACK / MAX STACK
    // =========================================================

    /**
     * MinStack — stack with O(1) getMin operation
     * LeetCode #155
     */
    public static class MinStack {
        private final Deque<Integer> stack = new ArrayDeque<>();
        private final Deque<Integer> minStack = new ArrayDeque<>();

        public void push(int val) {
            stack.push(val);
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }

        public void pop() {
            int val = stack.pop();
            if (val == minStack.peek()) {
                minStack.pop();
            }
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    /**
     * MaxStack — stack with O(1) getMax and O(log n) popMax
     * Similar to MinStack but with additional popMax operation
     */
    public static class MaxStack {
        private final Deque<int[]> stack = new ArrayDeque<>(); // [value, maxSoFar]

        public void push(int val) {
            int max = stack.isEmpty() ? val : Math.max(val, stack.peek()[1]);
            stack.push(new int[]{val, max});
        }

        public int pop() {
            return stack.pop()[0];
        }

        public int top() {
            return stack.peek()[0];
        }

        public int peekMax() {
            return stack.peek()[1];
        }

        public int popMax() {
            int max = peekMax();
            Deque<Integer> buffer = new ArrayDeque<>();
            while (top() != max) {
                buffer.push(pop());
            }
            pop();
            while (!buffer.isEmpty()) {
                push(buffer.pop());
            }
            return max;
        }
    }

    // =========================================================
    // 4. LRU CACHE (Least Recently Used)
    // =========================================================

    /**
     * LRU Cache — O(1) get and put
     * LeetCode #146
     * Uses HashMap + Doubly Linked List
     */
    public static class LRUCache {
        private final int capacity;
        private final Map<Integer, Node> map = new HashMap<>();
        private final Node head = new Node(0, 0);
        private final Node tail = new Node(0, 0);

        class Node {
            int key, value;
            Node prev, next;
            Node(int k, int v) { key = k; value = v; }
        }

        public LRUCache(int capacity) {
            this.capacity = capacity;
            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            if (!map.containsKey(key)) return -1;
            Node node = map.get(key);
            remove(node);
            addToHead(node);
            return node.value;
        }

        public void put(int key, int value) {
            if (map.containsKey(key)) {
                remove(map.get(key));
            }
            Node node = new Node(key, value);
            map.put(key, node);
            addToHead(node);
            if (map.size() > capacity) {
                Node lru = tail.prev;
                remove(lru);
                map.remove(lru.key);
            }
        }

        private void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void addToHead(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }
    }

    /**
     * LFU Cache — Least Frequently Used Cache with O(1) get and put
     * LeetCode #460
     * Uses HashMap + frequency-based doubly linked lists
     * 
     * When there's a tie in frequency, evict the least recently used item.
     * 
     * Example:
     *   LFUCache cache = new LFUCache(2);
     *   cache.put(1, 1);   // cache=[1,_], 1 has freq=1
     *   cache.put(2, 2);   // cache=[2,1], 2 has freq=1, 1 has freq=1
     *   cache.get(1);      // returns 1, cache=[1,2], 1 has freq=2
     *   cache.put(3, 3);   // evicts key 2, cache=[3,1]
     *   cache.get(2);      // returns -1 (not found)
     */
    public static class LFUCache {
        private final int capacity;
        private int minFreq;
        private final Map<Integer, Node> keyToNode = new HashMap<>();
        private final Map<Integer, DLList> freqToList = new HashMap<>();

        class Node {
            int key, value, freq;
            Node prev, next;
            Node(int k, int v) {
                key = k;
                value = v;
                freq = 1;
            }
        }

        class DLList {
            Node head = new Node(0, 0);
            Node tail = new Node(0, 0);
            int size = 0;

            DLList() {
                head.next = tail;
                tail.prev = head;
            }

            void addToHead(Node node) {
                node.next = head.next;
                node.prev = head;
                head.next.prev = node;
                head.next = node;
                size++;
            }

            void remove(Node node) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                size--;
            }

            Node removeLast() {
                if (size > 0) {
                    Node node = tail.prev;
                    remove(node);
                    return node;
                }
                return null;
            }
        }

        public LFUCache(int capacity) {
            this.capacity = capacity;
            this.minFreq = 0;
        }

        public int get(int key) {
            if (!keyToNode.containsKey(key)) return -1;
            Node node = keyToNode.get(key);
            updateFreq(node);
            return node.value;
        }

        public void put(int key, int value) {
            if (capacity == 0) return;

            if (keyToNode.containsKey(key)) {
                Node node = keyToNode.get(key);
                node.value = value;
                updateFreq(node);
            } else {
                if (keyToNode.size() >= capacity) {
                    // Evict least frequently used (and least recently used if tie)
                    DLList minFreqList = freqToList.get(minFreq);
                    Node evict = minFreqList.removeLast();
                    keyToNode.remove(evict.key);
                }
                Node newNode = new Node(key, value);
                keyToNode.put(key, newNode);
                freqToList.computeIfAbsent(1, k -> new DLList()).addToHead(newNode);
                minFreq = 1;
            }
        }

        private void updateFreq(Node node) {
            int oldFreq = node.freq;
            DLList oldList = freqToList.get(oldFreq);
            oldList.remove(node);

            // Update minFreq if we just removed the last node at minFreq
            if (oldFreq == minFreq && oldList.size == 0) {
                minFreq++;
            }

            node.freq++;
            freqToList.computeIfAbsent(node.freq, k -> new DLList()).addToHead(node);
        }
    }

    // =========================================================
    // 5. INTERVAL OPERATIONS
    // =========================================================

    /**
     * Merge Intervals — merge overlapping intervals
     * Example: [[1,3],[2,6],[8,10],[15,18]] → [[1,6],[8,10],[15,18]]
     * Time: O(n log n), Space: O(n)
     */
    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals.length <= 1) return intervals;
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> result = new ArrayList<>();
        int[] current = intervals[0];
        result.add(current);
        
        for (int[] interval : intervals) {
            if (interval[0] <= current[1]) {
                current[1] = Math.max(current[1], interval[1]);
            } else {
                current = interval;
                result.add(current);
            }
        }
        return result.toArray(new int[0][]);
    }

    /**
     * Insert Interval — insert and merge new interval
     * Example: intervals = [[1,3],[6,9]], newInterval = [2,5] → [[1,5],[6,9]]
     * Time: O(n), Space: O(n)
     */
    public static int[][] insertInterval(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0, n = intervals.length;
        
        // Add all intervals before newInterval
        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i++]);
        }
        
        // Merge overlapping intervals
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval);
        
        // Add remaining intervals
        while (i < n) {
            result.add(intervals[i++]);
        }
        
        return result.toArray(new int[0][]);
    }

    /**
     * Non-overlapping Intervals — min removals to make non-overlapping
     * Example: [[1,2],[2,3],[3,4],[1,3]] → 1 (remove [1,3])
     * Time: O(n log n), Space: O(1)
     */
    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0) return 0;
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
        int count = 0, end = intervals[0][1];
        
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < end) {
                count++;
            } else {
                end = intervals[i][1];
            }
        }
        return count;
    }

    /**
     * Meeting Rooms II — minimum meeting rooms needed
     * Example: [[0,30],[5,10],[15,20]] → 2
     * Time: O(n log n), Space: O(n)
     */
    public static int minMeetingRooms(int[][] intervals) {
        if (intervals.length == 0) return 0;
        int[] starts = new int[intervals.length];
        int[] ends = new int[intervals.length];
        
        for (int i = 0; i < intervals.length; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }
        
        Arrays.sort(starts);
        Arrays.sort(ends);
        
        int rooms = 0, endIdx = 0;
        for (int start : starts) {
            if (start < ends[endIdx]) {
                rooms++;
            } else {
                endIdx++;
            }
        }
        return rooms;
    }

    // =========================================================
    // 6. MISCELLANEOUS PATTERNS
    // =========================================================

    /**
     * Trapping Rain Water — calculate trapped water
     * Example: [0,1,0,2,1,0,1,3,2,1,2,1] → 6
     * Time: O(n), Space: O(1)
     */
    public static int trap(int[] height) {
        if (height.length == 0) return 0;
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0, water = 0;
        
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    water += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    water += rightMax - height[right];
                }
                right--;
            }
        }
        return water;
    }

    /**
     * Product of Array Except Self — without division
     * Example: [1,2,3,4] → [24,12,8,6]
     * Time: O(n), Space: O(1) excluding output
     */
    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        
        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }
        
        int right = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= right;
            right *= nums[i];
        }
        return result;
    }
}
