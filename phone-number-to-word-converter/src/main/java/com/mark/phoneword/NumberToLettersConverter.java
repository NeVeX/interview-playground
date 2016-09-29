package com.mark.phoneword;

import com.mark.phoneword.util.NumberUtils;
import com.mark.phoneword.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
class NumberToLettersConverter {

    private final Map<Byte, Set<Character>> digitToLetters;

    /**
     * Initialize a new number to letters converter that will use the provided mapping table
     * <br>Note, only single digits are supported (0 - 9)
     * <br>To control which digits can be converted, only include those in the provided map
     * @param digitToLetters - the map to use for all conversions
     */
    NumberToLettersConverter(Map<Byte, Set<Character>> digitToLetters) {
        if (digitToLetters == null || digitToLetters.isEmpty()) {
            throw new IllegalArgumentException("Provided digitToLetters cannot be null or empty");
        }
        // Use the input and create our own internal reference to the data
        this.digitToLetters = digitToLetters.entrySet()
            .stream()
            .filter(this::isMapEntryValid)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k,v) -> Collections.unmodifiableSet(v)));
    }

    Set<String> convert(long number) {

        List<Byte> splitDigits = NumberUtils.splitToList(number);
        if ( !splitDigits.isEmpty()) {
            List<Set<Character>> matrixOfLetters = getMatrixOfLettersForDigits(splitDigits);

            return calculateAllLetterPermutations(matrixOfLetters)
                    .stream()
                    .map(this::splitLetterCombinationsIntoChunks)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

        }
        return new HashSet<>();

    }



    private List<Set<Character>> getMatrixOfLettersForDigits(List<Byte> digits) {
        // If the digit cannot be found, then just add the digit as the character key
        return digits.stream()
                .map(digit -> {
                    if (digitToLetters.containsKey(digit)) {
                       return digitToLetters.get(digit);
                    } else {
                        // If the digit cannot be found, then just add the digit as the character key
                        Set<Character> numberSet = new HashSet<>();
                        numberSet.add(digit.toString().charAt(0));
                        return numberSet;
                    }})
                .collect(Collectors.toList());
    }

    /**
     * One digit numbers are not converted (e.g. 2 will not return anything since a one letter character is not a word)
     * @param matrixOfLetters
     * @return
     */
    private Set<String> calculateAllLetterPermutations(List<Set<Character>> matrixOfLetters) {

        Set<String> fullLengthLetters = new HashSet<>();

        if ( !matrixOfLetters.isEmpty()) {

            List<Set<String>> levelToLetters = new ArrayList<>();
            // Add the first line
            levelToLetters.add(matrixOfLetters.get(0)
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet()));

            for (int row = 1; row < matrixOfLetters.size(); row++) {
                Set<Character> currentRow = matrixOfLetters.get(row);
                Set<String> newRowStrings = levelToLetters
                        .get(row - 1)
                        .stream()
                        .map(prefix -> appendPrefixToLetters(prefix, currentRow))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
                if ( row == matrixOfLetters.size() -1 ) {
                    fullLengthLetters.addAll(newRowStrings);
                } else {
                    levelToLetters.add(newRowStrings);
                }
            }
        }
        // Don't include the first row of strings in the result, since they are single char's (e.g. "a", "h" - not words)
        return fullLengthLetters; //fullLengthLetters.stream().flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
    }

    private Set<String> splitLetterCombinationsIntoChunks(final String letterCombination) {
        // Split this letterCombination up
        // E.g. mart -> m, art | ma, rt | mar, t
        Set<String> combinations = IntStream.range(1, letterCombination.length()) // We don't want one character words
                .mapToObj( index -> {
                    String firstCombo = letterCombination.substring(0, index);
                    String secondCombo = letterCombination.substring(index, letterCombination.length());
                    return Stream.of(firstCombo, secondCombo).collect(Collectors.toSet());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        combinations.add(letterCombination);
        return combinations;
    }

    private List<String> appendPrefixToLetters(final String prefix, final Set<Character> letters) {
        return letters.stream()
            .map( letter -> prefix + letter )
            .collect(Collectors.toList());
    }

    /**
     * Determine if the input number to letters is a valid entry
     * @param entry - the entry to check (null key, empty sets...)
     * @return - True if this entry is good, false otherwise
     */
    private boolean isMapEntryValid(Map.Entry<Byte, Set<Character>> entry) {
        Byte key = entry.getKey();
        Set<Character> letters = entry.getValue();
        return key != null && letters != null && !letters.isEmpty()
                // Check also that all characters are not blank
                && letters.stream().filter(StringUtils::isNotBlank).findAny().isPresent();
    }

}
