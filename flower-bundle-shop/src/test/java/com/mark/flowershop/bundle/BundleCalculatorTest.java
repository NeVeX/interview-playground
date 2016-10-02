package com.mark.flowershop.bundle;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class BundleCalculatorTest {

    @Test
    public void assertGivenBundleSizeThatEqualsAnExactBundleThatItIsSelected() {

        int bundleRequested = 10;
        BundleOptions bundleOptions = BundleOptions.builder().addBundleEntry(10, BigDecimal.TEN).build();

        BundleCalculator bundleCalculator = new BundleCalculator();
        Set<Bundle> calculatedBundles = bundleCalculator.calculateBundle(bundleRequested, bundleOptions);

        assertThat(calculatedBundles).hasSize(1); // Only one bundle expected

        Set<Bundle> expectedBundles = new HashSet<>();
        expectedBundles.add(new Bundle(10, BigDecimal.TEN));

        assertThat(expectedBundles).containsAll(expectedBundles); // Only one bundle expected
    }

    

}
