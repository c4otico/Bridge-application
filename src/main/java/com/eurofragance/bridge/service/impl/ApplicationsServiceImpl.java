package com.eurofragance.bridge.service.impl;

import com.eurofragance.bridge.service.ApplicationsService;
import com.eurofragance.bridge.domain.Applications;
import com.eurofragance.bridge.repository.ApplicationsRepository;
import com.eurofragance.bridge.repository.search.ApplicationsSearchRepository;
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
 * Service Implementation for managing {@link Applications}.
 */
@Service
@Transactional
public class ApplicationsServiceImpl implements ApplicationsService {

    private final Logger log = LoggerFactory.getLogger(ApplicationsServiceImpl.class);

    private final ApplicationsRepository applicationsRepository;

    private final ApplicationsSearchRepository applicationsSearchRepository;

    public ApplicationsServiceImpl(ApplicationsRepository applicationsRepository, ApplicationsSearchRepository applicationsSearchRepository) {
        this.applicationsRepository = applicationsRepository;
        this.applicationsSearchRepository = applicationsSearchRepository;
    }

    /**
     * Save a applications.
     *
     * @param applications the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Applications save(Applications applications) {
        log.debug("Request to save Applications : {}", applications);
        Applications result = applicationsRepository.save(applications);
        applicationsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the applications.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Applications> findAll() {
        log.debug("Request to get all Applications");
        return applicationsRepository.findAll();
    }


    /**
     * Get one applications by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Applications> findOne(Long id) {
        log.debug("Request to get Applications : {}", id);
        return applicationsRepository.findById(id);
    }

    /**
     * Delete the applications by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Applications : {}", id);
        applicationsRepository.deleteById(id);
        applicationsSearchRepository.deleteById(id);
    }

    /**
     * Search for the applications corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Applications> search(String query) {
        log.debug("Request to search Applications for query {}", query);
        return StreamSupport
            .stream(applicationsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
