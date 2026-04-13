package com.cp.algorithms;

import com.cp.datastructures.TreeNode;
import com.cp.problems.Complexity;
import java.util.*;

/**
 * Binary Search Tree (BST) algorithm templates for coding interviews.
 * 
 * BST Property: For every node, left subtree < node < right subtree
 * 
 * Coverage:
 * - BST Insert, Delete, Search
 * - Validate BST
 * - Kth Smallest/Largest Element
 * - Inorder Successor/Predecessor
 * - Convert Sorted Array to BST
 * - BST Iterator
 * - Range Sum in BST
 * 
 * Critical for: All FAANG companies
 * Frequency: ~25% of tree interview problems
 */
public class BSTTemplates {

    // ============================================================================
    // BASIC BST OPERATIONS
    // ============================================================================
    
    /**
     * Insert a value into BST.
     * Returns the root of the modified tree.
     */
    @Complexity(time = "O(h) where h=height, O(log n) balanced, O(n) skewed", space = "O(h) for recursion")
    public static TreeNode insert(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);
        
        if (val < root.val) {
            root.left = insert(root.left, val);
        } else if (val > root.val) {
            root.right = insert(root.right, val);
        }
        // If val == root.val, do nothing (no duplicates)
        
        return root;
    }
    
    /**
     * Insert into BST (iterative version).
     * Interview tip: Mention iterative uses O(1) space.
     */
    @Complexity(time = "O(h)", space = "O(1)")
    public static TreeNode insertIterative(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);
        
        TreeNode curr = root;
        while (true) {
            if (val < curr.val) {
                if (curr.left == null) {
                    curr.left = new TreeNode(val);
                    break;
                }
                curr = curr.left;
            } else if (val > curr.val) {
                if (curr.right == null) {
                    curr.right = new TreeNode(val);
                    break;
                }
                curr = curr.right;
            } else {
                break; // duplicate
            }
        }
        
        return root;
    }
    
    /**
     * Search for a value in BST.
     * Returns the node containing val, or null if not found.
     */
    @Complexity(time = "O(h)", space = "O(1)")
    public static TreeNode search(TreeNode root, int val) {
        while (root != null && root.val != val) {
            root = (val < root.val) ? root.left : root.right;
        }
        return root;
    }
    
    /**
     * Delete a node from BST.
     * Three cases:
     * 1. Node is leaf: just remove
     * 2. Node has one child: replace with child
     * 3. Node has two children: replace with inorder successor (or predecessor)
     */
    @Complexity(time = "O(h)", space = "O(h)")
    public static TreeNode delete(TreeNode root, int key) {
        if (root == null) return null;
        
        if (key < root.val) {
            root.left = delete(root.left, key);
        } else if (key > root.val) {
            root.right = delete(root.right, key);
        } else {
            // Found node to delete
            
            // Case 1 & 2: Node has 0 or 1 child
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            
            // Case 3: Node has 2 children
            // Replace with inorder successor (min in right subtree)
            TreeNode successor = findMin(root.right);
            root.val = successor.val;
            root.right = delete(root.right, successor.val);
        }
        
        return root;
    }
    
    /**
     * Find minimum value node in BST (leftmost node).
     */
    @Complexity(time = "O(h)", space = "O(1)")
    public static TreeNode findMin(TreeNode root) {
        while (root != null && root.left != null) {
            root = root.left;
        }
        return root;
    }
    
    /**
     * Find maximum value node in BST (rightmost node).
     */
    @Complexity(time = "O(h)", space = "O(1)")
    public static TreeNode findMax(TreeNode root) {
        while (root != null && root.right != null) {
            root = root.right;
        }
        return root;
    }
    
    // ============================================================================
    // VALIDATE BST
    // ============================================================================
    
    /**
     * Validate if a binary tree is a valid BST.
     * Common mistake: Only checking left.val < root.val < right.val
     * Correct: Check entire left subtree < root < entire right subtree
     * 
     * Interview favorite: Amazon, Microsoft, Meta
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static boolean isValidBST(TreeNode root) {
        return isValidBST(root, null, null);
    }
    
    private static boolean isValidBST(TreeNode root, Integer min, Integer max) {
        if (root == null) return true;
        
        // Check current node is within range
        if ((min != null && root.val <= min) || (max != null && root.val >= max)) {
            return false;
        }
        
        // Check left subtree (all values must be < root.val)
        // Check right subtree (all values must be > root.val)
        return isValidBST(root.left, min, root.val) && 
               isValidBST(root.right, root.val, max);
    }
    
    /**
     * Validate BST using inorder traversal.
     * BST property: Inorder traversal should be strictly increasing.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static boolean isValidBSTInorder(TreeNode root) {
        return isValidBSTInorder(root, new long[]{Long.MIN_VALUE});
    }
    
    private static boolean isValidBSTInorder(TreeNode root, long[] prev) {
        if (root == null) return true;
        
        if (!isValidBSTInorder(root.left, prev)) return false;
        
        if (root.val <= prev[0]) return false;
        prev[0] = root.val;
        
        return isValidBSTInorder(root.right, prev);
    }
    
    // ============================================================================
    // KTH SMALLEST/LARGEST
    // ============================================================================
    
    /**
     * Kth Smallest Element in BST.
     * Pattern: Inorder traversal gives sorted order.
     * 
     * Interview tip: Mention follow-up: what if BST is modified frequently?
     * Answer: Maintain subtree sizes for O(h) queries.
     * 
     * Companies: Meta, Amazon, Google
     */
    @Complexity(time = "O(h + k)", space = "O(h)")
    public static int kthSmallest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        
        while (curr != null || !stack.isEmpty()) {
            // Go left as far as possible
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            
            curr = stack.pop();
            k--;
            if (k == 0) return curr.val;
            
            curr = curr.right;
        }
        
        return -1; // Should not reach here if k is valid
    }
    
    /**
     * Kth Smallest (recursive with counter).
     */
    @Complexity(time = "O(h + k)", space = "O(h)")
    public static int kthSmallestRecursive(TreeNode root, int k) {
        int[] result = new int[]{-1};
        int[] count = new int[]{0};
        kthSmallestHelper(root, k, count, result);
        return result[0];
    }
    
    private static void kthSmallestHelper(TreeNode root, int k, int[] count, int[] result) {
        if (root == null || count[0] >= k) return;
        
        kthSmallestHelper(root.left, k, count, result);
        
        count[0]++;
        if (count[0] == k) {
            result[0] = root.val;
            return;
        }
        
        kthSmallestHelper(root.right, k, count, result);
    }
    
    /**
     * Kth Largest Element in BST.
     * Pattern: Reverse inorder (right-root-left) gives descending order.
     */
    @Complexity(time = "O(h + k)", space = "O(h)")
    public static int kthLargest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        
        while (curr != null || !stack.isEmpty()) {
            // Go right as far as possible
            while (curr != null) {
                stack.push(curr);
                curr = curr.right;
            }
            
            curr = stack.pop();
            k--;
            if (k == 0) return curr.val;
            
            curr = curr.left;
        }
        
        return -1;
    }
    
    // ============================================================================
    // INORDER SUCCESSOR/PREDECESSOR
    // ============================================================================
    
    /**
     * Inorder Successor in BST.
     * Successor of node with value val is the smallest value > val.
     * 
     * Two cases:
     * 1. If node has right child: successor is min in right subtree
     * 2. Otherwise: successor is lowest ancestor where node is in left subtree
     */
    @Complexity(time = "O(h)", space = "O(1)")
    public static TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        // Case 1: Node has right child
        if (p.right != null) {
            return findMin(p.right);
        }
        
        // Case 2: Traverse from root to find successor
        TreeNode successor = null;
        TreeNode curr = root;
        
        while (curr != null) {
            if (p.val < curr.val) {
                successor = curr; // Potential successor
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        
        return successor;
    }
    
    /**
     * Inorder Predecessor in BST.
     * Predecessor of node with value val is the largest value < val.
     */
    @Complexity(time = "O(h)", space = "O(1)")
    public static TreeNode inorderPredecessor(TreeNode root, TreeNode p) {
        // Case 1: Node has left child
        if (p.left != null) {
            return findMax(p.left);
        }
        
        // Case 2: Traverse from root
        TreeNode predecessor = null;
        TreeNode curr = root;
        
        while (curr != null) {
            if (p.val > curr.val) {
                predecessor = curr; // Potential predecessor
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        
        return predecessor;
    }
    
    // ============================================================================
    // CONVERT SORTED ARRAY TO BST
    // ============================================================================
    
    /**
     * Convert Sorted Array to Balanced BST.
     * Pattern: Use middle element as root for balance.
     * Recursively build left and right subtrees.
     * 
     * Companies: Meta, Amazon, Microsoft
     */
    @Complexity(time = "O(n)", space = "O(log n) for recursion")
    public static TreeNode sortedArrayToBST(int[] nums) {
        return sortedArrayToBST(nums, 0, nums.length - 1);
    }
    
    private static TreeNode sortedArrayToBST(int[] nums, int left, int right) {
        if (left > right) return null;
        
        int mid = left + (right - left) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        
        root.left = sortedArrayToBST(nums, left, mid - 1);
        root.right = sortedArrayToBST(nums, mid + 1, right);
        
        return root;
    }
    
    /**
     * Convert Sorted Linked List to Balanced BST.
     * Pattern: Two pointer to find middle + recursion.
     * Note: Uses simple helper ListNode class (defined at bottom).
     */
    @Complexity(time = "O(n log n)", space = "O(log n)")
    public static TreeNode sortedListToBST(ListNodeHelper head) {
        if (head == null) return null;
        if (head.next == null) return new TreeNode(head.val);
        
        // Find middle using slow-fast pointers
        ListNodeHelper slow = head, fast = head, prev = null;
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // Disconnect left half
        if (prev != null) prev.next = null;
        
        TreeNode root = new TreeNode(slow.val);
        root.left = sortedListToBST(head == slow ? null : head);
        root.right = sortedListToBST(slow.next);
        
        return root;
    }
    
    // ============================================================================
    // BST ITERATOR
    // ============================================================================
    
    /**
     * BST Iterator for inorder traversal.
     * Implement next() and hasNext() both in O(1) average time.
     * 
     * Pattern: Use stack to simulate iterative inorder traversal.
     * 
     * Interview favorite: Meta, Google
     */
    public static class BSTIterator {
        private Stack<TreeNode> stack;
        
        @Complexity(time = "O(h)", space = "O(h)")
        public BSTIterator(TreeNode root) {
            stack = new Stack<>();
            pushLeft(root);
        }
        
        @Complexity(time = "O(1) amortized", space = "O(1)")
        public int next() {
            TreeNode node = stack.pop();
            pushLeft(node.right);
            return node.val;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        
        private void pushLeft(TreeNode node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }
    }
    
    // ============================================================================
    // RANGE QUERIES IN BST
    // ============================================================================
    
    /**
     * Range Sum Query in BST.
     * Find sum of all values in range [low, high].
     * 
     * Pattern: Prune search space using BST property.
     */
    @Complexity(time = "O(n) worst case, O(h + k) where k=nodes in range", space = "O(h)")
    public static int rangeSumBST(TreeNode root, int low, int high) {
        if (root == null) return 0;
        
        int sum = 0;
        
        // If current value is in range, add it
        if (root.val >= low && root.val <= high) {
            sum += root.val;
        }
        
        // Search left only if current val > low
        if (root.val > low) {
            sum += rangeSumBST(root.left, low, high);
        }
        
        // Search right only if current val < high
        if (root.val < high) {
            sum += rangeSumBST(root.right, low, high);
        }
        
        return sum;
    }
    
    /**
     * Count nodes in range [low, high].
     */
    @Complexity(time = "O(h + k)", space = "O(h)")
    public static int countNodesInRange(TreeNode root, int low, int high) {
        if (root == null) return 0;
        
        int count = 0;
        if (root.val >= low && root.val <= high) count = 1;
        
        if (root.val > low) {
            count += countNodesInRange(root.left, low, high);
        }
        if (root.val < high) {
            count += countNodesInRange(root.right, low, high);
        }
        
        return count;
    }
    
    // ============================================================================
    // ADVANCED BST PATTERNS
    // ============================================================================
    
    /**
     * Lowest Common Ancestor in BST.
     * Optimized for BST property.
     */
    @Complexity(time = "O(h)", space = "O(1)")
    public static TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        while (root != null) {
            // Both in left subtree
            if (p.val < root.val && q.val < root.val) {
                root = root.left;
            }
            // Both in right subtree
            else if (p.val > root.val && q.val > root.val) {
                root = root.right;
            }
            // Split point or one equals root
            else {
                return root;
            }
        }
        return null;
    }
    
    /**
     * Trim BST to range [low, high].
     * Remove nodes outside range.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static TreeNode trimBST(TreeNode root, int low, int high) {
        if (root == null) return null;
        
        // If root is less than low, trim left subtree and recurse right
        if (root.val < low) {
            return trimBST(root.right, low, high);
        }
        
        // If root is greater than high, trim right subtree and recurse left
        if (root.val > high) {
            return trimBST(root.left, low, high);
        }
        
        // Root is in range, trim both subtrees
        root.left = trimBST(root.left, low, high);
        root.right = trimBST(root.right, low, high);
        
        return root;
    }
    
    /**
     * Convert BST to Greater Sum Tree.
     * Each node value becomes sum of all values >= current value.
     * 
     * Pattern: Reverse inorder traversal (right-root-left).
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static TreeNode convertBSTtoGST(TreeNode root) {
        convertBSTtoGST(root, new int[]{0});
        return root;
    }
    
    private static void convertBSTtoGST(TreeNode root, int[] sum) {
        if (root == null) return;
        
        // Traverse right first (larger values)
        convertBSTtoGST(root.right, sum);
        
        // Update current node
        sum[0] += root.val;
        root.val = sum[0];
        
        // Traverse left
        convertBSTtoGST(root.left, sum);
    }
    
    /**
     * Recover BST where exactly two nodes are swapped.
     * Pattern: Find violations in inorder traversal.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static void recoverBST(TreeNode root) {
        TreeNode[] violations = new TreeNode[2];
        TreeNode[] prev = new TreeNode[]{null};
        
        findViolations(root, prev, violations);
        
        // Swap values back
        if (violations[0] != null && violations[1] != null) {
            int temp = violations[0].val;
            violations[0].val = violations[1].val;
            violations[1].val = temp;
        }
    }
    
    private static void findViolations(TreeNode root, TreeNode[] prev, TreeNode[] violations) {
        if (root == null) return;
        
        findViolations(root.left, prev, violations);
        
        if (prev[0] != null && prev[0].val > root.val) {
            if (violations[0] == null) {
                violations[0] = prev[0];
            }
            violations[1] = root;
        }
        prev[0] = root;
        
        findViolations(root.right, prev, violations);
    }
    
    /**
     * Check if two BSTs contain same set of elements.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static boolean isSameBST(TreeNode root1, TreeNode root2) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        
        inorder(root1, list1);
        inorder(root2, list2);
        
        return list1.equals(list2);
    }
    
    private static void inorder(TreeNode root, List<Integer> list) {
        if (root == null) return;
        inorder(root.left, list);
        list.add(root.val);
        inorder(root.right, list);
    }
    
    // Helper class for sorted list to BST (simple singly-linked list)
    public static class ListNodeHelper {
        public int val;
        public ListNodeHelper next;
        public ListNodeHelper(int val) { this.val = val; }
    }
}
