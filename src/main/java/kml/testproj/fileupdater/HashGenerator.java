package kml.testproj.fileupdater;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    /**
     * This method calculates checksum by using SHA-256 algorithm.
     * @param file The file for computing checksum.
     * @return A string with result where symbol "|" is used as a separator.
     */
    protected String getChecksum(File file) throws NoSuchAlgorithmException, IOException{

        MessageDigest messageDigest;

        byte[] buffer = new byte[1024];
        int offset = 0;
        int bufferLength;

            messageDigest = MessageDigest.getInstance("SHA-256");
            FileInputStream fileInputStream = new FileInputStream(file);

            while ((bufferLength = fileInputStream.read(buffer)) != -1){
                messageDigest.update(buffer, offset, bufferLength);
            }
            fileInputStream.close();

        byte[] hashBytes = messageDigest.digest();

        return getHexadecimal(hashBytes);
    }

    /**
     * This method converts hash in a hexadecimal format.
     * @param hash The bytes of hash for convert.
     * @return A formatted string.
     */
    private String getHexadecimal(byte[] hash){
        StringBuilder hexadecimal = new StringBuilder();
        for (byte b: hash){
            hexadecimal.append(Integer.toHexString(0xFF & b));
        }

        return hexadecimal.toString();
    }

}
