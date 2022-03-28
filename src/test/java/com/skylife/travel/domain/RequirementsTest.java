package com.skylife.travel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.skylife.travel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequirementsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Requirements.class);
        Requirements requirements1 = new Requirements();
        requirements1.setId(1L);
        Requirements requirements2 = new Requirements();
        requirements2.setId(requirements1.getId());
        assertThat(requirements1).isEqualTo(requirements2);
        requirements2.setId(2L);
        assertThat(requirements1).isNotEqualTo(requirements2);
        requirements1.setId(null);
        assertThat(requirements1).isNotEqualTo(requirements2);
    }
}
