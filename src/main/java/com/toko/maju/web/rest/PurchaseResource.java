package com.toko.maju.web.rest;
import com.toko.maju.service.PurchaseService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.PurchaseDTO;
import com.toko.maju.service.dto.PurchaseCriteria;
import com.toko.maju.service.PurchaseQueryService;
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
 * REST controller for managing Purchase.
 */
@RestController
@RequestMapping("/api")
public class PurchaseResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseResource.class);

    private static final String ENTITY_NAME = "purchase";

    private final PurchaseService purchaseService;

    private final PurchaseQueryService purchaseQueryService;

    public PurchaseResource(PurchaseService purchaseService, PurchaseQueryService purchaseQueryService) {
        this.purchaseService = purchaseService;
        this.purchaseQueryService = purchaseQueryService;
    }

    /**
     * POST  /purchases : Create a new purchase.
     *
     * @param purchaseDTO the purchaseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchaseDTO, or with status 400 (Bad Request) if the purchase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purchases")
    public ResponseEntity<PurchaseDTO> createPurchase(@Valid @RequestBody PurchaseDTO purchaseDTO) throws URISyntaxException {
        log.debug("REST request to save Purchase : {}", purchaseDTO);
        if (purchaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseDTO result = purchaseService.save(purchaseDTO);
        return ResponseEntity.created(new URI("/api/purchases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchases : Updates an existing purchase.
     *
     * @param purchaseDTO the purchaseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchaseDTO,
     * or with status 400 (Bad Request) if the purchaseDTO is not valid,
     * or with status 500 (Internal Server Error) if the purchaseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purchases")
    public ResponseEntity<PurchaseDTO> updatePurchase(@Valid @RequestBody PurchaseDTO purchaseDTO) throws URISyntaxException {
        log.debug("REST request to update Purchase : {}", purchaseDTO);
        if (purchaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaseDTO result = purchaseService.save(purchaseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchases : get all the purchases.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of purchases in body
     */
    @GetMapping("/purchases")
    public ResponseEntity<List<PurchaseDTO>> getAllPurchases(PurchaseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Purchases by criteria: {}", criteria);
        Page<PurchaseDTO> page = purchaseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purchases");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /purchases/count : count all the purchases.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/purchases/count")
    public ResponseEntity<Long> countPurchases(PurchaseCriteria criteria) {
        log.debug("REST request to count Purchases by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchaseQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /purchases/:id : get the "id" purchase.
     *
     * @param id the id of the purchaseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchaseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/purchases/{id}")
    public ResponseEntity<PurchaseDTO> getPurchase(@PathVariable Long id) {
        log.debug("REST request to get Purchase : {}", id);
        Optional<PurchaseDTO> purchaseDTO = purchaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseDTO);
    }

    /**
     * DELETE  /purchases/:id : delete the "id" purchase.
     *
     * @param id the id of the purchaseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchases/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        log.debug("REST request to delete Purchase : {}", id);
        purchaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/purchases?query=:query : search for the purchase corresponding
     * to the query.
     *
     * @param query the query of the purchase search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/purchases")
    public ResponseEntity<List<PurchaseDTO>> searchPurchases(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Purchases for query {}", query);
        Page<PurchaseDTO> page = purchaseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/purchases");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
