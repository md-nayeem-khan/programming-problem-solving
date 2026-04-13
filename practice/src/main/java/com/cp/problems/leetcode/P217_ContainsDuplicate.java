package com.cp.problems.leetcode;

import com.cp.problems.BaseSolution;
import com.cp.problems.Problem;
import java.util.HashSet;
import java.util.Set;


/**
 * leetcode 217 - Contains Duplicate
 
 * TODO: Add problem description

 * TODO: Add solution approach
 */
@Problem(
        id = "217",
        title = "Contains Duplicate",
        platform = Problem.Platform.LEETCODE,
        difficulty = Problem.Difficulty.EASY,
        tags = {"Array", "HashTable"},
        url = "https://leetcode.com/problems/contains-duplicate/"
)
public class P217_ContainsDuplicate extends BaseSolution {

    // TODO: Implement your solution method(s) here
    public boolean solve(int[] nums) {
        // Your code here
        Set<Integer> st = new HashSet<>();
        for (int n : nums) {
            if (st.contains(n)) {
                return true;
            }
            st.add(n);
        }
        return false;
    }
}
