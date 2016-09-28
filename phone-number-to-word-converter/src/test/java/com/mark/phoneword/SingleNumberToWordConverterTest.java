package com.mark.phoneword;

import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
public class SingleNumberToWordConverterTest {

    @Test
    public void assertOneDigitNumberConvertsToLetterCombinations() {
        int number = 2;
        Set<Character> expectedLetters = new HashSet<>();
        expectedLetters.add('a');
        expectedLetters.add('b');
        expectedLetters.add('c');

        // Create a conversion map
        Map<Integer, Set<Character>> conversionMap = new HashMap<>();
        Set<Character> abcSet = new HashSet<>();
        abcSet.addAll(expectedLetters);
        conversionMap.put(number, abcSet);


        SingleNumberToWordConverter singleNumberToWordConverter = new SingleNumberToWordConverter(conversionMap);
        Set<String> convertedLetters = singleNumberToWordConverter.convert(number);

//        assertThat(convertedLetters).containsAll(expectedLetters);
    }

}
