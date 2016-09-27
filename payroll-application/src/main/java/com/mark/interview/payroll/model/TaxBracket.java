package com.mark.interview.payroll.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Mark Cunningham on 9/25/2016.
 * <br>Immutable POJO that represents a TaxBracket in a given {@link TaxYearInformation}
 * Generally tax years are broken into brackets so that one's salary falls within a tax bracket to know one's
 * tax payments.
 * <br>Implements {@link Comparable} to using the minimum salary {{@link #getMinimumSalary()}}
 */
public final class TaxBracket implements Comparable<TaxBracket> {

    private final BigDecimal minimumSalary;
    private final BigDecimal maximumSalary;
    private final BigDecimal taxBaseAmount;
    private final BigDecimal surplusTaxMinimumSalary;
    private final BigDecimal surplusTaxRatePerDollar;

    private TaxBracket(Builder builder) {
        this.minimumSalary = builder.minimumSalary;
        this.maximumSalary = builder.maximumSalary;
        this.taxBaseAmount = builder.taxBaseAmount;
        this.surplusTaxMinimumSalary = builder.surplusTaxMinimumSalary;
        this.surplusTaxRatePerDollar = builder.surplusTaxRatePerDollar;
    }

    public BigDecimal getMinimumSalary() {
        return minimumSalary;
    }

    /**
     * @return - An optional for the maximum salary since at the higher end, there is not max salary bracket
     */
    public Optional<BigDecimal> getMaximumSalary() {
        return Optional.ofNullable(maximumSalary); // Can be null; so wrap in an optional
    }

    public BigDecimal getTaxBaseAmount() {
        return taxBaseAmount;
    }

    public BigDecimal getSurplusTaxMinimumSalary() {
        return surplusTaxMinimumSalary;
    }

    public BigDecimal getSurplusTaxRatePerDollar() {
        return surplusTaxRatePerDollar;
    }

    /**
     * @param salary - non null salary
     * @return - True/false if this salary is within the min/max range (inclusive)
     */
    public boolean isSalaryWithinBracket(BigDecimal salary) {
        return salary != null && this.minimumSalary.compareTo(salary) <= 0
                && (this.maximumSalary == null || this.maximumSalary.compareTo(salary) >= 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxBracket that = (TaxBracket) o;
        return Objects.equals(minimumSalary, that.minimumSalary) &&
                Objects.equals(maximumSalary, that.maximumSalary) &&
                Objects.equals(taxBaseAmount, that.taxBaseAmount) &&
                Objects.equals(surplusTaxMinimumSalary, that.surplusTaxMinimumSalary) &&
                Objects.equals(surplusTaxRatePerDollar, that.surplusTaxRatePerDollar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimumSalary, maximumSalary, taxBaseAmount, surplusTaxMinimumSalary, surplusTaxRatePerDollar);
    }

    @Override
    public String toString() {
        return "TaxBracket{" +
                "minimumSalary=" + minimumSalary +
                ", maximumSalary=" + maximumSalary +
                ", taxBaseAmount=" + taxBaseAmount +
                ", surplusTaxMinimumSalary=" + surplusTaxMinimumSalary +
                ", surplusTaxRatePerDollar=" + surplusTaxRatePerDollar +
                '}';
    }

    @Override
    public int compareTo(TaxBracket that) {
        return this.getMinimumSalary().compareTo(that.getMinimumSalary());
    }

    /**
     * @return - Helper method to get a new instance of the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class to help with creation of {@link TaxBracket} instances
     */
    public static class Builder {
        private BigDecimal minimumSalary;
        private BigDecimal maximumSalary;
        private BigDecimal taxBaseAmount;
        private BigDecimal surplusTaxMinimumSalary;
        private BigDecimal surplusTaxRatePerDollar;

        private Builder() { }

        public Builder withMinimumSalary(BigDecimal minimumSalary) {
            this.minimumSalary = minimumSalary;
            return this;
        }

        public Builder withMaximumSalary(BigDecimal maximumSalary) {
            this.maximumSalary = maximumSalary;
            return this;
        }

        public Builder withTaxBaseAmount(BigDecimal taxBaseAmount) {
            this.taxBaseAmount = taxBaseAmount;
            return this;
        }

        public Builder withSurplusTaxMinimumSalary(BigDecimal surplusTaxMinimumSalary) {
            this.surplusTaxMinimumSalary = surplusTaxMinimumSalary;
            return this;
        }

        public Builder withSurplusTaxRatePerDollar(BigDecimal surplusTaxRatePerDollar) {
            this.surplusTaxRatePerDollar = surplusTaxRatePerDollar;
            return this;
        }

        /**
         * Given the date in this builder instance, a new {@link TaxBracket} instance will be created
         * @return - the non null, new TaxBracket instance
         * @throws IllegalArgumentException - if the data is not valid
         */
        public TaxBracket build() throws IllegalArgumentException {
            TaxBracket taxBracket = new TaxBracket(this); // Thread safety, create the instance first
            if ( taxBracket.minimumSalary == null) { throw new IllegalArgumentException("Provided minimumSalary cannot be null"); }
            if ( taxBracket.taxBaseAmount == null) { throw new IllegalArgumentException("Provided taxBaseAmount cannot be null"); }
            if ( taxBracket.surplusTaxMinimumSalary == null) { throw new IllegalArgumentException("Provided surplusTaxMinimumSalary cannot be null"); }
            if ( taxBracket.surplusTaxRatePerDollar == null) { throw new IllegalArgumentException("Provided surplusTaxRatePerDollar cannot be null"); }
            if ( taxBracket.minimumSalary.signum()== -1 ) { throw new IllegalArgumentException("Provided minimumSalary cannot be negative"); }
            if ( taxBracket.taxBaseAmount.signum()== -1 ) { throw new IllegalArgumentException("Provided taxBaseAmount cannot be negative"); }
            if ( taxBracket.maximumSalary != null && taxBracket.maximumSalary.signum()== -1 ) {
                throw new IllegalArgumentException("Provided maximumSalary cannot be negative");
            }
            if ( taxBracket.surplusTaxMinimumSalary.signum()== -1 ) { throw new IllegalArgumentException("Provided surplusTaxMinimumSalary cannot be negative"); }
            if ( taxBracket.surplusTaxRatePerDollar.signum()== -1 ) { throw new IllegalArgumentException("Provided surplusTaxRatePerDollar cannot be negative"); }
            if ( taxBracket.surplusTaxRatePerDollar.compareTo(new BigDecimal("1")) > 0) { throw new IllegalArgumentException("Provided surplusTaxRatePerDollar cannot exceed 100%"); }

            return taxBracket;
        }
    }

}
