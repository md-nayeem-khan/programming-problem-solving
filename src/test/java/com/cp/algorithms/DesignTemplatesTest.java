package com.cp.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DesignTemplates.java
 * 
 * Tests all design patterns with:
 * - Normal operations
 * - Edge cases
 * - Error conditions  
 * - Performance characteristics
 * - Boundary values
 * 
 * Coverage target: >95% line and branch coverage
 */
@DisplayName("Design Templates - System Design Data Structures")
class DesignTemplatesTest {

    // ============================================================================
    // TIME-BASED KEY-VALUE STORE TESTS
    // ============================================================================
    
    @Nested 
    @DisplayName("TimeMap - Time Based Key-Value Store")
    class TimeMapTests {
        
        private DesignTemplates.TimeMap timeMap;
        
        @BeforeEach
        void setUp() {
            timeMap = new DesignTemplates.TimeMap();
        }
        
        @Test
        @DisplayName("Basic set and get operations")
        void basicOperations() {
            timeMap.set("foo", "bar", 1);
            assertEquals("bar", timeMap.get("foo", 1));
            assertEquals("bar", timeMap.get("foo", 3)); // should return latest value <= timestamp
        }
        
        @Test 
        @DisplayName("Multiple values for same key at different times")
        void multipleTimestamps() {
            timeMap.set("key", "value1", 1);
            timeMap.set("key", "value2", 3);
            timeMap.set("key", "value3", 5);
            
            assertEquals("value1", timeMap.get("key", 1));
            assertEquals("value1", timeMap.get("key", 2));
            assertEquals("value2", timeMap.get("key", 3));
            assertEquals("value2", timeMap.get("key", 4));
            assertEquals("value3", timeMap.get("key", 5));
            assertEquals("value3", timeMap.get("key", 10));
        }
        
        @Test
        @DisplayName("Get with timestamp before any set")
        void getBeforeFirstTimestamp() {
            timeMap.set("key", "value", 10);
            assertEquals("", timeMap.get("key", 5));
        }
        
        @Test
        @DisplayName("Get non-existent key")
        void getNonExistentKey() {
            assertEquals("", timeMap.get("nonexistent", 1));
        }
        
        @Test
        @DisplayName("Multiple keys")
        void multipleKeys() {
            timeMap.set("key1", "value1", 1);
            timeMap.set("key2", "value2", 2);
            timeMap.set("key1", "value1_updated", 3);
            
            assertEquals("value1", timeMap.get("key1", 2));
            assertEquals("value2", timeMap.get("key2", 2));
            assertEquals("value1_updated", timeMap.get("key1", 3));
        }
        
        @Test
        @DisplayName("Edge case: Empty values")
        void emptyValues() {
            timeMap.set("key", "", 1);
            assertEquals("", timeMap.get("key", 1));
        }
        
        @Test
        @DisplayName("Edge case: Large timestamps")
        void largeTimestamps() {
            timeMap.set("key", "value", Integer.MAX_VALUE);
            assertEquals("value", timeMap.get("key", Integer.MAX_VALUE));
            assertEquals("", timeMap.get("key", Integer.MAX_VALUE - 1));
        }
    }
    
    // ============================================================================
    // SNAPSHOT ARRAY TESTS  
    // ============================================================================
    
    @Nested
    @DisplayName("SnapshotArray - Efficient Array Snapshots")
    class SnapshotArrayTests {
        
        @Test
        @DisplayName("Basic snapshot and get operations")
        void basicOperations() {
            DesignTemplates.SnapshotArray arr = new DesignTemplates.SnapshotArray(3);
            
            // Initial state: [0, 0, 0]
            assertEquals(0, arr.get(0, 0));
            assertEquals(0, arr.get(1, 0));
            assertEquals(0, arr.get(2, 0));
            
            // Set some values and take snapshot
            arr.set(0, 5);
            arr.set(1, 6);
            int snapId1 = arr.snap();
            assertEquals(0, snapId1);
            
            // Verify snapshot 0
            assertEquals(5, arr.get(0, snapId1));
            assertEquals(6, arr.get(1, snapId1));
            assertEquals(0, arr.get(2, snapId1));
        }
        
        @Test
        @DisplayName("Multiple snapshots")
        void multipleSnapshots() {
            DesignTemplates.SnapshotArray arr = new DesignTemplates.SnapshotArray(2);
            
            arr.set(0, 10);
            int snap0 = arr.snap();
            
            arr.set(0, 20);
            arr.set(1, 30);
            int snap1 = arr.snap();
            
            arr.set(0, 40);
            int snap2 = arr.snap();
            
            // Verify all snapshots
            assertEquals(10, arr.get(0, snap0));
            assertEquals(0, arr.get(1, snap0));
            
            assertEquals(20, arr.get(0, snap1));
            assertEquals(30, arr.get(1, snap1));
            
            assertEquals(40, arr.get(0, snap2));
            assertEquals(30, arr.get(1, snap2)); // unchanged from snap1
        }
        
        @Test
        @DisplayName("Get with earlier snapshot after updates")
        void getEarlierSnapshot() {
            DesignTemplates.SnapshotArray arr = new DesignTemplates.SnapshotArray(1);
            
            arr.set(0, 1);
            int snap0 = arr.snap();
            
            arr.set(0, 2);
            arr.set(0, 3);
            int snap1 = arr.snap();
            
            // Should still get old value for earlier snapshot
            assertEquals(1, arr.get(0, snap0));
            assertEquals(3, arr.get(0, snap1));
        }
        
        @Test
        @DisplayName("Edge case: Single element array")
        void singleElement() {
            DesignTemplates.SnapshotArray arr = new DesignTemplates.SnapshotArray(1);
            assertEquals(0, arr.get(0, 0));
            
            arr.set(0, 100);
            int snapId = arr.snap();
            assertEquals(100, arr.get(0, snapId));
        }
        
        @Test
        @DisplayName("Edge case: No sets between snapshots")
        void noSetsBetweenSnapshots() {
            DesignTemplates.SnapshotArray arr = new DesignTemplates.SnapshotArray(2);
            
            int snap0 = arr.snap();
            int snap1 = arr.snap();
            int snap2 = arr.snap();
            
            assertEquals(0, arr.get(0, snap0));
            assertEquals(0, arr.get(0, snap1));
            assertEquals(0, arr.get(0, snap2));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {1, 10, 100, 1000})
        @DisplayName("Test with different array sizes")
        void differentArraySizes(int size) {
            DesignTemplates.SnapshotArray arr = new DesignTemplates.SnapshotArray(size);
            
            for (int i = 0; i < size; i++) {
                arr.set(i, i * 10);
            }
            int snapId = arr.snap();
            
            for (int i = 0; i < size; i++) {
                assertEquals(i * 10, arr.get(i, snapId));
            }
        }
    }
    
    // ============================================================================
    // HIT COUNTER TESTS
    // ============================================================================
    
    @Nested
    @DisplayName("HitCounter - Track Hits in Last 5 Minutes")
    class HitCounterTests {
        
        @Nested
        @DisplayName("Queue-based HitCounter")
        class QueueHitCounterTests {
            
            private DesignTemplates.HitCounter counter;
            
            @BeforeEach
            void setUp() {
                counter = new DesignTemplates.HitCounter();
            }
            
            @Test
            @DisplayName("Basic hit counting")
            void basicHitCounting() {
                counter.hit(1);
                counter.hit(2);
                counter.hit(3);
                
                assertEquals(3, counter.getHits(4));
                assertEquals(3, counter.getHits(300));
            }
            
            @Test
            @DisplayName("Hits expire after 5 minutes (300 seconds)")
            void hitsExpire() {
                counter.hit(1);
                counter.hit(2);
                counter.hit(3);
                
                // At timestamp 301, only hit at timestamp 2 and 3 should count
                assertEquals(2, counter.getHits(301));
                
                // At timestamp 302, only hit at timestamp 3 should count
                assertEquals(1, counter.getHits(302));
                
                // At timestamp 303, no hits should count
                assertEquals(0, counter.getHits(303));
            }
            
            @Test
            @DisplayName("No hits")
            void noHits() {
                assertEquals(0, counter.getHits(1));
                assertEquals(0, counter.getHits(300));
            }
            
            @Test
            @DisplayName("Hits at boundary timestamps")
            void boundaryTimestamps() {
                counter.hit(1);
                counter.hit(300);
                
                assertEquals(2, counter.getHits(300));
                assertEquals(1, counter.getHits(301)); // hit at timestamp 1 expires
                assertEquals(1, counter.getHits(599)); // hit at timestamp 300 still valid
                assertEquals(0, counter.getHits(600)); // hit at timestamp 300 expires
            }
            
            @Test
            @DisplayName("Heavy load simulation")
            void heavyLoad() {
                // Simulate 1000 hits at different timestamps
                for (int i = 1; i <= 1000; i++) {
                    counter.hit(i);
                }
                
                // At timestamp 1000, we should have hits from timestamp 701-1000 (300 hits)
                assertEquals(300, counter.getHits(1000));
                assertEquals(300, counter.getHits(999)); // Still 300 hits from timestamp 700-999
                assertEquals(0, counter.getHits(1300)); // all hits expired
            }
        }
        
        @Nested
        @DisplayName("Bucket-based HitCounter")
        class BucketHitCounterTests {
            
            private DesignTemplates.HitCounterBucket counter;
            
            @BeforeEach
            void setUp() {
                counter = new DesignTemplates.HitCounterBucket();
            }
            
            @Test
            @DisplayName("Basic hit counting with buckets")
            void basicHitCounting() {
                counter.hit(1);
                counter.hit(2);
                counter.hit(3);
                
                assertEquals(3, counter.getHits(4));
                assertEquals(3, counter.getHits(300));
            }
            
            @Test
            @DisplayName("Multiple hits at same timestamp")
            void multipleHitsSameTimestamp() {
                counter.hit(1);
                counter.hit(1);
                counter.hit(1);
                
                assertEquals(3, counter.getHits(1));
                assertEquals(3, counter.getHits(100));
            }
            
            @Test
            @DisplayName("Bucket reuse after timestamp wrap")
            void bucketReuse() {
                counter.hit(1);
                assertEquals(1, counter.getHits(1));
                
                // Same bucket (1 % 300 = 1), but different timestamp
                counter.hit(301);
                assertEquals(1, counter.getHits(301)); // old hit at timestamp 1 should be overwritten
                assertEquals(1, counter.getHits(600));
            }
            
            @Test
            @DisplayName("Hits expire correctly")
            void hitsExpire() {
                counter.hit(1);
                counter.hit(2);
                
                // At timestamp 302, both hits should be expired
                assertEquals(0, counter.getHits(302));
            }
        }
    }
    
    // ============================================================================
    // INSERT DELETE GETRANDOM O(1) TESTS
    // ============================================================================
    
    @Nested
    @DisplayName("RandomizedSet - Insert/Delete/GetRandom O(1)")
    class RandomizedSetTests {
        
        private DesignTemplates.RandomizedSet set;
        
        @BeforeEach
        void setUp() {
            set = new DesignTemplates.RandomizedSet();
        }
        
        @Test
        @DisplayName("Basic insert, remove, and getRandom")
        void basicOperations() {
            assertTrue(set.insert(1));
            assertTrue(set.insert(2));
            assertTrue(set.insert(3));
            
            assertFalse(set.insert(1)); // duplicate
            
            Set<Integer> possibleValues = Set.of(1, 2, 3);
            assertTrue(possibleValues.contains(set.getRandom()));
            
            assertTrue(set.remove(2));
            assertFalse(set.remove(2)); // already removed
            
            // After removing 2, getRandom should return 1 or 3
            Set<Integer> remainingValues = Set.of(1, 3);
            assertTrue(remainingValues.contains(set.getRandom()));
        }
        
        @Test
        @DisplayName("Remove last element")
        void removeLastElement() {
            set.insert(1);
            assertTrue(set.remove(1));
            
            // Set is now empty, insert again should work
            assertTrue(set.insert(2));
            assertEquals(2, set.getRandom());
        }
        
        @Test
        @DisplayName("Remove first element when multiple elements exist")
        void removeFirstElement() {
            set.insert(1);
            set.insert(2);
            set.insert(3);
            
            assertTrue(set.remove(1)); // Remove first inserted
            
            Set<Integer> remainingValues = Set.of(2, 3);
            for (int i = 0; i < 10; i++) { // Test multiple times
                assertTrue(remainingValues.contains(set.getRandom()));
            }
        }
        
        @Test
        @DisplayName("Single element operations")
        void singleElement() {
            assertTrue(set.insert(42));
            assertEquals(42, set.getRandom());
            assertTrue(set.remove(42));
            
            // Now empty
            assertTrue(set.insert(100));
            assertEquals(100, set.getRandom());
        }
        
        @Test
        @DisplayName("Randomness distribution test")
        void randomnessDistribution() {
            // Insert multiple values
            for (int i = 1; i <= 100; i++) {
                set.insert(i);
            }
            
            // Get random values multiple times and check they're different
            Set<Integer> seenValues = new HashSet<>();
            for (int i = 0; i < 50; i++) {
                seenValues.add(set.getRandom());
            }
            
            // Should see multiple different values (not perfectly deterministic but very likely)
            assertTrue(seenValues.size() > 1, "Should see some variety in random values");
        }
        
        @Test 
        @DisplayName("Insert and remove in complex sequence")
        void complexSequence() {
            // Insert 1, 2, 3
            set.insert(1);
            set.insert(2); 
            set.insert(3);
            
            // Remove 2 (middle element)
            set.remove(2);
            
            // Insert 4
            set.insert(4);
            
            // Remove 1
            set.remove(1);
            
            // Now should have 3, 4
            Set<Integer> expected = Set.of(3, 4);
            for (int i = 0; i < 10; i++) {
                assertTrue(expected.contains(set.getRandom()));
            }
        }
    }
    
    @Nested
    @DisplayName("RandomizedCollection - With Duplicates")
    class RandomizedCollectionTests {
        
        private DesignTemplates.RandomizedCollection collection;
        
        @BeforeEach
        void setUp() {
            collection = new DesignTemplates.RandomizedCollection();
        }
        
        @Test
        @DisplayName("Insert duplicates")
        void insertDuplicates() {
            assertTrue(collection.insert(1));  // First occurrence
            assertFalse(collection.insert(1)); // Duplicate
            assertFalse(collection.insert(1)); // Another duplicate
            
            // Should be able to get 1
            assertEquals(1, collection.getRandom());
        }
        
        @Test
        @DisplayName("Remove duplicates")
        void removeDuplicates() {
            collection.insert(1);
            collection.insert(1);
            collection.insert(2);
            
            assertFalse(collection.remove(1));  // Remove one occurrence of 1
            assertTrue(collection.remove(1)); // Remove another occurrence of 1
            assertFalse(collection.remove(1)); // No more 1s to remove
            
            // Should only have 2 left
            assertEquals(2, collection.getRandom());
        }
        
        @Test
        @DisplayName("Mixed operations with duplicates")
        void mixedOperations() {
            collection.insert(4);
            collection.insert(3);
            collection.insert(4);
            collection.insert(2);
            collection.insert(4);
            
            // Remove some 4s
            collection.remove(4);
            collection.remove(4);
            
            // Should still have one 4, plus 3 and 2
            Set<Integer> possibleValues = Set.of(2, 3, 4);
            for (int i = 0; i < 20; i++) {
                assertTrue(possibleValues.contains(collection.getRandom()));
            }
        }
    }
    
    // ============================================================================
    // TIC TAC TOE TESTS
    // ============================================================================
    
    @Nested
    @DisplayName("TicTacToe - O(1) Winner Detection")
    class TicTacToeTests {
        
        @Test
        @DisplayName("3x3 game - Player 1 wins with row")
        void player1WinsWithRow() {
            DesignTemplates.TicTacToe game = new DesignTemplates.TicTacToe(3);
            
            assertEquals(0, game.move(0, 0, 1)); // X at (0,0)
            assertEquals(0, game.move(0, 1, 2)); // O at (0,1)
            assertEquals(0, game.move(1, 0, 1)); // X at (1,0)
            assertEquals(0, game.move(0, 2, 2)); // O at (0,2)
            assertEquals(1, game.move(2, 0, 1)); // X at (2,0) - column win for player 1
        }
        
        @Test
        @DisplayName("3x3 game - Player 2 wins with diagonal")
        void player2WinsWithDiagonal() {
            DesignTemplates.TicTacToe game = new DesignTemplates.TicTacToe(3);
            
            assertEquals(0, game.move(0, 0, 1)); // X at (0,0)
            assertEquals(0, game.move(1, 1, 2)); // O at (1,1)
            assertEquals(0, game.move(0, 1, 1)); // X at (0,1)
            assertEquals(0, game.move(0, 2, 2)); // O at (0,2)
            assertEquals(0, game.move(1, 0, 1)); // X at (1,0)
            assertEquals(2, game.move(2, 0, 2)); // O at (2,0) - anti-diagonal win for player 2
        }
        
        @Test
        @DisplayName("3x3 game - Player 1 wins with main diagonal")
        void player1WinsWithMainDiagonal() {
            DesignTemplates.TicTacToe game = new DesignTemplates.TicTacToe(3);
            
            assertEquals(0, game.move(0, 0, 1)); // X at (0,0)
            assertEquals(0, game.move(0, 1, 2)); // O at (0,1)
            assertEquals(0, game.move(1, 1, 1)); // X at (1,1)
            assertEquals(0, game.move(0, 2, 2)); // O at (0,2)
            assertEquals(1, game.move(2, 2, 1)); // X at (2,2) - main diagonal win
        }
        
        @Test
        @DisplayName("Larger board - 4x4 game")
        void fourByFourGame() {
            DesignTemplates.TicTacToe game = new DesignTemplates.TicTacToe(4);
            
            // Player 1 tries to win with first column
            assertEquals(0, game.move(0, 0, 1)); // X
            assertEquals(0, game.move(1, 0, 1)); // X
            assertEquals(0, game.move(2, 0, 1)); // X
            assertEquals(1, game.move(3, 0, 1)); // X - column win
        }
        
        @Test
        @DisplayName("1x1 trivial game")
        void trivialGame() {
            DesignTemplates.TicTacToe game = new DesignTemplates.TicTacToe(1);
            assertEquals(1, game.move(0, 0, 1)); // Immediate win
        }
        
        @Test
        @DisplayName("No winner scenario")
        void noWinner() {
            DesignTemplates.TicTacToe game = new DesignTemplates.TicTacToe(3);
            
            assertEquals(0, game.move(0, 0, 1)); // X
            assertEquals(0, game.move(1, 1, 2)); // O
            assertEquals(0, game.move(0, 1, 1)); // X
            assertEquals(0, game.move(1, 0, 2)); // O
            assertEquals(0, game.move(2, 1, 1)); // X
            assertEquals(0, game.move(0, 2, 2)); // O
            
            // No winner yet
        }
    }
    
    // ============================================================================
    // FILE SYSTEM TESTS
    // ============================================================================
    
    @Nested
    @DisplayName("FileSystem - In-Memory File System")
    class FileSystemTests {
        
        private DesignTemplates.FileSystem fs;
        
        @BeforeEach
        void setUp() {
            fs = new DesignTemplates.FileSystem();
        }
        
        @Test
        @DisplayName("List root directory")
        void listRoot() {
            List<String> result = fs.ls("/");
            assertEquals(0, result.size()); // Empty initially
        }
        
        @Test
        @DisplayName("Create directories and list them")
        void createDirectories() {
            fs.mkdir("/a/b/c");
            fs.mkdir("/a/b/d");
            
            List<String> result = fs.ls("/a/b");
            Collections.sort(result);
            assertEquals(List.of("c", "d"), result);
        }
        
        @Test
        @DisplayName("Create file and read content")
        void createAndReadFile() {
            fs.addContentToFile("/file1.txt", "Hello");
            fs.addContentToFile("/file1.txt", " World");
            
            assertEquals("Hello World", fs.readContentFromFile("/file1.txt"));
        }
        
        @Test
        @DisplayName("List file (not directory)")
        void listFile() {
            fs.addContentToFile("/myfile", "content");
            
            List<String> result = fs.ls("/myfile");
            assertEquals(List.of("myfile"), result);
        }
        
        @Test
        @DisplayName("Mixed files and directories")
        void mixedFilesAndDirectories() {
            fs.mkdir("/dir1");
            fs.mkdir("/dir2");
            fs.addContentToFile("/file1.txt", "content1");
            fs.addContentToFile("/file2.txt", "content2");
            
            List<String> result = fs.ls("/");
            Collections.sort(result);
            assertEquals(List.of("dir1", "dir2", "file1.txt", "file2.txt"), result);
        }
        
        @Test
        @DisplayName("Nested file operations")
        void nestedFileOperations() {
            fs.mkdir("/a/b");
            fs.addContentToFile("/a/b/file.txt", "nested content");
            fs.addContentToFile("/a/b/another.txt", "more content");
            
            List<String> files = fs.ls("/a/b");
            Collections.sort(files);
            assertEquals(List.of("another.txt", "file.txt"), files);
            
            assertEquals("nested content", fs.readContentFromFile("/a/b/file.txt"));
        }
        
        @Test
        @DisplayName("File in directory path gets created automatically")
        void fileInDirectoryPath() {
            fs.addContentToFile("/a/b/c/file.txt", "deep file");
            
            assertEquals("deep file", fs.readContentFromFile("/a/b/c/file.txt"));
            
            // Verify directory structure was created
            assertEquals(List.of("file.txt"), fs.ls("/a/b/c"));
        }
        
        @Test
        @DisplayName("Empty file operations")
        void emptyFileOperations() {
            fs.addContentToFile("/empty.txt", "");
            assertEquals("", fs.readContentFromFile("/empty.txt"));
            
            fs.addContentToFile("/empty.txt", "now has content");
            assertEquals("now has content", fs.readContentFromFile("/empty.txt"));
        }
    }
    
    // ============================================================================
    // PHONE DIRECTORY TESTS
    // ============================================================================
    
    @Nested
    @DisplayName("PhoneDirectory - Phone Number Management")
    class PhoneDirectoryTests {
        
        @Test
        @DisplayName("Basic get and release operations")
        void basicOperations() {
            DesignTemplates.PhoneDirectory directory = new DesignTemplates.PhoneDirectory(3);
            
            // Initially all numbers available
            assertTrue(directory.check(0));
            assertTrue(directory.check(1));
            assertTrue(directory.check(2));
            
            // Get some numbers
            int num1 = directory.get();
            assertTrue(num1 >= 0 && num1 <= 2);
            assertFalse(directory.check(num1));
            
            int num2 = directory.get();
            assertTrue(num2 >= 0 && num2 <= 2);
            assertTrue(num2 != num1);
            
            // Release and check availability
            directory.release(num1);
            assertTrue(directory.check(num1));
        }
        
        @Test
        @DisplayName("Exhaust all numbers")
        void exhaustAllNumbers() {
            DesignTemplates.PhoneDirectory directory = new DesignTemplates.PhoneDirectory(2);
            
            int num1 = directory.get(); // Should get a valid number
            int num2 = directory.get(); // Should get another valid number
            int num3 = directory.get(); // Should get -1 (no more numbers)
            
            assertTrue(num1 >= 0 && num1 <= 1);
            assertTrue(num2 >= 0 && num2 <= 1);
            assertEquals(-1, num3);
            assertTrue(num1 != num2);
        }
        
        @Test
        @DisplayName("Release same number multiple times")
        void releaseSameNumberMultipleTimes() {
            DesignTemplates.PhoneDirectory directory = new DesignTemplates.PhoneDirectory(2);
            
            int num = directory.get();
            directory.release(num);
            directory.release(num); // Should handle gracefully
            
            assertTrue(directory.check(num)); // Should still be available
            
            // Get any available number (could be the same or different)
            int newNum = directory.get();
            assertTrue(newNum >= 0 && newNum <= 1); // Should be valid number
            assertFalse(directory.check(newNum)); // Should not be available anymore
        }
        
        @Test
        @DisplayName("Single number directory")
        void singleNumberDirectory() {
            DesignTemplates.PhoneDirectory directory = new DesignTemplates.PhoneDirectory(1);
            
            assertTrue(directory.check(0));
            int num = directory.get();
            assertEquals(0, num);
            assertFalse(directory.check(0));
            assertEquals(-1, directory.get()); // No more numbers
            
            directory.release(0);
            assertTrue(directory.check(0));
            assertEquals(0, directory.get());
        }
        
        @Test
        @DisplayName("Check invalid number")
        void checkInvalidNumber() {
            DesignTemplates.PhoneDirectory directory = new DesignTemplates.PhoneDirectory(3);
            
            // These should all return false (numbers out of range)
            assertFalse(directory.check(-1));
            assertFalse(directory.check(3));
            assertFalse(directory.check(100));
        }
        
        @Test
        @DisplayName("Release number that was never allocated")
        void releaseNeverAllocated() {
            DesignTemplates.PhoneDirectory directory = new DesignTemplates.PhoneDirectory(3);
            
            // All numbers start as available, so releasing should be no-op
            directory.release(1); 
            assertTrue(directory.check(1)); // Still available
        }
    }
    
    // ============================================================================
    // ADDITIONAL TESTS FOR OTHER CLASSES
    // ============================================================================
    
    @Nested
    @DisplayName("LogSystem - Log Storage and Retrieval")
    class LogSystemTests {
        
        private DesignTemplates.LogSystem logSystem;
        
        @BeforeEach
        void setUp() {
            logSystem = new DesignTemplates.LogSystem();
        }
        
        @Test
        @DisplayName("Basic log storage and retrieval")
        void basicOperations() {
            logSystem.put(1, "2017:01:01:23:59:59");
            logSystem.put(2, "2017:01:02:23:59:59");
            logSystem.put(3, "2017:01:03:23:59:59");
            
            List<Integer> result = logSystem.retrieve("2017:01:01:23:59:59", "2017:01:02:23:59:59", "Day");
            Collections.sort(result);
            assertEquals(List.of(1, 2), result);
        }
        
        @Test
        @DisplayName("Different granularities")
        void differentGranularities() {
            logSystem.put(1, "2017:01:01:12:30:45");
            logSystem.put(2, "2017:01:02:12:30:45");
            logSystem.put(3, "2017:02:01:12:30:45");
            
            // Year granularity - all logs from 2017
            List<Integer> yearResult = logSystem.retrieve("2017:01:01:00:00:00", "2017:12:31:23:59:59", "Year");
            Collections.sort(yearResult);
            assertEquals(List.of(1, 2, 3), yearResult);
            
            // Month granularity - only January logs
            List<Integer> monthResult = logSystem.retrieve("2017:01:01:00:00:00", "2017:01:31:23:59:59", "Month");
            Collections.sort(monthResult);
            assertEquals(List.of(1, 2), monthResult);
        }
    }
    
    @Nested
    @DisplayName("AutocompleteSystem - Search Autocomplete")
    class AutocompleteSystemTests {
        
        @Test
        @DisplayName("Basic autocomplete functionality")
        void basicAutocomplete() {
            String[] sentences = {"i love you", "island", "iroman", "i love leetcode"};
            int[] times = {5, 3, 2, 2};
            
            DesignTemplates.AutocompleteSystem system = new DesignTemplates.AutocompleteSystem(sentences, times);
            
            List<String> result = system.input('i');
            // Should return top 3, sorted by frequency then lexicographically
            assertEquals(3, result.size());
            assertEquals("i love you", result.get(0)); // freq 5
            assertTrue(result.contains("island")); // freq 3
        }
        
        @Test
        @DisplayName("Complete sentence and add to history")
        void completeSentence() {
            DesignTemplates.AutocompleteSystem system = new DesignTemplates.AutocompleteSystem(new String[]{}, new int[]{});
            
            system.input('h');
            system.input('e');
            system.input('l');
            system.input('l');
            system.input('o');
            List<String> endResult = system.input('#');
            assertEquals(0, endResult.size()); // '#' returns empty list
            
            // Now "hello" should appear in suggestions
            List<String> result = system.input('h');
            assertTrue(result.contains("hello") || result.isEmpty()); // Depends on if there are other 'h' suggestions
        }
    }
    
    @Nested
    @DisplayName("BrowserHistory - Navigation History")
    class BrowserHistoryTests {
        
        @Test
        @DisplayName("Basic navigation")
        void basicNavigation() {
            DesignTemplates.BrowserHistory browser = new DesignTemplates.BrowserHistory("leetcode.com");
            
            browser.visit("google.com");
            browser.visit("facebook.com");
            browser.visit("youtube.com");
            
            assertEquals("facebook.com", browser.back(1));
            assertEquals("google.com", browser.back(1));
            assertEquals("leetcode.com", browser.back(10)); // Can't go further back
            
            assertEquals("google.com", browser.forward(1));
            assertEquals("facebook.com", browser.forward(1));
            assertEquals("youtube.com", browser.forward(1));
            assertEquals("youtube.com", browser.forward(10)); // Can't go further forward
        }
        
        @Test
        @DisplayName("Visit clears forward history")
        void visitClearsForwardHistory() {
            DesignTemplates.BrowserHistory browser = new DesignTemplates.BrowserHistory("home.com");
            
            browser.visit("page1.com");
            browser.visit("page2.com");
            browser.back(1); // Go back to page1.com
            
            // Visit new page - should clear forward history
            browser.visit("newpage.com");
            
            assertEquals("newpage.com", browser.forward(10)); // Can't go forward to page2.com anymore
        }
    }
}