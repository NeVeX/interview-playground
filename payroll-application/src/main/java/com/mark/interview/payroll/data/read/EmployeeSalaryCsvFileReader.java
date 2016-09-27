package com.mark.interview.payroll.data.read;

import com.mark.interview.payroll.PayrollApplication;
import com.mark.interview.payroll.util.DateUtils;
import com.mark.interview.payroll.model.Employee;
import com.mark.interview.payroll.model.EmployeeSalary;
import com.mark.interview.payroll.model.Salary;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Created by Mark Cunningham on 9/24/2016.
 * <br> This class extends the {@link CsvFileReader} for it's helpful methods.
 * <br> Each line in the CSV is parsed and returned to the parent
 */
public class EmployeeSalaryCsvFileReader extends CsvFileReader<EmployeeSalary> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollApplication.class);
    public final static String HEADER = "first name,last name,annual salary,super rate (%),payment start date,payment end date";

    @Override
    protected EmployeeSalary extractFromCsvLine(Scanner lineScanner) {
        EmployeeSalary employeeSalary = null;

        while (lineScanner.hasNext()) {
            String firstName = null, lastName = null;
            BigDecimal annualSalary = null, superRate = null;
            LocalDate paymentStartDate = null, paymentEndDate = null;

            /**
             * We expect each token to line up with the expected columns (as per the {{@link #isCsvHeaderValid(Scanner)}} check
             */
            if (lineScanner.hasNext()) {
                firstName = lineScanner.next();
            }
            if (lineScanner.hasNext()) {
                lastName = lineScanner.next();
            }
            if (lineScanner.hasNextBigDecimal()) {
                annualSalary = lineScanner.nextBigDecimal();
            }
            if (lineScanner.hasNextBigDecimal()) {
                superRate = lineScanner.nextBigDecimal();
            }
            if (lineScanner.hasNext()) {
                paymentStartDate = DateUtils.tryParseDateFromString(lineScanner.next());
            }
            if (lineScanner.hasNext()) {
                paymentEndDate = DateUtils.tryParseDateFromString(lineScanner.next());
            }

            employeeSalary = tryBuildEmployeeSalary(firstName, lastName, annualSalary, superRate, paymentStartDate, paymentEndDate);
        }
        return employeeSalary;
    }

    @Override // Make sure header line equals the expected header
    protected boolean isCsvHeaderValid(Scanner headerScanner) {
        if ( headerScanner.hasNextLine()) {
            String inputHeader = StringUtils.remove(headerScanner.nextLine(), " ");
            String expectedHeader = StringUtils.remove(HEADER, " ");
            return StringUtils.equalsIgnoreCase(inputHeader, expectedHeader);
        }
        return false;
    }

    /** Helper method to take the CSV parsed data and to create a new instance of the Salary **/
    private EmployeeSalary tryBuildEmployeeSalary(String firstName, String lastName, BigDecimal annualSalary,
                                                  BigDecimal superRate, LocalDate paymentStartDate, LocalDate paymentEndDate) {
        // At this point, we have what we need, or we don't, so try and build the object with what we have
        Employee employee = null;
        if ( !StringUtils.isBlank(firstName) && !StringUtils.isBlank(lastName)) {
            employee = new Employee(firstName, lastName);
        }
        // The salary instance is more complicated (with validations) but offers a builder, so let's use that
        Salary salary = null;
        try {
            salary = Salary.builder()
                    .withAnnualSalary(annualSalary)
                    .withAnnualSuperRate(superRate)
                    .withStartDate(paymentStartDate)
                    .withEndDate(paymentEndDate)
                    .build();
        } catch (Exception e ) {
            // This data must be corrupt, so log a message of the problem and avoid this bad data
            LOGGER.warn("Could not create Salary instance - Reason: {}", e.getMessage());
        }
        if ( employee != null && salary != null) {
            return new EmployeeSalary(employee, salary); // All good
        }
        return null;
    }

}
