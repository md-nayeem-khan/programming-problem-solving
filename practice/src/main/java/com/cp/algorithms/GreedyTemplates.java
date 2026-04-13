package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Greedy Algorithm Templates for coding interviews.
 * 
 * Greedy Strategy: Make locally optimal choice at each step hoping to find global optimum.
 * Key: Prove the greedy choice property and optimal substructure.
 * 
 * Coverage:
 * - Jump Game I & II
 * - Gas Station
 * - Candy Distribution
 * - Remove K Digits
 * - Activity Selection
 * - Fractional Knapsack
 * - Meeting Rooms variants
 * - Partition Labels
 * - Task Assignment
 * 
 * Critical for: All FAANG companies
 * Frequency: ~15% of medium interview problems
 */
public class GreedyTemplates {

    // ============================================================================
    // JUMP GAME PATTERNS
    // ============================================================================
    
    /**
     * Jump Game I - can reach last index?
     * Pattern: Track furthest reachable position.
     * 
     * Companies: Amazon, Meta, Google
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static boolean canJump(int[] nums) {
        int maxReach = 0;
        
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false; // Can't reach position i
            maxReach = Math.max(maxReach, i + nums[i]);
            if (maxReach >= nums.length - 1) return true;
        }
        
        return true;
    }
    
    /**
     * Jump Game II - minimum jumps to reach end.
     * Pattern: BFS-like greedy, track current range and next range.
     * 
     * Companies: Meta, Amazon, Microsoft
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int jump(int[] nums) {
        if (nums.length <= 1) return 0;
        
        int jumps = 0;
        int currentEnd = 0;  // End of current jump range
        int farthest = 0;     // Farthest we can reach
        
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            
            // Reached end of current jump range, must make another jump
            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;
                
                if (currentEnd >= nums.length - 1) break;
            }
        }
        
        return jumps;
    }
    
    /**
     * Jump Game III - can reach any index with value 0?
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static boolean canReachZero(int[] arr, int start) {
        if (start < 0 || start >= arr.length) return false;
        if (arr[start] == 0) return true;
        if (arr[start] < 0) return false; // Already visited
        
        int jump = arr[start];
        arr[start] = -arr[start]; // Mark as visited
        
        return canReachZero(arr, start + jump) || canReachZero(arr, start - jump);
    }
    
    // ============================================================================
    // GAS STATION PATTERN
    // ============================================================================
    
    /**
     * Gas Station - find starting station to complete circular route.
     * 
     * Greedy insight: If total gas >= total cost, solution exists.
     * Start from index where we can accumulate positive gas.
     * 
     * Companies: Amazon, Meta
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int totalGas = 0;
        int totalCost = 0;
        int currentGas = 0;
        int startStation = 0;
        
        for (int i = 0; i < gas.length; i++) {
            totalGas += gas[i];
            totalCost += cost[i];
            currentGas += gas[i] - cost[i];
            
            // If can't reach next station, try starting from next station
            if (currentGas < 0) {
                startStation = i + 1;
                currentGas = 0;
            }
        }
        
        return totalGas >= totalCost ? startStation : -1;
    }
    
    // ============================================================================
    // CANDY DISTRIBUTION
    // ============================================================================
    
    /**
     * Candy - distribute candies to children with ratings.
     * Rules: Each child gets at least 1, higher rating gets more than neighbors.
     * 
     * Pattern: Two-pass greedy (left-to-right, then right-to-left).
     * 
     * Companies: Amazon, Google
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static int candy(int[] ratings) {
        int n = ratings.length;
        int[] candies = new int[n];
        Arrays.fill(candies, 1);
        
        // Left to right: if ratings[i] > ratings[i-1], give more candy
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        
        // Right to left: if ratings[i] > ratings[i+1], ensure more candy
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }
        
        int total = 0;
        for (int c : candies) total += c;
        return total;
    }
    
    // ============================================================================
    // REMOVE K DIGITS
    // ============================================================================
    
    /**
     * Remove K Digits - remove k digits to make smallest number.
     * 
     * Pattern: Monotonic stack - remove digits that are greater than next digit.
     * 
     * Companies: Meta, Google
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static String removeKdigits(String num, int k) {
        Deque<Character> stack = new ArrayDeque<>();
        
        for (char digit : num.toCharArray()) {
            // Remove digits from stack if they're larger than current digit
            while (!stack.isEmpty() && k > 0 && stack.peek() > digit) {
                stack.pop();
                k--;
            }
            stack.push(digit);
        }
        
        // Remove remaining k digits from end
        while (k > 0) {
            stack.pop();
            k--;
        }
        
        // Build result (remove leading zeros)
        StringBuilder sb = new StringBuilder();
        boolean leadingZero = true;
        while (!stack.isEmpty()) {
            char digit = stack.removeLast();
            if (leadingZero && digit == '0') continue;
            leadingZero = false;
            sb.append(digit);
        }
        
        return sb.length() == 0 ? "0" : sb.toString();
    }
    
    // ============================================================================
    // ACTIVITY SELECTION (INTERVAL SCHEDULING)
    // ============================================================================
    
    /**
     * Activity Selection - maximum non-overlapping intervals.
     * 
     * Greedy: Sort by end time, select earliest ending interval.
     * 
     * Companies: All FAANG
     */
    @Complexity(time = "O(n log n)", space = "O(1)")
    public static int maxNonOverlappingIntervals(int[][] intervals) {
        if (intervals.length == 0) return 0;
        
        // Sort by end time
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        
        int count = 1;
        int lastEnd = intervals[0][1];
        
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= lastEnd) {
                count++;
                lastEnd = intervals[i][1];
            }
        }
        
        return count;
    }
    
    /**
     * Minimum intervals to remove to make non-overlapping.
     */
    @Complexity(time = "O(n log n)", space = "O(1)")
    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0) return 0;
        
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        
        int count = 0;
        int lastEnd = intervals[0][1];
        
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < lastEnd) {
                count++; // Overlaps, need to remove
            } else {
                lastEnd = intervals[i][1];
            }
        }
        
        return count;
    }
    
    // ============================================================================
    // FRACTIONAL KNAPSACK
    // ============================================================================
    
    /**
     * Fractional Knapsack - can take fractions of items.
     * 
     * Greedy: Sort by value/weight ratio, take highest ratio first.
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static double fractionalKnapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        Item[] items = new Item[n];
        
        for (int i = 0; i < n; i++) {
            items[i] = new Item(weights[i], values[i]);
        }
        
        // Sort by value/weight ratio descending
        Arrays.sort(items, (a, b) -> Double.compare(b.ratio, a.ratio));
        
        double totalValue = 0;
        int remainingCapacity = capacity;
        
        for (Item item : items) {
            if (remainingCapacity >= item.weight) {
                totalValue += item.value;
                remainingCapacity -= item.weight;
            } else {
                // Take fraction
                totalValue += item.value * ((double) remainingCapacity / item.weight);
                break;
            }
        }
        
        return totalValue;
    }
    
    private static class Item {
        int weight, value;
        double ratio;
        
        Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
            this.ratio = (double) value / weight;
        }
    }
    
    // ============================================================================
    // PARTITION LABELS
    // ============================================================================
    
    /**
     * Partition Labels - partition string so each letter appears in at most one part.
     * 
     * Greedy: Track last occurrence of each character, extend partition.
     * 
     * Companies: Amazon, Meta
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static List<Integer> partitionLabels(String s) {
        int[] lastOccurrence = new int[26];
        for (int i = 0; i < s.length(); i++) {
            lastOccurrence[s.charAt(i) - 'a'] = i;
        }
        
        List<Integer> result = new ArrayList<>();
        int start = 0, end = 0;
        
        for (int i = 0; i < s.length(); i++) {
            end = Math.max(end, lastOccurrence[s.charAt(i) - 'a']);
            
            if (i == end) {
                result.add(end - start + 1);
                start = i + 1;
            }
        }
        
        return result;
    }
    
    // ============================================================================
    // TASK ASSIGNMENT
    // ============================================================================
    
    /**
     * Minimum Cost to Hire K Workers.
     * Complex greedy with heap.
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static double minCostToHireWorkers(int[] quality, int[] wage, int k) {
        int n = quality.length;
        Worker[] workers = new Worker[n];
        
        for (int i = 0; i < n; i++) {
            workers[i] = new Worker(quality[i], wage[i]);
        }
        
        // Sort by wage/quality ratio
        Arrays.sort(workers, (a, b) -> Double.compare(a.ratio, b.ratio));
        
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        int qualitySum = 0;
        double minCost = Double.MAX_VALUE;
        
        for (Worker worker : workers) {
            maxHeap.offer(worker.quality);
            qualitySum += worker.quality;
            
            if (maxHeap.size() > k) {
                qualitySum -= maxHeap.poll();
            }
            
            if (maxHeap.size() == k) {
                minCost = Math.min(minCost, qualitySum * worker.ratio);
            }
        }
        
        return minCost;
    }
    
    private static class Worker {
        int quality, wage;
        double ratio;
        
        Worker(int quality, int wage) {
            this.quality = quality;
            this.wage = wage;
            this.ratio = (double) wage / quality;
        }
    }
    
    // ============================================================================
    // BOATS TO SAVE PEOPLE
    // ============================================================================
    
    /**
     * Boats to Save People - minimum boats needed (limit per boat).
     * 
     * Greedy: Two pointers - pair heaviest with lightest if possible.
     */
    @Complexity(time = "O(n log n)", space = "O(1)")
    public static int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int left = 0, right = people.length - 1;
        int boats = 0;
        
        while (left <= right) {
            if (people[left] + people[right] <= limit) {
                left++; // Pair lightest with heaviest
            }
            right--; // Heaviest always goes on boat
            boats++;
        }
        
        return boats;
    }
    
    // ============================================================================
    // MINIMUM ARROWS TO BURST BALLOONS
    // ============================================================================
    
    /**
     * Minimum Arrows to Burst Balloons.
     * Pattern: Similar to interval merging.
     * 
     * Companies: Amazon, Microsoft
     */
    @Complexity(time = "O(n log n)", space = "O(1)")
    public static int findMinArrowShots(int[][] points) {
        if (points.length == 0) return 0;
        
        // Sort by end position
        Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));
        
        int arrows = 1;
        int lastArrow = points[0][1];
        
        for (int i = 1; i < points.length; i++) {
            if (points[i][0] > lastArrow) {
                // Need new arrow
                arrows++;
                lastArrow = points[i][1];
            }
        }
        
        return arrows;
    }
    
    // ============================================================================
    // MINIMIZE MAXIMUM PAIR SUM
    // ============================================================================
    
    /**
     * Minimize Maximum Pair Sum in Array.
     * 
     * Greedy: Sort and pair smallest with largest.
     */
    @Complexity(time = "O(n log n)", space = "O(1)")
    public static int minPairSum(int[] nums) {
        Arrays.sort(nums);
        int maxPairSum = 0;
        int left = 0, right = nums.length - 1;
        
        while (left < right) {
            maxPairSum = Math.max(maxPairSum, nums[left] + nums[right]);
            left++;
            right--;
        }
        
        return maxPairSum;
    }
    
    // ============================================================================
    // MAXIMUM UNITS ON TRUCK
    // ============================================================================
    
    /**
     * Maximum Units on a Truck.
     * Pattern: Greedy by units per box.
     */
    @Complexity(time = "O(n log n)", space = "O(1)")
    public static int maximumUnits(int[][] boxTypes, int truckSize) {
        // Sort by units per box descending
        Arrays.sort(boxTypes, (a, b) -> b[1] - a[1]);
        
        int totalUnits = 0;
        int remainingSize = truckSize;
        
        for (int[] box : boxTypes) {
            int boxes = box[0];
            int units = box[1];
            
            int take = Math.min(boxes, remainingSize);
            totalUnits += take * units;
            remainingSize -= take;
            
            if (remainingSize == 0) break;
        }
        
        return totalUnits;
    }
    
    // ============================================================================
    // WIGGLE SUBSEQUENCE
    // ============================================================================
    
    /**
     * Longest Wiggle Subsequence.
     * Pattern: Greedy - only count direction changes.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int wiggleMaxLength(int[] nums) {
        if (nums.length < 2) return nums.length;
        
        int up = 1, down = 1;
        
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) {
                up = down + 1;
            } else if (nums[i] < nums[i - 1]) {
                down = up + 1;
            }
        }
        
        return Math.max(up, down);
    }
    
    // ============================================================================
    // QUEUE RECONSTRUCTION BY HEIGHT
    // ============================================================================
    
    /**
     * Queue Reconstruction by Height.
     * Pattern: Sort + insert by position.
     * 
     * Companies: Google, Meta
     */
    @Complexity(time = "O(n²)", space = "O(n)")
    public static int[][] reconstructQueue(int[][] people) {
        // Sort by height descending, then by k ascending
        Arrays.sort(people, (a, b) -> {
            if (a[0] != b[0]) return b[0] - a[0];
            return a[1] - b[1];
        });
        
        List<int[]> result = new ArrayList<>();
        for (int[] person : people) {
            result.add(person[1], person);
        }
        
        return result.toArray(new int[people.length][2]);
    }
    
    // ============================================================================
    // HAND OF STRAIGHTS
    // ============================================================================
    
    /**
     * Hand of Straights - can divide cards into groups of consecutive cards.
     * Pattern: Greedy with TreeMap.
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static boolean isNStraightHand(int[] hand, int groupSize) {
        if (hand.length % groupSize != 0) return false;
        
        TreeMap<Integer, Integer> count = new TreeMap<>();
        for (int card : hand) {
            count.put(card, count.getOrDefault(card, 0) + 1);
        }
        
        while (!count.isEmpty()) {
            int start = count.firstKey();
            
            for (int i = 0; i < groupSize; i++) {
                int card = start + i;
                if (!count.containsKey(card)) return false;
                
                count.put(card, count.get(card) - 1);
                if (count.get(card) == 0) {
                    count.remove(card);
                }
            }
        }
        
        return true;
    }
}
