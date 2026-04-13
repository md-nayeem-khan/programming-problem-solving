package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for CartesianTreeTemplates
 * Tests all components: tree building, RMQ, nearest smaller elements, and largest rectangle
 */
public class CartesianTreeTemplatesTest {

    // ============= CARTESIAN TREE BUILDING TESTS =============

    @Test
    @DisplayName("Build tree from simple array")
    public void testBuildSimpleTree() {
        int[] arr = {3, 2, 6, 1, 9, 7};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertNotNull(root);
        assertEquals(1, root.value, "Root should be minimum element");
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
    }

    @Test
    @DisplayName("Build tree from empty array")
    public void testBuildEmptyTree() {
        int[] arr = {};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertNull(root, "Root should be null for empty array");
    }

    @Test
    @DisplayName("Build tree from single element")
    public void testBuildSingleElement() {
        int[] arr = {42};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertNotNull(root);
        assertEquals(42, root.value);
        assertNull(root.left);
        assertNull(root.right);
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
    }

    @Test
    @DisplayName("Build tree from sorted array (right-skewed)")
    public void testBuildSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertNotNull(root);
        assertEquals(1, root.value);
        assertNull(root.left, "Left should be null for sorted array");
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
    }

    @Test
    @DisplayName("Build tree from reverse sorted array (left-skewed)")
    public void testBuildReverseSortedArray() {
        int[] arr = {5, 4, 3, 2, 1};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertNotNull(root);
        assertEquals(1, root.value);
        assertNull(root.right, "Right should be null for reverse sorted array");
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
    }

    @Test
    @DisplayName("Build tree with duplicate values")
    public void testBuildWithDuplicates() {
        int[] arr = {3, 2, 2, 1, 1, 2};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertNotNull(root);
        assertEquals(1, root.value);
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
    }

    @Test
    @DisplayName("Build tree with all same values")
    public void testBuildAllSame() {
        int[] arr = {5, 5, 5, 5, 5};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertNotNull(root);
        assertEquals(5, root.value);
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
    }

    @Test
    @DisplayName("Build tree with negative values")
    public void testBuildWithNegativeValues() {
        int[] arr = {-3, -1, -5, -2, -4};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertNotNull(root);
        assertEquals(-5, root.value, "Root should be minimum element");
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
    }

    @Test
    @DisplayName("Build tree with buildTreeWithParents method")
    public void testBuildTreeWithParents() {
        int[] arr = {3, 2, 6, 1, 9, 7};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTreeWithParents(arr);
        
        assertNotNull(root);
        assertEquals(1, root.value);
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
        
        // Verify parent pointers are set correctly
        if (root.left != null) {
            assertEquals(root, root.left.parent);
        }
        if (root.right != null) {
            assertEquals(root, root.right.parent);
        }
    }

    @Test
    @DisplayName("Build large tree")
    public void testBuildLargeTree() {
        int[] arr = new int[1000];
        Random rand = new Random(42);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(10000);
        }
        
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertNotNull(root);
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
    }

    // ============= RANGE MINIMUM QUERY TESTS =============

    @Test
    @DisplayName("RMQ basic queries")
    public void testRMQBasic() {
        int[] arr = {4, 2, 5, 1, 6, 3};
        CartesianTreeTemplates.CartesianRMQ rmq = new CartesianTreeTemplates.CartesianRMQ(arr);
        
        assertEquals(1, rmq.rmq(1, 4), "RMQ(1,4) should be 1");
        assertEquals(2, rmq.rmq(0, 2), "RMQ(0,2) should be 2");
        assertEquals(1, rmq.rmq(3, 5), "RMQ(3,5) should be 1");
        assertEquals(1, rmq.rmq(0, 5), "RMQ(0,5) should be 1");
    }

    @Test
    @DisplayName("RMQ single element range")
    public void testRMQSingleElement() {
        int[] arr = {4, 2, 5, 1, 6, 3};
        CartesianTreeTemplates.CartesianRMQ rmq = new CartesianTreeTemplates.CartesianRMQ(arr);
        
        assertEquals(4, rmq.rmq(0, 0), "RMQ(0,0) should be 4");
        assertEquals(2, rmq.rmq(1, 1), "RMQ(1,1) should be 2");
        assertEquals(1, rmq.rmq(3, 3), "RMQ(3,3) should be 1");
    }

    @Test
    @DisplayName("RMQ with reversed indices")
    public void testRMQReversedIndices() {
        int[] arr = {4, 2, 5, 1, 6, 3};
        CartesianTreeTemplates.CartesianRMQ rmq = new CartesianTreeTemplates.CartesianRMQ(arr);
        
        assertEquals(1, rmq.rmq(4, 1), "RMQ(4,1) should be same as RMQ(1,4)");
        assertEquals(2, rmq.rmq(2, 0), "RMQ(2,0) should be same as RMQ(0,2)");
    }

    @Test
    @DisplayName("RMQ index queries")
    public void testRMQIndex() {
        int[] arr = {4, 2, 5, 1, 6, 3};
        CartesianTreeTemplates.CartesianRMQ rmq = new CartesianTreeTemplates.CartesianRMQ(arr);
        
        assertEquals(3, rmq.rmqIndex(1, 4), "Index of min in range [1,4] should be 3");
        assertEquals(1, rmq.rmqIndex(0, 2), "Index of min in range [0,2] should be 1");
        assertEquals(3, rmq.rmqIndex(3, 5), "Index of min in range [3,5] should be 3");
    }

    @Test
    @DisplayName("RMQ with duplicates")
    public void testRMQDuplicates() {
        int[] arr = {3, 2, 2, 1, 1, 2};
        CartesianTreeTemplates.CartesianRMQ rmq = new CartesianTreeTemplates.CartesianRMQ(arr);
        
        assertEquals(1, rmq.rmq(0, 5), "RMQ over entire array should be 1");
        assertEquals(2, rmq.rmq(0, 2), "RMQ(0,2) should be 2");
        assertEquals(1, rmq.rmq(3, 4), "RMQ(3,4) should be 1");
    }

    @Test
    @DisplayName("RMQ with negative values")
    public void testRMQNegative() {
        int[] arr = {-3, -1, -5, -2, -4};
        CartesianTreeTemplates.CartesianRMQ rmq = new CartesianTreeTemplates.CartesianRMQ(arr);
        
        assertEquals(-5, rmq.rmq(0, 4), "RMQ over entire array should be -5");
        assertEquals(-3, rmq.rmq(0, 1), "RMQ(0,1) should be -3");
        assertEquals(-4, rmq.rmq(3, 4), "RMQ(3,4) should be -4");
    }

    @Test
    @DisplayName("RMQ stress test with random queries")
    public void testRMQStress() {
        int[] arr = new int[100];
        Random rand = new Random(42);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(1000);
        }
        
        CartesianTreeTemplates.CartesianRMQ rmq = new CartesianTreeTemplates.CartesianRMQ(arr);
        
        // Test 100 random queries
        for (int t = 0; t < 100; t++) {
            int i = rand.nextInt(arr.length);
            int j = rand.nextInt(arr.length);
            
            int rmqResult = rmq.rmq(i, j);
            
            // Verify against brute force
            int min = Integer.MAX_VALUE;
            int start = Math.min(i, j);
            int end = Math.max(i, j);
            for (int k = start; k <= end; k++) {
                min = Math.min(min, arr[k]);
            }
            
            assertEquals(min, rmqResult, 
                String.format("RMQ(%d,%d) failed", i, j));
        }
    }

    // ============= NEAREST SMALLER ELEMENTS TESTS =============

    @Test
    @DisplayName("Nearest smaller elements basic test")
    public void testNearestSmallerBasic() {
        int[] arr = {4, 5, 2, 10, 8};
        int[][] result = CartesianTreeTemplates.NearestSmallerElements.allNearestSmaller(arr);
        
        int[] leftSmaller = result[0];
        int[] rightSmaller = result[1];
        
        assertArrayEquals(new int[]{-1, 0, -1, 2, 2}, leftSmaller);
        assertArrayEquals(new int[]{2, 2, -1, 4, -1}, rightSmaller);
    }

    @Test
    @DisplayName("Nearest smaller elements - sorted array")
    public void testNearestSmallerSorted() {
        int[] arr = {1, 2, 3, 4, 5};
        int[][] result = CartesianTreeTemplates.NearestSmallerElements.allNearestSmaller(arr);
        
        int[] leftSmaller = result[0];
        int[] rightSmaller = result[1];
        
        // In sorted array, each element has previous as left smaller
        assertArrayEquals(new int[]{-1, 0, 1, 2, 3}, leftSmaller);
        // No right smaller in increasing sequence
        assertArrayEquals(new int[]{-1, -1, -1, -1, -1}, rightSmaller);
    }

    @Test
    @DisplayName("Nearest smaller elements - reverse sorted array")
    public void testNearestSmallerReverseSorted() {
        int[] arr = {5, 4, 3, 2, 1};
        int[][] result = CartesianTreeTemplates.NearestSmallerElements.allNearestSmaller(arr);
        
        int[] leftSmaller = result[0];
        int[] rightSmaller = result[1];
        
        // No left smaller in decreasing sequence
        assertArrayEquals(new int[]{-1, -1, -1, -1, -1}, leftSmaller);
        // Each element has next as right smaller
        assertArrayEquals(new int[]{1, 2, 3, 4, -1}, rightSmaller);
    }

    @Test
    @DisplayName("Nearest smaller elements - all same values")
    public void testNearestSmallerAllSame() {
        int[] arr = {5, 5, 5, 5};
        int[][] result = CartesianTreeTemplates.NearestSmallerElements.allNearestSmaller(arr);
        
        int[] leftSmaller = result[0];
        int[] rightSmaller = result[1];
        
        // No smaller elements when all are same
        assertArrayEquals(new int[]{-1, -1, -1, -1}, leftSmaller);
        assertArrayEquals(new int[]{-1, -1, -1, -1}, rightSmaller);
    }

    @Test
    @DisplayName("Nearest smaller elements - single element")
    public void testNearestSmallerSingleElement() {
        int[] arr = {42};
        int[][] result = CartesianTreeTemplates.NearestSmallerElements.allNearestSmaller(arr);
        
        int[] leftSmaller = result[0];
        int[] rightSmaller = result[1];
        
        assertArrayEquals(new int[]{-1}, leftSmaller);
        assertArrayEquals(new int[]{-1}, rightSmaller);
    }

    @Test
    @DisplayName("Nearest smaller elements - empty array")
    public void testNearestSmallerEmpty() {
        int[] arr = {};
        int[][] result = CartesianTreeTemplates.NearestSmallerElements.allNearestSmaller(arr);
        
        int[] leftSmaller = result[0];
        int[] rightSmaller = result[1];
        
        assertEquals(0, leftSmaller.length);
        assertEquals(0, rightSmaller.length);
    }

    @Test
    @DisplayName("Nearest smaller elements with negative values")
    public void testNearestSmallerNegative() {
        int[] arr = {-1, -3, -2, -5, -4};
        int[][] result = CartesianTreeTemplates.NearestSmallerElements.allNearestSmaller(arr);
        
        int[] leftSmaller = result[0];
        int[] rightSmaller = result[1];
        
        // Verify correctness manually
        assertEquals(-1, leftSmaller[0]);
        assertEquals(1, leftSmaller[2]); // -2 has -3 to left
        assertEquals(3, leftSmaller[4]); // -4 has -5 to left
    }

    // ============= LARGEST RECTANGLE TESTS =============

    @Test
    @DisplayName("Largest rectangle - LeetCode example")
    public void testLargestRectangleLeetCodeExample() {
        int[] heights = {2, 1, 5, 6, 2, 3};
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(10, result, "Largest rectangle should be 10");
    }

    @Test
    @DisplayName("Largest rectangle - single bar")
    public void testLargestRectangleSingleBar() {
        int[] heights = {5};
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(5, result);
    }

    @Test
    @DisplayName("Largest rectangle - all same height")
    public void testLargestRectangleAllSame() {
        int[] heights = {4, 4, 4, 4};
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(16, result, "4 bars of height 4 = area 16");
    }

    @Test
    @DisplayName("Largest rectangle - increasing heights")
    public void testLargestRectangleIncreasing() {
        int[] heights = {1, 2, 3, 4, 5};
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(9, result, "3 bars of height 3 = area 9");
    }

    @Test
    @DisplayName("Largest rectangle - decreasing heights")
    public void testLargestRectangleDecreasing() {
        int[] heights = {5, 4, 3, 2, 1};
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(9, result, "3 bars of height 3 = area 9");
    }

    @Test
    @DisplayName("Largest rectangle - empty histogram")
    public void testLargestRectangleEmpty() {
        int[] heights = {};
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Largest rectangle - one tall bar")
    public void testLargestRectangleOneTall() {
        int[] heights = {1, 1, 10, 1, 1};
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(10, result, "Single tall bar of height 10");
    }

    @Test
    @DisplayName("Largest rectangle - valley shape")
    public void testLargestRectangleValley() {
        int[] heights = {5, 4, 3, 2, 3, 4, 5};
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(14, result, "7 bars with min height 2 = area 14");
    }

    @Test
    @DisplayName("Largest rectangle - mountain shape")
    public void testLargestRectangleMountain() {
        int[] heights = {1, 2, 3, 4, 3, 2, 1};
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(10, result);
    }

    @Test
    @DisplayName("Largest rectangle - complex pattern")
    public void testLargestRectangleComplex() {
        int[] heights = {6, 2, 5, 4, 5, 1, 6};
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(12, result, "3 bars of height 4 = area 12");
    }

    @Test
    @DisplayName("Largest rectangle - large histogram")
    public void testLargestRectangleLarge() {
        int[] heights = new int[1000];
        Arrays.fill(heights, 5);
        int result = CartesianTreeTemplates.LargestRectangle.largestRectangleArea(heights);
        assertEquals(5000, result, "1000 bars of height 5 = area 5000");
    }

    // ============= TREE UTILITIES TESTS =============

    @Test
    @DisplayName("Validate correct Cartesian tree")
    public void testValidateCorrectTree() {
        int[] arr = {3, 2, 6, 1, 9, 7};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
    }

    @Test
    @DisplayName("Validate fails for mismatched array")
    public void testValidateWrongArray() {
        int[] arr1 = {3, 2, 6, 1, 9, 7};
        int[] arr2 = {1, 2, 3, 4, 5, 6};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr1);
        
        assertFalse(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr2));
    }

    @Test
    @DisplayName("Print tree doesn't throw exception")
    public void testPrintTree() {
        int[] arr = {3, 2, 6, 1, 9, 7};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertDoesNotThrow(() -> 
            CartesianTreeTemplates.CartesianTreeUtils.printTree(root));
    }

    @Test
    @DisplayName("Print null tree doesn't throw exception")
    public void testPrintNullTree() {
        assertDoesNotThrow(() -> 
            CartesianTreeTemplates.CartesianTreeUtils.printTree(null));
    }

    // ============= EDGE CASES AND INTEGRATION TESTS =============

    @Test
    @DisplayName("Integration test - all operations on same tree")
    public void testIntegration() {
        int[] arr = {5, 3, 7, 2, 8, 4, 6, 1};
        
        // Build tree
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        assertTrue(CartesianTreeTemplates.CartesianTreeUtils.isValidCartesianTree(root, arr));
        
        // RMQ
        CartesianTreeTemplates.CartesianRMQ rmq = new CartesianTreeTemplates.CartesianRMQ(arr);
        assertEquals(1, rmq.rmq(0, 7), "Min of entire array");
        assertEquals(2, rmq.rmq(0, 3), "Min of first 4 elements");
        
        // Nearest smaller
        int[][] nearestSmaller = 
            CartesianTreeTemplates.NearestSmallerElements.allNearestSmaller(arr);
        assertNotNull(nearestSmaller[0]);
        assertNotNull(nearestSmaller[1]);
    }

    @Test
    @DisplayName("Performance test - O(n) build verification")
    public void testPerformanceBuild() {
        int size = 10000;
        int[] arr = new int[size];
        Random rand = new Random(42);
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(100000);
        }
        
        long start = System.currentTimeMillis();
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        long end = System.currentTimeMillis();
        
        assertNotNull(root);
        assertTrue(end - start < 1000, 
            "Build should complete in under 1 second for 10k elements");
    }

    @Test
    @DisplayName("Performance test - O(1) RMQ verification")
    public void testPerformanceRMQ() {
        int size = 10000;
        int[] arr = new int[size];
        Random rand = new Random(42);
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(100000);
        }
        
        CartesianTreeTemplates.CartesianRMQ rmq = new CartesianTreeTemplates.CartesianRMQ(arr);
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int l = rand.nextInt(size);
            int r = rand.nextInt(size);
            rmq.rmq(l, r);
        }
        long end = System.currentTimeMillis();
        
        assertTrue(end - start < 100, 
            "10k RMQ queries should complete in under 100ms");
    }

    @Test
    @DisplayName("Correctness - RMQ vs brute force on random data")
    public void testCorrectnessRMQVsBruteForce() {
        Random rand = new Random(42);
        
        for (int test = 0; test < 10; test++) {
            int size = 50 + rand.nextInt(50);
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = rand.nextInt(1000) - 500; // Include negative numbers
            }
            
            CartesianTreeTemplates.CartesianRMQ rmq = 
                new CartesianTreeTemplates.CartesianRMQ(arr);
            
            for (int q = 0; q < 20; q++) {
                int i = rand.nextInt(size);
                int j = rand.nextInt(size);
                
                int rmqResult = rmq.rmq(i, j);
                
                // Brute force
                int start = Math.min(i, j);
                int end = Math.max(i, j);
                int expected = arr[start];
                for (int k = start; k <= end; k++) {
                    expected = Math.min(expected, arr[k]);
                }
                
                assertEquals(expected, rmqResult, 
                    String.format("Test %d, Query RMQ(%d,%d)", test, i, j));
            }
        }
    }

    @Test
    @DisplayName("Verify heap property is maintained")
    public void testHeapPropertyMaintained() {
        int[] arr = {7, 3, 9, 2, 8, 1, 6, 4, 5};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        assertTrue(verifyMinHeapProperty(root), 
            "Tree should maintain min-heap property");
    }

    public boolean verifyMinHeapProperty(CartesianTreeTemplates.CartesianNode node) {
        if (node == null) return true;
        
        if (node.left != null && node.left.value < node.value) {
            return false;
        }
        if (node.right != null && node.right.value < node.value) {
            return false;
        }
        
        return verifyMinHeapProperty(node.left) && verifyMinHeapProperty(node.right);
    }

    @Test
    @DisplayName("Verify inorder traversal gives original array")
    public void testInorderGivesOriginalArray() {
        int[] arr = {7, 3, 9, 2, 8, 1, 6, 4, 5};
        CartesianTreeTemplates.CartesianNode root = 
            CartesianTreeTemplates.CartesianTreeBuilder.buildTree(arr);
        
        List<Integer> inorder = new ArrayList<>();
        collectInorder(root, inorder);
        
        assertEquals(arr.length, inorder.size());
        for (int i = 0; i < arr.length; i++) {
            assertEquals(arr[i], inorder.get(i), 
                "Inorder should match original array");
        }
    }

    public void collectInorder(CartesianTreeTemplates.CartesianNode node, 
                                 List<Integer> result) {
        if (node == null) return;
        collectInorder(node.left, result);
        result.add(node.value);
        collectInorder(node.right, result);
    }
}
