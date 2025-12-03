package com.zidir.medcom.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class WatchListItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WatchListItem getWatchListItemSample1() {
        return new WatchListItem().id(1L);
    }

    public static WatchListItem getWatchListItemSample2() {
        return new WatchListItem().id(2L);
    }

    public static WatchListItem getWatchListItemRandomSampleGenerator() {
        return new WatchListItem().id(longCount.incrementAndGet());
    }
}
