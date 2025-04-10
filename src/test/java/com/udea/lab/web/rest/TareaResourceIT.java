package com.udea.lab.web.rest;

import static com.udea.lab.domain.TareaAsserts.*;
import static com.udea.lab.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.lab.IntegrationTest;
import com.udea.lab.domain.Tarea;
import com.udea.lab.repository.TareaRepository;
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
 * Integration tests for the {@link TareaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TareaResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tareas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTareaMockMvc;

    private Tarea tarea;

    private Tarea insertedTarea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createEntity() {
        return new Tarea().titulo(DEFAULT_TITULO).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createUpdatedEntity() {
        return new Tarea().titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        tarea = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTarea != null) {
            tareaRepository.delete(insertedTarea);
            insertedTarea = null;
        }
    }

    @Test
    @Transactional
    void createTarea() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tarea
        var returnedTarea = om.readValue(
            restTareaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarea)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Tarea.class
        );

        // Validate the Tarea in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTareaUpdatableFieldsEquals(returnedTarea, getPersistedTarea(returnedTarea));

        insertedTarea = returnedTarea;
    }

    @Test
    @Transactional
    void createTareaWithExistingId() throws Exception {
        // Create the Tarea with an existing ID
        tarea.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTareaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarea)))
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTareas() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList
        restTareaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarea.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getTarea() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        // Get the tarea
        restTareaMockMvc
            .perform(get(ENTITY_API_URL_ID, tarea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarea.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingTarea() throws Exception {
        // Get the tarea
        restTareaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTarea() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea
        Tarea updatedTarea = tareaRepository.findById(tarea.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTarea are not directly saved in db
        em.detach(updatedTarea);
        updatedTarea.titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION);

        restTareaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTarea.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTareaToMatchAllProperties(updatedTarea);
    }

    @Test
    @Transactional
    void putNonExistingTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(put(ENTITY_API_URL_ID, tarea.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarea)))
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tarea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarea)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTareaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTarea, tarea), getPersistedTarea(tarea));
    }

    @Test
    @Transactional
    void fullUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        partialUpdatedTarea.titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION);

        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTareaUpdatableFieldsEquals(partialUpdatedTarea, getPersistedTarea(partialUpdatedTarea));
    }

    @Test
    @Transactional
    void patchNonExistingTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarea.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tarea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tarea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tarea)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarea() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.saveAndFlush(tarea);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tarea
        restTareaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarea.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tareaRepository.count();
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

    protected Tarea getPersistedTarea(Tarea tarea) {
        return tareaRepository.findById(tarea.getId()).orElseThrow();
    }

    protected void assertPersistedTareaToMatchAllProperties(Tarea expectedTarea) {
        assertTareaAllPropertiesEquals(expectedTarea, getPersistedTarea(expectedTarea));
    }

    protected void assertPersistedTareaToMatchUpdatableProperties(Tarea expectedTarea) {
        assertTareaAllUpdatablePropertiesEquals(expectedTarea, getPersistedTarea(expectedTarea));
    }
}
