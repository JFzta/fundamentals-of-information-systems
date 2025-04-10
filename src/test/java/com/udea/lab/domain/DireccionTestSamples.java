package com.udea.lab.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DireccionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Direccion getDireccionSample1() {
        return new Direccion().id(1L).calle("calle1").codigoPostal("codigoPostal1").ciudad("ciudad1").provincia("provincia1");
    }

    public static Direccion getDireccionSample2() {
        return new Direccion().id(2L).calle("calle2").codigoPostal("codigoPostal2").ciudad("ciudad2").provincia("provincia2");
    }

    public static Direccion getDireccionRandomSampleGenerator() {
        return new Direccion()
            .id(longCount.incrementAndGet())
            .calle(UUID.randomUUID().toString())
            .codigoPostal(UUID.randomUUID().toString())
            .ciudad(UUID.randomUUID().toString())
            .provincia(UUID.randomUUID().toString());
    }
}
