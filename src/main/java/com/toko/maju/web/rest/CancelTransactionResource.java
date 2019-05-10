package com.toko.maju.web.rest;
import com.toko.maju.service.CancelTransactionService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.CancelTransactionDTO;
import com.toko.maju.service.dto.CancelTransactionCriteria;
import com.toko.maju.service.CancelTransactionQueryService;
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
 * REST controller for managing CancelTransaction.
 */
@RestController
@RequestMapping("/api")
public class CancelTransactionResource {

    private final Logger log = LoggerFactory.getLogger(CancelTransactionResource.class);

    private static final String ENTITY_NAME = "cancelTransaction";

    private final CancelTransactionService cancelTransactionService;

    private final CancelTransactionQueryService cancelTransactionQueryService;

    public CancelTransactionResource(CancelTransactionService cancelTransactionService, CancelTransactionQueryService cancelTransactionQueryService) {
        this.cancelTransactionService = cancelTransactionService;
        this.cancelTransactionQueryService = cancelTransactionQueryService;
    }

    /**
     * POST  /cancel-transactions : Create a new cancelTransaction.
     *
     * @param cancelTransactionDTO the cancelTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cancelTransactionDTO, or with status 400 (Bad Request) if the cancelTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cancel-transactions")
    public ResponseEntity<CancelTransactionDTO> createCancelTransaction(@Valid @RequestBody CancelTransactionDTO cancelTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save CancelTransaction : {}", cancelTransactionDTO);
        if (cancelTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new cancelTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CancelTransactionDTO result = cancelTransactionService.save(cancelTransactionDTO);
        return ResponseEntity.created(new URI("/api/cancel-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cancel-transactions : Updates an existing cancelTransaction.
     *
     * @param cancelTransactionDTO the cancelTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cancelTransactionDTO,
     * or with status 400 (Bad Request) if the cancelTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the cancelTransactionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cancel-transactions")
    public ResponseEntity<CancelTransactionDTO> updateCancelTransaction(@Valid @RequestBody CancelTransactionDTO cancelTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update CancelTransaction : {}", cancelTransactionDTO);
        if (cancelTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CancelTransactionDTO result = cancelTransactionService.save(cancelTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cancelTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cancel-transactions : get all the cancelTransactions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of cancelTransactions in body
     */
    @GetMapping("/cancel-transactions")
    public ResponseEntity<List<CancelTransactionDTO>> getAllCancelTransactions(CancelTransactionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CancelTransactions by criteria: {}", criteria);
        Page<CancelTransactionDTO> page = cancelTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cancel-transactions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /cancel-transactions/count : count all the cancelTransactions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/cancel-transactions/count")
    public ResponseEntity<Long> countCancelTransactions(CancelTransactionCriteria criteria) {
        log.debug("REST request to count CancelTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(cancelTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /cancel-transactions/:id : get the "id" cancelTransaction.
     *
     * @param id the id of the cancelTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cancelTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cancel-transactions/{id}")
    public ResponseEntity<CancelTransactionDTO> getCancelTransaction(@PathVariable Long id) {
        log.debug("REST request to get CancelTransaction : {}", id);
        Optional<CancelTransactionDTO> cancelTransactionDTO = cancelTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cancelTransactionDTO);
    }

    /**
     * DELETE  /cancel-transactions/:id : delete the "id" cancelTransaction.
     *
     * @param id the id of the cancelTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cancel-transactions/{id}")
    public ResponseEntity<Void> deleteCancelTransaction(@PathVariable Long id) {
        log.debug("REST request to delete CancelTransaction : {}", id);
        cancelTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cancel-transactions?query=:query : search for the cancelTransaction corresponding
     * to the query.
     *
     * @param query the query of the cancelTransaction search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cancel-transactions")
    public ResponseEntity<List<CancelTransactionDTO>> searchCancelTransactions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CancelTransactions for query {}", query);
        Page<CancelTransactionDTO> page = cancelTransactionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cancel-transactions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
