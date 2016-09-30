package com.mark.phoneword.input;

import com.mark.phoneword.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public class InputReader {
    private final static String ARGUMENT_OPERATOR = "=";
    private final static String DICTIONARY_FILE_ARG = "-f";
    /**
     * Given the application input arguments, this method will return the input file location given, if found.
     * <br>The input file should be given under the "-f" argument - e.g. -f=/usr/mark/english.dictionary
     * @param args - the input arguments from the application
     * @return - An optional that will/will not, contain the value for the dictionary file
     */
    public Optional<String> getDictionaryFile(String[] args) {
        return Optional.ofNullable(getValueForArgument(DICTIONARY_FILE_ARG, args));
    }

    private String getValueForArgument(String argument, String[] args) {
        String argumentLowerCase = argument.trim().toLowerCase();
        if ( args != null && args.length > 0) {
            Optional<String> foundValueOpt = Arrays.stream(args)
                    .filter(StringUtils::isNotBlank)
                    .map(s -> s.trim().toLowerCase())
                    .filter(s -> {
                            String argumentWithOperator = argumentLowerCase + ARGUMENT_OPERATOR;
                            return s.length() > argumentWithOperator.length()
                                && s.substring(0, argumentWithOperator.length()).equals(argumentWithOperator);
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


}
