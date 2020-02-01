package com.eurofragance.bridge.service.impl;

import com.eurofragance.bridge.service.FormulaStatusService;
import com.eurofragance.bridge.domain.FormulaStatus;
import com.eurofragance.bridge.repository.FormulaStatusRepository;
import com.eurofragance.bridge.repository.search.FormulaStatusSearchRepository;
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
 * Service Implementation for managing {@link FormulaStatus}.
 */
@Service
@Transactional
public class FormulaStatusServiceImpl implements FormulaStatusService {

    private final Logger log = LoggerFactory.getLogger(FormulaStatusServiceImpl.class);

    private final FormulaStatusRepository formulaStatusRepository;

    private final FormulaStatusSearchRepository formulaStatusSearchRepository;

    public FormulaStatusServiceImpl(FormulaStatusRepository formulaStatusRepository, FormulaStatusSearchRepository formulaStatusSearchRepository) {
        this.formulaStatusRepository = formulaStatusRepository;
        this.formulaStatusSearchRepository = formulaStatusSearchRepository;
    }

    /**
     * Save a formulaStatus.
     *
     * @param formulaStatus the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FormulaStatus save(FormulaStatus formulaStatus) {
        log.debug("Request to save FormulaStatus : {}", formulaStatus);
        FormulaStatus result = formulaStatusRepository.save(formulaStatus);
        formulaStatusSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the formulaStatuses.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FormulaStatus> findAll() {
        log.debug("Request to get all FormulaStatuses");
        return formulaStatusRepository.findAll();
    }


    /**
     * Get one formulaStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FormulaStatus> findOne(Long id) {
        log.debug("Request to get FormulaStatus : {}", id);
        return formulaStatusRepository.findById(id);
    }

    /**
     * Delete the formulaStatus by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormulaStatus : {}", id);
        formulaStatusRepository.deleteById(id);
        formulaStatusSearchRepository.deleteById(id);
    }

    /**
     * Search for the formulaStatus corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FormulaStatus> search(String query) {
        log.debug("Request to search FormulaStatuses for query {}", query);
        return StreamSupport
            .stream(formulaStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
