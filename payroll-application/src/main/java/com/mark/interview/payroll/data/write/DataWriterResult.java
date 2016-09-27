package com.mark.interview.payroll.data.write;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mark Cunningham on 9/25/2016.
 * <br>Simple POJO class to contain the result and information of a data write
 */
public class DataWriterResult {

    private boolean isSuccessfullyWritten;
    private String resourceUsed;
    private long writtenDataCount;

    DataWriterResult(boolean isSuccessfullyWritten, String resourceUsed, long writtenDataCount) {
        if (StringUtils.isBlank(resourceUsed)) { throw new IllegalArgumentException("Provided resourceUsed cannot be blank"); }
        this.isSuccessfullyWritten = isSuccessfullyWritten;
        this.resourceUsed = resourceUsed;
        this.writtenDataCount = writtenDataCount;
    }

    public boolean isSuccessfullyWritten() {
        return isSuccessfullyWritten;
    }

    /**
     * @return - The resource that was used for the write
     */
    public String getResourceUsed() {
        return resourceUsed;
    }

    /**
     * @return - how many data records were written
     */
    public long getWrittenDataCount() {
        return writtenDataCount;
    }

    @Override
    public String toString() {
        return "DataWriterResult{" +
                "isSuccessfullyWritten=" + isSuccessfullyWritten +
                ", resourceUsed='" + resourceUsed + '\'' +
                ", dataWriteCount=" + writtenDataCount +
                '}';
    }
}
