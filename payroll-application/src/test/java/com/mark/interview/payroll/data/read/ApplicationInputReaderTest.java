package com.mark.interview.payroll.data.read;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class ApplicationInputReaderTest {

    @Test
    public void assertValidInputs() {
        ApplicationInputReader reader = new ApplicationInputReader();
        String inputFileName = "some_file";
        ApplicationInputResult result = reader.processInput(createValidArgs(inputFileName));
        assertTrue("Expected input given to be valid", result.isValidInput());
        assertEquals("Extracted file name is not as expected from given input", inputFileName, result.getPayrollFile());
        assertTrue("Did not expect any invalid arguments in result", result.getInvalidInputs().isEmpty());
    }

    @Test
    public void assertMissingInputs() {
        ApplicationInputReader reader = new ApplicationInputReader();
        ApplicationInputResult result = reader.processInput(new String[0]); // Nothing to process
        validateMissingFileInputs(result);
    }

    @Test
    public void assertInvalidFileInput() {
        ApplicationInputReader reader = new ApplicationInputReader();
        ApplicationInputResult result = reader.processInput(createValidArgs("")); // No file input value
        validateMissingFileInputs(result);
    }

    @Test
    public void assertNullInputArgs() {
        ApplicationInputReader reader = new ApplicationInputReader();
        ApplicationInputResult result = reader.processInput(null); // Null input - it should not cause problems
        validateMissingFileInputs(result);
    }

    private void validateMissingFileInputs(ApplicationInputResult result) {
        assertFalse("Expected invalid input result", result.isValidInput());
        assertFalse("Expected invalid input list to not be empty", result.getInvalidInputs().isEmpty());
        // We only expect one invalid argument, so get the first in the list
        assertEquals("Expected invalid argument file in invalid list result", "f", result.getInvalidInputs().get(0));
    }

    private String[] createValidArgs(String fileInput) {
        String[] args = new String[1];
        args[0] = "-f="+fileInput;
        return args;
    }

}
