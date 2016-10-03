package com.mark.flowershop.bundle;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class BundleCalculatorTest {

    private BundleCalculator defaultBundleCalculator;

    @Before
    public void before() {
        this.defaultBundleCalculator = new BundleCalculator();
    }

    @Test
    public void assertGivenBundleSizeThatEqualsAnExactBundleThatItIsSelected() {

        int bundleRequested = 10;
        BundleOptions bundleOptions = BundleOptions.builder().addBundleEntry(10, BigDecimal.TEN).build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(bundleRequested, bundleOptions);
        assertThat(calcOptional).isPresent(); // should get a calculated result

        List<BundleAmount> expectedBundles = new ArrayList<>();
        expectedBundles.add(new BundleAmount(1, new Bundle(10, BigDecimal.TEN)));
        BundleCalculatedResult expectedResult = new BundleCalculatedResult(expectedBundles);

        assertBundlesAreEqual(expectedResult, calcOptional.get());
    }

    @Test
    public void assertBundleSizeThatIsTooLowReturnsNoResults() {
        int orderSize = 2;
        // The lowest size is 5 below, hence the above should not get a bundle
        BundleOptions bundleOptions = BundleOptions.builder().addBundleEntry(5, BigDecimal.TEN).build();

        Optional<BundleCalculatedResult> calculatedOptional = defaultBundleCalculator.calculateBundle(orderSize, bundleOptions);
        assertThat(calculatedOptional).isNotPresent(); // Should not get any bundles
    }

    @Test
    public void assertNoBundleReturnedForOrderSizeThatCannotBeBrokenIntoABundle() {
        int orderSize = 20;
        BundleOptions bundleOptions = BundleOptions.builder()
                .addBundleEntry(3, BigDecimal.ONE)
                .addBundleEntry(6, BigDecimal.TEN)
                .addBundleEntry(9, BigDecimal.TEN)
                .build();
        Optional<BundleCalculatedResult> calculatedResult = defaultBundleCalculator.calculateBundle(orderSize, bundleOptions);
        assertThat(calculatedResult).isNotPresent(); // We shouldn't get any calculated results
    }

    @Test
    public void assertOrderCanBeBrokenInto2Bundles() {
        int orderSize = 15;
        BundleOptions bundleOptions = BundleOptions.builder()
                .addBundleEntry(5, BigDecimal.ONE)
                .addBundleEntry(10, BigDecimal.TEN)
                .build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(orderSize, bundleOptions);
        assertThat(calcOptional).isPresent(); // Expect a result

        List<BundleAmount> expectedBundles = new ArrayList<>();
        expectedBundles.add(new BundleAmount(1, new Bundle(5, BigDecimal.ONE)));
        expectedBundles.add(new BundleAmount(1, new Bundle(10, BigDecimal.TEN)));
        BundleCalculatedResult expectedResult = new BundleCalculatedResult(expectedBundles);

        assertBundlesAreEqual(expectedResult, calcOptional.get());
    }

    @Test
    public void assertOrderCanBeBrokenInto3Bundles() {
        int orderSize = 17;
        BundleOptions bundleOptions = BundleOptions.builder()
                .addBundleEntry(2, BigDecimal.ONE)
                .addBundleEntry(5, BigDecimal.ONE)
                .addBundleEntry(10, BigDecimal.TEN)
                .build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(orderSize, bundleOptions);
        assertThat(calcOptional).isPresent(); // Expect a result

        List<BundleAmount> expectedBundles = new ArrayList<>();
        expectedBundles.add(new BundleAmount(1, new Bundle(2, BigDecimal.ONE)));
        expectedBundles.add(new BundleAmount(1, new Bundle(5, BigDecimal.ONE)));
        expectedBundles.add(new BundleAmount(1, new Bundle(10, BigDecimal.TEN)));
        BundleCalculatedResult expectedResult = new BundleCalculatedResult(expectedBundles);

        assertBundlesAreEqual(expectedResult, calcOptional.get());
    }

    @Test
    public void assertOrderIsBrokenIntoBundlesAtTheMaximumSize() {
        int orderSize = 13;
        // 13 above cannot be broken up using the maximum bundle of 9 below, so we expect he 5 + 3 option
        BundleOptions bundleOptions = BundleOptions.builder()
                .addBundleEntry(3, BigDecimal.ONE)
                .addBundleEntry(5, BigDecimal.ONE)
                .addBundleEntry(9, BigDecimal.TEN)
                .build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(orderSize, bundleOptions);
        assertThat(calcOptional).isPresent(); // Expect a result

        List<BundleAmount> expectedBundles = new ArrayList<>();
        expectedBundles.add(new BundleAmount(1, new Bundle(3, BigDecimal.ONE)));
        expectedBundles.add(new BundleAmount(2, new Bundle(5, BigDecimal.ONE))); // We expect 2 of these
        BundleCalculatedResult expectedBundleOptions = new BundleCalculatedResult(expectedBundles);

        assertBundlesAreEqual(expectedBundleOptions, calcOptional.get());
    }

    @Test
    public void assertOrderIsBrokenIntoBundlesAtTheMaximumSizeWhenLowerBundlesExist() {
        int orderSize = 13;
        // Given 13, there are multiple ways to build this bundle, but the largest should always be choosen
        BundleOptions bundleOptions = BundleOptions.builder()
                .addBundleEntry(3, BigDecimal.ONE)
                .addBundleEntry(5, BigDecimal.ONE)
                .addBundleEntry(10, BigDecimal.TEN)
                .build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(orderSize, bundleOptions);
        assertThat(calcOptional).isPresent(); // Expect a result

        List<BundleAmount> expectedBundles = new ArrayList<>();
        expectedBundles.add(new BundleAmount(1, new Bundle(3, BigDecimal.ONE)));
        expectedBundles.add(new BundleAmount(1, new Bundle(10, BigDecimal.TEN))); // We expect 1 of each
        BundleCalculatedResult expectedBundleOptions = new BundleCalculatedResult(expectedBundles);

        assertBundlesAreEqual(expectedBundleOptions, calcOptional.get());
    }

    private void assertBundlesAreEqual(BundleCalculatedResult expectedResult, BundleCalculatedResult calculatedResult) {
        assertThat(calculatedResult).isNotNull();
        assertThat(calculatedResult.getPrice()).isEqualTo(expectedResult.getPrice());


        List<BundleAmount> calcBundles = expectedResult.getBundleAmounts();
        List<BundleAmount> expectedBundles = calculatedResult.getBundleAmounts();

        assertThat(expectedBundles).containsAll(calcBundles); // Make sure only the exact expected bundles are calculated
    }



}
