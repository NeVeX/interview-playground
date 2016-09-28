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


        number = 22;
        singleNumberToWordConverter = new SingleNumberToWordConverter(conversionMap);
        convertedLetters = singleNumberToWordConverter.convert(number);

    }

    @Test
    public void assertExampleCombinationWorks() {

        // Create a conversion map
        Map<Integer, Set<Character>> conversionMap = new HashMap<>();

        Set<Character> abcSet = new HashSet<>();
        abcSet.addAll(Arrays.asList('a', 'b', 'c'));
        conversionMap.put(2, abcSet);

        Set<Character> defSet = new HashSet<>();
        defSet.addAll(Arrays.asList('d', 'e', 'f'));
        conversionMap.put(3, defSet);

        Set<Character> ghiSet = new HashSet<>();
        ghiSet.addAll(Arrays.asList('g', 'h', 'i'));
        conversionMap.put(4, ghiSet);

        Set<Character> jklSet = new HashSet<>();
        jklSet.addAll(Arrays.asList('j', 'k', 'l'));
        conversionMap.put(5, jklSet);

        Set<Character> mnoSet = new HashSet<>();
        mnoSet.addAll(Arrays.asList('m', 'n', 'o'));
        conversionMap.put(6, mnoSet);


        SingleNumberToWordConverter singleNumberToWordConverter = new SingleNumberToWordConverter(conversionMap);
        Set<String> convertedLetters = singleNumberToWordConverter.convert(225563);

//        assertThat(convertedLetters).containsAll(expectedLetters);
        assertThat(convertedLetters.contains("callme")).isTrue();

    }

}
