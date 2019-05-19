package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.StockOrderReceive;
import com.toko.maju.domain.User;
import com.toko.maju.repository.StockOrderReceiveRepository;
import com.toko.maju.repository.search.StockOrderReceiveSearchRepository;
import com.toko.maju.service.StockOrderReceiveService;
import com.toko.maju.service.dto.StockOrderReceiveDTO;
import com.toko.maju.service.mapper.StockOrderReceiveMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.StockOrderReceiveCriteria;
import com.toko.maju.service.StockOrderReceiveQueryService;

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
 * Test class for the StockOrderReceiveResource REST controller.
 *
 * @see StockOrderReceiveResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class StockOrderReceiveResourceIntTest {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private StockOrderReceiveRepository stockOrderReceiveRepository;

    @Autowired
    private StockOrderReceiveMapper stockOrderReceiveMapper;

    @Autowired
    private StockOrderReceiveService stockOrderReceiveService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.StockOrderReceiveSearchRepositoryMockConfiguration
     */
    @Autowired
    private StockOrderReceiveSearchRepository mockStockOrderReceiveSearchRepository;

    @Autowired
    private StockOrderReceiveQueryService stockOrderReceiveQueryService;

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

    private MockMvc restStockOrderReceiveMockMvc;

    private StockOrderReceive stockOrderReceive;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockOrderReceiveResource stockOrderReceiveResource = new StockOrderReceiveResource(stockOrderReceiveService, stockOrderReceiveQueryService);
        this.restStockOrderReceiveMockMvc = MockMvcBuilders.standaloneSetup(stockOrderReceiveResource)
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
    public static StockOrderReceive createEntity(EntityManager em) {
        StockOrderReceive stockOrderReceive = new StockOrderReceive()
            .barcode(DEFAULT_BARCODE)
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .createdDate(DEFAULT_CREATED_DATE);
        return stockOrderReceive;
    }

    @Before
    public void initTest() {
        stockOrderReceive = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOrderReceive() throws Exception {
        int databaseSizeBeforeCreate = stockOrderReceiveRepository.findAll().size();

        // Create the StockOrderReceive
        StockOrderReceiveDTO stockOrderReceiveDTO = stockOrderReceiveMapper.toDto(stockOrderReceive);
        restStockOrderReceiveMockMvc.perform(post("/api/stock-order-receives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderReceiveDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOrderReceive in the database
        List<StockOrderReceive> stockOrderReceiveList = stockOrderReceiveRepository.findAll();
        assertThat(stockOrderReceiveList).hasSize(databaseSizeBeforeCreate + 1);
        StockOrderReceive testStockOrderReceive = stockOrderReceiveList.get(stockOrderReceiveList.size() - 1);
        assertThat(testStockOrderReceive.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testStockOrderReceive.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStockOrderReceive.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockOrderReceive.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);

        // Validate the StockOrderReceive in Elasticsearch
        verify(mockStockOrderReceiveSearchRepository, times(1)).save(testStockOrderReceive);
    }

    @Test
    @Transactional
    public void createStockOrderReceiveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOrderReceiveRepository.findAll().size();

        // Create the StockOrderReceive with an existing ID
        stockOrderReceive.setId(1L);
        StockOrderReceiveDTO stockOrderReceiveDTO = stockOrderReceiveMapper.toDto(stockOrderReceive);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOrderReceiveMockMvc.perform(post("/api/stock-order-receives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderReceiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrderReceive in the database
        List<StockOrderReceive> stockOrderReceiveList = stockOrderReceiveRepository.findAll();
        assertThat(stockOrderReceiveList).hasSize(databaseSizeBeforeCreate);

        // Validate the StockOrderReceive in Elasticsearch
        verify(mockStockOrderReceiveSearchRepository, times(0)).save(stockOrderReceive);
    }

    @Test
    @Transactional
    public void getAllStockOrderReceives() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList
        restStockOrderReceiveMockMvc.perform(get("/api/stock-order-receives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrderReceive.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getStockOrderReceive() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get the stockOrderReceive
        restStockOrderReceiveMockMvc.perform(get("/api/stock-order-receives/{id}", stockOrderReceive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOrderReceive.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where barcode equals to DEFAULT_BARCODE
        defaultStockOrderReceiveShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the stockOrderReceiveList where barcode equals to UPDATED_BARCODE
        defaultStockOrderReceiveShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultStockOrderReceiveShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the stockOrderReceiveList where barcode equals to UPDATED_BARCODE
        defaultStockOrderReceiveShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where barcode is not null
        defaultStockOrderReceiveShouldBeFound("barcode.specified=true");

        // Get all the stockOrderReceiveList where barcode is null
        defaultStockOrderReceiveShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where name equals to DEFAULT_NAME
        defaultStockOrderReceiveShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the stockOrderReceiveList where name equals to UPDATED_NAME
        defaultStockOrderReceiveShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStockOrderReceiveShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the stockOrderReceiveList where name equals to UPDATED_NAME
        defaultStockOrderReceiveShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where name is not null
        defaultStockOrderReceiveShouldBeFound("name.specified=true");

        // Get all the stockOrderReceiveList where name is null
        defaultStockOrderReceiveShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where quantity equals to DEFAULT_QUANTITY
        defaultStockOrderReceiveShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the stockOrderReceiveList where quantity equals to UPDATED_QUANTITY
        defaultStockOrderReceiveShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultStockOrderReceiveShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the stockOrderReceiveList where quantity equals to UPDATED_QUANTITY
        defaultStockOrderReceiveShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where quantity is not null
        defaultStockOrderReceiveShouldBeFound("quantity.specified=true");

        // Get all the stockOrderReceiveList where quantity is null
        defaultStockOrderReceiveShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultStockOrderReceiveShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the stockOrderReceiveList where quantity greater than or equals to UPDATED_QUANTITY
        defaultStockOrderReceiveShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where quantity less than or equals to DEFAULT_QUANTITY
        defaultStockOrderReceiveShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the stockOrderReceiveList where quantity less than or equals to UPDATED_QUANTITY
        defaultStockOrderReceiveShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllStockOrderReceivesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where createdDate equals to DEFAULT_CREATED_DATE
        defaultStockOrderReceiveShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the stockOrderReceiveList where createdDate equals to UPDATED_CREATED_DATE
        defaultStockOrderReceiveShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultStockOrderReceiveShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the stockOrderReceiveList where createdDate equals to UPDATED_CREATED_DATE
        defaultStockOrderReceiveShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        // Get all the stockOrderReceiveList where createdDate is not null
        defaultStockOrderReceiveShouldBeFound("createdDate.specified=true");

        // Get all the stockOrderReceiveList where createdDate is null
        defaultStockOrderReceiveShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockOrderReceivesByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User creator = UserResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        stockOrderReceive.setCreator(creator);
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);
        Long creatorId = creator.getId();

        // Get all the stockOrderReceiveList where creator equals to creatorId
        defaultStockOrderReceiveShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the stockOrderReceiveList where creator equals to creatorId + 1
        defaultStockOrderReceiveShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStockOrderReceiveShouldBeFound(String filter) throws Exception {
        restStockOrderReceiveMockMvc.perform(get("/api/stock-order-receives?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrderReceive.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restStockOrderReceiveMockMvc.perform(get("/api/stock-order-receives/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStockOrderReceiveShouldNotBeFound(String filter) throws Exception {
        restStockOrderReceiveMockMvc.perform(get("/api/stock-order-receives?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockOrderReceiveMockMvc.perform(get("/api/stock-order-receives/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStockOrderReceive() throws Exception {
        // Get the stockOrderReceive
        restStockOrderReceiveMockMvc.perform(get("/api/stock-order-receives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOrderReceive() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        int databaseSizeBeforeUpdate = stockOrderReceiveRepository.findAll().size();

        // Update the stockOrderReceive
        StockOrderReceive updatedStockOrderReceive = stockOrderReceiveRepository.findById(stockOrderReceive.getId()).get();
        // Disconnect from session so that the updates on updatedStockOrderReceive are not directly saved in db
        em.detach(updatedStockOrderReceive);
        updatedStockOrderReceive
            .barcode(UPDATED_BARCODE)
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .createdDate(UPDATED_CREATED_DATE);
        StockOrderReceiveDTO stockOrderReceiveDTO = stockOrderReceiveMapper.toDto(updatedStockOrderReceive);

        restStockOrderReceiveMockMvc.perform(put("/api/stock-order-receives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderReceiveDTO)))
            .andExpect(status().isOk());

        // Validate the StockOrderReceive in the database
        List<StockOrderReceive> stockOrderReceiveList = stockOrderReceiveRepository.findAll();
        assertThat(stockOrderReceiveList).hasSize(databaseSizeBeforeUpdate);
        StockOrderReceive testStockOrderReceive = stockOrderReceiveList.get(stockOrderReceiveList.size() - 1);
        assertThat(testStockOrderReceive.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testStockOrderReceive.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStockOrderReceive.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockOrderReceive.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);

        // Validate the StockOrderReceive in Elasticsearch
        verify(mockStockOrderReceiveSearchRepository, times(1)).save(testStockOrderReceive);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOrderReceive() throws Exception {
        int databaseSizeBeforeUpdate = stockOrderReceiveRepository.findAll().size();

        // Create the StockOrderReceive
        StockOrderReceiveDTO stockOrderReceiveDTO = stockOrderReceiveMapper.toDto(stockOrderReceive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockOrderReceiveMockMvc.perform(put("/api/stock-order-receives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderReceiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrderReceive in the database
        List<StockOrderReceive> stockOrderReceiveList = stockOrderReceiveRepository.findAll();
        assertThat(stockOrderReceiveList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StockOrderReceive in Elasticsearch
        verify(mockStockOrderReceiveSearchRepository, times(0)).save(stockOrderReceive);
    }

    @Test
    @Transactional
    public void deleteStockOrderReceive() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);

        int databaseSizeBeforeDelete = stockOrderReceiveRepository.findAll().size();

        // Delete the stockOrderReceive
        restStockOrderReceiveMockMvc.perform(delete("/api/stock-order-receives/{id}", stockOrderReceive.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOrderReceive> stockOrderReceiveList = stockOrderReceiveRepository.findAll();
        assertThat(stockOrderReceiveList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StockOrderReceive in Elasticsearch
        verify(mockStockOrderReceiveSearchRepository, times(1)).deleteById(stockOrderReceive.getId());
    }

    @Test
    @Transactional
    public void searchStockOrderReceive() throws Exception {
        // Initialize the database
        stockOrderReceiveRepository.saveAndFlush(stockOrderReceive);
        when(mockStockOrderReceiveSearchRepository.search(queryStringQuery("id:" + stockOrderReceive.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stockOrderReceive), PageRequest.of(0, 1), 1));
        // Search the stockOrderReceive
        restStockOrderReceiveMockMvc.perform(get("/api/_search/stock-order-receives?query=id:" + stockOrderReceive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrderReceive.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrderReceive.class);
        StockOrderReceive stockOrderReceive1 = new StockOrderReceive();
        stockOrderReceive1.setId(1L);
        StockOrderReceive stockOrderReceive2 = new StockOrderReceive();
        stockOrderReceive2.setId(stockOrderReceive1.getId());
        assertThat(stockOrderReceive1).isEqualTo(stockOrderReceive2);
        stockOrderReceive2.setId(2L);
        assertThat(stockOrderReceive1).isNotEqualTo(stockOrderReceive2);
        stockOrderReceive1.setId(null);
        assertThat(stockOrderReceive1).isNotEqualTo(stockOrderReceive2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrderReceiveDTO.class);
        StockOrderReceiveDTO stockOrderReceiveDTO1 = new StockOrderReceiveDTO();
        stockOrderReceiveDTO1.setId(1L);
        StockOrderReceiveDTO stockOrderReceiveDTO2 = new StockOrderReceiveDTO();
        assertThat(stockOrderReceiveDTO1).isNotEqualTo(stockOrderReceiveDTO2);
        stockOrderReceiveDTO2.setId(stockOrderReceiveDTO1.getId());
        assertThat(stockOrderReceiveDTO1).isEqualTo(stockOrderReceiveDTO2);
        stockOrderReceiveDTO2.setId(2L);
        assertThat(stockOrderReceiveDTO1).isNotEqualTo(stockOrderReceiveDTO2);
        stockOrderReceiveDTO1.setId(null);
        assertThat(stockOrderReceiveDTO1).isNotEqualTo(stockOrderReceiveDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockOrderReceiveMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockOrderReceiveMapper.fromId(null)).isNull();
    }
}
