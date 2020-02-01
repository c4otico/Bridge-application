package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.domain.FormulaItems;
import com.eurofragance.bridge.service.FormulaItemsService;
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
 * REST controller for managing {@link com.eurofragance.bridge.domain.FormulaItems}.
 */
@RestController
@RequestMapping("/api")
public class FormulaItemsResource {

    private final Logger log = LoggerFactory.getLogger(FormulaItemsResource.class);

    private static final String ENTITY_NAME = "formulaItems";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormulaItemsService formulaItemsService;

    public FormulaItemsResource(FormulaItemsService formulaItemsService) {
        this.formulaItemsService = formulaItemsService;
    }

    /**
     * {@code POST  /formula-items} : Create a new formulaItems.
     *
     * @param formulaItems the formulaItems to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formulaItems, or with status {@code 400 (Bad Request)} if the formulaItems has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formula-items")
    public ResponseEntity<FormulaItems> createFormulaItems(@RequestBody FormulaItems formulaItems) throws URISyntaxException {
        log.debug("REST request to save FormulaItems : {}", formulaItems);
        if (formulaItems.getId() != null) {
            throw new BadRequestAlertException("A new formulaItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormulaItems result = formulaItemsService.save(formulaItems);
        return ResponseEntity.created(new URI("/api/formula-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formula-items} : Updates an existing formulaItems.
     *
     * @param formulaItems the formulaItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formulaItems,
     * or with status {@code 400 (Bad Request)} if the formulaItems is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formulaItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formula-items")
    public ResponseEntity<FormulaItems> updateFormulaItems(@RequestBody FormulaItems formulaItems) throws URISyntaxException {
        log.debug("REST request to update FormulaItems : {}", formulaItems);
        if (formulaItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormulaItems result = formulaItemsService.save(formulaItems);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formulaItems.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /formula-items} : get all the formulaItems.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formulaItems in body.
     */
    @GetMapping("/formula-items")
    public List<FormulaItems> getAllFormulaItems() {
        log.debug("REST request to get all FormulaItems");
        return formulaItemsService.findAll();
    }

    /**
     * {@code GET  /formula-items/:id} : get the "id" formulaItems.
     *
     * @param id the id of the formulaItems to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formulaItems, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formula-items/{id}")
    public ResponseEntity<FormulaItems> getFormulaItems(@PathVariable Long id) {
        log.debug("REST request to get FormulaItems : {}", id);
        Optional<FormulaItems> formulaItems = formulaItemsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formulaItems);
    }

    /**
     * {@code DELETE  /formula-items/:id} : delete the "id" formulaItems.
     *
     * @param id the id of the formulaItems to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formula-items/{id}")
    public ResponseEntity<Void> deleteFormulaItems(@PathVariable Long id) {
        log.debug("REST request to delete FormulaItems : {}", id);
        formulaItemsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/formula-items?query=:query} : search for the formulaItems corresponding
     * to the query.
     *
     * @param query the query of the formulaItems search.
     * @return the result of the search.
     */
    @GetMapping("/_search/formula-items")
    public List<FormulaItems> searchFormulaItems(@RequestParam String query) {
        log.debug("REST request to search FormulaItems for query {}", query);
        return formulaItemsService.search(query);
    }
}
