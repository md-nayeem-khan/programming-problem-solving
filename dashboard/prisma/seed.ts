import { PrismaClient } from '@prisma/client'

const prisma = new PrismaClient()

// Pattern data extracted from PATTERNS.md
const patterns = [
  // Array and String
  { name: "Two Pointers", category: "Array and String", description: "Use two pointers to traverse array" },
  { name: "Sliding Window", category: "Array and String", description: "Maintain a window that slides through array" },
  { name: "Binary Search", category: "Array and String", description: "Search in sorted data" },
  { name: "Binary Search on Answer", category: "Array and String", description: "Binary search to find optimal value" },
  { name: "Prefix Sum", category: "Array and String", description: "Precompute cumulative sums" },
  { name: "Monotonic Stack", category: "Array and String", description: "Stack maintaining monotonic order" },
  { name: "Monotonic Queue", category: "Array and String", description: "Queue for sliding window maximum/minimum" },
  
  // Tree
  { name: "Tree BFS", category: "Tree", description: "Level-order traversal" },
  { name: "Tree DFS", category: "Tree", description: "Depth-first traversal (inorder, preorder, postorder)" },
  { name: "Binary Search Tree", category: "Tree", description: "BST properties and operations" },
  { name: "Binary Lifting", category: "Tree", description: "Lowest common ancestor" },
  { name: "Segment Tree", category: "Tree", description: "Range query and update" },
  { name: "Sparse Table", category: "Tree", description: "Static range minimum query" },
  { name: "Heavy Light Decomposition", category: "Tree", description: "Tree path queries" },
  
  // Graph
  { name: "Graph BFS", category: "Graph", description: "Shortest path in unweighted graph" },
  { name: "Graph DFS", category: "Graph", description: "Traversal and cycle detection" },
  { name: "Dijkstra", category: "Graph", description: "Shortest path with non-negative weights" },
  { name: "Bellman-Ford", category: "Graph", description: "Shortest path with negative weights" },
  { name: "Floyd-Warshall", category: "Graph", description: "All pairs shortest path" },
  { name: "Topological Sort", category: "Graph", description: "Dependency ordering" },
  { name: "Minimum Spanning Tree", category: "Graph", description: "Kruskal or Prim algorithm" },
  { name: "Union-Find (DSU)", category: "Graph", description: "Disjoint set union for connectivity" },
  { name: "Tarjan SCC", category: "Graph", description: "Strongly connected components" },
  { name: "Network Flow", category: "Graph", description: "Maximum flow and minimum cut" },
  
  // Dynamic Programming
  { name: "Linear DP", category: "Dynamic Programming", description: "1D DP problems" },
  { name: "2D DP", category: "Dynamic Programming", description: "2D DP grid problems" },
  { name: "Longest Common Subsequence", category: "Dynamic Programming", description: "LCS pattern" },
  { name: "Longest Increasing Subsequence", category: "Dynamic Programming", description: "LIS with binary search" },
  { name: "Knapsack", category: "Dynamic Programming", description: "0/1 and unbounded knapsack" },
  { name: "Edit Distance", category: "Dynamic Programming", description: "String transformation" },
  { name: "Kadane's Algorithm", category: "Dynamic Programming", description: "Maximum subarray sum" },
  { name: "Bitmask DP", category: "Dynamic Programming", description: "DP with state compression" },
  { name: "Digit DP", category: "Dynamic Programming", description: "Count numbers with digit constraints" },
  { name: "Tree DP", category: "Dynamic Programming", description: "DP on tree structures" },
  
  // Backtracking
  { name: "Permutations", category: "Backtracking", description: "Generate all permutations" },
  { name: "Combinations", category: "Backtracking", description: "Generate all combinations" },
  { name: "Subsets", category: "Backtracking", description: "Generate all subsets" },
  { name: "N-Queens", category: "Backtracking", description: "Constraint satisfaction" },
  { name: "Sudoku Solver", category: "Backtracking", description: "Constraint satisfaction" },
  { name: "Word Search", category: "Backtracking", description: "Grid backtracking" },
  
  // Data Structures
  { name: "Stack", category: "Data Structures", description: "LIFO data structure" },
  { name: "Queue", category: "Data Structures", description: "FIFO data structure" },
  { name: "Heap/Priority Queue", category: "Data Structures", description: "Min/max heap operations" },
  { name: "Hash Table", category: "Data Structures", description: "Fast lookup and counting" },
  { name: "Trie", category: "Data Structures", description: "Prefix tree for strings" },
  { name: "LRU Cache", category: "Data Structures", description: "Cache with LRU eviction" },
  { name: "LFU Cache", category: "Data Structures", description: "Cache with LFU eviction" },
  
  // Math
  { name: "GCD/LCM", category: "Math", description: "Euclidean algorithm" },
  { name: "Prime Numbers", category: "Math", description: "Sieve of Eratosthenes" },
  { name: "Fast Power", category: "Math", description: "Modular exponentiation" },
  { name: "Combinatorics", category: "Math", description: "Permutations and combinations" },
  { name: "Matrix Exponentiation", category: "Math", description: "Fast matrix power for DP" },
  
  // Greedy
  { name: "Greedy Algorithm", category: "Greedy", description: "Make locally optimal choices" },
  { name: "Interval Scheduling", category: "Greedy", description: "Merge or schedule intervals" },
  
  // String
  { name: "KMP", category: "String", description: "Pattern matching in linear time" },
  { name: "Manacher", category: "String", description: "Longest palindrome substring" },
  { name: "Rabin-Karp", category: "String", description: "Rolling hash for pattern matching" },
  { name: "Z-Algorithm", category: "String", description: "Pattern matching" },
  { name: "Suffix Array", category: "String", description: "Sorted suffixes" },
  { name: "Aho-Corasick", category: "String", description: "Multiple pattern matching" },
  
  // Range Queries
  { name: "Fenwick Tree (BIT)", category: "Range Queries", description: "Point update, prefix query" },
  { name: "Lazy Segment Tree", category: "Range Queries", description: "Range update and query" },
  { name: "Mo's Algorithm", category: "Range Queries", description: "Offline range queries" },
]

async function main() {
  console.log('🌱 Starting seed...')

  // Clear existing patterns
  await prisma.pattern.deleteMany({})
  console.log('🗑️  Cleared existing patterns')

  // Insert patterns
  for (const pattern of patterns) {
    await prisma.pattern.create({
      data: pattern,
    })
  }

  console.log(`✅ Seeded ${patterns.length} patterns`)
  
  // Display summary
  const patternsByCategory = await prisma.pattern.groupBy({
    by: ['category'],
    _count: true,
  })

  console.log('\n📊 Patterns by category:')
  for (const group of patternsByCategory) {
    console.log(`   ${group.category}: ${group._count} patterns`)
  }
}

main()
  .catch((e) => {
    console.error('❌ Error seeding database:', e)
    process.exit(1)
  })
  .finally(async () => {
    await prisma.$disconnect()
  })
