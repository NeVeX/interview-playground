package com.mark.phoneword.data.read;

import com.mark.phoneword.util.StringUtils;
import com.sun.org.apache.regexp.internal.RE;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public abstract class FileReader<T> {

    private static final Logger LOGGER = Logger.getLogger(FileReader.class.getName());

    protected abstract T process(BufferedReader br) throws IOException;

    /**
     * Read the given resource - (No exceptions are thrown)
     * @param resource - the non-null resource
     * @return - The result of this data read
     */
    Optional<T> readResource(String resource) {
        return readStream(resource, this.getClass().getClassLoader().getResourceAsStream(resource));
    }

    public Optional<T> readFile(String inputFile) {
        if (!StringUtils.isBlank(inputFile)) {
            try (InputStream is = new FileInputStream(inputFile) ) {
                return readStream(inputFile, is);
            } catch (IOException e ) {
                LOGGER.log(Level.SEVERE, "An exception occurred while reading input file ["+inputFile+"]{0}", e);;
            }
        }
        return Optional.empty();
    }

    private Optional<T> readStream(String resource, InputStream is) {

        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                T result = process(br);
                return Optional.ofNullable(result);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "An exception occurred while reading the stream for resource ["+resource+"]{0}", e);
            }

        }
        return Optional.empty();
    }

}