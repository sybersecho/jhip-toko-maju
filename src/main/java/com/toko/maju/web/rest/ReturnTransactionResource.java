package com.toko.maju.web.rest;
import com.toko.maju.service.ReturnTransactionService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.ReturnTransactionDTO;
import com.toko.maju.service.dto.ReturnTransactionCriteria;
import com.toko.maju.service.ReturnTransactionQueryService;
import com.toko.maju.web.rest.vm.ReturnTransactionVM;
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
 * REST controller for managing ReturnTransaction.
 */
@RestController
@RequestMapping("/api")
public class ReturnTransactionResource {

    private final Logger log = LoggerFactory.getLogger(ReturnTransactionResource.class);

    private static final String ENTITY_NAME = "returnTransaction";

    private final ReturnTransactionService returnTransactionService;

    private final ReturnTransactionQueryService returnTransactionQueryService;

    public ReturnTransactionResource(ReturnTransactionService returnTransactionService, ReturnTransactionQueryService returnTransactionQueryService) {
        this.returnTransactionService = returnTransactionService;
        this.returnTransactionQueryService = returnTransactionQueryService;
    }

    /**
     * POST  /return-transactions : Create a new returnTransaction.
     *
     * @param returnTransactionVM the returnTransactionVM to create
     * @return the ResponseEntity with status 201 (Created) and with body the new returnTransactionVM, or with status 400 (Bad Request) if the returnTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/return-transactions")
    public ResponseEntity<ReturnTransactionDTO> createReturnTransaction(@Valid @RequestBody ReturnTransactionVM returnTransactionVM) throws URISyntaxException {
        log.debug("REST request to save ReturnTransaction : {}", returnTransactionVM);
        if (returnTransactionVM.getId() != null) {
            throw new BadRequestAlertException("A new returnTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        log.debug("is case return: {}",returnTransactionVM.getCashReturn());
        ReturnTransactionDTO result = returnTransactionService.save(returnTransactionVM);

        // TODO:: SAVE CASE RETURN IF TRUE
        return ResponseEntity.created(new URI("/api/return-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /return-transactions : Updates an existing returnTransaction.
     *
     * @param returnTransactionDTO the returnTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated returnTransactionDTO,
     * or with status 400 (Bad Request) if the returnTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the returnTransactionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/return-transactions")
    public ResponseEntity<ReturnTransactionDTO> updateReturnTransaction(@Valid @RequestBody ReturnTransactionDTO returnTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update ReturnTransaction : {}", returnTransactionDTO);
        if (returnTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReturnTransactionDTO result = returnTransactionService.save(returnTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, returnTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /return-transactions : get all the returnTransactions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of returnTransactions in body
     */
    @GetMapping("/return-transactions")
    public ResponseEntity<List<ReturnTransactionDTO>> getAllReturnTransactions(ReturnTransactionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReturnTransactions by criteria: {}", criteria);
        Page<ReturnTransactionDTO> page = returnTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/return-transactions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /return-transactions/count : count all the returnTransactions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/return-transactions/count")
    public ResponseEntity<Long> countReturnTransactions(ReturnTransactionCriteria criteria) {
        log.debug("REST request to count ReturnTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(returnTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /return-transactions/:id : get the "id" returnTransaction.
     *
     * @param id the id of the returnTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the returnTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/return-transactions/{id}")
    public ResponseEntity<ReturnTransactionDTO> getReturnTransaction(@PathVariable Long id) {
        log.debug("REST request to get ReturnTransaction : {}", id);
        Optional<ReturnTransactionDTO> returnTransactionDTO = returnTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(returnTransactionDTO);
    }

    /**
     * DELETE  /return-transactions/:id : delete the "id" returnTransaction.
     *
     * @param id the id of the returnTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/return-transactions/{id}")
    public ResponseEntity<Void> deleteReturnTransaction(@PathVariable Long id) {
        log.debug("REST request to delete ReturnTransaction : {}", id);
        returnTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/return-transactions?query=:query : search for the returnTransaction corresponding
     * to the query.
     *
     * @param query the query of the returnTransaction search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/return-transactions")
    public ResponseEntity<List<ReturnTransactionDTO>> searchReturnTransactions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReturnTransactions for query {}", query);
        Page<ReturnTransactionDTO> page = returnTransactionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/return-transactions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
