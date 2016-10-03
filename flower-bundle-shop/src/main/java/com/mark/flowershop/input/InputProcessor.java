package com.mark.flowershop.input;

import com.mark.flowershop.bundle.BundleCalculatedResult;
import com.mark.flowershop.product.ProductRepository;
import com.sun.org.apache.regexp.internal.RE;
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
 */
public class InputProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(InputProcessor.class);
    private final static String ORDER_FINISH = "done";
    private final static String QUIT = "quit";

    private final OrderProcessor orderProcessor = new OrderProcessor();

    public int processFromStdIn() {

        printWelcomeMessage();
        Scanner inputScanner = new Scanner(System.in);

        while ( true ) {

            int orderNumber = 1;
            List<InputOrderParsedResult> inputOrderParsedResults = new ArrayList<>();


            while ( true ) {

                printInputOrder(orderNumber++);

                // keep scanning in orders
                String newOrder = inputScanner.nextLine();

                if ( StringUtils.equalsIgnoreCase(newOrder, ORDER_FINISH)) {
                    break;
                }
                if ( StringUtils.equalsIgnoreCase(newOrder, QUIT)) {
                    return 0;
                }

                InputOrderParsedResult inputOrderParsedResult = isInputOrderValid(newOrder);
                if ( inputOrderParsedResult.isValid) {
                    inputOrderParsedResults.add(inputOrderParsedResult);
                } else {
                    LOGGER.debug("Determined input [{}] was not valid for processing because: {}", newOrder, inputOrderParsedResult.invalidReason);
                    printErrorMessage(inputOrderParsedResult.invalidReason);
                }
            }
            // If we get here, then we need to process all the orders and get results
            if ( !inputOrderParsedResults.isEmpty() ) {
                printInfoMessage("Processing your input orders");
                processNewOrders(inputOrderParsedResults);
            }
        }
    }

    private void printInputOrder(int orderNumber) {
        System.out.print("["+orderNumber+"] Enter an order: ");
        System.out.flush();
    }

    void processNewOrders(List<InputOrderParsedResult> inputOrderParsedResults) {
        List<InputOrderBundle> inputOrderBundles = new ArrayList<>();
        inputOrderBundles.addAll(
            inputOrderParsedResults.stream()
                .map( parsedResult -> new InputOrderBundle(parsedResult.orderSize, parsedResult.productCode))
                .collect(Collectors.toList())
        );
        if ( !inputOrderBundles.isEmpty()) {
            processNewOrderBundles(inputOrderBundles);
        }
    }

    void processNewOrderBundles(List<InputOrderBundle> inputOrderBundles) {
        List<InputOrderBundleResult> results = orderProcessor.processOrders(inputOrderBundles);
        if ( !results.isEmpty()) {
            printInfoMessage("Below are all the order bundle results:"+System.lineSeparator());
            // For each bundle result, print it to the screen
            results.stream().forEach(
                result -> {
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
                        orderInfo.append(" - $").append(calculatedResult.getPrice());
                        orderInfo.append(System.lineSeparator());
                        calculatedResult.getBundleAmounts().stream()
                            .forEach( bundleAmount ->
                                orderInfo
                                .append("  ")
                                .append(bundleAmount.getAmount())
                                .append(" x ")
                                .append(bundleAmount.getBundle().getSize())
                                .append(" @ $")
                                .append(bundleAmount.getBundle().getPrice())
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

    private void printInfoMessage(String message) {
        System.out.println(message);
        System.out.flush();
    }

    private void printErrorMessage(String error) {
        System.err.println("  ** Error: "+error);
        System.err.flush();
    }

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

    InputOrderParsedResult isInputOrderValid(String input) {
        if ( !StringUtils.isBlank(input)) {
            String[] inputSplit = StringUtils.split(input, " ");
            if ( inputSplit.length == 2) {

                String orderSizeString = inputSplit[0];
                boolean isNumberValid = StringUtils.isNotBlank(orderSizeString) && NumberUtils.isNumber(orderSizeString);
                if ( !isNumberValid ) { return new InputOrderParsedResult("Order size ["+orderSizeString+"] is not a number"); }

                String productCode = inputSplit[1];
                boolean isProductCodeValid = StringUtils.isNotBlank(productCode);
                if ( !isProductCodeValid) { return new InputOrderParsedResult("No product code received"); }

                boolean doesProductExist = ProductRepository.doesBundleExistForProduct(productCode);
                if ( !doesProductExist) { return new InputOrderParsedResult("No bundle exists for product ["+productCode+"]"); }

                return new InputOrderParsedResult(NumberUtils.toInt(orderSizeString), productCode);
            }
            return new InputOrderParsedResult("Input order is not in expected format - e.g. 10 T58");
        }
        return new InputOrderParsedResult("Input order is empty");
    }

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
