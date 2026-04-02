package com.cp.algorithms;

/**
 * Comprehensive test suite for ConvexHullOptimization
 * Tests all CHT variants and edge cases for big-tech interview readiness
 */
public class ConvexHullOptimizationTest {

    public static void main(String[] args) {
        System.out.println("=== ConvexHullOptimization Test Suite ===\n");
        
        int passed = 0;
        int total = 0;
        
        // Basic functionality tests
        passed += runTest("Line Evaluation", ConvexHullOptimizationTest::testLineEvaluation);
        total++;
        
        passed += runTest("CHT Min Basic", ConvexHullOptimizationTest::testConvexHullTrickMinBasic);
        total++;
        
        passed += runTest("CHT Min Single Line", ConvexHullOptimizationTest::testConvexHullTrickMinSingleLine);
        total++;
        
        passed += runTest("CHT Min General", ConvexHullOptimizationTest::testConvexHullTrickMinGeneralBasic);
        total++;
        
        passed += runTest("CHT Max Basic", ConvexHullOptimizationTest::testConvexHullTrickMaxBasic);
        total++;
        
        passed += runTest("Li Chao Tree", ConvexHullOptimizationTest::testLiChaoTreeBasic);
        total++;
        
        passed += runTest("Batch Scheduling", ConvexHullOptimizationTest::testBatchSchedulingSmall);
        total++;
        
        passed += runTest("DP Optimization", ConvexHullOptimizationTest::testOptimizeDPBasic);
        total++;
        
        passed += runTest("Mathematical Correctness", ConvexHullOptimizationTest::testMathematicalCorrectness);
        total++;
        
        passed += runTest("Edge Cases", ConvexHullOptimizationTest::testEdgeCases);
        total++;
        
        passed += runTest("Performance", ConvexHullOptimizationTest::testPerformance);
        total++;
        
        System.out.println("\n=== Test Results ===");
        System.out.println("Passed: " + passed + "/" + total);
        System.out.println("Success Rate: " + (passed * 100.0 / total) + "%");
        
        if (passed == total) {
            System.out.println("🎉 ALL TESTS PASSED! ConvexHullOptimization is ready for big-tech interviews!");
        } else {
            System.out.println("❌ Some tests failed. Review implementation.");
        }
    }
    
    static int runTest(String name, Runnable test) {
        try {
            test.run();
            System.out.println("✅ " + name + " - PASSED");
            return 1;
        } catch (Exception e) {
            System.out.println("❌ " + name + " - FAILED: " + e.getMessage());
            return 0;
        }
    }
    
    static void assertEquals(long expected, long actual) {
        if (expected != actual) {
            throw new RuntimeException("Expected: " + expected + ", Actual: " + actual);
        }
    }
    
    static void assertTrue(boolean condition) {
        if (!condition) {
            throw new RuntimeException("Expected true but got false");
        }
    }

    // ========== TEST METHODS ==========
    
    static void testLineEvaluation() {
        ConvexHullOptimization.Line line = new ConvexHullOptimization.Line(2, 3);
        assertEquals(7, line.eval(2)); // 2*2 + 3 = 7
        assertEquals(3, line.eval(0)); // 2*0 + 3 = 3
        assertEquals(-1, line.eval(-2)); // 2*(-2) + 3 = -1
    }

    static void testConvexHullTrickMinBasic() {
        ConvexHullOptimization.ConvexHullTrickMin cht = new ConvexHullOptimization.ConvexHullTrickMin();
        
        // Add lines in decreasing slope order
        cht.addLine(0, 2);   // y = 2
        cht.addLine(-1, 5);  // y = -x + 5
        
        assertEquals(2, cht.query(0)); // min(2, 5) = 2
        assertEquals(2, cht.query(3)); // min(2, 2) = 2
        assertEquals(0, cht.query(5)); // min(2, 0) = 0
    }

    static void testConvexHullTrickMinSingleLine() {
        ConvexHullOptimization.ConvexHullTrickMin cht = new ConvexHullOptimization.ConvexHullTrickMin();
        cht.addLine(3, -2); // y = 3x - 2
        
        assertEquals(1, cht.query(1));  // 3*1 - 2 = 1
        assertEquals(10, cht.query(4)); // 3*4 - 2 = 10
    }

    static void testConvexHullTrickMinGeneralBasic() {
        ConvexHullOptimization.ConvexHullTrickMinGeneral cht = new ConvexHullOptimization.ConvexHullTrickMinGeneral();
        
        cht.addLine(0, 3);   // y = 3
        cht.addLine(-1, 4);  // y = -x + 4
        cht.addLine(-2, 8);  // y = -2x + 8
        
        // Can query in any order
        assertEquals(3, cht.query(1));  // min(3, 3, 6) = 3
        assertEquals(-2, cht.query(5));  // min(3, -1, -2) = -2 (corrected expectation)
    }

    static void testConvexHullTrickMaxBasic() {
        ConvexHullOptimization.ConvexHullTrickMax cht = new ConvexHullOptimization.ConvexHullTrickMax();
        
        // For maximum, slopes should be increasing
        cht.addLine(0, 1);  // y = 1
        cht.addLine(1, 0);  // y = x
        cht.addLine(2, -1); // y = 2x - 1
        
        assertEquals(1, cht.query(0));  // max(1, 0, -1) = 1
        assertEquals(3, cht.query(2));  // max(1, 2, 3) = 3
    }

    static void testLiChaoTreeBasic() {
        ConvexHullOptimization.LiChaoTree lct = new ConvexHullOptimization.LiChaoTree(0, 10);
        
        lct.addLine(1, 0);   // y = x
        lct.addLine(-1, 10); // y = -x + 10
        lct.addLine(0, 5);   // y = 5
        
        assertEquals(0, lct.query(0));   // min(0, 10, 5) = 0
        assertEquals(5, lct.query(5));   // min(5, 5, 5) = 5
        assertEquals(0, lct.query(10));  // min(10, 0, 5) = 0
    }

    static void testBatchSchedulingSmall() {
        int[] jobs = {1, 2, 3};
        long cost = 5;
        long result = ConvexHullOptimization.batchScheduling(jobs, cost);
        assertTrue(result > 0); // Should have some cost
        assertTrue(result <= 100); // Sanity check
    }

    static void testOptimizeDPBasic() {
        long[] a = {3, 2, 1};     // decreasing
        long[] b = {1, 2, 3};     // increasing  
        long[] c = {0, 1, 2};
        
        long[] result = ConvexHullOptimization.optimizeDP(a, b, c);
        
        assertEquals(3, result.length);
        assertEquals(0, result[0]); // Base case
        assertTrue(result[1] >= 0);
        assertTrue(result[2] >= 0);
    }

    static void testMathematicalCorrectness() {
        ConvexHullOptimization.ConvexHullTrickMin cht = new ConvexHullOptimization.ConvexHullTrickMin();
        
        // Lines: y = 0x + 4, y = -1x + 6, y = -2x + 10
        cht.addLine(0, 4);   // y = 4
        cht.addLine(-1, 6);  // y = -x + 6  
        cht.addLine(-2, 10); // y = -2x + 10
        
        // At x=0: min(4, 6, 10) = 4
        assertEquals(4, cht.query(0));
        
        // At x=2: min(4, 4, 6) = 4
        assertEquals(4, cht.query(2));
        
        // At x=3: min(4, 3, 4) = 3
        assertEquals(3, cht.query(3));
        
        // At x=5: min(4, 1, 0) = 0
        assertEquals(0, cht.query(5));
    }
    
    static void testEdgeCases() {
        // Test single line CHT
        ConvexHullOptimization.ConvexHullTrickMin cht1 = new ConvexHullOptimization.ConvexHullTrickMin();
        cht1.addLine(2, -3);
        assertEquals(-1, cht1.query(1)); // 2*1 - 3 = -1
        
        // Test Li Chao Tree with large bounds
        ConvexHullOptimization.LiChaoTree lct = new ConvexHullOptimization.LiChaoTree(-100, 100);
        lct.addLine(1, 0);
        assertEquals(-100, lct.query(-100));
        assertEquals(100, lct.query(100));
        
        // Test empty arrays
        int[] emptyJobs = {};
        assertEquals(0, ConvexHullOptimization.batchScheduling(emptyJobs, 10));
    }
    
    static void testPerformance() {
        // Test that CHT is reasonably fast
        long startTime = System.nanoTime();
        
        ConvexHullOptimization.ConvexHullTrickMin cht = new ConvexHullOptimization.ConvexHullTrickMin();
        
        // Add 1000 lines and query 1000 times
        for (int i = 1000; i > 0; i--) {
            cht.addLine(-i, i * 100L);
        }
        
        for (int x = 1; x <= 1000; x++) {
            cht.query(x);
        }
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        // Should complete in reasonable time (less than 1 second)
        assertTrue(duration < 1_000_000_000L); // 1 second in nanoseconds
    }
}