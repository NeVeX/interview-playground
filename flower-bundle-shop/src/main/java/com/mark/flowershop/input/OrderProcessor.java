package com.mark.flowershop.input;

import com.mark.flowershop.bundle.BundleCalculatedResult;
import com.mark.flowershop.bundle.BundleCalculator;
import com.mark.flowershop.bundle.BundleOptions;
import com.mark.flowershop.product.ProductRepository;
import com.sun.org.apache.bcel.internal.generic.INEG;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class OrderProcessor {

    private final BundleCalculator bundleCalculator = new BundleCalculator();

    public List<InputOrderBundleResult> processOrders(List<InputOrderBundle> inputOrderBundles) {
        List<InputOrderBundleResult> results = new ArrayList<>();
        if ( inputOrderBundles != null && !inputOrderBundles.isEmpty()) {
            results.addAll(
                inputOrderBundles.stream().map(this::processOrder).collect(Collectors.toSet())
            );
        }
        return results;
    }

    public InputOrderBundleResult processOrder(InputOrderBundle inputOrderBundle) {
        Optional<BundleOptions> bundleOptionsOptional = ProductRepository.getBundleForProductCode(inputOrderBundle.getProductCode());
        if ( bundleOptionsOptional.isPresent()) {
            BundleOptions bundleOptions = bundleOptionsOptional.get();
            Optional<BundleCalculatedResult> bundleResultOptional = bundleCalculator.calculateBundle(inputOrderBundle.getOrderSize(), bundleOptions);
            if ( bundleResultOptional.isPresent()) {
                return new InputOrderBundleResult(inputOrderBundle, bundleResultOptional.get());
            } else {
                return new InputOrderBundleResult(inputOrderBundle, false);
            }
        }
        return new InputOrderBundleResult(inputOrderBundle);
    }

}
