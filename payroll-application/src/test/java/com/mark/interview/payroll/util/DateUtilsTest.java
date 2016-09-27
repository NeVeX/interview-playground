package com.mark.interview.payroll.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class DateUtilsTest {

    @Test
    public void validateParsingDatesToStrings() {
        LocalDate parsedDate = DateUtils.tryParseDateFromString("03/10/2016");
        LocalDate expectedDate = LocalDate.of(2016, Month.OCTOBER, 3);
        assertEquals(parsedDate, expectedDate);
        // swap the month and parsedDate around
        parsedDate = DateUtils.tryParseDateFromString("10/03/2016");
        expectedDate = LocalDate.of(2016, Month.MARCH, 10);
        assertEquals(parsedDate, expectedDate);
        // Don't include the year
        parsedDate = DateUtils.tryParseDateFromString("10/03");
        assertNull("Did not expect a parsed date", parsedDate);
        // Don't include the month (which is like not including the day)
        parsedDate = DateUtils.tryParseDateFromString("10/2016");
        assertNull("Did not expect a parsed date", parsedDate);
        // Give a bad month day
        parsedDate = DateUtils.tryParseDateFromString("34/10/2016");
        assertNull("Did not expect a parsed date", parsedDate);
        // Give a bad month
        parsedDate = DateUtils.tryParseDateFromString("01/44/2016");
        assertNull("Did not expect a parsed date", parsedDate);
        // Give a bad year
        parsedDate = DateUtils.tryParseDateFromString("01/10/44444");
        assertNull("Did not expect a parsed date", parsedDate);
        // Give text (not a date)
        parsedDate = DateUtils.tryParseDateFromString("i am a nice string");
        assertNull("Did not expect a parsed date", parsedDate);
    }

    @Test
    public void validateFormattingDates() {
        LocalDate date = LocalDate.of(2016, Month.JULY, 10);
        String dateString = DateUtils.formatDate(date);
        assertEquals("10/07/2016", dateString);
    }

    @Test
    public void validateGettingDaysBetweenDates() {
        LocalDate earlyDate = LocalDate.of(2016, Month.JUNE, 15);
        LocalDate laterDate = LocalDate.of(2016, Month.JUNE, 20);
        long daysBetween = DateUtils.getTotalDaysInclusive(earlyDate, laterDate);
        assertEquals(6, daysBetween);

        // switch the dates around
        daysBetween = DateUtils.getTotalDaysInclusive(laterDate, earlyDate);
        assertEquals(-6, daysBetween); // Same amount of days, just negative

        // try the same date
        daysBetween = DateUtils.getTotalDaysInclusive(earlyDate, earlyDate);
        assertEquals(1, daysBetween);
    }

    @Test
    public void validateCheckingWhenTwoDatesSpanTheSameMonth() {
        // Create dates that span the same month exactly
        LocalDate startDate = LocalDate.of(2016, Month.JUNE, 1);
        LocalDate endDate = LocalDate.of(2016, Month.JUNE, 30);
        boolean isExactMonth = DateUtils.isAFullMonthPeriod(startDate, endDate);
        assertTrue("Expected the 2 dates to span a full month", isExactMonth);

        // change the start date
        startDate = startDate.plusDays(1); // not at the start of the month
        isExactMonth = DateUtils.isAFullMonthPeriod(startDate, endDate);
        assertFalse("Expected the 2 dates to NOT span a full month", isExactMonth);
        startDate = startDate.minusDays(1); // go back one
        endDate = endDate.minusDays(1); // end date is not the end of the month anymore
        isExactMonth = DateUtils.isAFullMonthPeriod(startDate, endDate);
        assertFalse("Expected the 2 dates to NOT span a full month", isExactMonth);

        // Set two dates at the start/end of months - but they are not the same month
        startDate = LocalDate.of(2016, Month.JUNE, 1);
        endDate = LocalDate.of(2016, Month.SEPTEMBER, 30);
        isExactMonth = DateUtils.isAFullMonthPeriod(startDate, endDate);
        assertFalse("Expected the 2 dates to NOT span a full month", isExactMonth); // months are different

        // Set two dates at the start/end of months - but they are not the same year
        startDate = LocalDate.of(2015, Month.JUNE, 1);
        endDate = LocalDate.of(2016, Month.JUNE, 30);
        isExactMonth = DateUtils.isAFullMonthPeriod(startDate, endDate);
        assertFalse("Expected the 2 dates to NOT span a full month", isExactMonth); // months are different
    }

    @Test
    public void validateDateUtilForBoundaryDates() {
        // Create 2 dates
        LocalDate startDate = LocalDate.of(2016, Month.JUNE, 10);
        LocalDate endDate = LocalDate.of(2016, Month.JUNE, 23);
        // Create a date boundary
        LocalDate boundaryStart = LocalDate.of(2016, Month.JUNE, 5);
        LocalDate boundaryEnd = LocalDate.of(2016, Month.JUNE, 30);
        boolean isWithinBounds = DateUtils.isDateRangeWithinGivenBounds(startDate, endDate, boundaryStart, boundaryEnd);
        assertTrue("Expected dates to be within bounds", isWithinBounds);

        // move the boundary to fit outside the dates
        boundaryStart = LocalDate.of(2016, Month.JUNE, 20); // Only one of the dates is within the boundary now
        isWithinBounds = DateUtils.isDateRangeWithinGivenBounds(startDate, endDate, boundaryStart, boundaryEnd);
        assertFalse("Expected dates to NOT be within bounds", isWithinBounds);

        startDate = endDate = boundaryStart = boundaryEnd; // all dates are the same
        isWithinBounds = DateUtils.isDateRangeWithinGivenBounds(startDate, endDate, boundaryStart, boundaryEnd);
        assertTrue("Expected dates to be within bounds", isWithinBounds);
    }

}
