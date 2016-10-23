package com.mark.redbubble.input;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class ApplicationInputArguments {

    private final String cameraWorksApiUrl;
    private final String htmlOutputDirectory;

    public ApplicationInputArguments(String cameraWorksApiUrl, String htmlOutputDirectory) {
        if ( StringUtils.isBlank(cameraWorksApiUrl)) { throw new IllegalArgumentException("Provided cameraWorksApiUrl is blank"); }
        if ( StringUtils.isBlank(htmlOutputDirectory)) { throw new IllegalArgumentException("Provided htmlOutputDirectory is blank"); }
        this.cameraWorksApiUrl = cameraWorksApiUrl;
        this.htmlOutputDirectory = htmlOutputDirectory;
    }

    public String getCameraWorksApiUrl() {
        return cameraWorksApiUrl;
    }

    public String getHtmlOutputDirectory() {
        return htmlOutputDirectory;
    }
}