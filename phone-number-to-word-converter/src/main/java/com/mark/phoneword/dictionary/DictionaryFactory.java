package com.mark.phoneword.dictionary;


import com.mark.phoneword.data.read.FileReaderFactory;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/28/2016.
 * <br>This factory has helper methods to create various types of dictionaries and from various sources
 */
public final class DictionaryFactory {

    private final static String RESOURCE_LOCATION = "dictionaries";
    private final static String ENGLISH_DICTIONARY_LOCATION = RESOURCE_LOCATION+"/english.dic";

    /**
     * Returns the default dictionary of this application
     * @return - the default instance
     */
    public static Dictionary getDefault() {
        return DefaultDictionaryHolder.INSTANCE;
    }

    /**
     * For the given path and file name (must be the absolute path and file name), this method will
     * attempt to read the file and create a new instance of a dictionary from it.
     * @param pathToFile - the full absolute path to the dictionary file
     * @return - the new dictionary if successful, otherwise, an empty optional
     */
    public static Optional<Dictionary> fromFile(String pathToFile) {
        Optional<Set<String>> readInDictionaryOptional = FileReaderFactory.lettersOnlyLineReader().readFile(pathToFile);
        if ( readInDictionaryOptional.isPresent()) {
            return Optional.of(new Dictionary(readInDictionaryOptional.get()));
        }
        return Optional.empty();
    }

    private DictionaryFactory(){}

    /**
     * Inner class that holds the defaults. Lazy loaded and thread safe
     * Using example from Bill Pugh singleton - http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
     */
    private static class DefaultDictionaryHolder {

        private static final Dictionary INSTANCE;

        static {
            /**
             * Loading the default dictionary in the application - which is the english dictionary
             * Note: This dictionary was obtained from: https://sourceforge.net/projects/jazzy/?source=typ_redirect
             */
            Optional<Set<String>> englishWordsOptional = FileReaderFactory.lettersOnlyLineReader().readResource(ENGLISH_DICTIONARY_LOCATION);
            if ( englishWordsOptional.isPresent()) {
                INSTANCE = new Dictionary(englishWordsOptional.get());
            } else {
                // If we can't load this, then we are in a bad shape, so throw a runtime to indicate as such
                throw new IllegalStateException("Could not load default application dictionary ["+ENGLISH_DICTIONARY_LOCATION+"]");
            }
        }
    }

}
