package com.mark.interview.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static com.mark.interview.payroll.data.DataFormatConfiguration.*;

/**
 * Created by Mark Cunningham on 9/25/2016.
 * <br>Immutable POJO for Salary data
 */
public final class Salary {

    private final BigDecimal annualSalary;
    private final BigDecimal annualSuperRate;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private Salary(Builder builder) {
        this.annualSalary = builder.annualSalary;
        this.annualSuperRate = builder.annualSuperRate;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
    }

    /**
     * @return - The annual salary that applies over the start/end date period
     */
    public BigDecimal getAnnualSalary() {
        return annualSalary;
    }

    /**
     * @return - The annual super rate that applies over the start/end date period
     */
    public BigDecimal getAnnualSuperRate() {
        return annualSuperRate;
    }

    /**
     * @return - The Start date of this pay period
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @return - The End date of this pay period
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salary salary = (Salary) o;
        return Objects.equals(annualSalary, salary.annualSalary) &&
                Objects.equals(annualSuperRate, salary.annualSuperRate) &&
                Objects.equals(startDate, salary.startDate) &&
                Objects.equals(endDate, salary.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annualSalary, annualSuperRate, startDate, endDate);
    }

    @Override
    public String toString() {
        return "Salary{" +
                "annualSalary=" + annualSalary +
                ", annualSuperRate=" + annualSuperRate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Static builder class to ease with Salary data construction
     */
    public static class Builder {

        private BigDecimal annualSalary;
        private BigDecimal annualSuperRate;
        private LocalDate startDate;
        private LocalDate endDate;

        private Builder() { }

        public Builder withAnnualSalary(BigDecimal annualSalary) {
            this.annualSalary = annualSalary;
            return this;
        }

        public Builder withAnnualSuperRate(BigDecimal annualSuperRate) {
            this.annualSuperRate = annualSuperRate;
            if ( this.annualSuperRate != null && this.annualSuperRate.compareTo(BigDecimal.ONE) == 1) {
                this.annualSuperRate = this.annualSuperRate.setScale(DEFAULT_DECIMAL_SCALE, DEFAULT_ROUNDING).divide(ONE_HUNDRED, DEFAULT_ROUNDING);
            }
            return this;
        }

        public Builder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        /**
         * Once all the various date points have being set, invoke this method to create a new Salary instance.
         * @return - The newly build Salary instance
         * @throws IllegalArgumentException - if the data is not valid
         */
        public Salary build() throws IllegalArgumentException{
            Salary salary = new Salary(this); // Thread safety, create the instance first
            if (salary.annualSalary == null) { throw new IllegalArgumentException("Provided annualSalary is null"); }
            if (salary.annualSuperRate == null) { throw new IllegalArgumentException("Provided annualSuperRate is null"); }
            if (salary.startDate == null) { throw new IllegalArgumentException("Provided startDate is null"); }
            if (salary.endDate == null) { throw new IllegalArgumentException("Provided endDate is null"); }
            if (salary.annualSalary.signum() == -1) { throw new IllegalArgumentException("Provided annualSalary cannot be negative"); }
            if (salary.annualSuperRate.signum() == -1 || salary.annualSuperRate.compareTo(new BigDecimal("0.5")) == 1) {
                throw new IllegalArgumentException("Provided AnnualSuperRate must be between 0% and 50% (inclusive)");
            }
            if ( salary.endDate.compareTo(startDate) <= 0 ) {
                throw new IllegalArgumentException("Provided endDate ["+salary.endDate+"] cannot be before startDate ["+salary.startDate+"]");
            }
            return salary;
        }
    }

}
