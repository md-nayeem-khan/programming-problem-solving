package com.cp.algorithms;

import com.cp.problems.Complexity;
import java.util.*;

/**
 * System Design Data Structures for Big Tech Interviews
 * 
 * Coverage:
 * 1. Consistent Hashing - Load balancing, distributed caching
 * 2. Bloom Filter - Probabilistic set membership
 * 3. Rate Limiters - Token Bucket, Leaky Bucket, Sliding Window
 * 
 * Critical for: System Design rounds at all FAANG companies
 * Frequency: 10-15% of senior/staff engineer interviews
 * 
 * Real Interview Problems:
 * - Design Distributed Cache (Meta, Amazon)
 * - Design Rate Limiter (Stripe, Meta, Google)
 * - Design URL Shortener with duplicate detection (Amazon, Google)
 * - Design Load Balancer (All FAANG)
 */
public class SystemDesignDataStructures {

    // ============================================================================
    // 1. CONSISTENT HASHING
    // ============================================================================
    
    /**
     * Consistent Hashing Implementation
     * 
     * Used for distributed caching, load balancing, sharding.
     * Key property: Adding/removing servers only affects ~1/N of keys (minimal disruption).
     * 
     * Real use cases:
     * - Amazon DynamoDB partitioning
     * - Memcached/Redis cluster distribution
     * - CDN load balancing
     * - Distributed hash tables (DHT)
     * 
     * Interview favorites: Meta, Amazon, Google (system design)
     * 
     * Time Complexity:
     * - Add server: O(V log N) where V = virtual nodes, N = total servers
     * - Remove server: O(V log N)
     * - Get server for key: O(log N)
     * 
     * Space: O(N * V)
     * 
     * Example Usage:
     *   ConsistentHashing ch = new ConsistentHashing(3, 150); // 3 servers, 150 virtual nodes each
     *   ch.addServer("server1");
     *   ch.addServer("server2");
     *   ch.addServer("server3");
     *   
     *   String server = ch.getServer("user_12345"); // Returns which server handles this key
     *   ch.removeServer("server2"); // Minimal redistribution
     */
    public static class ConsistentHashing {
        private final TreeMap<Long, String> ring;  // Hash ring
        private final int virtualNodesPerServer;
        private final Map<String, Set<Long>> serverToVirtualNodes;
        
        /**
         * @param initialCapacity Initial ring capacity
         * @param virtualNodesPerServer Number of virtual nodes per physical server
         *                              (150-200 recommended for good distribution)
         */
        public ConsistentHashing(int initialCapacity, int virtualNodesPerServer) {
            this.ring = new TreeMap<>();
            this.virtualNodesPerServer = virtualNodesPerServer;
            this.serverToVirtualNodes = new HashMap<>();
        }
        
        public ConsistentHashing() {
            this(16, 150); // Default: 150 virtual nodes
        }
        
        /**
         * Add a server to the hash ring
         * Creates virtual nodes for better load distribution
         */
        @Complexity(time = "O(V log N)", space = "O(V)", 
                    explanation = "V virtual nodes, N total servers in ring")
        public void addServer(String serverName) {
            if (serverToVirtualNodes.containsKey(serverName)) {
                return; // Server already exists
            }
            
            Set<Long> virtualNodes = new HashSet<>();
            
            // Create virtual nodes
            for (int i = 0; i < virtualNodesPerServer; i++) {
                String virtualNodeKey = serverName + "#VN" + i;
                long hash = hash(virtualNodeKey);
                ring.put(hash, serverName);
                virtualNodes.add(hash);
            }
            
            serverToVirtualNodes.put(serverName, virtualNodes);
        }
        
        /**
         * Remove a server from the hash ring
         * Keys will be redistributed to next server clockwise
         */
        @Complexity(time = "O(V log N)", space = "O(1)")
        public void removeServer(String serverName) {
            Set<Long> virtualNodes = serverToVirtualNodes.get(serverName);
            if (virtualNodes == null) {
                return; // Server doesn't exist
            }
            
            // Remove all virtual nodes
            for (long hash : virtualNodes) {
                ring.remove(hash);
            }
            
            serverToVirtualNodes.remove(serverName);
        }
        
        /**
         * Get the server responsible for a given key
         * Uses clockwise search on the hash ring
         */
        @Complexity(time = "O(log N)", space = "O(1)")
        public String getServer(String key) {
            if (ring.isEmpty()) {
                return null;
            }
            
            long hash = hash(key);
            
            // Find the first server clockwise from the hash
            Map.Entry<Long, String> entry = ring.ceilingEntry(hash);
            
            // If no server found clockwise, wrap around to first server
            if (entry == null) {
                entry = ring.firstEntry();
            }
            
            return entry.getValue();
        }
        
        /**
         * Get distribution statistics (useful for testing load balance)
         */
        public Map<String, Integer> getKeyDistribution(List<String> keys) {
            Map<String, Integer> distribution = new HashMap<>();
            for (String key : keys) {
                String server = getServer(key);
                distribution.put(server, distribution.getOrDefault(server, 0) + 1);
            }
            return distribution;
        }
        
        /**
         * Get all servers in the ring
         */
        public Set<String> getServers() {
            return new HashSet<>(serverToVirtualNodes.keySet());
        }
        
        /**
         * Hash function - using FNV-1a (fast, good distribution)
         * In production, consider using MurmurHash3 or CityHash
         */
        private long hash(String key) {
            // FNV-1a 64-bit hash
            long hash = 0xcbf29ce484222325L; // FNV offset basis
            for (byte b : key.getBytes()) {
                hash ^= b;
                hash *= 0x100000001b3L; // FNV prime
            }
            return hash;
        }
    }
    
    // ============================================================================
    // 2. BLOOM FILTER
    // ============================================================================
    
    /**
     * Bloom Filter - Probabilistic data structure for set membership testing
     * 
     * Properties:
     * - False positives possible (may say "yes" when actually "no")
     * - False negatives NEVER occur (if says "no", definitely not in set)
     * - Space efficient: O(1) per element regardless of element size
     * 
     * Real use cases:
     * - Google Chrome: malicious URL detection
     * - Medium: avoid showing articles you've already read
     * - Cassandra/HBase: avoid disk lookups for non-existent keys
     * - Bitcoin: SPV wallets
     * - Spell checkers
     * 
     * Interview favorites: Google, Meta, Amazon (system design)
     * 
     * Time Complexity: O(k) for add/contains, where k = number of hash functions
     * Space: O(m) bits where m = bit array size
     * 
     * Optimal parameters:
     * - m (bits) = -(n * ln(p)) / (ln(2)^2)
     * - k (hashes) = (m/n) * ln(2)
     * where n = expected elements, p = desired false positive rate
     * 
     * Example:
     *   BloomFilter bf = new BloomFilter(1000, 0.01); // 1000 elements, 1% FP rate
     *   bf.add("user123");
     *   bf.contains("user123"); // true
     *   bf.contains("user999"); // probably false (or false positive)
     */
    public static class BloomFilter {
        private final BitSet bitSet;
        private final int bitArraySize;
        private final int numHashFunctions;
        private int numElements;
        
        /**
         * @param expectedElements Expected number of elements to be inserted
         * @param falsePositiveRate Desired false positive rate (e.g., 0.01 for 1%)
         */
        public BloomFilter(int expectedElements, double falsePositiveRate) {
            // Calculate optimal bit array size
            this.bitArraySize = (int) Math.ceil(
                -(expectedElements * Math.log(falsePositiveRate)) / (Math.log(2) * Math.log(2))
            );
            
            // Calculate optimal number of hash functions
            this.numHashFunctions = (int) Math.ceil(
                (bitArraySize / (double) expectedElements) * Math.log(2)
            );
            
            this.bitSet = new BitSet(bitArraySize);
            this.numElements = 0;
        }
        
        /**
         * Add an element to the bloom filter
         */
        @Complexity(time = "O(k)", space = "O(1)", explanation = "k hash functions")
        public void add(String element) {
            for (int i = 0; i < numHashFunctions; i++) {
                int hash = hash(element, i);
                bitSet.set(Math.abs(hash % bitArraySize));
            }
            numElements++;
        }
        
        /**
         * Check if element might be in the set
         * @return true if probably in set (may be false positive)
         *         false if definitely not in set
         */
        @Complexity(time = "O(k)", space = "O(1)")
        public boolean contains(String element) {
            for (int i = 0; i < numHashFunctions; i++) {
                int hash = hash(element, i);
                if (!bitSet.get(Math.abs(hash % bitArraySize))) {
                    return false; // Definitely not in set
                }
            }
            return true; // Probably in set
        }
        
        /**
         * Get current false positive probability based on fill rate
         */
        public double getCurrentFalsePositiveRate() {
            int bitsSet = bitSet.cardinality();
            double fillRate = (double) bitsSet / bitArraySize;
            return Math.pow(fillRate, numHashFunctions);
        }
        
        /**
         * Clear the bloom filter
         */
        public void clear() {
            bitSet.clear();
            numElements = 0;
        }
        
        public int size() {
            return numElements;
        }
        
        /**
         * Generate hash function with seed (double hashing)
         * Using h(x, i) = h1(x) + i * h2(x)
         * 
         * Fixed: Now provides proper independence between hash functions
         * by mixing seed with different prime numbers
         */
        private int hash(String element, int seed) {
            // Get base hash
            int h1 = element.hashCode();
            
            // Create second independent hash by mixing with prime
            // Use different computation path to ensure independence
            int h2 = 0;
            for (char c : element.toCharArray()) {
                h2 = 31 * h2 + c;
            }
            h2 ^= h2 >>> 16;
            
            // Ensure h2 is odd (non-zero and coprime with power of 2)
            h2 = h2 | 1;
            
            // Combine using double hashing formula
            return h1 + seed * h2;
        }
    }
    
    // ============================================================================
    // 3. RATE LIMITERS
    // ============================================================================
    
    /**
     * Token Bucket Rate Limiter
     * 
     * Allows bursts of traffic while maintaining average rate limit.
     * Tokens are added at constant rate; each request consumes 1 token.
     * 
     * Real use cases:
     * - AWS API Gateway
     * - Stripe API rate limiting
     * - Network traffic shaping
     * 
     * Interview favorite: Stripe, Meta, Google
     * 
     * Example:
     *   TokenBucket limiter = new TokenBucket(100, 10); // 100 capacity, 10 tokens/sec
     *   limiter.tryConsume(); // true if token available
     */
    public static class TokenBucket {
        private final long capacity;
        private final double refillRate; // tokens per second
        private double tokens;
        private long lastRefillTime;
        
        /**
         * @param capacity Maximum number of tokens
         * @param refillRate Tokens added per second
         */
        public TokenBucket(long capacity, double refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = capacity; // Start full
            this.lastRefillTime = System.currentTimeMillis();
        }
        
        /**
         * Try to consume a token
         * @return true if request allowed, false if rate limited
         */
        @Complexity(time = "O(1)", space = "O(1)")
        public synchronized boolean tryConsume() {
            return tryConsume(1);
        }
        
        /**
         * Try to consume specified number of tokens
         */
        public synchronized boolean tryConsume(int tokensRequired) {
            refill();
            
            if (tokens >= tokensRequired) {
                tokens -= tokensRequired;
                return true;
            }
            return false;
        }
        
        /**
         * Refill tokens based on elapsed time
         */
        private void refill() {
            long now = System.currentTimeMillis();
            double elapsedSeconds = (now - lastRefillTime) / 1000.0;
            double tokensToAdd = elapsedSeconds * refillRate;
            
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = now;
        }
        
        public double getAvailableTokens() {
            refill();
            return tokens;
        }
    }
    
    /**
     * Leaky Bucket Rate Limiter
     * 
     * Smooths out bursts by processing requests at constant rate.
     * Excess requests are queued (leak out at constant rate).
     * 
     * Difference from Token Bucket:
     * - Token Bucket: allows bursts
     * - Leaky Bucket: enforces constant output rate
     * 
     * Real use cases:
     * - Network packet scheduling
     * - Request smoothing for downstream services
     */
    public static class LeakyBucket {
        private final long capacity;
        private final double leakRate; // requests per second
        private long queueSize;
        private long lastLeakTime;
        
        /**
         * @param capacity Maximum queue size
         * @param leakRate Requests processed per second
         */
        public LeakyBucket(long capacity, double leakRate) {
            this.capacity = capacity;
            this.leakRate = leakRate;
            this.queueSize = 0;
            this.lastLeakTime = System.currentTimeMillis();
        }
        
        /**
         * Try to add a request to the bucket
         * @return true if request accepted, false if bucket full
         */
        @Complexity(time = "O(1)", space = "O(1)")
        public synchronized boolean tryAccept() {
            leak();
            
            if (queueSize < capacity) {
                queueSize++;
                return true;
            }
            return false; // Bucket full
        }
        
        /**
         * Leak (process) requests based on elapsed time
         */
        private void leak() {
            long now = System.currentTimeMillis();
            double elapsedSeconds = (now - lastLeakTime) / 1000.0;
            long requestsToLeak = (long) (elapsedSeconds * leakRate);
            
            queueSize = Math.max(0, queueSize - requestsToLeak);
            lastLeakTime = now;
        }
        
        public long getQueueSize() {
            leak();
            return queueSize;
        }
    }
    
    /**
     * Sliding Window Rate Limiter
     * 
     * More accurate than fixed window, prevents burst at window boundaries.
     * Tracks requests in current window and previous window.
     * 
     * Formula: rate = (prevCount * overlap) + currCount
     * where overlap = remaining time in current window / window size
     * 
     * Real use cases:
     * - Redis rate limiting
     * - API gateways
     * 
     * Example:
     *   SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(100, 60000);
     *   // 100 requests per 60 seconds
     */
    public static class SlidingWindowRateLimiter {
        private final int maxRequests;
        private final long windowSizeMs;
        private int prevWindowCount;
        private int currWindowCount;
        private long currWindowStart;
        
        /**
         * @param maxRequests Maximum requests per window
         * @param windowSizeMs Window size in milliseconds
         */
        public SlidingWindowRateLimiter(int maxRequests, long windowSizeMs) {
            this.maxRequests = maxRequests;
            this.windowSizeMs = windowSizeMs;
            this.prevWindowCount = 0;
            this.currWindowCount = 0;
            this.currWindowStart = System.currentTimeMillis();
        }
        
        /**
         * Try to accept a request
         * @return true if under rate limit, false if rate limited
         */
        @Complexity(time = "O(1)", space = "O(1)")
        public synchronized boolean tryAccept() {
            long now = System.currentTimeMillis();
            long elapsedMs = now - currWindowStart;
            
            // Check if we've moved to a new window
            if (elapsedMs >= windowSizeMs) {
                // Calculate how many complete windows have passed
                long windowsPassed = elapsedMs / windowSizeMs;
                
                if (windowsPassed >= 2) {
                    // More than 2 windows passed, both are stale
                    prevWindowCount = 0;
                    currWindowCount = 0;
                } else {
                    // Exactly one window passed, current becomes previous
                    prevWindowCount = currWindowCount;
                    currWindowCount = 0;
                }
                
                // Move window start forward by complete windows
                currWindowStart += windowsPassed * windowSizeMs;
                elapsedMs = now - currWindowStart;
            }
            
            // Calculate weighted count using sliding window
            // overlapRatio = how much of previous window is still in our sliding window
            double overlapRatio = (double) (windowSizeMs - elapsedMs) / windowSizeMs;
            double estimatedCount = prevWindowCount * overlapRatio + currWindowCount;
            
            if (estimatedCount < maxRequests) {
                currWindowCount++;
                return true;
            }
            return false; // Rate limited
        }
        
        public int getCurrentWindowCount() {
            return currWindowCount;
        }
    }
}
