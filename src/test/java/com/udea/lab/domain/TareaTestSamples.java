package com.udea.lab.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TareaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Tarea getTareaSample1() {
        return new Tarea().id(1L).titulo("titulo1").descripcion("descripcion1");
    }

    public static Tarea getTareaSample2() {
        return new Tarea().id(2L).titulo("titulo2").descripcion("descripcion2");
    }

    public static Tarea getTareaRandomSampleGenerator() {
        return new Tarea().id(longCount.incrementAndGet()).titulo(UUID.randomUUID().toString()).descripcion(UUID.randomUUID().toString());
    }
}
