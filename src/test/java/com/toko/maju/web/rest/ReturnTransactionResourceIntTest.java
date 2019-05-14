package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.ReturnTransaction;
import com.toko.maju.domain.User;
import com.toko.maju.domain.Customer;
import com.toko.maju.domain.Supplier;
import com.toko.maju.domain.ReturnItem;
import com.toko.maju.repository.ReturnTransactionRepository;
import com.toko.maju.repository.search.ReturnTransactionSearchRepository;
import com.toko.maju.service.ReturnTransactionService;
import com.toko.maju.service.dto.ReturnTransactionDTO;
import com.toko.maju.service.mapper.ReturnTransactionMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.ReturnTransactionCriteria;
import com.toko.maju.service.ReturnTransactionQueryService;

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

import com.toko.maju.domain.enumeration.TransactionType;
/**
 * Test class for the ReturnTransactionResource REST controller.
 *
 * @see ReturnTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class ReturnTransactionResourceIntTest {

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.SHOP;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.SUPPLIER;

    private static final BigDecimal DEFAULT_TOTAL_PRICE_RETURN = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE_RETURN = new BigDecimal(2);

    @Autowired
    private ReturnTransactionRepository returnTransactionRepository;

    @Autowired
    private ReturnTransactionMapper returnTransactionMapper;

    @Autowired
    private ReturnTransactionService returnTransactionService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.ReturnTransactionSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReturnTransactionSearchRepository mockReturnTransactionSearchRepository;

    @Autowired
    private ReturnTransactionQueryService returnTransactionQueryService;

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

    private MockMvc restReturnTransactionMockMvc;

    private ReturnTransaction returnTransaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReturnTransactionResource returnTransactionResource = new ReturnTransactionResource(returnTransactionService, returnTransactionQueryService);
        this.restReturnTransactionMockMvc = MockMvcBuilders.standaloneSetup(returnTransactionResource)
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
    public static ReturnTransaction createEntity(EntityManager em) {
        ReturnTransaction returnTransaction = new ReturnTransaction()
            .created_date(DEFAULT_CREATED_DATE)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .totalPriceReturn(DEFAULT_TOTAL_PRICE_RETURN);
        return returnTransaction;
    }

    @Before
    public void initTest() {
        returnTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createReturnTransaction() throws Exception {
        int databaseSizeBeforeCreate = returnTransactionRepository.findAll().size();

        // Create the ReturnTransaction
        ReturnTransactionDTO returnTransactionDTO = returnTransactionMapper.toDto(returnTransaction);
        restReturnTransactionMockMvc.perform(post("/api/return-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        ReturnTransaction testReturnTransaction = returnTransactionList.get(returnTransactionList.size() - 1);
        assertThat(testReturnTransaction.getCreated_date()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testReturnTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testReturnTransaction.getTotalPriceReturn()).isEqualTo(DEFAULT_TOTAL_PRICE_RETURN);

        // Validate the ReturnTransaction in Elasticsearch
        verify(mockReturnTransactionSearchRepository, times(1)).save(testReturnTransaction);
    }

    @Test
    @Transactional
    public void createReturnTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = returnTransactionRepository.findAll().size();

        // Create the ReturnTransaction with an existing ID
        returnTransaction.setId(1L);
        ReturnTransactionDTO returnTransactionDTO = returnTransactionMapper.toDto(returnTransaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReturnTransactionMockMvc.perform(post("/api/return-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReturnTransaction in Elasticsearch
        verify(mockReturnTransactionSearchRepository, times(0)).save(returnTransaction);
    }

    @Test
    @Transactional
    public void checkTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnTransactionRepository.findAll().size();
        // set the field null
        returnTransaction.setTransactionType(null);

        // Create the ReturnTransaction, which fails.
        ReturnTransactionDTO returnTransactionDTO = returnTransactionMapper.toDto(returnTransaction);

        restReturnTransactionMockMvc.perform(post("/api/return-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalPriceReturnIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnTransactionRepository.findAll().size();
        // set the field null
        returnTransaction.setTotalPriceReturn(null);

        // Create the ReturnTransaction, which fails.
        ReturnTransactionDTO returnTransactionDTO = returnTransactionMapper.toDto(returnTransaction);

        restReturnTransactionMockMvc.perform(post("/api/return-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReturnTransactions() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList
        restReturnTransactionMockMvc.perform(get("/api/return-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].created_date").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].totalPriceReturn").value(hasItem(DEFAULT_TOTAL_PRICE_RETURN.intValue())));
    }
    
    @Test
    @Transactional
    public void getReturnTransaction() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get the returnTransaction
        restReturnTransactionMockMvc.perform(get("/api/return-transactions/{id}", returnTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(returnTransaction.getId().intValue()))
            .andExpect(jsonPath("$.created_date").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.totalPriceReturn").value(DEFAULT_TOTAL_PRICE_RETURN.intValue()));
    }

    @Test
    @Transactional
    public void getAllReturnTransactionsByCreated_dateIsEqualToSomething() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList where created_date equals to DEFAULT_CREATED_DATE
        defaultReturnTransactionShouldBeFound("created_date.equals=" + DEFAULT_CREATED_DATE);

        // Get all the returnTransactionList where created_date equals to UPDATED_CREATED_DATE
        defaultReturnTransactionShouldNotBeFound("created_date.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReturnTransactionsByCreated_dateIsInShouldWork() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList where created_date in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultReturnTransactionShouldBeFound("created_date.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the returnTransactionList where created_date equals to UPDATED_CREATED_DATE
        defaultReturnTransactionShouldNotBeFound("created_date.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReturnTransactionsByCreated_dateIsNullOrNotNull() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList where created_date is not null
        defaultReturnTransactionShouldBeFound("created_date.specified=true");

        // Get all the returnTransactionList where created_date is null
        defaultReturnTransactionShouldNotBeFound("created_date.specified=false");
    }

    @Test
    @Transactional
    public void getAllReturnTransactionsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList where transactionType equals to DEFAULT_TRANSACTION_TYPE
        defaultReturnTransactionShouldBeFound("transactionType.equals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the returnTransactionList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultReturnTransactionShouldNotBeFound("transactionType.equals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllReturnTransactionsByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList where transactionType in DEFAULT_TRANSACTION_TYPE or UPDATED_TRANSACTION_TYPE
        defaultReturnTransactionShouldBeFound("transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE);

        // Get all the returnTransactionList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultReturnTransactionShouldNotBeFound("transactionType.in=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllReturnTransactionsByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList where transactionType is not null
        defaultReturnTransactionShouldBeFound("transactionType.specified=true");

        // Get all the returnTransactionList where transactionType is null
        defaultReturnTransactionShouldNotBeFound("transactionType.specified=false");
    }

    @Test
    @Transactional
    public void getAllReturnTransactionsByTotalPriceReturnIsEqualToSomething() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList where totalPriceReturn equals to DEFAULT_TOTAL_PRICE_RETURN
        defaultReturnTransactionShouldBeFound("totalPriceReturn.equals=" + DEFAULT_TOTAL_PRICE_RETURN);

        // Get all the returnTransactionList where totalPriceReturn equals to UPDATED_TOTAL_PRICE_RETURN
        defaultReturnTransactionShouldNotBeFound("totalPriceReturn.equals=" + UPDATED_TOTAL_PRICE_RETURN);
    }

    @Test
    @Transactional
    public void getAllReturnTransactionsByTotalPriceReturnIsInShouldWork() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList where totalPriceReturn in DEFAULT_TOTAL_PRICE_RETURN or UPDATED_TOTAL_PRICE_RETURN
        defaultReturnTransactionShouldBeFound("totalPriceReturn.in=" + DEFAULT_TOTAL_PRICE_RETURN + "," + UPDATED_TOTAL_PRICE_RETURN);

        // Get all the returnTransactionList where totalPriceReturn equals to UPDATED_TOTAL_PRICE_RETURN
        defaultReturnTransactionShouldNotBeFound("totalPriceReturn.in=" + UPDATED_TOTAL_PRICE_RETURN);
    }

    @Test
    @Transactional
    public void getAllReturnTransactionsByTotalPriceReturnIsNullOrNotNull() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList where totalPriceReturn is not null
        defaultReturnTransactionShouldBeFound("totalPriceReturn.specified=true");

        // Get all the returnTransactionList where totalPriceReturn is null
        defaultReturnTransactionShouldNotBeFound("totalPriceReturn.specified=false");
    }

    @Test
    @Transactional
    public void getAllReturnTransactionsByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User creator = UserResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        returnTransaction.setCreator(creator);
        returnTransactionRepository.saveAndFlush(returnTransaction);
        Long creatorId = creator.getId();

        // Get all the returnTransactionList where creator equals to creatorId
        defaultReturnTransactionShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the returnTransactionList where creator equals to creatorId + 1
        defaultReturnTransactionShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }


    @Test
    @Transactional
    public void getAllReturnTransactionsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        returnTransaction.setCustomer(customer);
        returnTransactionRepository.saveAndFlush(returnTransaction);
        Long customerId = customer.getId();

        // Get all the returnTransactionList where customer equals to customerId
        defaultReturnTransactionShouldBeFound("customerId.equals=" + customerId);

        // Get all the returnTransactionList where customer equals to customerId + 1
        defaultReturnTransactionShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }


    @Test
    @Transactional
    public void getAllReturnTransactionsBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        Supplier supplier = SupplierResourceIntTest.createEntity(em);
        em.persist(supplier);
        em.flush();
        returnTransaction.setSupplier(supplier);
        returnTransactionRepository.saveAndFlush(returnTransaction);
        Long supplierId = supplier.getId();

        // Get all the returnTransactionList where supplier equals to supplierId
        defaultReturnTransactionShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the returnTransactionList where supplier equals to supplierId + 1
        defaultReturnTransactionShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }


    @Test
    @Transactional
    public void getAllReturnTransactionsByReturnItemIsEqualToSomething() throws Exception {
        // Initialize the database
        ReturnItem returnItem = ReturnItemResourceIntTest.createEntity(em);
        em.persist(returnItem);
        em.flush();
        returnTransaction.addReturnItem(returnItem);
        returnTransactionRepository.saveAndFlush(returnTransaction);
        Long returnItemId = returnItem.getId();

        // Get all the returnTransactionList where returnItem equals to returnItemId
        defaultReturnTransactionShouldBeFound("returnItemId.equals=" + returnItemId);

        // Get all the returnTransactionList where returnItem equals to returnItemId + 1
        defaultReturnTransactionShouldNotBeFound("returnItemId.equals=" + (returnItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReturnTransactionShouldBeFound(String filter) throws Exception {
        restReturnTransactionMockMvc.perform(get("/api/return-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].created_date").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].totalPriceReturn").value(hasItem(DEFAULT_TOTAL_PRICE_RETURN.intValue())));

        // Check, that the count call also returns 1
        restReturnTransactionMockMvc.perform(get("/api/return-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReturnTransactionShouldNotBeFound(String filter) throws Exception {
        restReturnTransactionMockMvc.perform(get("/api/return-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReturnTransactionMockMvc.perform(get("/api/return-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingReturnTransaction() throws Exception {
        // Get the returnTransaction
        restReturnTransactionMockMvc.perform(get("/api/return-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReturnTransaction() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();

        // Update the returnTransaction
        ReturnTransaction updatedReturnTransaction = returnTransactionRepository.findById(returnTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedReturnTransaction are not directly saved in db
        em.detach(updatedReturnTransaction);
        updatedReturnTransaction
            .created_date(UPDATED_CREATED_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .totalPriceReturn(UPDATED_TOTAL_PRICE_RETURN);
        ReturnTransactionDTO returnTransactionDTO = returnTransactionMapper.toDto(updatedReturnTransaction);

        restReturnTransactionMockMvc.perform(put("/api/return-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnTransactionDTO)))
            .andExpect(status().isOk());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);
        ReturnTransaction testReturnTransaction = returnTransactionList.get(returnTransactionList.size() - 1);
        assertThat(testReturnTransaction.getCreated_date()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testReturnTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testReturnTransaction.getTotalPriceReturn()).isEqualTo(UPDATED_TOTAL_PRICE_RETURN);

        // Validate the ReturnTransaction in Elasticsearch
        verify(mockReturnTransactionSearchRepository, times(1)).save(testReturnTransaction);
    }

    @Test
    @Transactional
    public void updateNonExistingReturnTransaction() throws Exception {
        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();

        // Create the ReturnTransaction
        ReturnTransactionDTO returnTransactionDTO = returnTransactionMapper.toDto(returnTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnTransactionMockMvc.perform(put("/api/return-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReturnTransaction in Elasticsearch
        verify(mockReturnTransactionSearchRepository, times(0)).save(returnTransaction);
    }

    @Test
    @Transactional
    public void deleteReturnTransaction() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        int databaseSizeBeforeDelete = returnTransactionRepository.findAll().size();

        // Delete the returnTransaction
        restReturnTransactionMockMvc.perform(delete("/api/return-transactions/{id}", returnTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReturnTransaction in Elasticsearch
        verify(mockReturnTransactionSearchRepository, times(1)).deleteById(returnTransaction.getId());
    }

    @Test
    @Transactional
    public void searchReturnTransaction() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);
        when(mockReturnTransactionSearchRepository.search(queryStringQuery("id:" + returnTransaction.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(returnTransaction), PageRequest.of(0, 1), 1));
        // Search the returnTransaction
        restReturnTransactionMockMvc.perform(get("/api/_search/return-transactions?query=id:" + returnTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].created_date").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].totalPriceReturn").value(hasItem(DEFAULT_TOTAL_PRICE_RETURN.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnTransaction.class);
        ReturnTransaction returnTransaction1 = new ReturnTransaction();
        returnTransaction1.setId(1L);
        ReturnTransaction returnTransaction2 = new ReturnTransaction();
        returnTransaction2.setId(returnTransaction1.getId());
        assertThat(returnTransaction1).isEqualTo(returnTransaction2);
        returnTransaction2.setId(2L);
        assertThat(returnTransaction1).isNotEqualTo(returnTransaction2);
        returnTransaction1.setId(null);
        assertThat(returnTransaction1).isNotEqualTo(returnTransaction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnTransactionDTO.class);
        ReturnTransactionDTO returnTransactionDTO1 = new ReturnTransactionDTO();
        returnTransactionDTO1.setId(1L);
        ReturnTransactionDTO returnTransactionDTO2 = new ReturnTransactionDTO();
        assertThat(returnTransactionDTO1).isNotEqualTo(returnTransactionDTO2);
        returnTransactionDTO2.setId(returnTransactionDTO1.getId());
        assertThat(returnTransactionDTO1).isEqualTo(returnTransactionDTO2);
        returnTransactionDTO2.setId(2L);
        assertThat(returnTransactionDTO1).isNotEqualTo(returnTransactionDTO2);
        returnTransactionDTO1.setId(null);
        assertThat(returnTransactionDTO1).isNotEqualTo(returnTransactionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(returnTransactionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(returnTransactionMapper.fromId(null)).isNull();
    }
}
