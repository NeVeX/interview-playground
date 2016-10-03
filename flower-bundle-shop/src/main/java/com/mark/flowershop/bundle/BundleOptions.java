package com.mark.flowershop.bundle;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>This class stores the allowable options for a {@link com.mark.flowershop.product.Product}'s {@link Bundle}
 */
public final class BundleOptions {

    private final Set<Bundle> bundles;
    private final int minimumBundleSize;

    /**
     * Creating an instance needs the {@link Builder} to do so
     */
    private BundleOptions(Builder builder) {
        this.bundles = builder.bundles;
        // Get some statistics on the provided bundles using the steams api
        IntSummaryStatistics entryStatistics = this.bundles.parallelStream().mapToInt(Bundle::getSize).summaryStatistics();
        this.minimumBundleSize = entryStatistics.getMin();
    }

    /**
     * Returns a list of the bundles in decreasing bundle size
     */
    List<Bundle> getBundlesInDecreasingSize() {
        return this.bundles.stream()
                .sorted( (bundle1, bundle12) -> (bundle1.getSize() - bundle12.getSize())* -1)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the given number is less the minimum size bundle for this amount
     */
    boolean isSizeLessThanMinimumBundleSize(int size) {
        return size < minimumBundleSize;
    }

    /**
     * Returns an Optional that represents if a Bundle was found for the exact size given
     */
    Optional<Bundle> getBundleForSize(int bundleSize) {
        return bundles.parallelStream().filter(bundle -> bundle.getSize() == bundleSize).findFirst();
    }

    /**
     * Wrapper for the method {{@link #getBundleForSize(int)}} that checks if it does exist
     */
    boolean hasExactBundleSize(int bundleSize) {
        return getBundleForSize(bundleSize).isPresent();
    }

    /**
     * Returns a new instance of the Builder that can be used to build a new instance of {@link BundleOptions}
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class to help with creating BundleOptions
     */
    public static class Builder {
        private Set<Bundle> bundles = new HashSet<>();

        Builder() { }

        Builder addBundleEntry(int size, BigDecimal price) {
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
