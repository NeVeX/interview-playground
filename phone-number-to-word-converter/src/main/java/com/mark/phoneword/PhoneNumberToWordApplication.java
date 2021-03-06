package com.mark.phoneword;


import com.mark.phoneword.convert.Converter;
import com.mark.phoneword.convert.ConverterFactory;
import com.mark.phoneword.data.read.FileReaderFactory;
import com.mark.phoneword.dictionary.Dictionary;
import com.mark.phoneword.dictionary.DictionaryFactory;
import com.mark.phoneword.input.InputArgumentReader;
import com.mark.phoneword.input.InputProcessor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.mark.phoneword.util.OutputUtils.*;

/**
 * Created by Mark Cunningham on 9/29/2016.
 * <br>This is the entry into the Application. The application is started with a call to either the
 * static {{@link #main(String[])}} or {{@link #run(String[])}} methods.
 */
public class PhoneNumberToWordApplication {
    private final static Logger LOGGER = Logger.getLogger(PhoneNumberToWordApplication.class.getName());

    /**
     * The set of exit codes that will be returned to the JVM on exit
     */
    private final static int EXIT_CODE_OK = 0;
    private final static int EXIT_CODE_NO_DICTIONARY_LOADED = 1;
    private final static int EXIT_CODE_INPUT_PHONE_FILE_NOT_LOADED = 2;

    /**
     * Starts a new processing run with the given input arguments
     * @param args - the application input arguments. See {@link InputArgumentReader} for more info
     * @return - the exit code to return to the JVM
     */
    int run(String[] args) {
        LOGGER.info("The phone number to word application has started");
        int exitCode;

        InputArgumentReader inputArgumentReader = new InputArgumentReader(args);
        // Print welcome info using the parsed input
        printWelcomeMessage(inputArgumentReader.getArgumentUsageInfo(), inputArgumentReader.getArgumentValues());
        // Determine what dictionary we are to use
        Dictionary dictionaryToUse = loadDictionary(inputArgumentReader);

        if ( dictionaryToUse != null) {
            exitCode = onDictionaryLoaded(inputArgumentReader, dictionaryToUse);
        } else {
            exitCode = EXIT_CODE_NO_DICTIONARY_LOADED;
        }
        printExitMessage();
        LOGGER.log(Level.INFO, "The phone number to word application is exiting with exit code {0}", exitCode);
        return exitCode;
    }

    /**
     * This method will return the dictionary to use within the application. If a file is provided as input,
     * that dictionary is used, otherwise the default is used.
     * @param inputArgumentReader - the current arguments
     * @return - the dictionary to use (or null if none can be found)
     */
    private Dictionary loadDictionary(InputArgumentReader inputArgumentReader) {
        // Check if we were given a dictionary file
        Optional<String> inputDictionaryFileOptional = inputArgumentReader.getDictionaryFile();
        if ( inputDictionaryFileOptional.isPresent()) {
            String inputDictionaryFile = inputDictionaryFileOptional.get();
            // Use the factory to load the dictionary
            Optional<Dictionary> inputDictionaryOptional = DictionaryFactory.fromFile(inputDictionaryFile);
            if ( inputDictionaryOptional.isPresent()) {
                return inputDictionaryOptional.get();
            } else {
                printError("Could not load input dictionary from file ["+inputDictionaryFile+"]");
                return null;
            }
        } else {
            return DictionaryFactory.getDefault(); // get the default application dictionary
        }
    }

    private int onDictionaryLoaded(InputArgumentReader inputArgumentReader, Dictionary dictionaryToUse) {

        Converter<Long, String> phoneNumberToWordConverter = ConverterFactory.phoneNumberToWords(dictionaryToUse);
        InputProcessor inputProcessor = new InputProcessor(phoneNumberToWordConverter);

        Optional<String> phoneNumbersFileOptional = inputArgumentReader.getPhoneNumbersFile();
        if ( phoneNumbersFileOptional.isPresent()) {
            String phoneNumbersFile = phoneNumbersFileOptional.get();
            Optional<Set<Long>> readPhoneNumbersOptional = FileReaderFactory.longsOnlyLineReader().readFile(phoneNumbersFile);
            if ( readPhoneNumbersOptional.isPresent()) {
                inputProcessor.processBatch(readPhoneNumbersOptional.get());
            } else {
                return EXIT_CODE_INPUT_PHONE_FILE_NOT_LOADED;
            }
        } else {
            inputProcessor.processStdIn();
        }
        return EXIT_CODE_OK;
    }

    /**
     * Prints the welcome information to the user on screen, plus the argument usage and values received.
     */
    private void printWelcomeMessage(List<String> argumentInfo, List<String> argumentValues) {
        printInfo(System.lineSeparator()+"Welcome to the Phone Number to Word Application!");
        printInfo(System.lineSeparator()+"The following are the input options:");
        argumentInfo.forEach( info -> printInfo("  "+info));
        // Print out any argument values gotten
        printInfo(System.lineSeparator()+"The following were the parsed input values:");
        argumentValues.forEach(info -> printInfo("  "+info));
        printInfo(System.lineSeparator());
    }

    private void printExitMessage() {
        printInfo(System.lineSeparator()+"Thank you for using this Phone Number to Word Application - Goodbye! :-)");
        printInfo("");
    }

    /**
     * The JVM entry point into the application. This method sets up the logging and then calls {{@link #run(String[])}}
     */
    public static void main(String[] args) {
        setupSimpleLogging();
        int exitCode = new PhoneNumberToWordApplication().run(args);
        System.exit(exitCode);
    }

    /**
     * This method sets up simple logging for this application
     */
    private static void setupSimpleLogging() {
        try {
            Logger applicationRootLogger = Logger.getLogger("com.mark");
            // Create a file handler - set to 10mb files at 5 max to keep
            FileHandler fileHandler = new FileHandler("phone-number-to-word-converter.log", 10000000, 5, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            applicationRootLogger.addHandler(fileHandler);
            // don't send logs up the chain (we don't want logs appearing onscreen for example)
            applicationRootLogger.setUseParentHandlers(false);
        } catch (Exception e ) {
            // This isn't great, but we'll decide to let the program continue
            printWarning("There was a problem loading the log file, the application will continue but without logging. " +
                    "Error: "+e.getMessage());
        }
    }

}
