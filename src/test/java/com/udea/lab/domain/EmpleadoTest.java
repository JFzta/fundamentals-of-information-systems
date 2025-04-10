package com.udea.lab.domain;

import static com.udea.lab.domain.ContratoTestSamples.*;
import static com.udea.lab.domain.DepartamentoTestSamples.*;
import static com.udea.lab.domain.EmpleadoTestSamples.*;
import static com.udea.lab.domain.EmpleadoTestSamples.*;
import static com.udea.lab.domain.TrabajoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.lab.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmpleadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Empleado.class);
        Empleado empleado1 = getEmpleadoSample1();
        Empleado empleado2 = new Empleado();
        assertThat(empleado1).isNotEqualTo(empleado2);

        empleado2.setId(empleado1.getId());
        assertThat(empleado1).isEqualTo(empleado2);

        empleado2 = getEmpleadoSample2();
        assertThat(empleado1).isNotEqualTo(empleado2);
    }

    @Test
    void trabajoTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Trabajo trabajoBack = getTrabajoRandomSampleGenerator();

        empleado.addTrabajo(trabajoBack);
        assertThat(empleado.getTrabajos()).containsOnly(trabajoBack);
        assertThat(trabajoBack.getEmpleado()).isEqualTo(empleado);

        empleado.removeTrabajo(trabajoBack);
        assertThat(empleado.getTrabajos()).doesNotContain(trabajoBack);
        assertThat(trabajoBack.getEmpleado()).isNull();

        empleado.trabajos(new HashSet<>(Set.of(trabajoBack)));
        assertThat(empleado.getTrabajos()).containsOnly(trabajoBack);
        assertThat(trabajoBack.getEmpleado()).isEqualTo(empleado);

        empleado.setTrabajos(new HashSet<>());
        assertThat(empleado.getTrabajos()).doesNotContain(trabajoBack);
        assertThat(trabajoBack.getEmpleado()).isNull();
    }

    @Test
    void inmediatosuperiorTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        empleado.setInmediatosuperior(empleadoBack);
        assertThat(empleado.getInmediatosuperior()).isEqualTo(empleadoBack);

        empleado.inmediatosuperior(null);
        assertThat(empleado.getInmediatosuperior()).isNull();
    }

    @Test
    void departamentoTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Departamento departamentoBack = getDepartamentoRandomSampleGenerator();

        empleado.setDepartamento(departamentoBack);
        assertThat(empleado.getDepartamento()).isEqualTo(departamentoBack);

        empleado.departamento(null);
        assertThat(empleado.getDepartamento()).isNull();
    }

    @Test
    void contratoTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Contrato contratoBack = getContratoRandomSampleGenerator();

        empleado.setContrato(contratoBack);
        assertThat(empleado.getContrato()).isEqualTo(contratoBack);
        assertThat(contratoBack.getEmpleado()).isEqualTo(empleado);

        empleado.contrato(null);
        assertThat(empleado.getContrato()).isNull();
        assertThat(contratoBack.getEmpleado()).isNull();
    }
}
