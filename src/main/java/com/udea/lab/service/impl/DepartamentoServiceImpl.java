package com.udea.lab.service.impl;

import com.udea.lab.domain.Departamento;
import com.udea.lab.repository.DepartamentoRepository;
import com.udea.lab.service.DepartamentoService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.udea.lab.domain.Departamento}.
 */
@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    private static final Logger LOG = LoggerFactory.getLogger(DepartamentoServiceImpl.class);

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Departamento save(Departamento departamento) {
        LOG.debug("Request to save Departamento : {}", departamento);
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento update(Departamento departamento) {
        LOG.debug("Request to update Departamento : {}", departamento);
        return departamentoRepository.save(departamento);
    }

    @Override
    public Optional<Departamento> partialUpdate(Departamento departamento) {
        LOG.debug("Request to partially update Departamento : {}", departamento);

        return departamentoRepository
            .findById(departamento.getId())
            .map(existingDepartamento -> {
                if (departamento.getNombreDepartamento() != null) {
                    existingDepartamento.setNombreDepartamento(departamento.getNombreDepartamento());
                }

                return existingDepartamento;
            })
            .map(departamentoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Departamento> findAll() {
        LOG.debug("Request to get all Departamentos");
        return departamentoRepository.findAll();
    }

    /**
     *  Get all the departamentos where Contrato is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Departamento> findAllWhereContratoIsNull() {
        LOG.debug("Request to get all departamentos where Contrato is null");
        return StreamSupport.stream(departamentoRepository.findAll().spliterator(), false)
            .filter(departamento -> departamento.getContrato() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Departamento> findOne(Long id) {
        LOG.debug("Request to get Departamento : {}", id);
        return departamentoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Departamento : {}", id);
        departamentoRepository.deleteById(id);
    }
}
