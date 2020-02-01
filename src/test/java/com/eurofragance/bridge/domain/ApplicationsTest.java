package com.eurofragance.bridge.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.eurofragance.bridge.web.rest.TestUtil;

public class ApplicationsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Applications.class);
        Applications applications1 = new Applications();
        applications1.setId(1L);
        Applications applications2 = new Applications();
        applications2.setId(applications1.getId());
        assertThat(applications1).isEqualTo(applications2);
        applications2.setId(2L);
        assertThat(applications1).isNotEqualTo(applications2);
        applications1.setId(null);
        assertThat(applications1).isNotEqualTo(applications2);
    }
}
