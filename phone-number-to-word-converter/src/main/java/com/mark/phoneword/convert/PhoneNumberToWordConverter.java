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
 */
class PhoneNumberToWordConverter implements Converter<Long, String> {

    private final static String WORD_SPLITTER = "-";
    private final NumberToLettersConverter longToLetterConverter;
    private final Dictionary dictionary;

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

    public Set<String> convert(Long number) {
        // Get the number into it's various permutations form
        Set<String> letterCombinations = longToLetterConverter.convert(number);
        if ( !letterCombinations.isEmpty() ) {
            return letterCombinations
                    .parallelStream()
                    .map(this::getWordSplits)
                    .flatMap(Collection::parallelStream)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private Set<String> getWordSplits(final String letterCombination) {

        // Split this letterCombination up
        // E.g. mart -> m, art | ma, rt | mar, t
        Set<String> wordSplits = IntStream.range(1, letterCombination.length())
                .parallel()
                .mapToObj( index -> getWordSplitsUsingIndex(index, letterCombination) )
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toSet());
        if ( dictionary.isWord(letterCombination)) {
            wordSplits.add(createNewWord(letterCombination)); // add the full word
        }
        return wordSplits;
    }

    private Set<String> getWordSplitsUsingIndex(int index, String letterCombination) {
        String firstSplitLetters = letterCombination.substring(0, index);
        String secondSplitLetters = letterCombination.substring(index, letterCombination.length());
        Set<String> validWords = new HashSet<>();

        boolean firstComboIsADigit = firstSplitLetters.length() == 1 && StringUtils.isDigit(firstSplitLetters.charAt(0));
        boolean secondComboIsADigit = secondSplitLetters.length() == 1 && StringUtils.isDigit(secondSplitLetters.charAt(0));
        boolean secondComboHasFirstDigit = secondSplitLetters.length() > 1 && StringUtils.isDigit(secondSplitLetters.charAt(0));
        boolean isFirstComboAWord = dictionary.isWord(firstSplitLetters);

        boolean continueProcessing = false;

        if ( isFirstComboAWord && secondComboIsADigit) {
            validWords.add(createNewWord(firstSplitLetters, secondSplitLetters)); // this is the end of the combination, so add it
        } else if ( isFirstComboAWord && secondComboHasFirstDigit) {
            continueProcessing = true;
        } else if ( isFirstComboAWord || firstComboIsADigit) {
            continueProcessing = true;
        }

        if ( continueProcessing ) {
            validWords.addAll(
                    getWordSplits(secondSplitLetters)
                            .parallelStream()
                            .map(s -> createNewWord(firstSplitLetters, s))
                            .collect(Collectors.toSet()));
        }
        return validWords;
    }

    private String createNewWord(String word) {
        return word.toUpperCase();
    }

    private String createNewWord(String partOne, String partTwo) {
        return createNewWord(partOne + WORD_SPLITTER + partTwo);
    }
}
