package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.BridgeApp;
import com.eurofragance.bridge.config.TestSecurityConfiguration;
import com.eurofragance.bridge.domain.Formula;
import com.eurofragance.bridge.repository.FormulaRepository;
import com.eurofragance.bridge.repository.search.FormulaSearchRepository;
import com.eurofragance.bridge.service.FormulaService;
import com.eurofragance.bridge.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.eurofragance.bridge.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FormulaResource} REST controller.
 */
@SpringBootTest(classes = {BridgeApp.class, TestSecurityConfiguration.class})
public class FormulaResourceIT {

    private static final String DEFAULT_FORMULA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FORMULA_NAME = "BBBBBBBBBB";

    @Autowired
    private FormulaRepository formulaRepository;

    @Autowired
    private FormulaService formulaService;

    /**
     * This repository is mocked in the com.eurofragance.bridge.repository.search test package.
     *
     * @see com.eurofragance.bridge.repository.search.FormulaSearchRepositoryMockConfiguration
     */
    @Autowired
    private FormulaSearchRepository mockFormulaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFormulaMockMvc;

    private Formula formula;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormulaResource formulaResource = new FormulaResource(formulaService);
        this.restFormulaMockMvc = MockMvcBuilders.standaloneSetup(formulaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formula createEntity(EntityManager em) {
        Formula formula = new Formula()
            .formulaName(DEFAULT_FORMULA_NAME);
        return formula;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formula createUpdatedEntity(EntityManager em) {
        Formula formula = new Formula()
            .formulaName(UPDATED_FORMULA_NAME);
        return formula;
    }

    @BeforeEach
    public void initTest() {
        formula = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormula() throws Exception {
        int databaseSizeBeforeCreate = formulaRepository.findAll().size();

        // Create the Formula
        restFormulaMockMvc.perform(post("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formula)))
            .andExpect(status().isCreated());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeCreate + 1);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getFormulaName()).isEqualTo(DEFAULT_FORMULA_NAME);

        // Validate the Formula in Elasticsearch
        verify(mockFormulaSearchRepository, times(1)).save(testFormula);
    }

    @Test
    @Transactional
    public void createFormulaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formulaRepository.findAll().size();

        // Create the Formula with an existing ID
        formula.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormulaMockMvc.perform(post("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formula)))
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Formula in Elasticsearch
        verify(mockFormulaSearchRepository, times(0)).save(formula);
    }


    @Test
    @Transactional
    public void getAllFormulas() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList
        restFormulaMockMvc.perform(get("/api/formulas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formula.getId().intValue())))
            .andExpect(jsonPath("$.[*].formulaName").value(hasItem(DEFAULT_FORMULA_NAME)));
    }
    
    @Test
    @Transactional
    public void getFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get the formula
        restFormulaMockMvc.perform(get("/api/formulas/{id}", formula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formula.getId().intValue()))
            .andExpect(jsonPath("$.formulaName").value(DEFAULT_FORMULA_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingFormula() throws Exception {
        // Get the formula
        restFormulaMockMvc.perform(get("/api/formulas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormula() throws Exception {
        // Initialize the database
        formulaService.save(formula);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFormulaSearchRepository);

        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();

        // Update the formula
        Formula updatedFormula = formulaRepository.findById(formula.getId()).get();
        // Disconnect from session so that the updates on updatedFormula are not directly saved in db
        em.detach(updatedFormula);
        updatedFormula
            .formulaName(UPDATED_FORMULA_NAME);

        restFormulaMockMvc.perform(put("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormula)))
            .andExpect(status().isOk());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getFormulaName()).isEqualTo(UPDATED_FORMULA_NAME);

        // Validate the Formula in Elasticsearch
        verify(mockFormulaSearchRepository, times(1)).save(testFormula);
    }

    @Test
    @Transactional
    public void updateNonExistingFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();

        // Create the Formula

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaMockMvc.perform(put("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formula)))
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Formula in Elasticsearch
        verify(mockFormulaSearchRepository, times(0)).save(formula);
    }

    @Test
    @Transactional
    public void deleteFormula() throws Exception {
        // Initialize the database
        formulaService.save(formula);

        int databaseSizeBeforeDelete = formulaRepository.findAll().size();

        // Delete the formula
        restFormulaMockMvc.perform(delete("/api/formulas/{id}", formula.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Formula in Elasticsearch
        verify(mockFormulaSearchRepository, times(1)).deleteById(formula.getId());
    }

    @Test
    @Transactional
    public void searchFormula() throws Exception {
        // Initialize the database
        formulaService.save(formula);
        when(mockFormulaSearchRepository.search(queryStringQuery("id:" + formula.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(formula), PageRequest.of(0, 1), 1));
        // Search the formula
        restFormulaMockMvc.perform(get("/api/_search/formulas?query=id:" + formula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formula.getId().intValue())))
            .andExpect(jsonPath("$.[*].formulaName").value(hasItem(DEFAULT_FORMULA_NAME)));
    }
}
