package com.gnsg.app.service.impl;

import com.gnsg.app.service.ASPathService;
import com.gnsg.app.domain.ASPath;
import com.gnsg.app.repository.ASPathRepository;
import com.gnsg.app.repository.search.ASPathSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ASPath}.
 */
@Service
@Transactional
public class ASPathServiceImpl implements ASPathService {

    private final Logger log = LoggerFactory.getLogger(ASPathServiceImpl.class);

    private final ASPathRepository aSPathRepository;

    private final ASPathSearchRepository aSPathSearchRepository;

    public ASPathServiceImpl(ASPathRepository aSPathRepository, ASPathSearchRepository aSPathSearchRepository) {
        this.aSPathRepository = aSPathRepository;
        this.aSPathSearchRepository = aSPathSearchRepository;
    }

    /**
     * Save a aSPath.
     *
     * @param aSPath the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ASPath save(ASPath aSPath) {
        log.debug("Request to save ASPath : {}", aSPath);
        ASPath result = aSPathRepository.save(aSPath);
        aSPathSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the aSPaths.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ASPath> findAll(Pageable pageable) {
        log.debug("Request to get all ASPaths");
        return aSPathRepository.findAll(pageable);
    }


    /**
     * Get one aSPath by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ASPath> findOne(Long id) {
        log.debug("Request to get ASPath : {}", id);
        return aSPathRepository.findById(id);
    }

    /**
     * Delete the aSPath by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ASPath : {}", id);

        aSPathRepository.deleteById(id);
        aSPathSearchRepository.deleteById(id);
    }

    /**
     * Search for the aSPath corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ASPath> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ASPaths for query {}", query);
        return aSPathSearchRepository.search(queryStringQuery(query), pageable);    }
}
