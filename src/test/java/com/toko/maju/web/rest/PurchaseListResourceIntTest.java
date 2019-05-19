package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.PurchaseList;
import com.toko.maju.domain.Purchase;
import com.toko.maju.repository.PurchaseListRepository;
import com.toko.maju.repository.search.PurchaseListSearchRepository;
import com.toko.maju.service.PurchaseListService;
import com.toko.maju.service.dto.PurchaseListDTO;
import com.toko.maju.service.mapper.PurchaseListMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.PurchaseListCriteria;
import com.toko.maju.service.PurchaseListQueryService;

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
 * Test class for the PurchaseListResource REST controller.
 *
 * @see PurchaseListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class PurchaseListResourceIntTest {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);

    @Autowired
    private PurchaseListRepository purchaseListRepository;

    @Autowired
    private PurchaseListMapper purchaseListMapper;

    @Autowired
    private PurchaseListService purchaseListService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.PurchaseListSearchRepositoryMockConfiguration
     */
    @Autowired
    private PurchaseListSearchRepository mockPurchaseListSearchRepository;

    @Autowired
    private PurchaseListQueryService purchaseListQueryService;

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

    private MockMvc restPurchaseListMockMvc;

    private PurchaseList purchaseList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseListResource purchaseListResource = new PurchaseListResource(purchaseListService, purchaseListQueryService);
        this.restPurchaseListMockMvc = MockMvcBuilders.standaloneSetup(purchaseListResource)
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
    public static PurchaseList createEntity(EntityManager em) {
        PurchaseList purchaseList = new PurchaseList()
            .barcode(DEFAULT_BARCODE)
            .name(DEFAULT_NAME)
            .unit(DEFAULT_UNIT)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .total(DEFAULT_TOTAL);
        // Add required entity
        Purchase purchase = PurchaseResourceIntTest.createEntity(em);
        em.persist(purchase);
        em.flush();
        purchaseList.setPurchase(purchase);
        return purchaseList;
    }

    @Before
    public void initTest() {
        purchaseList = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseList() throws Exception {
        int databaseSizeBeforeCreate = purchaseListRepository.findAll().size();

        // Create the PurchaseList
        PurchaseListDTO purchaseListDTO = purchaseListMapper.toDto(purchaseList);
        restPurchaseListMockMvc.perform(post("/api/purchase-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseListDTO)))
            .andExpect(status().isCreated());

        // Validate the PurchaseList in the database
        List<PurchaseList> purchaseListList = purchaseListRepository.findAll();
        assertThat(purchaseListList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseList testPurchaseList = purchaseListList.get(purchaseListList.size() - 1);
        assertThat(testPurchaseList.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testPurchaseList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPurchaseList.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testPurchaseList.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testPurchaseList.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testPurchaseList.getTotal()).isEqualTo(DEFAULT_TOTAL);

        // Validate the PurchaseList in Elasticsearch
        verify(mockPurchaseListSearchRepository, times(1)).save(testPurchaseList);
    }

    @Test
    @Transactional
    public void createPurchaseListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseListRepository.findAll().size();

        // Create the PurchaseList with an existing ID
        purchaseList.setId(1L);
        PurchaseListDTO purchaseListDTO = purchaseListMapper.toDto(purchaseList);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseListMockMvc.perform(post("/api/purchase-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseList in the database
        List<PurchaseList> purchaseListList = purchaseListRepository.findAll();
        assertThat(purchaseListList).hasSize(databaseSizeBeforeCreate);

        // Validate the PurchaseList in Elasticsearch
        verify(mockPurchaseListSearchRepository, times(0)).save(purchaseList);
    }

    @Test
    @Transactional
    public void getAllPurchaseLists() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList
        restPurchaseListMockMvc.perform(get("/api/purchase-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseList.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }
    
    @Test
    @Transactional
    public void getPurchaseList() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get the purchaseList
        restPurchaseListMockMvc.perform(get("/api/purchase-lists/{id}", purchaseList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseList.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()));
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where barcode equals to DEFAULT_BARCODE
        defaultPurchaseListShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the purchaseListList where barcode equals to UPDATED_BARCODE
        defaultPurchaseListShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultPurchaseListShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the purchaseListList where barcode equals to UPDATED_BARCODE
        defaultPurchaseListShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where barcode is not null
        defaultPurchaseListShouldBeFound("barcode.specified=true");

        // Get all the purchaseListList where barcode is null
        defaultPurchaseListShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where name equals to DEFAULT_NAME
        defaultPurchaseListShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the purchaseListList where name equals to UPDATED_NAME
        defaultPurchaseListShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPurchaseListShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the purchaseListList where name equals to UPDATED_NAME
        defaultPurchaseListShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where name is not null
        defaultPurchaseListShouldBeFound("name.specified=true");

        // Get all the purchaseListList where name is null
        defaultPurchaseListShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where unit equals to DEFAULT_UNIT
        defaultPurchaseListShouldBeFound("unit.equals=" + DEFAULT_UNIT);

        // Get all the purchaseListList where unit equals to UPDATED_UNIT
        defaultPurchaseListShouldNotBeFound("unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where unit in DEFAULT_UNIT or UPDATED_UNIT
        defaultPurchaseListShouldBeFound("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT);

        // Get all the purchaseListList where unit equals to UPDATED_UNIT
        defaultPurchaseListShouldNotBeFound("unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where unit is not null
        defaultPurchaseListShouldBeFound("unit.specified=true");

        // Get all the purchaseListList where unit is null
        defaultPurchaseListShouldNotBeFound("unit.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultPurchaseListShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the purchaseListList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultPurchaseListShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultPurchaseListShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the purchaseListList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultPurchaseListShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where unitPrice is not null
        defaultPurchaseListShouldBeFound("unitPrice.specified=true");

        // Get all the purchaseListList where unitPrice is null
        defaultPurchaseListShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where quantity equals to DEFAULT_QUANTITY
        defaultPurchaseListShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the purchaseListList where quantity equals to UPDATED_QUANTITY
        defaultPurchaseListShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultPurchaseListShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the purchaseListList where quantity equals to UPDATED_QUANTITY
        defaultPurchaseListShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where quantity is not null
        defaultPurchaseListShouldBeFound("quantity.specified=true");

        // Get all the purchaseListList where quantity is null
        defaultPurchaseListShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultPurchaseListShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the purchaseListList where quantity greater than or equals to UPDATED_QUANTITY
        defaultPurchaseListShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where quantity less than or equals to DEFAULT_QUANTITY
        defaultPurchaseListShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the purchaseListList where quantity less than or equals to UPDATED_QUANTITY
        defaultPurchaseListShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllPurchaseListsByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where total equals to DEFAULT_TOTAL
        defaultPurchaseListShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the purchaseListList where total equals to UPDATED_TOTAL
        defaultPurchaseListShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultPurchaseListShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the purchaseListList where total equals to UPDATED_TOTAL
        defaultPurchaseListShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        // Get all the purchaseListList where total is not null
        defaultPurchaseListShouldBeFound("total.specified=true");

        // Get all the purchaseListList where total is null
        defaultPurchaseListShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseListsByPurchaseIsEqualToSomething() throws Exception {
        // Initialize the database
        Purchase purchase = PurchaseResourceIntTest.createEntity(em);
        em.persist(purchase);
        em.flush();
        purchaseList.setPurchase(purchase);
        purchaseListRepository.saveAndFlush(purchaseList);
        Long purchaseId = purchase.getId();

        // Get all the purchaseListList where purchase equals to purchaseId
        defaultPurchaseListShouldBeFound("purchaseId.equals=" + purchaseId);

        // Get all the purchaseListList where purchase equals to purchaseId + 1
        defaultPurchaseListShouldNotBeFound("purchaseId.equals=" + (purchaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPurchaseListShouldBeFound(String filter) throws Exception {
        restPurchaseListMockMvc.perform(get("/api/purchase-lists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseList.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));

        // Check, that the count call also returns 1
        restPurchaseListMockMvc.perform(get("/api/purchase-lists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPurchaseListShouldNotBeFound(String filter) throws Exception {
        restPurchaseListMockMvc.perform(get("/api/purchase-lists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseListMockMvc.perform(get("/api/purchase-lists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPurchaseList() throws Exception {
        // Get the purchaseList
        restPurchaseListMockMvc.perform(get("/api/purchase-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseList() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        int databaseSizeBeforeUpdate = purchaseListRepository.findAll().size();

        // Update the purchaseList
        PurchaseList updatedPurchaseList = purchaseListRepository.findById(purchaseList.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseList are not directly saved in db
        em.detach(updatedPurchaseList);
        updatedPurchaseList
            .barcode(UPDATED_BARCODE)
            .name(UPDATED_NAME)
            .unit(UPDATED_UNIT)
            .unitPrice(UPDATED_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .total(UPDATED_TOTAL);
        PurchaseListDTO purchaseListDTO = purchaseListMapper.toDto(updatedPurchaseList);

        restPurchaseListMockMvc.perform(put("/api/purchase-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseListDTO)))
            .andExpect(status().isOk());

        // Validate the PurchaseList in the database
        List<PurchaseList> purchaseListList = purchaseListRepository.findAll();
        assertThat(purchaseListList).hasSize(databaseSizeBeforeUpdate);
        PurchaseList testPurchaseList = purchaseListList.get(purchaseListList.size() - 1);
        assertThat(testPurchaseList.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testPurchaseList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPurchaseList.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testPurchaseList.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testPurchaseList.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPurchaseList.getTotal()).isEqualTo(UPDATED_TOTAL);

        // Validate the PurchaseList in Elasticsearch
        verify(mockPurchaseListSearchRepository, times(1)).save(testPurchaseList);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseList() throws Exception {
        int databaseSizeBeforeUpdate = purchaseListRepository.findAll().size();

        // Create the PurchaseList
        PurchaseListDTO purchaseListDTO = purchaseListMapper.toDto(purchaseList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseListMockMvc.perform(put("/api/purchase-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseList in the database
        List<PurchaseList> purchaseListList = purchaseListRepository.findAll();
        assertThat(purchaseListList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PurchaseList in Elasticsearch
        verify(mockPurchaseListSearchRepository, times(0)).save(purchaseList);
    }

    @Test
    @Transactional
    public void deletePurchaseList() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);

        int databaseSizeBeforeDelete = purchaseListRepository.findAll().size();

        // Delete the purchaseList
        restPurchaseListMockMvc.perform(delete("/api/purchase-lists/{id}", purchaseList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PurchaseList> purchaseListList = purchaseListRepository.findAll();
        assertThat(purchaseListList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PurchaseList in Elasticsearch
        verify(mockPurchaseListSearchRepository, times(1)).deleteById(purchaseList.getId());
    }

    @Test
    @Transactional
    public void searchPurchaseList() throws Exception {
        // Initialize the database
        purchaseListRepository.saveAndFlush(purchaseList);
        when(mockPurchaseListSearchRepository.search(queryStringQuery("id:" + purchaseList.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(purchaseList), PageRequest.of(0, 1), 1));
        // Search the purchaseList
        restPurchaseListMockMvc.perform(get("/api/_search/purchase-lists?query=id:" + purchaseList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseList.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseList.class);
        PurchaseList purchaseList1 = new PurchaseList();
        purchaseList1.setId(1L);
        PurchaseList purchaseList2 = new PurchaseList();
        purchaseList2.setId(purchaseList1.getId());
        assertThat(purchaseList1).isEqualTo(purchaseList2);
        purchaseList2.setId(2L);
        assertThat(purchaseList1).isNotEqualTo(purchaseList2);
        purchaseList1.setId(null);
        assertThat(purchaseList1).isNotEqualTo(purchaseList2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseListDTO.class);
        PurchaseListDTO purchaseListDTO1 = new PurchaseListDTO();
        purchaseListDTO1.setId(1L);
        PurchaseListDTO purchaseListDTO2 = new PurchaseListDTO();
        assertThat(purchaseListDTO1).isNotEqualTo(purchaseListDTO2);
        purchaseListDTO2.setId(purchaseListDTO1.getId());
        assertThat(purchaseListDTO1).isEqualTo(purchaseListDTO2);
        purchaseListDTO2.setId(2L);
        assertThat(purchaseListDTO1).isNotEqualTo(purchaseListDTO2);
        purchaseListDTO1.setId(null);
        assertThat(purchaseListDTO1).isNotEqualTo(purchaseListDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(purchaseListMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(purchaseListMapper.fromId(null)).isNull();
    }
}
