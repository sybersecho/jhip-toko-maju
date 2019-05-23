package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.GeraiUpdateHistory;
import com.toko.maju.repository.GeraiUpdateHistoryRepository;
import com.toko.maju.repository.search.GeraiUpdateHistorySearchRepository;
import com.toko.maju.service.GeraiUpdateHistoryService;
import com.toko.maju.service.dto.GeraiUpdateHistoryDTO;
import com.toko.maju.service.mapper.GeraiUpdateHistoryMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.GeraiUpdateHistoryCriteria;
import com.toko.maju.service.GeraiUpdateHistoryQueryService;

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
 * Test class for the GeraiUpdateHistoryResource REST controller.
 *
 * @see GeraiUpdateHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class GeraiUpdateHistoryResourceIntTest {

    private static final Long DEFAULT_LAST_SALE_ID = 1L;
    private static final Long UPDATED_LAST_SALE_ID = 2L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SALE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SALE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private GeraiUpdateHistoryRepository geraiUpdateHistoryRepository;

    @Autowired
    private GeraiUpdateHistoryMapper geraiUpdateHistoryMapper;

    @Autowired
    private GeraiUpdateHistoryService geraiUpdateHistoryService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.GeraiUpdateHistorySearchRepositoryMockConfiguration
     */
    @Autowired
    private GeraiUpdateHistorySearchRepository mockGeraiUpdateHistorySearchRepository;

    @Autowired
    private GeraiUpdateHistoryQueryService geraiUpdateHistoryQueryService;

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

    private MockMvc restGeraiUpdateHistoryMockMvc;

    private GeraiUpdateHistory geraiUpdateHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GeraiUpdateHistoryResource geraiUpdateHistoryResource = new GeraiUpdateHistoryResource(geraiUpdateHistoryService, geraiUpdateHistoryQueryService);
        this.restGeraiUpdateHistoryMockMvc = MockMvcBuilders.standaloneSetup(geraiUpdateHistoryResource)
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
    public static GeraiUpdateHistory createEntity(EntityManager em) {
        GeraiUpdateHistory geraiUpdateHistory = new GeraiUpdateHistory()
            .lastSaleId(DEFAULT_LAST_SALE_ID)
            .createdDate(DEFAULT_CREATED_DATE)
            .saleDate(DEFAULT_SALE_DATE);
        return geraiUpdateHistory;
    }

    @Before
    public void initTest() {
        geraiUpdateHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createGeraiUpdateHistory() throws Exception {
        int databaseSizeBeforeCreate = geraiUpdateHistoryRepository.findAll().size();

        // Create the GeraiUpdateHistory
        GeraiUpdateHistoryDTO geraiUpdateHistoryDTO = geraiUpdateHistoryMapper.toDto(geraiUpdateHistory);
        restGeraiUpdateHistoryMockMvc.perform(post("/api/gerai-update-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiUpdateHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the GeraiUpdateHistory in the database
        List<GeraiUpdateHistory> geraiUpdateHistoryList = geraiUpdateHistoryRepository.findAll();
        assertThat(geraiUpdateHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        GeraiUpdateHistory testGeraiUpdateHistory = geraiUpdateHistoryList.get(geraiUpdateHistoryList.size() - 1);
        assertThat(testGeraiUpdateHistory.getLastSaleId()).isEqualTo(DEFAULT_LAST_SALE_ID);
        assertThat(testGeraiUpdateHistory.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGeraiUpdateHistory.getSaleDate()).isEqualTo(DEFAULT_SALE_DATE);

        // Validate the GeraiUpdateHistory in Elasticsearch
        verify(mockGeraiUpdateHistorySearchRepository, times(1)).save(testGeraiUpdateHistory);
    }

    @Test
    @Transactional
    public void createGeraiUpdateHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = geraiUpdateHistoryRepository.findAll().size();

        // Create the GeraiUpdateHistory with an existing ID
        geraiUpdateHistory.setId(1L);
        GeraiUpdateHistoryDTO geraiUpdateHistoryDTO = geraiUpdateHistoryMapper.toDto(geraiUpdateHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeraiUpdateHistoryMockMvc.perform(post("/api/gerai-update-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiUpdateHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeraiUpdateHistory in the database
        List<GeraiUpdateHistory> geraiUpdateHistoryList = geraiUpdateHistoryRepository.findAll();
        assertThat(geraiUpdateHistoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the GeraiUpdateHistory in Elasticsearch
        verify(mockGeraiUpdateHistorySearchRepository, times(0)).save(geraiUpdateHistory);
    }

    @Test
    @Transactional
    public void checkLastSaleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiUpdateHistoryRepository.findAll().size();
        // set the field null
        geraiUpdateHistory.setLastSaleId(null);

        // Create the GeraiUpdateHistory, which fails.
        GeraiUpdateHistoryDTO geraiUpdateHistoryDTO = geraiUpdateHistoryMapper.toDto(geraiUpdateHistory);

        restGeraiUpdateHistoryMockMvc.perform(post("/api/gerai-update-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiUpdateHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiUpdateHistory> geraiUpdateHistoryList = geraiUpdateHistoryRepository.findAll();
        assertThat(geraiUpdateHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiUpdateHistoryRepository.findAll().size();
        // set the field null
        geraiUpdateHistory.setCreatedDate(null);

        // Create the GeraiUpdateHistory, which fails.
        GeraiUpdateHistoryDTO geraiUpdateHistoryDTO = geraiUpdateHistoryMapper.toDto(geraiUpdateHistory);

        restGeraiUpdateHistoryMockMvc.perform(post("/api/gerai-update-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiUpdateHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiUpdateHistory> geraiUpdateHistoryList = geraiUpdateHistoryRepository.findAll();
        assertThat(geraiUpdateHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSaleDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiUpdateHistoryRepository.findAll().size();
        // set the field null
        geraiUpdateHistory.setSaleDate(null);

        // Create the GeraiUpdateHistory, which fails.
        GeraiUpdateHistoryDTO geraiUpdateHistoryDTO = geraiUpdateHistoryMapper.toDto(geraiUpdateHistory);

        restGeraiUpdateHistoryMockMvc.perform(post("/api/gerai-update-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiUpdateHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiUpdateHistory> geraiUpdateHistoryList = geraiUpdateHistoryRepository.findAll();
        assertThat(geraiUpdateHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistories() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList
        restGeraiUpdateHistoryMockMvc.perform(get("/api/gerai-update-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraiUpdateHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastSaleId").value(hasItem(DEFAULT_LAST_SALE_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getGeraiUpdateHistory() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get the geraiUpdateHistory
        restGeraiUpdateHistoryMockMvc.perform(get("/api/gerai-update-histories/{id}", geraiUpdateHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(geraiUpdateHistory.getId().intValue()))
            .andExpect(jsonPath("$.lastSaleId").value(DEFAULT_LAST_SALE_ID.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.saleDate").value(DEFAULT_SALE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesByLastSaleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where lastSaleId equals to DEFAULT_LAST_SALE_ID
        defaultGeraiUpdateHistoryShouldBeFound("lastSaleId.equals=" + DEFAULT_LAST_SALE_ID);

        // Get all the geraiUpdateHistoryList where lastSaleId equals to UPDATED_LAST_SALE_ID
        defaultGeraiUpdateHistoryShouldNotBeFound("lastSaleId.equals=" + UPDATED_LAST_SALE_ID);
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesByLastSaleIdIsInShouldWork() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where lastSaleId in DEFAULT_LAST_SALE_ID or UPDATED_LAST_SALE_ID
        defaultGeraiUpdateHistoryShouldBeFound("lastSaleId.in=" + DEFAULT_LAST_SALE_ID + "," + UPDATED_LAST_SALE_ID);

        // Get all the geraiUpdateHistoryList where lastSaleId equals to UPDATED_LAST_SALE_ID
        defaultGeraiUpdateHistoryShouldNotBeFound("lastSaleId.in=" + UPDATED_LAST_SALE_ID);
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesByLastSaleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where lastSaleId is not null
        defaultGeraiUpdateHistoryShouldBeFound("lastSaleId.specified=true");

        // Get all the geraiUpdateHistoryList where lastSaleId is null
        defaultGeraiUpdateHistoryShouldNotBeFound("lastSaleId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesByLastSaleIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where lastSaleId greater than or equals to DEFAULT_LAST_SALE_ID
        defaultGeraiUpdateHistoryShouldBeFound("lastSaleId.greaterOrEqualThan=" + DEFAULT_LAST_SALE_ID);

        // Get all the geraiUpdateHistoryList where lastSaleId greater than or equals to UPDATED_LAST_SALE_ID
        defaultGeraiUpdateHistoryShouldNotBeFound("lastSaleId.greaterOrEqualThan=" + UPDATED_LAST_SALE_ID);
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesByLastSaleIdIsLessThanSomething() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where lastSaleId less than or equals to DEFAULT_LAST_SALE_ID
        defaultGeraiUpdateHistoryShouldNotBeFound("lastSaleId.lessThan=" + DEFAULT_LAST_SALE_ID);

        // Get all the geraiUpdateHistoryList where lastSaleId less than or equals to UPDATED_LAST_SALE_ID
        defaultGeraiUpdateHistoryShouldBeFound("lastSaleId.lessThan=" + UPDATED_LAST_SALE_ID);
    }


    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where createdDate equals to DEFAULT_CREATED_DATE
        defaultGeraiUpdateHistoryShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the geraiUpdateHistoryList where createdDate equals to UPDATED_CREATED_DATE
        defaultGeraiUpdateHistoryShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultGeraiUpdateHistoryShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the geraiUpdateHistoryList where createdDate equals to UPDATED_CREATED_DATE
        defaultGeraiUpdateHistoryShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where createdDate is not null
        defaultGeraiUpdateHistoryShouldBeFound("createdDate.specified=true");

        // Get all the geraiUpdateHistoryList where createdDate is null
        defaultGeraiUpdateHistoryShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesBySaleDateIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where saleDate equals to DEFAULT_SALE_DATE
        defaultGeraiUpdateHistoryShouldBeFound("saleDate.equals=" + DEFAULT_SALE_DATE);

        // Get all the geraiUpdateHistoryList where saleDate equals to UPDATED_SALE_DATE
        defaultGeraiUpdateHistoryShouldNotBeFound("saleDate.equals=" + UPDATED_SALE_DATE);
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesBySaleDateIsInShouldWork() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where saleDate in DEFAULT_SALE_DATE or UPDATED_SALE_DATE
        defaultGeraiUpdateHistoryShouldBeFound("saleDate.in=" + DEFAULT_SALE_DATE + "," + UPDATED_SALE_DATE);

        // Get all the geraiUpdateHistoryList where saleDate equals to UPDATED_SALE_DATE
        defaultGeraiUpdateHistoryShouldNotBeFound("saleDate.in=" + UPDATED_SALE_DATE);
    }

    @Test
    @Transactional
    public void getAllGeraiUpdateHistoriesBySaleDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        // Get all the geraiUpdateHistoryList where saleDate is not null
        defaultGeraiUpdateHistoryShouldBeFound("saleDate.specified=true");

        // Get all the geraiUpdateHistoryList where saleDate is null
        defaultGeraiUpdateHistoryShouldNotBeFound("saleDate.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGeraiUpdateHistoryShouldBeFound(String filter) throws Exception {
        restGeraiUpdateHistoryMockMvc.perform(get("/api/gerai-update-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraiUpdateHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastSaleId").value(hasItem(DEFAULT_LAST_SALE_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())));

        // Check, that the count call also returns 1
        restGeraiUpdateHistoryMockMvc.perform(get("/api/gerai-update-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGeraiUpdateHistoryShouldNotBeFound(String filter) throws Exception {
        restGeraiUpdateHistoryMockMvc.perform(get("/api/gerai-update-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeraiUpdateHistoryMockMvc.perform(get("/api/gerai-update-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGeraiUpdateHistory() throws Exception {
        // Get the geraiUpdateHistory
        restGeraiUpdateHistoryMockMvc.perform(get("/api/gerai-update-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeraiUpdateHistory() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        int databaseSizeBeforeUpdate = geraiUpdateHistoryRepository.findAll().size();

        // Update the geraiUpdateHistory
        GeraiUpdateHistory updatedGeraiUpdateHistory = geraiUpdateHistoryRepository.findById(geraiUpdateHistory.getId()).get();
        // Disconnect from session so that the updates on updatedGeraiUpdateHistory are not directly saved in db
        em.detach(updatedGeraiUpdateHistory);
        updatedGeraiUpdateHistory
            .lastSaleId(UPDATED_LAST_SALE_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .saleDate(UPDATED_SALE_DATE);
        GeraiUpdateHistoryDTO geraiUpdateHistoryDTO = geraiUpdateHistoryMapper.toDto(updatedGeraiUpdateHistory);

        restGeraiUpdateHistoryMockMvc.perform(put("/api/gerai-update-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiUpdateHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the GeraiUpdateHistory in the database
        List<GeraiUpdateHistory> geraiUpdateHistoryList = geraiUpdateHistoryRepository.findAll();
        assertThat(geraiUpdateHistoryList).hasSize(databaseSizeBeforeUpdate);
        GeraiUpdateHistory testGeraiUpdateHistory = geraiUpdateHistoryList.get(geraiUpdateHistoryList.size() - 1);
        assertThat(testGeraiUpdateHistory.getLastSaleId()).isEqualTo(UPDATED_LAST_SALE_ID);
        assertThat(testGeraiUpdateHistory.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGeraiUpdateHistory.getSaleDate()).isEqualTo(UPDATED_SALE_DATE);

        // Validate the GeraiUpdateHistory in Elasticsearch
        verify(mockGeraiUpdateHistorySearchRepository, times(1)).save(testGeraiUpdateHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingGeraiUpdateHistory() throws Exception {
        int databaseSizeBeforeUpdate = geraiUpdateHistoryRepository.findAll().size();

        // Create the GeraiUpdateHistory
        GeraiUpdateHistoryDTO geraiUpdateHistoryDTO = geraiUpdateHistoryMapper.toDto(geraiUpdateHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeraiUpdateHistoryMockMvc.perform(put("/api/gerai-update-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiUpdateHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeraiUpdateHistory in the database
        List<GeraiUpdateHistory> geraiUpdateHistoryList = geraiUpdateHistoryRepository.findAll();
        assertThat(geraiUpdateHistoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GeraiUpdateHistory in Elasticsearch
        verify(mockGeraiUpdateHistorySearchRepository, times(0)).save(geraiUpdateHistory);
    }

    @Test
    @Transactional
    public void deleteGeraiUpdateHistory() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);

        int databaseSizeBeforeDelete = geraiUpdateHistoryRepository.findAll().size();

        // Delete the geraiUpdateHistory
        restGeraiUpdateHistoryMockMvc.perform(delete("/api/gerai-update-histories/{id}", geraiUpdateHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GeraiUpdateHistory> geraiUpdateHistoryList = geraiUpdateHistoryRepository.findAll();
        assertThat(geraiUpdateHistoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GeraiUpdateHistory in Elasticsearch
        verify(mockGeraiUpdateHistorySearchRepository, times(1)).deleteById(geraiUpdateHistory.getId());
    }

    @Test
    @Transactional
    public void searchGeraiUpdateHistory() throws Exception {
        // Initialize the database
        geraiUpdateHistoryRepository.saveAndFlush(geraiUpdateHistory);
        when(mockGeraiUpdateHistorySearchRepository.search(queryStringQuery("id:" + geraiUpdateHistory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(geraiUpdateHistory), PageRequest.of(0, 1), 1));
        // Search the geraiUpdateHistory
        restGeraiUpdateHistoryMockMvc.perform(get("/api/_search/gerai-update-histories?query=id:" + geraiUpdateHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraiUpdateHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastSaleId").value(hasItem(DEFAULT_LAST_SALE_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeraiUpdateHistory.class);
        GeraiUpdateHistory geraiUpdateHistory1 = new GeraiUpdateHistory();
        geraiUpdateHistory1.setId(1L);
        GeraiUpdateHistory geraiUpdateHistory2 = new GeraiUpdateHistory();
        geraiUpdateHistory2.setId(geraiUpdateHistory1.getId());
        assertThat(geraiUpdateHistory1).isEqualTo(geraiUpdateHistory2);
        geraiUpdateHistory2.setId(2L);
        assertThat(geraiUpdateHistory1).isNotEqualTo(geraiUpdateHistory2);
        geraiUpdateHistory1.setId(null);
        assertThat(geraiUpdateHistory1).isNotEqualTo(geraiUpdateHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeraiUpdateHistoryDTO.class);
        GeraiUpdateHistoryDTO geraiUpdateHistoryDTO1 = new GeraiUpdateHistoryDTO();
        geraiUpdateHistoryDTO1.setId(1L);
        GeraiUpdateHistoryDTO geraiUpdateHistoryDTO2 = new GeraiUpdateHistoryDTO();
        assertThat(geraiUpdateHistoryDTO1).isNotEqualTo(geraiUpdateHistoryDTO2);
        geraiUpdateHistoryDTO2.setId(geraiUpdateHistoryDTO1.getId());
        assertThat(geraiUpdateHistoryDTO1).isEqualTo(geraiUpdateHistoryDTO2);
        geraiUpdateHistoryDTO2.setId(2L);
        assertThat(geraiUpdateHistoryDTO1).isNotEqualTo(geraiUpdateHistoryDTO2);
        geraiUpdateHistoryDTO1.setId(null);
        assertThat(geraiUpdateHistoryDTO1).isNotEqualTo(geraiUpdateHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(geraiUpdateHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(geraiUpdateHistoryMapper.fromId(null)).isNull();
    }
}
