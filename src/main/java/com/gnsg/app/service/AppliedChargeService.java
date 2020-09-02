package com.gnsg.app.service;

import com.gnsg.app.domain.AppliedCharge;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link AppliedCharge}.
 */
public interface AppliedChargeService {

    /**
     * Save a appliedCharge.
     *
     * @param appliedCharge the entity to save.
     * @return the persisted entity.
     */
    AppliedCharge save(AppliedCharge appliedCharge);

    /**
     * Get all the appliedCharges.
     *
     * @return the list of entities.
     */
    List<AppliedCharge> findAll();


    /**
     * Get the "id" appliedCharge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppliedCharge> findOne(Long id);

    /**
     * Delete the "id" appliedCharge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the appliedCharge corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<AppliedCharge> search(String query);
}
