package com.zidir.medcom.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Notification getNotificationSample1() {
        return new Notification().id(1L).message("message1").failureReason("failureReason1").externalMessageId("externalMessageId1");
    }

    public static Notification getNotificationSample2() {
        return new Notification().id(2L).message("message2").failureReason("failureReason2").externalMessageId("externalMessageId2");
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(longCount.incrementAndGet())
            .message(UUID.randomUUID().toString())
            .failureReason(UUID.randomUUID().toString())
            .externalMessageId(UUID.randomUUID().toString());
    }
}
