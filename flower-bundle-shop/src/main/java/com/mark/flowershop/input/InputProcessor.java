package com.mark.flowershop.input;

import com.mark.flowershop.FlowerBundleShopApplication;
import com.mark.flowershop.product.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class InputProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(InputProcessor.class);

    private final static String ORDER_FINISH = "done";
    private final static String QUIT = "quit";

    public int processFromStdIn() {

        printWelcomeMessage();

        Scanner inputScanner = new Scanner(System.in);
//        inputScanner.useDelimiter(",");

        while ( true ) {

            List<InputOrder> inputOrders = new ArrayList<>();

            while ( true ) {

                printInputOrder();

                // keep scanning in orders
                String newOrder = inputScanner.nextLine();

                if ( StringUtils.equalsIgnoreCase(newOrder, ORDER_FINISH)) {
                    break;
                }
                if ( StringUtils.equalsIgnoreCase(newOrder, QUIT)) {
                    return 0;
                }

                InputOrder inputOrder = isInputOrderValid(newOrder);
                if ( inputOrder.isValid) {
                    inputOrders.add(inputOrder);
                } else {
                    LOGGER.debug("Determined input [{}] was not valid for processing because: {}", newOrder, inputOrder.invalidReason);
                    printErrorMessage(inputOrder.invalidReason);
                }
            }
            // If we get here, then we need to process all the orders and get results
            if ( !inputOrders.isEmpty() ) {
                processNewOrders(inputOrders);
            }
        }
    }

    private void printInputOrder() {
        System.out.print("Enter an order: ");
    }

    private void processNewOrders(List<InputOrder> inputOrders) {
        for( InputOrder order : inputOrders) {

        }
    }

    private void printErrorMessage(String error) {
        System.err.println("**Error: "+error);
    }

    private InputOrder isInputOrderValid(String input) {
        if ( !StringUtils.isBlank(input)) {
            String[] inputSplit = StringUtils.split(input, " ");
            if ( inputSplit.length == 2) {

                String orderSizeString = inputSplit[0];
                boolean isNumberValid = StringUtils.isNotBlank(orderSizeString) && NumberUtils.isNumber(orderSizeString);
                if ( !isNumberValid ) { return new InputOrder("Order size ["+orderSizeString+"] is not a number"); }

                String productCode = inputSplit[1];
                boolean isProductCodeValid = StringUtils.isNotBlank(productCode);
                if ( !isProductCodeValid) { return new InputOrder("No product code received"); }

                boolean doesProductExist = ProductRepository.doesBundleExistForProduct(productCode);
                if ( !doesProductExist) { return new InputOrder("No bundle exists for product ["+productCode+"]"); }

                return new InputOrder(NumberUtils.toInt(orderSizeString), productCode);
            }
            return new InputOrder("Input order is not in expected format - e.g. 10 T58");
        }
        return new InputOrder("Input order is empty");
    }

    private void printWelcomeMessage() {
        System.out.print("");
        System.out.println("Welcome to the Flower Bundle Shop Application");
    }

    private static class InputOrder {
        private boolean isValid;
        private String invalidReason;
        private int orderSize;
        private String productCode;

        public InputOrder(int orderSize, String productCode) {
            this.isValid = true;
            this.orderSize = orderSize;
            this.productCode = productCode;
        }

        public InputOrder(String invalidReason) {
            this.isValid = false;
            this.invalidReason = invalidReason;
        }

    }

}
