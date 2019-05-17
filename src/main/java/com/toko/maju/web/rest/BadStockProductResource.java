package com.toko.maju.web.rest;
import com.toko.maju.service.BadStockProductService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.BadStockProductDTO;
import com.toko.maju.service.dto.BadStockProductCriteria;
import com.toko.maju.service.BadStockProductQueryService;
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
 * REST controller for managing BadStockProduct.
 */
@RestController
@RequestMapping("/api")
public class BadStockProductResource {

    private final Logger log = LoggerFactory.getLogger(BadStockProductResource.class);

    private static final String ENTITY_NAME = "badStockProduct";

    private final BadStockProductService badStockProductService;

    private final BadStockProductQueryService badStockProductQueryService;

    public BadStockProductResource(BadStockProductService badStockProductService, BadStockProductQueryService badStockProductQueryService) {
        this.badStockProductService = badStockProductService;
        this.badStockProductQueryService = badStockProductQueryService;
    }

    /**
     * POST  /bad-stock-products : Create a new badStockProduct.
     *
     * @param badStockProductDTO the badStockProductDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new badStockProductDTO, or with status 400 (Bad Request) if the badStockProduct has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bad-stock-products")
    public ResponseEntity<BadStockProductDTO> createBadStockProduct(@Valid @RequestBody BadStockProductDTO badStockProductDTO) throws URISyntaxException {
        log.debug("REST request to save BadStockProduct : {}", badStockProductDTO);
        if (badStockProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new badStockProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BadStockProductDTO result = badStockProductService.save(badStockProductDTO);
        return ResponseEntity.created(new URI("/api/bad-stock-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bad-stock-products : Updates an existing badStockProduct.
     *
     * @param badStockProductDTO the badStockProductDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated badStockProductDTO,
     * or with status 400 (Bad Request) if the badStockProductDTO is not valid,
     * or with status 500 (Internal Server Error) if the badStockProductDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bad-stock-products")
    public ResponseEntity<BadStockProductDTO> updateBadStockProduct(@Valid @RequestBody BadStockProductDTO badStockProductDTO) throws URISyntaxException {
        log.debug("REST request to update BadStockProduct : {}", badStockProductDTO);
        if (badStockProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BadStockProductDTO result = badStockProductService.save(badStockProductDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, badStockProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bad-stock-products : get all the badStockProducts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of badStockProducts in body
     */
    @GetMapping("/bad-stock-products")
    public ResponseEntity<List<BadStockProductDTO>> getAllBadStockProducts(BadStockProductCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BadStockProducts by criteria: {}", criteria);
        Page<BadStockProductDTO> page = badStockProductQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bad-stock-products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /bad-stock-products/count : count all the badStockProducts.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/bad-stock-products/count")
    public ResponseEntity<Long> countBadStockProducts(BadStockProductCriteria criteria) {
        log.debug("REST request to count BadStockProducts by criteria: {}", criteria);
        return ResponseEntity.ok().body(badStockProductQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /bad-stock-products/:id : get the "id" badStockProduct.
     *
     * @param id the id of the badStockProductDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the badStockProductDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bad-stock-products/{id}")
    public ResponseEntity<BadStockProductDTO> getBadStockProduct(@PathVariable Long id) {
        log.debug("REST request to get BadStockProduct : {}", id);
        Optional<BadStockProductDTO> badStockProductDTO = badStockProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(badStockProductDTO);
    }

    /**
     * DELETE  /bad-stock-products/:id : delete the "id" badStockProduct.
     *
     * @param id the id of the badStockProductDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bad-stock-products/{id}")
    public ResponseEntity<Void> deleteBadStockProduct(@PathVariable Long id) {
        log.debug("REST request to delete BadStockProduct : {}", id);
        badStockProductService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bad-stock-products?query=:query : search for the badStockProduct corresponding
     * to the query.
     *
     * @param query the query of the badStockProduct search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bad-stock-products")
    public ResponseEntity<List<BadStockProductDTO>> searchBadStockProducts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BadStockProducts for query {}", query);
        Page<BadStockProductDTO> page = badStockProductService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bad-stock-products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
