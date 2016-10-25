package com.mark.redbubble.html;

import com.mark.redbubble.output.OutputWriter;
import com.mark.redbubble.output.OutputWriterException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/23/2016.
 * <br>This generator is responsible for creating all the resources for the website (styles, js scripts...etc)
 */
class StaticResourcesGenerator implements Generator {

    private final Set<StaticResourceInformation> allScripts;
    private final Set<StaticResourceInformation> allStyles;

    private static final String TEMPLATES_LOCATION = "templates";
    private static final String SCRIPTS_DIRECTORY_NAME = "scripts";
    private static final String STYLES_DIRECTORY_NAME = "styles";

    private static final String SCRIPTS_LOCATION = TEMPLATES_LOCATION + "/" + SCRIPTS_DIRECTORY_NAME;
    private static final String STYLES_LOCATION = TEMPLATES_LOCATION + "/" + STYLES_DIRECTORY_NAME;


    StaticResourcesGenerator() {
        try {
            // Load all the script locations that we have in the resources
            allScripts = getAllFilesInResourceDirectory(SCRIPTS_LOCATION, SCRIPTS_DIRECTORY_NAME);
            // Load all the style locations that we have in the resources
            allStyles = getAllFilesInResourceDirectory(STYLES_LOCATION, STYLES_DIRECTORY_NAME);
        } catch (IOException ioException) {
            // Wrap the checked exception into a un-checked exception while in the constructor
            // This method should never fail, hence it's ok to make it a runtime exception
            throw new IllegalStateException("Could not load all static resources", ioException);
        }
    }

    /**
     * Initiates a new generation of all the dependent resources for the website
     * @param outputWriter - the valid file writer to use
     * @throws OutputWriterException
     */
    @Override
    public void generate(OutputWriter outputWriter) throws OutputWriterException {

        writeAllResources(allScripts, outputWriter);
        writeAllResources(allStyles, outputWriter);

    }

    /**
     * Writes all the given resources to the given file writer
     * @param resources - the resources in the application
     * @param outputWriter - the place to write to
     * @throws OutputWriterException
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
     * @throws OutputWriterException
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
     * Helper method to get all the resource files for a given directory
     * @param directory - the directory resource to search
     * @param outputDirectory - the out put directory that this resource should go to later
     * @return - the set of resources loaded
     * @throws IOException
     */
    private Set<StaticResourceInformation> getAllFilesInResourceDirectory(String directory, String outputDirectory) throws IOException {
        try (InputStream inputStream = this.getClass().getResourceAsStream(directory);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            // Each line inf the buffered reader is a file name
            return bufferedReader
                    .lines()
                    .map(entry -> new StaticResourceInformation(entry, directory, outputDirectory))
                    .collect(Collectors.toSet());

        }
    }

    /**
     * Helpful inner class with decorated information on resource information
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
