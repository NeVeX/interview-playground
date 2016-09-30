package com.mark.phoneword.convert;

import com.mark.phoneword.dictionary.DictionaryFactory;
import com.mark.phoneword.dictionary.Dictionary;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Created by Mark Cunningham on 9/27/2016.
 */
class NumberToWordConverter implements Converter<Long, String> {

    private final static String WORD_SPLITTER = "-";
    private final NumberToLettersConverter longToLetterConverter;
    private final Dictionary dictionary;

    NumberToWordConverter(NumberToLettersConverter longToLetterConverter, Dictionary dictionary) {
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
        Set<String> letterCombinations = longToLetterConverter.convert(number);
        if ( !letterCombinations.isEmpty() ) {

            return letterCombinations
                    .parallelStream()
                    .map(this::getWordSplits)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private Set<String> getWordSplits(final String letterCombination) {
        // Split this letterCombination up
        // E.g. mart -> m, art | ma, rt | mar, t
        return IntStream.range(1, letterCombination.length()) // We don't want one character words
                .parallel()
                .mapToObj( index -> getWordSplitsUsingIndex(index, letterCombination) )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<String> getWordSplitsUsingIndex(int index, String letterCombination) {
        String firstCombo = letterCombination.substring(0, index);
        String secondCombo = letterCombination.substring(index, letterCombination.length());
        Set<String> wordCombinations = new HashSet<>();
        if ( dictionary.isWord(firstCombo) && dictionary.isWord(secondCombo) )  {
            wordCombinations.add(createNewWord(firstCombo, secondCombo));
            wordCombinations.addAll(
                    getWordSplits(secondCombo)
                    .stream()
                    .map(s -> createNewWord(firstCombo, s))
                    .collect(Collectors.toSet())
            );
        }
        return wordCombinations;
    }

    private String createNewWord(String partOne, String partTwo) {
        return (partOne + WORD_SPLITTER + partTwo).toUpperCase();
    }
}
