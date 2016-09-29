package com.mark.phoneword;

import com.mark.phoneword.util.NumberUtils;
import com.mark.phoneword.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
class NumberToWordConverter {

    private final NumberToLettersConverter numberToLettersConverter;

    public NumberToWordConverter() {
        this(new DefaultNumberToLettersConverter());
    }

    public NumberToWordConverter(NumberToLettersConverter numberToLettersConverter) {
        if ( numberToLettersConverter == null ) {
            throw new IllegalArgumentException("Provided numberToLettersConverter cannot be null");
        }
        this.numberToLettersConverter = numberToLettersConverter;
    }

    boolean convert(int number) {
        Set<String> letterCombinations = numberToLettersConverter.convert(number);
        return true;
    }



}
