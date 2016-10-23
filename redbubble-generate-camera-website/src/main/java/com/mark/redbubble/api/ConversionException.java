package com.mark.redbubble.api;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class ConversionException extends Exception {

    public ConversionException(String message, Exception exception) {
        super(message, exception);
    }

}
