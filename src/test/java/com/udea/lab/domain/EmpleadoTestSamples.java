package com.udea.lab.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmpleadoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Empleado getEmpleadoSample1() {
        return new Empleado()
            .id(1L)
            .nombres("nombres1")
            .apellidos("apellidos1")
            .correo("correo1")
            .nroCelular("nroCelular1")
            .salario(1L)
            .comisionPorcentaje(1L);
    }

    public static Empleado getEmpleadoSample2() {
        return new Empleado()
            .id(2L)
            .nombres("nombres2")
            .apellidos("apellidos2")
            .correo("correo2")
            .nroCelular("nroCelular2")
            .salario(2L)
            .comisionPorcentaje(2L);
    }

    public static Empleado getEmpleadoRandomSampleGenerator() {
        return new Empleado()
            .id(longCount.incrementAndGet())
            .nombres(UUID.randomUUID().toString())
            .apellidos(UUID.randomUUID().toString())
            .correo(UUID.randomUUID().toString())
            .nroCelular(UUID.randomUUID().toString())
            .salario(longCount.incrementAndGet())
            .comisionPorcentaje(longCount.incrementAndGet());
    }
}
