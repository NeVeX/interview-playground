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

        Optional<String> extractedDictFileOptional = new InputReader(args).getDictionaryFile();
        assertThat(extractedDictFileOptional).as("Expected a parsed dictionary file").isPresent();
        assertThat(extractedDictFileOptional.get()).as("Expected dictionary file to match input").isEqualTo(dictionaryFileExpected);

        // put spaces before the file, it should still parse
        args = new String[] { "   -d=      "+dictionaryFileExpected};
        extractedDictFileOptional = new InputReader(args).getDictionaryFile();
        assertThat(extractedDictFileOptional).isPresent();
        assertThat(extractedDictFileOptional.get()).isEqualTo(dictionaryFileExpected);

        // put spaces in between the operator and argument, it should not pass
        args = new String[] { "   -d   =      "+dictionaryFileExpected};
        extractedDictFileOptional = new InputReader(args).getDictionaryFile();
        assertThat(extractedDictFileOptional).isNotPresent();


        // Make sure no value if given when no argument value is given
        args = new String[] { "-d="};
        extractedDictFileOptional = new InputReader(args).getDictionaryFile();
        assertThat(extractedDictFileOptional).isNotPresent();

        // Make sure incorrect file argument doesn't parse
        args = new String[] { "-dictionary=/blah/mark.dictionary"};
        extractedDictFileOptional = new InputReader(args).getDictionaryFile();
        assertThat(extractedDictFileOptional).isNotPresent();

        // Provide a wrong operator (not "=")
        args = new String[] { "-d>/blah/mark.dictionary"};
        extractedDictFileOptional = new InputReader(args).getDictionaryFile();
        assertThat(extractedDictFileOptional).isNotPresent();

    }

    @Test
    public void assertPhoneNumberFileIsReadFromInputArguments() {
        String phoneNumberFileExpected = "/phones/all.numbers";
        String[] args = new String[] {"-p="+phoneNumberFileExpected};

        Optional<String> extractedPhoneFileOptional = new InputReader(args).getPhoneNumbersFile();
        assertThat(extractedPhoneFileOptional).as("Expected a parsed phone numbers file").isPresent();
        assertThat(extractedPhoneFileOptional.get()).as("Expected phone numbers file to match input").isEqualTo(phoneNumberFileExpected);

        // put spaces before the file, it should still parse
        args = new String[] { "   -p=      "+phoneNumberFileExpected};
        extractedPhoneFileOptional = new InputReader(args).getPhoneNumbersFile();
        assertThat(extractedPhoneFileOptional).isPresent();
        assertThat(extractedPhoneFileOptional.get()).isEqualTo(phoneNumberFileExpected);

        // put spaces in between the operator and argument, it should not pass
        args = new String[] { "   -p   =      "+phoneNumberFileExpected};
        extractedPhoneFileOptional = new InputReader(args).getPhoneNumbersFile();
        assertThat(extractedPhoneFileOptional).isNotPresent();


        // Make sure no value if given when no argument value is given
        args = new String[] { "-p="};
        extractedPhoneFileOptional = new InputReader(args).getPhoneNumbersFile();
        assertThat(extractedPhoneFileOptional).isNotPresent();

        // Make sure incorrect file argument doesn't parse
        args = new String[] { "-phones=/blah/all.phones"};
        extractedPhoneFileOptional = new InputReader(args).getPhoneNumbersFile();
        assertThat(extractedPhoneFileOptional).isNotPresent();

        // Provide a wrong operator (not "=")
        args = new String[] { "-p>/blah/phone.numbers"};
        extractedPhoneFileOptional = new InputReader(args).getPhoneNumbersFile();
        assertThat(extractedPhoneFileOptional).isNotPresent();

    }

    @Test
    public void assertAllArgumentsAreParsedCorrectly() {
        String dictionary = "/path/dictionary";
        String phoneNumbers = "/path/phone/numbers.txt";
        String[] allArgs = new String[] { "-d="+dictionary, "-p="+phoneNumbers};
        InputReader inputReader = new InputReader(allArgs);

        assertThat(inputReader.getDictionaryFile()).as("Expected a parsed input dictionary").isPresent();
        assertThat(inputReader.getPhoneNumbersFile()).as("Expected a parsed phone number file").isPresent();


        allArgs = new String[] { "-d="+dictionary, "-p="}; // no phone
        inputReader = new InputReader(allArgs);
        assertThat(inputReader.getDictionaryFile()).as("Expected a parsed input dictionary").isPresent();
        assertThat(inputReader.getPhoneNumbersFile()).as("Not expecting a parsed phone number file").isNotPresent();

        allArgs = new String[] { "-d=", "-p="+phoneNumbers}; // no dictionary
        inputReader = new InputReader(allArgs);
        assertThat(inputReader.getPhoneNumbersFile()).as("Expected a parsed phone number file").isPresent();
        assertThat(inputReader.getDictionaryFile()).as("Not expecting a parsed dictionary file").isNotPresent();

        allArgs = new String[] { "-d=", "-p="}; // nothing
        inputReader = new InputReader(allArgs);
        assertThat(inputReader.getDictionaryFile()).as("Not expecting a parsed dictionary file").isNotPresent();
        assertThat(inputReader.getPhoneNumbersFile()).as("Not expecting a parsed phone number file").isNotPresent();

        allArgs = new String[] { "", "-p="}; // nothing still
        inputReader = new InputReader(allArgs);
        assertThat(inputReader.getDictionaryFile()).as("Not expecting a parsed dictionary file").isNotPresent();
        assertThat(inputReader.getPhoneNumbersFile()).as("Not expecting a parsed phone number file").isNotPresent();


    }

}
