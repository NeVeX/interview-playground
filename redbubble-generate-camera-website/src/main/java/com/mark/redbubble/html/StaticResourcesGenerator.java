package com.mark.redbubble.html;

import com.mark.redbubble.output.FileWriter;
import com.mark.redbubble.output.FileWriterException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/23/2016.
 */
class StaticResourcesGenerator implements Generator {

    private final Set<StaticResourceInformation> allScripts;
    private final Set<StaticResourceInformation> allStyles;

    private static final String TEMPLATES_LOCATION = "/templates";
    private static final String SCRIPTS_DIRECTORY_NAME = "scripts";
    private static final String STYLES_DIRECTORY_NAME = "styles";

    private static final String SCRIPTS_LOCATION = TEMPLATES_LOCATION + "/" + SCRIPTS_DIRECTORY_NAME;
    private static final String STYLES_LOCATION = TEMPLATES_LOCATION + "/" + STYLES_DIRECTORY_NAME;

    StaticResourcesGenerator() {
        try {
            allScripts = getAllFilesInResourceDirectory(SCRIPTS_LOCATION, SCRIPTS_DIRECTORY_NAME);
            allStyles = getAllFilesInResourceDirectory(STYLES_LOCATION, STYLES_DIRECTORY_NAME);
        } catch (GeneratorException generatorException) {
            // Wrap the checked exception into a un-checked exception while in the constructor
            // This method should never fail, hence it's ok to make it a runtime exception
            throw new IllegalStateException("Could not load all static resources", generatorException);
        }
    }

    @Override
    public void generate(FileWriter fileWriter) throws FileWriterException {

        writeAllResources(allScripts, fileWriter);
        writeAllResources(allStyles, fileWriter);

    }

    private void writeAllResources(Set<StaticResourceInformation> resources, FileWriter fileWriter) throws FileWriterException {
        for ( StaticResourceInformation staticResourceInformation : resources) {
            writeResourceToFile(staticResourceInformation, fileWriter);
        }
    }

    private void writeResourceToFile(StaticResourceInformation resourceInfo, FileWriter fileWriter) throws FileWriterException {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resourceInfo.resourceLocation);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String contents = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));

            fileWriter.writeContentsToFile(resourceInfo.outputDirectory, resourceInfo.outputFileName, contents);

        } catch (Exception exception) {
            throw new FileWriterException("Could not write resource file ["+resourceInfo.outputFileName+"]", exception);
        }
    }

    private Set<StaticResourceInformation> getAllFilesInResourceDirectory(String directory, String outputDirectory) throws GeneratorException {
        try(InputStream inputStream = this.getClass().getResourceAsStream(directory);
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( inputStream ) ) ) {

            return bufferedReader
                    .lines()
                    .map(entry -> new StaticResourceInformation(entry, directory, outputDirectory))
                    .collect(Collectors.toSet());

        } catch (Exception exception )  {
            throw new GeneratorException("Could not get all files in resource directory ["+directory+"]", exception);
        }
    }


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
