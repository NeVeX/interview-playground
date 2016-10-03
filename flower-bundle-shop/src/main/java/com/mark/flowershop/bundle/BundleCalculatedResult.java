package com.mark.flowershop.bundle;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class BundleCalculatedResult {

    private final Map<Integer, Bundle> bundleMap;

    public BundleCalculatedResult(Map<Integer, Bundle> bundleMap) {
        if ( bundleMap == null || bundleMap.isEmpty()) {
            throw new IllegalArgumentException("Provided bundle map cannot be null or empty");
        }
        this.bundleMap = Collections.unmodifiableMap(bundleMap);
    }

    public Map<Integer, Bundle> getBundles() {
        return bundleMap;
    }

    public BigDecimal getPrice() {
        return this.bundleMap.entrySet().stream()
            .map(bundleEntry ->
                // Calculate the price by multiply the price by the count of the bundle
                bundleEntry.getValue().getPrice().multiply(BigDecimal.valueOf(bundleEntry.getKey()))
            )
            .reduce(BigDecimal::add)
            .get();
    }

}
