package com.gnsg.app.web.rest;

import com.gnsg.app.domain.ASPath;
import com.gnsg.app.service.ASPathService;
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
 * REST controller for managing {@link com.gnsg.app.domain.ASPath}.
 */
@RestController
@RequestMapping("/api")
public class ASPathResource {

    private final Logger log = LoggerFactory.getLogger(ASPathResource.class);

    private static final String ENTITY_NAME = "aSPath";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ASPathService aSPathService;

    public ASPathResource(ASPathService aSPathService) {
        this.aSPathService = aSPathService;
    }

    /**
     * {@code POST  /as-paths} : Create a new aSPath.
     *
     * @param aSPath the aSPath to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aSPath, or with status {@code 400 (Bad Request)} if the aSPath has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/as-paths")
    public ResponseEntity<ASPath> createASPath(@RequestBody ASPath aSPath) throws URISyntaxException {
        log.debug("REST request to save ASPath : {}", aSPath);
        if (aSPath.getId() != null) {
            throw new BadRequestAlertException("A new aSPath cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ASPath result = aSPathService.save(aSPath);
        return ResponseEntity.created(new URI("/api/as-paths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /as-paths} : Updates an existing aSPath.
     *
     * @param aSPath the aSPath to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aSPath,
     * or with status {@code 400 (Bad Request)} if the aSPath is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aSPath couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/as-paths")
    public ResponseEntity<ASPath> updateASPath(@RequestBody ASPath aSPath) throws URISyntaxException {
        log.debug("REST request to update ASPath : {}", aSPath);
        if (aSPath.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ASPath result = aSPathService.save(aSPath);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aSPath.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /as-paths} : get all the aSPaths.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aSPaths in body.
     */
    @GetMapping("/as-paths")
    public ResponseEntity<List<ASPath>> getAllASPaths(Pageable pageable) {
        log.debug("REST request to get a page of ASPaths");
        Page<ASPath> page = aSPathService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /as-paths/:id} : get the "id" aSPath.
     *
     * @param id the id of the aSPath to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aSPath, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/as-paths/{id}")
    public ResponseEntity<ASPath> getASPath(@PathVariable Long id) {
        log.debug("REST request to get ASPath : {}", id);
        Optional<ASPath> aSPath = aSPathService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aSPath);
    }

    /**
     * {@code DELETE  /as-paths/:id} : delete the "id" aSPath.
     *
     * @param id the id of the aSPath to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/as-paths/{id}")
    public ResponseEntity<Void> deleteASPath(@PathVariable Long id) {
        log.debug("REST request to delete ASPath : {}", id);

        aSPathService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/as-paths?query=:query} : search for the aSPath corresponding
     * to the query.
     *
     * @param query the query of the aSPath search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/as-paths")
    public ResponseEntity<List<ASPath>> searchASPaths(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ASPaths for query {}", query);
        Page<ASPath> page = aSPathService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
