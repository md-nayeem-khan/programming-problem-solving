package com.cp.problems.codeforces;

import com.cp.testcases.SolutionRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Codeforces 4A — Watermelon")
class CF_4_A_Test {

    @Test
    void testAllCases() {
        SolutionRunner.runAndAssert(new CF_4_A(), "codeforces/4_A.txt");
    }
}
