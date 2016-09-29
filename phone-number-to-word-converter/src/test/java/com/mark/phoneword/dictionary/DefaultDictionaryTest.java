package com.mark.phoneword.dictionary;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public class DefaultDictionaryTest {

    @Test
    public void assertDefaultDictionaryResourceLoadsAndHasWords() {

        DefaultDictionary defaultDictionary = new DefaultDictionary();

        assertThat(defaultDictionary.isWord("mark")).isTrue();
        assertThat(defaultDictionary.isWord("MARK")).isTrue();
        assertThat(defaultDictionary.isWord("    mARk   ")).isTrue();
        assertThat(defaultDictionary.getWordCount()).isEqualTo(150824);

    }

}
