package com.cp.testcases;

import com.cp.datastructures.ListNode;
import com.cp.datastructures.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * InputParser — converts plain text (copy-pasted from LeetCode/Codeforces)
 * into Java objects automatically.
 *
 * Supported formats (all are LeetCode-style copy-paste friendly):
 *   int          -> "42"
 *   int[]        -> "[1,2,3,4,5]"
 *   int[][]      -> "[[1,2],[3,4]]"
 *   String       -> "\"hello\"" or "hello"
 *   String[]     -> "[\"abc\",\"def\"]"
 *   boolean      -> "true" / "false"
 *   TreeNode     -> "[1,2,3,null,5]"   (level-order)
 *   ListNode     -> "[1,2,3,4,5]"
 *   List<Integer>-> "[1,2,3]"
 */
public class InputParser {

    public static int parseInt(String s) {
        return Integer.parseInt(s.trim());
    }

    public static long parseLong(String s) {
        return Long.parseLong(s.trim());
    }

    public static double parseDouble(String s) {
        return Double.parseDouble(s.trim());
    }

    public static boolean parseBoolean(String s) {
        return Boolean.parseBoolean(s.trim());
    }

    public static String parseString(String s) {
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"")) return s.substring(1, s.length() - 1);
        return s;
    }

    // ---------------------------------------------------------------
    // int[] from "[1,2,3,4,5]"
    // ---------------------------------------------------------------
    public static int[] parseIntArray(String s) {
        s = s.trim();
        if (s.equals("[]")) return new int[0];
        s = s.substring(1, s.length() - 1); // strip [ ]
        String[] tokens = s.split(",");
        int[] result = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) result[i] = Integer.parseInt(tokens[i].trim());
        return result;
    }

    // ---------------------------------------------------------------
    // int[][] from "[[1,2],[3,4],[5,6]]"
    // ---------------------------------------------------------------
    public static int[][] parseInt2DArray(String s) {
        s = s.trim();
        if (s.equals("[]")) return new int[0][0];
        // Remove outer [ ]
        s = s.substring(1, s.length() - 1);
        List<int[]> rows = new ArrayList<>();
        int depth = 0, start = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') { if (depth++ == 0) start = i; }
            else if (c == ']') {
                if (--depth == 0) rows.add(parseIntArray(s.substring(start, i + 1)));
            }
        }
        return rows.toArray(new int[0][]);
    }

    // ---------------------------------------------------------------
    // String[] from "[\"abc\",\"def\"]"
    // ---------------------------------------------------------------
    public static String[] parseStringArray(String s) {
        s = s.trim();
        if (s.equals("[]")) return new String[0];
        s = s.substring(1, s.length() - 1);
        // Split by comma not inside quotes
        List<String> tokens = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuote = false;
        for (char c : s.toCharArray()) {
            if (c == '"')       inQuote = !inQuote;
            else if (c == ',' && !inQuote) { tokens.add(parseString(cur.toString())); cur.setLength(0); continue; }
            else                cur.append(c);
        }
        if (cur.length() > 0) tokens.add(parseString(cur.toString()));
        return tokens.toArray(new String[0]);
    }

    // ---------------------------------------------------------------
    // List<Integer> from "[1,2,3]"
    // ---------------------------------------------------------------
    public static List<Integer> parseIntList(String s) {
        int[] arr = parseIntArray(s);
        List<Integer> list = new ArrayList<>();
        for (int v : arr) list.add(v);
        return list;
    }

    // ---------------------------------------------------------------
    // TreeNode from "[1,2,3,null,5]"
    // ---------------------------------------------------------------
    public static TreeNode parseTreeNode(String s) {
        s = s.trim();
        if (s.equals("[]") || s.equals("null")) return null;
        s = s.substring(1, s.length() - 1);
        String[] tokens = s.split(",");
        Integer[] values = new Integer[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String t = tokens[i].trim();
            values[i] = t.equals("null") ? null : Integer.parseInt(t);
        }
        return TreeNode.fromArray(values);
    }

    // ---------------------------------------------------------------
    // ListNode from "[1,2,3,4,5]"
    // ---------------------------------------------------------------
    public static ListNode parseListNode(String s) {
        int[] arr = parseIntArray(s);
        return ListNode.fromArray(arr);
    }

    // ---------------------------------------------------------------
    // char[][] from grid strings like ["#.#","...","#.#"]
    // ---------------------------------------------------------------
    public static char[][] parseCharGrid(String s) {
        String[] rows = parseStringArray(s);
        if (rows.length == 0) return new char[0][0];
        char[][] grid = new char[rows.length][rows[0].length()];
        for (int r = 0; r < rows.length; r++) grid[r] = rows[r].toCharArray();
        return grid;
    }
}
