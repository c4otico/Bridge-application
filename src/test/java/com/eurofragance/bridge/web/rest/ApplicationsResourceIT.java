package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.BridgeApp;
import com.eurofragance.bridge.config.TestSecurityConfiguration;
import com.eurofragance.bridge.domain.Applications;
import com.eurofragance.bridge.repository.ApplicationsRepository;
import com.eurofragance.bridge.repository.search.ApplicationsSearchRepository;
import com.eurofragance.bridge.service.ApplicationsService;
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
 * Integration tests for the {@link ApplicationsResource} REST controller.
 */
@SpringBootTest(classes = {BridgeApp.class, TestSecurityConfiguration.class})
public class ApplicationsResourceIT {

    private static final String DEFAULT_APPLICATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_NAME = "BBBBBBBBBB";

    @Autowired
    private ApplicationsRepository applicationsRepository;

    @Autowired
    private ApplicationsService applicationsService;

    /**
     * This repository is mocked in the com.eurofragance.bridge.repository.search test package.
     *
     * @see com.eurofragance.bridge.repository.search.ApplicationsSearchRepositoryMockConfiguration
     */
    @Autowired
    private ApplicationsSearchRepository mockApplicationsSearchRepository;

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

    private MockMvc restApplicationsMockMvc;

    private Applications applications;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationsResource applicationsResource = new ApplicationsResource(applicationsService);
        this.restApplicationsMockMvc = MockMvcBuilders.standaloneSetup(applicationsResource)
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
    public static Applications createEntity(EntityManager em) {
        Applications applications = new Applications()
            .applicationName(DEFAULT_APPLICATION_NAME);
        return applications;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applications createUpdatedEntity(EntityManager em) {
        Applications applications = new Applications()
            .applicationName(UPDATED_APPLICATION_NAME);
        return applications;
    }

    @BeforeEach
    public void initTest() {
        applications = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplications() throws Exception {
        int databaseSizeBeforeCreate = applicationsRepository.findAll().size();

        // Create the Applications
        restApplicationsMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isCreated());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeCreate + 1);
        Applications testApplications = applicationsList.get(applicationsList.size() - 1);
        assertThat(testApplications.getApplicationName()).isEqualTo(DEFAULT_APPLICATION_NAME);

        // Validate the Applications in Elasticsearch
        verify(mockApplicationsSearchRepository, times(1)).save(testApplications);
    }

    @Test
    @Transactional
    public void createApplicationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationsRepository.findAll().size();

        // Create the Applications with an existing ID
        applications.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationsMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isBadRequest());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Applications in Elasticsearch
        verify(mockApplicationsSearchRepository, times(0)).save(applications);
    }


    @Test
    @Transactional
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList
        restApplicationsMockMvc.perform(get("/api/applications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applications.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationName").value(hasItem(DEFAULT_APPLICATION_NAME)));
    }
    
    @Test
    @Transactional
    public void getApplications() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get the applications
        restApplicationsMockMvc.perform(get("/api/applications/{id}", applications.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applications.getId().intValue()))
            .andExpect(jsonPath("$.applicationName").value(DEFAULT_APPLICATION_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingApplications() throws Exception {
        // Get the applications
        restApplicationsMockMvc.perform(get("/api/applications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplications() throws Exception {
        // Initialize the database
        applicationsService.save(applications);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockApplicationsSearchRepository);

        int databaseSizeBeforeUpdate = applicationsRepository.findAll().size();

        // Update the applications
        Applications updatedApplications = applicationsRepository.findById(applications.getId()).get();
        // Disconnect from session so that the updates on updatedApplications are not directly saved in db
        em.detach(updatedApplications);
        updatedApplications
            .applicationName(UPDATED_APPLICATION_NAME);

        restApplicationsMockMvc.perform(put("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplications)))
            .andExpect(status().isOk());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeUpdate);
        Applications testApplications = applicationsList.get(applicationsList.size() - 1);
        assertThat(testApplications.getApplicationName()).isEqualTo(UPDATED_APPLICATION_NAME);

        // Validate the Applications in Elasticsearch
        verify(mockApplicationsSearchRepository, times(1)).save(testApplications);
    }

    @Test
    @Transactional
    public void updateNonExistingApplications() throws Exception {
        int databaseSizeBeforeUpdate = applicationsRepository.findAll().size();

        // Create the Applications

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationsMockMvc.perform(put("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isBadRequest());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Applications in Elasticsearch
        verify(mockApplicationsSearchRepository, times(0)).save(applications);
    }

    @Test
    @Transactional
    public void deleteApplications() throws Exception {
        // Initialize the database
        applicationsService.save(applications);

        int databaseSizeBeforeDelete = applicationsRepository.findAll().size();

        // Delete the applications
        restApplicationsMockMvc.perform(delete("/api/applications/{id}", applications.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Applications in Elasticsearch
        verify(mockApplicationsSearchRepository, times(1)).deleteById(applications.getId());
    }

    @Test
    @Transactional
    public void searchApplications() throws Exception {
        // Initialize the database
        applicationsService.save(applications);
        when(mockApplicationsSearchRepository.search(queryStringQuery("id:" + applications.getId())))
            .thenReturn(Collections.singletonList(applications));
        // Search the applications
        restApplicationsMockMvc.perform(get("/api/_search/applications?query=id:" + applications.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applications.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationName").value(hasItem(DEFAULT_APPLICATION_NAME)));
    }
}
