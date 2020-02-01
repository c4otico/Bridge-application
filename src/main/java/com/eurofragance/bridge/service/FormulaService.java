package com.eurofragance.bridge.service;

import com.eurofragance.bridge.domain.Formula;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Formula}.
 */
public interface FormulaService {

    /**
     * Save a formula.
     *
     * @param formula the entity to save.
     * @return the persisted entity.
     */
    Formula save(Formula formula);

    /**
     * Get all the formulas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Formula> findAll(Pageable pageable);


    /**
     * Get the "id" formula.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Formula> findOne(Long id);

    /**
     * Delete the "id" formula.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the formula corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Formula> search(String query, Pageable pageable);
}
