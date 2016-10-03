package com.mark.flowershop.bundle;

import java.util.*;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class BundleCalculator {

    public Optional<BundleCalculatedResult> calculateBundle(int orderSize, BundleOptions bundleOptions) {
        Map<Integer, Bundle> calculatedBundles = new HashMap<>();
        if ( bundleOptions.hasExactBundleSize(orderSize)) {
            Bundle exactBundle = bundleOptions.getBundleForSize(orderSize).get();
            calculatedBundles.put(1, exactBundle);
        }
        if ( calculatedBundles.isEmpty()) {
            return Optional.empty();
        }

        BundleCalculatedResult result = new BundleCalculatedResult(calculatedBundles);
        return Optional.of(result);
    }

}
