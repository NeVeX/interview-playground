package com.mark.phoneword;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
public class SingleNumberToWordConverterTest {

    @Test
    public void assertOneDigitNumberConvertsToLetterCombinations() {
        int number = 2;
        Set<Character> number2ToLettersMap = new HashSet<>();
        number2ToLettersMap.add('a');
        number2ToLettersMap.add('b');
        number2ToLettersMap.add('c');

        // Create a conversion map
        Map<Integer, Set<Character>> conversionMap = new HashMap<>();

        conversionMap.put(number, number2ToLettersMap);


        SingleNumberToWordConverter singleNumberToWordConverter = new SingleNumberToWordConverter(conversionMap);
        Set<String> convertedLetters = singleNumberToWordConverter.convert(number);

        Set<String> expectedLetters = new HashSet<>();

        assertThat(convertedLetters).containsAll(expectedLetters);

        number = 22;
        singleNumberToWordConverter = new SingleNumberToWordConverter(conversionMap);
        convertedLetters = singleNumberToWordConverter.convert(number);
        expectedLetters = new HashSet<>(Arrays.asList("aa", "ab", "ac", "ba", "bb", "bc", "ca", "cb", "cc"));
        assertThat(convertedLetters).containsAll(expectedLetters);

        number = 20;
        singleNumberToWordConverter = new SingleNumberToWordConverter(conversionMap);
        convertedLetters = singleNumberToWordConverter.convert(number);
        expectedLetters = new HashSet<>(Arrays.asList("a0", "b0", "c0"));
        assertThat(convertedLetters).containsAll(expectedLetters);

    }

    @Test
    public void assertConsecutiveDigitsNotReturned() {
        int number = 233; // Contains consecutive digits
        Set<Character> number2ToLettersMap = new HashSet<>();
        number2ToLettersMap.add('a');
        Set<Character> number3ToLettersMap = new HashSet<>();
        number3ToLettersMap.add('b');

        // Create a conversion map
        Map<Integer, Set<Character>> conversionMap = new HashMap<>();
        conversionMap.put(2, number2ToLettersMap);
        conversionMap.put(3, number3ToLettersMap);

        SingleNumberToWordConverter singleNumberToWordConverter = new SingleNumberToWordConverter(conversionMap);
        Set<String> convertedLetters = singleNumberToWordConverter.convert(number);

        assertThat(convertedLetters.isEmpty()).isTrue(); // Nothing should return

    }

    @Test
    public void assertExampleCombinationWorks() {
        int exampleNumber = 225563;
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
        Set<String> convertedLetters = singleNumberToWordConverter.convert(exampleNumber);

        assertThat(convertedLetters.contains("callme")).isTrue();

    }

}
