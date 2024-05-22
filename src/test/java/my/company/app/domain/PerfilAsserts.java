package my.company.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PerfilAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPerfilAllPropertiesEquals(Perfil expected, Perfil actual) {
        assertPerfilAutoGeneratedPropertiesEquals(expected, actual);
        assertPerfilAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPerfilAllUpdatablePropertiesEquals(Perfil expected, Perfil actual) {
        assertPerfilUpdatableFieldsEquals(expected, actual);
        assertPerfilUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPerfilAutoGeneratedPropertiesEquals(Perfil expected, Perfil actual) {
        assertThat(expected)
            .as("Verify Perfil auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPerfilUpdatableFieldsEquals(Perfil expected, Perfil actual) {
        assertThat(expected)
            .as("Verify Perfil relevant properties")
            .satisfies(e -> assertThat(e.getAdmin()).as("check admin").isEqualTo(actual.getAdmin()))
            .satisfies(e -> assertThat(e.getUsuario()).as("check usuario").isEqualTo(actual.getUsuario()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPerfilUpdatableRelationshipsEquals(Perfil expected, Perfil actual) {}
}
