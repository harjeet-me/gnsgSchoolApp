package com.gnsg.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gnsg.app.web.rest.TestUtil;

public class PRoulTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PRoul.class);
        PRoul pRoul1 = new PRoul();
        pRoul1.setId(1L);
        PRoul pRoul2 = new PRoul();
        pRoul2.setId(pRoul1.getId());
        assertThat(pRoul1).isEqualTo(pRoul2);
        pRoul2.setId(2L);
        assertThat(pRoul1).isNotEqualTo(pRoul2);
        pRoul1.setId(null);
        assertThat(pRoul1).isNotEqualTo(pRoul2);
    }
}
