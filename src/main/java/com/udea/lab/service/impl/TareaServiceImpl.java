package com.udea.lab.service.impl;

import com.udea.lab.domain.Tarea;
import com.udea.lab.repository.TareaRepository;
import com.udea.lab.service.TareaService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.udea.lab.domain.Tarea}.
 */
@Service
@Transactional
public class TareaServiceImpl implements TareaService {

    private static final Logger LOG = LoggerFactory.getLogger(TareaServiceImpl.class);

    private final TareaRepository tareaRepository;

    public TareaServiceImpl(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public Tarea save(Tarea tarea) {
        LOG.debug("Request to save Tarea : {}", tarea);
        return tareaRepository.save(tarea);
    }

    @Override
    public Tarea update(Tarea tarea) {
        LOG.debug("Request to update Tarea : {}", tarea);
        return tareaRepository.save(tarea);
    }

    @Override
    public Optional<Tarea> partialUpdate(Tarea tarea) {
        LOG.debug("Request to partially update Tarea : {}", tarea);

        return tareaRepository
            .findById(tarea.getId())
            .map(existingTarea -> {
                if (tarea.getTitulo() != null) {
                    existingTarea.setTitulo(tarea.getTitulo());
                }
                if (tarea.getDescripcion() != null) {
                    existingTarea.setDescripcion(tarea.getDescripcion());
                }

                return existingTarea;
            })
            .map(tareaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tarea> findAll() {
        LOG.debug("Request to get all Tareas");
        return tareaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tarea> findOne(Long id) {
        LOG.debug("Request to get Tarea : {}", id);
        return tareaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Tarea : {}", id);
        tareaRepository.deleteById(id);
    }
}
