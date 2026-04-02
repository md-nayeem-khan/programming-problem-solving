package com.cp.algorithms;

import java.util.*;

/**
 * A* Search Algorithm Templates - Optimal Pathfinding with Heuristics
 * 
 * WHEN TO USE:
 * - Need shortest path from source to target
 * - Can define admissible heuristic (never overestimates true cost)
 * - Want better performance than Dijkstra
 * - Grid-based pathfinding, maze solving, game AI
 * 
 * KEY INSIGHT:
 * A* = Dijkstra + Heuristic guidance
 * f(n) = g(n) + h(n)
 * - g(n) = actual cost from start to n
 * - h(n) = estimated cost from n to goal (heuristic)
 * - f(n) = estimated total cost through n
 * 
 * HEURISTIC PROPERTIES:
 * - Admissible: h(n) ≤ actual cost (never overestimate) → guarantees optimal path
 * - Consistent: h(n) ≤ cost(n, n') + h(n') → more efficient
 * 
 * COMPLEXITY:
 * - Time: O(E log V) worst case, but usually much better with good heuristic
 * - Space: O(V)
 * - Better than Dijkstra when heuristic guides search toward goal
 * 
 * COMPARISON:
 * - Dijkstra: No heuristic, explores all directions equally, always optimal
 * - A*: Uses heuristic, explores toward goal first, optimal if h is admissible
 * - Greedy Best-First: Only uses h(n), fast but not optimal
 * - BFS: Unweighted graphs only, O(V + E)
 * 
 * COMMON INTERVIEW PROBLEMS:
 * - LeetCode 1091: Shortest Path in Binary Matrix
 * - LeetCode 1293: Shortest Path with Obstacles Elimination
 * - LeetCode 1263: Minimum Moves to Move Box
 * - LeetCode 752: Open the Lock
 * - LeetCode 773: Sliding Puzzle
 * 
 *
 */
public class AStarSearchTemplates {

    /**
     * 1. A* ON GRID - Most Common Interview Use Case
     * 
     * Problem: Find shortest path in 2D grid from start to end
     * Grid: 0 = passable, 1 = blocked
     * Movements: 4-directional or 8-directional
     * 
     * Time: O(rows * cols * log(rows * cols)) worst case
     * Space: O(rows * cols)
     * 
     * Heuristic: Manhattan distance for 4-dir, Chebyshev/Euclidean for 8-dir
     */
    static class GridAStar {
        private static final int[][] DIRS_4 = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        private static final int[][] DIRS_8 = {{0,1}, {1,0}, {0,-1}, {-1,0}, 
                                                {1,1}, {1,-1}, {-1,1}, {-1,-1}};
        
        static class Node implements Comparable<Node> {
            int x, y;
            int g;  // Actual cost from start
            int h;  // Heuristic cost to goal
            
            Node(int x, int y, int g, int h) {
                this.x = x; this.y = y;
                this.g = g; this.h = h;
            }
            
            int f() { return g + h; }  // Total estimated cost
            
            @Override
            public int compareTo(Node other) {
                int fComp = Integer.compare(this.f(), other.f());
                return fComp != 0 ? fComp : Integer.compare(this.h, other.h);
            }
        }
        
        // 4-directional A* with Manhattan distance heuristic
        public static int shortestPath4Dir(int[][] grid, int[] start, int[] end) {
            int rows = grid.length, cols = grid[0].length;
            int sx = start[0], sy = start[1];
            int ex = end[0], ey = end[1];
            
            if (grid[sx][sy] == 1 || grid[ex][ey] == 1) return -1;
            if (sx == ex && sy == ey) return 0;
            
            PriorityQueue<Node> pq = new PriorityQueue<>();
            int[][] dist = new int[rows][cols];
            for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
            
            dist[sx][sy] = 0;
            pq.offer(new Node(sx, sy, 0, manhattan(sx, sy, ex, ey)));
            
            while (!pq.isEmpty()) {
                Node curr = pq.poll();
                int x = curr.x, y = curr.y, g = curr.g;
                
                if (x == ex && y == ey) return g;
                if (g > dist[x][y]) continue;  // Already found better path
                
                for (int[] dir : DIRS_4) {
                    int nx = x + dir[0], ny = y + dir[1];
                    if (nx < 0 || nx >= rows || ny < 0 || ny >= cols) continue;
                    if (grid[nx][ny] == 1) continue;
                    
                    int newG = g + 1;
                    if (newG < dist[nx][ny]) {
                        dist[nx][ny] = newG;
                        int h = manhattan(nx, ny, ex, ey);
                        pq.offer(new Node(nx, ny, newG, h));
                    }
                }
            }
            
            return -1;
        }
        
        // 8-directional A* with Chebyshev distance heuristic
        public static int shortestPath8Dir(int[][] grid, int[] start, int[] end) {
            int rows = grid.length, cols = grid[0].length;
            int sx = start[0], sy = start[1];
            int ex = end[0], ey = end[1];
            
            if (grid[sx][sy] == 1 || grid[ex][ey] == 1) return -1;
            if (sx == ex && sy == ey) return 0;
            
            PriorityQueue<Node> pq = new PriorityQueue<>();
            int[][] dist = new int[rows][cols];
            for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
            
            dist[sx][sy] = 0;
            pq.offer(new Node(sx, sy, 0, chebyshev(sx, sy, ex, ey)));
            
            while (!pq.isEmpty()) {
                Node curr = pq.poll();
                int x = curr.x, y = curr.y, g = curr.g;
                
                if (x == ex && y == ey) return g;
                if (g > dist[x][y]) continue;
                
                for (int[] dir : DIRS_8) {
                    int nx = x + dir[0], ny = y + dir[1];
                    if (nx < 0 || nx >= rows || ny < 0 || ny >= cols) continue;
                    if (grid[nx][ny] == 1) continue;
                    
                    int newG = g + 1;
                    if (newG < dist[nx][ny]) {
                        dist[nx][ny] = newG;
                        int h = chebyshev(nx, ny, ex, ey);
                        pq.offer(new Node(nx, ny, newG, h));
                    }
                }
            }
            
            return -1;
        }
        
        // Reconstruct path (need to track parent pointers)
        public static List<int[]> shortestPathWithRoute(int[][] grid, int[] start, int[] end) {
            int rows = grid.length, cols = grid[0].length;
            int sx = start[0], sy = start[1];
            int ex = end[0], ey = end[1];
            
            if (grid[sx][sy] == 1 || grid[ex][ey] == 1) return null;
            
            PriorityQueue<Node> pq = new PriorityQueue<>();
            int[][] dist = new int[rows][cols];
            int[][][] parent = new int[rows][cols][2];
            for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    parent[i][j][0] = -1;
                    parent[i][j][1] = -1;
                }
            }
            
            dist[sx][sy] = 0;
            pq.offer(new Node(sx, sy, 0, manhattan(sx, sy, ex, ey)));
            
            while (!pq.isEmpty()) {
                Node curr = pq.poll();
                int x = curr.x, y = curr.y, g = curr.g;
                
                if (x == ex && y == ey) {
                    // Reconstruct path
                    List<int[]> path = new ArrayList<>();
                    int px = ex, py = ey;
                    while (px != -1 && py != -1) {
                        path.add(new int[]{px, py});
                        int[] p = parent[px][py];
                        px = p[0]; py = p[1];
                    }
                    Collections.reverse(path);
                    return path;
                }
                
                if (g > dist[x][y]) continue;
                
                for (int[] dir : DIRS_4) {
                    int nx = x + dir[0], ny = y + dir[1];
                    if (nx < 0 || nx >= rows || ny < 0 || ny >= cols) continue;
                    if (grid[nx][ny] == 1) continue;
                    
                    int newG = g + 1;
                    if (newG < dist[nx][ny]) {
                        dist[nx][ny] = newG;
                        parent[nx][ny][0] = x;
                        parent[nx][ny][1] = y;
                        int h = manhattan(nx, ny, ex, ey);
                        pq.offer(new Node(nx, ny, newG, h));
                    }
                }
            }
            
            return null;
        }
        
        // Manhattan distance (for 4-directional movement)
        private static int manhattan(int x1, int y1, int x2, int y2) {
            return Math.abs(x1 - x2) + Math.abs(y1 - y2);
        }
        
        // Chebyshev distance (for 8-directional movement)
        private static int chebyshev(int x1, int y1, int x2, int y2) {
            return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
        }
        
        // Euclidean distance (squared to avoid floating point)
        private static int euclideanSquared(int x1, int y1, int x2, int y2) {
            int dx = x1 - x2, dy = y1 - y2;
            return dx * dx + dy * dy;
        }
    }

    /**
     * 2. A* WITH OBSTACLES AND ELIMINATION
     * 
     * Problem: LeetCode 1293 - Shortest Path with Obstacles Elimination
     * Can eliminate up to k obstacles
     * State: (x, y, obstacles_eliminated)
     * 
     * Time: O(rows * cols * k * log(rows * cols * k))
     * Space: O(rows * cols * k)
     */
    static class AStarWithObstacleElimination {
        private static final int[][] DIRS = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        
        static class State implements Comparable<State> {
            int x, y, eliminated, g, h;
            
            State(int x, int y, int eliminated, int g, int h) {
                this.x = x; this.y = y;
                this.eliminated = eliminated;
                this.g = g; this.h = h;
            }
            
            int f() { return g + h; }
            
            @Override
            public int compareTo(State other) {
                return Integer.compare(this.f(), other.f());
            }
        }
        
        public static int shortestPathWithElimination(int[][] grid, int k) {
            int rows = grid.length, cols = grid[0].length;
            if (rows == 1 && cols == 1) return 0;
            
            PriorityQueue<State> pq = new PriorityQueue<>();
            int[][][] dist = new int[rows][cols][k + 1];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Arrays.fill(dist[i][j], Integer.MAX_VALUE);
                }
            }
            
            dist[0][0][0] = 0;
            pq.offer(new State(0, 0, 0, 0, rows - 1 + cols - 1));
            
            while (!pq.isEmpty()) {
                State curr = pq.poll();
                int x = curr.x, y = curr.y, elim = curr.eliminated, g = curr.g;
                
                if (x == rows - 1 && y == cols - 1) return g;
                if (g > dist[x][y][elim]) continue;
                
                for (int[] dir : DIRS) {
                    int nx = x + dir[0], ny = y + dir[1];
                    if (nx < 0 || nx >= rows || ny < 0 || ny >= cols) continue;
                    
                    int newElim = elim + grid[nx][ny];
                    if (newElim > k) continue;
                    
                    int newG = g + 1;
                    if (newG < dist[nx][ny][newElim]) {
                        dist[nx][ny][newElim] = newG;
                        int h = (rows - 1 - nx) + (cols - 1 - ny);
                        pq.offer(new State(nx, ny, newElim, newG, h));
                    }
                }
            }
            
            return -1;
        }
    }

    /**
     * 3. WEIGHTED A* (GRAPH WITH EDGE WEIGHTS)
     * 
     * Problem: Shortest path in weighted graph
     * Works like Dijkstra but with heuristic guidance
     * 
     * Time: O(E log V)
     * Space: O(V)
     */
    static class WeightedGraphAStar {
        static class Edge {
            int to, weight;
            Edge(int to, int weight) {
                this.to = to; this.weight = weight;
            }
        }
        
        static class Node implements Comparable<Node> {
            int id, g, h;
            
            Node(int id, int g, int h) {
                this.id = id; this.g = g; this.h = h;
            }
            
            int f() { return g + h; }
            
            @Override
            public int compareTo(Node other) {
                return Integer.compare(this.f(), other.f());
            }
        }
        
        // Generic A* on weighted graph with custom heuristic
        public static int astar(List<Edge>[] graph, int start, int goal, 
                               java.util.function.IntUnaryOperator heuristic) {
            int n = graph.length;
            int[] dist = new int[n];
            Arrays.fill(dist, Integer.MAX_VALUE);
            
            PriorityQueue<Node> pq = new PriorityQueue<>();
            dist[start] = 0;
            pq.offer(new Node(start, 0, heuristic.applyAsInt(start)));
            
            while (!pq.isEmpty()) {
                Node curr = pq.poll();
                int u = curr.id, g = curr.g;
                
                if (u == goal) return g;
                if (g > dist[u]) continue;
                
                for (Edge edge : graph[u]) {
                    int v = edge.to, weight = edge.weight;
                    int newG = g + weight;
                    
                    if (newG < dist[v]) {
                        dist[v] = newG;
                        int h = heuristic.applyAsInt(v);
                        pq.offer(new Node(v, newG, h));
                    }
                }
            }
            
            return -1;
        }
    }

    /**
     * 4. BIDIRECTIONAL A* SEARCH
     * 
     * Search from both start and goal simultaneously
     * Meet in the middle for massive speedup
     * 
     * Time: Significantly faster than unidirectional A* in practice
     * Space: O(V)
     * 
     * Used in: Word Ladder, shortest transformation sequences
     */
    static class BidirectionalAStar {
        static class Node implements Comparable<Node> {
            int x, y, g, h;
            
            Node(int x, int y, int g, int h) {
                this.x = x; this.y = y;
                this.g = g; this.h = h;
            }
            
            int f() { return g + h; }
            
            @Override
            public int compareTo(Node other) {
                return Integer.compare(this.f(), other.f());
            }
        }
        
        public static int bidirectionalSearch(int[][] grid, int[] start, int[] end) {
            int rows = grid.length, cols = grid[0].length;
            if (start[0] == end[0] && start[1] == end[1]) return 0;
            
            // Forward search from start
            PriorityQueue<Node> forwardPQ = new PriorityQueue<>();
            int[][] forwardDist = new int[rows][cols];
            for (int[] row : forwardDist) Arrays.fill(row, Integer.MAX_VALUE);
            forwardDist[start[0]][start[1]] = 0;
            forwardPQ.offer(new Node(start[0], start[1], 0, 
                           manhattan(start[0], start[1], end[0], end[1])));
            
            // Backward search from end
            PriorityQueue<Node> backwardPQ = new PriorityQueue<>();
            int[][] backwardDist = new int[rows][cols];
            for (int[] row : backwardDist) Arrays.fill(row, Integer.MAX_VALUE);
            backwardDist[end[0]][end[1]] = 0;
            backwardPQ.offer(new Node(end[0], end[1], 0, 
                            manhattan(end[0], end[1], start[0], start[1])));
            
            int bestPath = Integer.MAX_VALUE;
            int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
            
            while (!forwardPQ.isEmpty() || !backwardPQ.isEmpty()) {
                // Expand forward search
                if (!forwardPQ.isEmpty()) {
                    Node curr = forwardPQ.poll();
                    if (curr.f() >= bestPath) break;
                    
                    for (int[] dir : dirs) {
                        int nx = curr.x + dir[0], ny = curr.y + dir[1];
                        if (nx < 0 || nx >= rows || ny < 0 || ny >= cols) continue;
                        if (grid[nx][ny] == 1) continue;
                        
                        int newG = curr.g + 1;
                        if (newG < forwardDist[nx][ny]) {
                            forwardDist[nx][ny] = newG;
                            forwardPQ.offer(new Node(nx, ny, newG, 
                                           manhattan(nx, ny, end[0], end[1])));
                            
                            // Check if backward search reached this cell
                            if (backwardDist[nx][ny] != Integer.MAX_VALUE) {
                                bestPath = Math.min(bestPath, 
                                                   forwardDist[nx][ny] + backwardDist[nx][ny]);
                            }
                        }
                    }
                }
                
                // Expand backward search
                if (!backwardPQ.isEmpty()) {
                    Node curr = backwardPQ.poll();
                    if (curr.f() >= bestPath) break;
                    
                    for (int[] dir : dirs) {
                        int nx = curr.x + dir[0], ny = curr.y + dir[1];
                        if (nx < 0 || nx >= rows || ny < 0 || ny >= cols) continue;
                        if (grid[nx][ny] == 1) continue;
                        
                        int newG = curr.g + 1;
                        if (newG < backwardDist[nx][ny]) {
                            backwardDist[nx][ny] = newG;
                            backwardPQ.offer(new Node(nx, ny, newG, 
                                            manhattan(nx, ny, start[0], start[1])));
                            
                            // Check if forward search reached this cell
                            if (forwardDist[nx][ny] != Integer.MAX_VALUE) {
                                bestPath = Math.min(bestPath, 
                                                   forwardDist[nx][ny] + backwardDist[nx][ny]);
                            }
                        }
                    }
                }
            }
            
            return bestPath == Integer.MAX_VALUE ? -1 : bestPath;
        }
        
        private static int manhattan(int x1, int y1, int x2, int y2) {
            return Math.abs(x1 - x2) + Math.abs(y1 - y2);
        }
    }

    /**
     * 5. IDA* (ITERATIVE DEEPENING A*)
     * 
     * Memory-efficient variant of A*
     * Uses depth-first search with iteratively increasing cost limit
     * Good when memory is constrained
     * 
     * Time: Exponential worst case, but efficient with good heuristic
     * Space: O(depth) instead of O(V)
     */
    static class IterativeDeepeningAStar {
        private static final int[][] DIRS = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        private static final int FOUND = -1;
        
        public static int idastar(int[][] grid, int[] start, int[] end) {
            int bound = manhattan(start[0], start[1], end[0], end[1]);
            boolean[][] visited = new boolean[grid.length][grid[0].length];
            
            while (bound < Integer.MAX_VALUE) {
                visited[start[0]][start[1]] = true;
                int t = search(grid, start[0], start[1], end[0], end[1], 
                             0, bound, visited);
                if (t == FOUND) return bound;
                if (t == Integer.MAX_VALUE) return -1;
                bound = t;
                // Clear visited for next iteration
                for (boolean[] row : visited) Arrays.fill(row, false);
            }
            
            return -1;
        }
        
        private static int search(int[][] grid, int x, int y, int ex, int ey, 
                                 int g, int bound, boolean[][] visited) {
            int f = g + manhattan(x, y, ex, ey);
            if (f > bound) return f;
            if (x == ex && y == ey) return FOUND;
            
            int min = Integer.MAX_VALUE;
            for (int[] dir : DIRS) {
                int nx = x + dir[0], ny = y + dir[1];
                if (nx < 0 || nx >= grid.length || ny < 0 || ny >= grid[0].length) continue;
                if (grid[nx][ny] == 1 || visited[nx][ny]) continue;
                
                visited[nx][ny] = true;
                int t = search(grid, nx, ny, ex, ey, g + 1, bound, visited);
                if (t == FOUND) return FOUND;
                if (t < min) min = t;
                visited[nx][ny] = false;
            }
            
            return min;
        }
        
        private static int manhattan(int x1, int y1, int x2, int y2) {
            return Math.abs(x1 - x2) + Math.abs(y1 - y2);
        }
    }

    /**
     * USAGE EXAMPLES AND TEST CASES
     */
    public static void main(String[] args) {
        // Example 1: Basic grid A* (4-directional)
        int[][] grid1 = {
            {0, 0, 0, 0},
            {1, 1, 0, 1},
            {0, 0, 0, 0},
            {0, 1, 1, 0}
        };
        int dist1 = GridAStar.shortestPath4Dir(grid1, new int[]{0, 0}, new int[]{3, 3});
        System.out.println("Shortest path (4-dir): " + dist1); // Expected: 6
        
        // Example 2: Grid A* with path reconstruction
        List<int[]> path = GridAStar.shortestPathWithRoute(grid1, new int[]{0, 0}, new int[]{3, 3});
        System.out.print("Path: ");
        if (path != null) {
            for (int[] p : path) System.out.print("(" + p[0] + "," + p[1] + ") ");
            System.out.println();
        }
        
        // Example 3: 8-directional movement
        int dist2 = GridAStar.shortestPath8Dir(grid1, new int[]{0, 0}, new int[]{3, 3});
        System.out.println("Shortest path (8-dir): " + dist2); // Expected: 4 (diagonal moves)
        
        // Example 4: With obstacle elimination
        int[][] grid2 = {
            {0, 1, 1},
            {0, 1, 0},
            {0, 0, 0}
        };
        int dist3 = AStarWithObstacleElimination.shortestPathWithElimination(grid2, 1);
        System.out.println("Path with 1 elimination: " + dist3); // Expected: 4
        
        // Example 5: Weighted graph A*
        @SuppressWarnings("unchecked")
        List<WeightedGraphAStar.Edge>[] graph = new ArrayList[5];
        for (int i = 0; i < 5; i++) graph[i] = new ArrayList<>();
        graph[0].add(new WeightedGraphAStar.Edge(1, 4));
        graph[0].add(new WeightedGraphAStar.Edge(2, 1));
        graph[1].add(new WeightedGraphAStar.Edge(3, 1));
        graph[2].add(new WeightedGraphAStar.Edge(1, 2));
        graph[2].add(new WeightedGraphAStar.Edge(3, 5));
        graph[3].add(new WeightedGraphAStar.Edge(4, 3));
        
        // Simple heuristic (could be improved with actual coordinates)
        int dist4 = WeightedGraphAStar.astar(graph, 0, 4, node -> Math.max(0, 4 - node));
        System.out.println("Weighted graph A*: " + dist4); // Expected: 7
        
        // Example 6: Bidirectional A*
        int dist5 = BidirectionalAStar.bidirectionalSearch(grid1, new int[]{0, 0}, new int[]{3, 3});
        System.out.println("Bidirectional A*: " + dist5); // Expected: 6
        
        // Example 7: IDA*
        int[][] grid3 = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        int dist6 = IterativeDeepeningAStar.idastar(grid3, new int[]{0, 0}, new int[]{2, 2});
        System.out.println("IDA*: " + dist6); // Expected: 4
    }
}
