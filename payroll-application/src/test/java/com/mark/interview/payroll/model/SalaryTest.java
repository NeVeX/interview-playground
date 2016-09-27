package com.mark.interview.payroll.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class SalaryTest {

    @Test
    public void assertSalaryBuilderHappyPath() {
        getValidSalaryBuilder().build(); // No exceptions should throw
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSalaryBuilderExceptionForNullAnnualSalary() {
        getValidSalaryBuilder().withAnnualSalary(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSalaryBuilderExceptionForNullAnnualSuper() {
        getValidSalaryBuilder().withAnnualSuperRate(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSalaryBuilderExceptionForNullStartDate() {
        getValidSalaryBuilder().withStartDate(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSalaryBuilderExceptionForNullEndDate() {
        getValidSalaryBuilder().withEndDate(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSalaryBuilderExceptionForNegativeSalary() {
        getValidSalaryBuilder().withAnnualSalary(new BigDecimal(-40000)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSalaryBuilderExceptionForNegativeSuper() {
        getValidSalaryBuilder().withAnnualSuperRate(new BigDecimal(-0.2)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSalaryBuilderExceptionForExceeding50PercentSuper() {
        getValidSalaryBuilder().withAnnualSuperRate(new BigDecimal(0.51)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSalaryBuilderExceptionForExceeding100PercentSuper() {
        getValidSalaryBuilder().withAnnualSuperRate(new BigDecimal(101)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSalaryBuilderExceptionForEndDateBeforeStartDate() {
        getValidSalaryBuilder().withEndDate(LocalDate.now().minusYears(20)).build();
    }

    private Salary.Builder getValidSalaryBuilder() {
        return Salary.builder()
                .withAnnualSalary(new BigDecimal(20000))
                .withAnnualSuperRate(new BigDecimal(0.1))
                .withStartDate(LocalDate.now().minusMonths(2))
                .withEndDate(LocalDate.now().plusMonths(2));

    }

}
