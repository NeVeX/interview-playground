package com.mark.flowershop.bundle;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Optional;
import java.util.Set;

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

    public int getMinimumBundleSize() {
        return minimumBundleSize;
    }

    public int getMaximumBundleSize() {
        return maximumBundleSize;
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
