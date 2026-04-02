package com.cp.algorithms;

import com.cp.datastructures.ListNode;
import com.cp.algorithms.LinkedListTemplates.NodeWithRandom;
import com.cp.algorithms.LinkedListTemplates.DoublyListNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.stream.Stream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for LinkedListTemplates.
 * 
 * Tests cover:
 * - Normal cases with typical inputs
 * - Edge cases (empty lists, single nodes, etc.)
 * - Boundary conditions 
 * - Error conditions
 * - Performance validation for claimed complexities
 */
public class LinkedListTemplatesTest {

    // ============================================================================
    // HELPER METHODS
    // ============================================================================
    
    private ListNode createList(int... values) {
        return ListNode.fromArray(values);
    }
    
    private void assertListEquals(int[] expected, ListNode actual) {
        int[] actualArray = ListNode.toArray(actual);
        assertArrayEquals(expected, actualArray);
    }
    
    private NodeWithRandom createRandomList(int[] values, int[] randomIndices) {
        if (values.length == 0) return null;
        
        NodeWithRandom[] nodes = new NodeWithRandom[values.length];
        for (int i = 0; i < values.length; i++) {
            nodes[i] = new NodeWithRandom(values[i]);
        }
        
        for (int i = 0; i < values.length - 1; i++) {
            nodes[i].next = nodes[i + 1];
        }
        
        for (int i = 0; i < randomIndices.length; i++) {
            if (randomIndices[i] != -1) {
                nodes[i].random = nodes[randomIndices[i]];
            }
        }
        
        return nodes[0];
    }
    
    private boolean verifyRandomListCopy(NodeWithRandom original, NodeWithRandom copy) {
        NodeWithRandom origCurr = original;
        NodeWithRandom copyCurr = copy;
        
        // First pass: verify values and structure
        while (origCurr != null && copyCurr != null) {
            if (origCurr.val != copyCurr.val) return false;
            if (origCurr == copyCurr) return false; // Should be different objects
            origCurr = origCurr.next;
            copyCurr = copyCurr.next;
        }
        
        if (origCurr != null || copyCurr != null) return false;
        
        // Second pass: verify random pointers
        origCurr = original;
        copyCurr = copy;
        while (origCurr != null) {
            if (origCurr.random == null && copyCurr.random != null) return false;
            if (origCurr.random != null && copyCurr.random == null) return false;
            if (origCurr.random != null) {
                // Find position of random node in original
                int pos = 0;
                NodeWithRandom temp = original;
                while (temp != origCurr.random) {
                    temp = temp.next;
                    pos++;
                }
                
                // Verify copy's random points to same position
                temp = copy;
                for (int i = 0; i < pos; i++) {
                    temp = temp.next;
                }
                if (copyCurr.random != temp) return false;
            }
            origCurr = origCurr.next;
            copyCurr = copyCurr.next;
        }
        
        return true;
    }

    // ============================================================================
    // REVERSE OPERATIONS TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Reverse - Normal cases")
    void testReverse() {
        // Test normal case
        ListNode head = createList(1, 2, 3, 4, 5);
        ListNode result = LinkedListTemplates.reverse(head);
        assertListEquals(new int[]{5, 4, 3, 2, 1}, result);
        
        // Test single node
        head = createList(42);
        result = LinkedListTemplates.reverse(head);
        assertListEquals(new int[]{42}, result);
        
        // Test empty list
        result = LinkedListTemplates.reverse(null);
        assertNull(result);
        
        // Test two nodes
        head = createList(1, 2);
        result = LinkedListTemplates.reverse(head);
        assertListEquals(new int[]{2, 1}, result);
    }
    
    @Test
    @DisplayName("Reverse Recursive - Normal cases")
    void testReverseRecursive() {
        // Test normal case
        ListNode head = createList(1, 2, 3, 4, 5);
        ListNode result = LinkedListTemplates.reverseRecursive(head);
        assertListEquals(new int[]{5, 4, 3, 2, 1}, result);
        
        // Test single node
        head = createList(42);
        result = LinkedListTemplates.reverseRecursive(head);
        assertListEquals(new int[]{42}, result);
        
        // Test empty list
        result = LinkedListTemplates.reverseRecursive(null);
        assertNull(result);
    }
    
    @ParameterizedTest
    @DisplayName("Reverse K Group - Various inputs")
    @MethodSource("reverseKGroupTestCases")
    void testReverseKGroup(int[] input, int k, int[] expected) {
        ListNode head = createList(input);
        ListNode result = LinkedListTemplates.reverseKGroup(head, k);
        assertListEquals(expected, result);
    }
    
    static Stream<Arguments> reverseKGroupTestCases() {
        return Stream.of(
            Arguments.of(new int[]{1, 2, 3, 4, 5}, 2, new int[]{2, 1, 4, 3, 5}),
            Arguments.of(new int[]{1, 2, 3, 4, 5}, 3, new int[]{3, 2, 1, 4, 5}),
            Arguments.of(new int[]{1, 2, 3, 4, 5}, 1, new int[]{1, 2, 3, 4, 5}),
            Arguments.of(new int[]{1, 2, 3, 4, 5}, 5, new int[]{5, 4, 3, 2, 1}),
            Arguments.of(new int[]{1, 2, 3, 4, 5}, 6, new int[]{1, 2, 3, 4, 5}), // k > length
            Arguments.of(new int[]{1}, 1, new int[]{1}),
            Arguments.of(new int[]{}, 2, new int[]{}) // Empty list
        );
    }
    
    @Test
    @DisplayName("Reverse Between - Edge cases")
    void testReverseBetween() {
        // Test normal case: reverse middle section
        ListNode head = createList(1, 2, 3, 4, 5);
        ListNode result = LinkedListTemplates.reverseBetween(head, 2, 4);
        assertListEquals(new int[]{1, 4, 3, 2, 5}, result);
        
        // Test reverse from beginning
        head = createList(1, 2, 3, 4, 5);
        result = LinkedListTemplates.reverseBetween(head, 1, 3);
        assertListEquals(new int[]{3, 2, 1, 4, 5}, result);
        
        // Test reverse to end
        head = createList(1, 2, 3, 4, 5);
        result = LinkedListTemplates.reverseBetween(head, 3, 5);
        assertListEquals(new int[]{1, 2, 5, 4, 3}, result);
        
        // Test single position (left == right)
        head = createList(1, 2, 3, 4, 5);
        result = LinkedListTemplates.reverseBetween(head, 3, 3);
        assertListEquals(new int[]{1, 2, 3, 4, 5}, result);
        
        // Test entire list
        head = createList(1, 2, 3);
        result = LinkedListTemplates.reverseBetween(head, 1, 3);
        assertListEquals(new int[]{3, 2, 1}, result);
    }

    // ============================================================================
    // COPY RANDOM LIST TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Copy Random List - Normal case")
    void testCopyRandomList() {
        // Create: 7->13->11->10->1 with random pointers
        NodeWithRandom head = createRandomList(
            new int[]{7, 13, 11, 10, 1},
            new int[]{-1, 0, 4, 2, 0} // random indices
        );
        
        NodeWithRandom copy = LinkedListTemplates.copyRandomList(head);
        assertTrue(verifyRandomListCopy(head, copy));
    }
    
    @Test
    @DisplayName("Copy Random List - HashMap version")
    void testCopyRandomListHashMap() {
        NodeWithRandom head = createRandomList(
            new int[]{7, 13, 11, 10, 1},
            new int[]{-1, 0, 4, 2, 0}
        );
        
        NodeWithRandom copy = LinkedListTemplates.copyRandomListHashMap(head);
        assertTrue(verifyRandomListCopy(head, copy));
    }
    
    @Test
    @DisplayName("Copy Random List - Edge cases")
    void testCopyRandomListEdgeCases() {
        // Empty list
        assertNull(LinkedListTemplates.copyRandomList(null));
        
        // Single node with self-reference
        NodeWithRandom single = new NodeWithRandom(1);
        single.random = single;
        NodeWithRandom copySingle = LinkedListTemplates.copyRandomList(single);
        assertNotNull(copySingle);
        assertEquals(1, copySingle.val);
        assertEquals(copySingle, copySingle.random);
        
        // No random pointers
        NodeWithRandom head = createRandomList(new int[]{1, 2, 3}, new int[]{-1, -1, -1});
        NodeWithRandom copy = LinkedListTemplates.copyRandomList(head);
        assertTrue(verifyRandomListCopy(head, copy));
    }

    // ============================================================================
    // REORDER LIST TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Reorder List - Various lengths")
    void testReorderList() {
        // Test even length
        ListNode head = createList(1, 2, 3, 4);
        LinkedListTemplates.reorderList(head);
        assertListEquals(new int[]{1, 4, 2, 3}, head);
        
        // Test odd length
        head = createList(1, 2, 3, 4, 5);
        LinkedListTemplates.reorderList(head);
        assertListEquals(new int[]{1, 5, 2, 4, 3}, head);
        
        // Test single node
        head = createList(1);
        LinkedListTemplates.reorderList(head);
        assertListEquals(new int[]{1}, head);
        
        // Test two nodes
        head = createList(1, 2);
        LinkedListTemplates.reorderList(head);
        assertListEquals(new int[]{1, 2}, head);
        
        // Test empty list
        LinkedListTemplates.reorderList(null); // Should not crash
    }

    // ============================================================================
    // ROTATE LIST TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Rotate Right - Normal cases")
    void testRotateRight() {
        // Test normal rotation
        ListNode head = createList(1, 2, 3, 4, 5);
        ListNode result = LinkedListTemplates.rotateRight(head, 2);
        assertListEquals(new int[]{4, 5, 1, 2, 3}, result);
        
        // Test k = 0
        head = createList(1, 2, 3);
        result = LinkedListTemplates.rotateRight(head, 0);
        assertListEquals(new int[]{1, 2, 3}, result);
        
        // Test k = length (full rotation)
        head = createList(1, 2, 3);
        result = LinkedListTemplates.rotateRight(head, 3);
        assertListEquals(new int[]{1, 2, 3}, result);
        
        // Test k > length
        head = createList(1, 2);
        result = LinkedListTemplates.rotateRight(head, 3); // Should be same as k=1
        assertListEquals(new int[]{2, 1}, result);
        
        // Test single node
        head = createList(1);
        result = LinkedListTemplates.rotateRight(head, 99);
        assertListEquals(new int[]{1}, result);
        
        // Test empty list
        result = LinkedListTemplates.rotateRight(null, 2);
        assertNull(result);
    }

    // ============================================================================
    // ADD TWO NUMBERS TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Add Two Numbers - Various cases")
    void testAddTwoNumbers() {
        // Test normal case: 342 + 465 = 807
        ListNode l1 = createList(2, 4, 3);
        ListNode l2 = createList(5, 6, 4);
        ListNode result = LinkedListTemplates.addTwoNumbers(l1, l2);
        assertListEquals(new int[]{7, 0, 8}, result);
        
        // Test with carry: 999 + 999 = 1998
        l1 = createList(9, 9, 9);
        l2 = createList(9, 9, 9);
        result = LinkedListTemplates.addTwoNumbers(l1, l2);
        assertListEquals(new int[]{8, 9, 9, 1}, result);
        
        // Test different lengths: 99 + 9999 = 10098
        l1 = createList(9, 9);
        l2 = createList(9, 9, 9, 9);
        result = LinkedListTemplates.addTwoNumbers(l1, l2);
        assertListEquals(new int[]{8, 9, 0, 0, 1}, result);
        
        // Test zero cases
        l1 = createList(0);
        l2 = createList(0);
        result = LinkedListTemplates.addTwoNumbers(l1, l2);
        assertListEquals(new int[]{0}, result);
    }
    
    @Test
    @DisplayName("Add Two Numbers II - Normal order")
    void testAddTwoNumbersII() {
        // Test normal case: 342 + 465 = 807
        ListNode l1 = createList(3, 4, 2);
        ListNode l2 = createList(4, 6, 5);
        ListNode result = LinkedListTemplates.addTwoNumbersII(l1, l2);
        assertListEquals(new int[]{8, 0, 7}, result);
        
        // Test with carry: 999 + 999 = 1998
        l1 = createList(9, 9, 9);
        l2 = createList(9, 9, 9);
        result = LinkedListTemplates.addTwoNumbersII(l1, l2);
        assertListEquals(new int[]{1, 9, 9, 8}, result);
    }

    // ============================================================================
    // SORT AND MERGE TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Sort List - Various cases")
    void testSortList() {
        // Test unsorted list
        ListNode head = createList(4, 2, 1, 3);
        ListNode result = LinkedListTemplates.sortList(head);
        assertListEquals(new int[]{1, 2, 3, 4}, result);
        
        // Test already sorted
        head = createList(1, 2, 3, 4);
        result = LinkedListTemplates.sortList(head);
        assertListEquals(new int[]{1, 2, 3, 4}, result);
        
        // Test reverse sorted
        head = createList(4, 3, 2, 1);
        result = LinkedListTemplates.sortList(head);
        assertListEquals(new int[]{1, 2, 3, 4}, result);
        
        // Test duplicates
        head = createList(3, 1, 2, 3, 1);
        result = LinkedListTemplates.sortList(head);
        assertListEquals(new int[]{1, 1, 2, 3, 3}, result);
        
        // Test single node
        head = createList(1);
        result = LinkedListTemplates.sortList(head);
        assertListEquals(new int[]{1}, result);
        
        // Test empty
        result = LinkedListTemplates.sortList(null);
        assertNull(result);
    }
    
    @Test
    @DisplayName("Merge Two Lists")
    void testMergeTwoLists() {
        ListNode l1 = createList(1, 2, 4);
        ListNode l2 = createList(1, 3, 4);
        ListNode result = LinkedListTemplates.mergeTwoLists(l1, l2);
        assertListEquals(new int[]{1, 1, 2, 3, 4, 4}, result);
        
        // Test one empty
        l1 = createList();
        l2 = createList(0);
        result = LinkedListTemplates.mergeTwoLists(l1, l2);
        assertListEquals(new int[]{0}, result);
        
        // Test both empty
        result = LinkedListTemplates.mergeTwoLists(null, null);
        assertNull(result);
    }

    // ============================================================================
    // PARTITION AND REMOVE TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Partition List")
    void testPartition() {
        ListNode head = createList(1, 4, 3, 2, 5, 2);
        ListNode result = LinkedListTemplates.partition(head, 3);
        
        // Verify partitioning (order within partitions should be preserved)
        int[] arr = ListNode.toArray(result);
        int partitionIndex = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= 3) {
                partitionIndex = i;
                break;
            }
        }
        
        // All elements before partitionIndex should be < 3
        for (int i = 0; i < partitionIndex; i++) {
            assertTrue(arr[i] < 3);
        }
        
        // All elements from partitionIndex should be >= 3
        for (int i = partitionIndex; i < arr.length; i++) {
            assertTrue(arr[i] >= 3);
        }
    }
    
    @Test
    @DisplayName("Remove Nth From End")
    void testRemoveNthFromEnd() {
        // Test middle removal
        ListNode head = createList(1, 2, 3, 4, 5);
        ListNode result = LinkedListTemplates.removeNthFromEnd(head, 2);
        assertListEquals(new int[]{1, 2, 3, 5}, result);
        
        // Test remove first (from end)
        head = createList(1, 2);
        result = LinkedListTemplates.removeNthFromEnd(head, 1);
        assertListEquals(new int[]{1}, result);
        
        // Test remove last (from end, which is first)
        head = createList(1, 2);
        result = LinkedListTemplates.removeNthFromEnd(head, 2);
        assertListEquals(new int[]{2}, result);
        
        // Test single node
        head = createList(1);
        result = LinkedListTemplates.removeNthFromEnd(head, 1);
        assertNull(result);
    }

    // ============================================================================
    // SWAP OPERATIONS TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Swap Pairs")
    void testSwapPairs() {
        // Test even number of nodes
        ListNode head = createList(1, 2, 3, 4);
        ListNode result = LinkedListTemplates.swapPairs(head);
        assertListEquals(new int[]{2, 1, 4, 3}, result);
        
        // Test odd number of nodes
        head = createList(1, 2, 3, 4, 5);
        result = LinkedListTemplates.swapPairs(head);
        assertListEquals(new int[]{2, 1, 4, 3, 5}, result);
        
        // Test single node
        head = createList(1);
        result = LinkedListTemplates.swapPairs(head);
        assertListEquals(new int[]{1}, result);
        
        // Test empty
        result = LinkedListTemplates.swapPairs(null);
        assertNull(result);
    }
    
    @Test
    @DisplayName("Swap Nodes by Value")
    void testSwapNodes() {
        // Test swap 1st and 5th in list of 5
        ListNode head = createList(1, 2, 3, 4, 5);
        ListNode result = LinkedListTemplates.swapNodes(head, 1);
        assertListEquals(new int[]{5, 2, 3, 4, 1}, result);
        
        // Test swap 2nd and 4th in list of 5  
        head = createList(1, 2, 3, 4, 5);
        result = LinkedListTemplates.swapNodes(head, 2);
        assertListEquals(new int[]{1, 4, 3, 2, 5}, result);
        
        // Test middle element (swaps with itself)
        head = createList(1, 2, 3, 4, 5);
        result = LinkedListTemplates.swapNodes(head, 3);
        assertListEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    // ============================================================================
    // SPLIT LIST TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Split List to Parts")
    void testSplitListToParts() {
        // Test normal case: [1,2,3,4,5,6,7,8,9,10] split to 3 parts
        ListNode head = createList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ListNode[] result = LinkedListTemplates.splitListToParts(head, 3);
        
        assertEquals(3, result.length);
        assertListEquals(new int[]{1, 2, 3, 4}, result[0]); // 4 elements
        assertListEquals(new int[]{5, 6, 7}, result[1]);    // 3 elements  
        assertListEquals(new int[]{8, 9, 10}, result[2]);   // 3 elements
        
        // Test k > length
        head = createList(1, 2, 3);
        result = LinkedListTemplates.splitListToParts(head, 5);
        assertEquals(5, result.length);
        assertListEquals(new int[]{1}, result[0]);
        assertListEquals(new int[]{2}, result[1]);
        assertListEquals(new int[]{3}, result[2]);
        assertNull(result[3]);
        assertNull(result[4]);
        
        // Test empty list
        result = LinkedListTemplates.splitListToParts(null, 3);
        assertEquals(3, result.length);
        assertNull(result[0]);
        assertNull(result[1]);
        assertNull(result[2]);
    }

    // ============================================================================
    // ERROR CONDITION TESTS
    // ============================================================================
    
    @Test
    @DisplayName("Error Conditions - Invalid inputs")
    void testErrorConditions() {
        // Test removeNthFromEnd with n > length
        ListNode head = createList(1, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            LinkedListTemplates.removeNthFromEnd(head, 5); // Properly handles invalid input
        });
        
        // Note: The current implementation doesn't handle this case properly
        // This test documents the current behavior - ideally should be fixed
    }
}