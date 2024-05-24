package my.company.app.domain;

import static my.company.app.domain.PedidosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import my.company.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PedidosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pedidos.class);
        Pedidos pedidos1 = getPedidosSample1();
        Pedidos pedidos2 = new Pedidos();
        assertThat(pedidos1).isNotEqualTo(pedidos2);

        pedidos2.setId(pedidos1.getId());
        assertThat(pedidos1).isEqualTo(pedidos2);

        pedidos2 = getPedidosSample2();
        assertThat(pedidos1).isNotEqualTo(pedidos2);
    }
}
