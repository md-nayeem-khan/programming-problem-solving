package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.cp.algorithms.AVLTreeTemplates.AVLTree;
import com.cp.algorithms.AVLTreeTemplates.DynamicOrderStatistics;

/**
 * Comprehensive test suite for AVLTreeTemplates
 * Tests all functionality including edge cases and stress tests
 */
public class AVLTreeTemplatesTest {

    private AVLTree tree;

    @BeforeEach
    void setUp() {
        tree = new AVLTree();
    }

    // ========== BASIC INSERTION TESTS ==========

    @Test
    @DisplayName("Test single insertion")
    void testSingleInsertion() {
        tree.insert(10);
        assertEquals(1, tree.size());
        assertTrue(tree.search(10));
        assertEquals(1, tree.height());
    }

    @Test
    @DisplayName("Test multiple insertions")
    void testMultipleInsertions() {
        int[] values = {10, 20, 30, 40, 50};
        for (int val : values) {
            tree.insert(val);
        }
        
        assertEquals(5, tree.size());
        for (int val : values) {
            assertTrue(tree.search(val), "Value " + val + " should be in tree");
        }
    }

    @Test
    @DisplayName("Test duplicate insertion")
    void testDuplicateInsertion() {
        tree.insert(10);
        tree.insert(10);
        assertEquals(1, tree.size(), "Duplicate should not increase size");
        assertTrue(tree.search(10));
    }

    @Test
    @DisplayName("Test insertion maintains sorted order")
    void testInsertionMaintainsOrder() {
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int val : values) {
            tree.insert(val);
        }
        
        List<Integer> inorder = tree.inorder();
        assertEquals(Arrays.asList(20, 30, 40, 50, 60, 70, 80), inorder);
    }

    // ========== BALANCE TESTS ==========

    @Test
    @DisplayName("Test tree remains balanced after insertions")
    void testBalanceAfterInsertions() {
        // Insert values that would create unbalanced tree in regular BST
        for (int i = 1; i <= 100; i++) {
            tree.insert(i);
            assertTrue(tree.isBalanced(), "Tree should be balanced after inserting " + i);
        }
    }

    @Test
    @DisplayName("Test LL rotation")
    void testLLRotation() {
        tree.insert(30);
        tree.insert(20);
        tree.insert(10); // Should trigger LL rotation
        
        assertTrue(tree.isBalanced());
        assertEquals(Arrays.asList(10, 20, 30), tree.inorder());
        assertEquals(2, tree.height());
    }

    @Test
    @DisplayName("Test RR rotation")
    void testRRRotation() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(30); // Should trigger RR rotation
        
        assertTrue(tree.isBalanced());
        assertEquals(Arrays.asList(10, 20, 30), tree.inorder());
        assertEquals(2, tree.height());
    }

    @Test
    @DisplayName("Test LR rotation")
    void testLRRotation() {
        tree.insert(30);
        tree.insert(10);
        tree.insert(20); // Should trigger LR rotation
        
        assertTrue(tree.isBalanced());
        assertEquals(Arrays.asList(10, 20, 30), tree.inorder());
        assertEquals(2, tree.height());
    }

    @Test
    @DisplayName("Test RL rotation")
    void testRLRotation() {
        tree.insert(10);
        tree.insert(30);
        tree.insert(20); // Should trigger RL rotation
        
        assertTrue(tree.isBalanced());
        assertEquals(Arrays.asList(10, 20, 30), tree.inorder());
        assertEquals(2, tree.height());
    }

    // ========== DELETION TESTS ==========

    @Test
    @DisplayName("Test delete leaf node")
    void testDeleteLeaf() {
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        
        tree.delete(10);
        assertEquals(2, tree.size());
        assertFalse(tree.search(10));
        assertTrue(tree.isBalanced());
    }

    @Test
    @DisplayName("Test delete node with one child")
    void testDeleteNodeWithOneChild() {
        tree.insert(20);
        tree.insert(10);
        tree.insert(5);
        
        tree.delete(10);
        assertEquals(2, tree.size());
        assertFalse(tree.search(10));
        assertTrue(tree.search(5));
        assertTrue(tree.isBalanced());
    }

    @Test
    @DisplayName("Test delete node with two children")
    void testDeleteNodeWithTwoChildren() {
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        tree.insert(5);
        tree.insert(15);
        
        tree.delete(10);
        assertEquals(4, tree.size());
        assertFalse(tree.search(10));
        assertTrue(tree.search(15));
        assertTrue(tree.isBalanced());
    }

    @Test
    @DisplayName("Test delete root")
    void testDeleteRoot() {
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        
        tree.delete(20);
        assertEquals(2, tree.size());
        assertFalse(tree.search(20));
        assertTrue(tree.isBalanced());
    }

    @Test
    @DisplayName("Test delete non-existent element")
    void testDeleteNonExistent() {
        tree.insert(10);
        tree.insert(20);
        
        tree.delete(30);
        assertEquals(2, tree.size());
        assertTrue(tree.search(10));
        assertTrue(tree.search(20));
    }

    @Test
    @DisplayName("Test balance after deletions")
    void testBalanceAfterDeletions() {
        // Insert 50 elements
        for (int i = 1; i <= 50; i++) {
            tree.insert(i);
        }
        
        // Delete every other element
        for (int i = 2; i <= 50; i += 2) {
            tree.delete(i);
            assertTrue(tree.isBalanced(), "Tree should be balanced after deleting " + i);
        }
        
        assertEquals(25, tree.size());
    }

    // ========== SEARCH TESTS ==========

    @Test
    @DisplayName("Test search in empty tree")
    void testSearchEmptyTree() {
        assertFalse(tree.search(10));
    }

    @Test
    @DisplayName("Test search existing elements")
    void testSearchExisting() {
        int[] values = {15, 10, 20, 5, 12, 17, 25};
        for (int val : values) {
            tree.insert(val);
        }
        
        for (int val : values) {
            assertTrue(tree.search(val), "Should find " + val);
        }
    }

    @Test
    @DisplayName("Test search non-existing elements")
    void testSearchNonExisting() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        
        assertFalse(tree.search(5));
        assertFalse(tree.search(15));
        assertFalse(tree.search(25));
        assertFalse(tree.search(40));
    }

    // ========== ORDER STATISTICS TESTS ==========

    @Test
    @DisplayName("Test kth smallest")
    void testKthSmallest() {
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int val : values) {
            tree.insert(val);
        }
        
        assertEquals(20, tree.kthSmallest(1));
        assertEquals(30, tree.kthSmallest(2));
        assertEquals(40, tree.kthSmallest(3));
        assertEquals(50, tree.kthSmallest(4));
        assertEquals(60, tree.kthSmallest(5));
        assertEquals(70, tree.kthSmallest(6));
        assertEquals(80, tree.kthSmallest(7));
    }

    @Test
    @DisplayName("Test kth largest")
    void testKthLargest() {
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int val : values) {
            tree.insert(val);
        }
        
        assertEquals(80, tree.kthLargest(1));
        assertEquals(70, tree.kthLargest(2));
        assertEquals(60, tree.kthLargest(3));
        assertEquals(50, tree.kthLargest(4));
        assertEquals(40, tree.kthLargest(5));
        assertEquals(30, tree.kthLargest(6));
        assertEquals(20, tree.kthLargest(7));
    }

    @Test
    @DisplayName("Test kth smallest out of bounds")
    void testKthSmallestOutOfBounds() {
        tree.insert(10);
        tree.insert(20);
        
        assertNull(tree.kthSmallest(0));
        assertNull(tree.kthSmallest(3));
        assertNull(tree.kthSmallest(100));
    }

    @Test
    @DisplayName("Test kth smallest in empty tree")
    void testKthSmallestEmpty() {
        assertNull(tree.kthSmallest(1));
    }

    // ========== COUNT QUERIES TESTS ==========

    @Test
    @DisplayName("Test count smaller")
    void testCountSmaller() {
        int[] values = {10, 20, 30, 40, 50};
        for (int val : values) {
            tree.insert(val);
        }
        
        assertEquals(0, tree.countSmaller(10));
        assertEquals(0, tree.countSmaller(5));
        assertEquals(2, tree.countSmaller(25));
        assertEquals(3, tree.countSmaller(35));
        assertEquals(5, tree.countSmaller(60));
    }

    @Test
    @DisplayName("Test count in range")
    void testCountInRange() {
        int[] values = {10, 20, 30, 40, 50, 60, 70};
        for (int val : values) {
            tree.insert(val);
        }
        
        assertEquals(3, tree.countInRange(20, 40));
        assertEquals(5, tree.countInRange(20, 60));
        assertEquals(7, tree.countInRange(10, 70));
        assertEquals(0, tree.countInRange(80, 90));
        assertEquals(1, tree.countInRange(30, 30));
    }

    @Test
    @DisplayName("Test count in range edge cases")
    void testCountInRangeEdgeCases() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        
        assertEquals(3, tree.countInRange(10, 30));
        assertEquals(2, tree.countInRange(10, 20));
        assertEquals(1, tree.countInRange(20, 20));
        assertEquals(0, tree.countInRange(5, 8));
        assertEquals(0, tree.countInRange(40, 50));
    }

    // ========== RANGE QUERIES TESTS ==========

    @Test
    @DisplayName("Test range sum")
    void testRangeSum() {
        int[] values = {10, 20, 30, 40, 50};
        for (int val : values) {
            tree.insert(val);
        }
        
        assertEquals(90, tree.rangeSum(20, 40)); // 20 + 30 + 40
        assertEquals(150, tree.rangeSum(10, 50)); // All elements
        assertEquals(0, tree.rangeSum(60, 70)); // No elements
        assertEquals(30, tree.rangeSum(30, 30)); // Single element
    }

    @Test
    @DisplayName("Test range sum empty tree")
    void testRangeSumEmpty() {
        assertEquals(0, tree.rangeSum(1, 100));
    }

    // ========== SIZE AND HEIGHT TESTS ==========

    @Test
    @DisplayName("Test size tracking")
    void testSizeTracking() {
        assertEquals(0, tree.size());
        
        tree.insert(10);
        assertEquals(1, tree.size());
        
        tree.insert(20);
        tree.insert(30);
        assertEquals(3, tree.size());
        
        tree.delete(20);
        assertEquals(2, tree.size());
        
        tree.delete(10);
        tree.delete(30);
        assertEquals(0, tree.size());
    }

    @Test
    @DisplayName("Test height is logarithmic")
    void testHeightLogarithmic() {
        // Insert 1000 elements
        for (int i = 1; i <= 1000; i++) {
            tree.insert(i);
        }
        
        // Height should be O(log n), for 1000 elements should be around 10-12
        int height = tree.height();
        assertTrue(height <= 15, "Height " + height + " is not logarithmic for 1000 elements");
    }

    // ========== LEETCODE 315 TESTS ==========

    @Test
    @DisplayName("Test LeetCode 315 - Count Smaller Numbers After Self")
    void testCountSmallerAfterSelf() {
        int[] nums = {5, 2, 6, 1};
        List<Integer> result = AVLTreeTemplates.countSmaller(nums);
        assertEquals(Arrays.asList(2, 1, 1, 0), result);
    }

    @Test
    @DisplayName("Test LeetCode 315 - Empty array")
    void testCountSmallerEmpty() {
        int[] nums = {};
        List<Integer> result = AVLTreeTemplates.countSmaller(nums);
        assertEquals(Arrays.asList(), result);
    }

    @Test
    @DisplayName("Test LeetCode 315 - Single element")
    void testCountSmallerSingle() {
        int[] nums = {5};
        List<Integer> result = AVLTreeTemplates.countSmaller(nums);
        assertEquals(Arrays.asList(0), result);
    }

    @Test
    @DisplayName("Test LeetCode 315 - Descending order")
    void testCountSmallerDescending() {
        int[] nums = {5, 4, 3, 2, 1};
        List<Integer> result = AVLTreeTemplates.countSmaller(nums);
        assertEquals(Arrays.asList(4, 3, 2, 1, 0), result);
    }

    @Test
    @DisplayName("Test LeetCode 315 - Ascending order")
    void testCountSmallerAscending() {
        int[] nums = {1, 2, 3, 4, 5};
        List<Integer> result = AVLTreeTemplates.countSmaller(nums);
        assertEquals(Arrays.asList(0, 0, 0, 0, 0), result);
    }

    @Test
    @DisplayName("Test LeetCode 315 - Duplicates")
    void testCountSmallerDuplicates() {
        // Note: AVL tree ignores duplicate values, so when we process [3,3,3,1,1] right to left:
        // Insert 1 -> tree {1}, countSmaller(1) = 0
        // Insert 1 -> duplicate ignored, tree {1}, countSmaller(1) = 0
        // Insert 3 -> tree {1,3}, countSmaller(3) = 1
        // Insert 3 -> duplicate ignored, tree {1,3}, countSmaller(3) = 1
        // Insert 3 -> duplicate ignored, tree {1,3}, countSmaller(3) = 1
        // Result reversed: [1, 1, 1, 0, 0]
        int[] nums = {3, 3, 3, 1, 1};
        List<Integer> result = AVLTreeTemplates.countSmaller(nums);
        assertEquals(Arrays.asList(1, 1, 1, 0, 0), result);
    }

    // ========== DYNAMIC ORDER STATISTICS TESTS ==========

    @Test
    @DisplayName("Test Dynamic Order Statistics")
    void testDynamicOrderStatistics() {
        DynamicOrderStatistics dos = new DynamicOrderStatistics();
        
        dos.insert(5);
        dos.insert(2);
        dos.insert(8);
        dos.insert(1);
        dos.insert(9);
        
        assertEquals(5, dos.size());
        assertEquals(2, dos.kthMin(2));
        assertEquals(8, dos.kthMax(2));
        assertEquals(3, dos.countRange(2, 8));
        
        dos.delete(5);
        assertEquals(4, dos.size());
        assertEquals(2, dos.kthMin(2));
        assertEquals(2, dos.countRange(2, 8));
    }

    // ========== STRESS TESTS ==========

    @Test
    @DisplayName("Stress test - Large insertions")
    void stressTestLargeInsertions() {
        for (int i = 1; i <= 10000; i++) {
            tree.insert(i);
        }
        
        assertEquals(10000, tree.size());
        assertTrue(tree.isBalanced());
        assertTrue(tree.search(5000));
        assertTrue(tree.search(1));
        assertTrue(tree.search(10000));
    }

    @Test
    @DisplayName("Stress test - Random insertions and deletions")
    void stressTestRandomOps() {
        java.util.Random rand = new java.util.Random(42);
        java.util.Set<Integer> expected = new java.util.HashSet<>();
        
        // Random insertions
        for (int i = 0; i < 1000; i++) {
            int val = rand.nextInt(10000);
            tree.insert(val);
            expected.add(val);
        }
        
        assertEquals(expected.size(), tree.size());
        assertTrue(tree.isBalanced());
        
        // Random deletions
        Integer[] values = expected.toArray(new Integer[0]);
        for (int i = 0; i < values.length / 2; i++) {
            tree.delete(values[i]);
        }
        
        assertTrue(tree.isBalanced());
    }

    @Test
    @DisplayName("Stress test - Reverse order insertions")
    void stressTestReverseOrder() {
        for (int i = 1000; i >= 1; i--) {
            tree.insert(i);
        }
        
        assertEquals(1000, tree.size());
        assertTrue(tree.isBalanced());
        
        List<Integer> inorder = tree.inorder();
        for (int i = 0; i < 1000; i++) {
            assertEquals(i + 1, inorder.get(i));
        }
    }

    @Test
    @DisplayName("Stress test - Alternating insertions and deletions")
    void stressTestAlternatingOps() {
        for (int i = 1; i <= 100; i++) {
            tree.insert(i * 2);
        }
        
        for (int i = 1; i <= 50; i++) {
            tree.delete(i * 2);
            tree.insert(i * 2 - 1);
        }
        
        assertTrue(tree.isBalanced());
        assertEquals(100, tree.size());
    }

    // ========== EDGE CASES ==========

    @Test
    @DisplayName("Test operations on empty tree")
    void testEmptyTree() {
        assertEquals(0, tree.size());
        assertEquals(0, tree.height());
        assertTrue(tree.isBalanced());
        assertFalse(tree.search(10));
        assertNull(tree.kthSmallest(1));
        assertEquals(0, tree.countSmaller(10));
        assertEquals(0, tree.countInRange(1, 100));
        assertEquals(0, tree.rangeSum(1, 100));
    }

    @Test
    @DisplayName("Test single node tree")
    void testSingleNodeTree() {
        tree.insert(42);
        
        assertEquals(1, tree.size());
        assertEquals(1, tree.height());
        assertTrue(tree.isBalanced());
        assertTrue(tree.search(42));
        assertEquals(42, tree.kthSmallest(1));
        assertEquals(42, tree.kthLargest(1));
        assertEquals(0, tree.countSmaller(42));
        assertEquals(1, tree.countSmaller(43));
        assertEquals(1, tree.countInRange(42, 42));
        assertEquals(42, tree.rangeSum(42, 42));
    }

    @Test
    @DisplayName("Test negative numbers")
    void testNegativeNumbers() {
        tree.insert(-10);
        tree.insert(-5);
        tree.insert(-20);
        tree.insert(0);
        tree.insert(5);
        
        assertTrue(tree.isBalanced());
        assertEquals(Arrays.asList(-20, -10, -5, 0, 5), tree.inorder());
        assertEquals(-20, tree.kthSmallest(1));
        assertEquals(5, tree.kthLargest(1));
        assertEquals(3, tree.countInRange(-10, 0));
    }

    @Test
    @DisplayName("Test same element inserted multiple times")
    void testMultipleSameElement() {
        tree.insert(10);
        tree.insert(10);
        tree.insert(10);
        
        assertEquals(1, tree.size());
        assertEquals(1, tree.height());
        assertEquals(Arrays.asList(10), tree.inorder());
    }

    // ========== CORRECTNESS VALIDATION TESTS ==========

    @Test
    @DisplayName("Validate BST property is maintained")
    void validateBSTProperty() {
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};
        for (int val : values) {
            tree.insert(val);
        }
        
        List<Integer> inorder = tree.inorder();
        for (int i = 1; i < inorder.size(); i++) {
            assertTrue(inorder.get(i - 1) < inorder.get(i), 
                "BST property violated: " + inorder.get(i - 1) + " >= " + inorder.get(i));
        }
    }

    @Test
    @DisplayName("Validate height calculation is correct")
    void validateHeightCalculation() {
        tree.insert(50);
        assertEquals(1, tree.height());
        
        tree.insert(30);
        tree.insert(70);
        assertEquals(2, tree.height());
        
        tree.insert(20);
        tree.insert(40);
        assertEquals(3, tree.height());
    }

    @Test
    @DisplayName("Validate size calculation after complex operations")
    void validateSizeCalculation() {
        java.util.Set<Integer> elements = new java.util.HashSet<>();
        
        // Add 100 unique elements
        for (int i = 1; i <= 100; i++) {
            tree.insert(i);
            elements.add(i);
        }
        assertEquals(elements.size(), tree.size());
        
        // Delete 30 elements
        for (int i = 1; i <= 30; i++) {
            tree.delete(i);
            elements.remove(i);
        }
        assertEquals(elements.size(), tree.size());
        
        // Add back 15 elements
        for (int i = 1; i <= 15; i++) {
            tree.insert(i);
            elements.add(i);
        }
        assertEquals(elements.size(), tree.size());
    }

    @Test
    @DisplayName("Validate inorder traversal after rotations")
    void validateInorderAfterRotations() {
        // This will trigger multiple rotations
        int[] values = {50, 25, 75, 10, 30, 60, 80, 5, 15, 27, 35};
        for (int val : values) {
            tree.insert(val);
        }
        
        List<Integer> inorder = tree.inorder();
        int[] expected = {5, 10, 15, 25, 27, 30, 35, 50, 60, 75, 80};
        
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], inorder.get(i));
        }
    }
}
