import java.util.Scanner;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;

public class DBParser extends GraphParser {

    private Scanner sc; // a scanner reading the CSV file

    // map csv headers to column indices
    private HashMap<String,Integer> fieldKey;

    private Graph graph;

    /** Constructor: create parser. */
    public DBParser() {
        graph = new Graph();
    }

    /** Open the given file and prepare to parse it. */
    @Override
    public void open(File f) throws FileNotFoundException {
        sc = new Scanner(f);
        parseHeaders(sc.nextLine());
    }

    /** Parse an opened file and return a Graph representing the data in the
     * file. Precondition: open() has been successfully called. */
    @Override
    public Graph parse() {
        graph = new Graph();
        while (sc.hasNextLine()) {
            parseCSVData(sc.nextLine().split(","));
        }
        return graph;
    }

    /* Parse a line of the CSV file and construct a Sidewalk object (edge)
     * representing the data given in that line.  If the origin or destination
     * is not a node in the graph, add it to the graph. Add this sidewalk
     * to the list of destinations from the origin. */
    private void parseCSVData(String[] fields) {
        String SidewalkOrigCode = fields[fieldKey.get("origin")].replace("\"","");;
        String SidewalkDestCode = fields[fieldKey.get("dest")].replace("\"","");;

        // if origin is not in graph, parse and create the new sidewalk

        Node SidewalkOrig = graph.getNode(SidewalkOrigCode);
        Node SidewalkDest = graph.getNode(SidewalkDestCode);

        double AccessibilityScore = Double.parseDouble(fields[fieldKey.get("distance")]);
        graph.addEdge(SidewalkOrig, SidewalkDest, AccessibilityScore);
    }

    /* create fieldKey from the header line of the csv file.
     * fieldKey maps a field's name to its column index in the csv file. */
    private void parseHeaders(String headerLine) {
        fieldKey = new HashMap<String,Integer>();
        String[] headers = headerLine.split(",");
        for (int i = 0; i < headers.length; i++) {
            fieldKey.put(headers[i].replace("\"","").toLowerCase(), i);
            //System.out.println(headers[i] + ": " + i);
        }
    }

    /** Main method: open a csv file and parse a graph.
     *  If no command line arguments are given, parse the entire file.
     *  If one command line is given, it specifies the maximum number of lines
     *  to parse before ignoring the rest of the file. */
    public static void main(String[] args) {
        DBParser parser = new DBParser();
        try {
            parser.open(new File(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file " + args[0]);
            return;
        }
        int maxLines = -1;
        if (args.length > 1) {
            maxLines = Integer.parseInt(args[1]);
        }

        Graph g = parser.parse();
        g.report();
    }


}
