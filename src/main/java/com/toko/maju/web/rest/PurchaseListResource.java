package com.toko.maju.web.rest;
import com.toko.maju.service.PurchaseListService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.PurchaseListDTO;
import com.toko.maju.service.dto.PurchaseListCriteria;
import com.toko.maju.service.PurchaseListQueryService;
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
 * REST controller for managing PurchaseList.
 */
@RestController
@RequestMapping("/api")
public class PurchaseListResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseListResource.class);

    private static final String ENTITY_NAME = "purchaseList";

    private final PurchaseListService purchaseListService;

    private final PurchaseListQueryService purchaseListQueryService;

    public PurchaseListResource(PurchaseListService purchaseListService, PurchaseListQueryService purchaseListQueryService) {
        this.purchaseListService = purchaseListService;
        this.purchaseListQueryService = purchaseListQueryService;
    }

    /**
     * POST  /purchase-lists : Create a new purchaseList.
     *
     * @param purchaseListDTO the purchaseListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchaseListDTO, or with status 400 (Bad Request) if the purchaseList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purchase-lists")
    public ResponseEntity<PurchaseListDTO> createPurchaseList(@Valid @RequestBody PurchaseListDTO purchaseListDTO) throws URISyntaxException {
        log.debug("REST request to save PurchaseList : {}", purchaseListDTO);
        if (purchaseListDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchaseList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseListDTO result = purchaseListService.save(purchaseListDTO);
        return ResponseEntity.created(new URI("/api/purchase-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchase-lists : Updates an existing purchaseList.
     *
     * @param purchaseListDTO the purchaseListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchaseListDTO,
     * or with status 400 (Bad Request) if the purchaseListDTO is not valid,
     * or with status 500 (Internal Server Error) if the purchaseListDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purchase-lists")
    public ResponseEntity<PurchaseListDTO> updatePurchaseList(@Valid @RequestBody PurchaseListDTO purchaseListDTO) throws URISyntaxException {
        log.debug("REST request to update PurchaseList : {}", purchaseListDTO);
        if (purchaseListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaseListDTO result = purchaseListService.save(purchaseListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchaseListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchase-lists : get all the purchaseLists.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of purchaseLists in body
     */
    @GetMapping("/purchase-lists")
    public ResponseEntity<List<PurchaseListDTO>> getAllPurchaseLists(PurchaseListCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PurchaseLists by criteria: {}", criteria);
        Page<PurchaseListDTO> page = purchaseListQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purchase-lists");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /purchase-lists/count : count all the purchaseLists.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/purchase-lists/count")
    public ResponseEntity<Long> countPurchaseLists(PurchaseListCriteria criteria) {
        log.debug("REST request to count PurchaseLists by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchaseListQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /purchase-lists/:id : get the "id" purchaseList.
     *
     * @param id the id of the purchaseListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchaseListDTO, or with status 404 (Not Found)
     */
    @GetMapping("/purchase-lists/{id}")
    public ResponseEntity<PurchaseListDTO> getPurchaseList(@PathVariable Long id) {
        log.debug("REST request to get PurchaseList : {}", id);
        Optional<PurchaseListDTO> purchaseListDTO = purchaseListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseListDTO);
    }

    /**
     * DELETE  /purchase-lists/:id : delete the "id" purchaseList.
     *
     * @param id the id of the purchaseListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchase-lists/{id}")
    public ResponseEntity<Void> deletePurchaseList(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseList : {}", id);
        purchaseListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/purchase-lists?query=:query : search for the purchaseList corresponding
     * to the query.
     *
     * @param query the query of the purchaseList search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/purchase-lists")
    public ResponseEntity<List<PurchaseListDTO>> searchPurchaseLists(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PurchaseLists for query {}", query);
        Page<PurchaseListDTO> page = purchaseListService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/purchase-lists");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
