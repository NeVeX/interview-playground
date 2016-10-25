package com.mark.redbubble;

import com.mark.redbubble.api.ApiException;
import com.mark.redbubble.api.CameraWorksApiClient;
import com.mark.redbubble.input.ApplicationInputArguments;
import com.mark.redbubble.input.ApplicationInputException;
import com.mark.redbubble.input.ApplicationInputReader;
import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.output.OutputWriter;
import com.mark.redbubble.output.OutputWriterException;
import com.mark.redbubble.output.html.CameraWebsiteGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Set;

/**
 * Created by Mark Cunningham on 10/22/2016.
 * <br>This class is the entry into the application (it contains the required {@link #main(String[])} method to start
 * java applications.
 */
public class CameraWebsiteCreatorApplication {

    /**
     * Defining the JVM codes to use for various exit statues (ok status vs error status)
     */
    private final static int OK_EXIT_CODE = 0;
    private final static int ERROR_EXIT_CODE = 1;

    private final static Logger LOGGER = LoggerFactory.getLogger(CameraWebsiteCreatorApplication.class);

    /**
     * Give the input arguments, this will run through all the various parts of the application to generate a webiste
     * @param args - the input arguments
     * @throws Exception - the wrapper exception that will be thrown if any stage of execution fails
     */
    void run(String[] args) throws Exception {
        // Get the valid arguments from the input
        ApplicationInputArguments inputArguments = getInputArguments(args);
        writeValidArgumentsToScreen(inputArguments);
        writeInfoMessageToScreen("Attempting to get all Cameras from the API...");
        // Get the input API url to get all the cameras at that endpoint
        Set<CameraInformation> cameras = getAllCameras(inputArguments.getCameraWorksApiUrl());
        writeInfoMessageToScreen("Received ["+cameras.size()+"] cameras from the API");
        // With all the cameras - use the generator to create the website
        generateWebsiteForCameras(cameras, inputArguments.getHtmlOutputDirectory());
        writeInfoMessageToScreen("");
        writeInfoMessageToScreen("Success! Generated all html web pages to: "+inputArguments.getHtmlOutputDirectory());
        writeInfoMessageToScreen("");
    }

    private void writeValidArgumentsToScreen(ApplicationInputArguments inputArguments) {
        writeInfoMessageToScreen("");
        writeInfoMessageToScreen("Valid Arguments received from input:");
        writeInfoMessageToScreen(" -- Api Camera Url:   "+inputArguments.getCameraWorksApiUrl());
        writeInfoMessageToScreen(" -- Output Directory: "+inputArguments.getHtmlOutputDirectory());
        writeInfoMessageToScreen("");
    }

    /**
     * For the given raw array of inputs, this method will use the {@link ApplicationInputReader} to extract
     * the valid options for this application
     * @param args - the raw arguments from the jvm
     * @return - the valid set of options for this application
     * @throws ApplicationInputException - if the arguments cannot be determined
     */
    private ApplicationInputArguments getInputArguments(String[] args) throws ApplicationInputException {
        ApplicationInputReader inputReader = new ApplicationInputReader();
        inputReader.printUsageInformation(System.out);
        // Parse the input and return the extracted options (or throw the exception if things are bad)
        return inputReader.processInput(args);
    }

    /**
     * For the given API url, this method will use the various clients to get the camera data
     * @param cameraWorksApiUrl - the valid url to invoke for camera information
     * @return - the set of found cameras
     * @throws ApiException - if something went wrong talking to the API
     */
    private Set<CameraInformation> getAllCameras(String cameraWorksApiUrl) throws ApiException {
        CameraWorksApiClient cameraWorksApiClient = new CameraWorksApiClient(cameraWorksApiUrl);
        return cameraWorksApiClient.getCameras();
    }

    /**
     * For the given set of cameras and the output directory, this method will start the process for generating
     * all the web pages for each camera
     * @param cameras - the set of cameras to use for website generation
     * @param outputDirectory - the directory to save all output to
     * @throws OutputWriterException - if files cannot be written
     */
    private void generateWebsiteForCameras(Set<CameraInformation> cameras, Path outputDirectory) throws OutputWriterException {
        // Create the File writer that is set to the output directory given
        OutputWriter outputWriter = new OutputWriter(outputDirectory);
        // Create the website generator to use - give it the cameras we'll work on
        CameraWebsiteGenerator cameraWebsiteGenerator = new CameraWebsiteGenerator(cameras);
        // Now for the magic, generate all the web pages - using the given file writer to output
        cameraWebsiteGenerator.generate(outputWriter);
    }

    private void writeInfoMessageToScreen(String message) {
        System.out.println(message);
    }

    /**
     * The main entry into the application for the JVM. This method will simply invoke the {@link #run(String[])} to
     * kick things off, but if there are any exceptions, they are caught here, instead error codes are returned
     * to the jvm
     * @param args - the arguments from the JVM
     */
    public static void main(String[] args) {
        try {
            new CameraWebsiteCreatorApplication().run(args);
            System.exit(OK_EXIT_CODE);
        } catch (Exception exception) {
            // Log the full exception and return a bad status to the JVM
            LOGGER.error("An error occurred during application execution", exception);
            System.err.println("\n****ERROR: A problem occurred during execution - "+exception.getMessage()
                    +"\nFor more information, look in the log file within the /logs directory.\n\n");
            System.exit(ERROR_EXIT_CODE);
        }
    }
}
