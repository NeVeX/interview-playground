package com.mark.flowershop.product;

import com.mark.flowershop.bundle.BundleOptions;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>Class the represents a particular flower's bundle options
 */
public class FlowerBundle {

    private final Flower flower;
    private final BundleOptions bundleOptions;

    public FlowerBundle(Flower flower, BundleOptions bundleOptions) {
        if (flower == null) { throw new IllegalArgumentException("Provided flower cannot be null"); }
        if (bundleOptions == null) { throw new IllegalArgumentException("Provided bundleOptions cannot be null"); }
        this.flower = flower;
        this.bundleOptions = bundleOptions;
    }

    /**
     * Get the flower for this bundle
     */
    Flower getFlower() {
        return flower;
    }

    /**
     * Get the available bundle options
     */
    BundleOptions getBundleOptions() {
        return bundleOptions;
    }
}
