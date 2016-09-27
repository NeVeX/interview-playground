package com.mark.interview.payroll.data.read;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mark Cunningham on 9/24/2016.
 * <br> This class will parse input from the application and return the parsed result in {@link ApplicationInputResult}
 */
public class ApplicationInputReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationInputReader.class);
    private static Option FILE_INPUT_OPTION;
    private static final Options ALL_INPUT_OPTIONS = new Options();

    static {
        /**
         * Define the input arguments that we expect and their properties.
         */
        FILE_INPUT_OPTION = Option.builder("f").hasArg().required()
                .desc("The file (with full location) that contains the employee salary information").build();

        ALL_INPUT_OPTIONS.addOption(FILE_INPUT_OPTION);
    }

    /**
     * Given input arguments, this method will parse and return the validated input arguments, required and optional
     * @param args - the arguments to parse
     * @return - The result of this processing
     */
    public ApplicationInputResult processInput(String[] args) {
        List<String> invalidInputs = new ArrayList<>();
        CommandLine clp;
        try {
            // Use the CLI parser to help us parse the input
            clp = new DefaultParser().parse(ALL_INPUT_OPTIONS, args);
            String payrollFile = null;
            if (clp.hasOption(FILE_INPUT_OPTION.getOpt())) {
                payrollFile = clp.getOptionValue(FILE_INPUT_OPTION.getOpt());
            }
            // Let's make sure we got a file
            if (!StringUtils.isBlank(payrollFile)) {
                return new ApplicationInputResult(payrollFile); // all good
            } else {
                invalidInputs.add(FILE_INPUT_OPTION.getOpt()); // add this missing argument to the result
            }
        } catch (MissingOptionException e) {
            LOGGER.error("The required arguments {} were missing", e.getMissingOptions());
            List<?> missingOptions = e.getMissingOptions();
            missingOptions.forEach( missingOption -> invalidInputs.add(missingOption.toString()));
        } catch (MissingArgumentException e) {
            LOGGER.error("The required argument [{}] is missing its value", e.getOption().getOpt());
            invalidInputs.add(e.getOption().getOpt());
        } catch (ParseException pe) {
            LOGGER.error("An exception occurred while parsing command line read of arguments: {}{}", Arrays.toString(args), pe);
        }
        return new ApplicationInputResult(invalidInputs);
    }

    /**
     * Helpful method that can be used to print information about the input arguments to the given stream
     * @param printStream - The stream to write the help information to
     */
    public void printUsageInformation(OutputStream printStream) {
        HelpFormatter formatter = new HelpFormatter();
        String header = System.lineSeparator()+"See below on how to use this wonderful Payroll Application";
        try (PrintWriter printWriter = new PrintWriter(printStream)) {
            formatter.printHelp(printWriter, 300, "java -jar payroll-application-*.jar", header, ALL_INPUT_OPTIONS, 0, 0, System.lineSeparator(), true);
        }
    }

}
