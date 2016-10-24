package com.mark.redbubble.output;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class FileWriter {

    private final Path outputParentDirectory;

    public FileWriter(Path outputParentDirectory) {
        if (outputParentDirectory == null) { throw new IllegalArgumentException("Provided outputParentDirectory is null"); }
        if (!Files.isDirectory(outputParentDirectory)) { throw new IllegalArgumentException("Provided outputParentDirectory is not a directory"); }
        this.outputParentDirectory = outputParentDirectory;
    }

    public void writeContentsToFile(String childDirectory, String inputFileName, String contents) throws FileWriterException {
        Path childDirPath = Paths.get(outputParentDirectory.toString(), childDirectory);
        if ( !Files.exists(childDirPath)) {
            childDirPath = createDirectory(childDirPath);
        }
        writeContentsToFile(childDirPath, inputFileName, contents);
    }

    public void writeContentsToFile(String inputFileName, String contents) throws FileWriterException {
        writeContentsToFile(outputParentDirectory, inputFileName, contents);
    }

    private void writeContentsToFile(Path parentPath, String fullFileName, String contents) throws FileWriterException {

        Path newFile = Paths.get(parentPath.toString(), fullFileName);
        if ( Files.exists(newFile) ) {
            throw new FileWriterException("File ["+newFile.toAbsolutePath()+"] already exists - will not overwrite");
        }
        try {
            Files.createFile(newFile);
        } catch (Exception exception) {
            throw new FileWriterException("Could not create the file ["+newFile.toAbsolutePath()+"] to save the html to", exception);
        }

        if ( !Files.exists(newFile) || !Files.isWritable(newFile)) {
            throw new FileWriterException("Cannot access or write to the file ["+newFile.toAbsolutePath()+"]");
        }

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(newFile)) {
            bufferedWriter.write(contents);
        } catch (Exception exception) {
            throw new FileWriterException("Could not write contents to the file ["+newFile.toAbsolutePath()+"]", exception);
        }
    }

    private Path createDirectory(Path directoryToCreate) throws FileWriterException {
        try {
            return Files.createDirectory(directoryToCreate);
        } catch (Exception exception ) {
            throw new FileWriterException("Could not create directory ["+directoryToCreate.toAbsolutePath()+"]", exception);
        }
    }


}
