package com.zidir.medcom.domain;

import static com.zidir.medcom.domain.NotificationTestSamples.*;
import static com.zidir.medcom.domain.PharmacyTestSamples.*;
import static com.zidir.medcom.domain.WatchListItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.zidir.medcom.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void pharmacyTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        Pharmacy pharmacyBack = getPharmacyRandomSampleGenerator();

        notification.setPharmacy(pharmacyBack);
        assertThat(notification.getPharmacy()).isEqualTo(pharmacyBack);

        notification.pharmacy(null);
        assertThat(notification.getPharmacy()).isNull();
    }

    @Test
    void watchListItemTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        WatchListItem watchListItemBack = getWatchListItemRandomSampleGenerator();

        notification.setWatchListItem(watchListItemBack);
        assertThat(notification.getWatchListItem()).isEqualTo(watchListItemBack);

        notification.watchListItem(null);
        assertThat(notification.getWatchListItem()).isNull();
    }
}
