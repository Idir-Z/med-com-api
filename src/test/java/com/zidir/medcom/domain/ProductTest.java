package com.zidir.medcom.domain;

import static com.zidir.medcom.domain.ProductTestSamples.*;
import static com.zidir.medcom.domain.WatchListItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.zidir.medcom.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void watchListItemTest() {
        Product product = getProductRandomSampleGenerator();
        WatchListItem watchListItemBack = getWatchListItemRandomSampleGenerator();

        product.addWatchListItem(watchListItemBack);
        assertThat(product.getWatchListItems()).containsOnly(watchListItemBack);
        assertThat(watchListItemBack.getProduct()).isEqualTo(product);

        product.removeWatchListItem(watchListItemBack);
        assertThat(product.getWatchListItems()).doesNotContain(watchListItemBack);
        assertThat(watchListItemBack.getProduct()).isNull();

        product.watchListItems(new HashSet<>(Set.of(watchListItemBack)));
        assertThat(product.getWatchListItems()).containsOnly(watchListItemBack);
        assertThat(watchListItemBack.getProduct()).isEqualTo(product);

        product.setWatchListItems(new HashSet<>());
        assertThat(product.getWatchListItems()).doesNotContain(watchListItemBack);
        assertThat(watchListItemBack.getProduct()).isNull();
    }
}
