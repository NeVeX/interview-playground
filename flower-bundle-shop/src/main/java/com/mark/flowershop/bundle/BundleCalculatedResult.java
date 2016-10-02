package com.mark.flowershop.bundle;

import java.util.Optional;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class BundleCalculatedResult {

    private final BundleOptions bundleOptions;

    public BundleCalculatedResult() {
        bundleOptions = null;
    }

    public BundleCalculatedResult(BundleOptions bundleOptions) {
        this.bundleOptions = bundleOptions;
    }

    public boolean doesBundleOptionExist() {
        return bundleOptions != null;
    }

    public Optional<BundleOptions> getBundleOptions() {
        return Optional.ofNullable(bundleOptions);
    }

}
