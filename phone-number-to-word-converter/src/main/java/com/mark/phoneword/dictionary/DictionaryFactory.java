package com.mark.phoneword.dictionary;

import com.mark.phoneword.data.read.DictionaryFileReader;

import java.io.File;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public final class DictionaryFactory {

    private final static String RESOURCE_LOCATION = "dictionaries";
    private final static String ENGLISH_DICTIONARY_LOCATION = RESOURCE_LOCATION + File.separator + "english.dic";
    private final static DictionaryFileReader DICTIONARY_FILE_READER = new DictionaryFileReader();

    public static Dictionary getDefault() {
        return DefaultDictionaryHolder.INSTANCE;
    }

    private DictionaryFactory(){}

    // Bill Pugh singleton - http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
    private static class DefaultDictionaryHolder {

        private static final Dictionary INSTANCE;

        static {
            // https://sourceforge.net/projects/jazzy/?source=typ_redirect -- Dictionary obtained from here
            Optional<Set<String>> englishWordsOptional = DICTIONARY_FILE_READER.readResource(ENGLISH_DICTIONARY_LOCATION);
            if ( englishWordsOptional.isPresent()) {
                INSTANCE = new Dictionary(englishWordsOptional.get());
            } else {
                throw new IllegalStateException("Could not load default application dictionary ["+ENGLISH_DICTIONARY_LOCATION+"]");
            }
        }
    }

}