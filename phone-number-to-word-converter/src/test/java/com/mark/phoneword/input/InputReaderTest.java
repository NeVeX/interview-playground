package com.mark.phoneword.input;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public class InputReaderTest {

    @Test
    public void assertDictionaryFileIsReadFromInputArguments() {
        String dictionaryFileExpected = "/this/is/where/the/file/english.dictionary";
        String[] args = new String[] {"-d="+dictionaryFileExpected};

        InputReader inputReader = new InputReader();
        Optional<String> extractedDictFileOptional = inputReader.getDictionaryFile(args);
        assertThat(extractedDictFileOptional).as("Expected a parsed dictionary file").isPresent();
        assertThat(extractedDictFileOptional.get()).as("Expected dictionary file to match input").isEqualTo(dictionaryFileExpected);

        // put spaces before the file, it should still parse
        args = new String[] { "   -d=      "+dictionaryFileExpected};
        extractedDictFileOptional = inputReader.getDictionaryFile(args);
        assertThat(extractedDictFileOptional).isPresent();
        assertThat(extractedDictFileOptional.get()).isEqualTo(dictionaryFileExpected);

        // put spaces in between the operator and argument, it should not pass
        args = new String[] { "   -d   =      "+dictionaryFileExpected};
        extractedDictFileOptional = inputReader.getDictionaryFile(args);
        assertThat(extractedDictFileOptional).isNotPresent();


        // Make sure no value if given when no argument value is given
        args = new String[] { "-d="};
        extractedDictFileOptional = inputReader.getDictionaryFile(args);
        assertThat(extractedDictFileOptional).isNotPresent();

        // Make sure incorrect file argument doesn't parse
        args = new String[] { "-dictionary=/blah/mark.dictionary"};
        extractedDictFileOptional = inputReader.getDictionaryFile(args);
        assertThat(extractedDictFileOptional).isNotPresent();

        // Provide a wrong operator (not "=")
        args = new String[] { "-d>/blah/mark.dictionary"};
        extractedDictFileOptional = inputReader.getDictionaryFile(args);
        assertThat(extractedDictFileOptional).isNotPresent();



    }



}
