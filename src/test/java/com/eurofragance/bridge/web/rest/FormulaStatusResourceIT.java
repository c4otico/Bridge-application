package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.BridgeApp;
import com.eurofragance.bridge.config.TestSecurityConfiguration;
import com.eurofragance.bridge.domain.FormulaStatus;
import com.eurofragance.bridge.repository.FormulaStatusRepository;
import com.eurofragance.bridge.repository.search.FormulaStatusSearchRepository;
import com.eurofragance.bridge.service.FormulaStatusService;
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
 * Integration tests for the {@link FormulaStatusResource} REST controller.
 */
@SpringBootTest(classes = {BridgeApp.class, TestSecurityConfiguration.class})
public class FormulaStatusResourceIT {

    private static final String DEFAULT_NAME_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_NAME_STATUS = "BBBBBBBBBB";

    @Autowired
    private FormulaStatusRepository formulaStatusRepository;

    @Autowired
    private FormulaStatusService formulaStatusService;

    /**
     * This repository is mocked in the com.eurofragance.bridge.repository.search test package.
     *
     * @see com.eurofragance.bridge.repository.search.FormulaStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private FormulaStatusSearchRepository mockFormulaStatusSearchRepository;

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

    private MockMvc restFormulaStatusMockMvc;

    private FormulaStatus formulaStatus;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormulaStatusResource formulaStatusResource = new FormulaStatusResource(formulaStatusService);
        this.restFormulaStatusMockMvc = MockMvcBuilders.standaloneSetup(formulaStatusResource)
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
    public static FormulaStatus createEntity(EntityManager em) {
        FormulaStatus formulaStatus = new FormulaStatus()
            .nameStatus(DEFAULT_NAME_STATUS);
        return formulaStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormulaStatus createUpdatedEntity(EntityManager em) {
        FormulaStatus formulaStatus = new FormulaStatus()
            .nameStatus(UPDATED_NAME_STATUS);
        return formulaStatus;
    }

    @BeforeEach
    public void initTest() {
        formulaStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormulaStatus() throws Exception {
        int databaseSizeBeforeCreate = formulaStatusRepository.findAll().size();

        // Create the FormulaStatus
        restFormulaStatusMockMvc.perform(post("/api/formula-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaStatus)))
            .andExpect(status().isCreated());

        // Validate the FormulaStatus in the database
        List<FormulaStatus> formulaStatusList = formulaStatusRepository.findAll();
        assertThat(formulaStatusList).hasSize(databaseSizeBeforeCreate + 1);
        FormulaStatus testFormulaStatus = formulaStatusList.get(formulaStatusList.size() - 1);
        assertThat(testFormulaStatus.getNameStatus()).isEqualTo(DEFAULT_NAME_STATUS);

        // Validate the FormulaStatus in Elasticsearch
        verify(mockFormulaStatusSearchRepository, times(1)).save(testFormulaStatus);
    }

    @Test
    @Transactional
    public void createFormulaStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formulaStatusRepository.findAll().size();

        // Create the FormulaStatus with an existing ID
        formulaStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormulaStatusMockMvc.perform(post("/api/formula-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaStatus)))
            .andExpect(status().isBadRequest());

        // Validate the FormulaStatus in the database
        List<FormulaStatus> formulaStatusList = formulaStatusRepository.findAll();
        assertThat(formulaStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the FormulaStatus in Elasticsearch
        verify(mockFormulaStatusSearchRepository, times(0)).save(formulaStatus);
    }


    @Test
    @Transactional
    public void getAllFormulaStatuses() throws Exception {
        // Initialize the database
        formulaStatusRepository.saveAndFlush(formulaStatus);

        // Get all the formulaStatusList
        restFormulaStatusMockMvc.perform(get("/api/formula-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formulaStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameStatus").value(hasItem(DEFAULT_NAME_STATUS)));
    }
    
    @Test
    @Transactional
    public void getFormulaStatus() throws Exception {
        // Initialize the database
        formulaStatusRepository.saveAndFlush(formulaStatus);

        // Get the formulaStatus
        restFormulaStatusMockMvc.perform(get("/api/formula-statuses/{id}", formulaStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formulaStatus.getId().intValue()))
            .andExpect(jsonPath("$.nameStatus").value(DEFAULT_NAME_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingFormulaStatus() throws Exception {
        // Get the formulaStatus
        restFormulaStatusMockMvc.perform(get("/api/formula-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormulaStatus() throws Exception {
        // Initialize the database
        formulaStatusService.save(formulaStatus);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFormulaStatusSearchRepository);

        int databaseSizeBeforeUpdate = formulaStatusRepository.findAll().size();

        // Update the formulaStatus
        FormulaStatus updatedFormulaStatus = formulaStatusRepository.findById(formulaStatus.getId()).get();
        // Disconnect from session so that the updates on updatedFormulaStatus are not directly saved in db
        em.detach(updatedFormulaStatus);
        updatedFormulaStatus
            .nameStatus(UPDATED_NAME_STATUS);

        restFormulaStatusMockMvc.perform(put("/api/formula-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormulaStatus)))
            .andExpect(status().isOk());

        // Validate the FormulaStatus in the database
        List<FormulaStatus> formulaStatusList = formulaStatusRepository.findAll();
        assertThat(formulaStatusList).hasSize(databaseSizeBeforeUpdate);
        FormulaStatus testFormulaStatus = formulaStatusList.get(formulaStatusList.size() - 1);
        assertThat(testFormulaStatus.getNameStatus()).isEqualTo(UPDATED_NAME_STATUS);

        // Validate the FormulaStatus in Elasticsearch
        verify(mockFormulaStatusSearchRepository, times(1)).save(testFormulaStatus);
    }

    @Test
    @Transactional
    public void updateNonExistingFormulaStatus() throws Exception {
        int databaseSizeBeforeUpdate = formulaStatusRepository.findAll().size();

        // Create the FormulaStatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaStatusMockMvc.perform(put("/api/formula-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaStatus)))
            .andExpect(status().isBadRequest());

        // Validate the FormulaStatus in the database
        List<FormulaStatus> formulaStatusList = formulaStatusRepository.findAll();
        assertThat(formulaStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FormulaStatus in Elasticsearch
        verify(mockFormulaStatusSearchRepository, times(0)).save(formulaStatus);
    }

    @Test
    @Transactional
    public void deleteFormulaStatus() throws Exception {
        // Initialize the database
        formulaStatusService.save(formulaStatus);

        int databaseSizeBeforeDelete = formulaStatusRepository.findAll().size();

        // Delete the formulaStatus
        restFormulaStatusMockMvc.perform(delete("/api/formula-statuses/{id}", formulaStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormulaStatus> formulaStatusList = formulaStatusRepository.findAll();
        assertThat(formulaStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FormulaStatus in Elasticsearch
        verify(mockFormulaStatusSearchRepository, times(1)).deleteById(formulaStatus.getId());
    }

    @Test
    @Transactional
    public void searchFormulaStatus() throws Exception {
        // Initialize the database
        formulaStatusService.save(formulaStatus);
        when(mockFormulaStatusSearchRepository.search(queryStringQuery("id:" + formulaStatus.getId())))
            .thenReturn(Collections.singletonList(formulaStatus));
        // Search the formulaStatus
        restFormulaStatusMockMvc.perform(get("/api/_search/formula-statuses?query=id:" + formulaStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formulaStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameStatus").value(hasItem(DEFAULT_NAME_STATUS)));
    }
}
