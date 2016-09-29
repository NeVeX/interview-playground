package com.mark.phoneword;

import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/27/2016.
 */
public class NumberToWordConverterTest {

    @Test
    public void assertExampleRequirementWorks_CALLME() {

        int number = 225563;
        NumberToWordConverter numberToWordConverter = new NumberToWordConverter();
        Set<String> matches = numberToWordConverter.convert(number);
        assertThat(matches.isEmpty()).isFalse();
    }

}
