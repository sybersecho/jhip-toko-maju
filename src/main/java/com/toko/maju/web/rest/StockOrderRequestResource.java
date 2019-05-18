package com.toko.maju.web.rest;
import com.toko.maju.service.StockOrderRequestService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.StockOrderRequestDTO;
import com.toko.maju.service.dto.StockOrderRequestCriteria;
import com.toko.maju.service.StockOrderRequestQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing StockOrderRequest.
 */
@RestController
@RequestMapping("/api")
public class StockOrderRequestResource {

    private final Logger log = LoggerFactory.getLogger(StockOrderRequestResource.class);

    private static final String ENTITY_NAME = "stockOrderRequest";

    private final StockOrderRequestService stockOrderRequestService;

    private final StockOrderRequestQueryService stockOrderRequestQueryService;

    public StockOrderRequestResource(StockOrderRequestService stockOrderRequestService, StockOrderRequestQueryService stockOrderRequestQueryService) {
        this.stockOrderRequestService = stockOrderRequestService;
        this.stockOrderRequestQueryService = stockOrderRequestQueryService;
    }

    /**
     * POST  /stock-order-requests : Create a new stockOrderRequest.
     *
     * @param stockOrderRequestDTO the stockOrderRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOrderRequestDTO, or with status 400 (Bad Request) if the stockOrderRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-order-requests")
    public ResponseEntity<StockOrderRequestDTO> createStockOrderRequest(@RequestBody StockOrderRequestDTO stockOrderRequestDTO) throws URISyntaxException {
        log.debug("REST request to save StockOrderRequest : {}", stockOrderRequestDTO);
        if (stockOrderRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockOrderRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockOrderRequestDTO result = stockOrderRequestService.save(stockOrderRequestDTO);
        return ResponseEntity.created(new URI("/api/stock-order-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-order-requests : Updates an existing stockOrderRequest.
     *
     * @param stockOrderRequestDTO the stockOrderRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOrderRequestDTO,
     * or with status 400 (Bad Request) if the stockOrderRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOrderRequestDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-order-requests")
    public ResponseEntity<StockOrderRequestDTO> updateStockOrderRequest(@RequestBody StockOrderRequestDTO stockOrderRequestDTO) throws URISyntaxException {
        log.debug("REST request to update StockOrderRequest : {}", stockOrderRequestDTO);
        if (stockOrderRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockOrderRequestDTO result = stockOrderRequestService.save(stockOrderRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOrderRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-order-requests : get all the stockOrderRequests.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of stockOrderRequests in body
     */
    @GetMapping("/stock-order-requests")
    public ResponseEntity<List<StockOrderRequestDTO>> getAllStockOrderRequests(StockOrderRequestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StockOrderRequests by criteria: {}", criteria);
        Page<StockOrderRequestDTO> page = stockOrderRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-order-requests");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /stock-order-requests/count : count all the stockOrderRequests.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/stock-order-requests/count")
    public ResponseEntity<Long> countStockOrderRequests(StockOrderRequestCriteria criteria) {
        log.debug("REST request to count StockOrderRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(stockOrderRequestQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /stock-order-requests/:id : get the "id" stockOrderRequest.
     *
     * @param id the id of the stockOrderRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOrderRequestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-order-requests/{id}")
    public ResponseEntity<StockOrderRequestDTO> getStockOrderRequest(@PathVariable Long id) {
        log.debug("REST request to get StockOrderRequest : {}", id);
        Optional<StockOrderRequestDTO> stockOrderRequestDTO = stockOrderRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockOrderRequestDTO);
    }

    /**
     * DELETE  /stock-order-requests/:id : delete the "id" stockOrderRequest.
     *
     * @param id the id of the stockOrderRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-order-requests/{id}")
    public ResponseEntity<Void> deleteStockOrderRequest(@PathVariable Long id) {
        log.debug("REST request to delete StockOrderRequest : {}", id);
        stockOrderRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stock-order-requests?query=:query : search for the stockOrderRequest corresponding
     * to the query.
     *
     * @param query the query of the stockOrderRequest search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stock-order-requests")
    public ResponseEntity<List<StockOrderRequestDTO>> searchStockOrderRequests(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StockOrderRequests for query {}", query);
        Page<StockOrderRequestDTO> page = stockOrderRequestService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stock-order-requests");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
