package com.udea.lab.domain;

import static com.udea.lab.domain.DireccionTestSamples.*;
import static com.udea.lab.domain.PaisTestSamples.*;
import static com.udea.lab.domain.RegionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.lab.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pais.class);
        Pais pais1 = getPaisSample1();
        Pais pais2 = new Pais();
        assertThat(pais1).isNotEqualTo(pais2);

        pais2.setId(pais1.getId());
        assertThat(pais1).isEqualTo(pais2);

        pais2 = getPaisSample2();
        assertThat(pais1).isNotEqualTo(pais2);
    }

    @Test
    void regionTest() {
        Pais pais = getPaisRandomSampleGenerator();
        Region regionBack = getRegionRandomSampleGenerator();

        pais.setRegion(regionBack);
        assertThat(pais.getRegion()).isEqualTo(regionBack);

        pais.region(null);
        assertThat(pais.getRegion()).isNull();
    }

    @Test
    void direccionTest() {
        Pais pais = getPaisRandomSampleGenerator();
        Direccion direccionBack = getDireccionRandomSampleGenerator();

        pais.setDireccion(direccionBack);
        assertThat(pais.getDireccion()).isEqualTo(direccionBack);
        assertThat(direccionBack.getPais()).isEqualTo(pais);

        pais.direccion(null);
        assertThat(pais.getDireccion()).isNull();
        assertThat(direccionBack.getPais()).isNull();
    }
}
