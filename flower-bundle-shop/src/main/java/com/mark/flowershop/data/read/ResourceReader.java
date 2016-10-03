package com.mark.flowershop.data.read;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
abstract class ResourceReader<T> {

    protected abstract T process(BufferedReader bufferedReader) throws IOException;

    protected T readResource(String resource) {
        if (!StringUtils.isBlank(resource)) {
            try (InputStream inputStream = this.getClass().getResourceAsStream(resource);
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                return process(bufferedReader);
            } catch (IOException e) {
                /// TODO: Log..
                e.printStackTrace();
            }
        }
        return null;
    }

}
