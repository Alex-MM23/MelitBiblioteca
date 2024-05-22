package my.company.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UsuariosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Usuarios getUsuariosSample1() {
        return new Usuarios()
            .id(1L)
            .username("username1")
            .password("password1")
            .email("email1")
            .nombre("nombre1")
            .apellido("apellido1")
            .direccion("direccion1");
    }

    public static Usuarios getUsuariosSample2() {
        return new Usuarios()
            .id(2L)
            .username("username2")
            .password("password2")
            .email("email2")
            .nombre("nombre2")
            .apellido("apellido2")
            .direccion("direccion2");
    }

    public static Usuarios getUsuariosRandomSampleGenerator() {
        return new Usuarios()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString());
    }
}
