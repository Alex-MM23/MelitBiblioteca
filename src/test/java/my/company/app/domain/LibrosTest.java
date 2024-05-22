package my.company.app.domain;

import static my.company.app.domain.LibrosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import my.company.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LibrosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Libros.class);
        Libros libros1 = getLibrosSample1();
        Libros libros2 = new Libros();
        assertThat(libros1).isNotEqualTo(libros2);

        libros2.setId(libros1.getId());
        assertThat(libros1).isEqualTo(libros2);

        libros2 = getLibrosSample2();
        assertThat(libros1).isNotEqualTo(libros2);
    }
}
