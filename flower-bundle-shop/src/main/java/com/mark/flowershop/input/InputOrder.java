package com.mark.flowershop.input;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class InputOrder {

    private final int orderSize;
    private final String productCode;

    public InputOrder(int orderSize, String productCode) {
        if ( orderSize < 1) { throw new IllegalArgumentException("Provided orderSize ["+orderSize+"] is not valid"); }
        if (StringUtils.isBlank(productCode)) { throw new IllegalArgumentException("Provided productCode is blank"); }
        this.orderSize = orderSize;
        this.productCode = productCode;
    }

    public int getOrderSize() {
        return orderSize;
    }

    public String getProductCode() {
        return productCode;
    }
}
