package com.cp.datastructures;

/**
 * LeetCode-style Singly Linked List Node.
 *
 * Usage:
 *   ListNode head = ListNode.fromArray(new int[]{1, 2, 3, 4, 5});
 *   System.out.println(ListNode.serialize(head)); // [1,2,3,4,5]
 */
public class ListNode {

    public int val;
    public ListNode next;

    public ListNode() {}

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    // ---------------------------------------------------------------
    // Factory: build linked list from int array
    // ---------------------------------------------------------------
    public static ListNode fromArray(int[] values) {
        if (values == null || values.length == 0) return null;
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : values) {
            cur.next = new ListNode(v);
            cur = cur.next;
        }
        return dummy.next;
    }

    // ---------------------------------------------------------------
    // Serialize to string "[1,2,3,4,5]"
    // ---------------------------------------------------------------
    public static String serialize(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        ListNode cur = head;
        while (cur != null) {
            sb.append(cur.val);
            if (cur.next != null) sb.append(",");
            cur = cur.next;
        }
        return sb.append("]").toString();
    }

    // ---------------------------------------------------------------
    // Convert to int array (useful for assertion)
    // ---------------------------------------------------------------
    public static int[] toArray(ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        ListNode cur = head;
        while (cur != null) { list.add(cur.val); cur = cur.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    // ---------------------------------------------------------------
    // Create cycle at given position (pos=-1 means no cycle)
    // ---------------------------------------------------------------
    public static ListNode fromArrayWithCycle(int[] values, int pos) {
        ListNode head = fromArray(values);
        if (pos < 0 || head == null) return head;
        ListNode cycleNode = head, tail = head;
        int i = 0;
        while (tail.next != null) {
            if (i == pos) cycleNode = tail;
            tail = tail.next;
            i++;
        }
        tail.next = cycleNode;
        return head;
    }

    @Override
    public String toString() {
        return serialize(this);
    }
}
