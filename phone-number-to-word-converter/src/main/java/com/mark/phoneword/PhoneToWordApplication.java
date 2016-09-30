package com.mark.phoneword;


import com.mark.phoneword.convert.ConverterFactory;
import com.mark.phoneword.data.read.FileReaderFactory;
import com.mark.phoneword.dictionary.Dictionary;
import com.mark.phoneword.dictionary.DictionaryFactory;
import com.mark.phoneword.input.InputArgumentReader;
import com.mark.phoneword.input.InputProcessor;

import static com.mark.phoneword.util.OutputUtils.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public class PhoneToWordApplication {

    private final static int EXIT_CODE_OK = 0;
    private final static int EXIT_CODE_NO_DICTIONARY_LOADED = 1;
    private final static int EXIT_CODE_INPUT_PHONE_FILE_NOT_LOADED = 2;

    private int run(String[] args) {

        int exitCode;

        InputArgumentReader inputArgumentReader = new InputArgumentReader(args);

        printWelcomeMessage(inputArgumentReader.getArgumentUsageInfo(), inputArgumentReader.getArgumentValues());

        Dictionary dictionaryToUse = null;

        // Check if we were given a dictionary file
        Optional<String> inputDictionaryFileOptional = inputArgumentReader.getDictionaryFile();
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
            exitCode = onDictionaryLoaded(inputArgumentReader, dictionaryToUse);
        } else {
            exitCode = EXIT_CODE_NO_DICTIONARY_LOADED;
        }


        return exitCode;
    }

    private int onDictionaryLoaded(InputArgumentReader inputArgumentReader, Dictionary dictionaryToUse) {

        Optional<String> phoneNumbersFileOptional = inputArgumentReader.getPhoneNumbersFile();
        if ( phoneNumbersFileOptional.isPresent()) {
            String phoneNumbersFile = phoneNumbersFileOptional.get();
            Optional<Set<Long>> readPhoneNumbersOptional = FileReaderFactory.longsOnlyLineReader().readFile(phoneNumbersFile);
            if ( readPhoneNumbersOptional.isPresent()) {
                ConverterFactory.longNumberToWords();
            } else {
                return EXIT_CODE_INPUT_PHONE_FILE_NOT_LOADED;
            }
        } else {
            new InputProcessor(ConverterFactory.longNumberToWords()).processFromStdIn();
//            onReadPhoneNumbersFromTerminal(dictionaryToUse);
        }
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

}
