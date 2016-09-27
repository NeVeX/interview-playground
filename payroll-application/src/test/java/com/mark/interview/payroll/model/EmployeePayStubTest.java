package com.mark.interview.payroll.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class EmployeePayStubTest {

    @Test
    public void assertValidEmployeePayStubBuilder() {
        createValidBuilder().build(); // No exceptions
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNullEmployee() {
        createValidBuilder().withEmployee(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNullIncomeTax() {
        createValidBuilder().withIncomeTax(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNullNetIncome() {
        createValidBuilder().withNetIncome(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNullGrossIncome() {
        createValidBuilder().withGrossIncome(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNullGrossSuper() {
        createValidBuilder().withGrossSuper(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNullStartDate() {
        createValidBuilder().withStartDate(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNullEndDate() {
        createValidBuilder().withEndDate(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNegativeIncomeTax() {
        createValidBuilder().withIncomeTax(new BigDecimal(-10000)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNegativeNetIncome() {
        createValidBuilder().withNetIncome(new BigDecimal(-10000)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNegativeGrossIncome() {
        createValidBuilder().withGrossIncome(new BigDecimal(-10000)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithNegativeGrossSuper() {
        createValidBuilder().withGrossSuper(new BigDecimal(-10000)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForEmployeePayStubBuilderWithEndDateBeforeStartDate() {
        createValidBuilder().withEndDate(LocalDate.now().minusYears(20)).build();
    }

    private EmployeePayStub.Builder createValidBuilder() {
        Employee employee = new Employee("Test", "Name");
        return EmployeePayStub.builder()
                .withEmployee(employee)
                .withIncomeTax(new BigDecimal(20000))
                .withNetIncome(new BigDecimal(12345))
                .withGrossIncome(new BigDecimal(3424))
                .withGrossSuper(new BigDecimal(12311))
                .withStartDate(LocalDate.now().minusMonths(2))
                .withEndDate(LocalDate.now().plusMonths(2));
    }
}
