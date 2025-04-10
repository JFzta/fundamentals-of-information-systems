package com.udea.lab.domain;

import static com.udea.lab.domain.ContratoTestSamples.*;
import static com.udea.lab.domain.DepartamentoTestSamples.*;
import static com.udea.lab.domain.EmpleadoTestSamples.*;
import static com.udea.lab.domain.TrabajoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.lab.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContratoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contrato.class);
        Contrato contrato1 = getContratoSample1();
        Contrato contrato2 = new Contrato();
        assertThat(contrato1).isNotEqualTo(contrato2);

        contrato2.setId(contrato1.getId());
        assertThat(contrato1).isEqualTo(contrato2);

        contrato2 = getContratoSample2();
        assertThat(contrato1).isNotEqualTo(contrato2);
    }

    @Test
    void trabajoTest() {
        Contrato contrato = getContratoRandomSampleGenerator();
        Trabajo trabajoBack = getTrabajoRandomSampleGenerator();

        contrato.setTrabajo(trabajoBack);
        assertThat(contrato.getTrabajo()).isEqualTo(trabajoBack);

        contrato.trabajo(null);
        assertThat(contrato.getTrabajo()).isNull();
    }

    @Test
    void departamentoTest() {
        Contrato contrato = getContratoRandomSampleGenerator();
        Departamento departamentoBack = getDepartamentoRandomSampleGenerator();

        contrato.setDepartamento(departamentoBack);
        assertThat(contrato.getDepartamento()).isEqualTo(departamentoBack);

        contrato.departamento(null);
        assertThat(contrato.getDepartamento()).isNull();
    }

    @Test
    void empleadoTest() {
        Contrato contrato = getContratoRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        contrato.setEmpleado(empleadoBack);
        assertThat(contrato.getEmpleado()).isEqualTo(empleadoBack);

        contrato.empleado(null);
        assertThat(contrato.getEmpleado()).isNull();
    }
}
