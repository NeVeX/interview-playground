package com.mark.phoneword.convert;

import com.mark.phoneword.util.NumberUtils;
import com.mark.phoneword.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 9/27/2016.
 * <br>Converter that supports converting Long numbers to Strings, using a table of reference for mappings.
 * <br>Note, how numbers are converted, is decided with the given Number to Letter conversion map
 */
class NumberToLettersConverter implements Converter<Long, String> {

    private final Map<Byte, Set<Character>> digitToLetters;

    /**
     * Creates a new instance with the given number to letter conversion map
     * @param digitToLetters - The non null/empty conversion map
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



    /**
     * Converts the given number into a Set of letter combinations using this instance's number to letter conversion map.
     * <br> Note, that after the conversion, if there are any conversions with consecutive digits, then those conversions
     * are discarded, i.e. abc4ter is ok, but abc45ter is not.
     * <br>Example, given:
     *  - input = 25
     *  - number to letter mapping table : 2 = {a, b} and 5 = { g, h}
     * then an example of output will be
     *  - output = {ag, ah, bg, bh}
     *
     * @param number - The number to convert
     * @return - The set of letter combinations for the number given, or empty if no conversion were found
     */
    public Set<String> convert(Long number) {
        List<Byte> splitDigits = NumberUtils.splitToList(number);
        if ( !splitDigits.isEmpty()) {
            List<Set<Character>> matrixOfLetters = getMatrixOfLettersForDigits(splitDigits);
            return calculateAllLetterPermutations(matrixOfLetters);
        }
        return new HashSet<>();
    }

    /**
     * For the given number (split by digits), this returns the matrix to apply for conversions.
     * <br>Example, given:
     *  - input {1, 2, 3}
     *  - conversion map: 1 = {a, b}, 2 = { j, k } 4 = { p } (note, there is no 0 or 3 )
     * Then the output is
     *  - 0 = { 0 }
     *    1 = { a, b }
     *    2 = { j, k}
     *    3 = { 3 }
     *    4 = { p }
     *    ... and so on
     * @param digits - The list of individual digits that make up a number
     * @return - The conversion matrix to use for the given number
     */
    private List<Set<Character>> getMatrixOfLettersForDigits(List<Byte> digits) {
        // For each digit find and map the the letter combinations for that digit
        return digits
            .stream()
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
     * For the given matrix of number to letter combinations, this method will find all full length letter combinations.
     * <br>The approach of this algorithm, is to identify the starting letters possible. Then for each starting letter,
     * a string is built up from start of the matrix to the end (length of the number).
     * For example: Given
     *  - input: 1 = { a, b }, 2 = { d, e}, 3 = { 3 }
     *  - The algorithm will set { a, b } as the start letters, then traverse downwards keeping a running string for each
     *      First level (a, b) :         a, b
     *      Second Level (d, e):    ad, ae, bd, be
     *      Third Level (3)    :   ad3, ae3, bd3, be3 --> final level, hence these are all the combinations
     * Then the output would look like this:
     *  - output = { "ad3", "ae3", "bd3", "be3" }
     * @param matrixOfLetters - The matrix of digit to letter combination map
     * @return - The set of all combinations found, or empty if none found
     */
    private Set<String> calculateAllLetterPermutations(List<Set<Character>> matrixOfLetters) {

        Set<String> fullLengthLetters = new HashSet<>();

        if ( !matrixOfLetters.isEmpty()) {
            // Define the current matrix of letter combinations
            List<Set<String>> matrixLevelOfLetters = new ArrayList<>();
            // Add the first line, since this will always be the starting point
            matrixLevelOfLetters.add(matrixOfLetters.get(0)
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet()));

            // Not using an IntStream due to how we need to traverse the lists below
            for (int row = 1; row < matrixOfLetters.size(); row++) {
                // Get the current row
                Set<Character> currentRow = matrixOfLetters.get(row);
                // Then for each character, prefix the previous row string to it (checking if the new combo is valid)
                Set<String> newRowStrings = matrixLevelOfLetters
                        .get(row - 1)
                        .stream()
                        .map(prefix -> appendNewLetterToPrefix(prefix, currentRow))
                        .flatMap(Collection::parallelStream)
                        .collect(Collectors.toSet());
                // If we are the last row, then simply add all the combinations
                if ( row == matrixOfLetters.size() -1 ) {
                    fullLengthLetters.addAll(newRowStrings);
                } else {
                    // We are not the last row, so add the current row to the matrix of levels
                    matrixLevelOfLetters.add(newRowStrings);
                }
            }
        }
        // Don't include the first row of strings in the result, since they are single char's (e.g. "a", "h" - not words)
        return fullLengthLetters;
    }

    /**
     * For the given prefix, append the letter for each in the set it, to it.
     * <br> For example, if the prefix is "abc" and the letters given are "t,e", then the output will be
     *  - "abct" and "abce"
     * @param prefix - The string to prefix to each letter
     * @param letters - The set of letters to append to the prefix above
     * @return - The list of strings that have being merged
     */
    private Set<String> appendNewLetterToPrefix(final String prefix, final Set<Character> letters) {
        return letters
            .parallelStream()
            // For this new combination, we need to be sure it's valid
            .filter( newLetter -> isNewLetterCombinationValid(prefix, newLetter))
            // Anything that passes the above test, we'll simple merge below
            .map( letter -> prefix + letter )
            .collect(Collectors.toSet());
    }

    /**
     * Given a prefix and a postfix character, this method determines if it is ok.
     * <br> The current rule is that no 2 digits may be consecutive
     * <br>Example, given "ab" and char '4', the output would be "ab4" which is valid.
     * However, for "ab3" and char '4', the output would be "ab34", which is not valid
     * @param prefix - The prefix to use
     * @param postFix - The postfix to use
     * @return - True if this merge is valid, False otherwise
     */
    private boolean isNewLetterCombinationValid(String prefix, Character postFix) {
        // Apply the rules for the letter combinations
        // There cannot be consecutive digits, i.e. abc5 + e is ok, but abc5 + 7 is not since it will mean 57 are together
        return !StringUtils.areDigits(prefix.charAt(prefix.length()-1), postFix);
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
