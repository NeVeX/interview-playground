package com.mark.flowershop.product;

import com.mark.flowershop.bundle.BundleOptions;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mark Cunningham on 10/2/2016.
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

    public Flower getFlower() {
        return flower;
    }

    public BundleOptions getBundleOptions() {
        return bundleOptions;
    }
}
