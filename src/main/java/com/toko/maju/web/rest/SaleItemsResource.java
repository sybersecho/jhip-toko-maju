package com.toko.maju.web.rest;
import com.toko.maju.domain.SaleItems;
import com.toko.maju.repository.SaleItemsRepository;
import com.toko.maju.repository.search.SaleItemsSearchRepository;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SaleItems.
 */
@RestController
@RequestMapping("/api")
public class SaleItemsResource {

    private final Logger log = LoggerFactory.getLogger(SaleItemsResource.class);

    private static final String ENTITY_NAME = "saleItems";

    private final SaleItemsRepository saleItemsRepository;

    private final SaleItemsSearchRepository saleItemsSearchRepository;

    public SaleItemsResource(SaleItemsRepository saleItemsRepository, SaleItemsSearchRepository saleItemsSearchRepository) {
        this.saleItemsRepository = saleItemsRepository;
        this.saleItemsSearchRepository = saleItemsSearchRepository;
    }

    /**
     * POST  /sale-items : Create a new saleItems.
     *
     * @param saleItems the saleItems to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saleItems, or with status 400 (Bad Request) if the saleItems has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-items")
    public ResponseEntity<SaleItems> createSaleItems(@Valid @RequestBody SaleItems saleItems) throws URISyntaxException {
        log.debug("REST request to save SaleItems : {}", saleItems);
        if (saleItems.getId() != null) {
            throw new BadRequestAlertException("A new saleItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaleItems result = saleItemsRepository.save(saleItems);
        saleItemsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sale-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-items : Updates an existing saleItems.
     *
     * @param saleItems the saleItems to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saleItems,
     * or with status 400 (Bad Request) if the saleItems is not valid,
     * or with status 500 (Internal Server Error) if the saleItems couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-items")
    public ResponseEntity<SaleItems> updateSaleItems(@Valid @RequestBody SaleItems saleItems) throws URISyntaxException {
        log.debug("REST request to update SaleItems : {}", saleItems);
        if (saleItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SaleItems result = saleItemsRepository.save(saleItems);
        saleItemsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saleItems.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sale-items : get all the saleItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of saleItems in body
     */
    @GetMapping("/sale-items")
    public ResponseEntity<List<SaleItems>> getAllSaleItems(Pageable pageable) {
        log.debug("REST request to get a page of SaleItems");
        Page<SaleItems> page = saleItemsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sale-items");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /sale-items/:id : get the "id" saleItems.
     *
     * @param id the id of the saleItems to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saleItems, or with status 404 (Not Found)
     */
    @GetMapping("/sale-items/{id}")
    public ResponseEntity<SaleItems> getSaleItems(@PathVariable Long id) {
        log.debug("REST request to get SaleItems : {}", id);
        Optional<SaleItems> saleItems = saleItemsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(saleItems);
    }

    /**
     * DELETE  /sale-items/:id : delete the "id" saleItems.
     *
     * @param id the id of the saleItems to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-items/{id}")
    public ResponseEntity<Void> deleteSaleItems(@PathVariable Long id) {
        log.debug("REST request to delete SaleItems : {}", id);
        saleItemsRepository.deleteById(id);
        saleItemsSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sale-items?query=:query : search for the saleItems corresponding
     * to the query.
     *
     * @param query the query of the saleItems search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sale-items")
    public ResponseEntity<List<SaleItems>> searchSaleItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SaleItems for query {}", query);
        Page<SaleItems> page = saleItemsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sale-items");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
