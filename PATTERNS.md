# Pattern Recognition Guide

Map problem keywords to the right algorithm template.

## How to Use

1. Read the problem
2. Find keywords in tables below
3. Use the matching template

---

## Pattern Lookup

### Array and String

| Keywords | Pattern | Template |
|----------|---------|----------|
| two numbers sum to | Two Pointers | SearchTemplates.twoSum() |
| subarray with sum | Sliding Window | SearchTemplates.minSubarrayLen() |
| longest substring | Sliding Window | SearchTemplates |
| maximum in window | Monotonic Queue | AdvancedDataStructures.maxSlidingWindow() |
| next greater element | Monotonic Stack | AdvancedDataStructures.nextGreaterElement() |
| histogram, trap water | Monotonic Stack | AdvancedDataStructures.largestRectangleArea() |
| merge intervals | Sorting + Greedy | GreedyTemplates |
| pattern in text | KMP | StringAlgorithms.KMP.search() |
| longest palindrome | Manacher | StringAlgorithms.longestPalindrome() |
| sorted array search | Binary Search | SearchTemplates.binarySearch() |
| first/last occurrence | Binary Search | SearchTemplates.lowerBound() |
| minimize/maximize | Binary Search on Answer | SearchTemplates.binarySearchOnAnswer() |
| prefix sum | Prefix Sum | SearchTemplates.PrefixSum |

### Tree

| Keywords | Pattern | Template |
|----------|---------|----------|
| level order | BFS | BFSDFSTemplates.levelOrder() |
| inorder/preorder/postorder | DFS | BFSDFSTemplates |
| serialize tree | BFS/DFS | TreeNode.fromArray() |
| lowest common ancestor | Binary Lifting | TreeTemplates.BinaryLiftingLCA |
| path sum | DFS | TreeTemplates |
| tree diameter | DFS | TreeTemplates |
| range sum query | Segment Tree | SegmentTree.query() |
| range minimum query | Sparse Table | SparseTableTemplates |
| tree path queries | HLD | HeavyLightDecomposition |

### Graph

| Keywords | Pattern | Template |
|----------|---------|----------|
| shortest path unweighted | BFS | BFSDFSTemplates.graphBFS() |
| shortest path weighted | Dijkstra | GraphAlgorithms.dijkstra() |
| negative weights | Bellman-Ford | GraphAlgorithms.bellmanFord() |
| all pairs shortest | Floyd-Warshall | GraphAlgorithms.floydWarshall() |
| detect cycle | DFS / DSU | BFSDFSTemplates.hasCycleDirected() |
| connected components | DFS / DSU | BFSDFSTemplates.countComponents() |
| minimum spanning tree | Kruskal / Prim | GraphAlgorithms.kruskalMST() |
| topological sort | Kahn's | GraphAlgorithms.topoSortKahn() |
| number of islands | Grid BFS/DFS | BFSDFSTemplates.gridBFS() |
| bridges, articulation | Tarjan | AdvancedGraphTemplates |
| strongly connected | Tarjan SCC | AdvancedGraphTemplates |
| maximum flow | Dinic | NetworkFlowTemplates |
| bipartite matching | Max Flow | NetworkFlowTemplates |

### Dynamic Programming

| Keywords | Pattern | Template |
|----------|---------|----------|
| longest common subsequence | LCS | DPAndMathTemplates.lcs() |
| longest increasing subsequence | LIS | DPAndMathTemplates.lis() |
| knapsack, subset sum | Knapsack | DPAndMathTemplates.knapsack() |
| coin change | Unbounded Knapsack | DPAndMathTemplates.coinChange() |
| edit distance | Edit Distance | DPAndMathTemplates.editDistance() |
| maximum subarray | Kadane | DPAndMathTemplates.maxSubarraySum() |
| TSP, visit all | Bitmask DP | BitmaskDPTemplates.tsp() |
| count numbers with digit | Digit DP | DigitDPTemplates |
| tree DP | Tree DP | TreeDPTemplates |

### Backtracking

| Keywords | Pattern | Template |
|----------|---------|----------|
| all permutations | Backtracking | BacktrackingTemplates.permute() |
| all combinations | Backtracking | BacktrackingTemplates.combine() |
| all subsets | Backtracking | BacktrackingTemplates.subsets() |
| N-Queens | Constraint | BacktrackingTemplates.solveNQueens() |
| Sudoku | Constraint | BacktrackingTemplates.solveSudoku() |
| generate parentheses | Backtracking | BacktrackingTemplates.generateParenthesis() |
| word search in grid | Grid Backtracking | BacktrackingTemplates.existWordInGrid() |
| combination sum | Backtracking | BacktrackingTemplates.combinationSum() |

### Data Structures

| Keywords | Pattern | Template |
|----------|---------|----------|
| LRU cache | LRU Cache | AdvancedDataStructures.LRUCache |
| LFU cache | LFU Cache | AdvancedDataStructures.LFUCache |
| min stack | Min Stack | AdvancedDataStructures.MinStack |
| prefix tree, trie | Trie | TrieNode |
| time-based store | Design | DesignTemplates.TimeMap |

### Math

| Keywords | Pattern | Template |
|----------|---------|----------|
| GCD | Euclidean | DPAndMathTemplates.gcd() |
| prime numbers | Sieve | DPAndMathTemplates.sieve() |
| primality test | Miller-Rabin | NumberTheoryTemplates.MillerRabinTest |
| factorization | Pollard Rho | NumberTheoryTemplates.PollardRho |
| power mod | Fast Power | DPAndMathTemplates.modPow() |
| C(n,k) mod p | Lucas | CombinatoricsTemplates.LucasTheorem |
| count inversions | Merge Sort | DPAndMathTemplates.countInversions() |

### Union-Find

| Keywords | Pattern | Template |
|----------|---------|----------|
| dynamic connectivity | DSU | DSU |
| cycle in undirected | DSU | DSU.union() |
| graph connectivity | DSU | DSU.connected() |
| MST | Kruskal + DSU | GraphAlgorithms.kruskalMST() |

### Range Queries

| Keywords | Pattern | Template |
|----------|---------|----------|
| range sum immutable | Prefix Sum | SearchTemplates.PrefixSum |
| range sum mutable | Segment Tree | SegmentTree |
| range min static | Sparse Table | SparseTableTemplates |
| range update query | Lazy Segment Tree | LazySegmentTreeTemplates |
| offline range queries | Mo's Algorithm | MoAlgorithmTemplates |

---

## Core Templates

### Two Pointers

```java
int left = 0, right = n - 1;
while (left < right) {
    if (condition) { /* found */ }
    else if (arr[left] + arr[right] < target) left++;
    else right--;
}
```

### Sliding Window

```java
int left = 0;
for (int right = 0; right < n; right++) {
    // add arr[right]
    while (/* invalid */) {
        // remove arr[left]
        left++;
    }
    // update answer
}
```

### Monotonic Stack

```java
Deque<Integer> stack = new ArrayDeque<>();
for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
        result[stack.pop()] = nums[i];
    }
    stack.push(i);
}
```

### Binary Search on Answer

```java
int lo = min, hi = max;
while (lo < hi) {
    int mid = lo + (hi - lo) / 2;
    if (isFeasible(mid)) hi = mid;
    else lo = mid + 1;
}
return lo;
```

### BFS

```java
Queue<Integer> queue = new LinkedList<>();
boolean[] visited = new boolean[n];
queue.offer(start);
visited[start] = true;

while (!queue.isEmpty()) {
    int node = queue.poll();
    for (int next : adj.get(node)) {
        if (!visited[next]) {
            visited[next] = true;
            queue.offer(next);
        }
    }
}
```

### DFS

```java
void dfs(int node, boolean[] visited) {
    visited[node] = true;
    for (int next : adj.get(node)) {
        if (!visited[next]) {
            dfs(next, visited);
        }
    }
}
```

### DP

```java
int[] dp = new int[n + 1];
dp[0] = base;
for (int i = 1; i <= n; i++) {
    dp[i] = /* recurrence */;
}
return dp[n];
```

---

## Advanced Patterns

### Network Flow

| Keywords | Template |
|----------|----------|
| maximum flow | NetworkFlowTemplates |
| bipartite matching | NetworkFlowTemplates.maxBipartiteMatching() |
| assignment problem | NetworkFlowTemplates.hungarianAlgorithm() |

### Advanced DP

| Keywords | Template |
|----------|----------|
| TSP, visit all cities | BitmaskDPTemplates.tsp() |
| count numbers in range | DigitDPTemplates |
| DP optimization | ConvexHullOptimization |

### Game Theory

| Keywords | Template |
|----------|----------|
| two players optimal | GameTheoryTemplates |
| pile of stones | GameTheoryTemplates.nimGame() |
| Grundy number | GameTheoryTemplates.grundyNumber() |

### Advanced Tree

| Keywords | Template |
|----------|----------|
| tree path queries | HeavyLightDecomposition |
| tree distance | CentroidDecompositionTemplates |
| balanced BST | AVLTreeTemplates |

### Advanced String

| Keywords | Template |
|----------|----------|
| longest duplicate substring | SuffixArrayTemplates |
| multiple pattern search | AhoCorasickTemplates |
| all palindromes | PalindromicTreeTemplates |

### Range Queries

| Keywords | Template |
|----------|----------|
| range update + query | LazySegmentTreeTemplates |
| offline queries | MoAlgorithmTemplates |
| version queries | PersistentDataStructures |

### Math

| Keywords | Template |
|----------|----------|
| polynomial multiply | FFTTemplates |
| matrix exponentiation | DPAndMathTemplates.matrixPower() |
| reservoir sampling | DPAndMathTemplates.reservoirSampling() |

---

## Complexity Reference

| Algorithm | Time | Space |
|-----------|------|-------|
| Binary Search | O(log n) | O(1) |
| Two Pointers | O(n) | O(1) |
| Sliding Window | O(n) | O(k) |
| BFS/DFS | O(V+E) | O(V) |
| Dijkstra | O((V+E) log V) | O(V) |
| Bellman-Ford | O(VE) | O(V) |
| Floyd-Warshall | O(V^3) | O(V^2) |
| Kruskal | O(E log E) | O(E) |
| KMP | O(n+m) | O(m) |
| LIS | O(n log n) | O(n) |
| LCS | O(nm) | O(nm) |
| Knapsack | O(nW) | O(W) |
| Segment Tree | O(log n) | O(n) |
| DSU | O(a(n)) | O(n) |
| Bitmask DP | O(n * 2^n) | O(2^n) |
| FFT | O(n log n) | O(n) |

---

## Data Structure Complexity

| Structure | Access | Search | Insert | Delete |
|-----------|--------|--------|--------|--------|
| Array | O(1) | O(n) | O(n) | O(n) |
| Linked List | O(n) | O(n) | O(1) | O(1) |
| Hash Table | - | O(1) | O(1) | O(1) |
| BST | O(log n) | O(log n) | O(log n) | O(log n) |
| Heap | - | O(n) | O(log n) | O(log n) |
| Trie | - | O(m) | O(m) | O(m) |
| Segment Tree | - | O(log n) | O(log n) | - |

---

## Problem Selection Guide

```
Graph with capacities     -> NetworkFlowTemplates
Small sets (n <= 20)      -> BitmaskDPTemplates
Count numbers in range    -> DigitDPTemplates
Two-player game           -> GameTheoryTemplates
Tree path queries         -> HLD / CentroidDecomposition
Multiple pattern match    -> AhoCorasickTemplates
Range updates + queries   -> LazySegmentTreeTemplates
Offline range queries     -> MoAlgorithmTemplates
Version history           -> PersistentDataStructures
Polynomial operations     -> FFTTemplates
Large exponents           -> Matrix Exponentiation
```

---

## Project Navigation

| Category | Path |
|----------|------|
| Data Structures | src/main/java/com/cp/datastructures/ |
| Algorithms | src/main/java/com/cp/algorithms/ |
| Problems | src/main/java/com/cp/problems/ |
| Tests | src/test/java/com/cp/ |
