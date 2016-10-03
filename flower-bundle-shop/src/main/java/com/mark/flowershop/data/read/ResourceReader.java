package com.mark.flowershop.data.read;

import com.mark.flowershop.product.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>Abstract class that assists in the reading of resource files.
 * <br>Sub-classes need to implement the conversion process when given the BufferedReader
 */
abstract class ResourceReader<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceReader.class);

    /**
     * Given the BufferedReader for this resource, return the data extracted.
     * @param bufferedReader - the valid resource reader
     * @return - The data read - returning null is valid here too
     * @throws IOException - If something goes wrong reading the bufferedReader
     */
    protected abstract T process(BufferedReader bufferedReader) throws IOException;

    /**
     * Reads the given resource into the required data type, if possible
     * @param resource - the resource to read
     * @return An Optional representing the read in data, or empty if nothing could be read
     */
    public Optional<T> readResource(String resource) {
        if (!StringUtils.isBlank(resource)) {
            try (InputStream inputStream = this.getClass().getResourceAsStream(resource);
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                return Optional.ofNullable(process(bufferedReader));
            } catch (IOException e) {
                LOGGER.error("Could not load the resource [{}]", resource, e);
            }
        }
        return Optional.empty();
    }

}
