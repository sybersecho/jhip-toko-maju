package com.toko.maju.web.rest;
import com.toko.maju.service.GeraiService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.web.rest.util.PaginationUtil;
import com.toko.maju.service.dto.GeraiDTO;
import com.toko.maju.service.dto.GeraiCriteria;
import com.toko.maju.service.GeraiQueryService;
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
 * REST controller for managing Gerai.
 */
@RestController
@RequestMapping("/api")
public class GeraiResource {

    private final Logger log = LoggerFactory.getLogger(GeraiResource.class);

    private static final String ENTITY_NAME = "gerai";

    private final GeraiService geraiService;

    private final GeraiQueryService geraiQueryService;

    public GeraiResource(GeraiService geraiService, GeraiQueryService geraiQueryService) {
        this.geraiService = geraiService;
        this.geraiQueryService = geraiQueryService;
    }

    /**
     * POST  /gerais : Create a new gerai.
     *
     * @param geraiDTO the geraiDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new geraiDTO, or with status 400 (Bad Request) if the gerai has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gerais")
    public ResponseEntity<GeraiDTO> createGerai(@Valid @RequestBody GeraiDTO geraiDTO) throws URISyntaxException {
        log.debug("REST request to save Gerai : {}", geraiDTO);
        if (geraiDTO.getId() != null) {
            throw new BadRequestAlertException("A new gerai cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeraiDTO result = geraiService.save(geraiDTO);
        return ResponseEntity.created(new URI("/api/gerais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gerais : Updates an existing gerai.
     *
     * @param geraiDTO the geraiDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated geraiDTO,
     * or with status 400 (Bad Request) if the geraiDTO is not valid,
     * or with status 500 (Internal Server Error) if the geraiDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gerais")
    public ResponseEntity<GeraiDTO> updateGerai(@Valid @RequestBody GeraiDTO geraiDTO) throws URISyntaxException {
        log.debug("REST request to update Gerai : {}", geraiDTO);
        if (geraiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GeraiDTO result = geraiService.save(geraiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, geraiDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gerais : get all the gerais.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of gerais in body
     */
    @GetMapping("/gerais")
    public ResponseEntity<List<GeraiDTO>> getAllGerais(GeraiCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Gerais by criteria: {}", criteria);
        Page<GeraiDTO> page = geraiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gerais");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /gerais/count : count all the gerais.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/gerais/count")
    public ResponseEntity<Long> countGerais(GeraiCriteria criteria) {
        log.debug("REST request to count Gerais by criteria: {}", criteria);
        return ResponseEntity.ok().body(geraiQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /gerais/:id : get the "id" gerai.
     *
     * @param id the id of the geraiDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the geraiDTO, or with status 404 (Not Found)
     */
    @GetMapping("/gerais/{id}")
    public ResponseEntity<GeraiDTO> getGerai(@PathVariable Long id) {
        log.debug("REST request to get Gerai : {}", id);
        Optional<GeraiDTO> geraiDTO = geraiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geraiDTO);
    }

    /**
     * DELETE  /gerais/:id : delete the "id" gerai.
     *
     * @param id the id of the geraiDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gerais/{id}")
    public ResponseEntity<Void> deleteGerai(@PathVariable Long id) {
        log.debug("REST request to delete Gerai : {}", id);
        geraiService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/gerais?query=:query : search for the gerai corresponding
     * to the query.
     *
     * @param query the query of the gerai search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/gerais")
    public ResponseEntity<List<GeraiDTO>> searchGerais(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Gerais for query {}", query);
        Page<GeraiDTO> page = geraiService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/gerais");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
