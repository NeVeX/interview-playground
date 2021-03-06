package com.mark.phoneword.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class NumberUtilsTest {

    @Test
    public void assertNumberSplittingWorks() {
        long number = 1;
        List<Byte> expectedNumbers = new ArrayList<>();
        expectedNumbers.add((byte)1);

        List<Byte> splitNumbers = NumberUtils.splitToList(number);
        assertContainsExactly(splitNumbers, expectedNumbers);

        // add another digit
        number = 12;
        expectedNumbers.add((byte)2); // expect after the above
        splitNumbers = NumberUtils.splitToList(number);
        assertContainsExactly(splitNumbers, expectedNumbers);

        // add more digits
        number = 12567;
        expectedNumbers.addAll(Arrays.asList((byte)5, (byte)6, (byte)7));
        splitNumbers = NumberUtils.splitToList(number);
        assertContainsExactly(splitNumbers, expectedNumbers);

        // make sure zero works
        number = 0;
        expectedNumbers.clear();
        expectedNumbers.add((byte)0);
        splitNumbers = NumberUtils.splitToList(number);
        assertContainsExactly(splitNumbers, expectedNumbers);

        // negative numbers - they should not parse
        number = -789;
        expectedNumbers.clear();
        splitNumbers = NumberUtils.splitToList(number);
        assertContainsExactly(splitNumbers, expectedNumbers);
    }

    @Test
    public void assertLongConversionWorks() {
        String longString = "1234";
        assertThat(NumberUtils.tryConvert(longString)).isEqualTo(Optional.of(1234L));
        longString = "12345       ";
        assertThat(NumberUtils.tryConvert(longString)).isEqualTo(Optional.of(12345L));
        longString = "abc";
        assertThat(NumberUtils.tryConvert(longString)).isEqualTo(Optional.empty());
        longString = "-45";
        assertThat(NumberUtils.tryConvert(longString)).isEqualTo(Optional.of(-45L));
    }

    /**
     * Checks the two lists have exactly the same elements in the same order
     * @param actualList
     * @param expectedList
     */
    private void assertContainsExactly(List<Byte> actualList, List<Byte> expectedList) {
        assertThat(actualList).containsExactly(expectedList.toArray(new Byte[expectedList.size()]));
    }

}
