package com.mark.interview.payroll.model;

import com.mark.interview.payroll.util.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by NeVeX on 9/22/2016.
 */
public final class TaxYearInformation implements Comparable<TaxYearInformation> {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Set<TaxBracket> taxBrackets;

    public TaxYearInformation(LocalDate startDate, LocalDate endDate, Set<TaxBracket> taxBrackets) {
        if ( taxBrackets == null ) { throw new IllegalArgumentException("Provided taxBrackets cannot be null"); }
        if ( startDate == null ) { throw new IllegalArgumentException("Provided startDate cannot be null"); }
        if ( endDate == null ) { throw new IllegalArgumentException("Provided endDate cannot be null"); }

        // End date cannot be before start date
        if ( endDate.compareTo(startDate) <= 0) {
            throw new IllegalArgumentException("Provided endDate ["+endDate+"] cannot be before startDate ["+startDate+"]");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.taxBrackets = taxBrackets;
    }

    public int getYear() {
        return endDate.getYear();
    }

    public Set<TaxBracket> getTaxBrackets() {
        return taxBrackets;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public long getTotalDays() {
        return DateUtils.getTotalDaysInclusive(startDate, endDate);
    }

    public boolean isDateRangeWithinTaxYear(LocalDate startDate, LocalDate endDate) {
        return DateUtils.isDateRangeWithinGivenBounds(startDate, endDate, this.startDate, this.endDate);
    }

    public Optional<TaxBracket> getTaxBracketForSalary(BigDecimal salary) {
        return taxBrackets.stream().filter(f -> f.isSalaryWithinBracket(salary)).findFirst();
    }

    @Override
    public int compareTo(TaxYearInformation that) {
        return getYear() - that.getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxYearInformation that = (TaxYearInformation) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(taxBrackets, that.taxBrackets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, taxBrackets);
    }

    @Override
    public String toString() {
        return "TaxYearInformation{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", taxBrackets=" + taxBrackets +
                '}';
    }
}
