package com.mark.phoneword.convert;

import com.mark.phoneword.dictionary.DictionaryFactory;
import com.mark.phoneword.dictionary.Dictionary;
import com.mark.phoneword.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


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
//                    .parallelStream()
                    .stream()
                    .map(this::getWordSplits)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private Set<String> getWordSplits(final String letterCombination) {

        // Split this letterCombination up
        // E.g. mart -> m, art | ma, rt | mar, t
        Set<String> wordSplits = IntStream.range(1, letterCombination.length())
//                .parallel()
                .mapToObj( index -> getWordSplitsUsingIndex(index, letterCombination) )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        if ( dictionary.isWord(letterCombination)) {
            wordSplits.add(createNewWord(letterCombination)); // add the full word
        }
        return wordSplits;
    }

    private Set<String> getWordSplitsUsingIndex(int index, String letterCombination) {
        String firstCombo = letterCombination.substring(0, index);
        String secondCombo = letterCombination.substring(index, letterCombination.length());
        Set<String> wordCombinations = new HashSet<>();

        if ( letterCombination.equals("1bckl1me1")) {
             //TODO:
            // just get everything as is
            // then at the end, remove all numbers and put it through the splitter - if it comes back, then golden
            // 1callme1 => callme => call-me ==> 1-call-me-1
            // 1call3me1 => callme => call-me ==> 1-call-3-me-1

            this.toString();
        }

        boolean firstComboHasLastDigit = index > 0 && StringUtils.isDigit(firstCombo.charAt(index-1));
        boolean secondComboIsADigit = secondCombo.length() == 1 && StringUtils.isDigit(secondCombo.charAt(0));
        boolean secondComboHasFirstDigit = secondCombo.length() > 1 && StringUtils.isDigit(secondCombo.charAt(0));
        boolean isFirstComboAWord = dictionary.isWord(firstCombo);

        boolean continueProcessing = false;
        if ( isFirstComboAWord || firstComboHasLastDigit || secondComboHasFirstDigit || secondComboIsADigit) {
            if ( isFirstComboAWord && secondComboIsADigit ) {
                wordCombinations.add(createNewWord(firstCombo, secondCombo));
            } else if ( isFirstComboAWord && secondComboHasFirstDigit) {
                continueProcessing = true;
            } else {
                continueProcessing = true;
            }
        }

//        if ( dictionary.isWord(firstCombo)) {
//            if ( firstComboHasLastDigit) {
//                wordCombinations.add(createNewWord(firstCombo, secondCombo));
//            } else if ( secondComboHasFirstDigit) {
//                wordCombinations.addAll(
//                    getWordSplits(secondCombo)
//                        .stream()
//                        .map(s -> createNewWord(firstCombo, s))
//                        .collect(Collectors.toSet()));
//            }
//        }

        if ( continueProcessing ) {
            wordCombinations.addAll(
                    getWordSplits(secondCombo)
                            .stream()
                            .map(s -> createNewWord(firstCombo, s))
                            .collect(Collectors.toSet()));
        }


//        // Check if the first word combination is either a word or a number
//        if ( dictionary.isWord(firstCombo) || ) {
//            if (dictionary.isWord(firstCombo) && ())) {
//                wordCombinations.add(createNewWord(firstCombo, secondCombo));
//            } else if (dictionary.isWord(firstCombo) && (secondCombo.length() > 1 && StringUtils.isDigit(secondCombo.charAt(0)))) {
//
//            } else if (dictionary.isWord(firstCombo)) {
//
//            }
//        }
        return wordCombinations;
    }

    private String createNewWord(String word) {
        return word.toUpperCase();
    }

    private String createNewWord(String partOne, String partTwo) {
        return createNewWord(partOne + WORD_SPLITTER + partTwo);
    }
}
