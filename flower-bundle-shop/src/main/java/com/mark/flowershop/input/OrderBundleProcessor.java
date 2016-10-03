package com.mark.flowershop.input;

import com.mark.flowershop.bundle.BundleCalculatedResult;
import com.mark.flowershop.bundle.BundleCalculator;
import com.mark.flowershop.bundle.BundleOptions;
import com.mark.flowershop.product.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>This class will accept {@link InputOrderBundle} and process them to produce {@link InputOrderBundleResult}'s
 */
final class OrderBundleProcessor {

    private final BundleCalculator bundleCalculator = new BundleCalculator();

    /**
     * This method acts as an extension of {{@link #processOrder(InputOrderBundle)}} to provide easy use of processing
     * list of input orders
     * @param inputOrderBundles - the list of input orders
     * @return - the List of all the results for each order
     */
    List<InputOrderBundleResult> processOrders(List<InputOrderBundle> inputOrderBundles) {
        List<InputOrderBundleResult> results = new ArrayList<>();
        if ( inputOrderBundles != null && !inputOrderBundles.isEmpty()) {
            results.addAll(
                inputOrderBundles.stream().map(this::processOrder).collect(Collectors.toSet())
            );
        }
        return results;
    }

    /**
     * For the given inputOrder, the {@link BundleCalculator} is used to calculate the best bundle option
     * @param inputOrderBundle - The non-null input order
     * @return the bundle result and the original order
     */
    InputOrderBundleResult processOrder(InputOrderBundle inputOrderBundle) {
        // Make sure we can get a bundle option for the given product code
        Optional<BundleOptions> bundleOptionsOptional = ProductRepository.getBundleForProductCode(inputOrderBundle.getProductCode());
        if ( bundleOptionsOptional.isPresent()) {
            BundleOptions bundleOptions = bundleOptionsOptional.get();
            // We got everything, so ask the calculator to get us the best bundle if possible
            Optional<BundleCalculatedResult> bundleResultOptional = bundleCalculator.calculateBundle(inputOrderBundle.getOrderSize(), bundleOptions);
            if ( bundleResultOptional.isPresent()) {
                // We got a bundle!
                return new InputOrderBundleResult(inputOrderBundle, bundleResultOptional.get());
            } else {
                return new InputOrderBundleResult(inputOrderBundle, false); // No bundle exists
            }
        }
        // The input is not valid (no product exists)
        return new InputOrderBundleResult(inputOrderBundle);
    }

}
