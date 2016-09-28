package com.mark.phoneword;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
public class NumberToWordConverterTest {

    @Test
    public void assertOneDigitNumberConvertsToLetterCombinations() {
        int number = 2;
        Set<String> expectedLetters = new HashSet<>();
        expectedLetters.add("a");
        expectedLetters.add("b");
        expectedLetters.add("c");
        NumberToWordConverter numberToWordConverter = new NumberToWordConverter();
        Set<String> convertedLetters = numberToWordConverter.convert(number);

        assertThat(convertedLetters).containsAll(expectedLetters);
    }

}
