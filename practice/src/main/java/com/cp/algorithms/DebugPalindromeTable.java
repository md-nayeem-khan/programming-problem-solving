package com.cp.algorithms;
import java.util.*;

public class DebugPalindromeTable {
    public static void main(String[] args) {
        String s = "aabaa";
        int n = s.length();
        PalindromicTreeTemplates.PalindromicTree tree = 
            new PalindromicTreeTemplates.PalindromicTree(s);
        
        // Build palindrome table the way minCuts does it
        boolean[][] isPalin = new boolean[n][n];
        for (PalindromicTreeTemplates.PalindromicNode node : tree.getAllNodes()) {
            System.out.println("Node: start=" + node.start + ", end=" + node.end + 
                ", length=" + node.length + ", palindrome='" + s.substring(node.start, node.end + 1) + "'");
            isPalin[node.start][node.end] = true;
        }
        
        System.out.println();
        System.out.println("isPalin[0][4] (entire 'aabaa')? " + isPalin[0][4]);
        System.out.println();
        
        // Now trace minCuts
        int[] dp = new int[n + 1];
        for (int i = 0; i <= n; i++) dp[i] = i - 1;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (isPalin[j][i - 1]) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                    if (j == 0) System.out.println("i=" + i + ", j=0: entire prefix is palindrome, dp[" + i + "]=0? No, dp[0]+1=" + (dp[j]+1));
                }
            }
        }
        
        System.out.println("dp[5] = " + dp[5]);
    }
}
