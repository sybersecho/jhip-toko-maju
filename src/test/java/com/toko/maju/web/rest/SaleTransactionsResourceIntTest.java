package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.SaleTransactions;
import com.toko.maju.domain.SaleItem;
import com.toko.maju.domain.Customer;
import com.toko.maju.domain.User;
import com.toko.maju.repository.SaleTransactionsRepository;
import com.toko.maju.repository.search.SaleTransactionsSearchRepository;
import com.toko.maju.service.SaleTransactionsService;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import com.toko.maju.service.mapper.SaleTransactionsMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.SaleTransactionsCriteria;
import com.toko.maju.service.SaleTransactionsQueryService;

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
 * Test class for the SaleTransactionsResource REST controller.
 *
 * @see SaleTransactionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class SaleTransactionsResourceIntTest {

    private static final String DEFAULT_NO_INVOICE = "AAAAAAAAAA";
    private static final String UPDATED_NO_INVOICE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DISCOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISCOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_TOTAL_PAYMENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PAYMENT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_REMAINING_PAYMENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_REMAINING_PAYMENT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_PAID = new BigDecimal(0);
    private static final BigDecimal UPDATED_PAID = new BigDecimal(1);

    private static final Instant DEFAULT_SALE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SALE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_SETTLED = false;
    private static final Boolean UPDATED_SETTLED = true;

    @Autowired
    private SaleTransactionsRepository saleTransactionsRepository;

    @Autowired
    private SaleTransactionsMapper saleTransactionsMapper;

    @Autowired
    private SaleTransactionsService saleTransactionsService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.SaleTransactionsSearchRepositoryMockConfiguration
     */
    @Autowired
    private SaleTransactionsSearchRepository mockSaleTransactionsSearchRepository;

    @Autowired
    private SaleTransactionsQueryService saleTransactionsQueryService;

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

    private MockMvc restSaleTransactionsMockMvc;

    private SaleTransactions saleTransactions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaleTransactionsResource saleTransactionsResource = new SaleTransactionsResource(saleTransactionsService, saleTransactionsQueryService);
        this.restSaleTransactionsMockMvc = MockMvcBuilders.standaloneSetup(saleTransactionsResource)
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
    public static SaleTransactions createEntity(EntityManager em) {
        SaleTransactions saleTransactions = new SaleTransactions()
            .noInvoice(DEFAULT_NO_INVOICE)
            .discount(DEFAULT_DISCOUNT)
            .totalPayment(DEFAULT_TOTAL_PAYMENT)
            .remainingPayment(DEFAULT_REMAINING_PAYMENT)
            .paid(DEFAULT_PAID)
            .saleDate(DEFAULT_SALE_DATE)
            .settled(DEFAULT_SETTLED);
        // Add required entity
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        saleTransactions.setCustomer(customer);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        saleTransactions.setCreator(user);
        return saleTransactions;
    }

    @Before
    public void initTest() {
        saleTransactions = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaleTransactions() throws Exception {
        int databaseSizeBeforeCreate = saleTransactionsRepository.findAll().size();

        // Create the SaleTransactions
        SaleTransactionsDTO saleTransactionsDTO = saleTransactionsMapper.toDto(saleTransactions);
        restSaleTransactionsMockMvc.perform(post("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleTransactionsDTO)))
            .andExpect(status().isCreated());

        // Validate the SaleTransactions in the database
        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeCreate + 1);
        SaleTransactions testSaleTransactions = saleTransactionsList.get(saleTransactionsList.size() - 1);
        assertThat(testSaleTransactions.getNoInvoice()).isEqualTo(DEFAULT_NO_INVOICE);
        assertThat(testSaleTransactions.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testSaleTransactions.getTotalPayment()).isEqualTo(DEFAULT_TOTAL_PAYMENT);
        assertThat(testSaleTransactions.getRemainingPayment()).isEqualTo(DEFAULT_REMAINING_PAYMENT);
        assertThat(testSaleTransactions.getPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testSaleTransactions.getSaleDate()).isEqualTo(DEFAULT_SALE_DATE);
        assertThat(testSaleTransactions.isSettled()).isEqualTo(DEFAULT_SETTLED);

        // Validate the SaleTransactions in Elasticsearch
        verify(mockSaleTransactionsSearchRepository, times(1)).save(testSaleTransactions);
    }

    @Test
    @Transactional
    public void createSaleTransactionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saleTransactionsRepository.findAll().size();

        // Create the SaleTransactions with an existing ID
        saleTransactions.setId(1L);
        SaleTransactionsDTO saleTransactionsDTO = saleTransactionsMapper.toDto(saleTransactions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleTransactionsMockMvc.perform(post("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleTransactionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SaleTransactions in the database
        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeCreate);

        // Validate the SaleTransactions in Elasticsearch
        verify(mockSaleTransactionsSearchRepository, times(0)).save(saleTransactions);
    }

    @Test
    @Transactional
    public void checkTotalPaymentIsRequired() throws Exception {
        int databaseSizeBeforeTest = saleTransactionsRepository.findAll().size();
        // set the field null
        saleTransactions.setTotalPayment(null);

        // Create the SaleTransactions, which fails.
        SaleTransactionsDTO saleTransactionsDTO = saleTransactionsMapper.toDto(saleTransactions);

        restSaleTransactionsMockMvc.perform(post("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleTransactionsDTO)))
            .andExpect(status().isBadRequest());

        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = saleTransactionsRepository.findAll().size();
        // set the field null
        saleTransactions.setPaid(null);

        // Create the SaleTransactions, which fails.
        SaleTransactionsDTO saleTransactionsDTO = saleTransactionsMapper.toDto(saleTransactions);

        restSaleTransactionsMockMvc.perform(post("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleTransactionsDTO)))
            .andExpect(status().isBadRequest());

        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSettledIsRequired() throws Exception {
        int databaseSizeBeforeTest = saleTransactionsRepository.findAll().size();
        // set the field null
        saleTransactions.setSettled(null);

        // Create the SaleTransactions, which fails.
        SaleTransactionsDTO saleTransactionsDTO = saleTransactionsMapper.toDto(saleTransactions);

        restSaleTransactionsMockMvc.perform(post("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleTransactionsDTO)))
            .andExpect(status().isBadRequest());

        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSaleTransactions() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList
        restSaleTransactionsMockMvc.perform(get("/api/sale-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].noInvoice").value(hasItem(DEFAULT_NO_INVOICE.toString())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.intValue())))
            .andExpect(jsonPath("$.[*].totalPayment").value(hasItem(DEFAULT_TOTAL_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].remainingPayment").value(hasItem(DEFAULT_REMAINING_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].settled").value(hasItem(DEFAULT_SETTLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSaleTransactions() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get the saleTransactions
        restSaleTransactionsMockMvc.perform(get("/api/sale-transactions/{id}", saleTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saleTransactions.getId().intValue()))
            .andExpect(jsonPath("$.noInvoice").value(DEFAULT_NO_INVOICE.toString()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.intValue()))
            .andExpect(jsonPath("$.totalPayment").value(DEFAULT_TOTAL_PAYMENT.intValue()))
            .andExpect(jsonPath("$.remainingPayment").value(DEFAULT_REMAINING_PAYMENT.intValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.intValue()))
            .andExpect(jsonPath("$.saleDate").value(DEFAULT_SALE_DATE.toString()))
            .andExpect(jsonPath("$.settled").value(DEFAULT_SETTLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByNoInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where noInvoice equals to DEFAULT_NO_INVOICE
        defaultSaleTransactionsShouldBeFound("noInvoice.equals=" + DEFAULT_NO_INVOICE);

        // Get all the saleTransactionsList where noInvoice equals to UPDATED_NO_INVOICE
        defaultSaleTransactionsShouldNotBeFound("noInvoice.equals=" + UPDATED_NO_INVOICE);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByNoInvoiceIsInShouldWork() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where noInvoice in DEFAULT_NO_INVOICE or UPDATED_NO_INVOICE
        defaultSaleTransactionsShouldBeFound("noInvoice.in=" + DEFAULT_NO_INVOICE + "," + UPDATED_NO_INVOICE);

        // Get all the saleTransactionsList where noInvoice equals to UPDATED_NO_INVOICE
        defaultSaleTransactionsShouldNotBeFound("noInvoice.in=" + UPDATED_NO_INVOICE);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByNoInvoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where noInvoice is not null
        defaultSaleTransactionsShouldBeFound("noInvoice.specified=true");

        // Get all the saleTransactionsList where noInvoice is null
        defaultSaleTransactionsShouldNotBeFound("noInvoice.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where discount equals to DEFAULT_DISCOUNT
        defaultSaleTransactionsShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the saleTransactionsList where discount equals to UPDATED_DISCOUNT
        defaultSaleTransactionsShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultSaleTransactionsShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the saleTransactionsList where discount equals to UPDATED_DISCOUNT
        defaultSaleTransactionsShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where discount is not null
        defaultSaleTransactionsShouldBeFound("discount.specified=true");

        // Get all the saleTransactionsList where discount is null
        defaultSaleTransactionsShouldNotBeFound("discount.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByTotalPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where totalPayment equals to DEFAULT_TOTAL_PAYMENT
        defaultSaleTransactionsShouldBeFound("totalPayment.equals=" + DEFAULT_TOTAL_PAYMENT);

        // Get all the saleTransactionsList where totalPayment equals to UPDATED_TOTAL_PAYMENT
        defaultSaleTransactionsShouldNotBeFound("totalPayment.equals=" + UPDATED_TOTAL_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByTotalPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where totalPayment in DEFAULT_TOTAL_PAYMENT or UPDATED_TOTAL_PAYMENT
        defaultSaleTransactionsShouldBeFound("totalPayment.in=" + DEFAULT_TOTAL_PAYMENT + "," + UPDATED_TOTAL_PAYMENT);

        // Get all the saleTransactionsList where totalPayment equals to UPDATED_TOTAL_PAYMENT
        defaultSaleTransactionsShouldNotBeFound("totalPayment.in=" + UPDATED_TOTAL_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByTotalPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where totalPayment is not null
        defaultSaleTransactionsShouldBeFound("totalPayment.specified=true");

        // Get all the saleTransactionsList where totalPayment is null
        defaultSaleTransactionsShouldNotBeFound("totalPayment.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByRemainingPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where remainingPayment equals to DEFAULT_REMAINING_PAYMENT
        defaultSaleTransactionsShouldBeFound("remainingPayment.equals=" + DEFAULT_REMAINING_PAYMENT);

        // Get all the saleTransactionsList where remainingPayment equals to UPDATED_REMAINING_PAYMENT
        defaultSaleTransactionsShouldNotBeFound("remainingPayment.equals=" + UPDATED_REMAINING_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByRemainingPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where remainingPayment in DEFAULT_REMAINING_PAYMENT or UPDATED_REMAINING_PAYMENT
        defaultSaleTransactionsShouldBeFound("remainingPayment.in=" + DEFAULT_REMAINING_PAYMENT + "," + UPDATED_REMAINING_PAYMENT);

        // Get all the saleTransactionsList where remainingPayment equals to UPDATED_REMAINING_PAYMENT
        defaultSaleTransactionsShouldNotBeFound("remainingPayment.in=" + UPDATED_REMAINING_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByRemainingPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where remainingPayment is not null
        defaultSaleTransactionsShouldBeFound("remainingPayment.specified=true");

        // Get all the saleTransactionsList where remainingPayment is null
        defaultSaleTransactionsShouldNotBeFound("remainingPayment.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where paid equals to DEFAULT_PAID
        defaultSaleTransactionsShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the saleTransactionsList where paid equals to UPDATED_PAID
        defaultSaleTransactionsShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultSaleTransactionsShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the saleTransactionsList where paid equals to UPDATED_PAID
        defaultSaleTransactionsShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where paid is not null
        defaultSaleTransactionsShouldBeFound("paid.specified=true");

        // Get all the saleTransactionsList where paid is null
        defaultSaleTransactionsShouldNotBeFound("paid.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsBySaleDateIsEqualToSomething() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where saleDate equals to DEFAULT_SALE_DATE
        defaultSaleTransactionsShouldBeFound("saleDate.equals=" + DEFAULT_SALE_DATE);

        // Get all the saleTransactionsList where saleDate equals to UPDATED_SALE_DATE
        defaultSaleTransactionsShouldNotBeFound("saleDate.equals=" + UPDATED_SALE_DATE);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsBySaleDateIsInShouldWork() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where saleDate in DEFAULT_SALE_DATE or UPDATED_SALE_DATE
        defaultSaleTransactionsShouldBeFound("saleDate.in=" + DEFAULT_SALE_DATE + "," + UPDATED_SALE_DATE);

        // Get all the saleTransactionsList where saleDate equals to UPDATED_SALE_DATE
        defaultSaleTransactionsShouldNotBeFound("saleDate.in=" + UPDATED_SALE_DATE);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsBySaleDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where saleDate is not null
        defaultSaleTransactionsShouldBeFound("saleDate.specified=true");

        // Get all the saleTransactionsList where saleDate is null
        defaultSaleTransactionsShouldNotBeFound("saleDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsBySettledIsEqualToSomething() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where settled equals to DEFAULT_SETTLED
        defaultSaleTransactionsShouldBeFound("settled.equals=" + DEFAULT_SETTLED);

        // Get all the saleTransactionsList where settled equals to UPDATED_SETTLED
        defaultSaleTransactionsShouldNotBeFound("settled.equals=" + UPDATED_SETTLED);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsBySettledIsInShouldWork() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where settled in DEFAULT_SETTLED or UPDATED_SETTLED
        defaultSaleTransactionsShouldBeFound("settled.in=" + DEFAULT_SETTLED + "," + UPDATED_SETTLED);

        // Get all the saleTransactionsList where settled equals to UPDATED_SETTLED
        defaultSaleTransactionsShouldNotBeFound("settled.in=" + UPDATED_SETTLED);
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsBySettledIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        // Get all the saleTransactionsList where settled is not null
        defaultSaleTransactionsShouldBeFound("settled.specified=true");

        // Get all the saleTransactionsList where settled is null
        defaultSaleTransactionsShouldNotBeFound("settled.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleTransactionsByItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        SaleItem items = SaleItemResourceIntTest.createEntity(em);
        em.persist(items);
        em.flush();
        saleTransactions.addItems(items);
        saleTransactionsRepository.saveAndFlush(saleTransactions);
        Long itemsId = items.getId();

        // Get all the saleTransactionsList where items equals to itemsId
        defaultSaleTransactionsShouldBeFound("itemsId.equals=" + itemsId);

        // Get all the saleTransactionsList where items equals to itemsId + 1
        defaultSaleTransactionsShouldNotBeFound("itemsId.equals=" + (itemsId + 1));
    }


    @Test
    @Transactional
    public void getAllSaleTransactionsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        saleTransactions.setCustomer(customer);
        saleTransactionsRepository.saveAndFlush(saleTransactions);
        Long customerId = customer.getId();

        // Get all the saleTransactionsList where customer equals to customerId
        defaultSaleTransactionsShouldBeFound("customerId.equals=" + customerId);

        // Get all the saleTransactionsList where customer equals to customerId + 1
        defaultSaleTransactionsShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }


    @Test
    @Transactional
    public void getAllSaleTransactionsByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User creator = UserResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        saleTransactions.setCreator(creator);
        saleTransactionsRepository.saveAndFlush(saleTransactions);
        Long creatorId = creator.getId();

        // Get all the saleTransactionsList where creator equals to creatorId
        defaultSaleTransactionsShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the saleTransactionsList where creator equals to creatorId + 1
        defaultSaleTransactionsShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSaleTransactionsShouldBeFound(String filter) throws Exception {
        restSaleTransactionsMockMvc.perform(get("/api/sale-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].noInvoice").value(hasItem(DEFAULT_NO_INVOICE)))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.intValue())))
            .andExpect(jsonPath("$.[*].totalPayment").value(hasItem(DEFAULT_TOTAL_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].remainingPayment").value(hasItem(DEFAULT_REMAINING_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].settled").value(hasItem(DEFAULT_SETTLED.booleanValue())));

        // Check, that the count call also returns 1
        restSaleTransactionsMockMvc.perform(get("/api/sale-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSaleTransactionsShouldNotBeFound(String filter) throws Exception {
        restSaleTransactionsMockMvc.perform(get("/api/sale-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSaleTransactionsMockMvc.perform(get("/api/sale-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSaleTransactions() throws Exception {
        // Get the saleTransactions
        restSaleTransactionsMockMvc.perform(get("/api/sale-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaleTransactions() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        int databaseSizeBeforeUpdate = saleTransactionsRepository.findAll().size();

        // Update the saleTransactions
        SaleTransactions updatedSaleTransactions = saleTransactionsRepository.findById(saleTransactions.getId()).get();
        // Disconnect from session so that the updates on updatedSaleTransactions are not directly saved in db
        em.detach(updatedSaleTransactions);
        updatedSaleTransactions
            .noInvoice(UPDATED_NO_INVOICE)
            .discount(UPDATED_DISCOUNT)
            .totalPayment(UPDATED_TOTAL_PAYMENT)
            .remainingPayment(UPDATED_REMAINING_PAYMENT)
            .paid(UPDATED_PAID)
            .saleDate(UPDATED_SALE_DATE)
            .settled(UPDATED_SETTLED);
        SaleTransactionsDTO saleTransactionsDTO = saleTransactionsMapper.toDto(updatedSaleTransactions);

        restSaleTransactionsMockMvc.perform(put("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleTransactionsDTO)))
            .andExpect(status().isOk());

        // Validate the SaleTransactions in the database
        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeUpdate);
        SaleTransactions testSaleTransactions = saleTransactionsList.get(saleTransactionsList.size() - 1);
        assertThat(testSaleTransactions.getNoInvoice()).isEqualTo(UPDATED_NO_INVOICE);
        assertThat(testSaleTransactions.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testSaleTransactions.getTotalPayment()).isEqualTo(UPDATED_TOTAL_PAYMENT);
        assertThat(testSaleTransactions.getRemainingPayment()).isEqualTo(UPDATED_REMAINING_PAYMENT);
        assertThat(testSaleTransactions.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testSaleTransactions.getSaleDate()).isEqualTo(UPDATED_SALE_DATE);
        assertThat(testSaleTransactions.isSettled()).isEqualTo(UPDATED_SETTLED);

        // Validate the SaleTransactions in Elasticsearch
        verify(mockSaleTransactionsSearchRepository, times(1)).save(testSaleTransactions);
    }

    @Test
    @Transactional
    public void updateNonExistingSaleTransactions() throws Exception {
        int databaseSizeBeforeUpdate = saleTransactionsRepository.findAll().size();

        // Create the SaleTransactions
        SaleTransactionsDTO saleTransactionsDTO = saleTransactionsMapper.toDto(saleTransactions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleTransactionsMockMvc.perform(put("/api/sale-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleTransactionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SaleTransactions in the database
        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SaleTransactions in Elasticsearch
        verify(mockSaleTransactionsSearchRepository, times(0)).save(saleTransactions);
    }

    @Test
    @Transactional
    public void deleteSaleTransactions() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);

        int databaseSizeBeforeDelete = saleTransactionsRepository.findAll().size();

        // Delete the saleTransactions
        restSaleTransactionsMockMvc.perform(delete("/api/sale-transactions/{id}", saleTransactions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SaleTransactions> saleTransactionsList = saleTransactionsRepository.findAll();
        assertThat(saleTransactionsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SaleTransactions in Elasticsearch
        verify(mockSaleTransactionsSearchRepository, times(1)).deleteById(saleTransactions.getId());
    }

    @Test
    @Transactional
    public void searchSaleTransactions() throws Exception {
        // Initialize the database
        saleTransactionsRepository.saveAndFlush(saleTransactions);
        when(mockSaleTransactionsSearchRepository.search(queryStringQuery("id:" + saleTransactions.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(saleTransactions), PageRequest.of(0, 1), 1));
        // Search the saleTransactions
        restSaleTransactionsMockMvc.perform(get("/api/_search/sale-transactions?query=id:" + saleTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].noInvoice").value(hasItem(DEFAULT_NO_INVOICE)))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.intValue())))
            .andExpect(jsonPath("$.[*].totalPayment").value(hasItem(DEFAULT_TOTAL_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].remainingPayment").value(hasItem(DEFAULT_REMAINING_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].settled").value(hasItem(DEFAULT_SETTLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleTransactions.class);
        SaleTransactions saleTransactions1 = new SaleTransactions();
        saleTransactions1.setId(1L);
        SaleTransactions saleTransactions2 = new SaleTransactions();
        saleTransactions2.setId(saleTransactions1.getId());
        assertThat(saleTransactions1).isEqualTo(saleTransactions2);
        saleTransactions2.setId(2L);
        assertThat(saleTransactions1).isNotEqualTo(saleTransactions2);
        saleTransactions1.setId(null);
        assertThat(saleTransactions1).isNotEqualTo(saleTransactions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleTransactionsDTO.class);
        SaleTransactionsDTO saleTransactionsDTO1 = new SaleTransactionsDTO();
        saleTransactionsDTO1.setId(1L);
        SaleTransactionsDTO saleTransactionsDTO2 = new SaleTransactionsDTO();
        assertThat(saleTransactionsDTO1).isNotEqualTo(saleTransactionsDTO2);
        saleTransactionsDTO2.setId(saleTransactionsDTO1.getId());
        assertThat(saleTransactionsDTO1).isEqualTo(saleTransactionsDTO2);
        saleTransactionsDTO2.setId(2L);
        assertThat(saleTransactionsDTO1).isNotEqualTo(saleTransactionsDTO2);
        saleTransactionsDTO1.setId(null);
        assertThat(saleTransactionsDTO1).isNotEqualTo(saleTransactionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(saleTransactionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(saleTransactionsMapper.fromId(null)).isNull();
    }
}
