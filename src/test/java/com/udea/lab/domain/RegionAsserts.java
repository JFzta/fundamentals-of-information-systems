package com.udea.lab.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class RegionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRegionAllPropertiesEquals(Region expected, Region actual) {
        assertRegionAutoGeneratedPropertiesEquals(expected, actual);
        assertRegionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRegionAllUpdatablePropertiesEquals(Region expected, Region actual) {
        assertRegionUpdatableFieldsEquals(expected, actual);
        assertRegionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRegionAutoGeneratedPropertiesEquals(Region expected, Region actual) {
        assertThat(actual)
            .as("Verify Region auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRegionUpdatableFieldsEquals(Region expected, Region actual) {
        assertThat(actual)
            .as("Verify Region relevant properties")
            .satisfies(a -> assertThat(a.getNombreRegion()).as("check nombreRegion").isEqualTo(expected.getNombreRegion()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRegionUpdatableRelationshipsEquals(Region expected, Region actual) {
        // empty method
    }
}
