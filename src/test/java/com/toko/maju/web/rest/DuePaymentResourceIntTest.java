package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.DuePayment;
import com.toko.maju.domain.User;
import com.toko.maju.repository.DuePaymentRepository;
import com.toko.maju.repository.search.DuePaymentSearchRepository;
import com.toko.maju.service.DuePaymentService;
import com.toko.maju.service.dto.DuePaymentDTO;
import com.toko.maju.service.mapper.DuePaymentMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.DuePaymentCriteria;
import com.toko.maju.service.DuePaymentQueryService;

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
 * Test class for the DuePaymentResource REST controller.
 *
 * @see DuePaymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class DuePaymentResourceIntTest {

    private static final BigDecimal DEFAULT_REMAINING_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REMAINING_PAYMENT = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_SETTLED = false;
    private static final Boolean UPDATED_SETTLED = true;

    private static final BigDecimal DEFAULT_PAID = new BigDecimal(0);
    private static final BigDecimal UPDATED_PAID = new BigDecimal(1);

    @Autowired
    private DuePaymentRepository duePaymentRepository;

    @Autowired
    private DuePaymentMapper duePaymentMapper;

    @Autowired
    private DuePaymentService duePaymentService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.DuePaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private DuePaymentSearchRepository mockDuePaymentSearchRepository;

    @Autowired
    private DuePaymentQueryService duePaymentQueryService;

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

    private MockMvc restDuePaymentMockMvc;

    private DuePayment duePayment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DuePaymentResource duePaymentResource = new DuePaymentResource(duePaymentService, duePaymentQueryService);
        this.restDuePaymentMockMvc = MockMvcBuilders.standaloneSetup(duePaymentResource)
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
    public static DuePayment createEntity(EntityManager em) {
        DuePayment duePayment = new DuePayment()
            .remainingPayment(DEFAULT_REMAINING_PAYMENT)
            .createdDate(DEFAULT_CREATED_DATE)
            .settled(DEFAULT_SETTLED)
            .paid(DEFAULT_PAID);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        duePayment.setCreator(user);
        return duePayment;
    }

    @Before
    public void initTest() {
        duePayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createDuePayment() throws Exception {
        int databaseSizeBeforeCreate = duePaymentRepository.findAll().size();

        // Create the DuePayment
        DuePaymentDTO duePaymentDTO = duePaymentMapper.toDto(duePayment);
        restDuePaymentMockMvc.perform(post("/api/due-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duePaymentDTO)))
            .andExpect(status().isCreated());

        // Validate the DuePayment in the database
        List<DuePayment> duePaymentList = duePaymentRepository.findAll();
        assertThat(duePaymentList).hasSize(databaseSizeBeforeCreate + 1);
        DuePayment testDuePayment = duePaymentList.get(duePaymentList.size() - 1);
        assertThat(testDuePayment.getRemainingPayment()).isEqualTo(DEFAULT_REMAINING_PAYMENT);
        assertThat(testDuePayment.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDuePayment.isSettled()).isEqualTo(DEFAULT_SETTLED);
        assertThat(testDuePayment.getPaid()).isEqualTo(DEFAULT_PAID);

        // Validate the DuePayment in Elasticsearch
        verify(mockDuePaymentSearchRepository, times(1)).save(testDuePayment);
    }

    @Test
    @Transactional
    public void createDuePaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = duePaymentRepository.findAll().size();

        // Create the DuePayment with an existing ID
        duePayment.setId(1L);
        DuePaymentDTO duePaymentDTO = duePaymentMapper.toDto(duePayment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDuePaymentMockMvc.perform(post("/api/due-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duePaymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DuePayment in the database
        List<DuePayment> duePaymentList = duePaymentRepository.findAll();
        assertThat(duePaymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the DuePayment in Elasticsearch
        verify(mockDuePaymentSearchRepository, times(0)).save(duePayment);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = duePaymentRepository.findAll().size();
        // set the field null
        duePayment.setCreatedDate(null);

        // Create the DuePayment, which fails.
        DuePaymentDTO duePaymentDTO = duePaymentMapper.toDto(duePayment);

        restDuePaymentMockMvc.perform(post("/api/due-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duePaymentDTO)))
            .andExpect(status().isBadRequest());

        List<DuePayment> duePaymentList = duePaymentRepository.findAll();
        assertThat(duePaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSettledIsRequired() throws Exception {
        int databaseSizeBeforeTest = duePaymentRepository.findAll().size();
        // set the field null
        duePayment.setSettled(null);

        // Create the DuePayment, which fails.
        DuePaymentDTO duePaymentDTO = duePaymentMapper.toDto(duePayment);

        restDuePaymentMockMvc.perform(post("/api/due-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duePaymentDTO)))
            .andExpect(status().isBadRequest());

        List<DuePayment> duePaymentList = duePaymentRepository.findAll();
        assertThat(duePaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = duePaymentRepository.findAll().size();
        // set the field null
        duePayment.setPaid(null);

        // Create the DuePayment, which fails.
        DuePaymentDTO duePaymentDTO = duePaymentMapper.toDto(duePayment);

        restDuePaymentMockMvc.perform(post("/api/due-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duePaymentDTO)))
            .andExpect(status().isBadRequest());

        List<DuePayment> duePaymentList = duePaymentRepository.findAll();
        assertThat(duePaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDuePayments() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList
        restDuePaymentMockMvc.perform(get("/api/due-payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(duePayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].remainingPayment").value(hasItem(DEFAULT_REMAINING_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].settled").value(hasItem(DEFAULT_SETTLED.booleanValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())));
    }
    
    @Test
    @Transactional
    public void getDuePayment() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get the duePayment
        restDuePaymentMockMvc.perform(get("/api/due-payments/{id}", duePayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(duePayment.getId().intValue()))
            .andExpect(jsonPath("$.remainingPayment").value(DEFAULT_REMAINING_PAYMENT.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.settled").value(DEFAULT_SETTLED.booleanValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.intValue()));
    }

    @Test
    @Transactional
    public void getAllDuePaymentsByRemainingPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where remainingPayment equals to DEFAULT_REMAINING_PAYMENT
        defaultDuePaymentShouldBeFound("remainingPayment.equals=" + DEFAULT_REMAINING_PAYMENT);

        // Get all the duePaymentList where remainingPayment equals to UPDATED_REMAINING_PAYMENT
        defaultDuePaymentShouldNotBeFound("remainingPayment.equals=" + UPDATED_REMAINING_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllDuePaymentsByRemainingPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where remainingPayment in DEFAULT_REMAINING_PAYMENT or UPDATED_REMAINING_PAYMENT
        defaultDuePaymentShouldBeFound("remainingPayment.in=" + DEFAULT_REMAINING_PAYMENT + "," + UPDATED_REMAINING_PAYMENT);

        // Get all the duePaymentList where remainingPayment equals to UPDATED_REMAINING_PAYMENT
        defaultDuePaymentShouldNotBeFound("remainingPayment.in=" + UPDATED_REMAINING_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllDuePaymentsByRemainingPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where remainingPayment is not null
        defaultDuePaymentShouldBeFound("remainingPayment.specified=true");

        // Get all the duePaymentList where remainingPayment is null
        defaultDuePaymentShouldNotBeFound("remainingPayment.specified=false");
    }

    @Test
    @Transactional
    public void getAllDuePaymentsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where createdDate equals to DEFAULT_CREATED_DATE
        defaultDuePaymentShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the duePaymentList where createdDate equals to UPDATED_CREATED_DATE
        defaultDuePaymentShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDuePaymentsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultDuePaymentShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the duePaymentList where createdDate equals to UPDATED_CREATED_DATE
        defaultDuePaymentShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDuePaymentsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where createdDate is not null
        defaultDuePaymentShouldBeFound("createdDate.specified=true");

        // Get all the duePaymentList where createdDate is null
        defaultDuePaymentShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDuePaymentsBySettledIsEqualToSomething() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where settled equals to DEFAULT_SETTLED
        defaultDuePaymentShouldBeFound("settled.equals=" + DEFAULT_SETTLED);

        // Get all the duePaymentList where settled equals to UPDATED_SETTLED
        defaultDuePaymentShouldNotBeFound("settled.equals=" + UPDATED_SETTLED);
    }

    @Test
    @Transactional
    public void getAllDuePaymentsBySettledIsInShouldWork() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where settled in DEFAULT_SETTLED or UPDATED_SETTLED
        defaultDuePaymentShouldBeFound("settled.in=" + DEFAULT_SETTLED + "," + UPDATED_SETTLED);

        // Get all the duePaymentList where settled equals to UPDATED_SETTLED
        defaultDuePaymentShouldNotBeFound("settled.in=" + UPDATED_SETTLED);
    }

    @Test
    @Transactional
    public void getAllDuePaymentsBySettledIsNullOrNotNull() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where settled is not null
        defaultDuePaymentShouldBeFound("settled.specified=true");

        // Get all the duePaymentList where settled is null
        defaultDuePaymentShouldNotBeFound("settled.specified=false");
    }

    @Test
    @Transactional
    public void getAllDuePaymentsByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where paid equals to DEFAULT_PAID
        defaultDuePaymentShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the duePaymentList where paid equals to UPDATED_PAID
        defaultDuePaymentShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllDuePaymentsByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultDuePaymentShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the duePaymentList where paid equals to UPDATED_PAID
        defaultDuePaymentShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllDuePaymentsByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        // Get all the duePaymentList where paid is not null
        defaultDuePaymentShouldBeFound("paid.specified=true");

        // Get all the duePaymentList where paid is null
        defaultDuePaymentShouldNotBeFound("paid.specified=false");
    }

    @Test
    @Transactional
    public void getAllDuePaymentsByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User creator = UserResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        duePayment.setCreator(creator);
        duePaymentRepository.saveAndFlush(duePayment);
        Long creatorId = creator.getId();

        // Get all the duePaymentList where creator equals to creatorId
        defaultDuePaymentShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the duePaymentList where creator equals to creatorId + 1
        defaultDuePaymentShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDuePaymentShouldBeFound(String filter) throws Exception {
        restDuePaymentMockMvc.perform(get("/api/due-payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(duePayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].remainingPayment").value(hasItem(DEFAULT_REMAINING_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].settled").value(hasItem(DEFAULT_SETTLED.booleanValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())));

        // Check, that the count call also returns 1
        restDuePaymentMockMvc.perform(get("/api/due-payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDuePaymentShouldNotBeFound(String filter) throws Exception {
        restDuePaymentMockMvc.perform(get("/api/due-payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDuePaymentMockMvc.perform(get("/api/due-payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDuePayment() throws Exception {
        // Get the duePayment
        restDuePaymentMockMvc.perform(get("/api/due-payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDuePayment() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        int databaseSizeBeforeUpdate = duePaymentRepository.findAll().size();

        // Update the duePayment
        DuePayment updatedDuePayment = duePaymentRepository.findById(duePayment.getId()).get();
        // Disconnect from session so that the updates on updatedDuePayment are not directly saved in db
        em.detach(updatedDuePayment);
        updatedDuePayment
            .remainingPayment(UPDATED_REMAINING_PAYMENT)
            .createdDate(UPDATED_CREATED_DATE)
            .settled(UPDATED_SETTLED)
            .paid(UPDATED_PAID);
        DuePaymentDTO duePaymentDTO = duePaymentMapper.toDto(updatedDuePayment);

        restDuePaymentMockMvc.perform(put("/api/due-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duePaymentDTO)))
            .andExpect(status().isOk());

        // Validate the DuePayment in the database
        List<DuePayment> duePaymentList = duePaymentRepository.findAll();
        assertThat(duePaymentList).hasSize(databaseSizeBeforeUpdate);
        DuePayment testDuePayment = duePaymentList.get(duePaymentList.size() - 1);
        assertThat(testDuePayment.getRemainingPayment()).isEqualTo(UPDATED_REMAINING_PAYMENT);
        assertThat(testDuePayment.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDuePayment.isSettled()).isEqualTo(UPDATED_SETTLED);
        assertThat(testDuePayment.getPaid()).isEqualTo(UPDATED_PAID);

        // Validate the DuePayment in Elasticsearch
        verify(mockDuePaymentSearchRepository, times(1)).save(testDuePayment);
    }

    @Test
    @Transactional
    public void updateNonExistingDuePayment() throws Exception {
        int databaseSizeBeforeUpdate = duePaymentRepository.findAll().size();

        // Create the DuePayment
        DuePaymentDTO duePaymentDTO = duePaymentMapper.toDto(duePayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDuePaymentMockMvc.perform(put("/api/due-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duePaymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DuePayment in the database
        List<DuePayment> duePaymentList = duePaymentRepository.findAll();
        assertThat(duePaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DuePayment in Elasticsearch
        verify(mockDuePaymentSearchRepository, times(0)).save(duePayment);
    }

    @Test
    @Transactional
    public void deleteDuePayment() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);

        int databaseSizeBeforeDelete = duePaymentRepository.findAll().size();

        // Delete the duePayment
        restDuePaymentMockMvc.perform(delete("/api/due-payments/{id}", duePayment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DuePayment> duePaymentList = duePaymentRepository.findAll();
        assertThat(duePaymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DuePayment in Elasticsearch
        verify(mockDuePaymentSearchRepository, times(1)).deleteById(duePayment.getId());
    }

    @Test
    @Transactional
    public void searchDuePayment() throws Exception {
        // Initialize the database
        duePaymentRepository.saveAndFlush(duePayment);
        when(mockDuePaymentSearchRepository.search(queryStringQuery("id:" + duePayment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(duePayment), PageRequest.of(0, 1), 1));
        // Search the duePayment
        restDuePaymentMockMvc.perform(get("/api/_search/due-payments?query=id:" + duePayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(duePayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].remainingPayment").value(hasItem(DEFAULT_REMAINING_PAYMENT.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].settled").value(hasItem(DEFAULT_SETTLED.booleanValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DuePayment.class);
        DuePayment duePayment1 = new DuePayment();
        duePayment1.setId(1L);
        DuePayment duePayment2 = new DuePayment();
        duePayment2.setId(duePayment1.getId());
        assertThat(duePayment1).isEqualTo(duePayment2);
        duePayment2.setId(2L);
        assertThat(duePayment1).isNotEqualTo(duePayment2);
        duePayment1.setId(null);
        assertThat(duePayment1).isNotEqualTo(duePayment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DuePaymentDTO.class);
        DuePaymentDTO duePaymentDTO1 = new DuePaymentDTO();
        duePaymentDTO1.setId(1L);
        DuePaymentDTO duePaymentDTO2 = new DuePaymentDTO();
        assertThat(duePaymentDTO1).isNotEqualTo(duePaymentDTO2);
        duePaymentDTO2.setId(duePaymentDTO1.getId());
        assertThat(duePaymentDTO1).isEqualTo(duePaymentDTO2);
        duePaymentDTO2.setId(2L);
        assertThat(duePaymentDTO1).isNotEqualTo(duePaymentDTO2);
        duePaymentDTO1.setId(null);
        assertThat(duePaymentDTO1).isNotEqualTo(duePaymentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(duePaymentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(duePaymentMapper.fromId(null)).isNull();
    }
}
