package com.mark.flowershop.input;

import com.mark.flowershop.bundle.BundleCalculatedResult;
import com.mark.flowershop.product.ProductRepository;
import com.mark.flowershop.util.CurrencyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>This class provides methods to parse input and handle orders from users.
 */
public class InputProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(InputProcessor.class);
    private final static String ORDER_FINISH = "done";
    private final static String QUIT = "quit";

    private final OrderBundleProcessor orderBundleProcessor = new OrderBundleProcessor();

    /**
     * Invoking this method will cause blocking on this thread, since this method will wait on user
     * commands on how to proceed. Until the user quits the terminal, this method will not return.
     * <br>All input is parsed and if valid orders, the orders are processed and shown on screen.
     * @return - True/False if everything went ok (True is a normal exit)
     */
    public boolean processFromStdIn() {

        printWelcomeMessage(); // Say hello with some helpful tips
        Scanner inputScanner = new Scanner(System.in); // Scan the standard in

        while ( true ) { // Keep looping until the user quits
            int orderNumber = 1;
            List<InputOrderParsedResult> inputOrderParsedResults = new ArrayList<>();

            while ( true ) { // Keep looping over each order until the user quits or hits "done"
                printInputOrder(orderNumber++); // print out the next order input line
                String newOrder = inputScanner.nextLine(); // This blocks waiting for user input (new line/enter key)

                if ( StringUtils.equalsIgnoreCase(newOrder, ORDER_FINISH)) {
                    break; // Process the orders we have so far
                }
                if ( StringUtils.equalsIgnoreCase(newOrder, QUIT)) {
                    return true; // User is done, so return from this method
                }

                // Check if the given input is good
                InputOrderParsedResult inputOrderParsedResult = isInputOrderValid(newOrder);
                if ( inputOrderParsedResult.isValid) {
                    inputOrderParsedResults.add(inputOrderParsedResult); // Add the order to the list to be parsed later
                } else {
                    LOGGER.debug("Determined input [{}] was not valid for processing because: {}", newOrder, inputOrderParsedResult.invalidReason);
                    // Inform the user of the reason the input was rejected
                    printErrorMessage(inputOrderParsedResult.invalidReason);
                }
            }
            // If we get here, then we need to process all the orders and get results
            if ( !inputOrderParsedResults.isEmpty() ) {
                printInfoMessage("Processing your input orders");
                processNewOrders(inputOrderParsedResults);
            } else {
                printInfoMessage("No orders were input - please enter orders and then enter ["+ORDER_FINISH+"] when finished");
                printInfoMessage("");
            }
        }
    }

    /**
     * Given a list of user input orders (that are valid), this method will process each one
     */
    private void processNewOrders(List<InputOrderParsedResult> inputOrderParsedResults) {
        List<InputOrderBundle> inputOrderBundles = new ArrayList<>();
        inputOrderBundles.addAll(
            inputOrderParsedResults.stream()
                .map( parsedResult -> new InputOrderBundle(parsedResult.orderSize, parsedResult.productCode))
                .collect(Collectors.toList())
        );
        if ( !inputOrderBundles.isEmpty()) {
            List<InputOrderBundleResult> results = orderBundleProcessor.processOrders(inputOrderBundles);
            onOrderResultsCalculated(results);
        }
    }

    /**
     * This method is invoked when the input orders have been calculated.
     * <br>The method will look at each result and print on screen the various information to the user,
     * that can include invalid orders, un-satisfied orders and bundle prices
     * @param results - The calculated results
     */
    private void onOrderResultsCalculated(List<InputOrderBundleResult> results) {
        if ( !results.isEmpty()) {
            printInfoMessage("Below are all the order bundle results:"+System.lineSeparator());
            // For each bundle result, print it to the screen
            results.forEach(
                result -> {
                    // Start by creating the info String we'll use for the user screen output
                    StringBuilder orderInfo = new StringBuilder();
                    orderInfo.append(result.getInputOrderBundle().getOrderSize());
                    orderInfo.append(" ").append(result.getInputOrderBundle().getProductCode());

                    if ( !result.isValidOrder()) {
                        orderInfo.append(System.lineSeparator());
                        orderInfo.append("  ** This order was not valid **");
                    } else if ( !result.doesBundleExistForOrder()) {
                        orderInfo.append(System.lineSeparator());
                        orderInfo.append("  ** No bundle exists for this order **");
                    } else {
                        BundleCalculatedResult calculatedResult = result.getResult();
                        orderInfo.append(" - ").append(CurrencyUtils.convertToDefaultCurrency(calculatedResult.getTotalPrice()));
                        orderInfo.append(System.lineSeparator());
                        calculatedResult.getBundleAmounts()
                            .forEach( bundleAmount ->
                                orderInfo
                                .append("  ")
                                .append(bundleAmount.getAmount())
                                .append(" x ")
                                .append(bundleAmount.getBundle().getSize())
                                .append(" @ ")
                                .append(CurrencyUtils.convertToDefaultCurrency(bundleAmount.getBundle().getPrice()))
                                .append(" ea")
                            );
                    }
                    orderInfo.append(System.lineSeparator());
                    printInfoMessage(orderInfo.toString());
                }
            );
        } else {
            printInfoMessage("No suitable bundles could be found for any orders");
        }
    }

    /**
     * Helper method to print a info message to screen
     */
    private void printInfoMessage(String message) {
        System.out.println(message);
        System.out.flush();
    }

    /**
     * Helper method to print a error message to screen - An error string is prefixed to the given error message
     */
    private void printErrorMessage(String error) {
        System.err.println("  ** Error: "+error);
        System.err.flush();
    }

    /**
     * Helper method to print a request for input (leaving the cursor on the same line)
     */
    private void printInputOrder(int orderNumber) {
        System.out.print("["+orderNumber+"] Enter an order: ");
        System.out.flush();
    }

    /**
     * Helper method to print helpful text about the application and how to use it
     */
    private void printWelcomeMessage() {
        System.out.println("");
        System.out.println("Welcome to the Flower Bundle Shop Application!");
        System.out.println("");
        System.out.println("Below you will be able to input orders in the form: 'X ABC' - where 'X' is the order amount and 'ABC' is the product code.");
        System.out.println("You can input multiple orders by using the enter key when each order line is completed.");
        System.out.println("Once you are done with a particular order, enter 'done' to calculate all the order bundles.");
        System.out.println("");
        System.out.println("To exit the application, enter 'quit'.");
        System.out.println();
        System.out.flush();
    }

    /**
     * Given an input String from the user, this method will disect it and return the result - indicating if the input
     * is valid or not (and what the values it parsed out were)
     * @param input - the user input string
     * @return - The result of the parse
     */
    InputOrderParsedResult isInputOrderValid(String input) {
        if ( !StringUtils.isBlank(input)) {
            String[] inputSplit = StringUtils.split(input, " ");
            if ( inputSplit.length == 2) { // We expect only 2 inputs
                String orderSizeString = inputSplit[0];
                // Make sure the first string is a valid number
                boolean isNumberValid = StringUtils.isNotBlank(orderSizeString) && NumberUtils.isNumber(orderSizeString);
                if ( !isNumberValid ) { return new InputOrderParsedResult("Order size ["+orderSizeString+"] is not a number"); }

                // Make sure we get a product code
                String productCode = inputSplit[1];
                boolean isProductCodeValid = StringUtils.isNotBlank(productCode);
                if ( !isProductCodeValid) { return new InputOrderParsedResult("No product code received"); }

                // Make sure the product code exists
                boolean doesProductExist = ProductRepository.doesBundleExistForProduct(productCode);
                if ( !doesProductExist) { return new InputOrderParsedResult("No bundle exists for product ["+productCode+"]"); }

                return new InputOrderParsedResult(NumberUtils.toInt(orderSizeString), productCode);
            }
            return new InputOrderParsedResult("Input order is not in expected format - e.g. 10 T58");
        }
        return new InputOrderParsedResult("Input order is empty");
    }

    /**
     * Inner class to hold parsed values and the result of a user input parse
     */
    static class InputOrderParsedResult {
        boolean isValid;
        String invalidReason;
        int orderSize;
        String productCode;

        InputOrderParsedResult(int orderSize, String productCode) {
            this.isValid = true;
            this.orderSize = orderSize;
            this.productCode = productCode;
        }

        InputOrderParsedResult(String invalidReason) {
            this.isValid = false;
            this.invalidReason = invalidReason;
        }

    }

}
