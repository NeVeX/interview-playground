package com.mark.interview.payroll.data.write;

import com.mark.interview.payroll.util.DateUtils;
import com.mark.interview.payroll.model.EmployeePayStub;

/**
 * Created by Mark Cunningham on 9/25/2016.
 * <br>This class extends the {@link CsvFileWriter} to gain methods to help with writing the employee pay stubs
 * to a file
 */
public class EmployeePayStubCsvFileWriter extends CsvFileWriter<EmployeePayStub> {

    final static String HEADER = "name,pay period,gross income,income tax,net income,super";

    @Override // Given the paystub, create the string to output to the csv file
    protected String convertToCsvString(EmployeePayStub payStub) {
        String name = payStub.getEmployee().getFullName();
        String payPeriod = DateUtils.formatDate(payStub.getStartDate())
                        + " - " + DateUtils.formatDate(payStub.getEndDate());

        return name + COMMA + payPeriod + COMMA + payStub.getGrossIncome() + COMMA
                + payStub.getIncomeTax() + COMMA + payStub.getNetIncome() + COMMA + payStub.getGrossSuper();

    }

    @Override
    protected String getCsvHeader() {
        return HEADER;
    }
}
