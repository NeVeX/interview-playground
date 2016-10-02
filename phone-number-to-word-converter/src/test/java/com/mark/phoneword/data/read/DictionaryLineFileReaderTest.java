package com.mark.phoneword.data.read;

import com.mark.phoneword.TestingUtils;
import com.mark.phoneword.dictionary.*;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.Dictionary;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/1/2016.
 */
public class DictionaryLineFileReaderTest {

    @Test
    public void assertCanReadFiles() {
        List<String> lines = new ArrayList<>();
        lines.add("mark");
        lines.add("was");
        lines.add("     "); // blank
        lines.add("here");
        lines.add(""); // blank
        lines.add("123456"); // no letters
        lines.add("!@#$%^&*()_+~`/;'[]\\,?><:\"{}"); // no letters

        File file = TestingUtils.createRandomFileWithData(lines);

        FileReader<Set<String>> fileReader = FileReaderFactory.dictionaryLineReader();
        Optional<Set<String>> readLinesOptional = fileReader.readFile(file.getAbsolutePath());
        assertThat(readLinesOptional).isPresent();
        Set<String> readLines = readLinesOptional.get();
        assertThat(readLines).contains("mark");
        assertThat(readLines).contains("was");
        assertThat(readLines).contains("here");
        assertThat(readLines).size().isEqualTo(3); // No other lines should be read
    }

    @Test
    public void assertCustomDictionaryDoesNotLoadNonCharacterWords() {
        String customDictionaryInput = "mark1234\njohn\n!superman\nspace "; // Only john should be valid here
        File dictionaryFile = TestingUtils.createRandomFileWithData(customDictionaryInput);

        DictionaryLineFileReader dictionaryReader = new DictionaryLineFileReader();
        Optional<Set<String>> readWordsOptional = dictionaryReader.readFile(dictionaryFile.getAbsolutePath());
        assertThat(readWordsOptional).isPresent();
        Set<String> readWords = readWordsOptional.get();
        assertThat(readWords).doesNotContain("mark");
        assertThat(readWords).doesNotContain("mark1234");
        assertThat(readWords).contains("john"); // John should be there
        assertThat(readWords).doesNotContain("superman");
        assertThat(readWords).doesNotContain("!superman");
        assertThat(readWords).doesNotContain("space");
        assertThat(readWords).doesNotContain("space ");

        assertThat(readWords.size()).isEqualTo(1); // only john should be there
    }


    @Test
    public void assertNoExceptionsForNullInputFile() {
        FileReaderFactory.dictionaryLineReader().readFile(null);
    }

    @Test
    public void assertNoExceptionsForBlankInputFile() {
        FileReaderFactory.dictionaryLineReader().readFile("");
    }


    @Test
    public void assertNoExceptionsForNonExistenceInputFile() {
        FileReaderFactory.dictionaryLineReader().readFile(UUID.randomUUID().toString());
    }
}
