package com.mark.interview.payroll;

import com.mark.interview.payroll.data.read.EmployeeSalaryCsvFileReader;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class PayrollApplicationTest {

    private static final String OUTPUT_PREFIX = "output-paystubs-";

    @Test
    public void assertExitCodeForApplicationRunHappyPath() {

        String validEmployeeSalaryData = getValidEmployeeSalaryCsvData(); // Get valid data
        // Create a random file with the above data in it
        File randomAndValidInputFile = TestingUtils.createRandomCsvFileWithData(validEmployeeSalaryData);

        int exitCode = runApplication(randomAndValidInputFile); // Run the application
        assertEquals("Exit code is not as expected", 0, exitCode); // Validate that the exit code is good (zero)
        // Now, make sure the paystubs were saved
        Optional<File> payStubFileOptional = getExpectedOutputFileFromInputOption(randomAndValidInputFile);
        // Did we find it?
        assertTrue("Could not find the output paystub file", payStubFileOptional.isPresent());
        payStubFileOptional.get().deleteOnExit(); // delete this file - clean house
    }

    @Test
    public void assertExitCodeForMissingFileInputParameter() {
        int exitCode = new PayrollApplication().run(new String[0]); // Run the application with no args
        assertEquals("Exit code is not as expected", 1, exitCode); // Exit code should state no inputs given
    }

    @Test
    public void assertExitCodeForEmptyFileInputParameter() {
        File randomButEmptyInputFile = TestingUtils.createRandomCsvFile(); // Create a random file - but don't save data to it
        int exitCode = runApplication(randomButEmptyInputFile); // Run the application
        assertEquals("Exit code is not as expected", 2, exitCode); // Exit code should state no data found
    }

    @Test
    public void assertExitCodeForInvalidFileInputParameter() {
        File nonExistenceFile = new File("i_dont_exist"); // Create a file handle to nothing
        int exitCode = runApplication(nonExistenceFile); // Run the application
        assertEquals("Exit code is not as expected", 2, exitCode); // Exit code should state no data found
    }

    @Test
    public void assertExitCodeForFileWriteProblem() {

        String validEmployeeSalaryData = getValidEmployeeSalaryCsvData();
        File randomValidInputFile = TestingUtils.createRandomCsvFileWithData(validEmployeeSalaryData);

        // Get the file handle on the expected output file
        File expectedOutputFile = new File(randomValidInputFile.getParent()+"/"+OUTPUT_PREFIX+randomValidInputFile.getName());
        if ( !expectedOutputFile.exists()) {
            try {
                // Create this file since it does not exist
                expectedOutputFile.createNewFile();
            } catch (Exception e ) {
                fail("Could not create expected output file for testing\n"+e);
            }
        }
        expectedOutputFile.setReadOnly(); // Set this to readonly - the application should fail to write to it later
        int exitCode = runApplication(randomValidInputFile); // Run the application
        assertEquals("Exit code is not as expected", 4, exitCode); // Exit code should state it can't write the file
    }

    private Optional<File> getExpectedOutputFileFromInputOption(File inputFile) {
        File inputFileDirectory = new File(inputFile.getParent());
        String payStubCreated = OUTPUT_PREFIX+inputFile.getName(); // We expect this file will/does exist
        // For each file in the same directory as the input, look for the file that was just created for the payStubs
        Optional<File> payStubFileOptional =
                Arrays.stream(inputFileDirectory.listFiles()).filter(f -> f.getName().equals(payStubCreated)).findFirst();
        return payStubFileOptional;
    }

    private int runApplication(File inputDataFile) {
        String[] inputArgs = new String[1];
        inputArgs[0] = "-f="+inputDataFile.getAbsolutePath();
        return new PayrollApplication().run(inputArgs);
    }

    private String getValidEmployeeSalaryCsvData(){
        return new StringBuilder()
            .append(EmployeeSalaryCsvFileReader.HEADER)
            .append(System.lineSeparator())
            .append("Mark,Cunningham,59123,16,03/01/2013,16/03/2013")
            .append(System.lineSeparator())
            .append("Bruce,Wayne,88120,5,10/04/2013,06/05/2013")
            .toString();
    }

}
