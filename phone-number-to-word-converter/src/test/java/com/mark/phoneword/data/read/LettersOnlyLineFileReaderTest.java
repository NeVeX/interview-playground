package com.mark.phoneword.data.read;

import com.mark.phoneword.TestingUtils;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/1/2016.
 */
public class LettersOnlyLineFileReaderTest {

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

        FileReader<Set<String>> fileReader = FileReaderFactory.lettersOnlyLineReader();
        Optional<Set<String>> readLinesOptional = fileReader.readFile(file.getAbsolutePath());
        assertThat(readLinesOptional).isPresent();
        Set<String> readLines = readLinesOptional.get();
        assertThat(readLines).contains("mark");
        assertThat(readLines).contains("was");
        assertThat(readLines).contains("here");
        assertThat(readLines).size().isEqualTo(3); // No other lines should be read
    }

    @Test
    public void assertNoExceptionsForNullInputFile() {
        FileReaderFactory.lettersOnlyLineReader().readFile(null);
    }

    @Test
    public void assertNoExceptionsForBlankInputFile() {
        FileReaderFactory.lettersOnlyLineReader().readFile("");
    }


    @Test
    public void assertNoExceptionsForNonExistenceInputFile() {
        FileReaderFactory.lettersOnlyLineReader().readFile(UUID.randomUUID().toString());
    }
}
