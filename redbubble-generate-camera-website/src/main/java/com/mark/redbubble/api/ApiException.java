package com.mark.redbubble.api;

import java.io.IOException;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class ApiException extends Exception {

    ApiException(String message, Exception exception) {
        super(message, exception);
    }
}
