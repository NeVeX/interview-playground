package com.mark.phoneword.convert;

import com.mark.phoneword.dictionary.Dictionary;
import com.mark.phoneword.dictionary.DictionaryFactory;

import java.util.*;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public final class ConverterFactory {


    public static Converter<Long, String> longNumberToLetters() {
        return Holder.DEFAULT_NUMBER_TO_LETTER_CONVERTER;
    }

    public static Converter<Long, String> longNumberToWords() {
        return longNumberToWords(DictionaryFactory.getDefault());
    }

    public static Converter<Long, String> longNumberToWords(Dictionary dictionary) {
        return new PhoneNumberToWordConverter(Holder.DEFAULT_NUMBER_TO_LETTER_CONVERTER, dictionary);
    }

    private static class Holder {


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
