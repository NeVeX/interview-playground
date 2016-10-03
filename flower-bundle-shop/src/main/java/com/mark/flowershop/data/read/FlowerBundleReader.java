package com.mark.flowershop.data.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mark.flowershop.bundle.Bundle;
import com.mark.flowershop.bundle.BundleOptions;
import com.mark.flowershop.product.Flower;
import com.mark.flowershop.product.FlowerBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/2/2016.
 * <br>This class will help read FlowerBundles from a given resource as input
 */
public final class FlowerBundleReader extends ResourceReader<List<FlowerBundle>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(FlowerBundleReader.class);
    private final static String FLOWER_BUNDLE_RESOURCE = "/products/flowers/flower_bundles.json";
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Given the Buffered reader, this method will de-serialized the input into a list of {@link FlowerBundle}'s
     * @param bufferedReader - the valid buffered read to use
     * @return - The List of read flower bundles, or empty if none found
     * @throws IOException - if something went wrong reading the reader
     */
    @Override
    protected List<FlowerBundle> process(BufferedReader bufferedReader) throws IOException {
        try {
            List<FlowerBundleJson> flowerBundleJsons = OBJECT_MAPPER.readValue(bufferedReader, new TypeReference<List<FlowerBundleJson>>(){});
            if ( flowerBundleJsons != null && !flowerBundleJsons.isEmpty()) {
                return convertJson(flowerBundleJsons);
            }
        } catch (Exception e) {
            LOGGER.error("Could not parse the JSON flower bundles", e);
        }
        return new ArrayList<>(); // Nothing was read (or there were problems)
    }

    /**
     * Converts the given input JSON FlowerBundles into the model {@link FlowerBundle} POJO's
     * @param flowerBundleJsons - the read in Json flower bundles
     * @return
     */
    private List<FlowerBundle> convertJson(List<FlowerBundleJson> flowerBundleJsons) {
        return flowerBundleJsons.stream()
            .map( flowerBundleJson -> {
                Flower flower = new Flower(flowerBundleJson.productCode, flowerBundleJson.flowerName);
                Set<Bundle> bundles = flowerBundleJson.bundles.stream()
                    .map( bundleJson -> new Bundle(bundleJson.size, bundleJson.price))
                    .collect(Collectors.toSet());
                return new FlowerBundle(flower, BundleOptions.builder().withBundles(bundles).build());
            }).collect(Collectors.toList());
    }

    /**
     * Returns the application default Flower Bundles
     */
    public static List<FlowerBundle> getDefault() {
        Optional<List<FlowerBundle>> flowerBundlesOptional = new FlowerBundleReader().readResource(FLOWER_BUNDLE_RESOURCE);
        if ( flowerBundlesOptional.isPresent()) {
            return flowerBundlesOptional.get();
        } else {
            // This is bad, so throw a runtime - this should never happen, but if so, we need to know
            throw new IllegalStateException("Could not get the list of default flower bundles");
        }
    }

    /**
     * Inner class that represents the FlowerBundle in the resources folder.
     * Since this needs to be mutable, we do not reuse the immutable {@link FlowerBundle}
     */
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
