package com.mark.flowershop;

import com.mark.flowershop.input.InputProcessor;
import com.mark.flowershop.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>This is the entry point to starting the Flower Shop application
 */
public class FlowerBundleShopApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger(FlowerBundleShopApplication.class);

    /**
     * JVM entry point to start the application
     */
    public static void main(String[] args) {
        LOGGER.info("The application has started. Arguments received: {}", Arrays.toString(args));
        // Before staring the application, initialize the data store
        ProductRepository.initialize();
        boolean isSuccessful = new InputProcessor().processFromStdIn(); // Process user inputs
        int exitCode = isSuccessful ? 0 : 1; // Set an exit code for the JVM
        LOGGER.info("The application is exiting with exit code [{}]", exitCode);
        System.exit(exitCode);
    }

}
