package com.mark.flowershop.data.read;

import com.mark.flowershop.product.FlowerBundle;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/2/2016.
 */
public class FlowerBundleReaderTest {

    @Test
    public void assertFlowerBundlesAreReadFromResources() {

        Optional<List<FlowerBundle>> flowerBundlesOptional = new FlowerBundleReader().readResource("/products/flowers/flower_bundles.json");
        assertThat(flowerBundlesOptional).isPresent();
        List<FlowerBundle> flowerBundles = flowerBundlesOptional.get();
        assertThat(flowerBundles).isNotEmpty();
        assertThat(flowerBundles).hasSize(3); // Only 3 bundles expected
        // Make sure we get the three product code we expect also
        assertThat(flowerBundles).extracting("flower.code").contains("R12", "L09", "T58");
    }

}
