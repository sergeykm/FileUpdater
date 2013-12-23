package kml.testproj.fileupdater;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class FileComparator {

    /**
     * This method compares lists of files by name.
     * @param verifiable The verifiable list.
     * @param sample The list serving as an example.
     * @return A list of files that are not included in the sample.
     */
    protected LinkedList<String> compareByName(LinkedList<String> verifiable, LinkedList<String> sample){

        LinkedList<String> result = new LinkedList<String>();

        for (String v : verifiable) {
            if (!sample.contains(v)) {
                result.add(v);
            }
        }

        return result;
    }

    /**
     * This method compares lists of files by hash.
     * @param prepared The list of files which have identical names in the verifiable files list and in the
     *                 sample files list.
     * @param verifiableTrimmedPath The differ part of the verifiable files path.
     * @param sampleTrimmedPath The differ part of the sample files path.
     * @return A list of files that are different from the sample files by hash.
     */
    protected LinkedList<String> compareByHash(LinkedList<String> prepared,
                                               String verifiableTrimmedPath, String sampleTrimmedPath)
            throws InterruptedException, NoSuchAlgorithmException, IOException{

        LinkedList<String> result = new LinkedList<String>();
        String preparedFileChecksum, sampleFileChecksum;

        for(int i = 0; i< prepared.size(); i++){
            String element = prepared.get(i);
            preparedFileChecksum = getHashValue(new File(verifiableTrimmedPath + element));
            sampleFileChecksum = getHashValue(new File(sampleTrimmedPath + element));

            if(!preparedFileChecksum.equals(sampleFileChecksum)){
                result.add(element);
            }

            if(i%100 == 0){
                canceller();
            }

        }

        return result;
    }

    /**
     * @param f The file for hashing.
     * @return A hash value.
     */
    private String getHashValue(File f) throws NoSuchAlgorithmException, IOException {
        return hashGenerator.getChecksum(f);
    }

    private void canceller() throws InterruptedException{
        Thread.sleep(1);
    }

    private static final HashGenerator hashGenerator = new HashGenerator();
}
