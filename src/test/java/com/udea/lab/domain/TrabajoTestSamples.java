package com.udea.lab.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TrabajoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Trabajo getTrabajoSample1() {
        return new Trabajo().id(1L).tituloTrabajo("tituloTrabajo1").salarioMin(1L).salarioMax(1L);
    }

    public static Trabajo getTrabajoSample2() {
        return new Trabajo().id(2L).tituloTrabajo("tituloTrabajo2").salarioMin(2L).salarioMax(2L);
    }

    public static Trabajo getTrabajoRandomSampleGenerator() {
        return new Trabajo()
            .id(longCount.incrementAndGet())
            .tituloTrabajo(UUID.randomUUID().toString())
            .salarioMin(longCount.incrementAndGet())
            .salarioMax(longCount.incrementAndGet());
    }
}
