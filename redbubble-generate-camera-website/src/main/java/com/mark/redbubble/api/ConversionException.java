package com.mark.redbubble.api;

/**
 * Created by Mark Cunningham on 10/22/2016.
 * <br>This exception represents problems encountered when converting from API data into Application models
 */
public class ConversionException extends ApiException {

    public ConversionException(String message, Exception exception) {
        super(message, exception);
    }

}
