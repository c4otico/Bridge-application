package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.domain.ToMfg;
import com.eurofragance.bridge.service.ToMfgService;
import com.eurofragance.bridge.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.eurofragance.bridge.domain.ToMfg}.
 */
@RestController
@RequestMapping("/api")
public class ToMfgResource {

    private final Logger log = LoggerFactory.getLogger(ToMfgResource.class);

    private static final String ENTITY_NAME = "toMfg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToMfgService toMfgService;

    public ToMfgResource(ToMfgService toMfgService) {
        this.toMfgService = toMfgService;
    }

    /**
     * {@code POST  /to-mfgs} : Create a new toMfg.
     *
     * @param toMfg the toMfg to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toMfg, or with status {@code 400 (Bad Request)} if the toMfg has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/to-mfgs")
    public ResponseEntity<ToMfg> createToMfg(@RequestBody ToMfg toMfg) throws URISyntaxException {
        log.debug("REST request to save ToMfg : {}", toMfg);
        if (toMfg.getId() != null) {
            throw new BadRequestAlertException("A new toMfg cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToMfg result = toMfgService.save(toMfg);
        return ResponseEntity.created(new URI("/api/to-mfgs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /to-mfgs} : Updates an existing toMfg.
     *
     * @param toMfg the toMfg to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toMfg,
     * or with status {@code 400 (Bad Request)} if the toMfg is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toMfg couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/to-mfgs")
    public ResponseEntity<ToMfg> updateToMfg(@RequestBody ToMfg toMfg) throws URISyntaxException {
        log.debug("REST request to update ToMfg : {}", toMfg);
        if (toMfg.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ToMfg result = toMfgService.save(toMfg);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toMfg.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /to-mfgs} : get all the toMfgs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toMfgs in body.
     */
    @GetMapping("/to-mfgs")
    public List<ToMfg> getAllToMfgs() {
        log.debug("REST request to get all ToMfgs");
        return toMfgService.findAll();
    }

    /**
     * {@code GET  /to-mfgs/:id} : get the "id" toMfg.
     *
     * @param id the id of the toMfg to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toMfg, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/to-mfgs/{id}")
    public ResponseEntity<ToMfg> getToMfg(@PathVariable Long id) {
        log.debug("REST request to get ToMfg : {}", id);
        Optional<ToMfg> toMfg = toMfgService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toMfg);
    }

    /**
     * {@code DELETE  /to-mfgs/:id} : delete the "id" toMfg.
     *
     * @param id the id of the toMfg to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/to-mfgs/{id}")
    public ResponseEntity<Void> deleteToMfg(@PathVariable Long id) {
        log.debug("REST request to delete ToMfg : {}", id);
        toMfgService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/to-mfgs?query=:query} : search for the toMfg corresponding
     * to the query.
     *
     * @param query the query of the toMfg search.
     * @return the result of the search.
     */
    @GetMapping("/_search/to-mfgs")
    public List<ToMfg> searchToMfgs(@RequestParam String query) {
        log.debug("REST request to search ToMfgs for query {}", query);
        return toMfgService.search(query);
    }
}
