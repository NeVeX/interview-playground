package com.mark.flowershop.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by mcunningham on 10/3/2016.
 * <br>Class that offers a set of utilities relating to Currencies
 */
public class CurrencyUtils {

    private final static NumberFormat AUD_NUMBER_FORMAT;

    static {
        // Default number/currency will be the Australian dollar
        AUD_NUMBER_FORMAT = NumberFormat.getCurrencyInstance(new Locale("en", "AU"));
        AUD_NUMBER_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
    }

    /**
     * Format the given BigDecimal and get a currency string.
     * E.g: For 3.445; this will return "$3.45" - Rounding is "HALF_UP"
     */
    public static String convertToDefaultCurrency(BigDecimal bigDecimal) {
        return AUD_NUMBER_FORMAT.format(bigDecimal.doubleValue());
    }

}
