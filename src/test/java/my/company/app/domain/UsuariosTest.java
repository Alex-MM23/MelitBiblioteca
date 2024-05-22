package my.company.app.domain;

import static my.company.app.domain.UsuariosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import my.company.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuariosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Usuarios.class);
        Usuarios usuarios1 = getUsuariosSample1();
        Usuarios usuarios2 = new Usuarios();
        assertThat(usuarios1).isNotEqualTo(usuarios2);

        usuarios2.setId(usuarios1.getId());
        assertThat(usuarios1).isEqualTo(usuarios2);

        usuarios2 = getUsuariosSample2();
        assertThat(usuarios1).isNotEqualTo(usuarios2);
    }
}
