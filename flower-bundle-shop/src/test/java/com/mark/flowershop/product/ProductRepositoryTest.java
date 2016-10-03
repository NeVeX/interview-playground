package com.mark.flowershop.product;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class ProductRepositoryTest {

    @Test
    public void assertProductRepositoryLoadsProducts() {

        String productCode = "T58";
        assertProductAndProductBundleExists(productCode);

        productCode = "L09";
        assertProductAndProductBundleExists(productCode);

        productCode = "R12";
        assertProductAndProductBundleExists(productCode);

    }

    private void assertProductAndProductBundleExists(String productCode) {
        String upperCaseProductCode = productCode.toUpperCase();
        assertThat(ProductRepository.doesProductExist(upperCaseProductCode))
                .as("Expected product code ["+upperCaseProductCode+"] to exist").isTrue();
        assertThat(ProductRepository.doesBundleExistForProduct(upperCaseProductCode))
                .as("Expected product code ["+upperCaseProductCode+"] to exist").isTrue();

        String lowerCaseProductCode = productCode.toLowerCase();
        assertThat(ProductRepository.doesProductExist(lowerCaseProductCode))
                .as("Expected product code ["+lowerCaseProductCode+"] to exist").isTrue();
        assertThat(ProductRepository.doesBundleExistForProduct(lowerCaseProductCode))
                .as("Expected product code ["+lowerCaseProductCode+"] to exist").isTrue();

    }

}
