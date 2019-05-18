package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.StockOrder;
import com.toko.maju.domain.User;
import com.toko.maju.repository.StockOrderRepository;
import com.toko.maju.repository.search.StockOrderSearchRepository;
import com.toko.maju.service.StockOrderService;
import com.toko.maju.service.dto.StockOrderDTO;
import com.toko.maju.service.mapper.StockOrderMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.StockOrderCriteria;
import com.toko.maju.service.StockOrderQueryService;

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
 * Test class for the StockOrderResource REST controller.
 *
 * @see StockOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class StockOrderResourceIntTest {

    private static final String DEFAULT_SITE_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_SITE_LOCATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PROCESSED = false;
    private static final Boolean UPDATED_PROCESSED = true;

    private static final Instant DEFAULT_PROCESSED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private StockOrderRepository stockOrderRepository;

    @Autowired
    private StockOrderMapper stockOrderMapper;

    @Autowired
    private StockOrderService stockOrderService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.StockOrderSearchRepositoryMockConfiguration
     */
    @Autowired
    private StockOrderSearchRepository mockStockOrderSearchRepository;

    @Autowired
    private StockOrderQueryService stockOrderQueryService;

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

    private MockMvc restStockOrderMockMvc;

    private StockOrder stockOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockOrderResource stockOrderResource = new StockOrderResource(stockOrderService, stockOrderQueryService);
        this.restStockOrderMockMvc = MockMvcBuilders.standaloneSetup(stockOrderResource)
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
    public static StockOrder createEntity(EntityManager em) {
        StockOrder stockOrder = new StockOrder()
            .siteLocation(DEFAULT_SITE_LOCATION)
            .createdDate(DEFAULT_CREATED_DATE)
            .processed(DEFAULT_PROCESSED)
            .processedDate(DEFAULT_PROCESSED_DATE);
        return stockOrder;
    }

    @Before
    public void initTest() {
        stockOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOrder() throws Exception {
        int databaseSizeBeforeCreate = stockOrderRepository.findAll().size();

        // Create the StockOrder
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);
        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeCreate + 1);
        StockOrder testStockOrder = stockOrderList.get(stockOrderList.size() - 1);
        assertThat(testStockOrder.getSiteLocation()).isEqualTo(DEFAULT_SITE_LOCATION);
        assertThat(testStockOrder.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testStockOrder.isProcessed()).isEqualTo(DEFAULT_PROCESSED);
        assertThat(testStockOrder.getProcessedDate()).isEqualTo(DEFAULT_PROCESSED_DATE);

        // Validate the StockOrder in Elasticsearch
        verify(mockStockOrderSearchRepository, times(1)).save(testStockOrder);
    }

    @Test
    @Transactional
    public void createStockOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOrderRepository.findAll().size();

        // Create the StockOrder with an existing ID
        stockOrder.setId(1L);
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeCreate);

        // Validate the StockOrder in Elasticsearch
        verify(mockStockOrderSearchRepository, times(0)).save(stockOrder);
    }

    @Test
    @Transactional
    public void checkSiteLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOrderRepository.findAll().size();
        // set the field null
        stockOrder.setSiteLocation(null);

        // Create the StockOrder, which fails.
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOrderRepository.findAll().size();
        // set the field null
        stockOrder.setCreatedDate(null);

        // Create the StockOrder, which fails.
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProcessedIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOrderRepository.findAll().size();
        // set the field null
        stockOrder.setProcessed(null);

        // Create the StockOrder, which fails.
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOrders() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList
        restStockOrderMockMvc.perform(get("/api/stock-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].siteLocation").value(hasItem(DEFAULT_SITE_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getStockOrder() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get the stockOrder
        restStockOrderMockMvc.perform(get("/api/stock-orders/{id}", stockOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOrder.getId().intValue()))
            .andExpect(jsonPath("$.siteLocation").value(DEFAULT_SITE_LOCATION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.processed").value(DEFAULT_PROCESSED.booleanValue()))
            .andExpect(jsonPath("$.processedDate").value(DEFAULT_PROCESSED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllStockOrdersBySiteLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where siteLocation equals to DEFAULT_SITE_LOCATION
        defaultStockOrderShouldBeFound("siteLocation.equals=" + DEFAULT_SITE_LOCATION);

        // Get all the stockOrderList where siteLocation equals to UPDATED_SITE_LOCATION
        defaultStockOrderShouldNotBeFound("siteLocation.equals=" + UPDATED_SITE_LOCATION);
    }

    @Test
    @Transactional
    public void getAllStockOrdersBySiteLocationIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where siteLocation in DEFAULT_SITE_LOCATION or UPDATED_SITE_LOCATION
        defaultStockOrderShouldBeFound("siteLocation.in=" + DEFAULT_SITE_LOCATION + "," + UPDATED_SITE_LOCATION);

        // Get all the stockOrderList where siteLocation equals to UPDATED_SITE_LOCATION
        defaultStockOrderShouldNotBeFound("siteLocation.in=" + UPDATED_SITE_LOCATION);
    }

    @Test
    @Transactional
    public void getAllStockOrdersBySiteLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where siteLocation is not null
        defaultStockOrderShouldBeFound("siteLocation.specified=true");

        // Get all the stockOrderList where siteLocation is null
        defaultStockOrderShouldNotBeFound("siteLocation.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrdersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where createdDate equals to DEFAULT_CREATED_DATE
        defaultStockOrderShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the stockOrderList where createdDate equals to UPDATED_CREATED_DATE
        defaultStockOrderShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllStockOrdersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultStockOrderShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the stockOrderList where createdDate equals to UPDATED_CREATED_DATE
        defaultStockOrderShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllStockOrdersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where createdDate is not null
        defaultStockOrderShouldBeFound("createdDate.specified=true");

        // Get all the stockOrderList where createdDate is null
        defaultStockOrderShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrdersByProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where processed equals to DEFAULT_PROCESSED
        defaultStockOrderShouldBeFound("processed.equals=" + DEFAULT_PROCESSED);

        // Get all the stockOrderList where processed equals to UPDATED_PROCESSED
        defaultStockOrderShouldNotBeFound("processed.equals=" + UPDATED_PROCESSED);
    }

    @Test
    @Transactional
    public void getAllStockOrdersByProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where processed in DEFAULT_PROCESSED or UPDATED_PROCESSED
        defaultStockOrderShouldBeFound("processed.in=" + DEFAULT_PROCESSED + "," + UPDATED_PROCESSED);

        // Get all the stockOrderList where processed equals to UPDATED_PROCESSED
        defaultStockOrderShouldNotBeFound("processed.in=" + UPDATED_PROCESSED);
    }

    @Test
    @Transactional
    public void getAllStockOrdersByProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where processed is not null
        defaultStockOrderShouldBeFound("processed.specified=true");

        // Get all the stockOrderList where processed is null
        defaultStockOrderShouldNotBeFound("processed.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrdersByProcessedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where processedDate equals to DEFAULT_PROCESSED_DATE
        defaultStockOrderShouldBeFound("processedDate.equals=" + DEFAULT_PROCESSED_DATE);

        // Get all the stockOrderList where processedDate equals to UPDATED_PROCESSED_DATE
        defaultStockOrderShouldNotBeFound("processedDate.equals=" + UPDATED_PROCESSED_DATE);
    }

    @Test
    @Transactional
    public void getAllStockOrdersByProcessedDateIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where processedDate in DEFAULT_PROCESSED_DATE or UPDATED_PROCESSED_DATE
        defaultStockOrderShouldBeFound("processedDate.in=" + DEFAULT_PROCESSED_DATE + "," + UPDATED_PROCESSED_DATE);

        // Get all the stockOrderList where processedDate equals to UPDATED_PROCESSED_DATE
        defaultStockOrderShouldNotBeFound("processedDate.in=" + UPDATED_PROCESSED_DATE);
    }

    @Test
    @Transactional
    public void getAllStockOrdersByProcessedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList where processedDate is not null
        defaultStockOrderShouldBeFound("processedDate.specified=true");

        // Get all the stockOrderList where processedDate is null
        defaultStockOrderShouldNotBeFound("processedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrdersByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User creator = UserResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        stockOrder.setCreator(creator);
        stockOrderRepository.saveAndFlush(stockOrder);
        Long creatorId = creator.getId();

        // Get all the stockOrderList where creator equals to creatorId
        defaultStockOrderShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the stockOrderList where creator equals to creatorId + 1
        defaultStockOrderShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }


    @Test
    @Transactional
    public void getAllStockOrdersByApprovalIsEqualToSomething() throws Exception {
        // Initialize the database
        User approval = UserResourceIntTest.createEntity(em);
        em.persist(approval);
        em.flush();
        stockOrder.setApproval(approval);
        stockOrderRepository.saveAndFlush(stockOrder);
        Long approvalId = approval.getId();

        // Get all the stockOrderList where approval equals to approvalId
        defaultStockOrderShouldBeFound("approvalId.equals=" + approvalId);

        // Get all the stockOrderList where approval equals to approvalId + 1
        defaultStockOrderShouldNotBeFound("approvalId.equals=" + (approvalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStockOrderShouldBeFound(String filter) throws Exception {
        restStockOrderMockMvc.perform(get("/api/stock-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].siteLocation").value(hasItem(DEFAULT_SITE_LOCATION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())));

        // Check, that the count call also returns 1
        restStockOrderMockMvc.perform(get("/api/stock-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStockOrderShouldNotBeFound(String filter) throws Exception {
        restStockOrderMockMvc.perform(get("/api/stock-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockOrderMockMvc.perform(get("/api/stock-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStockOrder() throws Exception {
        // Get the stockOrder
        restStockOrderMockMvc.perform(get("/api/stock-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOrder() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        int databaseSizeBeforeUpdate = stockOrderRepository.findAll().size();

        // Update the stockOrder
        StockOrder updatedStockOrder = stockOrderRepository.findById(stockOrder.getId()).get();
        // Disconnect from session so that the updates on updatedStockOrder are not directly saved in db
        em.detach(updatedStockOrder);
        updatedStockOrder
            .siteLocation(UPDATED_SITE_LOCATION)
            .createdDate(UPDATED_CREATED_DATE)
            .processed(UPDATED_PROCESSED)
            .processedDate(UPDATED_PROCESSED_DATE);
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(updatedStockOrder);

        restStockOrderMockMvc.perform(put("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isOk());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeUpdate);
        StockOrder testStockOrder = stockOrderList.get(stockOrderList.size() - 1);
        assertThat(testStockOrder.getSiteLocation()).isEqualTo(UPDATED_SITE_LOCATION);
        assertThat(testStockOrder.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testStockOrder.isProcessed()).isEqualTo(UPDATED_PROCESSED);
        assertThat(testStockOrder.getProcessedDate()).isEqualTo(UPDATED_PROCESSED_DATE);

        // Validate the StockOrder in Elasticsearch
        verify(mockStockOrderSearchRepository, times(1)).save(testStockOrder);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOrder() throws Exception {
        int databaseSizeBeforeUpdate = stockOrderRepository.findAll().size();

        // Create the StockOrder
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockOrderMockMvc.perform(put("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StockOrder in Elasticsearch
        verify(mockStockOrderSearchRepository, times(0)).save(stockOrder);
    }

    @Test
    @Transactional
    public void deleteStockOrder() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        int databaseSizeBeforeDelete = stockOrderRepository.findAll().size();

        // Delete the stockOrder
        restStockOrderMockMvc.perform(delete("/api/stock-orders/{id}", stockOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StockOrder in Elasticsearch
        verify(mockStockOrderSearchRepository, times(1)).deleteById(stockOrder.getId());
    }

    @Test
    @Transactional
    public void searchStockOrder() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);
        when(mockStockOrderSearchRepository.search(queryStringQuery("id:" + stockOrder.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stockOrder), PageRequest.of(0, 1), 1));
        // Search the stockOrder
        restStockOrderMockMvc.perform(get("/api/_search/stock-orders?query=id:" + stockOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].siteLocation").value(hasItem(DEFAULT_SITE_LOCATION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrder.class);
        StockOrder stockOrder1 = new StockOrder();
        stockOrder1.setId(1L);
        StockOrder stockOrder2 = new StockOrder();
        stockOrder2.setId(stockOrder1.getId());
        assertThat(stockOrder1).isEqualTo(stockOrder2);
        stockOrder2.setId(2L);
        assertThat(stockOrder1).isNotEqualTo(stockOrder2);
        stockOrder1.setId(null);
        assertThat(stockOrder1).isNotEqualTo(stockOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrderDTO.class);
        StockOrderDTO stockOrderDTO1 = new StockOrderDTO();
        stockOrderDTO1.setId(1L);
        StockOrderDTO stockOrderDTO2 = new StockOrderDTO();
        assertThat(stockOrderDTO1).isNotEqualTo(stockOrderDTO2);
        stockOrderDTO2.setId(stockOrderDTO1.getId());
        assertThat(stockOrderDTO1).isEqualTo(stockOrderDTO2);
        stockOrderDTO2.setId(2L);
        assertThat(stockOrderDTO1).isNotEqualTo(stockOrderDTO2);
        stockOrderDTO1.setId(null);
        assertThat(stockOrderDTO1).isNotEqualTo(stockOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockOrderMapper.fromId(null)).isNull();
    }
}
