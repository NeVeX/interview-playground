package com.mark.phoneword.dictionary;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public class DictionaryFactoryTest {

    @Test
    public void assertDefaultDictionaryResourceLoadsAndHasWords() {

        Dictionary dictionary = DictionaryFactory.getDefault();

        assertThat(dictionary.isWord("mark")).isTrue();
        assertThat(dictionary.isWord("MARK")).isTrue();
        assertThat(dictionary.isWord("    mARk   ")).isTrue();
        assertThat(dictionary.getWordCount()).as("Check default dictionary size").isEqualTo(150629);

    }

}
