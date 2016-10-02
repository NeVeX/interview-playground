package com.mark.phoneword.convert;

import com.mark.phoneword.dictionary.Dictionary;
import com.mark.phoneword.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Created by Mark Cunningham on 9/27/2016.
 * <br>This class supports conversion of Longs into Words.
 * <br>The words are determined to be valid based on the dictionary provided.
 */
class PhoneNumberToWordConverter implements Converter<Long, String> {

    private final static String WORD_SPLITTER = "-";
    private final NumberToLettersConverter longToLetterConverter;
    private final Dictionary dictionary;

    /**
     * Create a new instance of the word converter
     * @param longToLetterConverter - The converter that supports Long to letter combinations
     * @param dictionary - The dictionary to use for word lookups
     */
    PhoneNumberToWordConverter(NumberToLettersConverter longToLetterConverter, Dictionary dictionary) {
        if ( longToLetterConverter == null ) {
            throw new IllegalArgumentException("Provided longToLetterConverter cannot be null");
        }
        if (dictionary == null) {
            throw new IllegalArgumentException("Provided dictionary cannot be null");
        }
        this.longToLetterConverter = longToLetterConverter;
        this.dictionary = dictionary;
    }

    /**
     * Given a valid number, this method will convert it to a valid word combination.
     * <br>There are some rules that determine what is a valid converted word.
     *  1 - Single digits are allowed in the converted word
     *  2 - Words and digits will be separated by a dash "-" (e.g. 1-MARK-2-CALL)
     * <br>The given number is converted to that letter combinations using the provided Letter converter in this instance
     * @param number - The number to convert
     * @return - The list of converted/valid words (with digits if applicable)
     */
    public Set<String> convert(Long number) {
        // Get the number into it's various permutations form
        Set<String> letterCombinations = longToLetterConverter.convert(number);
        if ( !letterCombinations.isEmpty() ) {
            // We have combinations, so parse each combination to determine if words/digits can be extracted
            return letterCombinations
                    .parallelStream()
                    .map(this::getWordSplits) // For each word, split the word up and check if it's a word also
                    .flatMap(Collection::parallelStream)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    /**
     * For the given letter combination, this method will split each word using {{@link #getWordSplitsUsingIndex(int, String)}}
     * @param letterCombination - The letter combination to check for words
     * @return - The set of words that are valid
     */
    private Set<String> getWordSplits(final String letterCombination) {

        // Split this letterCombination up
        // E.g. mart -> m, art | ma, rt | mar, t
        Set<String> wordSplits = IntStream.range(1, letterCombination.length())
                .parallel()
                .mapToObj( index -> getWordSplitsUsingIndex(index, letterCombination) )
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toSet());
        if ( dictionary.isWord(letterCombination)) {
            // This letter combination is itself a word, so simply add it
            wordSplits.add(createNewWord(letterCombination));
        }
        return wordSplits;
    }

    /**
     * This method will split the letter combination up, using the index as the splitter.
     * <br>Once the letter combination is split up, each split combination is then checked if it's a word, and if so,
     * it in turn is also split up - this method can recursively call on other split methods too.
     *
     * For example, given index 4 and word callmark:
     *  - the string "mark" is split into "call" and "mark"
     *  - since "call" is a word, it will then split up the "mark" using recursion
     *  -- since "mark" is also a word, the a new word is created "call-mark" and returned as a valid word
     *
     * @param index - the index to split the letter combination up
     * @param letterCombination - the string to work with
     * @return - the list of valid word permutations found
     */
    private Set<String> getWordSplitsUsingIndex(int index, String letterCombination) {
        // Split the combination up
        String firstSplitLetters = letterCombination.substring(0, index);
        String secondSplitLetters = letterCombination.substring(index, letterCombination.length());

        boolean firstComboIsADigit = firstSplitLetters.length() == 1 && StringUtils.isDigit(firstSplitLetters.charAt(0));
        boolean secondComboIsADigit = secondSplitLetters.length() == 1 && StringUtils.isDigit(secondSplitLetters.charAt(0));
        boolean secondComboHasFirstDigit = secondSplitLetters.length() > 1 && StringUtils.isDigit(secondSplitLetters.charAt(0));
        boolean isFirstComboAWord = dictionary.isWord(firstSplitLetters);

        boolean continueProcessing = false; // this determines if we should continue processing the letter combination
        Set<String> validWords = new HashSet<>();

        if ( isFirstComboAWord && secondComboIsADigit) {
            // this is the end of the combination, so add it
            validWords.add(createNewWord(firstSplitLetters, secondSplitLetters));
        } else if ( isFirstComboAWord && secondComboHasFirstDigit) {
            // We have a valid first word and the second combo has a digit as it's first char, so we should still
            // split the second word up
            // E.g. first = "mark", second = "1call", - we should still parse out "mark-1-call"
            continueProcessing = true;
        } else if ( isFirstComboAWord || firstComboIsADigit) {
            // Allow processing to continue when the first combo is also a digit, e.g. '1', but not 'a1'
            continueProcessing = true;
        }

        if ( continueProcessing ) {
            validWords.addAll(
                getWordSplits(secondSplitLetters) // continue to split the words up
                    .parallelStream()
                    .map(s -> createNewWord(firstSplitLetters, s)) // for each valid split word, create the new word
                    .collect(Collectors.toSet()));
        }
        return validWords;
    }

    /**
     * Returns the word with rules applied to it, currently made as uppercase
     * @param word - the word to apply rules to
     * @return - the new word
     */
    private String createNewWord(String word) {
        return word.toUpperCase();
    }

    /**
     * Given two strings, this returns the new word with them both merged (using the word splitter "-")
     */
    private String createNewWord(String partOne, String partTwo) {
        return createNewWord(partOne + WORD_SPLITTER + partTwo);
    }
}
