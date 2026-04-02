package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Centroid Decomposition of Trees
 * MEDIUM PRIORITY for Google hard tree problems
 * 
 * Essential for:
 * - Count paths in tree with specific properties
 * - Distance queries in tree
 * - Tree path aggregation
 * - Divide and conquer on trees
 * - K-th node on path queries
 * 
 * Interview Questions:
 * - Count paths with sum = K
 * - Paths with specific distance
 * - Tree DP optimization
 * - Center-based tree queries
 * 
 * Key Concepts:
 * - Centroid: Node whose removal splits tree into components of size ≤ N/2
 * - Recursively decompose tree using centroids
 * - Each level reduces problem size by at least half
 * - Height of decomposition tree: O(log N)
 * - Process paths through centroid independently
 * 
 * Companies: Google (4 stars), Competitive Programming (5 stars)
 */
public class CentroidDecompositionTemplates {
    
    // ========== CENTROID DECOMPOSITION ==========
    
    /**
     * Centroid Decomposition for tree path queries
     */
    @Complexity(time = "Build O(N log N), Query O(log N)", space = "O(N)")
    static class CentroidDecomposition {
        protected List<Integer>[] adj;
        protected boolean[] removed;
        protected int[] subtreeSize;
        protected int[] parent;  // Parent in centroid tree
        protected int n;
        
        @SuppressWarnings("unchecked")
        public CentroidDecomposition(int n) {
            this.n = n;
            this.adj = new List[n];
            for (int i = 0; i < n; i++) {
                adj[i] = new ArrayList<>();
            }
            this.removed = new boolean[n];
            this.subtreeSize = new int[n];
            this.parent = new int[n];
            Arrays.fill(parent, -1);
        }
        
        public void addEdge(int u, int v) {
            adj[u].add(v);
            adj[v].add(u);
        }
        
        /**
         * Build centroid decomposition
         */
        public void build() {
            if (n > 0) {
                decompose(0, -1);
            }
        }
        
        /**
         * Find size of subtree rooted at node
         */
        protected int getSize(int node, int par) {
            subtreeSize[node] = 1;
            for (int child : adj[node]) {
                if (child != par && !removed[child]) {
                    subtreeSize[node] += getSize(child, node);
                }
            }
            return subtreeSize[node];
        }
        
        /**
         * Find centroid of tree/subtree
         */
        protected int findCentroid(int node, int par, int treeSize) {
            for (int child : adj[node]) {
                if (child != par && !removed[child]) {
                    if (subtreeSize[child] > treeSize / 2) {
                        return findCentroid(child, node, treeSize);
                    }
                }
            }
            return node;
        }
        
        /**
         * Recursively decompose tree
         */
        private void decompose(int node, int par) {
            int treeSize = getSize(node, -1);
            int centroid = findCentroid(node, -1, treeSize);
            
            removed[centroid] = true;
            parent[centroid] = par;
            
            // Process centroid here if needed
            
            // Recursively decompose each component
            for (int child : adj[centroid]) {
                if (!removed[child]) {
                    decompose(child, centroid);
                }
            }
        }
        
        /**
         * Get parent in centroid tree
         */
        public int getParent(int node) {
            return parent[node];
        }
    }
    
    // ========== COUNT PATHS WITH SUM K ==========
    
    /**
     * Count paths in tree with given sum using centroid decomposition
     */
    @Complexity(time = "O(N log N)", space = "O(N)")
    static class PathSumCounter extends CentroidDecomposition {
        private int targetSum;
        private long pathCount;
        
        public PathSumCounter(int n, int targetSum) {
            super(n);
            this.targetSum = targetSum;
            this.pathCount = 0;
        }
        
        /**
         * Count paths with sum = target
         */
        public long countPaths(int[] values) {
            pathCount = 0;
            decompose(0, values, -1);
            return pathCount;
        }
        
        private void decompose(int node, int[] values, int par) {
            int treeSize = getSize(node, -1);
            int centroid = findCentroid(node, -1, treeSize);
            
            // Count paths through this centroid
            countPathsThroughCentroid(centroid, values);
            
            removed[centroid] = true;
            parent[centroid] = par;
            
            // Recursively decompose
            for (int child : adj[centroid]) {
                if (!removed[child]) {
                    decompose(child, values, centroid);
                }
            }
        }
        
        private void countPathsThroughCentroid(int centroid, int[] values) {
            Map<Long, Integer> pathSums = new HashMap<>();
            pathSums.put(0L, 1);  // Empty path
            
            // Check if the centroid value itself equals target sum
            if (values[centroid] == targetSum) {
                pathCount++;
            }
            
            for (int child : adj[centroid]) {
                if (!removed[child]) {
                    List<Long> sums = new ArrayList<>();
                    dfs(child, centroid, values[centroid], values, sums);
                    
                    // Count complementary paths
                    for (long sum : sums) {
                        long complement = targetSum - sum;
                        pathCount += pathSums.getOrDefault(complement, 0);
                    }
                    
                    // Add current subtree sums
                    for (long sum : sums) {
                        pathSums.put(sum, pathSums.getOrDefault(sum, 0) + 1);
                    }
                }
            }
        }
        
        private void dfs(int node, int par, long currentSum, int[] values, List<Long> sums) {
            currentSum += values[node];
            sums.add(currentSum);
            
            for (int child : adj[node]) {
                if (child != par && !removed[child]) {
                    dfs(child, node, currentSum, values, sums);
                }
            }
        }
    }
    
    // ========== DISTANCE QUERIES ==========
    
    /**
     * Answer distance queries using centroid decomposition
     */
    @Complexity(time = "Build O(N log N), Update O(log N), Query O(log N)", space = "O(N log N)")
    static class DistanceQueries extends CentroidDecomposition {
        private List<Integer>[][] distances;  // distances[node][level]
        
        @SuppressWarnings("unchecked")
        public DistanceQueries(int n) {
            super(n);
            this.distances = new List[n][20];  // log(n) levels
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 20; j++) {
                    distances[i][j] = new ArrayList<>();
                }
            }
        }
        
        /**
         * Find size of subtree rooted at node
         */
        @Override
        protected int getSize(int node, int par) {
            return super.getSize(node, par);
        }
        
        /**
         * Find centroid of tree/subtree
         */
        @Override
        protected int findCentroid(int node, int par, int treeSize) {
            return super.findCentroid(node, par, treeSize);
        }
        
        /**
         * Build with preprocessing
         */
        public void buildWithDistances() {
            decomposeWithDistances(0, 0);
        }
        
        private void decomposeWithDistances(int node, int level) {
            int treeSize = getSize(node, -1);
            int centroid = findCentroid(node, -1, treeSize);
            
            // Calculate distances from centroid
            calculateDistances(centroid, centroid, 0, level);
            
            removed[centroid] = true;
            
            for (int child : adj[centroid]) {
                if (!removed[child]) {
                    decomposeWithDistances(child, level + 1);
                }
            }
        }
        
        private void calculateDistances(int node, int par, int dist, int level) {
            distances[node][level].add(dist);
            
            for (int child : adj[node]) {
                if (child != par && !removed[child]) {
                    calculateDistances(child, node, dist + 1, level);
                }
            }
        }
        
        /**
         * Find k-th nearest colored node
         */
        public int kthNearest(int node, int k) {
            // This would require maintaining a set of colored nodes
            // and their distances for each level of the centroid tree
            // For now, return -1 to indicate feature not implemented
            // In a full implementation, we would:
            // 1. Maintain colored node sets at each centroid level
            // 2. Query distances at each level up the centroid tree
            // 3. Use a priority queue to find k-th nearest
            return -1;
        }
        
        /**
         * Get distance between two nodes (if both are colored)
         */
        public int getDistance(int u, int v) {
            // In a complete implementation, this would traverse up the centroid tree
            // to find LCA in centroid tree and sum distances
            return -1;
        }
    }
    
    // ========== COUNT DISTINCT VALUES ON PATH ==========
    
    /**
     * Count paths with K distinct values
     */
    @Complexity(time = "O(N log² N)", space = "O(N log N)")
    static class DistinctValuesPath {
        private List<Integer>[] adj;
        private boolean[] removed;
        private int[] subtreeSize;
        private int n;
        
        @SuppressWarnings("unchecked")
        public DistinctValuesPath(int n) {
            this.n = n;
            this.adj = new List[n];
            for (int i = 0; i < n; i++) {
                adj[i] = new ArrayList<>();
            }
            this.removed = new boolean[n];
            this.subtreeSize = new int[n];
        }
        
        public void addEdge(int u, int v) {
            adj[u].add(v);
            adj[v].add(u);
        }
        
        /**
         * Count paths with exactly k distinct values
         */
        public long countPaths(int[] values, int k) {
            return decompose(0, values, k);
        }
        
        private long decompose(int node, int[] values, int k) {
            int treeSize = getSize(node, -1);
            int centroid = findCentroid(node, -1, treeSize);
            
            long count = countPathsThroughCentroid(centroid, values, k);
            
            removed[centroid] = true;
            
            for (int child : adj[centroid]) {
                if (!removed[child]) {
                    count += decompose(child, values, k);
                }
            }
            
            return count;
        }
        
        private int getSize(int node, int par) {
            subtreeSize[node] = 1;
            for (int child : adj[node]) {
                if (child != par && !removed[child]) {
                    subtreeSize[node] += getSize(child, node);
                }
            }
            return subtreeSize[node];
        }
        
        private int findCentroid(int node, int par, int treeSize) {
            for (int child : adj[node]) {
                if (child != par && !removed[child]) {
                    if (subtreeSize[child] > treeSize / 2) {
                        return findCentroid(child, node, treeSize);
                    }
                }
            }
            return node;
        }
        
        private long countPathsThroughCentroid(int centroid, int[] values, int k) {
            Map<Integer, Integer> pathCounts = new HashMap<>();
            pathCounts.put(0, 1);  // Empty path from centroid
            
            long count = 0;
            
            for (int child : adj[centroid]) {
                if (!removed[child]) {
                    List<Map<Integer, Integer>> subtreePaths = new ArrayList<>();
                    dfs(child, centroid, new HashSet<>(), values, subtreePaths);
                    
                    // Count paths with exactly k distinct values through this centroid
                    for (Map<Integer, Integer> path : subtreePaths) {
                        if (path.size() == k) {
                            count++;
                        }
                        // Count paths combining with previous subtrees
                        for (Map.Entry<Integer, Integer> prevPath : pathCounts.entrySet()) {
                            int prevCount = prevPath.getKey();
                            if (prevCount + path.size() == k) {
                                count += prevPath.getValue();
                            }
                        }
                    }
                    
                    // Add current subtree paths to the map
                    for (Map<Integer, Integer> path : subtreePaths) {
                        pathCounts.put(path.size(), pathCounts.getOrDefault(path.size(), 0) + 1);
                    }
                }
            }
            
            return count;
        }
        
        private void dfs(int node, int par, Set<Integer> distinctValues, int[] values, 
                        List<Map<Integer, Integer>> result) {
            distinctValues.add(values[node]);
            Map<Integer, Integer> currentPath = new HashMap<>();
            for (int val : distinctValues) {
                currentPath.put(val, currentPath.getOrDefault(val, 0) + 1);
            }
            result.add(new HashMap<>(currentPath));
            
            for (int child : adj[node]) {
                if (child != par && !removed[child]) {
                    dfs(child, node, new HashSet<>(distinctValues), values, result);
                }
            }
        }
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Centroid Decomposition Examples ===\n");
        
        // Example 1: Basic centroid decomposition
        System.out.println("1. Build Centroid Tree:");
        CentroidDecomposition cd = new CentroidDecomposition(7);
        
        // Build tree: 0-1, 0-2, 1-3, 1-4, 2-5, 2-6
        cd.addEdge(0, 1);
        cd.addEdge(0, 2);
        cd.addEdge(1, 3);
        cd.addEdge(1, 4);
        cd.addEdge(2, 5);
        cd.addEdge(2, 6);
        
        cd.build();
        
        System.out.println("   Original tree:");
        System.out.println("         0");
        System.out.println("        / \\");
        System.out.println("       1   2");
        System.out.println("      /|   |\\");
        System.out.println("     3 4   5 6");
        System.out.println("   ");
        System.out.println("   Centroid tree parents:");
        for (int i = 0; i < 7; i++) {
            System.out.println("     Node " + i + " -> parent = " + cd.getParent(i));
        }
        
        // Example 2: Count paths with sum
        System.out.println("\n2. Count Paths with Sum:");
        PathSumCounter psc = new PathSumCounter(5, 8);
        
        // Build tree
        psc.addEdge(0, 1);
        psc.addEdge(1, 2);
        psc.addEdge(1, 3);
        psc.addEdge(0, 4);
        
        int[] values = {3, 2, 4, 1, 5};
        long pathCount = psc.countPaths(values);
        
        System.out.println("   Tree values: " + Arrays.toString(values));
        System.out.println("   Target sum: 8");
        System.out.println("   Number of paths with sum = 8: " + pathCount);
        
        // Example 3: Performance analysis
        System.out.println("\n3. Performance Analysis:");
        System.out.println("   Without centroid decomposition:");
        System.out.println("     Path counting: O(N²) - check all pairs");
        System.out.println("   ");
        System.out.println("   With centroid decomposition:");
        System.out.println("     Build: O(N log N)");
        System.out.println("     Path counting: O(N log N)");
        System.out.println("     Height of decomposition tree: O(log N)");
        System.out.println("     Each node processed O(log N) times");
        
        // Example 4: Key properties
        System.out.println("\n4. Centroid Properties:");
        System.out.println("   • Centroid splits tree into components of size ≤ N/2");
        System.out.println("   • Every tree has at least one centroid");
        System.out.println("   • Tree with N nodes has at most 2 centroids");
        System.out.println("   • Removing centroid creates components of size ≤ N/2");
        System.out.println("   • Decomposition tree has height O(log N)");
        
        // Example 5: When to use
        System.out.println("\n5. When to Use Centroid Decomposition:");
        System.out.println("   ✓ Count paths with specific properties");
        System.out.println("   ✓ Distance queries in tree");
        System.out.println("   ✓ Path aggregation (sum, XOR, etc.)");
        System.out.println("   ✓ K-th node on path");
        System.out.println("   ✓ Divide-and-conquer on trees");
        System.out.println("   ");
        System.out.println("   ✗ Simple tree DP (use regular DP)");
        System.out.println("   ✗ LCA queries (use binary lifting)");
        System.out.println("   ✗ Path queries (may use HLD instead)");
        
        // Example 6: Algorithm steps
        System.out.println("\n6. Algorithm Steps:");
        System.out.println("   1. Find centroid of current tree");
        System.out.println("   2. Process all paths through centroid");
        System.out.println("   3. Mark centroid as removed");
        System.out.println("   4. Recursively decompose each component");
        System.out.println("   5. Combine results");
        
        System.out.println("\n=== Key Points ===");
        System.out.println("Centroid: Node that balances tree when removed");
        System.out.println("Decomposition: Recursively split using centroids");
        System.out.println("Height: O(log N) guarantees efficiency");
        System.out.println("Applications: Path counting, distance queries, tree aggregation");
        System.out.println("Complexity: O(N log N) for most operations");
    }
}
