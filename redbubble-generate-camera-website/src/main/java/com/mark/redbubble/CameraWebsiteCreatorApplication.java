package com.mark.redbubble;

import com.mark.redbubble.api.CameraWorksApiClient;
import com.mark.redbubble.html.CameraWebsiteGenerator;
import com.mark.redbubble.input.ApplicationInputArguments;
import com.mark.redbubble.input.ApplicationInputReader;
import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.output.FileWriter;

import java.util.Set;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteCreatorApplication {

    void run(String[] args) throws Exception {

        this.getClass().getResource("/scripts");

        // Parse the input
        ApplicationInputReader inputReader = new ApplicationInputReader();
        inputReader.printUsageInformation(System.out);
        ApplicationInputArguments inputArguments = inputReader.processInput(args);

        Set<CameraInformation> cameras = new CameraWorksApiClient(inputArguments.getCameraWorksApiUrl()).getCameras();

        FileWriter fileWriter = new FileWriter(inputArguments.getHtmlOutputDirectory());
        new CameraWebsiteGenerator(cameras, fileWriter).generatePages();
    }

    public static void main(String[] args) throws Exception {
        new CameraWebsiteCreatorApplication().run(args);
    }
}
