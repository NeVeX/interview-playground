package com.mark.interview.payroll.data.write;

import com.mark.interview.payroll.TestingUtils;
import com.mark.interview.payroll.data.read.CsvFileReader;
import com.mark.interview.payroll.data.read.DataReaderResult;
import com.mark.interview.payroll.model.Employee;
import com.mark.interview.payroll.model.EmployeePayStub;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class EmployeePayStubCsvFileWriterTest extends CsvFileReader<String> {

    @Test
    public void assertWrittenPayStubIsValid() {

        File fileToWriteTo = TestingUtils.createRandomCsvFile();

        Set<EmployeePayStub> allPayStubs = new HashSet<>();
        allPayStubs.add(getMarkEmployeePayStub());
        allPayStubs.add(getJohnEmployeePayStub());

        DataWriterResult writerResult = new EmployeePayStubCsvFileWriter().writeToFile(fileToWriteTo.getAbsolutePath(), allPayStubs);

        assertTrue("Expected csv file to be successfully written", writerResult.isSuccessfullyWritten());
        assertEquals("Csv file used to write data is not correct", fileToWriteTo.getAbsolutePath(), writerResult.getResourceUsed());

        // Now read in the file that was just created
        DataReaderResult<String> dataReaderResult = super.readFile(fileToWriteTo.getAbsolutePath());
        // There should be no header read in
        assertEquals("Expected only a certain number of read csv lines", 2, dataReaderResult.getValidDataCount());
        List<String> parsedCsvLines = new ArrayList<>(dataReaderResult.getData());
        List<String> expectedCsvLines = new ArrayList<>();
        expectedCsvLines.add("Mark Cunningham,05/01/2013 - 05/03/2013,55200,550,1200,400");
        expectedCsvLines.add("John Wayne,10/06/2013 - 15/06/2013,4500000,90,55000,67000");

        assertThat(expectedCsvLines).containsAll(parsedCsvLines);
    }

    @Test
    public void assertNonExistenceFileWrite() {
        File nonExistenceFile = new File(UUID.randomUUID().toString()+".tmp");
        nonExistenceFile.deleteOnExit(); // delete afterwards
        assertFalse(nonExistenceFile.exists()); // just make sure (one in a bazillion)
        Set<EmployeePayStub> payStubs = new HashSet<>();
        payStubs.add(getMarkEmployeePayStub()); // valid paystubs
        DataWriterResult writerResult = new EmployeePayStubCsvFileWriter().writeToFile(nonExistenceFile.getAbsolutePath(), payStubs);
        assertTrue("The non existence file should have been created/written to", writerResult.isSuccessfullyWritten());
        assertTrue(nonExistenceFile.exists()); // it should now exist
    }

    @Test
    public void assertEmptyOrNullPayStubsFileWrite() {
        File randomFile = TestingUtils.createRandomCsvFile();
        Set<EmployeePayStub> payStubs = new HashSet<>();
        // Send these empty paystubs
        DataWriterResult writerResult = new EmployeePayStubCsvFileWriter().writeToFile(randomFile.getAbsolutePath(), payStubs);
        assertFalse("The data file should not of been written to since there is no data", writerResult.isSuccessfullyWritten());
        // Now send in NULL
        writerResult = new EmployeePayStubCsvFileWriter().writeToFile(randomFile.getAbsolutePath(), null);
        assertFalse("The data file should not of been written to since NULL data was used", writerResult.isSuccessfullyWritten());
    }

    private EmployeePayStub getMarkEmployeePayStub() {
        Employee mark = new Employee("Mark", "Cunningham");
        return EmployeePayStub.builder()
                .withEmployee(mark)
                .withGrossIncome(BigDecimal.valueOf(55200))
                .withNetIncome(BigDecimal.valueOf(1200))
                .withGrossSuper(BigDecimal.valueOf(400))
                .withIncomeTax(BigDecimal.valueOf(550))
                .withStartDate(LocalDate.of(2013, Month.JANUARY, 5))
                .withEndDate(LocalDate.of(2013, Month.MARCH, 5)).build();
    }

    private EmployeePayStub getJohnEmployeePayStub() {
        Employee john = new Employee("John", "Wayne");
        return EmployeePayStub.builder()
                .withEmployee(john)
                .withGrossIncome(BigDecimal.valueOf(4500000))
                .withNetIncome(BigDecimal.valueOf(55000))
                .withGrossSuper(BigDecimal.valueOf(67000))
                .withIncomeTax(BigDecimal.valueOf(90))
                .withStartDate(LocalDate.of(2013, Month.JUNE, 10))
                .withEndDate(LocalDate.of(2013, Month.JUNE, 15)).build();
    }

    @Override
    protected String extractFromCsvLine(Scanner lineScanner) {
        if (lineScanner.hasNextLine()) {
            return lineScanner.nextLine();
        }
        return null;
     }

    @Override
    protected boolean isCsvHeaderValid(Scanner headerScanner) {
        // Make sure the header is as expected using an exact match
        return headerScanner.hasNextLine() && StringUtils.equals(headerScanner.nextLine(), EmployeePayStubCsvFileWriter.HEADER);
    }
}
