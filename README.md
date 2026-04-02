# Programming Problem Solving

A comprehensive Java toolkit for competitive programming and technical interview preparation. Built with reusable data structures, algorithm templates, and an automatic test case system.

**Target:** Crack Big Tech coding interviews (FAANG/MAANG) with 5+ years of experience.

## Prerequisites

- Java 17+
- Maven 3.8+
- IntelliJ IDEA (recommended)

## Quick Start

```bash
# Clone and build
git clone <repository-url>
cd programming-problem-solving
mvn clean install

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=P001_TwoSumTest
```

## Project Structure

```
src/
├── main/java/com/cp/
│   ├── algorithms/           # 45+ algorithm templates
│   ├── datastructures/       # Core data structures
│   ├── problems/             # LeetCode & Codeforces solutions
│   │   ├── leetcode/
│   │   └── codeforces/
│   └── testcases/            # Auto test case loading system
└── test/
    ├── java/com/cp/          # Unit tests
    └── resources/testcases/  # Test case files
```

---

# 📚 Algorithm & Data Structure Catalog

## Overview: Preparation Tiers

| Tier | Coverage | Target Audience | Study Focus |
|------|----------|-----------------|-------------|
| **Tier 1: Core Fundamentals** | 70% of interviews | All candidates | Master these first |
| **Tier 2: Standard Interview** | 25% of interviews | Mid-Senior roles | Know well |
| **Tier 3: Advanced** | 5% of interviews | Senior+/FAANG L5+ | Recognize patterns |
| **Tier 4: Competitive** | Rare in interviews | CP enthusiasts | Optional |

---

## 🎯 Tier 1: Core Fundamentals (Must Master First)

> **Study Order:** Complete each category before moving to the next. These form the foundation.

### 1.1 Arrays & Strings (Start Here)
*Prerequisite: None*

| Template | Key Techniques | LeetCode Examples |
|----------|----------------|-------------------|
| `SearchTemplates` | Two pointers, binary search, sliding window | Two Sum, Container With Most Water, Longest Substring |
| `ArrayPatterns` | Prefix sum, difference array, Kadane's algorithm | Subarray Sum, Range Sum Query, Maximum Subarray |
| `SortingTemplates` | Quick sort/select, merge sort, counting sort | Kth Largest, Sort Colors, Merge Intervals |

**Key Patterns to Master:**
- ✅ Two Pointers (opposite ends, same direction)
- ✅ Sliding Window (fixed size, variable size)
- ✅ Binary Search (search space, answer space)
- ✅ Prefix Sum (range queries, subarray problems)

### 1.2 Basic Data Structures
*Prerequisite: 1.1 Arrays & Strings*

| Template | Key Techniques | LeetCode Examples |
|----------|----------------|-------------------|
| `LinkedListTemplates` | Reversal, cycle detection, merge, fast/slow pointers | Reverse Linked List, Linked List Cycle, Merge Two Lists |
| `HeapTemplates` | Top K, median finder, K-way merge | Kth Largest, Find Median, Merge K Sorted |
| `AdvancedDataStructures` | Stack, queue, monotonic stack/queue | Valid Parentheses, Daily Temperatures, Sliding Window Max |

**Data Structures to Know:**
| Structure | File | Use Case | Frequency |
|-----------|------|----------|-----------|
| Binary Tree | `TreeNode.java` | Tree traversal problems | ⭐⭐⭐⭐⭐ |
| Linked List | `ListNode.java` | Pointer manipulation | ⭐⭐⭐⭐⭐ |
| Stack/Queue | `AdvancedDataStructures.java` | LIFO/FIFO patterns | ⭐⭐⭐⭐⭐ |
| Heap/PriorityQueue | `HeapTemplates.java` | Top K, scheduling | ⭐⭐⭐⭐⭐ |
| HashMap/HashSet | (Java built-in) | O(1) lookup | ⭐⭐⭐⭐⭐ |

### 1.3 Trees & Binary Search Trees
*Prerequisite: 1.2 Basic Data Structures*

| Template | Key Techniques | LeetCode Examples |
|----------|----------------|-------------------|
| `TreeTemplates` | DFS/BFS traversals, path sum, diameter, LCA | Max Depth, Path Sum, Diameter, Lowest Common Ancestor |
| `BSTTemplates` | Search, insert, delete, validate, inorder properties | Validate BST, Kth Smallest, Inorder Successor |
| `BFSDFSTemplates` | Level order, zigzag, vertical order | Level Order Traversal, Vertical Order |

**Key Patterns:**
- ✅ Recursive DFS (preorder, inorder, postorder)
- ✅ BFS level-order traversal
- ✅ BST properties (in-order = sorted)
- ✅ Tree DP (subtree calculations)

### 1.4 Graphs
*Prerequisite: 1.3 Trees & BST*

| Template | Key Techniques | LeetCode Examples |
|----------|----------------|-------------------|
| `BFSDFSTemplates` | Grid BFS/DFS, connected components, cycle detection | Number of Islands, Clone Graph, Course Schedule |
| `GraphAlgorithms` | Dijkstra, topological sort, Kruskal/Prim MST | Network Delay Time, Course Schedule II, Min Cost to Connect |
| `DSU.java` | Union-Find with path compression + rank | Redundant Connection, Accounts Merge, Graph Valid Tree |

**Graph Representations:**
| Structure | File | Use Case | Frequency |
|-----------|------|----------|-----------|
| Adjacency List | `GraphNode.java` | Sparse graphs, most problems | ⭐⭐⭐⭐⭐ |
| Adjacency Matrix | `GraphNode.java` | Dense graphs, Floyd-Warshall | ⭐⭐⭐ |
| Union-Find (DSU) | `DSU.java` | Dynamic connectivity, MST | ⭐⭐⭐⭐ |

### 1.5 Dynamic Programming
*Prerequisite: 1.4 Graphs (recursion comfort)*

| Template | Key Techniques | LeetCode Examples |
|----------|----------------|-------------------|
| `DPAndMathTemplates` | LCS, LIS, knapsack, coin change, edit distance | Longest Common Subsequence, Coin Change, Edit Distance |
| `BacktrackingTemplates` | Permutations, combinations, subsets, constraint satisfaction | Permutations, Subsets, N-Queens, Sudoku |

**DP Patterns (Learn in Order):**
1. **1D DP** → Climbing Stairs, House Robber, Decode Ways
2. **2D DP** → Unique Paths, Min Path Sum, Dungeon Game
3. **Sequence DP** → LCS, LIS, Edit Distance
4. **Knapsack** → 0/1 Knapsack, Unbounded Knapsack, Partition Equal
5. **String DP** → Palindrome Partitioning, Word Break
6. **State Machine DP** → Stock Problems, Paint House

---

## 🔧 Tier 2: Standard Interview (Know Well)

> **Prerequisite:** Complete Tier 1. These appear in ~25% of FAANG interviews.

### 2.1 Advanced Tree Techniques
*Prerequisite: 1.3 Trees*

| Template | Key Techniques | When to Use |
|----------|----------------|-------------|
| `BinaryLiftingTemplates` | Fast LCA in O(log n), K-th ancestor | Multiple LCA queries, ancestor problems |
| `TreeDPTemplates` | Tree DP patterns | Max path, tree distances, subtree queries |

### 2.2 String Algorithms
*Prerequisite: 1.1 Arrays & Strings*

| Template | Key Techniques | When to Use |
|----------|----------------|-------------|
| `StringAlgorithms` | KMP, Z-function, Rabin-Karp, Manacher | Pattern matching, palindrome detection |
| `TrieNode.java` | Prefix tree operations | Autocomplete, word search, prefix queries |

### 2.3 Range Query Structures
*Prerequisite: 1.5 DP (understanding of state)*

| Template | Key Techniques | When to Use |
|----------|----------------|-------------|
| `SegmentTree.java` | Point update, range sum/min/max | Mutable array range queries |
| `FenwickTreeTemplates` | Efficient prefix sum updates | Counting inversions, range sum |
| `SparseTableTemplates` | Static RMQ in O(1) query | Immutable range minimum |

### 2.4 Greedy & Design
*Prerequisite: 1.5 DP*

| Template | Key Techniques | When to Use |
|----------|----------------|-------------|
| `GreedyTemplates` | Interval scheduling, jump game, gas station | Optimization with local choices |
| `DesignTemplates` | LRU/LFU cache, time-based storage | System design coding rounds |
| `SystemDesignDataStructures` | Distributed patterns, rate limiters | Senior+ design problems |

---

## 🚀 Tier 3: Advanced (Senior+ / FAANG L5+)

> **Prerequisite:** Strong command of Tier 1-2. Rarely asked but differentiates senior candidates.

### 3.1 Advanced Graph Algorithms
| Template | Key Algorithms | Interview Frequency |
|----------|----------------|---------------------|
| `AdvancedGraphTemplates` | Tarjan SCC, bridges, articulation points | Medium |
| `NetworkFlowTemplates` | Dinic's max flow, bipartite matching | Low |
| `AStarSearchTemplates` | Heuristic pathfinding | Low |

### 3.2 Advanced DP
| Template | Key Algorithms | Interview Frequency |
|----------|----------------|---------------------|
| `BitmaskDPTemplates` | TSP, subset DP, Hamiltonian path | Medium |
| `DigitDPTemplates` | Count numbers with digit constraints | Low |
| `ConvexHullOptimization` | DP optimization techniques | Low |

### 3.3 Advanced Range Queries
| Template | Key Algorithms | Interview Frequency |
|----------|----------------|---------------------|
| `LazySegmentTreeTemplates` | Range updates with lazy propagation | Medium |
| `MoAlgorithmTemplates` | Offline range queries O(n√n) | Rare |

### 3.4 Math & Number Theory
| Template | Key Algorithms | Interview Frequency |
|----------|----------------|---------------------|
| `MatrixTemplates` | Matrix exponentiation | Medium |
| `NumberTheoryTemplates` | Miller-Rabin, Pollard Rho, CRT | Low |
| `CombinatoricsTemplates` | Lucas theorem, Catalan numbers | Low |

---

## 🏆 Tier 4: Competitive Programming (Rarely in Interviews)

> **For CP enthusiasts and rare specialized roles.**

### 4.1 Advanced Tree Decomposition
| Template | Key Algorithms |
|----------|----------------|
| `HeavyLightDecomposition` | Tree path queries O(log²n) |
| `CentroidDecompositionTemplates` | Tree divide and conquer |
| `CartesianTreeTemplates` | Range queries optimization |

### 4.2 Advanced Structures
| Template | Key Algorithms |
|----------|----------------|
| `AVLTreeTemplates` | Self-balancing BST |
| `PersistentDataStructures` | Time-travel/versioned queries |

### 4.3 Advanced String
| Template | Key Algorithms |
|----------|----------------|
| `SuffixArrayTemplates` | Suffix array, LCP array |
| `AhoCorasickTemplates` | Multi-pattern matching |
| `PalindromicTreeTemplates` | Palindrome tree (eertree) |

### 4.4 Specialized
| Template | Key Algorithms |
|----------|----------------|
| `FFTTemplates` | Fast polynomial multiplication |
| `GameTheoryTemplates` | Nim, Sprague-Grundy theorem |
| `MinCutTemplates` | Stoer-Wagner, Gomory-Hu tree |

---

# 📅 Interview Preparation Plans

## Quick Reference: What to Study Based on Timeline

| Time Available | Focus Areas | Target Coverage |
|----------------|-------------|-----------------|
| **1 week** | Tier 1.1-1.3 only | Arrays, Strings, Trees basics |
| **2 weeks** | Tier 1 complete | Core fundamentals |
| **1 month** | Tier 1 + Tier 2.2-2.4 | Standard interview prep |
| **2 months** | Tier 1-2 complete | Comprehensive prep |
| **3+ months** | Tier 1-3 | Senior/Staff level prep |

## 8-Week Structured Study Plan

### Learning Philosophy
- **Master fundamentals before advancing** - Each phase builds on the previous
- **Template → Practice → Internalize** - Read code, solve problems, then implement from memory
- **Spaced repetition** - Review previous topics while learning new ones

### Phase 1: Array & String Mastery (Week 1-2)

> **Goal:** Handle 40% of interview problems confidently

| Days | Topic | Template | Practice Problems | Mastery Check |
|------|-------|----------|-------------------|---------------|
| 1-2 | Two Pointers | `SearchTemplates` | Two Sum, 3Sum, Container With Most Water | Solve without hints |
| 3-4 | Binary Search | `SearchTemplates` | Search Rotated Array, First/Last Position, Peak | Know all 3 variants |
| 5-6 | Sliding Window | `SearchTemplates` | Longest Substring, Min Window Substring | Fixed vs variable size |
| 7-8 | Prefix Sum | `ArrayPatterns` | Subarray Sum Equals K, Range Sum Query | Can derive formulas |
| 9-10 | Sorting | `SortingTemplates` | Sort Colors, Merge Intervals, Meeting Rooms | Quick select for Kth |
| 11-12 | Stacks | `AdvancedDataStructures` | Valid Parentheses, Daily Temperatures, Histogram | Monotonic stack pattern |
| 13-14 | Linked Lists | `LinkedListTemplates` | Reverse, Detect Cycle, Merge Two | Fast/slow pointer |

**Phase 1 Checkpoint:** Can you solve a medium array/string problem in 20 minutes?

### Phase 2: Trees & Graphs (Week 3-4)

> **Goal:** Handle graph/tree problems that make up 35% of FAANG interviews

| Days | Topic | Template | Practice Problems | Mastery Check |
|------|-------|----------|-------------------|---------------|
| 15-16 | Binary Tree DFS | `TreeTemplates` | Max Depth, Path Sum, Invert Tree | All 3 traversals |
| 17-18 | Binary Tree BFS | `BFSDFSTemplates` | Level Order, Zigzag, Right Side View | Level-by-level processing |
| 19-20 | BST Properties | `BSTTemplates` | Validate BST, Kth Smallest, LCA | In-order = sorted |
| 21-22 | Tree Construction | `TreeTemplates` | Serialize/Deserialize, Build from Traversals | Can reconstruct trees |
| 23-24 | Graph BFS/DFS | `BFSDFSTemplates` | Number of Islands, Clone Graph, Pacific Atlantic | Grid vs adjacency list |
| 25-26 | Shortest Path | `GraphAlgorithms` | Network Delay Time, Cheapest Flights | Dijkstra implementation |
| 27-28 | Topological Sort | `GraphAlgorithms` | Course Schedule I & II, Alien Dictionary | Detect cycles in DAG |

**Phase 2 Checkpoint:** Can you implement DFS/BFS from memory? Dijkstra?

### Phase 3: Dynamic Programming (Week 5-6)

> **Goal:** Solve DP problems methodically - they appear in 30% of hard problems

| Days | Topic | Template | Practice Problems | Mastery Check |
|------|-------|----------|-------------------|---------------|
| 29-30 | 1D DP Basics | `DPAndMathTemplates` | Climbing Stairs, House Robber, Decode Ways | Identify state & transition |
| 31-32 | 2D DP Grid | `DPAndMathTemplates` | Unique Paths, Min Path Sum, Dungeon Game | Optimize space to O(n) |
| 33-34 | Sequence DP | `DPAndMathTemplates` | LCS, LIS, Edit Distance | O(n log n) LIS |
| 35-36 | Knapsack Variants | `DPAndMathTemplates` | 0/1 Knapsack, Coin Change, Partition | Bounded vs unbounded |
| 37-38 | String DP | `DPAndMathTemplates` | Word Break, Palindrome Partition | Substring problems |
| 39-40 | State Machine DP | `DPAndMathTemplates` | Best Time to Buy/Sell Stock (all) | Multiple states |
| 41-42 | Backtracking | `BacktrackingTemplates` | Permutations, Subsets, N-Queens | Pruning strategies |

**Phase 3 Checkpoint:** Can you identify DP patterns and write recurrence relations?

### Phase 4: Advanced & Integration (Week 7-8)

> **Goal:** Round out knowledge, practice under time pressure

| Days | Topic | Template | Practice Problems | Mastery Check |
|------|-------|----------|-------------------|---------------|
| 43-44 | Union-Find | `DSU` | Redundant Connection, Accounts Merge | Path compression |
| 45-46 | Heaps | `HeapTemplates` | Kth Largest, Find Median, Merge K | K-way merge pattern |
| 47-48 | Trie | `TrieNode` | Implement Trie, Word Search II | Prefix operations |
| 49-50 | Greedy | `GreedyTemplates` | Jump Game, Gas Station, Task Scheduler | Prove correctness |
| 51-52 | Design | `DesignTemplates` | LRU Cache, LFU Cache, Twitter Design | OOP + data structures |
| 53-56 | Mock Interviews | All templates | Random mix, timed | 45 min per problem |

### Daily Routine (60 min/day)
```
📖 Template Review (15 min)
   - Read one template file
   - Understand key patterns
   
💻 Problem Practice (35 min)
   - Solve 1-2 problems from that topic
   - Time yourself
   
📝 Reflection (10 min)
   - Note patterns and edge cases
   - Mark problems to revisit
```

### Weekly Review Schedule
- **Day 7 of each week:** Review all topics from that week
- **Solve 2-3 problems from previous weeks** (spaced repetition)

---

## Test Case System

Auto-load test cases from files:

```
src/test/resources/testcases/
├── leetcode/
│   └── 001_two_sum.txt      # key=value format
└── codeforces/
    └── 1900_A.txt           # stdin/stdout format
```

**LeetCode format:**
```
INPUT=nums=[2,7,11,15], target=9
OUTPUT=[0,1]
```

**Codeforces format:**
```
5
1 2 3 4 5
---
15
```

## Creating a New Solution

### LeetCode

1. Copy `P000_Template.java` to `P{number}_{Name}.java`
2. Copy `P000_TemplateTest.java` for tests
3. Add test cases to `resources/testcases/leetcode/`

### Codeforces

1. Copy `CF_000_X.java` to `CF_{contest}_{problem}.java`
2. Extend `BaseSolution` and implement `solve()`
3. Add test cases to `resources/testcases/codeforces/`

---

## Complexity Reference

| Category | Algorithm | Time | Space |
|----------|-----------|------|-------|
| **Search** | Binary Search | O(log n) | O(1) |
| | Two Pointers | O(n) | O(1) |
| | Sliding Window | O(n) | O(1) |
| **Graph** | BFS/DFS | O(V + E) | O(V) |
| | Dijkstra (heap) | O((V+E) log V) | O(V) |
| | Bellman-Ford | O(VE) | O(V) |
| | Floyd-Warshall | O(V³) | O(V²) |
| | Topological Sort | O(V + E) | O(V) |
| | MST (Kruskal) | O(E log E) | O(V) |
| **String** | KMP | O(n + m) | O(m) |
| | Rabin-Karp | O(n + m) | O(1) |
| | Manacher | O(n) | O(n) |
| | Z-algorithm | O(n) | O(n) |
| **DP** | LIS (optimal) | O(n log n) | O(n) |
| | LCS | O(nm) | O(nm) |
| | Knapsack | O(n × W) | O(W) |
| | Edit Distance | O(nm) | O(min(n,m)) |
| **Tree** | LCA (Binary Lifting) | O(log n) query | O(n log n) |
| | Segment Tree | O(log n) | O(n) |
| | Fenwick Tree | O(log n) | O(n) |
| **Data Structures** | DSU (Union-Find) | O(α(n)) ≈ O(1) | O(n) |
| | Trie Operations | O(m) | O(alphabet × nodes) |
| | Heap Insert/Delete | O(log n) | O(n) |

---

## Quick Interview Checklist

### By Topic (% of Interview Questions)

| Topic | Coverage | Must-Know Patterns |
|-------|----------|-------------------|
| **Arrays & Strings** | 25% | Two pointers, sliding window, binary search, prefix sum |
| **Trees** | 20% | DFS/BFS traversals, path sum, LCA, validate BST |
| **Dynamic Programming** | 20% | LCS, LIS, knapsack, edit distance, 2D grid DP |
| **Graphs** | 15% | BFS/DFS, Dijkstra, topological sort, Union-Find |
| **Linked Lists** | 10% | Reverse, cycle detection, fast/slow pointers, merge |
| **Data Structures** | 10% | Monotonic stack, heap (top K), hash map/set |

---

## Common Patterns by Frequency

### Very High (Practice Daily)
1. **Two Pointers** — Sorted arrays, palindromes, container problems
2. **Sliding Window** — Subarray/substring with constraints
3. **Binary Search** — Search space reduction, answer binary search
4. **BFS/DFS** — Traversal, shortest path, connectivity
5. **Dynamic Programming** — Overlapping subproblems, optimal substructure

### High (Practice Weekly)
6. **Monotonic Stack** — Next greater element, histogram
7. **Backtracking** — Permutations, combinations, constraint satisfaction
8. **Union-Find** — Dynamic connectivity, MST (Kruskal)
9. **Greedy** — Interval scheduling, optimization
10. **Heap** — Top K, merge K sorted

### Medium (Know the Template)
11. **Trie** — Prefix operations
12. **Topological Sort** — DAG ordering
13. **Binary Lifting** — Fast tree queries
14. **KMP / String matching** — Pattern search
15. **Segment Tree** — Range queries

---

## Interview Pro Tips

### Time Management
- **Easy (5-10 min)**: Array/String basics, simple iterations
- **Medium (15-25 min)**: BFS/DFS, basic DP, two pointers
- **Hard (30-45 min)**: Advanced DP, graph algorithms, optimization

### Problem-Solving Framework
1. **Clarify (2 min)**: Ask about constraints, edge cases, examples
2. **Approach (5 min)**: Brute force → Optimize → Choose algorithm
3. **Code (15 min)**: Write clean, modular code
4. **Test (5 min)**: Walk through test cases, edge cases

### Common Edge Cases
```java
// Always check:
- Empty input: n = 0, arr = []
- Single element: n = 1
- Duplicates: [1,1,1,1]
- Negative numbers: [-5, -3, 2]
- Integer overflow: use long
- Null pointers: validate inputs
```

### Optimization Checklist
- ❓ Can I use a hash map to reduce time complexity?
- ❓ Can I sort first to enable two pointers?
- ❓ Is there a sliding window pattern?
- ❓ Can I use binary search on answer space?
- ❓ Can I trade space for time (memoization)?
- ❓ Is there a greedy approach?

---

## Running Tests

```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=P001_TwoSumTest

# All LeetCode tests
mvn test -Dtest="com.cp.problems.leetcode.*"

# With coverage report
mvn test jacoco:report
# View: target/site/jacoco/index.html
```

## IDE Setup (IntelliJ)

1. **Import:** File → Open → Select `pom.xml` → Open as Project
2. **Live Template:** Settings → Editor → Live Templates → Add `cp` template
3. **Run Tests:** Right-click test file → Run
4. **LeetCode Plugin:** Marketplace → Install "LeetCode Editor"

## License

MIT
