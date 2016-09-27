package com.mark.interview.payroll.data.read;

import com.mark.interview.payroll.model.TaxBracket;
import com.mark.interview.payroll.model.TaxYearInformation;
import org.junit.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class TaxInformationJsonFileReaderTest {

    @Test
    public void assertAllJsonTaxFilesAreReadFromResources() {
        Set<TaxYearInformation> allTaxYears = new TaxInformationJsonFileReader().getAll();
        assertNotNull("Expected tax years to be read from resources", allTaxYears);
        assertFalse("Expected non empty set of tax years to read", allTaxYears.isEmpty());
        assertEquals("Expected only 1 tax year to be read from resources", 1, allTaxYears.size());

        Set<Integer> expectedTaxYears = new HashSet<>();
        expectedTaxYears.add(2013);

        expectedTaxYears.forEach(expectedTaxYear -> {
            Optional<TaxYearInformation> foundTaxYear =
                    allTaxYears.stream().filter(readTaxYear -> readTaxYear.getYear() == expectedTaxYear).findFirst();
            if (!foundTaxYear.isPresent()) {
                fail("Could not find expected tax year [" + expectedTaxYear + "] in application tax years");
            }
        });
    }

    @Test // For each year we have, make sure the brackets are valid (not overlapping etc)
    public void assertAllTaxYearsBracketInformationIsValid() {
        // Get all the tax years
        Set<TaxYearInformation> allTaxYears = new TaxInformationJsonFileReader().getAll();
        allTaxYears.forEach(taxYear -> validateTaxBrackets(taxYear.getYear(), taxYear.getTaxBrackets()));
    }

    private void validateTaxBrackets(int year, Set<TaxBracket> taxBrackets) {
        // For each bracket, make sure there is not a conflicting one
        // For example; there should not be 2 brackets for the same salary range
        taxBrackets.stream().forEach( outerTaxBracket ->
            taxBrackets.stream().filter(innerTaxBracket ->
                    outerTaxBracket != innerTaxBracket
                    && (innerTaxBracket.isSalaryWithinBracket(outerTaxBracket.getMinimumSalary())
                        || ( outerTaxBracket.getMaximumSalary().isPresent()
                            && innerTaxBracket.isSalaryWithinBracket(outerTaxBracket.getMaximumSalary().get()))))
                .findAny().ifPresent(badTaxBracket ->
                    fail("Found an overlapping tax bracket in tax year ["+year+"] " +
                        "\nFirst Tax Bracket: " + outerTaxBracket + "." +
                        "\nSecond Tax Bracket: " + badTaxBracket)
        ));
    }

}
