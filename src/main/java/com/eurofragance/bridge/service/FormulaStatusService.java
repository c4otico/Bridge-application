package com.eurofragance.bridge.service;

import com.eurofragance.bridge.domain.FormulaStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link FormulaStatus}.
 */
public interface FormulaStatusService {

    /**
     * Save a formulaStatus.
     *
     * @param formulaStatus the entity to save.
     * @return the persisted entity.
     */
    FormulaStatus save(FormulaStatus formulaStatus);

    /**
     * Get all the formulaStatuses.
     *
     * @return the list of entities.
     */
    List<FormulaStatus> findAll();


    /**
     * Get the "id" formulaStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormulaStatus> findOne(Long id);

    /**
     * Delete the "id" formulaStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the formulaStatus corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<FormulaStatus> search(String query);
}
