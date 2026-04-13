package com.cp.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import com.cp.algorithms.PersistentDataStructures.*;

/**
 * Comprehensive test suite for Persistent Data Structures
 * Tests all four implementations:
 * 1. PersistentSegmentTree
 * 2. PersistentArray
 * 3. PersistentStack
 * 4. PersistentDSU
 */
public class PersistentDataStructuresTest {

    @Nested
    @DisplayName("Persistent Segment Tree Tests")
    class PersistentSegmentTreeTests {

        @Test
        @DisplayName("Build initial tree and query full range")
        void testBuildAndQueryFullRange() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            assertEquals(15, pst.query(0, 0, 4), "Sum of [1,2,3,4,5] should be 15");
        }

        @Test
        @DisplayName("Query partial ranges")
        void testQueryPartialRanges() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            assertEquals(6, pst.query(0, 0, 2), "Sum [0,2] should be 6 (1+2+3)");
            assertEquals(12, pst.query(0, 2, 4), "Sum [2,4] should be 12 (3+4+5)");
            assertEquals(3, pst.query(0, 2, 2), "Sum [2,2] should be 3");
            assertEquals(14, pst.query(0, 1, 4), "Sum [1,4] should be 14 (2+3+4+5)");
        }

        @Test
        @DisplayName("Single update creates new version")
        void testSingleUpdate() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            int v1 = pst.update(0, 2, 10);
            
            assertEquals(1, v1, "New version should be 1");
            assertEquals(15, pst.query(0, 0, 4), "Version 0 should remain unchanged: 15");
            assertEquals(22, pst.query(v1, 0, 4), "Version 1 should reflect update: 1+2+10+4+5 = 22");
        }

        @Test
        @DisplayName("Multiple updates create multiple versions")
        void testMultipleUpdates() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            int v1 = pst.update(0, 2, 10);  // [1, 2, 10, 4, 5]
            int v2 = pst.update(v1, 4, 20); // [1, 2, 10, 4, 20]
            int v3 = pst.update(v2, 0, 5);  // [5, 2, 10, 4, 20]
            
            assertEquals(15, pst.query(0, 0, 4), "Version 0: 15");
            assertEquals(22, pst.query(v1, 0, 4), "Version 1: 22");
            assertEquals(37, pst.query(v2, 0, 4), "Version 2: 37");
            assertEquals(41, pst.query(v3, 0, 4), "Version 3: 41");
        }

        @Test
        @DisplayName("Branching versions - update from different versions")
        void testBranchingVersions() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            int v1 = pst.update(0, 2, 10);  // Branch from v0
            int v2 = pst.update(0, 3, 20);  // Another branch from v0
            
            assertEquals(15, pst.query(0, 0, 4), "Version 0: 15");
            assertEquals(22, pst.query(v1, 0, 4), "Version 1 (from v0): 22");
            assertEquals(31, pst.query(v2, 0, 4), "Version 2 (from v0): 31");
        }

        @Test
        @DisplayName("Update at boundaries")
        void testUpdateAtBoundaries() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            int v1 = pst.update(0, 0, 100); // Update first element
            int v2 = pst.update(0, 4, 100); // Update last element
            
            assertEquals(114, pst.query(v1, 0, 4), "First element updated");
            assertEquals(110, pst.query(v2, 0, 4), "Last element updated");
        }

        @Test
        @DisplayName("Single element array")
        void testSingleElementArray() {
            int[] arr = {42};
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            assertEquals(42, pst.query(0, 0, 0), "Single element query");
            
            int v1 = pst.update(0, 0, 100);
            assertEquals(42, pst.query(0, 0, 0), "Version 0 unchanged");
            assertEquals(100, pst.query(v1, 0, 0), "Version 1 updated");
        }

        @Test
        @DisplayName("Large array performance")
        void testLargeArray() {
            int n = 10000;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = i + 1;
            }
            
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            long expectedSum = (long) n * (n + 1) / 2;
            assertEquals(expectedSum, pst.query(0, 0, n - 1), "Sum of 1 to n");
            
            // Multiple updates
            int v1 = pst.update(0, 0, 1000);
            int v2 = pst.update(v1, 5000, 2000);
            
            assertEquals(expectedSum, pst.query(0, 0, n - 1), "Version 0 unchanged");
            assertTrue(pst.query(v1, 0, n - 1) != expectedSum, "Version 1 changed");
            assertTrue(pst.query(v2, 0, n - 1) != pst.query(v1, 0, n - 1), "Version 2 different from v1");
        }

        @Test
        @DisplayName("Version count tracking")
        void testVersionCount() {
            int[] arr = {1, 2, 3};
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            assertEquals(1, pst.getVersionCount(), "Initial version count");
            
            pst.update(0, 0, 10);
            assertEquals(2, pst.getVersionCount(), "After one update");
            
            pst.update(1, 1, 20);
            assertEquals(3, pst.getVersionCount(), "After two updates");
        }

        @Test
        @DisplayName("Query outside range returns 0")
        void testQueryOutsideRange() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            assertEquals(0, pst.query(0, 5, 10), "Query completely outside should return 0");
            assertEquals(0, pst.query(0, -5, -1), "Query negative range should return 0");
        }
    }

    @Nested
    @DisplayName("Persistent Array Tests")
    class PersistentArrayTests {

        @Test
        @DisplayName("Initialize from array and get values")
        void testInitializeAndGet() {
            int[] arr = {10, 20, 30, 40, 50};
            PersistentArray pa = new PersistentArray(arr);
            
            assertEquals(10, pa.get(0), "First element");
            assertEquals(30, pa.get(2), "Middle element");
            assertEquals(50, pa.get(4), "Last element");
        }

        @Test
        @DisplayName("Set creates new version, old unchanged")
        void testSetCreatesNewVersion() {
            int[] arr = {10, 20, 30};
            PersistentArray pa1 = new PersistentArray(arr);
            PersistentArray pa2 = pa1.set(1, 100);
            
            assertEquals(20, pa1.get(1), "Original array unchanged");
            assertEquals(100, pa2.get(1), "New array has updated value");
            
            // Other elements remain same
            assertEquals(10, pa2.get(0), "Other elements unchanged in new version");
            assertEquals(30, pa2.get(2), "Other elements unchanged in new version");
        }

        @Test
        @DisplayName("Multiple sets create independent versions")
        void testMultipleSets() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentArray pa1 = new PersistentArray(arr);
            PersistentArray pa2 = pa1.set(2, 100);
            PersistentArray pa3 = pa2.set(4, 200);
            PersistentArray pa4 = pa1.set(0, 300); // Branch from pa1
            
            assertEquals(3, pa1.get(2), "pa1[2] unchanged");
            assertEquals(100, pa2.get(2), "pa2[2] updated");
            assertEquals(100, pa3.get(2), "pa3[2] inherited from pa2");
            assertEquals(200, pa3.get(4), "pa3[4] updated");
            
            assertEquals(300, pa4.get(0), "pa4[0] updated");
            assertEquals(2, pa4.get(1), "pa4[1] inherited from pa1");
            assertEquals(3, pa4.get(2), "pa4[2] inherited from pa1 (not pa2)");
        }

        @Test
        @DisplayName("Initialize with size creates zero array")
        void testInitializeWithSize() {
            PersistentArray pa = new PersistentArray(5);
            
            for (int i = 0; i < 5; i++) {
                assertEquals(0, pa.get(i), "All elements should be 0");
            }
        }

        @Test
        @DisplayName("Set at boundaries")
        void testSetAtBoundaries() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentArray pa = new PersistentArray(arr);
            
            PersistentArray pa1 = pa.set(0, 100);
            PersistentArray pa2 = pa.set(4, 200);
            
            assertEquals(100, pa1.get(0), "First element updated");
            assertEquals(1, pa.get(0), "Original first element unchanged");
            
            assertEquals(200, pa2.get(4), "Last element updated");
            assertEquals(5, pa.get(4), "Original last element unchanged");
        }

        @Test
        @DisplayName("Single element array")
        void testSingleElementArray() {
            int[] arr = {42};
            PersistentArray pa1 = new PersistentArray(arr);
            PersistentArray pa2 = pa1.set(0, 100);
            
            assertEquals(42, pa1.get(0), "Original value");
            assertEquals(100, pa2.get(0), "Updated value");
        }

        @Test
        @DisplayName("Large array performance")
        void testLargeArray() {
            int n = 10000;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = i;
            }
            
            PersistentArray pa = new PersistentArray(arr);
            
            // Multiple updates
            PersistentArray pa1 = pa.set(0, -1);
            PersistentArray pa2 = pa1.set(5000, -1);
            PersistentArray pa3 = pa2.set(9999, -1);
            
            assertEquals(0, pa.get(0), "Original unchanged");
            assertEquals(-1, pa1.get(0), "Version 1 updated");
            assertEquals(-1, pa2.get(5000), "Version 2 updated");
            assertEquals(-1, pa3.get(9999), "Version 3 updated");
            
            // Verify original still intact
            assertEquals(5000, pa.get(5000), "Original value at 5000");
            assertEquals(9999, pa.get(9999), "Original value at 9999");
        }

        @Test
        @DisplayName("Chain multiple sets")
        void testChainSets() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentArray pa = new PersistentArray(arr);
            
            PersistentArray result = pa.set(0, 10).set(1, 20).set(2, 30);
            
            assertEquals(10, result.get(0), "First set");
            assertEquals(20, result.get(1), "Second set");
            assertEquals(30, result.get(2), "Third set");
            assertEquals(4, result.get(3), "Unchanged");
            
            // Original unchanged
            assertEquals(1, pa.get(0), "Original unchanged");
        }
    }

    @Nested
    @DisplayName("Persistent Stack Tests")
    class PersistentStackTests {

        @Test
        @DisplayName("New stack is empty")
        void testNewStackIsEmpty() {
            PersistentStack<Integer> stack = new PersistentStack<>();
            
            assertTrue(stack.isEmpty(), "New stack should be empty");
            assertEquals(0, stack.size(), "Size should be 0");
        }

        @Test
        @DisplayName("Push adds element, old stack unchanged")
        void testPushCreatesNewStack() {
            PersistentStack<Integer> stack1 = new PersistentStack<>();
            PersistentStack<Integer> stack2 = stack1.push(10);
            
            assertTrue(stack1.isEmpty(), "Original stack unchanged");
            assertFalse(stack2.isEmpty(), "New stack has element");
            assertEquals(10, stack2.peek(), "New stack has correct element");
            assertEquals(1, stack2.size(), "Size is 1");
        }

        @Test
        @DisplayName("Multiple pushes")
        void testMultiplePushes() {
            PersistentStack<Integer> s1 = new PersistentStack<>();
            PersistentStack<Integer> s2 = s1.push(10);
            PersistentStack<Integer> s3 = s2.push(20);
            PersistentStack<Integer> s4 = s3.push(30);
            
            assertEquals(0, s1.size(), "s1 size");
            assertEquals(1, s2.size(), "s2 size");
            assertEquals(2, s3.size(), "s3 size");
            assertEquals(3, s4.size(), "s4 size");
            
            assertEquals(30, s4.peek(), "Top of s4");
            assertEquals(20, s3.peek(), "Top of s3");
            assertEquals(10, s2.peek(), "Top of s2");
        }

        @Test
        @DisplayName("Pop removes top, old stack unchanged")
        void testPopCreatesNewStack() {
            PersistentStack<Integer> s1 = new PersistentStack<Integer>().push(10).push(20).push(30);
            PersistentStack<Integer> s2 = s1.pop();
            
            assertEquals(30, s1.peek(), "Original stack unchanged");
            assertEquals(3, s1.size(), "Original size unchanged");
            
            assertEquals(20, s2.peek(), "New stack has second element");
            assertEquals(2, s2.size(), "New stack size reduced");
        }

        @Test
        @DisplayName("Peek on empty throws exception")
        void testPeekOnEmptyThrows() {
            PersistentStack<Integer> stack = new PersistentStack<>();
            
            assertThrows(IllegalStateException.class, stack::peek, "Peek on empty should throw");
        }

        @Test
        @DisplayName("Pop on empty throws exception")
        void testPopOnEmptyThrows() {
            PersistentStack<Integer> stack = new PersistentStack<>();
            
            assertThrows(IllegalStateException.class, stack::pop, "Pop on empty should throw");
        }

        @Test
        @DisplayName("Branching stacks")
        void testBranchingStacks() {
            PersistentStack<Integer> base = new PersistentStack<Integer>().push(10).push(20);
            
            PersistentStack<Integer> branch1 = base.push(30);
            PersistentStack<Integer> branch2 = base.push(40);
            
            assertEquals(20, base.peek(), "Base unchanged");
            assertEquals(30, branch1.peek(), "Branch 1 has 30");
            assertEquals(40, branch2.peek(), "Branch 2 has 40");
            
            assertEquals(2, base.size(), "Base size");
            assertEquals(3, branch1.size(), "Branch 1 size");
            assertEquals(3, branch2.size(), "Branch 2 size");
        }

        @Test
        @DisplayName("Push and pop sequence")
        void testPushPopSequence() {
            PersistentStack<String> s1 = new PersistentStack<>();
            PersistentStack<String> s2 = s1.push("A").push("B").push("C");
            PersistentStack<String> s3 = s2.pop();
            PersistentStack<String> s4 = s3.pop();
            PersistentStack<String> s5 = s4.push("D");
            
            assertEquals("C", s2.peek(), "s2 top");
            assertEquals("B", s3.peek(), "s3 top");
            assertEquals("A", s4.peek(), "s4 top");
            assertEquals("D", s5.peek(), "s5 top");
            
            assertEquals(1, s4.size(), "s4 size");
            assertEquals(2, s5.size(), "s5 size");
        }

        @Test
        @DisplayName("Size tracking")
        void testSizeTracking() {
            PersistentStack<Integer> stack = new PersistentStack<>();
            assertEquals(0, stack.size(), "Initial size 0");
            
            stack = stack.push(1);
            assertEquals(1, stack.size(), "After 1 push");
            
            stack = stack.push(2).push(3);
            assertEquals(3, stack.size(), "After 3 pushes total");
            
            stack = stack.pop();
            assertEquals(2, stack.size(), "After 1 pop");
        }

        @Test
        @DisplayName("Generic type - String")
        void testGenericString() {
            PersistentStack<String> stack = new PersistentStack<String>()
                .push("Hello")
                .push("World");
            
            assertEquals("World", stack.peek(), "String type works");
            assertEquals(2, stack.size(), "Size correct");
        }
    }

    @Nested
    @DisplayName("Persistent DSU Tests")
    class PersistentDSUTests {

        @Test
        @DisplayName("Initial state - all separate")
        void testInitialState() {
            PersistentDSU dsu = new PersistentDSU(5);
            
            for (int i = 0; i < 5; i++) {
                for (int j = i + 1; j < 5; j++) {
                    assertFalse(dsu.connected(i, j), 
                        String.format("Initially %d and %d should not be connected", i, j));
                }
            }
        }

        @Test
        @DisplayName("Self connection")
        void testSelfConnection() {
            PersistentDSU dsu = new PersistentDSU(5);
            
            for (int i = 0; i < 5; i++) {
                assertTrue(dsu.connected(i, i), 
                    String.format("Element %d should be connected to itself", i));
            }
        }

        @Test
        @DisplayName("Union connects two elements, old DSU unchanged")
        void testUnionConnects() {
            PersistentDSU dsu1 = new PersistentDSU(5);
            PersistentDSU dsu2 = dsu1.union(0, 1);
            
            assertFalse(dsu1.connected(0, 1), "Original DSU unchanged");
            assertTrue(dsu2.connected(0, 1), "New DSU has connection");
        }

        @Test
        @DisplayName("Transitive connections")
        void testTransitiveConnections() {
            PersistentDSU dsu = new PersistentDSU(5);
            dsu = dsu.union(0, 1);
            dsu = dsu.union(1, 2);
            
            assertTrue(dsu.connected(0, 1), "0-1 connected");
            assertTrue(dsu.connected(1, 2), "1-2 connected");
            assertTrue(dsu.connected(0, 2), "0-2 connected transitively");
            assertFalse(dsu.connected(0, 3), "0-3 not connected");
        }

        @Test
        @DisplayName("Union of already connected elements")
        void testUnionAlreadyConnected() {
            PersistentDSU dsu1 = new PersistentDSU(3);
            PersistentDSU dsu2 = dsu1.union(0, 1);
            PersistentDSU dsu3 = dsu2.union(0, 1); // Union again
            
            assertTrue(dsu3.connected(0, 1), "Still connected");
            // Should return same structure logically (even if new object)
        }

        @Test
        @DisplayName("Branching DSU versions")
        void testBranchingVersions() {
            PersistentDSU dsu0 = new PersistentDSU(5);
            PersistentDSU dsu1 = dsu0.union(0, 1);
            PersistentDSU dsu2 = dsu0.union(2, 3); // Branch from dsu0, not dsu1
            
            assertTrue(dsu1.connected(0, 1), "dsu1: 0-1 connected");
            assertFalse(dsu1.connected(2, 3), "dsu1: 2-3 not connected");
            
            assertTrue(dsu2.connected(2, 3), "dsu2: 2-3 connected");
            assertFalse(dsu2.connected(0, 1), "dsu2: 0-1 not connected");
            
            assertFalse(dsu0.connected(0, 1), "dsu0: nothing connected");
            assertFalse(dsu0.connected(2, 3), "dsu0: nothing connected");
        }

        @Test
        @DisplayName("Multiple unions forming larger components")
        void testMultipleUnions() {
            PersistentDSU dsu = new PersistentDSU(6);
            
            dsu = dsu.union(0, 1);
            dsu = dsu.union(2, 3);
            dsu = dsu.union(4, 5);
            dsu = dsu.union(1, 2); // Merge first two components
            dsu = dsu.union(3, 4); // Merge all into one
            
            // All should be connected now
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    assertTrue(dsu.connected(i, j), 
                        String.format("All elements should be connected: %d, %d", i, j));
                }
            }
        }

        @Test
        @DisplayName("Find returns same root for connected elements")
        void testFindReturns() {
            PersistentDSU dsu = new PersistentDSU(5);
            dsu = dsu.union(0, 1);
            dsu = dsu.union(1, 2);
            
            int root0 = dsu.find(0);
            int root1 = dsu.find(1);
            int root2 = dsu.find(2);
            
            assertEquals(root0, root1, "0 and 1 have same root");
            assertEquals(root1, root2, "1 and 2 have same root");
            
            int root3 = dsu.find(3);
            assertNotEquals(root0, root3, "3 has different root");
        }

        @Test
        @DisplayName("Chain of unions")
        void testChainOfUnions() {
            PersistentDSU dsu = new PersistentDSU(10);
            
            for (int i = 0; i < 9; i++) {
                dsu = dsu.union(i, i + 1);
            }
            
            // All should be connected
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    assertTrue(dsu.connected(i, j), 
                        String.format("Chain connected: %d, %d", i, j));
                }
            }
        }

        @Test
        @DisplayName("Large DSU performance")
        void testLargeDSU() {
            int n = 1000;
            PersistentDSU dsu = new PersistentDSU(n);
            
            // Union in pairs
            for (int i = 0; i < n; i += 2) {
                dsu = dsu.union(i, i + 1);
            }
            
            // Check pairs are connected
            for (int i = 0; i < n; i += 2) {
                assertTrue(dsu.connected(i, i + 1), 
                    String.format("Pair %d, %d should be connected", i, i + 1));
            }
            
            // Check different pairs are not connected
            if (n > 3) {
                assertFalse(dsu.connected(0, 2), "Different pairs not connected");
            }
        }

        @Test
        @DisplayName("Persistent history preservation")
        void testPersistentHistory() {
            PersistentDSU v0 = new PersistentDSU(4);
            PersistentDSU v1 = v0.union(0, 1);
            PersistentDSU v2 = v1.union(2, 3);
            PersistentDSU v3 = v2.union(1, 2);
            
            // v0: all separate
            assertFalse(v0.connected(0, 1), "v0: 0-1 separate");
            assertFalse(v0.connected(2, 3), "v0: 2-3 separate");
            
            // v1: 0-1 connected
            assertTrue(v1.connected(0, 1), "v1: 0-1 connected");
            assertFalse(v1.connected(2, 3), "v1: 2-3 separate");
            assertFalse(v1.connected(0, 2), "v1: 0-2 separate");
            
            // v2: 0-1 connected, 2-3 connected
            assertTrue(v2.connected(0, 1), "v2: 0-1 connected");
            assertTrue(v2.connected(2, 3), "v2: 2-3 connected");
            assertFalse(v2.connected(0, 2), "v2: 0-2 separate");
            
            // v3: all connected
            assertTrue(v3.connected(0, 1), "v3: 0-1 connected");
            assertTrue(v3.connected(2, 3), "v3: 2-3 connected");
            assertTrue(v3.connected(0, 2), "v3: 0-2 connected");
            assertTrue(v3.connected(1, 3), "v3: 1-3 connected");
        }
    }

    @Nested
    @DisplayName("Integration and Edge Case Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Verify immutability - segment tree")
        void testSegmentTreeImmutability() {
            int[] arr = {1, 2, 3};
            PersistentSegmentTree pst = new PersistentSegmentTree(arr);
            
            int originalSum = pst.query(0, 0, 2);
            
            // Create many versions
            for (int i = 0; i < 100; i++) {
                pst.update(0, i % 3, i);
            }
            
            assertEquals(originalSum, pst.query(0, 0, 2), 
                "Original version should never change despite many updates");
        }

        @Test
        @DisplayName("Verify immutability - array")
        void testArrayImmutability() {
            int[] arr = {1, 2, 3, 4, 5};
            PersistentArray original = new PersistentArray(arr);
            
            PersistentArray current = original;
            for (int i = 0; i < 100; i++) {
                current = current.set(i % 5, i);
            }
            
            // Original should be unchanged
            for (int i = 0; i < 5; i++) {
                assertEquals(arr[i], original.get(i), 
                    "Original array should be unchanged");
            }
        }

        @Test
        @DisplayName("Verify immutability - stack")
        void testStackImmutability() {
            PersistentStack<Integer> original = new PersistentStack<>();
            
            PersistentStack<Integer> current = original;
            for (int i = 0; i < 100; i++) {
                current = current.push(i);
            }
            
            assertTrue(original.isEmpty(), "Original stack should remain empty");
            assertEquals(100, current.size(), "Current stack has 100 elements");
        }

        @Test
        @DisplayName("Verify immutability - DSU")
        void testDSUImmutability() {
            PersistentDSU original = new PersistentDSU(10);
            
            PersistentDSU current = original;
            for (int i = 0; i < 9; i++) {
                current = current.union(i, i + 1);
            }
            
            // Original should have no connections
            for (int i = 0; i < 9; i++) {
                assertFalse(original.connected(i, i + 1), 
                    "Original DSU should have no connections");
            }
            
            // Current should have all connections
            assertTrue(current.connected(0, 9), "Current DSU should connect all");
        }

        @Test
        @DisplayName("Memory efficiency - structural sharing")
        void testStructuralSharing() {
            // This test verifies that structural sharing works by checking
            // that independent branches can coexist
            
            int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
            PersistentArray base = new PersistentArray(arr);
            
            // Create multiple branches
            PersistentArray[] branches = new PersistentArray[10];
            for (int i = 0; i < 10; i++) {
                branches[i] = base.set(i % 8, i * 100);
            }
            
            // Verify all branches are independent
            for (int i = 0; i < 10; i++) {
                assertEquals(i * 100, branches[i].get(i % 8), 
                    "Branch " + i + " should have its own update");
            }
            
            // Verify base is unchanged
            for (int i = 0; i < 8; i++) {
                assertEquals(arr[i], base.get(i), "Base unchanged");
            }
        }
    }
}
