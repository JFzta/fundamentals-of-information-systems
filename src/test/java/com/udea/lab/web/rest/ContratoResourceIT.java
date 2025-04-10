package com.udea.lab.web.rest;

import static com.udea.lab.domain.ContratoAsserts.*;
import static com.udea.lab.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.lab.IntegrationTest;
import com.udea.lab.domain.Contrato;
import com.udea.lab.domain.enumeration.Idioma;
import com.udea.lab.repository.ContratoRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ContratoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContratoResourceIT {

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Idioma DEFAULT_LENGUAJE = Idioma.ESPANOL;
    private static final Idioma UPDATED_LENGUAJE = Idioma.INGLES;

    private static final String ENTITY_API_URL = "/api/contratoes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContratoMockMvc;

    private Contrato contrato;

    private Contrato insertedContrato;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contrato createEntity() {
        return new Contrato().fechaInicio(DEFAULT_FECHA_INICIO).fechaFin(DEFAULT_FECHA_FIN).lenguaje(DEFAULT_LENGUAJE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contrato createUpdatedEntity() {
        return new Contrato().fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).lenguaje(UPDATED_LENGUAJE);
    }

    @BeforeEach
    void initTest() {
        contrato = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContrato != null) {
            contratoRepository.delete(insertedContrato);
            insertedContrato = null;
        }
    }

    @Test
    @Transactional
    void createContrato() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Contrato
        var returnedContrato = om.readValue(
            restContratoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contrato)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Contrato.class
        );

        // Validate the Contrato in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContratoUpdatableFieldsEquals(returnedContrato, getPersistedContrato(returnedContrato));

        insertedContrato = returnedContrato;
    }

    @Test
    @Transactional
    void createContratoWithExistingId() throws Exception {
        // Create the Contrato with an existing ID
        contrato.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContratoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contrato)))
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContratoes() throws Exception {
        // Initialize the database
        insertedContrato = contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList
        restContratoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contrato.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].lenguaje").value(hasItem(DEFAULT_LENGUAJE.toString())));
    }

    @Test
    @Transactional
    void getContrato() throws Exception {
        // Initialize the database
        insertedContrato = contratoRepository.saveAndFlush(contrato);

        // Get the contrato
        restContratoMockMvc
            .perform(get(ENTITY_API_URL_ID, contrato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contrato.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.lenguaje").value(DEFAULT_LENGUAJE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContrato() throws Exception {
        // Get the contrato
        restContratoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContrato() throws Exception {
        // Initialize the database
        insertedContrato = contratoRepository.saveAndFlush(contrato);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contrato
        Contrato updatedContrato = contratoRepository.findById(contrato.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContrato are not directly saved in db
        em.detach(updatedContrato);
        updatedContrato.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).lenguaje(UPDATED_LENGUAJE);

        restContratoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContrato.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContrato))
            )
            .andExpect(status().isOk());

        // Validate the Contrato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContratoToMatchAllProperties(updatedContrato);
    }

    @Test
    @Transactional
    void putNonExistingContrato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrato.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contrato.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contrato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContrato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrato.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contrato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContrato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrato.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contrato)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contrato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContratoWithPatch() throws Exception {
        // Initialize the database
        insertedContrato = contratoRepository.saveAndFlush(contrato);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contrato using partial update
        Contrato partialUpdatedContrato = new Contrato();
        partialUpdatedContrato.setId(contrato.getId());

        partialUpdatedContrato.lenguaje(UPDATED_LENGUAJE);

        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContrato.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContrato))
            )
            .andExpect(status().isOk());

        // Validate the Contrato in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContratoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContrato, contrato), getPersistedContrato(contrato));
    }

    @Test
    @Transactional
    void fullUpdateContratoWithPatch() throws Exception {
        // Initialize the database
        insertedContrato = contratoRepository.saveAndFlush(contrato);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contrato using partial update
        Contrato partialUpdatedContrato = new Contrato();
        partialUpdatedContrato.setId(contrato.getId());

        partialUpdatedContrato.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).lenguaje(UPDATED_LENGUAJE);

        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContrato.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContrato))
            )
            .andExpect(status().isOk());

        // Validate the Contrato in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContratoUpdatableFieldsEquals(partialUpdatedContrato, getPersistedContrato(partialUpdatedContrato));
    }

    @Test
    @Transactional
    void patchNonExistingContrato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrato.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contrato.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contrato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContrato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrato.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contrato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContrato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrato.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contrato)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contrato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContrato() throws Exception {
        // Initialize the database
        insertedContrato = contratoRepository.saveAndFlush(contrato);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contrato
        restContratoMockMvc
            .perform(delete(ENTITY_API_URL_ID, contrato.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contratoRepository.count();
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

    protected Contrato getPersistedContrato(Contrato contrato) {
        return contratoRepository.findById(contrato.getId()).orElseThrow();
    }

    protected void assertPersistedContratoToMatchAllProperties(Contrato expectedContrato) {
        assertContratoAllPropertiesEquals(expectedContrato, getPersistedContrato(expectedContrato));
    }

    protected void assertPersistedContratoToMatchUpdatableProperties(Contrato expectedContrato) {
        assertContratoAllUpdatablePropertiesEquals(expectedContrato, getPersistedContrato(expectedContrato));
    }
}
