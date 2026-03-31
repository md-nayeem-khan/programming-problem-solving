# CP Practice — Competitive Programming in Java (Maven + IntelliJ)

A structured Maven project for LeetCode and Codeforces practice with reusable
data structures, algorithm templates, and an automatic test case loading system.

---

## Project Structure

```
cp-practice/
├── pom.xml
└── src/
    ├── main/java/com/cp/
    │   ├── datastructures/
    │   │   ├── TreeNode.java          ← LeetCode binary tree (fromArray, serialize, print)
    │   │   ├── ListNode.java          ← LeetCode linked list
    │   │   ├── GraphNode.java         ← Clone-graph node + adjacency list + matrix
    │   │   ├── TrieNode.java          ← Trie with insert/search/delete
    │   │   ├── DSU.java               ← Union-Find with rank + weighted variant
    │   │   └── SegmentTree.java       ← Segment Tree + Binary Indexed Tree (Fenwick)
    │   ├── algorithms/
    │   │   ├── BFSDFSTemplates.java   ← Graph/Grid/Tree BFS+DFS, cycle detection
    │   │   ├── GraphAlgorithms.java   ← Dijkstra, Bellman-Ford, Floyd, Topo, MST
    │   │   ├── StringAlgorithms.java  ← KMP, Z-function, Rabin-Karp, Manacher
    │   │   ├── SearchTemplates.java   ← Binary search variants, Two Pointers, Sliding Window, Prefix Sum
    │   │   └── DPAndMathTemplates.java← LCS, LIS, Knapsack, Coin Change, GCD, Sieve, Bit tricks
    │   └── problems/
    │       ├── Problem.java           ← @Problem annotation (id, title, tags, difficulty)
    │       ├── BaseSolution.java      ← Base class (debug helper, CF solve() hook)
    │       ├── leetcode/
    │       │   ├── P000_Template.java ← Copy this for every new LeetCode problem
    │       │   └── P001_TwoSum.java   ← Example solution
    │       └── codeforces/
    │           ├── CF_000_X.java      ← Copy this for every new Codeforces problem
    │           └── CF_1900_A.java     ← Example solution
    └── test/
        ├── java/com/cp/
        │   ├── TemplateTests.java     ← Unit tests for all DS + algo templates
        │   └── problems/
        │       ├── leetcode/
        │       │   ├── P000_TemplateTest.java  ← Copy for new tests
        │       │   └── P001_TwoSumTest.java
        │       └── codeforces/
        │           └── CF_1900_A_Test.java
        └── resources/testcases/
            ├── leetcode/
            │   └── 001_two_sum.txt    ← key=value INPUT/OUTPUT blocks
            └── codeforces/
                └── 1900_A.txt         ← raw stdin/stdout blocks
```

---

## Workflow: Adding a New LeetCode Problem

### Step 1 — Create the solution file
Copy `P000_Template.java`, rename to `P{id}_{Title}.java`, fill in `@Problem`.

```java
@Problem(id="200", title="Number of Islands", difficulty=Difficulty.MEDIUM, tags={"BFS","Grid"})
public class P200_NumberOfIslands extends BaseSolution {
    public int numIslands(char[][] grid) {
        // your solution
    }
}
```

### Step 2 — Create the test case file
Create `src/test/resources/testcases/leetcode/200_number_of_islands.txt`:

```
INPUT
grid = [["1","1","1","1","0"],["1","1","0","1","0"],["1","1","0","0","0"],["0","0","0","0","0"]]
OUTPUT
1

INPUT
grid = [["1","1","0","0","0"],["1","1","0","0","0"],["0","0","1","0","0"],["0","0","0","1","1"]]
OUTPUT
3
```

### Step 3 — Create the test class
Copy `P000_TemplateTest.java`, rename, parse your params:

```java
@ParameterizedTest @MethodSource("caseIndices")
void test(int i) {
    TestCase tc = CASES.get(i);
    char[][] grid = InputParser.parseCharGrid(tc.getParam("grid"));
    int expected  = InputParser.parseInt(tc.output);
    assertEquals(expected, solution.numIslands(grid));
}
```

---

## Workflow: Adding a New Codeforces Problem

### Step 1 — Create the solution file
Copy `CF_000_X.java`, rename to `CF_{contestId}_{letter}.java`.

### Step 2 — Create the test case file
Create `src/test/resources/testcases/codeforces/1900_B.txt`:

```
INPUT
3
abc
abc
OUTPUT
0 1 2
---
INPUT
2
ab
ba
OUTPUT
1 0
```

### Step 3 — Create the test class
```java
@Test void testAllCases() {
    SolutionRunner.runAndAssert(new CF_1900_B(), "codeforces/1900_B.txt");
}
```

---

## InputParser Reference

| Method | Input string | Java type |
|---|---|---|
| `parseInt(s)` | `"42"` | `int` |
| `parseIntArray(s)` | `"[1,2,3]"` | `int[]` |
| `parseInt2DArray(s)` | `"[[1,2],[3,4]]"` | `int[][]` |
| `parseString(s)` | `"\"hello\""` | `String` |
| `parseStringArray(s)` | `"[\"a\",\"b\"]"` | `String[]` |
| `parseBoolean(s)` | `"true"` | `boolean` |
| `parseTreeNode(s)` | `"[1,2,3,null,5]"` | `TreeNode` |
| `parseListNode(s)` | `"[1,2,3]"` | `ListNode` |
| `parseIntList(s)` | `"[1,2,3]"` | `List<Integer>` |
| `parseCharGrid(s)` | `"[\"#.\",\".#\"]"` | `char[][]` |

---

## Algorithm Templates Quick Reference

### BFS / DFS
```java
// Graph BFS (shortest distance)
int[] dist = BFSDFSTemplates.graphBFS(adj, src, n);

// Grid BFS (min steps in 2D grid)
int steps = BFSDFSTemplates.gridBFS(grid, sr, sc, er, ec);

// Level-order tree traversal
List<List<Integer>> levels = BFSDFSTemplates.levelOrder(root);

// Tree iterative DFS
List<Integer> inorder = BFSDFSTemplates.inOrder(root);
```

### Graph Shortest Path
```java
int[] dist = GraphAlgorithms.dijkstra(adj, src, n);          // non-negative weights
int[] dist = GraphAlgorithms.bellmanFord(n, edges, src);     // negative weights
int[][] dist = GraphAlgorithms.floydWarshall(distMatrix);    // all-pairs
```

### String Matching
```java
List<Integer> pos = StringAlgorithms.KMP.search(text, pattern);
List<Integer> pos = StringAlgorithms.zSearch(text, pattern);
String pal = StringAlgorithms.longestPalindrome(s);          // Manacher O(n)
```

### Binary Search
```java
int idx = SearchTemplates.binarySearch(nums, target);
int lb  = SearchTemplates.lowerBound(nums, target);    // first >= target
int ub  = SearchTemplates.upperBound(nums, target);    // first > target
// Binary search on answer:
int ans = SearchTemplates.binarySearchOnAnswer(lo, hi, k -> isFeasible(k));
```

### Dynamic Programming
```java
int len  = DPAndMathTemplates.lcs(a, b);
int len  = DPAndMathTemplates.lis(nums);
int val  = DPAndMathTemplates.knapsack(weights, values, capacity);
int min  = DPAndMathTemplates.coinChange(coins, amount);
int dist = DPAndMathTemplates.editDistance(a, b);
```

### Math
```java
long g = DPAndMathTemplates.gcd(a, b);
long p = DPAndMathTemplates.modPow(base, exp, mod);
boolean[] primes = DPAndMathTemplates.sieve(n);
```

---

## Running Tests

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=P001_TwoSumTest

# Run all LeetCode tests
mvn test -Dtest="com.cp.problems.leetcode.*"
```

---

## IntelliJ Tips

- Right-click any `*Test.java` → **Run** to execute just that file.
- Set a **Live Template** in IntelliJ for `cp` → expands to the full problem template.
- Use **Bookmarks** (F11) to mark solutions you want to revisit.
- Install the **LeetCode plugin** for IntelliJ to pull problem statements directly into the IDE.
