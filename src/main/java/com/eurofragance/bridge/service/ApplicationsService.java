package com.eurofragance.bridge.service;

import com.eurofragance.bridge.domain.Applications;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Applications}.
 */
public interface ApplicationsService {

    /**
     * Save a applications.
     *
     * @param applications the entity to save.
     * @return the persisted entity.
     */
    Applications save(Applications applications);

    /**
     * Get all the applications.
     *
     * @return the list of entities.
     */
    List<Applications> findAll();


    /**
     * Get the "id" applications.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Applications> findOne(Long id);

    /**
     * Delete the "id" applications.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the applications corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Applications> search(String query);
}
