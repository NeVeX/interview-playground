package com.mark.phoneword.util;

import java.util.logging.Logger;

/**
 * Created by Mark Cunningham on 9/30/2016.
 * <br>Utility class for helpful output functions
 */
public class OutputUtils {
    private final static Logger LOGGER = Logger.getLogger(OutputUtils.class.getName());

    /**
     * Prints the message to the screen with no modifications
     */
    public static void printInfo(String message) {
        System.out.println(message);
    }

    /**
     * Prints the message to the screen with ERROR prefixed
     */
    public static void printError(String message) {
        System.err.println("***ERROR: "+message);
        LOGGER.severe(message);
    }

    /**
     * Prints the message to the screen WARNING prefixed
     */
    public static void printWarning(String message) {
        System.err.println("*Warning: "+message);
        LOGGER.warning(message);
    }

    /**
     * Prints the message to the screen and puts cursor after message
     */
    public static void printInput(String message) {
        System.out.print(message);
    }

}
