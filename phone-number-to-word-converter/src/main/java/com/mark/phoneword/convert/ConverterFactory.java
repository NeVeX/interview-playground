package com.mark.phoneword.convert;

import com.mark.phoneword.dictionary.Dictionary;
import com.mark.phoneword.dictionary.DictionaryFactory;

import java.util.*;

/**
 * Created by Mark Cunningham on 9/29/2016.
 * <br>Use this converter factory to create various instances of converters for use.
 * <br>All instances implement {@link Converter}
 */
public final class ConverterFactory {

    private ConverterFactory() { }

    /**
     * @returns - Returns the converter that supports converting Long numbers to letters as combinations
     */
    static Converter<Long, String> longNumberToLetters() {
        return DefaultHolder.DEFAULT_NUMBER_TO_LETTER_CONVERTER;
    }

    /**
     * @return - Returns an instance that supports conversion of Long numbers to Words using the default dictionary
     */
    static Converter<Long, String> longNumberToWords() {
        return longNumberToWords(DictionaryFactory.getDefault());
    }

    /**
     * Create an instance of the {@link Converter} using the given input dictionary {@link Dictionary}
     * @param dictionary - The non null dictionary to use for conversions
     * @return - The new instance to use for conversions
     */
    public static Converter<Long, String> longNumberToWords(Dictionary dictionary) {
        return new PhoneNumberToWordConverter(DefaultHolder.DEFAULT_NUMBER_TO_LETTER_CONVERTER, dictionary);
    }

    /**
     * Static class to hold the default implementations we have.
     * This setup allows for easy lazy init and thread safety
     */
    private static class DefaultHolder {

        private final static NumberToLettersConverter DEFAULT_NUMBER_TO_LETTER_CONVERTER;

        static {
            Map<Byte, Set<Character>> defaultNumberToLetterMap = new HashMap<>();
            defaultNumberToLetterMap.put((byte)2, new HashSet<>(Arrays.asList('a', 'b', 'c')));
            defaultNumberToLetterMap.put((byte)3, new HashSet<>(Arrays.asList('d', 'e', 'f')));
            defaultNumberToLetterMap.put((byte)4, new HashSet<>(Arrays.asList('g', 'h', 'i')));
            defaultNumberToLetterMap.put((byte)5, new HashSet<>(Arrays.asList('j', 'k', 'l')));
            defaultNumberToLetterMap.put((byte)6, new HashSet<>(Arrays.asList('m', 'n', 'o')));
            defaultNumberToLetterMap.put((byte)7, new HashSet<>(Arrays.asList('p', 'q', 'r', 's')));
            defaultNumberToLetterMap.put((byte)8, new HashSet<>(Arrays.asList('t', 'u', 'v')));
            defaultNumberToLetterMap.put((byte)9, new HashSet<>(Arrays.asList('w', 'x', 'y', 'z')));
            DEFAULT_NUMBER_TO_LETTER_CONVERTER = new NumberToLettersConverter(defaultNumberToLetterMap);
        }

    }
}
