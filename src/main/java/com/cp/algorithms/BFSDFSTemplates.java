package com.cp.algorithms;

import com.cp.datastructures.TreeNode;

import java.util.*;

/**
 * BFS and DFS templates — copy the one you need into your solution.
 *
 * Covers:
 *   - Graph BFS (shortest path, level traversal)
 *   - Grid BFS (2D matrix, 4-dir and 8-dir)
 *   - Graph DFS (iterative + recursive)
 *   - Tree BFS (level-order)
 *   - Tree DFS (pre/in/post order)
 *   - Multi-source BFS
 */
public class BFSDFSTemplates {

    // ---------------------------------------------------------------
    // 1. Graph BFS — shortest path (unweighted), returns distance array
    // ---------------------------------------------------------------
    public static int[] graphBFS(List<List<Integer>> adj, int src, int n) {
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        dist[src] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(src);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : adj.get(u)) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    queue.offer(v);
                }
            }
        }
        return dist;
    }

    // ---------------------------------------------------------------
    // 2. Grid BFS — 4-directional (change dirs for 8-dir)
    // Returns minimum steps from (sr, sc) to (er, ec), -1 if unreachable
    // grid[r][c] == 0 means open cell
    // ---------------------------------------------------------------
    private static final int[][] DIRS4 = {{0,1},{0,-1},{1,0},{-1,0}};
    private static final int[][] DIRS8 = {{0,1},{0,-1},{1,0},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};

    public static int gridBFS(int[][] grid, int sr, int sc, int er, int ec) {
        int rows = grid.length, cols = grid[0].length;
        if (grid[sr][sc] != 0 || grid[er][ec] != 0) return -1;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{sr, sc, 0});
        visited[sr][sc] = true;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int r = cur[0], c = cur[1], steps = cur[2];
            if (r == er && c == ec) return steps;

            for (int[] dir : DIRS4) {
                int nr = r + dir[0], nc = c + dir[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && grid[nr][nc] == 0 && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    queue.offer(new int[]{nr, nc, steps + 1});
                }
            }
        }
        return -1;
    }

    // ---------------------------------------------------------------
    // 3. Multi-source BFS — start from multiple sources simultaneously
    // Useful for: rotting oranges, walls-and-gates problems
    // ---------------------------------------------------------------
    public static int[][] multiSourceBFS(int[][] grid, int sourceValue) {
        int rows = grid.length, cols = grid[0].length;
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        Queue<int[]> queue = new LinkedList<>();

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c] == sourceValue) { dist[r][c] = 0; queue.offer(new int[]{r, c}); }

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int[] dir : DIRS4) {
                int nr = cur[0] + dir[0], nc = cur[1] + dir[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && dist[nr][nc] == Integer.MAX_VALUE) {
                    dist[nr][nc] = dist[cur[0]][cur[1]] + 1;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
        return dist;
    }

    // ---------------------------------------------------------------
    // 4. Graph DFS — recursive, detects cycle, tracks component
    // ---------------------------------------------------------------
    public static void graphDFS(List<List<Integer>> adj, int u, boolean[] visited, List<Integer> component) {
        visited[u] = true;
        component.add(u);
        for (int v : adj.get(u)) {
            if (!visited[v]) graphDFS(adj, v, visited, component);
        }
    }

    /** Count connected components */
    public static int countComponents(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                graphDFS(adj, i, visited, new ArrayList<>());
                count++;
            }
        }
        return count;
    }

    // ---------------------------------------------------------------
    // 5. Cycle detection in DIRECTED graph (using DFS + recStack)
    // ---------------------------------------------------------------
    public static boolean hasCycleDirected(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        boolean[] recStack = new boolean[n];
        for (int i = 0; i < n; i++)
            if (!visited[i] && dfsCycle(adj, i, visited, recStack)) return true;
        return false;
    }

    private static boolean dfsCycle(List<List<Integer>> adj, int u, boolean[] visited, boolean[] recStack) {
        visited[u] = recStack[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v] && dfsCycle(adj, v, visited, recStack)) return true;
            if (recStack[v]) return true;
        }
        recStack[u] = false;
        return false;
    }

    // ---------------------------------------------------------------
    // 6. Tree BFS — level-order traversal, returns list of levels
    // ---------------------------------------------------------------
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null)  queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }

    // ---------------------------------------------------------------
    // 7. Tree DFS — iterative pre/in/post order
    // ---------------------------------------------------------------
    public static List<Integer> preOrder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        if (root != null) stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);
            if (node.right != null) stack.push(node.right);
            if (node.left != null)  stack.push(node.left);
        }
        return result;
    }

    public static List<Integer> inOrder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) { stack.push(cur); cur = cur.left; }
            cur = stack.pop();
            result.add(cur.val);
            cur = cur.right;
        }
        return result;
    }

    public static List<Integer> postOrder(TreeNode root) {
        LinkedList<Integer> result = new LinkedList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        if (root != null) stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.addFirst(node.val); // reverse insert
            if (node.left != null)  stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }
        return result;
    }
}
