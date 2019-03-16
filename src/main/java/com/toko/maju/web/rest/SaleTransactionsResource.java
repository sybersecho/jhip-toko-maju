package com.toko.maju.web.rest;
import com.toko.maju.service.SaleTransactionsService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.SaleTransactionsDTO;
import com.toko.maju.service.dto.SaleTransactionsCriteria;
import com.toko.maju.service.SaleTransactionsQueryService;
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
 * REST controller for managing SaleTransactions.
 */
@RestController
@RequestMapping("/api")
public class SaleTransactionsResource {

    private final Logger log = LoggerFactory.getLogger(SaleTransactionsResource.class);

    private static final String ENTITY_NAME = "saleTransactions";

    private final SaleTransactionsService saleTransactionsService;

    private final SaleTransactionsQueryService saleTransactionsQueryService;

    public SaleTransactionsResource(SaleTransactionsService saleTransactionsService, SaleTransactionsQueryService saleTransactionsQueryService) {
        this.saleTransactionsService = saleTransactionsService;
        this.saleTransactionsQueryService = saleTransactionsQueryService;
    }

    /**
     * POST  /sale-transactions : Create a new saleTransactions.
     *
     * @param saleTransactionsDTO the saleTransactionsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saleTransactionsDTO, or with status 400 (Bad Request) if the saleTransactions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-transactions")
    public ResponseEntity<SaleTransactionsDTO> createSaleTransactions(@Valid @RequestBody SaleTransactionsDTO saleTransactionsDTO) throws URISyntaxException {
        log.debug("REST request to save SaleTransactions : {}", saleTransactionsDTO);
        if (saleTransactionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new saleTransactions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaleTransactionsDTO result = saleTransactionsService.save(saleTransactionsDTO);
        return ResponseEntity.created(new URI("/api/sale-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-transactions : Updates an existing saleTransactions.
     *
     * @param saleTransactionsDTO the saleTransactionsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saleTransactionsDTO,
     * or with status 400 (Bad Request) if the saleTransactionsDTO is not valid,
     * or with status 500 (Internal Server Error) if the saleTransactionsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-transactions")
    public ResponseEntity<SaleTransactionsDTO> updateSaleTransactions(@Valid @RequestBody SaleTransactionsDTO saleTransactionsDTO) throws URISyntaxException {
        log.debug("REST request to update SaleTransactions : {}", saleTransactionsDTO);
        if (saleTransactionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SaleTransactionsDTO result = saleTransactionsService.save(saleTransactionsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saleTransactionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sale-transactions : get all the saleTransactions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of saleTransactions in body
     */
    @GetMapping("/sale-transactions")
    public ResponseEntity<List<SaleTransactionsDTO>> getAllSaleTransactions(SaleTransactionsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SaleTransactions by criteria: {}", criteria);
        Page<SaleTransactionsDTO> page = saleTransactionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sale-transactions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /sale-transactions/count : count all the saleTransactions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/sale-transactions/count")
    public ResponseEntity<Long> countSaleTransactions(SaleTransactionsCriteria criteria) {
        log.debug("REST request to count SaleTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(saleTransactionsQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /sale-transactions/:id : get the "id" saleTransactions.
     *
     * @param id the id of the saleTransactionsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saleTransactionsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sale-transactions/{id}")
    public ResponseEntity<SaleTransactionsDTO> getSaleTransactions(@PathVariable Long id) {
        log.debug("REST request to get SaleTransactions : {}", id);
        Optional<SaleTransactionsDTO> saleTransactionsDTO = saleTransactionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saleTransactionsDTO);
    }

    /**
     * DELETE  /sale-transactions/:id : delete the "id" saleTransactions.
     *
     * @param id the id of the saleTransactionsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-transactions/{id}")
    public ResponseEntity<Void> deleteSaleTransactions(@PathVariable Long id) {
        log.debug("REST request to delete SaleTransactions : {}", id);
        saleTransactionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sale-transactions?query=:query : search for the saleTransactions corresponding
     * to the query.
     *
     * @param query the query of the saleTransactions search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sale-transactions")
    public ResponseEntity<List<SaleTransactionsDTO>> searchSaleTransactions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SaleTransactions for query {}", query);
        Page<SaleTransactionsDTO> page = saleTransactionsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sale-transactions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
