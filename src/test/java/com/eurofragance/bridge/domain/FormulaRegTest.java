package com.eurofragance.bridge.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.eurofragance.bridge.web.rest.TestUtil;

public class FormulaRegTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormulaReg.class);
        FormulaReg formulaReg1 = new FormulaReg();
        formulaReg1.setId(1L);
        FormulaReg formulaReg2 = new FormulaReg();
        formulaReg2.setId(formulaReg1.getId());
        assertThat(formulaReg1).isEqualTo(formulaReg2);
        formulaReg2.setId(2L);
        assertThat(formulaReg1).isNotEqualTo(formulaReg2);
        formulaReg1.setId(null);
        assertThat(formulaReg1).isNotEqualTo(formulaReg2);
    }
}
