package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.Gerai;
import com.toko.maju.domain.User;
import com.toko.maju.repository.GeraiRepository;
import com.toko.maju.repository.search.GeraiSearchRepository;
import com.toko.maju.service.GeraiService;
import com.toko.maju.service.dto.GeraiDTO;
import com.toko.maju.service.mapper.GeraiMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.GeraiCriteria;
import com.toko.maju.service.GeraiQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the GeraiResource REST controller.
 *
 * @see GeraiResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class GeraiResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private GeraiRepository geraiRepository;

    @Autowired
    private GeraiMapper geraiMapper;

    @Autowired
    private GeraiService geraiService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.GeraiSearchRepositoryMockConfiguration
     */
    @Autowired
    private GeraiSearchRepository mockGeraiSearchRepository;

    @Autowired
    private GeraiQueryService geraiQueryService;

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

    private MockMvc restGeraiMockMvc;

    private Gerai gerai;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GeraiResource geraiResource = new GeraiResource(geraiService, geraiQueryService);
        this.restGeraiMockMvc = MockMvcBuilders.standaloneSetup(geraiResource)
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
    public static Gerai createEntity(EntityManager em) {
        Gerai gerai = new Gerai()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .password(DEFAULT_PASSWORD)
            .createdDate(DEFAULT_CREATED_DATE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        gerai.setCreator(user);
        return gerai;
    }

    @Before
    public void initTest() {
        gerai = createEntity(em);
    }

    @Test
    @Transactional
    public void createGerai() throws Exception {
        int databaseSizeBeforeCreate = geraiRepository.findAll().size();

        // Create the Gerai
        GeraiDTO geraiDTO = geraiMapper.toDto(gerai);
        restGeraiMockMvc.perform(post("/api/gerais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiDTO)))
            .andExpect(status().isCreated());

        // Validate the Gerai in the database
        List<Gerai> geraiList = geraiRepository.findAll();
        assertThat(geraiList).hasSize(databaseSizeBeforeCreate + 1);
        Gerai testGerai = geraiList.get(geraiList.size() - 1);
        assertThat(testGerai.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testGerai.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGerai.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testGerai.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testGerai.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);

        // Validate the Gerai in Elasticsearch
        verify(mockGeraiSearchRepository, times(1)).save(testGerai);
    }

    @Test
    @Transactional
    public void createGeraiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = geraiRepository.findAll().size();

        // Create the Gerai with an existing ID
        gerai.setId(1L);
        GeraiDTO geraiDTO = geraiMapper.toDto(gerai);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeraiMockMvc.perform(post("/api/gerais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gerai in the database
        List<Gerai> geraiList = geraiRepository.findAll();
        assertThat(geraiList).hasSize(databaseSizeBeforeCreate);

        // Validate the Gerai in Elasticsearch
        verify(mockGeraiSearchRepository, times(0)).save(gerai);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiRepository.findAll().size();
        // set the field null
        gerai.setCode(null);

        // Create the Gerai, which fails.
        GeraiDTO geraiDTO = geraiMapper.toDto(gerai);

        restGeraiMockMvc.perform(post("/api/gerais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiDTO)))
            .andExpect(status().isBadRequest());

        List<Gerai> geraiList = geraiRepository.findAll();
        assertThat(geraiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiRepository.findAll().size();
        // set the field null
        gerai.setName(null);

        // Create the Gerai, which fails.
        GeraiDTO geraiDTO = geraiMapper.toDto(gerai);

        restGeraiMockMvc.perform(post("/api/gerais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiDTO)))
            .andExpect(status().isBadRequest());

        List<Gerai> geraiList = geraiRepository.findAll();
        assertThat(geraiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiRepository.findAll().size();
        // set the field null
        gerai.setLocation(null);

        // Create the Gerai, which fails.
        GeraiDTO geraiDTO = geraiMapper.toDto(gerai);

        restGeraiMockMvc.perform(post("/api/gerais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiDTO)))
            .andExpect(status().isBadRequest());

        List<Gerai> geraiList = geraiRepository.findAll();
        assertThat(geraiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiRepository.findAll().size();
        // set the field null
        gerai.setPassword(null);

        // Create the Gerai, which fails.
        GeraiDTO geraiDTO = geraiMapper.toDto(gerai);

        restGeraiMockMvc.perform(post("/api/gerais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiDTO)))
            .andExpect(status().isBadRequest());

        List<Gerai> geraiList = geraiRepository.findAll();
        assertThat(geraiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiRepository.findAll().size();
        // set the field null
        gerai.setCreatedDate(null);

        // Create the Gerai, which fails.
        GeraiDTO geraiDTO = geraiMapper.toDto(gerai);

        restGeraiMockMvc.perform(post("/api/gerais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiDTO)))
            .andExpect(status().isBadRequest());

        List<Gerai> geraiList = geraiRepository.findAll();
        assertThat(geraiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGerais() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList
        restGeraiMockMvc.perform(get("/api/gerais?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gerai.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getGerai() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get the gerai
        restGeraiMockMvc.perform(get("/api/gerais/{id}", gerai.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gerai.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllGeraisByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where code equals to DEFAULT_CODE
        defaultGeraiShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the geraiList where code equals to UPDATED_CODE
        defaultGeraiShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllGeraisByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where code in DEFAULT_CODE or UPDATED_CODE
        defaultGeraiShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the geraiList where code equals to UPDATED_CODE
        defaultGeraiShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllGeraisByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where code is not null
        defaultGeraiShouldBeFound("code.specified=true");

        // Get all the geraiList where code is null
        defaultGeraiShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where name equals to DEFAULT_NAME
        defaultGeraiShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the geraiList where name equals to UPDATED_NAME
        defaultGeraiShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGeraisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGeraiShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the geraiList where name equals to UPDATED_NAME
        defaultGeraiShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGeraisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where name is not null
        defaultGeraiShouldBeFound("name.specified=true");

        // Get all the geraiList where name is null
        defaultGeraiShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraisByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where location equals to DEFAULT_LOCATION
        defaultGeraiShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the geraiList where location equals to UPDATED_LOCATION
        defaultGeraiShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllGeraisByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultGeraiShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the geraiList where location equals to UPDATED_LOCATION
        defaultGeraiShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllGeraisByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where location is not null
        defaultGeraiShouldBeFound("location.specified=true");

        // Get all the geraiList where location is null
        defaultGeraiShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraisByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where password equals to DEFAULT_PASSWORD
        defaultGeraiShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the geraiList where password equals to UPDATED_PASSWORD
        defaultGeraiShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllGeraisByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultGeraiShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the geraiList where password equals to UPDATED_PASSWORD
        defaultGeraiShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllGeraisByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where password is not null
        defaultGeraiShouldBeFound("password.specified=true");

        // Get all the geraiList where password is null
        defaultGeraiShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraisByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where createdDate equals to DEFAULT_CREATED_DATE
        defaultGeraiShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the geraiList where createdDate equals to UPDATED_CREATED_DATE
        defaultGeraiShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllGeraisByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultGeraiShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the geraiList where createdDate equals to UPDATED_CREATED_DATE
        defaultGeraiShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllGeraisByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        // Get all the geraiList where createdDate is not null
        defaultGeraiShouldBeFound("createdDate.specified=true");

        // Get all the geraiList where createdDate is null
        defaultGeraiShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraisByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User creator = UserResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        gerai.setCreator(creator);
        geraiRepository.saveAndFlush(gerai);
        Long creatorId = creator.getId();

        // Get all the geraiList where creator equals to creatorId
        defaultGeraiShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the geraiList where creator equals to creatorId + 1
        defaultGeraiShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGeraiShouldBeFound(String filter) throws Exception {
        restGeraiMockMvc.perform(get("/api/gerais?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gerai.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restGeraiMockMvc.perform(get("/api/gerais/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGeraiShouldNotBeFound(String filter) throws Exception {
        restGeraiMockMvc.perform(get("/api/gerais?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeraiMockMvc.perform(get("/api/gerais/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGerai() throws Exception {
        // Get the gerai
        restGeraiMockMvc.perform(get("/api/gerais/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGerai() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        int databaseSizeBeforeUpdate = geraiRepository.findAll().size();

        // Update the gerai
        Gerai updatedGerai = geraiRepository.findById(gerai.getId()).get();
        // Disconnect from session so that the updates on updatedGerai are not directly saved in db
        em.detach(updatedGerai);
        updatedGerai
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .password(UPDATED_PASSWORD)
            .createdDate(UPDATED_CREATED_DATE);
        GeraiDTO geraiDTO = geraiMapper.toDto(updatedGerai);

        restGeraiMockMvc.perform(put("/api/gerais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiDTO)))
            .andExpect(status().isOk());

        // Validate the Gerai in the database
        List<Gerai> geraiList = geraiRepository.findAll();
        assertThat(geraiList).hasSize(databaseSizeBeforeUpdate);
        Gerai testGerai = geraiList.get(geraiList.size() - 1);
        assertThat(testGerai.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testGerai.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGerai.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testGerai.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testGerai.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);

        // Validate the Gerai in Elasticsearch
        verify(mockGeraiSearchRepository, times(1)).save(testGerai);
    }

    @Test
    @Transactional
    public void updateNonExistingGerai() throws Exception {
        int databaseSizeBeforeUpdate = geraiRepository.findAll().size();

        // Create the Gerai
        GeraiDTO geraiDTO = geraiMapper.toDto(gerai);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeraiMockMvc.perform(put("/api/gerais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gerai in the database
        List<Gerai> geraiList = geraiRepository.findAll();
        assertThat(geraiList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Gerai in Elasticsearch
        verify(mockGeraiSearchRepository, times(0)).save(gerai);
    }

    @Test
    @Transactional
    public void deleteGerai() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);

        int databaseSizeBeforeDelete = geraiRepository.findAll().size();

        // Delete the gerai
        restGeraiMockMvc.perform(delete("/api/gerais/{id}", gerai.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Gerai> geraiList = geraiRepository.findAll();
        assertThat(geraiList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Gerai in Elasticsearch
        verify(mockGeraiSearchRepository, times(1)).deleteById(gerai.getId());
    }

    @Test
    @Transactional
    public void searchGerai() throws Exception {
        // Initialize the database
        geraiRepository.saveAndFlush(gerai);
        when(mockGeraiSearchRepository.search(queryStringQuery("id:" + gerai.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(gerai), PageRequest.of(0, 1), 1));
        // Search the gerai
        restGeraiMockMvc.perform(get("/api/_search/gerais?query=id:" + gerai.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gerai.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gerai.class);
        Gerai gerai1 = new Gerai();
        gerai1.setId(1L);
        Gerai gerai2 = new Gerai();
        gerai2.setId(gerai1.getId());
        assertThat(gerai1).isEqualTo(gerai2);
        gerai2.setId(2L);
        assertThat(gerai1).isNotEqualTo(gerai2);
        gerai1.setId(null);
        assertThat(gerai1).isNotEqualTo(gerai2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeraiDTO.class);
        GeraiDTO geraiDTO1 = new GeraiDTO();
        geraiDTO1.setId(1L);
        GeraiDTO geraiDTO2 = new GeraiDTO();
        assertThat(geraiDTO1).isNotEqualTo(geraiDTO2);
        geraiDTO2.setId(geraiDTO1.getId());
        assertThat(geraiDTO1).isEqualTo(geraiDTO2);
        geraiDTO2.setId(2L);
        assertThat(geraiDTO1).isNotEqualTo(geraiDTO2);
        geraiDTO1.setId(null);
        assertThat(geraiDTO1).isNotEqualTo(geraiDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(geraiMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(geraiMapper.fromId(null)).isNull();
    }
}
