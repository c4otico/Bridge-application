package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.domain.FormulaReg;
import com.eurofragance.bridge.service.FormulaRegService;
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
 * REST controller for managing {@link com.eurofragance.bridge.domain.FormulaReg}.
 */
@RestController
@RequestMapping("/api")
public class FormulaRegResource {

    private final Logger log = LoggerFactory.getLogger(FormulaRegResource.class);

    private static final String ENTITY_NAME = "formulaReg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormulaRegService formulaRegService;

    public FormulaRegResource(FormulaRegService formulaRegService) {
        this.formulaRegService = formulaRegService;
    }

    /**
     * {@code POST  /formula-regs} : Create a new formulaReg.
     *
     * @param formulaReg the formulaReg to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formulaReg, or with status {@code 400 (Bad Request)} if the formulaReg has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formula-regs")
    public ResponseEntity<FormulaReg> createFormulaReg(@RequestBody FormulaReg formulaReg) throws URISyntaxException {
        log.debug("REST request to save FormulaReg : {}", formulaReg);
        if (formulaReg.getId() != null) {
            throw new BadRequestAlertException("A new formulaReg cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormulaReg result = formulaRegService.save(formulaReg);
        return ResponseEntity.created(new URI("/api/formula-regs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formula-regs} : Updates an existing formulaReg.
     *
     * @param formulaReg the formulaReg to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formulaReg,
     * or with status {@code 400 (Bad Request)} if the formulaReg is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formulaReg couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formula-regs")
    public ResponseEntity<FormulaReg> updateFormulaReg(@RequestBody FormulaReg formulaReg) throws URISyntaxException {
        log.debug("REST request to update FormulaReg : {}", formulaReg);
        if (formulaReg.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormulaReg result = formulaRegService.save(formulaReg);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formulaReg.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /formula-regs} : get all the formulaRegs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formulaRegs in body.
     */
    @GetMapping("/formula-regs")
    public List<FormulaReg> getAllFormulaRegs() {
        log.debug("REST request to get all FormulaRegs");
        return formulaRegService.findAll();
    }

    /**
     * {@code GET  /formula-regs/:id} : get the "id" formulaReg.
     *
     * @param id the id of the formulaReg to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formulaReg, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formula-regs/{id}")
    public ResponseEntity<FormulaReg> getFormulaReg(@PathVariable Long id) {
        log.debug("REST request to get FormulaReg : {}", id);
        Optional<FormulaReg> formulaReg = formulaRegService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formulaReg);
    }

    /**
     * {@code DELETE  /formula-regs/:id} : delete the "id" formulaReg.
     *
     * @param id the id of the formulaReg to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formula-regs/{id}")
    public ResponseEntity<Void> deleteFormulaReg(@PathVariable Long id) {
        log.debug("REST request to delete FormulaReg : {}", id);
        formulaRegService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/formula-regs?query=:query} : search for the formulaReg corresponding
     * to the query.
     *
     * @param query the query of the formulaReg search.
     * @return the result of the search.
     */
    @GetMapping("/_search/formula-regs")
    public List<FormulaReg> searchFormulaRegs(@RequestParam String query) {
        log.debug("REST request to search FormulaRegs for query {}", query);
        return formulaRegService.search(query);
    }
}
