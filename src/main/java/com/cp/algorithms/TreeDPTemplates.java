package com.cp.algorithms;

import com.cp.datastructures.TreeNode;
import com.cp.problems.Complexity;
import java.util.*;

/**
 * Tree Dynamic Programming Templates for coding interviews.
 * 
 * Tree DP Pattern: Define state for each subtree, combine child states.
 * Often uses post-order traversal (process children before parent).
 * 
 * Coverage:
 * - House Robber III (rob houses in tree)
 * - Binary Tree Cameras
 * - Distribute Coins in Binary Tree
 * - Longest Univalue Path
 * - Binary Tree Coloring Game
 * - Count Nodes Equal to Sum of Descendants
 * - Maximum Product of Splitted Tree
 * - Number of Ways to Reorder Array
 * 
 * Critical for: Amazon, Meta, Google (hard problems)
 * Frequency: ~10% of hard tree problems
 */
public class TreeDPTemplates {

    // ============================================================================
    // HOUSE ROBBER ON TREE
    // ============================================================================
    
    /**
     * House Robber III - maximize money, can't rob adjacent nodes.
     * 
     * DP State: For each node, compute [rob_this_node, dont_rob_this_node]
     * - Rob this node: rob_val + don't_rob(left) + don't_rob(right)
     * - Don't rob: max(left) + max(right)
     * 
     * Interview favorite: Meta, Amazon
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int rob(TreeNode root) {
        int[] result = robHelper(root);
        return Math.max(result[0], result[1]);
    }
    
    // Returns [rob_this, dont_rob_this]
    private static int[] robHelper(TreeNode node) {
        if (node == null) return new int[]{0, 0};
        
        int[] left = robHelper(node.left);
        int[] right = robHelper(node.right);
        
        // Rob this node: can't rob children
        int robThis = node.val + left[1] + right[1];
        
        // Don't rob this node: take max from children
        int dontRobThis = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        return new int[]{robThis, dontRobThis};
    }
    
    // ============================================================================
    // BINARY TREE CAMERAS
    // ============================================================================
    
    /**
     * Binary Tree Cameras - minimum cameras to monitor all nodes.
     * Camera at node covers itself, parent, and children.
     * 
     * DP State: 3 states per node
     * 0 = not covered
     * 1 = covered but no camera
     * 2 = has camera
     * 
     * Interview favorite: Google, Meta (hard)
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int minCameraCover(TreeNode root) {
        int[] cameras = new int[]{0};
        int state = cameraHelper(root, cameras);
        
        // If root not covered, need camera at root
        if (state == 0) cameras[0]++;
        
        return cameras[0];
    }
    
    // Returns: 0=not covered, 1=covered no camera, 2=has camera
    private static int cameraHelper(TreeNode node, int[] cameras) {
        if (node == null) return 1; // null is considered covered
        
        int left = cameraHelper(node.left, cameras);
        int right = cameraHelper(node.right, cameras);
        
        // If either child not covered, must place camera here
        if (left == 0 || right == 0) {
            cameras[0]++;
            return 2;
        }
        
        // If either child has camera, this node is covered
        if (left == 2 || right == 2) {
            return 1;
        }
        
        // Both children covered but no camera, this node not covered
        return 0;
    }
    
    // ============================================================================
    // DISTRIBUTE COINS IN BINARY TREE
    // ============================================================================
    
    /**
     * Distribute Coins - each node has coins, move to make each node have exactly 1.
     * Return minimum moves (moving 1 coin over 1 edge = 1 move).
     * 
     * DP State: For each subtree, compute excess/deficit coins.
     * Moves for subtree = |excess|
     * 
     * Interview favorite: Amazon, Google
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int distributeCoins(TreeNode root) {
        int[] moves = new int[]{0};
        coinsHelper(root, moves);
        return moves[0];
    }
    
    // Returns excess coins in subtree (can be negative = deficit)
    private static int coinsHelper(TreeNode node, int[] moves) {
        if (node == null) return 0;
        
        int leftExcess = coinsHelper(node.left, moves);
        int rightExcess = coinsHelper(node.right, moves);
        
        // Moves = sum of absolute values of excess from children
        moves[0] += Math.abs(leftExcess) + Math.abs(rightExcess);
        
        // This subtree's excess = coins here + left + right - 1 (keep 1 for this node)
        return node.val + leftExcess + rightExcess - 1;
    }
    
    // ============================================================================
    // LONGEST UNIVALUE PATH
    // ============================================================================
    
    /**
     * Longest Univalue Path - longest path where all nodes have same value.
     * Path doesn't need to pass through root.
     * 
     * DP State: For each node, track longest univalue path through it.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int longestUnivaluePath(TreeNode root) {
        int[] maxLen = new int[]{0};
        univalueHelper(root, maxLen);
        return maxLen[0];
    }
    
    // Returns longest univalue path starting from this node going down
    private static int univalueHelper(TreeNode node, int[] maxLen) {
        if (node == null) return 0;
        
        int left = univalueHelper(node.left, maxLen);
        int right = univalueHelper(node.right, maxLen);
        
        int leftPath = 0, rightPath = 0;
        
        // If left child has same value, extend path
        if (node.left != null && node.left.val == node.val) {
            leftPath = left + 1;
        }
        
        // If right child has same value, extend path
        if (node.right != null && node.right.val == node.val) {
            rightPath = right + 1;
        }
        
        // Update global max (path through current node)
        maxLen[0] = Math.max(maxLen[0], leftPath + rightPath);
        
        // Return longer path for parent
        return Math.max(leftPath, rightPath);
    }
    
    // ============================================================================
    // BINARY TREE COLORING GAME
    // ============================================================================
    
    /**
     * Binary Tree Coloring Game - two players color nodes in tree.
     * Player 1 colors node x first. Can player 2 win?
     * 
     * Player 2 wins if they can claim > n/2 nodes.
     * Best strategy: color parent, left child, or right child of x.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static boolean btreeGameWinningMove(TreeNode root, int n, int x) {
        TreeNode xNode = findNode(root, x);
        if (xNode == null) return false;
        
        int leftSize = countNodes(xNode.left);
        int rightSize = countNodes(xNode.right);
        int parentSize = n - leftSize - rightSize - 1;
        
        // Player 2 wins if any of these regions has > n/2 nodes
        int maxRegion = Math.max(parentSize, Math.max(leftSize, rightSize));
        return maxRegion > n / 2;
    }
    
    private static TreeNode findNode(TreeNode root, int x) {
        if (root == null || root.val == x) return root;
        TreeNode left = findNode(root.left, x);
        return left != null ? left : findNode(root.right, x);
    }
    
    private static int countNodes(TreeNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }
    
    // ============================================================================
    // COUNT NODES EQUAL TO SUM OF DESCENDANTS
    // ============================================================================
    
    /**
     * Count nodes where node value equals sum of all descendant values.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int equalToDescendants(TreeNode root) {
        int[] count = new int[]{0};
        descendantSumHelper(root, count);
        return count[0];
    }
    
    // Returns sum of subtree rooted at node
    private static int descendantSumHelper(TreeNode node, int[] count) {
        if (node == null) return 0;
        
        int leftSum = descendantSumHelper(node.left, count);
        int rightSum = descendantSumHelper(node.right, count);
        
        if (node.val == leftSum + rightSum) {
            count[0]++;
        }
        
        return node.val + leftSum + rightSum;
    }
    
    // ============================================================================
    // MAXIMUM PRODUCT OF SPLITTED BINARY TREE
    // ============================================================================
    
    /**
     * Maximum Product of Splitted Binary Tree.
     * Remove one edge to split into two subtrees, maximize product of sums.
     * 
     * DP: Calculate total sum, then for each subtree find product.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int maxProduct(TreeNode root) {
        long totalSum = treeSum(root);
        long[] maxProd = new long[]{0};
        productHelper(root, totalSum, maxProd);
        return (int) (maxProd[0] % 1000000007);
    }
    
    private static long treeSum(TreeNode node) {
        if (node == null) return 0;
        return node.val + treeSum(node.left) + treeSum(node.right);
    }
    
    // Returns sum of subtree
    private static long productHelper(TreeNode node, long totalSum, long[] maxProd) {
        if (node == null) return 0;
        
        long leftSum = productHelper(node.left, totalSum, maxProd);
        long rightSum = productHelper(node.right, totalSum, maxProd);
        long subtreeSum = node.val + leftSum + rightSum;
        
        // If we cut edge above this subtree
        long product = subtreeSum * (totalSum - subtreeSum);
        maxProd[0] = Math.max(maxProd[0], product);
        
        return subtreeSum;
    }
    
    // ============================================================================
    // SUM OF DISTANCES IN TREE
    // ============================================================================
    
    /**
     * Sum of Distances in Tree - for each node, sum of distances to all other nodes.
     * 
     * Two-pass DP:
     * 1. Calculate answer for root
     * 2. Re-root technique to calculate for all nodes
     * 
     * Interview favorite: Google (hard)
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static int[] sumOfDistancesInTree(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        int[] count = new int[n]; // count[i] = nodes in subtree rooted at i
        int[] answer = new int[n]; // answer[i] = sum of distances from i
        
        Arrays.fill(count, 1);
        
        // First DFS: calculate count and answer for root (node 0)
        dfs1(0, -1, graph, count, answer);
        
        // Second DFS: re-root to calculate answer for all nodes
        dfs2(0, -1, n, graph, count, answer);
        
        return answer;
    }
    
    private static void dfs1(int node, int parent, List<List<Integer>> graph,
                            int[] count, int[] answer) {
        for (int child : graph.get(node)) {
            if (child == parent) continue;
            
            dfs1(child, node, graph, count, answer);
            count[node] += count[child];
            answer[node] += answer[child] + count[child];
        }
    }
    
    private static void dfs2(int node, int parent, int n, List<List<Integer>> graph,
                            int[] count, int[] answer) {
        for (int child : graph.get(node)) {
            if (child == parent) continue;
            
            // Re-root from node to child
            // answer[child] = answer[node] - count[child] + (n - count[child])
            answer[child] = answer[node] - count[child] + (n - count[child]);
            
            dfs2(child, node, n, graph, count, answer);
        }
    }
    
    // ============================================================================
    // MINIMUM HEIGHT TREES
    // ============================================================================
    
    /**
     * Minimum Height Trees - find all root nodes that give minimum height.
     * 
     * Pattern: Topological sort from leaves (like peeling onion).
     * Answer is the center(s) of the tree.
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (n == 1) return Collections.singletonList(0);
        
        List<Set<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new HashSet<>());
        }
        
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        // Start with leaves (nodes with degree 1)
        List<Integer> leaves = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (graph.get(i).size() == 1) {
                leaves.add(i);
            }
        }
        
        // Remove leaves layer by layer
        int remaining = n;
        while (remaining > 2) {
            remaining -= leaves.size();
            List<Integer> newLeaves = new ArrayList<>();
            
            for (int leaf : leaves) {
                int neighbor = graph.get(leaf).iterator().next();
                graph.get(neighbor).remove(leaf);
                
                if (graph.get(neighbor).size() == 1) {
                    newLeaves.add(neighbor);
                }
            }
            
            leaves = newLeaves;
        }
        
        return leaves;
    }
    
    // ============================================================================
    // EVEN ODD TREE
    // ============================================================================
    
    /**
     * Check if binary tree is Even-Odd tree.
     * Even levels: strictly increasing odd values
     * Odd levels: strictly decreasing even values
     */
    @Complexity(time = "O(n)", space = "O(w) where w=max width")
    public static boolean isEvenOddTree(TreeNode root) {
        if (root == null) return true;
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            int prev = (level % 2 == 0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                
                // Check value parity matches level
                if (level % 2 == 0) {
                    // Even level: odd values, strictly increasing
                    if (node.val % 2 == 0 || node.val <= prev) {
                        return false;
                    }
                } else {
                    // Odd level: even values, strictly decreasing
                    if (node.val % 2 == 1 || node.val >= prev) {
                        return false;
                    }
                }
                
                prev = node.val;
                
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            
            level++;
        }
        
        return true;
    }
    
    // ============================================================================
    // COUNT GOOD NODES IN BINARY TREE
    // ============================================================================
    
    /**
     * Count Good Nodes - node is good if no node on path from root has greater value.
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static int goodNodes(TreeNode root) {
        return goodNodesHelper(root, Integer.MIN_VALUE);
    }
    
    private static int goodNodesHelper(TreeNode node, int maxSoFar) {
        if (node == null) return 0;
        
        int count = 0;
        if (node.val >= maxSoFar) {
            count = 1;
            maxSoFar = node.val;
        }
        
        count += goodNodesHelper(node.left, maxSoFar);
        count += goodNodesHelper(node.right, maxSoFar);
        
        return count;
    }
    
    // ============================================================================
    // DELETE NODES AND RETURN FOREST
    // ============================================================================
    
    /**
     * Delete nodes and return forest (list of tree roots after deletions).
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        Set<Integer> toDelete = new HashSet<>();
        for (int val : to_delete) {
            toDelete.add(val);
        }
        
        List<TreeNode> forest = new ArrayList<>();
        if (deleteHelper(root, toDelete, forest) != null) {
            forest.add(root);
        }
        
        return forest;
    }
    
    // Returns null if node is deleted, otherwise returns node
    private static TreeNode deleteHelper(TreeNode node, Set<Integer> toDelete, 
                                        List<TreeNode> forest) {
        if (node == null) return null;
        
        node.left = deleteHelper(node.left, toDelete, forest);
        node.right = deleteHelper(node.right, toDelete, forest);
        
        if (toDelete.contains(node.val)) {
            // Add children to forest if they exist
            if (node.left != null) forest.add(node.left);
            if (node.right != null) forest.add(node.right);
            return null; // Delete this node
        }
        
        return node;
    }
    
    // ============================================================================
    // STEP-BY-STEP DIRECTIONS FROM NODE TO NODE
    // ============================================================================
    
    /**
     * Find shortest path between two nodes as direction string (L/R/U).
     */
    @Complexity(time = "O(n)", space = "O(h)")
    public static String getDirections(TreeNode root, int startValue, int destValue) {
        StringBuilder pathToStart = new StringBuilder();
        StringBuilder pathToDest = new StringBuilder();
        
        findPath(root, startValue, pathToStart);
        findPath(root, destValue, pathToDest);
        
        // Remove common prefix (path to LCA)
        int i = 0;
        while (i < pathToStart.length() && i < pathToDest.length() &&
               pathToStart.charAt(i) == pathToDest.charAt(i)) {
            i++;
        }
        
        // Go up from start to LCA, then down to dest
        StringBuilder result = new StringBuilder();
        for (int j = i; j < pathToStart.length(); j++) {
            result.append('U');
        }
        for (int j = i; j < pathToDest.length(); j++) {
            result.append(pathToDest.charAt(j));
        }
        
        return result.toString();
    }
    
    private static boolean findPath(TreeNode node, int target, StringBuilder path) {
        if (node == null) return false;
        if (node.val == target) return true;
        
        path.append('L');
        if (findPath(node.left, target, path)) return true;
        path.deleteCharAt(path.length() - 1);
        
        path.append('R');
        if (findPath(node.right, target, path)) return true;
        path.deleteCharAt(path.length() - 1);
        
        return false;
    }
}
