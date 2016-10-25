package com.mark.redbubble.input;

/**
 * Created by Mark Cunningham on 10/22/2016.
 * <br>Exception class that represents a problem with reading user inputs
 */
public class ApplicationInputException extends Exception {

    ApplicationInputException(String message) {
        super(message);
    }

    ApplicationInputException(String message, Exception exception) {
        super(message, exception);
    }
}
