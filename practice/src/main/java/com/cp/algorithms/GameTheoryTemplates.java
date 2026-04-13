package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * Game Theory - Nim Game & Grundy Numbers - HIGH PRIORITY for Google (10%)
 * 
 * Essential for:
 * - Nim game and variants
 * - Sprague-Grundy theorem
 * - Impartial game analysis
 * - Win/loss game state determination
 * - Multi-pile game problems
 * 
 * Interview Questions:
 * - LeetCode 292: Nim Game
 * - LeetCode 1690: Stone Game VII
 * - LeetCode 1872: Stone Game VIII
 * - LeetCode 1510: Stone Game IV
 * - Multi-pile Nim variants
 * 
 * Key Concepts:
 * - Nim Sum: XOR of all pile sizes
 * - Grundy Number: Mex (minimum excludant) of reachable states
 * - Winning position: Grundy number != 0
 * - Losing position: Grundy number == 0
 * 
 * Companies: Google (4/5 stars), Competitive Programming (5/5 stars)
 */
public class GameTheoryTemplates {
    
    // ========== NIM GAME BASICS ==========
    
    /**
     * Classic Nim Game
     * 
     * Rules: Two players alternate removing any number of stones from a single pile
     * Loser: Player who cannot make a move
     * 
     * Strategy: First player wins if XOR of all piles != 0
     * 
     * @param piles array of pile sizes
     * @return true if first player can win
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static boolean canWinNim(int[] piles) {
        int xorSum = 0;
        for (int pile : piles) {
            xorSum ^= pile;
        }
        return xorSum != 0;
    }
    
    /**
     * Single pile Nim - LeetCode 292
     * 
     * Rules: Remove 1-3 stones from pile
     * Loser: Player who takes the last stone
     * 
     * Strategy: Lose if n % 4 == 0
     */
    @Complexity(time = "O(1)", space = "O(1)")
    public static boolean canWinNimSingle(int n) {
        return n % 4 != 0;
    }
    
    /**
     * Misère Nim (losing variant)
     * 
     * Rules: Same as Nim, but player who takes last stone LOSES
     * 
     * Strategy: 
     * - If all piles have size 1: First player wins if odd number of piles
     * - Otherwise: First player wins if XOR != 0
     */
    @Complexity(time = "O(n)", space = "O(1)")
    public static boolean canWinMisereNim(int[] piles) {
        int xorSum = 0;
        boolean allOnes = true;
        
        for (int pile : piles) {
            xorSum ^= pile;
            if (pile > 1) allOnes = false;
        }
        
        if (allOnes) {
            return piles.length % 2 == 1;
        }
        
        return xorSum != 0;
    }
    
    // ========== GRUNDY NUMBERS (SPRAGUE-GRUNDY THEOREM) ==========
    
    /**
     * Calculate Grundy number (nimber) for a game state
     * 
     * Grundy number = mex(reachable states)
     * mex = minimum excludant (smallest non-negative integer not in set)
     * 
     * @param state current game state
     * @param memo memoization map
     * @param getNextStates function to get all reachable next states
     * @return Grundy number (0 = losing position, >0 = winning)
     */
    @Complexity(time = "O(states × transitions)", space = "O(states)")
    public static int grundyNumber(int state, Map<Integer, Integer> memo, 
                                    java.util.function.Function<Integer, List<Integer>> getNextStates) {
        if (memo.containsKey(state)) {
            return memo.get(state);
        }
        
        List<Integer> nextStates = getNextStates.apply(state);
        
        // Base case: no moves available
        if (nextStates.isEmpty()) {
            memo.put(state, 0);
            return 0;
        }
        
        // Calculate Grundy numbers of all reachable states
        Set<Integer> reachable = new HashSet<>();
        for (int next : nextStates) {
            reachable.add(grundyNumber(next, memo, getNextStates));
        }
        
        // Mex: find smallest non-negative integer not in reachable
        int mex = 0;
        while (reachable.contains(mex)) {
            mex++;
        }
        
        memo.put(state, mex);
        return mex;
    }
    
    /**
     * Calculate mex (minimum excludant) of a set
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static int mex(Set<Integer> set) {
        int mex = 0;
        while (set.contains(mex)) {
            mex++;
        }
        return mex;
    }
    
    /**
     * Calculate mex of an array
     */
    public static int mex(int[] arr) {
        Set<Integer> set = new HashSet<>();
        for (int x : arr) {
            set.add(x);
        }
        return mex(set);
    }
    
    // ========== LEETCODE 1510: STONE GAME IV ==========
    
    /**
     * LeetCode 1510: Stone Game IV
     * 
     * Rules: Two players alternate removing a perfect square number of stones
     * Winner: Player who takes the last stone
     * 
     * @param n number of stones
     * @return true if first player can win
     */
    @Complexity(time = "O(n√n)", space = "O(n)")
    public static boolean winnerSquareGame(int n) {
        // dp[i] = true if current player can win with i stones
        boolean[] dp = new boolean[n + 1];
        
        for (int i = 1; i <= n; i++) {
            // Try all perfect square moves
            for (int k = 1; k * k <= i; k++) {
                int remove = k * k;
                // If opponent loses after we remove k^2 stones, we win
                if (!dp[i - remove]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * Stone Game IV using Grundy numbers
     */
    @Complexity(time = "O(n√n)", space = "O(n)")
    public static boolean winnerSquareGameGrundy(int n) {
        Map<Integer, Integer> memo = new HashMap<>();
        
        java.util.function.Function<Integer, List<Integer>> getNextStates = state -> {
            List<Integer> next = new ArrayList<>();
            for (int k = 1; k * k <= state; k++) {
                next.add(state - k * k);
            }
            return next;
        };
        
        return grundyNumber(n, memo, getNextStates) != 0;
    }
    
    // ========== LEETCODE 1690: STONE GAME VII ==========
    
    /**
     * LeetCode 1690: Stone Game VII
     * 
     * Rules: Players alternate removing leftmost or rightmost stone
     * Score: Sum of remaining stones after removal
     * Goal: Maximize (your score - opponent score)
     * 
     * @param stones array of stone values
     * @return maximum score difference
     */
    @Complexity(time = "O(n²)", space = "O(n²)")
    public static int stoneGameVII(int[] stones) {
        int n = stones.length;
        
        // Prefix sum for quick range sum
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        // dp[i][j] = max score difference for range [i, j]
        int[][] dp = new int[n][n];
        
        // Length of subarray
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                int j = i + len - 1;
                
                int rangeSum = prefixSum[j + 1] - prefixSum[i];
                
                // Remove left stone: score = sum - stones[i]
                int removeLeft = (rangeSum - stones[i]) - dp[i + 1][j];
                
                // Remove right stone: score = sum - stones[j]
                int removeRight = (rangeSum - stones[j]) - dp[i][j - 1];
                
                dp[i][j] = Math.max(removeLeft, removeRight);
            }
        }
        
        return dp[0][n - 1];
    }
    
    // ========== LEETCODE 1872: STONE GAME VIII ==========
    
    /**
     * LeetCode 1872: Stone Game VIII
     * 
     * Rules: Combine first k stones (k >= 2) into one with sum value
     * Score: The sum value
     * Goal: Maximize (your score - opponent score)
     * 
     * @param stones array of stone values
     * @return maximum score difference
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static int stoneGameVIII(int[] stones) {
        int n = stones.length;
        
        // Prefix sum
        int[] prefixSum = new int[n];
        prefixSum[0] = stones[0];
        for (int i = 1; i < n; i++) {
            prefixSum[i] = prefixSum[i - 1] + stones[i];
        }
        
        // dp[i] = max score difference when we can merge first i+1 stones
        // Work backwards
        int dp = prefixSum[n - 1]; // Base case: merge all stones
        
        for (int i = n - 2; i >= 1; i--) {
            // Choice: merge first i+1 stones now, or let opponent play
            dp = Math.max(dp, prefixSum[i] - dp);
        }
        
        return dp;
    }
    
    // ========== GENERALIZED GAME FRAMEWORK ==========
    
    /**
     * Generic game state for DP-based game solving
     */
    static class GameState {
        int state;
        int player; // 0 or 1
        
        public GameState(int state, int player) {
            this.state = state;
            this.player = player;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof GameState)) return false;
            GameState g = (GameState) o;
            return state == g.state && player == g.player;
        }
        
        @Override
        public int hashCode() {
            return state * 2 + player;
        }
    }
    
    /**
     * Solve game using minimax with memoization
     * 
     * @param state current state
     * @param player current player (0 or 1)
     * @param getMoves function to get available moves
     * @param isTerminal check if state is terminal
     * @param evaluate evaluate terminal state
     * @return best score for current player
     */
    public static int solveGame(int state, int player,
                                 java.util.function.BiFunction<Integer, Integer, List<Integer>> getMoves,
                                 java.util.function.Predicate<Integer> isTerminal,
                                 java.util.function.ToIntFunction<Integer> evaluate,
                                 Map<GameState, Integer> memo) {
        GameState gs = new GameState(state, player);
        
        if (memo.containsKey(gs)) {
            return memo.get(gs);
        }
        
        if (isTerminal.test(state)) {
            int value = evaluate.applyAsInt(state);
            memo.put(gs, value);
            return value;
        }
        
        List<Integer> moves = getMoves.apply(state, player);
        
        if (moves.isEmpty()) {
            memo.put(gs, 0);
            return 0;
        }
        
        int bestScore = (player == 0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        
        for (int nextState : moves) {
            int score = solveGame(nextState, 1 - player, getMoves, isTerminal, evaluate, memo);
            
            if (player == 0) {
                bestScore = Math.max(bestScore, score);
            } else {
                bestScore = Math.min(bestScore, score);
            }
        }
        
        memo.put(gs, bestScore);
        return bestScore;
    }
    
    // ========== COMPOSITE GAME (MULTIPLE INDEPENDENT GAMES) ==========
    
    /**
     * Solve composite game (multiple independent games)
     * 
     * By Sprague-Grundy theorem: XOR all Grundy numbers
     * If XOR != 0, first player wins
     */
    @Complexity(time = "O(n × states)", space = "O(states)")
    public static boolean canWinCompositeGame(int[] games,
                                               java.util.function.Function<Integer, List<Integer>> getNextStates) {
        int xorSum = 0;
        Map<Integer, Integer> memo = new HashMap<>();
        
        for (int game : games) {
            xorSum ^= grundyNumber(game, memo, getNextStates);
        }
        
        return xorSum != 0;
    }
    
    // ========== GREEN HACKENBUSH (ADVANCED) ==========
    
    /**
     * Green Hackenbush on trees
     * 
     * Rules: Remove edges from tree, lose if tree disconnects from ground
     * Grundy number = XOR of (distance_to_ground + 1) for all leaves
     */
    @Complexity(time = "O(n)", space = "O(n)")
    public static int greenHackenbushGrundy(int root, List<Integer>[] tree) {
        int[] depth = new int[tree.length];
        dfsDepth(root, -1, 0, tree, depth);
        
        int grundy = 0;
        
        // Find all leaves and XOR their (depth + 1)
        for (int i = 0; i < tree.length; i++) {
            if (i != root && tree[i].size() == 1) { // Leaf node
                grundy ^= (depth[i] + 1);
            }
        }
        
        return grundy;
    }
    
    private static void dfsDepth(int u, int parent, int d, List<Integer>[] tree, int[] depth) {
        depth[u] = d;
        for (int v : tree[u]) {
            if (v != parent) {
                dfsDepth(v, u, d + 1, tree, depth);
            }
        }
    }
    
    // ========== USAGE EXAMPLES ==========
    
    public static void main(String[] args) {
        System.out.println("=== Game Theory Examples ===\n");
        
        // Example 1: Classic Nim
        int[] piles1 = {3, 5, 7};
        System.out.println("1. Classic Nim with piles [3, 5, 7]:");
        System.out.println("   Can first player win? " + canWinNim(piles1));
        System.out.println("   XOR sum: " + (3 ^ 5 ^ 7) + " (win if != 0)");
        
        // Example 2: Single pile Nim (LeetCode 292)
        System.out.println("\n2. Single pile Nim with n=4:");
        System.out.println("   Can win? " + canWinNimSingle(4)); // false
        System.out.println("   With n=5: " + canWinNimSingle(5)); // true
        
        // Example 3: Misère Nim
        int[] piles2 = {1, 1, 1};
        System.out.println("\n3. Misère Nim with piles [1, 1, 1]:");
        System.out.println("   Can first player win? " + canWinMisereNim(piles2));
        
        // Example 4: Stone Game IV (LeetCode 1510)
        System.out.println("\n4. Stone Game IV with n=7:");
        System.out.println("   Can win? " + winnerSquareGame(7));
        System.out.println("   Using Grundy: " + winnerSquareGameGrundy(7));
        
        // Example 5: Stone Game VII (LeetCode 1690)
        int[] stones7 = {5, 3, 1, 4, 2};
        System.out.println("\n5. Stone Game VII with stones [5,3,1,4,2]:");
        System.out.println("   Max score difference: " + stoneGameVII(stones7));
        
        // Example 6: Stone Game VIII (LeetCode 1872)
        int[] stones8 = {-1, 2, -3, 4, -5};
        System.out.println("\n6. Stone Game VIII with stones [-1,2,-3,4,-5]:");
        System.out.println("   Max score difference: " + stoneGameVIII(stones8));
        
        // Example 7: Mex calculation
        System.out.println("\n7. Mex of {0, 1, 3, 4}:");
        Set<Integer> set = new HashSet<>(Arrays.asList(0, 1, 3, 4));
        System.out.println("   Mex: " + mex(set)); // 2
        
        // Example 8: Grundy number for simple game
        System.out.println("\n8. Grundy number calculation:");
        Map<Integer, Integer> memo = new HashMap<>();
        
        // Game: Remove 1 or 2 stones
        java.util.function.Function<Integer, List<Integer>> getNextStates = state -> {
            List<Integer> next = new ArrayList<>();
            if (state >= 1) next.add(state - 1);
            if (state >= 2) next.add(state - 2);
            return next;
        };
        
        for (int i = 0; i <= 10; i++) {
            int g = grundyNumber(i, memo, getNextStates);
            System.out.println("   Grundy[" + i + "] = " + g + 
                             " (" + (g != 0 ? "WIN" : "LOSE") + ")");
        }
        
        System.out.println("\n=== Key Concepts ===");
        System.out.println("Nim Sum: XOR of all piles (win if != 0)");
        System.out.println("Grundy Number: Mex of reachable states");
        System.out.println("Composite Games: XOR all Grundy numbers");
        System.out.println("Sprague-Grundy: Any impartial game ≡ Nim pile");
    }
}
