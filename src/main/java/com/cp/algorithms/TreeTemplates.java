package com.cp.algorithms;

import com.cp.datastructures.TreeNode;
import com.cp.problems.Complexity;
import java.util.*;

/**
 * Advanced Tree Algorithm Templates for coding interviews.
 * 
 * Coverage:
 * - Lowest Common Ancestor (LCA) - Binary Tree & BST
 * - Tree Diameter
 * - Maximum Path Sum
 * - Distance Between Nodes
 * - All Root-to-Leaf Paths
 * - Path Sum Variants
 * - Binary Lifting for LCA (advanced)
 * 
 * Critical for: All FAANG companies
 * Frequency: ~15% of tree interview problems
 */
public class TreeTemplates {

    // ============================================================================
    // LOWEST COMMON ANCESTOR (LCA)
    // ============================================================================
    
    /**
     * Lowest Common Ancestor in Binary Tree.
     * LCA is the lowest node that has both p and q as descendants.
     * 
     * Pattern: Post-order traversal. If found p or q, return it.
     * If left and right both non-null, current is LCA.
     * 
     * Interview favorite: Amazon, Meta, Google, Microsoft
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }
        
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        // If both left and right are non-null, root is LCA
        if (left != null && right != null) {
            return root;
        }
        
        // Return non-null child
        return left != null ? left : right;
    }
    
    /**
     * LCA with parent pointers (optimized).
     * Pattern: Convert to linked list intersection problem.
     */
    @Complexity(time = "O(h)", space = "O(1)")
    public static TreeNodeWithParent lowestCommonAncestorWithParent(
            TreeNodeWithParent p, TreeNodeWithParent q) {
        TreeNodeWithParent a = p, b = q;
        
        // Similar to linked list intersection
        while (a != b) {
            a = (a == null) ? q : a.parent;
            b = (b == null) ? p : b.parent;
        }
        
        return a;
    }
    
    /**
     * LCA for multiple nodes.
     * Find LCA of a set of nodes.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static TreeNode lowestCommonAncestorMultiple(TreeNode root, Set<TreeNode> nodes) {
        if (root == null || nodes.contains(root)) {
            return root;
        }
        
        TreeNode left = lowestCommonAncestorMultiple(root.left, nodes);
        TreeNode right = lowestCommonAncestorMultiple(root.right, nodes);
        
        if (left != null && right != null) {
            return root;
        }
        
        return left != null ? left : right;
    }
    
    /**
     * Distance between two nodes in binary tree.
     * Pattern: dist(p, q) = dist(root, p) + dist(root, q) - 2 * dist(root, LCA)
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int distanceBetweenNodes(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode lca = lowestCommonAncestor(root, p, q);
        int distP = findDistance(lca, p, 0);
        int distQ = findDistance(lca, q, 0);
        return distP + distQ;
    }
    
    private static int findDistance(TreeNode root, TreeNode target, int dist) {
        if (root == null) return -1;
        if (root == target) return dist;
        
        int left = findDistance(root.left, target, dist + 1);
        if (left != -1) return left;
        
        return findDistance(root.right, target, dist + 1);
    }
    
    // ============================================================================
    // BINARY LIFTING FOR LCA (ADVANCED)
    // ============================================================================
    
    /**
     * Binary Lifting for LCA queries in O(log n) time.
     * Preprocessing: O(n log n), Query: O(log n).
     * 
     * Use case: Multiple LCA queries on same tree.
     * Companies: Google (advanced rounds)
     */
    public static class BinaryLiftingLCA {
        private static final int MAX_LOG = 20; // log2(1000000) ~ 20
        private int[][] parent; // parent[i][j] = 2^j-th parent of node i
        private int[] depth;
        private int n;
        
        @Complexity(time = "O(n log n)", space = "O(n log n)")
        public BinaryLiftingLCA(TreeNode root, int nodeCount) {
            this.n = nodeCount;
            this.parent = new int[n][MAX_LOG];
            this.depth = new int[n];
            
            // Initialize with -1
            for (int[] row : parent) Arrays.fill(row, -1);
            
            // DFS to build parent and depth arrays
            dfs(root, -1, 0);
            
            // Build binary lifting table
            for (int j = 1; j < MAX_LOG; j++) {
                for (int i = 0; i < n; i++) {
                    if (parent[i][j - 1] != -1) {
                        parent[i][j] = parent[parent[i][j - 1]][j - 1];
                    }
                }
            }
        }
        
        private void dfs(TreeNode node, int par, int d) {
            if (node == null) return;
            
            depth[node.val] = d;
            parent[node.val][0] = par;
            
            dfs(node.left, node.val, d + 1);
            dfs(node.right, node.val, d + 1);
        }
        
        @Complexity(time = "O(log n)", space = "O(1)")
        public int lca(int u, int v) {
            // Make u deeper node
            if (depth[u] < depth[v]) {
                int temp = u; u = v; v = temp;
            }
            
            // Bring u to same level as v
            int diff = depth[u] - depth[v];
            for (int i = 0; i < MAX_LOG; i++) {
                if ((diff & (1 << i)) != 0) {
                    u = parent[u][i];
                }
            }
            
            if (u == v) return u;
            
            // Binary search for LCA
            for (int i = MAX_LOG - 1; i >= 0; i--) {
                if (parent[u][i] != parent[v][i]) {
                    u = parent[u][i];
                    v = parent[v][i];
                }
            }
            
            return parent[u][0];
        }
    }
    
    // ============================================================================
    // TREE DIAMETER
    // ============================================================================
    
    /**
     * Diameter of Binary Tree.
     * Diameter = longest path between any two nodes (may not pass through root).
     * 
     * Pattern: At each node, diameter = left_height + right_height.
     * Track global max during height calculation.
     * 
     * Companies: Amazon, Meta, Google
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int diameterOfBinaryTree(TreeNode root) {
        int[] diameter = new int[]{0};
        height(root, diameter);
        return diameter[0];
    }
    
    private static int height(TreeNode root, int[] diameter) {
        if (root == null) return 0;
        
        int leftHeight = height(root.left, diameter);
        int rightHeight = height(root.right, diameter);
        
        // Update diameter (path through current node)
        diameter[0] = Math.max(diameter[0], leftHeight + rightHeight);
        
        return 1 + Math.max(leftHeight, rightHeight);
    }
    
    /**
     * Diameter of N-ary Tree.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int diameterNaryTree(NaryNode root) {
        int[] diameter = new int[]{0};
        heightNary(root, diameter);
        return diameter[0];
    }
    
    private static int heightNary(NaryNode root, int[] diameter) {
        if (root == null) return 0;
        
        // Find two largest heights among children
        int max1 = 0, max2 = 0;
        for (NaryNode child : root.children) {
            int h = heightNary(child, diameter);
            if (h > max1) {
                max2 = max1;
                max1 = h;
            } else if (h > max2) {
                max2 = h;
            }
        }
        
        // Diameter through current node
        diameter[0] = Math.max(diameter[0], max1 + max2);
        
        return 1 + max1;
    }
    
    // ============================================================================
    // MAXIMUM PATH SUM
    // ============================================================================
    
    /**
     * Binary Tree Maximum Path Sum.
     * Path can start and end at any node (not necessarily root-to-leaf).
     * 
     * Pattern: Similar to diameter. At each node:
     * - max_path_through_node = node.val + max(left_sum, 0) + max(right_sum, 0)
     * - return to parent: node.val + max(left_sum, right_sum, 0)
     * 
     * Interview favorite: Meta, Amazon, Google
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int maxPathSum(TreeNode root) {
        if (root == null) return 0;
        int[] maxSum = new int[]{Integer.MIN_VALUE};
        maxPathSumHelper(root, maxSum);
        return maxSum[0];
    }
    
    private static int maxPathSumHelper(TreeNode root, int[] maxSum) {
        if (root == null) return 0;
        
        // Get max sum from left and right (ignore negative sums)
        int leftSum = Math.max(0, maxPathSumHelper(root.left, maxSum));
        int rightSum = Math.max(0, maxPathSumHelper(root.right, maxSum));
        
        // Max path through current node
        int pathThroughNode = root.val + leftSum + rightSum;
        maxSum[0] = Math.max(maxSum[0], pathThroughNode);
        
        // Return max path ending at current node
        return root.val + Math.max(leftSum, rightSum);
    }
    
    /**
     * Maximum Path Sum from any node to any node in path (can go up and down).
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int maxPathSumAnyToAny(TreeNode root) {
        return maxPathSum(root); // Same as above
    }
    
    /**
     * Maximum Path Sum from root to leaf.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int maxPathSumRootToLeaf(TreeNode root) {
        if (root == null) return 0;
        if (root.left == null && root.right == null) return root.val;
        
        int leftMax = (root.left != null) ? maxPathSumRootToLeaf(root.left) : Integer.MIN_VALUE;
        int rightMax = (root.right != null) ? maxPathSumRootToLeaf(root.right) : Integer.MIN_VALUE;
        
        return root.val + Math.max(leftMax, rightMax);
    }
    
    // ============================================================================
    // PATH SUM VARIANTS
    // ============================================================================
    
    /**
     * Path Sum - check if root-to-leaf path with given sum exists.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;
        
        // Leaf node
        if (root.left == null && root.right == null) {
            return root.val == targetSum;
        }
        
        int remaining = targetSum - root.val;
        return hasPathSum(root.left, remaining) || hasPathSum(root.right, remaining);
    }
    
    /**
     * Path Sum II - find all root-to-leaf paths with given sum.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        pathSumHelper(root, targetSum, new ArrayList<>(), result);
        return result;
    }
    
    private static void pathSumHelper(TreeNode root, int remaining, 
                                     List<Integer> path, List<List<Integer>> result) {
        if (root == null) return;
        
        path.add(root.val);
        
        // Leaf node
        if (root.left == null && root.right == null && root.val == remaining) {
            result.add(new ArrayList<>(path));
        }
        
        pathSumHelper(root.left, remaining - root.val, path, result);
        pathSumHelper(root.right, remaining - root.val, path, result);
        
        path.remove(path.size() - 1); // Backtrack
    }
    
    /**
     * Path Sum III - count paths with given sum (can start/end anywhere, must go down).
     * 
     * Pattern: Use prefix sum + HashMap.
     * Companies: Meta, Amazon
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int pathSumIII(TreeNode root, int targetSum) {
        Map<Long, Integer> prefixSum = new HashMap<>();
        prefixSum.put(0L, 1); // Base case: empty path
        return pathSumIIIHelper(root, 0L, targetSum, prefixSum);
    }
    
    private static int pathSumIIIHelper(TreeNode root, long currSum, int targetSum, 
                                       Map<Long, Integer> prefixSum) {
        if (root == null) return 0;
        
        currSum += root.val;
        
        // Number of paths ending at current node
        int count = prefixSum.getOrDefault(currSum - targetSum, 0);
        
        // Add current sum to map
        prefixSum.put(currSum, prefixSum.getOrDefault(currSum, 0) + 1);
        
        // Recurse
        count += pathSumIIIHelper(root.left, currSum, targetSum, prefixSum);
        count += pathSumIIIHelper(root.right, currSum, targetSum, prefixSum);
        
        // Backtrack: remove current sum
        prefixSum.put(currSum, prefixSum.get(currSum) - 1);
        
        return count;
    }
    
    /**
     * Sum Root to Leaf Numbers.
     * E.g., path 1->2->3 represents 123.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int sumNumbers(TreeNode root) {
        return sumNumbersHelper(root, 0);
    }
    
    private static int sumNumbersHelper(TreeNode root, int currentSum) {
        if (root == null) return 0;
        
        currentSum = currentSum * 10 + root.val;
        
        // Leaf node
        if (root.left == null && root.right == null) {
            return currentSum;
        }
        
        return sumNumbersHelper(root.left, currentSum) + 
               sumNumbersHelper(root.right, currentSum);
    }
    
    // ============================================================================
    // ALL ROOT-TO-LEAF PATHS
    // ============================================================================
    
    /**
     * Binary Tree Paths - all root-to-leaf paths as strings.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root != null) {
            binaryTreePathsHelper(root, "", result);
        }
        return result;
    }
    
    private static void binaryTreePathsHelper(TreeNode root, String path, List<String> result) {
        if (root.left == null && root.right == null) {
            result.add(path + root.val);
            return;
        }
        
        String newPath = path + root.val + "->";
        if (root.left != null) binaryTreePathsHelper(root.left, newPath, result);
        if (root.right != null) binaryTreePathsHelper(root.right, newPath, result);
    }
    
    /**
     * All paths from root to leaves as integer lists.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static List<List<Integer>> allRootToLeafPaths(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        allPathsHelper(root, new ArrayList<>(), result);
        return result;
    }
    
    private static void allPathsHelper(TreeNode root, List<Integer> path, 
                                      List<List<Integer>> result) {
        if (root == null) return;
        
        path.add(root.val);
        
        if (root.left == null && root.right == null) {
            result.add(new ArrayList<>(path));
        } else {
            allPathsHelper(root.left, path, result);
            allPathsHelper(root.right, path, result);
        }
        
        path.remove(path.size() - 1);
    }
    
    // ============================================================================
    // DISTANCE AND DEPTH QUERIES
    // ============================================================================
    
    /**
     * All nodes at distance K from target node.
     * Pattern: Convert to graph + BFS or DFS with parent tracking.
     * 
     * Companies: Meta, Amazon
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        // Build parent map
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        buildParentMap(root, null, parentMap);
        
        // BFS from target
        List<Integer> result = new ArrayList<>();
        Set<TreeNode> visited = new HashSet<>();
        Queue<TreeNode> queue = new LinkedList<>();
        
        queue.offer(target);
        visited.add(target);
        int distance = 0;
        
        while (!queue.isEmpty()) {
            if (distance == k) {
                for (TreeNode node : queue) {
                    result.add(node.val);
                }
                break;
            }
            
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                
                // Check children
                if (node.left != null && !visited.contains(node.left)) {
                    queue.offer(node.left);
                    visited.add(node.left);
                }
                if (node.right != null && !visited.contains(node.right)) {
                    queue.offer(node.right);
                    visited.add(node.right);
                }
                
                // Check parent
                TreeNode parent = parentMap.get(node);
                if (parent != null && !visited.contains(parent)) {
                    queue.offer(parent);
                    visited.add(parent);
                }
            }
            
            distance++;
        }
        
        return result;
    }
    
    private static void buildParentMap(TreeNode node, TreeNode parent, 
                                      Map<TreeNode, TreeNode> parentMap) {
        if (node == null) return;
        
        parentMap.put(node, parent);
        buildParentMap(node.left, node, parentMap);
        buildParentMap(node.right, node, parentMap);
    }
    
    /**
     * Maximum depth of binary tree.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }
    
    /**
     * Minimum depth of binary tree (shortest root-to-leaf path).
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int minDepth(TreeNode root) {
        if (root == null) return 0;
        
        // Leaf node
        if (root.left == null && root.right == null) return 1;
        
        // If one child is null, use the other
        if (root.left == null) return 1 + minDepth(root.right);
        if (root.right == null) return 1 + minDepth(root.left);
        
        return 1 + Math.min(minDepth(root.left), minDepth(root.right));
    }
    
    // ============================================================================
    // HELPER CLASSES
    // ============================================================================
    // MORRIS TRAVERSAL - O(1) SPACE TREE TRAVERSAL
    // ============================================================================
    
    /**
     * Morris Traversal - Inorder traversal with O(1) space.
     * 
     * Key idea: Use threaded binary tree concept.
     * - Create temporary links from predecessor to current node
     * - Visit nodes using these links
     * - Remove links after visiting
     * 
     * No recursion, no stack - only O(1) extra space!
     * 
     * Real interview problems:
     * - LC 94: Binary Tree Inorder Traversal (follow-up: O(1) space)
     * - LC 99: Recover Binary Search Tree (O(1) space)
     * 
     * Interview favorites: Google (space optimization questions)
     * 
     * Time: O(n), Space: O(1)
     * Each edge is visited at most twice (once to create thread, once to remove).
     */
    @Complexity(time = "O(n)", space = "O(1)", 
                explanation = "No recursion stack or explicit stack")
    public static List<Integer> morrisInorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode current = root;
        
        while (current != null) {
            if (current.left == null) {
                // No left subtree, visit current and go right
                result.add(current.val);
                current = current.right;
            } else {
                // Find inorder predecessor (rightmost node in left subtree)
                TreeNode predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }
                
                if (predecessor.right == null) {
                    // Create thread: link predecessor to current
                    predecessor.right = current;
                    current = current.left;
                } else {
                    // Thread exists, remove it and visit current
                    predecessor.right = null;
                    result.add(current.val);
                    current = current.right;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Morris Preorder Traversal with O(1) space.
     * 
     * Difference from inorder: Visit node when creating thread, not when removing.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static List<Integer> morrisPreorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode current = root;
        
        while (current != null) {
            if (current.left == null) {
                // No left subtree, visit current and go right
                result.add(current.val);
                current = current.right;
            } else {
                // Find predecessor
                TreeNode predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }
                
                if (predecessor.right == null) {
                    // Create thread and visit current (preorder!)
                    result.add(current.val);
                    predecessor.right = current;
                    current = current.left;
                } else {
                    // Remove thread 
                    predecessor.right = null;
                    current = current.right;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Morris Traversal to find kth smallest in BST with O(1) space.
     * 
     * Interview favorite: Google, Amazon
     * 
     * Note: This implementation completes the full traversal to properly
     * restore the tree structure even after finding the kth element.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static int kthSmallestMorris(TreeNode root, int k) {
        TreeNode current = root;
        int count = 0;
        int result = -1;
        
        while (current != null) {
            if (current.left == null) {
                count++;
                if (count == k) result = current.val;
                current = current.right;
            } else {
                TreeNode predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }
                
                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;
                    count++;
                    if (count == k) result = current.val;
                    current = current.right;
                }
            }
        }
        
        return result; // Returns -1 if k is out of bounds
    }
    
    /**
     * Recover BST with O(1) space using Morris Traversal.
     * Two nodes are swapped by mistake - recover the tree.
     * 
     * Pattern: Inorder should be sorted. Find two violations.
     * 
     * Interview favorite: Google (hard)
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static void recoverBSTMorris(TreeNode root) {
        TreeNode first = null, second = null;
        TreeNode prev = null, current = root;
        
        while (current != null) {
            if (current.left == null) {
                // Check violation
                if (prev != null && prev.val > current.val) {
                    if (first == null) first = prev;
                    second = current;
                }
                prev = current;
                current = current.right;
            } else {
                TreeNode predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }
                
                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;
                    // Check violation
                    if (prev != null && prev.val > current.val) {
                        if (first == null) first = prev;
                        second = current;
                    }
                    prev = current;
                    current = current.right;
                }
            }
        }
        
        // Swap values
        if (first != null && second != null) {
            int temp = first.val;
            first.val = second.val;
            second.val = temp;
        }
    }
    
    /**
     * Convert BST to Greater Sum Tree using Morris Traversal.
     * Replace each node's value with sum of all greater values.
     * 
     * Pattern: Reverse inorder traversal (right-root-left).
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static TreeNode bstToGSTMorris(TreeNode root) {
        int sum = 0;
        TreeNode current = root;
        
        while (current != null) {
            if (current.right == null) {
                // Visit current
                sum += current.val;
                current.val = sum;
                current = current.left;
            } else {
                // Find successor (leftmost in right subtree)
                TreeNode successor = current.right;
                while (successor.left != null && successor.left != current) {
                    successor = successor.left;
                }
                
                if (successor.left == null) {
                    successor.left = current;
                    current = current.right;
                } else {
                    successor.left = null;
                    sum += current.val;
                    current.val = sum;
                    current = current.left;
                }
            }
        }
        
        return root;
    }
    
    /**
     * Flatten Binary Tree to Linked List with O(1) space (Morris-like).
     * 
     * Interview favorite: Amazon, Meta
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static void flattenMorris(TreeNode root) {
        TreeNode current = root;
        
        while (current != null) {
            if (current.left != null) {
                // Find rightmost node in left subtree
                TreeNode rightmost = current.left;
                while (rightmost.right != null) {
                    rightmost = rightmost.right;
                }
                
                // Rewire: rightmost.right = current.right
                rightmost.right = current.right;
                current.right = current.left;
                current.left = null;
            }
            
            current = current.right;
        }
    }
    
    // ============================================================================
    
    public static class TreeNodeWithParent {
        public int val;
        public TreeNodeWithParent left;
        public TreeNodeWithParent right;
        public TreeNodeWithParent parent;
        
        public TreeNodeWithParent(int val) {
            this.val = val;
        }
    }
    
    public static class NaryNode {
        public int val;
        public List<NaryNode> children;
        
        public NaryNode(int val) {
            this.val = val;
            this.children = new ArrayList<>();
        }
    }
}
