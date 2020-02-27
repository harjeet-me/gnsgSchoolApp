package com.gnsg.app.service.impl;

import com.gnsg.app.service.ChargeService;
import com.gnsg.app.domain.Charge;
import com.gnsg.app.repository.ChargeRepository;
import com.gnsg.app.repository.search.ChargeSearchRepository;
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
 * Service Implementation for managing {@link Charge}.
 */
@Service
@Transactional
public class ChargeServiceImpl implements ChargeService {

    private final Logger log = LoggerFactory.getLogger(ChargeServiceImpl.class);

    private final ChargeRepository chargeRepository;

    private final ChargeSearchRepository chargeSearchRepository;

    public ChargeServiceImpl(ChargeRepository chargeRepository, ChargeSearchRepository chargeSearchRepository) {
        this.chargeRepository = chargeRepository;
        this.chargeSearchRepository = chargeSearchRepository;
    }

    /**
     * Save a charge.
     *
     * @param charge the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Charge save(Charge charge) {
        log.debug("Request to save Charge : {}", charge);
        Charge result = chargeRepository.save(charge);
        chargeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the charges.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Charge> findAll() {
        log.debug("Request to get all Charges");
        return chargeRepository.findAll();
    }

    /**
     * Get one charge by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Charge> findOne(Long id) {
        log.debug("Request to get Charge : {}", id);
        return chargeRepository.findById(id);
    }

    /**
     * Delete the charge by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Charge : {}", id);
        chargeRepository.deleteById(id);
        chargeSearchRepository.deleteById(id);
    }

    /**
     * Search for the charge corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Charge> search(String query) {
        log.debug("Request to search Charges for query {}", query);
        return StreamSupport
            .stream(chargeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
