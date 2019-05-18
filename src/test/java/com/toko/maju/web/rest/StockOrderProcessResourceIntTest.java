package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.StockOrderProcess;
import com.toko.maju.domain.User;
import com.toko.maju.repository.StockOrderProcessRepository;
import com.toko.maju.repository.search.StockOrderProcessSearchRepository;
import com.toko.maju.service.StockOrderProcessService;
import com.toko.maju.service.dto.StockOrderProcessDTO;
import com.toko.maju.service.mapper.StockOrderProcessMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.StockOrderProcessCriteria;
import com.toko.maju.service.StockOrderProcessQueryService;

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
 * Test class for the StockOrderProcessResource REST controller.
 *
 * @see StockOrderProcessResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class StockOrderProcessResourceIntTest {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY_REQUEST = 1;
    private static final Integer UPDATED_QUANTITY_REQUEST = 2;

    private static final Integer DEFAULT_STOCK_IN_HAND = 1;
    private static final Integer UPDATED_STOCK_IN_HAND = 2;

    private static final Integer DEFAULT_QUANTITY_APPROVE = 1;
    private static final Integer UPDATED_QUANTITY_APPROVE = 2;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private StockOrderProcessRepository stockOrderProcessRepository;

    @Autowired
    private StockOrderProcessMapper stockOrderProcessMapper;

    @Autowired
    private StockOrderProcessService stockOrderProcessService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.StockOrderProcessSearchRepositoryMockConfiguration
     */
    @Autowired
    private StockOrderProcessSearchRepository mockStockOrderProcessSearchRepository;

    @Autowired
    private StockOrderProcessQueryService stockOrderProcessQueryService;

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

    private MockMvc restStockOrderProcessMockMvc;

    private StockOrderProcess stockOrderProcess;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockOrderProcessResource stockOrderProcessResource = new StockOrderProcessResource(stockOrderProcessService, stockOrderProcessQueryService);
        this.restStockOrderProcessMockMvc = MockMvcBuilders.standaloneSetup(stockOrderProcessResource)
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
    public static StockOrderProcess createEntity(EntityManager em) {
        StockOrderProcess stockOrderProcess = new StockOrderProcess()
            .barcode(DEFAULT_BARCODE)
            .name(DEFAULT_NAME)
            .quantityRequest(DEFAULT_QUANTITY_REQUEST)
            .stockInHand(DEFAULT_STOCK_IN_HAND)
            .quantityApprove(DEFAULT_QUANTITY_APPROVE)
            .createdDate(DEFAULT_CREATED_DATE);
        return stockOrderProcess;
    }

    @Before
    public void initTest() {
        stockOrderProcess = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOrderProcess() throws Exception {
        int databaseSizeBeforeCreate = stockOrderProcessRepository.findAll().size();

        // Create the StockOrderProcess
        StockOrderProcessDTO stockOrderProcessDTO = stockOrderProcessMapper.toDto(stockOrderProcess);
        restStockOrderProcessMockMvc.perform(post("/api/stock-order-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderProcessDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOrderProcess in the database
        List<StockOrderProcess> stockOrderProcessList = stockOrderProcessRepository.findAll();
        assertThat(stockOrderProcessList).hasSize(databaseSizeBeforeCreate + 1);
        StockOrderProcess testStockOrderProcess = stockOrderProcessList.get(stockOrderProcessList.size() - 1);
        assertThat(testStockOrderProcess.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testStockOrderProcess.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStockOrderProcess.getQuantityRequest()).isEqualTo(DEFAULT_QUANTITY_REQUEST);
        assertThat(testStockOrderProcess.getStockInHand()).isEqualTo(DEFAULT_STOCK_IN_HAND);
        assertThat(testStockOrderProcess.getQuantityApprove()).isEqualTo(DEFAULT_QUANTITY_APPROVE);
        assertThat(testStockOrderProcess.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);

        // Validate the StockOrderProcess in Elasticsearch
        verify(mockStockOrderProcessSearchRepository, times(1)).save(testStockOrderProcess);
    }

    @Test
    @Transactional
    public void createStockOrderProcessWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOrderProcessRepository.findAll().size();

        // Create the StockOrderProcess with an existing ID
        stockOrderProcess.setId(1L);
        StockOrderProcessDTO stockOrderProcessDTO = stockOrderProcessMapper.toDto(stockOrderProcess);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOrderProcessMockMvc.perform(post("/api/stock-order-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderProcessDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrderProcess in the database
        List<StockOrderProcess> stockOrderProcessList = stockOrderProcessRepository.findAll();
        assertThat(stockOrderProcessList).hasSize(databaseSizeBeforeCreate);

        // Validate the StockOrderProcess in Elasticsearch
        verify(mockStockOrderProcessSearchRepository, times(0)).save(stockOrderProcess);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcesses() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList
        restStockOrderProcessMockMvc.perform(get("/api/stock-order-processes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrderProcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantityRequest").value(hasItem(DEFAULT_QUANTITY_REQUEST)))
            .andExpect(jsonPath("$.[*].stockInHand").value(hasItem(DEFAULT_STOCK_IN_HAND)))
            .andExpect(jsonPath("$.[*].quantityApprove").value(hasItem(DEFAULT_QUANTITY_APPROVE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getStockOrderProcess() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get the stockOrderProcess
        restStockOrderProcessMockMvc.perform(get("/api/stock-order-processes/{id}", stockOrderProcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOrderProcess.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.quantityRequest").value(DEFAULT_QUANTITY_REQUEST))
            .andExpect(jsonPath("$.stockInHand").value(DEFAULT_STOCK_IN_HAND))
            .andExpect(jsonPath("$.quantityApprove").value(DEFAULT_QUANTITY_APPROVE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where barcode equals to DEFAULT_BARCODE
        defaultStockOrderProcessShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the stockOrderProcessList where barcode equals to UPDATED_BARCODE
        defaultStockOrderProcessShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultStockOrderProcessShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the stockOrderProcessList where barcode equals to UPDATED_BARCODE
        defaultStockOrderProcessShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where barcode is not null
        defaultStockOrderProcessShouldBeFound("barcode.specified=true");

        // Get all the stockOrderProcessList where barcode is null
        defaultStockOrderProcessShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where name equals to DEFAULT_NAME
        defaultStockOrderProcessShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the stockOrderProcessList where name equals to UPDATED_NAME
        defaultStockOrderProcessShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStockOrderProcessShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the stockOrderProcessList where name equals to UPDATED_NAME
        defaultStockOrderProcessShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where name is not null
        defaultStockOrderProcessShouldBeFound("name.specified=true");

        // Get all the stockOrderProcessList where name is null
        defaultStockOrderProcessShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByQuantityRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where quantityRequest equals to DEFAULT_QUANTITY_REQUEST
        defaultStockOrderProcessShouldBeFound("quantityRequest.equals=" + DEFAULT_QUANTITY_REQUEST);

        // Get all the stockOrderProcessList where quantityRequest equals to UPDATED_QUANTITY_REQUEST
        defaultStockOrderProcessShouldNotBeFound("quantityRequest.equals=" + UPDATED_QUANTITY_REQUEST);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByQuantityRequestIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where quantityRequest in DEFAULT_QUANTITY_REQUEST or UPDATED_QUANTITY_REQUEST
        defaultStockOrderProcessShouldBeFound("quantityRequest.in=" + DEFAULT_QUANTITY_REQUEST + "," + UPDATED_QUANTITY_REQUEST);

        // Get all the stockOrderProcessList where quantityRequest equals to UPDATED_QUANTITY_REQUEST
        defaultStockOrderProcessShouldNotBeFound("quantityRequest.in=" + UPDATED_QUANTITY_REQUEST);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByQuantityRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where quantityRequest is not null
        defaultStockOrderProcessShouldBeFound("quantityRequest.specified=true");

        // Get all the stockOrderProcessList where quantityRequest is null
        defaultStockOrderProcessShouldNotBeFound("quantityRequest.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByQuantityRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where quantityRequest greater than or equals to DEFAULT_QUANTITY_REQUEST
        defaultStockOrderProcessShouldBeFound("quantityRequest.greaterOrEqualThan=" + DEFAULT_QUANTITY_REQUEST);

        // Get all the stockOrderProcessList where quantityRequest greater than or equals to UPDATED_QUANTITY_REQUEST
        defaultStockOrderProcessShouldNotBeFound("quantityRequest.greaterOrEqualThan=" + UPDATED_QUANTITY_REQUEST);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByQuantityRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where quantityRequest less than or equals to DEFAULT_QUANTITY_REQUEST
        defaultStockOrderProcessShouldNotBeFound("quantityRequest.lessThan=" + DEFAULT_QUANTITY_REQUEST);

        // Get all the stockOrderProcessList where quantityRequest less than or equals to UPDATED_QUANTITY_REQUEST
        defaultStockOrderProcessShouldBeFound("quantityRequest.lessThan=" + UPDATED_QUANTITY_REQUEST);
    }


    @Test
    @Transactional
    public void getAllStockOrderProcessesByStockInHandIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where stockInHand equals to DEFAULT_STOCK_IN_HAND
        defaultStockOrderProcessShouldBeFound("stockInHand.equals=" + DEFAULT_STOCK_IN_HAND);

        // Get all the stockOrderProcessList where stockInHand equals to UPDATED_STOCK_IN_HAND
        defaultStockOrderProcessShouldNotBeFound("stockInHand.equals=" + UPDATED_STOCK_IN_HAND);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByStockInHandIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where stockInHand in DEFAULT_STOCK_IN_HAND or UPDATED_STOCK_IN_HAND
        defaultStockOrderProcessShouldBeFound("stockInHand.in=" + DEFAULT_STOCK_IN_HAND + "," + UPDATED_STOCK_IN_HAND);

        // Get all the stockOrderProcessList where stockInHand equals to UPDATED_STOCK_IN_HAND
        defaultStockOrderProcessShouldNotBeFound("stockInHand.in=" + UPDATED_STOCK_IN_HAND);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByStockInHandIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where stockInHand is not null
        defaultStockOrderProcessShouldBeFound("stockInHand.specified=true");

        // Get all the stockOrderProcessList where stockInHand is null
        defaultStockOrderProcessShouldNotBeFound("stockInHand.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByStockInHandIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where stockInHand greater than or equals to DEFAULT_STOCK_IN_HAND
        defaultStockOrderProcessShouldBeFound("stockInHand.greaterOrEqualThan=" + DEFAULT_STOCK_IN_HAND);

        // Get all the stockOrderProcessList where stockInHand greater than or equals to UPDATED_STOCK_IN_HAND
        defaultStockOrderProcessShouldNotBeFound("stockInHand.greaterOrEqualThan=" + UPDATED_STOCK_IN_HAND);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByStockInHandIsLessThanSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where stockInHand less than or equals to DEFAULT_STOCK_IN_HAND
        defaultStockOrderProcessShouldNotBeFound("stockInHand.lessThan=" + DEFAULT_STOCK_IN_HAND);

        // Get all the stockOrderProcessList where stockInHand less than or equals to UPDATED_STOCK_IN_HAND
        defaultStockOrderProcessShouldBeFound("stockInHand.lessThan=" + UPDATED_STOCK_IN_HAND);
    }


    @Test
    @Transactional
    public void getAllStockOrderProcessesByQuantityApproveIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where quantityApprove equals to DEFAULT_QUANTITY_APPROVE
        defaultStockOrderProcessShouldBeFound("quantityApprove.equals=" + DEFAULT_QUANTITY_APPROVE);

        // Get all the stockOrderProcessList where quantityApprove equals to UPDATED_QUANTITY_APPROVE
        defaultStockOrderProcessShouldNotBeFound("quantityApprove.equals=" + UPDATED_QUANTITY_APPROVE);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByQuantityApproveIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where quantityApprove in DEFAULT_QUANTITY_APPROVE or UPDATED_QUANTITY_APPROVE
        defaultStockOrderProcessShouldBeFound("quantityApprove.in=" + DEFAULT_QUANTITY_APPROVE + "," + UPDATED_QUANTITY_APPROVE);

        // Get all the stockOrderProcessList where quantityApprove equals to UPDATED_QUANTITY_APPROVE
        defaultStockOrderProcessShouldNotBeFound("quantityApprove.in=" + UPDATED_QUANTITY_APPROVE);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByQuantityApproveIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where quantityApprove is not null
        defaultStockOrderProcessShouldBeFound("quantityApprove.specified=true");

        // Get all the stockOrderProcessList where quantityApprove is null
        defaultStockOrderProcessShouldNotBeFound("quantityApprove.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByQuantityApproveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where quantityApprove greater than or equals to DEFAULT_QUANTITY_APPROVE
        defaultStockOrderProcessShouldBeFound("quantityApprove.greaterOrEqualThan=" + DEFAULT_QUANTITY_APPROVE);

        // Get all the stockOrderProcessList where quantityApprove greater than or equals to UPDATED_QUANTITY_APPROVE
        defaultStockOrderProcessShouldNotBeFound("quantityApprove.greaterOrEqualThan=" + UPDATED_QUANTITY_APPROVE);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByQuantityApproveIsLessThanSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where quantityApprove less than or equals to DEFAULT_QUANTITY_APPROVE
        defaultStockOrderProcessShouldNotBeFound("quantityApprove.lessThan=" + DEFAULT_QUANTITY_APPROVE);

        // Get all the stockOrderProcessList where quantityApprove less than or equals to UPDATED_QUANTITY_APPROVE
        defaultStockOrderProcessShouldBeFound("quantityApprove.lessThan=" + UPDATED_QUANTITY_APPROVE);
    }


    @Test
    @Transactional
    public void getAllStockOrderProcessesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where createdDate equals to DEFAULT_CREATED_DATE
        defaultStockOrderProcessShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the stockOrderProcessList where createdDate equals to UPDATED_CREATED_DATE
        defaultStockOrderProcessShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultStockOrderProcessShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the stockOrderProcessList where createdDate equals to UPDATED_CREATED_DATE
        defaultStockOrderProcessShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        // Get all the stockOrderProcessList where createdDate is not null
        defaultStockOrderProcessShouldBeFound("createdDate.specified=true");

        // Get all the stockOrderProcessList where createdDate is null
        defaultStockOrderProcessShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderProcessesByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User creator = UserResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        stockOrderProcess.setCreator(creator);
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);
        Long creatorId = creator.getId();

        // Get all the stockOrderProcessList where creator equals to creatorId
        defaultStockOrderProcessShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the stockOrderProcessList where creator equals to creatorId + 1
        defaultStockOrderProcessShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStockOrderProcessShouldBeFound(String filter) throws Exception {
        restStockOrderProcessMockMvc.perform(get("/api/stock-order-processes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrderProcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].quantityRequest").value(hasItem(DEFAULT_QUANTITY_REQUEST)))
            .andExpect(jsonPath("$.[*].stockInHand").value(hasItem(DEFAULT_STOCK_IN_HAND)))
            .andExpect(jsonPath("$.[*].quantityApprove").value(hasItem(DEFAULT_QUANTITY_APPROVE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restStockOrderProcessMockMvc.perform(get("/api/stock-order-processes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStockOrderProcessShouldNotBeFound(String filter) throws Exception {
        restStockOrderProcessMockMvc.perform(get("/api/stock-order-processes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockOrderProcessMockMvc.perform(get("/api/stock-order-processes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStockOrderProcess() throws Exception {
        // Get the stockOrderProcess
        restStockOrderProcessMockMvc.perform(get("/api/stock-order-processes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOrderProcess() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        int databaseSizeBeforeUpdate = stockOrderProcessRepository.findAll().size();

        // Update the stockOrderProcess
        StockOrderProcess updatedStockOrderProcess = stockOrderProcessRepository.findById(stockOrderProcess.getId()).get();
        // Disconnect from session so that the updates on updatedStockOrderProcess are not directly saved in db
        em.detach(updatedStockOrderProcess);
        updatedStockOrderProcess
            .barcode(UPDATED_BARCODE)
            .name(UPDATED_NAME)
            .quantityRequest(UPDATED_QUANTITY_REQUEST)
            .stockInHand(UPDATED_STOCK_IN_HAND)
            .quantityApprove(UPDATED_QUANTITY_APPROVE)
            .createdDate(UPDATED_CREATED_DATE);
        StockOrderProcessDTO stockOrderProcessDTO = stockOrderProcessMapper.toDto(updatedStockOrderProcess);

        restStockOrderProcessMockMvc.perform(put("/api/stock-order-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderProcessDTO)))
            .andExpect(status().isOk());

        // Validate the StockOrderProcess in the database
        List<StockOrderProcess> stockOrderProcessList = stockOrderProcessRepository.findAll();
        assertThat(stockOrderProcessList).hasSize(databaseSizeBeforeUpdate);
        StockOrderProcess testStockOrderProcess = stockOrderProcessList.get(stockOrderProcessList.size() - 1);
        assertThat(testStockOrderProcess.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testStockOrderProcess.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStockOrderProcess.getQuantityRequest()).isEqualTo(UPDATED_QUANTITY_REQUEST);
        assertThat(testStockOrderProcess.getStockInHand()).isEqualTo(UPDATED_STOCK_IN_HAND);
        assertThat(testStockOrderProcess.getQuantityApprove()).isEqualTo(UPDATED_QUANTITY_APPROVE);
        assertThat(testStockOrderProcess.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);

        // Validate the StockOrderProcess in Elasticsearch
        verify(mockStockOrderProcessSearchRepository, times(1)).save(testStockOrderProcess);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOrderProcess() throws Exception {
        int databaseSizeBeforeUpdate = stockOrderProcessRepository.findAll().size();

        // Create the StockOrderProcess
        StockOrderProcessDTO stockOrderProcessDTO = stockOrderProcessMapper.toDto(stockOrderProcess);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockOrderProcessMockMvc.perform(put("/api/stock-order-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderProcessDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrderProcess in the database
        List<StockOrderProcess> stockOrderProcessList = stockOrderProcessRepository.findAll();
        assertThat(stockOrderProcessList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StockOrderProcess in Elasticsearch
        verify(mockStockOrderProcessSearchRepository, times(0)).save(stockOrderProcess);
    }

    @Test
    @Transactional
    public void deleteStockOrderProcess() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);

        int databaseSizeBeforeDelete = stockOrderProcessRepository.findAll().size();

        // Delete the stockOrderProcess
        restStockOrderProcessMockMvc.perform(delete("/api/stock-order-processes/{id}", stockOrderProcess.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOrderProcess> stockOrderProcessList = stockOrderProcessRepository.findAll();
        assertThat(stockOrderProcessList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StockOrderProcess in Elasticsearch
        verify(mockStockOrderProcessSearchRepository, times(1)).deleteById(stockOrderProcess.getId());
    }

    @Test
    @Transactional
    public void searchStockOrderProcess() throws Exception {
        // Initialize the database
        stockOrderProcessRepository.saveAndFlush(stockOrderProcess);
        when(mockStockOrderProcessSearchRepository.search(queryStringQuery("id:" + stockOrderProcess.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stockOrderProcess), PageRequest.of(0, 1), 1));
        // Search the stockOrderProcess
        restStockOrderProcessMockMvc.perform(get("/api/_search/stock-order-processes?query=id:" + stockOrderProcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrderProcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].quantityRequest").value(hasItem(DEFAULT_QUANTITY_REQUEST)))
            .andExpect(jsonPath("$.[*].stockInHand").value(hasItem(DEFAULT_STOCK_IN_HAND)))
            .andExpect(jsonPath("$.[*].quantityApprove").value(hasItem(DEFAULT_QUANTITY_APPROVE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrderProcess.class);
        StockOrderProcess stockOrderProcess1 = new StockOrderProcess();
        stockOrderProcess1.setId(1L);
        StockOrderProcess stockOrderProcess2 = new StockOrderProcess();
        stockOrderProcess2.setId(stockOrderProcess1.getId());
        assertThat(stockOrderProcess1).isEqualTo(stockOrderProcess2);
        stockOrderProcess2.setId(2L);
        assertThat(stockOrderProcess1).isNotEqualTo(stockOrderProcess2);
        stockOrderProcess1.setId(null);
        assertThat(stockOrderProcess1).isNotEqualTo(stockOrderProcess2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrderProcessDTO.class);
        StockOrderProcessDTO stockOrderProcessDTO1 = new StockOrderProcessDTO();
        stockOrderProcessDTO1.setId(1L);
        StockOrderProcessDTO stockOrderProcessDTO2 = new StockOrderProcessDTO();
        assertThat(stockOrderProcessDTO1).isNotEqualTo(stockOrderProcessDTO2);
        stockOrderProcessDTO2.setId(stockOrderProcessDTO1.getId());
        assertThat(stockOrderProcessDTO1).isEqualTo(stockOrderProcessDTO2);
        stockOrderProcessDTO2.setId(2L);
        assertThat(stockOrderProcessDTO1).isNotEqualTo(stockOrderProcessDTO2);
        stockOrderProcessDTO1.setId(null);
        assertThat(stockOrderProcessDTO1).isNotEqualTo(stockOrderProcessDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockOrderProcessMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockOrderProcessMapper.fromId(null)).isNull();
    }
}
