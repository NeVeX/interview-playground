package com.mark.phoneword;

import com.mark.phoneword.dictionary.DefaultDictionary;
import com.mark.phoneword.dictionary.Dictionary;

import java.util.Set;


/**
 * Created by Mark Cunningham on 9/27/2016.
 */
class NumberToWordConverter {

    private final NumberToLettersConverter numberToLettersConverter;
    private final Dictionary dictionary;

    public NumberToWordConverter() {
        this(new DefaultNumberToLettersConverter(), new DefaultDictionary());
    }

    NumberToWordConverter(NumberToLettersConverter numberToLettersConverter, Dictionary dictionary) {
        if ( numberToLettersConverter == null ) {
            throw new IllegalArgumentException("Provided numberToLettersConverter cannot be null");
        }
        if (dictionary == null) {
            throw new IllegalArgumentException("Provided dictionary cannot be null");
        }
        this.numberToLettersConverter = numberToLettersConverter;
        this.dictionary = dictionary;
    }

    boolean convert(int number) {
        Set<String> letterCombinations = numberToLettersConverter.convert(number);
        return true;
    }



}
