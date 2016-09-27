package com.mark.interview.payroll;

import com.mark.interview.payroll.model.*;
import com.mark.interview.payroll.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static com.mark.interview.payroll.data.DataFormatConfiguration.*;
import static java.util.stream.Collectors.toSet;

/**
 * Created by Mark Cunningham on 9/25/2016.
 * <br>This is the brains of the application - this class offers various methods to calculate
 * {@link EmployeePayStub} paystubs from various input {@link EmployeeSalary} salaries using the application
 * supported {@link TaxYearInformation} tax years
 */
class PayStubCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayStubCalculator.class);
    private final Map<Integer, TaxYearInformation> allTaxYears = new TreeMap<>(); // Simple map lookup for tax years
    private static final LocalDate DEFAULT_TAX_YEAR_START = LocalDate.of(2016, Month.JULY, 1); // Year does not matter

    PayStubCalculator(Set<TaxYearInformation> allTaxYearInformation) {
        if ( allTaxYearInformation == null || allTaxYearInformation.isEmpty()) {
            throw new IllegalArgumentException("Provided allTaxYearInformation input cannot be null/empty");
        }
        allTaxYearInformation.forEach(taxYear -> this.allTaxYears.put(taxYear.getYear(), taxYear));
    }

    /**
     * @return - Returns this calculators supported tax years
     */
    Set<Integer> getSupportedTaxYears() {
        return allTaxYears.keySet();
    }

    /**
     * Calculates pay stubs for the given set of input, by looping over each and invoking {{@link #calculate(EmployeeSalary)}}
     * @param employeeSalaries - The employee salaries
     * @return - The set of calculated paystubs (can be empty - never null)
     */
    Set<EmployeePayStub> calculate(Set<EmployeeSalary> employeeSalaries) {
        if ( employeeSalaries != null ) {
            return employeeSalaries.stream()
                .map(this::calculate) // Calculate the paystubs
                .filter(Optional::isPresent) // Filter out non calculated ones
                .map(Optional::get) // Get the data
                .collect(toSet()); // Put it into a set and return
        }
        return new HashSet<>();
    }

    /**
     * Given an employee's salary, this will attempt to calculate the pay stub
     * @param employeeSalary - The non null employee salary
     * @return - An optional of the paystub - it's an optional since the salary period may not be supported by this calculator
     */
    Optional<EmployeePayStub> calculate(EmployeeSalary employeeSalary) {
        Salary salary = employeeSalary.getSalary();
        int salaryYear = getTaxYearForSalary(salary); // Determine the tax year of this salary's dates
        if ( allTaxYears.containsKey(salaryYear)) { // make sure we support the tax year
            TaxYearInformation taxYear = allTaxYears.get(salaryYear);
            // We have the tax year, but make sure that the salary dates are valid within the tax year
            if ( taxYear.isDateRangeWithinTaxYear(salary.getStartDate(), salary.getEndDate())) {
                // The salary dates fall within the tax year, now get the bracket for the given salary
                Optional<TaxBracket> taxRateRangeOptional = taxYear.getTaxBracketForSalary(salary.getAnnualSalary());
                if (taxRateRangeOptional.isPresent()) {
                    TaxBracket taxBracket = taxRateRangeOptional.get();
                    // We have everything almost - we need to get the period divisor to use (yearly, monthly, custom)
                    BigDecimal payStubPeriodDivisor = getPeriodDivisorToUseForCalculations(taxYear, salary);
                    return Optional.of(calculate(employeeSalary, taxBracket, payStubPeriodDivisor)); // calculate!
                }
            } else {
                // The salary start and end date's fall outside the bounds of the tax year; meaning 2 or more tax years
                // would need to be consulted to determine the pay stub.
                // This application can be extended support this, but I think it's outside the realm of what is asked.
                LOGGER.warn("The provided employee salary dates [{} - {}] do not fit within the tax year [{}] range [{} - {}]",
                        salary.getStartDate(), salary.getEndDate(), salaryYear, taxYear.getStartDate(), taxYear.getStartDate());
            }
        }
        return Optional.empty(); // Return an empty optional in cases where we don't have tax year information
    }

    /**
     * This method determines what tax year the given salary falls within.
     * <br> It uses the endDate year to determine this. Basically, if < July; use that year - else; use the next year
     * @param salary - the non null salary
     * @return - The tax year that applies to this salary
     */
    private int getTaxYearForSalary(Salary salary) {
        LocalDate endDate = salary.getEndDate();
        if ( endDate.compareTo(DEFAULT_TAX_YEAR_START.withYear(endDate.getYear())) <= 0 ) {
            return endDate.getYear();
        }
        return endDate.getYear() + 1;
    }

    /**
     * The fun begins - this method is fed all the data it needs to calculate the paystub
     * @param employeeSalary - The non null employee salary
     * @param taxBracket - The tax bracket to be used for this salary
     * @param payStubPeriodDivisor - The pay period divisor to use (yearly, monthly, custom)
     * @return
     */
    private EmployeePayStub calculate(EmployeeSalary employeeSalary, TaxBracket taxBracket, BigDecimal payStubPeriodDivisor) {

        Salary salary = employeeSalary.getSalary();
        /**
         * Before beginning calculations, set the scale (precision) for our calcuations
         */
        BigDecimal annualSalary = salary.getAnnualSalary().setScale(DEFAULT_DECIMAL_SCALE, DEFAULT_ROUNDING);
        BigDecimal superRate = salary.getAnnualSuperRate().setScale(DEFAULT_DECIMAL_SCALE, DEFAULT_ROUNDING);
        BigDecimal baseTaxAmount = taxBracket.getTaxBaseAmount().setScale(DEFAULT_DECIMAL_SCALE, DEFAULT_ROUNDING);
        BigDecimal surplusTaxRatePerDollar = taxBracket.getSurplusTaxRatePerDollar().setScale(DEFAULT_DECIMAL_SCALE, DEFAULT_ROUNDING);
        BigDecimal surplusTaxMinimumSalaryStart = taxBracket.getSurplusTaxMinimumSalary().setScale(DEFAULT_DECIMAL_SCALE, DEFAULT_ROUNDING);

        /**
         * Now, calculate each line item of the pay stub
         */
        BigDecimal grossIncome = annualSalary.divide(payStubPeriodDivisor, DEFAULT_ROUNDING);
        BigDecimal incomeTax = annualSalary.subtract(surplusTaxMinimumSalaryStart)
                                            .multiply(surplusTaxRatePerDollar)
                                            .add(baseTaxAmount)
                                            .divide(payStubPeriodDivisor, DEFAULT_ROUNDING);
        BigDecimal netIncome = grossIncome.subtract(incomeTax);
        BigDecimal grossSuper = grossIncome.multiply(superRate);

        /**
         * Everything is all calculated now, so let's scale the calculations to what we want and return
         */
        return EmployeePayStub.builder()
                .withEmployee(employeeSalary.getEmployee())
                .withGrossIncome(grossIncome.setScale(0, RoundingMode.DOWN)) // rounding mode as per requirement
                .withNetIncome(netIncome.setScale(0, DEFAULT_ROUNDING))
                .withGrossSuper(grossSuper.setScale(0, RoundingMode.DOWN)) // rounding mode as per requirement
                .withIncomeTax(incomeTax.setScale(0, RoundingMode.UP)) // rounding mode as per requirement
                .withStartDate(salary.getStartDate())
                .withEndDate(salary.getEndDate())
                .build();
    }

    /**
     * This method will determine what decimal divisor to use for the paystub calculations.
     * <br>E.g. For yearly calculations, it will return ONE
     * <br>For monthly, it will return TWELVE
     * <br>For custom ranges (everything else), it will return the tax-year-days/salary-range (e.g. 365 / 20)
     * @param taxYear - The tax year to use
     * @param salary - The salary that needs a paystub
     * @return - The period divisor to use in paystub calculations
     */
    private BigDecimal getPeriodDivisorToUseForCalculations(TaxYearInformation taxYear, Salary salary) {
        // Get the total tax days in the tax year and set it to a scale for calculations
        BigDecimal daysInTheTaxYear = new BigDecimal(taxYear.getTotalDays()).setScale(DEFAULT_DECIMAL_SCALE, DEFAULT_ROUNDING);
        BigDecimal salaryPeriodDivisor;
        // Check if this salary period is EXACTLY equal to it's month
        // (e.g. 01/01/2016 - 31/01/2016 is month of January); but 01/01/2016 - 25/01/2016, is not a the month of Januaru
        if ( DateUtils.isAFullMonthPeriod(salary.getStartDate(), salary.getEndDate())) {
            salaryPeriodDivisor = TWELVE_MONTHS;
        } else if (areDatesEqualToTaxYearBounds(taxYear, salary.getStartDate(), salary.getEndDate())) {
            // The salary dates given match the tax year bounds (e.g. July 1st -> June 30th)
            salaryPeriodDivisor = BigDecimal.ONE;
        } else {
            // It's not a yearly or monthly salary range, so calculate the custom divisor
            // which is the tax-days / period-days
            BigDecimal daysInSalaryPeriod = new BigDecimal(DateUtils.getTotalDaysInclusive(salary.getStartDate(), salary.getEndDate()));
            salaryPeriodDivisor = daysInTheTaxYear.divide(daysInSalaryPeriod, DEFAULT_ROUNDING);
        }
        return salaryPeriodDivisor;
    }

    /**
     * Helper method to determine if the bounds of the tax year, equal to the start and end date range provided
     * @return - T/F if the dates are the same span as the tax year
     */
    private boolean areDatesEqualToTaxYearBounds(TaxYearInformation taxYear, LocalDate startDate, LocalDate endDate) {
        return startDate.compareTo(taxYear.getStartDate()) == 0 && endDate.compareTo(taxYear.getEndDate()) == 0;
    }
}
