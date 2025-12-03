package com.zidir.medcom.domain;

import static com.zidir.medcom.domain.PharmacyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.zidir.medcom.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PharmacyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pharmacy.class);
        Pharmacy pharmacy1 = getPharmacySample1();
        Pharmacy pharmacy2 = new Pharmacy();
        assertThat(pharmacy1).isNotEqualTo(pharmacy2);

        pharmacy2.setId(pharmacy1.getId());
        assertThat(pharmacy1).isEqualTo(pharmacy2);

        pharmacy2 = getPharmacySample2();
        assertThat(pharmacy1).isNotEqualTo(pharmacy2);
    }
}
