package com.mark.flowershop.bundle;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class BundleCalculatedResultTest {

    @Test
    public void assertCalculatedResultAddsUpCorrectly() {

        BundleAmount bundleAmountOne = new BundleAmount(1, new Bundle(2, new BigDecimal("10.50")));
        BundleAmount bundleAmountTwo = new BundleAmount(2, new Bundle(2, new BigDecimal("20.00")));
        BundleAmount bundleAmountThree = new BundleAmount(3, new Bundle(2, new BigDecimal("2.00")));

        List<BundleAmount> allBundles = new ArrayList<>();
        allBundles.add(bundleAmountOne);
        allBundles.add(bundleAmountTwo);
        allBundles.add(bundleAmountThree);

        BundleCalculatedResult calculatedResult = new BundleCalculatedResult(allBundles);

        assertThat(calculatedResult.getTotalPrice()).isEqualTo(new BigDecimal("56.50"));

    }

}
