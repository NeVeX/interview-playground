package com.mark.redbubble.api;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class ApiException extends Exception {

    ApiException(String message, Exception exception) {
        super(message, exception);
    }

    ApiException(String message) {
        super(message);
    }

    ApiException(Exception exception) {
        super(exception);
    }
}
