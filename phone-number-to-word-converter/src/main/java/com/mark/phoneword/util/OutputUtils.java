package com.mark.phoneword.util;

/**
 * Created by Mark Cunningham on 9/30/2016.
 */
public class OutputUtils {


    public static void printInfo(String message) {
        System.out.println(message);
    }

    public static void printError(String message) {
        System.err.println("***ERROR: "+message);
    }


    public static void printInput(String message) {
        System.out.print(message);
    }

}
