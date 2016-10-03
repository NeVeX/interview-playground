package com.mark.flowershop.product;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>Abstract class that all Products will extend from
 */
public abstract class Product {

    private final String code;

    Product(String code) {
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Provided product code cannot be null or empty");
        }
        this.code = code;
    }

    /**
     * Gets this products code (which is unique across all products)
     */
    public String getCode() {
        return code;
    }
}
