package com.mark.interview.payroll.model;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.math.BigDecimal;

import static com.mark.interview.payroll.data.DataFormatConfiguration.DEFAULT_DECIMAL_SCALE;
import static com.mark.interview.payroll.data.DataFormatConfiguration.DEFAULT_ROUNDING;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class TaxBracketTest {

    /**
     * The tax bracket has methods to determine if a salary range is within a bracket, so we test this here,
     * since it's crucial to the paystub calculations
     */
    @Test
    public void assertTaxBracketBoundariesWithoutPrecision() {
        BigDecimal minimumSalary = BigDecimal.ZERO;
        BigDecimal maximumSalary = new BigDecimal(10000);
        TaxBracket zeroTo10k = createTaxBracket(minimumSalary, maximumSalary);
        validateSalaryFitsWithinBracket(zeroTo10k, minimumSalary);
        validateSalaryFitsWithinBracket(zeroTo10k, maximumSalary);
        validateSalaryFitsWithinBracket(zeroTo10k, minimumSalary.add(BigDecimal.ONE));
        validateSalaryFitsWithinBracket(zeroTo10k, maximumSalary.subtract(BigDecimal.ONE));

        // Make sure when the bounds are breached, that the tax bracket does not apply anymore
        validateSalaryDoesNotFitWithinBracket(zeroTo10k, minimumSalary.subtract(BigDecimal.ONE));
        validateSalaryDoesNotFitWithinBracket(zeroTo10k, maximumSalary.add(BigDecimal.ONE));
    }

    @Test
    public void assertTaxBracketBoundariesWithPrecisions() {
        // Set the decimal places to the right on the 5's - if the scale changes in code, it should break here
        String minimumSalaryString = "60000.555555555555555555555555555555555555555555555555555555555555";
        String maximumSalaryString = "130000.555555555555555555555555555555555555555555555555555555555555";

        BigDecimal minimumSalary = new BigDecimal(minimumSalaryString).setScale(DEFAULT_DECIMAL_SCALE, DEFAULT_ROUNDING);
        BigDecimal maximumSalary = new BigDecimal(maximumSalaryString).setScale(DEFAULT_DECIMAL_SCALE, DEFAULT_ROUNDING);
        TaxBracket sixtyTo130k = createTaxBracket(minimumSalary, maximumSalary);
        validateSalaryFitsWithinBracket(sixtyTo130k, minimumSalary);
        validateSalaryFitsWithinBracket(sixtyTo130k, maximumSalary);
        // Since we expect the rounding to make the last digit of the decimal a "6", we can change scales directly
        // Increase the minimum salary scale
        validateSalaryFitsWithinBracket(sixtyTo130k, new BigDecimal(StringUtils.removeEnd(minimumSalary.toString(), "6") + "61"));
        // Same for the max salary
        validateSalaryFitsWithinBracket(sixtyTo130k, new BigDecimal(StringUtils.removeEnd(maximumSalary.toString(), "6" + "59")));
        // Now do the reverse, reduce the scale so that it falls outside the range
        validateSalaryDoesNotFitWithinBracket(sixtyTo130k, new BigDecimal(StringUtils.removeEnd(minimumSalary.toString(), "6") + "59"));
        validateSalaryDoesNotFitWithinBracket(sixtyTo130k, new BigDecimal(StringUtils.removeEnd(maximumSalary.toString(), "6") + "61"));
    }

    @Test
    public void assertValidTaxBracketBuilder() {
        createValidTaxBracketBuilder().build(); // No exceptions
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForTaxBracketBuilderWithNullMinimumSalary() {
        createValidTaxBracketBuilder().withMinimumSalary(null).build();
    }

    @Test
    public void assertNoExceptionThrownForTaxBracketBuilderWithNullMaximumSalary() {
        createValidTaxBracketBuilder().withMaximumSalary(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForTaxBracketBuilderWithNullBaseTaxAmount() {
        createValidTaxBracketBuilder().withTaxBaseAmount(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForTaxBracketBuilderWithNullSurplusMinimum() {
        createValidTaxBracketBuilder().withSurplusTaxMinimumSalary(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForTaxBracketBuilderWithNullSurplusAmountPerDollar() {
        createValidTaxBracketBuilder().withSurplusTaxRatePerDollar(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForTaxBracketBuilderWithNegativeMinimumSalary() {
        createValidTaxBracketBuilder().withMinimumSalary(new BigDecimal(-10000)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForTaxBracketBuilderWithNegativeMaximumSalary() {
        createValidTaxBracketBuilder().withMaximumSalary(new BigDecimal(-10000)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForTaxBracketBuilderWithNegativeTaxBaseAmount() {
        createValidTaxBracketBuilder().withTaxBaseAmount(new BigDecimal(-10000)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForTaxBracketBuilderWithNegativeSurplusMinimum() {
        createValidTaxBracketBuilder().withSurplusTaxMinimumSalary(new BigDecimal(-10000)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForTaxBracketBuilderWithNegativeSurplusRate() {
        createValidTaxBracketBuilder().withSurplusTaxRatePerDollar(new BigDecimal(-0.1)).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownForTaxBracketBuilderWithSurplusRateExceeding100() {
        createValidTaxBracketBuilder().withSurplusTaxRatePerDollar(new BigDecimal(1.1)).build();
    }

    private void validateSalaryFitsWithinBracket(TaxBracket taxBracket, BigDecimal salary) {
        assertTrue("Salary ["+salary+"] should of fit within tax bracket range " +
                "["+taxBracket.getMinimumSalary()+" - "+taxBracket.getMaximumSalary()+"]", taxBracket.isSalaryWithinBracket(salary));
    }

    private void validateSalaryDoesNotFitWithinBracket(TaxBracket taxBracket, BigDecimal salary) {
        assertFalse("Salary ["+salary+"] should NOT of fit within tax bracket range " +
                "["+taxBracket.getMinimumSalary()+" - "+taxBracket.getMaximumSalary()+"]", taxBracket.isSalaryWithinBracket(salary));
    }

    private TaxBracket createTaxBracket(BigDecimal minimumSalary, BigDecimal maximumSalary) {
        return TaxBracket.builder()
                    .withMinimumSalary(minimumSalary)
                    .withMaximumSalary(maximumSalary)
                    .withTaxBaseAmount(new BigDecimal(3000))
                    .withSurplusTaxMinimumSalary(minimumSalary.add(BigDecimal.ONE))
                    .withSurplusTaxRatePerDollar(new BigDecimal(0.1))
                    .build();
    }

    private TaxBracket.Builder createValidTaxBracketBuilder() {
        return TaxBracket.builder()
                .withMinimumSalary(new BigDecimal(20000))
                .withMaximumSalary(new BigDecimal(150000))
                .withTaxBaseAmount(new BigDecimal(3000))
                .withSurplusTaxMinimumSalary(new BigDecimal(40000))
                .withSurplusTaxRatePerDollar(new BigDecimal(0.1));
    }
}
