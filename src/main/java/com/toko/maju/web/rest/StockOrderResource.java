package com.toko.maju.web.rest;
import com.toko.maju.domain.StockOrderRequest;
import com.toko.maju.service.StockOrderRequestQueryService;
import com.toko.maju.service.StockOrderRequestService;
import com.toko.maju.service.StockOrderService;
import com.toko.maju.service.dto.StockOrderRequestDTO;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.StockOrderDTO;
import com.toko.maju.service.dto.StockOrderCriteria;
import com.toko.maju.service.StockOrderQueryService;
import com.toko.maju.web.rest.vm.StockOrderVM;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing StockOrder.
 */
@RestController
@RequestMapping("/api")
public class StockOrderResource {

    private final Logger log = LoggerFactory.getLogger(StockOrderResource.class);

    private static final String ENTITY_NAME = "stockOrder";

    private final StockOrderService stockOrderService;

    private final StockOrderQueryService stockOrderQueryService;

    @Autowired
    private final StockOrderRequestService stockOrderRequestService = null;

//    @Autowired
//    private final Stockorderre

    public StockOrderResource(StockOrderService stockOrderService, StockOrderQueryService stockOrderQueryService) {
        this.stockOrderService = stockOrderService;
        this.stockOrderQueryService = stockOrderQueryService;
    }

    /**
     * POST  /stock-orders : Create a new stockOrder.
     *
     * @param stockOrderVM the stockOrderVM to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOrderVM, or with status 400 (Bad Request) if the stockOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-orders")
    public ResponseEntity<StockOrderDTO> createStockOrder(@Valid @RequestBody StockOrderVM stockOrderVM) throws URISyntaxException {
        log.debug("REST request to save StockOrder : {}", stockOrderVM);
        if (stockOrderVM.getId() != null) {
            throw new BadRequestAlertException("A new stockOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockOrderDTO result = stockOrderService.save(stockOrderVM);
        stockOrderVM.setCurrentDate();
        stockOrderVM.setOrder(result);
        stockOrderRequestService.saveAll(stockOrderVM.getStockOrderRequests());
        return ResponseEntity.created(new URI("/api/stock-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-orders : Updates an existing stockOrder.
     *
     * @param stockOrderDTO the stockOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOrderDTO,
     * or with status 400 (Bad Request) if the stockOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-orders")
    public ResponseEntity<StockOrderDTO> updateStockOrder(@Valid @RequestBody StockOrderDTO stockOrderDTO) throws URISyntaxException {
        log.debug("REST request to update StockOrder : {}", stockOrderDTO);
        if (stockOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockOrderDTO result = stockOrderService.save(stockOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-orders : get all the stockOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of stockOrders in body
     */
    @GetMapping("/stock-orders")
    public ResponseEntity<List<StockOrderDTO>> getAllStockOrders(StockOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StockOrders by criteria: {}", criteria);
        Page<StockOrderDTO> page = stockOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-orders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /stock-orders/count : count all the stockOrders.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/stock-orders/count")
    public ResponseEntity<Long> countStockOrders(StockOrderCriteria criteria) {
        log.debug("REST request to count StockOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(stockOrderQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /stock-orders/:id : get the "id" stockOrder.
     *
     * @param id the id of the stockOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-orders/{id}")
    public ResponseEntity<StockOrderDTO> getStockOrder(@PathVariable Long id) {
        log.debug("REST request to get StockOrder : {}", id);
        Optional<StockOrderDTO> stockOrderDTO = stockOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockOrderDTO);
    }

    /**
     * DELETE  /stock-orders/:id : delete the "id" stockOrder.
     *
     * @param id the id of the stockOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-orders/{id}")
    public ResponseEntity<Void> deleteStockOrder(@PathVariable Long id) {
        log.debug("REST request to delete StockOrder : {}", id);
        stockOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stock-orders?query=:query : search for the stockOrder corresponding
     * to the query.
     *
     * @param query the query of the stockOrder search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stock-orders")
    public ResponseEntity<List<StockOrderDTO>> searchStockOrders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StockOrders for query {}", query);
        Page<StockOrderDTO> page = stockOrderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stock-orders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
