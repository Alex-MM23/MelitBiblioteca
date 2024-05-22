package my.company.app.domain;

import static my.company.app.domain.PerfilTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import my.company.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerfilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Perfil.class);
        Perfil perfil1 = getPerfilSample1();
        Perfil perfil2 = new Perfil();
        assertThat(perfil1).isNotEqualTo(perfil2);

        perfil2.setId(perfil1.getId());
        assertThat(perfil1).isEqualTo(perfil2);

        perfil2 = getPerfilSample2();
        assertThat(perfil1).isNotEqualTo(perfil2);
    }
}
