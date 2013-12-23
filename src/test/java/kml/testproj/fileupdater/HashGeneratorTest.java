package kml.testproj.fileupdater;

import junit.framework.TestCase;

import java.io.File;

public class HashGeneratorTest extends TestCase{

    private static final HashGenerator hashGenerator = new HashGenerator();
    private File testExistingFile, testNoExistingFile;

    public void setUp() throws Exception{
        String existingFileName = "pom.xml";
        String noExistingFile = "NoExistingFile";
        testExistingFile = new File(System.getProperty("user.dir") + File.separator + existingFileName);
        testNoExistingFile = new File(System.getProperty("user.dir") + File.separator + noExistingFile);
    }

    public void testGetChecksum() throws Exception{

        String checksum = hashGenerator.getChecksum(testExistingFile);
        boolean actual = checksum.contains("OK");

        assertTrue(actual);

        checksum = hashGenerator.getChecksum(testNoExistingFile);
        actual = checksum.contains("ERROR");

        assertTrue(actual);
    }

}
