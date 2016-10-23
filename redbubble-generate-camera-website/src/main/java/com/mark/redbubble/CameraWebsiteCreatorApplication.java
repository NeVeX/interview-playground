package com.mark.redbubble;

import com.mark.redbubble.api.CameraWorksApiClient;
import com.mark.redbubble.input.ApplicationInputArguments;
import com.mark.redbubble.input.ApplicationInputReader;
import com.mark.redbubble.model.CameraInformation;

import java.util.Set;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteCreatorApplication {

    void run(String[] args) throws Exception {
        // Parse the input
        ApplicationInputArguments inputArguments = new ApplicationInputReader().processInput(args);

        Set<CameraInformation> cameras = new CameraWorksApiClient(inputArguments.getCameraWorksApiUrl()).getCameras();


    }

    public static void main(String[] args) throws Exception {
        new CameraWebsiteCreatorApplication().run(args);
    }
}
