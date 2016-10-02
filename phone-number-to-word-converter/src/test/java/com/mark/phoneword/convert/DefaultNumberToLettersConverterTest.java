package com.mark.phoneword.convert;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
public class DefaultNumberToLettersConverterTest {

    @Test
    public void assertUpTo10DigitConversionWorks() {
        long number = 2;
        Converter<Long, String> lettersConverter = ConverterFactory.longNumberToLetters();
        assertThat(lettersConverter.convert(number)).isEmpty();

        number = 23; // We'll expect a certain string
        assertThat(lettersConverter.convert(number)).contains("ad");

        number = 234;
        assertThat(lettersConverter.convert(number)).contains("adg");

        number = 2345;
        assertThat(lettersConverter.convert(number)).contains("adgj");

        number = 2345;
        assertThat(lettersConverter.convert(number)).contains("adgj");

        number = 23456;
        assertThat(lettersConverter.convert(number)).contains("adgjm");

        number = 234567;
        assertThat(lettersConverter.convert(number)).contains("adgjmp");

        number = 2345678;
        assertThat(lettersConverter.convert(number)).contains("adgjmpt");

        number = 23456789;
        assertThat(lettersConverter.convert(number)).contains("adgjmptw");

        number = 234567890;
        assertThat(lettersConverter.convert(number)).contains("adgjmptw0"); // Zero won't be converted to a char

        number = 234567891;
        assertThat(lettersConverter.convert(number)).contains("adgjmptw1"); // Zero won't be converted to a char
    }

    @Test
    public void assertDigitZeroDoesNotGetConverted() {
        long number = 20;
        Converter<Long, String> numberToLettersConverter = ConverterFactory.longNumberToLetters();
        assertThat(numberToLettersConverter.convert(number)).contains("a0");
    }

    @Test
    public void assertDigitOneDoesNotGetConverted() {
        long number = 21;
        Converter<Long, String> numberToLettersConverter = ConverterFactory.longNumberToLetters();
        assertThat(numberToLettersConverter.convert(number)).contains("a1");
    }

    @Test
    public void assertPhoneNumberLengthConversionWorks() {
        long phoneNumber = 2223334444L; // A typical phone number size
        Converter<Long, String> numberToLettersConverter = ConverterFactory.longNumberToLetters();
        assertThat(numberToLettersConverter.convert(phoneNumber)).contains("aaadddgggg");

    }

    @Test
    public void assertGivenRequirementCallMeExampleWorks() {
        long callme = 225563;
        Converter<Long, String> numberToLettersConverter = ConverterFactory.longNumberToLetters();
        assertThat(numberToLettersConverter.convert(callme)).contains("callme");

    }
}
