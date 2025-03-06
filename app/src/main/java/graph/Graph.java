import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/** A graph class representing a graph in adjacency-list style format. The
 * Graph stores a mapping from unique String node identifiers to respective
 * Node objects. Edges are stored in each Node's neighbors, accessible by its
 * getNeighbors field. */
public class Graph {

    // maps node ids to nodes:
    private HashMap<String,Node> IDnodes;

    /** Constructor: create an empty graph */
    public Graph() {
        IDnodes = new HashMap<String,Node>();
    }

    /** Return the node with id s. If no such node exists already, create
     * and return it. */
    public Node getNode(String s) {
        if (IDnodes.containsKey(s)) {
            return IDnodes.get(s);
        }
        Node n = new Node(s);
        IDnodes.put(s, n);
        return n;
    }

    /** Return a read-only view on the map from node ids to nodes. This is
     * read-only to avoid modification by client code that could create nodes
     * with duplicate id's. Node creation should be done exclusively with
     * getNode.  */
    public Map<String,Node> getNodes() {
        return Collections.unmodifiableMap(IDnodes);
    }

    /** Add an edge to the graph. If an edge from orig to
     * dest exists already, overwrite it. */
    public void addEdge(Node SidewalkOrig, Node SidewalkDest, double distance) {
        SidewalkOrig.addNeighbor(SidewalkDest, distance);
    }

    /** Print a report of the graph's statistics, including number of nodes,
     * number of edges, and average degree. */
    public void report() {
        int numNodes = IDnodes.size();
        int numEdges = 0;
        for (Node n : IDnodes.values()) {
            numEdges += n.getNeighbors().size();
        }

        System.out.println("Graph has: ");
        System.out.println(numNodes + " nodes.");
        System.out.println(numEdges + " edges.");
        System.out.println("Average degree " + ((double) numEdges) / ((double) numNodes));
    }

}
