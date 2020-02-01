package com.eurofragance.bridge.service;

import com.eurofragance.bridge.domain.FormulaItems;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link FormulaItems}.
 */
public interface FormulaItemsService {

    /**
     * Save a formulaItems.
     *
     * @param formulaItems the entity to save.
     * @return the persisted entity.
     */
    FormulaItems save(FormulaItems formulaItems);

    /**
     * Get all the formulaItems.
     *
     * @return the list of entities.
     */
    List<FormulaItems> findAll();


    /**
     * Get the "id" formulaItems.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormulaItems> findOne(Long id);

    /**
     * Delete the "id" formulaItems.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the formulaItems corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<FormulaItems> search(String query);
}
