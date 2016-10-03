package com.mark.flowershop.input;

import com.mark.flowershop.bundle.BundleCalculatedResult;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class InputOrderBundleResult {

    private final InputOrderBundle inputOrderBundle;
    private final BundleCalculatedResult result;
    private final boolean isValidOrder;
    private final boolean doesBundleExistForOrder;

    public InputOrderBundleResult(InputOrderBundle inputOrderBundle, BundleCalculatedResult result) {
        if ( inputOrderBundle == null) { throw new IllegalArgumentException("Provided inputOrderBundle cannot be null"); }
        if ( result == null ) {
            throw new IllegalArgumentException("Provided result cannot be null");
        }
        this.doesBundleExistForOrder = true;
        this.inputOrderBundle = inputOrderBundle;
        this.result = result;
        this.isValidOrder = true;
    }

    public InputOrderBundleResult(InputOrderBundle inputOrderBundle) {
        if ( inputOrderBundle == null) { throw new IllegalArgumentException("Provided inputOrderBundle cannot be null"); }
        this.inputOrderBundle = inputOrderBundle;
        this.doesBundleExistForOrder = false;
        this.isValidOrder = false;
        this.result = null;
    }

    public InputOrderBundleResult(InputOrderBundle inputOrderBundle, boolean doesBundleExistForOrder) {
        if ( inputOrderBundle == null) { throw new IllegalArgumentException("Provided inputOrderBundle cannot be null"); }
        this.doesBundleExistForOrder = doesBundleExistForOrder;
        this.inputOrderBundle = inputOrderBundle;
        this.isValidOrder = true;
        this.result = null;
    }

    public boolean doesBundleExistForOrder() {
        return doesBundleExistForOrder;
    }

    public BundleCalculatedResult getResult() {
        return result;
    }

    public boolean isValidOrder() {
        return isValidOrder;
    }

    public InputOrderBundle getInputOrderBundle() {
        return inputOrderBundle;
    }
}
