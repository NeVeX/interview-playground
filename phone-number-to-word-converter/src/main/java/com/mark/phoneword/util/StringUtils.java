package com.mark.phoneword.util;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class StringUtils {

    private final static String LETTERS_ONLY_REGEX = "[^a-zA-Z ]";

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

    public static String getLettersOnly(String string) {
        if ( string != null ) {
            return string.replaceAll(LETTERS_ONLY_REGEX, "");
        }
        return null;
    }

}
