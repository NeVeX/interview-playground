package com.mark.interview.payroll.data.read;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/24/2016.
 * <br>Abstract class that offers methods to read files and resources.
 * <br>This class has abstract methods that can be implemented for easier parsing of file/resource data
 */
public abstract class FileReader<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReader.class);
    protected abstract FileReaderResult<T> process(BufferedReader br) throws IOException;

    /**
     * Read the given resource - (No exceptions are thrown)
     * @param resource - the non-null resource
     * @return - The result of this data read
     */
    DataReaderResult<T> readResource(String resource) {
        return readStream(resource, this.getClass().getClassLoader().getResourceAsStream(resource));
    }

    /**
     * Read the given string (which should be a fully qualified file name) - (No exceptions are thrown)
     * @param inputFile - The file to read
     * @return - The result of this data read
     */
    public DataReaderResult<T> readFile(String inputFile) {
        if (!StringUtils.isBlank(inputFile)) {
            try (InputStream is = new FileInputStream(inputFile) ) {
                return readStream(inputFile, is);
            } catch (IOException e ) {
                // Welp, this sucks, log the error and stack here - we'll inform the caller of the bad news later
                LOGGER.error("An exception occurred while reading input file [{}]{}", inputFile, e);
            }
        }
        return new DataReaderResult<>(inputFile, false);
    }

    /**
     * Helper method to read a given stream (No exceptions are thrown)
     * @param resource - the resource name
     * @param is - The input stream for that resource
     * @return - The result of this data read
     */
    private DataReaderResult<T> readStream(String resource, InputStream is) {
        Set<T> parsedData = new HashSet<>();
        boolean wasResourceFound = true;
        int invalidDataCount = 0;
        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                FileReaderResult<T> result = process(br);
                if ( result != null ) {
                    parsedData = result.data; // can be null
                    invalidDataCount = result.invalidDataCount;
                }
            } catch (IOException e) {
                LOGGER.error("An exception occurred while reading the stream for resource [{}]{}", resource, e);
                wasResourceFound = false;
            }

        }
        return new DataReaderResult<>(resource, wasResourceFound, invalidDataCount, parsedData);
    }

    /**
     * Helper data holder for data read results
     * @param <T>
     */
    static class FileReaderResult<T> {

        private Set<T> data;
        private int invalidDataCount;

        FileReaderResult() {}

        FileReaderResult(Set<T> data, int invalidDataCount) {
            this.data = data;
            this.invalidDataCount = invalidDataCount;
        }
    }


}
