package my.company.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LibrosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Libros getLibrosSample1() {
        return new Libros()
            .id(1L)
            .isbn(1)
            .stock(1)
            .autor("autor1")
            .imagen("imagen1")
            .paginas(1)
            .titulo("titulo1")
            .numeroAlquilados(1)
            .idTematica(1);
    }

    public static Libros getLibrosSample2() {
        return new Libros()
            .id(2L)
            .isbn(2)
            .stock(2)
            .autor("autor2")
            .imagen("imagen2")
            .paginas(2)
            .titulo("titulo2")
            .numeroAlquilados(2)
            .idTematica(2);
    }

    public static Libros getLibrosRandomSampleGenerator() {
        return new Libros()
            .id(longCount.incrementAndGet())
            .isbn(intCount.incrementAndGet())
            .stock(intCount.incrementAndGet())
            .autor(UUID.randomUUID().toString())
            .imagen(UUID.randomUUID().toString())
            .paginas(intCount.incrementAndGet())
            .titulo(UUID.randomUUID().toString())
            .numeroAlquilados(intCount.incrementAndGet())
            .idTematica(intCount.incrementAndGet());
    }
}
