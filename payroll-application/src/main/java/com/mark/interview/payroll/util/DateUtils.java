package com.mark.interview.payroll.util;

import com.mark.interview.payroll.data.DataFormatConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by NeVeX on 9/25/2016.
 */
public class DateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    public static LocalDate tryParseDateFromString(String dateAsString) {
        try {
            return LocalDate.parse(dateAsString, DataFormatConfiguration.DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            // Hmm, so the date is probably not in the format we expect
            LOGGER.warn("Could not parse date string [{}]. Format expected is [{}]. Error: {}",
                    dateAsString, DataFormatConfiguration.DATE_FORMAT, e.getMessage());
        }
        return null;
    }

    public static String formatDate(LocalDate date) {
        return date.format(DataFormatConfiguration.DATE_FORMATTER);
    }

    public static long getTotalDaysInclusive(LocalDate startDate, LocalDate endDate) {
        long daysBetween = DAYS.between(startDate, endDate);
        return daysBetween < 0 ? daysBetween - 1 : daysBetween + 1;
    }

    public static boolean isAFullMonthPeriod(LocalDate startDate, LocalDate endDate) {
        return startDate.getDayOfMonth() == 1
                && endDate.lengthOfMonth() == endDate.getDayOfMonth()
                && startDate.getMonth() == endDate.getMonth()
                && startDate.getYear() == endDate.getYear();
    }

    public static boolean isDateRangeWithinGivenBounds(LocalDate startDate, LocalDate endDate,
                                                LocalDate startDateBoundary, LocalDate endDateBoundary) {
        return startDate.compareTo(startDateBoundary) >= 0 && endDate.compareTo(endDateBoundary) <= 0;
    }
}
