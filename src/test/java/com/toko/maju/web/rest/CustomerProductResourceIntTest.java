package com.toko.maju.web.rest;

import static com.toko.maju.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import com.toko.maju.JhiptokomajuApp;
import com.toko.maju.domain.Customer;
import com.toko.maju.domain.CustomerProduct;
import com.toko.maju.domain.enumeration.Gender;
import com.toko.maju.repository.CustomerProductRepository;
import com.toko.maju.repository.search.CustomerProductSearchRepository;
import com.toko.maju.service.CustomerProductService;
import com.toko.maju.service.dto.CustomerDTO;
import com.toko.maju.service.dto.CustomerProductDTO;
import com.toko.maju.service.mapper.CustomerMapper;
import com.toko.maju.service.mapper.CustomerProductMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CustomerProductResource REST controller.
 *
 * @see CustomerProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class CustomerProductResourceIntTest {

    private static final BigDecimal DEFAULT_SPECIAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_SPECIAL_PRICE = new BigDecimal(1);

    @Autowired
    private CustomerProductRepository customerProductRepository;

    @Autowired
    private CustomerProductMapper customerProductMapper;
    
    @Autowired
    private CustomerMapper customerMapper;

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
    
    private Customer customer;
    
    private static List<CustomerProduct> customerProducts = new ArrayList<CustomerProduct>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerProductResource customerProductResource = new CustomerProductResource(customerProductService);
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
        CustomerProduct customerProduct = new CustomerProduct()
            .specialPrice(DEFAULT_SPECIAL_PRICE);
        return customerProduct;
    }
    
    public static List<CustomerProduct> createEntities(EntityManager em) {
    	customerProducts = new ArrayList<CustomerProduct>();
    	Customer customer = createCustomer(em);
    	
    	CustomerProduct firstCustomerProduct = new CustomerProduct()
                .specialPrice(DEFAULT_SPECIAL_PRICE);
    	firstCustomerProduct.setCustomer(customer);
    	
    	CustomerProduct secondcustomerProduct = new CustomerProduct()
                .specialPrice(UPDATED_SPECIAL_PRICE);
    	secondcustomerProduct.setCustomer(customer);
    	
    	customerProducts.add(firstCustomerProduct);
    	customerProducts.add(secondcustomerProduct);
    	
    	return customerProducts;
    	
    }
    
    public static Customer createCustomer(EntityManager em) {
    	Customer customer = new Customer();
    	customer.setId(1L);;
    	customer.setCode("Code 1");
    	customer.setAddress("Address");
    	customer.setFirstName("Fransisko");
    	customer.setGender(Gender.MALE);
    	customer.setLastName("Sanaky");
    	customer.setPhoneNumber("0911347655");
    	
    	return customer;
    }

    @Before
    public void initTest() {
        customerProduct = createEntity(em);
        customerProducts = createEntities(em);
        
        customer = createCustomer(em);
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
        assertThat(testCustomerProduct.getSpecialPrice()).isEqualTo(DEFAULT_SPECIAL_PRICE);

        // Validate the CustomerProduct in Elasticsearch
        verify(mockCustomerProductSearchRepository, times(1)).save(testCustomerProduct);
    }
    
    @Test
    @Transactional
    public void createCustomerProducts() throws Exception{
    	int databaseSizeBeforeCreate = customerProductRepository.findAll().size();
    	
    	// Create the CustomerProduct
        List<CustomerProductDTO> customerProductDTOs = customerProductMapper.toDto(customerProducts);
        customerProductDTOs.forEach(dto -> {
        	System.out.println(dto.getSpecialPrice() == null ? "special price is null" : dto.getSpecialPrice() + "");
        });
        restCustomerProductMockMvc.perform(put("/api/customer-products/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProductDTOs)))
            .andExpect(status().isCreated());
        
     // Validate the CustomerProduct in the database
        List<CustomerProduct> customerProductList = customerProductRepository.findAll();
        assertThat(customerProductList).hasSize(databaseSizeBeforeCreate + 2);
        CustomerProduct testCustomerProduct = customerProductList.get(customerProductList.size() - 1);
        assertThat(testCustomerProduct.getSpecialPrice()).isEqualTo(UPDATED_SPECIAL_PRICE);
    	
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
    public void checkSpecialPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerProductRepository.findAll().size();
        // set the field null
        customerProduct.setSpecialPrice(null);

        // Create the CustomerProduct, which fails.
        CustomerProductDTO customerProductDTO = customerProductMapper.toDto(customerProduct);

        restCustomerProductMockMvc.perform(post("/api/customer-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProductDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerProduct> customerProductList = customerProductRepository.findAll();
        assertThat(customerProductList).hasSize(databaseSizeBeforeTest);
    }
    
//    @Test
//    @Transactional
    public void getByExistingCustomer() throws Exception {
        // Initialize the database
//    	custo
        customerProductRepository.saveAll(customerProducts);
        System.out.println("get customer");
        Customer cust = customerProductRepository.findAll().get(0).getCustomer();
        
//        CustomerDTO customertDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerProductMockMvc.perform(get("/api/customer-products/customers/{id}",Long.MAX_VALUE)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(customertDTO)))
            ).andExpect(status().isOk());
//            .andExpect(jsonPath("$.[*].id").value(hasItem(customerProduct.getId().intValue())))
//            .andExpect(jsonPath("$.[*].specialPrice").value(hasItem(DEFAULT_SPECIAL_PRICE.intValue())));
//
//        // Get all the customerProductList
//        restCustomerProductMockMvc.perform(get("/api/customer-products/customers"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(customerProduct.getId().intValue())))
//            .andExpect(jsonPath("$.[*].specialPrice").value(hasItem(DEFAULT_SPECIAL_PRICE.intValue())));
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].specialPrice").value(hasItem(DEFAULT_SPECIAL_PRICE.intValue())));
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
            .andExpect(jsonPath("$.id").value(customerProduct.getId().intValue()))
            .andExpect(jsonPath("$.specialPrice").value(DEFAULT_SPECIAL_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerProduct() throws Exception {
        // Get the customerProduct
        restCustomerProductMockMvc.perform(get("/api/customer-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
    
//    @Test
//    @Transactional
    public void getNonExistingCustomer() throws Exception {//
         // Create the CustomerProduct
//         CustomerDTO customertDTO = customerMapper.toDto(new Customer());

         // If the entity doesn't have an ID, it will throw BadRequestAlertException
         restCustomerProductMockMvc.perform(get("/api/customer-products/customers/{id}", Long.MAX_VALUE)
//             .contentType(TestUtil.APPLICATION_JSON_UTF8)
//             .content(TestUtil.convertObjectToJsonBytes(customertDTO)))
             ).andExpect(status().isBadRequest());
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
        updatedCustomerProduct
            .specialPrice(UPDATED_SPECIAL_PRICE);
        CustomerProductDTO customerProductDTO = customerProductMapper.toDto(updatedCustomerProduct);

        restCustomerProductMockMvc.perform(put("/api/customer-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProductDTO)))
            .andExpect(status().isOk());

        // Validate the CustomerProduct in the database
        List<CustomerProduct> customerProductList = customerProductRepository.findAll();
        assertThat(customerProductList).hasSize(databaseSizeBeforeUpdate);
        CustomerProduct testCustomerProduct = customerProductList.get(customerProductList.size() - 1);
        assertThat(testCustomerProduct.getSpecialPrice()).isEqualTo(UPDATED_SPECIAL_PRICE);

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
        when(mockCustomerProductSearchRepository.search(queryStringQuery("id:" + customerProduct.getId())))
            .thenReturn(Collections.singletonList(customerProduct));
        // Search the customerProduct
        restCustomerProductMockMvc.perform(get("/api/_search/customer-products?query=id:" + customerProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].specialPrice").value(hasItem(DEFAULT_SPECIAL_PRICE.intValue())));
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
