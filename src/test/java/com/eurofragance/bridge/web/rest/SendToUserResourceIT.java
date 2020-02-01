package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.BridgeApp;
import com.eurofragance.bridge.config.TestSecurityConfiguration;
import com.eurofragance.bridge.domain.SendToUser;
import com.eurofragance.bridge.repository.SendToUserRepository;
import com.eurofragance.bridge.repository.search.SendToUserSearchRepository;
import com.eurofragance.bridge.service.SendToUserService;
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
 * Integration tests for the {@link SendToUserResource} REST controller.
 */
@SpringBootTest(classes = {BridgeApp.class, TestSecurityConfiguration.class})
public class SendToUserResourceIT {

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SendToUserRepository sendToUserRepository;

    @Autowired
    private SendToUserService sendToUserService;

    /**
     * This repository is mocked in the com.eurofragance.bridge.repository.search test package.
     *
     * @see com.eurofragance.bridge.repository.search.SendToUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private SendToUserSearchRepository mockSendToUserSearchRepository;

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

    private MockMvc restSendToUserMockMvc;

    private SendToUser sendToUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SendToUserResource sendToUserResource = new SendToUserResource(sendToUserService);
        this.restSendToUserMockMvc = MockMvcBuilders.standaloneSetup(sendToUserResource)
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
    public static SendToUser createEntity(EntityManager em) {
        SendToUser sendToUser = new SendToUser()
            .creationDate(DEFAULT_CREATION_DATE);
        return sendToUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SendToUser createUpdatedEntity(EntityManager em) {
        SendToUser sendToUser = new SendToUser()
            .creationDate(UPDATED_CREATION_DATE);
        return sendToUser;
    }

    @BeforeEach
    public void initTest() {
        sendToUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createSendToUser() throws Exception {
        int databaseSizeBeforeCreate = sendToUserRepository.findAll().size();

        // Create the SendToUser
        restSendToUserMockMvc.perform(post("/api/send-to-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendToUser)))
            .andExpect(status().isCreated());

        // Validate the SendToUser in the database
        List<SendToUser> sendToUserList = sendToUserRepository.findAll();
        assertThat(sendToUserList).hasSize(databaseSizeBeforeCreate + 1);
        SendToUser testSendToUser = sendToUserList.get(sendToUserList.size() - 1);
        assertThat(testSendToUser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);

        // Validate the SendToUser in Elasticsearch
        verify(mockSendToUserSearchRepository, times(1)).save(testSendToUser);
    }

    @Test
    @Transactional
    public void createSendToUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sendToUserRepository.findAll().size();

        // Create the SendToUser with an existing ID
        sendToUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSendToUserMockMvc.perform(post("/api/send-to-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendToUser)))
            .andExpect(status().isBadRequest());

        // Validate the SendToUser in the database
        List<SendToUser> sendToUserList = sendToUserRepository.findAll();
        assertThat(sendToUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the SendToUser in Elasticsearch
        verify(mockSendToUserSearchRepository, times(0)).save(sendToUser);
    }


    @Test
    @Transactional
    public void getAllSendToUsers() throws Exception {
        // Initialize the database
        sendToUserRepository.saveAndFlush(sendToUser);

        // Get all the sendToUserList
        restSendToUserMockMvc.perform(get("/api/send-to-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sendToUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getSendToUser() throws Exception {
        // Initialize the database
        sendToUserRepository.saveAndFlush(sendToUser);

        // Get the sendToUser
        restSendToUserMockMvc.perform(get("/api/send-to-users/{id}", sendToUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sendToUser.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSendToUser() throws Exception {
        // Get the sendToUser
        restSendToUserMockMvc.perform(get("/api/send-to-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSendToUser() throws Exception {
        // Initialize the database
        sendToUserService.save(sendToUser);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSendToUserSearchRepository);

        int databaseSizeBeforeUpdate = sendToUserRepository.findAll().size();

        // Update the sendToUser
        SendToUser updatedSendToUser = sendToUserRepository.findById(sendToUser.getId()).get();
        // Disconnect from session so that the updates on updatedSendToUser are not directly saved in db
        em.detach(updatedSendToUser);
        updatedSendToUser
            .creationDate(UPDATED_CREATION_DATE);

        restSendToUserMockMvc.perform(put("/api/send-to-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSendToUser)))
            .andExpect(status().isOk());

        // Validate the SendToUser in the database
        List<SendToUser> sendToUserList = sendToUserRepository.findAll();
        assertThat(sendToUserList).hasSize(databaseSizeBeforeUpdate);
        SendToUser testSendToUser = sendToUserList.get(sendToUserList.size() - 1);
        assertThat(testSendToUser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);

        // Validate the SendToUser in Elasticsearch
        verify(mockSendToUserSearchRepository, times(1)).save(testSendToUser);
    }

    @Test
    @Transactional
    public void updateNonExistingSendToUser() throws Exception {
        int databaseSizeBeforeUpdate = sendToUserRepository.findAll().size();

        // Create the SendToUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSendToUserMockMvc.perform(put("/api/send-to-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendToUser)))
            .andExpect(status().isBadRequest());

        // Validate the SendToUser in the database
        List<SendToUser> sendToUserList = sendToUserRepository.findAll();
        assertThat(sendToUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SendToUser in Elasticsearch
        verify(mockSendToUserSearchRepository, times(0)).save(sendToUser);
    }

    @Test
    @Transactional
    public void deleteSendToUser() throws Exception {
        // Initialize the database
        sendToUserService.save(sendToUser);

        int databaseSizeBeforeDelete = sendToUserRepository.findAll().size();

        // Delete the sendToUser
        restSendToUserMockMvc.perform(delete("/api/send-to-users/{id}", sendToUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SendToUser> sendToUserList = sendToUserRepository.findAll();
        assertThat(sendToUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SendToUser in Elasticsearch
        verify(mockSendToUserSearchRepository, times(1)).deleteById(sendToUser.getId());
    }

    @Test
    @Transactional
    public void searchSendToUser() throws Exception {
        // Initialize the database
        sendToUserService.save(sendToUser);
        when(mockSendToUserSearchRepository.search(queryStringQuery("id:" + sendToUser.getId())))
            .thenReturn(Collections.singletonList(sendToUser));
        // Search the sendToUser
        restSendToUserMockMvc.perform(get("/api/_search/send-to-users?query=id:" + sendToUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sendToUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
}
