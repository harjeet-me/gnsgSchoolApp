package com.gnsg.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gnsg.app.web.rest.TestUtil;

public class AppliedChargeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppliedCharge.class);
        AppliedCharge appliedCharge1 = new AppliedCharge();
        appliedCharge1.setId(1L);
        AppliedCharge appliedCharge2 = new AppliedCharge();
        appliedCharge2.setId(appliedCharge1.getId());
        assertThat(appliedCharge1).isEqualTo(appliedCharge2);
        appliedCharge2.setId(2L);
        assertThat(appliedCharge1).isNotEqualTo(appliedCharge2);
        appliedCharge1.setId(null);
        assertThat(appliedCharge1).isNotEqualTo(appliedCharge2);
    }
}
