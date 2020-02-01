package com.eurofragance.bridge.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.eurofragance.bridge.web.rest.TestUtil;

public class ToMfgTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToMfg.class);
        ToMfg toMfg1 = new ToMfg();
        toMfg1.setId(1L);
        ToMfg toMfg2 = new ToMfg();
        toMfg2.setId(toMfg1.getId());
        assertThat(toMfg1).isEqualTo(toMfg2);
        toMfg2.setId(2L);
        assertThat(toMfg1).isNotEqualTo(toMfg2);
        toMfg1.setId(null);
        assertThat(toMfg1).isNotEqualTo(toMfg2);
    }
}
