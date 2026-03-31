package com.cp.testcases;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * TestCaseLoader — reads test case files from src/test/resources/testcases/
 *
 * File format (copy-paste from LeetCode "Example" section):
 * ─────────────────────────────────────────────────────────────────
 * # Comment lines start with '#'
 *
 * INPUT
 * nums = [2,7,11,15]
 * target = 9
 * OUTPUT
 * [0,1]
 *
 * INPUT
 * nums = [3,2,4]
 * target = 6
 * OUTPUT
 * [1,2]
 * ─────────────────────────────────────────────────────────────────
 *
 * Usage:
 *   List<TestCase> cases = TestCaseLoader.load("leetcode/001_two_sum.txt");
 *   for (TestCase tc : cases) {
 *       int[] nums   = InputParser.parseIntArray(tc.getParam("nums"));
 *       int target   = InputParser.parseInt(tc.getParam("target"));
 *       int[] expect = InputParser.parseIntArray(tc.output);
 *   }
 */
public class TestCaseLoader {

    private static final String BASE_DIR = "testcases/";

    /**
     * Load all test cases from a file in test resources.
     */
    public static List<TestCase> load(String relPath) {
        String resourcePath = BASE_DIR + relPath;
        InputStream is = TestCaseLoader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) throw new RuntimeException("Test case file not found: " + resourcePath);

        List<TestCase> cases = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            TestCase current = null;
            boolean inInput = false, inOutput = false;
            StringBuilder outputBuilder = new StringBuilder();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.equalsIgnoreCase("INPUT")) {
                    if (current != null) { current.output = outputBuilder.toString().trim(); cases.add(current); }
                    current = new TestCase();
                    outputBuilder.setLength(0);
                    inInput = true; inOutput = false;
                } else if (line.equalsIgnoreCase("OUTPUT")) {
                    inInput = false; inOutput = true;
                } else if (inInput && current != null) {
                    // Parse "key = value" or just "value"
                    if (line.contains("=")) {
                        int eqIdx = line.indexOf('=');
                        String key = line.substring(0, eqIdx).trim();
                        String val = line.substring(eqIdx + 1).trim();
                        current.params.put(key, val);
                    } else {
                        current.rawLines.add(line);
                    }
                } else if (inOutput) {
                    outputBuilder.append(line).append("\n");
                }
            }
            if (current != null) { current.output = outputBuilder.toString().trim(); cases.add(current); }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test cases from: " + resourcePath, e);
        }
        return cases;
    }

    /**
     * Load raw Codeforces-style test cases (plain stdin/stdout blocks separated by "---").
     * Returns list of [input, expectedOutput] string pairs.
     */
    public static List<String[]> loadRaw(String relPath) {
        String resourcePath = BASE_DIR + relPath;
        InputStream is = TestCaseLoader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) throw new RuntimeException("Test case file not found: " + resourcePath);

        List<String[]> cases = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder input = new StringBuilder(), output = new StringBuilder();
            boolean readingOutput = false;
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("#")) continue;
                if (line.trim().equals("---")) {
                    if (input.length() > 0 || output.length() > 0) {
                        cases.add(new String[]{input.toString().trim(), output.toString().trim()});
                        input.setLength(0); output.setLength(0); readingOutput = false;
                    }
                } else if (line.trim().equalsIgnoreCase("INPUT")) {
                    readingOutput = false;
                } else if (line.trim().equalsIgnoreCase("OUTPUT")) {
                    readingOutput = true;
                } else {
                    if (readingOutput) output.append(line).append("\n");
                    else               input.append(line).append("\n");
                }
            }
            if (input.length() > 0 || output.length() > 0)
                cases.add(new String[]{input.toString().trim(), output.toString().trim()});

        } catch (IOException e) {
            throw new RuntimeException("Failed to load test cases from: " + resourcePath, e);
        }
        return cases;
    }

    // ---------------------------------------------------------------
    // TestCase record
    // ---------------------------------------------------------------
    public static class TestCase {
        public final java.util.LinkedHashMap<String, String> params = new java.util.LinkedHashMap<>();
        public final List<String> rawLines = new ArrayList<>(); // for positional access
        public String output = "";

        /** Get parameter by name */
        public String getParam(String name) {
            if (!params.containsKey(name)) throw new IllegalArgumentException("Param not found: " + name);
            return params.get(name);
        }

        /** Get parameter by 0-based position (for positional inputs) */
        public String getLine(int index) { return rawLines.get(index); }

        @Override
        public String toString() {
            return "TestCase{params=" + params + ", output='" + output + "'}";
        }
    }
}
