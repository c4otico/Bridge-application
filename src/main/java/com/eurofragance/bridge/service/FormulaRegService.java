package com.eurofragance.bridge.service;

import com.eurofragance.bridge.domain.FormulaReg;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link FormulaReg}.
 */
public interface FormulaRegService {

    /**
     * Save a formulaReg.
     *
     * @param formulaReg the entity to save.
     * @return the persisted entity.
     */
    FormulaReg save(FormulaReg formulaReg);

    /**
     * Get all the formulaRegs.
     *
     * @return the list of entities.
     */
    List<FormulaReg> findAll();


    /**
     * Get the "id" formulaReg.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormulaReg> findOne(Long id);

    /**
     * Delete the "id" formulaReg.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the formulaReg corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<FormulaReg> search(String query);
}
