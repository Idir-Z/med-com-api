package com.zidir.medcom.domain;

import static com.zidir.medcom.domain.NotificationTestSamples.*;
import static com.zidir.medcom.domain.PharmacyTestSamples.*;
import static com.zidir.medcom.domain.ProductTestSamples.*;
import static com.zidir.medcom.domain.WatchListItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.zidir.medcom.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WatchListItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatchListItem.class);
        WatchListItem watchListItem1 = getWatchListItemSample1();
        WatchListItem watchListItem2 = new WatchListItem();
        assertThat(watchListItem1).isNotEqualTo(watchListItem2);

        watchListItem2.setId(watchListItem1.getId());
        assertThat(watchListItem1).isEqualTo(watchListItem2);

        watchListItem2 = getWatchListItemSample2();
        assertThat(watchListItem1).isNotEqualTo(watchListItem2);
    }

    @Test
    void productTest() {
        WatchListItem watchListItem = getWatchListItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        watchListItem.setProduct(productBack);
        assertThat(watchListItem.getProduct()).isEqualTo(productBack);

        watchListItem.product(null);
        assertThat(watchListItem.getProduct()).isNull();
    }

    @Test
    void pharmacyTest() {
        WatchListItem watchListItem = getWatchListItemRandomSampleGenerator();
        Pharmacy pharmacyBack = getPharmacyRandomSampleGenerator();

        watchListItem.setPharmacy(pharmacyBack);
        assertThat(watchListItem.getPharmacy()).isEqualTo(pharmacyBack);

        watchListItem.pharmacy(null);
        assertThat(watchListItem.getPharmacy()).isNull();
    }

    @Test
    void notificationTest() {
        WatchListItem watchListItem = getWatchListItemRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        watchListItem.addNotification(notificationBack);
        assertThat(watchListItem.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getWatchListItem()).isEqualTo(watchListItem);

        watchListItem.removeNotification(notificationBack);
        assertThat(watchListItem.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getWatchListItem()).isNull();

        watchListItem.notifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(watchListItem.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getWatchListItem()).isEqualTo(watchListItem);

        watchListItem.setNotifications(new HashSet<>());
        assertThat(watchListItem.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getWatchListItem()).isNull();
    }
}
