package com.mark.redbubble.output;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class FileWriterException extends Exception {

    public FileWriterException(String message) {
        super(message);
    }

    public FileWriterException(String message, Exception exception) {
        super(message, exception);
    }
}
