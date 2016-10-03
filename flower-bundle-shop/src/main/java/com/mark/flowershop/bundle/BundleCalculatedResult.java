package com.mark.flowershop.bundle;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class BundleCalculatedResult {

    private final List<BundleAmount> bundleAmounts;

    public BundleCalculatedResult(List<BundleAmount> bundleAmounts) {
        if ( bundleAmounts == null || bundleAmounts.isEmpty()) {
            throw new IllegalArgumentException("Provided bundle amounts cannot be null or empty");
        }
        this.bundleAmounts = Collections.unmodifiableList(bundleAmounts);
    }

    public List<BundleAmount> getBundleAmounts() {
        return bundleAmounts;
    }

    public BigDecimal getPrice() {
        return this.bundleAmounts.stream()
            .map(bundleEntry ->
                // Calculate the price by multiply the price by the count of the bundle
                bundleEntry.getBundle().getPrice().multiply(BigDecimal.valueOf(bundleEntry.getAmount()))
            )
            .reduce(BigDecimal::add)
            .get();
    }

}
