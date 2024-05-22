package my.company.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PerfilTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Perfil getPerfilSample1() {
        return new Perfil().id(1L).admin("admin1").usuario("usuario1");
    }

    public static Perfil getPerfilSample2() {
        return new Perfil().id(2L).admin("admin2").usuario("usuario2");
    }

    public static Perfil getPerfilRandomSampleGenerator() {
        return new Perfil().id(longCount.incrementAndGet()).admin(UUID.randomUUID().toString()).usuario(UUID.randomUUID().toString());
    }
}
