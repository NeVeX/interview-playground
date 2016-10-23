package com.mark.redbubble.output;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

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



}
