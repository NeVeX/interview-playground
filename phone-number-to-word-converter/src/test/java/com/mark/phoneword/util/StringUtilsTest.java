package com.mark.phoneword.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}
