package com.eurofragance.bridge.service.impl;

import com.eurofragance.bridge.service.FormulaService;
import com.eurofragance.bridge.domain.Formula;
import com.eurofragance.bridge.repository.FormulaRepository;
import com.eurofragance.bridge.repository.search.FormulaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Formula}.
 */
@Service
@Transactional
public class FormulaServiceImpl implements FormulaService {

    private final Logger log = LoggerFactory.getLogger(FormulaServiceImpl.class);

    private final FormulaRepository formulaRepository;

    private final FormulaSearchRepository formulaSearchRepository;

    public FormulaServiceImpl(FormulaRepository formulaRepository, FormulaSearchRepository formulaSearchRepository) {
        this.formulaRepository = formulaRepository;
        this.formulaSearchRepository = formulaSearchRepository;
    }

    /**
     * Save a formula.
     *
     * @param formula the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Formula save(Formula formula) {
        log.debug("Request to save Formula : {}", formula);
        Formula result = formulaRepository.save(formula);
        formulaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the formulas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Formula> findAll(Pageable pageable) {
        log.debug("Request to get all Formulas");
        return formulaRepository.findAll(pageable);
    }


    /**
     * Get one formula by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Formula> findOne(Long id) {
        log.debug("Request to get Formula : {}", id);
        return formulaRepository.findById(id);
    }

    /**
     * Delete the formula by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Formula : {}", id);
        formulaRepository.deleteById(id);
        formulaSearchRepository.deleteById(id);
    }

    /**
     * Search for the formula corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Formula> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Formulas for query {}", query);
        return formulaSearchRepository.search(queryStringQuery(query), pageable);    }
}
