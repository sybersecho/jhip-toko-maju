package com.toko.maju.web.rest;

import com.toko.maju.JhiptokomajuApp;

import com.toko.maju.domain.ProjectProduct;
import com.toko.maju.domain.Product;
import com.toko.maju.domain.Project;
import com.toko.maju.repository.ProjectProductRepository;
import com.toko.maju.repository.search.ProjectProductSearchRepository;
import com.toko.maju.service.ProjectProductService;
import com.toko.maju.service.dto.ProjectProductDTO;
import com.toko.maju.service.mapper.ProjectProductMapper;
import com.toko.maju.web.rest.errors.ExceptionTranslator;
import com.toko.maju.service.dto.ProjectProductCriteria;
import com.toko.maju.service.ProjectProductQueryService;

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
 * Test class for the ProjectProductResource REST controller.
 *
 * @see ProjectProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiptokomajuApp.class)
public class ProjectProductResourceIntTest {

    private static final BigDecimal DEFAULT_SPECIAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_SPECIAL_PRICE = new BigDecimal(1);

    @Autowired
    private ProjectProductRepository projectProductRepository;

    @Autowired
    private ProjectProductMapper projectProductMapper;

    @Autowired
    private ProjectProductService projectProductService;

    /**
     * This repository is mocked in the com.toko.maju.repository.search test package.
     *
     * @see com.toko.maju.repository.search.ProjectProductSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProjectProductSearchRepository mockProjectProductSearchRepository;

    @Autowired
    private ProjectProductQueryService projectProductQueryService;

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

    private MockMvc restProjectProductMockMvc;

    private ProjectProduct projectProduct;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectProductResource projectProductResource = new ProjectProductResource(projectProductService, projectProductQueryService);
        this.restProjectProductMockMvc = MockMvcBuilders.standaloneSetup(projectProductResource)
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
    public static ProjectProduct createEntity(EntityManager em) {
        ProjectProduct projectProduct = new ProjectProduct()
            .specialPrice(DEFAULT_SPECIAL_PRICE);
        // Add required entity
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        projectProduct.setProduct(product);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectProduct.setProject(project);
        return projectProduct;
    }

    @Before
    public void initTest() {
        projectProduct = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectProduct() throws Exception {
        int databaseSizeBeforeCreate = projectProductRepository.findAll().size();

        // Create the ProjectProduct
        ProjectProductDTO projectProductDTO = projectProductMapper.toDto(projectProduct);
        restProjectProductMockMvc.perform(post("/api/project-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectProductDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectProduct in the database
        List<ProjectProduct> projectProductList = projectProductRepository.findAll();
        assertThat(projectProductList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectProduct testProjectProduct = projectProductList.get(projectProductList.size() - 1);
        assertThat(testProjectProduct.getSpecialPrice()).isEqualTo(DEFAULT_SPECIAL_PRICE);

        // Validate the ProjectProduct in Elasticsearch
        verify(mockProjectProductSearchRepository, times(1)).save(testProjectProduct);
    }

    @Test
    @Transactional
    public void createProjectProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectProductRepository.findAll().size();

        // Create the ProjectProduct with an existing ID
        projectProduct.setId(1L);
        ProjectProductDTO projectProductDTO = projectProductMapper.toDto(projectProduct);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectProductMockMvc.perform(post("/api/project-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectProduct in the database
        List<ProjectProduct> projectProductList = projectProductRepository.findAll();
        assertThat(projectProductList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProjectProduct in Elasticsearch
        verify(mockProjectProductSearchRepository, times(0)).save(projectProduct);
    }

    @Test
    @Transactional
    public void checkSpecialPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectProductRepository.findAll().size();
        // set the field null
        projectProduct.setSpecialPrice(null);

        // Create the ProjectProduct, which fails.
        ProjectProductDTO projectProductDTO = projectProductMapper.toDto(projectProduct);

        restProjectProductMockMvc.perform(post("/api/project-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectProductDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectProduct> projectProductList = projectProductRepository.findAll();
        assertThat(projectProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectProducts() throws Exception {
        // Initialize the database
        projectProductRepository.saveAndFlush(projectProduct);

        // Get all the projectProductList
        restProjectProductMockMvc.perform(get("/api/project-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].specialPrice").value(hasItem(DEFAULT_SPECIAL_PRICE.intValue())));
    }
    
    @Test
    @Transactional
    public void getProjectProduct() throws Exception {
        // Initialize the database
        projectProductRepository.saveAndFlush(projectProduct);

        // Get the projectProduct
        restProjectProductMockMvc.perform(get("/api/project-products/{id}", projectProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectProduct.getId().intValue()))
            .andExpect(jsonPath("$.specialPrice").value(DEFAULT_SPECIAL_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getAllProjectProductsBySpecialPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        projectProductRepository.saveAndFlush(projectProduct);

        // Get all the projectProductList where specialPrice equals to DEFAULT_SPECIAL_PRICE
        defaultProjectProductShouldBeFound("specialPrice.equals=" + DEFAULT_SPECIAL_PRICE);

        // Get all the projectProductList where specialPrice equals to UPDATED_SPECIAL_PRICE
        defaultProjectProductShouldNotBeFound("specialPrice.equals=" + UPDATED_SPECIAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllProjectProductsBySpecialPriceIsInShouldWork() throws Exception {
        // Initialize the database
        projectProductRepository.saveAndFlush(projectProduct);

        // Get all the projectProductList where specialPrice in DEFAULT_SPECIAL_PRICE or UPDATED_SPECIAL_PRICE
        defaultProjectProductShouldBeFound("specialPrice.in=" + DEFAULT_SPECIAL_PRICE + "," + UPDATED_SPECIAL_PRICE);

        // Get all the projectProductList where specialPrice equals to UPDATED_SPECIAL_PRICE
        defaultProjectProductShouldNotBeFound("specialPrice.in=" + UPDATED_SPECIAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllProjectProductsBySpecialPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectProductRepository.saveAndFlush(projectProduct);

        // Get all the projectProductList where specialPrice is not null
        defaultProjectProductShouldBeFound("specialPrice.specified=true");

        // Get all the projectProductList where specialPrice is null
        defaultProjectProductShouldNotBeFound("specialPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectProductsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        projectProduct.setProduct(product);
        projectProductRepository.saveAndFlush(projectProduct);
        Long productId = product.getId();

        // Get all the projectProductList where product equals to productId
        defaultProjectProductShouldBeFound("productId.equals=" + productId);

        // Get all the projectProductList where product equals to productId + 1
        defaultProjectProductShouldNotBeFound("productId.equals=" + (productId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectProductsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectProduct.setProject(project);
        projectProductRepository.saveAndFlush(projectProduct);
        Long projectId = project.getId();

        // Get all the projectProductList where project equals to projectId
        defaultProjectProductShouldBeFound("projectId.equals=" + projectId);

        // Get all the projectProductList where project equals to projectId + 1
        defaultProjectProductShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProjectProductShouldBeFound(String filter) throws Exception {
        restProjectProductMockMvc.perform(get("/api/project-products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].specialPrice").value(hasItem(DEFAULT_SPECIAL_PRICE.intValue())));

        // Check, that the count call also returns 1
        restProjectProductMockMvc.perform(get("/api/project-products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProjectProductShouldNotBeFound(String filter) throws Exception {
        restProjectProductMockMvc.perform(get("/api/project-products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectProductMockMvc.perform(get("/api/project-products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProjectProduct() throws Exception {
        // Get the projectProduct
        restProjectProductMockMvc.perform(get("/api/project-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectProduct() throws Exception {
        // Initialize the database
        projectProductRepository.saveAndFlush(projectProduct);

        int databaseSizeBeforeUpdate = projectProductRepository.findAll().size();

        // Update the projectProduct
        ProjectProduct updatedProjectProduct = projectProductRepository.findById(projectProduct.getId()).get();
        // Disconnect from session so that the updates on updatedProjectProduct are not directly saved in db
        em.detach(updatedProjectProduct);
        updatedProjectProduct
            .specialPrice(UPDATED_SPECIAL_PRICE);
        ProjectProductDTO projectProductDTO = projectProductMapper.toDto(updatedProjectProduct);

        restProjectProductMockMvc.perform(put("/api/project-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectProductDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectProduct in the database
        List<ProjectProduct> projectProductList = projectProductRepository.findAll();
        assertThat(projectProductList).hasSize(databaseSizeBeforeUpdate);
        ProjectProduct testProjectProduct = projectProductList.get(projectProductList.size() - 1);
        assertThat(testProjectProduct.getSpecialPrice()).isEqualTo(UPDATED_SPECIAL_PRICE);

        // Validate the ProjectProduct in Elasticsearch
        verify(mockProjectProductSearchRepository, times(1)).save(testProjectProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectProduct() throws Exception {
        int databaseSizeBeforeUpdate = projectProductRepository.findAll().size();

        // Create the ProjectProduct
        ProjectProductDTO projectProductDTO = projectProductMapper.toDto(projectProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectProductMockMvc.perform(put("/api/project-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectProduct in the database
        List<ProjectProduct> projectProductList = projectProductRepository.findAll();
        assertThat(projectProductList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProjectProduct in Elasticsearch
        verify(mockProjectProductSearchRepository, times(0)).save(projectProduct);
    }

    @Test
    @Transactional
    public void deleteProjectProduct() throws Exception {
        // Initialize the database
        projectProductRepository.saveAndFlush(projectProduct);

        int databaseSizeBeforeDelete = projectProductRepository.findAll().size();

        // Delete the projectProduct
        restProjectProductMockMvc.perform(delete("/api/project-products/{id}", projectProduct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectProduct> projectProductList = projectProductRepository.findAll();
        assertThat(projectProductList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProjectProduct in Elasticsearch
        verify(mockProjectProductSearchRepository, times(1)).deleteById(projectProduct.getId());
    }

    @Test
    @Transactional
    public void searchProjectProduct() throws Exception {
        // Initialize the database
        projectProductRepository.saveAndFlush(projectProduct);
        when(mockProjectProductSearchRepository.search(queryStringQuery("id:" + projectProduct.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(projectProduct), PageRequest.of(0, 1), 1));
        // Search the projectProduct
        restProjectProductMockMvc.perform(get("/api/_search/project-products?query=id:" + projectProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].specialPrice").value(hasItem(DEFAULT_SPECIAL_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectProduct.class);
        ProjectProduct projectProduct1 = new ProjectProduct();
        projectProduct1.setId(1L);
        ProjectProduct projectProduct2 = new ProjectProduct();
        projectProduct2.setId(projectProduct1.getId());
        assertThat(projectProduct1).isEqualTo(projectProduct2);
        projectProduct2.setId(2L);
        assertThat(projectProduct1).isNotEqualTo(projectProduct2);
        projectProduct1.setId(null);
        assertThat(projectProduct1).isNotEqualTo(projectProduct2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectProductDTO.class);
        ProjectProductDTO projectProductDTO1 = new ProjectProductDTO();
        projectProductDTO1.setId(1L);
        ProjectProductDTO projectProductDTO2 = new ProjectProductDTO();
        assertThat(projectProductDTO1).isNotEqualTo(projectProductDTO2);
        projectProductDTO2.setId(projectProductDTO1.getId());
        assertThat(projectProductDTO1).isEqualTo(projectProductDTO2);
        projectProductDTO2.setId(2L);
        assertThat(projectProductDTO1).isNotEqualTo(projectProductDTO2);
        projectProductDTO1.setId(null);
        assertThat(projectProductDTO1).isNotEqualTo(projectProductDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectProductMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectProductMapper.fromId(null)).isNull();
    }
}
