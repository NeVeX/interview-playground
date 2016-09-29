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

    /**
     * One digit numbers are not converted (e.g. 2 will not return anything since a one word character is not a word???)
     * @param digits
     * @return
     */
    private Set<String> calculateLetterPermutationsForDigits(List<Integer> digits) {

        // If the digit cannot be found, then just add the digit as the character key
        List<List<Character>> listOfLetterSets = digits.stream()
        .filter(digit -> digit >= MINIMUM_ALLOWED_DIGIT && digit <= MAXIMUM_ALLOWED_DIGIT)
        .map(digit -> {
            if (digitToLetters.containsKey(digit)) {
                return digitToLetters.get(digit);
            } else {
                // If the digit cannot be found, then just add the digit as the character key
                return Collections.singletonList(digit.toString().charAt(0));
            }})
        .collect(Collectors.toList());

        Set<String> words = new HashSet<>();

        if ( listOfLetterSets.isEmpty()) {
            return words;
        }

        List<List<String>> rowStrings = new ArrayList<>();
        List<String> firstLine = new ArrayList<>();
        listOfLetterSets.get(0).forEach(c -> firstLine.add(c.toString()));
        rowStrings.add(firstLine);

        for ( int row = 1; row < listOfLetterSets.size(); row++ ) {
            List<Character> currentRow = listOfLetterSets.get(row);

            List<String> newRowOfStrings = new ArrayList<>();
            rowStrings.add(newRowOfStrings);

            rowStrings.get(row - 1).forEach( prefix ->
                    currentRow.forEach(cr -> {
                                String newWord = prefix + cr;
                                // Check if this is a consecutive number
                                if ( !StringUtils.areBothCharactersNumbers(StringUtils.getLastCharacter(prefix).get(), cr)) {
                                    words.add(newWord);
                                    newRowOfStrings.add(newWord);
                                }
                            }
                    ));
        }
        return words;
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
        digitToLetters.put(entryToAdd.getKey(), Collections.unmodifiableList(letters));
    }
}
