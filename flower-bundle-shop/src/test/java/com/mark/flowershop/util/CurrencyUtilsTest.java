package com.mark.flowershop.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mcunningham on 10/3/2016.
 */
public class CurrencyUtilsTest {

    @Test
    public void assertCurrencyConversionsAreWorking() {
        BigDecimal number = new BigDecimal("12.11");
        String numberString = CurrencyUtils.convertToDefaultCurrency(number);
        assertThat(numberString).isEqualTo("$12.11");

        // make sure precisions are ok
        number = new BigDecimal("12.1");
        numberString = CurrencyUtils.convertToDefaultCurrency(number);
        assertThat(numberString).isEqualTo("$12.10");

        number = new BigDecimal("12");
        numberString = CurrencyUtils.convertToDefaultCurrency(number);
        assertThat(numberString).isEqualTo("$12.00");

        number = new BigDecimal("12.");
        numberString = CurrencyUtils.convertToDefaultCurrency(number);
        assertThat(numberString).isEqualTo("$12.00");

        number = new BigDecimal("12.1234");
        numberString = CurrencyUtils.convertToDefaultCurrency(number);
        assertThat(numberString).isEqualTo("$12.12"); // should round down

        number = new BigDecimal("12.1254");
        numberString = CurrencyUtils.convertToDefaultCurrency(number);
        assertThat(numberString).isEqualTo("$12.13"); // should round up

    }
}
