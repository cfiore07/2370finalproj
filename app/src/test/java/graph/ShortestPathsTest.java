package graph;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.FileNotFoundException;

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

    /** Dummy test case demonstrating syntax to create a graph from scratch.
     * Write your own tests below. */
    @Test
    public void test00Nothing() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        g.addEdge(a, b, 1);

        // sample assertion statements:
        assertTrue(true);
        assertEquals(2+2, 4);
    }

    /** Minimal test case to check the path from A to B in Simple0.txt */
    @Test
    public void test01Simple0() {
        Graph g = loadBasicGraph("Simple0.txt");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        Node b = g.getNode("B");
        LinkedList<Node> abPath = sp.shortestPath(b);
        assertEquals(abPath.size(), 2);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(),  b);
        assertEquals(sp.shortestPathLength(b), 1.0, 1e-6);
    }

    @Test
    public void testRootDistanceNoOtherNodes() {
        // Test for correct distance on root node without any other nodes
        // present
        Graph g = new Graph();
        Node a = g.getNode("A");
        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);

        LinkedList<Node> path = sp.shortestPath(a);

        // Path for first node should be 0 always
        assertEquals("Shortest path to itself should be 1 node long", 0, path.size());
        assertEquals("First (and only) node in path should be A", a, path.getFirst());

    }

    @Test
    public void testRootDistanceNodesPointingAway(){
        // Test for correct distance on root node with other nodes present but none returning to a
        Graph g = loadBasicGraph("Simple01.txt");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);


        LinkedList<Node> path = sp.shortestPath(a);

        // Path for first node should be 0 always
        assertEquals("Shortest path to itself should be 1 node long", 0, path.size());
        assertEquals("First (and only) node in path should be A", a, path.getFirst());
    }

    @Test
    public void testRootDistanceNodesCircleBack(){
        // Test for correct distance on root node with other nodes present but none returning to a
        Graph g = loadBasicGraph("Simple01.txt");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);


        LinkedList<Node> path = sp.shortestPath(a);

        // Path for first node should be 0 always
        assertEquals("Shortest path to itself should be 1 node long", 0, path.size());
        assertEquals("First (and only) node in path should be A", a, path.getFirst());
    }
    /* Pro tip: unless you include @Test on the line above your method header,
     * gradle test will not run it! This gets me every time. */
}
