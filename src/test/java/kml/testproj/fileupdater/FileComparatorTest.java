package kml.testproj.fileupdater;

import junit.framework.TestCase;

import java.io.File;
import java.util.LinkedList;

public class FileComparatorTest extends TestCase{

    private static final FileComparator fileComparator = new FileComparator();
    private File verifiableDirectory, sampleDirectory;
    private LinkedList<String> verifiable, sample;

    public void setUp() throws Exception{
        String inputPath = System.getProperty("user.dir") + File.separator + "inputTest";
        String destinationPath = System.getProperty("user.dir") + File.separator + "destinationTest";

        verifiableDirectory = new File(inputPath);
        sampleDirectory = new File(destinationPath);

        PathWalker pathWalker = new PathWalker();
        LinkedList<File> verifiableFiles = pathWalker.explorePath(verifiableDirectory);
        LinkedList<File> sampleFiles = pathWalker.explorePath(sampleDirectory);
        verifiable = pathWalker.getRelativePaths(verifiableFiles, verifiableDirectory.getPath());
        sample = pathWalker.getRelativePaths(sampleFiles, sampleDirectory.getPath());
    }


    public void testFileComparator() throws Exception{

        LinkedList<String> newFiles = fileComparator.compareByName(verifiable, sample);

        assertTrue(newFiles.size()==2);

        String actual = newFiles.get(0);
        String expected = "\\pomNew.xml";
        assertEquals(expected, actual);

        actual = newFiles.get(1);
        expected = "\\newDirectory\\pom.xml";
        assertEquals(expected, actual);



        LinkedList<String> prepared = (LinkedList<String>) verifiable.clone();
        for (String nf: newFiles){
            prepared.remove(nf);
        }
        LinkedList<String> differentFiles = fileComparator.compareByHash(prepared, verifiableDirectory.getPath(),
                sampleDirectory.getPath());

        assertTrue(newFiles.size()==2);

        actual = differentFiles.get(0);
        expected = "\\pom.xml";
        assertEquals(expected, actual);

        actual = differentFiles.get(1);
        expected = "\\directory\\pom.xml";
        assertEquals(expected, actual);
    }

}
