package com.mark.phoneword;


import com.mark.phoneword.dictionary.Dictionary;
import com.mark.phoneword.dictionary.DictionaryFactory;
import com.mark.phoneword.input.InputReader;

import java.util.List;
import java.util.Optional;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public class PhoneToWordApplication {

    private final static int EXIT_CODE_OK = 0;
    private final static int EXIT_CODE_NO_DICTIONARY_LOADED = 0;

    private int run(String[] args) {

        int exitCode;

        InputReader inputReader = new InputReader(args);

        printWelcomeMessage(inputReader.getArgumentUsageInfo(), inputReader.getArgumentValues());

        Dictionary dictionaryToUse = null;

        // Check if we were given a dictionary file
        Optional<String> inputDictionaryFileOptional = inputReader.getDictionaryFile();
        if ( inputDictionaryFileOptional.isPresent()) {
            Optional<Dictionary> inputDictionaryOptional = DictionaryFactory.fromFile(inputDictionaryFileOptional.get());
            if ( inputDictionaryOptional.isPresent()) {
                dictionaryToUse = inputDictionaryOptional.get();
            } else {
                printError("Could not load input dictionary from file ["+inputDictionaryFileOptional.get()+"]");
            }
        } else {
            dictionaryToUse = DictionaryFactory.getDefault();
        }

        if ( dictionaryToUse != null) {
            exitCode = onDictionaryLoaded(inputReader, dictionaryToUse);
        } else {
            exitCode = EXIT_CODE_NO_DICTIONARY_LOADED;
        }


        return exitCode;
    }

    private int onDictionaryLoaded(InputReader inputReader, Dictionary dictionaryToUse) {
        return EXIT_CODE_OK;
    }

    private void printWelcomeMessage(List<String> argumentInfo, List<String> argumentValues) {
        printInfo(System.lineSeparator()+"Welcome to the Number to Word Application!");
        printInfo(System.lineSeparator()+"The following are the input options:");
        argumentInfo.forEach( info -> printInfo("  "+info));

        // Print out any argument values gotten
        printInfo(System.lineSeparator()+"The following are the parsed input values:");
        argumentValues.forEach(info -> printInfo("  "+info));
        printInfo(System.lineSeparator());

    }

    public static void main(String[] args) {
        int exitCode = new PhoneToWordApplication().run(args);
        System.exit(exitCode);
    }

    private void printInfo(String message) {
        System.out.println(message);
    }

    private void printError(String message) {
        System.err.println("***ERROR: "+message);
    }

}
