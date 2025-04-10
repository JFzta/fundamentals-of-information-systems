package com.udea.lab.service.impl;

import com.udea.lab.domain.Contrato;
import com.udea.lab.repository.ContratoRepository;
import com.udea.lab.service.ContratoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.udea.lab.domain.Contrato}.
 */
@Service
@Transactional
public class ContratoServiceImpl implements ContratoService {

    private static final Logger LOG = LoggerFactory.getLogger(ContratoServiceImpl.class);

    private final ContratoRepository contratoRepository;

    public ContratoServiceImpl(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    @Override
    public Contrato save(Contrato contrato) {
        LOG.debug("Request to save Contrato : {}", contrato);
        return contratoRepository.save(contrato);
    }

    @Override
    public Contrato update(Contrato contrato) {
        LOG.debug("Request to update Contrato : {}", contrato);
        return contratoRepository.save(contrato);
    }

    @Override
    public Optional<Contrato> partialUpdate(Contrato contrato) {
        LOG.debug("Request to partially update Contrato : {}", contrato);

        return contratoRepository
            .findById(contrato.getId())
            .map(existingContrato -> {
                if (contrato.getFechaInicio() != null) {
                    existingContrato.setFechaInicio(contrato.getFechaInicio());
                }
                if (contrato.getFechaFin() != null) {
                    existingContrato.setFechaFin(contrato.getFechaFin());
                }
                if (contrato.getLenguaje() != null) {
                    existingContrato.setLenguaje(contrato.getLenguaje());
                }

                return existingContrato;
            })
            .map(contratoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contrato> findAll(Pageable pageable) {
        LOG.debug("Request to get all Contratoes");
        return contratoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contrato> findOne(Long id) {
        LOG.debug("Request to get Contrato : {}", id);
        return contratoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Contrato : {}", id);
        contratoRepository.deleteById(id);
    }
}
