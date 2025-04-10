package com.udea.lab.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TrabajoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTrabajoAllPropertiesEquals(Trabajo expected, Trabajo actual) {
        assertTrabajoAutoGeneratedPropertiesEquals(expected, actual);
        assertTrabajoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTrabajoAllUpdatablePropertiesEquals(Trabajo expected, Trabajo actual) {
        assertTrabajoUpdatableFieldsEquals(expected, actual);
        assertTrabajoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTrabajoAutoGeneratedPropertiesEquals(Trabajo expected, Trabajo actual) {
        assertThat(actual)
            .as("Verify Trabajo auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTrabajoUpdatableFieldsEquals(Trabajo expected, Trabajo actual) {
        assertThat(actual)
            .as("Verify Trabajo relevant properties")
            .satisfies(a -> assertThat(a.getTituloTrabajo()).as("check tituloTrabajo").isEqualTo(expected.getTituloTrabajo()))
            .satisfies(a -> assertThat(a.getSalarioMin()).as("check salarioMin").isEqualTo(expected.getSalarioMin()))
            .satisfies(a -> assertThat(a.getSalarioMax()).as("check salarioMax").isEqualTo(expected.getSalarioMax()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTrabajoUpdatableRelationshipsEquals(Trabajo expected, Trabajo actual) {
        assertThat(actual)
            .as("Verify Trabajo relationships")
            .satisfies(a -> assertThat(a.getTareas()).as("check tareas").isEqualTo(expected.getTareas()))
            .satisfies(a -> assertThat(a.getEmpleado()).as("check empleado").isEqualTo(expected.getEmpleado()));
    }
}
