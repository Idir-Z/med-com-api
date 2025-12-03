package com.zidir.medcom.service.mapper;

import static com.zidir.medcom.domain.PharmacyAsserts.*;
import static com.zidir.medcom.domain.PharmacyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PharmacyMapperTest {

    private PharmacyMapper pharmacyMapper;

    @BeforeEach
    void setUp() {
        pharmacyMapper = new PharmacyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPharmacySample1();
        var actual = pharmacyMapper.toEntity(pharmacyMapper.toDto(expected));
        assertPharmacyAllPropertiesEquals(expected, actual);
    }
}
