package com.mark.flowershop.product;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public abstract class Product {

    private final String code;

    public Product(String code) {
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Provided product code cannot be null or empty");
        }
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
