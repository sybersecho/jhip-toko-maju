package com.toko.maju.web.rest;

import com.toko.maju.security.SecurityUtils;
import com.toko.maju.service.StockOrderProcessService;
import com.toko.maju.service.UserService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.StockOrderProcessDTO;
import com.toko.maju.service.dto.StockOrderProcessCriteria;
import com.toko.maju.service.StockOrderProcessQueryService;
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

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing StockOrderProcess.
 */
@RestController
@RequestMapping("/api")
public class StockOrderProcessResource {

    private final Logger log = LoggerFactory.getLogger(StockOrderProcessResource.class);

    private static final String ENTITY_NAME = "stockOrderProcess";

    private final StockOrderProcessService stockOrderProcessService;

    private final StockOrderProcessQueryService stockOrderProcessQueryService;

    public StockOrderProcessResource(StockOrderProcessService stockOrderProcessService, StockOrderProcessQueryService stockOrderProcessQueryService) {
        this.stockOrderProcessService = stockOrderProcessService;
        this.stockOrderProcessQueryService = stockOrderProcessQueryService;
    }

    /**
     * POST  /stock-order-processes : Create a new stockOrderProcess.
     *
     * @param stockOrderProcessDTO the stockOrderProcessDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOrderProcessDTO, or with status 400 (Bad Request) if the stockOrderProcess has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-order-processes")
    public ResponseEntity<StockOrderProcessDTO> createStockOrderProcess(@RequestBody StockOrderProcessDTO stockOrderProcessDTO) throws URISyntaxException {
        log.debug("REST request to save StockOrderProcess : {}", stockOrderProcessDTO);
        if (stockOrderProcessDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockOrderProcess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockOrderProcessDTO result = stockOrderProcessService.save(stockOrderProcessDTO);
        return ResponseEntity.created(new URI("/api/stock-order-processes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-order-processes : Updates an existing stockOrderProcess.
     *
     * @param stockOrderProcessDTO the stockOrderProcessDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOrderProcessDTO,
     * or with status 400 (Bad Request) if the stockOrderProcessDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOrderProcessDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-order-processes")
    public ResponseEntity<StockOrderProcessDTO> updateStockOrderProcess(@RequestBody StockOrderProcessDTO stockOrderProcessDTO) throws URISyntaxException {
        log.debug("REST request to update StockOrderProcess : {}", stockOrderProcessDTO);
        if (stockOrderProcessDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockOrderProcessDTO result = stockOrderProcessService.save(stockOrderProcessDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOrderProcessDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-order-processes : get all the stockOrderProcesses.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of stockOrderProcesses in body
     */
    @GetMapping("/stock-order-processes")
    public ResponseEntity<List<StockOrderProcessDTO>> getAllStockOrderProcesses(StockOrderProcessCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StockOrderProcesses by criteria: {}", criteria);
        Page<StockOrderProcessDTO> page = stockOrderProcessQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-order-processes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /stock-order-processes/count : count all the stockOrderProcesses.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/stock-order-processes/count")
    public ResponseEntity<Long> countStockOrderProcesses(StockOrderProcessCriteria criteria) {
        log.debug("REST request to count StockOrderProcesses by criteria: {}", criteria);
        return ResponseEntity.ok().body(stockOrderProcessQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /stock-order-processes/:id : get the "id" stockOrderProcess.
     *
     * @param id the id of the stockOrderProcessDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOrderProcessDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-order-processes/{id}")
    public ResponseEntity<StockOrderProcessDTO> getStockOrderProcess(@PathVariable Long id) {
        log.debug("REST request to get StockOrderProcess : {}", id);
        Optional<StockOrderProcessDTO> stockOrderProcessDTO = stockOrderProcessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockOrderProcessDTO);
    }

    /**
     * DELETE  /stock-order-processes/:id : delete the "id" stockOrderProcess.
     *
     * @param id the id of the stockOrderProcessDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-order-processes/{id}")
    public ResponseEntity<Void> deleteStockOrderProcess(@PathVariable Long id) {
        log.debug("REST request to delete StockOrderProcess : {}", id);
        stockOrderProcessService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stock-order-processes?query=:query : search for the stockOrderProcess corresponding
     * to the query.
     *
     * @param query    the query of the stockOrderProcess search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stock-order-processes")
    public ResponseEntity<List<StockOrderProcessDTO>> searchStockOrderProcesses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StockOrderProcesses for query {}", query);
        Page<StockOrderProcessDTO> page = stockOrderProcessService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stock-order-processes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * POST  /stock-order-processes : Create a new stockOrderProcess.
     *
     * @param stockOrderProcessDTOS the stockOrderProcessDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOrderProcessDTO, or with status 400 (Bad Request) if the stockOrderProcess has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-order-processes/orders")
    public ResponseEntity<List<StockOrderProcessDTO>> createStockOrderProcesses(@RequestBody List<StockOrderProcessDTO> stockOrderProcessDTOS) throws URISyntaxException {
        log.debug("REST request to save StockOrderProcesses : {}", stockOrderProcessDTOS);
        if (stockOrderProcessDTOS.size() <= 0) {
            throw new BadRequestAlertException("A new stockOrderProcess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        initValueIfEmpty(stockOrderProcessDTOS);
        stockOrderProcessDTOS = stockOrderProcessService.saveAll(stockOrderProcessDTOS);

        return ResponseEntity.ok().body(stockOrderProcessDTOS);
    }

    private void initValueIfEmpty(List<StockOrderProcessDTO> stockOrderProcessDTOS) {
        stockOrderProcessDTOS.forEach(it -> {
            if(it.getCreatedDate() == null)
                it.setCreatedDate(Instant.now());
        });
    }

}
