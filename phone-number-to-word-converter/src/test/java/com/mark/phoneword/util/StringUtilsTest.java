package com.mark.phoneword.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

}
