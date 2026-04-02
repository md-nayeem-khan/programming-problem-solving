package com.cp.algorithms;

import com.cp.datastructures.TreeNode;
import com.cp.algorithms.TreeTemplates.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Comprehensive test suite for TreeTemplates.
 * Tests all tree algorithms for correctness, edge cases, and boundary conditions.
 */
public class TreeTemplatesTest {

    // ============================================================================
    // LCA TESTS
    // ============================================================================
    
    @Test
    @DisplayName("LCA: Basic case - nodes in different subtrees")
    public void testLowestCommonAncestor_BasicCase() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});
        TreeNode p = findNode(root, 5);
        TreeNode q = findNode(root, 1);
        TreeNode lca = TreeTemplates.lowestCommonAncestor(root, p, q);
        assertEquals(3, lca.val);
    }
    
    @Test
    @DisplayName("LCA: Node is ancestor of another")
    public void testLowestCommonAncestor_NodeIsAncestor() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});
        TreeNode p = findNode(root, 5);
        TreeNode q = findNode(root, 4);
        TreeNode lca = TreeTemplates.lowestCommonAncestor(root, p, q);
        assertEquals(5, lca.val);
    }
    
    @Test
    @DisplayName("LCA: Both nodes are same")
    public void testLowestCommonAncestor_SameNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        TreeNode p = findNode(root, 2);
        TreeNode lca = TreeTemplates.lowestCommonAncestor(root, p, p);
        assertEquals(2, lca.val);
    }
    
    @Test
    @DisplayName("LCA: Root is one of the nodes")
    public void testLowestCommonAncestor_RootNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        TreeNode q = findNode(root, 3);
        TreeNode lca = TreeTemplates.lowestCommonAncestor(root, root, q);
        assertEquals(1, lca.val);
    }
    
    @Test
    @DisplayName("LCA: Multiple nodes")
    public void testLowestCommonAncestor_Multiple() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});
        Set<TreeNode> nodes = new HashSet<>();
        nodes.add(findNode(root, 7));
        nodes.add(findNode(root, 4));
        nodes.add(findNode(root, 6));
        TreeNode lca = TreeTemplates.lowestCommonAncestorMultiple(root, nodes);
        assertEquals(5, lca.val);
    }
    
    @Test
    @DisplayName("LCA: With parent pointers")
    public void testLowestCommonAncestorWithParent() {
        // Build tree with parent pointers
        TreeNodeWithParent root = new TreeNodeWithParent(3);
        TreeNodeWithParent left = new TreeNodeWithParent(5);
        TreeNodeWithParent right = new TreeNodeWithParent(1);
        TreeNodeWithParent ll = new TreeNodeWithParent(6);
        TreeNodeWithParent lr = new TreeNodeWithParent(2);
        
        root.left = left;
        root.right = right;
        left.parent = root;
        right.parent = root;
        left.left = ll;
        left.right = lr;
        ll.parent = left;
        lr.parent = left;
        
        TreeNodeWithParent lca = TreeTemplates.lowestCommonAncestorWithParent(ll, lr);
        assertEquals(5, lca.val);
    }
    
    // ============================================================================
    // DISTANCE TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Distance: Between nodes in different subtrees")
    public void testDistanceBetweenNodes() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});
        TreeNode p = findNode(root, 6);
        TreeNode q = findNode(root, 4);
        int distance = TreeTemplates.distanceBetweenNodes(root, p, q);
        // Path: 6 -> 5 -> 2 -> 4 (distance = 3)
        assertEquals(3, distance);
    }
    
    @Test
    @DisplayName("Distance: Same node")
    public void testDistanceBetweenNodes_SameNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        TreeNode p = findNode(root, 2);
        int distance = TreeTemplates.distanceBetweenNodes(root, p, p);
        assertEquals(0, distance);
    }
    
    @Test
    @DisplayName("Distance: Parent-child relationship")
    public void testDistanceBetweenNodes_ParentChild() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        TreeNode p = root;
        TreeNode q = findNode(root, 2);
        int distance = TreeTemplates.distanceBetweenNodes(root, p, q);
        assertEquals(1, distance);
    }
    
    // ============================================================================
    // BINARY LIFTING LCA TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Binary Lifting: Multiple LCA queries")
    public void testBinaryLiftingLCA() {
        // Build a tree with node values 0-6
        TreeNode root = TreeNode.fromArray(new Integer[]{0, 1, 2, 3, 4, 5, 6});
        BinaryLiftingLCA binaryLifting = new BinaryLiftingLCA(root, 7);
        
        assertEquals(0, binaryLifting.lca(1, 2));
        assertEquals(1, binaryLifting.lca(3, 4));
        assertEquals(0, binaryLifting.lca(3, 6));
    }
    
    // ============================================================================
    // DIAMETER TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Diameter: Basic binary tree")
    public void testDiameterOfBinaryTree() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5});
        int diameter = TreeTemplates.diameterOfBinaryTree(root);
        assertEquals(3, diameter);
    }
    
    @Test
    @DisplayName("Diameter: Single node")
    public void testDiameterOfBinaryTree_SingleNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1});
        int diameter = TreeTemplates.diameterOfBinaryTree(root);
        assertEquals(0, diameter);
    }
    
    @Test
    @DisplayName("Diameter: Skewed tree")
    public void testDiameterOfBinaryTree_SkewedTree() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, null, 3, null, 4});
        int diameter = TreeTemplates.diameterOfBinaryTree(root);
        assertEquals(3, diameter);
    }
    
    @Test
    @DisplayName("Diameter: N-ary tree")
    public void testDiameterNaryTree() {
        NaryNode root = new NaryNode(1);
        NaryNode child1 = new NaryNode(2);
        NaryNode child2 = new NaryNode(3);
        NaryNode child3 = new NaryNode(4);
        
        root.children.add(child1);
        root.children.add(child2);
        root.children.add(child3);
        
        NaryNode grandchild1 = new NaryNode(5);
        NaryNode grandchild2 = new NaryNode(6);
        child1.children.add(grandchild1);
        child1.children.add(grandchild2);
        
        int diameter = TreeTemplates.diameterNaryTree(root);
        assertEquals(3, diameter);
    }
    
    // ============================================================================
    // MAX PATH SUM TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Max Path Sum: Basic case")
    public void testMaxPathSum() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        int maxSum = TreeTemplates.maxPathSum(root);
        assertEquals(6, maxSum);
    }
    
    @Test
    @DisplayName("Max Path Sum: With negative values")
    public void testMaxPathSum_NegativeValues() {
        TreeNode root = TreeNode.fromArray(new Integer[]{-10, 9, 20, null, null, 15, 7});
        int maxSum = TreeTemplates.maxPathSum(root);
        assertEquals(42, maxSum);
    }
    
    @Test
    @DisplayName("Max Path Sum: All negative")
    public void testMaxPathSum_AllNegative() {
        TreeNode root = TreeNode.fromArray(new Integer[]{-3, -2, -1});
        int maxSum = TreeTemplates.maxPathSum(root);
        assertEquals(-1, maxSum);
    }
    
    @Test
    @DisplayName("Max Path Sum: Single node")
    public void testMaxPathSum_SingleNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5});
        int maxSum = TreeTemplates.maxPathSum(root);
        assertEquals(5, maxSum);
    }
    
    @Test
    @DisplayName("Max Path Sum: Root to leaf")
    public void testMaxPathSumRootToLeaf() {
        TreeNode root = TreeNode.fromArray(new Integer[]{-10, 9, 20, null, null, 15, 7});
        int maxSum = TreeTemplates.maxPathSumRootToLeaf(root);
        assertEquals(25, maxSum);
    }
    
    @Test
    @DisplayName("Max Path Sum: Root to leaf single path")
    public void testMaxPathSumRootToLeaf_SinglePath() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 4, null, 11, null, 2});
        int maxSum = TreeTemplates.maxPathSumRootToLeaf(root);
        assertEquals(22, maxSum);
    }
    
    // ============================================================================
    // PATH SUM TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Has Path Sum: True case")
    public void testHasPathSum_True() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1});
        assertTrue(TreeTemplates.hasPathSum(root, 22));
    }
    
    @Test
    @DisplayName("Has Path Sum: False case")
    public void testHasPathSum_False() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        assertFalse(TreeTemplates.hasPathSum(root, 5));
    }
    
    @Test
    @DisplayName("Has Path Sum: Empty tree")
    public void testHasPathSum_EmptyTree() {
        assertFalse(TreeTemplates.hasPathSum(null, 0));
    }
    
    @Test
    @DisplayName("Has Path Sum: Single node matching")
    public void testHasPathSum_SingleNodeMatch() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1});
        assertTrue(TreeTemplates.hasPathSum(root, 1));
    }
    
    @Test
    @DisplayName("Path Sum II: Find all paths")
    public void testPathSum() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 4, 8, 11, null, 13, 4, 7, 2, null, null, 5, 1});
        List<List<Integer>> result = TreeTemplates.pathSum(root, 22);
        assertEquals(2, result.size());
        assertTrue(result.contains(Arrays.asList(5, 4, 11, 2)));
        assertTrue(result.contains(Arrays.asList(5, 8, 4, 5)));
    }
    
    @Test
    @DisplayName("Path Sum II: No paths")
    public void testPathSum_NoPaths() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        List<List<Integer>> result = TreeTemplates.pathSum(root, 10);
        assertEquals(0, result.size());
    }
    
    @Test
    @DisplayName("Path Sum III: Count paths")
    public void testPathSumIII() {
        TreeNode root = TreeNode.fromArray(new Integer[]{10, 5, -3, 3, 2, null, 11, 3, -2, null, 1});
        int count = TreeTemplates.pathSumIII(root, 8);
        assertEquals(3, count);
    }
    
    @Test
    @DisplayName("Path Sum III: Single path")
    public void testPathSumIII_SinglePath() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, null, 2, null, 3, null, 4, null, 5});
        int count = TreeTemplates.pathSumIII(root, 3);
        assertEquals(2, count);
    }
    
    @Test
    @DisplayName("Path Sum III: Negative values")
    public void testPathSumIII_NegativeValues() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, -2, -3, 1, 3, -2, null, -1});
        int count = TreeTemplates.pathSumIII(root, -1);
        assertTrue(count > 0);
    }
    
    // ============================================================================
    // SUM NUMBERS TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Sum Numbers: Basic case")
    public void testSumNumbers() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        int sum = TreeTemplates.sumNumbers(root);
        assertEquals(25, sum); // 12 + 13
    }
    
    @Test
    @DisplayName("Sum Numbers: Single path")
    public void testSumNumbers_SinglePath() {
        TreeNode root = TreeNode.fromArray(new Integer[]{4, 9, 0, 5, 1});
        int sum = TreeTemplates.sumNumbers(root);
        assertEquals(1026, sum); // 495 + 491 + 40
    }
    
    @Test
    @DisplayName("Sum Numbers: Single node")
    public void testSumNumbers_SingleNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5});
        int sum = TreeTemplates.sumNumbers(root);
        assertEquals(5, sum);
    }
    
    // ============================================================================
    // BINARY TREE PATHS TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Binary Tree Paths: Basic case")
    public void testBinaryTreePaths() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, null, 5});
        List<String> paths = TreeTemplates.binaryTreePaths(root);
        assertEquals(2, paths.size());
        assertTrue(paths.contains("1->2->5"));
        assertTrue(paths.contains("1->3"));
    }
    
    @Test
    @DisplayName("Binary Tree Paths: Single node")
    public void testBinaryTreePaths_SingleNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1});
        List<String> paths = TreeTemplates.binaryTreePaths(root);
        assertEquals(1, paths.size());
        assertEquals("1", paths.get(0));
    }
    
    @Test
    @DisplayName("All Root to Leaf Paths")
    public void testAllRootToLeafPaths() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, null, 5});
        List<List<Integer>> paths = TreeTemplates.allRootToLeafPaths(root);
        assertEquals(2, paths.size());
        assertTrue(paths.contains(Arrays.asList(1, 2, 5)));
        assertTrue(paths.contains(Arrays.asList(1, 3)));
    }
    
    // ============================================================================
    // DISTANCE K TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Distance K: Basic case")
    public void testDistanceK() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});
        TreeNode target = findNode(root, 5);
        List<Integer> result = TreeTemplates.distanceK(root, target, 2);
        assertEquals(3, result.size());
        assertTrue(result.contains(7));
        assertTrue(result.contains(4));
        assertTrue(result.contains(1));
    }
    
    @Test
    @DisplayName("Distance K: Distance 0")
    public void testDistanceK_DistanceZero() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        List<Integer> result = TreeTemplates.distanceK(root, root, 0);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
    }
    
    @Test
    @DisplayName("Distance K: No nodes at distance")
    public void testDistanceK_NoNodes() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1});
        List<Integer> result = TreeTemplates.distanceK(root, root, 1);
        assertEquals(0, result.size());
    }
    
    // ============================================================================
    // DEPTH TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Max Depth: Basic tree")
    public void testMaxDepth() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 9, 20, null, null, 15, 7});
        int depth = TreeTemplates.maxDepth(root);
        assertEquals(3, depth);
    }
    
    @Test
    @DisplayName("Max Depth: Single node")
    public void testMaxDepth_SingleNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1});
        int depth = TreeTemplates.maxDepth(root);
        assertEquals(1, depth);
    }
    
    @Test
    @DisplayName("Max Depth: Empty tree")
    public void testMaxDepth_EmptyTree() {
        int depth = TreeTemplates.maxDepth(null);
        assertEquals(0, depth);
    }
    
    @Test
    @DisplayName("Min Depth: Basic tree")
    public void testMinDepth() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 9, 20, null, null, 15, 7});
        int depth = TreeTemplates.minDepth(root);
        assertEquals(2, depth);
    }
    
    @Test
    @DisplayName("Min Depth: Skewed tree")
    public void testMinDepth_SkewedTree() {
        TreeNode root = TreeNode.fromArray(new Integer[]{2, null, 3, null, 4, null, 5, null, 6});
        int depth = TreeTemplates.minDepth(root);
        assertEquals(5, depth);
    }
    
    @Test
    @DisplayName("Min Depth: Single node")
    public void testMinDepth_SingleNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1});
        int depth = TreeTemplates.minDepth(root);
        assertEquals(1, depth);
    }
    
    // ============================================================================
    // MORRIS TRAVERSAL TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Morris Inorder: Basic BST")
    public void testMorrisInorderTraversal() {
        TreeNode root = TreeNode.fromArray(new Integer[]{4, 2, 6, 1, 3, 5, 7});
        List<Integer> result = TreeTemplates.morrisInorderTraversal(root);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7), result);
    }
    
    @Test
    @DisplayName("Morris Inorder: Single node")
    public void testMorrisInorderTraversal_SingleNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1});
        List<Integer> result = TreeTemplates.morrisInorderTraversal(root);
        assertEquals(Arrays.asList(1), result);
    }
    
    @Test
    @DisplayName("Morris Inorder: Skewed left")
    public void testMorrisInorderTraversal_SkewedLeft() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 2, null, 1});
        List<Integer> result = TreeTemplates.morrisInorderTraversal(root);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }
    
    @Test
    @DisplayName("Morris Preorder: Basic tree")
    public void testMorrisPreorderTraversal() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        List<Integer> result = TreeTemplates.morrisPreorderTraversal(root);
        assertEquals(Arrays.asList(1, 2, 4, 5, 3, 6, 7), result);
    }
    
    @Test
    @DisplayName("Morris Preorder: Single node")
    public void testMorrisPreorderTraversal_SingleNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1});
        List<Integer> result = TreeTemplates.morrisPreorderTraversal(root);
        assertEquals(Arrays.asList(1), result);
    }
    
    @Test
    @DisplayName("Kth Smallest Morris: Basic BST")
    public void testKthSmallestMorris() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 3, 6, 2, 4, null, null, 1});
        assertEquals(1, TreeTemplates.kthSmallestMorris(root, 1));
        assertEquals(2, TreeTemplates.kthSmallestMorris(root, 2));
        assertEquals(3, TreeTemplates.kthSmallestMorris(root, 3));
        assertEquals(4, TreeTemplates.kthSmallestMorris(root, 4));
        assertEquals(5, TreeTemplates.kthSmallestMorris(root, 5));
    }
    
    @Test
    @DisplayName("Kth Smallest Morris: Out of bounds")
    public void testKthSmallestMorris_OutOfBounds() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 1, 4, null, 2});
        assertEquals(-1, TreeTemplates.kthSmallestMorris(root, 10));
    }
    
    // ============================================================================
    // MORRIS BST OPERATIONS TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Recover BST Morris: Two swapped nodes")
    public void testRecoverBSTMorris() {
        // Create BST: [1, 3, null, null, 2] (3 and 2 are swapped)
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(1);
        root.right = new TreeNode(2);
        
        TreeTemplates.recoverBSTMorris(root);
        
        // After recovery should be: [2, 1, 3]
        assertEquals(2, root.val);
        assertEquals(1, root.left.val);
        assertEquals(3, root.right.val);
    }
    
    @Test
    @DisplayName("Recover BST Morris: Adjacent swapped nodes")
    public void testRecoverBSTMorris_Adjacent() {
        // Create BST with adjacent swapped nodes
        TreeNode root = new TreeNode(2);
        root.left = new TreeNode(3);
        root.right = new TreeNode(1);
        
        TreeTemplates.recoverBSTMorris(root);
        
        // Verify tree is recovered
        List<Integer> inorder = TreeTemplates.morrisInorderTraversal(root);
        for (int i = 1; i < inorder.size(); i++) {
            assertTrue(inorder.get(i) > inorder.get(i - 1), "BST property violated");
        }
    }
    
    @Test
    @DisplayName("BST to GST Morris")
    public void testBstToGSTMorris() {
        // BST: [4, 1, 6, 0, 2, 5, 7, null, null, null, 3, null, null, null, 8]
        TreeNode root = TreeNode.fromArray(new Integer[]{4, 1, 6, 0, 2, 5, 7, null, null, null, 3, null, null, null, 8});
        TreeNode result = TreeTemplates.bstToGSTMorris(root);
        
        // Verify root value (should be sum of all nodes >= 4)
        // Original BST: 0, 1, 2, 3, 4, 5, 6, 7, 8
        // GST at 4: 4+5+6+7+8 = 30
        assertNotNull(result);
        assertEquals(30, result.val);
    }
    
    @Test
    @DisplayName("BST to GST Morris: Simple tree")
    public void testBstToGSTMorris_Simple() {
        TreeNode root = TreeNode.fromArray(new Integer[]{2, 1, 3});
        TreeNode result = TreeTemplates.bstToGSTMorris(root);
        
        assertNotNull(result);
        assertEquals(5, result.val);  // 2 + 3
        assertEquals(6, result.left.val);  // 1 + 2 + 3
        assertEquals(3, result.right.val);  // 3
    }
    
    // ============================================================================
    // FLATTEN TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Flatten Morris: Basic tree")
    public void testFlattenMorris() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 5, 3, 4, null, 6});
        TreeTemplates.flattenMorris(root);
        
        // Verify flattened structure (should be 1->2->3->4->5->6)
        List<Integer> result = new ArrayList<>();
        TreeNode curr = root;
        while (curr != null) {
            result.add(curr.val);
            assertNull(curr.left, "Left child should be null after flattening");
            curr = curr.right;
        }
        
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), result);
    }
    
    @Test
    @DisplayName("Flatten Morris: Single node")
    public void testFlattenMorris_SingleNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1});
        TreeTemplates.flattenMorris(root);
        
        assertEquals(1, root.val);
        assertNull(root.left);
        assertNull(root.right);
    }
    
    @Test
    @DisplayName("Flatten Morris: Skewed tree")
    public void testFlattenMorris_SkewedTree() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, null, 2, null, 3});
        TreeTemplates.flattenMorris(root);
        
        assertEquals(1, root.val);
        assertNull(root.left);
        assertEquals(2, root.right.val);
        assertEquals(3, root.right.right.val);
    }
    
    // ============================================================================
    // EDGE CASES AND STRESS TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Edge Case: Empty tree operations")
    public void testEmptyTree() {
        assertNull(TreeTemplates.lowestCommonAncestor(null, null, null));
        assertEquals(0, TreeTemplates.diameterOfBinaryTree(null));
        assertEquals(0, TreeTemplates.maxDepth(null));
        assertEquals(0, TreeTemplates.minDepth(null));
        assertEquals(0, TreeTemplates.sumNumbers(null));
        assertTrue(TreeTemplates.binaryTreePaths(null).isEmpty());
        assertTrue(TreeTemplates.allRootToLeafPaths(null).isEmpty());
    }
    
    @Test
    @DisplayName("Edge Case: Large balanced tree diameter")
    public void testLargeBalancedTree() {
        // Create a perfect binary tree of depth 4
        TreeNode root = TreeNode.fromArray(new Integer[]{
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15
        });
        
        int diameter = TreeTemplates.diameterOfBinaryTree(root);
        assertTrue(diameter > 0);
        assertEquals(4, TreeTemplates.maxDepth(root));
    }
    
    @Test
    @DisplayName("Stress Test: Deep skewed tree")
    public void testDeepSkewedTree() {
        // Build a deep left-skewed tree
        TreeNode root = new TreeNode(1);
        TreeNode current = root;
        for (int i = 2; i <= 100; i++) {
            current.left = new TreeNode(i);
            current = current.left;
        }
        
        assertEquals(100, TreeTemplates.maxDepth(root));
        assertEquals(100, TreeTemplates.minDepth(root));
        assertEquals(99, TreeTemplates.diameterOfBinaryTree(root));
    }
    
    @Test
    @DisplayName("Correctness: Morris vs Regular Inorder")
    public void testMorrisInorderCorrectness() {
        // Create random tree
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 3, 8, 1, 4, 7, 9, null, 2, null, null, 6});
        
        List<Integer> morris = TreeTemplates.morrisInorderTraversal(root);
        List<Integer> regular = regularInorderTraversal(root);
        
        assertEquals(regular, morris, "Morris traversal should match regular inorder");
    }
    
    @Test
    @DisplayName("Correctness: Morris vs Regular Preorder")
    public void testMorrisPreorderCorrectness() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 3, 8, 1, 4, 7, 9, null, 2, null, null, 6});
        
        List<Integer> morris = TreeTemplates.morrisPreorderTraversal(root);
        List<Integer> regular = regularPreorderTraversal(root);
        
        assertEquals(regular, morris, "Morris preorder should match regular preorder");
    }
    
    // ============================================================================
    // HELPER METHODS
    // ============================================================================
    
    private TreeNode findNode(TreeNode root, int val) {
        if (root == null) return null;
        if (root.val == val) return root;
        TreeNode left = findNode(root.left, val);
        if (left != null) return left;
        return findNode(root.right, val);
    }
    
    private List<Integer> regularInorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }
    
    private void inorderHelper(TreeNode root, List<Integer> result) {
        if (root == null) return;
        inorderHelper(root.left, result);
        result.add(root.val);
        inorderHelper(root.right, result);
    }
    
    private List<Integer> regularPreorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }
    
    private void preorderHelper(TreeNode root, List<Integer> result) {
        if (root == null) return;
        result.add(root.val);
        preorderHelper(root.left, result);
        preorderHelper(root.right, result);
    }
}
