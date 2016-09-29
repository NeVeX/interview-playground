package com.mark.phoneword.dictionary;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class DefaultDictionary extends Dictionary {

    private final static Set<String> DEFAULT_DICTIONARY;

    static {
        DEFAULT_DICTIONARY = new HashSet<>();
        DEFAULT_DICTIONARY.add("sadasd");
    }

    public DefaultDictionary() {
        super(DEFAULT_DICTIONARY);
    }
}
