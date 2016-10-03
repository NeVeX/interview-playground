package com.mark.flowershop.product;

import com.mark.flowershop.bundle.BundleOptions;
import com.mark.flowershop.data.read.FlowerBundleReader;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public final class ProductRepository {

    private final static String FLOWER_BUNDLE_RESOURCE = "/products/flowers/flower_bundles.json";
    private final static Map<String, Product> PRODUCT_MAP = new HashMap<>();
    private final static Map<String, BundleOptions> PRODUCT_BUNDLE_MAP = new HashMap<>();

    static {
        List<FlowerBundle> flowerBundles = new FlowerBundleReader().readResource(FLOWER_BUNDLE_RESOURCE);
        if ( flowerBundles != null && !flowerBundles.isEmpty()) {
            flowerBundles.stream()
                .forEach(flowerBundle -> {
                    // Make the product code lower case for the hash function
                    String productCode = flowerBundle.getFlower().getCode().toLowerCase();
                    productCode = productCode.toLowerCase();
                    PRODUCT_MAP.put(productCode, flowerBundle.getFlower());
                    PRODUCT_BUNDLE_MAP.put(productCode, flowerBundle.getBundleOptions());
                });
        }
    }

    public static boolean doesProductExist(String code) {
        if (!StringUtils.isBlank(code)) {
            return PRODUCT_MAP.containsKey(code.toLowerCase());
        }
        return false;
    }

    public static boolean doesBundleExistForProduct(String code) {
        if (!StringUtils.isBlank(code)) {
            return doesProductExist(code) && PRODUCT_BUNDLE_MAP.containsKey(code.toLowerCase());
        }
        return false;
    }
}
