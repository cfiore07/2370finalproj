package graph;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShortestPathsTest {


    /* Returns the Graph loaded from the file with filename fn. */
    private Graph loadBasicGraph(String fn) {
        Graph result = null;
        try {
            result = ShortestPaths.parseGraph("basic", fn);
        } catch (FileNotFoundException e) {
            fail("Could not find graph " + fn);
        }
        return result;
    }

    /**
     * Dummy test case demonstrating syntax to create a graph from scratch.
     * Write your own tests below.
     */
    @Test
    public void test00Nothing() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        g.addEdge(a, b, 1);

        // sample assertion statements:
        assertTrue(true);
        assertEquals(2 + 2, 4);
    }

    /**
     * Minimal test case to check the path from A to B in Simple0.txt.
     * Until shortestPath implementation this test will fail.
     * This is being kept for future test implementation as a reference.
     * Comment out until needed.
     * */

//    @Test
//    public void test01Simple0() {
//        Graph g = loadBasicGraph("Simple0.txt");
//        g.report();
//        ShortestPaths sp = new ShortestPaths();
//        Node a = g.getNode("A");
//        sp.compute(a);
//        Node b = g.getNode("B");
//        LinkedList<Node> abPath = sp.shortestPath(b);
//        assertEquals(abPath.size(), 2);
//        assertEquals(abPath.getFirst(), a);
//        assertEquals(abPath.getLast(),  b);
//        assertEquals(sp.shortestPathLength(b), 1.0, 1e-6);
//    }

    /**
     * Helper method to access the private "paths" field from a ShortestPaths object.
     */
    private HashMap<Node, ShortestPaths.PathData> getPaths(ShortestPaths sp) throws Exception {
        Field field = ShortestPaths.class.getDeclaredField("paths");
        field.setAccessible(true);
        return (HashMap<Node, ShortestPaths.PathData>) field.get(sp);
    }

    /**
     * Test compute() on a graph with a single node.
     * Expect that the origin is in the paths map with a distance of 0 and no predecessor.
     */
    @Test
    public void testComputeSingleNode() throws Exception {
        Graph g = new Graph();
        Node a = g.getNode("A");
        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);

        HashMap<Node, ShortestPaths.PathData> paths = getPaths(sp);
        assertTrue("Paths should contain the origin", paths.containsKey(a));
        assertEquals("Distance to the origin should be 0", 0.0, paths.get(a).distance, 1e-6);
        assertNull("Origin's predecessor should be null", paths.get(a).previous);
    }

    /**
     * Test compute() on a simple two-node graph.
     * Graph: A -> B (weight 5)
     * Expect that B is reachable with a distance of 5 and A as its predecessor.
     */
    @Test
    public void testComputeSimpleEdge() throws Exception {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        g.addEdge(a, b, 5.0);

        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);

        HashMap<Node, ShortestPaths.PathData> paths = getPaths(sp);
        assertTrue("Paths should contain node B", paths.containsKey(b));

        // Floating point errors may result in miniscule differences in result,
        // Delta value is some infinitesimal number to account for this.
        // In our cases here this should never be a problem, but for the large datasets
        // it could present an issue.
        assertEquals("Distance to B should be 5.0", 5.0, paths.get(b).distance, 1e-6);
        assertEquals("B's predecessor should be A", a, paths.get(b).previous);
    }

    /**
     * Test compute() on a graph with several nodes and edges.
     * Graph structure:
     * A -> B (1)
     * A -> C (4)
     * B -> C (2)
     * B -> D (5)
     * C -> D (1)
     * <p>
     * Expected shortest distances:
     * A: 0
     * B: 1
     * C: 3   (via A->B->C, not 4)
     * D: 4   (via A->B->C->D)
     */
    @Test
    public void testComputeMultipleEdges() throws Exception {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        Node d = g.getNode("D");

        g.addEdge(a, b, 1.0);
        g.addEdge(a, c, 4.0);
        g.addEdge(b, c, 2.0);
        g.addEdge(b, d, 5.0);
        g.addEdge(c, d, 1.0);

        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);

        HashMap<Node, ShortestPaths.PathData> paths = getPaths(sp);
        assertEquals("Distance to A should be 0", 0.0, paths.get(a).distance, 1e-6);
        assertEquals("Distance to B should be 1", 1.0, paths.get(b).distance, 1e-6);
        assertEquals("Distance to C should be 3", 3.0, paths.get(c).distance, 1e-6);
        assertEquals("Distance to D should be 4", 4.0, paths.get(d).distance, 1e-6);

        // Check predecessor chain:
        assertNull("A's predecessor should be null", paths.get(a).previous);
        assertEquals("B's predecessor should be A", a, paths.get(b).previous);
        assertEquals("C's predecessor should be B", b, paths.get(c).previous);
        assertEquals("D's predecessor should be C", c, paths.get(d).previous);
    }

    /**
     * Test compute() on a graph that contains a cycle.
     * Graph structure:
     * A -> B (2)
     * B -> C (3)
     * C -> A (1)
     * B -> D (4)
     * <p>
     * Expected shortest distances:
     * A: 0
     * B: 2
     * C: 5  (A->B->C)
     * D: 6  (A->B->D)
     */
    @Test
    public void testComputeCycle() throws Exception {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        Node d = g.getNode("D");

        g.addEdge(a, b, 2.0);
        g.addEdge(b, c, 3.0);
        g.addEdge(c, a, 1.0);
        g.addEdge(b, d, 4.0);

        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);

        HashMap<Node, ShortestPaths.PathData> paths = getPaths(sp);
        assertEquals("Distance to A should be 0", 0.0, paths.get(a).distance, 1e-6);
        assertEquals("Distance to B should be 2", 2.0, paths.get(b).distance, 1e-6);
        assertEquals("Distance to C should be 5", 5.0, paths.get(c).distance, 1e-6);
        assertEquals("Distance to D should be 6", 6.0, paths.get(d).distance, 1e-6);
    }

    /**
     * Test compute() on a graph where one node is disconnected.
     * Graph: A -> B (3) and a separate node C.
     * Expect that the paths map contains nodes reachable from A (A and B)
     * but not the unreachable node C.
     */
    @Test
    public void testComputeDisconnectedGraph() throws Exception {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");  // C is not connected to A

        g.addEdge(a, b, 3.0);

        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);

        HashMap<Node, ShortestPaths.PathData> paths = getPaths(sp);
        assertTrue("Paths should contain A", paths.containsKey(a));
        assertTrue("Paths should contain B", paths.containsKey(b));
        assertFalse("Paths should not contain disconnected node C", paths.containsKey(c));
    }

    /**
     * Test compute() on a graph with multiple routes to the same node.
     * Graph:
     * A -> B (10)
     * A -> C (1)
     * C -> B (2)
     * A -> D (5)
     * D -> B (1)
     * <p>
     * There are two routes from A to B:
     * 1. A -> C -> B, with distance 1 + 2 = 3
     * 2. A -> D -> B, with distance 5 + 1 = 6
     * The algorithm should choose the shorter route.
     */
    @Test
    public void testComputeMultiplePaths() throws Exception {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        Node d = g.getNode("D");

        g.addEdge(a, b, 10.0);
        g.addEdge(a, c, 1.0);
        g.addEdge(c, b, 2.0);
        g.addEdge(a, d, 5.0);
        g.addEdge(d, b, 1.0);

        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);

        HashMap<Node, ShortestPaths.PathData> paths = getPaths(sp);
        assertEquals("Distance to B should be 3 (via A->C->B)", 3.0, paths.get(b).distance, 1e-6);
        assertEquals("B's predecessor should be C", c, paths.get(b).previous);
    }

    /**
     * Test compute() on a graph input through the GraphParser class.
     * Graph:
     * A -> B (2)
     * A -> C (6)
     * B -> C (3)
     * <p>
     * Expect to see correctly weighted distances and predecessors similar
     * to testComputeMultipleEdges.
     * <p>
     * We also check for correct intake of data with GraphParser, however this is not entirely
     * necessary as the GraphParser class was supplied by the professor.
     */
    @Test
    public void testParseGraphFromFile() throws Exception, IOException, FileNotFoundException {
        /**
         * For future mutability and to avoid adding a million text files,
         * we will use temporary files for testing purposes.
         * This will be the general format for this kind of test going forward.
         */
        File tempFile = null;
        try {
            // Create a temporary file
            tempFile = File.createTempFile("tempGraph", ".txt");
            PrintWriter writer = new PrintWriter(tempFile);

            // Write graph data to the file:
            // Each line: ORIG DEST DISTANCE
            writer.println("A B 2.0");
            writer.println("B C 3.0");
            writer.println("A C 6.0");
            writer.close();

            // Use the provided parseGraph method with "basic" type
            Graph g = ShortestPaths.parseGraph("basic", tempFile.getAbsolutePath());

            Node a = g.getNode("A");
            Node b = g.getNode("B");
            Node c = g.getNode("C");

            // Check that the nodes have been added and edges are correct.
            // Node A should have neighbors B and C.
            assertTrue("Node A should have B as a neighbor", a.getNeighbors().containsKey(b));
            assertTrue("Node A should have C as a neighbor", a.getNeighbors().containsKey(c));
            assertEquals("Edge A->B should have weight 2.0", 2.0, a.getNeighbors().get(b), 1e-6);
            assertEquals("Edge A->C should have weight 4.0", 6.0, a.getNeighbors().get(c), 1e-6);

            // Node B should have neighbor C.
            assertTrue("Node B should have C as a neighbor", b.getNeighbors().containsKey(c));
            assertEquals("Edge B->C should have weight 3.0", 3.0, b.getNeighbors().get(c), 1e-6);

            // Node C should have no outgoing edges.
            assertTrue("Node C should have no neighbors", c.getNeighbors().isEmpty());

            ShortestPaths sp = new ShortestPaths();
            sp.compute(a);

            HashMap<Node, ShortestPaths.PathData> paths = getPaths(sp);
            assertEquals("Distance to A should be 0", 0.0, paths.get(a).distance, 1e-6);
            assertEquals("Distance to B should be 2", 2.0, paths.get(b).distance, 1e-6);
            assertEquals("Distance to C should be 5", 5.0, paths.get(c).distance, 1e-6);

            assertEquals("A's predecessor should be null", null, paths.get(a).previous);
            assertEquals("B's predecessor should be A", a, paths.get(b).previous);
            assertEquals("C's predecessor should be B", b, paths.get(c).previous);


        } finally {
            // Clean up temporary file if it was created
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}

    /* Pro tip: unless you include @Test on the line above your method header,
     * gradle test will not run it! This gets me every time. */

