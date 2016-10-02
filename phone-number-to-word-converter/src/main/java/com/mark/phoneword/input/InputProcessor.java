package com.mark.phoneword.input;

import com.mark.phoneword.convert.Converter;
import com.mark.phoneword.util.StringUtils;

import static com.mark.phoneword.util.OutputUtils.*;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

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

    public void processFromStdIn() {

        Scanner inputScanner = new Scanner(System.in);
        while ( true ) {


            printInput(System.lineSeparator()+INPUT_NUMBER);

            String inputReceived = inputScanner.next();

            if ( inputReceived.trim().toLowerCase().equals(QUIT_INPUT)) {
                break;
            }

            Optional<Long> parsedLongOptional = StringUtils.tryConvert(inputReceived);

            if ( parsedLongOptional.isPresent()) {
                long parsedNumber = parsedLongOptional.get();
                Set<String> convertedWords = convert(parsedNumber);
                if ( !convertedWords.isEmpty()) {
                    printInfo("Found ["+convertedWords.size()+"] word combinations for [" + parsedNumber + "]:");
                    printInfo("  "+convertedWords.toString());
                } else {
                    printInfo("No suitable word combinations found for input number ["+parsedNumber+"]");
                }
            } else {
                printError("Could not parse input ["+inputReceived+"] into a number");
            }

        }
    }


    private Set<String> convert(long number) {
        return this.converter.convert(number);
    }

}
