package com.mark.interview.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by Mark Cunningham on 9/25/2016.
 * <br>POJO representing the generated EmployeePayStub
 */
public final class EmployeePayStub {

    private final Employee employee;
    private final BigDecimal grossIncome;
    private final BigDecimal netIncome;
    private final BigDecimal grossSuper;
    private final BigDecimal incomeTax;
    private final LocalDate startDate;
    private final LocalDate endDate;

    // Require the builder to create instances
    private EmployeePayStub(Builder builder) {
        this.employee = builder.employee;
        this.grossIncome = builder.grossIncome;
        this.netIncome = builder.netIncome;
        this.grossSuper = builder.grossSuper;
        this.incomeTax = builder.incomeTax;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
    }

    public BigDecimal getGrossIncome() {
        return grossIncome;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public BigDecimal getGrossSuper() {
        return grossSuper;
    }

    public BigDecimal getIncomeTax() {
        return incomeTax;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayStub that = (EmployeePayStub) o;
        return Objects.equals(employee, that.employee) &&
                Objects.equals(grossIncome, that.grossIncome) &&
                Objects.equals(netIncome, that.netIncome) &&
                Objects.equals(grossSuper, that.grossSuper) &&
                Objects.equals(incomeTax, that.incomeTax) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, grossIncome, netIncome, grossSuper, incomeTax, startDate, endDate);
    }

    @Override
    public String toString() {
        return "EmployeePayStub{" +
                "employee=" + employee +
                ", grossIncome=" + grossIncome +
                ", netIncome=" + netIncome +
                ", grossSuper=" + grossSuper +
                ", incomeTax=" + incomeTax +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    /**
     * @return - New builder instance that can be used to easily create new EmployeePayStub instances
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class to help create {@link EmployeePayStub} instances
     */
    public static class Builder {
        private Employee employee;
        private BigDecimal grossIncome;
        private BigDecimal netIncome;
        private BigDecimal grossSuper;
        private BigDecimal incomeTax;
        private LocalDate startDate;
        private LocalDate endDate;

        private Builder() { }

        public Builder withEmployee(Employee employee) {
            this.employee = employee;
            return this;
        }

        public Builder withGrossIncome(BigDecimal grossIncome) {
            this.grossIncome = grossIncome;
            return this;
        }

        public Builder withNetIncome(BigDecimal netIncome) {
            this.netIncome = netIncome;
            return this;
        }

        public Builder withGrossSuper(BigDecimal grossSuper) {
            this.grossSuper = grossSuper;
            return this;
        }

        public Builder withIncomeTax(BigDecimal incomeTax) {
            this.incomeTax = incomeTax;
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
         * Once all the data is set on the builder, invoke this method to create an instance of {@link EmployeePayStub}
         * @return - The newly, never null, EmployeePayStub
         * @throws IllegalArgumentException - if any validation fails
         */
        public EmployeePayStub build() throws IllegalArgumentException {
            EmployeePayStub payStub = new EmployeePayStub(this); // Thread safety, create the instance first
            if ( payStub.employee == null) { throw new IllegalArgumentException("Provided employee cannot be null"); }
            if ( payStub.grossIncome == null ) { throw new IllegalArgumentException("Provided grossIncome cannot be null"); }
            if ( payStub.netIncome == null ) { throw new IllegalArgumentException("Provided netIncome cannot be null"); }
            if ( payStub.grossSuper == null ) { throw new IllegalArgumentException("Provided grossSuper cannot be null"); }
            if ( payStub.incomeTax == null ) { throw new IllegalArgumentException("Provided incomeTax cannot be null"); }
            if ( payStub.startDate == null ) { throw new IllegalArgumentException("Provided startDate cannot be null"); }
            if ( payStub.endDate == null ) { throw new IllegalArgumentException("Provided endDate cannot be null"); }
            if ( payStub.grossIncome.signum() == -1) { throw new IllegalArgumentException("Provided grossIncome cannot be negative"); }
            if ( payStub.netIncome.signum() == -1) { throw new IllegalArgumentException("Provided netIncome cannot be negative"); }
            if ( payStub.grossSuper.signum() == -1) { throw new IllegalArgumentException("Provided grossSuper cannot be negative"); }
            if ( payStub.incomeTax.signum() == -1) { throw new IllegalArgumentException("Provided incomeTax cannot be negative"); }
            if ( payStub.endDate.compareTo(payStub.startDate) < 0 ) { throw new IllegalArgumentException("Provided endDate cannot be before startDate"); }
            return payStub;
        }
    }

}

