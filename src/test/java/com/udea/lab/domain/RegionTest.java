package com.udea.lab.domain;

import static com.udea.lab.domain.PaisTestSamples.*;
import static com.udea.lab.domain.RegionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.lab.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Region.class);
        Region region1 = getRegionSample1();
        Region region2 = new Region();
        assertThat(region1).isNotEqualTo(region2);

        region2.setId(region1.getId());
        assertThat(region1).isEqualTo(region2);

        region2 = getRegionSample2();
        assertThat(region1).isNotEqualTo(region2);
    }

    @Test
    void paisTest() {
        Region region = getRegionRandomSampleGenerator();
        Pais paisBack = getPaisRandomSampleGenerator();

        region.setPais(paisBack);
        assertThat(region.getPais()).isEqualTo(paisBack);
        assertThat(paisBack.getRegion()).isEqualTo(region);

        region.pais(null);
        assertThat(region.getPais()).isNull();
        assertThat(paisBack.getRegion()).isNull();
    }
}
