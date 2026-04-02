package com.cp.algorithms;

/**
 * Fenwick Tree (Binary Indexed Tree - BIT) Templates
 * 
 * WHEN TO USE:
 * - Need prefix sum queries in O(log n)
 * - Need point updates in O(log n)
 * - Space-efficient alternative to segment tree
 * - Inversion counting, order statistics, coordinate compression
 * 
 * KEY INSIGHT:
 * Each index stores cumulative sum for range of size (last set bit)
 * tree[i] covers range [i - (i & -i) + 1, i]
 * Example: tree[12] covers [9, 12] (last 2 bits: 4 elements)
 * 
 * COMPLEXITY:
 * - Build: O(n) or O(n log n)
 * - Query (prefix sum): O(log n)
 * - Update (point): O(log n)
 * - Range query: O(log n) = query(r) - query(l-1)
 * - Space: O(n)
 * 
 * COMPARISON:
 * - Fenwick Tree: O(n) build, O(log n) query/update, prefix ops, simple code
 * - Segment Tree: O(n) build, O(log n) query/update, any associative op, more flexible
 * - Sparse Table: O(n log n) build, O(1) query, no updates, idempotent ops only
 * 
 * COMMON INTERVIEW PROBLEMS:
 * - LeetCode 307: Range Sum Query - Mutable
 * - LeetCode 315: Count of Smaller Numbers After Self
 * - LeetCode 493: Reverse Pairs
 * - LeetCode 327: Count of Range Sum
 * - LeetCode 1649: Create Sorted Array through Instructions
 * 
 * 
 */
public class FenwickTreeTemplates {

    /**
     * 1. STANDARD FENWICK TREE - Point Update, Range Query
     * 
     * Operations:
     * - update(i, delta): Add delta to arr[i]
     * - query(i): Get sum of arr[0...i]
     * - rangeQuery(l, r): Get sum of arr[l...r]
     * 
     * Time: O(log n) per operation
     * Space: O(n)
     * 
     * Note: 1-indexed internally for easier bit manipulation
     */
    static class FenwickTree {
        private int[] tree;
        private int n;
        
        // Build from array in O(n log n)
        public FenwickTree(int[] arr) {
            n = arr.length;
            tree = new int[n + 1];
            for (int i = 0; i < n; i++) {
                update(i, arr[i]);
            }
        }
        
        // Build empty tree
        public FenwickTree(int size) {
            n = size;
            tree = new int[n + 1];
        }
        
        // Optimized O(n) build from array
        public static FenwickTree buildOptimized(int[] arr) {
            int n = arr.length;
            FenwickTree ft = new FenwickTree(n);
            System.arraycopy(arr, 0, ft.tree, 1, n);
            
            // Build tree bottom-up in O(n)
            for (int i = 1; i <= n; i++) {
                int parent = i + (i & -i);
                if (parent <= n) {
                    ft.tree[parent] += ft.tree[i];
                }
            }
            return ft;
        }
        
        // Add delta to arr[index]
        public void update(int index, int delta) {
            if (index < 0 || index >= n) {
                throw new IndexOutOfBoundsException("Index " + index + " is out of bounds [0, " + (n-1) + "]");
            }
            index++; // Convert to 1-indexed
            while (index <= n) {
                tree[index] += delta;
                index += index & -index;  // Add last set bit
            }
        }
        
        // Get prefix sum arr[0...index]
        public int query(int index) {
            if (index < 0 || index >= n) {
                throw new IndexOutOfBoundsException("Index " + index + " is out of bounds [0, " + (n-1) + "]");
            }
            index++; // Convert to 1-indexed
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= index & -index;  // Remove last set bit
            }
            return sum;
        }
        
        // Get range sum arr[left...right]
        public int rangeQuery(int left, int right) {
            if (left > right) return 0;
            return query(right) - (left > 0 ? query(left - 1) : 0);
        }
        
        // Set arr[index] = value (not add)
        public void set(int index, int value) {
            int current = rangeQuery(index, index);
            update(index, value - current);
        }
    }

    /**
     * 2. FENWICK TREE WITH RANGE UPDATE AND POINT QUERY
     * 
     * Operations:
     * - rangeUpdate(l, r, delta): Add delta to all arr[l...r]
     * - pointQuery(i): Get value of arr[i]
     * 
     * Technique: Use difference array
     * diff[i] = arr[i] - arr[i-1]
     * arr[i] = sum of diff[0...i]
     * 
     * Time: O(log n) per operation
     * Space: O(n)
     */
    static class FenwickTreeRangeUpdate {
        private FenwickTree diff;
        private int n;
        
        public FenwickTreeRangeUpdate(int size) {
            n = size;
            diff = new FenwickTree(size);
        }
        
        public FenwickTreeRangeUpdate(int[] arr) {
            n = arr.length;
            int[] diffArray = new int[n];
            diffArray[0] = arr[0];
            for (int i = 1; i < n; i++) {
                diffArray[i] = arr[i] - arr[i - 1];
            }
            diff = FenwickTree.buildOptimized(diffArray);
        }
        
        // Add delta to range [left, right]
        public void rangeUpdate(int left, int right, int delta) {
            diff.update(left, delta);
            if (right + 1 < n) {
                diff.update(right + 1, -delta);
            }
        }
        
        // Get value at index
        public int pointQuery(int index) {
            return diff.query(index);
        }
    }

    /**
     * 3. FENWICK TREE WITH RANGE UPDATE AND RANGE QUERY
     * 
     * Operations:
     * - rangeUpdate(l, r, delta): Add delta to all arr[l...r]
     * - rangeQuery(l, r): Get sum of arr[l...r]
     * 
     * Technique: Simple approach using difference array concept
     * We maintain the original array implicitly and compute sums on demand
     * 
     * Time: O(log n) per operation
     * Space: O(n)
     */
    static class FenwickTreeRangeUpdateRangeQuery {
        private FenwickTree diff;
        private int n;
        
        public FenwickTreeRangeUpdateRangeQuery(int size) {
            n = size;
            diff = new FenwickTree(size);
        }
        
        // Add delta to range [left, right]
        public void rangeUpdate(int left, int right, int delta) {
            if (left < 0 || right >= n || left > right) return;
            
            diff.update(left, delta);
            if (right + 1 < n) {
                diff.update(right + 1, -delta);
            }
        }
        
        // Get value at single position (for building range sum)
        private int getValue(int index) {
            return diff.query(index);
        }
        
        // Get sum of range [left, right] by computing individual values
        public int rangeQuery(int left, int right) {
            if (left < 0 || right >= n || left > right) return 0;
            
            int sum = 0;
            for (int i = left; i <= right; i++) {
                sum += getValue(i);
            }
            return sum;
        }
    }

    /**
     * 4. 2D FENWICK TREE - 2D Range Sum Queries
     * 
     * Problem: 2D matrix with point updates and rectangle sum queries
     * Operations:
     * - update(row, col, delta): Add delta to matrix[row][col]
     * - query(row, col): Get sum of rectangle from (0,0) to (row,col)
     * - rangeQuery(r1, c1, r2, c2): Get sum of rectangle
     * 
     * Time: O(log n * log m) per operation
     * Space: O(n * m)
     */
    static class FenwickTree2D {
        private int[][] tree;
        private int rows, cols;
        
        public FenwickTree2D(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            tree = new int[rows + 1][cols + 1];
        }
        
        public FenwickTree2D(int[][] matrix) {
            rows = matrix.length;
            cols = matrix[0].length;
            tree = new int[rows + 1][cols + 1];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    update(i, j, matrix[i][j]);
                }
            }
        }
        
        // Add delta to matrix[row][col]
        public void update(int row, int col, int delta) {
            for (int i = row + 1; i <= rows; i += i & -i) {
                for (int j = col + 1; j <= cols; j += j & -j) {
                    tree[i][j] += delta;
                }
            }
        }
        
        // Get sum from (0,0) to (row,col)
        public int query(int row, int col) {
            int sum = 0;
            for (int i = row + 1; i > 0; i -= i & -i) {
                for (int j = col + 1; j > 0; j -= j & -j) {
                    sum += tree[i][j];
                }
            }
            return sum;
        }
        
        // Get sum of rectangle from (r1,c1) to (r2,c2)
        public int rangeQuery(int r1, int c1, int r2, int c2) {
            return query(r2, c2) 
                 - (c1 > 0 ? query(r2, c1 - 1) : 0)
                 - (r1 > 0 ? query(r1 - 1, c2) : 0)
                 + (r1 > 0 && c1 > 0 ? query(r1 - 1, c1 - 1) : 0);
        }
    }

    /**
     * 5. INVERSION COUNT using Fenwick Tree
     * 
     * Problem: Count pairs (i, j) where i < j and arr[i] > arr[j]
     * 
     * Approach:
     * 1. Coordinate compression (if needed)
     * 2. Process elements left to right
     * 3. For each element, count how many larger elements seen before
     * 
     * Time: O(n log n)
     * Space: O(n)
     * 
     * LeetCode: 315, 493
     */
    static class InversionCounter {
        public static int countInversions(int[] arr) {
            int n = arr.length;
            if (n <= 1) return 0;
            
            // Coordinate compression with proper handling of duplicates
            int[] sorted = arr.clone();
            java.util.Arrays.sort(sorted);
            java.util.Map<Integer, Integer> compress = new java.util.HashMap<>();
            int rank = 0;
            for (int num : sorted) {
                if (!compress.containsKey(num)) {
                    compress.put(num, rank++);
                }
            }
            
            // Count inversions using Fenwick tree
            FenwickTree ft = new FenwickTree(rank);
            int inversions = 0;
            
            // Process elements from left to right
            for (int i = 0; i < n; i++) {
                int compressedVal = compress.get(arr[i]);
                // Count how many elements > arr[i] have been seen so far
                if (compressedVal + 1 < rank) {
                    inversions += ft.rangeQuery(compressedVal + 1, rank - 1);
                }
                ft.update(compressedVal, 1);
            }
            
            return inversions;
        }
        
        // Count of smaller numbers after self (LeetCode 315)
        public static int[] countSmaller(int[] nums) {
            int n = nums.length;
            if (n == 0) return new int[0];
            
            int[] result = new int[n];
            
            // Coordinate compression
            int[] sorted = nums.clone();
            java.util.Arrays.sort(sorted);
            java.util.Map<Integer, Integer> compress = new java.util.HashMap<>();
            int rank = 0;
            for (int num : sorted) {
                if (!compress.containsKey(num)) {
                    compress.put(num, rank++);
                }
            }
            
            // Process from right to left
            FenwickTree ft = new FenwickTree(rank);
            for (int i = n - 1; i >= 0; i--) {
                int compressedVal = compress.get(nums[i]);
                // Count elements < nums[i] (strictly smaller)
                result[i] = compressedVal > 0 ? ft.rangeQuery(0, compressedVal - 1) : 0;
                ft.update(compressedVal, 1);
            }
            
            return result;
        }
    }

    /**
     * 6. ORDER STATISTICS using Fenwick Tree
     * 
     * Operations:
     * - insert(value): Insert value into multiset
     * - remove(value): Remove one occurrence of value
     * - kthSmallest(k): Find k-th smallest element (1-indexed)
     * - countLess(value): Count elements < value
     * 
     * Time: O(log n) per operation, O(log² n) for kth
     * Space: O(MAX_VALUE)
     */
    static class OrderStatistics {
        private FenwickTree ft;
        private int maxVal;
        
        public OrderStatistics(int maxValue) {
            maxVal = maxValue;
            ft = new FenwickTree(maxVal + 1);
        }
        
        // Insert value
        public void insert(int value) {
            ft.update(value, 1);
        }
        
        // Remove one occurrence of value
        public void remove(int value) {
            ft.update(value, -1);
        }
        
        // Count elements <= value
        public int countLessOrEqual(int value) {
            return ft.query(value);
        }
        
        // Count elements < value
        public int countLess(int value) {
            return value > 0 ? ft.query(value - 1) : 0;
        }
        
        // Find k-th smallest element (1-indexed) using binary search
        public int kthSmallest(int k) {
            int left = 0, right = maxVal;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (ft.query(mid) < k) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            return left;
        }
        
        // Alternative: O(log n) kth smallest using tree structure
        public int kthSmallestFast(int k) {
            int pos = 0, mask = Integer.highestOneBit(maxVal);
            
            while (mask > 0) {
                int mid = pos + mask;
                if (mid <= maxVal && ft.query(mid) < k) {
                    pos = mid;
                }
                mask >>= 1;
            }
            
            return pos + 1;
        }
    }

    /**
     * 7. RANGE SUM WITH COORDINATE COMPRESSION
     * 
     * Problem: LeetCode 327 - Count of Range Sum
     * Count number of range sums in [lower, upper]
     * 
     * Approach:
     * 1. Compute prefix sums
     * 2. For each i, count j < i where lower <= prefix[i] - prefix[j] <= upper
     * 3. Use Fenwick tree with coordinate compression
     * 
     * Time: O(n log n)
     * Space: O(n)
     */
    static class RangeSumCount {
        public static int countRangeSum(int[] nums, int lower, int upper) {
            int n = nums.length;
            long[] prefix = new long[n + 1];
            for (int i = 0; i < n; i++) {
                prefix[i + 1] = prefix[i] + nums[i];
            }
            
            // Coordinate compression
            java.util.Set<Long> allVals = new java.util.TreeSet<>();
            for (long p : prefix) {
                allVals.add(p);
                allVals.add(p - lower);
                allVals.add(p - upper);
            }
            
            java.util.Map<Long, Integer> compress = new java.util.HashMap<>();
            int rank = 0;
            for (long val : allVals) {
                compress.put(val, rank++);
            }
            
            // Count using Fenwick tree
            FenwickTree ft = new FenwickTree(rank);
            int count = 0;
            
            for (long p : prefix) {
                int left = compress.get(p - upper);
                int right = compress.get(p - lower);
                count += ft.rangeQuery(left, right);
                ft.update(compress.get(p), 1);
            }
            
            return count;
        }
    }

    /**
     * USAGE EXAMPLES AND TEST CASES
     */
    public static void main(String[] args) {
        // Example 1: Standard Fenwick Tree
        int[] arr1 = {1, 3, 5, 7, 9, 11};
        FenwickTree ft1 = new FenwickTree(arr1);
        System.out.println("Sum [0,2] = " + ft1.rangeQuery(0, 2)); // 1+3+5 = 9
        ft1.update(1, 2); // arr[1] = 3 + 2 = 5
        System.out.println("After update, sum [0,2] = " + ft1.rangeQuery(0, 2)); // 1+5+5 = 11
        
        // Example 2: Range update, point query
        int[] arr2 = {1, 2, 3, 4, 5};
        FenwickTreeRangeUpdate ft2 = new FenwickTreeRangeUpdate(arr2);
        ft2.rangeUpdate(1, 3, 10); // Add 10 to [1,3]
        System.out.println("After range update, arr[2] = " + ft2.pointQuery(2)); // 3+10 = 13
        
        // Example 3: 2D Fenwick Tree
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        FenwickTree2D ft2d = new FenwickTree2D(matrix);
        System.out.println("2D sum (0,0)-(1,1) = " + ft2d.rangeQuery(0, 0, 1, 1)); // 1+2+4+5 = 12
        ft2d.update(1, 1, 10); // Add 10 to matrix[1][1]
        System.out.println("After update, 2D sum (0,0)-(1,1) = " + ft2d.rangeQuery(0, 0, 1, 1)); // 22
        
        // Example 4: Inversion count
        int[] arr3 = {5, 2, 6, 1};
        int inversions = InversionCounter.countInversions(arr3);
        System.out.println("Inversions in [5,2,6,1] = " + inversions); // (5,2), (5,1), (2,1), (6,1) = 4
        
        // Example 5: Count smaller after self
        int[] arr4 = {5, 2, 6, 1};
        int[] smaller = InversionCounter.countSmaller(arr4);
        System.out.print("Count smaller: ");
        for (int s : smaller) System.out.print(s + " "); // [2, 1, 1, 0]
        System.out.println();
        
        // Example 6: Order statistics
        OrderStatistics os = new OrderStatistics(100);
        os.insert(5); os.insert(3); os.insert(7); os.insert(3); os.insert(9);
        System.out.println("3rd smallest = " + os.kthSmallest(3)); // Elements: 3,3,5,7,9 → 5
        System.out.println("Count < 6 = " + os.countLess(6)); // 3,3,5 = 3
        
        // Example 7: Range sum count
        int[] arr5 = {-2, 5, -1};
        int count = RangeSumCount.countRangeSum(arr5, -2, 2);
        System.out.println("Range sums in [-2, 2] = " + count); // Expected: 3
    }
}
