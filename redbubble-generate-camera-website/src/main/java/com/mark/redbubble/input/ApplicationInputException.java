package com.mark.redbubble.input;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
class ApplicationInputException extends Exception {

    ApplicationInputException(String message) {
        super(message);
    }

    ApplicationInputException(String message, Exception exception) {
        super(message, exception);
    }
}
