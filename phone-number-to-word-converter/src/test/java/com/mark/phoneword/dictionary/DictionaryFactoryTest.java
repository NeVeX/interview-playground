package com.mark.phoneword.dictionary;

import com.mark.phoneword.TestingUtils;
import org.junit.Test;

import java.io.File;
import java.util.Optional;

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

    @Test
    public void assertCustomDictionaryLoadsAndHasWords() {
        String customDictionaryInput = "mark\njohn\nsuperman";
        File dictionaryFile = TestingUtils.createRandomFileWithData(customDictionaryInput);

        Optional<Dictionary> customDictionaryOptional = DictionaryFactory.fromFile(dictionaryFile.getAbsolutePath());
        assertThat(customDictionaryOptional).isPresent();

        Dictionary customDictionary = customDictionaryOptional.get();
        assertThat(customDictionary.isWord("mark")).isTrue();
        assertThat(customDictionary.isWord("john")).isTrue();
        assertThat(customDictionary.isWord("superman")).isTrue();

        assertThat(customDictionary.getWordCount()).isEqualTo(3); // only 3 words expected

    }

}
