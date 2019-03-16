package com.toko.maju.web.rest;
import com.toko.maju.service.SaleItemService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.SaleItemDTO;
import com.toko.maju.service.dto.SaleItemCriteria;
import com.toko.maju.service.SaleItemQueryService;
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
 * REST controller for managing SaleItem.
 */
@RestController
@RequestMapping("/api")
public class SaleItemResource {

    private final Logger log = LoggerFactory.getLogger(SaleItemResource.class);

    private static final String ENTITY_NAME = "saleItem";

    private final SaleItemService saleItemService;

    private final SaleItemQueryService saleItemQueryService;

    public SaleItemResource(SaleItemService saleItemService, SaleItemQueryService saleItemQueryService) {
        this.saleItemService = saleItemService;
        this.saleItemQueryService = saleItemQueryService;
    }

    /**
     * POST  /sale-items : Create a new saleItem.
     *
     * @param saleItemDTO the saleItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saleItemDTO, or with status 400 (Bad Request) if the saleItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-items")
    public ResponseEntity<SaleItemDTO> createSaleItem(@Valid @RequestBody SaleItemDTO saleItemDTO) throws URISyntaxException {
        log.debug("REST request to save SaleItem : {}", saleItemDTO);
        if (saleItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new saleItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaleItemDTO result = saleItemService.save(saleItemDTO);
        return ResponseEntity.created(new URI("/api/sale-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-items : Updates an existing saleItem.
     *
     * @param saleItemDTO the saleItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saleItemDTO,
     * or with status 400 (Bad Request) if the saleItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the saleItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-items")
    public ResponseEntity<SaleItemDTO> updateSaleItem(@Valid @RequestBody SaleItemDTO saleItemDTO) throws URISyntaxException {
        log.debug("REST request to update SaleItem : {}", saleItemDTO);
        if (saleItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SaleItemDTO result = saleItemService.save(saleItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saleItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sale-items : get all the saleItems.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of saleItems in body
     */
    @GetMapping("/sale-items")
    public ResponseEntity<List<SaleItemDTO>> getAllSaleItems(SaleItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SaleItems by criteria: {}", criteria);
        Page<SaleItemDTO> page = saleItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sale-items");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /sale-items/count : count all the saleItems.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/sale-items/count")
    public ResponseEntity<Long> countSaleItems(SaleItemCriteria criteria) {
        log.debug("REST request to count SaleItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(saleItemQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /sale-items/:id : get the "id" saleItem.
     *
     * @param id the id of the saleItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saleItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sale-items/{id}")
    public ResponseEntity<SaleItemDTO> getSaleItem(@PathVariable Long id) {
        log.debug("REST request to get SaleItem : {}", id);
        Optional<SaleItemDTO> saleItemDTO = saleItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saleItemDTO);
    }

    /**
     * DELETE  /sale-items/:id : delete the "id" saleItem.
     *
     * @param id the id of the saleItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-items/{id}")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable Long id) {
        log.debug("REST request to delete SaleItem : {}", id);
        saleItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sale-items?query=:query : search for the saleItem corresponding
     * to the query.
     *
     * @param query the query of the saleItem search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sale-items")
    public ResponseEntity<List<SaleItemDTO>> searchSaleItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SaleItems for query {}", query);
        Page<SaleItemDTO> page = saleItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sale-items");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
