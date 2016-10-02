package com.mark.phoneword.util;

import java.util.Optional;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class StringUtils {

    private final static String LETTERS_ONLY_REGEX = "[^a-zA-Z]+";
    private final static String NUMBERS_ONLY_REGEX = "[^0-9]+";

    /**
     * Checks if the given character is null or blank
     */
    public static boolean isBlank(Character character) {
        return character == null || character == ' ';
    }

    /**
     * Checks if the given character is not null and blank
     */
    public static boolean isNotBlank(Character character) {
        return !isBlank(character);
    }

    /**
     * Checks if the given string is null or blank
     */
    public static boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * Checks if the given string is not null and blank
     */
    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    /**
     * Checks if both characters are digits
     */
    public static boolean areDigits(Character one, Character two) {
        return isDigit(one) && isDigit(two);
    }

    /**
     * Checks if the given character is a digit
     */
    public static boolean isDigit(Character character) {
        return character != null && Character.isDigit(character);
    }

    /**
     * Tries to convert the given String (after stripping all non digits from the string) into an optional
     */
    public static Optional<Long> tryConvertAfterRemovingInvalidChars(String string) {
        String longOnly = getNumbersOnly(string);
        return NumberUtils.tryConvert(longOnly);
    }

    /**
     * For the given string, return the string with only letters
     */
    public static String getLettersOnly(String string) {
        return doRegex(string, LETTERS_ONLY_REGEX);
    }

    /**
     * For the given string, return the string with only numbers
     */
    public static String getNumbersOnly(String string) {
        return doRegex(string, NUMBERS_ONLY_REGEX);
    }

    /**
     * Returns the string after applying the given regex
     */
    private static String doRegex(String string, String regex) {
        if ( string != null ) {
            return string.replaceAll(regex, "");
        }
        return null;
    }
}
