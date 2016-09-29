package com.mark.phoneword;

import com.mark.phoneword.util.StringUtils;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class Dictionary {

    private final Set<String> dictionary;

    /**
     * NOTE: Words must be all lower case and trimmed
     * @param dictionary
     */
    public Dictionary(Set<String> dictionary) {
        if ( dictionary == null ) {
            throw new IllegalArgumentException("Provided dictionary cannot be null");
        }
        this.dictionary = Collections.unmodifiableSet(dictionary);
    }

    public boolean isWord(String word) {
        if (StringUtils.isNotBlank(word)) {
            return dictionary.contains(word.trim().toLowerCase());
        } else {
            return false;
        }
    }


}
