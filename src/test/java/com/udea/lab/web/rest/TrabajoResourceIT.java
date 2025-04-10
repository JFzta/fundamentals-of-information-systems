package com.udea.lab.web.rest;

import static com.udea.lab.domain.TrabajoAsserts.*;
import static com.udea.lab.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.lab.IntegrationTest;
import com.udea.lab.domain.Trabajo;
import com.udea.lab.repository.TrabajoRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TrabajoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrabajoResourceIT {

    private static final String DEFAULT_TITULO_TRABAJO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO_TRABAJO = "BBBBBBBBBB";

    private static final Long DEFAULT_SALARIO_MIN = 1L;
    private static final Long UPDATED_SALARIO_MIN = 2L;

    private static final Long DEFAULT_SALARIO_MAX = 1L;
    private static final Long UPDATED_SALARIO_MAX = 2L;

    private static final String ENTITY_API_URL = "/api/trabajos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Mock
    private TrabajoRepository trabajoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrabajoMockMvc;

    private Trabajo trabajo;

    private Trabajo insertedTrabajo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajo createEntity() {
        return new Trabajo().tituloTrabajo(DEFAULT_TITULO_TRABAJO).salarioMin(DEFAULT_SALARIO_MIN).salarioMax(DEFAULT_SALARIO_MAX);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajo createUpdatedEntity() {
        return new Trabajo().tituloTrabajo(UPDATED_TITULO_TRABAJO).salarioMin(UPDATED_SALARIO_MIN).salarioMax(UPDATED_SALARIO_MAX);
    }

    @BeforeEach
    void initTest() {
        trabajo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrabajo != null) {
            trabajoRepository.delete(insertedTrabajo);
            insertedTrabajo = null;
        }
    }

    @Test
    @Transactional
    void createTrabajo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Trabajo
        var returnedTrabajo = om.readValue(
            restTrabajoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Trabajo.class
        );

        // Validate the Trabajo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTrabajoUpdatableFieldsEquals(returnedTrabajo, getPersistedTrabajo(returnedTrabajo));

        insertedTrabajo = returnedTrabajo;
    }

    @Test
    @Transactional
    void createTrabajoWithExistingId() throws Exception {
        // Create the Trabajo with an existing ID
        trabajo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrabajoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajo)))
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrabajos() throws Exception {
        // Initialize the database
        insertedTrabajo = trabajoRepository.saveAndFlush(trabajo);

        // Get all the trabajoList
        restTrabajoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajo.getId().intValue())))
            .andExpect(jsonPath("$.[*].tituloTrabajo").value(hasItem(DEFAULT_TITULO_TRABAJO)))
            .andExpect(jsonPath("$.[*].salarioMin").value(hasItem(DEFAULT_SALARIO_MIN.intValue())))
            .andExpect(jsonPath("$.[*].salarioMax").value(hasItem(DEFAULT_SALARIO_MAX.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrabajosWithEagerRelationshipsIsEnabled() throws Exception {
        when(trabajoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrabajoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trabajoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrabajosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trabajoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrabajoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(trabajoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrabajo() throws Exception {
        // Initialize the database
        insertedTrabajo = trabajoRepository.saveAndFlush(trabajo);

        // Get the trabajo
        restTrabajoMockMvc
            .perform(get(ENTITY_API_URL_ID, trabajo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trabajo.getId().intValue()))
            .andExpect(jsonPath("$.tituloTrabajo").value(DEFAULT_TITULO_TRABAJO))
            .andExpect(jsonPath("$.salarioMin").value(DEFAULT_SALARIO_MIN.intValue()))
            .andExpect(jsonPath("$.salarioMax").value(DEFAULT_SALARIO_MAX.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTrabajo() throws Exception {
        // Get the trabajo
        restTrabajoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrabajo() throws Exception {
        // Initialize the database
        insertedTrabajo = trabajoRepository.saveAndFlush(trabajo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trabajo
        Trabajo updatedTrabajo = trabajoRepository.findById(trabajo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrabajo are not directly saved in db
        em.detach(updatedTrabajo);
        updatedTrabajo.tituloTrabajo(UPDATED_TITULO_TRABAJO).salarioMin(UPDATED_SALARIO_MIN).salarioMax(UPDATED_SALARIO_MAX);

        restTrabajoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrabajo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTrabajo))
            )
            .andExpect(status().isOk());

        // Validate the Trabajo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrabajoToMatchAllProperties(updatedTrabajo);
    }

    @Test
    @Transactional
    void putNonExistingTrabajo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(put(ENTITY_API_URL_ID, trabajo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajo)))
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrabajo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrabajo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trabajo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrabajoWithPatch() throws Exception {
        // Initialize the database
        insertedTrabajo = trabajoRepository.saveAndFlush(trabajo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trabajo using partial update
        Trabajo partialUpdatedTrabajo = new Trabajo();
        partialUpdatedTrabajo.setId(trabajo.getId());

        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrabajo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrabajo))
            )
            .andExpect(status().isOk());

        // Validate the Trabajo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrabajoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTrabajo, trabajo), getPersistedTrabajo(trabajo));
    }

    @Test
    @Transactional
    void fullUpdateTrabajoWithPatch() throws Exception {
        // Initialize the database
        insertedTrabajo = trabajoRepository.saveAndFlush(trabajo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trabajo using partial update
        Trabajo partialUpdatedTrabajo = new Trabajo();
        partialUpdatedTrabajo.setId(trabajo.getId());

        partialUpdatedTrabajo.tituloTrabajo(UPDATED_TITULO_TRABAJO).salarioMin(UPDATED_SALARIO_MIN).salarioMax(UPDATED_SALARIO_MAX);

        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrabajo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrabajo))
            )
            .andExpect(status().isOk());

        // Validate the Trabajo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrabajoUpdatableFieldsEquals(partialUpdatedTrabajo, getPersistedTrabajo(partialUpdatedTrabajo));
    }

    @Test
    @Transactional
    void patchNonExistingTrabajo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trabajo.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrabajo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrabajo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trabajo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trabajo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrabajo() throws Exception {
        // Initialize the database
        insertedTrabajo = trabajoRepository.saveAndFlush(trabajo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trabajo
        restTrabajoMockMvc
            .perform(delete(ENTITY_API_URL_ID, trabajo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trabajoRepository.count();
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

    protected Trabajo getPersistedTrabajo(Trabajo trabajo) {
        return trabajoRepository.findById(trabajo.getId()).orElseThrow();
    }

    protected void assertPersistedTrabajoToMatchAllProperties(Trabajo expectedTrabajo) {
        assertTrabajoAllPropertiesEquals(expectedTrabajo, getPersistedTrabajo(expectedTrabajo));
    }

    protected void assertPersistedTrabajoToMatchUpdatableProperties(Trabajo expectedTrabajo) {
        assertTrabajoAllUpdatablePropertiesEquals(expectedTrabajo, getPersistedTrabajo(expectedTrabajo));
    }
}
