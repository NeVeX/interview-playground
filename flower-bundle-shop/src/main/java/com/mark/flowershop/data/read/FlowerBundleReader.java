package com.mark.flowershop.data.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mark.flowershop.bundle.Bundle;
import com.mark.flowershop.bundle.BundleOptions;
import com.mark.flowershop.product.Flower;
import com.mark.flowershop.product.FlowerBundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public final class FlowerBundleReader extends ResourceReader<List<FlowerBundle>> {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected List<FlowerBundle> process(BufferedReader bufferedReader) throws IOException {
        List<FlowerBundleJson> flowerBundleJsons = OBJECT_MAPPER.readValue(bufferedReader, new TypeReference<List<FlowerBundleJson>>(){});
        if ( flowerBundleJsons != null && !flowerBundleJsons.isEmpty()) {
            try {
                return convertJson(flowerBundleJsons);
            } catch (Exception e) {
                // TODO: log
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    private List<FlowerBundle> convertJson(List<FlowerBundleJson> flowerBundleJsons) {
        return flowerBundleJsons.stream()
            .map( flowerBundleJson -> {
                Flower flower = new Flower(flowerBundleJson.productCode, flowerBundleJson.flowerName);
                Set<Bundle> bundles = flowerBundleJson.bundles.stream()
                    .map( bundleJson -> {
                        return new Bundle(bundleJson.size, bundleJson.price);
                    })
                    .collect(Collectors.toSet());
                return new FlowerBundle(flower, BundleOptions.builder().withBundles(bundles).build());
            }).collect(Collectors.toList());
    }


    private static class FlowerBundleJson {
        @JsonProperty("product_code")
        private String productCode;
        @JsonProperty("flower_name")
        private String flowerName;
        @JsonProperty("bundles")
        private List<BundleEntry> bundles;

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getFlowerName() {
            return flowerName;
        }

        public void setFlowerName(String flowerName) {
            this.flowerName = flowerName;
        }

        public List<BundleEntry> getBundles() {
            return bundles;
        }

        public void setBundles(List<BundleEntry> bundles) {
            this.bundles = bundles;
        }

        private static class BundleEntry {
            @JsonProperty("size")
            private Integer size;
            @JsonProperty("price")
            private BigDecimal price;

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public BigDecimal getPrice() {
                return price;
            }

            public void setPrice(BigDecimal price) {
                this.price = price;
            }
        }
    }

}
