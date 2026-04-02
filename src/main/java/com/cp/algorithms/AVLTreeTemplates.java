package com.cp.algorithms;

import com.cp.problems.Complexity;

/**
 * AVL Tree (Self-Balancing BST) - CRITICAL for Apple (20%), Microsoft (15%)
 * 
 * Essential for:
 * - Self-balancing Binary Search Tree operations
 * - Order statistics (kth smallest/largest)
 * - Range queries with updates
 * - Sorted data with dynamic insertions/deletions
 * 
 * Interview Questions:
 * - LeetCode 315: Count of Smaller Numbers After Self
 * - Implement AVL Tree
 * - Design a data structure for range queries with updates
 * - Order statistics tree
 * 
 * Balance Factor: height(left) - height(right) ∈ {-1, 0, 1}
 * Rotations: LL, RR, LR, RL to maintain balance
 * 
 * Companies: Apple (5 stars), Microsoft (4 stars), Amazon (3 stars)
 */
public class AVLTreeTemplates {
    
    /**
     * AVL Tree Node with height tracking
     */
    public static class AVLNode {
        int val;
        int height;
        int size; // For order statistics
        AVLNode left, right;
        
        public AVLNode(int val) {
            this.val = val;
            this.height = 1;
            this.size = 1;
        }
    }
    
    /**
     * AVL Tree implementation with all operations
     */
    public static class AVLTree {
        private AVLNode root;
        
        public AVLTree() {
            this.root = null;
        }
        
        // ========== UTILITY METHODS ==========
        
        /**
         * Get height of node (null-safe)
         */
        private int height(AVLNode node) {
            return node == null ? 0 : node.height;
        }
        
        /**
         * Get size of subtree (null-safe)
         */
        private int size(AVLNode node) {
            return node == null ? 0 : node.size;
        }
        
        /**
         * Update height and size of node based on children
         */
        private void updateNode(AVLNode node) {
            if (node != null) {
                node.height = 1 + Math.max(height(node.left), height(node.right));
                node.size = 1 + size(node.left) + size(node.right);
            }
        }
        
        /**
         * Get balance factor of node
         * Positive: left-heavy, Negative: right-heavy
         */
        private int getBalance(AVLNode node) {
            return node == null ? 0 : height(node.left) - height(node.right);
        }
        
        // ========== ROTATIONS ==========
        
        /**
         * Right rotation (LL case)
         *        y                    x
         *       / \                  / \
         *      x   C    --->        A   y
         *     / \                      / \
         *    A   B                    B   C
         */
        @Complexity(time = "O(1)", space = "O(1)")
        private AVLNode rightRotate(AVLNode y) {
            AVLNode x = y.left;
            AVLNode B = x.right;
            
            // Perform rotation
            x.right = y;
            y.left = B;
            
            // Update heights and sizes
            updateNode(y);
            updateNode(x);
            
            return x;
        }
        
        /**
         * Left rotation (RR case)
         *      x                      y
         *     / \                    / \
         *    A   y       --->       x   C
         *       / \                / \
         *      B   C              A   B
         */
        @Complexity(time = "O(1)", space = "O(1)")
        private AVLNode leftRotate(AVLNode x) {
            AVLNode y = x.right;
            AVLNode B = y.left;
            
            // Perform rotation
            y.left = x;
            x.right = B;
            
            // Update heights and sizes
            updateNode(x);
            updateNode(y);
            
            return y;
        }
        
        // ========== INSERT ==========
        
        /**
         * Insert a value into AVL tree
         */
        @Complexity(time = "O(log n)", space = "O(log n)")
        public void insert(int val) {
            root = insertRec(root, val);
        }
        
        private AVLNode insertRec(AVLNode node, int val) {
            // Standard BST insertion
            if (node == null) {
                return new AVLNode(val);
            }
            
            if (val < node.val) {
                node.left = insertRec(node.left, val);
            } else if (val > node.val) {
                node.right = insertRec(node.right, val);
            } else {
                // Duplicate values: ignore (standard BST behavior)
                return node;
            }
            
            // Update height and size
            updateNode(node);
            
            // Get balance factor
            int balance = getBalance(node);
            
            // Left-Left Case (LL)
            if (balance > 1 && val < node.left.val) {
                return rightRotate(node);
            }
            
            // Right-Right Case (RR)
            if (balance < -1 && val > node.right.val) {
                return leftRotate(node);
            }
            
            // Left-Right Case (LR)
            if (balance > 1 && val > node.left.val) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            
            // Right-Left Case (RL)
            if (balance < -1 && val < node.right.val) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
            
            return node;
        }
        
        // ========== DELETE ==========
        
        /**
         * Delete a value from AVL tree
         */
        @Complexity(time = "O(log n)", space = "O(log n)")
        public void delete(int val) {
            root = deleteRec(root, val);
        }
        
        private AVLNode deleteRec(AVLNode node, int val) {
            // Standard BST deletion
            if (node == null) {
                return node;
            }
            
            if (val < node.val) {
                node.left = deleteRec(node.left, val);
            } else if (val > node.val) {
                node.right = deleteRec(node.right, val);
            } else {
                // Node to be deleted found
                
                // Case 1: Node with only one child or no child
                if (node.left == null || node.right == null) {
                    AVLNode temp = (node.left != null) ? node.left : node.right;
                    
                    // No child case
                    if (temp == null) {
                        return null;
                    } else {
                        // One child case
                        return temp;
                    }
                } else {
                    // Case 2: Node with two children
                    // Get inorder successor (smallest in right subtree)
                    AVLNode temp = minValueNode(node.right);
                    
                    // Copy successor's value to this node
                    node.val = temp.val;
                    
                    // Delete the successor
                    node.right = deleteRec(node.right, temp.val);
                }
            }
            
            // Update height and size
            updateNode(node);
            
            // Get balance factor
            int balance = getBalance(node);
            
            // Left-Left Case
            if (balance > 1 && getBalance(node.left) >= 0) {
                return rightRotate(node);
            }
            
            // Left-Right Case
            if (balance > 1 && getBalance(node.left) < 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            
            // Right-Right Case
            if (balance < -1 && getBalance(node.right) <= 0) {
                return leftRotate(node);
            }
            
            // Right-Left Case
            if (balance < -1 && getBalance(node.right) > 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
            
            return node;
        }
        
        /**
         * Find node with minimum value in tree
         */
        private AVLNode minValueNode(AVLNode node) {
            AVLNode current = node;
            while (current.left != null) {
                current = current.left;
            }
            return current;
        }
        
        // ========== SEARCH ==========
        
        /**
         * Search for a value in AVL tree
         */
        @Complexity(time = "O(log n)", space = "O(log n)")
        public boolean search(int val) {
            return searchRec(root, val);
        }
        
        private boolean searchRec(AVLNode node, int val) {
            if (node == null) return false;
            
            if (val == node.val) return true;
            if (val < node.val) return searchRec(node.left, val);
            return searchRec(node.right, val);
        }
        
        // ========== ORDER STATISTICS ==========
        
        /**
         * Find kth smallest element (1-indexed)
         */
        @Complexity(time = "O(log n)", space = "O(log n)")
        public Integer kthSmallest(int k) {
            return kthSmallestRec(root, k);
        }
        
        private Integer kthSmallestRec(AVLNode node, int k) {
            if (node == null) return null;
            
            int leftSize = size(node.left);
            
            if (k <= leftSize) {
                return kthSmallestRec(node.left, k);
            } else if (k == leftSize + 1) {
                return node.val;
            } else {
                return kthSmallestRec(node.right, k - leftSize - 1);
            }
        }
        
        /**
         * Find kth largest element (1-indexed)
         */
        @Complexity(time = "O(log n)", space = "O(log n)")
        public Integer kthLargest(int k) {
            return kthSmallest(size(root) - k + 1);
        }
        
        /**
         * Count elements smaller than val
         */
        @Complexity(time = "O(log n)", space = "O(log n)")
        public int countSmaller(int val) {
            return countSmallerRec(root, val);
        }
        
        private int countSmallerRec(AVLNode node, int val) {
            if (node == null) return 0;
            
            if (val <= node.val) {
                return countSmallerRec(node.left, val);
            } else {
                return 1 + size(node.left) + countSmallerRec(node.right, val);
            }
        }
        
        /**
         * Count elements in range [low, high]
         */
        @Complexity(time = "O(log n)", space = "O(log n)")
        public int countInRange(int low, int high) {
            return countSmallerOrEqual(high) - countSmallerOrEqual(low - 1);
        }
        
        private int countSmallerOrEqual(int val) {
            return countSmallerOrEqualRec(root, val);
        }
        
        private int countSmallerOrEqualRec(AVLNode node, int val) {
            if (node == null) return 0;
            
            if (val < node.val) {
                return countSmallerOrEqualRec(node.left, val);
            } else {
                return 1 + size(node.left) + countSmallerOrEqualRec(node.right, val);
            }
        }
        
        // ========== RANGE QUERIES ==========
        
        /**
         * Sum of all elements in range [low, high]
         */
        @Complexity(time = "O(log n + k)", space = "O(log n)")
        public long rangeSum(int low, int high) {
            return rangeSumRec(root, low, high);
        }
        
        private long rangeSumRec(AVLNode node, int low, int high) {
            if (node == null) return 0;
            
            long sum = 0;
            
            if (node.val >= low && node.val <= high) {
                sum += node.val;
            }
            
            if (node.val > low) {
                sum += rangeSumRec(node.left, low, high);
            }
            
            if (node.val < high) {
                sum += rangeSumRec(node.right, low, high);
            }
            
            return sum;
        }
        
        // ========== UTILITY ==========
        
        /**
         * Get size of tree
         */
        public int size() {
            return size(root);
        }
        
        /**
         * Get height of tree
         */
        public int height() {
            return height(root);
        }
        
        /**
         * Check if tree is balanced (for testing)
         */
        public boolean isBalanced() {
            return isBalancedRec(root);
        }
        
        private boolean isBalancedRec(AVLNode node) {
            if (node == null) return true;
            
            int balance = getBalance(node);
            if (Math.abs(balance) > 1) return false;
            
            return isBalancedRec(node.left) && isBalancedRec(node.right);
        }
        
        /**
         * Inorder traversal (sorted order)
         */
        public java.util.List<Integer> inorder() {
            java.util.List<Integer> result = new java.util.ArrayList<>();
            inorderRec(root, result);
            return result;
        }
        
        private void inorderRec(AVLNode node, java.util.List<Integer> result) {
            if (node != null) {
                inorderRec(node.left, result);
                result.add(node.val);
                inorderRec(node.right, result);
            }
        }
        
        /**
         * Print tree structure (for debugging)
         */
        public void printTree() {
            printTreeRec(root, "", true);
        }
        
        private void printTreeRec(AVLNode node, String prefix, boolean isTail) {
            if (node == null) return;
            
            System.out.println(prefix + (isTail ? "└── " : "├── ") + 
                             node.val + " (h=" + node.height + ", s=" + node.size + ")");
            
            if (node.left != null || node.right != null) {
                if (node.left != null) {
                    printTreeRec(node.left, prefix + (isTail ? "    " : "│   "), false);
                }
                if (node.right != null) {
                    printTreeRec(node.right, prefix + (isTail ? "    " : "│   "), true);
                }
            }
        }
    }
    
    // ========== LEETCODE 315: COUNT OF SMALLER NUMBERS AFTER SELF ==========
    
    /**
     * LeetCode 315: Count of Smaller Numbers After Self
     * 
     * For each element in array, count how many smaller elements appear after it.
     * Solution: Process array from right to left, insert into AVL tree and count smaller.
     */
    @Complexity(time = "O(n log n)", space = "O(n)")
    public static java.util.List<Integer> countSmaller(int[] nums) {
        AVLTree tree = new AVLTree();
        int n = nums.length;
        java.util.List<Integer> result = new java.util.ArrayList<>();
        
        // Process from right to left
        for (int i = n - 1; i >= 0; i--) {
            int count = tree.countSmaller(nums[i]);
            result.add(count);
            tree.insert(nums[i]);
        }
        
        // Reverse to get correct order
        java.util.Collections.reverse(result);
        return result;
    }
    
    // ========== EXAMPLE: ORDER STATISTICS QUERIES ==========
    
    /**
     * Process queries on a dynamic set:
     * - INSERT x: Insert x into set
     * - DELETE x: Delete x from set
     * - KTHMIN k: Find kth smallest
     * - KTHMAX k: Find kth largest
     * - COUNT_RANGE l r: Count elements in [l, r]
     */
    public static class DynamicOrderStatistics {
        private AVLTree tree;
        
        public DynamicOrderStatistics() {
            tree = new AVLTree();
        }
        
        public void insert(int val) {
            tree.insert(val);
        }
        
        public void delete(int val) {
            tree.delete(val);
        }
        
        public Integer kthMin(int k) {
            return tree.kthSmallest(k);
        }
        
        public Integer kthMax(int k) {
            return tree.kthLargest(k);
        }
        
        public int countRange(int low, int high) {
            return tree.countInRange(low, high);
        }
        
        public int size() {
            return tree.size();
        }
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        
        System.out.println("=== AVL Tree Demo ===");
        
        // Insert elements
        int[] values = {10, 20, 30, 40, 50, 25};
        for (int val : values) {
            tree.insert(val);
            System.out.println("Inserted " + val + ", balanced: " + tree.isBalanced());
        }
        
        System.out.println("\nTree structure:");
        tree.printTree();
        
        System.out.println("\nInorder traversal: " + tree.inorder());
        System.out.println("Tree size: " + tree.size());
        System.out.println("Tree height: " + tree.height());
        
        // Order statistics
        System.out.println("\n3rd smallest: " + tree.kthSmallest(3)); // 25
        System.out.println("2nd largest: " + tree.kthLargest(2)); // 40
        
        // Count queries
        System.out.println("\nElements smaller than 35: " + tree.countSmaller(35)); // 3
        System.out.println("Elements in range [20, 40]: " + tree.countInRange(20, 40)); // 4
        
        // Range sum
        System.out.println("Sum of range [20, 40]: " + tree.rangeSum(20, 40)); // 115
        
        // Delete elements
        tree.delete(20);
        System.out.println("\nAfter deleting 20:");
        System.out.println("Inorder traversal: " + tree.inorder());
        System.out.println("Balanced: " + tree.isBalanced());
        
        // LeetCode 315
        System.out.println("\n=== LeetCode 315 Example ===");
        int[] nums = {5, 2, 6, 1};
        System.out.println("Input: " + java.util.Arrays.toString(nums));
        System.out.println("Count smaller after self: " + countSmaller(nums)); // [2, 1, 1, 0]
        
        // Dynamic Order Statistics
        System.out.println("\n=== Dynamic Order Statistics ===");
        DynamicOrderStatistics dos = new DynamicOrderStatistics();
        dos.insert(5);
        dos.insert(2);
        dos.insert(8);
        dos.insert(1);
        dos.insert(9);
        
        System.out.println("2nd smallest: " + dos.kthMin(2)); // 2
        System.out.println("Count in range [2, 8]: " + dos.countRange(2, 8)); // 3
        
        dos.delete(5);
        System.out.println("After deleting 5, 2nd smallest: " + dos.kthMin(2)); // 2
    }
}
