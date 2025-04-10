package com.udea.lab.repository;

import com.udea.lab.domain.Trabajo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TrabajoRepositoryWithBagRelationshipsImpl implements TrabajoRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String TRABAJOS_PARAMETER = "trabajos";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Trabajo> fetchBagRelationships(Optional<Trabajo> trabajo) {
        return trabajo.map(this::fetchTareas);
    }

    @Override
    public Page<Trabajo> fetchBagRelationships(Page<Trabajo> trabajos) {
        return new PageImpl<>(fetchBagRelationships(trabajos.getContent()), trabajos.getPageable(), trabajos.getTotalElements());
    }

    @Override
    public List<Trabajo> fetchBagRelationships(List<Trabajo> trabajos) {
        return Optional.of(trabajos).map(this::fetchTareas).orElse(Collections.emptyList());
    }

    Trabajo fetchTareas(Trabajo result) {
        return entityManager
            .createQuery("select trabajo from Trabajo trabajo left join fetch trabajo.tareas where trabajo.id = :id", Trabajo.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Trabajo> fetchTareas(List<Trabajo> trabajos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, trabajos.size()).forEach(index -> order.put(trabajos.get(index).getId(), index));
        List<Trabajo> result = entityManager
            .createQuery("select trabajo from Trabajo trabajo left join fetch trabajo.tareas where trabajo in :trabajos", Trabajo.class)
            .setParameter(TRABAJOS_PARAMETER, trabajos)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
