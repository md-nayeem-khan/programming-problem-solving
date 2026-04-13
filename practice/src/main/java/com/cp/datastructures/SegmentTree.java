package com.cp.datastructures;

/**
 * Segment Tree — supports range sum query and point update in O(log n).
 * Also includes Binary Indexed Tree (Fenwick Tree) for prefix sums.
 *
 * Usage (Segment Tree):
 *   SegmentTree st = new SegmentTree(new int[]{1,3,5,7,9,11});
 *   st.query(1, 3);      // sum of index 1..3 = 15
 *   st.update(1, 10);    // set index 1 to 10
 *
 * Usage (BIT):
 *   BinaryIndexedTree bit = new BinaryIndexedTree(6);
 *   bit.update(1, 3);  // add 3 at position 1 (1-indexed)
 *   bit.query(1, 3);   // prefix sum 1..3
 */
public class SegmentTree {

    private final int[] tree;
    private final int n;

    public SegmentTree(int[] nums) {
        n = nums.length;
        tree = new int[4 * n];
        build(nums, 0, 0, n - 1);
    }

    private void build(int[] nums, int node, int start, int end) {
        if (start == end) { tree[node] = nums[start]; return; }
        int mid = (start + end) / 2;
        build(nums, 2 * node + 1, start, mid);
        build(nums, 2 * node + 2, mid + 1, end);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    /** Point update: set index idx to val */
    public void update(int idx, int val) { update(0, 0, n - 1, idx, val); }

    private void update(int node, int start, int end, int idx, int val) {
        if (start == end) { tree[node] = val; return; }
        int mid = (start + end) / 2;
        if (idx <= mid) update(2 * node + 1, start, mid, idx, val);
        else            update(2 * node + 2, mid + 1, end, idx, val);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    /** Range sum query [l, r] inclusive, 0-indexed */
    public int query(int l, int r) { return query(0, 0, n - 1, l, r); }

    private int query(int node, int start, int end, int l, int r) {
        if (r < start || end < l) return 0;
        if (l <= start && end <= r) return tree[node];
        int mid = (start + end) / 2;
        return query(2 * node + 1, start, mid, l, r)
                + query(2 * node + 2, mid + 1, end, l, r);
    }

    // ---------------------------------------------------------------
    // Binary Indexed Tree (Fenwick Tree) — 1-indexed
    // ---------------------------------------------------------------
    public static class BinaryIndexedTree {
        private final int[] bit;
        private final int n;

        public BinaryIndexedTree(int n) { this.n = n; bit = new int[n + 1]; }

        public BinaryIndexedTree(int[] nums) {
            this(nums.length);
            for (int i = 0; i < nums.length; i++) update(i + 1, nums[i]);
        }

        /** Add delta at position i (1-indexed) */
        public void update(int i, int delta) {
            for (; i <= n; i += i & -i) bit[i] += delta;
        }

        /** Prefix sum [1..i] */
        public int prefixSum(int i) {
            int sum = 0;
            for (; i > 0; i -= i & -i) sum += bit[i];
            return sum;
        }

        /** Range sum [l..r] (1-indexed) */
        public int query(int l, int r) { return prefixSum(r) - prefixSum(l - 1); }

        /** Point set: set position i to val (requires tracking original values) */
        public void set(int i, int oldVal, int newVal) { update(i, newVal - oldVal); }
    }
}
