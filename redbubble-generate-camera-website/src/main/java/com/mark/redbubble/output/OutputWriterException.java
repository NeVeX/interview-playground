package com.mark.redbubble.output;

/**
 * Created by Mark Cunningham on 10/22/2016.
 * <br>Class that will represent exceptions/problems during writing of data/contents
 */
public class OutputWriterException extends Exception {

    public OutputWriterException(String message) {
        super(message);
    }

    public OutputWriterException(String message, Exception exception) {
        super(message, exception);
    }
}
