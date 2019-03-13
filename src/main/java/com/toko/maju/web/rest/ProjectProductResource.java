package com.toko.maju.web.rest;
import com.toko.maju.service.ProjectProductService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.ProjectProductDTO;
import com.toko.maju.service.dto.ProjectProductCriteria;
import com.toko.maju.service.ProjectProductQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProjectProduct.
 */
@RestController
@RequestMapping("/api")
public class ProjectProductResource {

    private final Logger log = LoggerFactory.getLogger(ProjectProductResource.class);

    private static final String ENTITY_NAME = "projectProduct";
    
    private static final String PARENT_ENTITY_NAME = "project";

    private final ProjectProductService projectProductService;

    private final ProjectProductQueryService projectProductQueryService;

    public ProjectProductResource(ProjectProductService projectProductService, ProjectProductQueryService projectProductQueryService) {
        this.projectProductService = projectProductService;
        this.projectProductQueryService = projectProductQueryService;
    }

    /**
     * POST  /project-products : Create a new projectProduct.
     *
     * @param projectProductDTO the projectProductDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectProductDTO, or with status 400 (Bad Request) if the projectProduct has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-products")
    public ResponseEntity<ProjectProductDTO> createProjectProduct(@Valid @RequestBody ProjectProductDTO projectProductDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectProduct : {}", projectProductDTO);
        if (projectProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectProductDTO result = projectProductService.save(projectProductDTO);
        return ResponseEntity.created(new URI("/api/project-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-products : Updates an existing projectProduct.
     *
     * @param projectProductDTO the projectProductDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectProductDTO,
     * or with status 400 (Bad Request) if the projectProductDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectProductDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-products")
    public ResponseEntity<ProjectProductDTO> updateProjectProduct(@Valid @RequestBody ProjectProductDTO projectProductDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectProduct : {}", projectProductDTO);
        if (projectProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProjectProductDTO result = projectProductService.save(projectProductDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectProductDTO.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /project-products : Updates an existing projectProduct.
     *
     * @param projectProductDTO the projectProductDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectProductDTO,
     * or with status 400 (Bad Request) if the projectProductDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectProductDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-products/products")
    public ResponseEntity<ProjectProductDTO> updateProjectProducts(@Valid @RequestBody List<ProjectProductDTO> projectProductDTOs) throws URISyntaxException {
        log.debug("REST request to update ProjectProduct : {}", projectProductDTOs.size());
        if (projectProductDTOs.size() <= 0) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProjectProductDTO result = projectProductService.batchSave(projectProductDTOs);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(PARENT_ENTITY_NAME, result.getProjectName()))
            .body(result);
    }

    /**
     * GET  /project-products : get all the projectProducts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of projectProducts in body
     */
    @GetMapping("/project-products")
    public ResponseEntity<List<ProjectProductDTO>> getAllProjectProducts(ProjectProductCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProjectProducts by criteria: {}", criteria);
        Page<ProjectProductDTO> page = projectProductQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /project-products/count : count all the projectProducts.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/project-products/count")
    public ResponseEntity<Long> countProjectProducts(ProjectProductCriteria criteria) {
        log.debug("REST request to count ProjectProducts by criteria: {}", criteria);
        return ResponseEntity.ok().body(projectProductQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /project-products/:id : get the "id" projectProduct.
     *
     * @param id the id of the projectProductDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectProductDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-products/{id}")
    public ResponseEntity<ProjectProductDTO> getProjectProduct(@PathVariable Long id) {
        log.debug("REST request to get ProjectProduct : {}", id);
        Optional<ProjectProductDTO> projectProductDTO = projectProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectProductDTO);
    }

    /**
     * DELETE  /project-products/:id : delete the "id" projectProduct.
     *
     * @param id the id of the projectProductDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-products/{id}")
    public ResponseEntity<Void> deleteProjectProduct(@PathVariable Long id) {
        log.debug("REST request to delete ProjectProduct : {}", id);
        projectProductService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/project-products?query=:query : search for the projectProduct corresponding
     * to the query.
     *
     * @param query the query of the projectProduct search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/project-products")
    public ResponseEntity<List<ProjectProductDTO>> searchProjectProducts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProjectProducts for query {}", query);
        Page<ProjectProductDTO> page = projectProductService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
