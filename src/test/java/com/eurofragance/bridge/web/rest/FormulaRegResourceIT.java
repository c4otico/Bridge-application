package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.BridgeApp;
import com.eurofragance.bridge.config.TestSecurityConfiguration;
import com.eurofragance.bridge.domain.FormulaReg;
import com.eurofragance.bridge.repository.FormulaRegRepository;
import com.eurofragance.bridge.repository.search.FormulaRegSearchRepository;
import com.eurofragance.bridge.service.FormulaRegService;
import com.eurofragance.bridge.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Integration tests for the {@link FormulaRegResource} REST controller.
 */
@SpringBootTest(classes = {BridgeApp.class, TestSecurityConfiguration.class})
public class FormulaRegResourceIT {

    private static final String DEFAULT_MORE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_MORE_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_EVEN_MORE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_EVEN_MORE_DETAILS = "BBBBBBBBBB";

    @Autowired
    private FormulaRegRepository formulaRegRepository;

    @Autowired
    private FormulaRegService formulaRegService;

    /**
     * This repository is mocked in the com.eurofragance.bridge.repository.search test package.
     *
     * @see com.eurofragance.bridge.repository.search.FormulaRegSearchRepositoryMockConfiguration
     */
    @Autowired
    private FormulaRegSearchRepository mockFormulaRegSearchRepository;

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

    private MockMvc restFormulaRegMockMvc;

    private FormulaReg formulaReg;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormulaRegResource formulaRegResource = new FormulaRegResource(formulaRegService);
        this.restFormulaRegMockMvc = MockMvcBuilders.standaloneSetup(formulaRegResource)
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
    public static FormulaReg createEntity(EntityManager em) {
        FormulaReg formulaReg = new FormulaReg()
            .moreDetails(DEFAULT_MORE_DETAILS)
            .evenMoreDetails(DEFAULT_EVEN_MORE_DETAILS);
        return formulaReg;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormulaReg createUpdatedEntity(EntityManager em) {
        FormulaReg formulaReg = new FormulaReg()
            .moreDetails(UPDATED_MORE_DETAILS)
            .evenMoreDetails(UPDATED_EVEN_MORE_DETAILS);
        return formulaReg;
    }

    @BeforeEach
    public void initTest() {
        formulaReg = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormulaReg() throws Exception {
        int databaseSizeBeforeCreate = formulaRegRepository.findAll().size();

        // Create the FormulaReg
        restFormulaRegMockMvc.perform(post("/api/formula-regs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaReg)))
            .andExpect(status().isCreated());

        // Validate the FormulaReg in the database
        List<FormulaReg> formulaRegList = formulaRegRepository.findAll();
        assertThat(formulaRegList).hasSize(databaseSizeBeforeCreate + 1);
        FormulaReg testFormulaReg = formulaRegList.get(formulaRegList.size() - 1);
        assertThat(testFormulaReg.getMoreDetails()).isEqualTo(DEFAULT_MORE_DETAILS);
        assertThat(testFormulaReg.getEvenMoreDetails()).isEqualTo(DEFAULT_EVEN_MORE_DETAILS);

        // Validate the FormulaReg in Elasticsearch
        verify(mockFormulaRegSearchRepository, times(1)).save(testFormulaReg);
    }

    @Test
    @Transactional
    public void createFormulaRegWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formulaRegRepository.findAll().size();

        // Create the FormulaReg with an existing ID
        formulaReg.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormulaRegMockMvc.perform(post("/api/formula-regs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaReg)))
            .andExpect(status().isBadRequest());

        // Validate the FormulaReg in the database
        List<FormulaReg> formulaRegList = formulaRegRepository.findAll();
        assertThat(formulaRegList).hasSize(databaseSizeBeforeCreate);

        // Validate the FormulaReg in Elasticsearch
        verify(mockFormulaRegSearchRepository, times(0)).save(formulaReg);
    }


    @Test
    @Transactional
    public void getAllFormulaRegs() throws Exception {
        // Initialize the database
        formulaRegRepository.saveAndFlush(formulaReg);

        // Get all the formulaRegList
        restFormulaRegMockMvc.perform(get("/api/formula-regs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formulaReg.getId().intValue())))
            .andExpect(jsonPath("$.[*].moreDetails").value(hasItem(DEFAULT_MORE_DETAILS)))
            .andExpect(jsonPath("$.[*].evenMoreDetails").value(hasItem(DEFAULT_EVEN_MORE_DETAILS)));
    }
    
    @Test
    @Transactional
    public void getFormulaReg() throws Exception {
        // Initialize the database
        formulaRegRepository.saveAndFlush(formulaReg);

        // Get the formulaReg
        restFormulaRegMockMvc.perform(get("/api/formula-regs/{id}", formulaReg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formulaReg.getId().intValue()))
            .andExpect(jsonPath("$.moreDetails").value(DEFAULT_MORE_DETAILS))
            .andExpect(jsonPath("$.evenMoreDetails").value(DEFAULT_EVEN_MORE_DETAILS));
    }

    @Test
    @Transactional
    public void getNonExistingFormulaReg() throws Exception {
        // Get the formulaReg
        restFormulaRegMockMvc.perform(get("/api/formula-regs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormulaReg() throws Exception {
        // Initialize the database
        formulaRegService.save(formulaReg);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFormulaRegSearchRepository);

        int databaseSizeBeforeUpdate = formulaRegRepository.findAll().size();

        // Update the formulaReg
        FormulaReg updatedFormulaReg = formulaRegRepository.findById(formulaReg.getId()).get();
        // Disconnect from session so that the updates on updatedFormulaReg are not directly saved in db
        em.detach(updatedFormulaReg);
        updatedFormulaReg
            .moreDetails(UPDATED_MORE_DETAILS)
            .evenMoreDetails(UPDATED_EVEN_MORE_DETAILS);

        restFormulaRegMockMvc.perform(put("/api/formula-regs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormulaReg)))
            .andExpect(status().isOk());

        // Validate the FormulaReg in the database
        List<FormulaReg> formulaRegList = formulaRegRepository.findAll();
        assertThat(formulaRegList).hasSize(databaseSizeBeforeUpdate);
        FormulaReg testFormulaReg = formulaRegList.get(formulaRegList.size() - 1);
        assertThat(testFormulaReg.getMoreDetails()).isEqualTo(UPDATED_MORE_DETAILS);
        assertThat(testFormulaReg.getEvenMoreDetails()).isEqualTo(UPDATED_EVEN_MORE_DETAILS);

        // Validate the FormulaReg in Elasticsearch
        verify(mockFormulaRegSearchRepository, times(1)).save(testFormulaReg);
    }

    @Test
    @Transactional
    public void updateNonExistingFormulaReg() throws Exception {
        int databaseSizeBeforeUpdate = formulaRegRepository.findAll().size();

        // Create the FormulaReg

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaRegMockMvc.perform(put("/api/formula-regs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaReg)))
            .andExpect(status().isBadRequest());

        // Validate the FormulaReg in the database
        List<FormulaReg> formulaRegList = formulaRegRepository.findAll();
        assertThat(formulaRegList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FormulaReg in Elasticsearch
        verify(mockFormulaRegSearchRepository, times(0)).save(formulaReg);
    }

    @Test
    @Transactional
    public void deleteFormulaReg() throws Exception {
        // Initialize the database
        formulaRegService.save(formulaReg);

        int databaseSizeBeforeDelete = formulaRegRepository.findAll().size();

        // Delete the formulaReg
        restFormulaRegMockMvc.perform(delete("/api/formula-regs/{id}", formulaReg.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormulaReg> formulaRegList = formulaRegRepository.findAll();
        assertThat(formulaRegList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FormulaReg in Elasticsearch
        verify(mockFormulaRegSearchRepository, times(1)).deleteById(formulaReg.getId());
    }

    @Test
    @Transactional
    public void searchFormulaReg() throws Exception {
        // Initialize the database
        formulaRegService.save(formulaReg);
        when(mockFormulaRegSearchRepository.search(queryStringQuery("id:" + formulaReg.getId())))
            .thenReturn(Collections.singletonList(formulaReg));
        // Search the formulaReg
        restFormulaRegMockMvc.perform(get("/api/_search/formula-regs?query=id:" + formulaReg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formulaReg.getId().intValue())))
            .andExpect(jsonPath("$.[*].moreDetails").value(hasItem(DEFAULT_MORE_DETAILS)))
            .andExpect(jsonPath("$.[*].evenMoreDetails").value(hasItem(DEFAULT_EVEN_MORE_DETAILS)));
    }
}
