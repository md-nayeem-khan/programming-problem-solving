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

## 🎯 Problem Selection Guide

### Quick Decision Tree

```
1. What type of problem is it?
   │
   ├─ Array/String manipulation?
   │   ├─ Find pair/triplet with sum → Two Pointers (SearchTemplates)
   │   ├─ Subarray with property → Sliding Window (SearchTemplates)
   │   ├─ Search in sorted/rotated → Binary Search (SearchTemplates)
   │   ├─ Range sum query → Prefix Sum (SearchTemplates.PrefixSum)
   │   ├─ Next greater/smaller → Monotonic Stack (AdvancedDataStructures)
   │   └─ Minimize/maximize answer → Binary Search on Answer (SearchTemplates)
   │
   ├─ Graph problem?
   │   ├─ Shortest path (no negative) → Dijkstra (GraphAlgorithms)
   │   ├─ Shortest path (negative) → Bellman-Ford (GraphAlgorithms)
   │   ├─ All pairs shortest → Floyd-Warshall (GraphAlgorithms)
   │   ├─ Dependency ordering → Topological Sort (GraphAlgorithms)
   │   ├─ Minimum spanning tree → Kruskal/Prim (GraphAlgorithms)
   │   ├─ Connected components → DFS/DSU (BFSDFSTemplates, DSU)
   │   ├─ Detect cycle → DFS or DSU
   │   ├─ Max flow / min cut → NetworkFlowTemplates
   │   └─ Find bridges/SCC → AdvancedGraphTemplates
   │
   ├─ Tree problem?
   │   ├─ Level traversal → BFS (BFSDFSTemplates)
   │   ├─ Path sum/properties → DFS (TreeTemplates)
   │   ├─ Lowest common ancestor → BinaryLiftingTemplates
   │   ├─ Range query on tree → HeavyLightDecomposition
   │   └─ Subtree aggregation → TreeDPTemplates
   │
   ├─ Dynamic Programming?
   │   ├─ Sequence (LCS, LIS) → DPAndMathTemplates
   │   ├─ Knapsack/subset sum → DPAndMathTemplates
   │   ├─ Tree DP → TreeDPTemplates
   │   ├─ Small n (≤20) visit all → BitmaskDPTemplates
   │   └─ Count numbers with digit constraint → DigitDPTemplates
   │
   ├─ Generate all possibilities?
   │   ├─ All permutations → BacktrackingTemplates.permute()
   │   ├─ All combinations → BacktrackingTemplates.combine()
   │   ├─ All subsets → BacktrackingTemplates.subsets()
   │   └─ Constraint satisfaction → BacktrackingTemplates (N-Queens, Sudoku)
   │
   └─ Range query?
       ├─ Static, immutable → Prefix Sum or SparseTableTemplates
       ├─ Point update + query → SegmentTree or FenwickTreeTemplates
       └─ Range update + query → LazySegmentTreeTemplates
```

---

### By Constraint Size

| Constraint | Approach | Template |
|------------|----------|----------|
| **n ≤ 10** | Brute force / Bitmask DP | `BitmaskDPTemplates` |
| **n ≤ 20** | Bitmask DP / Meet in Middle | `BitmaskDPTemplates`, `SearchTemplates` |
| **n ≤ 500** | O(n³) OK - Floyd-Warshall | `GraphAlgorithms` |
| **n ≤ 5,000** | O(n²) OK - Basic DP | `DPAndMathTemplates` |
| **n ≤ 100,000** | O(n log n) - Sorting, Heap, Seg Tree | `SortingTemplates`, `HeapTemplates` |
| **n ≤ 10⁶** | O(n) - Linear, prefix sum | `SearchTemplates` |
| **n ≤ 10⁸** | O(log n) - Binary search | `SearchTemplates` |
| **n ≤ 10¹⁸** | O(log n) - Math, Binary lifting | `DPAndMathTemplates`, `MatrixTemplates` |

---

### By Problem Pattern

#### Optimization Problems ("Find minimum/maximum...")

| Pattern | Signal Keywords | Template |
|---------|-----------------|----------|
| Binary Search on Answer | "minimize maximum", "maximize minimum" | `SearchTemplates.binarySearchOnAnswer()` |
| Greedy | "minimum moves", "can achieve" | `GreedyTemplates` |
| DP | "minimum cost", "maximum profit" | `DPAndMathTemplates` |
| Shortest Path | "minimum distance", "cheapest" | `GraphAlgorithms.dijkstra()` |

#### Counting Problems ("Count/Find all...")

| Pattern | Signal Keywords | Template |
|---------|-----------------|----------|
| Combinatorics | "how many ways", "count paths" | `CombinatoricsTemplates` |
| DP Counting | "count subsequences", "count partitions" | `DPAndMathTemplates` |
| Digit DP | "count numbers in range [L,R]" | `DigitDPTemplates` |
| Backtracking | "generate all", "find all combinations" | `BacktrackingTemplates` |

#### Existence Problems ("Can we achieve...?")

| Pattern | Signal Keywords | Template |
|---------|-----------------|----------|
| Greedy | "can reach", "possible to" | `GreedyTemplates` |
| DP | "is it possible", "subset sum" | `DPAndMathTemplates` |
| BFS/DFS | "can traverse", "path exists" | `BFSDFSTemplates` |

---

### By Interview Frequency (FAANG)

#### Tier 1: Must Know (70% of problems)

| Category | % of Interviews | Key Templates |
|----------|-----------------|---------------|
| Arrays/Strings | 25% | `SearchTemplates`, `ArrayPatterns` |
| Trees | 20% | `TreeTemplates`, `BSTTemplates`, `BFSDFSTemplates` |
| DP | 20% | `DPAndMathTemplates`, `BacktrackingTemplates` |
| Graphs | 15% | `BFSDFSTemplates`, `GraphAlgorithms` |
| Linked Lists | 10% | `LinkedListTemplates` |

#### Tier 2: Know Well (25% of problems)

| Category | % of Interviews | Key Templates |
|----------|-----------------|---------------|
| Heaps | 8% | `HeapTemplates` |
| Greedy | 8% | `GreedyTemplates` |
| String Matching | 5% | `StringAlgorithms` |
| Range Queries | 4% | `SegmentTree`, `FenwickTreeTemplates` |

#### Tier 3: Advanced (5% of problems - Senior+)

| Category | % of Interviews | Key Templates |
|----------|-----------------|---------------|
| Bitmask DP | 2% | `BitmaskDPTemplates` |
| Network Flow | 1% | `NetworkFlowTemplates` |
| Advanced Graph | 1% | `AdvancedGraphTemplates` |
| Advanced String | 1% | `SuffixArrayTemplates`, `AhoCorasickTemplates` |

---

### Company-Specific Focus

#### Google (Algorithm-Heavy)
```
Priority 1: Bitmask DP, Graph (SCC, bridges), Binary Lifting
Priority 2: Digit DP, String algorithms (KMP, suffix array)
Priority 3: Convex Hull Optimization, FFT
```

#### Meta/Facebook (Pattern Recognition)
```
Priority 1: Backtracking (40% chance), Tree DP
Priority 2: Linked Lists (reverse k-group), Greedy
Priority 3: Design patterns (LRU cache)
```

#### Amazon (Practical + System Design)
```
Priority 1: Heaps (top K), Graphs (MST, shortest path)
Priority 2: DP (standard patterns), Greedy
Priority 3: Design (rate limiters), Network Flow
```

#### Microsoft (Data Structures)
```
Priority 1: Trees (BST, AVL), Linked Lists
Priority 2: Arrays (matrix operations), Heaps
Priority 3: Segment Trees, Design patterns
```

---

### Problem Recognition Cheatsheet

| See This | Think This | Template |
|----------|------------|----------|
| "Two numbers sum to target" | Two pointers or hashmap | `SearchTemplates` |
| "Longest substring without repeating" | Sliding window | `SearchTemplates` |
| "Search in rotated sorted array" | Modified binary search | `SearchTemplates` |
| "Next greater element" | Monotonic stack | `AdvancedDataStructures` |
| "Maximum in sliding window" | Monotonic deque | `AdvancedDataStructures` |
| "Number of islands" | Grid BFS/DFS | `BFSDFSTemplates` |
| "Shortest path in weighted graph" | Dijkstra | `GraphAlgorithms` |
| "Course schedule" | Topological sort | `GraphAlgorithms` |
| "Connect all cities minimum cost" | MST (Kruskal/Prim) | `GraphAlgorithms` |
| "Level order traversal" | BFS | `BFSDFSTemplates` |
| "Path sum in tree" | DFS | `TreeTemplates` |
| "Lowest common ancestor" | Binary lifting or Euler tour | `BinaryLiftingTemplates` |
| "Kth smallest in BST" | Inorder traversal | `BSTTemplates` |
| "Longest common subsequence" | 2D DP | `DPAndMathTemplates` |
| "Longest increasing subsequence" | Binary search DP | `DPAndMathTemplates` |
| "Coin change" | Unbounded knapsack | `DPAndMathTemplates` |
| "Edit distance" | 2D DP | `DPAndMathTemplates` |
| "House robber on tree" | Tree DP | `TreeDPTemplates` |
| "Visit all nodes (n ≤ 20)" | Bitmask DP | `BitmaskDPTemplates` |
| "Count numbers with digit property" | Digit DP | `DigitDPTemplates` |
| "All permutations" | Backtracking | `BacktrackingTemplates` |
| "Generate valid parentheses" | Backtracking | `BacktrackingTemplates` |
| "Kth largest element" | Heap or quick select | `HeapTemplates` |
| "Merge K sorted lists" | Min heap | `HeapTemplates` |
| "Jump game" | Greedy | `GreedyTemplates` |
| "Meeting rooms needed" | Greedy + sorting | `GreedyTemplates` |
| "Range sum with updates" | Segment tree or Fenwick | `SegmentTree`, `FenwickTreeTemplates` |
| "Range minimum (static)" | Sparse table | `SparseTableTemplates` |
| "Find pattern in text" | KMP | `StringAlgorithms` |
| "Longest palindrome substring" | Manacher | `StringAlgorithms` |
| "Maximum flow" | Dinic | `NetworkFlowTemplates` |
| "Bipartite matching" | Max flow or Hungarian | `NetworkFlowTemplates` |
| "Two player game optimal play" | Game theory / Minimax | `GameTheoryTemplates` |
| "LRU/LFU cache" | LinkedHashMap or custom | `DesignTemplates` |

---