package com.eurofragance.bridge.service;

import com.eurofragance.bridge.domain.Submission;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Submission}.
 */
public interface SubmissionService {

    /**
     * Save a submission.
     *
     * @param submission the entity to save.
     * @return the persisted entity.
     */
    Submission save(Submission submission);

    /**
     * Get all the submissions.
     *
     * @return the list of entities.
     */
    List<Submission> findAll();


    /**
     * Get the "id" submission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Submission> findOne(Long id);

    /**
     * Delete the "id" submission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the submission corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Submission> search(String query);
}
