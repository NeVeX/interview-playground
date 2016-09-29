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
            List<List<Character>> matrixOfLetters = getMatrixOfLettersForDigits(splitDigits);
            return calculateAllLetterPermutations(matrixOfLetters);
        }
        return new HashSet<>();

    }

    private List<List<Character>> getMatrixOfLettersForDigits(List<Integer> digits) {
        // If the digit cannot be found, then just add the digit as the character key
        return digits.stream()
                .map(digit -> {
                    if (digitToLetters.containsKey(digit)) {
                        return digitToLetters.get(digit);
                    } else {
                        // If the digit cannot be found, then just add the digit as the character key
                        return Collections.singletonList(digit.toString().charAt(0));
                    }})
                .collect(Collectors.toList());
    }

    /**
     * One digit numbers are not converted (e.g. 2 will not return anything since a one letter character is not a word)
     * @param matrixOfLetters
     * @return
     */
    private Set<String> calculateAllLetterPermutations(List<List<Character>> matrixOfLetters) {

        List<List<String>> rowStrings = new ArrayList<>();

        if ( !matrixOfLetters.isEmpty()) {

            List<String> firstLine = matrixOfLetters.get(0)
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());

            rowStrings.add(firstLine);

            for (int row = 1; row < matrixOfLetters.size(); row++) {
                List<Character> currentRow = matrixOfLetters.get(row);
                List<String> newRowStrings = rowStrings
                        .get(row - 1)
                        .stream()
                        .map(prefix -> appendPrefixToLetters(prefix, currentRow))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

                rowStrings.add(newRowStrings);
            }
        }
        // Don't include the first row of strings in the result, since they are single char's (e.g. "a", "h" - not words)
        return rowStrings.stream().skip(1).flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
    }

    private List<String> appendPrefixToLetters(final String prefix, final List<Character> letters) {
        return letters.stream()
            .map( letter -> prefix + letter )
            .filter(this::isNewLetterCombinationAcceptable)
            .collect(Collectors.toList());
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
                return !StringUtils.areDigits(letters.charAt(length-2), letters.charAt(length-1));
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
