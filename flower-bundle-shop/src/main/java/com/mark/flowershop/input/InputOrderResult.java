package com.mark.flowershop.input;

import com.mark.flowershop.bundle.BundleCalculatedResult;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class InputOrderResult extends InputOrder {

    private final BundleCalculatedResult result;
    private final boolean isValidOrder;

    public InputOrderResult(int orderSize, String productCode, BundleCalculatedResult result) {
        super(orderSize, productCode);
        if ( result == null ) {
            throw new IllegalArgumentException("Provided result cannot be null");
        }
        this.result = result;
        this.isValidOrder = true;
    }

    public BundleCalculatedResult getResult() {
        return result;
    }

    public boolean isValidOrder() {
        return isValidOrder;
    }

}
