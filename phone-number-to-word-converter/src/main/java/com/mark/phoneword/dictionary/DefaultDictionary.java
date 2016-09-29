package com.mark.phoneword.dictionary;

import com.mark.phoneword.data.read.DictionaryFileReader;

import java.io.File;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class DefaultDictionary extends Dictionary {

    private final static Set<String> DEFAULT_DICTIONARY_WORDS;
    private final static String RESOURCE_LOCATION = "dictionaries";

    static {

        String englishDictionaryResource = RESOURCE_LOCATION + File.separator + "english.dic";
        Optional<Set<String>> englishWordsOptional = new DictionaryFileReader().readResource(englishDictionaryResource);
        if ( englishWordsOptional.isPresent()) {
            DEFAULT_DICTIONARY_WORDS = englishWordsOptional.get();
        } else {
            throw new IllegalStateException("Could not load default application dictionary ["+englishDictionaryResource+"]");
        }
    }

    public DefaultDictionary() {
        super(DEFAULT_DICTIONARY_WORDS);
    }
}
