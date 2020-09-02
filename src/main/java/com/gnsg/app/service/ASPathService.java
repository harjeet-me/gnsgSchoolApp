package com.gnsg.app.service;

import com.gnsg.app.domain.ASPath;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ASPath}.
 */
public interface ASPathService {

    /**
     * Save a aSPath.
     *
     * @param aSPath the entity to save.
     * @return the persisted entity.
     */
    ASPath save(ASPath aSPath);

    /**
     * Get all the aSPaths.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ASPath> findAll(Pageable pageable);


    /**
     * Get the "id" aSPath.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ASPath> findOne(Long id);

    /**
     * Delete the "id" aSPath.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the aSPath corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ASPath> search(String query, Pageable pageable);
}
