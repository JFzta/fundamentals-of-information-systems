package com.udea.lab.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ContratoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Contrato getContratoSample1() {
        return new Contrato().id(1L);
    }

    public static Contrato getContratoSample2() {
        return new Contrato().id(2L);
    }

    public static Contrato getContratoRandomSampleGenerator() {
        return new Contrato().id(longCount.incrementAndGet());
    }
}
