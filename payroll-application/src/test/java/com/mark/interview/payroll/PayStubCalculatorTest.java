package com.mark.interview.payroll;

import com.mark.interview.payroll.data.read.TaxInformationJsonFileReader;
import com.mark.interview.payroll.model.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class PayStubCalculatorTest {

    @Test
    public void assertGivenRequirementsPayStubCalculationsAreCorrect() {
        Set<EmployeePayStub> expectedCalculatedPayStubs = getValidExpectedPayStubResults();
        assertPayStubCalculationsAreCorrect(expectedCalculatedPayStubs);
    }

    @Test(expected = AssertionError.class)
    public void assertPayStubCalculationFailsByModifyingValidExpectedResults() {

        Set<EmployeePayStub> expectedCalculatedPayStubs = new HashSet<>();
        // Let's modified the expected results and confirm that it does not match
        expectedCalculatedPayStubs.add(getRyanChenExpectedPayStub());
        EmployeePayStub modifiedDavidExpectedResult = getDavidRuddExpectedPayStub();
        modifiedDavidExpectedResult = EmployeePayStub.builder()
                .withEmployee(modifiedDavidExpectedResult.getEmployee())
                .withGrossIncome(modifiedDavidExpectedResult.getGrossIncome())
                .withNetIncome(modifiedDavidExpectedResult.getNetIncome().subtract(BigDecimal.TEN))
                .withGrossSuper(modifiedDavidExpectedResult.getGrossSuper())
                .withIncomeTax(modifiedDavidExpectedResult.getIncomeTax().add(BigDecimal.TEN))
                .withStartDate(modifiedDavidExpectedResult.getStartDate())
                .withEndDate(modifiedDavidExpectedResult.getEndDate()).build();
        expectedCalculatedPayStubs.add(modifiedDavidExpectedResult);

        assertPayStubCalculationsAreCorrect(expectedCalculatedPayStubs);
    }

    @Test
    public void assertCalculationsArePerformedCorrectly() {
        // Create a set of various inputs that span across all tax brackets
        Set<EmployeeSalary> inputSalaries = new HashSet<>();
        inputSalaries.add(getMarkCunninghamSalary());
        inputSalaries.add(getJohnWayneSalary());
        inputSalaries.add(getClarkKentSalary());
        inputSalaries.add(getAustinPowersSalary());
        inputSalaries.add(getStephenGerardSalary());
        // Generate the paystubs
        Set<EmployeePayStub> generatedPayStubs = getDefaultCalculator().calculate(inputSalaries);
        // Create the set of expected paystubs
        Set<EmployeePayStub> expectedPayStubs = new HashSet<>();
        expectedPayStubs.add(getMarkCunninghamExpectedPayStub());
        expectedPayStubs.add(getJohnWayneExpectedPayStub());
        expectedPayStubs.add(getClarkKentExpectedPayStub());
        expectedPayStubs.add(getAustinPowersExpectedPayStub());
        expectedPayStubs.add(getStephenGerardExpectedPayStub());

        // We have everything, so let' make sure both expected and calculated sets are identical
        assertThat(generatedPayStubs).containsAll(expectedPayStubs);
    }

    @Test
    public void assertMonthlyPayStubCalculationsAreValid() {
        /**
         * Create a tax bracket that we'll test against
         */
        TaxBracket taxBracket = TaxBracket.builder()
                .withMinimumSalary(new BigDecimal(55000))
                .withMaximumSalary(new BigDecimal(80000))
                .withTaxBaseAmount(new BigDecimal(20000))
                .withSurplusTaxMinimumSalary(new BigDecimal(40000))
                .withSurplusTaxRatePerDollar(new BigDecimal(0.1)).build();
        Set<TaxBracket> taxBrackets = new HashSet<>();
        taxBrackets.add(taxBracket);
        /**
         * Set up a tax year
         */
        LocalDate taxYearStart = LocalDate.of(2015, Month.JULY, 1);
        LocalDate taxYearEnd = LocalDate.of(2016, Month.JUNE, 30);
        TaxYearInformation taxYear = new TaxYearInformation(taxYearStart, taxYearEnd, taxBrackets);
        Set<TaxYearInformation> allTaxYears = new HashSet<>();
        allTaxYears.add(taxYear);
        /**
         * Create an employee with salary that will fit into the above.
         * Make this salary range equal to one full month
         */
        Employee employee = new Employee("Peter", "Parker");
        LocalDate salaryStart = LocalDate.of(2015, Month.OCTOBER, 1);
        LocalDate salaryEnd = LocalDate.of(2015, Month.OCTOBER, 31); // One month exactly
        Salary salary = Salary.builder().withAnnualSalary(new BigDecimal(70000))
                            .withAnnualSuperRate(new BigDecimal(0.1))
                            .withStartDate(salaryStart)
                            .withEndDate(salaryEnd).build();
        EmployeeSalary employeeSalary = new EmployeeSalary(employee, salary);

        // Let's do the calculations
        Optional<EmployeePayStub> payStubOptional = new PayStubCalculator(allTaxYears).calculate(employeeSalary);
        assertTrue("Expected calculated pay stub", payStubOptional.isPresent());

        EmployeePayStub payStub = payStubOptional.get();
        // Match the gross => 70000 / 12 = 5833
        assertEquals(new BigDecimal(5833), payStub.getGrossIncome());
        // Income tax => (20000 + ((70000 - 40000) * 0.1) / 12 = 1917
        assertEquals(new BigDecimal(1917), payStub.getIncomeTax());
        // Net Income => 5833 - 1917 = 3917 (rounding half even)
        assertEquals(new BigDecimal(3917), payStub.getNetIncome());
        // Super rate => 5833 * 0.1 = 583
        assertEquals(new BigDecimal(583), payStub.getGrossSuper());
    }

    @Test
    public void assertYearlyPeriodPayStubsAreCalculatedCorrectly() {
        /**
         * Create a tax bracket that we'll test against
         */
        TaxBracket taxBracket = TaxBracket.builder()
                .withMinimumSalary(new BigDecimal(20000))
                .withMaximumSalary(new BigDecimal(50000))
                .withTaxBaseAmount(new BigDecimal(5000))
                .withSurplusTaxMinimumSalary(new BigDecimal(13000))
                .withSurplusTaxRatePerDollar(new BigDecimal(0.4)).build();
        Set<TaxBracket> taxBrackets = new HashSet<>();
        taxBrackets.add(taxBracket);
        /**
         * Set up a tax year
         */
        LocalDate taxYearStart = LocalDate.of(2015, Month.JULY, 1);
        LocalDate taxYearEnd = LocalDate.of(2016, Month.JUNE, 30);
        TaxYearInformation taxYear = new TaxYearInformation(taxYearStart, taxYearEnd, taxBrackets);
        Set<TaxYearInformation> allTaxYears = new HashSet<>();
        allTaxYears.add(taxYear);
        /**
         * Create an employee with salary that will fit into the above.
         * Make this salary range equal to the full tax year
         */
        Employee employee = new Employee("BoJack", "Horseman");
        Salary salary = Salary.builder().withAnnualSalary(new BigDecimal(40000))
                .withAnnualSuperRate(new BigDecimal(0.2))
                .withStartDate(taxYearStart)
                .withEndDate(taxYearEnd).build(); // Set the start/end dates to the tax year start/end
        EmployeeSalary employeeSalary = new EmployeeSalary(employee, salary);

        // Let's do the calculations
        Optional<EmployeePayStub> payStubOptional = new PayStubCalculator(allTaxYears).calculate(employeeSalary);
        assertTrue("Expected calculated pay stub", payStubOptional.isPresent());

        EmployeePayStub payStub = payStubOptional.get();
        // Match the gross => 40000 / 1 = 40000
        assertEquals(new BigDecimal(40000), payStub.getGrossIncome());
        // Income tax => (5000 + ((40000 - 13000) * 0.4) / 1 = 15800 (round up)
        assertEquals(new BigDecimal(15801), payStub.getIncomeTax());
        // Net Income => 40000 - 15800 = 24200 (rounding half even)
        assertEquals(new BigDecimal(24200), payStub.getNetIncome());
        // Super rate => 40000 * 0.2 = 8000
        assertEquals(new BigDecimal(8000), payStub.getGrossSuper());
    }

    @Test
    public void assertCustomPeriodPayStubsAreCalculatedCorrectly() {
        /**
         * Create a tax bracket that we'll test against
         */
        TaxBracket taxBracket = TaxBracket.builder()
                .withMinimumSalary(new BigDecimal(30000))
                .withMaximumSalary(new BigDecimal(50000))
                .withTaxBaseAmount(new BigDecimal(5000))
                .withSurplusTaxMinimumSalary(new BigDecimal(8000))
                .withSurplusTaxRatePerDollar(new BigDecimal(0.3)).build();
        Set<TaxBracket> taxBrackets = new HashSet<>();
        taxBrackets.add(taxBracket);
        /**
         * Set up a tax year
         */
        LocalDate taxYearStart = LocalDate.of(2014, Month.JULY, 1);
        LocalDate taxYearEnd = LocalDate.of(2015, Month.JUNE, 30);
        TaxYearInformation taxYear = new TaxYearInformation(taxYearStart, taxYearEnd, taxBrackets);
        Set<TaxYearInformation> allTaxYears = new HashSet<>();
        allTaxYears.add(taxYear);
        /**
         * Create an employee with salary that will fit into the above.
         * Make this salary period a custom one - x amount of days
         */
        Employee employee = new Employee("BoJack", "Horseman");
        LocalDate salaryStartDate = LocalDate.of(2014, Month.OCTOBER, 5);
        LocalDate salaryEndDate = LocalDate.of(2014, Month.OCTOBER, 9); // 5 day period (inclusive)
        Salary salary = Salary.builder().withAnnualSalary(new BigDecimal(45000))
                .withAnnualSuperRate(new BigDecimal(0.3))
                .withStartDate(salaryStartDate)
                .withEndDate(salaryEndDate).build();
        EmployeeSalary employeeSalary = new EmployeeSalary(employee, salary);

        // Let's do the calculations
        Optional<EmployeePayStub> payStubOptional = new PayStubCalculator(allTaxYears).calculate(employeeSalary);
        assertTrue("Expected calculated pay stub", payStubOptional.isPresent());

        EmployeePayStub payStub = payStubOptional.get();
        // Match the gross => 45000 / (365 / 5) = 616
        assertEquals(new BigDecimal(616), payStub.getGrossIncome());
        // Income tax => (5000 + ((45000 - 8000) * 0.3) / (365 / 5) = 16100 / 221
        assertEquals(new BigDecimal(221), payStub.getIncomeTax());
        // Net Income => 616 - 221 = 395 (rounding)
        assertEquals(new BigDecimal(396), payStub.getNetIncome());
        // Super rate => 616 * 0.3 = 184
        assertEquals(new BigDecimal(184), payStub.getGrossSuper());
    }

    @Test
    public void makeSureCalculatorWorksAsExpectedForVariousTaxYears() {
        EmployeeSalary davidRuddEmployeeSalary = getDavidRuddEmployeeSalary();
        int davidRuddTaxYear = davidRuddEmployeeSalary.getSalary().getStartDate().getYear();

        Set<TaxYearInformation> taxYearInformationSet = new HashSet<>();
        // Get taxable year information for a year that is not in the employee's start date
        TaxYearInformation nonDavidTaxYearInfo = createTaxYearInformation(davidRuddTaxYear+10);
        taxYearInformationSet.add(nonDavidTaxYearInfo);

        PayStubCalculator payStubCalculator = new PayStubCalculator(taxYearInformationSet);
        assertThat(payStubCalculator.getSupportedTaxYears()).contains(nonDavidTaxYearInfo.getYear());

        Optional<EmployeePayStub> payStubOptional = payStubCalculator.calculate(davidRuddEmployeeSalary);
        assertThat(payStubOptional.isPresent()).isFalse();

        // Now, set tax year information that this employee will have pay stubs generated for
        taxYearInformationSet.add(createTaxYearInformation(davidRuddTaxYear));
        payStubCalculator = new PayStubCalculator(taxYearInformationSet);

        payStubOptional = payStubCalculator.calculate(davidRuddEmployeeSalary);
        assertThat(payStubOptional.isPresent()).isTrue(); // We now should get a paystub since the calculator has the year
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertPayrollCalculatorValidatesInputForNull() {
        new PayStubCalculator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertPayrollCalculatorValidatesInputForEmptySet() {
        new PayStubCalculator(new HashSet<>());
    }

    private void assertPayStubCalculationsAreCorrect(Set<EmployeePayStub> expectedCalculatedPayStubs) {
        PayStubCalculator payStubCalculator = getDefaultCalculator();
        Set<EmployeeSalary> allInterviewInput = getValidEmployeeSalaryInput();
        Set<EmployeePayStub> calculatedPayStubs = payStubCalculator.calculate(allInterviewInput);

        assertThat(expectedCalculatedPayStubs).containsAll(calculatedPayStubs);
    }

    private PayStubCalculator getDefaultCalculator() {
        Set<TaxYearInformation> allTaxYearInformation = new TaxInformationJsonFileReader().getAll();
        return new PayStubCalculator(allTaxYearInformation);
    }

    private Set<EmployeeSalary> getValidEmployeeSalaryInput() {
        Set<EmployeeSalary> allInput = new HashSet<>();
        allInput.add(getDavidRuddEmployeeSalary());
        allInput.add(getRyanChenEmployeeSalary());
        return allInput;
    }

    private Set<EmployeePayStub> getValidExpectedPayStubResults() {
        Set<EmployeePayStub> allExpectedPayStubs = new HashSet<>();
        allExpectedPayStubs.add(getDavidRuddExpectedPayStub());
        allExpectedPayStubs.add(getRyanChenExpectedPayStub());
        return allExpectedPayStubs;
    }

    private EmployeePayStub getDavidRuddExpectedPayStub() {
        EmployeeSalary davidRuddEmployeeSalary = getDavidRuddEmployeeSalary();
        return EmployeePayStub.builder()
                .withEmployee(davidRuddEmployeeSalary.getEmployee())
                .withGrossIncome(BigDecimal.valueOf(5004))
                .withNetIncome(BigDecimal.valueOf(4082))
                .withGrossSuper(BigDecimal.valueOf(450))
                .withIncomeTax(BigDecimal.valueOf(922))
                .withStartDate(davidRuddEmployeeSalary.getSalary().getStartDate())
                .withEndDate(davidRuddEmployeeSalary.getSalary().getEndDate()).build();
    }

    private EmployeePayStub getRyanChenExpectedPayStub() {
        EmployeeSalary ryanChenEmployeeSalary = getRyanChenEmployeeSalary();
        return EmployeePayStub.builder()
                .withEmployee(ryanChenEmployeeSalary.getEmployee())
                .withGrossIncome(BigDecimal.valueOf(10000))
                .withNetIncome(BigDecimal.valueOf(7304))
                .withGrossSuper(BigDecimal.valueOf(1000))
                .withIncomeTax(BigDecimal.valueOf(2696))
                .withStartDate(ryanChenEmployeeSalary.getSalary().getStartDate())
                .withEndDate(ryanChenEmployeeSalary.getSalary().getEndDate()).build();
    }

    private EmployeeSalary getDavidRuddEmployeeSalary() {
        Employee davidRudd = new Employee("David", "Rudd");
        Salary davidRuddSalary = Salary.builder()
                .withAnnualSalary(new BigDecimal(60050))
                .withAnnualSuperRate(new BigDecimal(0.09))
                .withStartDate(LocalDate.of(2013, Month.MARCH, 1))
                .withEndDate(LocalDate.of(2013, Month.MARCH, 31))
                .build();
        return new EmployeeSalary(davidRudd, davidRuddSalary);
    }

    private EmployeeSalary getRyanChenEmployeeSalary() {
        Employee ryanChen = new Employee("Ryan", "Chen");
        Salary ryanChenSalary = Salary.builder()
                .withAnnualSalary(new BigDecimal(120000))
                .withAnnualSuperRate(new BigDecimal(0.1))
                .withStartDate(LocalDate.of(2013, Month.MARCH, 1))
                .withEndDate(LocalDate.of(2013, Month.MARCH, 31))
                .build();
        return new EmployeeSalary(ryanChen, ryanChenSalary);
    }

    private EmployeeSalary getMarkCunninghamSalary() {
        Employee mark = new Employee("Mark", "Cunningham");
        Salary markSalary = Salary.builder()
                .withAnnualSalary(new BigDecimal(0))
                .withAnnualSuperRate(new BigDecimal(0.15))
                .withStartDate(LocalDate.of(2013, Month.JANUARY, 5))
                .withEndDate(LocalDate.of(2013, Month.MARCH, 5))
                .build();
        return new EmployeeSalary(mark, markSalary);
    }

    private EmployeePayStub getMarkCunninghamExpectedPayStub() {
        EmployeeSalary markSalary = getMarkCunninghamSalary();
        return EmployeePayStub.builder()
                .withEmployee(markSalary.getEmployee())
                .withGrossIncome(BigDecimal.valueOf(0))
                .withNetIncome(BigDecimal.valueOf(0))
                .withGrossSuper(BigDecimal.valueOf(0))
                .withIncomeTax(BigDecimal.valueOf(0))
                .withStartDate(markSalary.getSalary().getStartDate())
                .withEndDate(markSalary.getSalary().getEndDate()).build();
    }

    private EmployeeSalary getJohnWayneSalary() {
        Employee john = new Employee("John", "Wayne");
        Salary johnSalary = Salary.builder()
                .withAnnualSalary(new BigDecimal(19800))
                .withAnnualSuperRate(new BigDecimal(0.24))
                .withStartDate(LocalDate.of(2013, Month.JUNE, 6))
                .withEndDate(LocalDate.of(2013, Month.JUNE, 15))
                .build();
        return new EmployeeSalary(john, johnSalary);
    }

    private EmployeePayStub getJohnWayneExpectedPayStub() {
        EmployeeSalary johnSalary = getJohnWayneSalary();
        return EmployeePayStub.builder()
                .withEmployee(johnSalary.getEmployee())
                .withGrossIncome(BigDecimal.valueOf(542))
                .withNetIncome(BigDecimal.valueOf(534))
                .withGrossSuper(BigDecimal.valueOf(130))
                .withIncomeTax(BigDecimal.valueOf(9))
                .withStartDate(johnSalary.getSalary().getStartDate())
                .withEndDate(johnSalary.getSalary().getEndDate()).build();
    }

    private EmployeeSalary getClarkKentSalary() {
        Employee clark = new Employee("Clark", "Kent");
        Salary clarkSalary = Salary.builder()
                .withAnnualSalary(new BigDecimal(57000))
                .withAnnualSuperRate(new BigDecimal(0.13))
                .withStartDate(LocalDate.of(2012, Month.OCTOBER, 11))
                .withEndDate(LocalDate.of(2012, Month.NOVEMBER, 5))
                .build();
        return new EmployeeSalary(clark, clarkSalary);
    }

    private EmployeePayStub getClarkKentExpectedPayStub() {
        EmployeeSalary clarkSalary = getClarkKentSalary();
        return EmployeePayStub.builder()
                .withEmployee(clarkSalary.getEmployee())
                .withGrossIncome(BigDecimal.valueOf(4060))
                .withNetIncome(BigDecimal.valueOf(3343))
                .withGrossSuper(BigDecimal.valueOf(527))
                .withIncomeTax(BigDecimal.valueOf(718))
                .withStartDate(clarkSalary.getSalary().getStartDate())
                .withEndDate(clarkSalary.getSalary().getEndDate()).build();
    }

    private EmployeeSalary getAustinPowersSalary() {
        Employee clark = new Employee("Austin", "Powers");
        Salary clarkSalary = Salary.builder()
                .withAnnualSalary(new BigDecimal(122300))
                .withAnnualSuperRate(new BigDecimal(0.19))
                .withStartDate(LocalDate.of(2013, Month.JANUARY, 1))
                .withEndDate(LocalDate.of(2013, Month.APRIL, 25))
                .build();
        return new EmployeeSalary(clark, clarkSalary);
    }

    private EmployeePayStub getAustinPowersExpectedPayStub() {
        EmployeeSalary austinSalary = getAustinPowersSalary();
        return EmployeePayStub.builder()
                .withEmployee(austinSalary.getEmployee())
                .withGrossIncome(BigDecimal.valueOf(38532))
                .withNetIncome(BigDecimal.valueOf(28073))
                .withGrossSuper(BigDecimal.valueOf(7321))
                .withIncomeTax(BigDecimal.valueOf(10460))
                .withStartDate(austinSalary.getSalary().getStartDate())
                .withEndDate(austinSalary.getSalary().getEndDate()).build();
    }

    private EmployeeSalary getStephenGerardSalary() {
        Employee clark = new Employee("Stephen", "Gerard");
        Salary clarkSalary = Salary.builder()
                .withAnnualSalary(new BigDecimal(450000))
                .withAnnualSuperRate(new BigDecimal(0.43))
                // These start dates will cross over years
                .withStartDate(LocalDate.of(2012, Month.DECEMBER, 8))
                .withEndDate(LocalDate.of(2013, Month.MARCH, 17))
                .build();
        return new EmployeeSalary(clark, clarkSalary);
    }

    private EmployeePayStub getStephenGerardExpectedPayStub() {
        EmployeeSalary stephenSalary = getStephenGerardSalary();
        return EmployeePayStub.builder()
                .withEmployee(stephenSalary.getEmployee())
                .withGrossIncome(BigDecimal.valueOf(123287))
                .withNetIncome(BigDecimal.valueOf(75056))
                .withGrossSuper(BigDecimal.valueOf(53013))
                .withIncomeTax(BigDecimal.valueOf(48233))
                .withStartDate(stephenSalary.getSalary().getStartDate())
                .withEndDate(stephenSalary.getSalary().getEndDate()).build();
    }

    private TaxYearInformation createTaxYearInformation(int year) {
        TaxBracket below25k = TaxBracket.builder()
                                .withMinimumSalary(BigDecimal.ZERO).withMaximumSalary(new BigDecimal(25000))
                                .withTaxBaseAmount(new BigDecimal(4500)).withSurplusTaxMinimumSalary(BigDecimal.ZERO)
                                .withSurplusTaxRatePerDollar(new BigDecimal(0.1)).build();

        TaxBracket above25kBelow50k = TaxBracket.builder()
                                .withMinimumSalary(new BigDecimal(25001)).withMaximumSalary(new BigDecimal(50000))
                                .withTaxBaseAmount(new BigDecimal(8600)).withSurplusTaxMinimumSalary(new BigDecimal(25000))
                                .withSurplusTaxRatePerDollar(new BigDecimal(0.17)).build();

        TaxBracket above50k = TaxBracket.builder()
                                .withMinimumSalary(new BigDecimal(50001)).withMaximumSalary(null)
                                .withTaxBaseAmount(new BigDecimal(14500)).withSurplusTaxMinimumSalary(new BigDecimal(50000))
                                .withSurplusTaxRatePerDollar(new BigDecimal(0.26)).build();
        Set<TaxBracket> brackets = new HashSet<>();
        brackets.add(below25k);
        brackets.add(above25kBelow50k);
        brackets.add(above50k);

        return new TaxYearInformation(LocalDate.of(year-1, Month.JULY, 1), LocalDate.of(year, Month.JUNE, 30), brackets);
    }

}
