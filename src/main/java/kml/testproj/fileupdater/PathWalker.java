package kml.testproj.fileupdater;

import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

public class PathWalker {

    /**
     * This method searches for files in the specified directory.
     * @param mainDirectory The directory where file search is starting and deepening.
     * @return A list with the found files.
     */
    protected LinkedList<File> explorePath(File mainDirectory){

        LinkedList<File> files = new LinkedList<File>();
        Stack<File> directoryStack = new Stack<File>();
        directoryStack.push((mainDirectory));

        while(!directoryStack.empty()){
            File directory = directoryStack.pop();
            File[] directoryFiles = directory.listFiles();

            if(directoryFiles != null){
                for (File f: directoryFiles){
                    if(f.isDirectory()){
                        directoryStack.push(f);
                    } else{
                        // Only the files are including in the list. Directory are not including.
                        files.add(f);
                    }
                }
            }

        }

        return files;
    }

    /**
     * This method creates a list with relative paths.
     * @param files The list of files with absolutely paths.
     * @param dependentPath The trimming part of the path.
     * @return A list with relative paths.
     */

    protected LinkedList<String> getRelativePaths(LinkedList<File> files, String dependentPath){

        ListIterator<File> listIterator = files.listIterator();
        LinkedList<String> paths = new LinkedList<String>();

        while(listIterator.hasNext()){
            paths.add(trimDependentPath(listIterator.next(), dependentPath));
        }

        return paths;
    }

    /**
     * This method trims dependent path.
     * @param file The file with absolutely path.
     * @param dependentPath The trimming part of the path.
     * @return The relative part of the absolutely path.
     */
    private String trimDependentPath(File file, String dependentPath){
        String relative = file.getPath();
        relative = relative.replace(dependentPath, "");

        return relative;
    }

}
