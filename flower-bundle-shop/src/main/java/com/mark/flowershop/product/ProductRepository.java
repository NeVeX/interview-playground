package com.mark.flowershop.product;

import com.mark.flowershop.bundle.BundleOptions;
import com.mark.flowershop.data.read.FlowerBundleReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public final class ProductRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductRepository.class);

    public static void initialize() {
        Holder.initialize();
    }

    public static boolean doesProductExist(String code) {
        if (!StringUtils.isBlank(code)) {
            return Holder.PRODUCT_MAP.containsKey(code.toLowerCase());
        }
        return false;
    }

    public static boolean doesBundleExistForProduct(String code) {
        if (!StringUtils.isBlank(code)) {
            return doesProductExist(code) && Holder.PRODUCT_BUNDLE_MAP.containsKey(code.toLowerCase());
        }
        return false;
    }

    private static class Holder {
        private final static Map<String, Product> PRODUCT_MAP = new HashMap<>();
        private final static Map<String, BundleOptions> PRODUCT_BUNDLE_MAP = new HashMap<>();

        static {
            List<FlowerBundle> flowerBundles = FlowerBundleReader.getDefault();
            if ( flowerBundles != null && !flowerBundles.isEmpty()) {
                flowerBundles.stream()
                        .forEach(flowerBundle -> {
                            // Make the product code lower case for the hash function
                            String productCode = flowerBundle.getFlower().getCode().toLowerCase();
                            productCode = productCode.toLowerCase();
                            PRODUCT_MAP.put(productCode, flowerBundle.getFlower());
                            PRODUCT_BUNDLE_MAP.put(productCode, flowerBundle.getBundleOptions());
                        });
                LOGGER.info("Loaded [{}] products into the application", PRODUCT_MAP.size());
                LOGGER.info("Loaded [{}] flower bundles into the application", PRODUCT_BUNDLE_MAP.size());
            } else {
                // This is bad, if we can't load the flower bundles, then we are in a bad shape, so throw a runtime exception
                LOGGER.error("Could not load the flower bundles - the application will not start");
                throw new IllegalStateException("No flower bundles were loaded by the application");
            }
        }

        static void initialize() {

        }

    }

}
