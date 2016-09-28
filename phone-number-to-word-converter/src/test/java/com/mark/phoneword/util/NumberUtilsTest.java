package com.mark.phoneword.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/28/2016.
 */
public class NumberUtilsTest {

    @Test
    public void assertNumberSplittingWorks() {
        int number = 1;
        List<Integer> expectedNumbers = new ArrayList<>();
        expectedNumbers.add(1);

        List<Integer> splitNumbers = NumberUtils.splitToList(number);
        assertContainsExactly(splitNumbers, expectedNumbers);

        // add another digit
        number = 12;
        expectedNumbers.add(2); // expect after the above
        splitNumbers = NumberUtils.splitToList(number);
        assertContainsExactly(splitNumbers, expectedNumbers);

        // add more digits
        number = 12567;
        expectedNumbers.addAll(Arrays.asList(5, 6, 7));
        splitNumbers = NumberUtils.splitToList(number);
        assertContainsExactly(splitNumbers, expectedNumbers);

        // make sure zero works
        number = 0;
        expectedNumbers.clear();
        expectedNumbers.add(0);
        splitNumbers = NumberUtils.splitToList(number);
        assertContainsExactly(splitNumbers, expectedNumbers);

        // negative numbers - they should not parse
        number = -789;
        expectedNumbers.clear();
        splitNumbers = NumberUtils.splitToList(number);
        assertContainsExactly(splitNumbers, expectedNumbers);
    }

    /**
     * Checks the two lists have exactly the same elements in the same order
     * @param actualList
     * @param expectedList
     */
    private void assertContainsExactly(List<Integer> actualList, List<Integer> expectedList) {
        assertThat(actualList).containsExactly(expectedList.toArray(new Integer[expectedList.size()]));
    }

}
