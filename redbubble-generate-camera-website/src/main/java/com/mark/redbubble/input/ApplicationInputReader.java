package com.mark.redbubble.input;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class ApplicationInputReader {

    private static Option API_CAMERA_WORKS_LOCATION_OPTION;
    private static Option HTML_OUTPUT_DIRECTORY_OPTION;

    private static final Options ALL_INPUT_OPTIONS = new Options();

    static {
        /*
         * Define the input arguments that we expect and their properties.
         */
        API_CAMERA_WORKS_LOCATION_OPTION = Option.builder("a").hasArg().required()
                .desc("The camera works API to use for exif data").build();
        HTML_OUTPUT_DIRECTORY_OPTION = Option.builder("o").hasArg().required()
                .desc("The full directory location to output all generated HTML files").build();

        // Add all the input options to the full options
        ALL_INPUT_OPTIONS.addOption(API_CAMERA_WORKS_LOCATION_OPTION);
        ALL_INPUT_OPTIONS.addOption(HTML_OUTPUT_DIRECTORY_OPTION);
    }

    public ApplicationInputArguments processInput(String[] args) throws ApplicationInputException {
        try {
            // Use the CLI parser to help us parse the input
            CommandLine commandLine = new DefaultParser().parse(ALL_INPUT_OPTIONS, args);
            String cameraWorksApi = getValidCameraWorksApi(commandLine);
            File htmlOutputDirectory = getValidOutputDirectory(commandLine);

            return new ApplicationInputArguments(cameraWorksApi, htmlOutputDirectory);

        } catch (MissingOptionException e) {
            throw new ApplicationInputException("Missing required arguments: "+e.getMissingOptions());
        } catch (MissingArgumentException e) {
            throw new ApplicationInputException("Missing argument: "+e.getOption().getOpt());
        } catch (ParseException pe) {
            throw new ApplicationInputException("Could not parse input arguments", pe);
        }
    }

    private String getValidCameraWorksApi(CommandLine clp) throws ApplicationInputException {
        String cameraApi = null;
        if (clp.hasOption(API_CAMERA_WORKS_LOCATION_OPTION.getOpt())) {
             cameraApi = clp.getOptionValue(API_CAMERA_WORKS_LOCATION_OPTION.getOpt());
        }
        if ( StringUtils.isNotBlank(cameraApi)) {
            return cameraApi;
        }
        throw new ApplicationInputException("Invalid Camera works API ["+cameraApi+"] given");

    }

    private File getValidOutputDirectory(CommandLine commandLine) throws ApplicationInputException {
        String outputDirectoryString = null;
        if (commandLine.hasOption(HTML_OUTPUT_DIRECTORY_OPTION.getOpt())) {
            outputDirectoryString = commandLine.getOptionValue(HTML_OUTPUT_DIRECTORY_OPTION.getOpt());
        }
        File outputDirectory = null;
        if ( StringUtils.isNotBlank(outputDirectoryString)) {
            outputDirectory = new File(outputDirectoryString);
        }
        if ( outputDirectory != null && outputDirectory.isDirectory()) {
            return outputDirectory;
        }
        throw new ApplicationInputException("Invalid output directory ["+outputDirectoryString+"] given");
    }

    public void printUsageInformation(OutputStream printStream) {
        HelpFormatter formatter = new HelpFormatter();
        String header = System.lineSeparator()+"See below on how to use this RedBubble Camera Website Generator";
        try (PrintWriter printWriter = new PrintWriter(printStream)) {
            formatter.printHelp(printWriter, 300, "java -jar redbubble-generate-camera-website-*.jar", header, ALL_INPUT_OPTIONS, 0, 0, System.lineSeparator(), true);
        }
    }

}

