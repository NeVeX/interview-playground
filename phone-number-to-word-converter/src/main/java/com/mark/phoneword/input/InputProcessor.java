package com.mark.phoneword.input;

import com.mark.phoneword.convert.Converter;
import com.mark.phoneword.util.OutputUtils;
import com.mark.phoneword.util.StringUtils;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import static com.mark.phoneword.util.OutputUtils.*;

/**
 * Created by Mark Cunningham on 9/30/2016.
 * <br>This class provides methods that help with the processing of user input and data input
 */
public class InputProcessor {

    private final static Logger LOGGER = Logger.getLogger(OutputUtils.class.getName());
    private final static String QUIT_INPUT = "q";
    private final static String INPUT_NUMBER = "Enter a number (or quit with '"+QUIT_INPUT+"'): ";

    private final Converter<Long, String> converter;

    /**
     * Creates a new instance of the the processor, using the converter as provided.
     * @param converter - The converter to use for all data processing
     */
    public InputProcessor(Converter<Long, String> converter) {
        if ( converter == null) {
            throw new IllegalArgumentException("Provided converter cannot be null");
        }
        this.converter = converter;
    }

    /**
     * Invoking this method will listen for input from the standard input, hence this method blocks.
     * This method exists only when the user asks to exit/stop processing from standard in
     */
    public void processStdIn() {
        // Create a scanner using standard in
        Scanner inputScanner = new Scanner(System.in);
        while ( true ) {
            printInput(System.lineSeparator()+INPUT_NUMBER); // print how to enter numbers and quit

            String inputReceived = inputScanner.next(); // This blocks until the user enters input

            if ( inputReceived.trim().toLowerCase().equals(QUIT_INPUT)) {
                // We are quitting...
                break;
            }
            LOGGER.fine("Received user input ["+inputReceived+"]");
            printInfo("Working...");

            // Check what we got as input
            Optional<Long> parsedLongOptional = StringUtils.tryConvertAfterRemovingInvalidChars(inputReceived);
            if ( parsedLongOptional.isPresent()) {
                // We have a number, so let's convert it using the converter in this instance
                long parsedNumber = parsedLongOptional.get();
                Set<String> convertedWords = convertToSet(parsedNumber);
                // Print the result to the terminal
                printConversionResult(parsedNumber, convertedWords);
            } else {
                printError("Could not parse input ["+inputReceived+"] into a number");
            }
        }
    }

    /**
     * For the given result, print it out to the screen
     */
    private void printConversionResult(long inputNumber, Set<String> convertedWords) {
        if ( !convertedWords.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Found [")
                    .append(convertedWords.size())
                    .append("] word combinations for [")
                    .append(inputNumber)
                    .append("]:")
                    .append(System.lineSeparator());
            convertedWords.forEach( word -> stringBuilder.append("  - ").append(word).append(System.lineSeparator()));
            printInfo(stringBuilder.toString());
        } else {
            printInfo("No suitable word combinations found for input number ["+inputNumber+"]");
        }
    }

    /**
     * Converts the number into a set of Strings
     */
    private Set<String> convertToSet(long number) {
        return this.converter.convert(number);
    }

    /**
     * A wrapper to the {{@link #convertToSet(long)}} that creates a result object also
     */
    private ConversionResult convertToResult(long number) {
        Set<String> convertedWords = this.converter.convert(number);
        return new ConversionResult(number, convertedWords);
    }

    /**
     * This method will batch process all the input numbers and print it's result to the screen
     * <br>Note this method may take some time to complete
     * @param numbersToProcess - the numbers to process
     */
    public void processBatch(Set<Long> numbersToProcess) {
        if ( numbersToProcess != null && !numbersToProcess.isEmpty()) {

            String message = "Starting batch process to convert all ["+numbersToProcess.size()+"] phone numbers to words";
            printInfo(message);
            LOGGER.info(message);

            numbersToProcess
                .parallelStream()
                .map(this::convertToResult)
                .forEach( result -> printConversionResult(result.inputNumber, result.convertedWords));

            message = "Finished batch process on converting ["+numbersToProcess.size()+"] phone numbers to words";
            printInfo(message);
            LOGGER.info(message);
        } else {
            printError("No input phone numbers were provided to batch process");
        }
    }

    /**
     * Simple wrapper class that represents conversion results and is used within streams
     */
    private static class ConversionResult {
        private long inputNumber;
        private Set<String> convertedWords;

        ConversionResult(long inputNumber, Set<String> convertedWords) {
            this.inputNumber = inputNumber;
            this.convertedWords = convertedWords;
        }
    }

}
