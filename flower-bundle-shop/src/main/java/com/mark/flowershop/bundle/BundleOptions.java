package com.mark.flowershop.bundle;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public final class BundleOptions {

    private final Set<Bundle> bundles;
    private final int minimumBundleSize;
    private final int maximumBundleSize;

    private BundleOptions(Builder builder) {
        this.bundles = builder.bundles;
        // Get some statistics on the provided bundles using the steams api
        IntSummaryStatistics entryStatistics = this.bundles.parallelStream().mapToInt(Bundle::getSize).summaryStatistics();
        this.minimumBundleSize = entryStatistics.getMin();
        this.maximumBundleSize = entryStatistics.getMax();
    }

    public BigDecimal getPrice() {
        return bundles.parallelStream().map(Bundle::getPrice).reduce(BigDecimal::add).get();
    }

    public Set<Bundle> getBundles() {
        return bundles;
    }

    public List<Bundle> getBundlesInDecreasingSize() {
        return this.bundles.stream()
                .sorted( (bundle1, bundle12) -> (bundle1.getSize() - bundle12.getSize())* -1)
                .collect(Collectors.toList());
    }

    public boolean isSizeLessThanMinimumBundleSize(int size) {
        return size < minimumBundleSize;
    }

    public Optional<Bundle> getBundleForSize(int bundleSize) {
        return bundles.parallelStream().filter(bundle -> bundle.getSize() == bundleSize).findFirst();
    }

    public boolean hasExactBundleSize(int bundleSize) {
        return getBundleForSize(bundleSize).isPresent();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Set<Bundle> bundles = new HashSet<>();

        Builder() { }

        public Builder addBundleEntry(int size, BigDecimal price) {
            this.bundles.add(new Bundle(size, price));
            return this;
        }

        public Builder withBundles(Set<Bundle> bundles) {
            if ( bundles != null) {
                this.bundles.addAll(bundles);
            }
            return this;
        }

        public BundleOptions build() {
            BundleOptions newBundleOptions = new BundleOptions(this); // Create a new instance and then validate for thread safety
            if ( newBundleOptions.bundles.isEmpty()) {
                throw new IllegalArgumentException("Provided bundle cannot be empty");
            }
            return newBundleOptions;
        }
    }
}
