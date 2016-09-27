package com.mark.interview.payroll.data.read;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Mark Cunningham on 9/24/2016.
 * <br>An abstract JsonFileReader, to, help read files that are in the JSON format
 * <br> Subclasses will be given the file contents as an object to parse further
 */
abstract class JsonFileReader<T, J> extends FileReader<T> {

    private final static ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();
    private ObjectMapper objectMapper = DEFAULT_OBJECT_MAPPER;
    private final Class<J> clazz;

    /**
     * Given the parsed/read JSON object, convert it to another data object/pojo if needed
     * @param jsonObject - the read json object
     * @return - the converted json object data
     */
    protected abstract T process(J jsonObject);

    JsonFileReader(Class<J> clazz) {
        if ( clazz == null) { throw new IllegalArgumentException("Provided class cannot be null"); }
        this.clazz = clazz;
    }

    /**
     * Option to override the objectmapper to a different one than the default, if needed
     * @param objectMapper
     */
    protected void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override // Implements the parent class to process the reader stream given
    protected final FileReaderResult<T> process(BufferedReader br) throws IOException {

        J jsonObject = objectMapper.readValue(br, clazz);
        T parsedData = process(jsonObject);
        if ( parsedData != null) {
            return new FileReaderResult<>(new HashSet<T>() {{ add(parsedData);}}, 0);
        } else {
            return new FileReaderResult<>(); // Nothing was parsed...:-(
        }
    }
}
