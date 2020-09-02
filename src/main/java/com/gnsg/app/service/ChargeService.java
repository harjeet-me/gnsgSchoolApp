package com.gnsg.app.service;

import com.gnsg.app.domain.Charge;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Charge}.
 */
public interface ChargeService {

    /**
     * Save a charge.
     *
     * @param charge the entity to save.
     * @return the persisted entity.
     */
    Charge save(Charge charge);

    /**
     * Get all the charges.
     *
     * @return the list of entities.
     */
    List<Charge> findAll();


    /**
     * Get the "id" charge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Charge> findOne(Long id);

    /**
     * Delete the "id" charge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the charge corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Charge> search(String query);
}
