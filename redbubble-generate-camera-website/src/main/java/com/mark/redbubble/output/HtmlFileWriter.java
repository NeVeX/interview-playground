package com.mark.redbubble.output;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.OpenOption;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class HtmlFileWriter {

    private final File outputDirectory;

    public HtmlFileWriter(File outputDirectory) {
        if (outputDirectory == null) { throw new IllegalArgumentException("Provided outputDirectory is null"); }
        if (!outputDirectory.isDirectory()) { throw new IllegalArgumentException("Provided outputDirectory is not a directory"); }
        this.outputDirectory = outputDirectory;
    }

    public void writeHtml(String inputFileName, String contents) throws FileWriterException {
        String fileName = postFixInputFileName(inputFileName);
        File newFile = new File(outputDirectory, fileName);
        if ( newFile.exists() ) {
            throw new FileWriterException("File ["+newFile.getAbsolutePath()+"] already exists - will not overwrite");
        }
        try {
            newFile.createNewFile();
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

    private String postFixInputFileName(String inputFileName) {
        if (!StringUtils.endsWith(inputFileName, ".html")) {
            return inputFileName + ".html";
        }
        return inputFileName;
    }


}
