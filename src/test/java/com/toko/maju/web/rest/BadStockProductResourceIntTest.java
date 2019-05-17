package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.BadStockProduct;
import com.toko.maju.repository.BadStockProductRepository;
import com.toko.maju.repository.search.BadStockProductSearchRepository;
import com.toko.maju.service.BadStockProductService;
import com.toko.maju.service.dto.BadStockProductDTO;
import com.toko.maju.service.mapper.BadStockProductMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.BadStockProductCriteria;
import com.toko.maju.service.BadStockProductQueryService;

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
 * Test class for the BadStockProductResource REST controller.
 *
 * @see BadStockProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class BadStockProductResourceIntTest {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    @Autowired
    private BadStockProductRepository badStockProductRepository;

    @Autowired
    private BadStockProductMapper badStockProductMapper;

    @Autowired
    private BadStockProductService badStockProductService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.BadStockProductSearchRepositoryMockConfiguration
     */
    @Autowired
    private BadStockProductSearchRepository mockBadStockProductSearchRepository;

    @Autowired
    private BadStockProductQueryService badStockProductQueryService;

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

    private MockMvc restBadStockProductMockMvc;

    private BadStockProduct badStockProduct;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BadStockProductResource badStockProductResource = new BadStockProductResource(badStockProductService, badStockProductQueryService);
        this.restBadStockProductMockMvc = MockMvcBuilders.standaloneSetup(badStockProductResource)
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
    public static BadStockProduct createEntity(EntityManager em) {
        BadStockProduct badStockProduct = new BadStockProduct()
            .barcode(DEFAULT_BARCODE)
            .productName(DEFAULT_PRODUCT_NAME)
            .quantity(DEFAULT_QUANTITY);
        return badStockProduct;
    }

    @Before
    public void initTest() {
        badStockProduct = createEntity(em);
    }

    @Test
    @Transactional
    public void createBadStockProduct() throws Exception {
        int databaseSizeBeforeCreate = badStockProductRepository.findAll().size();

        // Create the BadStockProduct
        BadStockProductDTO badStockProductDTO = badStockProductMapper.toDto(badStockProduct);
        restBadStockProductMockMvc.perform(post("/api/bad-stock-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(badStockProductDTO)))
            .andExpect(status().isCreated());

        // Validate the BadStockProduct in the database
        List<BadStockProduct> badStockProductList = badStockProductRepository.findAll();
        assertThat(badStockProductList).hasSize(databaseSizeBeforeCreate + 1);
        BadStockProduct testBadStockProduct = badStockProductList.get(badStockProductList.size() - 1);
        assertThat(testBadStockProduct.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testBadStockProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testBadStockProduct.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the BadStockProduct in Elasticsearch
        verify(mockBadStockProductSearchRepository, times(1)).save(testBadStockProduct);
    }

    @Test
    @Transactional
    public void createBadStockProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = badStockProductRepository.findAll().size();

        // Create the BadStockProduct with an existing ID
        badStockProduct.setId(1L);
        BadStockProductDTO badStockProductDTO = badStockProductMapper.toDto(badStockProduct);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBadStockProductMockMvc.perform(post("/api/bad-stock-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(badStockProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BadStockProduct in the database
        List<BadStockProduct> badStockProductList = badStockProductRepository.findAll();
        assertThat(badStockProductList).hasSize(databaseSizeBeforeCreate);

        // Validate the BadStockProduct in Elasticsearch
        verify(mockBadStockProductSearchRepository, times(0)).save(badStockProduct);
    }

    @Test
    @Transactional
    public void checkBarcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = badStockProductRepository.findAll().size();
        // set the field null
        badStockProduct.setBarcode(null);

        // Create the BadStockProduct, which fails.
        BadStockProductDTO badStockProductDTO = badStockProductMapper.toDto(badStockProduct);

        restBadStockProductMockMvc.perform(post("/api/bad-stock-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(badStockProductDTO)))
            .andExpect(status().isBadRequest());

        List<BadStockProduct> badStockProductList = badStockProductRepository.findAll();
        assertThat(badStockProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = badStockProductRepository.findAll().size();
        // set the field null
        badStockProduct.setProductName(null);

        // Create the BadStockProduct, which fails.
        BadStockProductDTO badStockProductDTO = badStockProductMapper.toDto(badStockProduct);

        restBadStockProductMockMvc.perform(post("/api/bad-stock-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(badStockProductDTO)))
            .andExpect(status().isBadRequest());

        List<BadStockProduct> badStockProductList = badStockProductRepository.findAll();
        assertThat(badStockProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = badStockProductRepository.findAll().size();
        // set the field null
        badStockProduct.setQuantity(null);

        // Create the BadStockProduct, which fails.
        BadStockProductDTO badStockProductDTO = badStockProductMapper.toDto(badStockProduct);

        restBadStockProductMockMvc.perform(post("/api/bad-stock-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(badStockProductDTO)))
            .andExpect(status().isBadRequest());

        List<BadStockProduct> badStockProductList = badStockProductRepository.findAll();
        assertThat(badStockProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBadStockProducts() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList
        restBadStockProductMockMvc.perform(get("/api/bad-stock-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(badStockProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getBadStockProduct() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get the badStockProduct
        restBadStockProductMockMvc.perform(get("/api/bad-stock-products/{id}", badStockProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(badStockProduct.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where barcode equals to DEFAULT_BARCODE
        defaultBadStockProductShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the badStockProductList where barcode equals to UPDATED_BARCODE
        defaultBadStockProductShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultBadStockProductShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the badStockProductList where barcode equals to UPDATED_BARCODE
        defaultBadStockProductShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where barcode is not null
        defaultBadStockProductShouldBeFound("barcode.specified=true");

        // Get all the badStockProductList where barcode is null
        defaultBadStockProductShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where productName equals to DEFAULT_PRODUCT_NAME
        defaultBadStockProductShouldBeFound("productName.equals=" + DEFAULT_PRODUCT_NAME);

        // Get all the badStockProductList where productName equals to UPDATED_PRODUCT_NAME
        defaultBadStockProductShouldNotBeFound("productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where productName in DEFAULT_PRODUCT_NAME or UPDATED_PRODUCT_NAME
        defaultBadStockProductShouldBeFound("productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME);

        // Get all the badStockProductList where productName equals to UPDATED_PRODUCT_NAME
        defaultBadStockProductShouldNotBeFound("productName.in=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where productName is not null
        defaultBadStockProductShouldBeFound("productName.specified=true");

        // Get all the badStockProductList where productName is null
        defaultBadStockProductShouldNotBeFound("productName.specified=false");
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where quantity equals to DEFAULT_QUANTITY
        defaultBadStockProductShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the badStockProductList where quantity equals to UPDATED_QUANTITY
        defaultBadStockProductShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultBadStockProductShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the badStockProductList where quantity equals to UPDATED_QUANTITY
        defaultBadStockProductShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where quantity is not null
        defaultBadStockProductShouldBeFound("quantity.specified=true");

        // Get all the badStockProductList where quantity is null
        defaultBadStockProductShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultBadStockProductShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the badStockProductList where quantity greater than or equals to UPDATED_QUANTITY
        defaultBadStockProductShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllBadStockProductsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        // Get all the badStockProductList where quantity less than or equals to DEFAULT_QUANTITY
        defaultBadStockProductShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the badStockProductList where quantity less than or equals to UPDATED_QUANTITY
        defaultBadStockProductShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBadStockProductShouldBeFound(String filter) throws Exception {
        restBadStockProductMockMvc.perform(get("/api/bad-stock-products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(badStockProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));

        // Check, that the count call also returns 1
        restBadStockProductMockMvc.perform(get("/api/bad-stock-products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBadStockProductShouldNotBeFound(String filter) throws Exception {
        restBadStockProductMockMvc.perform(get("/api/bad-stock-products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBadStockProductMockMvc.perform(get("/api/bad-stock-products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBadStockProduct() throws Exception {
        // Get the badStockProduct
        restBadStockProductMockMvc.perform(get("/api/bad-stock-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBadStockProduct() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        int databaseSizeBeforeUpdate = badStockProductRepository.findAll().size();

        // Update the badStockProduct
        BadStockProduct updatedBadStockProduct = badStockProductRepository.findById(badStockProduct.getId()).get();
        // Disconnect from session so that the updates on updatedBadStockProduct are not directly saved in db
        em.detach(updatedBadStockProduct);
        updatedBadStockProduct
            .barcode(UPDATED_BARCODE)
            .productName(UPDATED_PRODUCT_NAME)
            .quantity(UPDATED_QUANTITY);
        BadStockProductDTO badStockProductDTO = badStockProductMapper.toDto(updatedBadStockProduct);

        restBadStockProductMockMvc.perform(put("/api/bad-stock-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(badStockProductDTO)))
            .andExpect(status().isOk());

        // Validate the BadStockProduct in the database
        List<BadStockProduct> badStockProductList = badStockProductRepository.findAll();
        assertThat(badStockProductList).hasSize(databaseSizeBeforeUpdate);
        BadStockProduct testBadStockProduct = badStockProductList.get(badStockProductList.size() - 1);
        assertThat(testBadStockProduct.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testBadStockProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testBadStockProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the BadStockProduct in Elasticsearch
        verify(mockBadStockProductSearchRepository, times(1)).save(testBadStockProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingBadStockProduct() throws Exception {
        int databaseSizeBeforeUpdate = badStockProductRepository.findAll().size();

        // Create the BadStockProduct
        BadStockProductDTO badStockProductDTO = badStockProductMapper.toDto(badStockProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBadStockProductMockMvc.perform(put("/api/bad-stock-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(badStockProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BadStockProduct in the database
        List<BadStockProduct> badStockProductList = badStockProductRepository.findAll();
        assertThat(badStockProductList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BadStockProduct in Elasticsearch
        verify(mockBadStockProductSearchRepository, times(0)).save(badStockProduct);
    }

    @Test
    @Transactional
    public void deleteBadStockProduct() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);

        int databaseSizeBeforeDelete = badStockProductRepository.findAll().size();

        // Delete the badStockProduct
        restBadStockProductMockMvc.perform(delete("/api/bad-stock-products/{id}", badStockProduct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BadStockProduct> badStockProductList = badStockProductRepository.findAll();
        assertThat(badStockProductList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BadStockProduct in Elasticsearch
        verify(mockBadStockProductSearchRepository, times(1)).deleteById(badStockProduct.getId());
    }

    @Test
    @Transactional
    public void searchBadStockProduct() throws Exception {
        // Initialize the database
        badStockProductRepository.saveAndFlush(badStockProduct);
        when(mockBadStockProductSearchRepository.search(queryStringQuery("id:" + badStockProduct.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(badStockProduct), PageRequest.of(0, 1), 1));
        // Search the badStockProduct
        restBadStockProductMockMvc.perform(get("/api/_search/bad-stock-products?query=id:" + badStockProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(badStockProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BadStockProduct.class);
        BadStockProduct badStockProduct1 = new BadStockProduct();
        badStockProduct1.setId(1L);
        BadStockProduct badStockProduct2 = new BadStockProduct();
        badStockProduct2.setId(badStockProduct1.getId());
        assertThat(badStockProduct1).isEqualTo(badStockProduct2);
        badStockProduct2.setId(2L);
        assertThat(badStockProduct1).isNotEqualTo(badStockProduct2);
        badStockProduct1.setId(null);
        assertThat(badStockProduct1).isNotEqualTo(badStockProduct2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BadStockProductDTO.class);
        BadStockProductDTO badStockProductDTO1 = new BadStockProductDTO();
        badStockProductDTO1.setId(1L);
        BadStockProductDTO badStockProductDTO2 = new BadStockProductDTO();
        assertThat(badStockProductDTO1).isNotEqualTo(badStockProductDTO2);
        badStockProductDTO2.setId(badStockProductDTO1.getId());
        assertThat(badStockProductDTO1).isEqualTo(badStockProductDTO2);
        badStockProductDTO2.setId(2L);
        assertThat(badStockProductDTO1).isNotEqualTo(badStockProductDTO2);
        badStockProductDTO1.setId(null);
        assertThat(badStockProductDTO1).isNotEqualTo(badStockProductDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(badStockProductMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(badStockProductMapper.fromId(null)).isNull();
    }
}
