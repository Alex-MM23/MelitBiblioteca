package my.company.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TematicaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Tematica getTematicaSample1() {
        return new Tematica().id(1L).abreviatura("abreviatura1").descripcion("descripcion1");
    }

    public static Tematica getTematicaSample2() {
        return new Tematica().id(2L).abreviatura("abreviatura2").descripcion("descripcion2");
    }

    public static Tematica getTematicaRandomSampleGenerator() {
        return new Tematica()
            .id(longCount.incrementAndGet())
            .abreviatura(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
