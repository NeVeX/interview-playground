package com.mark.redbubble.html;

/**
 * Created by Mark Cunningham on 10/23/2016.
 */
public class GeneratorException extends Exception {

    GeneratorException(String message, Exception exception) {
        super(message, exception);
    }

}
