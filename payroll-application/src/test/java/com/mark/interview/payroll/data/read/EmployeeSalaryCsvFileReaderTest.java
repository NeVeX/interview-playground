package com.mark.interview.payroll.data.read;

import com.mark.interview.payroll.TestingUtils;
import com.mark.interview.payroll.util.DateUtils;
import com.mark.interview.payroll.model.Employee;
import com.mark.interview.payroll.model.EmployeeSalary;
import com.mark.interview.payroll.model.Salary;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class EmployeeSalaryCsvFileReaderTest {

    private final static String COMMA = ",";

    @Test
    public void assertValidEmployeeSalaryCsvIsRead() {
        // Create an example of a csv file
        String header = EmployeeSalaryCsvFileReader.HEADER;
        EmployeeSalary employeeSalary = createValidEmployeeSalary();
        String csvDataLine = createCsvLine(employeeSalary);

        File randomValidFile = TestingUtils.createRandomCsvFileWithData(header+System.lineSeparator()+csvDataLine);

        DataReaderResult<EmployeeSalary> parsedData = new EmployeeSalaryCsvFileReader().readFile(randomValidFile.getAbsolutePath());

        assertNotNull("Expected a non null data result for csv read", parsedData);
        assertTrue("Expected the random csv file to be found", parsedData.wasResourceFound());
        assertEquals("Expected the resource file name to match", randomValidFile.getAbsolutePath(), parsedData.getResourceUsed());
        assertEquals("Expected only one data parse count", 1, parsedData.getValidDataCount());
        assertEquals("Expected there to be no invalid data parse count", 0, parsedData.getInvalidDataCount());
        assertTrue("Expected data to be present in the csv data read", parsedData.hasData());

        Set<EmployeeSalary> givenInputData = new HashSet<>();
        givenInputData.add(employeeSalary);

        assertThat(givenInputData).containsAll(parsedData.getData()); // Both data sets should be equal
    }

    @Test
    public void assertEmptyMissingFirstNameColumnsCausesNoDataExtraction() {
        String header = EmployeeSalaryCsvFileReader.HEADER;
        String missingFirstName = createCsvLine("", "Cunningham", "1234", "12", "03/01/2013", "03/03/2013");
        validateNoDataIsExtracted(header+System.lineSeparator()+missingFirstName);
    }

    @Test
    public void assertEmptyMissingLastNameColumnsCausesNoDataExtraction() {
        String header = EmployeeSalaryCsvFileReader.HEADER;
        String missingLastName = createCsvLine("Mark", "", "1234", "12", "03/01/2013", "03/03/2013");
        validateNoDataIsExtracted(header+System.lineSeparator()+missingLastName);
    }

    @Test
    public void assertEmptyMissingAnnualSalaryColumnsCausesNoDataExtraction() {
        String header = EmployeeSalaryCsvFileReader.HEADER;
        String missingAnnualSalary = createCsvLine("Mark", "Cunningham", "", "12", "03/01/2013", "03/03/2013");
        validateNoDataIsExtracted(header+System.lineSeparator()+missingAnnualSalary);
    }

    @Test
    public void assertEmptyMissingSuperRateColumnsCausesNoDataExtraction() {
        String header = EmployeeSalaryCsvFileReader.HEADER;
        String missingSuperRate = createCsvLine("Mark", "Cunningham", "1234", "", "03/01/2013", "03/03/2013");
        validateNoDataIsExtracted(header+System.lineSeparator()+missingSuperRate);
    }

    @Test
    public void assertEmptyMissingStartDateColumnsCausesNoDataExtraction() {
        String header = EmployeeSalaryCsvFileReader.HEADER;
        String missingStartDate = createCsvLine("Mark", "Cunningham", "1234", "12", "", "03/03/2013");
        validateNoDataIsExtracted(header+System.lineSeparator()+missingStartDate);
    }

    @Test
    public void assertEmptyMissingEndDateColumnsCausesNoDataExtraction() {
        String header = EmployeeSalaryCsvFileReader.HEADER;
        String missingStartDate = createCsvLine("Mark", "Cunningham", "1234", "12", "03/03/2013", "");
        validateNoDataIsExtracted(header+System.lineSeparator()+missingStartDate);
    }


    @Test
    public void assertNegativeSuperRateColumnsCausesNoDataExtraction() {
        String header = EmployeeSalaryCsvFileReader.HEADER;
        String missingSuperRate = createCsvLine("Mark", "Cunningham", "1234", "-10", "03/01/2013", "03/03/2013");
        validateNoDataIsExtracted(header+System.lineSeparator()+missingSuperRate);
    }

    @Test
    public void assertSuperRateGreaterThan50ColumnsCausesNoDataExtraction() {
        String header = EmployeeSalaryCsvFileReader.HEADER;
        String missingSuperRate = createCsvLine("Mark", "Cunningham", "1234", "65", "03/01/2013", "03/03/2013");
        validateNoDataIsExtracted(header+System.lineSeparator()+missingSuperRate);
    }

    @Test
    public void assertNegativeAnnualSalaryColumnsCausesNoDataExtraction() {
        String header = EmployeeSalaryCsvFileReader.HEADER;
        String missingSuperRate = createCsvLine("Mark", "Cunningham", "-50450", "65", "03/01/2013", "03/03/2013");
        validateNoDataIsExtracted(header+System.lineSeparator()+missingSuperRate);
    }

    @Test
    public void assertInvalidHeaderCausesNoDataExtraction() {
        EmployeeSalary employeeSalary = createValidEmployeeSalary();
        String csvLine = createCsvLine(employeeSalary);

        File randomFileWithNoHeader = TestingUtils.createRandomCsvFileWithData(csvLine);
        DataReaderResult<EmployeeSalary> parsedData = new EmployeeSalaryCsvFileReader().readFile(randomFileWithNoHeader.getAbsolutePath());

        assertNotNull("Expected a non null data result for csv read", parsedData);
        assertEquals("Expected no data parse count", 0, parsedData.getValidDataCount());
        assertFalse("Expected no data to be present in the csv data read", parsedData.hasData());
    }

    @Test
    public void assertInvalidFileAsInput() {
        File nonExistenceFile = new File("file_is_a_ghost");
        DataReaderResult<EmployeeSalary> parsedData = new EmployeeSalaryCsvFileReader().readFile(nonExistenceFile.getAbsolutePath());
        assertNotNull("Expected a non null data result for csv read", parsedData);
        assertEquals("Expected no data parse count", 0, parsedData.getValidDataCount());
        assertFalse("Expected no data to be present in the csv data read", parsedData.hasData());
        assertFalse("Expected resource to not be found", parsedData.wasResourceFound());
    }

    @Test
    public void assertNullFileAsInput() {
        // Do a simple check to make sure that null inputs don't screw us over
        DataReaderResult<EmployeeSalary> parsedData = new EmployeeSalaryCsvFileReader().readFile(null);
        assertNotNull("Expected a non null data result for csv read", parsedData);
        assertFalse("Expected resource to not be found", parsedData.wasResourceFound());
    }

    private EmployeeSalary createValidEmployeeSalary() {
        Employee employee = new Employee("Mark", "Cunningham");
        Salary salary = Salary.builder()
                .withAnnualSalary(BigDecimal.valueOf(55000))
                .withAnnualSuperRate(BigDecimal.valueOf(10))
                .withStartDate(LocalDate.now())
                .withEndDate(LocalDate.now().plusDays(30))
                .build();
        return new EmployeeSalary(employee, salary);
    }

    private void validateNoDataIsExtracted(String inputCsvData) {
        // Test this missing first name
        File randomFileWithEmptyColumn = TestingUtils.createRandomCsvFileWithData(inputCsvData);
        DataReaderResult<EmployeeSalary> parsedData = new EmployeeSalaryCsvFileReader().readFile(randomFileWithEmptyColumn.getAbsolutePath());
        // Make sure nothing was read
        assertEquals("Expected no data parse count", 0, parsedData.getValidDataCount());
        assertFalse("Expected no data to be present in the csv data read", parsedData.hasData());
    }

    private String createCsvLine(EmployeeSalary employeeSalary) {
        return createCsvLine(employeeSalary.getEmployee().getFirstName(),
                employeeSalary.getEmployee().getLastName(),
                employeeSalary.getSalary().getAnnualSalary().toString(),
                employeeSalary.getSalary().getAnnualSuperRate().toString(),
                DateUtils.formatDate(employeeSalary.getSalary().getStartDate()),
                DateUtils.formatDate(employeeSalary.getSalary().getEndDate()));
    }

    private String createCsvLine(String firstName, String lastName, String annualSalary,
                                 String superRate, String startDate, String endDate) {
        return firstName + COMMA
                + lastName + COMMA
                + annualSalary + COMMA
                + superRate + COMMA
                + startDate + COMMA
                + endDate;
    }



}
