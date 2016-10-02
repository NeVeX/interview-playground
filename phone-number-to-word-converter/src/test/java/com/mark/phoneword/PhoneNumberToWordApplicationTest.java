package com.mark.phoneword;

import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/1/2016.
 */
public class PhoneNumberToWordApplicationTest {

    @Test
    public void assertHappyPathWorksForBatchProcessingOfPhoneNumbers() {
        // Create a phone number file
        String phoneNumbersText = "225563\n12255";
        File inputPhoneNumbersFile = TestingUtils.createRandomFileWithData(phoneNumbersText);

        String[] inputArgs = new String[]{ "-p="+inputPhoneNumbersFile.getAbsolutePath() };

        PhoneNumberToWordApplication application = new PhoneNumberToWordApplication();
        int exitCode = application.run(inputArgs);
        assertThat(exitCode).isEqualTo(0); // OK status expected (meaning everything was fine)

    }

    @Test
    public void assertHappyPathWorksForBatchProcessingWithInputDictionaryAndPhoneNumbers() {
        // Create a dictionary file
        String dictionaryInputText = "mark\nhello\nword";
        File inputDictionaryFile = TestingUtils.createRandomFileWithData(dictionaryInputText);

        // Create a phone number file
        String phoneNumbersText = "225563\n12255";
        File inputPhoneNumbersFile = TestingUtils.createRandomFileWithData(phoneNumbersText);

        // give the dictionary and phone as input
        String[] inputArgs = new String[] {
                "-d="+inputDictionaryFile.getAbsolutePath() ,
                "-p="+inputPhoneNumbersFile.getAbsolutePath()
        };

        PhoneNumberToWordApplication application = new PhoneNumberToWordApplication();
        int exitCode = application.run(inputArgs);
        assertThat(exitCode).isEqualTo(0); // OK status expected (meaning everything was fine)

    }

    @Test
    public void assertExitStatusForBadInputDictionary() {

        String[] inputArgs = new String[] { "-d=/bad/input" };
        PhoneNumberToWordApplication application = new PhoneNumberToWordApplication();
        int exitCode = application.run(inputArgs);
        assertThat(exitCode).isEqualTo(1); // exit status that states bad dictionary load

        // Give an input file with no words too
        File dictionaryInputFile = TestingUtils.createRandomFileWithData("");
        inputArgs = new String[] { "-d="+dictionaryInputFile.getAbsolutePath() };
        exitCode = application.run(inputArgs);
        assertThat(exitCode).isEqualTo(1); // exit status that states bad dictionary load

        // Give input with bad contents (no words ultimately)
        dictionaryInputFile = TestingUtils.createRandomFileWithData("123456\n!!#$%");
        inputArgs = new String[] { "-d="+dictionaryInputFile.getAbsolutePath() };
        exitCode = application.run(inputArgs);
        assertThat(exitCode).isEqualTo(1); // exit status that states bad dictionary load
    }

    @Test
    public void assertExitStatusForBadPhoneNumbersInput() {

        String[] inputArgs = new String[] { "-p=/bad/numbers" };
        PhoneNumberToWordApplication application = new PhoneNumberToWordApplication();
        int exitCode = application.run(inputArgs);
        assertThat(exitCode).isEqualTo(2); // exit status that states bad/invalid phone numbers load

        // Give an input file with no phone numbers
        File phoneNumberInputFile = TestingUtils.createRandomFileWithData("");
        inputArgs = new String[] { "-p="+phoneNumberInputFile.getAbsolutePath() };
        exitCode = application.run(inputArgs);
        assertThat(exitCode).isEqualTo(2); // exit status that states bad/invalid phone numbers load

        // Give input with bad contents (no phone numbers ultimately)
        phoneNumberInputFile = TestingUtils.createRandomFileWithData("avcdr\n!!#$%");
        inputArgs = new String[] { "-p="+phoneNumberInputFile.getAbsolutePath() };
        exitCode = application.run(inputArgs);
        assertThat(exitCode).isEqualTo(2); // exit status that states bad/invalid phone numbers load
    }

}
