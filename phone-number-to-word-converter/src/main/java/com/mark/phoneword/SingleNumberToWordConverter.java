package com.mark.phoneword;

import com.mark.phoneword.util.NumberUtils;
import com.mark.phoneword.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
class SingleNumberToWordConverter {

    private static final int MINIMUM_ALLOWED_DIGIT = 0;
    private static final int MAXIMUM_ALLOWED_DIGIT = 9;
    private final Map<Integer, List<Character>> digitToLetters = new HashMap<>(); // can be a list, the input is a set and we need ordering

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
        digitToLetters.entrySet()
            .stream()
            .filter(this::isMapEntryValid)
            .forEach(this::addNewNumberToLetterEntry);
    }

    Set<String> convert(int number) {

        List<Integer> splitDigits = NumberUtils.splitToList(number);
        if ( !splitDigits.isEmpty()) {
            return calculateLetterPermutationsForDigits(splitDigits);
        }
        return new HashSet<>();

    }

    private Set<String> calculateLetterPermutationsForDigits(List<Integer> digits) {

        List<List<Character>> listOfLetterSets = new ArrayList<>();
        for (Integer digit : digits) {
            if ( digit >= MINIMUM_ALLOWED_DIGIT && digit <= MAXIMUM_ALLOWED_DIGIT) {
                if (digitToLetters.containsKey(digit)) {
                    listOfLetterSets.add(digitToLetters.get(digit));
                }
            }
        }

        Set<String> returnWords = new HashSet<>();

        if ( listOfLetterSets.size() == 1 ) {
            listOfLetterSets.get(0).forEach(c -> returnWords.add(c.toString()));
            return returnWords;
        }

        if ( !listOfLetterSets.isEmpty() ) {

            int endLoopIndex = listOfLetterSets.size() - 1;
            int indexSize = endLoopIndex;

            StringBuilder runningSb = new StringBuilder();

            int index = 0;
            int startRowIndex = -1;
            boolean movingForward = true;
            while ( index >= 0 ) {

                if (index == endLoopIndex) {
                    String prefix = runningSb.substring(0, index);
                    for ( int j = 0;  j < listOfLetterSets.get(index).size(); j++) {

                        String word = prefix + listOfLetterSets.get(index).get(j);

                        System.err.println(word);
                        returnWords.add(word);
                    }
                    movingForward = false;
                    endLoopIndex--;
                }

//                if ( movingForward) {
                    if (index == 0) {
                        if (++startRowIndex >= listOfLetterSets.get(index).size()) {
                            break;
                        }
                        runningSb = new StringBuilder();
                        endLoopIndex = indexSize;
//                        startRowIndex++;
                        movingForward = true;
                    }
                    if (movingForward) {
                        Character character = listOfLetterSets.get(index).get(startRowIndex);
                        runningSb.append(character);
                        System.err.println("trail: "+runningSb.toString());
                    }
//                }

                index += movingForward ? 1 : -1;
            }

        }


        return returnWords;
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
        List<Character> letters = new ArrayList<>();
        letters.addAll(entryToAdd.getValue());
        digitToLetters.put(entryToAdd.getKey(), letters);
    }
}
