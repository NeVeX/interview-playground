package com.mark.flowershop.bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>This class encapsulated the logic for calculating product bundles when given a certain order
 */
public class BundleCalculator {

    /**
     * For the given orderSize and the {@link BundleOptions} to choose from, this will try to calculate the best bundles
     * that result in the fewest bundles required to fulfil the order
     * @param orderSize - The size of the order
     * @param bundleOptions - The bundle options to choose from to fulfil the order
     * @return - An Optional that represents if a suitable bundle was calculated or not
     */
    public Optional<BundleCalculatedResult> calculateBundle(int orderSize, BundleOptions bundleOptions) {
        List<BundleAmount> calculatedBundles = new ArrayList<>();
        //Check the simple edge cases first
        if ( bundleOptions.isSizeLessThanMinimumBundleSize(orderSize)) {
            // No bundle can fulfill this order, so we return from here
            return Optional.empty();
        } else if ( bundleOptions.hasExactBundleSize(orderSize)) {
            // This order size fits into a bundle size exactly, so we are golden, we just return that bundle
            Bundle exactBundle = bundleOptions.getBundleForSize(orderSize).get();
            calculatedBundles.add(new BundleAmount(1, exactBundle));
        } else {
            // So the order size is at least valid for bundles, so let's see if we can find a suitable order
            calculatedBundles = calculateSuitableBundles(orderSize, bundleOptions);
        }

        if ( calculatedBundles.isEmpty()) {
            return Optional.empty(); // No bundles calculated
        }
        BundleCalculatedResult result = new BundleCalculatedResult(calculatedBundles);
        return Optional.of(result);
    }

    /**
     * A continuation of the {{@link #calculateBundle(int, BundleOptions)}} method, that does not check for edge cases.
     * Instead this method will start a workflow, starting with the largest bundle and seeing if permuatations of larger
     * bundles exist.
     * @param orderSize - The size to fulfill of bundles
     * @param bundleOptions - The bundle options to choose from
     * @return - The list of bundle amounts that fulfill the order (or empty if none found)
     */
    private List<BundleAmount> calculateSuitableBundles(int orderSize, BundleOptions bundleOptions) {
        // Get the list of bundles in decreasing size (we want to start with the biggest bundle first)
        List<Bundle> decreasingBundleSizeList = bundleOptions.getBundlesInDecreasingSize();
        for ( int index = 0; index < decreasingBundleSizeList.size(); index++) {
            // For each bundle size, see if that size and below can fulfil the order
            List<BundleAmount> currentBundles = calculateSuitableBundleFromIndex(orderSize, index, decreasingBundleSizeList);
            if ( currentBundles != null && !currentBundles.isEmpty()) {
                // Looks like we got our bundles that satisfy the order, so we can exit this
                return currentBundles;
            }
            // If we are here, we couldn't satisfy the bundle request, so let's retry, moving down the bundle list
        }
        // If we are here, the above logic could not find a suitable bundle, so we cannot satisfy the order :-(
        return new ArrayList<>();
    }

    /**
     * A recursive extension of the method {{@link #calculateSuitableBundles(int, BundleOptions)}} that will start trying
     * to find suitable bundles from the given list, at the specified index given
     * @param orderSize - the size of the order to fulfil
     * @param startIndex - the index to start from ( the first bundle to use)
     * @param decreasingBundleSizeList - the list of avaialable bundles to use (in descending order)
     * @return - The list of bundle amounts that fulfill the order (or empty if none found)
     */
    private List<BundleAmount> calculateSuitableBundleFromIndex(int orderSize, int startIndex, List<Bundle> decreasingBundleSizeList) {
        int bundleSizeWanted = orderSize;
        List<BundleAmount> currentBundles = new ArrayList<>();
        for ( int index = startIndex; index < decreasingBundleSizeList.size(); index++) {
            // Pick the next largest bundle in the list of bundles
            Bundle currentBundle = decreasingBundleSizeList.get(index);
            // See how many bundles can fit into this order
            int bundleCount = (int) ((double) bundleSizeWanted / (double) currentBundle.getSize());
            if (bundleCount > 0) {
                // We can use this bundle, so save how many we can use
                currentBundles.add(new BundleAmount(bundleCount, currentBundle));
                // Now decrease the order size left to fulfil
                bundleSizeWanted -= ( bundleCount * currentBundle.getSize());
            }
            if (bundleSizeWanted == 0 ) {
                // all done - we have fulfilled the order. We quit early since the earlier we quit, the larger
                // bundle size will be, which is what we want
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
