package com.mark.phoneword.data.read;

import com.mark.phoneword.TestingUtils;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/1/2016.
 */
public class NumbersOnlyLineFileReaderTest {

    @Test
    public void assertCanReadFiles() {
        List<String> lines = new ArrayList<>();
        lines.add("number");
        lines.add("123456");
        lines.add("!@#$%^&*()_+~`/;'[]\\,?><:\"{}"); // no numbers
        lines.add("34234.4564"); // will be one number after the removal of the decimal (period)
        lines.add("     ");
        lines.add("");
        lines.add("      445               ");

        File file = TestingUtils.createRandomFileWithData(lines);

        FileReader<Set<Long>> fileReader = FileReaderFactory.longsOnlyLineReader();
        Optional<Set<Long>> readLinesOptional = fileReader.readFile(file.getAbsolutePath());
        assertThat(readLinesOptional).isPresent();

        Set<Long> readLines = readLinesOptional.get();
        assertThat(readLines).contains(123456L);
        assertThat(readLines).contains(342344564L);
        assertThat(readLines).contains(445L);

        assertThat(readLines).size().isEqualTo(3); // No other lines should be read
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
