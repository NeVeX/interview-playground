package com.mark.flowershop.input;

import com.mark.flowershop.bundle.BundleAmount;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class OrderBundleProcessorTest {

    private OrderBundleProcessor defaultOrderBundleProcessor;

    @Before
    public void before() {
        defaultOrderBundleProcessor = new OrderBundleProcessor();
    }
    @Test
    public void assertOrderProcessingOfValidInputOrder() {
        int orderSize = 5;
        String product = "R12";
        InputOrderBundle inputOrderBundle = new InputOrderBundle(orderSize, product);

        InputOrderBundleResult result = defaultOrderBundleProcessor.processOrder(inputOrderBundle);
        assertThat(result).isNotNull();
        assertThat(result.getInputOrderBundle().getOrderSize()).isEqualTo(orderSize);
        assertThat(result.getInputOrderBundle().getProductCode()).isEqualTo(product);
        assertThat(result.isValidOrder()).isTrue();
        assertThat(result.getResult().getTotalPrice()).isEqualTo(new BigDecimal("6.99"));

        List<BundleAmount> bundleAmounts = result.getResult().getBundleAmounts();
        assertThat(bundleAmounts.size()).isEqualTo(1);

        BundleAmount singleAmount = bundleAmounts.get(0);
        assertThat(singleAmount.getAmount()).isEqualTo(1); // Only one bundle amount expected
        assertThat(singleAmount.getBundle().getPrice()).isEqualTo(new BigDecimal("6.99"));
    }

    @Test
    public void assertInvalidOrderReturnsWithInvalidStatus() {
        int orderSize = 10;
        String code = "this does not exist";
        InputOrderBundle inputOrderBundle = new InputOrderBundle(orderSize, code);

        InputOrderBundleResult result = defaultOrderBundleProcessor.processOrder(inputOrderBundle);
        assertThat(result.isValidOrder()).isFalse(); // this order should not be valid
    }

    @Test
    public void assertNoBundleExistsStatusForBundleThatCannotBeFulfilled() {
        int orderSize = 133;
        String code = "R12";
        InputOrderBundle inputOrderBundle = new InputOrderBundle(orderSize, code);

        InputOrderBundleResult result = defaultOrderBundleProcessor.processOrder(inputOrderBundle);
        assertThat(result.isValidOrder()).isTrue(); // this order should still be valid
        assertThat(result.doesBundleExistForOrder()).isFalse(); // but no bundle will exist
    }
}
