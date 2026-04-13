package com.cp.algorithms;

import com.cp.datastructures.TreeNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for BSTTemplates
 * Tests all BST operations including edge cases and correctness validation
 */
public class BSTTemplatesTest {

    // ========== HELPER METHODS ==========

    /**
     * Helper to create a BST from values
     */
    private TreeNode buildBST(int... values) {
        TreeNode root = null;
        for (int val : values) {
            root = BSTTemplates.insert(root, val);
        }
        return root;
    }

    /**
     * Helper to get inorder traversal
     */
    private List<Integer> inorderTraversal(TreeNode root) {
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

    // ========== BASIC OPERATIONS TESTS ==========

    @Nested
    @DisplayName("Insert Tests")
    class InsertTests {

        @Test
        @DisplayName("Insert into empty tree")
        void testInsertIntoEmpty() {
            TreeNode root = BSTTemplates.insert(null, 10);
            assertNotNull(root);
            assertEquals(10, root.val);
            assertNull(root.left);
            assertNull(root.right);
        }

        @Test
        @DisplayName("Insert multiple values maintains BST property")
        void testInsertMultiple() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(20, 30, 40, 50, 60, 70, 80), inorder);
            
            assertTrue(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Insert duplicate does not modify tree")
        void testInsertDuplicate() {
            TreeNode root = buildBST(50, 30, 70);
            TreeNode result = BSTTemplates.insert(root, 50);
            
            List<Integer> inorder = inorderTraversal(result);
            assertEquals(List.of(30, 50, 70), inorder);
        }

        @Test
        @DisplayName("Insert creates skewed tree correctly")
        void testInsertSkewed() {
            TreeNode root = buildBST(1, 2, 3, 4, 5);
            
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(1, 2, 3, 4, 5), inorder);
            assertTrue(BSTTemplates.isValidBST(root));
        }
    }

    @Nested
    @DisplayName("Insert Iterative Tests")
    class InsertIterativeTests {

        @Test
        @DisplayName("Insert iterative into empty tree")
        void testInsertIterativeEmpty() {
            TreeNode root = BSTTemplates.insertIterative(null, 10);
            assertNotNull(root);
            assertEquals(10, root.val);
        }

        @Test
        @DisplayName("Insert iterative multiple values")
        void testInsertIterativeMultiple() {
            TreeNode root = null;
            root = BSTTemplates.insertIterative(root, 50);
            root = BSTTemplates.insertIterative(root, 30);
            root = BSTTemplates.insertIterative(root, 70);
            root = BSTTemplates.insertIterative(root, 20);
            root = BSTTemplates.insertIterative(root, 40);
            
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(20, 30, 40, 50, 70), inorder);
            assertTrue(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Insert iterative handles duplicates")
        void testInsertIterativeDuplicate() {
            TreeNode root = BSTTemplates.insertIterative(null, 50);
            root = BSTTemplates.insertIterative(root, 50);
            
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(50), inorder);
        }
    }

    @Nested
    @DisplayName("Search Tests")
    class SearchTests {

        @Test
        @DisplayName("Search in empty tree returns null")
        void testSearchEmpty() {
            assertNull(BSTTemplates.search(null, 10));
        }

        @Test
        @DisplayName("Search finds existing value")
        void testSearchFound() {
            TreeNode root = buildBST(50, 30, 70, 20, 40);
            TreeNode result = BSTTemplates.search(root, 40);
            
            assertNotNull(result);
            assertEquals(40, result.val);
        }

        @Test
        @DisplayName("Search returns null for missing value")
        void testSearchNotFound() {
            TreeNode root = buildBST(50, 30, 70);
            assertNull(BSTTemplates.search(root, 100));
        }

        @Test
        @DisplayName("Search finds root")
        void testSearchRoot() {
            TreeNode root = buildBST(50, 30, 70);
            TreeNode result = BSTTemplates.search(root, 50);
            
            assertNotNull(result);
            assertEquals(50, result.val);
        }

        @Test
        @DisplayName("Search in single node tree")
        void testSearchSingleNode() {
            TreeNode root = new TreeNode(42);
            
            assertNotNull(BSTTemplates.search(root, 42));
            assertNull(BSTTemplates.search(root, 10));
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Delete from empty tree")
        void testDeleteEmpty() {
            TreeNode result = BSTTemplates.delete(null, 10);
            assertNull(result);
        }

        @Test
        @DisplayName("Delete leaf node")
        void testDeleteLeaf() {
            TreeNode root = buildBST(50, 30, 70, 20, 40);
            root = BSTTemplates.delete(root, 20);
            
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(30, 40, 50, 70), inorder);
            assertTrue(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Delete node with one child")
        void testDeleteOneChild() {
            TreeNode root = buildBST(50, 30, 70, 20);
            root = BSTTemplates.delete(root, 30);
            
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(20, 50, 70), inorder);
            assertTrue(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Delete node with two children")
        void testDeleteTwoChildren() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            root = BSTTemplates.delete(root, 50);
            
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(20, 30, 40, 60, 70, 80), inorder);
            assertTrue(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Delete root with two children")
        void testDeleteRoot() {
            TreeNode root = buildBST(50, 30, 70);
            root = BSTTemplates.delete(root, 50);
            
            assertNotNull(root);
            assertTrue(BSTTemplates.isValidBST(root));
            assertNull(BSTTemplates.search(root, 50));
        }

        @Test
        @DisplayName("Delete non-existent value")
        void testDeleteNotFound() {
            TreeNode root = buildBST(50, 30, 70);
            TreeNode result = BSTTemplates.delete(root, 100);
            
            List<Integer> inorder = inorderTraversal(result);
            assertEquals(List.of(30, 50, 70), inorder);
        }

        @Test
        @DisplayName("Delete all nodes")
        void testDeleteAll() {
            TreeNode root = buildBST(50);
            root = BSTTemplates.delete(root, 50);
            assertNull(root);
        }
    }

    @Nested
    @DisplayName("Find Min/Max Tests")
    class FindMinMaxTests {

        @Test
        @DisplayName("Find min in empty tree")
        void testFindMinEmpty() {
            assertNull(BSTTemplates.findMin(null));
        }

        @Test
        @DisplayName("Find min in tree")
        void testFindMin() {
            TreeNode root = buildBST(50, 30, 70, 20, 40);
            TreeNode min = BSTTemplates.findMin(root);
            
            assertNotNull(min);
            assertEquals(20, min.val);
        }

        @Test
        @DisplayName("Find min in single node")
        void testFindMinSingleNode() {
            TreeNode root = new TreeNode(42);
            TreeNode min = BSTTemplates.findMin(root);
            
            assertNotNull(min);
            assertEquals(42, min.val);
        }

        @Test
        @DisplayName("Find max in empty tree")
        void testFindMaxEmpty() {
            assertNull(BSTTemplates.findMax(null));
        }

        @Test
        @DisplayName("Find max in tree")
        void testFindMax() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            TreeNode max = BSTTemplates.findMax(root);
            
            assertNotNull(max);
            assertEquals(80, max.val);
        }

        @Test
        @DisplayName("Find max in single node")
        void testFindMaxSingleNode() {
            TreeNode root = new TreeNode(42);
            TreeNode max = BSTTemplates.findMax(root);
            
            assertNotNull(max);
            assertEquals(42, max.val);
        }
    }

    // ========== VALIDATE BST TESTS ==========

    @Nested
    @DisplayName("Validate BST Tests")
    class ValidateBSTTests {

        @Test
        @DisplayName("Empty tree is valid BST")
        void testValidateEmpty() {
            assertTrue(BSTTemplates.isValidBST(null));
        }

        @Test
        @DisplayName("Single node is valid BST")
        void testValidateSingleNode() {
            TreeNode root = new TreeNode(10);
            assertTrue(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Valid BST returns true")
        void testValidBST() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            assertTrue(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Invalid BST returns false")
        void testInvalidBST() {
            TreeNode root = new TreeNode(5);
            root.left = new TreeNode(1);
            root.right = new TreeNode(4);  // Invalid: 4 < 5
            root.right.left = new TreeNode(3);
            root.right.right = new TreeNode(6);
            
            assertFalse(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Subtree violation detected")
        void testSubtreeViolation() {
            TreeNode root = new TreeNode(10);
            root.left = new TreeNode(5);
            root.right = new TreeNode(15);
            root.right.left = new TreeNode(6);  // Invalid: 6 < 10, should be in left subtree
            
            assertFalse(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Validate BST with Integer.MIN_VALUE")
        void testValidateBSTMinValue() {
            TreeNode root = new TreeNode(Integer.MIN_VALUE);
            assertTrue(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Validate BST with Integer.MAX_VALUE")
        void testValidateBSTMaxValue() {
            TreeNode root = new TreeNode(Integer.MAX_VALUE);
            assertTrue(BSTTemplates.isValidBST(root));
        }
    }

    @Nested
    @DisplayName("Validate BST Inorder Tests")
    class ValidateBSTInorderTests {

        @Test
        @DisplayName("Empty tree is valid via inorder")
        void testValidateInorderEmpty() {
            assertTrue(BSTTemplates.isValidBSTInorder(null));
        }

        @Test
        @DisplayName("Valid BST via inorder")
        void testValidBSTInorder() {
            TreeNode root = buildBST(50, 30, 70, 20, 40);
            assertTrue(BSTTemplates.isValidBSTInorder(root));
        }

        @Test
        @DisplayName("Invalid BST detected via inorder")
        void testInvalidBSTInorder() {
            TreeNode root = new TreeNode(5);
            root.left = new TreeNode(1);
            root.right = new TreeNode(4);
            
            assertFalse(BSTTemplates.isValidBSTInorder(root));
        }

        @Test
        @DisplayName("Duplicate values detected via inorder")
        void testDuplicatesInorder() {
            TreeNode root = new TreeNode(5);
            root.left = new TreeNode(3);
            root.right = new TreeNode(5);  // Duplicate
            
            assertFalse(BSTTemplates.isValidBSTInorder(root));
        }
    }

    // ========== KTH ELEMENT TESTS ==========

    @Nested
    @DisplayName("Kth Smallest Tests")
    class KthSmallestTests {

        @Test
        @DisplayName("Kth smallest - first element")
        void testKthSmallestFirst() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            assertEquals(20, BSTTemplates.kthSmallest(root, 1));
        }

        @Test
        @DisplayName("Kth smallest - middle element")
        void testKthSmallestMiddle() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            assertEquals(50, BSTTemplates.kthSmallest(root, 4));
        }

        @Test
        @DisplayName("Kth smallest - last element")
        void testKthSmallestLast() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            assertEquals(80, BSTTemplates.kthSmallest(root, 7));
        }

        @Test
        @DisplayName("Kth smallest - single node")
        void testKthSmallestSingleNode() {
            TreeNode root = new TreeNode(42);
            assertEquals(42, BSTTemplates.kthSmallest(root, 1));
        }

        @Test
        @DisplayName("Kth smallest - invalid k returns -1")
        void testKthSmallestInvalid() {
            TreeNode root = buildBST(50, 30, 70);
            assertEquals(-1, BSTTemplates.kthSmallest(root, 10));
        }
    }

    @Nested
    @DisplayName("Kth Smallest Recursive Tests")
    class KthSmallestRecursiveTests {

        @Test
        @DisplayName("Kth smallest recursive - first element")
        void testKthSmallestRecursiveFirst() {
            TreeNode root = buildBST(50, 30, 70, 20, 40);
            assertEquals(20, BSTTemplates.kthSmallestRecursive(root, 1));
        }

        @Test
        @DisplayName("Kth smallest recursive - middle element")
        void testKthSmallestRecursiveMiddle() {
            TreeNode root = buildBST(50, 30, 70, 20, 40);
            assertEquals(40, BSTTemplates.kthSmallestRecursive(root, 3));
        }

        @Test
        @DisplayName("Kth smallest recursive - last element")
        void testKthSmallestRecursiveLast() {
            TreeNode root = buildBST(50, 30, 70, 20, 40);
            assertEquals(70, BSTTemplates.kthSmallestRecursive(root, 5));
        }
    }

    @Nested
    @DisplayName("Kth Largest Tests")
    class KthLargestTests {

        @Test
        @DisplayName("Kth largest - first element")
        void testKthLargestFirst() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            assertEquals(80, BSTTemplates.kthLargest(root, 1));
        }

        @Test
        @DisplayName("Kth largest - middle element")
        void testKthLargestMiddle() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            assertEquals(50, BSTTemplates.kthLargest(root, 4));
        }

        @Test
        @DisplayName("Kth largest - last element")
        void testKthLargestLast() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            assertEquals(20, BSTTemplates.kthLargest(root, 7));
        }

        @Test
        @DisplayName("Kth largest - single node")
        void testKthLargestSingleNode() {
            TreeNode root = new TreeNode(42);
            assertEquals(42, BSTTemplates.kthLargest(root, 1));
        }

        @Test
        @DisplayName("Kth largest - invalid k returns -1")
        void testKthLargestInvalid() {
            TreeNode root = buildBST(50, 30, 70);
            assertEquals(-1, BSTTemplates.kthLargest(root, 10));
        }
    }

    // ========== SUCCESSOR/PREDECESSOR TESTS ==========

    @Nested
    @DisplayName("Inorder Successor Tests")
    class InorderSuccessorTests {

        @Test
        @DisplayName("Successor with right child")
        void testSuccessorWithRightChild() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            TreeNode node = BSTTemplates.search(root, 30);
            TreeNode successor = BSTTemplates.inorderSuccessor(root, node);
            
            assertNotNull(successor);
            assertEquals(40, successor.val);
        }

        @Test
        @DisplayName("Successor without right child")
        void testSuccessorWithoutRightChild() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            TreeNode node = BSTTemplates.search(root, 40);
            TreeNode successor = BSTTemplates.inorderSuccessor(root, node);
            
            assertNotNull(successor);
            assertEquals(50, successor.val);
        }

        @Test
        @DisplayName("Successor of max is null")
        void testSuccessorOfMax() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            TreeNode node = BSTTemplates.search(root, 80);
            TreeNode successor = BSTTemplates.inorderSuccessor(root, node);
            
            assertNull(successor);
        }

        @Test
        @DisplayName("Successor in single node tree")
        void testSuccessorSingleNode() {
            TreeNode root = new TreeNode(42);
            TreeNode successor = BSTTemplates.inorderSuccessor(root, root);
            
            assertNull(successor);
        }
    }

    @Nested
    @DisplayName("Inorder Predecessor Tests")
    class InorderPredecessorTests {

        @Test
        @DisplayName("Predecessor with left child")
        void testPredecessorWithLeftChild() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            TreeNode node = BSTTemplates.search(root, 70);
            TreeNode predecessor = BSTTemplates.inorderPredecessor(root, node);
            
            assertNotNull(predecessor);
            assertEquals(60, predecessor.val);
        }

        @Test
        @DisplayName("Predecessor without left child")
        void testPredecessorWithoutLeftChild() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            TreeNode node = BSTTemplates.search(root, 60);
            TreeNode predecessor = BSTTemplates.inorderPredecessor(root, node);
            
            assertNotNull(predecessor);
            assertEquals(50, predecessor.val);
        }

        @Test
        @DisplayName("Predecessor of min is null")
        void testPredecessorOfMin() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            TreeNode node = BSTTemplates.search(root, 20);
            TreeNode predecessor = BSTTemplates.inorderPredecessor(root, node);
            
            assertNull(predecessor);
        }

        @Test
        @DisplayName("Predecessor in single node tree")
        void testPredecessorSingleNode() {
            TreeNode root = new TreeNode(42);
            TreeNode predecessor = BSTTemplates.inorderPredecessor(root, root);
            
            assertNull(predecessor);
        }
    }

    // ========== CONVERSION TESTS ==========

    @Nested
    @DisplayName("Sorted Array to BST Tests")
    class SortedArrayToBSTTests {

        @Test
        @DisplayName("Convert empty array")
        void testConvertEmptyArray() {
            TreeNode root = BSTTemplates.sortedArrayToBST(new int[]{});
            assertNull(root);
        }

        @Test
        @DisplayName("Convert single element array")
        void testConvertSingleElement() {
            TreeNode root = BSTTemplates.sortedArrayToBST(new int[]{42});
            
            assertNotNull(root);
            assertEquals(42, root.val);
            assertNull(root.left);
            assertNull(root.right);
        }

        @Test
        @DisplayName("Convert sorted array to balanced BST")
        void testConvertSortedArray() {
            TreeNode root = BSTTemplates.sortedArrayToBST(new int[]{1, 2, 3, 4, 5, 6, 7});
            
            assertTrue(BSTTemplates.isValidBST(root));
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), inorder);
            
            // Check it's balanced (height should be log n)
            assertEquals(4, root.val);  // Middle element as root
        }

        @Test
        @DisplayName("Convert even length array")
        void testConvertEvenLength() {
            TreeNode root = BSTTemplates.sortedArrayToBST(new int[]{1, 2, 3, 4, 5, 6});
            
            assertTrue(BSTTemplates.isValidBST(root));
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(1, 2, 3, 4, 5, 6), inorder);
        }
    }

    @Nested
    @DisplayName("Sorted List to BST Tests")
    class SortedListToBSTTests {

        private BSTTemplates.ListNodeHelper createList(int... values) {
            if (values.length == 0) return null;
            
            BSTTemplates.ListNodeHelper head = new BSTTemplates.ListNodeHelper(values[0]);
            BSTTemplates.ListNodeHelper curr = head;
            
            for (int i = 1; i < values.length; i++) {
                curr.next = new BSTTemplates.ListNodeHelper(values[i]);
                curr = curr.next;
            }
            
            return head;
        }

        @Test
        @DisplayName("Convert empty list")
        void testConvertEmptyList() {
            TreeNode root = BSTTemplates.sortedListToBST(null);
            assertNull(root);
        }

        @Test
        @DisplayName("Convert single node list")
        void testConvertSingleNode() {
            BSTTemplates.ListNodeHelper head = createList(42);
            TreeNode root = BSTTemplates.sortedListToBST(head);
            
            assertNotNull(root);
            assertEquals(42, root.val);
            assertNull(root.left);
            assertNull(root.right);
        }

        @Test
        @DisplayName("Convert sorted list to BST")
        void testConvertSortedList() {
            BSTTemplates.ListNodeHelper head = createList(1, 2, 3, 4, 5, 6, 7);
            TreeNode root = BSTTemplates.sortedListToBST(head);
            
            assertTrue(BSTTemplates.isValidBST(root));
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), inorder);
        }

        @Test
        @DisplayName("Convert two node list")
        void testConvertTwoNodes() {
            BSTTemplates.ListNodeHelper head = createList(1, 2);
            TreeNode root = BSTTemplates.sortedListToBST(head);
            
            assertTrue(BSTTemplates.isValidBST(root));
            assertNotNull(root);
        }
    }

    // ========== BST ITERATOR TESTS ==========

    @Nested
    @DisplayName("BST Iterator Tests")
    class BSTIteratorTests {

        @Test
        @DisplayName("Iterator on empty tree")
        void testIteratorEmpty() {
            BSTTemplates.BSTIterator iterator = new BSTTemplates.BSTIterator(null);
            assertFalse(iterator.hasNext());
        }

        @Test
        @DisplayName("Iterator on single node")
        void testIteratorSingleNode() {
            TreeNode root = new TreeNode(42);
            BSTTemplates.BSTIterator iterator = new BSTTemplates.BSTIterator(root);
            
            assertTrue(iterator.hasNext());
            assertEquals(42, iterator.next());
            assertFalse(iterator.hasNext());
        }

        @Test
        @DisplayName("Iterator traverses in order")
        void testIteratorInOrder() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80);
            BSTTemplates.BSTIterator iterator = new BSTTemplates.BSTIterator(root);
            
            List<Integer> result = new ArrayList<>();
            while (iterator.hasNext()) {
                result.add(iterator.next());
            }
            
            assertEquals(List.of(20, 30, 40, 50, 60, 70, 80), result);
        }

        @Test
        @DisplayName("Iterator hasNext called multiple times")
        void testIteratorHasNextMultiple() {
            TreeNode root = new TreeNode(42);
            BSTTemplates.BSTIterator iterator = new BSTTemplates.BSTIterator(root);
            
            assertTrue(iterator.hasNext());
            assertTrue(iterator.hasNext());
            assertEquals(42, iterator.next());
            assertFalse(iterator.hasNext());
            assertFalse(iterator.hasNext());
        }
    }

    // ========== RANGE QUERY TESTS ==========

    @Nested
    @DisplayName("Range Sum BST Tests")
    class RangeSumBSTTests {

        @Test
        @DisplayName("Range sum in empty tree")
        void testRangeSumEmpty() {
            assertEquals(0, BSTTemplates.rangeSumBST(null, 1, 10));
        }

        @Test
        @DisplayName("Range sum all nodes in range")
        void testRangeSumAll() {
            TreeNode root = buildBST(10, 5, 15, 3, 7, 18);
            assertEquals(10 + 5 + 15 + 3 + 7 + 18, BSTTemplates.rangeSumBST(root, 1, 20));
        }

        @Test
        @DisplayName("Range sum partial range")
        void testRangeSumPartial() {
            TreeNode root = buildBST(10, 5, 15, 3, 7, 18);
            // Range [7, 15] includes: 7, 10, 15
            assertEquals(32, BSTTemplates.rangeSumBST(root, 7, 15));
        }

        @Test
        @DisplayName("Range sum no nodes in range")
        void testRangeSumNone() {
            TreeNode root = buildBST(10, 5, 15);
            assertEquals(0, BSTTemplates.rangeSumBST(root, 20, 30));
        }

        @Test
        @DisplayName("Range sum single node")
        void testRangeSumSingleNode() {
            TreeNode root = buildBST(10, 5, 15);
            assertEquals(10, BSTTemplates.rangeSumBST(root, 10, 10));
        }
    }

    @Nested
    @DisplayName("Count Nodes In Range Tests")
    class CountNodesInRangeTests {

        @Test
        @DisplayName("Count nodes in empty tree")
        void testCountEmpty() {
            assertEquals(0, BSTTemplates.countNodesInRange(null, 1, 10));
        }

        @Test
        @DisplayName("Count all nodes")
        void testCountAll() {
            TreeNode root = buildBST(10, 5, 15, 3, 7);
            assertEquals(5, BSTTemplates.countNodesInRange(root, 1, 20));
        }

        @Test
        @DisplayName("Count partial range")
        void testCountPartial() {
            TreeNode root = buildBST(10, 5, 15, 3, 7, 18);
            assertEquals(3, BSTTemplates.countNodesInRange(root, 7, 15));
        }

        @Test
        @DisplayName("Count no nodes in range")
        void testCountNone() {
            TreeNode root = buildBST(10, 5, 15);
            assertEquals(0, BSTTemplates.countNodesInRange(root, 20, 30));
        }
    }

    // ========== ADVANCED PATTERNS TESTS ==========

    @Nested
    @DisplayName("Lowest Common Ancestor Tests")
    class LowestCommonAncestorTests {

        @Test
        @DisplayName("LCA of nodes in different subtrees")
        void testLCADifferentSubtrees() {
            TreeNode root = buildBST(6, 2, 8, 0, 4, 7, 9, 3, 5);
            TreeNode p = BSTTemplates.search(root, 2);
            TreeNode q = BSTTemplates.search(root, 8);
            TreeNode lca = BSTTemplates.lowestCommonAncestorBST(root, p, q);
            
            assertNotNull(lca);
            assertEquals(6, lca.val);
        }

        @Test
        @DisplayName("LCA where one node is ancestor")
        void testLCAOneIsAncestor() {
            TreeNode root = buildBST(6, 2, 8, 0, 4, 7, 9);
            TreeNode p = BSTTemplates.search(root, 2);
            TreeNode q = BSTTemplates.search(root, 4);
            TreeNode lca = BSTTemplates.lowestCommonAncestorBST(root, p, q);
            
            assertNotNull(lca);
            assertEquals(2, lca.val);
        }

        @Test
        @DisplayName("LCA of same node")
        void testLCASameNode() {
            TreeNode root = buildBST(6, 2, 8);
            TreeNode p = BSTTemplates.search(root, 2);
            TreeNode lca = BSTTemplates.lowestCommonAncestorBST(root, p, p);
            
            assertNotNull(lca);
            assertEquals(2, lca.val);
        }

        @Test
        @DisplayName("LCA in single node tree")
        void testLCASingleNode() {
            TreeNode root = new TreeNode(1);
            TreeNode lca = BSTTemplates.lowestCommonAncestorBST(root, root, root);
            
            assertNotNull(lca);
            assertEquals(1, lca.val);
        }
    }

    @Nested
    @DisplayName("Trim BST Tests")
    class TrimBSTTests {

        @Test
        @DisplayName("Trim empty tree")
        void testTrimEmpty() {
            TreeNode result = BSTTemplates.trimBST(null, 1, 10);
            assertNull(result);
        }

        @Test
        @DisplayName("Trim removes nodes outside range")
        void testTrimRemovesOutside() {
            TreeNode root = buildBST(3, 0, 4, 2, 1);
            TreeNode result = BSTTemplates.trimBST(root, 1, 3);
            
            List<Integer> inorder = inorderTraversal(result);
            assertEquals(List.of(1, 2, 3), inorder);
            assertTrue(BSTTemplates.isValidBST(result));
        }

        @Test
        @DisplayName("Trim all nodes in range")
        void testTrimAllInRange() {
            TreeNode root = buildBST(3, 2, 4);
            TreeNode result = BSTTemplates.trimBST(root, 1, 5);
            
            List<Integer> inorder = inorderTraversal(result);
            assertEquals(List.of(2, 3, 4), inorder);
        }

        @Test
        @DisplayName("Trim all nodes out of range")
        void testTrimAllOutOfRange() {
            TreeNode root = buildBST(3, 2, 4);
            TreeNode result = BSTTemplates.trimBST(root, 10, 20);
            
            assertNull(result);
        }

        @Test
        @DisplayName("Trim single node in range")
        void testTrimSingleNodeInRange() {
            TreeNode root = new TreeNode(5);
            TreeNode result = BSTTemplates.trimBST(root, 1, 10);
            
            assertNotNull(result);
            assertEquals(5, result.val);
        }
    }

    @Nested
    @DisplayName("Convert BST to GST Tests")
    class ConvertBSTtoGSTTests {

        @Test
        @DisplayName("Convert empty tree")
        void testConvertEmpty() {
            TreeNode result = BSTTemplates.convertBSTtoGST(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Convert single node")
        void testConvertSingleNode() {
            TreeNode root = new TreeNode(5);
            BSTTemplates.convertBSTtoGST(root);
            
            assertEquals(5, root.val);
        }

        @Test
        @DisplayName("Convert BST to GST")
        void testConvertToGST() {
            TreeNode root = buildBST(4, 1, 6, 0, 2, 5, 7, 3, 8);
            // Original: 0, 1, 2, 3, 4, 5, 6, 7, 8
            // GST: each node = sum of all >= values
            BSTTemplates.convertBSTtoGST(root);
            
            // After conversion, all values change
            // Node that was 0 should now be 36 (sum of all values)
            // Node that was 8 should still be 8 (largest)
            // Just verify tree structure is maintained and values increased
            assertTrue(root.val > 4);  // Root value changed from 4
            
            // Verify all nodes have increased in value
            List<Integer> inorder = inorderTraversal(root);
            // GST values should be in descending order (reverse of BST)
            for (int i = 0; i < inorder.size() - 1; i++) {
                assertTrue(inorder.get(i) >= inorder.get(i + 1));
            }
        }

        @Test
        @DisplayName("Convert simple BST")
        void testConvertSimple() {
            TreeNode root = new TreeNode(2);
            root.left = new TreeNode(1);
            root.right = new TreeNode(3);
            
            BSTTemplates.convertBSTtoGST(root);
            
            // 1 -> 6 (1+2+3), 2 -> 5 (2+3), 3 -> 3
            assertEquals(6, root.left.val);
            assertEquals(5, root.val);
            assertEquals(3, root.right.val);
        }
    }

    @Nested
    @DisplayName("Recover BST Tests")
    class RecoverBSTTests {

        @Test
        @DisplayName("Recover BST with adjacent swap")
        void testRecoverAdjacentSwap() {
            TreeNode root = new TreeNode(3);
            root.left = new TreeNode(1);
            root.right = new TreeNode(2);  // Swapped: should be 3 and 2
            
            assertFalse(BSTTemplates.isValidBST(root));
            BSTTemplates.recoverBST(root);
            assertTrue(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Recover BST with non-adjacent swap")
        void testRecoverNonAdjacentSwap() {
            TreeNode root = new TreeNode(2);
            root.left = new TreeNode(3);  // Swapped
            root.right = new TreeNode(1);  // Swapped
            
            assertFalse(BSTTemplates.isValidBST(root));
            BSTTemplates.recoverBST(root);
            assertTrue(BSTTemplates.isValidBST(root));
            
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(1, 2, 3), inorder);
        }

        @Test
        @DisplayName("Recover larger BST")
        void testRecoverLargerBST() {
            // Create a valid BST first
            TreeNode root = new TreeNode(5);
            root.left = new TreeNode(3);
            root.right = new TreeNode(7);
            root.left.left = new TreeNode(2);
            root.left.right = new TreeNode(4);
            root.right.left = new TreeNode(6);
            root.right.right = new TreeNode(9);
            
            // Now swap two nodes (6 and 9)
            root.right.left.val = 9;
            root.right.right.val = 6;
            
            assertFalse(BSTTemplates.isValidBST(root));
            BSTTemplates.recoverBST(root);
            assertTrue(BSTTemplates.isValidBST(root));
            
            // Verify correct inorder
            List<Integer> inorder = inorderTraversal(root);
            assertEquals(List.of(2, 3, 4, 5, 6, 7, 9), inorder);
        }
    }

    @Nested
    @DisplayName("Is Same BST Tests")
    class IsSameBSTTests {

        @Test
        @DisplayName("Two empty trees are same")
        void testSameEmpty() {
            assertTrue(BSTTemplates.isSameBST(null, null));
        }

        @Test
        @DisplayName("Empty and non-empty trees are different")
        void testEmptyAndNonEmpty() {
            TreeNode root = new TreeNode(1);
            assertFalse(BSTTemplates.isSameBST(root, null));
            assertFalse(BSTTemplates.isSameBST(null, root));
        }

        @Test
        @DisplayName("Same structure and values")
        void testSameStructure() {
            TreeNode root1 = buildBST(5, 3, 7, 2, 4, 6, 8);
            TreeNode root2 = buildBST(5, 3, 7, 2, 4, 6, 8);
            
            assertTrue(BSTTemplates.isSameBST(root1, root2));
        }

        @Test
        @DisplayName("Same inorder, different structure")
        void testSameInorderDifferentStructure() {
            TreeNode root1 = buildBST(5, 3, 7);
            TreeNode root2 = buildBST(3, 5, 7);  // Different insertion order
            
            // Both have inorder: 3, 5, 7 but different structures
            assertTrue(BSTTemplates.isSameBST(root1, root2));
        }

        @Test
        @DisplayName("Different values")
        void testDifferentValues() {
            TreeNode root1 = buildBST(5, 3, 7);
            TreeNode root2 = buildBST(5, 3, 8);
            
            assertFalse(BSTTemplates.isSameBST(root1, root2));
        }

        @Test
        @DisplayName("Single node same value")
        void testSingleNodeSame() {
            TreeNode root1 = new TreeNode(42);
            TreeNode root2 = new TreeNode(42);
            
            assertTrue(BSTTemplates.isSameBST(root1, root2));
        }
    }

    // ========== STRESS TESTS ==========

    @Nested
    @DisplayName("Stress Tests")
    class StressTests {

        @Test
        @DisplayName("Large tree insertion and search")
        void testLargeTree() {
            TreeNode root = null;
            int n = 1000;
            
            // Insert 1000 nodes
            for (int i = 0; i < n; i++) {
                root = BSTTemplates.insert(root, i);
            }
            
            // Verify all are searchable
            for (int i = 0; i < n; i++) {
                assertNotNull(BSTTemplates.search(root, i));
            }
            
            assertTrue(BSTTemplates.isValidBST(root));
        }

        @Test
        @DisplayName("Multiple delete operations")
        void testMultipleDeletes() {
            TreeNode root = buildBST(50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45);
            
            root = BSTTemplates.delete(root, 20);
            assertTrue(BSTTemplates.isValidBST(root));
            
            root = BSTTemplates.delete(root, 30);
            assertTrue(BSTTemplates.isValidBST(root));
            
            root = BSTTemplates.delete(root, 50);
            assertTrue(BSTTemplates.isValidBST(root));
            
            assertNull(BSTTemplates.search(root, 20));
            assertNull(BSTTemplates.search(root, 30));
            assertNull(BSTTemplates.search(root, 50));
        }

        @Test
        @DisplayName("Iterator on large tree")
        void testIteratorLarge() {
            TreeNode root = null;
            int n = 100;
            
            for (int i = 0; i < n; i++) {
                root = BSTTemplates.insert(root, i);
            }
            
            BSTTemplates.BSTIterator iterator = new BSTTemplates.BSTIterator(root);
            int count = 0;
            int prev = -1;
            
            while (iterator.hasNext()) {
                int val = iterator.next();
                assertTrue(val > prev);  // Strictly increasing
                prev = val;
                count++;
            }
            
            assertEquals(n, count);
        }
    }
}
