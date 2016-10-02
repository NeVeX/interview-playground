package com.mark.phoneword.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class StringUtilsTest {

    @Test
    public void assertCharacterBlankChecksWorks() {
        Character character = null;
        assertTrue("Character should be blank", StringUtils.isBlank(character));
        assertFalse("Character should be blank", StringUtils.isNotBlank(character));
        character = ' ';
        assertTrue("Character should be blank (empty space)", StringUtils.isBlank(character));
        assertFalse("Character should be blank (empty space)", StringUtils.isNotBlank(character));
        character = 'm';
        assertFalse("Character should not be blank", StringUtils.isBlank(character));
        assertTrue("Character should not be blank", StringUtils.isNotBlank(character));
    }

    @Test
    public void assertStringBlankChecksWorks() {
        String string = null;
        assertTrue("String should be blank", StringUtils.isBlank(string));
        assertFalse("String should be blank", StringUtils.isNotBlank(string));
        string = " ";
        assertTrue("String should be blank (empty space)", StringUtils.isBlank(string));
        assertFalse("String should be blank (empty space)", StringUtils.isNotBlank(string));
        string = "            "; // bigger space
        assertTrue("String should be blank (empty space)", StringUtils.isBlank(string));
        assertFalse("String should be blank (empty space)", StringUtils.isNotBlank(string));
        string = "m";
        assertFalse("String should not be blank", StringUtils.isBlank(string));
        assertTrue("String should not be blank", StringUtils.isNotBlank(string));
        string = "      m          ";
        assertFalse("String should not be blank", StringUtils.isBlank(string));
        assertTrue("String should not be blank", StringUtils.isNotBlank(string));
    }

    @Test
    public void assertCharacterDigitChecksWorks() {
        Character one = null;
        Character two = null;
        assertFalse("Characters are not digits", StringUtils.areDigits(one, two));
        one = '5';
        assertFalse("One of the characters is not a digit", StringUtils.areDigits(one, two));
        one = null;
        two = '9';
        assertFalse("One of the characters is not a digit", StringUtils.areDigits(one, two));
        one = '4';
        assertTrue("Characters should of been digits", StringUtils.areDigits(one, two));
        one = ' ';
        assertFalse("One of the characters is not a digit", StringUtils.areDigits(one, two));
    }

    @Test
    public void assertGetLettersOnlyWorks() {
        String input = "abcd";
        String output = StringUtils.getLettersOnly(input);
        assertEquals(input, output);

        input = "abcd!!!!";
        output = StringUtils.getLettersOnly(input);
        assertEquals("abcd", output);

        input = "";
        output = StringUtils.getLettersOnly(input);
        assertEquals("", output);

        input = "12345";
        output = StringUtils.getLettersOnly(input);
        assertEquals("", output);

        input = "a2b$c44";
        output = StringUtils.getLettersOnly(input);
        assertEquals("abc", output);

        input = "~!@#$%^*()_+}{\":?><|\\][=-;'/.,`1234567890']      a";
        output = StringUtils.getLettersOnly(input);
        assertEquals("a", output);

    }

    @Test
    public void assertGetNumbersOnlyWorks() {
        String input = "123456";
        String output = StringUtils.getNumbersOnly(input);
        assertEquals(input, output);

        input = "123!!!!";
        output = StringUtils.getNumbersOnly(input);
        assertEquals("123", output);

        input = "";
        output = StringUtils.getNumbersOnly(input);
        assertEquals("", output);

        input = "abcdefg";
        output = StringUtils.getNumbersOnly(input);
        assertEquals("", output);

        input = "a2b$c44";
        output = StringUtils.getNumbersOnly(input);
        assertEquals("244", output);

        input = "~!@#$%^*()_+}{\":?><|\\][=-;'/.,`abcdefghijklmnopqrstuvwxyz']      4";
        output = StringUtils.getNumbersOnly(input);
        assertEquals("4", output);

    }
}
