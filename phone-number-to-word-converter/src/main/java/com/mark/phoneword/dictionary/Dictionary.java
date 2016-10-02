package com.mark.phoneword.dictionary;

import com.mark.phoneword.util.StringUtils;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/28/2016.
 * <br>Dictionary class that holds all words that can be checked against.
 */
public final class Dictionary {

    private final Set<String> dictionary;

    /**
     * Creates a new instance of the dictionary.
     * <br>Note, each string must be valid, trimmed and in lower case
     */
    Dictionary(Set<String> dictionary) {
        if ( dictionary == null || dictionary.isEmpty()) {
            throw new IllegalArgumentException("Provided dictionary cannot be null or empty");
        }
        this.dictionary = Collections.unmodifiableSet(dictionary);
    }

    /**
     * Given a string, this method checks if it's a word against the current dictionary.
     * @param word - the non null/empty word to check - can be any case
     * @return - True if it's a word, false otherwise
     */
    public boolean isWord(String word) {
        if (StringUtils.isNotBlank(word)) {
            return dictionary.contains(word.trim().toLowerCase());
        } else {
            return false;
        }
    }

    /**
     * @return - how many words are in this dictioanry
     */
    int getWordCount() {
        return dictionary.size();
    }

}
