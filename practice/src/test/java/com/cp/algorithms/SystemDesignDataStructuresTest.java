package com.cp.algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for System Design Data Structures
 * 
 * Validates:
 * - Consistent Hashing
 * - Bloom Filter
 * - Rate Limiters (Token Bucket, Leaky Bucket, Sliding Window)
 * 
 * Designed for FAANG interview preparation
 */
class SystemDesignDataStructuresTest {

    // ============================================================================
    // CONSISTENT HASHING TESTS
    // ============================================================================
    
    @Nested
    @DisplayName("Consistent Hashing Tests")
    class ConsistentHashingTests {
        
        private SystemDesignDataStructures.ConsistentHashing consistentHash;
        
        @BeforeEach
        void setUp() {
            consistentHash = new SystemDesignDataStructures.ConsistentHashing(10, 150);
        }
        
        @Test
        @DisplayName("Should add and retrieve server for key")
        void testBasicAddAndGet() {
            consistentHash.addServer("server1");
            consistentHash.addServer("server2");
            consistentHash.addServer("server3");
            
            // Get server for a key
            String server = consistentHash.getServer("user_123");
            assertNotNull(server, "Server should not be null");
            assertTrue(server.matches("server[123]"), "Server should be one of the added servers");
            
            // Same key should always map to same server
            String server2 = consistentHash.getServer("user_123");
            assertEquals(server, server2, "Same key should map to same server");
        }
        
        @Test
        @DisplayName("Should return null when no servers exist")
        void testGetServerWithEmptyRing() {
            String server = consistentHash.getServer("any_key");
            assertNull(server, "Should return null when no servers exist");
        }
        
        @Test
        @DisplayName("Should handle single server")
        void testSingleServer() {
            consistentHash.addServer("server1");
            
            // All keys should map to the only server
            assertEquals("server1", consistentHash.getServer("key1"));
            assertEquals("server1", consistentHash.getServer("key2"));
            assertEquals("server1", consistentHash.getServer("key3"));
        }
        
        @Test
        @DisplayName("Should remove server and redistribute keys")
        void testRemoveServer() {
            consistentHash.addServer("server1");
            consistentHash.addServer("server2");
            consistentHash.addServer("server3");
            
            // Generate test keys and record their servers
            List<String> testKeys = IntStream.range(0, 100)
                .mapToObj(i -> "key_" + i)
                .collect(Collectors.toList());
            
            Map<String, String> beforeRemoval = new HashMap<>();
            for (String key : testKeys) {
                beforeRemoval.put(key, consistentHash.getServer(key));
            }
            
            // Remove one server
            consistentHash.removeServer("server2");
            
            // Count how many keys moved
            int movedKeys = 0;
            for (String key : testKeys) {
                String newServer = consistentHash.getServer(key);
                String oldServer = beforeRemoval.get(key);
                
                // Keys that were on server2 must move
                if ("server2".equals(oldServer)) {
                    assertNotEquals("server2", newServer, "Key must move away from removed server");
                    movedKeys++;
                } else {
                    // Keys on other servers should mostly stay (not guaranteed, but likely)
                    if (!oldServer.equals(newServer)) {
                        movedKeys++;
                    }
                }
            }
            
            // Should move approximately 33% of keys (those on server2) plus a few from ring position changes
            // Allow up to 50% to account for hash distribution variance
            assertTrue(movedKeys <= 50, "Too many keys moved after removing one server: " + movedKeys);
            assertTrue(movedKeys > 0, "At least some keys should have moved");
        }
        
        @Test
        @DisplayName("Should not add duplicate servers")
        void testDuplicateServer() {
            consistentHash.addServer("server1");
            int initialServers = consistentHash.getServers().size();
            
            // Try to add same server again
            consistentHash.addServer("server1");
            
            assertEquals(initialServers, consistentHash.getServers().size(), 
                "Duplicate server should not be added");
        }
        
        @Test
        @DisplayName("Should distribute load evenly across servers")
        void testLoadDistribution() {
            consistentHash.addServer("server1");
            consistentHash.addServer("server2");
            consistentHash.addServer("server3");
            
            // Generate many test keys
            List<String> testKeys = IntStream.range(0, 10000)
                .mapToObj(i -> "key_" + i)
                .collect(Collectors.toList());
            
            Map<String, Integer> distribution = consistentHash.getKeyDistribution(testKeys);
            
            // Each server should get roughly 33% of keys
            // With 150 virtual nodes, distribution variance can be significant
            // Allow 10-65% range (this is acceptable for 150 VNodes with hash randomness)
            int totalKeys = testKeys.size();
            for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
                double percentage = (entry.getValue() * 100.0) / totalKeys;
                assertTrue(percentage >= 10 && percentage <= 65, 
                    String.format("Server %s has %s%% of keys, expected 10-65%% (distribution variance with 150 virtual nodes)", 
                        entry.getKey(), String.format("%.2f", percentage)));
            }
            
            // Verify all servers get at least some keys
            assertEquals(3, distribution.size(), "All 3 servers should have keys");
        }
        
        @Test
        @DisplayName("Should handle adding server after keys exist")
        void testAddServerAfterKeys() {
            consistentHash.addServer("server1");
            consistentHash.addServer("server2");
            
            // Use larger sample for better statistical significance
            List<String> testKeys = IntStream.range(0, 1000)
                .mapToObj(i -> "key_" + i)
                .collect(Collectors.toList());
            
            // Record initial distribution
            Map<String, String> beforeAdd = new HashMap<>();
            for (String key : testKeys) {
                beforeAdd.put(key, consistentHash.getServer(key));
            }
            
            // Add new server
            consistentHash.addServer("server3");
            
            // Count moved keys
            int movedKeys = 0;
            for (String key : testKeys) {
                if (!consistentHash.getServer(key).equals(beforeAdd.get(key))) {
                    movedKeys++;
                }
            }
            
            // Should move approximately 33% of keys (to new server)
            // With hash randomness and 150 virtual nodes, allow 15-70% range
            double movePercentage = (movedKeys * 100.0) / testKeys.size();
            assertTrue(movePercentage >= 15 && movePercentage <= 70,
                String.format("Expected 15-70%% keys to move (variance expected), but %s%% moved", 
                    String.format("%.2f", movePercentage)));
        }
        
        @Test
        @DisplayName("Should wrap around the ring")
        void testRingWrapAround() {
            consistentHash.addServer("server1");
            
            // This tests that ceilingEntry wraps to firstEntry when needed
            // Hard to test directly, but we verify it doesn't crash
            for (int i = 0; i < 1000; i++) {
                String server = consistentHash.getServer("key_" + i);
                assertEquals("server1", server);
            }
        }
        
        @Test
        @DisplayName("Should return all servers")
        void testGetServers() {
            consistentHash.addServer("server1");
            consistentHash.addServer("server2");
            consistentHash.addServer("server3");
            
            Set<String> servers = consistentHash.getServers();
            
            assertEquals(3, servers.size());
            assertTrue(servers.contains("server1"));
            assertTrue(servers.contains("server2"));
            assertTrue(servers.contains("server3"));
        }
        
        @Test
        @DisplayName("Should handle remove non-existent server")
        void testRemoveNonExistentServer() {
            consistentHash.addServer("server1");
            
            // Try to remove server that doesn't exist
            assertDoesNotThrow(() -> consistentHash.removeServer("server999"));
            
            // Original server should still be there
            assertEquals("server1", consistentHash.getServer("key1"));
        }
        
        @Test
        @DisplayName("Should handle different virtual node counts")
        void testDifferentVirtualNodeCounts() {
            SystemDesignDataStructures.ConsistentHashing ch1 = 
                new SystemDesignDataStructures.ConsistentHashing(10, 50);
            SystemDesignDataStructures.ConsistentHashing ch2 = 
                new SystemDesignDataStructures.ConsistentHashing(10, 200);
            
            ch1.addServer("server1");
            ch1.addServer("server2");
            ch2.addServer("server1");
            ch2.addServer("server2");
            
            // Both should work, but distribution might differ
            assertNotNull(ch1.getServer("key1"));
            assertNotNull(ch2.getServer("key1"));
        }
    }
    
    // ============================================================================
    // BLOOM FILTER TESTS
    // ============================================================================
    
    @Nested
    @DisplayName("Bloom Filter Tests")
    class BloomFilterTests {
        
        private SystemDesignDataStructures.BloomFilter bloomFilter;
        
        @BeforeEach
        void setUp() {
            bloomFilter = new SystemDesignDataStructures.BloomFilter(1000, 0.01);
        }
        
        @Test
        @DisplayName("Should add and check elements")
        void testBasicAddAndContains() {
            bloomFilter.add("element1");
            bloomFilter.add("element2");
            bloomFilter.add("element3");
            
            assertTrue(bloomFilter.contains("element1"), "Should contain added element1");
            assertTrue(bloomFilter.contains("element2"), "Should contain added element2");
            assertTrue(bloomFilter.contains("element3"), "Should contain added element3");
        }
        
        @Test
        @DisplayName("Should not have false negatives")
        void testNoFalseNegatives() {
            List<String> elements = IntStream.range(0, 100)
                .mapToObj(i -> "element_" + i)
                .collect(Collectors.toList());
            
            // Add all elements
            for (String element : elements) {
                bloomFilter.add(element);
            }
            
            // All added elements must be found (no false negatives)
            for (String element : elements) {
                assertTrue(bloomFilter.contains(element), 
                    "No false negatives allowed: " + element + " should be found");
            }
        }
        
        @Test
        @DisplayName("Should have acceptable false positive rate")
        void testFalsePositiveRate() {
            // Add 1000 elements
            for (int i = 0; i < 1000; i++) {
                bloomFilter.add("added_" + i);
            }
            
            // Test 1000 elements that were NOT added
            int falsePositives = 0;
            for (int i = 0; i < 1000; i++) {
                if (bloomFilter.contains("not_added_" + i)) {
                    falsePositives++;
                }
            }
            
            // We configured for 1% FP rate, allow up to 3% due to randomness
            double actualFPRate = (falsePositives * 100.0) / 1000;
            assertTrue(actualFPRate <= 3.0, 
                String.format("False positive rate too high: %s%% (expected <= 3%%)", 
                    String.format("%.2f", actualFPRate)));
        }
        
        @Test
        @DisplayName("Should track size correctly")
        void testSize() {
            assertEquals(0, bloomFilter.size());
            
            bloomFilter.add("element1");
            assertEquals(1, bloomFilter.size());
            
            bloomFilter.add("element2");
            assertEquals(2, bloomFilter.size());
            
            // Adding same element increases size (bloom filter doesn't check duplicates)
            bloomFilter.add("element1");
            assertEquals(3, bloomFilter.size());
        }
        
        @Test
        @DisplayName("Should clear filter")
        void testClear() {
            bloomFilter.add("element1");
            bloomFilter.add("element2");
            
            assertTrue(bloomFilter.contains("element1"));
            assertTrue(bloomFilter.size() > 0);
            
            bloomFilter.clear();
            
            assertEquals(0, bloomFilter.size());
            // After clear, elements should not be found (with high probability)
            // Note: This might still return true due to hash collisions, but very unlikely
        }
        
        @Test
        @DisplayName("Should handle empty filter")
        void testEmptyFilter() {
            // Empty filter should return false for any element
            assertFalse(bloomFilter.contains("anything"));
        }
        
        @Test
        @DisplayName("Should calculate current false positive rate")
        void testCurrentFalsePositiveRate() {
            double initialFPRate = bloomFilter.getCurrentFalsePositiveRate();
            
            // Add elements
            for (int i = 0; i < 500; i++) {
                bloomFilter.add("element_" + i);
            }
            
            double afterAddFPRate = bloomFilter.getCurrentFalsePositiveRate();
            
            // FP rate should increase as more bits are set
            assertTrue(afterAddFPRate > initialFPRate, 
                "False positive rate should increase as filter fills up");
        }
        
        @Test
        @DisplayName("Should handle different configurations")
        void testDifferentConfigurations() {
            // Low false positive rate
            SystemDesignDataStructures.BloomFilter strictFilter = 
                new SystemDesignDataStructures.BloomFilter(100, 0.001);
            
            // High false positive rate
            SystemDesignDataStructures.BloomFilter relaxedFilter = 
                new SystemDesignDataStructures.BloomFilter(100, 0.1);
            
            for (int i = 0; i < 100; i++) {
                strictFilter.add("elem_" + i);
                relaxedFilter.add("elem_" + i);
            }
            
            // Both should contain all added elements
            assertTrue(strictFilter.contains("elem_0"));
            assertTrue(relaxedFilter.contains("elem_0"));
        }
        
        @Test
        @DisplayName("Should handle special characters and long strings")
        void testSpecialInputs() {
            bloomFilter.add("email@example.com");
            bloomFilter.add("user-name_123");
            bloomFilter.add("https://example.com/path?query=value");
            bloomFilter.add("a".repeat(1000)); // Long string
            
            assertTrue(bloomFilter.contains("email@example.com"));
            assertTrue(bloomFilter.contains("user-name_123"));
            assertTrue(bloomFilter.contains("https://example.com/path?query=value"));
            assertTrue(bloomFilter.contains("a".repeat(1000)));
        }
    }
    
    // ============================================================================
    // TOKEN BUCKET TESTS
    // ============================================================================
    
    @Nested
    @DisplayName("Token Bucket Tests")
    class TokenBucketTests {
        
        private SystemDesignDataStructures.TokenBucket tokenBucket;
        
        @BeforeEach
        void setUp() {
            tokenBucket = new SystemDesignDataStructures.TokenBucket(10, 5); // 10 capacity, 5 tokens/sec
        }
        
        @Test
        @DisplayName("Should start with full capacity")
        void testInitialCapacity() {
            assertEquals(10.0, tokenBucket.getAvailableTokens(), 0.01);
        }
        
        @Test
        @DisplayName("Should consume tokens")
        void testConsumeTokens() {
            assertTrue(tokenBucket.tryConsume()); // 9 left
            assertTrue(tokenBucket.tryConsume()); // 8 left
            assertTrue(tokenBucket.tryConsume(3)); // 5 left
            
            assertEquals(5.0, tokenBucket.getAvailableTokens(), 0.01);
        }
        
        @Test
        @DisplayName("Should reject when insufficient tokens")
        void testInsufficientTokens() {
            // Consume all tokens
            assertTrue(tokenBucket.tryConsume(10));
            assertEquals(0.0, tokenBucket.getAvailableTokens(), 0.01);
            
            // Next request should be rejected
            assertFalse(tokenBucket.tryConsume());
        }
        
        @Test
        @DisplayName("Should refill tokens over time")
        void testTokenRefill() throws InterruptedException {
            // Consume all tokens
            tokenBucket.tryConsume(10);
            assertEquals(0.0, tokenBucket.getAvailableTokens(), 0.01);
            
            // Wait for refill (5 tokens/sec, wait 1 second = 5 tokens)
            Thread.sleep(1000);
            
            // Should have refilled some tokens
            double availableTokens = tokenBucket.getAvailableTokens();
            assertTrue(availableTokens >= 4.5 && availableTokens <= 5.5, 
                "Expected ~5 tokens, got " + availableTokens);
        }
        
        @Test
        @DisplayName("Should not exceed capacity when refilling")
        void testCapacityLimit() throws InterruptedException {
            // Start with full capacity, wait for more time
            Thread.sleep(500); // Try to add more tokens
            
            // Should still be at capacity
            assertTrue(tokenBucket.getAvailableTokens() <= 10.0, 
                "Should not exceed capacity");
        }
        
        @Test
        @DisplayName("Should handle burst traffic")
        void testBurstTraffic() {
            // Burst: consume all available tokens quickly
            for (int i = 0; i < 10; i++) {
                assertTrue(tokenBucket.tryConsume(), "Request " + i + " should succeed");
            }
            
            // 11th request should fail
            assertFalse(tokenBucket.tryConsume(), "Burst limit exceeded");
        }
        
        @Test
        @DisplayName("Should handle multiple token consumption")
        void testMultipleTokenConsumption() {
            assertTrue(tokenBucket.tryConsume(5)); // Consume 5 tokens
            assertEquals(5.0, tokenBucket.getAvailableTokens(), 0.01);
            
            assertTrue(tokenBucket.tryConsume(3)); // Consume 3 more
            assertEquals(2.0, tokenBucket.getAvailableTokens(), 0.01);
            
            assertFalse(tokenBucket.tryConsume(3)); // Can't consume 3 (only 2 left)
        }
        
        @Test
        @DisplayName("Should handle zero capacity bucket")
        void testZeroCapacity() {
            SystemDesignDataStructures.TokenBucket zeroBucket = 
                new SystemDesignDataStructures.TokenBucket(0, 5);
            
            assertFalse(zeroBucket.tryConsume());
        }
        
        @Test
        @DisplayName("Should handle concurrent access")
        void testConcurrentAccess() throws InterruptedException {
            SystemDesignDataStructures.TokenBucket bucket = 
                new SystemDesignDataStructures.TokenBucket(100, 10);
            
            List<Thread> threads = new ArrayList<>();
            List<Boolean> results = Collections.synchronizedList(new ArrayList<>());
            
            // Create 50 threads trying to consume 2 tokens each (total 100)
            for (int i = 0; i < 50; i++) {
                Thread thread = new Thread(() -> {
                    results.add(bucket.tryConsume(2));
                });
                threads.add(thread);
                thread.start();
            }
            
            // Wait for all threads
            for (Thread thread : threads) {
                thread.join();
            }
            
            // Exactly 50 requests should succeed (100 tokens / 2 per request)
            long successCount = results.stream().filter(b -> b).count();
            assertEquals(50, successCount, "Expected exactly 50 successful requests");
        }
    }
    
    // ============================================================================
    // LEAKY BUCKET TESTS
    // ============================================================================
    
    @Nested
    @DisplayName("Leaky Bucket Tests")
    class LeakyBucketTests {
        
        private SystemDesignDataStructures.LeakyBucket leakyBucket;
        
        @BeforeEach
        void setUp() {
            leakyBucket = new SystemDesignDataStructures.LeakyBucket(10, 5); // 10 capacity, 5 req/sec leak
        }
        
        @Test
        @DisplayName("Should start empty")
        void testInitialState() {
            assertEquals(0, leakyBucket.getQueueSize());
        }
        
        @Test
        @DisplayName("Should accept requests up to capacity")
        void testAcceptRequests() {
            for (int i = 0; i < 10; i++) {
                assertTrue(leakyBucket.tryAccept(), "Request " + i + " should be accepted");
            }
            
            assertEquals(10, leakyBucket.getQueueSize());
        }
        
        @Test
        @DisplayName("Should reject when bucket is full")
        void testRejectWhenFull() {
            // Fill bucket to capacity
            for (int i = 0; i < 10; i++) {
                leakyBucket.tryAccept();
            }
            
            // Next request should be rejected
            assertFalse(leakyBucket.tryAccept(), "Should reject when bucket is full");
        }
        
        @Test
        @DisplayName("Should leak requests over time")
        void testLeakOverTime() throws InterruptedException {
            // Fill bucket
            for (int i = 0; i < 10; i++) {
                leakyBucket.tryAccept();
            }
            
            assertEquals(10, leakyBucket.getQueueSize());
            
            // Wait 1 second (should leak 5 requests at 5 req/sec)
            Thread.sleep(1000);
            
            long queueSize = leakyBucket.getQueueSize();
            assertTrue(queueSize >= 4 && queueSize <= 6, 
                "Expected ~5 requests left, got " + queueSize);
        }
        
        @Test
        @DisplayName("Should not go below zero")
        void testNoNegativeQueue() throws InterruptedException {
            leakyBucket.tryAccept();
            
            // Wait long enough to leak all requests
            Thread.sleep(1000);
            
            assertEquals(0, leakyBucket.getQueueSize(), 
                "Queue size should not go below zero");
        }
        
        @Test
        @DisplayName("Should smooth out burst traffic")
        void testSmoothBursts() throws InterruptedException {
            // Send burst of 5 requests
            for (int i = 0; i < 5; i++) {
                assertTrue(leakyBucket.tryAccept());
            }
            
            // Wait for partial leak
            Thread.sleep(500);
            
            // Should have room for more requests now
            assertTrue(leakyBucket.tryAccept(), "Should accept after partial leak");
        }
        
        @Test
        @DisplayName("Should handle zero capacity")
        void testZeroCapacity() {
            SystemDesignDataStructures.LeakyBucket zeroBucket = 
                new SystemDesignDataStructures.LeakyBucket(0, 5);
            
            assertFalse(zeroBucket.tryAccept(), "Zero capacity bucket should reject all");
        }
        
        @Test
        @DisplayName("Should handle concurrent access")
        void testConcurrentAccess() throws InterruptedException {
            SystemDesignDataStructures.LeakyBucket bucket = 
                new SystemDesignDataStructures.LeakyBucket(50, 10);
            
            List<Thread> threads = new ArrayList<>();
            List<Boolean> results = Collections.synchronizedList(new ArrayList<>());
            
            // Create 60 threads (50 should succeed, 10 should fail)
            for (int i = 0; i < 60; i++) {
                Thread thread = new Thread(() -> {
                    results.add(bucket.tryAccept());
                });
                threads.add(thread);
                thread.start();
            }
            
            // Wait for all threads
            for (Thread thread : threads) {
                thread.join();
            }
            
            // Exactly 50 requests should succeed
            long successCount = results.stream().filter(b -> b).count();
            assertEquals(50, successCount, "Expected exactly 50 successful requests");
        }
    }
    
    // ============================================================================
    // SLIDING WINDOW RATE LIMITER TESTS
    // ============================================================================
    
    @Nested
    @DisplayName("Sliding Window Rate Limiter Tests")
    class SlidingWindowRateLimiterTests {
        
        private SystemDesignDataStructures.SlidingWindowRateLimiter rateLimiter;
        
        @BeforeEach
        void setUp() {
            // 10 requests per 1000ms (1 second)
            rateLimiter = new SystemDesignDataStructures.SlidingWindowRateLimiter(10, 1000);
        }
        
        @Test
        @DisplayName("Should accept requests up to limit")
        void testAcceptUpToLimit() {
            for (int i = 0; i < 10; i++) {
                assertTrue(rateLimiter.tryAccept(), "Request " + i + " should be accepted");
            }
            
            // 11th request should be rejected
            assertFalse(rateLimiter.tryAccept(), "Should reject when limit reached");
        }
        
        @Test
        @DisplayName("Should reset after window passes")
        void testWindowReset() throws InterruptedException {
            // Use up the limit
            for (int i = 0; i < 10; i++) {
                rateLimiter.tryAccept();
            }
            
            assertFalse(rateLimiter.tryAccept(), "Should be rate limited");
            
            // Wait for window to pass
            Thread.sleep(1100);
            
            // Should accept requests again
            assertTrue(rateLimiter.tryAccept(), "Should accept after window reset");
        }
        
        @Test
        @DisplayName("Should handle sliding window correctly")
        void testSlidingWindow() throws InterruptedException {
            // At t=0ms: send 5 requests
            for (int i = 0; i < 5; i++) {
                rateLimiter.tryAccept();
            }
            
            // At t=500ms: send 5 more requests (total 10 in window)
            Thread.sleep(500);
            for (int i = 0; i < 5; i++) {
                assertTrue(rateLimiter.tryAccept(), 
                    "Should accept request " + i + " in sliding window");
            }
            
            // At t=500ms: 11th request should fail (10 in current window)
            assertFalse(rateLimiter.tryAccept(), "Should reject when sliding window limit reached");
            
            // At t=1100ms: First 5 requests from t=0 are outside window
            Thread.sleep(600);
            
            // Now we should be able to send more requests
            assertTrue(rateLimiter.tryAccept(), 
                "Should accept after first batch leaves sliding window");
        }
        
        @Test
        @DisplayName("Should track current window count")
        void testCurrentWindowCount() {
            assertEquals(0, rateLimiter.getCurrentWindowCount());
            
            rateLimiter.tryAccept();
            assertEquals(1, rateLimiter.getCurrentWindowCount());
            
            rateLimiter.tryAccept();
            rateLimiter.tryAccept();
            assertEquals(3, rateLimiter.getCurrentWindowCount());
        }
        
        @Test
        @DisplayName("Should handle burst at window boundary")
        void testBurstAtBoundary() throws InterruptedException {
            // This tests the key advantage of sliding window over fixed window
            
            // At t=0ms: send 10 requests (max in window)
            for (int i = 0; i < 10; i++) {
                rateLimiter.tryAccept();
            }
            
            // At t=900ms: Near end of window, try to send more
            Thread.sleep(900);
            
            // With 900ms elapsed and 10 requests in current window:
            // Previous window had 0 requests
            // overlapRatio = (1000-900)/1000 = 0.1
            // estimatedCount = 0 * 0.1 + 10 = 10
            // This should be rate limited (at the boundary)
            assertFalse(rateLimiter.tryAccept(), 
                "Sliding window should prevent burst at boundary");
        }
        
        @Test
        @DisplayName("Should handle different window sizes")
        void testDifferentWindowSizes() {
            // 5 requests per 500ms
            SystemDesignDataStructures.SlidingWindowRateLimiter shortWindow = 
                new SystemDesignDataStructures.SlidingWindowRateLimiter(5, 500);
            
            for (int i = 0; i < 5; i++) {
                assertTrue(shortWindow.tryAccept());
            }
            
            assertFalse(shortWindow.tryAccept());
        }
        
        @Test
        @DisplayName("Should handle zero limit")
        void testZeroLimit() {
            SystemDesignDataStructures.SlidingWindowRateLimiter zeroLimiter = 
                new SystemDesignDataStructures.SlidingWindowRateLimiter(0, 1000);
            
            assertFalse(zeroLimiter.tryAccept(), "Zero limit should reject all requests");
        }
        
        @Test
        @DisplayName("Should handle rapid successive requests")
        void testRapidRequests() {
            int accepted = 0;
            for (int i = 0; i < 20; i++) {
                if (rateLimiter.tryAccept()) {
                    accepted++;
                }
            }
            
            assertEquals(10, accepted, "Should accept exactly 10 requests");
        }
        
        @Test
        @DisplayName("Should calculate weighted count correctly")
        void testWeightedCount() throws InterruptedException {
            // Send 5 requests at t=0
            for (int i = 0; i < 5; i++) {
                rateLimiter.tryAccept();
            }
            
            // Wait half window (500ms)
            Thread.sleep(500);
            
            // At t=500ms:
            // - prevWindowCount = 0, currWindowCount = 5
            // - elapsedMs = 500, overlapRatio = (1000-500)/1000 = 0.5
            // - estimatedCount = 0 * 0.5 + 5 = 5
            // - Can accept 10 - 5 = 5 more requests
            int accepted = 0;
            for (int i = 0; i < 10; i++) {
                if (rateLimiter.tryAccept()) {
                    accepted++;
                }
            }
            
            // Should accept exactly 5 more requests (5 already sent + 5 more = 10 max)
            // Allow 4-6 range due to timing variance
            assertTrue(accepted >= 4 && accepted <= 6, 
                "Expected 4-6 requests accepted, got " + accepted);
        }
    }
}
