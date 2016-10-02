package com.mark.phoneword.input;

import com.mark.phoneword.util.StringUtils;

import java.util.*;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public class InputArgumentReader {
    private final static String ARGUMENT_OPERATOR = "=";
    private final static String DICTIONARY_FILE_ARG = "-d";
    private final static String PHONE_NUMBERS_FILE_ARG = "-p";
    private final static String NO_ARGUMENT_VALUE = "**None Provided**";
    private final static List<String> ALL_ARGUMENTS_USAGE_INFO;
    private final List<String> givenArgumentValues;
    private final String dictionaryFileLocation;
    private final String phoneNumbersFileLocation;

    static {
        List<String> mutableList = new ArrayList<>();
        mutableList.add(DICTIONARY_FILE_ARG+ARGUMENT_OPERATOR+"/path/to/my.dictionary  ==> Optionally provide your own dictionary file to use for conversion");
        mutableList.add(PHONE_NUMBERS_FILE_ARG +ARGUMENT_OPERATOR+"/path/to/phone.numbers  ==> Optionally provide a file of phone numbers to convert to words");
        ALL_ARGUMENTS_USAGE_INFO = Collections.unmodifiableList(mutableList);
    }

    /**
     * Create a new instance of the input reader, passing in the non null arguments received from the user
     * @param args - the non null arguments
     */
    public InputArgumentReader(String[] args) {
        if ( args == null) {
            throw new IllegalArgumentException("Provided input arguments cannot be null");
        }
        dictionaryFileLocation = getValueForArgument(DICTIONARY_FILE_ARG, args);
        phoneNumbersFileLocation = getValueForArgument(PHONE_NUMBERS_FILE_ARG, args);
        List<String> givenArgumentValues = new ArrayList<>();
        givenArgumentValues.add("Dictionary File   ==> "+
                ( StringUtils.isBlank(dictionaryFileLocation) ? NO_ARGUMENT_VALUE : dictionaryFileLocation));
        givenArgumentValues.add("Phone Number File ==> "+
                ( StringUtils.isBlank(phoneNumbersFileLocation) ? NO_ARGUMENT_VALUE : phoneNumbersFileLocation));
        this.givenArgumentValues = Collections.unmodifiableList(givenArgumentValues);
    }

    /**
     * Returns the parsed input dictionary file location, if it was given
     * @return - The optional containing the input dictionary file, or not
     */
    public Optional<String> getDictionaryFile() {
        return Optional.ofNullable(dictionaryFileLocation);
    }

    /**
     * Returns the parsed phone numbers file location, if it was given
     * @return - The optional containing the phone numbers file, or not
     */
    public Optional<String> getPhoneNumbersFile() {
        return Optional.ofNullable(phoneNumbersFileLocation);
    }

    private String getValueForArgument(String inputArgument, String[] args) {
        if ( args != null && args.length > 0) {
            Optional<String> foundValueOpt = Arrays.stream(args)
                    .filter(StringUtils::isNotBlank)
                    .filter(validArg -> {
                        String argumentLowerCase = validArg.trim().toLowerCase();
                        String argumentToFind = inputArgument.toLowerCase() + ARGUMENT_OPERATOR;
                        return argumentLowerCase.length() > argumentToFind.length()
                            && argumentLowerCase.substring(0, argumentToFind.length()).equals(argumentToFind);
                    })
                    .findFirst();
            if ( foundValueOpt.isPresent()) {
                String[] value = foundValueOpt.get().split(ARGUMENT_OPERATOR);
                if ( value.length == 2 && StringUtils.isNotBlank(value[1])) { //Only expect 2, the argument and the argument value
                    return value[1].trim(); // Looks like we have an argument value
                }
            }
        }
        return null;
    }

    public List<String> getArgumentUsageInfo() {
        return ALL_ARGUMENTS_USAGE_INFO;
    }

    public List<String> getArgumentValues() {
        return givenArgumentValues;
    }
}
