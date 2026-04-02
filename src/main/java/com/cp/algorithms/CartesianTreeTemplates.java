package com.cp.algorithms;

import java.util.*;

/**
 * Cartesian Tree Templates - Range Query Optimization
 * 
 * WHEN TO USE:
 * - Range Minimum Query (RMQ) with O(1) query after O(n) preprocessing
 * - All nearest smaller/greater elements
 * - Stack-based divide and conquer optimization
 * - Implicit treaps for advanced data structures
 * 
 * KEY INSIGHT:
 * Cartesian tree from array has heap property + in-order gives original array
 * - Parent has smaller value than children (min-heap property)
 * - In-order traversal reproduces original array
 * - Can be built in O(n) using monotonic stack
 * 
 * COMPLEXITY:
 * - Build: O(n)
 * - RMQ preprocessing: O(n)
 * - RMQ query: O(1)
 * - Space: O(n)
 * 
 * COMPARISON:
 * - Cartesian Tree + LCA: O(n) build, O(1) query, complex implementation
 * - Sparse Table: O(n log n) build, O(1) query, simpler
 * - Segment Tree: O(n) build, O(log n) query, supports updates
 * 
 * COMMON INTERVIEW PROBLEMS:
 * - LeetCode 84: Largest Rectangle in Histogram (related concept)
 * - Range Minimum Query problems
 * - All nearest smaller elements
 * - Advanced: Implicit treaps, persistent data structures
 * 
 * 
 */
public class CartesianTreeTemplates {

    /**
     * 1. CARTESIAN TREE NODE
     */
    static class CartesianNode {
        int value;
        int index;  // Original position in array
        CartesianNode left, right, parent;
        
        CartesianNode(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }

    /**
     * 2. BUILD CARTESIAN TREE - O(n) Stack-based Construction
     * 
     * Algorithm:
     * - Process elements left to right
     * - Maintain stack of nodes on rightmost path
     * - For each element, pop until stack top ≥ current value
     * - Last popped becomes left child, current becomes right child of remaining top
     * 
     * Time: O(n)
     * Space: O(n)
     * 
     * Example:
     * Array: [3, 2, 6, 1, 9]
     * Tree:      1
     *           / \
     *          2   9
     *         / \
     *        3   6
     */
    static class CartesianTreeBuilder {
        public static CartesianNode buildTree(int[] arr) {
            int n = arr.length;
            if (n == 0) return null;
            
            Deque<CartesianNode> stack = new ArrayDeque<>();
            CartesianNode root = null;
            
            for (int i = 0; i < n; i++) {
                CartesianNode curr = new CartesianNode(arr[i], i);
                CartesianNode lastPopped = null;
                
                // Pop while stack top has larger value
                while (!stack.isEmpty() && stack.peek().value > curr.value) {
                    lastPopped = stack.pop();
                }
                
                // Set parent-child relationships
                if (lastPopped != null) {
                    curr.left = lastPopped;
                    lastPopped.parent = curr;
                }
                
                if (!stack.isEmpty()) {
                    stack.peek().right = curr;
                    curr.parent = stack.peek();
                } else {
                    root = curr;  // New root
                }
                
                stack.push(curr);
            }
            
            return root;
        }
        
        // Alternative: Build with explicit parent tracking
        public static CartesianNode buildTreeWithParents(int[] arr) {
            int n = arr.length;
            if (n == 0) return null;
            
            CartesianNode[] nodes = new CartesianNode[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new CartesianNode(arr[i], i);
            }
            
            Deque<Integer> stack = new ArrayDeque<>();
            CartesianNode root = null;
            
            for (int i = 0; i < n; i++) {
                int lastPopped = -1;
                
                while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                    lastPopped = stack.pop();
                }
                
                if (lastPopped != -1) {
                    nodes[i].left = nodes[lastPopped];
                    nodes[lastPopped].parent = nodes[i];
                }
                
                if (!stack.isEmpty()) {
                    nodes[stack.peek()].right = nodes[i];
                    nodes[i].parent = nodes[stack.peek()];
                } else {
                    root = nodes[i];
                }
                
                stack.push(i);
            }
            
            return root;
        }
    }

    /**
     * 3. RMQ USING CARTESIAN TREE + LCA
     * 
     * Problem: Answer range minimum queries in O(1)
     * 
     * Approach:
     * 1. Build Cartesian tree from array
     * 2. RMQ(i, j) = LCA of nodes at positions i and j
     * 3. Use any O(1) LCA algorithm
     * 
     * Time: O(n) preprocessing, O(1) query
     * Space: O(n)
     */
    static class CartesianRMQ {
        private CartesianNode root;
        private CartesianNode[] nodes;
        private LCAProcessor lca;
        
        public CartesianRMQ(int[] arr) {
            int n = arr.length;
            root = CartesianTreeBuilder.buildTree(arr);
            nodes = new CartesianNode[n];
            collectNodes(root, nodes);
            lca = new LCAProcessor(root, n);
        }
        
        // Collect nodes by their original indices
        private void collectNodes(CartesianNode node, CartesianNode[] nodes) {
            if (node == null) return;
            nodes[node.index] = node;
            collectNodes(node.left, nodes);
            collectNodes(node.right, nodes);
        }
        
        // Range minimum query in O(1)
        public int rmq(int i, int j) {
            if (i > j) {
                int temp = i; i = j; j = temp;
            }
            CartesianNode lcaNode = lca.lca(nodes[i], nodes[j]);
            return lcaNode.value;
        }
        
        // Get index of minimum element in range
        public int rmqIndex(int i, int j) {
            if (i > j) {
                int temp = i; i = j; j = temp;
            }
            CartesianNode lcaNode = lca.lca(nodes[i], nodes[j]);
            return lcaNode.index;
        }
    }

    /**
     * 4. SIMPLE LCA FOR CARTESIAN TREE (Binary Lifting)
     */
    static class LCAProcessor {
        private CartesianNode[] nodes;
        private int[][] up;
        private int[] depth;
        private int maxLog;
        
        public LCAProcessor(CartesianNode root, int n) {
            nodes = new CartesianNode[n];
            maxLog = Integer.numberOfTrailingZeros(Integer.highestOneBit(n)) + 1;
            up = new int[n][maxLog];
            depth = new int[n];
            
            // Collect nodes and preprocess
            dfs(root, -1, 0);
            preprocess(n);
        }
        
        private void dfs(CartesianNode node, int parentIdx, int d) {
            if (node == null) return;
            
            int idx = node.index;
            nodes[idx] = node;
            depth[idx] = d;
            up[idx][0] = parentIdx;
            
            dfs(node.left, idx, d + 1);
            dfs(node.right, idx, d + 1);
        }
        
        private void preprocess(int n) {
            for (int j = 1; j < maxLog; j++) {
                for (int i = 0; i < n; i++) {
                    if (up[i][j - 1] != -1) {
                        up[i][j] = up[up[i][j - 1]][j - 1];
                    } else {
                        up[i][j] = -1;
                    }
                }
            }
        }
        
        public CartesianNode lca(CartesianNode u, CartesianNode v) {
            int a = u.index, b = v.index;
            
            if (depth[a] < depth[b]) {
                int temp = a; a = b; b = temp;
            }
            
            // Bring a to same level as b
            int diff = depth[a] - depth[b];
            for (int i = 0; i < maxLog; i++) {
                if (((diff >> i) & 1) == 1) {
                    a = up[a][i];
                }
            }
            
            if (a == b) return nodes[a];
            
            // Binary search for LCA
            for (int i = maxLog - 1; i >= 0; i--) {
                if (up[a][i] != up[b][i]) {
                    a = up[a][i];
                    b = up[b][i];
                }
            }
            
            return nodes[up[a][0]];
        }
    }

    /**
     * 5. ALL NEAREST SMALLER ELEMENTS using Stack-based approach
     * 
     * Problem: For each element, find nearest smaller element to left and right
     * 
     * Approach: Use monotonic stack to find nearest smaller elements
     * - For left smaller: scan left to right, maintain increasing stack
     * - For right smaller: scan right to left, maintain increasing stack
     * 
     * Time: O(n)
     * Space: O(n)
     */
    static class NearestSmallerElements {
        public static int[][] allNearestSmaller(int[] arr) {
            int n = arr.length;
            int[] leftSmaller = new int[n];
            int[] rightSmaller = new int[n];
            Arrays.fill(leftSmaller, -1);
            Arrays.fill(rightSmaller, -1);
            
            // Find left smaller using stack
            Deque<Integer> stack = new ArrayDeque<>();
            for (int i = 0; i < n; i++) {
                // Pop elements that are greater than or equal to current
                while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                    stack.pop();
                }
                
                if (!stack.isEmpty()) {
                    leftSmaller[i] = stack.peek();
                }
                
                stack.push(i);
            }
            
            // Find right smaller using stack
            stack.clear();
            for (int i = n - 1; i >= 0; i--) {
                // Pop elements that are greater than or equal to current
                while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                    stack.pop();
                }
                
                if (!stack.isEmpty()) {
                    rightSmaller[i] = stack.peek();
                }
                
                stack.push(i);
            }
            
            return new int[][]{leftSmaller, rightSmaller};
        }
    }

    /**
     * 6. LARGEST RECTANGLE IN HISTOGRAM using Cartesian Tree Concept
     * 
     * Problem: LeetCode 84 - Find largest rectangular area in histogram
     * 
     * Approach: For each bar as minimum height, find left and right bounds
     * 
     * Time: O(n)
     * Space: O(n)
     */
    static class LargestRectangle {
        public static int largestRectangleArea(int[] heights) {
            int n = heights.length;
            int maxArea = 0;
            
            // Find nearest smaller elements
            int[] leftSmaller = new int[n];
            int[] rightSmaller = new int[n];
            Arrays.fill(leftSmaller, -1);
            Arrays.fill(rightSmaller, n);
            
            // Left smaller using stack
            Deque<Integer> stack = new ArrayDeque<>();
            for (int i = 0; i < n; i++) {
                while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    leftSmaller[i] = stack.peek();
                }
                stack.push(i);
            }
            
            // Right smaller using stack
            stack.clear();
            for (int i = n - 1; i >= 0; i--) {
                while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    rightSmaller[i] = stack.peek();
                }
                stack.push(i);
            }
            
            // Calculate max area
            for (int i = 0; i < n; i++) {
                int width = rightSmaller[i] - leftSmaller[i] - 1;
                int area = heights[i] * width;
                maxArea = Math.max(maxArea, area);
            }
            
            return maxArea;
        }
    }

    /**
     * 7. TREE TRAVERSALS AND UTILITIES
     */
    static class CartesianTreeUtils {
        // Verify heap property
        public static boolean isValidCartesianTree(CartesianNode root, int[] originalArray) {
            List<Integer> inorder = new ArrayList<>();
            inorderTraversal(root, inorder);
            
            // Check if inorder matches original array
            if (inorder.size() != originalArray.length) return false;
            for (int i = 0; i < inorder.size(); i++) {
                if (inorder.get(i) != originalArray[i]) return false;
            }
            
            // Check heap property
            return checkHeapProperty(root);
        }
        
        private static void inorderTraversal(CartesianNode node, List<Integer> result) {
            if (node == null) return;
            inorderTraversal(node.left, result);
            result.add(node.value);
            inorderTraversal(node.right, result);
        }
        
        private static boolean checkHeapProperty(CartesianNode node) {
            if (node == null) return true;
            
            if (node.left != null && node.left.value < node.value) return false;
            if (node.right != null && node.right.value < node.value) return false;
            
            return checkHeapProperty(node.left) && checkHeapProperty(node.right);
        }
        
        // Print tree structure
        public static void printTree(CartesianNode root) {
            printTreeHelper(root, "", true);
        }
        
        private static void printTreeHelper(CartesianNode node, String prefix, boolean isLast) {
            if (node == null) return;
            
            System.out.println(prefix + (isLast ? "└── " : "├── ") + 
                             node.value + " (idx:" + node.index + ")");
            
            if (node.left != null || node.right != null) {
                printTreeHelper(node.left, prefix + (isLast ? "    " : "│   "), node.right == null);
                printTreeHelper(node.right, prefix + (isLast ? "    " : "│   "), true);
            }
        }
    }

    /**
     * USAGE EXAMPLES AND TEST CASES
     */
    public static void main(String[] args) {
        // Example 1: Build Cartesian tree
        System.out.println("=== Example 1: Build Cartesian Tree ===");
        int[] arr1 = {3, 2, 6, 1, 9, 7};
        CartesianNode root1 = CartesianTreeBuilder.buildTree(arr1);
        System.out.println("Array: " + Arrays.toString(arr1));
        System.out.println("Cartesian Tree:");
        CartesianTreeUtils.printTree(root1);
        System.out.println("Valid tree: " + CartesianTreeUtils.isValidCartesianTree(root1, arr1));
        
        // Example 2: RMQ using Cartesian tree
        System.out.println("\n=== Example 2: Range Minimum Query ===");
        int[] arr2 = {4, 2, 5, 1, 6, 3};
        CartesianRMQ rmq = new CartesianRMQ(arr2);
        System.out.println("Array: " + Arrays.toString(arr2));
        System.out.println("RMQ(1, 4) = " + rmq.rmq(1, 4)); // min of [2,5,1,6] = 1
        System.out.println("RMQ(0, 2) = " + rmq.rmq(0, 2)); // min of [4,2,5] = 2
        System.out.println("RMQ(3, 5) = " + rmq.rmq(3, 5)); // min of [1,6,3] = 1
        System.out.println("RMQ index(1, 4) = " + rmq.rmqIndex(1, 4)); // index of min
        
        // Example 3: Nearest smaller elements
        System.out.println("\n=== Example 3: Nearest Smaller Elements ===");
        int[] arr3 = {4, 5, 2, 10, 8};
        int[][] nearestSmaller = NearestSmallerElements.allNearestSmaller(arr3);
        System.out.println("Array: " + Arrays.toString(arr3));
        System.out.println("Left smaller: " + Arrays.toString(nearestSmaller[0]));
        System.out.println("Right smaller: " + Arrays.toString(nearestSmaller[1]));
        
        // Example 4: Largest rectangle in histogram
        System.out.println("\n=== Example 4: Largest Rectangle ===");
        int[] heights = {2, 1, 5, 6, 2, 3};
        int maxArea = LargestRectangle.largestRectangleArea(heights);
        System.out.println("Heights: " + Arrays.toString(heights));
        System.out.println("Largest rectangle area: " + maxArea); // 10
        
        // Example 5: Edge cases
        System.out.println("\n=== Example 5: Edge Cases ===");
        int[] sorted = {1, 2, 3, 4, 5}; // Right-skewed tree
        CartesianNode sortedRoot = CartesianTreeBuilder.buildTree(sorted);
        System.out.println("Sorted array tree:");
        CartesianTreeUtils.printTree(sortedRoot);
        
        int[] reverse = {5, 4, 3, 2, 1}; // Left-skewed tree
        CartesianNode reverseRoot = CartesianTreeBuilder.buildTree(reverse);
        System.out.println("\nReverse sorted array tree:");
        CartesianTreeUtils.printTree(reverseRoot);
    }
}