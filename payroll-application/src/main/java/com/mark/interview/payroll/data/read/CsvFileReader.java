package com.mark.interview.payroll.data.read;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/24/2016.
 * <br> Abstract CSV file reader - it handles common functionality with reading CSV files
 * <br> This class extends the {@link FileReader}
 */
public abstract class CsvFileReader<T> extends FileReader<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvFileReader.class);

    /**
     * Extract the Object from the given CSV line
     * @param lineScanner - The current line scanner for the csv file
     * @return - The parsed data object using the scanner
     */
    protected abstract T extractFromCsvLine(Scanner lineScanner);

    /**
     * Is this CSV header valid (columns in the right place?)
     * @param headerScanner - The scanner line for the first row in the CSV
     * @return - Is the header valid, returning false will not continue with the parsing of the csv
     */
    protected abstract boolean isCsvHeaderValid(Scanner headerScanner);

    /**
     * Implementing the parent {@link FileReader}, we'll process the reader and pass each line to the sub class
     * @param br - The opened reader to use
     * @return - The result of this CSV read
     * @throws IOException
     */
    @Override
    protected final FileReaderResult<T> process(BufferedReader br) throws IOException {
        Set<T> parsedData = new HashSet<>();
        int invalidLines = 0;
        boolean processedHeader = false;
        String line;
        Scanner lineScanner = null;
        while ((line = br.readLine()) != null) {

            lineScanner = new Scanner(line);
            lineScanner.useDelimiter(",");

            /**
             * Process the header and ask the sub class if the header is valid or not
             */
            if ( !processedHeader ) {
                processedHeader = true;
                if ( !isCsvHeaderValid(lineScanner)) {
                    LOGGER.error("The CSV Header line is not valid - will not continue to read this csv resource");
                    break;
                }
                continue;
            }

            // Get the sub class to parse the line of data
            T parsedObject = extractFromCsvLine(lineScanner);

            lineScanner.close();

            if (parsedObject != null) {
                parsedData.add(parsedObject);
            } else {
                invalidLines++;
            }
        }

        if ( lineScanner != null ) { lineScanner.close(); } // safety
        return new FileReaderResult<>(parsedData, invalidLines);
    }

}
