package com.cp.datastructures;

import java.util.*;

/**
 * LeetCode-style Binary Tree Node.
 *
 * Usage in solution:
 *   TreeNode root = TreeNode.fromArray(new Integer[]{1, 2, 3, null, 5});
 *   System.out.println(root.toArray());
 */
public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode() {}

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    // ---------------------------------------------------------------
    // Factory: build tree from LeetCode-style level-order array
    // e.g. [1, 2, 3, null, 5]  -> proper binary tree
    // ---------------------------------------------------------------
    public static TreeNode fromArray(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) return null;

        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;

        while (!queue.isEmpty() && i < values.length) {
            TreeNode node = queue.poll();

            if (i < values.length && values[i] != null) {
                node.left = new TreeNode(values[i]);
                queue.offer(node.left);
            }
            i++;

            if (i < values.length && values[i] != null) {
                node.right = new TreeNode(values[i]);
                queue.offer(node.right);
            }
            i++;
        }
        return root;
    }

    // ---------------------------------------------------------------
    // Serialize back to LeetCode level-order array string "[1,2,3,null,5]"
    // ---------------------------------------------------------------
    public static String serialize(TreeNode root) {
        if (root == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == null) {
                sb.append("null,");
            } else {
                sb.append(node.val).append(",");
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
        // Remove trailing nulls
        String result = sb.toString();
        while (result.endsWith("null,")) result = result.substring(0, result.length() - 5);
        return result.substring(0, result.length() - 1) + "]";
    }

    // ---------------------------------------------------------------
    // Pretty-print tree (horizontal layout)
    // ---------------------------------------------------------------
    public static void print(TreeNode root) {
        if (root == null) { System.out.println("(empty tree)"); return; }
        printHelper(root, "", true);
    }

    private static void printHelper(TreeNode node, String prefix, boolean isLeft) {
        if (node != null) {
            System.out.println(prefix + (isLeft ? "├── " : "└── ") + node.val);
            printHelper(node.left,  prefix + (isLeft ? "│   " : "    "), true);
            printHelper(node.right, prefix + (isLeft ? "│   " : "    "), false);
        }
    }

    @Override
    public String toString() {
        return "TreeNode(" + val + ")";
    }

    // ---------------------------------------------------------------
    // Equality check (structural)
    // ---------------------------------------------------------------
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        return p.val == q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}
