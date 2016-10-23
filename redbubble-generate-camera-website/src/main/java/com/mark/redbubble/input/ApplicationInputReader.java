package com.mark.redbubble.input;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class ApplicationInputReader {

    private static Option API_CAMERA_WORKS_LOCATION_OPTION;
    private static Option HTML_OUTPUT_DIRECTORY_OPTION;

    private static final Options ALL_INPUT_OPTIONS = new Options();

    static {
        /**
         * Define the input arguments that we expect and their properties.
         */
        API_CAMERA_WORKS_LOCATION_OPTION = Option.builder("a").hasArg().required()
                .desc("The camera works API to use for exif data").build();
        HTML_OUTPUT_DIRECTORY_OPTION = Option.builder("d").hasArg().required()
                .desc("The full directory location to output all generated HTML files").build();

        // Add all the input options to the full options
        ALL_INPUT_OPTIONS.addOption(API_CAMERA_WORKS_LOCATION_OPTION);
        ALL_INPUT_OPTIONS.addOption(HTML_OUTPUT_DIRECTORY_OPTION);
    }

    public ApplicationInputArguments processInput(String[] args) throws ApplicationInputException {
        List<String> invalidInputs = new ArrayList<>();
        CommandLine clp;
        try {
            // Use the CLI parser to help us parse the input
            clp = new DefaultParser().parse(ALL_INPUT_OPTIONS, args);
            String cameraWorksApi = null;
            String htmlOutputDirectory = null;

            if (clp.hasOption(API_CAMERA_WORKS_LOCATION_OPTION.getOpt())) {
                cameraWorksApi = clp.getOptionValue(API_CAMERA_WORKS_LOCATION_OPTION.getOpt());
            }
            if (clp.hasOption(HTML_OUTPUT_DIRECTORY_OPTION.getOpt())) {
                htmlOutputDirectory = clp.getOptionValue(HTML_OUTPUT_DIRECTORY_OPTION.getOpt());
            }

            // Let's make all input is valid
            if (StringUtils.isNotBlank(cameraWorksApi) && StringUtils.isNotBlank(htmlOutputDirectory)) {
                return new ApplicationInputArguments(cameraWorksApi, htmlOutputDirectory); // all good
            }

        } catch (MissingOptionException e) {
            throw new ApplicationInputException("Missing required arguments: "+e.getMissingOptions());
        } catch (MissingArgumentException e) {
            throw new ApplicationInputException("Missing argument: "+e.getOption().getOpt());
        } catch (ParseException pe) {
            throw new ApplicationInputException("Could not parse input arguments", pe);
        }
        throw new ApplicationInputException("No valid input arguments received");
    }

    public void printUsageInformation(OutputStream printStream) {
        HelpFormatter formatter = new HelpFormatter();
        String header = System.lineSeparator()+"See below on how to use this RedBubble Camera Website Generator";
        try (PrintWriter printWriter = new PrintWriter(printStream)) {
            formatter.printHelp(printWriter, 300, "java -jar redbubble-generate-camera-website-*.jar", header, ALL_INPUT_OPTIONS, 0, 0, System.lineSeparator(), true);
        }
    }

}

