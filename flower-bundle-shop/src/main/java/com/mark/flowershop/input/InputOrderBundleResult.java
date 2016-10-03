package com.mark.flowershop.input;

import com.mark.flowershop.bundle.BundleCalculatedResult;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>The result of a valid {@link InputOrderBundle} being processed by the {@link OrderBundleProcessor}
 */
final class InputOrderBundleResult {

    private final InputOrderBundle inputOrderBundle;
    private final BundleCalculatedResult result;
    private final boolean isValidOrder;
    private final boolean doesBundleExistForOrder;

    /**
     * Creates a new instance of the bundle result - this will be the valid result, i.e. bundle is found for a valid order
     */
    InputOrderBundleResult(InputOrderBundle inputOrderBundle, BundleCalculatedResult result) {
        if ( inputOrderBundle == null) { throw new IllegalArgumentException("Provided inputOrderBundle cannot be null"); }
        if ( result == null ) {
            throw new IllegalArgumentException("Provided result cannot be null");
        }
        this.doesBundleExistForOrder = true;
        this.inputOrderBundle = inputOrderBundle;
        this.result = result;
        this.isValidOrder = true;
    }

    /**
     * Creates a new instance of the result that will indicate that the inputOrder is not valid
     */
    InputOrderBundleResult(InputOrderBundle inputOrderBundle) {
        if ( inputOrderBundle == null) { throw new IllegalArgumentException("Provided inputOrderBundle cannot be null"); }
        this.inputOrderBundle = inputOrderBundle;
        this.doesBundleExistForOrder = false;
        this.isValidOrder = false;
        this.result = null;
    }

    /**
     * Creates a new instance of the result that indicates whether or not the bundle exists for the given order
     */
    InputOrderBundleResult(InputOrderBundle inputOrderBundle, boolean doesBundleExistForOrder) {
        if ( inputOrderBundle == null) { throw new IllegalArgumentException("Provided inputOrderBundle cannot be null"); }
        this.doesBundleExistForOrder = doesBundleExistForOrder;
        this.inputOrderBundle = inputOrderBundle;
        this.isValidOrder = true;
        this.result = null;
    }

    /**
     * @return - True if a bundle exists for the given order. False otherwise
     */
    boolean doesBundleExistForOrder() {
        return doesBundleExistForOrder;
    }

    /**
     * @return - Gets the result
     */
    BundleCalculatedResult getResult() {
        return result;
    }

    /**
     * @return - True if a input order is valid. False otherwise
     */
    boolean isValidOrder() {
        return isValidOrder;
    }

    /**
     * @return - Get the original order
     */
    InputOrderBundle getInputOrderBundle() {
        return inputOrderBundle;
    }
}
