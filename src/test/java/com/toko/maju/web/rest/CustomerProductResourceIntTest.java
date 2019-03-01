package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.CustomerProduct;
import com.toko.maju.repository.CustomerProductRepository;
import com.toko.maju.repository.search.CustomerProductSearchRepository;
import com.toko.maju.service.CustomerProductService;
import com.toko.maju.service.dto.CustomerProductDTO;
import com.toko.maju.service.mapper.CustomerProductMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.CustomerProductCriteria;
import com.toko.maju.service.CustomerProductQueryService;

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
 * Test class for the CustomerProductResource REST controller.
 *
 * @see CustomerProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class CustomerProductResourceIntTest {

    @Autowired
    private CustomerProductRepository customerProductRepository;

    @Autowired
    private CustomerProductMapper customerProductMapper;

    @Autowired
    private CustomerProductService customerProductService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.CustomerProductSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomerProductSearchRepository mockCustomerProductSearchRepository;

    @Autowired
    private CustomerProductQueryService customerProductQueryService;

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

    private MockMvc restCustomerProductMockMvc;

    private CustomerProduct customerProduct;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerProductResource customerProductResource = new CustomerProductResource(customerProductService, customerProductQueryService);
        this.restCustomerProductMockMvc = MockMvcBuilders.standaloneSetup(customerProductResource)
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
    public static CustomerProduct createEntity(EntityManager em) {
        CustomerProduct customerProduct = new CustomerProduct();
        return customerProduct;
    }

    @Before
    public void initTest() {
        customerProduct = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerProduct() throws Exception {
        int databaseSizeBeforeCreate = customerProductRepository.findAll().size();

        // Create the CustomerProduct
        CustomerProductDTO customerProductDTO = customerProductMapper.toDto(customerProduct);
        restCustomerProductMockMvc.perform(post("/api/customer-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProductDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerProduct in the database
        List<CustomerProduct> customerProductList = customerProductRepository.findAll();
        assertThat(customerProductList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerProduct testCustomerProduct = customerProductList.get(customerProductList.size() - 1);

        // Validate the CustomerProduct in Elasticsearch
        verify(mockCustomerProductSearchRepository, times(1)).save(testCustomerProduct);
    }

    @Test
    @Transactional
    public void createCustomerProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerProductRepository.findAll().size();

        // Create the CustomerProduct with an existing ID
        customerProduct.setId(1L);
        CustomerProductDTO customerProductDTO = customerProductMapper.toDto(customerProduct);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerProductMockMvc.perform(post("/api/customer-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerProduct in the database
        List<CustomerProduct> customerProductList = customerProductRepository.findAll();
        assertThat(customerProductList).hasSize(databaseSizeBeforeCreate);

        // Validate the CustomerProduct in Elasticsearch
        verify(mockCustomerProductSearchRepository, times(0)).save(customerProduct);
    }

    @Test
    @Transactional
    public void getAllCustomerProducts() throws Exception {
        // Initialize the database
        customerProductRepository.saveAndFlush(customerProduct);

        // Get all the customerProductList
        restCustomerProductMockMvc.perform(get("/api/customer-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerProduct.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getCustomerProduct() throws Exception {
        // Initialize the database
        customerProductRepository.saveAndFlush(customerProduct);

        // Get the customerProduct
        restCustomerProductMockMvc.perform(get("/api/customer-products/{id}", customerProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerProduct.getId().intValue()));
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCustomerProductShouldBeFound(String filter) throws Exception {
        restCustomerProductMockMvc.perform(get("/api/customer-products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerProduct.getId().intValue())));

        // Check, that the count call also returns 1
        restCustomerProductMockMvc.perform(get("/api/customer-products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCustomerProductShouldNotBeFound(String filter) throws Exception {
        restCustomerProductMockMvc.perform(get("/api/customer-products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerProductMockMvc.perform(get("/api/customer-products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCustomerProduct() throws Exception {
        // Get the customerProduct
        restCustomerProductMockMvc.perform(get("/api/customer-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerProduct() throws Exception {
        // Initialize the database
        customerProductRepository.saveAndFlush(customerProduct);

        int databaseSizeBeforeUpdate = customerProductRepository.findAll().size();

        // Update the customerProduct
        CustomerProduct updatedCustomerProduct = customerProductRepository.findById(customerProduct.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerProduct are not directly saved in db
        em.detach(updatedCustomerProduct);
        CustomerProductDTO customerProductDTO = customerProductMapper.toDto(updatedCustomerProduct);

        restCustomerProductMockMvc.perform(put("/api/customer-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProductDTO)))
            .andExpect(status().isOk());

        // Validate the CustomerProduct in the database
        List<CustomerProduct> customerProductList = customerProductRepository.findAll();
        assertThat(customerProductList).hasSize(databaseSizeBeforeUpdate);
        CustomerProduct testCustomerProduct = customerProductList.get(customerProductList.size() - 1);

        // Validate the CustomerProduct in Elasticsearch
        verify(mockCustomerProductSearchRepository, times(1)).save(testCustomerProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerProduct() throws Exception {
        int databaseSizeBeforeUpdate = customerProductRepository.findAll().size();

        // Create the CustomerProduct
        CustomerProductDTO customerProductDTO = customerProductMapper.toDto(customerProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerProductMockMvc.perform(put("/api/customer-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerProduct in the database
        List<CustomerProduct> customerProductList = customerProductRepository.findAll();
        assertThat(customerProductList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerProduct in Elasticsearch
        verify(mockCustomerProductSearchRepository, times(0)).save(customerProduct);
    }

    @Test
    @Transactional
    public void deleteCustomerProduct() throws Exception {
        // Initialize the database
        customerProductRepository.saveAndFlush(customerProduct);

        int databaseSizeBeforeDelete = customerProductRepository.findAll().size();

        // Delete the customerProduct
        restCustomerProductMockMvc.perform(delete("/api/customer-products/{id}", customerProduct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerProduct> customerProductList = customerProductRepository.findAll();
        assertThat(customerProductList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CustomerProduct in Elasticsearch
        verify(mockCustomerProductSearchRepository, times(1)).deleteById(customerProduct.getId());
    }

    @Test
    @Transactional
    public void searchCustomerProduct() throws Exception {
        // Initialize the database
        customerProductRepository.saveAndFlush(customerProduct);
        when(mockCustomerProductSearchRepository.search(queryStringQuery("id:" + customerProduct.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(customerProduct), PageRequest.of(0, 1), 1));
        // Search the customerProduct
        restCustomerProductMockMvc.perform(get("/api/_search/customer-products?query=id:" + customerProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerProduct.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerProduct.class);
        CustomerProduct customerProduct1 = new CustomerProduct();
        customerProduct1.setId(1L);
        CustomerProduct customerProduct2 = new CustomerProduct();
        customerProduct2.setId(customerProduct1.getId());
        assertThat(customerProduct1).isEqualTo(customerProduct2);
        customerProduct2.setId(2L);
        assertThat(customerProduct1).isNotEqualTo(customerProduct2);
        customerProduct1.setId(null);
        assertThat(customerProduct1).isNotEqualTo(customerProduct2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerProductDTO.class);
        CustomerProductDTO customerProductDTO1 = new CustomerProductDTO();
        customerProductDTO1.setId(1L);
        CustomerProductDTO customerProductDTO2 = new CustomerProductDTO();
        assertThat(customerProductDTO1).isNotEqualTo(customerProductDTO2);
        customerProductDTO2.setId(customerProductDTO1.getId());
        assertThat(customerProductDTO1).isEqualTo(customerProductDTO2);
        customerProductDTO2.setId(2L);
        assertThat(customerProductDTO1).isNotEqualTo(customerProductDTO2);
        customerProductDTO1.setId(null);
        assertThat(customerProductDTO1).isNotEqualTo(customerProductDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customerProductMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customerProductMapper.fromId(null)).isNull();
    }
}
