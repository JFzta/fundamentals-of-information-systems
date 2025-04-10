package com.udea.lab.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaisTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pais getPaisSample1() {
        return new Pais().id(1L).nombrePais("nombrePais1");
    }

    public static Pais getPaisSample2() {
        return new Pais().id(2L).nombrePais("nombrePais2");
    }

    public static Pais getPaisRandomSampleGenerator() {
        return new Pais().id(longCount.incrementAndGet()).nombrePais(UUID.randomUUID().toString());
    }
}
