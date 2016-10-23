package com.mark.redbubble.input;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class ApplicationInputArguments {

    private final String cameraWorksApiUrl;
    private final File htmlOutputDirectory;

    public ApplicationInputArguments(String cameraWorksApiUrl, File htmlOutputDirectory) {
        if ( StringUtils.isBlank(cameraWorksApiUrl)) { throw new IllegalArgumentException("Provided cameraWorksApiUrl is blank"); }
        if ( htmlOutputDirectory == null) { throw new IllegalArgumentException("Provided htmlOutputDirectory is null"); }
        this.cameraWorksApiUrl = cameraWorksApiUrl;
        this.htmlOutputDirectory = htmlOutputDirectory;
    }

    public String getCameraWorksApiUrl() {
        return cameraWorksApiUrl;
    }

    public File getHtmlOutputDirectory() {
        return htmlOutputDirectory;
    }
}