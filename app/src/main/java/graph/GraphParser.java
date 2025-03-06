import java.io.File;
import java.io.FileNotFoundException;

/** Abstract class for parsing graph information.
 * Example usage (for BasicParser, which extends this class):
 *     BasicParser p = new BasicParser();
 *     p.open(filename);
 *     Graph g = p.parse() */
public abstract class GraphParser {

    /** Open the given file and prepare to parse it. */
    public abstract void open(File f) throws FileNotFoundException;

    /** Parse an opened file and return a Graph representing the data in
     * the file. Precondition: open() has been successfully called. */
    public abstract Graph parse();

}
