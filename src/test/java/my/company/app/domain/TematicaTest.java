package my.company.app.domain;

import static my.company.app.domain.TematicaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import my.company.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TematicaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tematica.class);
        Tematica tematica1 = getTematicaSample1();
        Tematica tematica2 = new Tematica();
        assertThat(tematica1).isNotEqualTo(tematica2);

        tematica2.setId(tematica1.getId());
        assertThat(tematica1).isEqualTo(tematica2);

        tematica2 = getTematicaSample2();
        assertThat(tematica1).isNotEqualTo(tematica2);
    }
}
