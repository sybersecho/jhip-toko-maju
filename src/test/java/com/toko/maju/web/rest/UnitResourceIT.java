package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;
import com.toko.maju.domain.Unit;
import com.toko.maju.repository.UnitRepository;
import com.toko.maju.repository.search.UnitSearchRepository;
import com.toko.maju.service.UnitService;
import com.toko.maju.service.dto.UnitDTO;
import com.toko.maju.service.mapper.UnitMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
 * Integration tests for the {@Link UnitResource} REST controller.
 */
@SpringBootTest(classes = JhiptokomajuApp.class)
public class UnitResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UnitMapper unitMapper;

    @Autowired
    private UnitService unitService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.UnitSearchRepositoryMockConfiguration
     */
    @Autowired
    private UnitSearchRepository mockUnitSearchRepository;

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

    private MockMvc restUnitMockMvc;

    private Unit unit;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UnitResource unitResource = new UnitResource(unitService);
        this.restUnitMockMvc = MockMvcBuilders.standaloneSetup(unitResource)
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
    public static Unit createEntity(EntityManager em) {
        Unit unit = new Unit()
            .name(DEFAULT_NAME);
        return unit;
    }

    @BeforeEach
    public void initTest() {
        unit = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnit() throws Exception {
        int databaseSizeBeforeCreate = unitRepository.findAll().size();

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);
        restUnitMockMvc.perform(post("/api/units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitDTO)))
            .andExpect(status().isCreated());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeCreate + 1);
        Unit testUnit = unitList.get(unitList.size() - 1);
        assertThat(testUnit.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Unit in Elasticsearch
        verify(mockUnitSearchRepository, times(1)).save(testUnit);
    }

    @Test
    @Transactional
    public void createUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = unitRepository.findAll().size();

        // Create the Unit with an existing ID
        unit.setId(1L);
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitMockMvc.perform(post("/api/units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeCreate);

        // Validate the Unit in Elasticsearch
        verify(mockUnitSearchRepository, times(0)).save(unit);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitRepository.findAll().size();
        // set the field null
        unit.setName(null);

        // Create the Unit, which fails.
        UnitDTO unitDTO = unitMapper.toDto(unit);

        restUnitMockMvc.perform(post("/api/units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitDTO)))
            .andExpect(status().isBadRequest());

        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUnits() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        // Get all the unitList
        restUnitMockMvc.perform(get("/api/units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        // Get the unit
        restUnitMockMvc.perform(get("/api/units/{id}", unit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUnit() throws Exception {
        // Get the unit
        restUnitMockMvc.perform(get("/api/units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        int databaseSizeBeforeUpdate = unitRepository.findAll().size();

        // Update the unit
        Unit updatedUnit = unitRepository.findById(unit.getId()).get();
        // Disconnect from session so that the updates on updatedUnit are not directly saved in db
        em.detach(updatedUnit);
        updatedUnit
            .name(UPDATED_NAME);
        UnitDTO unitDTO = unitMapper.toDto(updatedUnit);

        restUnitMockMvc.perform(put("/api/units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitDTO)))
            .andExpect(status().isOk());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);
        Unit testUnit = unitList.get(unitList.size() - 1);
        assertThat(testUnit.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Unit in Elasticsearch
        verify(mockUnitSearchRepository, times(1)).save(testUnit);
    }

    @Test
    @Transactional
    public void updateNonExistingUnit() throws Exception {
        int databaseSizeBeforeUpdate = unitRepository.findAll().size();

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitMockMvc.perform(put("/api/units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Unit in Elasticsearch
        verify(mockUnitSearchRepository, times(0)).save(unit);
    }

    @Test
    @Transactional
    public void deleteUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        int databaseSizeBeforeDelete = unitRepository.findAll().size();

        // Delete the unit
        restUnitMockMvc.perform(delete("/api/units/{id}", unit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Unit in Elasticsearch
        verify(mockUnitSearchRepository, times(1)).deleteById(unit.getId());
    }

    @Test
    @Transactional
    public void searchUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);
        when(mockUnitSearchRepository.search(queryStringQuery("id:" + unit.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(unit), PageRequest.of(0, 1), 1));
        // Search the unit
        restUnitMockMvc.perform(get("/api/_search/units?query=id:" + unit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Unit.class);
        Unit unit1 = new Unit();
        unit1.setId(1L);
        Unit unit2 = new Unit();
        unit2.setId(unit1.getId());
        assertThat(unit1).isEqualTo(unit2);
        unit2.setId(2L);
        assertThat(unit1).isNotEqualTo(unit2);
        unit1.setId(null);
        assertThat(unit1).isNotEqualTo(unit2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitDTO.class);
        UnitDTO unitDTO1 = new UnitDTO();
        unitDTO1.setId(1L);
        UnitDTO unitDTO2 = new UnitDTO();
        assertThat(unitDTO1).isNotEqualTo(unitDTO2);
        unitDTO2.setId(unitDTO1.getId());
        assertThat(unitDTO1).isEqualTo(unitDTO2);
        unitDTO2.setId(2L);
        assertThat(unitDTO1).isNotEqualTo(unitDTO2);
        unitDTO1.setId(null);
        assertThat(unitDTO1).isNotEqualTo(unitDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(unitMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(unitMapper.fromId(null)).isNull();
    }
}
