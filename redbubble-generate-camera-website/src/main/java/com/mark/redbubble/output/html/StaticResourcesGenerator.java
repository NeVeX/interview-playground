package com.mark.redbubble.output.html;

import com.mark.redbubble.output.OutputWriter;
import com.mark.redbubble.output.OutputWriterException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/23/2016.
 * <br>This generator is responsible for creating all the resources for the website (styles, js scripts...etc)
 */
class StaticResourcesGenerator implements Generator {

    private static final Set<StaticResourceInformation> ALL_SCRIPTS = new HashSet<>();
    private static  Set<StaticResourceInformation> ALL_STYLES = new HashSet<>();

    private static final String TEMPLATES_LOCATION = "/templates";
    private static final String SCRIPTS_OUTPUT_DIRECTORY_NAME = "scripts";
    private static final String STYLES_OUTPUT_DIRECTORY_NAME = "styles";

    private static final String SCRIPTS_RESOURCE_LOCATION = TEMPLATES_LOCATION + "/" + SCRIPTS_OUTPUT_DIRECTORY_NAME;
    private static final String STYLES_RESOURCE_LOCATION = TEMPLATES_LOCATION + "/" + STYLES_OUTPUT_DIRECTORY_NAME;

    static {
        /*
            There are better ways to be be more abstract with regards to loading the resources needed.
            1 - package these resources outside the jar and simple copy them to the destination
            2 - read all resources in the jar and copy the contents to the destination (has complicated logic)
            3 - list out explicitly the resources to use (the approach here - which is not great, but given the time/size, using it here)

            Given the fact that we only want to copy 3 resources, for now, we'll set the names here.
            This would be bad in larger projects as the view dependency is coupled with this generator and can lead to problems
            when the view changes without the generator changes
         */
        // Add the scripts
        ALL_SCRIPTS.add(new StaticResourceInformation("jquery-3.1.1.min.js", SCRIPTS_RESOURCE_LOCATION, SCRIPTS_OUTPUT_DIRECTORY_NAME));
        ALL_SCRIPTS.add(new StaticResourceInformation("selection_control.js", SCRIPTS_RESOURCE_LOCATION, SCRIPTS_OUTPUT_DIRECTORY_NAME));
        // Add the styles
        ALL_STYLES.add(new StaticResourceInformation("main.css", STYLES_RESOURCE_LOCATION, STYLES_OUTPUT_DIRECTORY_NAME));
    }

    /**
     * Initiates a new generation of all the dependent resources for the website
     * @param outputWriter - the valid file writer to use
     * @throws OutputWriterException - if something went wrong writing the resources out
     */
    @Override
    public void generate(OutputWriter outputWriter) throws OutputWriterException {

        writeAllResources(ALL_SCRIPTS, outputWriter);
        writeAllResources(ALL_STYLES, outputWriter);

    }

    /**
     * Writes all the given resources to the given file writer
     * @param resources - the resources in the application
     * @param outputWriter - the place to write to
     * @throws OutputWriterException - if something went wrong writing the resources out
     */
    private void writeAllResources(Set<StaticResourceInformation> resources, OutputWriter outputWriter) throws OutputWriterException {
        for ( StaticResourceInformation staticResourceInformation : resources) {
            writeResourceToFile(staticResourceInformation, outputWriter);
        }
    }

    /**
     * Given a particular resource, this will write the resource into the given {@link OutputWriter}
     * @param resourceInfo - the resource info (name, location etc)
     * @param outputWriter - the file writer to output to
     * @throws OutputWriterException - if something went wrong writing the resources out
     */
    private void writeResourceToFile(StaticResourceInformation resourceInfo, OutputWriter outputWriter) throws OutputWriterException {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resourceInfo.resourceLocation);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            // Get all the data within -> Note: we expect this to be a small size, hence will fit into memory at once
            String contents = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
            // Once we have the content, we write it to the file
            outputWriter.writeContentsToFile(resourceInfo.outputDirectory, resourceInfo.outputFileName, contents);
        } catch (Exception exception) {
            throw new OutputWriterException("Could not write resource file ["+resourceInfo.outputFileName+"]", exception);
        }
    }

    /**
     * Helpful inner class with decorated information on resources (where they are, and where to relatively output them)
     */
    private static class StaticResourceInformation {

        private final String resourceLocation;
        private final String outputDirectory;
        private final String outputFileName;

        StaticResourceInformation(String resourceFileName, String resourceDirectory, String outputDirectory) {
            this.resourceLocation = resourceDirectory + "/" + resourceFileName;
            this.outputDirectory = outputDirectory;
            this.outputFileName = resourceFileName;
        }
    }

}
