package com.mark.phoneword.data.read;

import com.mark.phoneword.util.StringUtils;

import java.io.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Mark Cunningham on 9/29/2016.
 * <br>Abstract that helps with read files. Subclasses need only implement the actually reading of data with conversion
 */
public abstract class FileReader<T> {

    private static final Logger LOGGER = Logger.getLogger(FileReader.class.getName());

    /**
     * Given the bufferedReader for the resource/file, return the parsed data from it.
     * <br>Note, return null to indicate that no data was read from the file.
     * @param br - the buffered reader to use to parse the data
     * @return - return null to indicate no data read; otherwise, return the data
     * @throws IOException - if something went wrong :-(
     */
    protected abstract T process(BufferedReader br) throws IOException;

    /**
     * Read the given resource - (No exceptions are thrown)
     * @param resource - the non-null resource
     * @return - The result of this data read
     */
    public Optional<T> readResource(String resource) {
        if (StringUtils.isNotBlank(resource)) {
            LOGGER.info("Attempting to read resource ["+resource+"]");
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
                 InputStreamReader inputStreamReader = new InputStreamReader(is);
                 BufferedReader br = new BufferedReader(inputStreamReader)) {
                T result = process(br);
                return Optional.ofNullable(result);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "An exception occurred while reading the resource ["+resource+"]", e);
            }

        }
        return Optional.empty();
    }


    /**
     * For the given input file location, this will return the parsed/extracted data in the expected type
     * @param inputFile - the valid (absolute) file location to read
     * @return - The parsed data in the appropriate type. Returns an optional to indicate if data was extracted or not
     */
    public Optional<T> readFile(String inputFile) {
        if (StringUtils.isNotBlank(inputFile)) {
            try (InputStreamReader inputStreamReader = new java.io.FileReader(inputFile);
                 BufferedReader br = new BufferedReader(inputStreamReader)) {
                T result = process(br);
                return Optional.ofNullable(result);
            } catch (IOException e ) {
                LOGGER.log(Level.SEVERE, "An exception occurred while reading file ["+inputFile+"]", e);
            }
        }
        return Optional.empty();
    }


}
