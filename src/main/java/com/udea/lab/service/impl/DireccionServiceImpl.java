package com.udea.lab.service.impl;

import com.udea.lab.domain.Direccion;
import com.udea.lab.repository.DireccionRepository;
import com.udea.lab.service.DireccionService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.udea.lab.domain.Direccion}.
 */
@Service
@Transactional
public class DireccionServiceImpl implements DireccionService {

    private static final Logger LOG = LoggerFactory.getLogger(DireccionServiceImpl.class);

    private final DireccionRepository direccionRepository;

    public DireccionServiceImpl(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    @Override
    public Direccion save(Direccion direccion) {
        LOG.debug("Request to save Direccion : {}", direccion);
        return direccionRepository.save(direccion);
    }

    @Override
    public Direccion update(Direccion direccion) {
        LOG.debug("Request to update Direccion : {}", direccion);
        return direccionRepository.save(direccion);
    }

    @Override
    public Optional<Direccion> partialUpdate(Direccion direccion) {
        LOG.debug("Request to partially update Direccion : {}", direccion);

        return direccionRepository
            .findById(direccion.getId())
            .map(existingDireccion -> {
                if (direccion.getCalle() != null) {
                    existingDireccion.setCalle(direccion.getCalle());
                }
                if (direccion.getCodigoPostal() != null) {
                    existingDireccion.setCodigoPostal(direccion.getCodigoPostal());
                }
                if (direccion.getCiudad() != null) {
                    existingDireccion.setCiudad(direccion.getCiudad());
                }
                if (direccion.getProvincia() != null) {
                    existingDireccion.setProvincia(direccion.getProvincia());
                }

                return existingDireccion;
            })
            .map(direccionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Direccion> findAll() {
        LOG.debug("Request to get all Direccions");
        return direccionRepository.findAll();
    }

    /**
     *  Get all the direccions where Departamento is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Direccion> findAllWhereDepartamentoIsNull() {
        LOG.debug("Request to get all direccions where Departamento is null");
        return StreamSupport.stream(direccionRepository.findAll().spliterator(), false)
            .filter(direccion -> direccion.getDepartamento() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Direccion> findOne(Long id) {
        LOG.debug("Request to get Direccion : {}", id);
        return direccionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Direccion : {}", id);
        direccionRepository.deleteById(id);
    }
}
