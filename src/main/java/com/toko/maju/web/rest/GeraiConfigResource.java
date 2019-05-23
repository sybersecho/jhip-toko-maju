package com.toko.maju.web.rest;
import com.toko.maju.service.GeraiConfigService;
import com.toko.maju.web.rest.errors.BadRequestAlertException;
import com.toko.maju.web.rest.util.HeaderUtil;
import com.toko.maju.service.dto.GeraiConfigDTO;
import com.toko.maju.service.dto.GeraiConfigCriteria;
import com.toko.maju.service.GeraiConfigQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing GeraiConfig.
 */
@RestController
@RequestMapping("/api")
public class GeraiConfigResource {

    private final Logger log = LoggerFactory.getLogger(GeraiConfigResource.class);

    private static final String ENTITY_NAME = "geraiConfig";

    private final GeraiConfigService geraiConfigService;

    private final GeraiConfigQueryService geraiConfigQueryService;

    public GeraiConfigResource(GeraiConfigService geraiConfigService, GeraiConfigQueryService geraiConfigQueryService) {
        this.geraiConfigService = geraiConfigService;
        this.geraiConfigQueryService = geraiConfigQueryService;
    }

    /**
     * POST  /gerai-configs : Create a new geraiConfig.
     *
     * @param geraiConfigDTO the geraiConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new geraiConfigDTO, or with status 400 (Bad Request) if the geraiConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gerai-configs")
    public ResponseEntity<GeraiConfigDTO> createGeraiConfig(@Valid @RequestBody GeraiConfigDTO geraiConfigDTO) throws URISyntaxException {
        log.debug("REST request to save GeraiConfig : {}", geraiConfigDTO);
        if (geraiConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new geraiConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeraiConfigDTO result = geraiConfigService.save(geraiConfigDTO);
        return ResponseEntity.created(new URI("/api/gerai-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gerai-configs : Updates an existing geraiConfig.
     *
     * @param geraiConfigDTO the geraiConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated geraiConfigDTO,
     * or with status 400 (Bad Request) if the geraiConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the geraiConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gerai-configs")
    public ResponseEntity<GeraiConfigDTO> updateGeraiConfig(@Valid @RequestBody GeraiConfigDTO geraiConfigDTO) throws URISyntaxException {
        log.debug("REST request to update GeraiConfig : {}", geraiConfigDTO);
        if (geraiConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GeraiConfigDTO result = geraiConfigService.save(geraiConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, geraiConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gerai-configs : get all the geraiConfigs.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of geraiConfigs in body
     */
    @GetMapping("/gerai-configs")
    public ResponseEntity<List<GeraiConfigDTO>> getAllGeraiConfigs(GeraiConfigCriteria criteria) {
        log.debug("REST request to get GeraiConfigs by criteria: {}", criteria);
        List<GeraiConfigDTO> entityList = geraiConfigQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /gerai-configs/count : count all the geraiConfigs.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/gerai-configs/count")
    public ResponseEntity<Long> countGeraiConfigs(GeraiConfigCriteria criteria) {
        log.debug("REST request to count GeraiConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(geraiConfigQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /gerai-configs/:id : get the "id" geraiConfig.
     *
     * @param id the id of the geraiConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the geraiConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/gerai-configs/{id}")
    public ResponseEntity<GeraiConfigDTO> getGeraiConfig(@PathVariable Long id) {
        log.debug("REST request to get GeraiConfig : {}", id);
        Optional<GeraiConfigDTO> geraiConfigDTO = geraiConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geraiConfigDTO);
    }

    /**
     * DELETE  /gerai-configs/:id : delete the "id" geraiConfig.
     *
     * @param id the id of the geraiConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gerai-configs/{id}")
    public ResponseEntity<Void> deleteGeraiConfig(@PathVariable Long id) {
        log.debug("REST request to delete GeraiConfig : {}", id);
        geraiConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/gerai-configs?query=:query : search for the geraiConfig corresponding
     * to the query.
     *
     * @param query the query of the geraiConfig search
     * @return the result of the search
     */
    @GetMapping("/_search/gerai-configs")
    public List<GeraiConfigDTO> searchGeraiConfigs(@RequestParam String query) {
        log.debug("REST request to search GeraiConfigs for query {}", query);
        return geraiConfigService.search(query);
    }

}
