package com.mark.phoneword;

import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
public class NumberToLettersConverterTest {

    @Test
    public void assertOneDigitNumberDoesNotConvertToLetterCombinations() {
        byte number = 2;
        Set<Character> number2ToLettersMap = new HashSet<>();
        number2ToLettersMap.add('a');
        number2ToLettersMap.add('b');
        number2ToLettersMap.add('c');

        // Create a conversion map
        Map<Byte, Set<Character>> conversionMap = new HashMap<>();
        conversionMap.put(number, number2ToLettersMap);

        Set<String> expectedLetters = new HashSet<>();
        Set<String> convertedNumber = assertDigitNumberConversion(number, conversionMap, expectedLetters);
        assertThat(convertedNumber.isEmpty()).isTrue();
    }

    private Set<String> assertDigitNumberConversion(long number, Map<Byte, Set<Character>> inputMap, Set<String> expectedOutput) {
        NumberToLettersConverter numberToLettersConverter = new NumberToLettersConverter(inputMap);
        Set<String> convertedLetters = numberToLettersConverter.convert(number);
        assertThat(convertedLetters).containsAll(expectedOutput);
        return convertedLetters;
    }

    @Test
    public void assertTwoDigitNumberConvertsToLetterCombinations() {
        long number = 22;
        Set<Character> number2ToLettersMap = new HashSet<>();
        number2ToLettersMap.add('a');
        number2ToLettersMap.add('b');
        number2ToLettersMap.add('c');

        // Create a conversion map
        Map<Byte, Set<Character>> conversionMap = new HashMap<>();
        conversionMap.put((byte)2, number2ToLettersMap);

        Set<String> expectedLetters = expectedLetters =
                new HashSet<>(Arrays.asList("aa", "ab", "ac", "ba", "bb", "bc", "ca", "cb", "cc"));
        assertDigitNumberConversion(number, conversionMap, expectedLetters);
    }

    @Test
    public void assertDigitsReturnedWhenNoMatchFoundInConversionMap() {
        long number = 6789; // all of these are not in the below conversion, hence the converted set should be empty
        Set<Character> number2ToLettersMap = new HashSet<>();
        number2ToLettersMap.add('a');
        Set<Character> number3ToLettersMap = new HashSet<>();
        number3ToLettersMap.add('b');
        number3ToLettersMap.add('c');

        // Create a conversion map
        Map<Byte, Set<Character>> conversionMap = new HashMap<>();
        conversionMap.put((byte)2, number2ToLettersMap);
        conversionMap.put((byte)3, number3ToLettersMap);

        NumberToLettersConverter numberToLettersConverter = new NumberToLettersConverter(conversionMap);
        assertThat(numberToLettersConverter.convert(number).isEmpty()).isTrue(); // No conversion expected
    }

    @Test
    public void assertThreeDigitNumberConvertsToLetterCombinations() {
        int number = 234;
        Set<Character> number2ToLettersMap = new HashSet<>();
        number2ToLettersMap.add('a');
        Set<Character> number3ToLettersMap = new HashSet<>();
        number3ToLettersMap.add('b');
        Set<Character> number4ToLettersMap = new HashSet<>();
        number4ToLettersMap.add('c');

        // Create a conversion map
        Map<Byte, Set<Character>> conversionMap = new HashMap<>();
        conversionMap.put((byte)2, number2ToLettersMap);
        conversionMap.put((byte)3, number3ToLettersMap);
        conversionMap.put((byte)4, number4ToLettersMap);

        Set<String> expectedLetters = expectedLetters = new HashSet<>(Arrays.asList("ab", "abc"));
        assertDigitNumberConversion(number, conversionMap, expectedLetters);
    }

    @Test
    public void assertThreeDigitWithDepthNumberConvertsToLetterCombinations() {
        int number = 234;
        // Adding more options to various digits
        Set<Character> number2ToLettersMap = new HashSet<>();
        number2ToLettersMap.add('a');
        number2ToLettersMap.add('b');
        Set<Character> number3ToLettersMap = new HashSet<>();
        number3ToLettersMap.add('c');
        Set<Character> number4ToLettersMap = new HashSet<>();
        number4ToLettersMap.add('d');
        number4ToLettersMap.add('e');

        // Create a conversion map
        Map<Byte, Set<Character>> conversionMap = new HashMap<>();
        conversionMap.put((byte)2, number2ToLettersMap);
        conversionMap.put((byte)3, number3ToLettersMap);
        conversionMap.put((byte)4, number4ToLettersMap);

        Set<String> expectedLetters = expectedLetters = new HashSet<>(Arrays.asList(
                "ac", "acd", "ace", "bc", "bcd", "bce"));
        assertDigitNumberConversion(number, conversionMap, expectedLetters);
    }


    @Test
    public void assertDigitWithNoConversionMappingConvertsToLetterCombinations() {
        int number = 26; // no number six
        Set<Character> number2ToLettersMap = new HashSet<>();
        number2ToLettersMap.add('a');
        number2ToLettersMap.add('b');
        number2ToLettersMap.add('c');

        // Create a conversion map
        Map<Byte, Set<Character>> conversionMap = new HashMap<>();
        conversionMap.put((byte)2, number2ToLettersMap);

        Set<String> expectedLetters = expectedLetters = new HashSet<>(Arrays.asList("a6", "b6", "c6"));
        assertDigitNumberConversion(number, conversionMap, expectedLetters);
    }


    @Test
    public void assertConsecutiveDigitsNotReturned() {
        // 7 and 4 will not be mapped, hence the letter converter should not return anything after the 4
        int number = 237432;
        Set<Character> number2ToLettersMap = new HashSet<>();
        number2ToLettersMap.add('a');
        Set<Character> number3ToLettersMap = new HashSet<>();
        number3ToLettersMap.add('b');

        // Create a conversion map
        Map<Byte, Set<Character>> conversionMap = new HashMap<>();
        conversionMap.put((byte)2, number2ToLettersMap);
        conversionMap.put((byte)3, number3ToLettersMap);

        NumberToLettersConverter numberToLettersConverter = new NumberToLettersConverter(conversionMap);
        Set<String> convertedLetters = numberToLettersConverter.convert(number);


        Set<String> expectedLetters = new HashSet<>(Arrays.asList("ab", "ab7"));

        assertThat(convertedLetters).containsAll(expectedLetters);
    }

    @Test
    public void assertExampleCombinationWorks() {
        int exampleNumber = 225563;
        // Create a conversion map
        Map<Byte, Set<Character>> conversionMap = new HashMap<>();

        Set<Character> abcSet = new HashSet<>();
        abcSet.addAll(Arrays.asList('a', 'b', 'c'));
        conversionMap.put((byte)2, abcSet);

        Set<Character> defSet = new HashSet<>();
        defSet.addAll(Arrays.asList('d', 'e', 'f'));
        conversionMap.put((byte)3, defSet);

        Set<Character> ghiSet = new HashSet<>();
        ghiSet.addAll(Arrays.asList('g', 'h', 'i'));
        conversionMap.put((byte)4, ghiSet);

        Set<Character> jklSet = new HashSet<>();
        jklSet.addAll(Arrays.asList('j', 'k', 'l'));
        conversionMap.put((byte)5, jklSet);

        Set<Character> mnoSet = new HashSet<>();
        mnoSet.addAll(Arrays.asList('m', 'n', 'o'));
        conversionMap.put((byte)6, mnoSet);


        NumberToLettersConverter numberToLettersConverter = new NumberToLettersConverter(conversionMap);
        Set<String> convertedLetters = numberToLettersConverter.convert(exampleNumber);

        assertThat(convertedLetters.contains("callme")).isTrue();

    }

}
