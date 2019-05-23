package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.GeraiConfig;
import com.toko.maju.repository.GeraiConfigRepository;
import com.toko.maju.repository.search.GeraiConfigSearchRepository;
import com.toko.maju.service.GeraiConfigService;
import com.toko.maju.service.dto.GeraiConfigDTO;
import com.toko.maju.service.mapper.GeraiConfigMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.GeraiConfigCriteria;
import com.toko.maju.service.GeraiConfigQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.toko.maju.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GeraiConfigResource REST controller.
 *
 * @see GeraiConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class GeraiConfigResourceIntTest {

    private static final String DEFAULT_CODE_GERAI = "AAAAAAAAAA";
    private static final String UPDATED_CODE_GERAI = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_GERAI = "AAAAAAAAAA";
    private static final String UPDATED_NAME_GERAI = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_URL_TOKO = "AAAAAAAAAA";
    private static final String UPDATED_URL_TOKO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    @Autowired
    private GeraiConfigRepository geraiConfigRepository;

    @Autowired
    private GeraiConfigMapper geraiConfigMapper;

    @Autowired
    private GeraiConfigService geraiConfigService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.GeraiConfigSearchRepositoryMockConfiguration
     */
    @Autowired
    private GeraiConfigSearchRepository mockGeraiConfigSearchRepository;

    @Autowired
    private GeraiConfigQueryService geraiConfigQueryService;

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

    private MockMvc restGeraiConfigMockMvc;

    private GeraiConfig geraiConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GeraiConfigResource geraiConfigResource = new GeraiConfigResource(geraiConfigService, geraiConfigQueryService);
        this.restGeraiConfigMockMvc = MockMvcBuilders.standaloneSetup(geraiConfigResource)
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
    public static GeraiConfig createEntity(EntityManager em) {
        GeraiConfig geraiConfig = new GeraiConfig()
            .codeGerai(DEFAULT_CODE_GERAI)
            .nameGerai(DEFAULT_NAME_GERAI)
            .password(DEFAULT_PASSWORD)
            .urlToko(DEFAULT_URL_TOKO)
            .activated(DEFAULT_ACTIVATED);
        return geraiConfig;
    }

    @Before
    public void initTest() {
        geraiConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createGeraiConfig() throws Exception {
        int databaseSizeBeforeCreate = geraiConfigRepository.findAll().size();

        // Create the GeraiConfig
        GeraiConfigDTO geraiConfigDTO = geraiConfigMapper.toDto(geraiConfig);
        restGeraiConfigMockMvc.perform(post("/api/gerai-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the GeraiConfig in the database
        List<GeraiConfig> geraiConfigList = geraiConfigRepository.findAll();
        assertThat(geraiConfigList).hasSize(databaseSizeBeforeCreate + 1);
        GeraiConfig testGeraiConfig = geraiConfigList.get(geraiConfigList.size() - 1);
        assertThat(testGeraiConfig.getCodeGerai()).isEqualTo(DEFAULT_CODE_GERAI);
        assertThat(testGeraiConfig.getNameGerai()).isEqualTo(DEFAULT_NAME_GERAI);
        assertThat(testGeraiConfig.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testGeraiConfig.getUrlToko()).isEqualTo(DEFAULT_URL_TOKO);
        assertThat(testGeraiConfig.isActivated()).isEqualTo(DEFAULT_ACTIVATED);

        // Validate the GeraiConfig in Elasticsearch
        verify(mockGeraiConfigSearchRepository, times(1)).save(testGeraiConfig);
    }

    @Test
    @Transactional
    public void createGeraiConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = geraiConfigRepository.findAll().size();

        // Create the GeraiConfig with an existing ID
        geraiConfig.setId(1L);
        GeraiConfigDTO geraiConfigDTO = geraiConfigMapper.toDto(geraiConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeraiConfigMockMvc.perform(post("/api/gerai-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeraiConfig in the database
        List<GeraiConfig> geraiConfigList = geraiConfigRepository.findAll();
        assertThat(geraiConfigList).hasSize(databaseSizeBeforeCreate);

        // Validate the GeraiConfig in Elasticsearch
        verify(mockGeraiConfigSearchRepository, times(0)).save(geraiConfig);
    }

    @Test
    @Transactional
    public void checkCodeGeraiIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiConfigRepository.findAll().size();
        // set the field null
        geraiConfig.setCodeGerai(null);

        // Create the GeraiConfig, which fails.
        GeraiConfigDTO geraiConfigDTO = geraiConfigMapper.toDto(geraiConfig);

        restGeraiConfigMockMvc.perform(post("/api/gerai-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiConfigDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiConfig> geraiConfigList = geraiConfigRepository.findAll();
        assertThat(geraiConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameGeraiIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiConfigRepository.findAll().size();
        // set the field null
        geraiConfig.setNameGerai(null);

        // Create the GeraiConfig, which fails.
        GeraiConfigDTO geraiConfigDTO = geraiConfigMapper.toDto(geraiConfig);

        restGeraiConfigMockMvc.perform(post("/api/gerai-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiConfigDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiConfig> geraiConfigList = geraiConfigRepository.findAll();
        assertThat(geraiConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiConfigRepository.findAll().size();
        // set the field null
        geraiConfig.setPassword(null);

        // Create the GeraiConfig, which fails.
        GeraiConfigDTO geraiConfigDTO = geraiConfigMapper.toDto(geraiConfig);

        restGeraiConfigMockMvc.perform(post("/api/gerai-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiConfigDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiConfig> geraiConfigList = geraiConfigRepository.findAll();
        assertThat(geraiConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlTokoIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiConfigRepository.findAll().size();
        // set the field null
        geraiConfig.setUrlToko(null);

        // Create the GeraiConfig, which fails.
        GeraiConfigDTO geraiConfigDTO = geraiConfigMapper.toDto(geraiConfig);

        restGeraiConfigMockMvc.perform(post("/api/gerai-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiConfigDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiConfig> geraiConfigList = geraiConfigRepository.findAll();
        assertThat(geraiConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiConfigRepository.findAll().size();
        // set the field null
        geraiConfig.setActivated(null);

        // Create the GeraiConfig, which fails.
        GeraiConfigDTO geraiConfigDTO = geraiConfigMapper.toDto(geraiConfig);

        restGeraiConfigMockMvc.perform(post("/api/gerai-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiConfigDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiConfig> geraiConfigList = geraiConfigRepository.findAll();
        assertThat(geraiConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigs() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList
        restGeraiConfigMockMvc.perform(get("/api/gerai-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraiConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeGerai").value(hasItem(DEFAULT_CODE_GERAI.toString())))
            .andExpect(jsonPath("$.[*].nameGerai").value(hasItem(DEFAULT_NAME_GERAI.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].urlToko").value(hasItem(DEFAULT_URL_TOKO.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getGeraiConfig() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get the geraiConfig
        restGeraiConfigMockMvc.perform(get("/api/gerai-configs/{id}", geraiConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(geraiConfig.getId().intValue()))
            .andExpect(jsonPath("$.codeGerai").value(DEFAULT_CODE_GERAI.toString()))
            .andExpect(jsonPath("$.nameGerai").value(DEFAULT_NAME_GERAI.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.urlToko").value(DEFAULT_URL_TOKO.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByCodeGeraiIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where codeGerai equals to DEFAULT_CODE_GERAI
        defaultGeraiConfigShouldBeFound("codeGerai.equals=" + DEFAULT_CODE_GERAI);

        // Get all the geraiConfigList where codeGerai equals to UPDATED_CODE_GERAI
        defaultGeraiConfigShouldNotBeFound("codeGerai.equals=" + UPDATED_CODE_GERAI);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByCodeGeraiIsInShouldWork() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where codeGerai in DEFAULT_CODE_GERAI or UPDATED_CODE_GERAI
        defaultGeraiConfigShouldBeFound("codeGerai.in=" + DEFAULT_CODE_GERAI + "," + UPDATED_CODE_GERAI);

        // Get all the geraiConfigList where codeGerai equals to UPDATED_CODE_GERAI
        defaultGeraiConfigShouldNotBeFound("codeGerai.in=" + UPDATED_CODE_GERAI);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByCodeGeraiIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where codeGerai is not null
        defaultGeraiConfigShouldBeFound("codeGerai.specified=true");

        // Get all the geraiConfigList where codeGerai is null
        defaultGeraiConfigShouldNotBeFound("codeGerai.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByNameGeraiIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where nameGerai equals to DEFAULT_NAME_GERAI
        defaultGeraiConfigShouldBeFound("nameGerai.equals=" + DEFAULT_NAME_GERAI);

        // Get all the geraiConfigList where nameGerai equals to UPDATED_NAME_GERAI
        defaultGeraiConfigShouldNotBeFound("nameGerai.equals=" + UPDATED_NAME_GERAI);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByNameGeraiIsInShouldWork() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where nameGerai in DEFAULT_NAME_GERAI or UPDATED_NAME_GERAI
        defaultGeraiConfigShouldBeFound("nameGerai.in=" + DEFAULT_NAME_GERAI + "," + UPDATED_NAME_GERAI);

        // Get all the geraiConfigList where nameGerai equals to UPDATED_NAME_GERAI
        defaultGeraiConfigShouldNotBeFound("nameGerai.in=" + UPDATED_NAME_GERAI);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByNameGeraiIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where nameGerai is not null
        defaultGeraiConfigShouldBeFound("nameGerai.specified=true");

        // Get all the geraiConfigList where nameGerai is null
        defaultGeraiConfigShouldNotBeFound("nameGerai.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where password equals to DEFAULT_PASSWORD
        defaultGeraiConfigShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the geraiConfigList where password equals to UPDATED_PASSWORD
        defaultGeraiConfigShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultGeraiConfigShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the geraiConfigList where password equals to UPDATED_PASSWORD
        defaultGeraiConfigShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where password is not null
        defaultGeraiConfigShouldBeFound("password.specified=true");

        // Get all the geraiConfigList where password is null
        defaultGeraiConfigShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByUrlTokoIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where urlToko equals to DEFAULT_URL_TOKO
        defaultGeraiConfigShouldBeFound("urlToko.equals=" + DEFAULT_URL_TOKO);

        // Get all the geraiConfigList where urlToko equals to UPDATED_URL_TOKO
        defaultGeraiConfigShouldNotBeFound("urlToko.equals=" + UPDATED_URL_TOKO);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByUrlTokoIsInShouldWork() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where urlToko in DEFAULT_URL_TOKO or UPDATED_URL_TOKO
        defaultGeraiConfigShouldBeFound("urlToko.in=" + DEFAULT_URL_TOKO + "," + UPDATED_URL_TOKO);

        // Get all the geraiConfigList where urlToko equals to UPDATED_URL_TOKO
        defaultGeraiConfigShouldNotBeFound("urlToko.in=" + UPDATED_URL_TOKO);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByUrlTokoIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where urlToko is not null
        defaultGeraiConfigShouldBeFound("urlToko.specified=true");

        // Get all the geraiConfigList where urlToko is null
        defaultGeraiConfigShouldNotBeFound("urlToko.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where activated equals to DEFAULT_ACTIVATED
        defaultGeraiConfigShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the geraiConfigList where activated equals to UPDATED_ACTIVATED
        defaultGeraiConfigShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultGeraiConfigShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the geraiConfigList where activated equals to UPDATED_ACTIVATED
        defaultGeraiConfigShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllGeraiConfigsByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        // Get all the geraiConfigList where activated is not null
        defaultGeraiConfigShouldBeFound("activated.specified=true");

        // Get all the geraiConfigList where activated is null
        defaultGeraiConfigShouldNotBeFound("activated.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGeraiConfigShouldBeFound(String filter) throws Exception {
        restGeraiConfigMockMvc.perform(get("/api/gerai-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraiConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeGerai").value(hasItem(DEFAULT_CODE_GERAI)))
            .andExpect(jsonPath("$.[*].nameGerai").value(hasItem(DEFAULT_NAME_GERAI)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].urlToko").value(hasItem(DEFAULT_URL_TOKO)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));

        // Check, that the count call also returns 1
        restGeraiConfigMockMvc.perform(get("/api/gerai-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGeraiConfigShouldNotBeFound(String filter) throws Exception {
        restGeraiConfigMockMvc.perform(get("/api/gerai-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeraiConfigMockMvc.perform(get("/api/gerai-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGeraiConfig() throws Exception {
        // Get the geraiConfig
        restGeraiConfigMockMvc.perform(get("/api/gerai-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeraiConfig() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        int databaseSizeBeforeUpdate = geraiConfigRepository.findAll().size();

        // Update the geraiConfig
        GeraiConfig updatedGeraiConfig = geraiConfigRepository.findById(geraiConfig.getId()).get();
        // Disconnect from session so that the updates on updatedGeraiConfig are not directly saved in db
        em.detach(updatedGeraiConfig);
        updatedGeraiConfig
            .codeGerai(UPDATED_CODE_GERAI)
            .nameGerai(UPDATED_NAME_GERAI)
            .password(UPDATED_PASSWORD)
            .urlToko(UPDATED_URL_TOKO)
            .activated(UPDATED_ACTIVATED);
        GeraiConfigDTO geraiConfigDTO = geraiConfigMapper.toDto(updatedGeraiConfig);

        restGeraiConfigMockMvc.perform(put("/api/gerai-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiConfigDTO)))
            .andExpect(status().isOk());

        // Validate the GeraiConfig in the database
        List<GeraiConfig> geraiConfigList = geraiConfigRepository.findAll();
        assertThat(geraiConfigList).hasSize(databaseSizeBeforeUpdate);
        GeraiConfig testGeraiConfig = geraiConfigList.get(geraiConfigList.size() - 1);
        assertThat(testGeraiConfig.getCodeGerai()).isEqualTo(UPDATED_CODE_GERAI);
        assertThat(testGeraiConfig.getNameGerai()).isEqualTo(UPDATED_NAME_GERAI);
        assertThat(testGeraiConfig.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testGeraiConfig.getUrlToko()).isEqualTo(UPDATED_URL_TOKO);
        assertThat(testGeraiConfig.isActivated()).isEqualTo(UPDATED_ACTIVATED);

        // Validate the GeraiConfig in Elasticsearch
        verify(mockGeraiConfigSearchRepository, times(1)).save(testGeraiConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingGeraiConfig() throws Exception {
        int databaseSizeBeforeUpdate = geraiConfigRepository.findAll().size();

        // Create the GeraiConfig
        GeraiConfigDTO geraiConfigDTO = geraiConfigMapper.toDto(geraiConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeraiConfigMockMvc.perform(put("/api/gerai-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeraiConfig in the database
        List<GeraiConfig> geraiConfigList = geraiConfigRepository.findAll();
        assertThat(geraiConfigList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GeraiConfig in Elasticsearch
        verify(mockGeraiConfigSearchRepository, times(0)).save(geraiConfig);
    }

    @Test
    @Transactional
    public void deleteGeraiConfig() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);

        int databaseSizeBeforeDelete = geraiConfigRepository.findAll().size();

        // Delete the geraiConfig
        restGeraiConfigMockMvc.perform(delete("/api/gerai-configs/{id}", geraiConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GeraiConfig> geraiConfigList = geraiConfigRepository.findAll();
        assertThat(geraiConfigList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GeraiConfig in Elasticsearch
        verify(mockGeraiConfigSearchRepository, times(1)).deleteById(geraiConfig.getId());
    }

    @Test
    @Transactional
    public void searchGeraiConfig() throws Exception {
        // Initialize the database
        geraiConfigRepository.saveAndFlush(geraiConfig);
        when(mockGeraiConfigSearchRepository.search(queryStringQuery("id:" + geraiConfig.getId())))
            .thenReturn(Collections.singletonList(geraiConfig));
        // Search the geraiConfig
        restGeraiConfigMockMvc.perform(get("/api/_search/gerai-configs?query=id:" + geraiConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraiConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeGerai").value(hasItem(DEFAULT_CODE_GERAI)))
            .andExpect(jsonPath("$.[*].nameGerai").value(hasItem(DEFAULT_NAME_GERAI)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].urlToko").value(hasItem(DEFAULT_URL_TOKO)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeraiConfig.class);
        GeraiConfig geraiConfig1 = new GeraiConfig();
        geraiConfig1.setId(1L);
        GeraiConfig geraiConfig2 = new GeraiConfig();
        geraiConfig2.setId(geraiConfig1.getId());
        assertThat(geraiConfig1).isEqualTo(geraiConfig2);
        geraiConfig2.setId(2L);
        assertThat(geraiConfig1).isNotEqualTo(geraiConfig2);
        geraiConfig1.setId(null);
        assertThat(geraiConfig1).isNotEqualTo(geraiConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeraiConfigDTO.class);
        GeraiConfigDTO geraiConfigDTO1 = new GeraiConfigDTO();
        geraiConfigDTO1.setId(1L);
        GeraiConfigDTO geraiConfigDTO2 = new GeraiConfigDTO();
        assertThat(geraiConfigDTO1).isNotEqualTo(geraiConfigDTO2);
        geraiConfigDTO2.setId(geraiConfigDTO1.getId());
        assertThat(geraiConfigDTO1).isEqualTo(geraiConfigDTO2);
        geraiConfigDTO2.setId(2L);
        assertThat(geraiConfigDTO1).isNotEqualTo(geraiConfigDTO2);
        geraiConfigDTO1.setId(null);
        assertThat(geraiConfigDTO1).isNotEqualTo(geraiConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(geraiConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(geraiConfigMapper.fromId(null)).isNull();
    }
}
