package com.udea.lab.domain;

import static com.udea.lab.domain.TareaTestSamples.*;
import static com.udea.lab.domain.TrabajoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.lab.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TareaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tarea.class);
        Tarea tarea1 = getTareaSample1();
        Tarea tarea2 = new Tarea();
        assertThat(tarea1).isNotEqualTo(tarea2);

        tarea2.setId(tarea1.getId());
        assertThat(tarea1).isEqualTo(tarea2);

        tarea2 = getTareaSample2();
        assertThat(tarea1).isNotEqualTo(tarea2);
    }

    @Test
    void trabajoTest() {
        Tarea tarea = getTareaRandomSampleGenerator();
        Trabajo trabajoBack = getTrabajoRandomSampleGenerator();

        tarea.addTrabajo(trabajoBack);
        assertThat(tarea.getTrabajos()).containsOnly(trabajoBack);
        assertThat(trabajoBack.getTareas()).containsOnly(tarea);

        tarea.removeTrabajo(trabajoBack);
        assertThat(tarea.getTrabajos()).doesNotContain(trabajoBack);
        assertThat(trabajoBack.getTareas()).doesNotContain(tarea);

        tarea.trabajos(new HashSet<>(Set.of(trabajoBack)));
        assertThat(tarea.getTrabajos()).containsOnly(trabajoBack);
        assertThat(trabajoBack.getTareas()).containsOnly(tarea);

        tarea.setTrabajos(new HashSet<>());
        assertThat(tarea.getTrabajos()).doesNotContain(trabajoBack);
        assertThat(trabajoBack.getTareas()).doesNotContain(tarea);
    }
}
