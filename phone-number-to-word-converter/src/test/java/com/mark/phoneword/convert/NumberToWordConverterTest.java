package com.mark.phoneword.convert;

import com.mark.phoneword.convert.NumberToWordConverter;
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
        assertThat(matches).isNotEmpty();

        assertThat(matches).contains("CALL-ME");

    }

    @Test
    public void assertInnerWordSplitsAreWorking() {

        long number = 22556363L;
        Converter<Long, String> numberToWordConverter = ConverterFactory.longNumberToWords();
        Set<String> matches = numberToWordConverter.convert(number);
        assertThat(matches).isNotEmpty();

        String twoSplitWord = "CALL-MEND";
        String threeSplitWord = "CALL-ME-ND";
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
