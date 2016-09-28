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

}
