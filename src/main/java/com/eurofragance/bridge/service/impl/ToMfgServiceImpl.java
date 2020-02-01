package com.eurofragance.bridge.service.impl;

import com.eurofragance.bridge.service.ToMfgService;
import com.eurofragance.bridge.domain.ToMfg;
import com.eurofragance.bridge.repository.ToMfgRepository;
import com.eurofragance.bridge.repository.search.ToMfgSearchRepository;
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
 * Service Implementation for managing {@link ToMfg}.
 */
@Service
@Transactional
public class ToMfgServiceImpl implements ToMfgService {

    private final Logger log = LoggerFactory.getLogger(ToMfgServiceImpl.class);

    private final ToMfgRepository toMfgRepository;

    private final ToMfgSearchRepository toMfgSearchRepository;

    public ToMfgServiceImpl(ToMfgRepository toMfgRepository, ToMfgSearchRepository toMfgSearchRepository) {
        this.toMfgRepository = toMfgRepository;
        this.toMfgSearchRepository = toMfgSearchRepository;
    }

    /**
     * Save a toMfg.
     *
     * @param toMfg the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ToMfg save(ToMfg toMfg) {
        log.debug("Request to save ToMfg : {}", toMfg);
        ToMfg result = toMfgRepository.save(toMfg);
        toMfgSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the toMfgs.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ToMfg> findAll() {
        log.debug("Request to get all ToMfgs");
        return toMfgRepository.findAll();
    }


    /**
     * Get one toMfg by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ToMfg> findOne(Long id) {
        log.debug("Request to get ToMfg : {}", id);
        return toMfgRepository.findById(id);
    }

    /**
     * Delete the toMfg by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ToMfg : {}", id);
        toMfgRepository.deleteById(id);
        toMfgSearchRepository.deleteById(id);
    }

    /**
     * Search for the toMfg corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ToMfg> search(String query) {
        log.debug("Request to search ToMfgs for query {}", query);
        return StreamSupport
            .stream(toMfgSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
