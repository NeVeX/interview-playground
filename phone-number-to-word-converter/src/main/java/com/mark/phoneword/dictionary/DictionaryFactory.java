package com.mark.phoneword.dictionary;


import com.mark.phoneword.data.read.FileReaderFactory;

import java.io.File;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public final class DictionaryFactory {

    private final static String RESOURCE_LOCATION = "dictionaries";
    private final static String ENGLISH_DICTIONARY_LOCATION = RESOURCE_LOCATION + File.separator + "english.dic";

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

    // Bill Pugh singleton - http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
    private static class DefaultDictionaryHolder {

        private static final Dictionary INSTANCE;

        static {
            // https://sourceforge.net/projects/jazzy/?source=typ_redirect -- Dictionary obtained from here
            Optional<Set<String>> englishWordsOptional = FileReaderFactory.lettersOnlyLineReader().readResource(ENGLISH_DICTIONARY_LOCATION);
            if ( englishWordsOptional.isPresent()) {
                INSTANCE = new Dictionary(englishWordsOptional.get());
            } else {
                throw new IllegalStateException("Could not load default application dictionary ["+ENGLISH_DICTIONARY_LOCATION+"]");
            }
        }
    }

}
