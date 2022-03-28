package com.skylife.travel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.skylife.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PackageTourTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackageTour.class);
        PackageTour packageTour1 = new PackageTour();
        packageTour1.setId(1L);
        PackageTour packageTour2 = new PackageTour();
        packageTour2.setId(packageTour1.getId());
        assertThat(packageTour1).isEqualTo(packageTour2);
        packageTour2.setId(2L);
        assertThat(packageTour1).isNotEqualTo(packageTour2);
        packageTour1.setId(null);
        assertThat(packageTour1).isNotEqualTo(packageTour2);
    }
}
