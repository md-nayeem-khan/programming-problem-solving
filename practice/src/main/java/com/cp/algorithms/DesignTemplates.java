package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * System Design Data Structure Templates for coding interviews.
 * 
 * Coverage:
 * - Time-Based Key-Value Store
 * - Snapshot Array
 * - Design Hit Counter
 * - Design Log Storage System
 * - Insert Delete GetRandom O(1)
 * - Design Phone Directory
 * - Design Tic-Tac-Toe
 * - Design File System
 * 
 * Critical for: Meta, Amazon, Google (design rounds)
 * Frequency: ~10% of design interviews
 */
public class DesignTemplates {

    // ============================================================================
    // TIME-BASED KEY-VALUE STORE
    // ============================================================================
    
    /**
     * Time Based Key-Value Store - store multiple time-stamped values per key.
     * 
     * set(key, value, timestamp): Store value at timestamp.
     * get(key, timestamp): Return value at or before timestamp.
     * 
     * Interview favorite: Meta, Amazon
     */
    public static class TimeMap {
        private Map<String, TreeMap<Integer, String>> map;
        
        public TimeMap() {
            map = new HashMap<>();
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public void set(String key, String value, int timestamp) {
            map.computeIfAbsent(key, k -> new TreeMap<>()).put(timestamp, value);
        }
        
        @Complexity(time = "O(log n)", space = "O(1)")
        public String get(String key, int timestamp) {
            if (!map.containsKey(key)) return "";
            
            TreeMap<Integer, String> tree = map.get(key);
            Integer floorKey = tree.floorKey(timestamp);
            
            return floorKey == null ? "" : tree.get(floorKey);
        }
    }
    
    // ============================================================================
    // SNAPSHOT ARRAY
    // ============================================================================
    
    /**
     * Snapshot Array - efficiently store array snapshots.
     * 
     * set(index, val): Set value at index.
     * snap(): Take snapshot, return snap_id.
     * get(index, snap_id): Get value at index in snapshot.
     * 
     * Interview favorite: Google, Meta
     */
    public static class SnapshotArray {
        private Map<Integer, TreeMap<Integer, Integer>> history;
        private int snapId;
        
        @Complexity(time = "O(n)", space = "O(n)")
        public SnapshotArray(int length) {
            history = new HashMap<>();
            snapId = 0;
            
            // Initialize with 0s
            for (int i = 0; i < length; i++) {
                history.put(i, new TreeMap<>());
                history.get(i).put(0, 0);
            }
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public void set(int index, int val) {
            history.get(index).put(snapId, val);
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public int snap() {
            int currentSnap = snapId;
            snapId++;
            return currentSnap;
        }
        
        @Complexity(time = "O(log n)", space = "O(1)")
        public int get(int index, int snap_id) {
            return history.get(index).floorEntry(snap_id).getValue();
        }
    }
    
    // ============================================================================
    // DESIGN HIT COUNTER
    // ============================================================================
    
    /**
     * Design Hit Counter - count hits in last 5 minutes.
     * 
     * hit(timestamp): Record hit.
     * getHits(timestamp): Get hits in last 300 seconds.
     * 
     * Interview favorite: Meta, Amazon
     */
    public static class HitCounter {
        private Queue<Integer> hits;
        
        public HitCounter() {
            hits = new LinkedList<>();
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public void hit(int timestamp) {
            hits.offer(timestamp);
        }
        
        @Complexity(time = "O(n)", space = "O(1)")
        public int getHits(int timestamp) {
            while (!hits.isEmpty() && hits.peek() <= timestamp - 300) {
                hits.poll();
            }
            return hits.size();
        }
    }
    
    /**
     * Hit Counter with bucketing for better space efficiency.
     */
    public static class HitCounterBucket {
        private int[] times;
        private int[] hits;
        
        public HitCounterBucket() {
            times = new int[300];
            hits = new int[300];
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public void hit(int timestamp) {
            int idx = timestamp % 300;
            if (times[idx] != timestamp) {
                times[idx] = timestamp;
                hits[idx] = 1;
            } else {
                hits[idx]++;
            }
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public int getHits(int timestamp) {
            int total = 0;
            for (int i = 0; i < 300; i++) {
                if (timestamp - times[i] < 300) {
                    total += hits[i];
                }
            }
            return total;
        }
    }
    
    // ============================================================================
    // INSERT DELETE GETRANDOM O(1)
    // ============================================================================
    
    /**
     * Insert Delete GetRandom O(1) - all operations in O(1) average.
     * 
     * Pattern: ArrayList + HashMap for index lookup.
     * 
     * Interview favorite: Meta, Google, Amazon
     */
    public static class RandomizedSet {
        private List<Integer> list;
        private Map<Integer, Integer> map; // value -> index
        private Random rand;
        
        public RandomizedSet() {
            list = new ArrayList<>();
            map = new HashMap<>();
            rand = new Random();
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public boolean insert(int val) {
            if (map.containsKey(val)) return false;
            
            map.put(val, list.size());
            list.add(val);
            return true;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public boolean remove(int val) {
            if (!map.containsKey(val)) return false;
            
            int idx = map.get(val);
            int lastVal = list.get(list.size() - 1);
            
            // Swap with last element
            list.set(idx, lastVal);
            map.put(lastVal, idx);
            
            // Remove last
            list.remove(list.size() - 1);
            map.remove(val);
            
            return true;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public int getRandom() {
            return list.get(rand.nextInt(list.size()));
        }
    }
    
    /**
     * Insert Delete GetRandom O(1) - duplicates allowed.
     */
    public static class RandomizedCollection {
        private List<Integer> list;
        private Map<Integer, Set<Integer>> map; // value -> set of indices
        private Random rand;
        
        public RandomizedCollection() {
            list = new ArrayList<>();
            map = new HashMap<>();
            rand = new Random();
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public boolean insert(int val) {
            map.computeIfAbsent(val, k -> new HashSet<>()).add(list.size());
            list.add(val);
            return map.get(val).size() == 1;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public boolean remove(int val) {
            if (!map.containsKey(val) || map.get(val).isEmpty()) return false;
            
            int idx = map.get(val).iterator().next();
            map.get(val).remove(idx);
            
            int lastVal = list.get(list.size() - 1);
            list.set(idx, lastVal);
            
            if (idx < list.size() - 1) { // Only update if not removing the last element
                map.get(lastVal).add(idx);
                map.get(lastVal).remove(list.size() - 1);
            }
            
            list.remove(list.size() - 1);
            
            return map.get(val).size() == 0;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public int getRandom() {
            return list.get(rand.nextInt(list.size()));
        }
    }
    
    // ============================================================================
    // DESIGN LOG STORAGE SYSTEM
    // ============================================================================
    
    /**
     * Design Log Storage System - retrieve logs within time range.
     * 
     * put(id, timestamp): Store log.
     * retrieve(start, end, granularity): Get logs in range.
     */
    public static class LogSystem {
        private List<String[]> logs; // [timestamp, id]
        private Map<String, Integer> granularityMap;
        
        public LogSystem() {
            logs = new ArrayList<>();
            granularityMap = new HashMap<>();
            granularityMap.put("Year", 4);
            granularityMap.put("Month", 7);
            granularityMap.put("Day", 10);
            granularityMap.put("Hour", 13);
            granularityMap.put("Minute", 16);
            granularityMap.put("Second", 19);
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public void put(int id, String timestamp) {
            logs.add(new String[]{timestamp, String.valueOf(id)});
        }
        
        @Complexity(time = "O(n)", space = "O(n)")
        public List<Integer> retrieve(String start, String end, String granularity) {
            List<Integer> result = new ArrayList<>();
            int len = granularityMap.get(granularity);
            
            String s = start.substring(0, len);
            String e = end.substring(0, len);
            
            for (String[] log : logs) {
                String t = log[0].substring(0, len);
                if (t.compareTo(s) >= 0 && t.compareTo(e) <= 0) {
                    result.add(Integer.parseInt(log[1]));
                }
            }
            
            return result;
        }
    }
    
    // ============================================================================
    // DESIGN TIC-TAC-TOE
    // ============================================================================
    
    /**
     * Design Tic-Tac-Toe - determine winner in O(1) per move.
     * 
     * Pattern: Track sums for rows, cols, diagonals.
     * 
     * Interview favorite: Amazon, Meta
     */
    public static class TicTacToe {
        private int[] rows;
        private int[] cols;
        private int diagonal;
        private int antiDiagonal;
        private int n;
        
        public TicTacToe(int n) {
            this.n = n;
            rows = new int[n];
            cols = new int[n];
            diagonal = 0;
            antiDiagonal = 0;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public int move(int row, int col, int player) {
            int toAdd = (player == 1) ? 1 : -1;
            
            rows[row] += toAdd;
            cols[col] += toAdd;
            
            if (row == col) {
                diagonal += toAdd;
            }
            if (row + col == n - 1) {
                antiDiagonal += toAdd;
            }
            
            if (Math.abs(rows[row]) == n || Math.abs(cols[col]) == n ||
                Math.abs(diagonal) == n || Math.abs(antiDiagonal) == n) {
                return player;
            }
            
            return 0;
        }
    }
    
    // ============================================================================
    // DESIGN FILE SYSTEM
    // ============================================================================
    
    /**
     * Design In-Memory File System.
     * 
     * ls(path): List contents of directory.
     * mkdir(path): Create directory.
     * addContentToFile(path, content): Append to file.
     * readContentFromFile(path): Read file content.
     */
    public static class FileSystem {
        private FileNode root;
        
        class FileNode {
            boolean isFile;
            String content;
            Map<String, FileNode> children;
            
            FileNode() {
                content = "";
                children = new HashMap<>();
            }
        }
        
        public FileSystem() {
            root = new FileNode();
        }
        
        @Complexity(time = "O(n + k log k) where k=files in dir", space = "O(1)")
        public List<String> ls(String path) {
            FileNode node = navigate(path);
            List<String> result = new ArrayList<>();
            
            if (node.isFile) {
                String[] parts = path.split("/");
                result.add(parts[parts.length - 1]);
            } else {
                result.addAll(node.children.keySet());
                Collections.sort(result);
            }
            
            return result;
        }
        
        @Complexity(time = "O(n)", space = "O(n)")
        public void mkdir(String path) {
            navigate(path);
        }
        
        @Complexity(time = "O(n)", space = "O(1)")
        public void addContentToFile(String filePath, String content) {
            FileNode node = navigate(filePath);
            node.isFile = true;
            node.content += content;
        }
        
        @Complexity(time = "O(n)", space = "O(1)")
        public String readContentFromFile(String filePath) {
            FileNode node = navigate(filePath);
            return node.content;
        }
        
        private FileNode navigate(String path) {
            if (path.equals("/")) return root;
            
            String[] parts = path.split("/");
            FileNode node = root;
            
            for (int i = 1; i < parts.length; i++) {
                if (!node.children.containsKey(parts[i])) {
                    node.children.put(parts[i], new FileNode());
                }
                node = node.children.get(parts[i]);
            }
            
            return node;
        }
    }
    
    // ============================================================================
    // DESIGN PHONE DIRECTORY
    // ============================================================================
    
    /**
     * Design Phone Directory - manage phone number availability.
     * 
     * get(): Get an available number.
     * check(number): Check if number is available.
     * release(number): Release number back to pool.
     */
    public static class PhoneDirectory {
        private Set<Integer> available;
        private Queue<Integer> released;
        
        public PhoneDirectory(int maxNumbers) {
            available = new HashSet<>();
            released = new LinkedList<>();
            
            for (int i = 0; i < maxNumbers; i++) {
                available.add(i);
                released.offer(i);
            }
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public int get() {
            if (released.isEmpty()) return -1;
            
            int number = released.poll();
            available.remove(number);
            return number;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public boolean check(int number) {
            return available.contains(number);
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public void release(int number) {
            if (available.contains(number)) return;
            
            available.add(number);
            released.offer(number);
        }
    }
    
    // ============================================================================
    // DESIGN SEARCH AUTOCOMPLETE SYSTEM
    // ============================================================================
    
    /**
     * Design Search Autocomplete System.
     * 
     * input(c): Input character, return top 3 hot sentences.
     * Special: '#' ends current sentence and adds to history.
     */
    public static class AutocompleteSystem {
        private Map<String, Integer> freq;
        private StringBuilder current;
        
        public AutocompleteSystem(String[] sentences, int[] times) {
            freq = new HashMap<>();
            for (int i = 0; i < sentences.length; i++) {
                freq.put(sentences[i], times[i]);
            }
            current = new StringBuilder();
        }
        
        @Complexity(time = "O(n log n) where n=matching sentences", space = "O(n)")
        public List<String> input(char c) {
            if (c == '#') {
                freq.put(current.toString(), freq.getOrDefault(current.toString(), 0) + 1);
                current = new StringBuilder();
                return new ArrayList<>();
            }
            
            current.append(c);
            String prefix = current.toString();
            
            List<String> matches = new ArrayList<>();
            for (String sentence : freq.keySet()) {
                if (sentence.startsWith(prefix)) {
                    matches.add(sentence);
                }
            }
            
            // Sort by frequency desc, then lexicographically
            matches.sort((a, b) -> {
                int freqA = freq.get(a);
                int freqB = freq.get(b);
                if (freqA != freqB) {
                    return freqB - freqA;
                }
                return a.compareTo(b);
            });
            
            return matches.size() <= 3 ? matches : matches.subList(0, 3);
        }
    }
    
    // ============================================================================
    // DESIGN BROWSER HISTORY
    // ============================================================================
    
    /**
     * Design Browser History with back and forward.
     */
    public static class BrowserHistory {
        private List<String> history;
        private int current;
        
        public BrowserHistory(String homepage) {
            history = new ArrayList<>();
            history.add(homepage);
            current = 0;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public void visit(String url) {
            // Remove forward history
            while (history.size() > current + 1) {
                history.remove(history.size() - 1);
            }
            history.add(url);
            current++;
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public String back(int steps) {
            current = Math.max(0, current - steps);
            return history.get(current);
        }
        
        @Complexity(time = "O(1)", space = "O(1)")
        public String forward(int steps) {
            current = Math.min(history.size() - 1, current + steps);
            return history.get(current);
        }
    }
}
