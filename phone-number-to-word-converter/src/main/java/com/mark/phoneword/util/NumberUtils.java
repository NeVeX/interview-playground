package com.mark.phoneword.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

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
    public static List<Integer> splitToList(int number) {

        LinkedList<Integer> numberSplit = new LinkedList<>(); // Need to maintain order of the split
        if ( number == 0 ) {
            numberSplit.addFirst(number);
        } else {
            while (number > 0) {
                numberSplit.addFirst(number % 10);
                number = number / 10;
            }
        }
        return numberSplit;
    }

}
