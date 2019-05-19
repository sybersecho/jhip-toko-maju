package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.Purchase;
import com.toko.maju.domain.Supplier;
import com.toko.maju.domain.User;
import com.toko.maju.domain.PurchaseList;
import com.toko.maju.repository.PurchaseRepository;
import com.toko.maju.repository.search.PurchaseSearchRepository;
import com.toko.maju.service.PurchaseService;
import com.toko.maju.service.dto.PurchaseDTO;
import com.toko.maju.service.mapper.PurchaseMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.PurchaseCriteria;
import com.toko.maju.service.PurchaseQueryService;

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
 * Test class for the PurchaseResource REST controller.
 *
 * @see PurchaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class PurchaseResourceIntTest {

    private static final BigDecimal DEFAULT_TOTAL_PAYMENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PAYMENT = new BigDecimal(1);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseService purchaseService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.PurchaseSearchRepositoryMockConfiguration
     */
    @Autowired
    private PurchaseSearchRepository mockPurchaseSearchRepository;

    @Autowired
    private PurchaseQueryService purchaseQueryService;

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

    private MockMvc restPurchaseMockMvc;

    private Purchase purchase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseResource purchaseResource = new PurchaseResource(purchaseService, purchaseQueryService);
        this.restPurchaseMockMvc = MockMvcBuilders.standaloneSetup(purchaseResource)
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
    public static Purchase createEntity(EntityManager em) {
        Purchase purchase = new Purchase()
            .totalPayment(DEFAULT_TOTAL_PAYMENT)
            .createdDate(DEFAULT_CREATED_DATE)
            .note(DEFAULT_NOTE);
        // Add required entity
        Supplier supplier = SupplierResourceIntTest.createEntity(em);
        em.persist(supplier);
        em.flush();
        purchase.setSupplier(supplier);
        return purchase;
    }

    @Before
    public void initTest() {
        purchase = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchase() throws Exception {
        int databaseSizeBeforeCreate = purchaseRepository.findAll().size();

        // Create the Purchase
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(purchase);
        restPurchaseMockMvc.perform(post("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isCreated());

        // Validate the Purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeCreate + 1);
        Purchase testPurchase = purchaseList.get(purchaseList.size() - 1);
        assertThat(testPurchase.getTotalPayment()).isEqualTo(DEFAULT_TOTAL_PAYMENT);
        assertThat(testPurchase.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchase.getNote()).isEqualTo(DEFAULT_NOTE);

        // Validate the Purchase in Elasticsearch
        verify(mockPurchaseSearchRepository, times(1)).save(testPurchase);
    }

    @Test
    @Transactional
    public void createPurchaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseRepository.findAll().size();

        // Create the Purchase with an existing ID
        purchase.setId(1L);
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(purchase);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseMockMvc.perform(post("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Purchase in Elasticsearch
        verify(mockPurchaseSearchRepository, times(0)).save(purchase);
    }

    @Test
    @Transactional
    public void checkTotalPaymentIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseRepository.findAll().size();
        // set the field null
        purchase.setTotalPayment(null);

        // Create the Purchase, which fails.
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(purchase);

        restPurchaseMockMvc.perform(post("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isBadRequest());

        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseRepository.findAll().size();
        // set the field null
        purchase.setCreatedDate(null);

        // Create the Purchase, which fails.
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(purchase);

        restPurchaseMockMvc.perform(post("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isBadRequest());

        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchases() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList
        restPurchaseMockMvc.perform(get("/api/purchases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPayment").value(hasItem(DEFAULT_TOTAL_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }
    
    @Test
    @Transactional
    public void getPurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", purchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchase.getId().intValue()))
            .andExpect(jsonPath("$.totalPayment").value(DEFAULT_TOTAL_PAYMENT.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getAllPurchasesByTotalPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where totalPayment equals to DEFAULT_TOTAL_PAYMENT
        defaultPurchaseShouldBeFound("totalPayment.equals=" + DEFAULT_TOTAL_PAYMENT);

        // Get all the purchaseList where totalPayment equals to UPDATED_TOTAL_PAYMENT
        defaultPurchaseShouldNotBeFound("totalPayment.equals=" + UPDATED_TOTAL_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllPurchasesByTotalPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where totalPayment in DEFAULT_TOTAL_PAYMENT or UPDATED_TOTAL_PAYMENT
        defaultPurchaseShouldBeFound("totalPayment.in=" + DEFAULT_TOTAL_PAYMENT + "," + UPDATED_TOTAL_PAYMENT);

        // Get all the purchaseList where totalPayment equals to UPDATED_TOTAL_PAYMENT
        defaultPurchaseShouldNotBeFound("totalPayment.in=" + UPDATED_TOTAL_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllPurchasesByTotalPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where totalPayment is not null
        defaultPurchaseShouldBeFound("totalPayment.specified=true");

        // Get all the purchaseList where totalPayment is null
        defaultPurchaseShouldNotBeFound("totalPayment.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchasesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where createdDate equals to DEFAULT_CREATED_DATE
        defaultPurchaseShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the purchaseList where createdDate equals to UPDATED_CREATED_DATE
        defaultPurchaseShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchasesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultPurchaseShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the purchaseList where createdDate equals to UPDATED_CREATED_DATE
        defaultPurchaseShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchasesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where createdDate is not null
        defaultPurchaseShouldBeFound("createdDate.specified=true");

        // Get all the purchaseList where createdDate is null
        defaultPurchaseShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchasesByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where note equals to DEFAULT_NOTE
        defaultPurchaseShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the purchaseList where note equals to UPDATED_NOTE
        defaultPurchaseShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPurchasesByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultPurchaseShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the purchaseList where note equals to UPDATED_NOTE
        defaultPurchaseShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPurchasesByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where note is not null
        defaultPurchaseShouldBeFound("note.specified=true");

        // Get all the purchaseList where note is null
        defaultPurchaseShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchasesBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        Supplier supplier = SupplierResourceIntTest.createEntity(em);
        em.persist(supplier);
        em.flush();
        purchase.setSupplier(supplier);
        purchaseRepository.saveAndFlush(purchase);
        Long supplierId = supplier.getId();

        // Get all the purchaseList where supplier equals to supplierId
        defaultPurchaseShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the purchaseList where supplier equals to supplierId + 1
        defaultPurchaseShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }


    @Test
    @Transactional
    public void getAllPurchasesByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User creator = UserResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        purchase.setCreator(creator);
        purchaseRepository.saveAndFlush(purchase);
        Long creatorId = creator.getId();

        // Get all the purchaseList where creator equals to creatorId
        defaultPurchaseShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the purchaseList where creator equals to creatorId + 1
        defaultPurchaseShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }


    @Test
    @Transactional
    public void getAllPurchasesByPurchaseListIsEqualToSomething() throws Exception {
        // Initialize the database
        PurchaseList purchaseList = PurchaseListResourceIntTest.createEntity(em);
        em.persist(purchaseList);
        em.flush();
        purchase.addPurchaseList(purchaseList);
        purchaseRepository.saveAndFlush(purchase);
        Long purchaseListId = purchaseList.getId();

        // Get all the purchaseList where purchaseList equals to purchaseListId
        defaultPurchaseShouldBeFound("purchaseListId.equals=" + purchaseListId);

        // Get all the purchaseList where purchaseList equals to purchaseListId + 1
        defaultPurchaseShouldNotBeFound("purchaseListId.equals=" + (purchaseListId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPurchaseShouldBeFound(String filter) throws Exception {
        restPurchaseMockMvc.perform(get("/api/purchases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPayment").value(hasItem(DEFAULT_TOTAL_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restPurchaseMockMvc.perform(get("/api/purchases/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPurchaseShouldNotBeFound(String filter) throws Exception {
        restPurchaseMockMvc.perform(get("/api/purchases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseMockMvc.perform(get("/api/purchases/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPurchase() throws Exception {
        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        int databaseSizeBeforeUpdate = purchaseRepository.findAll().size();

        // Update the purchase
        Purchase updatedPurchase = purchaseRepository.findById(purchase.getId()).get();
        // Disconnect from session so that the updates on updatedPurchase are not directly saved in db
        em.detach(updatedPurchase);
        updatedPurchase
            .totalPayment(UPDATED_TOTAL_PAYMENT)
            .createdDate(UPDATED_CREATED_DATE)
            .note(UPDATED_NOTE);
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(updatedPurchase);

        restPurchaseMockMvc.perform(put("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isOk());

        // Validate the Purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeUpdate);
        Purchase testPurchase = purchaseList.get(purchaseList.size() - 1);
        assertThat(testPurchase.getTotalPayment()).isEqualTo(UPDATED_TOTAL_PAYMENT);
        assertThat(testPurchase.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchase.getNote()).isEqualTo(UPDATED_NOTE);

        // Validate the Purchase in Elasticsearch
        verify(mockPurchaseSearchRepository, times(1)).save(testPurchase);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchase() throws Exception {
        int databaseSizeBeforeUpdate = purchaseRepository.findAll().size();

        // Create the Purchase
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(purchase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseMockMvc.perform(put("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Purchase in Elasticsearch
        verify(mockPurchaseSearchRepository, times(0)).save(purchase);
    }

    @Test
    @Transactional
    public void deletePurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        int databaseSizeBeforeDelete = purchaseRepository.findAll().size();

        // Delete the purchase
        restPurchaseMockMvc.perform(delete("/api/purchases/{id}", purchase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Purchase in Elasticsearch
        verify(mockPurchaseSearchRepository, times(1)).deleteById(purchase.getId());
    }

    @Test
    @Transactional
    public void searchPurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);
        when(mockPurchaseSearchRepository.search(queryStringQuery("id:" + purchase.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(purchase), PageRequest.of(0, 1), 1));
        // Search the purchase
        restPurchaseMockMvc.perform(get("/api/_search/purchases?query=id:" + purchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPayment").value(hasItem(DEFAULT_TOTAL_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Purchase.class);
        Purchase purchase1 = new Purchase();
        purchase1.setId(1L);
        Purchase purchase2 = new Purchase();
        purchase2.setId(purchase1.getId());
        assertThat(purchase1).isEqualTo(purchase2);
        purchase2.setId(2L);
        assertThat(purchase1).isNotEqualTo(purchase2);
        purchase1.setId(null);
        assertThat(purchase1).isNotEqualTo(purchase2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseDTO.class);
        PurchaseDTO purchaseDTO1 = new PurchaseDTO();
        purchaseDTO1.setId(1L);
        PurchaseDTO purchaseDTO2 = new PurchaseDTO();
        assertThat(purchaseDTO1).isNotEqualTo(purchaseDTO2);
        purchaseDTO2.setId(purchaseDTO1.getId());
        assertThat(purchaseDTO1).isEqualTo(purchaseDTO2);
        purchaseDTO2.setId(2L);
        assertThat(purchaseDTO1).isNotEqualTo(purchaseDTO2);
        purchaseDTO1.setId(null);
        assertThat(purchaseDTO1).isNotEqualTo(purchaseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(purchaseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(purchaseMapper.fromId(null)).isNull();
    }
}
