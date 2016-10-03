package com.mark.flowershop.input;

import com.mark.flowershop.bundle.Bundle;
import com.mark.flowershop.bundle.BundleAmount;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class OrderProcessorTest {

    private OrderProcessor defaultOrderProcessor;

    @Before
    public void before() {
        defaultOrderProcessor = new OrderProcessor();
    }
    @Test
    public void assertOrderProcessingOfValidInputOrder() {
        int orderSize = 5;
        String product = "R12";
        InputOrder inputOrder = new InputOrder(orderSize, product);

        InputOrderResult result = defaultOrderProcessor.processOrder(inputOrder);
        assertThat(result).isNotNull();
        assertThat(result.getOrderSize()).isEqualTo(orderSize);
        assertThat(result.getProductCode()).isEqualTo(product);
        assertThat(result.isValidOrder()).isTrue();
        assertThat(result.getResult().getPrice()).isEqualTo(new BigDecimal("6.99"));

        List<BundleAmount> bundleAmounts = result.getResult().getBundleAmounts();
        assertThat(bundleAmounts.size()).isEqualTo(1);

        BundleAmount singleAmount = bundleAmounts.get(0);
        assertThat(singleAmount.getAmount()).isEqualTo(1); // Only one bundle amount expected
        assertThat(singleAmount.getBundle().getPrice()).isEqualTo(new BigDecimal("6.99"));
    }
}
