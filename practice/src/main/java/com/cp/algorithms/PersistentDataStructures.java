package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Persistent Data Structures
 * MEDIUM PRIORITY for Google very hard problems
 * 
 * Essential for:
 * - Query previous versions of data structure
 * - Time-travel queries
 * - Functional programming with immutability
 * - Version control systems
 * - Offline algorithms with history
 * 
 * Interview Questions:
 * - Query array at different time points
 * - Range queries on historical data
 * - Version management problems
 * - Undo/redo functionality
 * 
 * Key Concepts:
 * - Path copying: Copy only modified path
 * - Structural sharing: Share unchanged subtrees
 * - Persistent segment tree: Query any version
 * - Space: O(log n) per update instead of O(n)
 * 
 * Companies: Google (4/5), Competitive Programming (5/5)
 */
public class PersistentDataStructures {
    
    // ========== PERSISTENT SEGMENT TREE ==========
    
    /**
     * Persistent Segment Tree - maintains all versions
     * Each update creates new version with O(log n) space
     */
    @Complexity(time = "O(log n) per operation", space = "O(n + q log n)")
    static class PersistentSegmentTree {
        static class Node {
            int value;
            Node left, right;
            
            public Node(int value) {
                this.value = value;
            }
            
            public Node(Node other) {
                this.value = other.value;
                this.left = other.left;
                this.right = other.right;
            }
        }
        
        private List<Node> versions;  // roots of all versions
        private int n;
        
        public PersistentSegmentTree(int[] arr) {
            if (arr == null) {
                throw new NullPointerException("Array cannot be null");
            }
            if (arr.length == 0) {
                throw new IllegalArgumentException("Array cannot be empty");
            }
            this.n = arr.length;
            this.versions = new ArrayList<>();
            
            // Build initial version
            Node root = build(arr, 0, n - 1);
            versions.add(root);
        }
        
        private Node build(int[] arr, int start, int end) {
            if (start == end) {
                return new Node(arr[start]);
            }
            
            int mid = (start + end) / 2;
            Node node = new Node(0);
            node.left = build(arr, start, mid);
            node.right = build(arr, mid + 1, end);
            node.value = node.left.value + node.right.value;
            
            return node;
        }
        
        /**
         * Update creates new version
         * @param version which version to base update on
         * @param idx index to update
         * @param value new value
         * @return new version number
         */
        public int update(int version, int idx, int value) {
            if (version < 0 || version >= versions.size()) {
                throw new IndexOutOfBoundsException("Version " + version + " out of bounds");
            }
            if (idx < 0 || idx >= n) {
                throw new IndexOutOfBoundsException("Index " + idx + " out of bounds for size " + n);
            }
            Node oldRoot = versions.get(version);
            Node newRoot = update(oldRoot, 0, n - 1, idx, value);
            versions.add(newRoot);
            return versions.size() - 1;
        }
        
        private Node update(Node node, int start, int end, int idx, int value) {
            if (start == end) {
                return new Node(value);
            }
            
            // Create new node copying structure
            Node newNode = new Node(node);
            
            int mid = (start + end) / 2;
            if (idx <= mid) {
                newNode.left = update(node.left, start, mid, idx, value);
            } else {
                newNode.right = update(node.right, mid + 1, end, idx, value);
            }
            
            newNode.value = newNode.left.value + newNode.right.value;
            return newNode;
        }
        
        /**
         * Query a specific version
         */
        public int query(int version, int l, int r) {
            if (version < 0 || version >= versions.size()) {
                throw new IndexOutOfBoundsException("Version " + version + " out of bounds");
            }
            return query(versions.get(version), 0, n - 1, l, r);
        }
        
        private int query(Node node, int start, int end, int l, int r) {
            if (r < start || end < l || node == null) return 0;
            if (l <= start && end <= r) return node.value;
            
            int mid = (start + end) / 2;
            return query(node.left, start, mid, l, r) + 
                   query(node.right, mid + 1, end, l, r);
        }
        
        /**
         * Get number of versions
         */
        public int getVersionCount() {
            return versions.size();
        }
    }
    
    // ========== PERSISTENT ARRAY ==========
    
    /**
     * Persistent Array - immutable array with efficient updates
     * Uses binary tree representation
     */
    @Complexity(time = "O(log n) per operation", space = "O(log n) per update")
    static class PersistentArray {
        static class Node {
            int value;
            Node left, right;
            
            public Node(int value) {
                this.value = value;
            }
        }
        
        private Node root;
        private int size;
        
        public PersistentArray(int size) {
            if (size <= 0) {
                throw new IllegalArgumentException("Size must be positive, got: " + size);
            }
            this.size = size;
            this.root = build(0, size - 1);
        }
        
        public PersistentArray(int[] arr) {
            if (arr == null) {
                throw new NullPointerException("Array cannot be null");
            }
            if (arr.length == 0) {
                throw new IllegalArgumentException("Array cannot be empty");
            }
            this.size = arr.length;
            this.root = buildFrom(arr, 0, size - 1);
        }
        
        private PersistentArray(Node root, int size) {
            this.root = root;
            this.size = size;
        }
        
        private Node build(int start, int end) {
            if (start > end) return null;
            if (start == end) return new Node(0);
            
            int mid = (start + end) / 2;
            Node node = new Node(0);
            node.left = build(start, mid);
            node.right = build(mid + 1, end);
            return node;
        }
        
        private Node buildFrom(int[] arr, int start, int end) {
            if (start > end) return null;
            if (start == end) return new Node(arr[start]);
            
            int mid = (start + end) / 2;
            Node node = new Node(0);
            node.left = buildFrom(arr, start, mid);
            node.right = buildFrom(arr, mid + 1, end);
            return node;
        }
        
        /**
         * Set value at index - returns new array
         */
        public PersistentArray set(int idx, int value) {
            if (idx < 0 || idx >= size) {
                throw new IndexOutOfBoundsException("Index " + idx + " out of bounds for size " + size);
            }
            Node newRoot = set(root, 0, size - 1, idx, value);
            return new PersistentArray(newRoot, size);
        }
        
        private Node set(Node node, int start, int end, int idx, int value) {
            if (start == end) {
                return new Node(value);
            }
            
            Node newNode = new Node(0);
            int mid = (start + end) / 2;
            
            if (idx <= mid) {
                newNode.left = set(node.left, start, mid, idx, value);
                newNode.right = node.right;  // Share unchanged subtree
            } else {
                newNode.left = node.left;     // Share unchanged subtree
                newNode.right = set(node.right, mid + 1, end, idx, value);
            }
            
            return newNode;
        }
        
        /**
         * Get value at index
         */
        public int get(int idx) {
            if (idx < 0 || idx >= size) {
                throw new IndexOutOfBoundsException("Index " + idx + " out of bounds for size " + size);
            }
            return get(root, 0, size - 1, idx);
        }
        
        private int get(Node node, int start, int end, int idx) {
            if (start == end) {
                return node.value;
            }
            
            int mid = (start + end) / 2;
            if (idx <= mid) {
                return get(node.left, start, mid, idx);
            } else {
                return get(node.right, mid + 1, end, idx);
            }
        }
    }
    
    // ========== PERSISTENT STACK ==========
    
    /**
     * Persistent Stack - immutable stack with structural sharing
     */
    @Complexity(time = "O(1) per operation", space = "O(1) per operation")
    static class PersistentStack<T> {
        static class Node<T> {
            T value;
            Node<T> next;
            int size;
            
            public Node(T value, Node<T> next) {
                this.value = value;
                this.next = next;
                this.size = (next == null) ? 1 : next.size + 1;
            }
        }
        
        private Node<T> head;
        
        public PersistentStack() {
            this.head = null;
        }
        
        private PersistentStack(Node<T> head) {
            this.head = head;
        }
        
        /**
         * Push - returns new stack
         */
        public PersistentStack<T> push(T value) {
            return new PersistentStack<>(new Node<>(value, head));
        }
        
        /**
         * Pop - returns new stack
         */
        public PersistentStack<T> pop() {
            if (head == null) throw new IllegalStateException("Stack is empty");
            return new PersistentStack<>(head.next);
        }
        
        /**
         * Peek top element
         */
        public T peek() {
            if (head == null) throw new IllegalStateException("Stack is empty");
            return head.value;
        }
        
        /**
         * Check if empty
         */
        public boolean isEmpty() {
            return head == null;
        }
        
        /**
         * Get size
         */
        public int size() {
            return head == null ? 0 : head.size;
        }
    }
    
    // ========== PERSISTENT UNION-FIND (DSU) ==========
    
    /**
     * Persistent Union-Find with path copying
     */
    @Complexity(time = "O(log n) per operation", space = "O(log n) per operation")
    static class PersistentDSU {
        private PersistentArray parent;
        private PersistentArray rank;
        private int n;
        
        public PersistentDSU(int n) {
            if (n <= 0) {
                throw new IllegalArgumentException("Number of elements must be positive, got: " + n);
            }
            this.n = n;
            int[] parentArr = new int[n];
            int[] rankArr = new int[n];
            
            for (int i = 0; i < n; i++) {
                parentArr[i] = i;
                rankArr[i] = 0;
            }
            
            this.parent = new PersistentArray(parentArr);
            this.rank = new PersistentArray(rankArr);
        }
        
        private PersistentDSU(PersistentArray parent, PersistentArray rank, int n) {
            this.parent = parent;
            this.rank = rank;
            this.n = n;
        }
        
        /**
         * Find root - does not modify structure
         */
        public int find(int x) {
            if (x < 0 || x >= n) {
                throw new IndexOutOfBoundsException("Element " + x + " out of bounds for size " + n);
            }
            while (parent.get(x) != x) {
                x = parent.get(x);
            }
            return x;
        }
        
        /**
         * Union two sets - returns new DSU
         */
        public PersistentDSU union(int x, int y) {
            if (x < 0 || x >= n) {
                throw new IndexOutOfBoundsException("Element " + x + " out of bounds for size " + n);
            }
            if (y < 0 || y >= n) {
                throw new IndexOutOfBoundsException("Element " + y + " out of bounds for size " + n);
            }
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                return this;
            }
            
            int rankX = rank.get(rootX);
            int rankY = rank.get(rootY);
            
            PersistentArray newParent = parent;
            PersistentArray newRank = rank;
            
            if (rankX < rankY) {
                newParent = newParent.set(rootX, rootY);
            } else if (rankX > rankY) {
                newParent = newParent.set(rootY, rootX);
            } else {
                newParent = newParent.set(rootY, rootX);
                newRank = newRank.set(rootX, rankX + 1);
            }
            
            return new PersistentDSU(newParent, newRank, n);
        }
        
        /**
         * Check if connected
         */
        public boolean connected(int x, int y) {
            return find(x) == find(y);
        }
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Persistent Data Structures Examples ===\n");
        
        // Example 1: Persistent Segment Tree
        System.out.println("1. Persistent Segment Tree (Time Travel Queries):");
        int[] arr = {1, 2, 3, 4, 5};
        PersistentSegmentTree pst = new PersistentSegmentTree(arr);
        
        System.out.println("   Initial array: [1, 2, 3, 4, 5]");
        System.out.println("   Version 0 - Sum [0, 4] = " + pst.query(0, 0, 4)); // 15
        
        int v1 = pst.update(0, 2, 10); // Update index 2 to 10
        System.out.println("   Version 1 - Updated index 2 to 10");
        System.out.println("   Version 1 - Sum [0, 4] = " + pst.query(v1, 0, 4)); // 22
        
        int v2 = pst.update(v1, 4, 20); // Update index 4 to 20
        System.out.println("   Version 2 - Updated index 4 to 20");
        System.out.println("   Version 2 - Sum [0, 4] = " + pst.query(v2, 0, 4)); // 37
        
        // Can still query old versions!
        System.out.println("   Version 0 - Sum [0, 4] = " + pst.query(0, 0, 4)); // 15 (unchanged!)
        System.out.println("   Version 1 - Sum [0, 4] = " + pst.query(v1, 0, 4)); // 22 (unchanged!)
        
        // Example 2: Persistent Array
        System.out.println("\n2. Persistent Array:");
        int[] arr2 = {10, 20, 30, 40, 50};
        PersistentArray pa1 = new PersistentArray(arr2);
        
        System.out.println("   Array v1: [10, 20, 30, 40, 50]");
        System.out.println("   v1[2] = " + pa1.get(2)); // 30
        
        PersistentArray pa2 = pa1.set(2, 100);
        System.out.println("   Array v2 = v1.set(2, 100)");
        System.out.println("   v2[2] = " + pa2.get(2)); // 100
        System.out.println("   v1[2] = " + pa1.get(2)); // 30 (unchanged!)
        
        PersistentArray pa3 = pa2.set(4, 200);
        System.out.println("   Array v3 = v2.set(4, 200)");
        System.out.println("   v3[4] = " + pa3.get(4)); // 200
        System.out.println("   v2[4] = " + pa2.get(4)); // 50 (unchanged!)
        System.out.println("   v1[4] = " + pa1.get(4)); // 50 (unchanged!)
        
        // Example 3: Persistent Stack
        System.out.println("\n3. Persistent Stack:");
        PersistentStack<Integer> stack1 = new PersistentStack<>();
        System.out.println("   Stack v1: empty");
        
        PersistentStack<Integer> stack2 = stack1.push(10).push(20).push(30);
        System.out.println("   Stack v2: [10, 20, 30] (top)");
        System.out.println("   v2.peek() = " + stack2.peek()); // 30
        System.out.println("   v2.size() = " + stack2.size()); // 3
        
        PersistentStack<Integer> stack3 = stack2.pop();
        System.out.println("   Stack v3 = v2.pop()");
        System.out.println("   v3.peek() = " + stack3.peek()); // 20
        System.out.println("   v2.peek() = " + stack2.peek()); // 30 (unchanged!)
        
        PersistentStack<Integer> stack4 = stack2.push(40);
        System.out.println("   Stack v4 = v2.push(40)");
        System.out.println("   v4.peek() = " + stack4.peek()); // 40
        System.out.println("   v2.peek() = " + stack2.peek()); // 30 (unchanged!)
        
        // Example 4: Persistent DSU
        System.out.println("\n4. Persistent Union-Find:");
        PersistentDSU dsu1 = new PersistentDSU(5);
        System.out.println("   DSU v1: 5 separate components [0,1,2,3,4]");
        
        PersistentDSU dsu2 = dsu1.union(0, 1);
        System.out.println("   DSU v2 = v1.union(0, 1)");
        System.out.println("   v2.connected(0, 1) = " + dsu2.connected(0, 1)); // true
        System.out.println("   v1.connected(0, 1) = " + dsu1.connected(0, 1)); // false!
        
        PersistentDSU dsu3 = dsu2.union(2, 3).union(1, 2);
        System.out.println("   DSU v3 = v2.union(2, 3).union(1, 2)");
        System.out.println("   v3.connected(0, 3) = " + dsu3.connected(0, 3)); // true
        System.out.println("   v2.connected(0, 3) = " + dsu2.connected(0, 3)); // false!
        
        // Example 5: Performance
        System.out.println("\n5. Performance Characteristics:");
        System.out.println("   Regular array update: O(1) time, O(n) space per version");
        System.out.println("   Persistent array update: O(log n) time, O(log n) space per version");
        System.out.println("   ");
        System.out.println("   For 1000 versions on array of size 10^6:");
        System.out.println("   Regular: 1000 × 10^6 = 10^9 space");
        System.out.println("   Persistent: 1000 × log(10^6) ≈ 20,000 space");
        System.out.println("   Space savings: ~50,000×!");
        
        System.out.println("\n=== Key Points ===");
        System.out.println("Persistent: All versions are preserved");
        System.out.println("Path Copying: Only copy modified path in tree");
        System.out.println("Structural Sharing: Share unchanged subtrees");
        System.out.println("Applications: Time-travel queries, version control, functional programming");
        System.out.println("Trade-off: Extra log factor for immutability benefits");
    }
}
