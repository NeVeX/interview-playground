package com.mark.phoneword.input;

import com.mark.phoneword.convert.Converter;
import com.mark.phoneword.util.StringUtils;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import static com.mark.phoneword.util.OutputUtils.*;

/**
 * Created by Mark Cunningham on 9/30/2016.
 */
public class InputProcessor {

    private final static String QUIT_INPUT = "q";
    private final static String INPUT_NUMBER = "Enter a number (or quit with '"+QUIT_INPUT+"'): ";

    private final Converter<Long, String> converter;

    public InputProcessor(Converter<Long, String> converter) {
        if ( converter == null) {
            throw new IllegalArgumentException("Provided converter cannot be null");
        }
        this.converter = converter;
    }

    public void processStdIn() {

        Scanner inputScanner = new Scanner(System.in);
        while ( true ) {
            printInput(System.lineSeparator()+INPUT_NUMBER);

            String inputReceived = inputScanner.next();

            if ( inputReceived.trim().toLowerCase().equals(QUIT_INPUT)) {
                break;
            }
            printWorkingStatus();
            Optional<Long> parsedLongOptional = StringUtils.tryConvert(inputReceived);

            if ( parsedLongOptional.isPresent()) {
                long parsedNumber = parsedLongOptional.get();
                Set<String> convertedWords = convertToSet(parsedNumber);
                printConversionResult(parsedNumber, convertedWords);
            } else {
                printError("Could not parse input ["+inputReceived+"] into a number");
            }
        }
    }

    private void printWorkingStatus() {
        printInfo("Working...");
    }

    private void printConversionResult(long inputNumber, Set<String> convertedWords) {
        if ( !convertedWords.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Found [")
                    .append(convertedWords.size())
                    .append("] word combinations for [")
                    .append(inputNumber)
                    .append("]:")
                    .append(System.lineSeparator());
            convertedWords.forEach( word -> stringBuilder.append(word).append(System.lineSeparator()));
            printInfo(stringBuilder.toString());
        } else {
            printInfo("No suitable word combinations found for input number ["+inputNumber+"]");
        }
    }

    private Set<String> convertToSet(long number) {
        return this.converter.convert(number);
    }

    private ConversionResult convertToResult(long number) {
        Set<String> convertedWords = this.converter.convert(number);
        return new ConversionResult(number, convertedWords);
    }

    public void processBatch(Set<Long> numbersToProcess) {
        if ( numbersToProcess != null && !numbersToProcess.isEmpty()) {
            printInfo("Starting batch processBatch to convert all ["+numbersToProcess.size()+"] phone numbers to words...");
            numbersToProcess
                .parallelStream()
                .map(this::convertToResult)
                .forEach( result -> printConversionResult(result.inputNumber, result.convertedWords));
        } else {
            printError("No input phone numbers were provided to batch process");
        }
    }

    private static class ConversionResult {
        private long inputNumber;
        private Set<String> convertedWords;

        ConversionResult(long inputNumber, Set<String> convertedWords) {
            this.inputNumber = inputNumber;
            this.convertedWords = convertedWords;
        }
    }

}
