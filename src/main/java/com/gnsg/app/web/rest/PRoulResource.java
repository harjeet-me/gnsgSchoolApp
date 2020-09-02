package com.gnsg.app.web.rest;

import com.gnsg.app.domain.PRoul;
import com.gnsg.app.service.PRoulService;
import com.gnsg.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.gnsg.app.domain.PRoul}.
 */
@RestController
@RequestMapping("/api")
public class PRoulResource {

    private final Logger log = LoggerFactory.getLogger(PRoulResource.class);

    private static final String ENTITY_NAME = "pRoul";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PRoulService pRoulService;

    public PRoulResource(PRoulService pRoulService) {
        this.pRoulService = pRoulService;
    }

    /**
     * {@code POST  /p-rouls} : Create a new pRoul.
     *
     * @param pRoul the pRoul to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pRoul, or with status {@code 400 (Bad Request)} if the pRoul has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/p-rouls")
    public ResponseEntity<PRoul> createPRoul(@RequestBody PRoul pRoul) throws URISyntaxException {
        log.debug("REST request to save PRoul : {}", pRoul);
        if (pRoul.getId() != null) {
            throw new BadRequestAlertException("A new pRoul cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PRoul result = pRoulService.save(pRoul);
        return ResponseEntity.created(new URI("/api/p-rouls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /p-rouls} : Updates an existing pRoul.
     *
     * @param pRoul the pRoul to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pRoul,
     * or with status {@code 400 (Bad Request)} if the pRoul is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pRoul couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/p-rouls")
    public ResponseEntity<PRoul> updatePRoul(@RequestBody PRoul pRoul) throws URISyntaxException {
        log.debug("REST request to update PRoul : {}", pRoul);
        if (pRoul.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PRoul result = pRoulService.save(pRoul);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pRoul.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /p-rouls} : get all the pRouls.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pRouls in body.
     */
    @GetMapping("/p-rouls")
    public ResponseEntity<List<PRoul>> getAllPRouls(Pageable pageable) {
        log.debug("REST request to get a page of PRouls");
        Page<PRoul> page = pRoulService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /p-rouls/:id} : get the "id" pRoul.
     *
     * @param id the id of the pRoul to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pRoul, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/p-rouls/{id}")
    public ResponseEntity<PRoul> getPRoul(@PathVariable Long id) {
        log.debug("REST request to get PRoul : {}", id);
        Optional<PRoul> pRoul = pRoulService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pRoul);
    }

    /**
     * {@code DELETE  /p-rouls/:id} : delete the "id" pRoul.
     *
     * @param id the id of the pRoul to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/p-rouls/{id}")
    public ResponseEntity<Void> deletePRoul(@PathVariable Long id) {
        log.debug("REST request to delete PRoul : {}", id);

        pRoulService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/p-rouls?query=:query} : search for the pRoul corresponding
     * to the query.
     *
     * @param query the query of the pRoul search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/p-rouls")
    public ResponseEntity<List<PRoul>> searchPRouls(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PRouls for query {}", query);
        Page<PRoul> page = pRoulService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
