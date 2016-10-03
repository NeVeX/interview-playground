package com.mark.flowershop.data.read;

import com.mark.flowershop.product.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
abstract class ResourceReader<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceReader.class);

    protected abstract T process(BufferedReader bufferedReader) throws IOException;

    public T readResource(String resource) {
        if (!StringUtils.isBlank(resource)) {
            try (InputStream inputStream = this.getClass().getResourceAsStream(resource);
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                return process(bufferedReader);
            } catch (IOException e) {
                LOGGER.error("Could not load the resource [{}]", resource, e);
            }
        }
        return null;
    }

}
