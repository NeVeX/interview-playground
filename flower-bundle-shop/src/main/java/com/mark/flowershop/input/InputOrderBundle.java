package com.mark.flowershop.input;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>Class the represents a new user input order to process
 */
final class InputOrderBundle {

    private final int orderSize;
    private final String productCode;

    InputOrderBundle(int orderSize, String productCode) {
        if ( orderSize < 1) { throw new IllegalArgumentException("Provided orderSize ["+orderSize+"] is not valid"); }
        if (StringUtils.isBlank(productCode)) { throw new IllegalArgumentException("Provided productCode is blank"); }
        this.orderSize = orderSize;
        this.productCode = productCode;
    }

    /**
     * The size of the order requested
     */
    int getOrderSize() {
        return orderSize;
    }

    /**
     * The product wanted
     */
    String getProductCode() {
        return productCode;
    }
}
