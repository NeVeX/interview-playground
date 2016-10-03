package com.mark.flowershop.input;

import com.mark.flowershop.bundle.BundleCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class OrderProcessor {

    private final BundleCalculator bundleCalculator = new BundleCalculator();

    public List<InputOrderResult> processOrders(List<InputOrder> inputOrders) {
        List<InputOrderResult> results = new ArrayList<>();
        if ( inputOrders != null && inputOrders.isEmpty()) {

        }

        return results;
    }

    public InputOrderResult processOrder(InputOrder inputOrder) {
        return null;
    }

}
