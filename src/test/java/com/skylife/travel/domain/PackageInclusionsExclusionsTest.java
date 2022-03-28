package com.skylife.travel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.skylife.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PackageInclusionsExclusionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackageInclusionsExclusions.class);
        PackageInclusionsExclusions packageInclusionsExclusions1 = new PackageInclusionsExclusions();
        packageInclusionsExclusions1.setId(1L);
        PackageInclusionsExclusions packageInclusionsExclusions2 = new PackageInclusionsExclusions();
        packageInclusionsExclusions2.setId(packageInclusionsExclusions1.getId());
        assertThat(packageInclusionsExclusions1).isEqualTo(packageInclusionsExclusions2);
        packageInclusionsExclusions2.setId(2L);
        assertThat(packageInclusionsExclusions1).isNotEqualTo(packageInclusionsExclusions2);
        packageInclusionsExclusions1.setId(null);
        assertThat(packageInclusionsExclusions1).isNotEqualTo(packageInclusionsExclusions2);
    }
}
