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

## Data Structures

| Structure | File | Description |
|-----------|------|-------------|
| Binary Tree | `TreeNode.java` | LeetCode-style with fromArray, serialize, print |
| Linked List | `ListNode.java` | Singly linked list node |
| Graph | `GraphNode.java` | Adjacency list + matrix representations |
| Trie | `TrieNode.java` | Insert, search, delete, prefix operations |
| Union-Find | `DSU.java` | With rank compression + weighted variant |
| Segment Tree | `SegmentTree.java` | Range queries + Fenwick Tree |

## Algorithm Templates

### Foundation (Interview Essentials)

| Template | Key Algorithms |
|----------|----------------|
| `SearchTemplates` | Binary search, two pointers, sliding window |
| `BFSDFSTemplates` | Graph/grid/tree traversal, cycle detection |
| `GraphAlgorithms` | Dijkstra, Bellman-Ford, Floyd-Warshall, Topological Sort, MST |
| `DPAndMathTemplates` | LCS, LIS, knapsack, matrix exponentiation |
| `BacktrackingTemplates` | Permutations, combinations, N-Queens, Sudoku |
| `StringAlgorithms` | KMP, Z-function, Rabin-Karp, Manacher |
| `HeapTemplates` | Top K, median finder, K-way merge |
| `TreeTemplates` | LCA, diameter, path sum, binary lifting |
| `SortingTemplates` | Quick sort/select, counting/radix sort |
| `GreedyTemplates` | Jump game, gas station, activity selection |

### Advanced (Senior Level)

| Template | Key Algorithms |
|----------|----------------|
| `NetworkFlowTemplates` | Ford-Fulkerson, Dinic's, Hungarian |
| `BitmaskDPTemplates` | TSP, Hamiltonian path, subset DP |
| `AdvancedGraphTemplates` | Tarjan SCC, bridges, articulation points, Eulerian |
| `DigitDPTemplates` | Digit constraint problems |
| `GameTheoryTemplates` | Nim, Sprague-Grundy theorem |
| `FFTTemplates` | Fast polynomial multiplication |

### Expert (Competitive Programming)

| Template | Key Algorithms |
|----------|----------------|
| `LazySegmentTreeTemplates` | Range updates with lazy propagation |
| `SuffixArrayTemplates` | Suffix array, LCP array |
| `AhoCorasickTemplates` | Multi-pattern string matching |
| `HeavyLightDecomposition` | Tree path queries O(log²n) |
| `CentroidDecompositionTemplates` | Tree divide and conquer |
| `MoAlgorithmTemplates` | Offline range queries O(n√n) |
| `PersistentDataStructures` | Time-travel/versioned queries |
| `ConvexHullOptimization` | DP optimization techniques |

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

## Complexity Reference

| Category | Algorithm | Time | Space |
|----------|-----------|------|-------|
| **Search** | Binary Search | O(log n) | O(1) |
| | Two Pointers | O(n) | O(1) |
| **Graph** | BFS/DFS | O(V + E) | O(V) |
| | Dijkstra | O((V+E) log V) | O(V) |
| | Floyd-Warshall | O(V³) | O(V²) |
| **String** | KMP | O(n + m) | O(m) |
| | Manacher | O(n) | O(n) |
| **DP** | LIS (optimal) | O(n log n) | O(n) |
| | Knapsack | O(n × W) | O(W) |
| **Tree** | LCA (Binary Lifting) | O(log n) | O(n log n) |
| | Segment Tree Query | O(log n) | O(n) |

## Common Patterns

1. **Two Pointers** — Sorted arrays, palindromes, container problems
2. **Sliding Window** — Subarray/substring with constraints
3. **Monotonic Stack** — Next greater element, histogram
4. **Binary Search** — Search space reduction, answer binary search
5. **BFS/DFS** — Traversal, shortest path, connectivity
6. **Dynamic Programming** — Overlapping subproblems, optimal substructure
7. **Backtracking** — Permutations, combinations, constraint satisfaction
8. **Union-Find** — Dynamic connectivity, MST (Kruskal)

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
