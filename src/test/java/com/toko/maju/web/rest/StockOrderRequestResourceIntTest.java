package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.StockOrderRequest;
import com.toko.maju.domain.StockOrder;
import com.toko.maju.repository.StockOrderRequestRepository;
import com.toko.maju.repository.search.StockOrderRequestSearchRepository;
import com.toko.maju.service.StockOrderRequestService;
import com.toko.maju.service.dto.StockOrderRequestDTO;
import com.toko.maju.service.mapper.StockOrderRequestMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.StockOrderRequestCriteria;
import com.toko.maju.service.StockOrderRequestQueryService;

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
import java.math.BigDecimal;
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
 * Test class for the StockOrderRequestResource REST controller.
 *
 * @see StockOrderRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class StockOrderRequestResourceIntTest {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private StockOrderRequestRepository stockOrderRequestRepository;

    @Autowired
    private StockOrderRequestMapper stockOrderRequestMapper;

    @Autowired
    private StockOrderRequestService stockOrderRequestService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.StockOrderRequestSearchRepositoryMockConfiguration
     */
    @Autowired
    private StockOrderRequestSearchRepository mockStockOrderRequestSearchRepository;

    @Autowired
    private StockOrderRequestQueryService stockOrderRequestQueryService;

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

    private MockMvc restStockOrderRequestMockMvc;

    private StockOrderRequest stockOrderRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockOrderRequestResource stockOrderRequestResource = new StockOrderRequestResource(stockOrderRequestService, stockOrderRequestQueryService);
        this.restStockOrderRequestMockMvc = MockMvcBuilders.standaloneSetup(stockOrderRequestResource)
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
    public static StockOrderRequest createEntity(EntityManager em) {
        StockOrderRequest stockOrderRequest = new StockOrderRequest()
            .barcode(DEFAULT_BARCODE)
            .name(DEFAULT_NAME)
            .unitName(DEFAULT_UNIT_NAME)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .createdDate(DEFAULT_CREATED_DATE);
        return stockOrderRequest;
    }

    @Before
    public void initTest() {
        stockOrderRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOrderRequest() throws Exception {
        int databaseSizeBeforeCreate = stockOrderRequestRepository.findAll().size();

        // Create the StockOrderRequest
        StockOrderRequestDTO stockOrderRequestDTO = stockOrderRequestMapper.toDto(stockOrderRequest);
        restStockOrderRequestMockMvc.perform(post("/api/stock-order-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOrderRequest in the database
        List<StockOrderRequest> stockOrderRequestList = stockOrderRequestRepository.findAll();
        assertThat(stockOrderRequestList).hasSize(databaseSizeBeforeCreate + 1);
        StockOrderRequest testStockOrderRequest = stockOrderRequestList.get(stockOrderRequestList.size() - 1);
        assertThat(testStockOrderRequest.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testStockOrderRequest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStockOrderRequest.getUnitName()).isEqualTo(DEFAULT_UNIT_NAME);
        assertThat(testStockOrderRequest.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testStockOrderRequest.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockOrderRequest.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testStockOrderRequest.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);

        // Validate the StockOrderRequest in Elasticsearch
        verify(mockStockOrderRequestSearchRepository, times(1)).save(testStockOrderRequest);
    }

    @Test
    @Transactional
    public void createStockOrderRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOrderRequestRepository.findAll().size();

        // Create the StockOrderRequest with an existing ID
        stockOrderRequest.setId(1L);
        StockOrderRequestDTO stockOrderRequestDTO = stockOrderRequestMapper.toDto(stockOrderRequest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOrderRequestMockMvc.perform(post("/api/stock-order-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrderRequest in the database
        List<StockOrderRequest> stockOrderRequestList = stockOrderRequestRepository.findAll();
        assertThat(stockOrderRequestList).hasSize(databaseSizeBeforeCreate);

        // Validate the StockOrderRequest in Elasticsearch
        verify(mockStockOrderRequestSearchRepository, times(0)).save(stockOrderRequest);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequests() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList
        restStockOrderRequestMockMvc.perform(get("/api/stock-order-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrderRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME.toString())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getStockOrderRequest() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get the stockOrderRequest
        restStockOrderRequestMockMvc.perform(get("/api/stock-order-requests/{id}", stockOrderRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOrderRequest.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.unitName").value(DEFAULT_UNIT_NAME.toString()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where barcode equals to DEFAULT_BARCODE
        defaultStockOrderRequestShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the stockOrderRequestList where barcode equals to UPDATED_BARCODE
        defaultStockOrderRequestShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultStockOrderRequestShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the stockOrderRequestList where barcode equals to UPDATED_BARCODE
        defaultStockOrderRequestShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where barcode is not null
        defaultStockOrderRequestShouldBeFound("barcode.specified=true");

        // Get all the stockOrderRequestList where barcode is null
        defaultStockOrderRequestShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where name equals to DEFAULT_NAME
        defaultStockOrderRequestShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the stockOrderRequestList where name equals to UPDATED_NAME
        defaultStockOrderRequestShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStockOrderRequestShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the stockOrderRequestList where name equals to UPDATED_NAME
        defaultStockOrderRequestShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where name is not null
        defaultStockOrderRequestShouldBeFound("name.specified=true");

        // Get all the stockOrderRequestList where name is null
        defaultStockOrderRequestShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByUnitNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where unitName equals to DEFAULT_UNIT_NAME
        defaultStockOrderRequestShouldBeFound("unitName.equals=" + DEFAULT_UNIT_NAME);

        // Get all the stockOrderRequestList where unitName equals to UPDATED_UNIT_NAME
        defaultStockOrderRequestShouldNotBeFound("unitName.equals=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByUnitNameIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where unitName in DEFAULT_UNIT_NAME or UPDATED_UNIT_NAME
        defaultStockOrderRequestShouldBeFound("unitName.in=" + DEFAULT_UNIT_NAME + "," + UPDATED_UNIT_NAME);

        // Get all the stockOrderRequestList where unitName equals to UPDATED_UNIT_NAME
        defaultStockOrderRequestShouldNotBeFound("unitName.in=" + UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByUnitNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where unitName is not null
        defaultStockOrderRequestShouldBeFound("unitName.specified=true");

        // Get all the stockOrderRequestList where unitName is null
        defaultStockOrderRequestShouldNotBeFound("unitName.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultStockOrderRequestShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the stockOrderRequestList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultStockOrderRequestShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultStockOrderRequestShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the stockOrderRequestList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultStockOrderRequestShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where unitPrice is not null
        defaultStockOrderRequestShouldBeFound("unitPrice.specified=true");

        // Get all the stockOrderRequestList where unitPrice is null
        defaultStockOrderRequestShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where quantity equals to DEFAULT_QUANTITY
        defaultStockOrderRequestShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the stockOrderRequestList where quantity equals to UPDATED_QUANTITY
        defaultStockOrderRequestShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultStockOrderRequestShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the stockOrderRequestList where quantity equals to UPDATED_QUANTITY
        defaultStockOrderRequestShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where quantity is not null
        defaultStockOrderRequestShouldBeFound("quantity.specified=true");

        // Get all the stockOrderRequestList where quantity is null
        defaultStockOrderRequestShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultStockOrderRequestShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the stockOrderRequestList where quantity greater than or equals to UPDATED_QUANTITY
        defaultStockOrderRequestShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where quantity less than or equals to DEFAULT_QUANTITY
        defaultStockOrderRequestShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the stockOrderRequestList where quantity less than or equals to UPDATED_QUANTITY
        defaultStockOrderRequestShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllStockOrderRequestsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultStockOrderRequestShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the stockOrderRequestList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultStockOrderRequestShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultStockOrderRequestShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the stockOrderRequestList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultStockOrderRequestShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where totalPrice is not null
        defaultStockOrderRequestShouldBeFound("totalPrice.specified=true");

        // Get all the stockOrderRequestList where totalPrice is null
        defaultStockOrderRequestShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where createdDate equals to DEFAULT_CREATED_DATE
        defaultStockOrderRequestShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the stockOrderRequestList where createdDate equals to UPDATED_CREATED_DATE
        defaultStockOrderRequestShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultStockOrderRequestShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the stockOrderRequestList where createdDate equals to UPDATED_CREATED_DATE
        defaultStockOrderRequestShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        // Get all the stockOrderRequestList where createdDate is not null
        defaultStockOrderRequestShouldBeFound("createdDate.specified=true");

        // Get all the stockOrderRequestList where createdDate is null
        defaultStockOrderRequestShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderRequestsByStockOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        StockOrder stockOrder = StockOrderResourceIntTest.createEntity(em);
        em.persist(stockOrder);
        em.flush();
        stockOrderRequest.setStockOrder(stockOrder);
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);
        Long stockOrderId = stockOrder.getId();

        // Get all the stockOrderRequestList where stockOrder equals to stockOrderId
        defaultStockOrderRequestShouldBeFound("stockOrderId.equals=" + stockOrderId);

        // Get all the stockOrderRequestList where stockOrder equals to stockOrderId + 1
        defaultStockOrderRequestShouldNotBeFound("stockOrderId.equals=" + (stockOrderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStockOrderRequestShouldBeFound(String filter) throws Exception {
        restStockOrderRequestMockMvc.perform(get("/api/stock-order-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrderRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restStockOrderRequestMockMvc.perform(get("/api/stock-order-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStockOrderRequestShouldNotBeFound(String filter) throws Exception {
        restStockOrderRequestMockMvc.perform(get("/api/stock-order-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockOrderRequestMockMvc.perform(get("/api/stock-order-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStockOrderRequest() throws Exception {
        // Get the stockOrderRequest
        restStockOrderRequestMockMvc.perform(get("/api/stock-order-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOrderRequest() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        int databaseSizeBeforeUpdate = stockOrderRequestRepository.findAll().size();

        // Update the stockOrderRequest
        StockOrderRequest updatedStockOrderRequest = stockOrderRequestRepository.findById(stockOrderRequest.getId()).get();
        // Disconnect from session so that the updates on updatedStockOrderRequest are not directly saved in db
        em.detach(updatedStockOrderRequest);
        updatedStockOrderRequest
            .barcode(UPDATED_BARCODE)
            .name(UPDATED_NAME)
            .unitName(UPDATED_UNIT_NAME)
            .unitPrice(UPDATED_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdDate(UPDATED_CREATED_DATE);
        StockOrderRequestDTO stockOrderRequestDTO = stockOrderRequestMapper.toDto(updatedStockOrderRequest);

        restStockOrderRequestMockMvc.perform(put("/api/stock-order-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderRequestDTO)))
            .andExpect(status().isOk());

        // Validate the StockOrderRequest in the database
        List<StockOrderRequest> stockOrderRequestList = stockOrderRequestRepository.findAll();
        assertThat(stockOrderRequestList).hasSize(databaseSizeBeforeUpdate);
        StockOrderRequest testStockOrderRequest = stockOrderRequestList.get(stockOrderRequestList.size() - 1);
        assertThat(testStockOrderRequest.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testStockOrderRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStockOrderRequest.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testStockOrderRequest.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testStockOrderRequest.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockOrderRequest.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testStockOrderRequest.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);

        // Validate the StockOrderRequest in Elasticsearch
        verify(mockStockOrderRequestSearchRepository, times(1)).save(testStockOrderRequest);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOrderRequest() throws Exception {
        int databaseSizeBeforeUpdate = stockOrderRequestRepository.findAll().size();

        // Create the StockOrderRequest
        StockOrderRequestDTO stockOrderRequestDTO = stockOrderRequestMapper.toDto(stockOrderRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockOrderRequestMockMvc.perform(put("/api/stock-order-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrderRequest in the database
        List<StockOrderRequest> stockOrderRequestList = stockOrderRequestRepository.findAll();
        assertThat(stockOrderRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StockOrderRequest in Elasticsearch
        verify(mockStockOrderRequestSearchRepository, times(0)).save(stockOrderRequest);
    }

    @Test
    @Transactional
    public void deleteStockOrderRequest() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);

        int databaseSizeBeforeDelete = stockOrderRequestRepository.findAll().size();

        // Delete the stockOrderRequest
        restStockOrderRequestMockMvc.perform(delete("/api/stock-order-requests/{id}", stockOrderRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOrderRequest> stockOrderRequestList = stockOrderRequestRepository.findAll();
        assertThat(stockOrderRequestList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StockOrderRequest in Elasticsearch
        verify(mockStockOrderRequestSearchRepository, times(1)).deleteById(stockOrderRequest.getId());
    }

    @Test
    @Transactional
    public void searchStockOrderRequest() throws Exception {
        // Initialize the database
        stockOrderRequestRepository.saveAndFlush(stockOrderRequest);
        when(mockStockOrderRequestSearchRepository.search(queryStringQuery("id:" + stockOrderRequest.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stockOrderRequest), PageRequest.of(0, 1), 1));
        // Search the stockOrderRequest
        restStockOrderRequestMockMvc.perform(get("/api/_search/stock-order-requests?query=id:" + stockOrderRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrderRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrderRequest.class);
        StockOrderRequest stockOrderRequest1 = new StockOrderRequest();
        stockOrderRequest1.setId(1L);
        StockOrderRequest stockOrderRequest2 = new StockOrderRequest();
        stockOrderRequest2.setId(stockOrderRequest1.getId());
        assertThat(stockOrderRequest1).isEqualTo(stockOrderRequest2);
        stockOrderRequest2.setId(2L);
        assertThat(stockOrderRequest1).isNotEqualTo(stockOrderRequest2);
        stockOrderRequest1.setId(null);
        assertThat(stockOrderRequest1).isNotEqualTo(stockOrderRequest2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrderRequestDTO.class);
        StockOrderRequestDTO stockOrderRequestDTO1 = new StockOrderRequestDTO();
        stockOrderRequestDTO1.setId(1L);
        StockOrderRequestDTO stockOrderRequestDTO2 = new StockOrderRequestDTO();
        assertThat(stockOrderRequestDTO1).isNotEqualTo(stockOrderRequestDTO2);
        stockOrderRequestDTO2.setId(stockOrderRequestDTO1.getId());
        assertThat(stockOrderRequestDTO1).isEqualTo(stockOrderRequestDTO2);
        stockOrderRequestDTO2.setId(2L);
        assertThat(stockOrderRequestDTO1).isNotEqualTo(stockOrderRequestDTO2);
        stockOrderRequestDTO1.setId(null);
        assertThat(stockOrderRequestDTO1).isNotEqualTo(stockOrderRequestDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockOrderRequestMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockOrderRequestMapper.fromId(null)).isNull();
    }
}
