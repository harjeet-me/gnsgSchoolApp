package com.gnsg.app.web.rest;

import com.gnsg.app.domain.AppliedCharge;
import com.gnsg.app.service.AppliedChargeService;
import com.gnsg.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.gnsg.app.domain.AppliedCharge}.
 */
@RestController
@RequestMapping("/api")
public class AppliedChargeResource {

    private final Logger log = LoggerFactory.getLogger(AppliedChargeResource.class);

    private static final String ENTITY_NAME = "appliedCharge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppliedChargeService appliedChargeService;

    public AppliedChargeResource(AppliedChargeService appliedChargeService) {
        this.appliedChargeService = appliedChargeService;
    }

    /**
     * {@code POST  /applied-charges} : Create a new appliedCharge.
     *
     * @param appliedCharge the appliedCharge to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appliedCharge, or with status {@code 400 (Bad Request)} if the appliedCharge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/applied-charges")
    public ResponseEntity<AppliedCharge> createAppliedCharge(@RequestBody AppliedCharge appliedCharge) throws URISyntaxException {
        log.debug("REST request to save AppliedCharge : {}", appliedCharge);
        if (appliedCharge.getId() != null) {
            throw new BadRequestAlertException("A new appliedCharge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppliedCharge result = appliedChargeService.save(appliedCharge);
        return ResponseEntity.created(new URI("/api/applied-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /applied-charges} : Updates an existing appliedCharge.
     *
     * @param appliedCharge the appliedCharge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appliedCharge,
     * or with status {@code 400 (Bad Request)} if the appliedCharge is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appliedCharge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/applied-charges")
    public ResponseEntity<AppliedCharge> updateAppliedCharge(@RequestBody AppliedCharge appliedCharge) throws URISyntaxException {
        log.debug("REST request to update AppliedCharge : {}", appliedCharge);
        if (appliedCharge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AppliedCharge result = appliedChargeService.save(appliedCharge);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appliedCharge.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /applied-charges} : get all the appliedCharges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appliedCharges in body.
     */
    @GetMapping("/applied-charges")
    public List<AppliedCharge> getAllAppliedCharges() {
        log.debug("REST request to get all AppliedCharges");
        return appliedChargeService.findAll();
    }

    /**
     * {@code GET  /applied-charges/:id} : get the "id" appliedCharge.
     *
     * @param id the id of the appliedCharge to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appliedCharge, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/applied-charges/{id}")
    public ResponseEntity<AppliedCharge> getAppliedCharge(@PathVariable Long id) {
        log.debug("REST request to get AppliedCharge : {}", id);
        Optional<AppliedCharge> appliedCharge = appliedChargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appliedCharge);
    }

    /**
     * {@code DELETE  /applied-charges/:id} : delete the "id" appliedCharge.
     *
     * @param id the id of the appliedCharge to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/applied-charges/{id}")
    public ResponseEntity<Void> deleteAppliedCharge(@PathVariable Long id) {
        log.debug("REST request to delete AppliedCharge : {}", id);
        appliedChargeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/applied-charges?query=:query} : search for the appliedCharge corresponding
     * to the query.
     *
     * @param query the query of the appliedCharge search.
     * @return the result of the search.
     */
    @GetMapping("/_search/applied-charges")
    public List<AppliedCharge> searchAppliedCharges(@RequestParam String query) {
        log.debug("REST request to search AppliedCharges for query {}", query);
        return appliedChargeService.search(query);
    }
}
