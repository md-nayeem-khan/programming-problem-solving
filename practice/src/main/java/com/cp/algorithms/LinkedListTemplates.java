package com.cp.algorithms;

import com.cp.datastructures.ListNode;
import com.cp.problems.Complexity;
import java.util.*;

/**
 * Advanced Linked List Algorithm Templates for coding interviews.
 * 
 * Coverage:
 * - Reverse Nodes in K-Group
 * - Copy List with Random Pointer
 * - Flatten Multilevel Doubly Linked List
 * - Reorder List
 * - Rotate List
 * - Add Two Numbers (variants)
 * - Sort List
 * - Partition List
 * - Remove Nth Node from End
 * 
 * Critical for: Meta, Microsoft, Amazon
 * Frequency: ~10% of interview problems
 */
public class LinkedListTemplates {

    // ============================================================================
    // REVERSE OPERATIONS
    // ============================================================================
    
    /**
     * Reverse Linked List (iterative).
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        
        return prev;
    }
    
    /**
     * Reverse Linked List (recursive).
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static ListNode reverseRecursive(ListNode head) {
        if (head == null || head.next == null) return head;
        
        ListNode newHead = reverseRecursive(head.next);
        head.next.next = head;
        head.next = null;
        
        return newHead;
    }
    
    /**
     * Reverse Nodes in K-Group.
     * Reverse every k nodes, leave remainder as-is.
     * 
     * Pattern: Reverse k nodes at a time, connect groups.
     * 
     * Companies: Meta, Microsoft, Amazon
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k == 1) return head;
        
        // Check if we have k nodes to reverse
        ListNode curr = head;
        int count = 0;
        while (curr != null && count < k) {
            curr = curr.next;
            count++;
        }
        
        if (count < k) return head; // Less than k nodes left
        
        // Reverse first k nodes
        ListNode prev = null;
        curr = head;
        for (int i = 0; i < k; i++) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        
        // head is now tail of reversed group
        // prev is new head of group
        // curr points to next group
        head.next = reverseKGroup(curr, k);
        
        return prev;
    }
    
    /**
     * Reverse Between positions m and n (1-indexed).
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static ListNode reverseBetween(ListNode head, int left, int right) {
        if (left <= 0 || right <= 0) {
            throw new IllegalArgumentException("Positions must be positive (1-indexed)");
        }
        if (left > right) {
            throw new IllegalArgumentException("left (" + left + ") cannot be greater than right (" + right + ")");
        }
        if (head == null || left == right) return head;
        
        // Validate positions are within list bounds
        int length = 0;
        ListNode curr = head;
        while (curr != null) {
            length++;
            curr = curr.next;
        }
        
        if (right > length) {
            throw new IllegalArgumentException("right (" + right + ") cannot be greater than list length (" + length + ")");
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        // Move to position before left
        for (int i = 1; i < left; i++) {
            prev = prev.next;
        }
        
        // Reverse from left to right
        ListNode curr2 = prev.next;
        for (int i = 0; i < right - left; i++) {
            ListNode next = curr2.next;
            curr2.next = next.next;
            next.next = prev.next;
            prev.next = next;
        }
        
        return dummy.next;
    }
    
    // ============================================================================
    // COPY LIST WITH RANDOM POINTER
    // ============================================================================
    
    /**
     * Copy List with Random Pointer.
     * Each node has next and random pointer.
     * 
     * Pattern: Three passes - interweave, copy random, separate.
     * 
     * Companies: Meta, Amazon, Microsoft
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static NodeWithRandom copyRandomList(NodeWithRandom head) {
        if (head == null) return null;
        
        // Step 1: Create copy nodes interweaved with original
        NodeWithRandom curr = head;
        while (curr != null) {
            NodeWithRandom copy = new NodeWithRandom(curr.val);
            copy.next = curr.next;
            curr.next = copy;
            curr = copy.next;
        }
        
        // Step 2: Copy random pointers
        curr = head;
        while (curr != null) {
            if (curr.random != null) {
                curr.next.random = curr.random.next;
            }
            curr = curr.next.next;
        }
        
        // Step 3: Separate lists
        NodeWithRandom dummy = new NodeWithRandom(0);
        NodeWithRandom copyCurr = dummy;
        curr = head;
        
        while (curr != null) {
            NodeWithRandom next = curr.next.next;
            
            // Extract copy
            copyCurr.next = curr.next;
            copyCurr = copyCurr.next;
            
            // Restore original
            curr.next = next;
            curr = next;
        }
        
        return dummy.next;
    }
    
    /**
     * Alternative: Copy with HashMap.
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static NodeWithRandom copyRandomListHashMap(NodeWithRandom head) {
        if (head == null) return null;
        
        Map<NodeWithRandom, NodeWithRandom> map = new HashMap<>();
        
        // First pass: create all nodes
        NodeWithRandom curr = head;
        while (curr != null) {
            map.put(curr, new NodeWithRandom(curr.val));
            curr = curr.next;
        }
        
        // Second pass: link next and random
        curr = head;
        while (curr != null) {
            if (curr.next != null) {
                map.get(curr).next = map.get(curr.next);
            }
            if (curr.random != null) {
                map.get(curr).random = map.get(curr.random);
            }
            curr = curr.next;
        }
        
        return map.get(head);
    }
    
    // ============================================================================
    // FLATTEN MULTILEVEL DOUBLY LINKED LIST
    // ============================================================================
    
    /**
     * Flatten Multilevel Doubly Linked List.
     * Each node can have child pointer creating sublists.
     * 
     * Pattern: DFS to flatten child lists.
     * 
     * Companies: Meta, Amazon
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static DoublyListNode flatten(DoublyListNode head) {
        if (head == null) return null;
        
        DoublyListNode dummy = new DoublyListNode(0);
        flattenDFS(dummy, head);
        
        // Remove dummy
        dummy.next.prev = null;
        return dummy.next;
    }
    
    private static DoublyListNode flattenDFS(DoublyListNode prev, DoublyListNode curr) {
        if (curr == null) return prev;
        
        // Link prev and curr
        curr.prev = prev;
        prev.next = curr;
        
        DoublyListNode next = curr.next;
        
        // Flatten child first
        DoublyListNode tail = flattenDFS(curr, curr.child);
        curr.child = null;
        
        // Then process next
        return flattenDFS(tail, next);
    }
    
    // ============================================================================
    // REORDER LIST
    // ============================================================================
    
    /**
     * Reorder List: L0 → Ln → L1 → Ln-1 → L2 → Ln-2 → ...
     * 
     * Pattern: Find middle, reverse second half, merge alternately.
     * 
     * Companies: Meta, Amazon, Microsoft
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) return;
        
        // Find middle
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // Reverse second half
        ListNode secondHalf = reverse(slow.next);
        slow.next = null;
        
        // Merge alternately
        ListNode first = head;
        ListNode second = secondHalf;
        
        while (second != null) {
            ListNode tmp1 = first.next;
            ListNode tmp2 = second.next;
            
            first.next = second;
            second.next = tmp1;
            
            first = tmp1;
            second = tmp2;
        }
    }
    
    // ============================================================================
    // ROTATE LIST
    // ============================================================================
    
    /**
     * Rotate List Right by k positions.
     * 
     * Pattern: Make circular, find new head, break circle.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) return head;
        
        // Find length and tail
        int length = 1;
        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
            length++;
        }
        
        // Normalize k (handle negative rotations properly)
        k = k % length;
        if (k < 0) {
            k += length; // Convert negative rotation to positive equivalent
        }
        if (k == 0) return head;
        
        // Find new tail (at position length - k - 1)
        ListNode newTail = head;
        for (int i = 0; i < length - k - 1; i++) {
            newTail = newTail.next;
        }
        
        ListNode newHead = newTail.next;
        newTail.next = null;
        tail.next = head;
        
        return newHead;
    }
    
    // ============================================================================
    // ADD TWO NUMBERS
    // ============================================================================
    
    /**
     * Add Two Numbers (digits stored in reverse order).
     * E.g., 342 + 465 = 807 → [2,4,3] + [5,6,4] = [7,0,8]
     */
    @Complexity(time = "O(max(m,n))", space = "O(max(m,n))")
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        int carry = 0;
        
        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            
            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
        }
        
        return dummy.next;
    }
    
    /**
     * Add Two Numbers II (digits stored in normal order).
     * E.g., 342 + 465 = 807 → [3,4,2] + [4,6,5] = [8,0,7]
     * 
     * Pattern: Use stacks or reverse lists.
     */
    @Complexity(time = "O(max(m,n))", space = "O(max(m,n))")
    public static ListNode addTwoNumbersII(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();
        
        while (l1 != null) {
            s1.push(l1.val);
            l1 = l1.next;
        }
        while (l2 != null) {
            s2.push(l2.val);
            l2 = l2.next;
        }
        
        ListNode head = null;
        int carry = 0;
        
        while (!s1.isEmpty() || !s2.isEmpty() || carry != 0) {
            int sum = carry;
            if (!s1.isEmpty()) sum += s1.pop();
            if (!s2.isEmpty()) sum += s2.pop();
            
            ListNode node = new ListNode(sum % 10);
            node.next = head;
            head = node;
            
            carry = sum / 10;
        }
        
        return head;
    }
    
    // ============================================================================
    // SORT LIST
    // ============================================================================
    
    /**
     * Sort Linked List using Merge Sort.
     * 
     * Pattern: Find middle, split, sort recursively, merge.
     */
    @Complexity(time = "O(n log n)", space = "O(log n)")
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;
        
        // Find middle
        ListNode slow = head, fast = head, prev = null;
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // Split
        prev.next = null;
        
        // Sort each half
        ListNode left = sortList(head);
        ListNode right = sortList(slow);
        
        // Merge
        return mergeTwoLists(left, right);
    }
    
    /**
     * Merge Two Sorted Lists.
     */
    @Complexity(time = "O(m + n)", space = "O(1)")
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }
        
        curr.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }
    
    // ============================================================================
    // PARTITION LIST
    // ============================================================================
    
    /**
     * Partition List around value x.
     * All nodes < x before nodes >= x, preserve relative order.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static ListNode partition(ListNode head, int x) {
        ListNode beforeHead = new ListNode(0);
        ListNode afterHead = new ListNode(0);
        ListNode before = beforeHead;
        ListNode after = afterHead;
        
        while (head != null) {
            if (head.val < x) {
                before.next = head;
                before = before.next;
            } else {
                after.next = head;
                after = after.next;
            }
            head = head.next;
        }
        
        after.next = null;
        before.next = afterHead.next;
        
        return beforeHead.next;
    }
    
    // ============================================================================
    // REMOVE NTH NODE FROM END
    // ============================================================================
    
    /**
     * Remove Nth Node From End of List.
     * Pattern: Two pointers with n gap.
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive (1-indexed)");
        }
        
        if (head == null) {
            return null;
        }
        
        // First pass: calculate list length to validate n
        int length = 0;
        ListNode curr = head;
        while (curr != null) {
            length++;
            curr = curr.next;
        }
        
        // Validate n is within bounds
        if (n > length) {
            throw new IllegalArgumentException("n (" + n + ") cannot be greater than list length (" + length + ")");
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        ListNode first = dummy;
        ListNode second = dummy;
        
        // Move first n+1 steps ahead
        for (int i = 0; i <= n; i++) {
            first = first.next;
        }
        
        // Move both until first reaches end
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        
        // Remove nth node
        second.next = second.next.next;
        
        return dummy.next;
    }
    
    // ============================================================================
    // SWAP NODES
    // ============================================================================
    
    /**
     * Swap Nodes in Pairs.
     * E.g., 1→2→3→4 becomes 2→1→4→3
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        while (prev.next != null && prev.next.next != null) {
            ListNode first = prev.next;
            ListNode second = prev.next.next;
            
            // Swap
            first.next = second.next;
            second.next = first;
            prev.next = second;
            
            prev = first;
        }
        
        return dummy.next;
    }
    
    /**
     * Swap Nodes by Value (swap kth node from start with kth from end).
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static ListNode swapNodes(ListNode head, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive (1-indexed)");
        }
        if (head == null) return head;
        
        // Calculate length and validate k
        int length = 0;
        ListNode curr = head;
        while (curr != null) {
            length++;
            curr = curr.next;
        }
        
        if (k > length) {
            throw new IllegalArgumentException("k (" + k + ") cannot be greater than list length (" + length + ")");
        }
        
        ListNode first = head;
        ListNode second = head;
        curr = head;
        
        // Move first to kth node
        for (int i = 1; i < k; i++) {
            first = first.next;
        }
        
        // Use curr to find length
        curr = first;
        while (curr.next != null) {
            curr = curr.next;
            second = second.next;
        }
        
        // Swap values
        int temp = first.val;
        first.val = second.val;
        second.val = temp;
        
        return head;
    }
    
    // ============================================================================
    // SPLIT AND MERGE
    // ============================================================================
    
    /**
     * Split Linked List in Parts.
     */
    @Complexity(time = "O(n)", space = "O(k)")
    public static ListNode[] splitListToParts(ListNode head, int k) {
        // Count length
        int length = 0;
        ListNode curr = head;
        while (curr != null) {
            length++;
            curr = curr.next;
        }
        
        int baseSize = length / k;
        int extra = length % k;
        
        ListNode[] result = new ListNode[k];
        curr = head;
        
        for (int i = 0; i < k && curr != null; i++) {
            result[i] = curr;
            int partSize = baseSize + (i < extra ? 1 : 0);
            
            for (int j = 1; j < partSize; j++) {
                curr = curr.next;
            }
            
            ListNode next = curr.next;
            curr.next = null;
            curr = next;
        }
        
        return result;
    }
    
    // ============================================================================
    // HELPER CLASSES
    // ============================================================================
    
    public static class NodeWithRandom {
        public int val;
        public NodeWithRandom next;
        public NodeWithRandom random;
        
        public NodeWithRandom(int val) {
            this.val = val;
        }
    }
    
    public static class DoublyListNode {
        public int val;
        public DoublyListNode prev;
        public DoublyListNode next;
        public DoublyListNode child;
        
        public DoublyListNode(int val) {
            this.val = val;
        }
    }
}
