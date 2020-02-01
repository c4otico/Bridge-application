package com.eurofragance.bridge.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.eurofragance.bridge.web.rest.TestUtil;

public class FormulaStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormulaStatus.class);
        FormulaStatus formulaStatus1 = new FormulaStatus();
        formulaStatus1.setId(1L);
        FormulaStatus formulaStatus2 = new FormulaStatus();
        formulaStatus2.setId(formulaStatus1.getId());
        assertThat(formulaStatus1).isEqualTo(formulaStatus2);
        formulaStatus2.setId(2L);
        assertThat(formulaStatus1).isNotEqualTo(formulaStatus2);
        formulaStatus1.setId(null);
        assertThat(formulaStatus1).isNotEqualTo(formulaStatus2);
    }
}
