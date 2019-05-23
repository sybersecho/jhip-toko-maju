package com.toko.maju.web.rest;
import com.toko.maju.service.GeraiUpdateHistoryService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.GeraiUpdateHistoryDTO;
import com.toko.maju.service.dto.GeraiUpdateHistoryCriteria;
import com.toko.maju.service.GeraiUpdateHistoryQueryService;
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
 * REST controller for managing GeraiUpdateHistory.
 */
@RestController
@RequestMapping("/api")
public class GeraiUpdateHistoryResource {

    private final Logger log = LoggerFactory.getLogger(GeraiUpdateHistoryResource.class);

    private static final String ENTITY_NAME = "geraiUpdateHistory";

    private final GeraiUpdateHistoryService geraiUpdateHistoryService;

    private final GeraiUpdateHistoryQueryService geraiUpdateHistoryQueryService;

    public GeraiUpdateHistoryResource(GeraiUpdateHistoryService geraiUpdateHistoryService, GeraiUpdateHistoryQueryService geraiUpdateHistoryQueryService) {
        this.geraiUpdateHistoryService = geraiUpdateHistoryService;
        this.geraiUpdateHistoryQueryService = geraiUpdateHistoryQueryService;
    }

    /**
     * POST  /gerai-update-histories : Create a new geraiUpdateHistory.
     *
     * @param geraiUpdateHistoryDTO the geraiUpdateHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new geraiUpdateHistoryDTO, or with status 400 (Bad Request) if the geraiUpdateHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gerai-update-histories")
    public ResponseEntity<GeraiUpdateHistoryDTO> createGeraiUpdateHistory(@Valid @RequestBody GeraiUpdateHistoryDTO geraiUpdateHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save GeraiUpdateHistory : {}", geraiUpdateHistoryDTO);
        if (geraiUpdateHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new geraiUpdateHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeraiUpdateHistoryDTO result = geraiUpdateHistoryService.save(geraiUpdateHistoryDTO);
        return ResponseEntity.created(new URI("/api/gerai-update-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gerai-update-histories : Updates an existing geraiUpdateHistory.
     *
     * @param geraiUpdateHistoryDTO the geraiUpdateHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated geraiUpdateHistoryDTO,
     * or with status 400 (Bad Request) if the geraiUpdateHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the geraiUpdateHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gerai-update-histories")
    public ResponseEntity<GeraiUpdateHistoryDTO> updateGeraiUpdateHistory(@Valid @RequestBody GeraiUpdateHistoryDTO geraiUpdateHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update GeraiUpdateHistory : {}", geraiUpdateHistoryDTO);
        if (geraiUpdateHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GeraiUpdateHistoryDTO result = geraiUpdateHistoryService.save(geraiUpdateHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, geraiUpdateHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gerai-update-histories : get all the geraiUpdateHistories.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of geraiUpdateHistories in body
     */
    @GetMapping("/gerai-update-histories")
    public ResponseEntity<List<GeraiUpdateHistoryDTO>> getAllGeraiUpdateHistories(GeraiUpdateHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GeraiUpdateHistories by criteria: {}", criteria);
        Page<GeraiUpdateHistoryDTO> page = geraiUpdateHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gerai-update-histories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /gerai-update-histories/count : count all the geraiUpdateHistories.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/gerai-update-histories/count")
    public ResponseEntity<Long> countGeraiUpdateHistories(GeraiUpdateHistoryCriteria criteria) {
        log.debug("REST request to count GeraiUpdateHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(geraiUpdateHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /gerai-update-histories/:id : get the "id" geraiUpdateHistory.
     *
     * @param id the id of the geraiUpdateHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the geraiUpdateHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/gerai-update-histories/{id}")
    public ResponseEntity<GeraiUpdateHistoryDTO> getGeraiUpdateHistory(@PathVariable Long id) {
        log.debug("REST request to get GeraiUpdateHistory : {}", id);
        Optional<GeraiUpdateHistoryDTO> geraiUpdateHistoryDTO = geraiUpdateHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geraiUpdateHistoryDTO);
    }

    /**
     * DELETE  /gerai-update-histories/:id : delete the "id" geraiUpdateHistory.
     *
     * @param id the id of the geraiUpdateHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gerai-update-histories/{id}")
    public ResponseEntity<Void> deleteGeraiUpdateHistory(@PathVariable Long id) {
        log.debug("REST request to delete GeraiUpdateHistory : {}", id);
        geraiUpdateHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/gerai-update-histories?query=:query : search for the geraiUpdateHistory corresponding
     * to the query.
     *
     * @param query the query of the geraiUpdateHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/gerai-update-histories")
    public ResponseEntity<List<GeraiUpdateHistoryDTO>> searchGeraiUpdateHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of GeraiUpdateHistories for query {}", query);
        Page<GeraiUpdateHistoryDTO> page = geraiUpdateHistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/gerai-update-histories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
