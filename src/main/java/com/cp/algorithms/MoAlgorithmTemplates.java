package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Mo's Algorithm - Offline Range Query Optimization
 * MEDIUM PRIORITY for Competitive Programming
 * 
 * Essential for:
 * - Process multiple range queries efficiently
 * - Distinct elements in range
 * - Range mode (most frequent element)
 * - Range GCD, sum of squares, etc.
 * - Any query that can be extended/shrunk incrementally
 * 
 * Interview Questions:
 * - Count distinct elements in subarray
 * - Range XOR queries
 * - K-th smallest in range (offline)
 * - Range frequency queries
 * 
 * Key Concepts:
 * - Offline: All queries known in advance
 * - Block decomposition: Divide array into √n blocks
 * - Query reordering: Sort queries by (L/block, R)
 * - Incremental updates: add/remove one element at a time
 * - Time: O((N + Q)√N) instead of O(NQ)
 * 
 * Companies: Competitive Programming (5/5), Google (3/5)
 */
public class MoAlgorithmTemplates {
    
    // ========== MO'S ALGORITHM BASE CLASS ==========
    
    /**
     * Mo's Algorithm for range queries
     * Reorders queries to minimize pointer movements
     */
    @Complexity(time = "O((N + Q)√N)", space = "O(N + Q)")
    static class MoAlgorithm {
        static class Query implements Comparable<Query> {
            int left, right, index;
            int block;
            
            public Query(int left, int right, int index, int blockSize) {
                this.left = left;
                this.right = right;
                this.index = index;
                this.block = left / blockSize;
            }
            
            @Override
            public int compareTo(Query other) {
                if (this.block != other.block) {
                    return this.block - other.block;
                }
                // Alternate direction for odd/even blocks (optimization)
                if (this.block % 2 == 0) {
                    return this.right - other.right;
                } else {
                    return other.right - this.right;
                }
            }
        }
        
        protected int[] arr;
        protected int blockSize;
        protected int currentL, currentR;
        
        public MoAlgorithm(int[] arr) {
            this.arr = arr;
            this.blockSize = (int) Math.sqrt(arr.length) + 1;
            this.currentL = 0;
            this.currentR = -1;
        }
        
        /**
         * Process all queries and return answers
         */
        public int[] processQueries(int[][] queries) {
            int q = queries.length;
            Query[] sortedQueries = new Query[q];
            
            for (int i = 0; i < q; i++) {
                sortedQueries[i] = new Query(queries[i][0], queries[i][1], i, blockSize);
            }
            
            Arrays.sort(sortedQueries);
            
            int[] answers = new int[q];
            
            for (Query query : sortedQueries) {
                // Expand/shrink range to match query
                while (currentR < query.right) {
                    currentR++;
                    add(currentR);
                }
                while (currentR > query.right) {
                    remove(currentR);
                    currentR--;
                }
                while (currentL < query.left) {
                    remove(currentL);
                    currentL++;
                }
                while (currentL > query.left) {
                    currentL--;
                    add(currentL);
                }
                
                answers[query.index] = getAnswer();
            }
            
            return answers;
        }
        
        // Override these methods for specific query type
        protected void add(int index) {}
        protected void remove(int index) {}
        protected int getAnswer() { return 0; }
    }
    
    // ========== DISTINCT ELEMENTS IN RANGE ==========
    
    /**
     * Count distinct elements in range queries
     */
    @Complexity(time = "O((N + Q)√N)", space = "O(N)")
    static class DistinctInRange extends MoAlgorithm {
        private int[] frequency;
        private int distinctCount;
        private int maxValue;
        
        public DistinctInRange(int[] arr) {
            super(arr);
            this.maxValue = Arrays.stream(arr).max().orElse(0) + 1;
            this.frequency = new int[maxValue];
            this.distinctCount = 0;
        }
        
        @Override
        protected void add(int index) {
            int value = arr[index];
            if (frequency[value] == 0) {
                distinctCount++;
            }
            frequency[value]++;
        }
        
        @Override
        protected void remove(int index) {
            int value = arr[index];
            frequency[value]--;
            if (frequency[value] == 0) {
                distinctCount--;
            }
        }
        
        @Override
        protected int getAnswer() {
            return distinctCount;
        }
    }
    
    // ========== RANGE MODE (MOST FREQUENT ELEMENT) ==========
    
    /**
     * Find mode (most frequent element) in range
     */
    @Complexity(time = "O((N + Q)√N)", space = "O(N)")
    static class RangeMode extends MoAlgorithm {
        private int[] frequency;
        private int[] freqCount;  // freqCount[f] = how many numbers appear f times
        private int maxFrequency;
        private int maxValue;
        
        public RangeMode(int[] arr) {
            super(arr);
            this.maxValue = Arrays.stream(arr).max().orElse(0) + 1;
            this.frequency = new int[maxValue];
            this.freqCount = new int[arr.length + 1];
            this.maxFrequency = 0;
        }
        
        @Override
        protected void add(int index) {
            int value = arr[index];
            int oldFreq = frequency[value];
            int newFreq = oldFreq + 1;
            
            if (oldFreq > 0) {
                freqCount[oldFreq]--;
            }
            
            frequency[value] = newFreq;
            freqCount[newFreq]++;
            maxFrequency = Math.max(maxFrequency, newFreq);
        }
        
        @Override
        protected void remove(int index) {
            int value = arr[index];
            int oldFreq = frequency[value];
            int newFreq = oldFreq - 1;
            
            freqCount[oldFreq]--;
            if (freqCount[oldFreq] == 0 && oldFreq == maxFrequency) {
                maxFrequency--;
            }
            
            frequency[value] = newFreq;
            if (newFreq > 0) {
                freqCount[newFreq]++;
            }
        }
        
        @Override
        protected int getAnswer() {
            return maxFrequency;
        }
    }
    
    // ========== RANGE XOR ==========
    
    /**
     * XOR of all elements in range
     * Note: This specific problem has O(1) solution with prefix XOR,
     * but demonstrates Mo's algorithm pattern
     */
    @Complexity(time = "O((N + Q)√N)", space = "O(1)")
    static class RangeXOR extends MoAlgorithm {
        private int currentXor;
        
        public RangeXOR(int[] arr) {
            super(arr);
            this.currentXor = 0;
        }
        
        @Override
        protected void add(int index) {
            currentXor ^= arr[index];
        }
        
        @Override
        protected void remove(int index) {
            currentXor ^= arr[index];  // XOR is self-inverse
        }
        
        @Override
        protected int getAnswer() {
            return currentXor;
        }
    }
    
    // ========== RANGE SUM OF SQUARES ==========
    
    /**
     * Sum of squares of elements in range
     */
    @Complexity(time = "O((N + Q)√N)", space = "O(1)")
    static class RangeSumOfSquares extends MoAlgorithm {
        private long sumOfSquares;
        
        public RangeSumOfSquares(int[] arr) {
            super(arr);
            this.sumOfSquares = 0;
        }
        
        @Override
        protected void add(int index) {
            sumOfSquares += (long) arr[index] * arr[index];
        }
        
        @Override
        protected void remove(int index) {
            sumOfSquares -= (long) arr[index] * arr[index];
        }
        
        @Override
        protected int getAnswer() {
            return (int) sumOfSquares;
        }
    }
    
    // ========== RANGE GCD ==========
    
    /**
     * GCD of all elements in range
     */
    @Complexity(time = "O((N + Q)√N log V)", space = "O(N)")
    static class RangeGCD extends MoAlgorithm {
        private Map<Integer, Integer> frequency;
        
        public RangeGCD(int[] arr) {
            super(arr);
            this.frequency = new HashMap<>();
        }
        
        @Override
        protected void add(int index) {
            int value = arr[index];
            frequency.put(value, frequency.getOrDefault(value, 0) + 1);
        }
        
        @Override
        protected void remove(int index) {
            int value = arr[index];
            int count = frequency.get(value);
            if (count == 1) {
                frequency.remove(value);
            } else {
                frequency.put(value, count - 1);
            }
        }
        
        @Override
        protected int getAnswer() {
            if (frequency.isEmpty()) return 0;
            
            int result = 0;
            for (int value : frequency.keySet()) {
                result = gcd(result, value);
            }
            return result;
        }
        
        private int gcd(int a, int b) {
            while (b != 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Mo's Algorithm Examples ===\n");
        
        // Example 1: Distinct elements in range
        System.out.println("1. Distinct Elements in Range:");
        int[] arr1 = {1, 2, 1, 3, 2, 4, 3, 5};
        int[][] queries1 = {
            {0, 3},  // [1,2,1,3] -> 3 distinct
            {2, 5},  // [1,3,2,4] -> 4 distinct
            {0, 7},  // [1,2,1,3,2,4,3,5] -> 5 distinct
            {1, 4}   // [2,1,3,2] -> 3 distinct
        };
        
        DistinctInRange dir = new DistinctInRange(arr1);
        int[] answers1 = dir.processQueries(queries1);
        
        System.out.println("   Array: " + Arrays.toString(arr1));
        for (int i = 0; i < queries1.length; i++) {
            System.out.println("   Query [" + queries1[i][0] + ", " + queries1[i][1] + 
                             "] -> " + answers1[i] + " distinct elements");
        }
        
        // Example 2: Range mode (most frequent)
        System.out.println("\n2. Range Mode (Most Frequent Element):");
        int[] arr2 = {1, 2, 2, 3, 3, 3, 4, 4, 4, 4};
        int[][] queries2 = {
            {0, 3},  // [1,2,2,3] -> 2 appears twice
            {3, 6},  // [3,3,3,4] -> 3 appears thrice
            {6, 9}   // [4,4,4,4] -> 4 appears four times
        };
        
        RangeMode rm = new RangeMode(arr2);
        int[] answers2 = rm.processQueries(queries2);
        
        System.out.println("   Array: " + Arrays.toString(arr2));
        for (int i = 0; i < queries2.length; i++) {
            System.out.println("   Query [" + queries2[i][0] + ", " + queries2[i][1] + 
                             "] -> max frequency = " + answers2[i]);
        }
        
        // Example 3: Range XOR
        System.out.println("\n3. Range XOR:");
        int[] arr3 = {1, 2, 3, 4, 5};
        int[][] queries3 = {
            {0, 2},  // 1 XOR 2 XOR 3 = 0
            {1, 4},  // 2 XOR 3 XOR 4 XOR 5 = 4
            {0, 4}   // 1 XOR 2 XOR 3 XOR 4 XOR 5 = 1
        };
        
        RangeXOR rx = new RangeXOR(arr3);
        int[] answers3 = rx.processQueries(queries3);
        
        System.out.println("   Array: " + Arrays.toString(arr3));
        for (int i = 0; i < queries3.length; i++) {
            System.out.println("   Query [" + queries3[i][0] + ", " + queries3[i][1] + 
                             "] -> XOR = " + answers3[i]);
        }
        
        // Example 4: Range sum of squares
        System.out.println("\n4. Range Sum of Squares:");
        int[] arr4 = {1, 2, 3, 4, 5};
        int[][] queries4 = {
            {0, 2},  // 1² + 2² + 3² = 14
            {1, 4},  // 2² + 3² + 4² + 5² = 54
            {0, 4}   // 1² + 2² + 3² + 4² + 5² = 55
        };
        
        RangeSumOfSquares rss = new RangeSumOfSquares(arr4);
        int[] answers4 = rss.processQueries(queries4);
        
        System.out.println("   Array: " + Arrays.toString(arr4));
        for (int i = 0; i < queries4.length; i++) {
            System.out.println("   Query [" + queries4[i][0] + ", " + queries4[i][1] + 
                             "] -> sum of squares = " + answers4[i]);
        }
        
        // Example 5: Performance analysis
        System.out.println("\n5. Performance Comparison:");
        int n = 100000;
        int q = 100000;
        System.out.println("   Array size: " + n);
        System.out.println("   Number of queries: " + q);
        System.out.println("   ");
        System.out.println("   Naive approach (process each query independently):");
        System.out.println("     Time: O(N × Q) = " + ((long)n * q) + " operations");
        System.out.println("   ");
        System.out.println("   Mo's Algorithm:");
        int blockSize = (int) Math.sqrt(n);
        long operations = (long)(n + q) * blockSize;
        System.out.println("     Block size: √N = " + blockSize);
        System.out.println("     Time: O((N + Q)√N) = " + operations + " operations");
        System.out.println("     Speedup: " + ((n * q) / operations) + "×");
        
        // Example 6: When to use Mo's Algorithm
        System.out.println("\n6. When to Use Mo's Algorithm:");
        System.out.println("   ✓ All queries known in advance (offline)");
        System.out.println("   ✓ Can efficiently add/remove elements");
        System.out.println("   ✓ Query order doesn't matter");
        System.out.println("   ✓ No updates to array");
        System.out.println("   ");
        System.out.println("   ✗ Online queries (answers needed immediately)");
        System.out.println("   ✗ Array updates between queries");
        System.out.println("   ✗ Query order is important");
        
        System.out.println("\n=== Key Points ===");
        System.out.println("Mo's Algorithm: Reorder queries for efficiency");
        System.out.println("Block size: √N for optimal complexity");
        System.out.println("Sorting: By (L/block, R) to minimize pointer movements");
        System.out.println("Applications: Distinct count, mode, any incremental query");
        System.out.println("Complexity: O((N + Q)√N) vs O(NQ) naive");
    }
}
