package com.mark.phoneword;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

import java.util.*;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
public class DefaultNumberToLettersConverterTest {


//    DEFAULT_CONVERSION_MAP.put(6, new HashSet<>(Arrays.asList('m', 'n', 'o')));
//    DEFAULT_CONVERSION_MAP.put(7, new HashSet<>(Arrays.asList('p', 'q', 'r', 's')));
//    DEFAULT_CONVERSION_MAP.put(8, new HashSet<>(Arrays.asList('t', 'u', 'v')));
//    DEFAULT_CONVERSION_MAP.put(9, new HashSet<>(Arrays.asList('w', 'x', 'y', 'z')));

    @Test
    public void assertUpTo10DigitConversionWorks() {
        int number = 2;
        NumberToLettersConverter lettersConverter = new DefaultNumberToLettersConverter();
        assertThat(lettersConverter.convert(number).isEmpty()).isTrue();

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
        NumberToLettersConverter numberToLettersConverter = new DefaultNumberToLettersConverter();
        assertThat(numberToLettersConverter.convert(number)).contains("a0");
    }

    @Test
    public void assertDigitOneDoesNotGetConverted() {
        long number = 21;
        NumberToLettersConverter numberToLettersConverter = new DefaultNumberToLettersConverter();
        assertThat(numberToLettersConverter.convert(number)).contains("a1");
    }

    @Test
    public void assertPhoneNumberLengthConversionWorks() {
        long phoneNumber = 2223334444L; // A typical phone number size
        NumberToLettersConverter numberToLettersConverter = new DefaultNumberToLettersConverter();
        assertThat(numberToLettersConverter.convert(phoneNumber)).contains("aaadddgggg");

    }

    @Test
    public void assertGiven_CALLME_exampleWorks() {
        long callme = 225563;
        NumberToLettersConverter numberToLettersConverter = new DefaultNumberToLettersConverter();
        assertThat(numberToLettersConverter.convert(callme)).contains("callme");

    }
}
