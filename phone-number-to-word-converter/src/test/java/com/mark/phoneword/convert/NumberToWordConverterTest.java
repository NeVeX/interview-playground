package com.mark.phoneword.convert;

import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
public class NumberToWordConverterTest {

    @Test
    public void assertExampleRequirementForCallMeWorks() {

        long number = 225563;
        Converter<Long, String> numberToWordConverter = ConverterFactory.longNumberToWords();
        Set<String> matches = numberToWordConverter.convert(number);
        assertThat(matches).contains("CALL-ME");
    }

    @Test
    public void assertFullWordsAndInnerWordsAreConverted() {
        long number = 359929;
        Converter<Long, String> numberToWordConverter = ConverterFactory.longNumberToWords();
        Set<String> matches = numberToWordConverter.convert(number);
        assertThat(matches).contains("FLYWAY", "FLY-WAY");
    }

    @Test
    public void assertSingleNumbersAreMapped() {
        long number = 1225563; // we hope to get 1-CALL-ME
        Converter<Long, String> numberToWordConverter = ConverterFactory.longNumberToWords();
        Set<String> matches = numberToWordConverter.convert(number);
        assertThat(matches).contains("1-CALL-ME");

        number = 2255631; // we hope to get CALL-ME-1
        numberToWordConverter = ConverterFactory.longNumberToWords();
        matches = numberToWordConverter.convert(number);
        assertThat(matches).contains("CALL-ME-1");

        number = 12255631; // we hope to get 1-CALL-ME-1
        numberToWordConverter = ConverterFactory.longNumberToWords();
        matches = numberToWordConverter.convert(number);
        assertThat(matches).contains("1-CALL-ME-1");


        number = 12255631; // we hope to get 1-CALL-ME-1
        numberToWordConverter = ConverterFactory.longNumberToWords();
        matches = numberToWordConverter.convert(number);
        assertThat(matches).contains("1-CALL-1-ME-1");

    }

    @Test
    public void assertLongerNonWordsAreMatchedWhenTheyHaveWordsWithin() {
        long number = 22553535; // we hope to get CALL-ME-ME -> MEME, is not in the dictionary, so we need to make sure we get the inner words
        Converter<Long, String> numberToWordConverter = ConverterFactory.longNumberToWords();
        Set<String> matches = numberToWordConverter.convert(number);
        assertThat(matches).contains("CALL-ME-ME");

    }

    @Test
    public void assertInnerWordSplitsAreWorking() {

        long number = 22556686;
        Converter<Long, String> numberToWordConverter = ConverterFactory.longNumberToWords();
        Set<String> matches = numberToWordConverter.convert(number);
        assertThat(matches).isNotEmpty();

        String twoSplitWord = "CALL-ONTO";
        String threeSplitWord = "CALL-ON-TO";
        assertThat(matches).contains(twoSplitWord, threeSplitWord);
        assertThat(matches).doesNotContain(twoSplitWord.toLowerCase(), threeSplitWord.toLowerCase());
    }

    @Test
    public void assertWordSplitSplitterIsADash() {

        long number = 22552255;
        Converter<Long, String> numberToWordConverter = ConverterFactory.longNumberToWords();
        Set<String> matches = numberToWordConverter.convert(number);
        assertThat(matches).isNotEmpty();
        String splitter = "-";
        String twoSplitWord = "BALL" + splitter + "CALL";
        assertThat(matches).contains(twoSplitWord);

        splitter = "_"; // underscore
        twoSplitWord = "BALL" + splitter + "CALL";
        assertThat(matches).doesNotContain(twoSplitWord);

        splitter = ""; // nothing
        twoSplitWord = "BALL" + splitter + "CALL";
        assertThat(matches).doesNotContain(twoSplitWord);


    }

}
