package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.BridgeApp;
import com.eurofragance.bridge.config.TestSecurityConfiguration;
import com.eurofragance.bridge.domain.FormulaItems;
import com.eurofragance.bridge.repository.FormulaItemsRepository;
import com.eurofragance.bridge.repository.search.FormulaItemsSearchRepository;
import com.eurofragance.bridge.service.FormulaItemsService;
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
 * Integration tests for the {@link FormulaItemsResource} REST controller.
 */
@SpringBootTest(classes = {BridgeApp.class, TestSecurityConfiguration.class})
public class FormulaItemsResourceIT {

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    @Autowired
    private FormulaItemsRepository formulaItemsRepository;

    @Autowired
    private FormulaItemsService formulaItemsService;

    /**
     * This repository is mocked in the com.eurofragance.bridge.repository.search test package.
     *
     * @see com.eurofragance.bridge.repository.search.FormulaItemsSearchRepositoryMockConfiguration
     */
    @Autowired
    private FormulaItemsSearchRepository mockFormulaItemsSearchRepository;

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

    private MockMvc restFormulaItemsMockMvc;

    private FormulaItems formulaItems;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormulaItemsResource formulaItemsResource = new FormulaItemsResource(formulaItemsService);
        this.restFormulaItemsMockMvc = MockMvcBuilders.standaloneSetup(formulaItemsResource)
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
    public static FormulaItems createEntity(EntityManager em) {
        FormulaItems formulaItems = new FormulaItems()
            .countryName(DEFAULT_COUNTRY_NAME);
        return formulaItems;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormulaItems createUpdatedEntity(EntityManager em) {
        FormulaItems formulaItems = new FormulaItems()
            .countryName(UPDATED_COUNTRY_NAME);
        return formulaItems;
    }

    @BeforeEach
    public void initTest() {
        formulaItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormulaItems() throws Exception {
        int databaseSizeBeforeCreate = formulaItemsRepository.findAll().size();

        // Create the FormulaItems
        restFormulaItemsMockMvc.perform(post("/api/formula-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaItems)))
            .andExpect(status().isCreated());

        // Validate the FormulaItems in the database
        List<FormulaItems> formulaItemsList = formulaItemsRepository.findAll();
        assertThat(formulaItemsList).hasSize(databaseSizeBeforeCreate + 1);
        FormulaItems testFormulaItems = formulaItemsList.get(formulaItemsList.size() - 1);
        assertThat(testFormulaItems.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);

        // Validate the FormulaItems in Elasticsearch
        verify(mockFormulaItemsSearchRepository, times(1)).save(testFormulaItems);
    }

    @Test
    @Transactional
    public void createFormulaItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formulaItemsRepository.findAll().size();

        // Create the FormulaItems with an existing ID
        formulaItems.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormulaItemsMockMvc.perform(post("/api/formula-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaItems)))
            .andExpect(status().isBadRequest());

        // Validate the FormulaItems in the database
        List<FormulaItems> formulaItemsList = formulaItemsRepository.findAll();
        assertThat(formulaItemsList).hasSize(databaseSizeBeforeCreate);

        // Validate the FormulaItems in Elasticsearch
        verify(mockFormulaItemsSearchRepository, times(0)).save(formulaItems);
    }


    @Test
    @Transactional
    public void getAllFormulaItems() throws Exception {
        // Initialize the database
        formulaItemsRepository.saveAndFlush(formulaItems);

        // Get all the formulaItemsList
        restFormulaItemsMockMvc.perform(get("/api/formula-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formulaItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)));
    }
    
    @Test
    @Transactional
    public void getFormulaItems() throws Exception {
        // Initialize the database
        formulaItemsRepository.saveAndFlush(formulaItems);

        // Get the formulaItems
        restFormulaItemsMockMvc.perform(get("/api/formula-items/{id}", formulaItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formulaItems.getId().intValue()))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingFormulaItems() throws Exception {
        // Get the formulaItems
        restFormulaItemsMockMvc.perform(get("/api/formula-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormulaItems() throws Exception {
        // Initialize the database
        formulaItemsService.save(formulaItems);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFormulaItemsSearchRepository);

        int databaseSizeBeforeUpdate = formulaItemsRepository.findAll().size();

        // Update the formulaItems
        FormulaItems updatedFormulaItems = formulaItemsRepository.findById(formulaItems.getId()).get();
        // Disconnect from session so that the updates on updatedFormulaItems are not directly saved in db
        em.detach(updatedFormulaItems);
        updatedFormulaItems
            .countryName(UPDATED_COUNTRY_NAME);

        restFormulaItemsMockMvc.perform(put("/api/formula-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormulaItems)))
            .andExpect(status().isOk());

        // Validate the FormulaItems in the database
        List<FormulaItems> formulaItemsList = formulaItemsRepository.findAll();
        assertThat(formulaItemsList).hasSize(databaseSizeBeforeUpdate);
        FormulaItems testFormulaItems = formulaItemsList.get(formulaItemsList.size() - 1);
        assertThat(testFormulaItems.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);

        // Validate the FormulaItems in Elasticsearch
        verify(mockFormulaItemsSearchRepository, times(1)).save(testFormulaItems);
    }

    @Test
    @Transactional
    public void updateNonExistingFormulaItems() throws Exception {
        int databaseSizeBeforeUpdate = formulaItemsRepository.findAll().size();

        // Create the FormulaItems

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaItemsMockMvc.perform(put("/api/formula-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaItems)))
            .andExpect(status().isBadRequest());

        // Validate the FormulaItems in the database
        List<FormulaItems> formulaItemsList = formulaItemsRepository.findAll();
        assertThat(formulaItemsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FormulaItems in Elasticsearch
        verify(mockFormulaItemsSearchRepository, times(0)).save(formulaItems);
    }

    @Test
    @Transactional
    public void deleteFormulaItems() throws Exception {
        // Initialize the database
        formulaItemsService.save(formulaItems);

        int databaseSizeBeforeDelete = formulaItemsRepository.findAll().size();

        // Delete the formulaItems
        restFormulaItemsMockMvc.perform(delete("/api/formula-items/{id}", formulaItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormulaItems> formulaItemsList = formulaItemsRepository.findAll();
        assertThat(formulaItemsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FormulaItems in Elasticsearch
        verify(mockFormulaItemsSearchRepository, times(1)).deleteById(formulaItems.getId());
    }

    @Test
    @Transactional
    public void searchFormulaItems() throws Exception {
        // Initialize the database
        formulaItemsService.save(formulaItems);
        when(mockFormulaItemsSearchRepository.search(queryStringQuery("id:" + formulaItems.getId())))
            .thenReturn(Collections.singletonList(formulaItems));
        // Search the formulaItems
        restFormulaItemsMockMvc.perform(get("/api/_search/formula-items?query=id:" + formulaItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formulaItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)));
    }
}
