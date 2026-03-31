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

/**
 * HOW TO USE THIS TEMPLATE:
 * 1. Copy, rename to P{id}_{Title}Test.java
 * 2. Change TEST_FILE to match your .txt file in testcases/leetcode/
 * 3. In the test() method, parse each param using InputParser
 * 4. Call your solution method and assert
 */
@DisplayName("LeetCode #TODO — Problem Title")
class P000_TemplateTest {

    private static final String TEST_FILE = "leetcode/000_template.txt";
    private final P000_Template solution = new P000_Template();

    private static final List<TestCase> CASES = TestCaseLoader.load(TEST_FILE);

    static Stream<Integer> caseIndices() {
        return IntStream.range(0, CASES.size()).boxed();
    }

    @ParameterizedTest(name = "case #{index}")
    @MethodSource("caseIndices")
    void test(int i) {
        TestCase tc = CASES.get(i);

        // ── Parse inputs ────────────────────────────────────────
        int n = InputParser.parseInt(tc.getParam("n"));
        // int[] arr = InputParser.parseIntArray(tc.getParam("arr"));
        // String s  = InputParser.parseString(tc.getParam("s"));

        // ── Expected output ─────────────────────────────────────
        int expected = InputParser.parseInt(tc.output);

        // ── Assert ──────────────────────────────────────────────
        assertEquals(expected, solution.solve(n));
    }
}
