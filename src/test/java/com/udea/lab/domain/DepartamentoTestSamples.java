package com.udea.lab.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DepartamentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Departamento getDepartamentoSample1() {
        return new Departamento().id(1L).nombreDepartamento("nombreDepartamento1");
    }

    public static Departamento getDepartamentoSample2() {
        return new Departamento().id(2L).nombreDepartamento("nombreDepartamento2");
    }

    public static Departamento getDepartamentoRandomSampleGenerator() {
        return new Departamento().id(longCount.incrementAndGet()).nombreDepartamento(UUID.randomUUID().toString());
    }
}
