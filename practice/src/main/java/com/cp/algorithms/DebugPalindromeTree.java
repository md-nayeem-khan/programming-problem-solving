package com.cp.algorithms;
import java.util.*;

public class DebugPalindromeTree {
    public static void main(String[] args) {
        String test = "racecar";
        PalindromicTreeTemplates.PalindromicTree tree = 
            new PalindromicTreeTemplates.PalindromicTree(test);
            
        System.out.println("String: " + test);
        System.out.println("Total palindromes: " + tree.countAllPalindromes());
        System.out.println("Distinct palindromes: " + tree.countDistinctPalindromes());
        System.out.println("All palindromes: " + tree.getAllPalindromes());
        System.out.println("Frequencies:");
        for (Map.Entry<String, Long> e : tree.getPalindromeFrequencies().entrySet()) {
            System.out.println("  '" + e.getKey() + "': " + e.getValue());
        }
    }
}
