package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.Supplier;
import com.toko.maju.domain.Product;
import com.toko.maju.repository.SupplierRepository;
import com.toko.maju.repository.search.SupplierSearchRepository;
import com.toko.maju.service.SupplierService;
import com.toko.maju.service.dto.SupplierDTO;
import com.toko.maju.service.mapper.SupplierMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.SupplierCriteria;
import com.toko.maju.service.SupplierQueryService;

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
 * Test class for the SupplierResource REST controller.
 *
 * @see SupplierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class SupplierResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_NO_TELP = "AAAAAAAAAA";
    private static final String UPDATED_NO_TELP = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private SupplierService supplierService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.SupplierSearchRepositoryMockConfiguration
     */
    @Autowired
    private SupplierSearchRepository mockSupplierSearchRepository;

    @Autowired
    private SupplierQueryService supplierQueryService;

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

    private MockMvc restSupplierMockMvc;

    private Supplier supplier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SupplierResource supplierResource = new SupplierResource(supplierService, supplierQueryService);
        this.restSupplierMockMvc = MockMvcBuilders.standaloneSetup(supplierResource)
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
    public static Supplier createEntity(EntityManager em) {
        Supplier supplier = new Supplier()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .address(DEFAULT_ADDRESS)
            .noTelp(DEFAULT_NO_TELP)
            .bankAccount(DEFAULT_BANK_ACCOUNT)
            .bankName(DEFAULT_BANK_NAME);
        return supplier;
    }

    @Before
    public void initTest() {
        supplier = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupplier() throws Exception {
        int databaseSizeBeforeCreate = supplierRepository.findAll().size();

        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);
        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isCreated());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeCreate + 1);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupplier.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSupplier.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSupplier.getNoTelp()).isEqualTo(DEFAULT_NO_TELP);
        assertThat(testSupplier.getBankAccount()).isEqualTo(DEFAULT_BANK_ACCOUNT);
        assertThat(testSupplier.getBankName()).isEqualTo(DEFAULT_BANK_NAME);

        // Validate the Supplier in Elasticsearch
        verify(mockSupplierSearchRepository, times(1)).save(testSupplier);
    }

    @Test
    @Transactional
    public void createSupplierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = supplierRepository.findAll().size();

        // Create the Supplier with an existing ID
        supplier.setId(1L);
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeCreate);

        // Validate the Supplier in Elasticsearch
        verify(mockSupplierSearchRepository, times(0)).save(supplier);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setName(null);

        // Create the Supplier, which fails.
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setCode(null);

        // Create the Supplier, which fails.
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setAddress(null);

        // Create the Supplier, which fails.
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNoTelpIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setNoTelp(null);

        // Create the Supplier, which fails.
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuppliers() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList
        restSupplierMockMvc.perform(get("/api/suppliers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].noTelp").value(hasItem(DEFAULT_NO_TELP.toString())))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get the supplier
        restSupplierMockMvc.perform(get("/api/suppliers/{id}", supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(supplier.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.noTelp").value(DEFAULT_NO_TELP.toString()))
            .andExpect(jsonPath("$.bankAccount").value(DEFAULT_BANK_ACCOUNT.toString()))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllSuppliersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name equals to DEFAULT_NAME
        defaultSupplierShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the supplierList where name equals to UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSuppliersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSupplierShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the supplierList where name equals to UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSuppliersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name is not null
        defaultSupplierShouldBeFound("name.specified=true");

        // Get all the supplierList where name is null
        defaultSupplierShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuppliersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where code equals to DEFAULT_CODE
        defaultSupplierShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the supplierList where code equals to UPDATED_CODE
        defaultSupplierShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSuppliersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSupplierShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the supplierList where code equals to UPDATED_CODE
        defaultSupplierShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSuppliersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where code is not null
        defaultSupplierShouldBeFound("code.specified=true");

        // Get all the supplierList where code is null
        defaultSupplierShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuppliersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address equals to DEFAULT_ADDRESS
        defaultSupplierShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the supplierList where address equals to UPDATED_ADDRESS
        defaultSupplierShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSuppliersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultSupplierShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the supplierList where address equals to UPDATED_ADDRESS
        defaultSupplierShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSuppliersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address is not null
        defaultSupplierShouldBeFound("address.specified=true");

        // Get all the supplierList where address is null
        defaultSupplierShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuppliersByNoTelpIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where noTelp equals to DEFAULT_NO_TELP
        defaultSupplierShouldBeFound("noTelp.equals=" + DEFAULT_NO_TELP);

        // Get all the supplierList where noTelp equals to UPDATED_NO_TELP
        defaultSupplierShouldNotBeFound("noTelp.equals=" + UPDATED_NO_TELP);
    }

    @Test
    @Transactional
    public void getAllSuppliersByNoTelpIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where noTelp in DEFAULT_NO_TELP or UPDATED_NO_TELP
        defaultSupplierShouldBeFound("noTelp.in=" + DEFAULT_NO_TELP + "," + UPDATED_NO_TELP);

        // Get all the supplierList where noTelp equals to UPDATED_NO_TELP
        defaultSupplierShouldNotBeFound("noTelp.in=" + UPDATED_NO_TELP);
    }

    @Test
    @Transactional
    public void getAllSuppliersByNoTelpIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where noTelp is not null
        defaultSupplierShouldBeFound("noTelp.specified=true");

        // Get all the supplierList where noTelp is null
        defaultSupplierShouldNotBeFound("noTelp.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuppliersByBankAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankAccount equals to DEFAULT_BANK_ACCOUNT
        defaultSupplierShouldBeFound("bankAccount.equals=" + DEFAULT_BANK_ACCOUNT);

        // Get all the supplierList where bankAccount equals to UPDATED_BANK_ACCOUNT
        defaultSupplierShouldNotBeFound("bankAccount.equals=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    public void getAllSuppliersByBankAccountIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankAccount in DEFAULT_BANK_ACCOUNT or UPDATED_BANK_ACCOUNT
        defaultSupplierShouldBeFound("bankAccount.in=" + DEFAULT_BANK_ACCOUNT + "," + UPDATED_BANK_ACCOUNT);

        // Get all the supplierList where bankAccount equals to UPDATED_BANK_ACCOUNT
        defaultSupplierShouldNotBeFound("bankAccount.in=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    public void getAllSuppliersByBankAccountIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankAccount is not null
        defaultSupplierShouldBeFound("bankAccount.specified=true");

        // Get all the supplierList where bankAccount is null
        defaultSupplierShouldNotBeFound("bankAccount.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuppliersByBankNameIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankName equals to DEFAULT_BANK_NAME
        defaultSupplierShouldBeFound("bankName.equals=" + DEFAULT_BANK_NAME);

        // Get all the supplierList where bankName equals to UPDATED_BANK_NAME
        defaultSupplierShouldNotBeFound("bankName.equals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllSuppliersByBankNameIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankName in DEFAULT_BANK_NAME or UPDATED_BANK_NAME
        defaultSupplierShouldBeFound("bankName.in=" + DEFAULT_BANK_NAME + "," + UPDATED_BANK_NAME);

        // Get all the supplierList where bankName equals to UPDATED_BANK_NAME
        defaultSupplierShouldNotBeFound("bankName.in=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllSuppliersByBankNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankName is not null
        defaultSupplierShouldBeFound("bankName.specified=true");

        // Get all the supplierList where bankName is null
        defaultSupplierShouldNotBeFound("bankName.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuppliersByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        supplier.addProduct(product);
        supplierRepository.saveAndFlush(supplier);
        Long productId = product.getId();

        // Get all the supplierList where product equals to productId
        defaultSupplierShouldBeFound("productId.equals=" + productId);

        // Get all the supplierList where product equals to productId + 1
        defaultSupplierShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSupplierShouldBeFound(String filter) throws Exception {
        restSupplierMockMvc.perform(get("/api/suppliers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].noTelp").value(hasItem(DEFAULT_NO_TELP)))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)));

        // Check, that the count call also returns 1
        restSupplierMockMvc.perform(get("/api/suppliers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSupplierShouldNotBeFound(String filter) throws Exception {
        restSupplierMockMvc.perform(get("/api/suppliers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierMockMvc.perform(get("/api/suppliers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSupplier() throws Exception {
        // Get the supplier
        restSupplierMockMvc.perform(get("/api/suppliers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier
        Supplier updatedSupplier = supplierRepository.findById(supplier.getId()).get();
        // Disconnect from session so that the updates on updatedSupplier are not directly saved in db
        em.detach(updatedSupplier);
        updatedSupplier
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .address(UPDATED_ADDRESS)
            .noTelp(UPDATED_NO_TELP)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .bankName(UPDATED_BANK_NAME);
        SupplierDTO supplierDTO = supplierMapper.toDto(updatedSupplier);

        restSupplierMockMvc.perform(put("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplier.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSupplier.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSupplier.getNoTelp()).isEqualTo(UPDATED_NO_TELP);
        assertThat(testSupplier.getBankAccount()).isEqualTo(UPDATED_BANK_ACCOUNT);
        assertThat(testSupplier.getBankName()).isEqualTo(UPDATED_BANK_NAME);

        // Validate the Supplier in Elasticsearch
        verify(mockSupplierSearchRepository, times(1)).save(testSupplier);
    }

    @Test
    @Transactional
    public void updateNonExistingSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc.perform(put("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Supplier in Elasticsearch
        verify(mockSupplierSearchRepository, times(0)).save(supplier);
    }

    @Test
    @Transactional
    public void deleteSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeDelete = supplierRepository.findAll().size();

        // Delete the supplier
        restSupplierMockMvc.perform(delete("/api/suppliers/{id}", supplier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Supplier in Elasticsearch
        verify(mockSupplierSearchRepository, times(1)).deleteById(supplier.getId());
    }

    @Test
    @Transactional
    public void searchSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);
        when(mockSupplierSearchRepository.search(queryStringQuery("id:" + supplier.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(supplier), PageRequest.of(0, 1), 1));
        // Search the supplier
        restSupplierMockMvc.perform(get("/api/_search/suppliers?query=id:" + supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].noTelp").value(hasItem(DEFAULT_NO_TELP)))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Supplier.class);
        Supplier supplier1 = new Supplier();
        supplier1.setId(1L);
        Supplier supplier2 = new Supplier();
        supplier2.setId(supplier1.getId());
        assertThat(supplier1).isEqualTo(supplier2);
        supplier2.setId(2L);
        assertThat(supplier1).isNotEqualTo(supplier2);
        supplier1.setId(null);
        assertThat(supplier1).isNotEqualTo(supplier2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierDTO.class);
        SupplierDTO supplierDTO1 = new SupplierDTO();
        supplierDTO1.setId(1L);
        SupplierDTO supplierDTO2 = new SupplierDTO();
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
        supplierDTO2.setId(supplierDTO1.getId());
        assertThat(supplierDTO1).isEqualTo(supplierDTO2);
        supplierDTO2.setId(2L);
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
        supplierDTO1.setId(null);
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(supplierMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(supplierMapper.fromId(null)).isNull();
    }
}
