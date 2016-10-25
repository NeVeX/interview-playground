package com.mark.redbubble.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Mark Cunningham on 10/22/2016.
 * <br>This class represents an abstraction to write contents to a certain output location
 */
public class OutputWriter {

    private final Path outputParentDirectory; // the base location

    /**
     * @param outputParentDirectory - the base location that all sub directories/files will be saved to
     */
    public OutputWriter(Path outputParentDirectory) {
        if (outputParentDirectory == null) { throw new IllegalArgumentException("Provided outputParentDirectory is null"); }
        if ( !Files.isDirectory(outputParentDirectory)) {
            try {
                createDirectory(outputParentDirectory);
            } catch (OutputWriterException exception) {
                throw new IllegalStateException(exception); // wrap the checked exception in a runtime during construction
            }
        }
        this.outputParentDirectory = outputParentDirectory;
    }

    /**
     * Given the directory (that must exist), the valid file name and the contents to place in the file; this
     * will write all the data into the file, in the child directory
     * @param childDirectory - the child directory of this parent to write (if not exists, it will be created)
     * @param inputFileName - the name of the file to create
     * @param contents - the contents of the file
     * @throws OutputWriterException - if something went wrong with the writing
     */
    public void writeContentsToFile(String childDirectory, String inputFileName, String contents) throws OutputWriterException {
        Path childDirPath = Paths.get(outputParentDirectory.toString(), childDirectory);
        if ( !Files.exists(childDirPath)) {
            childDirPath = createDirectory(childDirPath);
        }
        writeContentsToFile(childDirPath, inputFileName, contents);
    }

    /**
     * Given a valid file name and it's contents, the contents will be written into the file, under this parent directory
     * @param inputFileName - the valid name of the file
     * @param contents - the contents
     * @throws OutputWriterException - if something went wrong with the writing
     */
    public void writeContentsToFile(String inputFileName, String contents) throws OutputWriterException {
        writeContentsToFile(outputParentDirectory, inputFileName, contents);
    }

    /**
     * Helper method to write the contents to the new file, under the directory given
     * @param parentPath - the parent path to create the file under
     * @param fullFileName - the valid file name to create
     * @param contents - the contents of this new file
     * @throws OutputWriterException - if something went wrong with the writing
     */
    private void writeContentsToFile(Path parentPath, String fullFileName, String contents) throws OutputWriterException {

        Path newFile = Paths.get(parentPath.toString(), fullFileName);
        if ( Files.exists(newFile) ) {
            throw new OutputWriterException("File ["+newFile.toAbsolutePath()+"] already exists - will not overwrite");
        }
        try {
            Files.createFile(newFile); // let's create the file that we'll write to
        } catch (Exception exception) {
            throw new OutputWriterException("Could not create the file ["+newFile.toAbsolutePath()+"] to save the html to", exception);
        }

        if ( !Files.exists(newFile) || !Files.isWritable(newFile)) {
            throw new OutputWriterException("Cannot access or write to the file ["+newFile.toAbsolutePath()+"]");
        }

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(newFile)) {
            bufferedWriter.write(contents);
        } catch (Exception exception) {
            throw new OutputWriterException("Could not write contents to the file ["+newFile.toAbsolutePath()+"]", exception);
        }
    }

    /**
     * Helper method to create the given directory
     * @param directoryToCreate - the directory to create
     * @return - the path of the directory created
     * @throws OutputWriterException - if the directory could not be created
     */
    private Path createDirectory(Path directoryToCreate) throws OutputWriterException {
        try {
            return Files.createDirectory(directoryToCreate);
        } catch (Exception exception ) {
            throw new OutputWriterException("Could not create directory ["+directoryToCreate.toAbsolutePath()+"]", exception);
        }
    }


}
