package com.gnsg.app.service.impl;

import com.gnsg.app.service.SevadarService;
import com.gnsg.app.domain.Sevadar;
import com.gnsg.app.repository.SevadarRepository;
import com.gnsg.app.repository.search.SevadarSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Sevadar}.
 */
@Service
@Transactional
public class SevadarServiceImpl implements SevadarService {

    private final Logger log = LoggerFactory.getLogger(SevadarServiceImpl.class);

    private final SevadarRepository sevadarRepository;

    private final SevadarSearchRepository sevadarSearchRepository;

    public SevadarServiceImpl(SevadarRepository sevadarRepository, SevadarSearchRepository sevadarSearchRepository) {
        this.sevadarRepository = sevadarRepository;
        this.sevadarSearchRepository = sevadarSearchRepository;
    }

    /**
     * Save a sevadar.
     *
     * @param sevadar the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Sevadar save(Sevadar sevadar) {
        log.debug("Request to save Sevadar : {}", sevadar);
        Sevadar result = sevadarRepository.save(sevadar);
        sevadarSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the sevadars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sevadar> findAll(Pageable pageable) {
        log.debug("Request to get all Sevadars");
        return sevadarRepository.findAll(pageable);
    }


    /**
     * Get one sevadar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Sevadar> findOne(Long id) {
        log.debug("Request to get Sevadar : {}", id);
        return sevadarRepository.findById(id);
    }

    /**
     * Delete the sevadar by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sevadar : {}", id);

        sevadarRepository.deleteById(id);
        sevadarSearchRepository.deleteById(id);
    }

    /**
     * Search for the sevadar corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sevadar> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sevadars for query {}", query);
        return sevadarSearchRepository.search(queryStringQuery(query), pageable);    }
}
