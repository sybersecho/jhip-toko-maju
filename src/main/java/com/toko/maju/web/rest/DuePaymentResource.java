package com.toko.maju.web.rest;
import com.toko.maju.service.DuePaymentService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.DuePaymentDTO;
import com.toko.maju.service.dto.DuePaymentCriteria;
import com.toko.maju.service.DuePaymentQueryService;
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
 * REST controller for managing DuePayment.
 */
@RestController
@RequestMapping("/api")
public class DuePaymentResource {

    private final Logger log = LoggerFactory.getLogger(DuePaymentResource.class);

    private static final String ENTITY_NAME = "duePayment";

    private final DuePaymentService duePaymentService;

    private final DuePaymentQueryService duePaymentQueryService;

    public DuePaymentResource(DuePaymentService duePaymentService, DuePaymentQueryService duePaymentQueryService) {
        this.duePaymentService = duePaymentService;
        this.duePaymentQueryService = duePaymentQueryService;
    }

    /**
     * POST  /due-payments : Create a new duePayment.
     *
     * @param duePaymentDTO the duePaymentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new duePaymentDTO, or with status 400 (Bad Request) if the duePayment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/due-payments")
    public ResponseEntity<DuePaymentDTO> createDuePayment(@Valid @RequestBody DuePaymentDTO duePaymentDTO) throws URISyntaxException {
        log.debug("REST request to save DuePayment : {}", duePaymentDTO);
        if (duePaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new duePayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DuePaymentDTO result = duePaymentService.save(duePaymentDTO);
        return ResponseEntity.created(new URI("/api/due-payments/" + result.getSaleNoInvoice()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getSaleNoInvoice()))
            .body(result);
    }

    /**
     * PUT  /due-payments : Updates an existing duePayment.
     *
     * @param duePaymentDTO the duePaymentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated duePaymentDTO,
     * or with status 400 (Bad Request) if the duePaymentDTO is not valid,
     * or with status 500 (Internal Server Error) if the duePaymentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/due-payments")
    public ResponseEntity<DuePaymentDTO> updateDuePayment(@Valid @RequestBody DuePaymentDTO duePaymentDTO) throws URISyntaxException {
        log.debug("REST request to update DuePayment : {}", duePaymentDTO);
        if (duePaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DuePaymentDTO result = duePaymentService.save(duePaymentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, duePaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /due-payments : get all the duePayments.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of duePayments in body
     */
    @GetMapping("/due-payments")
    public ResponseEntity<List<DuePaymentDTO>> getAllDuePayments(DuePaymentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DuePayments by criteria: {}", criteria);
        Page<DuePaymentDTO> page = duePaymentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/due-payments");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /due-payments/count : count all the duePayments.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/due-payments/count")
    public ResponseEntity<Long> countDuePayments(DuePaymentCriteria criteria) {
        log.debug("REST request to count DuePayments by criteria: {}", criteria);
        return ResponseEntity.ok().body(duePaymentQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /due-payments/:id : get the "id" duePayment.
     *
     * @param id the id of the duePaymentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the duePaymentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/due-payments/{id}")
    public ResponseEntity<DuePaymentDTO> getDuePayment(@PathVariable Long id) {
        log.debug("REST request to get DuePayment : {}", id);
        Optional<DuePaymentDTO> duePaymentDTO = duePaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(duePaymentDTO);
    }

    /**
     * DELETE  /due-payments/:id : delete the "id" duePayment.
     *
     * @param id the id of the duePaymentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/due-payments/{id}")
    public ResponseEntity<Void> deleteDuePayment(@PathVariable Long id) {
        log.debug("REST request to delete DuePayment : {}", id);
        duePaymentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/due-payments?query=:query : search for the duePayment corresponding
     * to the query.
     *
     * @param query the query of the duePayment search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/due-payments")
    public ResponseEntity<List<DuePaymentDTO>> searchDuePayments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DuePayments for query {}", query);
        Page<DuePaymentDTO> page = duePaymentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/due-payments");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
