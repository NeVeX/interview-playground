package com.mark.flowershop.bundle;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class BundleCalculator {

    public Set<Bundle> calculateBundle(int orderSize, BundleOptions bundleOptions) {
        Set<Bundle> calculatedBundles = new HashSet<>();
        if ( bundleOptions.hasExactBundleSize(orderSize)) {
            calculatedBundles.add(bundleOptions.getBundleForSize(orderSize).get());
        }
        return calculatedBundles;
    }

}
