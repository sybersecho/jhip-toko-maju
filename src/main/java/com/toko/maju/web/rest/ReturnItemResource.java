package com.toko.maju.web.rest;
import com.toko.maju.service.ReturnItemService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.ReturnItemDTO;
import com.toko.maju.service.dto.ReturnItemCriteria;
import com.toko.maju.service.ReturnItemQueryService;
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
 * REST controller for managing ReturnItem.
 */
@RestController
@RequestMapping("/api")
public class ReturnItemResource {

    private final Logger log = LoggerFactory.getLogger(ReturnItemResource.class);

    private static final String ENTITY_NAME = "returnItem";

    private final ReturnItemService returnItemService;

    private final ReturnItemQueryService returnItemQueryService;

    public ReturnItemResource(ReturnItemService returnItemService, ReturnItemQueryService returnItemQueryService) {
        this.returnItemService = returnItemService;
        this.returnItemQueryService = returnItemQueryService;
    }

    /**
     * POST  /return-items : Create a new returnItem.
     *
     * @param returnItemDTO the returnItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new returnItemDTO, or with status 400 (Bad Request) if the returnItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/return-items")
    public ResponseEntity<ReturnItemDTO> createReturnItem(@Valid @RequestBody ReturnItemDTO returnItemDTO) throws URISyntaxException {
        log.debug("REST request to save ReturnItem : {}", returnItemDTO);
        if (returnItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new returnItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReturnItemDTO result = returnItemService.save(returnItemDTO);
        return ResponseEntity.created(new URI("/api/return-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /return-items : Updates an existing returnItem.
     *
     * @param returnItemDTO the returnItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated returnItemDTO,
     * or with status 400 (Bad Request) if the returnItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the returnItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/return-items")
    public ResponseEntity<ReturnItemDTO> updateReturnItem(@Valid @RequestBody ReturnItemDTO returnItemDTO) throws URISyntaxException {
        log.debug("REST request to update ReturnItem : {}", returnItemDTO);
        if (returnItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReturnItemDTO result = returnItemService.save(returnItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, returnItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /return-items : get all the returnItems.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of returnItems in body
     */
    @GetMapping("/return-items")
    public ResponseEntity<List<ReturnItemDTO>> getAllReturnItems(ReturnItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReturnItems by criteria: {}", criteria);
        Page<ReturnItemDTO> page = returnItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/return-items");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /return-items/count : count all the returnItems.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/return-items/count")
    public ResponseEntity<Long> countReturnItems(ReturnItemCriteria criteria) {
        log.debug("REST request to count ReturnItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(returnItemQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /return-items/:id : get the "id" returnItem.
     *
     * @param id the id of the returnItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the returnItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/return-items/{id}")
    public ResponseEntity<ReturnItemDTO> getReturnItem(@PathVariable Long id) {
        log.debug("REST request to get ReturnItem : {}", id);
        Optional<ReturnItemDTO> returnItemDTO = returnItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(returnItemDTO);
    }

    /**
     * DELETE  /return-items/:id : delete the "id" returnItem.
     *
     * @param id the id of the returnItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/return-items/{id}")
    public ResponseEntity<Void> deleteReturnItem(@PathVariable Long id) {
        log.debug("REST request to delete ReturnItem : {}", id);
        returnItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/return-items?query=:query : search for the returnItem corresponding
     * to the query.
     *
     * @param query the query of the returnItem search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/return-items")
    public ResponseEntity<List<ReturnItemDTO>> searchReturnItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReturnItems for query {}", query);
        Page<ReturnItemDTO> page = returnItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/return-items");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
