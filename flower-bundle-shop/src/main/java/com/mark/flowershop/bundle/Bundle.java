package com.mark.flowershop.bundle;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>This class represents a Bundle option for a {@link com.mark.flowershop.product.Product}
 */
public final class Bundle {

    private final int size;
    private final BigDecimal price;

    public Bundle(int size, BigDecimal price) {
        if ( size < 1 ) { throw new IllegalArgumentException("Provided bundle size ["+size+"] is not valid"); }
        if ( price == null || price.signum() < 0) {
            throw new IllegalArgumentException("Provided bundle price [" + price + "] is not valid");
        }
        this.size = size;
        this.price = price;
    }

    /**
     * @return - The size of this bundle
     */
    public int getSize() {
        return size;
    }

    /**
     * @return - The price of the bundle - in raw precision
     */
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bundle bundle = (Bundle) o;
        return size == bundle.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size);
    }
}
