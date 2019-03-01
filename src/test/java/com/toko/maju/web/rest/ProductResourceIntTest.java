package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.Product;
import com.toko.maju.repository.ProductRepository;
import com.toko.maju.repository.search.ProductSearchRepository;
import com.toko.maju.service.ProductService;
import com.toko.maju.service.dto.ProductDTO;
import com.toko.maju.service.mapper.ProductMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.ProductCriteria;
import com.toko.maju.service.ProductQueryService;

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

import com.toko.maju.domain.enumeration.UnitMeasure;
/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class ProductResourceIntTest {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UnitMeasure DEFAULT_UNIT = UnitMeasure.KG;
    private static final UnitMeasure UPDATED_UNIT = UnitMeasure.SAK;

    private static final BigDecimal DEFAULT_WAREHOUSE_PRICES = new BigDecimal(0);
    private static final BigDecimal UPDATED_WAREHOUSE_PRICES = new BigDecimal(1);

    private static final BigDecimal DEFAULT_UNIT_PRICES = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_PRICES = new BigDecimal(1);

    private static final BigDecimal DEFAULT_SELLING_PRICES = new BigDecimal(0);
    private static final BigDecimal UPDATED_SELLING_PRICES = new BigDecimal(1);

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.ProductSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductSearchRepository mockProductSearchRepository;

    @Autowired
    private ProductQueryService productQueryService;

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

    private MockMvc restProductMockMvc;

    private Product product;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productService, productQueryService);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
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
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .barcode(DEFAULT_BARCODE)
            .name(DEFAULT_NAME)
            .unit(DEFAULT_UNIT)
            .warehousePrices(DEFAULT_WAREHOUSE_PRICES)
            .unitPrices(DEFAULT_UNIT_PRICES)
            .sellingPrices(DEFAULT_SELLING_PRICES)
            .stock(DEFAULT_STOCK);
        return product;
    }

    @Before
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testProduct.getWarehousePrices()).isEqualTo(DEFAULT_WAREHOUSE_PRICES);
        assertThat(testProduct.getUnitPrices()).isEqualTo(DEFAULT_UNIT_PRICES);
        assertThat(testProduct.getSellingPrices()).isEqualTo(DEFAULT_SELLING_PRICES);
        assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).save(testProduct);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(0)).save(product);
    }

    @Test
    @Transactional
    public void checkBarcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setBarcode(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setUnit(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWarehousePricesIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setWarehousePrices(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitPricesIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setUnitPrices(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSellingPricesIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setSellingPrices(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setStock(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].warehousePrices").value(hasItem(DEFAULT_WAREHOUSE_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].unitPrices").value(hasItem(DEFAULT_UNIT_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].sellingPrices").value(hasItem(DEFAULT_SELLING_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)));
    }
    
    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.warehousePrices").value(DEFAULT_WAREHOUSE_PRICES.intValue()))
            .andExpect(jsonPath("$.unitPrices").value(DEFAULT_UNIT_PRICES.intValue()))
            .andExpect(jsonPath("$.sellingPrices").value(DEFAULT_SELLING_PRICES.intValue()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK));
    }

    @Test
    @Transactional
    public void getAllProductsByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode equals to DEFAULT_BARCODE
        defaultProductShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the productList where barcode equals to UPDATED_BARCODE
        defaultProductShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllProductsByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultProductShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the productList where barcode equals to UPDATED_BARCODE
        defaultProductShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllProductsByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode is not null
        defaultProductShouldBeFound("barcode.specified=true");

        // Get all the productList where barcode is null
        defaultProductShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unit equals to DEFAULT_UNIT
        defaultProductShouldBeFound("unit.equals=" + DEFAULT_UNIT);

        // Get all the productList where unit equals to UPDATED_UNIT
        defaultProductShouldNotBeFound("unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unit in DEFAULT_UNIT or UPDATED_UNIT
        defaultProductShouldBeFound("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT);

        // Get all the productList where unit equals to UPDATED_UNIT
        defaultProductShouldNotBeFound("unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unit is not null
        defaultProductShouldBeFound("unit.specified=true");

        // Get all the productList where unit is null
        defaultProductShouldNotBeFound("unit.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByWarehousePricesIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where warehousePrices equals to DEFAULT_WAREHOUSE_PRICES
        defaultProductShouldBeFound("warehousePrices.equals=" + DEFAULT_WAREHOUSE_PRICES);

        // Get all the productList where warehousePrices equals to UPDATED_WAREHOUSE_PRICES
        defaultProductShouldNotBeFound("warehousePrices.equals=" + UPDATED_WAREHOUSE_PRICES);
    }

    @Test
    @Transactional
    public void getAllProductsByWarehousePricesIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where warehousePrices in DEFAULT_WAREHOUSE_PRICES or UPDATED_WAREHOUSE_PRICES
        defaultProductShouldBeFound("warehousePrices.in=" + DEFAULT_WAREHOUSE_PRICES + "," + UPDATED_WAREHOUSE_PRICES);

        // Get all the productList where warehousePrices equals to UPDATED_WAREHOUSE_PRICES
        defaultProductShouldNotBeFound("warehousePrices.in=" + UPDATED_WAREHOUSE_PRICES);
    }

    @Test
    @Transactional
    public void getAllProductsByWarehousePricesIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where warehousePrices is not null
        defaultProductShouldBeFound("warehousePrices.specified=true");

        // Get all the productList where warehousePrices is null
        defaultProductShouldNotBeFound("warehousePrices.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByUnitPricesIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrices equals to DEFAULT_UNIT_PRICES
        defaultProductShouldBeFound("unitPrices.equals=" + DEFAULT_UNIT_PRICES);

        // Get all the productList where unitPrices equals to UPDATED_UNIT_PRICES
        defaultProductShouldNotBeFound("unitPrices.equals=" + UPDATED_UNIT_PRICES);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitPricesIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrices in DEFAULT_UNIT_PRICES or UPDATED_UNIT_PRICES
        defaultProductShouldBeFound("unitPrices.in=" + DEFAULT_UNIT_PRICES + "," + UPDATED_UNIT_PRICES);

        // Get all the productList where unitPrices equals to UPDATED_UNIT_PRICES
        defaultProductShouldNotBeFound("unitPrices.in=" + UPDATED_UNIT_PRICES);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitPricesIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrices is not null
        defaultProductShouldBeFound("unitPrices.specified=true");

        // Get all the productList where unitPrices is null
        defaultProductShouldNotBeFound("unitPrices.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsBySellingPricesIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sellingPrices equals to DEFAULT_SELLING_PRICES
        defaultProductShouldBeFound("sellingPrices.equals=" + DEFAULT_SELLING_PRICES);

        // Get all the productList where sellingPrices equals to UPDATED_SELLING_PRICES
        defaultProductShouldNotBeFound("sellingPrices.equals=" + UPDATED_SELLING_PRICES);
    }

    @Test
    @Transactional
    public void getAllProductsBySellingPricesIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sellingPrices in DEFAULT_SELLING_PRICES or UPDATED_SELLING_PRICES
        defaultProductShouldBeFound("sellingPrices.in=" + DEFAULT_SELLING_PRICES + "," + UPDATED_SELLING_PRICES);

        // Get all the productList where sellingPrices equals to UPDATED_SELLING_PRICES
        defaultProductShouldNotBeFound("sellingPrices.in=" + UPDATED_SELLING_PRICES);
    }

    @Test
    @Transactional
    public void getAllProductsBySellingPricesIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sellingPrices is not null
        defaultProductShouldBeFound("sellingPrices.specified=true");

        // Get all the productList where sellingPrices is null
        defaultProductShouldNotBeFound("sellingPrices.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock equals to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.equals=" + DEFAULT_STOCK);

        // Get all the productList where stock equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock in DEFAULT_STOCK or UPDATED_STOCK
        defaultProductShouldBeFound("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK);

        // Get all the productList where stock equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is not null
        defaultProductShouldBeFound("stock.specified=true");

        // Get all the productList where stock is null
        defaultProductShouldNotBeFound("stock.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock greater than or equals to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.greaterOrEqualThan=" + 3);

        // Get all the productList where stock greater than or equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.greaterOrEqualThan=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock less than or equals to DEFAULT_STOCK
        defaultProductShouldNotBeFound("stock.lessThan=" + DEFAULT_STOCK);

        // Get all the productList where stock less than or equals to UPDATED_STOCK
        defaultProductShouldBeFound("stock.lessThan=" + UPDATED_STOCK);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].warehousePrices").value(hasItem(DEFAULT_WAREHOUSE_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].unitPrices").value(hasItem(DEFAULT_UNIT_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].sellingPrices").value(hasItem(DEFAULT_SELLING_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)));

        // Check, that the count call also returns 1
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .barcode(UPDATED_BARCODE)
            .name(UPDATED_NAME)
            .unit(UPDATED_UNIT)
            .warehousePrices(UPDATED_WAREHOUSE_PRICES)
            .unitPrices(UPDATED_UNIT_PRICES)
            .sellingPrices(UPDATED_SELLING_PRICES)
            .stock(UPDATED_STOCK);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testProduct.getWarehousePrices()).isEqualTo(UPDATED_WAREHOUSE_PRICES);
        assertThat(testProduct.getUnitPrices()).isEqualTo(UPDATED_UNIT_PRICES);
        assertThat(testProduct.getSellingPrices()).isEqualTo(UPDATED_SELLING_PRICES);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).save(testProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(0)).save(product);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).deleteById(product.getId());
    }

    @Test
    @Transactional
    public void searchProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        when(mockProductSearchRepository.search(queryStringQuery("id:" + product.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(product), PageRequest.of(0, 1), 1));
        // Search the product
        restProductMockMvc.perform(get("/api/_search/products?query=id:" + product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].warehousePrices").value(hasItem(DEFAULT_WAREHOUSE_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].unitPrices").value(hasItem(DEFAULT_UNIT_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].sellingPrices").value(hasItem(DEFAULT_SELLING_PRICES.intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(2L);
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDTO.class);
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        ProductDTO productDTO2 = new ProductDTO();
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO2.setId(productDTO1.getId());
        assertThat(productDTO1).isEqualTo(productDTO2);
        productDTO2.setId(2L);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO1.setId(null);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productMapper.fromId(null)).isNull();
    }
}
