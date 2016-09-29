package com.mark.phoneword.dictionary;

import com.mark.phoneword.dictionary.Dictionary;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class DictionaryTest {

    @Test
    public void assertCaseInsensitiveWordsAreInTheDictionary() {
        Set<String> dictionary = new HashSet<>();
        dictionary.add("mark");
        Dictionary dictionaryLookup = new Dictionary(dictionary);
        assertThat(dictionaryLookup.isWord("Mark")).isTrue(); // Capitalize
        assertThat(dictionaryLookup.isWord("mark")).isTrue(); // all lower case
        assertThat(dictionaryLookup.isWord("MARK")).isTrue(); // all upper case
        assertThat(dictionaryLookup.isWord("MaRK")).isTrue(); // mix of cases

        Set<String> badDictionary = new HashSet<>();
        badDictionary.add("SUSAN"); // Nothing can match this
        dictionaryLookup = new Dictionary(badDictionary);
        assertThat(dictionaryLookup.isWord("Susan")).isFalse(); // Capitalize
        assertThat(dictionaryLookup.isWord("susan")).isFalse(); // all lower case
        assertThat(dictionaryLookup.isWord("SUSAN")).isFalse(); // all upper case
        assertThat(dictionaryLookup.isWord("SuSaN")).isFalse(); // mix of cases
    }

    @Test
    public void assertWordsAreTrimmedBeforeLookupInTheDictionary() {
        Set<String> dictionary = new HashSet<>();
        dictionary.add("mark");
        dictionary.add("susan");
        dictionary.add("timmy");
        Dictionary dictionaryLookup = new Dictionary(dictionary);
        assertThat(dictionaryLookup.isWord("   mark")).isTrue(); // should find it
        assertThat(dictionaryLookup.isWord("   susan    ")).isTrue(); // should find it
        assertThat(dictionaryLookup.isWord("timmy    john")).isTrue(); // bad dictionary lookup
    }

}
