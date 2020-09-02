package com.gnsg.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gnsg.app.web.rest.TestUtil;

public class SevadarTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sevadar.class);
        Sevadar sevadar1 = new Sevadar();
        sevadar1.setId(1L);
        Sevadar sevadar2 = new Sevadar();
        sevadar2.setId(sevadar1.getId());
        assertThat(sevadar1).isEqualTo(sevadar2);
        sevadar2.setId(2L);
        assertThat(sevadar1).isNotEqualTo(sevadar2);
        sevadar1.setId(null);
        assertThat(sevadar1).isNotEqualTo(sevadar2);
    }
}
