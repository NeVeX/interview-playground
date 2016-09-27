package com.mark.interview.payroll.data.read;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/24/2016.
 * <br>POJO that holds the data results of a given data read request
 */
public final class DataReaderResult<T> {

    private final String resourceUsed;
    private final boolean wasResourceFound;
    private final int invalidDataCount;
    private final Set<T> data;

    DataReaderResult(String resourceUsed, boolean wasResourceFound) {
        this(resourceUsed, wasResourceFound, 0, null);
    }

    DataReaderResult(String resourceUsed, boolean wasResourceFound, int invalidDataCount, Set<T> validData) {
        this.resourceUsed = resourceUsed;
        this.wasResourceFound = wasResourceFound;
        this.invalidDataCount = invalidDataCount;

        Set<T> newData = new HashSet<>();
        if ( validData != null) {
            newData.addAll(validData); // Add all the elements instead of assigning the reference - outside caller may change it
        }
        this.data = Collections.unmodifiableSet(newData);

    }

    public boolean hasData() {
        return !data.isEmpty();
    }

    public String getResourceUsed() {
        return resourceUsed;
    }

    public boolean wasResourceFound() {
        return wasResourceFound;
    }

    public int getValidDataCount() {
        return data.size();
    }

    /**
     * @return - The count of invalid/bad data encountered during the read
     */
    public int getInvalidDataCount() {
        return invalidDataCount;
    }

    public Set<T> getData() {
        return data;
    }
}
