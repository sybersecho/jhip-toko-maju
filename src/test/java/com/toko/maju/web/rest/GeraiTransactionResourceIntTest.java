package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.GeraiTransaction;
import com.toko.maju.domain.Gerai;
import com.toko.maju.repository.GeraiTransactionRepository;
import com.toko.maju.repository.search.GeraiTransactionSearchRepository;
import com.toko.maju.service.GeraiTransactionService;
import com.toko.maju.service.dto.GeraiTransactionDTO;
import com.toko.maju.service.mapper.GeraiTransactionMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.GeraiTransactionCriteria;
import com.toko.maju.service.GeraiTransactionQueryService;

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
 * Test class for the GeraiTransactionResource REST controller.
 *
 * @see GeraiTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class GeraiTransactionResourceIntTest {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_CURRENT_STOCK = 1;
    private static final Integer UPDATED_CURRENT_STOCK = 2;

    private static final Instant DEFAULT_RECEIVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private GeraiTransactionRepository geraiTransactionRepository;

    @Autowired
    private GeraiTransactionMapper geraiTransactionMapper;

    @Autowired
    private GeraiTransactionService geraiTransactionService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.GeraiTransactionSearchRepositoryMockConfiguration
     */
    @Autowired
    private GeraiTransactionSearchRepository mockGeraiTransactionSearchRepository;

    @Autowired
    private GeraiTransactionQueryService geraiTransactionQueryService;

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

    private MockMvc restGeraiTransactionMockMvc;

    private GeraiTransaction geraiTransaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GeraiTransactionResource geraiTransactionResource = new GeraiTransactionResource(geraiTransactionService, geraiTransactionQueryService);
        this.restGeraiTransactionMockMvc = MockMvcBuilders.standaloneSetup(geraiTransactionResource)
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
    public static GeraiTransaction createEntity(EntityManager em) {
        GeraiTransaction geraiTransaction = new GeraiTransaction()
            .barcode(DEFAULT_BARCODE)
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .currentStock(DEFAULT_CURRENT_STOCK)
            .receivedDate(DEFAULT_RECEIVED_DATE);
        // Add required entity
        Gerai gerai = GeraiResourceIntTest.createEntity(em);
        em.persist(gerai);
        em.flush();
        geraiTransaction.setGerai(gerai);
        return geraiTransaction;
    }

    @Before
    public void initTest() {
        geraiTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createGeraiTransaction() throws Exception {
        int databaseSizeBeforeCreate = geraiTransactionRepository.findAll().size();

        // Create the GeraiTransaction
        GeraiTransactionDTO geraiTransactionDTO = geraiTransactionMapper.toDto(geraiTransaction);
        restGeraiTransactionMockMvc.perform(post("/api/gerai-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the GeraiTransaction in the database
        List<GeraiTransaction> geraiTransactionList = geraiTransactionRepository.findAll();
        assertThat(geraiTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        GeraiTransaction testGeraiTransaction = geraiTransactionList.get(geraiTransactionList.size() - 1);
        assertThat(testGeraiTransaction.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testGeraiTransaction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGeraiTransaction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testGeraiTransaction.getCurrentStock()).isEqualTo(DEFAULT_CURRENT_STOCK);
        assertThat(testGeraiTransaction.getReceivedDate()).isEqualTo(DEFAULT_RECEIVED_DATE);

        // Validate the GeraiTransaction in Elasticsearch
        verify(mockGeraiTransactionSearchRepository, times(1)).save(testGeraiTransaction);
    }

    @Test
    @Transactional
    public void createGeraiTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = geraiTransactionRepository.findAll().size();

        // Create the GeraiTransaction with an existing ID
        geraiTransaction.setId(1L);
        GeraiTransactionDTO geraiTransactionDTO = geraiTransactionMapper.toDto(geraiTransaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeraiTransactionMockMvc.perform(post("/api/gerai-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeraiTransaction in the database
        List<GeraiTransaction> geraiTransactionList = geraiTransactionRepository.findAll();
        assertThat(geraiTransactionList).hasSize(databaseSizeBeforeCreate);

        // Validate the GeraiTransaction in Elasticsearch
        verify(mockGeraiTransactionSearchRepository, times(0)).save(geraiTransaction);
    }

    @Test
    @Transactional
    public void checkBarcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiTransactionRepository.findAll().size();
        // set the field null
        geraiTransaction.setBarcode(null);

        // Create the GeraiTransaction, which fails.
        GeraiTransactionDTO geraiTransactionDTO = geraiTransactionMapper.toDto(geraiTransaction);

        restGeraiTransactionMockMvc.perform(post("/api/gerai-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiTransaction> geraiTransactionList = geraiTransactionRepository.findAll();
        assertThat(geraiTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiTransactionRepository.findAll().size();
        // set the field null
        geraiTransaction.setName(null);

        // Create the GeraiTransaction, which fails.
        GeraiTransactionDTO geraiTransactionDTO = geraiTransactionMapper.toDto(geraiTransaction);

        restGeraiTransactionMockMvc.perform(post("/api/gerai-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiTransaction> geraiTransactionList = geraiTransactionRepository.findAll();
        assertThat(geraiTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiTransactionRepository.findAll().size();
        // set the field null
        geraiTransaction.setQuantity(null);

        // Create the GeraiTransaction, which fails.
        GeraiTransactionDTO geraiTransactionDTO = geraiTransactionMapper.toDto(geraiTransaction);

        restGeraiTransactionMockMvc.perform(post("/api/gerai-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiTransaction> geraiTransactionList = geraiTransactionRepository.findAll();
        assertThat(geraiTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = geraiTransactionRepository.findAll().size();
        // set the field null
        geraiTransaction.setCurrentStock(null);

        // Create the GeraiTransaction, which fails.
        GeraiTransactionDTO geraiTransactionDTO = geraiTransactionMapper.toDto(geraiTransaction);

        restGeraiTransactionMockMvc.perform(post("/api/gerai-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<GeraiTransaction> geraiTransactionList = geraiTransactionRepository.findAll();
        assertThat(geraiTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactions() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList
        restGeraiTransactionMockMvc.perform(get("/api/gerai-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraiTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].currentStock").value(hasItem(DEFAULT_CURRENT_STOCK)))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getGeraiTransaction() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get the geraiTransaction
        restGeraiTransactionMockMvc.perform(get("/api/gerai-transactions/{id}", geraiTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(geraiTransaction.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.currentStock").value(DEFAULT_CURRENT_STOCK))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where barcode equals to DEFAULT_BARCODE
        defaultGeraiTransactionShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the geraiTransactionList where barcode equals to UPDATED_BARCODE
        defaultGeraiTransactionShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultGeraiTransactionShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the geraiTransactionList where barcode equals to UPDATED_BARCODE
        defaultGeraiTransactionShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where barcode is not null
        defaultGeraiTransactionShouldBeFound("barcode.specified=true");

        // Get all the geraiTransactionList where barcode is null
        defaultGeraiTransactionShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where name equals to DEFAULT_NAME
        defaultGeraiTransactionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the geraiTransactionList where name equals to UPDATED_NAME
        defaultGeraiTransactionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGeraiTransactionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the geraiTransactionList where name equals to UPDATED_NAME
        defaultGeraiTransactionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where name is not null
        defaultGeraiTransactionShouldBeFound("name.specified=true");

        // Get all the geraiTransactionList where name is null
        defaultGeraiTransactionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where quantity equals to DEFAULT_QUANTITY
        defaultGeraiTransactionShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the geraiTransactionList where quantity equals to UPDATED_QUANTITY
        defaultGeraiTransactionShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultGeraiTransactionShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the geraiTransactionList where quantity equals to UPDATED_QUANTITY
        defaultGeraiTransactionShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where quantity is not null
        defaultGeraiTransactionShouldBeFound("quantity.specified=true");

        // Get all the geraiTransactionList where quantity is null
        defaultGeraiTransactionShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultGeraiTransactionShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the geraiTransactionList where quantity greater than or equals to UPDATED_QUANTITY
        defaultGeraiTransactionShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where quantity less than or equals to DEFAULT_QUANTITY
        defaultGeraiTransactionShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the geraiTransactionList where quantity less than or equals to UPDATED_QUANTITY
        defaultGeraiTransactionShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllGeraiTransactionsByCurrentStockIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where currentStock equals to DEFAULT_CURRENT_STOCK
        defaultGeraiTransactionShouldBeFound("currentStock.equals=" + DEFAULT_CURRENT_STOCK);

        // Get all the geraiTransactionList where currentStock equals to UPDATED_CURRENT_STOCK
        defaultGeraiTransactionShouldNotBeFound("currentStock.equals=" + UPDATED_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByCurrentStockIsInShouldWork() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where currentStock in DEFAULT_CURRENT_STOCK or UPDATED_CURRENT_STOCK
        defaultGeraiTransactionShouldBeFound("currentStock.in=" + DEFAULT_CURRENT_STOCK + "," + UPDATED_CURRENT_STOCK);

        // Get all the geraiTransactionList where currentStock equals to UPDATED_CURRENT_STOCK
        defaultGeraiTransactionShouldNotBeFound("currentStock.in=" + UPDATED_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByCurrentStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where currentStock is not null
        defaultGeraiTransactionShouldBeFound("currentStock.specified=true");

        // Get all the geraiTransactionList where currentStock is null
        defaultGeraiTransactionShouldNotBeFound("currentStock.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByCurrentStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where currentStock greater than or equals to DEFAULT_CURRENT_STOCK
        defaultGeraiTransactionShouldBeFound("currentStock.greaterOrEqualThan=" + DEFAULT_CURRENT_STOCK);

        // Get all the geraiTransactionList where currentStock greater than or equals to UPDATED_CURRENT_STOCK
        defaultGeraiTransactionShouldNotBeFound("currentStock.greaterOrEqualThan=" + UPDATED_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByCurrentStockIsLessThanSomething() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where currentStock less than or equals to DEFAULT_CURRENT_STOCK
        defaultGeraiTransactionShouldNotBeFound("currentStock.lessThan=" + DEFAULT_CURRENT_STOCK);

        // Get all the geraiTransactionList where currentStock less than or equals to UPDATED_CURRENT_STOCK
        defaultGeraiTransactionShouldBeFound("currentStock.lessThan=" + UPDATED_CURRENT_STOCK);
    }


    @Test
    @Transactional
    public void getAllGeraiTransactionsByReceivedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where receivedDate equals to DEFAULT_RECEIVED_DATE
        defaultGeraiTransactionShouldBeFound("receivedDate.equals=" + DEFAULT_RECEIVED_DATE);

        // Get all the geraiTransactionList where receivedDate equals to UPDATED_RECEIVED_DATE
        defaultGeraiTransactionShouldNotBeFound("receivedDate.equals=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByReceivedDateIsInShouldWork() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where receivedDate in DEFAULT_RECEIVED_DATE or UPDATED_RECEIVED_DATE
        defaultGeraiTransactionShouldBeFound("receivedDate.in=" + DEFAULT_RECEIVED_DATE + "," + UPDATED_RECEIVED_DATE);

        // Get all the geraiTransactionList where receivedDate equals to UPDATED_RECEIVED_DATE
        defaultGeraiTransactionShouldNotBeFound("receivedDate.in=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByReceivedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        // Get all the geraiTransactionList where receivedDate is not null
        defaultGeraiTransactionShouldBeFound("receivedDate.specified=true");

        // Get all the geraiTransactionList where receivedDate is null
        defaultGeraiTransactionShouldNotBeFound("receivedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllGeraiTransactionsByGeraiIsEqualToSomething() throws Exception {
        // Initialize the database
        Gerai gerai = GeraiResourceIntTest.createEntity(em);
        em.persist(gerai);
        em.flush();
        geraiTransaction.setGerai(gerai);
        geraiTransactionRepository.saveAndFlush(geraiTransaction);
        Long geraiId = gerai.getId();

        // Get all the geraiTransactionList where gerai equals to geraiId
        defaultGeraiTransactionShouldBeFound("geraiId.equals=" + geraiId);

        // Get all the geraiTransactionList where gerai equals to geraiId + 1
        defaultGeraiTransactionShouldNotBeFound("geraiId.equals=" + (geraiId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGeraiTransactionShouldBeFound(String filter) throws Exception {
        restGeraiTransactionMockMvc.perform(get("/api/gerai-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraiTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].currentStock").value(hasItem(DEFAULT_CURRENT_STOCK)))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())));

        // Check, that the count call also returns 1
        restGeraiTransactionMockMvc.perform(get("/api/gerai-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGeraiTransactionShouldNotBeFound(String filter) throws Exception {
        restGeraiTransactionMockMvc.perform(get("/api/gerai-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeraiTransactionMockMvc.perform(get("/api/gerai-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGeraiTransaction() throws Exception {
        // Get the geraiTransaction
        restGeraiTransactionMockMvc.perform(get("/api/gerai-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeraiTransaction() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        int databaseSizeBeforeUpdate = geraiTransactionRepository.findAll().size();

        // Update the geraiTransaction
        GeraiTransaction updatedGeraiTransaction = geraiTransactionRepository.findById(geraiTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedGeraiTransaction are not directly saved in db
        em.detach(updatedGeraiTransaction);
        updatedGeraiTransaction
            .barcode(UPDATED_BARCODE)
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .currentStock(UPDATED_CURRENT_STOCK)
            .receivedDate(UPDATED_RECEIVED_DATE);
        GeraiTransactionDTO geraiTransactionDTO = geraiTransactionMapper.toDto(updatedGeraiTransaction);

        restGeraiTransactionMockMvc.perform(put("/api/gerai-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiTransactionDTO)))
            .andExpect(status().isOk());

        // Validate the GeraiTransaction in the database
        List<GeraiTransaction> geraiTransactionList = geraiTransactionRepository.findAll();
        assertThat(geraiTransactionList).hasSize(databaseSizeBeforeUpdate);
        GeraiTransaction testGeraiTransaction = geraiTransactionList.get(geraiTransactionList.size() - 1);
        assertThat(testGeraiTransaction.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testGeraiTransaction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGeraiTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testGeraiTransaction.getCurrentStock()).isEqualTo(UPDATED_CURRENT_STOCK);
        assertThat(testGeraiTransaction.getReceivedDate()).isEqualTo(UPDATED_RECEIVED_DATE);

        // Validate the GeraiTransaction in Elasticsearch
        verify(mockGeraiTransactionSearchRepository, times(1)).save(testGeraiTransaction);
    }

    @Test
    @Transactional
    public void updateNonExistingGeraiTransaction() throws Exception {
        int databaseSizeBeforeUpdate = geraiTransactionRepository.findAll().size();

        // Create the GeraiTransaction
        GeraiTransactionDTO geraiTransactionDTO = geraiTransactionMapper.toDto(geraiTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeraiTransactionMockMvc.perform(put("/api/gerai-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geraiTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeraiTransaction in the database
        List<GeraiTransaction> geraiTransactionList = geraiTransactionRepository.findAll();
        assertThat(geraiTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GeraiTransaction in Elasticsearch
        verify(mockGeraiTransactionSearchRepository, times(0)).save(geraiTransaction);
    }

    @Test
    @Transactional
    public void deleteGeraiTransaction() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);

        int databaseSizeBeforeDelete = geraiTransactionRepository.findAll().size();

        // Delete the geraiTransaction
        restGeraiTransactionMockMvc.perform(delete("/api/gerai-transactions/{id}", geraiTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GeraiTransaction> geraiTransactionList = geraiTransactionRepository.findAll();
        assertThat(geraiTransactionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GeraiTransaction in Elasticsearch
        verify(mockGeraiTransactionSearchRepository, times(1)).deleteById(geraiTransaction.getId());
    }

    @Test
    @Transactional
    public void searchGeraiTransaction() throws Exception {
        // Initialize the database
        geraiTransactionRepository.saveAndFlush(geraiTransaction);
        when(mockGeraiTransactionSearchRepository.search(queryStringQuery("id:" + geraiTransaction.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(geraiTransaction), PageRequest.of(0, 1), 1));
        // Search the geraiTransaction
        restGeraiTransactionMockMvc.perform(get("/api/_search/gerai-transactions?query=id:" + geraiTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geraiTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].currentStock").value(hasItem(DEFAULT_CURRENT_STOCK)))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeraiTransaction.class);
        GeraiTransaction geraiTransaction1 = new GeraiTransaction();
        geraiTransaction1.setId(1L);
        GeraiTransaction geraiTransaction2 = new GeraiTransaction();
        geraiTransaction2.setId(geraiTransaction1.getId());
        assertThat(geraiTransaction1).isEqualTo(geraiTransaction2);
        geraiTransaction2.setId(2L);
        assertThat(geraiTransaction1).isNotEqualTo(geraiTransaction2);
        geraiTransaction1.setId(null);
        assertThat(geraiTransaction1).isNotEqualTo(geraiTransaction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeraiTransactionDTO.class);
        GeraiTransactionDTO geraiTransactionDTO1 = new GeraiTransactionDTO();
        geraiTransactionDTO1.setId(1L);
        GeraiTransactionDTO geraiTransactionDTO2 = new GeraiTransactionDTO();
        assertThat(geraiTransactionDTO1).isNotEqualTo(geraiTransactionDTO2);
        geraiTransactionDTO2.setId(geraiTransactionDTO1.getId());
        assertThat(geraiTransactionDTO1).isEqualTo(geraiTransactionDTO2);
        geraiTransactionDTO2.setId(2L);
        assertThat(geraiTransactionDTO1).isNotEqualTo(geraiTransactionDTO2);
        geraiTransactionDTO1.setId(null);
        assertThat(geraiTransactionDTO1).isNotEqualTo(geraiTransactionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(geraiTransactionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(geraiTransactionMapper.fromId(null)).isNull();
    }
}
