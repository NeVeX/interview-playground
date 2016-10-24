package com.mark.redbubble.output;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class FileWriter {

    private final File outputDirectory;

    public FileWriter(File outputDirectory) {
        if (outputDirectory == null) { throw new IllegalArgumentException("Provided outputDirectory is null"); }
        if (!outputDirectory.isDirectory()) { throw new IllegalArgumentException("Provided outputDirectory is not a directory"); }
        this.outputDirectory = outputDirectory;
    }

    public void writeContentsToFile(String inputFileName, String filePostFix, String contents) throws FileWriterException {
        String fileName = inputFileName + filePostFix;
        File newFile = new File(outputDirectory, fileName);
        if ( newFile.exists() ) {
            throw new FileWriterException("File ["+newFile.getAbsolutePath()+"] already exists - will not overwrite");
        }
        try {
            Files.createFile(newFile.toPath());
        } catch (Exception exception) {
            throw new FileWriterException("Could not create the file ["+newFile.getAbsolutePath()+"] to save the html to", exception);
        }

        if ( !newFile.exists() || !newFile.canWrite()) {
            throw new FileWriterException("Cannot access or write to the file ["+newFile.getAbsolutePath()+"]");
        }

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(newFile.toPath())) {
            bufferedWriter.write(contents);
        } catch (Exception exception) {
            throw new FileWriterException("Could not write contents to the file ["+newFile.getAbsolutePath()+"]", exception);
        }
    }

    public void createDirectory(String directoryName) throws FileWriterException {
        File newDirectory = new File(outputDirectory, directoryName);
        try {
            Files.createDirectory(newDirectory.toPath());
        } catch (Exception exception ) {
            throw new FileWriterException("Could not create directory ["+newDirectory.getAbsolutePath()+"]", exception);
        }
    }

}
