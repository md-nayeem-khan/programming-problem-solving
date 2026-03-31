package com.cp.problems.leetcode;

import com.cp.problems.BaseSolution;
import com.cp.problems.Problem;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode #1 — Two Sum
 *
 * Copy-paste workflow:
 *   1. Create a new file: P{id}_{TitleCamelCase}.java
 *   2. Add @Problem annotation
 *   3. Write your solution method(s)
 *   4. Add test cases in src/test/resources/testcases/leetcode/001_two_sum.txt
 */
@Problem(
        id = "1",
        title = "Two Sum",
        platform = Problem.Platform.LEETCODE,
        difficulty = Problem.Difficulty.EASY,
        tags = {"Array", "HashTable"},
        url = "https://leetcode.com/problems/two-sum/"
)
public class P001_TwoSum extends BaseSolution {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) return new int[]{map.get(complement), i};
            map.put(nums[i], i);
        }
        return new int[]{};
    }
}
