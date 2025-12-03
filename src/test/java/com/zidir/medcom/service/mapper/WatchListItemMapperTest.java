package com.zidir.medcom.service.mapper;

import static com.zidir.medcom.domain.WatchListItemAsserts.*;
import static com.zidir.medcom.domain.WatchListItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WatchListItemMapperTest {

    private WatchListItemMapper watchListItemMapper;

    @BeforeEach
    void setUp() {
        watchListItemMapper = new WatchListItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWatchListItemSample1();
        var actual = watchListItemMapper.toEntity(watchListItemMapper.toDto(expected));
        assertWatchListItemAllPropertiesEquals(expected, actual);
    }
}
