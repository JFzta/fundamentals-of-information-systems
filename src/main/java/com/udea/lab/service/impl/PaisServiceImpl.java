package com.udea.lab.service.impl;

import com.udea.lab.domain.Pais;
import com.udea.lab.repository.PaisRepository;
import com.udea.lab.service.PaisService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.udea.lab.domain.Pais}.
 */
@Service
@Transactional
public class PaisServiceImpl implements PaisService {

    private static final Logger LOG = LoggerFactory.getLogger(PaisServiceImpl.class);

    private final PaisRepository paisRepository;

    public PaisServiceImpl(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public Pais save(Pais pais) {
        LOG.debug("Request to save Pais : {}", pais);
        return paisRepository.save(pais);
    }

    @Override
    public Pais update(Pais pais) {
        LOG.debug("Request to update Pais : {}", pais);
        return paisRepository.save(pais);
    }

    @Override
    public Optional<Pais> partialUpdate(Pais pais) {
        LOG.debug("Request to partially update Pais : {}", pais);

        return paisRepository
            .findById(pais.getId())
            .map(existingPais -> {
                if (pais.getNombrePais() != null) {
                    existingPais.setNombrePais(pais.getNombrePais());
                }

                return existingPais;
            })
            .map(paisRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pais> findAll() {
        LOG.debug("Request to get all Pais");
        return paisRepository.findAll();
    }

    /**
     *  Get all the pais where Direccion is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Pais> findAllWhereDireccionIsNull() {
        LOG.debug("Request to get all pais where Direccion is null");
        return StreamSupport.stream(paisRepository.findAll().spliterator(), false).filter(pais -> pais.getDireccion() == null).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pais> findOne(Long id) {
        LOG.debug("Request to get Pais : {}", id);
        return paisRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Pais : {}", id);
        paisRepository.deleteById(id);
    }
}
