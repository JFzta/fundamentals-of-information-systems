package com.udea.lab.domain;

import static com.udea.lab.domain.DepartamentoTestSamples.*;
import static com.udea.lab.domain.DireccionTestSamples.*;
import static com.udea.lab.domain.PaisTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.lab.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DireccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Direccion.class);
        Direccion direccion1 = getDireccionSample1();
        Direccion direccion2 = new Direccion();
        assertThat(direccion1).isNotEqualTo(direccion2);

        direccion2.setId(direccion1.getId());
        assertThat(direccion1).isEqualTo(direccion2);

        direccion2 = getDireccionSample2();
        assertThat(direccion1).isNotEqualTo(direccion2);
    }

    @Test
    void paisTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        Pais paisBack = getPaisRandomSampleGenerator();

        direccion.setPais(paisBack);
        assertThat(direccion.getPais()).isEqualTo(paisBack);

        direccion.pais(null);
        assertThat(direccion.getPais()).isNull();
    }

    @Test
    void departamentoTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        Departamento departamentoBack = getDepartamentoRandomSampleGenerator();

        direccion.setDepartamento(departamentoBack);
        assertThat(direccion.getDepartamento()).isEqualTo(departamentoBack);
        assertThat(departamentoBack.getDireccion()).isEqualTo(direccion);

        direccion.departamento(null);
        assertThat(direccion.getDepartamento()).isNull();
        assertThat(departamentoBack.getDireccion()).isNull();
    }
}
