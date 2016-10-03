package com.mark.flowershop.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by mcunningham on 10/3/2016.
 */
public class CurrencyUtils {

    private final static NumberFormat AUD_NUMBER_FORMAT;

    static {
        AUD_NUMBER_FORMAT = NumberFormat.getCurrencyInstance(new Locale("en", "AU"));
        AUD_NUMBER_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
    }

    public static String convertToDefaultCurrency(BigDecimal bigDecimal) {
        return AUD_NUMBER_FORMAT.format(bigDecimal.doubleValue());
    }

}
