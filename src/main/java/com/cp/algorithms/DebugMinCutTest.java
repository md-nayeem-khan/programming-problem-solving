package com.cp.algorithms;

public class DebugMinCutTest {
    public static void main(String[] args) {
        String[] tests = {"aab", "aabaa", "abcde"};
        for (String s : tests) {
            int cuts = PalindromicTreeTemplates.PalindromeDPOptimizations.minCuts(s);
            System.out.println("minCuts(\"" + s + "\") = " + cuts);
        }
    }
}
