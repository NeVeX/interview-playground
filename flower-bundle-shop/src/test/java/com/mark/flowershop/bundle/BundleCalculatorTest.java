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
        // Given 13, there are multiple ways to build this bundle, but the largest should always be chosen
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

    @Test
    public void assertPricesAreNotRoundedInResult() {
        int orderSize = 18;
        // The below prices are all in lower denominations than cents, so make sure the calculations round to the cent
        BundleOptions bundleOptions = BundleOptions.builder()
                .addBundleEntry(3, new BigDecimal("12.456"))
                .addBundleEntry(5, new BigDecimal("0.6745"))
                .addBundleEntry(10, new BigDecimal("20.333"))
                .build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(orderSize, bundleOptions);

        assertThat(calcOptional.get().getTotalPrice()).isEqualTo(new BigDecimal("33.4635")); // no rounding to occur
    }

    @Test
    public void assertLargestAndSmallestBundleIsSelectedForAppropriateOrder() {
        int orderSize = 15;
        // Given 12, we want to make sure that 14 and 1 is selected, not the 3 fives
        BundleOptions bundleOptions = BundleOptions.builder()
                .addBundleEntry(1, BigDecimal.ONE)
                .addBundleEntry(5, BigDecimal.ONE)
                .addBundleEntry(14, BigDecimal.TEN)
                .build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(orderSize, bundleOptions);
        assertThat(calcOptional).isPresent(); // Expect a result

        List<BundleAmount> expectedBundles = new ArrayList<>();
        expectedBundles.add(new BundleAmount(1, new Bundle(1, BigDecimal.ONE)));
        expectedBundles.add(new BundleAmount(1, new Bundle(14, BigDecimal.TEN)));

        BundleCalculatedResult expectedBundleOptions = new BundleCalculatedResult(expectedBundles);

        assertBundlesAreEqual(expectedBundleOptions, calcOptional.get());
    }

    @Test
    public void assertLargeOrderIsBrokenUpInSuitableBundles() {
        int orderSize = 1020;
        // Many ways to break this order up, but the largest should be selected always
        BundleOptions bundleOptions = BundleOptions.builder()
                .addBundleEntry(10, BigDecimal.ONE)
                .addBundleEntry(50, BigDecimal.ONE)
                .addBundleEntry(100, BigDecimal.TEN)
                .build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(orderSize, bundleOptions);
        assertThat(calcOptional).isPresent(); // Expect a result

        List<BundleAmount> expectedBundles = new ArrayList<>();
        expectedBundles.add(new BundleAmount(2, new Bundle(10, BigDecimal.ONE)));
        expectedBundles.add(new BundleAmount(10, new Bundle(100, BigDecimal.TEN)));
        BundleCalculatedResult expectedBundleOptions = new BundleCalculatedResult(expectedBundles);

        assertBundlesAreEqual(expectedBundleOptions, calcOptional.get());
    }

    @Test
    public void assertGivenRosesExampleRequirementWorks() {
        int orderSize = 10; // 10 R12
        BundleOptions roseBundles = BundleOptions.builder()
                .addBundleEntry(5, new BigDecimal("6.99"))  // 5 @ $6.99
                .addBundleEntry(10, new BigDecimal("12.99")) // 10 @ $12.99
                .build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(orderSize, roseBundles);
        assertThat(calcOptional).isPresent();

        List<BundleAmount> expectedBundles = new ArrayList<>();
        expectedBundles.add(new BundleAmount(1, new Bundle(10, new BigDecimal("12.99")))); // 1 x 10 $12.99
        BundleCalculatedResult expectedBundleOptions = new BundleCalculatedResult(expectedBundles);

        assertBundlesAreEqual(expectedBundleOptions, calcOptional.get());
        assertThat(calcOptional.get().getTotalPrice()).isEqualTo(new BigDecimal("12.99"));
    }

    @Test
    public void assertGivenLiliesExampleRequirementWorks() {
        int orderSize = 15; // 15 L09
        BundleOptions roseBundles = BundleOptions.builder()
                .addBundleEntry(3, new BigDecimal("9.95"))  // 3 @ $9.95
                .addBundleEntry(6, new BigDecimal("16.95")) // 6 @ $16.95
                .addBundleEntry(9, new BigDecimal("24.95")) // 9 @ $24.95
                .build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(orderSize, roseBundles);
        assertThat(calcOptional).isPresent();

        List<BundleAmount> expectedBundles = new ArrayList<>();
        expectedBundles.add(new BundleAmount(1, new Bundle(9, new BigDecimal("24.95")))); //  1 x 9 $24.95
        expectedBundles.add(new BundleAmount(1, new Bundle(6, new BigDecimal("16.95")))); //  1 x 6 $16.95
        BundleCalculatedResult expectedBundleOptions = new BundleCalculatedResult(expectedBundles);

        assertBundlesAreEqual(expectedBundleOptions, calcOptional.get());
        assertThat(calcOptional.get().getTotalPrice()).isEqualTo(new BigDecimal("41.90"));
    }

    @Test
    public void assertGivenTulipsExampleRequirementWorks() {
        int orderSize = 13; // 13 T58
        BundleOptions roseBundles = BundleOptions.builder()
                .addBundleEntry(3, new BigDecimal("5.95"))  // 3 @ $5.95
                .addBundleEntry(5, new BigDecimal("9.95")) // 5 @ $9.95
                .addBundleEntry(9, new BigDecimal("16.99")) // 9 @ $16.99
                .build();

        Optional<BundleCalculatedResult> calcOptional = defaultBundleCalculator.calculateBundle(orderSize, roseBundles);
        assertThat(calcOptional).isPresent();

        List<BundleAmount> expectedBundles = new ArrayList<>();
        expectedBundles.add(new BundleAmount(2, new Bundle(5, new BigDecimal("9.95")))); // 2 x 5 $9.95
        expectedBundles.add(new BundleAmount(1, new Bundle(3, new BigDecimal("5.95")))); // 1 x 3 $5.95
        BundleCalculatedResult expectedBundleOptions = new BundleCalculatedResult(expectedBundles);

        assertBundlesAreEqual(expectedBundleOptions, calcOptional.get());
        assertThat(calcOptional.get().getTotalPrice()).isEqualTo(new BigDecimal("25.85"));
    }

    private void assertBundlesAreEqual(BundleCalculatedResult expectedResult, BundleCalculatedResult calculatedResult) {
        assertThat(calculatedResult).isNotNull();
        assertThat(calculatedResult.getTotalPrice()).isEqualTo(expectedResult.getTotalPrice());

        List<BundleAmount> calcBundles = expectedResult.getBundleAmounts();
        List<BundleAmount> expectedBundles = calculatedResult.getBundleAmounts();

        assertThat(expectedBundles).containsAll(calcBundles); // Make sure only the exact expected bundles are calculated
    }



}
