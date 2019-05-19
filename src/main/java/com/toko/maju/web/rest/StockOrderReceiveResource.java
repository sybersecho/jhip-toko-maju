package com.toko.maju.web.rest;
import com.toko.maju.service.StockOrderReceiveService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.StockOrderReceiveDTO;
import com.toko.maju.service.dto.StockOrderReceiveCriteria;
import com.toko.maju.service.StockOrderReceiveQueryService;
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
 * REST controller for managing StockOrderReceive.
 */
@RestController
@RequestMapping("/api")
public class StockOrderReceiveResource {

    private final Logger log = LoggerFactory.getLogger(StockOrderReceiveResource.class);

    private static final String ENTITY_NAME = "stockOrderReceive";

    private final StockOrderReceiveService stockOrderReceiveService;

    private final StockOrderReceiveQueryService stockOrderReceiveQueryService;

    public StockOrderReceiveResource(StockOrderReceiveService stockOrderReceiveService, StockOrderReceiveQueryService stockOrderReceiveQueryService) {
        this.stockOrderReceiveService = stockOrderReceiveService;
        this.stockOrderReceiveQueryService = stockOrderReceiveQueryService;
    }

    /**
     * POST  /stock-order-receives : Create a new stockOrderReceive.
     *
     * @param stockOrderReceiveDTO the stockOrderReceiveDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOrderReceiveDTO, or with status 400 (Bad Request) if the stockOrderReceive has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-order-receives")
    public ResponseEntity<StockOrderReceiveDTO> createStockOrderReceive(@RequestBody StockOrderReceiveDTO stockOrderReceiveDTO) throws URISyntaxException {
        log.debug("REST request to save StockOrderReceive : {}", stockOrderReceiveDTO);
        if (stockOrderReceiveDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockOrderReceive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockOrderReceiveDTO result = stockOrderReceiveService.save(stockOrderReceiveDTO);
        return ResponseEntity.created(new URI("/api/stock-order-receives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-order-receives : Updates an existing stockOrderReceive.
     *
     * @param stockOrderReceiveDTO the stockOrderReceiveDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOrderReceiveDTO,
     * or with status 400 (Bad Request) if the stockOrderReceiveDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOrderReceiveDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-order-receives")
    public ResponseEntity<StockOrderReceiveDTO> updateStockOrderReceive(@RequestBody StockOrderReceiveDTO stockOrderReceiveDTO) throws URISyntaxException {
        log.debug("REST request to update StockOrderReceive : {}", stockOrderReceiveDTO);
        if (stockOrderReceiveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockOrderReceiveDTO result = stockOrderReceiveService.save(stockOrderReceiveDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOrderReceiveDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-order-receives : get all the stockOrderReceives.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of stockOrderReceives in body
     */
    @GetMapping("/stock-order-receives")
    public ResponseEntity<List<StockOrderReceiveDTO>> getAllStockOrderReceives(StockOrderReceiveCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StockOrderReceives by criteria: {}", criteria);
        Page<StockOrderReceiveDTO> page = stockOrderReceiveQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-order-receives");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /stock-order-receives/count : count all the stockOrderReceives.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/stock-order-receives/count")
    public ResponseEntity<Long> countStockOrderReceives(StockOrderReceiveCriteria criteria) {
        log.debug("REST request to count StockOrderReceives by criteria: {}", criteria);
        return ResponseEntity.ok().body(stockOrderReceiveQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /stock-order-receives/:id : get the "id" stockOrderReceive.
     *
     * @param id the id of the stockOrderReceiveDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOrderReceiveDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-order-receives/{id}")
    public ResponseEntity<StockOrderReceiveDTO> getStockOrderReceive(@PathVariable Long id) {
        log.debug("REST request to get StockOrderReceive : {}", id);
        Optional<StockOrderReceiveDTO> stockOrderReceiveDTO = stockOrderReceiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockOrderReceiveDTO);
    }

    /**
     * DELETE  /stock-order-receives/:id : delete the "id" stockOrderReceive.
     *
     * @param id the id of the stockOrderReceiveDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-order-receives/{id}")
    public ResponseEntity<Void> deleteStockOrderReceive(@PathVariable Long id) {
        log.debug("REST request to delete StockOrderReceive : {}", id);
        stockOrderReceiveService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stock-order-receives?query=:query : search for the stockOrderReceive corresponding
     * to the query.
     *
     * @param query the query of the stockOrderReceive search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stock-order-receives")
    public ResponseEntity<List<StockOrderReceiveDTO>> searchStockOrderReceives(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StockOrderReceives for query {}", query);
        Page<StockOrderReceiveDTO> page = stockOrderReceiveService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stock-order-receives");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }



    /**
     * POST  /stock-order-receives : Create a new stockOrderReceive.
     *
     * @param stockOrderReceiveDTOS the stockOrderReceiveDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOrderReceiveDTO, or with status 400 (Bad Request) if the stockOrderReceive has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-order-receives/orders")
    public ResponseEntity<List<StockOrderReceiveDTO>> createStockOrderReceives(@RequestBody List<StockOrderReceiveDTO> stockOrderReceiveDTOS) throws URISyntaxException {
        log.debug("REST request to save StockOrderReceives : {}", stockOrderReceiveDTOS);
        if (stockOrderReceiveDTOS.isEmpty()) {
            throw new BadRequestAlertException("A new stockOrderReceive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stockOrderReceiveDTOS = stockOrderReceiveService.saveAll(stockOrderReceiveDTOS);
        return ResponseEntity.ok(stockOrderReceiveDTOS);
    }

}
