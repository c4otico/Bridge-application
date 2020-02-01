package com.eurofragance.bridge.service.impl;

import com.eurofragance.bridge.service.SubmissionService;
import com.eurofragance.bridge.domain.Submission;
import com.eurofragance.bridge.repository.SubmissionRepository;
import com.eurofragance.bridge.repository.search.SubmissionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Submission}.
 */
@Service
@Transactional
public class SubmissionServiceImpl implements SubmissionService {

    private final Logger log = LoggerFactory.getLogger(SubmissionServiceImpl.class);

    private final SubmissionRepository submissionRepository;

    private final SubmissionSearchRepository submissionSearchRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository, SubmissionSearchRepository submissionSearchRepository) {
        this.submissionRepository = submissionRepository;
        this.submissionSearchRepository = submissionSearchRepository;
    }

    /**
     * Save a submission.
     *
     * @param submission the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Submission save(Submission submission) {
        log.debug("Request to save Submission : {}", submission);
        Submission result = submissionRepository.save(submission);
        submissionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the submissions.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Submission> findAll() {
        log.debug("Request to get all Submissions");
        return submissionRepository.findAll();
    }


    /**
     * Get one submission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Submission> findOne(Long id) {
        log.debug("Request to get Submission : {}", id);
        return submissionRepository.findById(id);
    }

    /**
     * Delete the submission by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Submission : {}", id);
        submissionRepository.deleteById(id);
        submissionSearchRepository.deleteById(id);
    }

    /**
     * Search for the submission corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Submission> search(String query) {
        log.debug("Request to search Submissions for query {}", query);
        return StreamSupport
            .stream(submissionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
