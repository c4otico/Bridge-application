package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.BridgeApp;
import com.eurofragance.bridge.config.TestSecurityConfiguration;
import com.eurofragance.bridge.domain.Developer;
import com.eurofragance.bridge.repository.DeveloperRepository;
import com.eurofragance.bridge.repository.search.DeveloperSearchRepository;
import com.eurofragance.bridge.service.DeveloperService;
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
 * Integration tests for the {@link DeveloperResource} REST controller.
 */
@SpringBootTest(classes = {BridgeApp.class, TestSecurityConfiguration.class})
public class DeveloperResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INITIALS = "AAA";
    private static final String UPDATED_INITIALS = "BBB";

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private DeveloperService developerService;

    /**
     * This repository is mocked in the com.eurofragance.bridge.repository.search test package.
     *
     * @see com.eurofragance.bridge.repository.search.DeveloperSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeveloperSearchRepository mockDeveloperSearchRepository;

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

    private MockMvc restDeveloperMockMvc;

    private Developer developer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeveloperResource developerResource = new DeveloperResource(developerService);
        this.restDeveloperMockMvc = MockMvcBuilders.standaloneSetup(developerResource)
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
    public static Developer createEntity(EntityManager em) {
        Developer developer = new Developer()
            .name(DEFAULT_NAME)
            .initials(DEFAULT_INITIALS);
        return developer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Developer createUpdatedEntity(EntityManager em) {
        Developer developer = new Developer()
            .name(UPDATED_NAME)
            .initials(UPDATED_INITIALS);
        return developer;
    }

    @BeforeEach
    public void initTest() {
        developer = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeveloper() throws Exception {
        int databaseSizeBeforeCreate = developerRepository.findAll().size();

        // Create the Developer
        restDeveloperMockMvc.perform(post("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developer)))
            .andExpect(status().isCreated());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeCreate + 1);
        Developer testDeveloper = developerList.get(developerList.size() - 1);
        assertThat(testDeveloper.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeveloper.getInitials()).isEqualTo(DEFAULT_INITIALS);

        // Validate the Developer in Elasticsearch
        verify(mockDeveloperSearchRepository, times(1)).save(testDeveloper);
    }

    @Test
    @Transactional
    public void createDeveloperWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = developerRepository.findAll().size();

        // Create the Developer with an existing ID
        developer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeveloperMockMvc.perform(post("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developer)))
            .andExpect(status().isBadRequest());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Developer in Elasticsearch
        verify(mockDeveloperSearchRepository, times(0)).save(developer);
    }


    @Test
    @Transactional
    public void getAllDevelopers() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);

        // Get all the developerList
        restDeveloperMockMvc.perform(get("/api/developers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(developer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].initials").value(hasItem(DEFAULT_INITIALS)));
    }
    
    @Test
    @Transactional
    public void getDeveloper() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);

        // Get the developer
        restDeveloperMockMvc.perform(get("/api/developers/{id}", developer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(developer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.initials").value(DEFAULT_INITIALS));
    }

    @Test
    @Transactional
    public void getNonExistingDeveloper() throws Exception {
        // Get the developer
        restDeveloperMockMvc.perform(get("/api/developers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeveloper() throws Exception {
        // Initialize the database
        developerService.save(developer);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDeveloperSearchRepository);

        int databaseSizeBeforeUpdate = developerRepository.findAll().size();

        // Update the developer
        Developer updatedDeveloper = developerRepository.findById(developer.getId()).get();
        // Disconnect from session so that the updates on updatedDeveloper are not directly saved in db
        em.detach(updatedDeveloper);
        updatedDeveloper
            .name(UPDATED_NAME)
            .initials(UPDATED_INITIALS);

        restDeveloperMockMvc.perform(put("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeveloper)))
            .andExpect(status().isOk());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeUpdate);
        Developer testDeveloper = developerList.get(developerList.size() - 1);
        assertThat(testDeveloper.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeveloper.getInitials()).isEqualTo(UPDATED_INITIALS);

        // Validate the Developer in Elasticsearch
        verify(mockDeveloperSearchRepository, times(1)).save(testDeveloper);
    }

    @Test
    @Transactional
    public void updateNonExistingDeveloper() throws Exception {
        int databaseSizeBeforeUpdate = developerRepository.findAll().size();

        // Create the Developer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeveloperMockMvc.perform(put("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developer)))
            .andExpect(status().isBadRequest());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Developer in Elasticsearch
        verify(mockDeveloperSearchRepository, times(0)).save(developer);
    }

    @Test
    @Transactional
    public void deleteDeveloper() throws Exception {
        // Initialize the database
        developerService.save(developer);

        int databaseSizeBeforeDelete = developerRepository.findAll().size();

        // Delete the developer
        restDeveloperMockMvc.perform(delete("/api/developers/{id}", developer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Developer in Elasticsearch
        verify(mockDeveloperSearchRepository, times(1)).deleteById(developer.getId());
    }

    @Test
    @Transactional
    public void searchDeveloper() throws Exception {
        // Initialize the database
        developerService.save(developer);
        when(mockDeveloperSearchRepository.search(queryStringQuery("id:" + developer.getId())))
            .thenReturn(Collections.singletonList(developer));
        // Search the developer
        restDeveloperMockMvc.perform(get("/api/_search/developers?query=id:" + developer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(developer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].initials").value(hasItem(DEFAULT_INITIALS)));
    }
}
