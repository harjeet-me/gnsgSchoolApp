package com.gnsg.app.web.rest;

import com.gnsg.app.domain.Sevadar;
import com.gnsg.app.service.SevadarService;
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
 * REST controller for managing {@link com.gnsg.app.domain.Sevadar}.
 */
@RestController
@RequestMapping("/api")
public class SevadarResource {

    private final Logger log = LoggerFactory.getLogger(SevadarResource.class);

    private static final String ENTITY_NAME = "sevadar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SevadarService sevadarService;

    public SevadarResource(SevadarService sevadarService) {
        this.sevadarService = sevadarService;
    }

    /**
     * {@code POST  /sevadars} : Create a new sevadar.
     *
     * @param sevadar the sevadar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sevadar, or with status {@code 400 (Bad Request)} if the sevadar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sevadars")
    public ResponseEntity<Sevadar> createSevadar(@RequestBody Sevadar sevadar) throws URISyntaxException {
        log.debug("REST request to save Sevadar : {}", sevadar);
        if (sevadar.getId() != null) {
            throw new BadRequestAlertException("A new sevadar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sevadar result = sevadarService.save(sevadar);
        return ResponseEntity.created(new URI("/api/sevadars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sevadars} : Updates an existing sevadar.
     *
     * @param sevadar the sevadar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sevadar,
     * or with status {@code 400 (Bad Request)} if the sevadar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sevadar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sevadars")
    public ResponseEntity<Sevadar> updateSevadar(@RequestBody Sevadar sevadar) throws URISyntaxException {
        log.debug("REST request to update Sevadar : {}", sevadar);
        if (sevadar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sevadar result = sevadarService.save(sevadar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sevadar.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sevadars} : get all the sevadars.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sevadars in body.
     */
    @GetMapping("/sevadars")
    public ResponseEntity<List<Sevadar>> getAllSevadars(Pageable pageable) {
        log.debug("REST request to get a page of Sevadars");
        Page<Sevadar> page = sevadarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sevadars/:id} : get the "id" sevadar.
     *
     * @param id the id of the sevadar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sevadar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sevadars/{id}")
    public ResponseEntity<Sevadar> getSevadar(@PathVariable Long id) {
        log.debug("REST request to get Sevadar : {}", id);
        Optional<Sevadar> sevadar = sevadarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sevadar);
    }

    /**
     * {@code DELETE  /sevadars/:id} : delete the "id" sevadar.
     *
     * @param id the id of the sevadar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sevadars/{id}")
    public ResponseEntity<Void> deleteSevadar(@PathVariable Long id) {
        log.debug("REST request to delete Sevadar : {}", id);

        sevadarService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/sevadars?query=:query} : search for the sevadar corresponding
     * to the query.
     *
     * @param query the query of the sevadar search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/sevadars")
    public ResponseEntity<List<Sevadar>> searchSevadars(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Sevadars for query {}", query);
        Page<Sevadar> page = sevadarService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
