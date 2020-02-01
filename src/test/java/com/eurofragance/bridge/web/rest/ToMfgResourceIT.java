package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.BridgeApp;
import com.eurofragance.bridge.config.TestSecurityConfiguration;
import com.eurofragance.bridge.domain.ToMfg;
import com.eurofragance.bridge.repository.ToMfgRepository;
import com.eurofragance.bridge.repository.search.ToMfgSearchRepository;
import com.eurofragance.bridge.service.ToMfgService;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ToMfgResource} REST controller.
 */
@SpringBootTest(classes = {BridgeApp.class, TestSecurityConfiguration.class})
public class ToMfgResourceIT {

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ToMfgRepository toMfgRepository;

    @Autowired
    private ToMfgService toMfgService;

    /**
     * This repository is mocked in the com.eurofragance.bridge.repository.search test package.
     *
     * @see com.eurofragance.bridge.repository.search.ToMfgSearchRepositoryMockConfiguration
     */
    @Autowired
    private ToMfgSearchRepository mockToMfgSearchRepository;

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

    private MockMvc restToMfgMockMvc;

    private ToMfg toMfg;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ToMfgResource toMfgResource = new ToMfgResource(toMfgService);
        this.restToMfgMockMvc = MockMvcBuilders.standaloneSetup(toMfgResource)
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
    public static ToMfg createEntity(EntityManager em) {
        ToMfg toMfg = new ToMfg()
            .creationDate(DEFAULT_CREATION_DATE);
        return toMfg;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToMfg createUpdatedEntity(EntityManager em) {
        ToMfg toMfg = new ToMfg()
            .creationDate(UPDATED_CREATION_DATE);
        return toMfg;
    }

    @BeforeEach
    public void initTest() {
        toMfg = createEntity(em);
    }

    @Test
    @Transactional
    public void createToMfg() throws Exception {
        int databaseSizeBeforeCreate = toMfgRepository.findAll().size();

        // Create the ToMfg
        restToMfgMockMvc.perform(post("/api/to-mfgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toMfg)))
            .andExpect(status().isCreated());

        // Validate the ToMfg in the database
        List<ToMfg> toMfgList = toMfgRepository.findAll();
        assertThat(toMfgList).hasSize(databaseSizeBeforeCreate + 1);
        ToMfg testToMfg = toMfgList.get(toMfgList.size() - 1);
        assertThat(testToMfg.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);

        // Validate the ToMfg in Elasticsearch
        verify(mockToMfgSearchRepository, times(1)).save(testToMfg);
    }

    @Test
    @Transactional
    public void createToMfgWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = toMfgRepository.findAll().size();

        // Create the ToMfg with an existing ID
        toMfg.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restToMfgMockMvc.perform(post("/api/to-mfgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toMfg)))
            .andExpect(status().isBadRequest());

        // Validate the ToMfg in the database
        List<ToMfg> toMfgList = toMfgRepository.findAll();
        assertThat(toMfgList).hasSize(databaseSizeBeforeCreate);

        // Validate the ToMfg in Elasticsearch
        verify(mockToMfgSearchRepository, times(0)).save(toMfg);
    }


    @Test
    @Transactional
    public void getAllToMfgs() throws Exception {
        // Initialize the database
        toMfgRepository.saveAndFlush(toMfg);

        // Get all the toMfgList
        restToMfgMockMvc.perform(get("/api/to-mfgs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toMfg.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getToMfg() throws Exception {
        // Initialize the database
        toMfgRepository.saveAndFlush(toMfg);

        // Get the toMfg
        restToMfgMockMvc.perform(get("/api/to-mfgs/{id}", toMfg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(toMfg.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingToMfg() throws Exception {
        // Get the toMfg
        restToMfgMockMvc.perform(get("/api/to-mfgs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateToMfg() throws Exception {
        // Initialize the database
        toMfgService.save(toMfg);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockToMfgSearchRepository);

        int databaseSizeBeforeUpdate = toMfgRepository.findAll().size();

        // Update the toMfg
        ToMfg updatedToMfg = toMfgRepository.findById(toMfg.getId()).get();
        // Disconnect from session so that the updates on updatedToMfg are not directly saved in db
        em.detach(updatedToMfg);
        updatedToMfg
            .creationDate(UPDATED_CREATION_DATE);

        restToMfgMockMvc.perform(put("/api/to-mfgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedToMfg)))
            .andExpect(status().isOk());

        // Validate the ToMfg in the database
        List<ToMfg> toMfgList = toMfgRepository.findAll();
        assertThat(toMfgList).hasSize(databaseSizeBeforeUpdate);
        ToMfg testToMfg = toMfgList.get(toMfgList.size() - 1);
        assertThat(testToMfg.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);

        // Validate the ToMfg in Elasticsearch
        verify(mockToMfgSearchRepository, times(1)).save(testToMfg);
    }

    @Test
    @Transactional
    public void updateNonExistingToMfg() throws Exception {
        int databaseSizeBeforeUpdate = toMfgRepository.findAll().size();

        // Create the ToMfg

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToMfgMockMvc.perform(put("/api/to-mfgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toMfg)))
            .andExpect(status().isBadRequest());

        // Validate the ToMfg in the database
        List<ToMfg> toMfgList = toMfgRepository.findAll();
        assertThat(toMfgList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ToMfg in Elasticsearch
        verify(mockToMfgSearchRepository, times(0)).save(toMfg);
    }

    @Test
    @Transactional
    public void deleteToMfg() throws Exception {
        // Initialize the database
        toMfgService.save(toMfg);

        int databaseSizeBeforeDelete = toMfgRepository.findAll().size();

        // Delete the toMfg
        restToMfgMockMvc.perform(delete("/api/to-mfgs/{id}", toMfg.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ToMfg> toMfgList = toMfgRepository.findAll();
        assertThat(toMfgList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ToMfg in Elasticsearch
        verify(mockToMfgSearchRepository, times(1)).deleteById(toMfg.getId());
    }

    @Test
    @Transactional
    public void searchToMfg() throws Exception {
        // Initialize the database
        toMfgService.save(toMfg);
        when(mockToMfgSearchRepository.search(queryStringQuery("id:" + toMfg.getId())))
            .thenReturn(Collections.singletonList(toMfg));
        // Search the toMfg
        restToMfgMockMvc.perform(get("/api/_search/to-mfgs?query=id:" + toMfg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toMfg.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
}
