package com.mark.phoneword.util;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class StringUtils {

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

}
