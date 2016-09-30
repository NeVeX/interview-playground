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
        Set<String> wordSplits = IntStream.range(1, letterCombination.length())
                .parallel()
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

//        String wordToSplit = secondCombo;

        if ( letterCombination.equals("callme1")) {


             //TODO:
            // just get everything as is
            // then at the end, remove all numbers and put it through the splitter - if it comes back, then golden
            // 1callme1 => callme => call-me ==> 1-call-me-1
            // 1call3me1 => callme => call-me ==> 1-call-3-me-1

            this.toString();
        }

        if ( (index > 0 && StringUtils.isDigit(firstCombo.charAt(index-1))) /*&& dictionary.isWord(secondCombo)*/ )  {
//            wordCombinations.add(createNewWord(firstCombo, secondCombo));
//            wordToSplit = secondCombo;
            wordCombinations.addAll(
                    getWordSplits(/*secondCombo*/ secondCombo)
                            .parallelStream()
                            .map(s -> createNewWord(firstCombo, s))
                            .collect(Collectors.toSet()));
        } else if ( (secondCombo.length() > 0 && StringUtils.isDigit(secondCombo.charAt(0)))) {
//            System.out.println("Not word: "+firstCombo+", "+secondCombo);
//            wordToSplit = firstCombo;
            wordCombinations.addAll(
                    getWordSplits(/*secondCombo*/ firstCombo)
                            .parallelStream()
                            .map(s -> createNewWord(secondCombo, s))
                            .collect(Collectors.toSet()));
        }

        return wordCombinations;
    }

    private String createNewWord(String word) {
        return word.toUpperCase();
    }

    private String createNewWord(String partOne, String partTwo) {
        return createNewWord(partOne + WORD_SPLITTER + partTwo);
    }
}
