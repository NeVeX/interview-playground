package com.mark.phoneword;

import com.mark.phoneword.util.NumberUtils;
import com.mark.phoneword.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
class DefaultNumberToLettersConverter extends NumberToLettersConverter {

    private final static Map<Integer, Set<Character>> DEFAULT_CONVERSION_MAP;

    static {
        DEFAULT_CONVERSION_MAP = new HashMap<>();
        DEFAULT_CONVERSION_MAP.put(2, new HashSet<>(Arrays.asList('a', 'b', 'c')));
        DEFAULT_CONVERSION_MAP.put(3, new HashSet<>(Arrays.asList('d', 'e', 'f')));
        DEFAULT_CONVERSION_MAP.put(4, new HashSet<>(Arrays.asList('g', 'h', 'i')));
        DEFAULT_CONVERSION_MAP.put(5, new HashSet<>(Arrays.asList('j', 'k', 'l')));
        DEFAULT_CONVERSION_MAP.put(6, new HashSet<>(Arrays.asList('m', 'n', 'o')));
        DEFAULT_CONVERSION_MAP.put(7, new HashSet<>(Arrays.asList('p', 'q', 'r', 's')));
        DEFAULT_CONVERSION_MAP.put(8, new HashSet<>(Arrays.asList('t', 'u', 'v')));
        DEFAULT_CONVERSION_MAP.put(9, new HashSet<>(Arrays.asList('w', 'x', 'y', 'z')));
    }

    DefaultNumberToLettersConverter() {
        super(DEFAULT_CONVERSION_MAP);
    }
}
