package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Bitmask Dynamic Programming - CRITICAL for Google L5+ (20%), Meta (15%)
 * 
 * Essential for:
 * - Traveling Salesman Problem (TSP)
 * - Hamiltonian path/cycle
 * - State compression using bitmasks
 * - Subset enumeration with DP
 * - Assignment problems with state tracking
 * 
 * Interview Questions:
 * - LeetCode 847: Shortest Path Visiting All Nodes
 * - LeetCode 1434: Number of Ways to Wear Different Hats
 * - LeetCode 1986: Minimum Number of Work Sessions to Finish the Tasks
 * - LeetCode 1723: Find Minimum Time to Finish All Jobs
 * - LeetCode 1125: Smallest Sufficient Team
 * 
 * Key Concept: Use integers as bitmasks to represent sets
 * - Bit i is 1 if element i is in the set
 * - Supports O(1) add/remove/check operations
 * - Can represent 2^n states compactly
 * 
 * Companies: Google (5 stars), Meta (4 stars), Amazon (3 stars)
 */
public class BitmaskDPTemplates {
    
    // ========== BITMASK OPERATIONS ==========
    
    /**
     * Check if i-th bit is set
     */
    public static boolean isSet(int mask, int i) {
        return (mask & (1 << i)) != 0;
    }
    
    /**
     * Set i-th bit to 1
     */
    public static int setBit(int mask, int i) {
        return mask | (1 << i);
    }
    
    /**
     * Clear i-th bit (set to 0)
     */
    public static int clearBit(int mask, int i) {
        return mask & ~(1 << i);
    }
    
    /**
     * Toggle i-th bit
     */
    public static int toggleBit(int mask, int i) {
        return mask ^ (1 << i);
    }
    
    /**
     * Count number of set bits (population count)
     */
    public static int countBits(int mask) {
        return Integer.bitCount(mask);
    }
    
    /**
     * Get lowest set bit
     */
    public static int lowestBit(int mask) {
        return mask & (-mask);
    }
    
    /**
     * Remove lowest set bit
     */
    public static int removeLowestBit(int mask) {
        return mask & (mask - 1);
    }
    
    /**
     * Iterate through all subsets of a mask
     * Example: mask = 5 (binary 101) -> subsets: 5, 4, 1, 0
     */
    public static List<Integer> allSubsets(int mask) {
        List<Integer> subsets = new ArrayList<>();
        for (int subset = mask; ; subset = (subset - 1) & mask) {
            subsets.add(subset);
            if (subset == 0) break;
        }
        return subsets;
    }
    
    // ========== TRAVELING SALESMAN PROBLEM (TSP) ==========
    
    /**
     * Traveling Salesman Problem - Find shortest path visiting all cities exactly once
     * 
     * State: dp[mask][i] = minimum cost to visit cities in 'mask' ending at city i
     * Transition: dp[new_mask][j] = min(dp[mask][i] + dist[i][j]) for all i in mask
     * 
     * @param dist distance matrix where dist[i][j] = distance from city i to city j
     * @return minimum cost to visit all cities and return to start
     */
    @Complexity(time = "O(2^n * n^2)", space = "O(2^n * n)")
    public static int tsp(int[][] dist) {
        int n = dist.length;
        int allVisited = (1 << n) - 1; // All bits set
        
        // dp[mask][i] = min cost to visit cities in mask, ending at i
        int[][] dp = new int[1 << n][n];
        for (int[] row : dp) Arrays.fill(row, Integer.MAX_VALUE / 2);
        
        // Start at city 0
        dp[1][0] = 0;
        
        // Iterate through all possible masks (sets of visited cities)
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                if (!isSet(mask, last)) continue;
                if (dp[mask][last] == Integer.MAX_VALUE / 2) continue;
                
                // Try visiting next city
                for (int next = 0; next < n; next++) {
                    if (isSet(mask, next)) continue; // Already visited
                    
                    int newMask = setBit(mask, next);
                    dp[newMask][next] = Math.min(dp[newMask][next], 
                                                 dp[mask][last] + dist[last][next]);
                }
            }
        }
        
        // Find minimum cost ending at any city, then return to start
        int minCost = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (dp[allVisited][i] != Integer.MAX_VALUE / 2) {
                minCost = Math.min(minCost, dp[allVisited][i] + dist[i][0]);
            }
        }
        
        return minCost == Integer.MAX_VALUE ? -1 : minCost;
    }
    
    /**
     * TSP - Return the actual path (not just cost)
     */
    @Complexity(time = "O(2^n * n^2)", space = "O(2^n * n)")
    public static List<Integer> tspPath(int[][] dist) {
        int n = dist.length;
        int allVisited = (1 << n) - 1;
        
        int[][] dp = new int[1 << n][n];
        int[][] parent = new int[1 << n][n];
        
        for (int[] row : dp) Arrays.fill(row, Integer.MAX_VALUE / 2);
        for (int[] row : parent) Arrays.fill(row, -1);
        
        dp[1][0] = 0;
        
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                if (!isSet(mask, last) || dp[mask][last] == Integer.MAX_VALUE / 2) continue;
                
                for (int next = 0; next < n; next++) {
                    if (isSet(mask, next)) continue;
                    
                    int newMask = setBit(mask, next);
                    int newCost = dp[mask][last] + dist[last][next];
                    
                    if (newCost < dp[newMask][next]) {
                        dp[newMask][next] = newCost;
                        parent[newMask][next] = last;
                    }
                }
            }
        }
        
        // Find best ending city
        int minCost = Integer.MAX_VALUE;
        int lastCity = -1;
        for (int i = 0; i < n; i++) {
            if (dp[allVisited][i] != Integer.MAX_VALUE / 2) {
                int totalCost = dp[allVisited][i] + dist[i][0];
                if (totalCost < minCost) {
                    minCost = totalCost;
                    lastCity = i;
                }
            }
        }
        
        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        int mask = allVisited;
        int current = lastCity;
        
        while (current != -1) {
            path.add(current);
            int prev = parent[mask][current];
            if (prev != -1) {
                mask = clearBit(mask, current);
            }
            current = prev;
        }
        
        Collections.reverse(path);
        return path;
    }
    
    // ========== HAMILTONIAN PATH ==========
    
    /**
     * Check if Hamiltonian path exists (path visiting all nodes exactly once)
     * 
     * @param graph adjacency matrix
     * @return true if Hamiltonian path exists
     */
    @Complexity(time = "O(2^n * n^2)", space = "O(2^n * n)")
    public static boolean hasHamiltonianPath(int[][] graph) {
        int n = graph.length;
        int allVisited = (1 << n) - 1;
        
        // dp[mask][i] = true if we can reach state mask ending at node i
        boolean[][] dp = new boolean[1 << n][n];
        
        // Start from each node
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = true;
        }
        
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                if (!dp[mask][last]) continue;
                
                // Try extending path to next node
                for (int next = 0; next < n; next++) {
                    if (isSet(mask, next)) continue;
                    if (graph[last][next] == 0) continue; // No edge
                    
                    int newMask = setBit(mask, next);
                    dp[newMask][next] = true;
                }
            }
        }
        
        // Check if we can reach all visited state
        for (int i = 0; i < n; i++) {
            if (dp[allVisited][i]) return true;
        }
        
        return false;
    }
    
    // ========== LEETCODE 847: SHORTEST PATH VISITING ALL NODES ==========
    
    /**
     * LeetCode 847: Shortest Path Visiting All Nodes
     * 
     * Find shortest path that visits all nodes. Can start/end at any node.
     * Can revisit nodes and edges.
     * 
     * State: (mask, node) = set of visited nodes + current node
     * Use BFS to find shortest path
     */
    @Complexity(time = "O(2^n * n^2)", space = "O(2^n * n)")
    public static int shortestPathLength(int[][] graph) {
        int n = graph.length;
        if (n == 1) return 0;
        
        int allVisited = (1 << n) - 1;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[1 << n][n];
        
        // Start from each node
        for (int i = 0; i < n; i++) {
            queue.offer(new int[]{1 << i, i, 0}); // {mask, node, distance}
            visited[1 << i][i] = true;
        }
        
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int mask = curr[0];
            int node = curr[1];
            int dist = curr[2];
            
            // Try visiting neighbors
            for (int next : graph[node]) {
                int newMask = mask | (1 << next);
                
                if (newMask == allVisited) {
                    return dist + 1;
                }
                
                if (!visited[newMask][next]) {
                    visited[newMask][next] = true;
                    queue.offer(new int[]{newMask, next, dist + 1});
                }
            }
        }
        
        return -1;
    }
    
    // ========== LEETCODE 1434: NUMBER OF WAYS TO WEAR DIFFERENT HATS ==========
    
    /**
     * LeetCode 1434: Number of Ways to Wear Different Hats to Each Other
     * 
     * Each person has a list of hats they like. Find number of ways to assign
     * one hat to each person such that each hat is assigned to at most one person.
     * 
     * State: dp[hat][mask] = ways to assign hats 1..hat to people in mask
     */
    @Complexity(time = "O(40 * 2^n * n)", space = "O(40 * 2^n)")
    public static int numberWays(List<List<Integer>> hats) {
        int MOD = 1_000_000_007;
        int n = hats.size();
        int maxHat = 40;
        
        // hatToPeople[hat] = list of people who like this hat
        List<Integer>[] hatToPeople = new List[maxHat + 1];
        for (int i = 0; i <= maxHat; i++) {
            hatToPeople[i] = new ArrayList<>();
        }
        
        for (int person = 0; person < n; person++) {
            for (int hat : hats.get(person)) {
                hatToPeople[hat].add(person);
            }
        }
        
        int allAssigned = (1 << n) - 1;
        
        // dp[mask] = ways to assign hats to people in mask
        long[] dp = new long[1 << n];
        dp[0] = 1; // Base case: 0 people assigned = 1 way (do nothing)
        
        // Process each hat
        for (int hat = 1; hat <= maxHat; hat++) {
            // Process in reverse to avoid using updated values
            for (int mask = allAssigned; mask >= 0; mask--) {
                if (dp[mask] == 0) continue;
                
                // Try assigning this hat to each person who likes it
                for (int person : hatToPeople[hat]) {
                    if (isSet(mask, person)) continue; // Person already has hat
                    
                    int newMask = setBit(mask, person);
                    dp[newMask] = (dp[newMask] + dp[mask]) % MOD;
                }
            }
        }
        
        return (int) dp[allAssigned];
    }
    
    // ========== LEETCODE 1986: MINIMUM WORK SESSIONS ==========
    
    /**
     * LeetCode 1986: Minimum Number of Work Sessions to Finish the Tasks
     * 
     * Given tasks with durations and a session time limit, find minimum
     * number of sessions needed to complete all tasks.
     * 
     * State: dp[mask] = (min_sessions, time_used_in_last_session)
     */
    @Complexity(time = "O(2^n * n)", space = "O(2^n)")
    public static int minSessions(int[] tasks, int sessionTime) {
        int n = tasks.length;
        int allDone = (1 << n) - 1;
        
        // dp[mask] = {min sessions, time used in last session}
        int[][] dp = new int[1 << n][2];
        for (int[] row : dp) {
            row[0] = Integer.MAX_VALUE / 2;
            row[1] = 0;
        }
        
        dp[0][0] = 1;
        dp[0][1] = 0;
        
        for (int mask = 0; mask < (1 << n); mask++) {
            if (dp[mask][0] == Integer.MAX_VALUE / 2) continue;
            
            for (int task = 0; task < n; task++) {
                if (isSet(mask, task)) continue;
                
                int newMask = setBit(mask, task);
                int taskDuration = tasks[task];
                
                // Try adding to current session
                if (dp[mask][1] + taskDuration <= sessionTime) {
                    int sessions = dp[mask][0];
                    int timeUsed = dp[mask][1] + taskDuration;
                    
                    if (sessions < dp[newMask][0] || 
                        (sessions == dp[newMask][0] && timeUsed < dp[newMask][1])) {
                        dp[newMask][0] = sessions;
                        dp[newMask][1] = timeUsed;
                    }
                }
                
                // Try starting new session
                int sessions = dp[mask][0] + 1;
                int timeUsed = taskDuration;
                
                if (sessions < dp[newMask][0] || 
                    (sessions == dp[newMask][0] && timeUsed < dp[newMask][1])) {
                    dp[newMask][0] = sessions;
                    dp[newMask][1] = timeUsed;
                }
            }
        }
        
        return dp[allDone][0];
    }
    
    // ========== SUBSET SUM WITH DP ==========
    
    /**
     * Count number of subsets with given sum using bitmask DP
     * 
     * @param nums array of numbers
     * @param target target sum
     * @return number of subsets with sum = target
     */
    @Complexity(time = "O(2^n * n)", space = "O(2^n)")
    public static int countSubsetsWithSum(int[] nums, int target) {
        int n = nums.length;
        Map<Integer, Integer> sumCount = new HashMap<>();
        
        // Iterate through all subsets
        for (int mask = 0; mask < (1 << n); mask++) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                if (isSet(mask, i)) {
                    sum += nums[i];
                }
            }
            if (sum == target) {
                sumCount.put(mask, sumCount.getOrDefault(mask, 0) + 1);
            }
        }
        
        return sumCount.size();
    }
    
    // ========== ASSIGNMENT WITH STATE COMPRESSION ==========
    
    /**
     * Assign n tasks to n workers, each worker does exactly one task
     * Minimize total cost
     * 
     * State: dp[mask] = min cost to assign tasks in mask to workers 0..count(mask)-1
     * 
     * @param cost cost[worker][task] = cost for worker to do task
     * @return minimum total cost
     */
    @Complexity(time = "O(2^n * n)", space = "O(2^n)")
    public static int minCostAssignment(int[][] cost) {
        int n = cost.length;
        int[] dp = new int[1 << n];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;
        
        for (int mask = 0; mask < (1 << n); mask++) {
            int worker = countBits(mask); // Next worker to assign
            
            for (int task = 0; task < n; task++) {
                if (isSet(mask, task)) continue; // Task already assigned
                
                int newMask = setBit(mask, task);
                dp[newMask] = Math.min(dp[newMask], dp[mask] + cost[worker][task]);
            }
        }
        
        return dp[(1 << n) - 1];
    }
    
    // ========== SUM OVER SUBSETS (SOS) DP ==========
    
    /**
     * Sum Over Subsets DP - CRITICAL OPTIMIZATION TECHNIQUE
     * 
     * Problem: Given array F[mask], compute for each mask:
     * G[mask] = sum of F[submask] for all submasks of mask
     * 
     * Naive: O(3^n) - iterate all submasks
     * SOS DP: O(n * 2^n) - dramatically faster!
     * 
     * KEY INSIGHT:
     * Build answer iteratively by considering each bit position
     * dp[mask][i] = sum over submasks differing only in first i bits
     * 
     * Applications:
     * - Subset sum queries on all subsets
     * - Compatible pair counting
     * - Intersection/union of multiple sets
     * - Google L5+: ~8-10% frequency
     * 
     * Interview Problems:
     * - Count pairs with AND = 0
     * - Maximum XOR subset
     * - Compatible assignments
     */
    
    /**
     * SOS DP Template - Sum over all submasks
     * 
     * For each mask, compute sum of values of all its submasks
     * 
     * Time: O(n * 2^n) instead of O(3^n)
     * Space: O(n * 2^n)
     * 
     * Example:
     * F = [1, 2, 3, 4] (values for masks 0,1,2,3)
     * G[3] = F[0] + F[1] + F[2] + F[3] = 1+2+3+4 = 10
     * (submasks of 11b are: 00b, 01b, 10b, 11b)
     */
    @Complexity(time = "O(n * 2^n)", space = "O(n * 2^n)")
    public static int[] sumOverSubsets(int[] F) {
        int n = Integer.numberOfTrailingZeros(Integer.highestOneBit(F.length));
        int N = 1 << n;
        int[][] dp = new int[N][n + 1];
        
        // Base case: no bits considered yet
        for (int mask = 0; mask < N; mask++) {
            dp[mask][0] = F[mask];
        }
        
        // Iterate over bit positions
        for (int i = 1; i <= n; i++) {
            for (int mask = 0; mask < N; mask++) {
                // If i-th bit is 0, only consider current submask
                dp[mask][i] = dp[mask][i - 1];
                
                // If i-th bit is 1, also add submask without this bit
                if ((mask & (1 << (i - 1))) != 0) {
                    dp[mask][i] += dp[mask ^ (1 << (i - 1))][i - 1];
                }
            }
        }
        
        // Extract final answers
        int[] result = new int[N];
        for (int mask = 0; mask < N; mask++) {
            result[mask] = dp[mask][n];
        }
        return result;
    }
    
    /**
     * Space-Optimized SOS DP
     * 
     * Time: O(n * 2^n)
     * Space: O(2^n)
     */
    @Complexity(time = "O(n * 2^n)", space = "O(2^n)")
    public static int[] sumOverSubsetsOptimized(int[] F) {
        int n = Integer.numberOfTrailingZeros(Integer.highestOneBit(F.length));
        int N = 1 << n;
        int[] dp = F.clone();
        
        // Process each bit position
        for (int i = 0; i < n; i++) {
            for (int mask = 0; mask < N; mask++) {
                // If i-th bit is set, add contribution from mask without this bit
                if ((mask & (1 << i)) != 0) {
                    dp[mask] += dp[mask ^ (1 << i)];
                }
            }
        }
        
        return dp;
    }
    
    /**
     * Sum Over Supersets - Reverse of SOS DP
     * 
     * For each mask, compute sum of all masks that contain it as submask
     * 
     * Time: O(n * 2^n)
     * Space: O(2^n)
     */
    @Complexity(time = "O(n * 2^n)", space = "O(2^n)")
    public static int[] sumOverSupersets(int[] F) {
        int n = Integer.numberOfTrailingZeros(Integer.highestOneBit(F.length));
        int N = 1 << n;
        int[] dp = F.clone();
        
        // Process each bit position (reverse order compared to SOS)
        for (int i = 0; i < n; i++) {
            for (int mask = N - 1; mask >= 0; mask--) {
                // If i-th bit is NOT set, add contribution from mask with this bit
                if ((mask & (1 << i)) == 0) {
                    dp[mask] += dp[mask | (1 << i)];
                }
            }
        }
        
        return dp;
    }
    
    /**
     * APPLICATION 1: Count pairs with AND = 0
     * 
     * Problem: Given array of integers, count pairs (i,j) where arr[i] & arr[j] = 0
     * 
     * Approach:
     * 1. For each number, count how many numbers are submasks of its complement
     * 2. A number y has AND=0 with x if y is a submask of ~x (complement of x)
     * 3. Use SOS DP to efficiently count submasks
     * 
     * Time: O(n + 2^bits * bits) where bits = max bit position
     * Space: O(2^bits)
     */
    @Complexity(time = "O(n + 2^k * k)", space = "O(2^k)")
    public static long countPairsWithANDZero(int[] arr) {
        // Find maximum value to determine bit count
        int maxVal = 0;
        for (int num : arr) maxVal = Math.max(maxVal, num);
        if (maxVal == 0) return (long) arr.length * (arr.length - 1) / 2;
        
        int bits = 32 - Integer.numberOfLeadingZeros(maxVal);
        int N = 1 << bits;
        int maxMask = N - 1; // All bits set
        
        // Count frequency of each number
        int[] freq = new int[N];
        for (int num : arr) {
            if (num < N) freq[num]++;
        }
        
        // SOS DP to get count of all submasks for each mask
        int[] subsetCount = sumOverSubsetsOptimized(freq);
        
        // For each number, count how many can pair with it (AND = 0)
        // A number y has AND=0 with x if (x & y) = 0
        // This means y must be a submask of ~x
        long count = 0;
        for (int num : arr) {
            int complement = maxMask ^ num; // Flip all bits in range
            count += subsetCount[complement];
        }
        
        // Each pair counted twice, subtract self-pairs (num & num might be 0 if num=0)
        // For num=0, it pairs with itself and all other nums
        long selfPairs = freq[0]; // Only 0&0=0
        return (count - selfPairs) / 2;
    }
    
    /**
     * APPLICATION 2: Maximum XOR of Subset
     * 
     * Problem: Find maximum XOR of any subset of array
     * 
     * Approach: Linear basis (Gaussian elimination on XOR)
     * 
     * Time: O(n * log(MAX_VAL))
     * Space: O(log(MAX_VAL))
     */
    @Complexity(time = "O(n * log C)", space = "O(log C)")
    public static int maximumXORSubset(int[] arr) {
        int[] basis = new int[30]; // Max 30 bits for int
        
        for (int num : arr) {
            int cur = num;
            for (int i = 29; i >= 0; i--) {
                if ((cur & (1 << i)) == 0) continue;
                
                if (basis[i] == 0) {
                    basis[i] = cur;
                    break;
                }
                
                cur ^= basis[i];
            }
        }
        
        // Build maximum XOR from basis
        int maxXOR = 0;
        for (int i = 29; i >= 0; i--) {
            maxXOR = Math.max(maxXOR, maxXOR ^ basis[i]);
        }
        
        return maxXOR;
    }
    
    /**
     * APPLICATION 3: Compatible Assignments
     * 
     * Problem: LeetCode 1125 - Smallest Sufficient Team
     * Given people with skills, find minimum team covering all skills
     * 
     * Approach: Bitmask DP with SOS optimization
     * 
     * Time: O(n * 2^skills)
     * Space: O(2^skills)
     */
    @Complexity(time = "O(n * 2^k)", space = "O(2^k)")
    public static int[] smallestSufficientTeam(String[] req_skills, 
                                              List<List<String>> people) {
        int n = req_skills.length;
        int N = 1 << n;
        
        // Map skills to indices
        Map<String, Integer> skillMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            skillMap.put(req_skills[i], i);
        }
        
        // Convert people to skill masks
        int[] personSkills = new int[people.size()];
        for (int i = 0; i < people.size(); i++) {
            for (String skill : people.get(i)) {
                if (skillMap.containsKey(skill)) {
                    personSkills[i] |= (1 << skillMap.get(skill));
                }
            }
        }
        
        // DP: dp[mask] = minimum team size to cover skills in mask
        int[] dp = new int[N];
        int[] parent = new int[N];
        int[] parentMask = new int[N];
        Arrays.fill(dp, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dp[0] = 0;
        
        for (int mask = 0; mask < N; mask++) {
            if (dp[mask] == Integer.MAX_VALUE) continue;
            
            for (int i = 0; i < people.size(); i++) {
                int newMask = mask | personSkills[i];
                if (dp[mask] + 1 < dp[newMask]) {
                    dp[newMask] = dp[mask] + 1;
                    parent[newMask] = i;
                    parentMask[newMask] = mask;
                }
            }
        }
        
        // Reconstruct team
        List<Integer> team = new ArrayList<>();
        int mask = N - 1;
        while (mask > 0) {
            team.add(parent[mask]);
            mask = parentMask[mask];
        }
        
        return team.stream().mapToInt(i -> i).toArray();
    }
    
    /**
     * APPLICATION 4: Intersection Count Over All Pairs
     * 
     * Problem: Given array of sets (as bitmasks), count total intersection size
     * 
     * Approach: SOS DP to count how many sets contain each subset
     * 
     * Time: O(n + 2^k * k)
     * Space: O(2^k)
     */
    @Complexity(time = "O(n + 2^k * k)", space = "O(2^k)")
    public static long totalIntersectionSize(int[] sets) {
        // Calculate sum of intersection sizes across all pairs of sets
        long total = 0;
        for (int i = 0; i < sets.length; i++) {
            for (int j = i + 1; j < sets.length; j++) {
                // Intersection is bitwise AND, count bits in result
                int intersection = sets[i] & sets[j];
                total += Integer.bitCount(intersection);
            }
        }
        return total;
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        // Example 1: TSP
        int[][] dist = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        System.out.println("TSP min cost: " + tsp(dist)); // 80
        System.out.println("TSP path: " + tspPath(dist));
        
        // Example 2: Hamiltonian path
        int[][] graph = {
            {0, 1, 1, 1},
            {1, 0, 1, 0},
            {1, 1, 0, 1},
            {1, 0, 1, 0}
        };
        System.out.println("Has Hamiltonian path: " + hasHamiltonianPath(graph));
        
        // Example 3: LeetCode 847
        int[][] graph2 = {{1,2,3},{0},{0},{0}};
        System.out.println("Shortest path visiting all: " + shortestPathLength(graph2)); // 4
        
        // Example 4: Bitmask operations
        int mask = 0b1010; // Binary: 1010 (decimal 10)
        System.out.println("Bit 1 set: " + isSet(mask, 1)); // true
        System.out.println("Bit 2 set: " + isSet(mask, 2)); // false
        System.out.println("Set bit 2: " + Integer.toBinaryString(setBit(mask, 2))); // 1110
        System.out.println("Count bits: " + countBits(mask)); // 2
        
        // Example 5: SOS DP
        int[] F = {1, 2, 3, 4}; // Values for masks 0,1,2,3
        int[] sosResult = sumOverSubsetsOptimized(F);
        System.out.println("\nSOS DP results:");
        for (int i = 0; i < sosResult.length; i++) {
            System.out.println("Mask " + i + " (" + Integer.toBinaryString(i) + "): " + sosResult[i]);
        }
        // mask 3 (11b) should sum submasks: 00,01,10,11 = 1+2+3+4 = 10
        
        // Example 6: Count pairs with AND = 0
        int[] arr = {1, 2, 3, 4, 5};
        long pairs = countPairsWithANDZero(arr);
        System.out.println("\nPairs with AND=0: " + pairs);
        
        // Example 7: Maximum XOR subset
        int[] xorArr = {2, 4, 5, 7};
        int maxXOR = maximumXORSubset(xorArr);
        System.out.println("Maximum XOR subset: " + maxXOR); // Expected: 7
    }
}
