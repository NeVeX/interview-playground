package com.mark.interview.payroll;

import com.mark.interview.payroll.data.read.*;
import com.mark.interview.payroll.data.write.DataWriterResult;
import com.mark.interview.payroll.data.write.EmployeePayStubCsvFileWriter;
import com.mark.interview.payroll.model.EmployeePayStub;
import com.mark.interview.payroll.model.EmployeeSalary;
import com.mark.interview.payroll.model.TaxYearInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintStream;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/25/2016.
 * <br>This is the engine of the application. There is a static main function that starts the application by
 * simply invoking the {{@link #run(String[])}} method on a new {@link PayrollApplication} instance.
 * <br>This class will act as the workflow processor, getting inputs and handing off output to other
 * various processes.
 */
public class PayrollApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollApplication.class);
    /**
     * A collection of various exit codes that will be used for the system exit
     */
    private static final int EXIT_CODE_OK = 0;
    private static final int EXIT_CODE_INVALID_INPUT = 1;
    private static final int EXIT_CODE_INPUT_DATA_LOAD_FAILURE = 2;
    private static final int EXIT_CODE_NO_PAY_STUBS_GENERATED = 3;
    private static final int EXIT_CODE_UNABLE_TO_WRITE_FILE = 4;

    /**
     * Run the application - note, this can become an expensive operation on huge data sets via the passed in arguments
     * @param args - the various arguments to configure the application - see {@link ApplicationInputReader}
     * @return - The exit code (final status) of the application
     */
    int run(String[] args) {
        int exitCode;
        // Get an instance of the input reader and process the args
        ApplicationInputReader applicationInputReader = new ApplicationInputReader();
        ApplicationInputResult applicationInputResult = applicationInputReader.processInput(args);

        if ( applicationInputResult.isValidInput() ) {
            // Inputs look good, so pass the flow onto the next stage
            exitCode = onValidApplicationInput(applicationInputResult.getPayrollFile());
        } else {
            printErrorToScreen("The given input parameters are not valid");
            exitCode = EXIT_CODE_INVALID_INPUT;
        }
        // Check to see what status we'll be exiting with
        if ( exitCode != EXIT_CODE_OK) {
            applicationInputReader.printUsageInformation(System.err); // Something went wrong, so print the help information
            printInfoToScreen("For more information on the problems encountered, please look at the log file");
        } else {
            printInfoToScreen(System.lineSeparator()+"Thanks for using this payroll application!");
            printInfoToScreen("Goodbye!");
        }
        return exitCode;
    }

    /**
     * This method is invoked when valid application input has being received and the next step in the workflow should proceed
     * @param inputPayrollFile - The valid (non blank) payroll file to read input from
     * @return The final status (exit code)
     */
    private int onValidApplicationInput(String inputPayrollFile) {
        DataReaderResult<EmployeeSalary> inputPayrollData = getPayrollInputDataFromCsv(inputPayrollFile);
        // Print out the statistics on this data load
        printInfoToScreen("Found ["+inputPayrollData.getValidDataCount()+"] valid and " +
                "["+inputPayrollData.getInvalidDataCount()+"] invalid employee salary records in resource ["+inputPayrollFile+"]");
        if ( inputPayrollData.hasData()) {
            // Great, we have payroll data from the input file, so let's determine the place we wish to save data
            String outputFile = getOutputFileToWriteTo(inputPayrollFile);
            return onInputEmployeeSalariesLoaded(outputFile, inputPayrollData.getData());
        } else {
            printErrorToScreen("Was not able to load input data from: ["+inputPayrollData.getResourceUsed()+"]");
            return EXIT_CODE_INPUT_DATA_LOAD_FAILURE;
        }
    }

    /**
     * This method is invoked when input has being loaded into the data sets. This method will use the input
     * to generate paystubs and if successful, invoke the next workflow step (writing them to disk)
     * @param outputFile - The file to use for saving generated paystubs too
     * @param inputPayrollData - The set of data read in
     * @return - The exit code (final status) of this workflow step
     */
    private int onInputEmployeeSalariesLoaded(String outputFile, Set<EmployeeSalary> inputPayrollData) {
        PayStubCalculator payStubCalculator = getPayStubCalculator();
        printInfoToScreen("The supported tax years of this application are: "+ payStubCalculator.getSupportedTaxYears());
        Set<EmployeePayStub> calculatedPayStubs = payStubCalculator.calculate(inputPayrollData);
        printInfoToScreen("Calculated a total of ["+calculatedPayStubs.size()+"] pay-stubs");
        if ( !calculatedPayStubs.isEmpty()) {
            return onPayStubsCalculated(outputFile, calculatedPayStubs);
        } else {
            return EXIT_CODE_NO_PAY_STUBS_GENERATED;
        }
    }

    /**
     * The final step in the workflow - we have the calculated paystubs and the location to save them too, so let's write data
     * @param outputFile - The file to write the paystubs too
     * @param calculatedPayStubs - The calculated paystubs
     * @return - The final status of the workflow
     */
    private int onPayStubsCalculated(String outputFile, Set<EmployeePayStub> calculatedPayStubs) {
        DataWriterResult payStubsWriterResult = writePayStubsToCsvFile(outputFile, calculatedPayStubs);
        if ( payStubsWriterResult.isSuccessfullyWritten()) { // Check if the file was written
            printInfoToScreen(System.lineSeparator()+"Successfully wrote all calculated pay-stub information to the file: ["+payStubsWriterResult.getResourceUsed()+"]");
            return EXIT_CODE_OK;
        } else {
            printErrorToScreen("Unable to write pay-stubs to resource: ["+payStubsWriterResult.getResourceUsed()+"]");
            return EXIT_CODE_UNABLE_TO_WRITE_FILE;
        }
    }

    /**
     * @param inputPayrollFile - Given a input pay salary file, this will attempt to parse all data within
     * @return - The employee salary data extracted (can be empty)
     */
    private DataReaderResult<EmployeeSalary> getPayrollInputDataFromCsv(String inputPayrollFile) {
        return new EmployeeSalaryCsvFileReader().readFile(inputPayrollFile);
    }

    /**
     * @return - Gets a new paystub calculator to use, populated with the supported tax years
     */
    private PayStubCalculator getPayStubCalculator() {
        Set<TaxYearInformation> taxYearInfoSet = new TaxInformationJsonFileReader().getAll();
        return new PayStubCalculator(taxYearInfoSet);
    }

    /**
     * @param outputFile - The output file to write all paystubs to
     * @param payStubs - The paystub data
     * @return - The result of the write
     */
    private DataWriterResult writePayStubsToCsvFile(String outputFile, Set<EmployeePayStub> payStubs) {
        return new EmployeePayStubCsvFileWriter().writeToFile(outputFile, payStubs);
    }

    /**
     * Given the input salary file location, determine the output file to use for writing
     * @param inputPayrollFile - the input salary file
     * @return - The output file to write pay stubs to
     */
    private String getOutputFileToWriteTo(String inputPayrollFile) {
        File inputFileHandle = new File(inputPayrollFile);
        String inputFilePath = inputFileHandle.getParent();
        String outputFileName = "output-paystubs-" + inputFileHandle.getName();
        return inputFilePath + File.separator + outputFileName;
    }

    private void printErrorToScreen(String message) {
        print(System.err, System.lineSeparator()+"**** Error: "+message);
        LOGGER.error("Error message displayed: {}", message);
    }

    private void printInfoToScreen(String message) {
        print(System.out, message);
        LOGGER.debug(message);
    }

    private void print(PrintStream ps, String message) {
        ps.println(message);
    }

    /**
     * The main entry point that kicks off the application
     * @param args - the passed in arguments (from the command line)
     */
    public static void main(String[] args) {
        LOGGER.info("Payroll Application started");
        int exitCode = new PayrollApplication().run(args);
        LOGGER.info("Payroll Application exited with code [{}]", exitCode);
        System.exit(exitCode);

    }

}
