package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.CancelTransaction;
import com.toko.maju.domain.SaleTransactions;
import com.toko.maju.repository.CancelTransactionRepository;
import com.toko.maju.repository.search.CancelTransactionSearchRepository;
import com.toko.maju.service.CancelTransactionService;
import com.toko.maju.service.dto.CancelTransactionDTO;
import com.toko.maju.service.mapper.CancelTransactionMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.CancelTransactionCriteria;
import com.toko.maju.service.CancelTransactionQueryService;

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
 * Test class for the CancelTransactionResource REST controller.
 *
 * @see CancelTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class CancelTransactionResourceIntTest {

    private static final String DEFAULT_NO_INVOICE = "AAAAAAAAAA";
    private static final String UPDATED_NO_INVOICE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CANCEL_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CANCEL_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    @Autowired
    private CancelTransactionRepository cancelTransactionRepository;

    @Autowired
    private CancelTransactionMapper cancelTransactionMapper;

    @Autowired
    private CancelTransactionService cancelTransactionService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.CancelTransactionSearchRepositoryMockConfiguration
     */
    @Autowired
    private CancelTransactionSearchRepository mockCancelTransactionSearchRepository;

    @Autowired
    private CancelTransactionQueryService cancelTransactionQueryService;

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

    private MockMvc restCancelTransactionMockMvc;

    private CancelTransaction cancelTransaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CancelTransactionResource cancelTransactionResource = new CancelTransactionResource(cancelTransactionService, cancelTransactionQueryService);
        this.restCancelTransactionMockMvc = MockMvcBuilders.standaloneSetup(cancelTransactionResource)
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
    public static CancelTransaction createEntity(EntityManager em) {
        CancelTransaction cancelTransaction = new CancelTransaction()
            .noInvoice(DEFAULT_NO_INVOICE)
            .cancelDate(DEFAULT_CANCEL_DATE)
            .note(DEFAULT_NOTE);
        return cancelTransaction;
    }

    @Before
    public void initTest() {
        cancelTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createCancelTransaction() throws Exception {
        int databaseSizeBeforeCreate = cancelTransactionRepository.findAll().size();

        // Create the CancelTransaction
        CancelTransactionDTO cancelTransactionDTO = cancelTransactionMapper.toDto(cancelTransaction);
        restCancelTransactionMockMvc.perform(post("/api/cancel-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cancelTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the CancelTransaction in the database
        List<CancelTransaction> cancelTransactionList = cancelTransactionRepository.findAll();
        assertThat(cancelTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        CancelTransaction testCancelTransaction = cancelTransactionList.get(cancelTransactionList.size() - 1);
        assertThat(testCancelTransaction.getNoInvoice()).isEqualTo(DEFAULT_NO_INVOICE);
        assertThat(testCancelTransaction.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
        assertThat(testCancelTransaction.getNote()).isEqualTo(DEFAULT_NOTE);

        // Validate the CancelTransaction in Elasticsearch
        verify(mockCancelTransactionSearchRepository, times(1)).save(testCancelTransaction);
    }

    @Test
    @Transactional
    public void createCancelTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cancelTransactionRepository.findAll().size();

        // Create the CancelTransaction with an existing ID
        cancelTransaction.setId(1L);
        CancelTransactionDTO cancelTransactionDTO = cancelTransactionMapper.toDto(cancelTransaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCancelTransactionMockMvc.perform(post("/api/cancel-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cancelTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CancelTransaction in the database
        List<CancelTransaction> cancelTransactionList = cancelTransactionRepository.findAll();
        assertThat(cancelTransactionList).hasSize(databaseSizeBeforeCreate);

        // Validate the CancelTransaction in Elasticsearch
        verify(mockCancelTransactionSearchRepository, times(0)).save(cancelTransaction);
    }

    @Test
    @Transactional
    public void checkNoInvoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cancelTransactionRepository.findAll().size();
        // set the field null
        cancelTransaction.setNoInvoice(null);

        // Create the CancelTransaction, which fails.
        CancelTransactionDTO cancelTransactionDTO = cancelTransactionMapper.toDto(cancelTransaction);

        restCancelTransactionMockMvc.perform(post("/api/cancel-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cancelTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<CancelTransaction> cancelTransactionList = cancelTransactionRepository.findAll();
        assertThat(cancelTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCancelDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cancelTransactionRepository.findAll().size();
        // set the field null
        cancelTransaction.setCancelDate(null);

        // Create the CancelTransaction, which fails.
        CancelTransactionDTO cancelTransactionDTO = cancelTransactionMapper.toDto(cancelTransaction);

        restCancelTransactionMockMvc.perform(post("/api/cancel-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cancelTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<CancelTransaction> cancelTransactionList = cancelTransactionRepository.findAll();
        assertThat(cancelTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = cancelTransactionRepository.findAll().size();
        // set the field null
        cancelTransaction.setNote(null);

        // Create the CancelTransaction, which fails.
        CancelTransactionDTO cancelTransactionDTO = cancelTransactionMapper.toDto(cancelTransaction);

        restCancelTransactionMockMvc.perform(post("/api/cancel-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cancelTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<CancelTransaction> cancelTransactionList = cancelTransactionRepository.findAll();
        assertThat(cancelTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCancelTransactions() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get all the cancelTransactionList
        restCancelTransactionMockMvc.perform(get("/api/cancel-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cancelTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].noInvoice").value(hasItem(DEFAULT_NO_INVOICE.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }
    
    @Test
    @Transactional
    public void getCancelTransaction() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get the cancelTransaction
        restCancelTransactionMockMvc.perform(get("/api/cancel-transactions/{id}", cancelTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cancelTransaction.getId().intValue()))
            .andExpect(jsonPath("$.noInvoice").value(DEFAULT_NO_INVOICE.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getAllCancelTransactionsByNoInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get all the cancelTransactionList where noInvoice equals to DEFAULT_NO_INVOICE
        defaultCancelTransactionShouldBeFound("noInvoice.equals=" + DEFAULT_NO_INVOICE);

        // Get all the cancelTransactionList where noInvoice equals to UPDATED_NO_INVOICE
        defaultCancelTransactionShouldNotBeFound("noInvoice.equals=" + UPDATED_NO_INVOICE);
    }

    @Test
    @Transactional
    public void getAllCancelTransactionsByNoInvoiceIsInShouldWork() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get all the cancelTransactionList where noInvoice in DEFAULT_NO_INVOICE or UPDATED_NO_INVOICE
        defaultCancelTransactionShouldBeFound("noInvoice.in=" + DEFAULT_NO_INVOICE + "," + UPDATED_NO_INVOICE);

        // Get all the cancelTransactionList where noInvoice equals to UPDATED_NO_INVOICE
        defaultCancelTransactionShouldNotBeFound("noInvoice.in=" + UPDATED_NO_INVOICE);
    }

    @Test
    @Transactional
    public void getAllCancelTransactionsByNoInvoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get all the cancelTransactionList where noInvoice is not null
        defaultCancelTransactionShouldBeFound("noInvoice.specified=true");

        // Get all the cancelTransactionList where noInvoice is null
        defaultCancelTransactionShouldNotBeFound("noInvoice.specified=false");
    }

    @Test
    @Transactional
    public void getAllCancelTransactionsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get all the cancelTransactionList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultCancelTransactionShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the cancelTransactionList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultCancelTransactionShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    public void getAllCancelTransactionsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get all the cancelTransactionList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultCancelTransactionShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the cancelTransactionList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultCancelTransactionShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    public void getAllCancelTransactionsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get all the cancelTransactionList where cancelDate is not null
        defaultCancelTransactionShouldBeFound("cancelDate.specified=true");

        // Get all the cancelTransactionList where cancelDate is null
        defaultCancelTransactionShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCancelTransactionsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get all the cancelTransactionList where note equals to DEFAULT_NOTE
        defaultCancelTransactionShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the cancelTransactionList where note equals to UPDATED_NOTE
        defaultCancelTransactionShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllCancelTransactionsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get all the cancelTransactionList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultCancelTransactionShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the cancelTransactionList where note equals to UPDATED_NOTE
        defaultCancelTransactionShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllCancelTransactionsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        // Get all the cancelTransactionList where note is not null
        defaultCancelTransactionShouldBeFound("note.specified=true");

        // Get all the cancelTransactionList where note is null
        defaultCancelTransactionShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllCancelTransactionsBySaleTransactionsIsEqualToSomething() throws Exception {
        // Initialize the database
        SaleTransactions saleTransactions = SaleTransactionsResourceIntTest.createEntity(em);
        em.persist(saleTransactions);
        em.flush();
        cancelTransaction.setSaleTransactions(saleTransactions);
        cancelTransactionRepository.saveAndFlush(cancelTransaction);
        Long saleTransactionsId = saleTransactions.getId();

        // Get all the cancelTransactionList where saleTransactions equals to saleTransactionsId
        defaultCancelTransactionShouldBeFound("saleTransactionsId.equals=" + saleTransactionsId);

        // Get all the cancelTransactionList where saleTransactions equals to saleTransactionsId + 1
        defaultCancelTransactionShouldNotBeFound("saleTransactionsId.equals=" + (saleTransactionsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCancelTransactionShouldBeFound(String filter) throws Exception {
        restCancelTransactionMockMvc.perform(get("/api/cancel-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cancelTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].noInvoice").value(hasItem(DEFAULT_NO_INVOICE)))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restCancelTransactionMockMvc.perform(get("/api/cancel-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCancelTransactionShouldNotBeFound(String filter) throws Exception {
        restCancelTransactionMockMvc.perform(get("/api/cancel-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCancelTransactionMockMvc.perform(get("/api/cancel-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCancelTransaction() throws Exception {
        // Get the cancelTransaction
        restCancelTransactionMockMvc.perform(get("/api/cancel-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCancelTransaction() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        int databaseSizeBeforeUpdate = cancelTransactionRepository.findAll().size();

        // Update the cancelTransaction
        CancelTransaction updatedCancelTransaction = cancelTransactionRepository.findById(cancelTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedCancelTransaction are not directly saved in db
        em.detach(updatedCancelTransaction);
        updatedCancelTransaction
            .noInvoice(UPDATED_NO_INVOICE)
            .cancelDate(UPDATED_CANCEL_DATE)
            .note(UPDATED_NOTE);
        CancelTransactionDTO cancelTransactionDTO = cancelTransactionMapper.toDto(updatedCancelTransaction);

        restCancelTransactionMockMvc.perform(put("/api/cancel-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cancelTransactionDTO)))
            .andExpect(status().isOk());

        // Validate the CancelTransaction in the database
        List<CancelTransaction> cancelTransactionList = cancelTransactionRepository.findAll();
        assertThat(cancelTransactionList).hasSize(databaseSizeBeforeUpdate);
        CancelTransaction testCancelTransaction = cancelTransactionList.get(cancelTransactionList.size() - 1);
        assertThat(testCancelTransaction.getNoInvoice()).isEqualTo(UPDATED_NO_INVOICE);
        assertThat(testCancelTransaction.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testCancelTransaction.getNote()).isEqualTo(UPDATED_NOTE);

        // Validate the CancelTransaction in Elasticsearch
        verify(mockCancelTransactionSearchRepository, times(1)).save(testCancelTransaction);
    }

    @Test
    @Transactional
    public void updateNonExistingCancelTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cancelTransactionRepository.findAll().size();

        // Create the CancelTransaction
        CancelTransactionDTO cancelTransactionDTO = cancelTransactionMapper.toDto(cancelTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCancelTransactionMockMvc.perform(put("/api/cancel-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cancelTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CancelTransaction in the database
        List<CancelTransaction> cancelTransactionList = cancelTransactionRepository.findAll();
        assertThat(cancelTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CancelTransaction in Elasticsearch
        verify(mockCancelTransactionSearchRepository, times(0)).save(cancelTransaction);
    }

    @Test
    @Transactional
    public void deleteCancelTransaction() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);

        int databaseSizeBeforeDelete = cancelTransactionRepository.findAll().size();

        // Delete the cancelTransaction
        restCancelTransactionMockMvc.perform(delete("/api/cancel-transactions/{id}", cancelTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CancelTransaction> cancelTransactionList = cancelTransactionRepository.findAll();
        assertThat(cancelTransactionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CancelTransaction in Elasticsearch
        verify(mockCancelTransactionSearchRepository, times(1)).deleteById(cancelTransaction.getId());
    }

    @Test
    @Transactional
    public void searchCancelTransaction() throws Exception {
        // Initialize the database
        cancelTransactionRepository.saveAndFlush(cancelTransaction);
        when(mockCancelTransactionSearchRepository.search(queryStringQuery("id:" + cancelTransaction.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cancelTransaction), PageRequest.of(0, 1), 1));
        // Search the cancelTransaction
        restCancelTransactionMockMvc.perform(get("/api/_search/cancel-transactions?query=id:" + cancelTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cancelTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].noInvoice").value(hasItem(DEFAULT_NO_INVOICE)))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CancelTransaction.class);
        CancelTransaction cancelTransaction1 = new CancelTransaction();
        cancelTransaction1.setId(1L);
        CancelTransaction cancelTransaction2 = new CancelTransaction();
        cancelTransaction2.setId(cancelTransaction1.getId());
        assertThat(cancelTransaction1).isEqualTo(cancelTransaction2);
        cancelTransaction2.setId(2L);
        assertThat(cancelTransaction1).isNotEqualTo(cancelTransaction2);
        cancelTransaction1.setId(null);
        assertThat(cancelTransaction1).isNotEqualTo(cancelTransaction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CancelTransactionDTO.class);
        CancelTransactionDTO cancelTransactionDTO1 = new CancelTransactionDTO();
        cancelTransactionDTO1.setId(1L);
        CancelTransactionDTO cancelTransactionDTO2 = new CancelTransactionDTO();
        assertThat(cancelTransactionDTO1).isNotEqualTo(cancelTransactionDTO2);
        cancelTransactionDTO2.setId(cancelTransactionDTO1.getId());
        assertThat(cancelTransactionDTO1).isEqualTo(cancelTransactionDTO2);
        cancelTransactionDTO2.setId(2L);
        assertThat(cancelTransactionDTO1).isNotEqualTo(cancelTransactionDTO2);
        cancelTransactionDTO1.setId(null);
        assertThat(cancelTransactionDTO1).isNotEqualTo(cancelTransactionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cancelTransactionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cancelTransactionMapper.fromId(null)).isNull();
    }
}
