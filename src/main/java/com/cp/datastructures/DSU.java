package com.cp.datastructures;

/**
 * Disjoint Set Union (Union-Find) with path compression + union by rank.
 *
 * Usage:
 *   DSU dsu = new DSU(n);
 *   dsu.union(0, 1);
 *   dsu.connected(0, 2); // false
 *   dsu.componentCount(); // number of connected components
 */
public class DSU {

    private final int[] parent;
    private final int[] rank;
    private int components;

    public DSU(int n) {
        parent = new int[n];
        rank   = new int[n];
        components = n;
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    /** Find root with path compression */
    public int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    /** Union by rank. Returns true if they were in different sets */
    public boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;
        if (rank[rx] < rank[ry]) { int tmp = rx; rx = ry; ry = tmp; }
        parent[ry] = rx;
        if (rank[rx] == rank[ry]) rank[rx]++;
        components--;
        return true;
    }

    public boolean connected(int x, int y) { return find(x) == find(y); }

    public int componentCount() { return components; }

    // ---------------------------------------------------------------
    // Weighted DSU — tracks size of each component
    // ---------------------------------------------------------------
    public static class WeightedDSU {
        private final int[] parent, size;
        private int components;

        public WeightedDSU(int n) {
            parent = new int[n]; size = new int[n];
            components = n;
            for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
        }

        public int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        public boolean union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return false;
            if (size[rx] < size[ry]) { int t = rx; rx = ry; ry = t; }
            parent[ry] = rx;
            size[rx] += size[ry];
            components--;
            return true;
        }

        public int size(int x) { return size[find(x)]; }
        public boolean connected(int x, int y) { return find(x) == find(y); }
        public int componentCount() { return components; }
    }
}
