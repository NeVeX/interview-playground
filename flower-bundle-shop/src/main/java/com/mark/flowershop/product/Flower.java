package com.mark.flowershop.product;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class Flower extends Product {

    private final String name;

    public Flower(String code, String name) {
        super(code);
        if (StringUtils.isBlank(name)) { throw new IllegalArgumentException("Provided flower name cannot be null or empty"); }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
