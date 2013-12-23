package kml.testproj.fileupdater;

import junit.framework.TestCase;
import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;

public class PathWalkerTest extends TestCase{

    private static final PathWalker pathWalker = new PathWalker();
    private File workingDirectory, notExistDirectory;

    public void setUp() throws Exception{
        String workingPath = System.getProperty("user.dir");
        String notExistPath = workingPath + File.separator + "NotExistingFolder";
        workingDirectory = new File(workingPath);
        notExistDirectory = new File(notExistPath);
    }

    public void testExplorePath() throws Exception{

        LinkedList<File> filesList = pathWalker.explorePath(workingDirectory);
        int  actual = filesList.size();

        assertTrue(actual > 0);

        filesList = pathWalker.explorePath(notExistDirectory);
        actual = filesList.size();

        assertEquals(actual, 0);
    }

    public void testGetRelativePaths() throws Exception{

        String expected;
        String actual;
        LinkedList<File> files = pathWalker.explorePath(workingDirectory);
        LinkedList<String> relativePaths = pathWalker.getRelativePaths(files, workingDirectory.getPath());
        ListIterator<File> fileIterator = files.listIterator();
        ListIterator<String> relativePathsIterator = relativePaths.listIterator();

        while (fileIterator.hasNext()){
            expected = fileIterator.next().getPath();
            expected = expected.replace(workingDirectory.getPath(), "");
            actual = relativePathsIterator.next();

            assertEquals(expected, actual);
        }
    }

}
