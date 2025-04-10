package com.udea.lab.service;

import com.udea.lab.domain.Pais;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.udea.lab.domain.Pais}.
 */
public interface PaisService {
    /**
     * Save a pais.
     *
     * @param pais the entity to save.
     * @return the persisted entity.
     */
    Pais save(Pais pais);

    /**
     * Updates a pais.
     *
     * @param pais the entity to update.
     * @return the persisted entity.
     */
    Pais update(Pais pais);

    /**
     * Partially updates a pais.
     *
     * @param pais the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pais> partialUpdate(Pais pais);

    /**
     * Get all the pais.
     *
     * @return the list of entities.
     */
    List<Pais> findAll();

    /**
     * Get all the Pais where Direccion is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Pais> findAllWhereDireccionIsNull();

    /**
     * Get the "id" pais.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pais> findOne(Long id);

    /**
     * Delete the "id" pais.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
