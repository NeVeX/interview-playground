package com.mark.flowershop.bundle;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>This class represents the result of a given order and bundle options when using the {@link BundleCalculator}
 */
public class BundleCalculatedResult {

    private final List<BundleAmount> bundleAmounts;

    BundleCalculatedResult(List<BundleAmount> bundleAmounts) {
        if ( bundleAmounts == null || bundleAmounts.isEmpty()) {
            throw new IllegalArgumentException("Provided bundle amounts cannot be null or empty");
        }
        this.bundleAmounts = Collections.unmodifiableList(bundleAmounts);
    }

    /**
     * Get all the bundle amounts that pertain to this calculation
     */
    public List<BundleAmount> getBundleAmounts() {
        return bundleAmounts;
    }

    /**
     * Get the total price of all the bundles included in this calculation
     * @return - The BigDecimal that is in it's raw form (no scaling/precision applied)
     */
    public BigDecimal getTotalPrice() {
        return this.bundleAmounts.stream()
            .map(bundleEntry ->
                // Calculate the price by multiply the price by the count of the bundle
                bundleEntry.getBundle().getPrice().multiply(BigDecimal.valueOf(bundleEntry.getAmount()))
            )
            .reduce(BigDecimal::add)
            .get();
    }

}
