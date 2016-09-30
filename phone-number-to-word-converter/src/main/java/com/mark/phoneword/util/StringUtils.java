package com.mark.phoneword.util;

import java.util.Optional;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class StringUtils {

    private final static String LETTERS_ONLY_REGEX = "[^a-zA-Z]+";
    private final static String NUMBERS_ONLY_REGEX = "[^0-9]+";

    public static boolean isBlank(Character character) {
        return character == null || character == ' ';
    }

    public static boolean isNotBlank(Character character) {
        return !isBlank(character);
    }

    public static boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    public static boolean areDigits(Character one, Character two) {
        return one != null && two != null && Character.isDigit(one) && Character.isDigit(two);
    }

    public static boolean isDigit(Character character) {
        return character != null && Character.isDigit(character);
    }

    public static Optional<Long> tryConvert(String string) {
        String longOnly = getNumbersOnly(string);
        return NumberUtils.tryConvert(longOnly);
    }

    public static String getLettersOnly(String string) {
        return doRegex(string, LETTERS_ONLY_REGEX);
    }

    public static String getNumbersOnly(String string) {
        return doRegex(string, NUMBERS_ONLY_REGEX);
    }

    private static String doRegex(String string, String regex) {
        if ( string != null ) {
            return string.replaceAll(regex, "");
        }
        return null;
    }
}
