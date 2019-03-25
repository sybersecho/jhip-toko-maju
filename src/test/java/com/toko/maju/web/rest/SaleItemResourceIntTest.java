package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.SaleItem;
import com.toko.maju.domain.SaleTransactions;
import com.toko.maju.domain.Product;
import com.toko.maju.repository.SaleItemRepository;
import com.toko.maju.repository.search.SaleItemSearchRepository;
import com.toko.maju.service.SaleItemService;
import com.toko.maju.service.dto.SaleItemDTO;
import com.toko.maju.service.mapper.SaleItemMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.SaleItemCriteria;
import com.toko.maju.service.SaleItemQueryService;

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
 * Test class for the SaleItemResource REST controller.
 *
 * @see SaleItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class SaleItemResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private SaleItemMapper saleItemMapper;

    @Autowired
    private SaleItemService saleItemService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.SaleItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private SaleItemSearchRepository mockSaleItemSearchRepository;

    @Autowired
    private SaleItemQueryService saleItemQueryService;

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

    private MockMvc restSaleItemMockMvc;

    private SaleItem saleItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaleItemResource saleItemResource = new SaleItemResource(saleItemService, saleItemQueryService);
        this.restSaleItemMockMvc = MockMvcBuilders.standaloneSetup(saleItemResource)
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
    public static SaleItem createEntity(EntityManager em) {
        SaleItem saleItem = new SaleItem()
            .quantity(DEFAULT_QUANTITY)
            .totalPrice(DEFAULT_TOTAL_PRICE);
        // Add required entity
        SaleTransactions saleTransactions = SaleTransactionsResourceIntTest.createEntity(em);
        em.persist(saleTransactions);
        em.flush();
        saleItem.setSale(saleTransactions);
        // Add required entity
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        saleItem.setProduct(product);
        return saleItem;
    }

    @Before
    public void initTest() {
        saleItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaleItem() throws Exception {
        int databaseSizeBeforeCreate = saleItemRepository.findAll().size();

        // Create the SaleItem
        SaleItemDTO saleItemDTO = saleItemMapper.toDto(saleItem);
        restSaleItemMockMvc.perform(post("/api/sale-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleItemDTO)))
            .andExpect(status().isCreated());

        // Validate the SaleItem in the database
        List<SaleItem> saleItemList = saleItemRepository.findAll();
        assertThat(saleItemList).hasSize(databaseSizeBeforeCreate + 1);
        SaleItem testSaleItem = saleItemList.get(saleItemList.size() - 1);
        assertThat(testSaleItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSaleItem.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);

        // Validate the SaleItem in Elasticsearch
        verify(mockSaleItemSearchRepository, times(1)).save(testSaleItem);
    }

    @Test
    @Transactional
    public void createSaleItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saleItemRepository.findAll().size();

        // Create the SaleItem with an existing ID
        saleItem.setId(1L);
        SaleItemDTO saleItemDTO = saleItemMapper.toDto(saleItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleItemMockMvc.perform(post("/api/sale-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SaleItem in the database
        List<SaleItem> saleItemList = saleItemRepository.findAll();
        assertThat(saleItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the SaleItem in Elasticsearch
        verify(mockSaleItemSearchRepository, times(0)).save(saleItem);
    }

    @Test
    @Transactional
    public void getAllSaleItems() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        // Get all the saleItemList
        restSaleItemMockMvc.perform(get("/api/sale-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())));
    }
    
    @Test
    @Transactional
    public void getSaleItem() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        // Get the saleItem
        restSaleItemMockMvc.perform(get("/api/sale-items/{id}", saleItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saleItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getAllSaleItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        // Get all the saleItemList where quantity equals to DEFAULT_QUANTITY
        defaultSaleItemShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the saleItemList where quantity equals to UPDATED_QUANTITY
        defaultSaleItemShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllSaleItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        // Get all the saleItemList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultSaleItemShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the saleItemList where quantity equals to UPDATED_QUANTITY
        defaultSaleItemShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllSaleItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        // Get all the saleItemList where quantity is not null
        defaultSaleItemShouldBeFound("quantity.specified=true");

        // Get all the saleItemList where quantity is null
        defaultSaleItemShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        // Get all the saleItemList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultSaleItemShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the saleItemList where quantity greater than or equals to UPDATED_QUANTITY
        defaultSaleItemShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllSaleItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        // Get all the saleItemList where quantity less than or equals to DEFAULT_QUANTITY
        defaultSaleItemShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the saleItemList where quantity less than or equals to UPDATED_QUANTITY
        defaultSaleItemShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllSaleItemsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        // Get all the saleItemList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultSaleItemShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the saleItemList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultSaleItemShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllSaleItemsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        // Get all the saleItemList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultSaleItemShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the saleItemList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultSaleItemShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllSaleItemsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        // Get all the saleItemList where totalPrice is not null
        defaultSaleItemShouldBeFound("totalPrice.specified=true");

        // Get all the saleItemList where totalPrice is null
        defaultSaleItemShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleItemsBySaleIsEqualToSomething() throws Exception {
        // Initialize the database
        SaleTransactions sale = SaleTransactionsResourceIntTest.createEntity(em);
        em.persist(sale);
        em.flush();
        saleItem.setSale(sale);
        saleItemRepository.saveAndFlush(saleItem);
        Long saleId = sale.getId();

        // Get all the saleItemList where sale equals to saleId
        defaultSaleItemShouldBeFound("saleId.equals=" + saleId);

        // Get all the saleItemList where sale equals to saleId + 1
        defaultSaleItemShouldNotBeFound("saleId.equals=" + (saleId + 1));
    }


    @Test
    @Transactional
    public void getAllSaleItemsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        saleItem.setProduct(product);
        saleItemRepository.saveAndFlush(saleItem);
        Long productId = product.getId();

        // Get all the saleItemList where product equals to productId
        defaultSaleItemShouldBeFound("productId.equals=" + productId);

        // Get all the saleItemList where product equals to productId + 1
        defaultSaleItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSaleItemShouldBeFound(String filter) throws Exception {
        restSaleItemMockMvc.perform(get("/api/sale-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())));

        // Check, that the count call also returns 1
        restSaleItemMockMvc.perform(get("/api/sale-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSaleItemShouldNotBeFound(String filter) throws Exception {
        restSaleItemMockMvc.perform(get("/api/sale-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSaleItemMockMvc.perform(get("/api/sale-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSaleItem() throws Exception {
        // Get the saleItem
        restSaleItemMockMvc.perform(get("/api/sale-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaleItem() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        int databaseSizeBeforeUpdate = saleItemRepository.findAll().size();

        // Update the saleItem
        SaleItem updatedSaleItem = saleItemRepository.findById(saleItem.getId()).get();
        // Disconnect from session so that the updates on updatedSaleItem are not directly saved in db
        em.detach(updatedSaleItem);
        updatedSaleItem
            .quantity(UPDATED_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE);
        SaleItemDTO saleItemDTO = saleItemMapper.toDto(updatedSaleItem);

        restSaleItemMockMvc.perform(put("/api/sale-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleItemDTO)))
            .andExpect(status().isOk());

        // Validate the SaleItem in the database
        List<SaleItem> saleItemList = saleItemRepository.findAll();
        assertThat(saleItemList).hasSize(databaseSizeBeforeUpdate);
        SaleItem testSaleItem = saleItemList.get(saleItemList.size() - 1);
        assertThat(testSaleItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSaleItem.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);

        // Validate the SaleItem in Elasticsearch
        verify(mockSaleItemSearchRepository, times(1)).save(testSaleItem);
    }

    @Test
    @Transactional
    public void updateNonExistingSaleItem() throws Exception {
        int databaseSizeBeforeUpdate = saleItemRepository.findAll().size();

        // Create the SaleItem
        SaleItemDTO saleItemDTO = saleItemMapper.toDto(saleItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleItemMockMvc.perform(put("/api/sale-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SaleItem in the database
        List<SaleItem> saleItemList = saleItemRepository.findAll();
        assertThat(saleItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SaleItem in Elasticsearch
        verify(mockSaleItemSearchRepository, times(0)).save(saleItem);
    }

    @Test
    @Transactional
    public void deleteSaleItem() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);

        int databaseSizeBeforeDelete = saleItemRepository.findAll().size();

        // Delete the saleItem
        restSaleItemMockMvc.perform(delete("/api/sale-items/{id}", saleItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SaleItem> saleItemList = saleItemRepository.findAll();
        assertThat(saleItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SaleItem in Elasticsearch
        verify(mockSaleItemSearchRepository, times(1)).deleteById(saleItem.getId());
    }

    @Test
    @Transactional
    public void searchSaleItem() throws Exception {
        // Initialize the database
        saleItemRepository.saveAndFlush(saleItem);
        when(mockSaleItemSearchRepository.search(queryStringQuery("id:" + saleItem.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(saleItem), PageRequest.of(0, 1), 1));
        // Search the saleItem
        restSaleItemMockMvc.perform(get("/api/_search/sale-items?query=id:" + saleItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleItem.class);
        SaleItem saleItem1 = new SaleItem();
        saleItem1.setId(1L);
        SaleItem saleItem2 = new SaleItem();
        saleItem2.setId(saleItem1.getId());
        assertThat(saleItem1).isEqualTo(saleItem2);
        saleItem2.setId(2L);
        assertThat(saleItem1).isNotEqualTo(saleItem2);
        saleItem1.setId(null);
        assertThat(saleItem1).isNotEqualTo(saleItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleItemDTO.class);
        SaleItemDTO saleItemDTO1 = new SaleItemDTO();
        saleItemDTO1.setId(1L);
        SaleItemDTO saleItemDTO2 = new SaleItemDTO();
        assertThat(saleItemDTO1).isNotEqualTo(saleItemDTO2);
        saleItemDTO2.setId(saleItemDTO1.getId());
        assertThat(saleItemDTO1).isEqualTo(saleItemDTO2);
        saleItemDTO2.setId(2L);
        assertThat(saleItemDTO1).isNotEqualTo(saleItemDTO2);
        saleItemDTO1.setId(null);
        assertThat(saleItemDTO1).isNotEqualTo(saleItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(saleItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(saleItemMapper.fromId(null)).isNull();
    }
}
