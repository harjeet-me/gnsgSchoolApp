package com.gnsg.app.service.impl;

import com.gnsg.app.service.PRoulService;
import com.gnsg.app.domain.PRoul;
import com.gnsg.app.repository.PRoulRepository;
import com.gnsg.app.repository.search.PRoulSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PRoul}.
 */
@Service
@Transactional
public class PRoulServiceImpl implements PRoulService {

    private final Logger log = LoggerFactory.getLogger(PRoulServiceImpl.class);

    private final PRoulRepository pRoulRepository;

    private final PRoulSearchRepository pRoulSearchRepository;

    public PRoulServiceImpl(PRoulRepository pRoulRepository, PRoulSearchRepository pRoulSearchRepository) {
        this.pRoulRepository = pRoulRepository;
        this.pRoulSearchRepository = pRoulSearchRepository;
    }

    /**
     * Save a pRoul.
     *
     * @param pRoul the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PRoul save(PRoul pRoul) {
        log.debug("Request to save PRoul : {}", pRoul);
        PRoul result = pRoulRepository.save(pRoul);
        pRoulSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the pRouls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PRoul> findAll(Pageable pageable) {
        log.debug("Request to get all PRouls");
        return pRoulRepository.findAll(pageable);
    }


    /**
     * Get one pRoul by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PRoul> findOne(Long id) {
        log.debug("Request to get PRoul : {}", id);
        return pRoulRepository.findById(id);
    }

    /**
     * Delete the pRoul by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PRoul : {}", id);

        pRoulRepository.deleteById(id);
        pRoulSearchRepository.deleteById(id);
    }

    /**
     * Search for the pRoul corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PRoul> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PRouls for query {}", query);
        return pRoulSearchRepository.search(queryStringQuery(query), pageable);    }
}
