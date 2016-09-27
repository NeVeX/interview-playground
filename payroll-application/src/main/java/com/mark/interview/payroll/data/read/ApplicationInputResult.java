package com.mark.interview.payroll.data.read;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by NeVeX on 9/24/2016.
 */
public class ApplicationInputResult {

    private final boolean validInput;
    private final List<String> invalidInputs;
    private final String payrollFile;

    ApplicationInputResult(String payrollFile) {
        if (StringUtils.isBlank(payrollFile)) { throw new IllegalArgumentException("Provided payrollFile cannot be blank"); }
        this.validInput = true;
        this.payrollFile = payrollFile;
        this.invalidInputs = new ArrayList<>();
    }

    ApplicationInputResult(List<String> invalidInputs) {
        List<String> mutableInvalidInputs = new ArrayList<>();
        if ( invalidInputs != null) {
            mutableInvalidInputs.addAll(invalidInputs);
        }
        this.invalidInputs = Collections.unmodifiableList(mutableInvalidInputs);
        this.payrollFile = null;
        this.validInput = false;
    }

    public boolean isValidInput() {
        return validInput;
    }

    public List<String> getInvalidInputs() {
        return invalidInputs;
    }

    public String getPayrollFile() {
        return payrollFile;
    }
}
