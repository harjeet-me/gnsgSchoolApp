package com.gnsg.app.service.impl;

import com.gnsg.app.service.AppliedChargeService;
import com.gnsg.app.domain.AppliedCharge;
import com.gnsg.app.repository.AppliedChargeRepository;
import com.gnsg.app.repository.search.AppliedChargeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AppliedCharge}.
 */
@Service
@Transactional
public class AppliedChargeServiceImpl implements AppliedChargeService {

    private final Logger log = LoggerFactory.getLogger(AppliedChargeServiceImpl.class);

    private final AppliedChargeRepository appliedChargeRepository;

    private final AppliedChargeSearchRepository appliedChargeSearchRepository;

    public AppliedChargeServiceImpl(AppliedChargeRepository appliedChargeRepository, AppliedChargeSearchRepository appliedChargeSearchRepository) {
        this.appliedChargeRepository = appliedChargeRepository;
        this.appliedChargeSearchRepository = appliedChargeSearchRepository;
    }

    /**
     * Save a appliedCharge.
     *
     * @param appliedCharge the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AppliedCharge save(AppliedCharge appliedCharge) {
        log.debug("Request to save AppliedCharge : {}", appliedCharge);
        AppliedCharge result = appliedChargeRepository.save(appliedCharge);
        appliedChargeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the appliedCharges.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AppliedCharge> findAll() {
        log.debug("Request to get all AppliedCharges");
        return appliedChargeRepository.findAll();
    }

    /**
     * Get one appliedCharge by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AppliedCharge> findOne(Long id) {
        log.debug("Request to get AppliedCharge : {}", id);
        return appliedChargeRepository.findById(id);
    }

    /**
     * Delete the appliedCharge by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppliedCharge : {}", id);
        appliedChargeRepository.deleteById(id);
        appliedChargeSearchRepository.deleteById(id);
    }

    /**
     * Search for the appliedCharge corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AppliedCharge> search(String query) {
        log.debug("Request to search AppliedCharges for query {}", query);
        return StreamSupport
            .stream(appliedChargeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
