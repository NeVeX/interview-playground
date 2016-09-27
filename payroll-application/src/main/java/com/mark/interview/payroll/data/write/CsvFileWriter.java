package com.mark.interview.payroll.data.write;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/25/2016.
 * <br>This abstract class aims to help with usages of writing csv data to files
 */
public abstract class CsvFileWriter<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvFileWriter.class);
    final static String COMMA = ",";

    /**
     * Given the data that needs to be written to CSV, return the CSV line to write
     * <br>Note, the returned string can be null/blank, if that data should not be written
     * @param data - The date to parse into a CSV string
     * @return - The (optionally) nullable data to write to the file. Do not include line breaks
     */
    protected abstract String convertToCsvString(T data);

    /**
     * Get the CSV header that should be inserted into this CSV file
     * @return - The CSV header - note, this can be null/blank to indicate no header to write. Do not include line breaks
     */
    protected abstract String getCsvHeader();

    /**
     * Given the set of data, write each one to the given file location
     * @param fileToUse - The fully qualified file to write to
     * @param dataList - The data that should be written to the file
     * @return - The result of this file write operation.
     */
    public DataWriterResult writeToFile(String fileToUse, Set<T> dataList) {
        boolean isSuccessful = true;
        long dataWriteCount = 0;
        if (!StringUtils.isBlank(fileToUse) && dataList != null && !dataList.isEmpty()) {

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileToUse))) {
                // Check if we should write a CSV header
                String headerLine = getCsvHeader();
                if (!StringUtils.isBlank(headerLine)) {
                    writer.write(getCsvHeader());
                    writer.write(System.lineSeparator());
                }

                // Checked exceptions don't work well inside streams, so using non-stream approach here
                for ( T data : dataList) {
                    // Check if we are to write this next data to the file
                    String stringToWrite = convertToCsvString(data);
                    if ( !StringUtils.isBlank(stringToWrite)) {
                        writer.write(convertToCsvString(data));
                        writer.write(System.lineSeparator());
                        dataWriteCount++;
                    }
                }
            } catch (IOException e) {
                LOGGER.error("An exception occurred while trying to write to the file [{}]{}", fileToUse, e);
                isSuccessful = false;
            }
        } else {
            isSuccessful = false;
        }

        return new DataWriterResult(isSuccessful, fileToUse, dataWriteCount);
    }

}
