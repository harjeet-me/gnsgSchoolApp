package com.gnsg.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gnsg.app.web.rest.TestUtil;

public class ASPathTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ASPath.class);
        ASPath aSPath1 = new ASPath();
        aSPath1.setId(1L);
        ASPath aSPath2 = new ASPath();
        aSPath2.setId(aSPath1.getId());
        assertThat(aSPath1).isEqualTo(aSPath2);
        aSPath2.setId(2L);
        assertThat(aSPath1).isNotEqualTo(aSPath2);
        aSPath1.setId(null);
        assertThat(aSPath1).isNotEqualTo(aSPath2);
    }
}
