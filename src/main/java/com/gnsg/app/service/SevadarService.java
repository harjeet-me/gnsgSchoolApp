package com.gnsg.app.service;

import com.gnsg.app.domain.Sevadar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Sevadar}.
 */
public interface SevadarService {

    /**
     * Save a sevadar.
     *
     * @param sevadar the entity to save.
     * @return the persisted entity.
     */
    Sevadar save(Sevadar sevadar);

    /**
     * Get all the sevadars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sevadar> findAll(Pageable pageable);


    /**
     * Get the "id" sevadar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sevadar> findOne(Long id);

    /**
     * Delete the "id" sevadar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the sevadar corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sevadar> search(String query, Pageable pageable);
}
