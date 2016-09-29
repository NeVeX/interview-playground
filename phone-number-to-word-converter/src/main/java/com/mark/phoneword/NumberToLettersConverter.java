package com.mark.phoneword;

import com.mark.phoneword.util.NumberUtils;
import com.mark.phoneword.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
class NumberToLettersConverter {

    private final Map<Integer, List<Character>> digitToLetters = new HashMap<>(); // can be a list, the input is a set and we need ordering

    /**
     * Initialize a new number to letters converter that will use the provided mapping table
     * <br>Note, only single digits are supported (0 - 9)
     * <br>To control which digits can be converted, only include those in the provided map
     * @param digitToLetters - the map to use for all conversions
     */
    NumberToLettersConverter(Map<Integer, Set<Character>> digitToLetters) {
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
     * One digit numbers are not converted (e.g. 2 will not return anything since a one letter character is not a word)
     * @param digits
     * @return
     */
    private Set<String> calculateLetterPermutationsForDigits(List<Integer> digits) {

        // If the digit cannot be found, then just add the digit as the character key
        List<List<Character>> listOfLetterSets = digits.stream()
        .map(digit -> {
            if (digitToLetters.containsKey(digit)) {
                return digitToLetters.get(digit);
            } else {
                // If the digit cannot be found, then just add the digit as the character key
                return Collections.singletonList(digit.toString().charAt(0));
            }})
        .collect(Collectors.toList());

        Set<String> letterCombinations = new HashSet<>();

        if ( listOfLetterSets.isEmpty()) {
            return letterCombinations;
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
                    String letterCombination = prefix + cr;
                    // Check if this new letter combination is acceptable (e.g. cannot have 2 digits; abc45, ter3sd34)
                    if ( !isNewLetterCombinationAcceptable(letterCombination)) {
                        letterCombinations.add(letterCombination);
                        newRowOfStrings.add(letterCombination);
                    }
                }));
        }
        return letterCombinations;
    }

    /**
     * Given a new letter combination that will be added to resulting permutation set, this method decides if it can be added.
     * @param letters
     * @return
     */
    private boolean isNewLetterCombinationAcceptable(String letters) {

        if ( StringUtils.isNotBlank(letters)) {
            int length = letters.length();
            if ( length >= 2) {
                return StringUtils.areDigits(letters.charAt(length-2), letters.charAt(length-1));
            }
            return true;
        }
        return false;
    }

    /**
     * Determine if the input number to letters is a valid entry
     * @param entry - the entry to check (null key, empty sets...)
     * @return - True if this entry is good, false otherwise
     */
    private boolean isMapEntryValid(Map.Entry<Integer, Set<Character>> entry) {
        Integer key = entry.getKey();
        Set<Character> letters = entry.getValue();
        return key != null && letters != null && !letters.isEmpty()
                // Check also that all characters are not blank
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
