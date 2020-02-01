package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.domain.FormulaStatus;
import com.eurofragance.bridge.service.FormulaStatusService;
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
 * REST controller for managing {@link com.eurofragance.bridge.domain.FormulaStatus}.
 */
@RestController
@RequestMapping("/api")
public class FormulaStatusResource {

    private final Logger log = LoggerFactory.getLogger(FormulaStatusResource.class);

    private static final String ENTITY_NAME = "formulaStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormulaStatusService formulaStatusService;

    public FormulaStatusResource(FormulaStatusService formulaStatusService) {
        this.formulaStatusService = formulaStatusService;
    }

    /**
     * {@code POST  /formula-statuses} : Create a new formulaStatus.
     *
     * @param formulaStatus the formulaStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formulaStatus, or with status {@code 400 (Bad Request)} if the formulaStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formula-statuses")
    public ResponseEntity<FormulaStatus> createFormulaStatus(@RequestBody FormulaStatus formulaStatus) throws URISyntaxException {
        log.debug("REST request to save FormulaStatus : {}", formulaStatus);
        if (formulaStatus.getId() != null) {
            throw new BadRequestAlertException("A new formulaStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormulaStatus result = formulaStatusService.save(formulaStatus);
        return ResponseEntity.created(new URI("/api/formula-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formula-statuses} : Updates an existing formulaStatus.
     *
     * @param formulaStatus the formulaStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formulaStatus,
     * or with status {@code 400 (Bad Request)} if the formulaStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formulaStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formula-statuses")
    public ResponseEntity<FormulaStatus> updateFormulaStatus(@RequestBody FormulaStatus formulaStatus) throws URISyntaxException {
        log.debug("REST request to update FormulaStatus : {}", formulaStatus);
        if (formulaStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormulaStatus result = formulaStatusService.save(formulaStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formulaStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /formula-statuses} : get all the formulaStatuses.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formulaStatuses in body.
     */
    @GetMapping("/formula-statuses")
    public List<FormulaStatus> getAllFormulaStatuses() {
        log.debug("REST request to get all FormulaStatuses");
        return formulaStatusService.findAll();
    }

    /**
     * {@code GET  /formula-statuses/:id} : get the "id" formulaStatus.
     *
     * @param id the id of the formulaStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formulaStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formula-statuses/{id}")
    public ResponseEntity<FormulaStatus> getFormulaStatus(@PathVariable Long id) {
        log.debug("REST request to get FormulaStatus : {}", id);
        Optional<FormulaStatus> formulaStatus = formulaStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formulaStatus);
    }

    /**
     * {@code DELETE  /formula-statuses/:id} : delete the "id" formulaStatus.
     *
     * @param id the id of the formulaStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formula-statuses/{id}")
    public ResponseEntity<Void> deleteFormulaStatus(@PathVariable Long id) {
        log.debug("REST request to delete FormulaStatus : {}", id);
        formulaStatusService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/formula-statuses?query=:query} : search for the formulaStatus corresponding
     * to the query.
     *
     * @param query the query of the formulaStatus search.
     * @return the result of the search.
     */
    @GetMapping("/_search/formula-statuses")
    public List<FormulaStatus> searchFormulaStatuses(@RequestParam String query) {
        log.debug("REST request to search FormulaStatuses for query {}", query);
        return formulaStatusService.search(query);
    }
}
