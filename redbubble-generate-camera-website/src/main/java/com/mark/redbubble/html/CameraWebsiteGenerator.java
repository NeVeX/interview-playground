package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;

import java.util.Set;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteGenerator {

    private final Set<CameraInformation> originalCameras;

    public CameraWebsiteGenerator(Set<CameraInformation> cameras) {
        if ( cameras == null || cameras.isEmpty()) { throw new IllegalArgumentException("Provided cameras is not valid"); }
        this.originalCameras = cameras;
    }



}
