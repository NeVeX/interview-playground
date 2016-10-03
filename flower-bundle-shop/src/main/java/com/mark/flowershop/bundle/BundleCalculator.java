package com.mark.flowershop.bundle;

import java.util.*;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class BundleCalculator {

    public Optional<BundleCalculatedResult> calculateBundle(int orderSize, BundleOptions bundleOptions) {
        List<BundleAmount> calculatedBundles = new ArrayList<>();

        //Check the edge cases
        if ( bundleOptions.isSizeLessThanMinimumBundleSize(orderSize)) {
            // No bundle can fulfill this order
            return Optional.empty();
        } else if ( bundleOptions.hasExactBundleSize(orderSize)) {
            // Check if the order size is equal to exactly one bundle, if so, we are golden!
            Bundle exactBundle = bundleOptions.getBundleForSize(orderSize).get();
            calculatedBundles.add(new BundleAmount(1, exactBundle));
        } else {
            calculatedBundles = calculateSuitableBundles(orderSize, bundleOptions);
        }

        if ( calculatedBundles.isEmpty()) {
            return Optional.empty();
        }

        BundleCalculatedResult result = new BundleCalculatedResult(calculatedBundles);
        return Optional.of(result);
    }

    private List<BundleAmount> calculateSuitableBundles(int orderSize, BundleOptions bundleOptions) {

        List<Bundle> decreasingBundleSizeList = bundleOptions.getBundlesInDecreasingSize();

        for ( int index = 0; index < decreasingBundleSizeList.size(); index++) {

            List<BundleAmount> currentBundles = calculateSuitableBundleFromIndex(orderSize, index, decreasingBundleSizeList);
            if ( currentBundles != null && !currentBundles.isEmpty()) {
                // Looks like we got our bundles that statisfy the order, so we can exit this
                return currentBundles;
            }
            // If we are here, we couldn't satisfy the bundle request, so let's retry, moving down the bundle list
        }
        // If we are here, the above logic could not find a suitable bundle, so we cannot statisfy the order :-(
        return new ArrayList<>(); // Nothing was found
    }

    private List<BundleAmount> calculateSuitableBundleFromIndex(int orderSize, int startIndex, List<Bundle> decreasingBundleSizeList) {
        int bundleSizeWanted = orderSize;
        List<BundleAmount> currentBundles = new ArrayList<>();
        for ( int index = startIndex; index < decreasingBundleSizeList.size(); index++) {
            Bundle currentBundle = decreasingBundleSizeList.get(index);
            int bundleCount = (int) ((double) bundleSizeWanted / (double) currentBundle.getSize());
            if (bundleCount > 0) {
                // We can use this bundle
                currentBundles.add(new BundleAmount(bundleCount, currentBundle));
                bundleSizeWanted -= ( bundleCount * currentBundle.getSize());
            }
            if (bundleSizeWanted == 0 ) {
                // all done
                break;
            }

            if (index == decreasingBundleSizeList.size() - 1) {
                // We are the last index in the list and we have not found a bundle that suited the order
                // So, if possible, move down the list of the largest bundle and start again
                currentBundles.clear();
            }
        }
        return currentBundles;
    }


}
