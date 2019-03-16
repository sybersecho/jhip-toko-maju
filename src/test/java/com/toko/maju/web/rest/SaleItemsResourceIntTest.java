package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.SaleItems;
import com.toko.maju.domain.Product;
import com.toko.maju.repository.SaleItemsRepository;
import com.toko.maju.repository.search.SaleItemsSearchRepository;
import com.toko.maju.web.rest.errors.ExceptionTranslator;

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
 * Test class for the SaleItemsResource REST controller.
 *
 * @see SaleItemsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class SaleItemsResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);

    @Autowired
    private SaleItemsRepository saleItemsRepository;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.SaleItemsSearchRepositoryMockConfiguration
     */
    @Autowired
    private SaleItemsSearchRepository mockSaleItemsSearchRepository;

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

    private MockMvc restSaleItemsMockMvc;

    private SaleItems saleItems;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaleItemsResource saleItemsResource = new SaleItemsResource(saleItemsRepository, mockSaleItemsSearchRepository);
        this.restSaleItemsMockMvc = MockMvcBuilders.standaloneSetup(saleItemsResource)
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
    public static SaleItems createEntity(EntityManager em) {
        SaleItems saleItems = new SaleItems()
            .quantity(DEFAULT_QUANTITY)
            .totalPrice(DEFAULT_TOTAL_PRICE);
        // Add required entity
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        saleItems.setProduct(product);
        return saleItems;
    }

    @Before
    public void initTest() {
        saleItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaleItems() throws Exception {
        int databaseSizeBeforeCreate = saleItemsRepository.findAll().size();

        // Create the SaleItems
        restSaleItemsMockMvc.perform(post("/api/sale-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleItems)))
            .andExpect(status().isCreated());

        // Validate the SaleItems in the database
        List<SaleItems> saleItemsList = saleItemsRepository.findAll();
        assertThat(saleItemsList).hasSize(databaseSizeBeforeCreate + 1);
        SaleItems testSaleItems = saleItemsList.get(saleItemsList.size() - 1);
        assertThat(testSaleItems.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSaleItems.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);

        // Validate the SaleItems in Elasticsearch
        verify(mockSaleItemsSearchRepository, times(1)).save(testSaleItems);
    }

    @Test
    @Transactional
    public void createSaleItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saleItemsRepository.findAll().size();

        // Create the SaleItems with an existing ID
        saleItems.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleItemsMockMvc.perform(post("/api/sale-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleItems)))
            .andExpect(status().isBadRequest());

        // Validate the SaleItems in the database
        List<SaleItems> saleItemsList = saleItemsRepository.findAll();
        assertThat(saleItemsList).hasSize(databaseSizeBeforeCreate);

        // Validate the SaleItems in Elasticsearch
        verify(mockSaleItemsSearchRepository, times(0)).save(saleItems);
    }

    @Test
    @Transactional
    public void getAllSaleItems() throws Exception {
        // Initialize the database
        saleItemsRepository.saveAndFlush(saleItems);

        // Get all the saleItemsList
        restSaleItemsMockMvc.perform(get("/api/sale-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())));
    }
    
    @Test
    @Transactional
    public void getSaleItems() throws Exception {
        // Initialize the database
        saleItemsRepository.saveAndFlush(saleItems);

        // Get the saleItems
        restSaleItemsMockMvc.perform(get("/api/sale-items/{id}", saleItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saleItems.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSaleItems() throws Exception {
        // Get the saleItems
        restSaleItemsMockMvc.perform(get("/api/sale-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaleItems() throws Exception {
        // Initialize the database
        saleItemsRepository.saveAndFlush(saleItems);

        int databaseSizeBeforeUpdate = saleItemsRepository.findAll().size();

        // Update the saleItems
        SaleItems updatedSaleItems = saleItemsRepository.findById(saleItems.getId()).get();
        // Disconnect from session so that the updates on updatedSaleItems are not directly saved in db
        em.detach(updatedSaleItems);
        updatedSaleItems
            .quantity(UPDATED_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE);

        restSaleItemsMockMvc.perform(put("/api/sale-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSaleItems)))
            .andExpect(status().isOk());

        // Validate the SaleItems in the database
        List<SaleItems> saleItemsList = saleItemsRepository.findAll();
        assertThat(saleItemsList).hasSize(databaseSizeBeforeUpdate);
        SaleItems testSaleItems = saleItemsList.get(saleItemsList.size() - 1);
        assertThat(testSaleItems.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSaleItems.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);

        // Validate the SaleItems in Elasticsearch
        verify(mockSaleItemsSearchRepository, times(1)).save(testSaleItems);
    }

    @Test
    @Transactional
    public void updateNonExistingSaleItems() throws Exception {
        int databaseSizeBeforeUpdate = saleItemsRepository.findAll().size();

        // Create the SaleItems

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleItemsMockMvc.perform(put("/api/sale-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleItems)))
            .andExpect(status().isBadRequest());

        // Validate the SaleItems in the database
        List<SaleItems> saleItemsList = saleItemsRepository.findAll();
        assertThat(saleItemsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SaleItems in Elasticsearch
        verify(mockSaleItemsSearchRepository, times(0)).save(saleItems);
    }

    @Test
    @Transactional
    public void deleteSaleItems() throws Exception {
        // Initialize the database
        saleItemsRepository.saveAndFlush(saleItems);

        int databaseSizeBeforeDelete = saleItemsRepository.findAll().size();

        // Delete the saleItems
        restSaleItemsMockMvc.perform(delete("/api/sale-items/{id}", saleItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SaleItems> saleItemsList = saleItemsRepository.findAll();
        assertThat(saleItemsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SaleItems in Elasticsearch
        verify(mockSaleItemsSearchRepository, times(1)).deleteById(saleItems.getId());
    }

    @Test
    @Transactional
    public void searchSaleItems() throws Exception {
        // Initialize the database
        saleItemsRepository.saveAndFlush(saleItems);
        when(mockSaleItemsSearchRepository.search(queryStringQuery("id:" + saleItems.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(saleItems), PageRequest.of(0, 1), 1));
        // Search the saleItems
        restSaleItemsMockMvc.perform(get("/api/_search/sale-items?query=id:" + saleItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleItems.class);
        SaleItems saleItems1 = new SaleItems();
        saleItems1.setId(1L);
        SaleItems saleItems2 = new SaleItems();
        saleItems2.setId(saleItems1.getId());
        assertThat(saleItems1).isEqualTo(saleItems2);
        saleItems2.setId(2L);
        assertThat(saleItems1).isNotEqualTo(saleItems2);
        saleItems1.setId(null);
        assertThat(saleItems1).isNotEqualTo(saleItems2);
    }
}
