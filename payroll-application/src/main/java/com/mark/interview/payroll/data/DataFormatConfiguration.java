package com.mark.interview.payroll.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class DataFormatConfiguration {

    /**
     * The date format that the application expects all date strings to be in
     */
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    /**
     * Constant values that are helpful throughout the application
     */
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    public static final BigDecimal TWELVE_MONTHS = new BigDecimal(12);
    public static final int DEFAULT_DECIMAL_SCALE = 32; // The default scale for all decimal calculations
    public static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP; // default rounding for decimal calculations

}
