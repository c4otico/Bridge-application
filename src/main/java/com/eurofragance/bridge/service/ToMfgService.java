package com.eurofragance.bridge.service;

import com.eurofragance.bridge.domain.ToMfg;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ToMfg}.
 */
public interface ToMfgService {

    /**
     * Save a toMfg.
     *
     * @param toMfg the entity to save.
     * @return the persisted entity.
     */
    ToMfg save(ToMfg toMfg);

    /**
     * Get all the toMfgs.
     *
     * @return the list of entities.
     */
    List<ToMfg> findAll();


    /**
     * Get the "id" toMfg.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ToMfg> findOne(Long id);

    /**
     * Delete the "id" toMfg.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the toMfg corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<ToMfg> search(String query);
}
