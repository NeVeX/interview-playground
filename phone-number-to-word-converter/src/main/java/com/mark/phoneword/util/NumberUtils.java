package com.mark.phoneword.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class NumberUtils {

    /**
     * Given a positive number, this method will return in order each digit found.
     * <br>E.g. For input 582, output is {5, 8, 2}
     * @param number = The non negative number
     * @return - The in order list of each digit
     */
    public static List<Byte> splitToList(Long number) {

        LinkedList<Long> numberSplit = new LinkedList<>(); // Need to maintain order of the split
        if ( number == 0 ) {
            numberSplit.addFirst(number);
        } else {
            while (number > 0) {
                numberSplit.addFirst(number % 10);
                number = number / 10;
            }
        }
        // All of the string elements are one digit longs, so convert down to an byte type
        return numberSplit.stream().map(Long::byteValue).collect(Collectors.toList());
    }

    static Optional<Long> tryConvert(String string) {
        if ( StringUtils.isNotBlank(string)) {
            try {
                return Optional.of(Long.valueOf(string.trim()));
            } catch (NumberFormatException numberFormatException) {
                // need to log...
            }
        }
        return Optional.empty();
    }

}
