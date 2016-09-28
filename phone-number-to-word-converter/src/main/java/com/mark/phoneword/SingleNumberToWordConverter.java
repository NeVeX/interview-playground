package com.mark.phoneword;

import com.mark.phoneword.util.NumberUtils;
import com.mark.phoneword.util.StringUtils;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
class SingleNumberToWordConverter {

    private static final int MINIMUM_ALLOWED_DIGIT = 0;
    private static final int MAXIMUM_ALLOWED_DIGIT = 0;
    private final Map<Integer, Set<Character>> numberToLetters;

    /**
     * Initialize a new number to word converter that will use the provided mapping table
     * <br>Note, only single digits are supported (0 - 9)
     * <br>To control which digits can be converted, only include those in the provided map
     * @param digitToLetters - the map to use for all conversions
     */
    SingleNumberToWordConverter(Map<Integer, Set<Character>> digitToLetters) {
        if (digitToLetters == null || digitToLetters.isEmpty()) {
            throw new IllegalArgumentException("Provided digitToLetters cannot be null or empty");
        }
        // Use the input and create our own internal reference to the data
        Map<Integer, Set<Character>> newNumberToLetters = new HashMap<>();
        newNumberToLetters.entrySet()
            .stream()
            .filter(this::isMapEntryValid)
            .forEach(this::addNewNumberToLetterEntry);
        this.numberToLetters = newNumberToLetters;
    }

    Set<String> convert(int number) {

        List<Integer> splitDigits = NumberUtils.splitToList(number);
        if ( !splitDigits.isEmpty()) {

        }
        return new HashSet<>();

    }

    /**
     * Determine if the input number to letters is a valid entry
     * @param entry - the entry to check (null key, empty sets...)
     * @return - True if this entry is good, false otherwise
     */
    private boolean isMapEntryValid(Map.Entry<Integer, Set<Character>> entry) {
        Integer key = entry.getKey();
        Set<Character> letters = entry.getValue();
        return key != null && key >= MINIMUM_ALLOWED_DIGIT && key <= MAXIMUM_ALLOWED_DIGIT
            && letters != null && !letters.isEmpty()
                // Check also that
                && letters.stream().filter(StringUtils::isNotBlank).findAny().isPresent();
    }

    /**
     * Add the given entry to the class map
     * @param entryToAdd - the (now valid) entry
     */
    private void addNewNumberToLetterEntry(Map.Entry<Integer, Set<Character>> entryToAdd) {
        Set<Character> letters = new HashSet<>();
        letters.addAll(entryToAdd.getValue());
        numberToLetters.put(entryToAdd.getKey(), letters);
    }
}
