package com.eurofragance.bridge.service;

import com.eurofragance.bridge.domain.SendToUser;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link SendToUser}.
 */
public interface SendToUserService {

    /**
     * Save a sendToUser.
     *
     * @param sendToUser the entity to save.
     * @return the persisted entity.
     */
    SendToUser save(SendToUser sendToUser);

    /**
     * Get all the sendToUsers.
     *
     * @return the list of entities.
     */
    List<SendToUser> findAll();


    /**
     * Get the "id" sendToUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SendToUser> findOne(Long id);

    /**
     * Delete the "id" sendToUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the sendToUser corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<SendToUser> search(String query);
}
