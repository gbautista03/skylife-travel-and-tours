package com.skylife.travel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.skylife.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OHDCTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OHDC.class);
        OHDC oHDC1 = new OHDC();
        oHDC1.setId(1L);
        OHDC oHDC2 = new OHDC();
        oHDC2.setId(oHDC1.getId());
        assertThat(oHDC1).isEqualTo(oHDC2);
        oHDC2.setId(2L);
        assertThat(oHDC1).isNotEqualTo(oHDC2);
        oHDC1.setId(null);
        assertThat(oHDC1).isNotEqualTo(oHDC2);
    }
}
