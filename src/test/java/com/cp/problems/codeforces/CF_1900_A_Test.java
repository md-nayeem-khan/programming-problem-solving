package com.cp.problems.codeforces;

import com.cp.testcases.SolutionRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Codeforces 1900A — Don't Try to Count")
class CF_1900_A_Test {

    @Test
    void testAllCases() {
        SolutionRunner.runAndAssert(new CF_1900_A(), "codeforces/1900_A.txt");
    }
}
