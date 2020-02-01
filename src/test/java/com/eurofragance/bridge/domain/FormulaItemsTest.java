package com.eurofragance.bridge.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.eurofragance.bridge.web.rest.TestUtil;

public class FormulaItemsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormulaItems.class);
        FormulaItems formulaItems1 = new FormulaItems();
        formulaItems1.setId(1L);
        FormulaItems formulaItems2 = new FormulaItems();
        formulaItems2.setId(formulaItems1.getId());
        assertThat(formulaItems1).isEqualTo(formulaItems2);
        formulaItems2.setId(2L);
        assertThat(formulaItems1).isNotEqualTo(formulaItems2);
        formulaItems1.setId(null);
        assertThat(formulaItems1).isNotEqualTo(formulaItems2);
    }
}
