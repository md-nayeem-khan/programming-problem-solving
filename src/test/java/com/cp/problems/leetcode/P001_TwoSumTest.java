package com.cp.problems.leetcode;

import com.cp.testcases.InputParser;
import com.cp.testcases.TestCaseLoader;
import com.cp.testcases.TestCaseLoader.TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@DisplayName("LeetCode #1 — Two Sum")
class P001_TwoSumTest {

    private final P001_TwoSum solution = new P001_TwoSum();

    // Load test cases once; stream indices as parameter source
    private static final List<TestCase> CASES =
            TestCaseLoader.load("leetcode/001_two_sum.txt");

    static Stream<Integer> caseIndices() {
        return IntStream.range(0, CASES.size()).boxed();
    }

    @ParameterizedTest(name = "case #{index}")
    @MethodSource("caseIndices")
    void test(int i) {
        TestCase tc = CASES.get(i);
        int[] nums   = InputParser.parseIntArray(tc.getParam("nums"));
        int target   = InputParser.parseInt(tc.getParam("target"));
        int[] expect = InputParser.parseIntArray(tc.output);

        int[] actual = solution.twoSum(nums, target);
        Arrays.sort(actual);
        Arrays.sort(expect);
        assertArrayEquals(expect, actual,
                () -> String.format("nums=%s target=%d", Arrays.toString(nums), target));
    }
}
