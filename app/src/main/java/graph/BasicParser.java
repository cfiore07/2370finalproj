package graph;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/** Basic parser for street information. Parses a text file with street
 * information; each line must contain:
 *  ORIG DEST DISTANCE
 * separated by a single space. ORIG and DEST are parsed as Strings, and
 * DISTANCE is parsed as a double. Any content after DISTANCE is ignored. */
public class BasicParser extends GraphParser {

    Scanner sc; // scanner to read the text file

    /** Open the given file and prepare to parse it. */
    @Override
    public void open(File f) throws FileNotFoundException {
        //sidewalks = new HashMap<String,Sidewalk>();
        sc = new Scanner(f);
    }

    /** Parse an opened file and return a Graph representing the data in
     * the file. Precondition: open() has been successfully called. */
    @Override
    public Graph parse() {
        Graph graph = new Graph();

        while (sc.hasNextLine()) {
            String[] data = sc.nextLine().split(" ");
            String SidewalkOrigNode = data[0];
            String SidewalkDestNode = data[1];
            double distance = Double.parseDouble(data[2]);

            Node SidewalkOrig = graph.getNode(SidewalkOrigNode);
            Node SidewalkDest = graph.getNode(SidewalkDestNode);
            graph.addEdge(SidewalkOrig, SidewalkDest, distance);
        }

        return graph;
    }
}
