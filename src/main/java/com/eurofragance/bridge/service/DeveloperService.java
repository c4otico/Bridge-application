package com.eurofragance.bridge.service;

import com.eurofragance.bridge.domain.Developer;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Developer}.
 */
public interface DeveloperService {

    /**
     * Save a developer.
     *
     * @param developer the entity to save.
     * @return the persisted entity.
     */
    Developer save(Developer developer);

    /**
     * Get all the developers.
     *
     * @return the list of entities.
     */
    List<Developer> findAll();


    /**
     * Get the "id" developer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Developer> findOne(Long id);

    /**
     * Delete the "id" developer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the developer corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Developer> search(String query);
}
