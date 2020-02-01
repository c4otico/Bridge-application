package com.eurofragance.bridge.service.impl;

import com.eurofragance.bridge.service.FormulaItemsService;
import com.eurofragance.bridge.domain.FormulaItems;
import com.eurofragance.bridge.repository.FormulaItemsRepository;
import com.eurofragance.bridge.repository.search.FormulaItemsSearchRepository;
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
 * Service Implementation for managing {@link FormulaItems}.
 */
@Service
@Transactional
public class FormulaItemsServiceImpl implements FormulaItemsService {

    private final Logger log = LoggerFactory.getLogger(FormulaItemsServiceImpl.class);

    private final FormulaItemsRepository formulaItemsRepository;

    private final FormulaItemsSearchRepository formulaItemsSearchRepository;

    public FormulaItemsServiceImpl(FormulaItemsRepository formulaItemsRepository, FormulaItemsSearchRepository formulaItemsSearchRepository) {
        this.formulaItemsRepository = formulaItemsRepository;
        this.formulaItemsSearchRepository = formulaItemsSearchRepository;
    }

    /**
     * Save a formulaItems.
     *
     * @param formulaItems the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FormulaItems save(FormulaItems formulaItems) {
        log.debug("Request to save FormulaItems : {}", formulaItems);
        FormulaItems result = formulaItemsRepository.save(formulaItems);
        formulaItemsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the formulaItems.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FormulaItems> findAll() {
        log.debug("Request to get all FormulaItems");
        return formulaItemsRepository.findAll();
    }


    /**
     * Get one formulaItems by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FormulaItems> findOne(Long id) {
        log.debug("Request to get FormulaItems : {}", id);
        return formulaItemsRepository.findById(id);
    }

    /**
     * Delete the formulaItems by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormulaItems : {}", id);
        formulaItemsRepository.deleteById(id);
        formulaItemsSearchRepository.deleteById(id);
    }

    /**
     * Search for the formulaItems corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FormulaItems> search(String query) {
        log.debug("Request to search FormulaItems for query {}", query);
        return StreamSupport
            .stream(formulaItemsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
