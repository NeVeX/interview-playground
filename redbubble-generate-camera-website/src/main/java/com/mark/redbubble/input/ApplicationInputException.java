package com.mark.redbubble.input;

import org.apache.commons.cli.ParseException;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class ApplicationInputException extends Exception {


    public ApplicationInputException(String message) {
        super(message);
    }

    public ApplicationInputException(String message, Exception exception) {
        super(message, exception);
    }
}
