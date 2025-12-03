package com.zidir.medcom.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.zidir.medcom.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WatchListItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatchListItemDTO.class);
        WatchListItemDTO watchListItemDTO1 = new WatchListItemDTO();
        watchListItemDTO1.setId(1L);
        WatchListItemDTO watchListItemDTO2 = new WatchListItemDTO();
        assertThat(watchListItemDTO1).isNotEqualTo(watchListItemDTO2);
        watchListItemDTO2.setId(watchListItemDTO1.getId());
        assertThat(watchListItemDTO1).isEqualTo(watchListItemDTO2);
        watchListItemDTO2.setId(2L);
        assertThat(watchListItemDTO1).isNotEqualTo(watchListItemDTO2);
        watchListItemDTO1.setId(null);
        assertThat(watchListItemDTO1).isNotEqualTo(watchListItemDTO2);
    }
}
