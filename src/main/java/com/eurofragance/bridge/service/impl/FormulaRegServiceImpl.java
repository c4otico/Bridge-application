package com.eurofragance.bridge.service.impl;

import com.eurofragance.bridge.service.FormulaRegService;
import com.eurofragance.bridge.domain.FormulaReg;
import com.eurofragance.bridge.repository.FormulaRegRepository;
import com.eurofragance.bridge.repository.search.FormulaRegSearchRepository;
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
 * Service Implementation for managing {@link FormulaReg}.
 */
@Service
@Transactional
public class FormulaRegServiceImpl implements FormulaRegService {

    private final Logger log = LoggerFactory.getLogger(FormulaRegServiceImpl.class);

    private final FormulaRegRepository formulaRegRepository;

    private final FormulaRegSearchRepository formulaRegSearchRepository;

    public FormulaRegServiceImpl(FormulaRegRepository formulaRegRepository, FormulaRegSearchRepository formulaRegSearchRepository) {
        this.formulaRegRepository = formulaRegRepository;
        this.formulaRegSearchRepository = formulaRegSearchRepository;
    }

    /**
     * Save a formulaReg.
     *
     * @param formulaReg the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FormulaReg save(FormulaReg formulaReg) {
        log.debug("Request to save FormulaReg : {}", formulaReg);
        FormulaReg result = formulaRegRepository.save(formulaReg);
        formulaRegSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the formulaRegs.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FormulaReg> findAll() {
        log.debug("Request to get all FormulaRegs");
        return formulaRegRepository.findAll();
    }


    /**
     * Get one formulaReg by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FormulaReg> findOne(Long id) {
        log.debug("Request to get FormulaReg : {}", id);
        return formulaRegRepository.findById(id);
    }

    /**
     * Delete the formulaReg by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormulaReg : {}", id);
        formulaRegRepository.deleteById(id);
        formulaRegSearchRepository.deleteById(id);
    }

    /**
     * Search for the formulaReg corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FormulaReg> search(String query) {
        log.debug("Request to search FormulaRegs for query {}", query);
        return StreamSupport
            .stream(formulaRegSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
