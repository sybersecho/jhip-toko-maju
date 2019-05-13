package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.ReturnItem;
import com.toko.maju.domain.Product;
import com.toko.maju.domain.ReturnTransaction;
import com.toko.maju.repository.ReturnItemRepository;
import com.toko.maju.repository.search.ReturnItemSearchRepository;
import com.toko.maju.service.ReturnItemService;
import com.toko.maju.service.dto.ReturnItemDTO;
import com.toko.maju.service.mapper.ReturnItemMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.ReturnItemCriteria;
import com.toko.maju.service.ReturnItemQueryService;

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

import com.toko.maju.domain.enumeration.ProductStatus;
/**
 * Test class for the ReturnItemResource REST controller.
 *
 * @see ReturnItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class ReturnItemResourceIntTest {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(1);

    private static final ProductStatus DEFAULT_PRODUCT_STATUS = ProductStatus.GOOD;
    private static final ProductStatus UPDATED_PRODUCT_STATUS = ProductStatus.BAD;

    @Autowired
    private ReturnItemRepository returnItemRepository;

    @Autowired
    private ReturnItemMapper returnItemMapper;

    @Autowired
    private ReturnItemService returnItemService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.ReturnItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReturnItemSearchRepository mockReturnItemSearchRepository;

    @Autowired
    private ReturnItemQueryService returnItemQueryService;

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

    private MockMvc restReturnItemMockMvc;

    private ReturnItem returnItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReturnItemResource returnItemResource = new ReturnItemResource(returnItemService, returnItemQueryService);
        this.restReturnItemMockMvc = MockMvcBuilders.standaloneSetup(returnItemResource)
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
    public static ReturnItem createEntity(EntityManager em) {
        ReturnItem returnItem = new ReturnItem()
            .barcode(DEFAULT_BARCODE)
            .productName(DEFAULT_PRODUCT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .productStatus(DEFAULT_PRODUCT_STATUS);
        // Add required entity
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        returnItem.setProduct(product);
        // Add required entity
        ReturnTransaction returnTransaction = ReturnTransactionResourceIntTest.createEntity(em);
        em.persist(returnTransaction);
        em.flush();
        returnItem.setReturnTransaction(returnTransaction);
        return returnItem;
    }

    @Before
    public void initTest() {
        returnItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createReturnItem() throws Exception {
        int databaseSizeBeforeCreate = returnItemRepository.findAll().size();

        // Create the ReturnItem
        ReturnItemDTO returnItemDTO = returnItemMapper.toDto(returnItem);
        restReturnItemMockMvc.perform(post("/api/return-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnItemDTO)))
            .andExpect(status().isCreated());

        // Validate the ReturnItem in the database
        List<ReturnItem> returnItemList = returnItemRepository.findAll();
        assertThat(returnItemList).hasSize(databaseSizeBeforeCreate + 1);
        ReturnItem testReturnItem = returnItemList.get(returnItemList.size() - 1);
        assertThat(testReturnItem.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testReturnItem.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testReturnItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testReturnItem.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testReturnItem.getProductStatus()).isEqualTo(DEFAULT_PRODUCT_STATUS);

        // Validate the ReturnItem in Elasticsearch
        verify(mockReturnItemSearchRepository, times(1)).save(testReturnItem);
    }

    @Test
    @Transactional
    public void createReturnItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = returnItemRepository.findAll().size();

        // Create the ReturnItem with an existing ID
        returnItem.setId(1L);
        ReturnItemDTO returnItemDTO = returnItemMapper.toDto(returnItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReturnItemMockMvc.perform(post("/api/return-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnItem in the database
        List<ReturnItem> returnItemList = returnItemRepository.findAll();
        assertThat(returnItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReturnItem in Elasticsearch
        verify(mockReturnItemSearchRepository, times(0)).save(returnItem);
    }

    @Test
    @Transactional
    public void checkBarcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnItemRepository.findAll().size();
        // set the field null
        returnItem.setBarcode(null);

        // Create the ReturnItem, which fails.
        ReturnItemDTO returnItemDTO = returnItemMapper.toDto(returnItem);

        restReturnItemMockMvc.perform(post("/api/return-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnItemDTO)))
            .andExpect(status().isBadRequest());

        List<ReturnItem> returnItemList = returnItemRepository.findAll();
        assertThat(returnItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnItemRepository.findAll().size();
        // set the field null
        returnItem.setProductName(null);

        // Create the ReturnItem, which fails.
        ReturnItemDTO returnItemDTO = returnItemMapper.toDto(returnItem);

        restReturnItemMockMvc.perform(post("/api/return-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnItemDTO)))
            .andExpect(status().isBadRequest());

        List<ReturnItem> returnItemList = returnItemRepository.findAll();
        assertThat(returnItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnItemRepository.findAll().size();
        // set the field null
        returnItem.setQuantity(null);

        // Create the ReturnItem, which fails.
        ReturnItemDTO returnItemDTO = returnItemMapper.toDto(returnItem);

        restReturnItemMockMvc.perform(post("/api/return-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnItemDTO)))
            .andExpect(status().isBadRequest());

        List<ReturnItem> returnItemList = returnItemRepository.findAll();
        assertThat(returnItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnItemRepository.findAll().size();
        // set the field null
        returnItem.setUnitPrice(null);

        // Create the ReturnItem, which fails.
        ReturnItemDTO returnItemDTO = returnItemMapper.toDto(returnItem);

        restReturnItemMockMvc.perform(post("/api/return-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnItemDTO)))
            .andExpect(status().isBadRequest());

        List<ReturnItem> returnItemList = returnItemRepository.findAll();
        assertThat(returnItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnItemRepository.findAll().size();
        // set the field null
        returnItem.setProductStatus(null);

        // Create the ReturnItem, which fails.
        ReturnItemDTO returnItemDTO = returnItemMapper.toDto(returnItem);

        restReturnItemMockMvc.perform(post("/api/return-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnItemDTO)))
            .andExpect(status().isBadRequest());

        List<ReturnItem> returnItemList = returnItemRepository.findAll();
        assertThat(returnItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReturnItems() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList
        restReturnItemMockMvc.perform(get("/api/return-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].productStatus").value(hasItem(DEFAULT_PRODUCT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getReturnItem() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get the returnItem
        restReturnItemMockMvc.perform(get("/api/return-items/{id}", returnItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(returnItem.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.productStatus").value(DEFAULT_PRODUCT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllReturnItemsByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where barcode equals to DEFAULT_BARCODE
        defaultReturnItemShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the returnItemList where barcode equals to UPDATED_BARCODE
        defaultReturnItemShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultReturnItemShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the returnItemList where barcode equals to UPDATED_BARCODE
        defaultReturnItemShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where barcode is not null
        defaultReturnItemShouldBeFound("barcode.specified=true");

        // Get all the returnItemList where barcode is null
        defaultReturnItemShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllReturnItemsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where productName equals to DEFAULT_PRODUCT_NAME
        defaultReturnItemShouldBeFound("productName.equals=" + DEFAULT_PRODUCT_NAME);

        // Get all the returnItemList where productName equals to UPDATED_PRODUCT_NAME
        defaultReturnItemShouldNotBeFound("productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where productName in DEFAULT_PRODUCT_NAME or UPDATED_PRODUCT_NAME
        defaultReturnItemShouldBeFound("productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME);

        // Get all the returnItemList where productName equals to UPDATED_PRODUCT_NAME
        defaultReturnItemShouldNotBeFound("productName.in=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where productName is not null
        defaultReturnItemShouldBeFound("productName.specified=true");

        // Get all the returnItemList where productName is null
        defaultReturnItemShouldNotBeFound("productName.specified=false");
    }

    @Test
    @Transactional
    public void getAllReturnItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where quantity equals to DEFAULT_QUANTITY
        defaultReturnItemShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the returnItemList where quantity equals to UPDATED_QUANTITY
        defaultReturnItemShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultReturnItemShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the returnItemList where quantity equals to UPDATED_QUANTITY
        defaultReturnItemShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where quantity is not null
        defaultReturnItemShouldBeFound("quantity.specified=true");

        // Get all the returnItemList where quantity is null
        defaultReturnItemShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllReturnItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultReturnItemShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the returnItemList where quantity greater than or equals to UPDATED_QUANTITY
        defaultReturnItemShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where quantity less than or equals to DEFAULT_QUANTITY
        defaultReturnItemShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the returnItemList where quantity less than or equals to UPDATED_QUANTITY
        defaultReturnItemShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllReturnItemsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultReturnItemShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the returnItemList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultReturnItemShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultReturnItemShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the returnItemList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultReturnItemShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where unitPrice is not null
        defaultReturnItemShouldBeFound("unitPrice.specified=true");

        // Get all the returnItemList where unitPrice is null
        defaultReturnItemShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllReturnItemsByProductStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where productStatus equals to DEFAULT_PRODUCT_STATUS
        defaultReturnItemShouldBeFound("productStatus.equals=" + DEFAULT_PRODUCT_STATUS);

        // Get all the returnItemList where productStatus equals to UPDATED_PRODUCT_STATUS
        defaultReturnItemShouldNotBeFound("productStatus.equals=" + UPDATED_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByProductStatusIsInShouldWork() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where productStatus in DEFAULT_PRODUCT_STATUS or UPDATED_PRODUCT_STATUS
        defaultReturnItemShouldBeFound("productStatus.in=" + DEFAULT_PRODUCT_STATUS + "," + UPDATED_PRODUCT_STATUS);

        // Get all the returnItemList where productStatus equals to UPDATED_PRODUCT_STATUS
        defaultReturnItemShouldNotBeFound("productStatus.in=" + UPDATED_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    public void getAllReturnItemsByProductStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where productStatus is not null
        defaultReturnItemShouldBeFound("productStatus.specified=true");

        // Get all the returnItemList where productStatus is null
        defaultReturnItemShouldNotBeFound("productStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllReturnItemsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        returnItem.setProduct(product);
        returnItemRepository.saveAndFlush(returnItem);
        Long productId = product.getId();

        // Get all the returnItemList where product equals to productId
        defaultReturnItemShouldBeFound("productId.equals=" + productId);

        // Get all the returnItemList where product equals to productId + 1
        defaultReturnItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }


    @Test
    @Transactional
    public void getAllReturnItemsByReturnTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        ReturnTransaction returnTransaction = ReturnTransactionResourceIntTest.createEntity(em);
        em.persist(returnTransaction);
        em.flush();
        returnItem.setReturnTransaction(returnTransaction);
        returnItemRepository.saveAndFlush(returnItem);
        Long returnTransactionId = returnTransaction.getId();

        // Get all the returnItemList where returnTransaction equals to returnTransactionId
        defaultReturnItemShouldBeFound("returnTransactionId.equals=" + returnTransactionId);

        // Get all the returnItemList where returnTransaction equals to returnTransactionId + 1
        defaultReturnItemShouldNotBeFound("returnTransactionId.equals=" + (returnTransactionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReturnItemShouldBeFound(String filter) throws Exception {
        restReturnItemMockMvc.perform(get("/api/return-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].productStatus").value(hasItem(DEFAULT_PRODUCT_STATUS.toString())));

        // Check, that the count call also returns 1
        restReturnItemMockMvc.perform(get("/api/return-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReturnItemShouldNotBeFound(String filter) throws Exception {
        restReturnItemMockMvc.perform(get("/api/return-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReturnItemMockMvc.perform(get("/api/return-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingReturnItem() throws Exception {
        // Get the returnItem
        restReturnItemMockMvc.perform(get("/api/return-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReturnItem() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        int databaseSizeBeforeUpdate = returnItemRepository.findAll().size();

        // Update the returnItem
        ReturnItem updatedReturnItem = returnItemRepository.findById(returnItem.getId()).get();
        // Disconnect from session so that the updates on updatedReturnItem are not directly saved in db
        em.detach(updatedReturnItem);
        updatedReturnItem
            .barcode(UPDATED_BARCODE)
            .productName(UPDATED_PRODUCT_NAME)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .productStatus(UPDATED_PRODUCT_STATUS);
        ReturnItemDTO returnItemDTO = returnItemMapper.toDto(updatedReturnItem);

        restReturnItemMockMvc.perform(put("/api/return-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnItemDTO)))
            .andExpect(status().isOk());

        // Validate the ReturnItem in the database
        List<ReturnItem> returnItemList = returnItemRepository.findAll();
        assertThat(returnItemList).hasSize(databaseSizeBeforeUpdate);
        ReturnItem testReturnItem = returnItemList.get(returnItemList.size() - 1);
        assertThat(testReturnItem.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testReturnItem.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testReturnItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testReturnItem.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testReturnItem.getProductStatus()).isEqualTo(UPDATED_PRODUCT_STATUS);

        // Validate the ReturnItem in Elasticsearch
        verify(mockReturnItemSearchRepository, times(1)).save(testReturnItem);
    }

    @Test
    @Transactional
    public void updateNonExistingReturnItem() throws Exception {
        int databaseSizeBeforeUpdate = returnItemRepository.findAll().size();

        // Create the ReturnItem
        ReturnItemDTO returnItemDTO = returnItemMapper.toDto(returnItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnItemMockMvc.perform(put("/api/return-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(returnItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnItem in the database
        List<ReturnItem> returnItemList = returnItemRepository.findAll();
        assertThat(returnItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReturnItem in Elasticsearch
        verify(mockReturnItemSearchRepository, times(0)).save(returnItem);
    }

    @Test
    @Transactional
    public void deleteReturnItem() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);

        int databaseSizeBeforeDelete = returnItemRepository.findAll().size();

        // Delete the returnItem
        restReturnItemMockMvc.perform(delete("/api/return-items/{id}", returnItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReturnItem> returnItemList = returnItemRepository.findAll();
        assertThat(returnItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReturnItem in Elasticsearch
        verify(mockReturnItemSearchRepository, times(1)).deleteById(returnItem.getId());
    }

    @Test
    @Transactional
    public void searchReturnItem() throws Exception {
        // Initialize the database
        returnItemRepository.saveAndFlush(returnItem);
        when(mockReturnItemSearchRepository.search(queryStringQuery("id:" + returnItem.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(returnItem), PageRequest.of(0, 1), 1));
        // Search the returnItem
        restReturnItemMockMvc.perform(get("/api/_search/return-items?query=id:" + returnItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].productStatus").value(hasItem(DEFAULT_PRODUCT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnItem.class);
        ReturnItem returnItem1 = new ReturnItem();
        returnItem1.setId(1L);
        ReturnItem returnItem2 = new ReturnItem();
        returnItem2.setId(returnItem1.getId());
        assertThat(returnItem1).isEqualTo(returnItem2);
        returnItem2.setId(2L);
        assertThat(returnItem1).isNotEqualTo(returnItem2);
        returnItem1.setId(null);
        assertThat(returnItem1).isNotEqualTo(returnItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnItemDTO.class);
        ReturnItemDTO returnItemDTO1 = new ReturnItemDTO();
        returnItemDTO1.setId(1L);
        ReturnItemDTO returnItemDTO2 = new ReturnItemDTO();
        assertThat(returnItemDTO1).isNotEqualTo(returnItemDTO2);
        returnItemDTO2.setId(returnItemDTO1.getId());
        assertThat(returnItemDTO1).isEqualTo(returnItemDTO2);
        returnItemDTO2.setId(2L);
        assertThat(returnItemDTO1).isNotEqualTo(returnItemDTO2);
        returnItemDTO1.setId(null);
        assertThat(returnItemDTO1).isNotEqualTo(returnItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(returnItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(returnItemMapper.fromId(null)).isNull();
    }
}
