package com.cp.algorithms;

import com.cp.datastructures.TreeNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for TreeDPTemplates.
 * Tests all tree DP algorithms with various edge cases.
 */
class TreeDPTemplatesTest {

    // ============================================================================
    // HOUSE ROBBER ON TREE TESTS
    // ============================================================================

    @Test
    @DisplayName("House Robber III - Basic case [3,2,3,null,3,null,1]")
    void testRobBasic() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 2, 3, null, 3, null, 1});
        assertEquals(7, TreeDPTemplates.rob(root)); // Rob 3, 3, 1
    }

    @Test
    @DisplayName("House Robber III - Another case [3,4,5,1,3,null,1]")
    void testRobCase2() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 4, 5, 1, 3, null, 1});
        assertEquals(9, TreeDPTemplates.rob(root)); // Rob 4, 5
    }

    @Test
    @DisplayName("House Robber III - Single node")
    void testRobSingleNode() {
        TreeNode root = new TreeNode(5);
        assertEquals(5, TreeDPTemplates.rob(root));
    }

    @Test
    @DisplayName("House Robber III - Null tree")
    void testRobNull() {
        assertEquals(0, TreeDPTemplates.rob(null));
    }

    @Test
    @DisplayName("House Robber III - Zigzag tree [4,1,null,2,null,3]")
    void testRobZigzag() {
        TreeNode root = TreeNode.fromArray(new Integer[]{4, 1, null, 2, null, 3});
        assertEquals(7, TreeDPTemplates.rob(root)); // Rob 4, 3
    }

    @Test
    @DisplayName("House Robber III - All same values")
    void testRobSameValues() {
        TreeNode root = TreeNode.fromArray(new Integer[]{2, 2, 2, 2, 2});
        assertEquals(6, TreeDPTemplates.rob(root)); // Rob root + two leaves
    }

    // ============================================================================
    // BINARY TREE CAMERAS TESTS
    // ============================================================================

    @Test
    @DisplayName("Binary Tree Cameras - Small tree [0,0,null,0,0]")
    void testMinCameraCoverBasic() {
        TreeNode root = TreeNode.fromArray(new Integer[]{0, 0, null, 0, 0});
        assertEquals(1, TreeDPTemplates.minCameraCover(root));
    }

    @Test
    @DisplayName("Binary Tree Cameras - Single node")
    void testMinCameraCoverSingleNode() {
        TreeNode root = new TreeNode(0);
        assertEquals(1, TreeDPTemplates.minCameraCover(root));
    }

    @Test
    @DisplayName("Binary Tree Cameras - Two nodes")
    void testMinCameraCoverTwoNodes() {
        TreeNode root = TreeNode.fromArray(new Integer[]{0, 0});
        assertEquals(1, TreeDPTemplates.minCameraCover(root));
    }

    @Test
    @DisplayName("Binary Tree Cameras - Complete binary tree depth 2")
    void testMinCameraCoverComplete() {
        TreeNode root = TreeNode.fromArray(new Integer[]{0, 0, 0, 0, 0, 0, 0});
        assertEquals(2, TreeDPTemplates.minCameraCover(root));
    }

    @Test
    @DisplayName("Binary Tree Cameras - Left skewed tree")
    void testMinCameraCoverLeftSkewed() {
        TreeNode root = TreeNode.fromArray(new Integer[]{0, 0, null, 0, null, 0});
        assertEquals(2, TreeDPTemplates.minCameraCover(root));
    }

    // ============================================================================
    // DISTRIBUTE COINS TESTS
    // ============================================================================

    @Test
    @DisplayName("Distribute Coins - Basic case [3,0,0]")
    void testDistributeCoinsBasic() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 0, 0});
        assertEquals(2, TreeDPTemplates.distributeCoins(root));
    }

    @Test
    @DisplayName("Distribute Coins - Case [0,3,0]")
    void testDistributeCoinsCase2() {
        TreeNode root = TreeNode.fromArray(new Integer[]{0, 3, 0});
        assertEquals(3, TreeDPTemplates.distributeCoins(root));
    }

    @Test
    @DisplayName("Distribute Coins - Case [1,0,2]")
    void testDistributeCoinsCase3() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 0, 2});
        assertEquals(2, TreeDPTemplates.distributeCoins(root));
    }

    @Test
    @DisplayName("Distribute Coins - Already balanced")
    void testDistributeCoinsBalanced() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 1, 1});
        assertEquals(0, TreeDPTemplates.distributeCoins(root));
    }

    @Test
    @DisplayName("Distribute Coins - Single node with 1 coin")
    void testDistributeCoinsSingleNode() {
        TreeNode root = new TreeNode(1);
        assertEquals(0, TreeDPTemplates.distributeCoins(root));
    }

    @Test
    @DisplayName("Distribute Coins - Complex case [4,0,0,null,null,0,0]")
    void testDistributeCoinsComplex() {
        TreeNode root = TreeNode.fromArray(new Integer[]{4, 0, 0, null, null, 0, 0});
        assertEquals(6, TreeDPTemplates.distributeCoins(root));
    }

    // ============================================================================
    // LONGEST UNIVALUE PATH TESTS
    // ============================================================================

    @Test
    @DisplayName("Longest Univalue Path - Basic [5,4,5,1,1,5]")
    void testLongestUnivaluePathBasic() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 4, 5, 1, 1, 5});
        assertEquals(2, TreeDPTemplates.longestUnivaluePath(root));
    }

    @Test
    @DisplayName("Longest Univalue Path - All same values")
    void testLongestUnivaluePathAllSame() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 1, 1, 1, 1, 1, 1});
        assertEquals(4, TreeDPTemplates.longestUnivaluePath(root));
    }

    @Test
    @DisplayName("Longest Univalue Path - No same values")
    void testLongestUnivaluePathNoSame() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5});
        assertEquals(0, TreeDPTemplates.longestUnivaluePath(root));
    }

    @Test
    @DisplayName("Longest Univalue Path - Single node")
    void testLongestUnivaluePathSingleNode() {
        TreeNode root = new TreeNode(1);
        assertEquals(0, TreeDPTemplates.longestUnivaluePath(root));
    }

    @Test
    @DisplayName("Longest Univalue Path - Left path only")
    void testLongestUnivaluePathLeftOnly() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 1, 2, 1});
        assertEquals(2, TreeDPTemplates.longestUnivaluePath(root));
    }

    // ============================================================================
    // BINARY TREE COLORING GAME TESTS
    // ============================================================================

    @Test
    @DisplayName("Binary Tree Coloring - Basic case n=11, x=3")
    void testBtreeGameWinningMoveBasic() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
        assertTrue(TreeDPTemplates.btreeGameWinningMove(root, 11, 3));
    }

    @Test
    @DisplayName("Binary Tree Coloring - Player 2 cannot win")
    void testBtreeGameWinningMoveCannotWin() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        assertFalse(TreeDPTemplates.btreeGameWinningMove(root, 3, 1));
    }

    @Test
    @DisplayName("Binary Tree Coloring - Small tree")
    void testBtreeGameWinningMoveSmall() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4});
        // Tree: 1 has left=2, right=3; 2 has left=4
        // If player 1 picks 2, sizes are: left(4)=1, right=0, parent(1,3)=2
        // Max region is 2, which is not > 4/2=2, so player 2 cannot win
        assertFalse(TreeDPTemplates.btreeGameWinningMove(root, 4, 2));
    }

    @Test
    @DisplayName("Binary Tree Coloring - Node not found")
    void testBtreeGameWinningMoveNotFound() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        assertFalse(TreeDPTemplates.btreeGameWinningMove(root, 3, 99));
    }

    // ============================================================================
    // COUNT NODES EQUAL TO SUM OF DESCENDANTS TESTS
    // ============================================================================

    @Test
    @DisplayName("Equal to Descendants - Basic case [10,3,4,2,1]")
    void testEqualToDescendantsBasic() {
        TreeNode root = TreeNode.fromArray(new Integer[]{10, 3, 4, 2, 1});
        assertEquals(2, TreeDPTemplates.equalToDescendants(root)); // 10 and 3
    }

    @Test
    @DisplayName("Equal to Descendants - No matches")
    void testEqualToDescendantsNoMatch() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        assertEquals(0, TreeDPTemplates.equalToDescendants(root));
    }

    @Test
    @DisplayName("Equal to Descendants - Single node")
    void testEqualToDescendantsSingleNode() {
        TreeNode root = new TreeNode(0);
        assertEquals(1, TreeDPTemplates.equalToDescendants(root)); // 0 == 0 (no descendants)
    }

    @Test
    @DisplayName("Equal to Descendants - All zeros")
    void testEqualToDescendantsZeros() {
        TreeNode root = TreeNode.fromArray(new Integer[]{0, 0, 0});
        assertEquals(3, TreeDPTemplates.equalToDescendants(root));
    }

    // ============================================================================
    // MAXIMUM PRODUCT OF SPLITTED TREE TESTS
    // ============================================================================

    @Test
    @DisplayName("Max Product Split - Basic case [1,2,3,4,5,6]")
    void testMaxProductBasic() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5, 6});
        assertEquals(110, TreeDPTemplates.maxProduct(root)); // (1+2+4+5)*(3+6) = 12*9 = 108, or 11*10=110
    }

    @Test
    @DisplayName("Max Product Split - Case [1,null,2,3,4,null,null,5,6]")
    void testMaxProductCase2() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, null, 2, 3, 4, null, null, 5, 6});
        assertEquals(90, TreeDPTemplates.maxProduct(root)); // (1+2+3+5)*(4+6) = 11*10 but check
    }

    @Test
    @DisplayName("Max Product Split - Small tree [1,2,3]")
    void testMaxProductSmall() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        int result = TreeDPTemplates.maxProduct(root);
        assertEquals(9, result); // (1+2)*3 = 9 or (1+3)*2 = 8, max is 9
    }

    // ============================================================================
    // SUM OF DISTANCES IN TREE TESTS
    // ============================================================================

    @Test
    @DisplayName("Sum of Distances - Basic tree n=6")
    void testSumOfDistancesBasic() {
        int[][] edges = {{0, 1}, {0, 2}, {2, 3}, {2, 4}, {2, 5}};
        int[] result = TreeDPTemplates.sumOfDistancesInTree(6, edges);
        assertArrayEquals(new int[]{8, 12, 6, 10, 10, 10}, result);
    }

    @Test
    @DisplayName("Sum of Distances - Single node")
    void testSumOfDistancesSingleNode() {
        int[][] edges = {};
        int[] result = TreeDPTemplates.sumOfDistancesInTree(1, edges);
        assertArrayEquals(new int[]{0}, result);
    }

    @Test
    @DisplayName("Sum of Distances - Linear tree")
    void testSumOfDistancesLinear() {
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}};
        int[] result = TreeDPTemplates.sumOfDistancesInTree(4, edges);
        assertArrayEquals(new int[]{6, 4, 4, 6}, result);
    }

    @Test
    @DisplayName("Sum of Distances - Star tree")
    void testSumOfDistancesStar() {
        int[][] edges = {{0, 1}, {0, 2}, {0, 3}};
        int[] result = TreeDPTemplates.sumOfDistancesInTree(4, edges);
        assertArrayEquals(new int[]{3, 5, 5, 5}, result);
    }

    // ============================================================================
    // MINIMUM HEIGHT TREES TESTS
    // ============================================================================

    @Test
    @DisplayName("Minimum Height Trees - Basic case n=4")
    void testFindMinHeightTreesBasic() {
        int[][] edges = {{1, 0}, {1, 2}, {1, 3}};
        List<Integer> result = TreeDPTemplates.findMinHeightTrees(4, edges);
        assertEquals(List.of(1), result);
    }

    @Test
    @DisplayName("Minimum Height Trees - Two centers")
    void testFindMinHeightTreesTwoCenters() {
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
        List<Integer> result = TreeDPTemplates.findMinHeightTrees(6, edges);
        assertTrue(result.size() == 2);
        assertTrue(result.contains(2) && result.contains(3));
    }

    @Test
    @DisplayName("Minimum Height Trees - Single node")
    void testFindMinHeightTreesSingleNode() {
        List<Integer> result = TreeDPTemplates.findMinHeightTrees(1, new int[][]{});
        assertEquals(List.of(0), result);
    }

    @Test
    @DisplayName("Minimum Height Trees - Two nodes")
    void testFindMinHeightTreesTwoNodes() {
        int[][] edges = {{0, 1}};
        List<Integer> result = TreeDPTemplates.findMinHeightTrees(2, edges);
        assertEquals(2, result.size());
        assertTrue(result.contains(0) && result.contains(1));
    }

    // ============================================================================
    // EVEN ODD TREE TESTS
    // ============================================================================

    @Test
    @DisplayName("Even Odd Tree - Valid tree")
    void testIsEvenOddTreeValid() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 10, 4, 3, null, 7, 9, 12, 8, 6, null, null, 2});
        assertTrue(TreeDPTemplates.isEvenOddTree(root));
    }

    @Test
    @DisplayName("Even Odd Tree - Invalid tree (even at level 0)")
    void testIsEvenOddTreeInvalid1() {
        TreeNode root = TreeNode.fromArray(new Integer[]{2, 10, 4});
        assertFalse(TreeDPTemplates.isEvenOddTree(root));
    }

    @Test
    @DisplayName("Even Odd Tree - Invalid tree (not strictly increasing)")
    void testIsEvenOddTreeInvalid2() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 4, 2, 3, 3, 7});
        assertFalse(TreeDPTemplates.isEvenOddTree(root));
    }

    @Test
    @DisplayName("Even Odd Tree - Single node odd")
    void testIsEvenOddTreeSingleNodeOdd() {
        TreeNode root = new TreeNode(1);
        assertTrue(TreeDPTemplates.isEvenOddTree(root));
    }

    @Test
    @DisplayName("Even Odd Tree - Single node even")
    void testIsEvenOddTreeSingleNodeEven() {
        TreeNode root = new TreeNode(2);
        assertFalse(TreeDPTemplates.isEvenOddTree(root));
    }

    @Test
    @DisplayName("Even Odd Tree - Null tree")
    void testIsEvenOddTreeNull() {
        assertTrue(TreeDPTemplates.isEvenOddTree(null));
    }

    // ============================================================================
    // COUNT GOOD NODES TESTS
    // ============================================================================

    @Test
    @DisplayName("Good Nodes - Basic case [3,1,4,3,null,1,5]")
    void testGoodNodesBasic() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 1, 4, 3, null, 1, 5});
        assertEquals(4, TreeDPTemplates.goodNodes(root)); // 3, 4, 3, 5
    }

    @Test
    @DisplayName("Good Nodes - All good [3,3,null,4,2]")
    void testGoodNodesAllGood() {
        TreeNode root = TreeNode.fromArray(new Integer[]{3, 3, null, 4, 2});
        assertEquals(3, TreeDPTemplates.goodNodes(root)); // 3, 3, 4
    }

    @Test
    @DisplayName("Good Nodes - Single node")
    void testGoodNodesSingleNode() {
        TreeNode root = new TreeNode(1);
        assertEquals(1, TreeDPTemplates.goodNodes(root));
    }

    @Test
    @DisplayName("Good Nodes - Increasing path")
    void testGoodNodesIncreasing() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        assertEquals(3, TreeDPTemplates.goodNodes(root));
    }

    @Test
    @DisplayName("Good Nodes - Decreasing path")
    void testGoodNodesDecreasing() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 3, 1});
        assertEquals(1, TreeDPTemplates.goodNodes(root)); // Only root
    }

    // ============================================================================
    // DELETE NODES AND RETURN FOREST TESTS
    // ============================================================================

    @Test
    @DisplayName("Delete Nodes - Basic case")
    void testDelNodesBasic() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        int[] toDelete = {3, 5};
        List<TreeNode> forest = TreeDPTemplates.delNodes(root, toDelete);
        assertEquals(3, forest.size());
        
        // Verify the forest contains correct roots
        Set<Integer> rootValues = new HashSet<>();
        for (TreeNode node : forest) {
            rootValues.add(node.val);
        }
        assertTrue(rootValues.contains(1));
        assertTrue(rootValues.contains(6));
        assertTrue(rootValues.contains(7));
    }

    @Test
    @DisplayName("Delete Nodes - Delete root only")
    void testDelNodesDeleteRoot() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        int[] toDelete = {1};
        List<TreeNode> forest = TreeDPTemplates.delNodes(root, toDelete);
        assertEquals(2, forest.size());
    }

    @Test
    @DisplayName("Delete Nodes - Delete nothing")
    void testDelNodesDeleteNothing() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        int[] toDelete = {};
        List<TreeNode> forest = TreeDPTemplates.delNodes(root, toDelete);
        assertEquals(1, forest.size());
        assertEquals(1, forest.get(0).val);
    }

    @Test
    @DisplayName("Delete Nodes - Delete all")
    void testDelNodesDeleteAll() {
        TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3});
        int[] toDelete = {1, 2, 3};
        List<TreeNode> forest = TreeDPTemplates.delNodes(root, toDelete);
        assertEquals(0, forest.size());
    }

    // ============================================================================
    // STEP-BY-STEP DIRECTIONS TESTS
    // ============================================================================

    @Test
    @DisplayName("Get Directions - Basic case")
    void testGetDirectionsBasic() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 1, 2, 3, null, 6, 4});
        String result = TreeDPTemplates.getDirections(root, 3, 6);
        assertEquals("UURL", result);
    }

    @Test
    @DisplayName("Get Directions - Same node")
    void testGetDirectionsSameNode() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 1, 2, 3, null, 6, 4});
        String result = TreeDPTemplates.getDirections(root, 3, 3);
        assertEquals("", result);
    }

    @Test
    @DisplayName("Get Directions - Parent to child")
    void testGetDirectionsParentToChild() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 1, 2, 3});
        String result = TreeDPTemplates.getDirections(root, 1, 3);
        assertEquals("L", result);
    }

    @Test
    @DisplayName("Get Directions - Child to parent")
    void testGetDirectionsChildToParent() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 1, 2, 3});
        String result = TreeDPTemplates.getDirections(root, 3, 1);
        assertEquals("U", result);
    }

    @Test
    @DisplayName("Get Directions - Across tree")
    void testGetDirectionsAcross() {
        TreeNode root = TreeNode.fromArray(new Integer[]{5, 1, 2, 3, 4, 6, 7});
        String result = TreeDPTemplates.getDirections(root, 3, 7);
        assertEquals("UURR", result);
    }

    // ============================================================================
    // EDGE CASES AND STRESS TESTS
    // ============================================================================

    @Test
    @DisplayName("Edge Case - Large tree for House Robber")
    void testRobLargeTree() {
        // Build a larger tree
        Integer[] values = new Integer[127];
        for (int i = 0; i < 127; i++) {
            values[i] = i + 1;
        }
        TreeNode root = TreeNode.fromArray(values);
        int result = TreeDPTemplates.rob(root);
        assertTrue(result > 0);
    }

    @Test
    @DisplayName("Edge Case - Deeply unbalanced tree for cameras")
    void testCamerasUnbalanced() {
        TreeNode root = new TreeNode(0);
        TreeNode current = root;
        for (int i = 0; i < 10; i++) {
            current.left = new TreeNode(0);
            current = current.left;
        }
        int cameras = TreeDPTemplates.minCameraCover(root);
        assertEquals(4, cameras); // Should need cameras at positions to cover all
    }

    @Test
    @DisplayName("Edge Case - Negative values in tree")
    void testNegativeValues() {
        TreeNode root = TreeNode.fromArray(new Integer[]{-1, -2, -3});
        int result = TreeDPTemplates.rob(root);
        assertEquals(0, result); // Better to rob nothing when all negative
    }
}
