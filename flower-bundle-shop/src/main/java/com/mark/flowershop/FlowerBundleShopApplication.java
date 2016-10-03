package com.mark.flowershop;

import com.mark.flowershop.input.InputProcessor;
import com.mark.flowershop.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class FlowerBundleShopApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger(FlowerBundleShopApplication.class);

    public static void main(String[] args) {
        LOGGER.info("The application has started. Arguments received: {}", Arrays.toString(args));
        ProductRepository.initialize();
        int exitCode = new InputProcessor().processFromStdIn();
        LOGGER.info("The application is exiting with exit code [{}]", exitCode);
        System.exit(exitCode);
    }


}
