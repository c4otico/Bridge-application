package com.eurofragance.bridge.service.impl;

import com.eurofragance.bridge.service.DeveloperService;
import com.eurofragance.bridge.domain.Developer;
import com.eurofragance.bridge.repository.DeveloperRepository;
import com.eurofragance.bridge.repository.search.DeveloperSearchRepository;
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
 * Service Implementation for managing {@link Developer}.
 */
@Service
@Transactional
public class DeveloperServiceImpl implements DeveloperService {

    private final Logger log = LoggerFactory.getLogger(DeveloperServiceImpl.class);

    private final DeveloperRepository developerRepository;

    private final DeveloperSearchRepository developerSearchRepository;

    public DeveloperServiceImpl(DeveloperRepository developerRepository, DeveloperSearchRepository developerSearchRepository) {
        this.developerRepository = developerRepository;
        this.developerSearchRepository = developerSearchRepository;
    }

    /**
     * Save a developer.
     *
     * @param developer the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Developer save(Developer developer) {
        log.debug("Request to save Developer : {}", developer);
        Developer result = developerRepository.save(developer);
        developerSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the developers.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Developer> findAll() {
        log.debug("Request to get all Developers");
        return developerRepository.findAll();
    }


    /**
     * Get one developer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Developer> findOne(Long id) {
        log.debug("Request to get Developer : {}", id);
        return developerRepository.findById(id);
    }

    /**
     * Delete the developer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Developer : {}", id);
        developerRepository.deleteById(id);
        developerSearchRepository.deleteById(id);
    }

    /**
     * Search for the developer corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Developer> search(String query) {
        log.debug("Request to search Developers for query {}", query);
        return StreamSupport
            .stream(developerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
