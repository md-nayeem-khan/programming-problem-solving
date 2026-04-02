package com.cp.algorithms;

import com.cp.datastructures.TreeNode;
import com.cp.problems.Complexity;

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
    @Complexity(time = "O(V + E)", space = "O(V)", explanation = "V vertices, E edges")
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

    @Complexity(time = "O(rows × cols)", space = "O(rows × cols)")
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
    @Complexity(time = "O(rows × cols)", space = "O(rows × cols)")
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
    
    // ---------------------------------------------------------------
    // 8. 0-1 BFS — Optimized BFS for graphs with edges of weight 0 or 1
    // ---------------------------------------------------------------
    
    /**
     * 0-1 BFS using Deque (Deque-based optimization).
     * 
     * For graphs where edges have weight 0 or 1 only.
     * More efficient than Dijkstra for this special case.
     * 
     * Key insight:
     * - If edge weight is 0: add to FRONT of deque
     * - If edge weight is 1: add to BACK of deque
     * - Maintains property that deque is sorted by distance
     * 
     * Real interview problems:
     * - LC 1368: Minimum Cost to Make at Least One Valid Path
     * - LC 2290: Minimum Obstacle Removal to Reach Corner
     * - Minimum flips to reach target
     * 
     * Interview favorites: Google, Meta (medium-hard)
     * 
     * Time: O(V + E), Space: O(V)
     * 
     * Example:
     *   Graph with edges: (u, v, 0) and (u, w, 1)
     *   From u, add v to front (dist unchanged), w to back (dist + 1)
     */
    @Complexity(time = "O(V + E)", space = "O(V)")
    public static int[] zeroOneBFS(List<List<int[]>> adj, int src, int n) {
        // adj[u] contains pairs [v, weight] where weight is 0 or 1
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        
        Deque<Integer> deque = new ArrayDeque<>();
        deque.offerFirst(src);
        
        while (!deque.isEmpty()) {
            int u = deque.pollFirst();
            
            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int weight = edge[1]; // 0 or 1
                
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    
                    if (weight == 0) {
                        deque.offerFirst(v); // Add to front
                    } else {
                        deque.offerLast(v);  // Add to back
                    }
                }
            }
        }
        
        return dist;
    }
    
    /**
     * 0-1 BFS for Grid with directional cost.
     * grid[r][c] represents preferred direction from cell (r,c):
     * 0=right, 1=down, 2=left, 3=up
     * 
     * Cost 0 if moving in preferred direction, cost 1 otherwise.
     * 
     * Example use case: Minimum cost path where:
     * - Moving in preferred direction costs 0
     * - Moving in other directions costs 1
     * 
     * Interview favorite: Google (hard)
     */
    @Complexity(time = "O(rows × cols)", space = "O(rows × cols)")
    public static int zeroOneBFSGrid(int[][] grid, int sr, int sc, int er, int ec) {
        int rows = grid.length, cols = grid[0].length;
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[sr][sc] = 0;
        
        Deque<int[]> deque = new ArrayDeque<>();
        deque.offerFirst(new int[]{sr, sc});
        
        while (!deque.isEmpty()) {
            int[] cur = deque.pollFirst();
            int r = cur[0], c = cur[1];
            
            for (int d = 0; d < 4; d++) {
                int nr = r + dirs[d][0], nc = c + dirs[d][1];
                
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    // Cost 0 if following preferred direction, cost 1 if changing
                    int moveCost = (grid[r][c] == d) ? 0 : 1;
                    int newDist = dist[r][c] + moveCost;
                    
                    if (newDist < dist[nr][nc]) {
                        dist[nr][nc] = newDist;
                        
                        if (moveCost == 0) {
                            deque.offerFirst(new int[]{nr, nc});
                        } else {
                            deque.offerLast(new int[]{nr, nc});
                        }
                    }
                }
            }
        }
        
        return dist[er][ec] == Integer.MAX_VALUE ? -1 : dist[er][ec];
    }
    
    /**
     * Minimum Cost to Change Array Direction (0-1 BFS application).
     * Array has arrows, change direction costs 1, follow arrow costs 0.
     * 
     * Pattern recognition: When you see "minimum flips/changes", think 0-1 BFS!
     */
    @Complexity(time = "O(n × m)", space = "O(n × m)")
    public static int minCostToReachEnd(int[][] grid) {
        // grid[i][j] represents direction: 0=right, 1=down, 2=left, 3=up
        int rows = grid.length, cols = grid[0].length;
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        
        int[][] cost = new int[rows][cols];
        for (int[] row : cost) Arrays.fill(row, Integer.MAX_VALUE);
        cost[0][0] = 0;
        
        Deque<int[]> deque = new ArrayDeque<>();
        deque.offerFirst(new int[]{0, 0});
        
        while (!deque.isEmpty()) {
            int[] cur = deque.pollFirst();
            int r = cur[0], c = cur[1];
            
            for (int d = 0; d < 4; d++) {
                int nr = r + dirs[d][0];
                int nc = c + dirs[d][1];
                
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    // Cost 0 if following arrow, cost 1 if changing
                    int changeCost = (grid[r][c] == d) ? 0 : 1;
                    
                    if (cost[r][c] + changeCost < cost[nr][nc]) {
                        cost[nr][nc] = cost[r][c] + changeCost;
                        
                        if (changeCost == 0) {
                            deque.offerFirst(new int[]{nr, nc});
                        } else {
                            deque.offerLast(new int[]{nr, nc});
                        }
                    }
                }
            }
        }
        
        return cost[rows - 1][cols - 1];
    }
    
    // ---------------------------------------------------------------
    // 9. Bidirectional BFS — Meet in the middle optimization
    // ---------------------------------------------------------------
    
    /**
     * Bidirectional BFS for shortest path between two nodes.
     * 
     * Optimization: Search from both source and target simultaneously.
     * Complexity reduces from O(b^d) to O(b^(d/2)) where:
     * - b = branching factor
     * - d = distance between source and target
     * 
     * Massive speedup for large graphs!
     * 
     * Real interview problems:
     * - LC 127: Word Ladder
     * - LC 752: Open the Lock
     * - Shortest transformation sequence
     * 
     * Interview favorites: Google, Meta (hard optimization)
     * 
     * Time: O(b^(d/2)), Space: O(b^(d/2))
     */
    @Complexity(time = "O(b^(d/2))", space = "O(b^(d/2))", 
                explanation = "b=branching factor, d=distance")
    public static int bidirectionalBFS(List<List<Integer>> adj, int start, int target, int n) {
        if (start == target) return 0;
        
        // Two frontiers: from start and from target
        Set<Integer> frontStart = new HashSet<>();
        Set<Integer> frontTarget = new HashSet<>();
        Set<Integer> visitedStart = new HashSet<>();
        Set<Integer> visitedTarget = new HashSet<>();
        
        frontStart.add(start);
        frontTarget.add(target);
        visitedStart.add(start);
        visitedTarget.add(target);
        
        int distance = 0;
        
        while (!frontStart.isEmpty() && !frontTarget.isEmpty()) {
            distance++;
            
            // Always expand smaller frontier (optimization)
            if (frontStart.size() > frontTarget.size()) {
                Set<Integer> temp = frontStart;
                frontStart = frontTarget;
                frontTarget = temp;
                
                Set<Integer> tempVisited = visitedStart;
                visitedStart = visitedTarget;
                visitedTarget = tempVisited;
            }
            
            Set<Integer> nextFront = new HashSet<>();
            
            for (int u : frontStart) {
                for (int v : adj.get(u)) {
                    // Found path: frontiers meet
                    if (visitedTarget.contains(v)) {
                        return distance;
                    }
                    
                    if (!visitedStart.contains(v)) {
                        visitedStart.add(v);
                        nextFront.add(v);
                    }
                }
            }
            
            frontStart = nextFront;
        }
        
        return -1; // No path found
    }
    
    /**
     * Bidirectional BFS for Word Ladder problem.
     * Transform beginWord to endWord by changing one letter at a time.
     * 
     * Interview favorite: Google, Meta (hard)
     */
    public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) return 0;
        
        // Special case: if begin and end are the same
        if (beginWord.equals(endWord)) return 1;
        
        Set<String> frontStart = new HashSet<>();
        Set<String> frontEnd = new HashSet<>();
        frontStart.add(beginWord);
        frontEnd.add(endWord);
        
        int length = 1;
        
        while (!frontStart.isEmpty() && !frontEnd.isEmpty()) {
            // Expand smaller frontier
            if (frontStart.size() > frontEnd.size()) {
                Set<String> temp = frontStart;
                frontStart = frontEnd;
                frontEnd = temp;
            }
            
            Set<String> nextFront = new HashSet<>();
            
            for (String word : frontStart) {
                char[] chars = word.toCharArray();
                
                for (int i = 0; i < chars.length; i++) {
                    char old = chars[i];
                    
                    for (char c = 'a'; c <= 'z'; c++) {
                        chars[i] = c;
                        String newWord = new String(chars);
                        
                        if (frontEnd.contains(newWord)) {
                            return length + 1;
                        }
                        
                        if (dict.contains(newWord)) {
                            nextFront.add(newWord);
                            dict.remove(newWord); // Avoid revisit
                        }
                    }
                    
                    chars[i] = old; // Restore
                }
            }
            
            frontStart = nextFront;
            length++;
        }
        
        return 0; // No transformation sequence
    }
    
    /**
     * Bidirectional BFS for Grid (optimized shortest path).
     */
    @Complexity(time = "O(rows × cols)", space = "O(rows × cols)")
    public static int bidirectionalBFSGrid(int[][] grid, int sr, int sc, int er, int ec) {
        if (sr == er && sc == ec) return 0;
        if (grid[sr][sc] == 1 || grid[er][ec] == 1) return -1;
        
        int rows = grid.length, cols = grid[0].length;
        
        Set<Integer> frontStart = new HashSet<>();
        Set<Integer> frontEnd = new HashSet<>();
        boolean[][] visitedStart = new boolean[rows][cols];
        boolean[][] visitedEnd = new boolean[rows][cols];
        
        frontStart.add(sr * cols + sc);
        frontEnd.add(er * cols + ec);
        visitedStart[sr][sc] = true;
        visitedEnd[er][ec] = true;
        
        int distance = 0;
        
        while (!frontStart.isEmpty() && !frontEnd.isEmpty()) {
            distance++;
            
            // Expand smaller frontier
            if (frontStart.size() > frontEnd.size()) {
                Set<Integer> temp = frontStart;
                frontStart = frontEnd;
                frontEnd = temp;
                
                boolean[][] tempVisited = visitedStart;
                visitedStart = visitedEnd;
                visitedEnd = tempVisited;
            }
            
            Set<Integer> nextFront = new HashSet<>();
            
            for (int pos : frontStart) {
                int r = pos / cols;
                int c = pos % cols;
                
                for (int[] dir : DIRS4) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];
                    
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 0) {
                        if (visitedEnd[nr][nc]) {
                            return distance;
                        }
                        
                        if (!visitedStart[nr][nc]) {
                            visitedStart[nr][nc] = true;
                            nextFront.add(nr * cols + nc);
                        }
                    }
                }
            }
            
            frontStart = nextFront;
        }
        
        return -1;
    }
}
