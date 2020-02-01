package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.domain.Developer;
import com.eurofragance.bridge.service.DeveloperService;
import com.eurofragance.bridge.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.eurofragance.bridge.domain.Developer}.
 */
@RestController
@RequestMapping("/api")
public class DeveloperResource {

    private final Logger log = LoggerFactory.getLogger(DeveloperResource.class);

    private static final String ENTITY_NAME = "developer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeveloperService developerService;

    public DeveloperResource(DeveloperService developerService) {
        this.developerService = developerService;
    }

    /**
     * {@code POST  /developers} : Create a new developer.
     *
     * @param developer the developer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new developer, or with status {@code 400 (Bad Request)} if the developer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/developers")
    public ResponseEntity<Developer> createDeveloper(@Valid @RequestBody Developer developer) throws URISyntaxException {
        log.debug("REST request to save Developer : {}", developer);
        if (developer.getId() != null) {
            throw new BadRequestAlertException("A new developer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Developer result = developerService.save(developer);
        return ResponseEntity.created(new URI("/api/developers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /developers} : Updates an existing developer.
     *
     * @param developer the developer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated developer,
     * or with status {@code 400 (Bad Request)} if the developer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the developer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/developers")
    public ResponseEntity<Developer> updateDeveloper(@Valid @RequestBody Developer developer) throws URISyntaxException {
        log.debug("REST request to update Developer : {}", developer);
        if (developer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Developer result = developerService.save(developer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, developer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /developers} : get all the developers.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of developers in body.
     */
    @GetMapping("/developers")
    public List<Developer> getAllDevelopers() {
        log.debug("REST request to get all Developers");
        return developerService.findAll();
    }

    /**
     * {@code GET  /developers/:id} : get the "id" developer.
     *
     * @param id the id of the developer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the developer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/developers/{id}")
    public ResponseEntity<Developer> getDeveloper(@PathVariable Long id) {
        log.debug("REST request to get Developer : {}", id);
        Optional<Developer> developer = developerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(developer);
    }

    /**
     * {@code DELETE  /developers/:id} : delete the "id" developer.
     *
     * @param id the id of the developer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/developers/{id}")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        log.debug("REST request to delete Developer : {}", id);
        developerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/developers?query=:query} : search for the developer corresponding
     * to the query.
     *
     * @param query the query of the developer search.
     * @return the result of the search.
     */
    @GetMapping("/_search/developers")
    public List<Developer> searchDevelopers(@RequestParam String query) {
        log.debug("REST request to search Developers for query {}", query);
        return developerService.search(query);
    }
}
