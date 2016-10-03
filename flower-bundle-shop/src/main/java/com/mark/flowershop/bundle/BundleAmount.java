package com.mark.flowershop.bundle;

import java.util.Objects;
import java.util.Set;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>This class represents a total amount of a certain bundle of a {@link com.mark.flowershop.product.Product}
 */
public class BundleAmount {

    private final int amount;
    private final Bundle bundle;

    BundleAmount(int amount, Bundle bundle) {
        if ( amount < 1 ) { throw new IllegalArgumentException("Provided amount ["+amount+"] is not valid"); }
        if ( bundle == null) {
            throw new IllegalArgumentException("Provided bundle cannot be null");
        }
        this.amount = amount;
        this.bundle = bundle;
    }

    /**
     * @return - How many of this particular bundle to obtain
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @return - The bundle wanted
     */
    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BundleAmount that = (BundleAmount) o;
        return amount == that.amount &&
                Objects.equals(bundle, that.bundle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, bundle);
    }
}
