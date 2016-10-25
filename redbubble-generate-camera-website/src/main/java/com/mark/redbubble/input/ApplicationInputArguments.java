package com.mark.redbubble.input;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Mark Cunningham on 10/22/2016.
 * <br>Class that represents valid arguments received from input (users)
 */
public class ApplicationInputArguments {

    private final String cameraWorksApiUrl;
    private final Path htmlOutputDirectory;

    ApplicationInputArguments(String cameraWorksApiUrl, Path htmlOutputDirectory) {
        if ( StringUtils.isBlank(cameraWorksApiUrl)) { throw new IllegalArgumentException("Provided cameraWorksApiUrl is blank"); }
        if ( htmlOutputDirectory == null) { throw new IllegalArgumentException("Provided htmlOutputDirectory is null"); }
        this.cameraWorksApiUrl = cameraWorksApiUrl;
        this.htmlOutputDirectory = htmlOutputDirectory;
    }

    /**
     * The given location of the API to use
     */
    public String getCameraWorksApiUrl() {
        return cameraWorksApiUrl;
    }

    /**
     * The location to output all created content
     */
    public Path getHtmlOutputDirectory() {
        return htmlOutputDirectory;
    }
}