package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.domain.Formula;
import com.eurofragance.bridge.service.FormulaService;
import com.eurofragance.bridge.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.eurofragance.bridge.domain.Formula}.
 */
@RestController
@RequestMapping("/api")
public class FormulaResource {

    private final Logger log = LoggerFactory.getLogger(FormulaResource.class);

    private static final String ENTITY_NAME = "formula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormulaService formulaService;

    public FormulaResource(FormulaService formulaService) {
        this.formulaService = formulaService;
    }

    /**
     * {@code POST  /formulas} : Create a new formula.
     *
     * @param formula the formula to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formula, or with status {@code 400 (Bad Request)} if the formula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formulas")
    public ResponseEntity<Formula> createFormula(@RequestBody Formula formula) throws URISyntaxException {
        log.debug("REST request to save Formula : {}", formula);
        if (formula.getId() != null) {
            throw new BadRequestAlertException("A new formula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Formula result = formulaService.save(formula);
        return ResponseEntity.created(new URI("/api/formulas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formulas} : Updates an existing formula.
     *
     * @param formula the formula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formula,
     * or with status {@code 400 (Bad Request)} if the formula is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formulas")
    public ResponseEntity<Formula> updateFormula(@RequestBody Formula formula) throws URISyntaxException {
        log.debug("REST request to update Formula : {}", formula);
        if (formula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Formula result = formulaService.save(formula);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formula.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /formulas} : get all the formulas.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formulas in body.
     */
    @GetMapping("/formulas")
    public ResponseEntity<List<Formula>> getAllFormulas(Pageable pageable) {
        log.debug("REST request to get a page of Formulas");
        Page<Formula> page = formulaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formulas/:id} : get the "id" formula.
     *
     * @param id the id of the formula to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formula, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formulas/{id}")
    public ResponseEntity<Formula> getFormula(@PathVariable Long id) {
        log.debug("REST request to get Formula : {}", id);
        Optional<Formula> formula = formulaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formula);
    }

    /**
     * {@code DELETE  /formulas/:id} : delete the "id" formula.
     *
     * @param id the id of the formula to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formulas/{id}")
    public ResponseEntity<Void> deleteFormula(@PathVariable Long id) {
        log.debug("REST request to delete Formula : {}", id);
        formulaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/formulas?query=:query} : search for the formula corresponding
     * to the query.
     *
     * @param query the query of the formula search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/formulas")
    public ResponseEntity<List<Formula>> searchFormulas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Formulas for query {}", query);
        Page<Formula> page = formulaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
