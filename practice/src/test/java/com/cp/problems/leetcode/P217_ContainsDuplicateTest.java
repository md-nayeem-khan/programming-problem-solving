package com.cp.problems.leetcode;

import com.cp.testcases.InputParser;
import com.cp.testcases.TestCaseLoader;
import com.cp.testcases.TestCaseLoader.TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("leetcode 217 - Contains Duplicate")
class P217_ContainsDuplicateTest {

    private final P217_ContainsDuplicate solution = new P217_ContainsDuplicate();

    // Load test cases once; stream indices as parameter source
    private static final List<TestCase> CASES =
            TestCaseLoader.load("leetcode/217_contains_duplicate.txt");

    static Stream<Integer> caseIndices() {
        return IntStream.range(0, CASES.size()).boxed();
    }

    @ParameterizedTest(name = "case #{index}")
    @MethodSource("caseIndices")
    void test(int i) {
        TestCase tc = CASES.get(i);
        
        // TODO: Parse input parameters
        // Example:
         int[] nums = InputParser.parseIntArray(tc.getParam("nums"));
        // int target = InputParser.parseInt(tc.getParam("target"));
        
        // TODO: Parse expected output
        // Example:
         boolean expected = InputParser.parseBoolean(tc.output);
        
        // TODO: Call solution method and assert
        // Example:
         boolean actual = solution.solve(nums);
         assertEquals(expected, actual);
    }
}
