package com.udea.lab.web.rest;

import static com.udea.lab.domain.DireccionAsserts.*;
import static com.udea.lab.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.lab.IntegrationTest;
import com.udea.lab.domain.Direccion;
import com.udea.lab.repository.DireccionRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DireccionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DireccionResourceIT {

    private static final String DEFAULT_CALLE = "AAAAAAAAAA";
    private static final String UPDATED_CALLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCIA = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/direccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDireccionMockMvc;

    private Direccion direccion;

    private Direccion insertedDireccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createEntity() {
        return new Direccion().calle(DEFAULT_CALLE).codigoPostal(DEFAULT_CODIGO_POSTAL).ciudad(DEFAULT_CIUDAD).provincia(DEFAULT_PROVINCIA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createUpdatedEntity() {
        return new Direccion().calle(UPDATED_CALLE).codigoPostal(UPDATED_CODIGO_POSTAL).ciudad(UPDATED_CIUDAD).provincia(UPDATED_PROVINCIA);
    }

    @BeforeEach
    void initTest() {
        direccion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDireccion != null) {
            direccionRepository.delete(insertedDireccion);
            insertedDireccion = null;
        }
    }

    @Test
    @Transactional
    void createDireccion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Direccion
        var returnedDireccion = om.readValue(
            restDireccionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Direccion.class
        );

        // Validate the Direccion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDireccionUpdatableFieldsEquals(returnedDireccion, getPersistedDireccion(returnedDireccion));

        insertedDireccion = returnedDireccion;
    }

    @Test
    @Transactional
    void createDireccionWithExistingId() throws Exception {
        // Create the Direccion with an existing ID
        direccion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccion)))
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDireccions() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].calle").value(hasItem(DEFAULT_CALLE)))
            .andExpect(jsonPath("$.[*].codigoPostal").value(hasItem(DEFAULT_CODIGO_POSTAL)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].provincia").value(hasItem(DEFAULT_PROVINCIA)));
    }

    @Test
    @Transactional
    void getDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get the direccion
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL_ID, direccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(direccion.getId().intValue()))
            .andExpect(jsonPath("$.calle").value(DEFAULT_CALLE))
            .andExpect(jsonPath("$.codigoPostal").value(DEFAULT_CODIGO_POSTAL))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD))
            .andExpect(jsonPath("$.provincia").value(DEFAULT_PROVINCIA));
    }

    @Test
    @Transactional
    void getNonExistingDireccion() throws Exception {
        // Get the direccion
        restDireccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion
        Direccion updatedDireccion = direccionRepository.findById(direccion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDireccion are not directly saved in db
        em.detach(updatedDireccion);
        updatedDireccion.calle(UPDATED_CALLE).codigoPostal(UPDATED_CODIGO_POSTAL).ciudad(UPDATED_CIUDAD).provincia(UPDATED_PROVINCIA);

        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDireccion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDireccionToMatchAllProperties(updatedDireccion);
    }

    @Test
    @Transactional
    void putNonExistingDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direccion.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(direccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion.codigoPostal(UPDATED_CODIGO_POSTAL).ciudad(UPDATED_CIUDAD);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDireccionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDireccion, direccion),
            getPersistedDireccion(direccion)
        );
    }

    @Test
    @Transactional
    void fullUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion
            .calle(UPDATED_CALLE)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .ciudad(UPDATED_CIUDAD)
            .provincia(UPDATED_PROVINCIA);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDireccionUpdatableFieldsEquals(partialUpdatedDireccion, getPersistedDireccion(partialUpdatedDireccion));
    }

    @Test
    @Transactional
    void patchNonExistingDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, direccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(direccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(direccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(direccion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the direccion
        restDireccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, direccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return direccionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Direccion getPersistedDireccion(Direccion direccion) {
        return direccionRepository.findById(direccion.getId()).orElseThrow();
    }

    protected void assertPersistedDireccionToMatchAllProperties(Direccion expectedDireccion) {
        assertDireccionAllPropertiesEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
    }

    protected void assertPersistedDireccionToMatchUpdatableProperties(Direccion expectedDireccion) {
        assertDireccionAllUpdatablePropertiesEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
    }
}
