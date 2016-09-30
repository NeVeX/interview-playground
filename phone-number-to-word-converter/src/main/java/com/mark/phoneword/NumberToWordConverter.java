package com.mark.phoneword;

import com.mark.phoneword.dictionary.DefaultDictionary;
import com.mark.phoneword.dictionary.Dictionary;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Created by Mark Cunningham on 9/27/2016.
 */
class NumberToWordConverter {

    private final NumberToLettersConverter numberToLettersConverter;
    private final Dictionary dictionary;

    public NumberToWordConverter() {
        this(new DefaultNumberToLettersConverter(), new DefaultDictionary());
    }

    NumberToWordConverter(NumberToLettersConverter numberToLettersConverter, Dictionary dictionary) {
        if ( numberToLettersConverter == null ) {
            throw new IllegalArgumentException("Provided numberToLettersConverter cannot be null");
        }
        if (dictionary == null) {
            throw new IllegalArgumentException("Provided dictionary cannot be null");
        }
        this.numberToLettersConverter = numberToLettersConverter;
        this.dictionary = dictionary;
    }

    Set<String> convert(long number) {
        Set<String> letterCombinations = numberToLettersConverter.convert(number);
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
        Set<String> combinations = IntStream.range(1, letterCombination.length()) // We don't want one character words
                .parallel()
                .mapToObj( index -> {


                    if ( letterCombination.equals("callmeme")) {
                        this.toString();
                    }

                    String firstCombo = letterCombination.substring(0, index);
                    String secondCombo = letterCombination.substring(index, letterCombination.length());
                    Set<String> wordCombinations = new HashSet<>();
                    if ( dictionary.isWord(firstCombo) && dictionary.isWord(secondCombo) )  {
                        wordCombinations.add(firstCombo + "-" +secondCombo);
                        wordCombinations.addAll(getWordSplits(secondCombo).stream().map(s -> firstCombo + "-" + s).collect(Collectors.toSet()));
                    }
                    return wordCombinations;
                }).flatMap(Collection::stream)
                .collect(Collectors.toSet());
        return combinations;
    }

}
