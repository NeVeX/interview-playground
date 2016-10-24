package com.mark.redbubble.input;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Mark Cunningham on 10/22/2016.
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

    public String getCameraWorksApiUrl() {
        return cameraWorksApiUrl;
    }

    public Path getHtmlOutputDirectory() {
        return htmlOutputDirectory;
    }
}